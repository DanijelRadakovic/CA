package megatravel.com.pki.generator.extension.holder;

public class BasicConstraintsHolder extends X509ExtensionHolder {

    private boolean cA;
    private int pathLenConstraint;

    public BasicConstraintsHolder() {
        this.name = "Basic Constraints";
        this.ASN1ObjectIdentifier = "2.5.29.19";
        this.critical = false;
    }

    public BasicConstraintsHolder(boolean critical, boolean cA, int pathLenConstraint) {
        this.name = "Basic Constraints";
        this.ASN1ObjectIdentifier = "2.5.29.19";
        this.critical = critical;
        this.cA = cA;
        this.pathLenConstraint = pathLenConstraint;
    }

    public boolean iscA() {
        return cA;
    }

    public void setcA(boolean cA) {
        this.cA = cA;
    }

    public int getPathLenConstraint() {
        return pathLenConstraint;
    }

    public void setPathLenConstraint(int pathLenConstraint) {
        this.pathLenConstraint = pathLenConstraint;
    }
}
