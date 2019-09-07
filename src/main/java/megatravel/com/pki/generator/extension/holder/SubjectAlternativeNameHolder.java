package megatravel.com.pki.generator.extension.holder;

import java.util.ArrayList;
import java.util.List;

public class SubjectAlternativeNameHolder extends X509ExtensionHolder {

    private List<String> alternativeNames;

    public SubjectAlternativeNameHolder() {
        this.name = "Subject Alternative Name";
        this.ASN1ObjectIdentifier = "2.5.29.17";
        this.critical = false;
        this.alternativeNames = new ArrayList<>();
    }

    public SubjectAlternativeNameHolder(boolean critical, List<String> alternativeNames) {
        this.name = "Subject Alternative Name";
        this.ASN1ObjectIdentifier = "2.5.29.17";
        this.critical = critical;
        this.alternativeNames = alternativeNames;
    }

    public List<String> getAlternativeNames() {
        return alternativeNames;
    }

    public void setAlternativeNames(List<String> alternativeNames) {
        this.alternativeNames = alternativeNames;
    }
}
