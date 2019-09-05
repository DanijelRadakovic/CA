package megatravel.com.pki.generator.extension.builder;

import megatravel.com.pki.generator.extension.holder.BasicConstraintsHolder;
import megatravel.com.pki.generator.extension.holder.X509ExtensionHolder;
import megatravel.com.pki.util.ValidationException;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;

public class BasicConstraintsBuilder implements X509ExtensionBuilder {

    @Override
    public void build(X509v3CertificateBuilder certificateBuilder, X509ExtensionHolder holder)
            throws ValidationException, CertIOException {
        if (holder instanceof BasicConstraintsHolder) {
            BasicConstraints basicConstraints;
            BasicConstraintsHolder bCHolder = (BasicConstraintsHolder) holder;
            if (!bCHolder.iscA()) {
                basicConstraints = new BasicConstraints(false);
            } else if (bCHolder.getPathLenConstraint() > 0) {
                basicConstraints = new BasicConstraints(bCHolder.getPathLenConstraint());
            } else {
                basicConstraints = new BasicConstraints(true);
            }
            certificateBuilder.addExtension(Extension.basicConstraints, holder.isCritical(), basicConstraints);
        } else {
            throw new ValidationException("Inappropriate holder received.");
        }
    }

    @Override
    public String getMatchingHolder() {
        return BasicConstraintsHolder.class.getCanonicalName();
    }
}
