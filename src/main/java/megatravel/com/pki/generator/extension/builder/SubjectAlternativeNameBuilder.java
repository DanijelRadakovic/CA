package megatravel.com.pki.generator.extension.builder;

import megatravel.com.pki.generator.extension.holder.SubjectAlternativeNameHolder;
import megatravel.com.pki.generator.extension.holder.X509ExtensionHolder;
import megatravel.com.pki.util.GeneralNameValidator;
import megatravel.com.pki.util.ValidationException;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNamesBuilder;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;

public class SubjectAlternativeNameBuilder implements X509ExtensionBuilder {

    private GeneralNameValidator validator;

    public SubjectAlternativeNameBuilder() {
        this.validator = new GeneralNameValidator();
    }

    @Override
    public void build(X509v3CertificateBuilder certificateBuilder, X509ExtensionHolder holder)
            throws ValidationException, CertIOException {
        if (holder instanceof SubjectAlternativeNameHolder) {
            SubjectAlternativeNameHolder sANHolder = (SubjectAlternativeNameHolder) holder;
            GeneralNamesBuilder generalNamesBuilder = new GeneralNamesBuilder();

            sANHolder.getAlternativeNames().forEach(name -> validate(name, generalNamesBuilder));

            certificateBuilder.addExtension(Extension.subjectAlternativeName, holder.isCritical(),
                    generalNamesBuilder.build());
        } else {
            throw new ValidationException("Inappropriate holder received.");
        }
    }

    @Override
    public String getMatchingHolder() {
        return SubjectAlternativeNameHolder.class.getCanonicalName();
    }

    private void validate(String name, GeneralNamesBuilder builder) {
        if (validator.isValidDN(name)) {
            builder.addName(new GeneralName(GeneralName.directoryName, name));
        } else if (validator.isValidEmail(name)) {
            builder.addName(new GeneralName(GeneralName.rfc822Name, name));
        } else if (validator.isValidDNS(name)) {
            builder.addName(new GeneralName(GeneralName.dNSName, name));
        } else if (validator.isValidIpAddress(name)) {
            builder.addName(new GeneralName(GeneralName.iPAddress, name));
        } else if (validator.isValidURI(name)) {
            builder.addName(new GeneralName(GeneralName.uniformResourceIdentifier, name));
        } else {
            throw new ValidationException("Invalid subject alternative name received.");
        }
    }

}
