package megatravel.com.ca.domain.dto.rbac;

import java.util.Set;

public class RoleDTO {

    private Long id;

    private String name;

    private Set<PrivilegeDTO> privileges;

    public RoleDTO() {
    }

    public RoleDTO(Long id, String name, Set<PrivilegeDTO> privileges) {
        this.name = name;
        this.privileges = privileges;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PrivilegeDTO> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<PrivilegeDTO> privileges) {
        this.privileges = privileges;
    }
}
