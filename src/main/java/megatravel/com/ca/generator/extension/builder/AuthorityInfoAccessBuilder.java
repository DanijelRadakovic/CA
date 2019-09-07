package megatravel.com.ca.generator.extension.builder;

import megatravel.com.ca.generator.extension.holder.AuthorityInfoAccessHolder;
import megatravel.com.ca.generator.extension.holder.X509ExtensionHolder;
import megatravel.com.ca.util.GeneralNameValidator;
import megatravel.com.ca.util.ValidationException;
import org.bouncycastle.asn1.x509.AccessDescription;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;

public class AuthorityInfoAccessBuilder implements X509ExtensionBuilder {

    private GeneralNameValidator validator;

    public AuthorityInfoAccessBuilder() {
        this.validator = new GeneralNameValidator();
    }

    @Override
    public void build(X509v3CertificateBuilder certificateBuilder, X509ExtensionHolder holder)
            throws ValidationException, CertIOException {
        if (holder instanceof AuthorityInfoAccessHolder) {
            AuthorityInfoAccessHolder aIAHolder = (AuthorityInfoAccessHolder) holder;
            aIAHolder.getAccessDescriptions().forEach(accessDescription -> {
                if (accessDescription.getAccessLocation().getTagNo() != GeneralName.uniformResourceIdentifier)
                    throw new ValidationException("Only URI is acceptable for AIA values.");
                if (!validator.isValidURI(accessDescription.getAccessLocation().getName()
                        .toASN1Primitive().toString())) {
                    throw new ValidationException("Invalid URI received.");
                }
            });
            certificateBuilder.addExtension(Extension.authorityInfoAccess, holder.isCritical(),
                    new AuthorityInformationAccess(aIAHolder.getAccessDescriptions()
                            .toArray(new AccessDescription[0])));
        } else {
            throw new ValidationException("Inappropriate holder received.");
        }
    }

    @Override
    public String getMatchingHolder() {
        return AuthorityInfoAccessHolder.class.getCanonicalName();
    }
}
