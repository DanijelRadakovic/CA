package megatravel.com.ca.domain.DTO.rbac;


import java.util.Set;

public class UserDTO {
    private String username;
    private Set<RoleDTO> roles;

    public UserDTO() {
    }

    public UserDTO(String username, String password, Set<RoleDTO> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }
}
