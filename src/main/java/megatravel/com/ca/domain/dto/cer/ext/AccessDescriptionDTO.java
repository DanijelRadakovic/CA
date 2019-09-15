package megatravel.com.ca.domain.dto.cer.ext;

import megatravel.com.ca.domain.enums.AccessMethodType;

public class AccessDescriptionDTO {
    private AccessMethodType accessMethod;
    private String location;

    public AccessDescriptionDTO() {
    }

    public AccessDescriptionDTO(AccessMethodType accessMethod, String location) {
        this.accessMethod = accessMethod;
        this.location = location;
    }

    public AccessMethodType getAccessMethod() {
        return accessMethod;
    }

    public void setAccessMethod(AccessMethodType accessMethod) {
        this.accessMethod = accessMethod;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
