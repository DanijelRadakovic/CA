package megatravel.com.ca.service;

import megatravel.com.ca.config.AppConfig;
import megatravel.com.ca.domain.cert.Certificate;
import megatravel.com.ca.domain.cert.CertificateDistribution;
import megatravel.com.ca.domain.enums.CerType;
import megatravel.com.ca.domain.enums.RevokeReason;
import megatravel.com.ca.generator.CertificateGenerator;
import megatravel.com.ca.generator.extension.holder.BasicConstraintsHolder;
import megatravel.com.ca.generator.extension.holder.ExtensionHolders;
import megatravel.com.ca.generator.extension.holder.NameConstraintsHolder;
import megatravel.com.ca.generator.extension.holder.SubjectAlternativeNameHolder;
import megatravel.com.ca.generator.helper.CerPrivateKey;
import megatravel.com.ca.repository.CertificateRepository;
import megatravel.com.ca.repository.CertificateStorage;
import megatravel.com.ca.util.GeneralException;
import megatravel.com.ca.util.ValidationException;
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
    private CertificateRepository repository;

    @Autowired
    private CertificateStorage certificateStorage;

    @Autowired
    private CertificateGenerator generator;

    public List<Certificate> findAll() {
        return certificateStorage.findAll();
    }

    public List<Certificate> findAllCA() {
        return validate(certificateStorage.findAllCA());
    }

    public List<Certificate> findAllActive() {
        return validate(certificateStorage.findAllActive());
    }

    public List<Certificate> findAllActiveCA() {
        return validate(certificateStorage.findAllActiveCA());
    }

    public List<Certificate> findAllClients() {
        return validate(certificateStorage.findAllClients());
    }

    public List<Certificate> findAllActiveClients() {
        return validate(certificateStorage.findAllActiveClients());
    }

    public void remove(Long id, RevokeReason revokeReason) {
        Certificate cert = certificateStorage.findById(id).orElseThrow(() ->
                new GeneralException("Certificate with id: " + id + ".", HttpStatus.BAD_REQUEST));
        cert.setActive(false);
        cert.setRevokeReason(revokeReason);
        certificateStorage.save(cert);
        repository.removeCertificate(cert.getSerialNumber());
    }

    public void save(X500Name subjectDN, String issuerSN, ExtensionHolders holders) {
        try {
            validate(subjectDN, issuerSN, holders);
            CerPrivateKey cerPrivateKey = generator.generateCertificate(subjectDN,
                    repository.findCAbySerialNumber(issuerSN), holders);
            certificateStorage.save(new Certificate(null, cerPrivateKey.getCertificate().
                    getSerialNumber().toString(), cerPrivateKey.getCertificate().getSubjectDN().toString(),
                    getCerType(cerPrivateKey.getCertificate()), true, null));
            repository.store(new X509Certificate[]{cerPrivateKey.getCertificate()}, cerPrivateKey.getPrivateKey());
            repository.sendToCertificateRepository(cerPrivateKey.getCertificate().getSerialNumber().toString(),
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
        repository.sendToCertificateRepository(distribution.getSerialNumber(), distribution.getHostname(),
                distribution.getDestination());
    }

    private List<Certificate> validate(List<Certificate> certs) {
        List<Certificate> result = new ArrayList<>(certs.size());
        List<Certificate> invalid = new ArrayList<>(certs.size());
        java.security.cert.Certificate[] chain;
        for (Certificate cert : certs) {
            chain = repository.getCertificateChain(cert.getSerialNumber(), false).getChain();
            try {
                ((X509Certificate) chain[0]).checkValidity();
                result.add(cert);
            } catch (CertificateExpiredException | CertificateNotYetValidException e) {
                cert.setActive(false);
                invalid.add(cert);
            }
        }
        certificateStorage.saveAll(invalid);
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
