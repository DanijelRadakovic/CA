package megatravel.com.ca.domain.dto.cer.ext;

import java.util.List;

public class NameConstraintsDTO {
    private boolean critical;
    private List<String> permitted;
    private List<String> excluded;

    public NameConstraintsDTO() {
    }

    public NameConstraintsDTO(boolean critical, List<String> permitted, List<String> excluded) {
        this.critical = critical;
        this.permitted = permitted;
        this.excluded = excluded;
    }

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
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
