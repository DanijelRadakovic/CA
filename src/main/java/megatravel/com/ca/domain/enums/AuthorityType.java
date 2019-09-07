package megatravel.com.ca.domain.enums;

import org.springframework.security.core.GrantedAuthority;

public enum AuthorityType implements GrantedAuthority {
    REGISTERED_USER,
    ADMIN,
    VALIDATOR,
    OPERATOR;

    public String getAuthority() {
        return name();
    }
}
