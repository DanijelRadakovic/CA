package megatravel.com.ca.generator.extension.holder;

import org.bouncycastle.asn1.x509.KeyPurposeId;

public class ExtendedKeyUsageHolder extends X509ExtensionHolder {

    private KeyPurposeId[] keyPurpose;

    public ExtendedKeyUsageHolder() {
        this.name = "Extended Key Usage";
        this.ASN1ObjectIdentifier = "2.5.29.37";
    }

    public ExtendedKeyUsageHolder(boolean critical, KeyPurposeId[] keyPurpose) {
        this.name = "Extended Key Usage";
        this.ASN1ObjectIdentifier = "2.5.29.37";
        this.critical = critical;
        this.keyPurpose = keyPurpose;
    }

    public KeyPurposeId[] getKeyPurpose() {
        return keyPurpose;
    }

    public void setKeyPurpose(KeyPurposeId[] keyPurpose) {
        this.keyPurpose = keyPurpose;
    }
}
