package megatravel.com.pki.generator.extension.holder;

import java.util.List;

public class NameConstraintsHolder extends X509ExtensionHolder {

    private List<String> permitted;
    private List<String> excluded;

    public NameConstraintsHolder() {
        this.name = "Name Constraints";
        this.ASN1ObjectIdentifier = "2.5.29.30";
        this.critical = false;
    }

    public NameConstraintsHolder(boolean critical, List<String> permitted, List<String> excluded) {
        this.name = "Name Constraints";
        this.ASN1ObjectIdentifier = "2.5.29.30";
        this.critical = critical;
        this.permitted = permitted;
        this.excluded = excluded;
    }

    public List<String> getPermitted() {
        return permitted;
    }

    public void setPermitted(List<String> permitted) {
        this.permitted = permitted;
    }

    public List<String> getExcluded() {
        return excluded;
    }

    public void setExcluded(List<String> excluded) {
        this.excluded = excluded;
    }
}
