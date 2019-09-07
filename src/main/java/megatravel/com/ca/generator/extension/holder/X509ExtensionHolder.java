package megatravel.com.ca.generator.extension.holder;

public abstract class X509ExtensionHolder {

    protected String name;
    protected String ASN1ObjectIdentifier;
    protected boolean critical;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getASN1ObjectIdentifier() {
        return ASN1ObjectIdentifier;
    }

    public void setASN1ObjectIdentifier(String ASN1ObjectIdentifier) {
        this.ASN1ObjectIdentifier = ASN1ObjectIdentifier;
    }

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }
}
