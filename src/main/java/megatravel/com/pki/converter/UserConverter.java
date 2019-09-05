package megatravel.com.pki.converter;

import megatravel.com.pki.domain.DTO.auth.LoggedUserDTO;
import megatravel.com.pki.domain.DTO.auth.RegisterDTO;
import megatravel.com.pki.domain.DTO.rbac.PrivilegeDTO;
import megatravel.com.pki.domain.DTO.rbac.RoleDTO;
import megatravel.com.pki.domain.DTO.rbac.UserDTO;
import megatravel.com.pki.domain.rbac.Privilege;
import megatravel.com.pki.domain.rbac.Role;
import megatravel.com.pki.domain.rbac.User;

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
        //user.setSalt(salt);
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
     * @param salt     - hehe :D
     * @return hashed password
     */
    private static String hashPassword(String password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 255);
        Arrays.fill(password.toCharArray(), Character.MIN_VALUE);
        return null;
//        try {
//            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
//
//            //return skf.generateSecret(spec).getEncoded();
//        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
//            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
//        } finally {
//            spec.clearPassword();
//        }
    }


    public static LoggedUserDTO fromLoggedEntity(User entity) {
        return new LoggedUserDTO(entity);

    }

}
