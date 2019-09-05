package megatravel.com.pki.domain.DTO.cer.ext;

import org.bouncycastle.asn1.x509.PolicyQualifierId;

import java.util.List;
import java.util.Map;

public class CertificatePoliciesDTO {
    private boolean critical;
    private Map<PolicyQualifierId, List<String>> policies;

    public CertificatePoliciesDTO() {
    }

    public CertificatePoliciesDTO(boolean critical, Map<PolicyQualifierId, List<String>> policies) {
        this.critical = critical;
        this.policies = policies;
    }

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    public Map<PolicyQualifierId, List<String>> getPolicies() {
        return policies;
    }

    public void setPolicies(Map<PolicyQualifierId, List<String>> policies) {
        this.policies = policies;
    }
}
