package megatravel.com.pki.domain.enums;

import org.bouncycastle.asn1.x509.KeyPurposeId;

public enum KeyPurposeType {

    SERVER_AUTH(KeyPurposeId.id_kp_serverAuth),
    CLIENT_AUTH(KeyPurposeId.id_kp_clientAuth),
    CODE_SIGNING(KeyPurposeId.id_kp_codeSigning),
    EMAIL_PROTECTION(KeyPurposeId.id_kp_emailProtection),
    TIME_STAMPING(KeyPurposeId.id_kp_timeStamping),
    OCSP_SIGNING(KeyPurposeId.id_kp_OCSPSigning);

    private final KeyPurposeId purpose;

    KeyPurposeType(KeyPurposeId purpose) {
        this.purpose = purpose;
    }

    public KeyPurposeId getPurpose() {
        return purpose;
    }
}
