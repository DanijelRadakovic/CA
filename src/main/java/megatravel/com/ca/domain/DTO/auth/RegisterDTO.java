package megatravel.com.ca.domain.DTO.auth;

import megatravel.com.ca.domain.DTO.rbac.RoleDTO;

import java.util.Set;

public class RegisterDTO {

    private String username;

    private String password;

    private Set<RoleDTO> roles;

    public RegisterDTO() {
    }

    public RegisterDTO(String username, String password, Set<RoleDTO> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }
}
