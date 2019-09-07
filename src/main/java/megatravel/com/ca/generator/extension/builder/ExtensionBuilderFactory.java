package megatravel.com.ca.generator.extension.builder;

import java.util.ArrayList;
import java.util.List;

public class ExtensionBuilderFactory {

    public static List<X509ExtensionBuilder> getAllExtensions() {
        return new ArrayList<X509ExtensionBuilder>() {{
            add(new BasicConstraintsBuilder());
            add(new KeyUsageBuilder());
            add(new ExtendedKeyUsageBuilder());
            add(new SubjectAlternativeNameBuilder());
            add(new AuthorityInfoAccessBuilder());
            add(new NameConstraintsBuilder());
            add(new CertificatePoliciesBuilder());
        }};
    }
}
