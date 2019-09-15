package megatravel.com.ca.domain.dto.rbac;

public class PrivilegeDTO {

    private Long id;

    private String name;

    public PrivilegeDTO() {
    }

    public PrivilegeDTO(Long id, String name) {
        this.id = id;
        this.name = name;
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
}
