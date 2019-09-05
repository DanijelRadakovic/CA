package megatravel.com.pki.domain.DTO.cer.ext;

import java.util.List;

public class SubjectAlternativeNameDTO {
    private boolean critical;
    private List<String> altNames;

    public SubjectAlternativeNameDTO() {
    }

    public SubjectAlternativeNameDTO(boolean critical, List<String> altNames) {
        this.critical = critical;
        this.altNames = altNames;
    }

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    public List<String> getAltNames() {
        return altNames;
    }

    public void setAltNames(List<String> altNames) {
        this.altNames = altNames;
    }
}
