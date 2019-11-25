package megatravel.com.ca.domain.dto.auth;


import megatravel.com.ca.domain.rbac.User;

public class LoggedUserDTO {

    private Long id;
    private String username;
    private String password;

    public LoggedUserDTO() {
    }

    public LoggedUserDTO(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public LoggedUserDTO(User entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.password = entity.getPassword();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
