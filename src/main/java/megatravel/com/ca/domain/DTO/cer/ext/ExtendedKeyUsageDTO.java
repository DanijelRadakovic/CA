package megatravel.com.ca.domain.DTO.cer.ext;

import megatravel.com.ca.domain.enums.KeyPurposeType;

import java.util.List;

public class ExtendedKeyUsageDTO {
    private boolean critical;
    private List<KeyPurposeType> keyPurpose;

    public ExtendedKeyUsageDTO() {
    }

    public ExtendedKeyUsageDTO(boolean critical, List<KeyPurposeType> keyPurpose) {
        this.critical = critical;
        this.keyPurpose = keyPurpose;
    }

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    public List<KeyPurposeType> getKeyPurpose() {
        return keyPurpose;
    }

    public void setKeyPurpose(List<KeyPurposeType> keyPurpose) {
        this.keyPurpose = keyPurpose;
    }
}
