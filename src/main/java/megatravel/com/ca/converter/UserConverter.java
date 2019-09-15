package megatravel.com.ca.converter;

import megatravel.com.ca.domain.dto.auth.LoggedUserDTO;
import megatravel.com.ca.domain.dto.auth.RegisterDTO;
import megatravel.com.ca.domain.dto.rbac.PrivilegeDTO;
import megatravel.com.ca.domain.dto.rbac.RoleDTO;
import megatravel.com.ca.domain.dto.rbac.UserDTO;
import megatravel.com.ca.domain.rbac.Privilege;
import megatravel.com.ca.domain.rbac.Role;
import megatravel.com.ca.domain.rbac.User;

import javax.crypto.spec.PBEKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class UserConverter extends AbstractConverter {

    public static User toRegisteringEntity(RegisterDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        byte[] salt = generateSalt();
        user.setPassword(hashPassword(dto.getPassword(), salt));
        return user;
    }

    public static UserDTO fromRegisteringEntity(User entity) {
        UserDTO dto = new UserDTO();
        dto.setUsername(entity.getUsername());
        Set<RoleDTO> roles = new HashSet<>();
        for (Role r : entity.getRoles()) {
            RoleDTO role = new RoleDTO();
            role.setId(r.getId());
            role.setName(r.getName());
            Set<PrivilegeDTO> privileges = new HashSet<>();
            for (Privilege p : r.getPrivileges()) {
                PrivilegeDTO privilege = new PrivilegeDTO();
                privilege.setId(p.getId());
                privilege.setName(p.getName());
                privileges.add(privilege);
            }
            role.setPrivileges(privileges);
            roles.add(role);
        }
        dto.setRoles(roles);
        return dto;
    }


    /**
     * generating salt for storing in database
     */
    private static byte[] generateSalt() {
        Random random = new SecureRandom(ByteBuffer.allocate(4)
                .putInt(LocalDateTime.now().getSecond()).array());
        byte[] salt = new byte[64];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Hashing password
     *
     * @param password - inserted password
     * @param salt     - to salt password
     * @return hashed password
     */
    private static String hashPassword(String password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 255);
        Arrays.fill(password.toCharArray(), Character.MIN_VALUE);
        return null;
    }


    public static LoggedUserDTO fromLoggedEntity(User entity) {
        return new LoggedUserDTO(entity);
    }

}
