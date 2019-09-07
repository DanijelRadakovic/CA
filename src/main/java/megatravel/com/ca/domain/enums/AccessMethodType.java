package megatravel.com.ca.domain.enums;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.AccessDescription;

public enum AccessMethodType {
    CA_ISSUERS(AccessDescription.id_ad_caIssuers),
    OCSP(AccessDescription.id_ad_ocsp);

    private final ASN1ObjectIdentifier type;

    AccessMethodType(ASN1ObjectIdentifier type) {
        this.type = type;
    }

    public ASN1ObjectIdentifier getType() {
        return type;
    }
}
