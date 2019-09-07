package megatravel.com.ca.generator.extension.holder;

import megatravel.com.ca.generator.helper.PolicyDescriber;

import java.util.List;

public class CertificatePoliciesHolder extends X509ExtensionHolder {

    private List<PolicyDescriber> policies;

    public CertificatePoliciesHolder() {
        this.name = "Certificate Policies";
        this.ASN1ObjectIdentifier = "2.5.29.32";
        this.critical = false;
    }

    public CertificatePoliciesHolder(boolean critical, List<PolicyDescriber> policies) {
        this.name = "Certificate Policies";
        this.ASN1ObjectIdentifier = "2.5.29.32";
        this.critical = critical;
        this.policies = policies;
    }

    public List<PolicyDescriber> getPolicies() {
        return policies;
    }

    public void setPolicies(List<PolicyDescriber> policies) {
        this.policies = policies;
    }
}
