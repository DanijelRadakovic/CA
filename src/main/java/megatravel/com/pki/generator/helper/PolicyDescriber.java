package megatravel.com.pki.generator.helper;

import org.bouncycastle.asn1.x509.PolicyQualifierId;

import java.util.List;

public class PolicyDescriber {

    private PolicyQualifierId qualifierId;
    private List<String> qualifiers;

    public PolicyDescriber() {
    }

    public PolicyDescriber(PolicyQualifierId qualifierId, List<String> qualifiers) {
        this.qualifierId = qualifierId;
        this.qualifiers = qualifiers;
    }

    public PolicyQualifierId getQualifierId() {
        return qualifierId;
    }

    public void setQualifierId(PolicyQualifierId qualifierId) {
        this.qualifierId = qualifierId;
    }

    public List<String> getQualifiers() {
        return qualifiers;
    }

    public void setQualifiers(List<String> qualifiers) {
        this.qualifiers = qualifiers;
    }
}
