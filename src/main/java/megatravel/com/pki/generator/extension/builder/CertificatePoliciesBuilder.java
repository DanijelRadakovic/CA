package megatravel.com.pki.generator.extension.builder;

import megatravel.com.pki.generator.extension.holder.CertificatePoliciesHolder;
import megatravel.com.pki.generator.extension.holder.X509ExtensionHolder;
import megatravel.com.pki.util.GeneralNameValidator;
import megatravel.com.pki.util.ValidationException;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;

import java.util.ArrayList;
import java.util.List;

public class CertificatePoliciesBuilder implements X509ExtensionBuilder {

    private GeneralNameValidator validator;

    public CertificatePoliciesBuilder() {
        this.validator = new GeneralNameValidator();
    }

    @Override
    public void build(X509v3CertificateBuilder certificateBuilder, X509ExtensionHolder holder)
            throws ValidationException, CertIOException {
        if (holder instanceof CertificatePoliciesHolder) {
            CertificatePoliciesHolder cPHolder = (CertificatePoliciesHolder) holder;
            List<PolicyInformation> policies = new ArrayList<>(cPHolder.getPolicies().size());
            cPHolder.getPolicies().forEach(policyDescriber -> {
                policyDescriber.getQualifiers().forEach(qualifier -> {
                    if (!validator.isValidURI(qualifier)) {
                        throw new ValidationException("Policy qualifier must be vali URI.");
                    }
                });
                policies.add(new PolicyInformation(policyDescriber.getQualifierId(),
                        new DERSequence(policyDescriber.getQualifiers().toArray(new PolicyQualifierInfo[0]))));
            });
            certificateBuilder.addExtension(Extension.certificatePolicies, holder.isCritical(),
                    new CertificatePolicies(policies.toArray(new PolicyInformation[0])));
        } else {
            throw new ValidationException("Inappropriate holder received.");
        }
    }

    @Override
    public String getMatchingHolder() {
        return CertificatePoliciesHolder.class.getCanonicalName();
    }
}
