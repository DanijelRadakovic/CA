package megatravel.com.pki.generator.helper;

import org.bouncycastle.asn1.x509.PolicyQualifierId;

public class PolicyQualifierDescriber {

    private PolicyQualifierId qualifierId;
    private String qualifier;

    public PolicyQualifierDescriber() {
    }

    public PolicyQualifierDescriber(PolicyQualifierId qualifierId, String qualifier) {
        this.qualifierId = qualifierId;
        this.qualifier = qualifier;
    }

    public PolicyQualifierId getQualifierId() {
        return qualifierId;
    }

    public void setQualifierId(PolicyQualifierId qualifierId) {
        this.qualifierId = qualifierId;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }
}
