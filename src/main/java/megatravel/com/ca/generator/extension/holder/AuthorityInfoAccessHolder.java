package megatravel.com.ca.generator.extension.holder;

import org.bouncycastle.asn1.x509.AccessDescription;

import java.util.List;

public class AuthorityInfoAccessHolder extends X509ExtensionHolder {

    private List<AccessDescription> accessDescriptions;

    public AuthorityInfoAccessHolder() {
        this.name = "Authority Info Access";
        this.ASN1ObjectIdentifier = "1.3.6.1.5.5.7.1.1";
        this.critical = false;
    }

    public AuthorityInfoAccessHolder(boolean critical, List<AccessDescription> accessDescriptions) {
        this.name = "Authority Info Access";
        this.ASN1ObjectIdentifier = "1.3.6.1.5.5.7.1.1";
        this.critical = critical;
        this.accessDescriptions = accessDescriptions;
    }

    public List<AccessDescription> getAccessDescriptions() {
        return accessDescriptions;
    }

    public void setAccessDescriptions(List<AccessDescription> accessDescriptions) {
        this.accessDescriptions = accessDescriptions;
    }
}
