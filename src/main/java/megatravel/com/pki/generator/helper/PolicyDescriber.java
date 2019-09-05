package megatravel.com.pki.generator.helper;

import java.util.List;

public class PolicyDescriber {

    private String policyId;
    private List<PolicyQualifierDescriber> qualifiers;


    public PolicyDescriber() {
    }

    public PolicyDescriber(String policyId, List<PolicyQualifierDescriber> qualifiers) {
        this.policyId = policyId;
        this.qualifiers = qualifiers;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public List<PolicyQualifierDescriber> getQualifiers() {
        return qualifiers;
    }

    public void setQualifiers(List<PolicyQualifierDescriber> qualifiers) {
        this.qualifiers = qualifiers;
    }
}
