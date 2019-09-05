package megatravel.com.pki.domain.DTO.cer.ext;

public class KeyUsageDTO {
    private boolean critical;
    private boolean digitalSignature;
    private boolean nonRepudiation;
    private boolean keyEncipherment;
    private boolean dataEncipherment;
    private boolean keyAgreement;
    private boolean keyCertSign;
    private boolean cRLSign;
    private boolean encipherOnly;
    private boolean decipherOnly;

    public KeyUsageDTO() {
    }

    public KeyUsageDTO(boolean critical,boolean digitalSignature, boolean nonRepudiation, boolean keyEncipherment,
                       boolean dataEncipherment, boolean keyAgreement, boolean keyCertSign, boolean cRLSign, boolean encipherOnly, boolean decipherOnly) {
        this.critical = critical;
        this.digitalSignature = digitalSignature;
        this.nonRepudiation = nonRepudiation;
        this.keyEncipherment = keyEncipherment;
        this.dataEncipherment = dataEncipherment;
        this.keyAgreement = keyAgreement;
        this.keyCertSign = keyCertSign;
        this.cRLSign = cRLSign;
        this.encipherOnly = encipherOnly;
        this.decipherOnly = decipherOnly;
    }

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    public boolean isDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(boolean digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    public boolean isNonRepudiation() {
        return nonRepudiation;
    }

    public void setNonRepudiation(boolean nonRepudiation) {
        this.nonRepudiation = nonRepudiation;
    }

    public boolean isKeyEncipherment() {
        return keyEncipherment;
    }

    public void setKeyEncipherment(boolean keyEncipherment) {
        this.keyEncipherment = keyEncipherment;
    }

    public boolean isDataEncipherment() {
        return dataEncipherment;
    }

    public void setDataEncipherment(boolean dataEncipherment) {
        this.dataEncipherment = dataEncipherment;
    }

    public boolean isKeyAgreement() {
        return keyAgreement;
    }

    public void setKeyAgreement(boolean keyAgreement) {
        this.keyAgreement = keyAgreement;
    }

    public boolean isKeyCertSign() {
        return keyCertSign;
    }

    public void setKeyCertSign(boolean keyCertSign) {
        this.keyCertSign = keyCertSign;
    }

    public boolean iscRLSign() {
        return cRLSign;
    }

    public void setcRLSign(boolean cRLSign) {
        this.cRLSign = cRLSign;
    }

    public boolean isEncipherOnly() {
        return encipherOnly;
    }

    public void setEncipherOnly(boolean encipherOnly) {
        this.encipherOnly = encipherOnly;
    }

    public boolean isDecipherOnly() {
        return decipherOnly;
    }

    public void setDecipherOnly(boolean decipherOnly) {
        this.decipherOnly = decipherOnly;
    }
}
