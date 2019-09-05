package megatravel.com.pki.generator.extension.builder;

import megatravel.com.pki.generator.extension.holder.X509ExtensionHolder;
import megatravel.com.pki.util.ValidationException;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;

public interface X509ExtensionBuilder {

    void build(X509v3CertificateBuilder certificateBuilder, X509ExtensionHolder holder)
            throws ValidationException, CertIOException;
    String getMatchingHolder();
}
