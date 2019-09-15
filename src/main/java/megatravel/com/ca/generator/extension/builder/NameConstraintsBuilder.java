package megatravel.com.ca.generator.extension.builder;

import megatravel.com.ca.generator.extension.holder.NameConstraintsHolder;
import megatravel.com.ca.generator.extension.holder.X509ExtensionHolder;
import megatravel.com.ca.util.exception.ValidationException;
import megatravel.com.ca.util.validator.GeneralNameValidator;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralSubtree;
import org.bouncycastle.asn1.x509.NameConstraints;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;

import java.util.ArrayList;
import java.util.List;

public class NameConstraintsBuilder implements X509ExtensionBuilder {

    private GeneralNameValidator validator;

    public NameConstraintsBuilder() {
        this.validator = new GeneralNameValidator();
    }

    @Override
    public void build(X509v3CertificateBuilder certificateBuilder, X509ExtensionHolder holder)
            throws ValidationException, CertIOException {
        if (holder instanceof NameConstraintsHolder) {
            NameConstraintsHolder nCHolder = (NameConstraintsHolder) holder;
            List<GeneralSubtree> permitted = new ArrayList<>(nCHolder.getPermitted().size());
            List<GeneralSubtree> excluded = new ArrayList<>(nCHolder.getExcluded().size());

            nCHolder.getPermitted().forEach(name -> validate(name, permitted));
            nCHolder.getExcluded().forEach(name -> validate(name, excluded));

            certificateBuilder.addExtension(Extension.nameConstraints, holder.isCritical(),
                    new NameConstraints(permitted.toArray(new GeneralSubtree[0]),
                            excluded.toArray(new GeneralSubtree[0])));
        } else {
            throw new ValidationException("Inappropriate holder received.");
        }
    }

    @Override
    public String getMatchingHolder() {
        return NameConstraintsHolder.class.getCanonicalName();
    }

    private void validate(String name, List<GeneralSubtree> list) {
        if (validator.isValidDN(name)) {
            list.add(new GeneralSubtree(new GeneralName(GeneralName.directoryName, name)));
        } else if (validator.isValidEmail(name)) {
            list.add(new GeneralSubtree(new GeneralName(GeneralName.rfc822Name, name)));
        } else if (validator.isValidDNS(name)) {
            list.add(new GeneralSubtree(new GeneralName(GeneralName.dNSName, name)));
        } else if (validator.isValidIpAddress(name)) {
            list.add(new GeneralSubtree(new GeneralName(GeneralName.iPAddress, name)));
        } else if (validator.isValidURI(name)) {
            list.add(new GeneralSubtree(new GeneralName(GeneralName.uniformResourceIdentifier, name)));
        } else {
            throw new ValidationException("Invalid name constraint received.");
        }
    }
}
