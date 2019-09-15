package megatravel.com.ca.domain.dto.cer.ext;

import java.util.List;

public class CertificatePolicyDTO {
    private String policyId;
    private List<PolicyQualifierDTO> qualifiers;

    public CertificatePolicyDTO() {
    }

    public CertificatePolicyDTO(String policyId, List<PolicyQualifierDTO> qualifiers) {
        this.policyId = policyId;
        this.qualifiers = qualifiers;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public List<PolicyQualifierDTO> getQualifiers() {
        return qualifiers;
    }

    public void setQualifiers(List<PolicyQualifierDTO> qualifiers) {
        this.qualifiers = qualifiers;
    }
}
