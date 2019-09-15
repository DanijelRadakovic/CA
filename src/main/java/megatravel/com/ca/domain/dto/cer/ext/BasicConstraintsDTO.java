package megatravel.com.ca.domain.dto.cer.ext;

public class BasicConstraintsDTO {
    private boolean critical;
    private boolean cA;
    private int pathLenConstraint;

    public BasicConstraintsDTO() {
    }

    public BasicConstraintsDTO(boolean critical, boolean cA, int pathLenConstraint) {
        this.critical = critical;
        this.cA = cA;
        this.pathLenConstraint = pathLenConstraint;
    }

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
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
