package megatravel.com.pki.generator.extension.builder;

import megatravel.com.pki.generator.extension.holder.ExtendedKeyUsageHolder;
import megatravel.com.pki.generator.extension.holder.X509ExtensionHolder;
import megatravel.com.pki.util.ValidationException;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;

public class ExtendedKeyUsageBuilder implements X509ExtensionBuilder {

    @Override
    public void build(X509v3CertificateBuilder certificateBuilder, X509ExtensionHolder holder)
            throws ValidationException, CertIOException {
        if (holder instanceof ExtendedKeyUsageHolder) {
            certificateBuilder.addExtension(Extension.extendedKeyUsage, holder.isCritical(),
                    new ExtendedKeyUsage(((ExtendedKeyUsageHolder) holder).getKeyPurpose()));
        } else {
            throw new ValidationException("Inappropriate holder received.");
        }
    }

    @Override
    public String getMatchingHolder() {
        return ExtendedKeyUsageHolder.class.getCanonicalName();
    }
}
