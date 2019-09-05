package megatravel.com.pki.service.security;


import megatravel.com.pki.domain.rbac.User;
import megatravel.com.pki.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link UserDetailsService}.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        final User user = userRepository.findUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(
                    "User '" + username + "' not found"
            );
        }

        return UserDetailsFactory.create(user);
    }

}