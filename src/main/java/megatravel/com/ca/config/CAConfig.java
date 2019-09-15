package megatravel.com.ca.config;

import megatravel.com.ca.domain.enums.PeriodUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CAConfig {
    @Value("${ca.keystore}")
    private String keystore;

    @Value("${ca.keystore.password}")
    private String keystorePassword;

    @Value("${ca.gen.certificate.provider}")
    private String provider;

    @Value("${ca.gen.algorithm.signature}")
    private String signatureAlgorithm;

    @Value("${ca.gen.algorithm.key}")
    private String keyAlgorithm;

    @Value("${ca.gen.seed.algorithm}")
    private String seedAlgorithm;

    @Value("${ca.gen.seed.provider}")
    private String seedProvider;

    @Value("${ca.gen.serialnumbersize}")
    private String serialNumberSize;

    @Value("${ca.gen.period.unit}")
    private String periodUnit;

    @Value("${ca.gen.endentity.keysize}")
    private String endEntityKeySize;

    @Value("${ca.gen.endentity.period}")
    private String endEntityPeriod;

    @Value("${ca.gen.ca.keysize}")
    private String caKeySize;

    @Value("${ca.gen.ca.period}")
    private String caPeriod;

    @Value("${ca.sftp.username}")
    private String sftpUsername;

    @Value("${ca.sftp.repo.location}")
    private String repositoryLocation;

    @Value("${ca.sftp.repo.hostname}")
    private String repositoryHostname;

    public String getKeystore() {
        return keystore;
    }

    public void setKeystore(String keystore) {
        this.keystore = keystore;
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

    public int getEndEntityKeySize() {
        return Integer.parseInt(endEntityKeySize);
    }

    public void setEndEntityKeySize(int endEntityKeySize) {
        this.endEntityKeySize = endEntityKeySize + "";
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
