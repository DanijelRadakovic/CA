package megatravel.com.ca.generator.extension.builder;

import megatravel.com.ca.generator.extension.holder.KeyUsageHolder;
import megatravel.com.ca.generator.extension.holder.X509ExtensionHolder;
import megatravel.com.ca.util.ValidationException;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;

public class KeyUsageBuilder implements X509ExtensionBuilder {

    @Override
    public void build(X509v3CertificateBuilder certificateBuilder, X509ExtensionHolder holder)
            throws ValidationException, CertIOException {
        if (holder instanceof KeyUsageHolder) {
            certificateBuilder.addExtension(Extension.keyUsage, holder.isCritical(),
                    new KeyUsage(((KeyUsageHolder) holder).getKeyUsageBits()));
        } else {
            throw new ValidationException("Inappropriate holder received.");
        }
    }

    @Override
    public String getMatchingHolder() {
        return KeyUsageHolder.class.getCanonicalName();
    }
}
