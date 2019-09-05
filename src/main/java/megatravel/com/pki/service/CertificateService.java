package megatravel.com.pki.service;

import megatravel.com.pki.config.AppConfig;
import megatravel.com.pki.domain.cert.Certificate;
import megatravel.com.pki.domain.cert.CertificateDistribution;
import megatravel.com.pki.domain.enums.CerType;
import megatravel.com.pki.generator.CertificateGenerator;
import megatravel.com.pki.generator.extension.holder.BasicConstraintsHolder;
import megatravel.com.pki.generator.extension.holder.ExtensionHolders;
import megatravel.com.pki.generator.extension.holder.NameConstraintsHolder;
import megatravel.com.pki.generator.extension.holder.SubjectAlternativeNameHolder;
import megatravel.com.pki.generator.helper.CerPrivateKey;
import megatravel.com.pki.repository.CertificateRepository;
import megatravel.com.pki.repository.CertificateStorage;
import megatravel.com.pki.util.GeneralException;
import megatravel.com.pki.util.ValidationException;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CertificateService {

    @Autowired
    private AppConfig config;

    @Autowired
    private CertificateStorage storage;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private CertificateGenerator generator;

    public List<Certificate> findAll() {
        return validate(certificateRepository.findAll());
    }

    public List<Certificate> findAllCA() {
        return validate(certificateRepository.findAllCA());
    }

    public List<Certificate> findAllActive() {
        return validate(certificateRepository.findAllActive());
    }

    public List<Certificate> findAllActiveCA() {
        return validate(certificateRepository.findAllActiveCA());
    }

    public List<Certificate> findAllClients() {
        return validate(certificateRepository.findAllClients());
    }

    public List<Certificate> findAllActiveClients() {
        return validate(certificateRepository.findAllActiveClients());
    }

    public void remove(Long id) {
        Certificate cert = certificateRepository.findById(id).orElseThrow(() ->
                new GeneralException("Certificate with id: " + id + ".", HttpStatus.BAD_REQUEST));
        cert.setActive(false);
        certificateRepository.save(cert);
    }

    public void save(X500Name subjectDN, String issuerSN, ExtensionHolders holders) {
        try {
            validate(subjectDN, issuerSN, holders);
            CerPrivateKey cerPrivateKey = generator.generateCertificate(subjectDN,
                    storage.findCAbySerialNumber(issuerSN), holders);
            certificateRepository.save(new Certificate(null, cerPrivateKey.getCertificate().
                    getSerialNumber().toString(), cerPrivateKey.getCertificate().getSubjectDN().toString(),
                    getCerType(cerPrivateKey.getCertificate()), true, null));
            storage.store(new X509Certificate[]{cerPrivateKey.getCertificate()}, cerPrivateKey.getPrivateKey());
            storage.sendToCertificateRepository(cerPrivateKey.getCertificate().getSerialNumber().toString(),
                    config.getRepositoryHostname(), config.getRepositoryLocation());
        } catch (DataIntegrityViolationException e) {
            throw new GeneralException("Subject name or serial number is not unique.", HttpStatus.BAD_REQUEST);
        } catch (ValidationException e) {
            throw new GeneralException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void distribute(CertificateDistribution distribution) {
        storage.sendToCertificateRepository(distribution.getSerialNumber(), distribution.getHostname(),
                distribution.getDestination());
    }

    private List<Certificate> validate(List<Certificate> certs) {
        List<Certificate> result = new ArrayList<>(certs.size());
        List<Certificate> invalid = new ArrayList<>(certs.size());
        java.security.cert.Certificate[] chain;
        for (Certificate cert : certs) {
            chain = storage.getCertificateChain(cert.getSerialNumber(), false).getChain();
            try {
                ((X509Certificate) chain[0]).checkValidity();
                result.add(cert);
            } catch (CertificateExpiredException | CertificateNotYetValidException e) {
                cert.setActive(false);
                invalid.add(cert);
            }
        }
        certificateRepository.saveAll(invalid);
        return result;
    }

    private CerType getCerType(X509Certificate certificate) {
        byte[] extensionValue = certificate.getExtensionValue(Extension.basicConstraints.getId());
        ASN1OctetString bcOc = ASN1OctetString.getInstance(extensionValue);
        BasicConstraints bc = BasicConstraints.getInstance(bcOc.getOctets());
        if (bc.isCA()) {
            return CerType.CA;
        } else {
            return CerType.END_ENTITY;
        }
    }

    private void validate(X500Name subjectDN, String issuerSN, ExtensionHolders holders) throws
            ValidationException {
        BasicConstraintsHolder bCHolder = (BasicConstraintsHolder) holders.getHolder(BasicConstraintsHolder
                .class.getCanonicalName());
        NameConstraintsHolder nCHolder = (NameConstraintsHolder) holders.getHolder(NameConstraintsHolder
                .class.getCanonicalName());
        SubjectAlternativeNameHolder sANHolder = (SubjectAlternativeNameHolder) holders
                .getHolder(SubjectAlternativeNameHolder.class.getCanonicalName());

        if (subjectDN == null && issuerSN == null) {
            throw new ValidationException("Issuer and subject can not be empty.");
        } else if (bCHolder == null || !bCHolder.isCritical()) {
            throw new ValidationException("Basic Constraints must be present and marked as critical.");
        } else if (nCHolder != null && !bCHolder.iscA()) {
            throw new ValidationException("End entity certificate  must not have Name Constraints field.");
        } else if (subjectDN == null && (sANHolder == null || !sANHolder.isCritical())) {
            throw new ValidationException("When subject is omitted Subject Alternative Name must be present " +
                    "and marked as critical.");
        }
    }
}
