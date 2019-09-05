package megatravel.com.pki.config;

import megatravel.com.pki.domain.enums.PeriodUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${pki.keystore}")
    private String keystore;

    @Value("${pki.keystore.password}")
    private String keystorePassword;

    @Value("${pki.truststore.dir}")
    private String truststore;

    @Value("${pki.truststore.password}")
    private String truststorePassword;

    @Value("${pki.certificates.dir}")
    private String certificates;

    @Value("${pki.certificate.provider}")
    private String provider;

    @Value("${pki.algorithm.signature}")
    private String signatureAlgorithm;

    @Value("${pki.algorithm.key}")
    private String keyAlgorithm;

    @Value("${pki.seed.algorithm}")
    private String seedAlgorithm;

    @Value("${pki.seed.provider}")
    private String seedProvider;

    @Value("${pki.serialnumbersize}")
    private String serialNumberSize;

    @Value("${pki.period.unit}")
    private String periodUnit;

    @Value("${pki.endentity.keysize}")
    private String endEntityKeysize;

    @Value("${pki.endentity.period}")
    private String endEntityPeriod;

    @Value("${pki.ca.keysize}")
    private String caKeySize;

    @Value("${pki.ca.period}")
    private String caPeriod;

    @Value("${pki.sftp.username}")
    private String sftpUsername;

    @Value("${pki.sftp.repo.location}")
    private String repositoryLocation;

    @Value("${pki.sftp.repo.hostname}")
    private String repositoryHostname;

    public String getKeystore() {
        return keystore;
    }

    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    public String getTruststore() {
        return truststore;
    }

    public void setTruststore(String truststore) {
        this.truststore = truststore;
    }

    public String getCertificates() {
        return certificates;
    }

    public void setCertificates(String certificates) {
        this.certificates = certificates;
    }

    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public String getKeyAlgorithm() {
        return keyAlgorithm;
    }

    public void setKeyAlgorithm(String keyAlgorithm) {
        this.keyAlgorithm = keyAlgorithm;
    }

    public String getSeedAlgorithm() {
        return seedAlgorithm;
    }

    public void setSeedAlgorithm(String seedAlgorithm) {
        this.seedAlgorithm = seedAlgorithm;
    }

    public String getSeedProvider() {
        return seedProvider;
    }

    public void setSeedProvider(String seedProvider) {
        this.seedProvider = seedProvider;
    }

    public PeriodUnit getPeriodUnit() {
        return PeriodUnit.factory(periodUnit);
    }

    public void setPeriodUnit(PeriodUnit periodUnit) {
        this.periodUnit = periodUnit.toString();
    }

    public int getEndEntityKeysize() {
        return Integer.parseInt(endEntityKeysize);
    }

    public void setEndEntityKeysize(int endEntityKeysize) {
        this.endEntityKeysize = endEntityKeysize + "";
    }

    public int getEndEntityPeriod() {
        return Integer.parseInt(endEntityPeriod);
    }

    public void setEndEntityPeriod(int endEntityPeriod) {
        this.endEntityPeriod = endEntityPeriod + "";
    }

    public int getCaKeySize() {
        return Integer.parseInt(caKeySize);
    }

    public void setCaKeySize(int caKeySize) {
        this.caKeySize = caKeySize + "";
    }

    public int getCaPeriod() {
        return Integer.parseInt(caPeriod);
    }

    public void setCaPeriod(int caPeriod) {
        this.caPeriod = caPeriod + "";
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public String getTruststorePassword() {
        return truststorePassword;
    }

    public void setTruststorePassword(String truststorePassword) {
        this.truststorePassword = truststorePassword;
    }

    public String getSftpUsername() {
        return sftpUsername;
    }

    public void setSftpUsername(String sftpUsername) {
        this.sftpUsername = sftpUsername;
    }

    public int getSerialNumberSize() {
        return Integer.parseInt(serialNumberSize);
    }

    public void setSerialNumberSize(String serialNumberSize) {
        this.serialNumberSize = serialNumberSize;
    }

    public String getRepositoryLocation() {
        return repositoryLocation;
    }

    public void setRepositoryLocation(String repositoryLocation) {
        this.repositoryLocation = repositoryLocation;
    }

    public String getRepositoryHostname() {
        return repositoryHostname;
    }

    public void setRepositoryHostname(String repositoryHostname) {
        this.repositoryHostname = repositoryHostname;
    }
}
