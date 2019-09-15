package megatravel.com.ca.domain.dto.cer.ext;

import org.bouncycastle.asn1.x509.PolicyQualifierId;

public enum PolicyQualifier {
    CPS(PolicyQualifierId.id_qt_cps),
    USER_NOTICE(PolicyQualifierId.id_qt_unotice);

    private final PolicyQualifierId qualifierId;

    PolicyQualifier(PolicyQualifierId qualifierId) {
        this.qualifierId = qualifierId;
    }

    public PolicyQualifierId getQualifierId() {
        return qualifierId;
    }
}
