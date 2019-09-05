package megatravel.com.pki.domain.rbac;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Privilege implements Serializable, GrantedAuthority {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Set<Role> roles;

    public Privilege() {
    }

    public Privilege(Long id, String name, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.roles = roles;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
