package megatravel.com.pki.generator;

import megatravel.com.pki.config.AppConfig;
import megatravel.com.pki.domain.enums.PeriodUnit;
import megatravel.com.pki.generator.extension.builder.ExtensionBuilderFactory;
import megatravel.com.pki.generator.extension.builder.X509ExtensionBuilder;
import megatravel.com.pki.generator.extension.holder.BasicConstraintsHolder;
import megatravel.com.pki.generator.extension.holder.ExtensionHolders;
import megatravel.com.pki.generator.helper.CerPrivateKey;
import megatravel.com.pki.generator.helper.IssuerData;
import megatravel.com.pki.generator.helper.SubjectData;
import megatravel.com.pki.util.ValidationException;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class CertificateGenerator {

    @Autowired
    private AppConfig config;

    private List<X509ExtensionBuilder> extensionBuilders;

    public CertificateGenerator() {
        this.extensionBuilders = ExtensionBuilderFactory.getAllExtensions();
    }

    public CertificateGenerator(AppConfig config) {
        this.config = config;
        this.extensionBuilders = ExtensionBuilderFactory.getAllExtensions();
    }

    public CerPrivateKey generateCertificate(X500Name subjectDN, IssuerData issuerData, ExtensionHolders holders)
            throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPair keyPair;
        SubjectData subject;

        extensionBuilders = ExtensionBuilderFactory.getAllExtensions();
        // filter unused extension builders
        filterExtensionBuilders(holders);

        try {
            if (issuerData == null) { // Self-signed certificate
                keyPair = generateKeyPair(true);
                subject = generateSubjectData(keyPair.getPublic(), subjectDN, true);
                issuerData = new IssuerData(keyPair.getPrivate(), subjectDN, subject.getPublicKey(),
                        subject.getSerialNumber());
                return new CerPrivateKey(buildCertificate(subject, issuerData, holders), keyPair.getPrivate());
            } else if (((BasicConstraintsHolder) holders.getHolders() // Intermediate certificate
                    .get(BasicConstraintsHolder.class.getCanonicalName())).iscA()) {
                keyPair = generateKeyPair(true);
                subject = generateSubjectData(keyPair.getPublic(), subjectDN, true);
                return new CerPrivateKey(buildCertificate(subject, issuerData, holders), keyPair.getPrivate());
            } else { // End-entity certificate
                keyPair = generateKeyPair(false);
                subject = generateSubjectData(keyPair.getPublic(), subjectDN, false);
                return new CerPrivateKey(buildCertificate(subject, issuerData, holders),
                        keyPair.getPrivate());
            }
        } catch (NullPointerException e) {
            throw new ValidationException("Invalid data received.");
        }

    }

    private KeyPair generateKeyPair(boolean isCA) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(config.getKeyAlgorithm());
        SecureRandom random = SecureRandom.getInstance(config.getSeedAlgorithm(), config.getSeedProvider());
        if (isCA) keyGen.initialize(config.getCaKeySize(), random);
        else keyGen.initialize(config.getEndEntityKeysize(), random);
        return keyGen.generateKeyPair();
    }

    private SubjectData generateSubjectData(PublicKey publicKey, X500Name subjectDN, boolean isCA)
            throws NoSuchProviderException, NoSuchAlgorithmException {
        long now = System.currentTimeMillis();
        Date startDate = new Date(now);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        Date endDate = getValidityPeriod(calendar, isCA);


        byte[] serialNumberBytes = new byte[config.getSerialNumberSize()];
        SecureRandom random = SecureRandom.getInstance(config.getSeedAlgorithm(), config.getSeedProvider());
        random.setSeed(SecureRandom.getSeed(config.getSerialNumberSize()));
        random.nextBytes(serialNumberBytes);

        return new SubjectData(publicKey, subjectDN, new BigInteger(serialNumberBytes).abs(), startDate, endDate);
    }

    private Date getValidityPeriod(Calendar now, boolean isCA) {
        if (config.getPeriodUnit() == PeriodUnit.DAY && isCA) {
            now.add(Calendar.DATE, config.getCaPeriod());
            return now.getTime();
        } else if (config.getPeriodUnit() == PeriodUnit.DAY && !isCA) {
            now.add(Calendar.DATE, config.getEndEntityPeriod());
            return now.getTime();
        } else if (config.getPeriodUnit() == PeriodUnit.MONTH && isCA) {
            now.add(Calendar.MONTH, config.getCaPeriod());
            return now.getTime();
        } else if (config.getPeriodUnit() == PeriodUnit.MONTH && !isCA) {
            now.add(Calendar.MONTH, config.getEndEntityPeriod());
            return now.getTime();
        } else if (config.getPeriodUnit() == PeriodUnit.YEAR && isCA) {
            now.add(Calendar.YEAR, config.getCaPeriod());
            return now.getTime();
        } else {
            now.add(Calendar.YEAR, config.getEndEntityPeriod());
            return now.getTime();
        }
    }

    private X509Certificate buildCertificate(SubjectData subjectData, IssuerData issuerData, ExtensionHolders holders) {
        try {
            ContentSigner contentSigner = new JcaContentSignerBuilder(
                    config.getSignatureAlgorithm())
                    .setProvider(config.getProvider())
                    .build(issuerData.getPrivateKey());

            X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(issuerData.getX500name(),
                    subjectData.getSerialNumber(),
                    subjectData.getStartDate(),
                    subjectData.getEndDate(),
                    subjectData.getX500name(),
                    subjectData.getPublicKey());


            for (X509ExtensionBuilder builder : extensionBuilders) {
                builder.build(certBuilder, holders.getHolders().get(builder.getMatchingHolder()));
            }

            JcaX509ExtensionUtils extensionUtils = new JcaX509ExtensionUtils();

            AuthorityKeyIdentifier authorityKeyIdentifier = extensionUtils
                    .createAuthorityKeyIdentifier(issuerData.getPublicKey());
            certBuilder.addExtension(Extension.authorityKeyIdentifier, false, authorityKeyIdentifier);

            SubjectKeyIdentifier subjectKeyIdentifier = extensionUtils
                    .createSubjectKeyIdentifier(subjectData.getPublicKey());
            certBuilder.addExtension(Extension.subjectKeyIdentifier, false, subjectKeyIdentifier);

            return new JcaX509CertificateConverter().setProvider(config.getProvider())
                    .getCertificate(certBuilder.build(contentSigner));
        } catch (IllegalArgumentException | IllegalStateException | OperatorCreationException |
                CertificateException | CertIOException | NoSuchAlgorithmException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    private void filterExtensionBuilders(ExtensionHolders holders) {
        List<X509ExtensionBuilder> unused = new ArrayList<>();
        extensionBuilders.forEach(builder -> {
            if (holders.getHolder(builder.getMatchingHolder()) == null) {
                unused.add(builder);
            }
        });
        extensionBuilders.removeIf(unused::contains);
    }

    public List<X509ExtensionBuilder> getExtensionBuilders() {
        return extensionBuilders;
    }

    public void setExtensionBuilders(List<X509ExtensionBuilder> extensionBuilders) {
        this.extensionBuilders = extensionBuilders;
    }


}
