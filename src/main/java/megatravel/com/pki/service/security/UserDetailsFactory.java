package megatravel.com.pki.service.security;

import megatravel.com.pki.domain.rbac.User;
import megatravel.com.pki.domain.security.UserDetailsImpl;

/**
 * Factory for creating instance of {@link UserDetailsImpl}.
 */
public class UserDetailsFactory {

    private UserDetailsFactory() {
    }

    /**
     * Creates UserDetailsImpl from a user.
     *
     * @param user user model
     * @return UserDetailsImpl
     */
    public static UserDetailsImpl create(User user) {
        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}

