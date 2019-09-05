package megatravel.com.pki.domain.DTO.cer.ext;

import java.util.List;

public class AuthorityInfoAccessDTO {
    private boolean critical;
    private List<AccessDescriptionDTO> accessDescriptions;

    public AuthorityInfoAccessDTO() {
    }

    public AuthorityInfoAccessDTO(boolean critical, List<AccessDescriptionDTO> accessDescriptions) {
        this.critical = critical;
        this.accessDescriptions = accessDescriptions;
    }

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    public List<AccessDescriptionDTO> getAccessDescriptions() {
        return accessDescriptions;
    }

    public void setAccessDescriptions(List<AccessDescriptionDTO> accessDescriptions) {
        this.accessDescriptions = accessDescriptions;
    }
}
