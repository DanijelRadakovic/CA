package megatravel.com.pki.service;

import megatravel.com.pki.domain.rbac.User;
import megatravel.com.pki.repository.UserRepository;
import megatravel.com.pki.util.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Finding all users from database.
     *
     * @return list of users.
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Finding user with given ID.
     *
     * @param id - id of User we searching for.
     * @return User with given ID, throws GeneralException if not found or given ID is null.
     */
    public User findById(Long id) {
        try {
            return userRepository.findById(id).orElseThrow(() ->
                    new GeneralException("User with given ID doesn't exists!", HttpStatus.BAD_REQUEST));

        } catch (InvalidDataAccessApiUsageException e) {
            throw new GeneralException("Id cannot be null!", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Saving new user to database
     *
     * @param user - User with given data
     * @return user - if successfully saved, throws GeneralException if given username already exists,
     * or if failed to save new user
     */
    public User register(User user) {
        try {

            if (userRepository.findUserByUsername(user.getUsername()) != null) {
                throw new GeneralException("User with given ID already exists!", HttpStatus.CONFLICT);
            }
            return userRepository.save(user);
        } catch (Exception e) {
            throw new GeneralException("Failed to save user!", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deleting user with given ID
     *
     * @param id - id of user we want to delete
     *           Throws GeneralException when trying to delete with unexisting ID
     */
    public void deleteById(Long id) {
        try {
            userRepository.findById(id).orElseThrow(() ->
                    new GeneralException("Cannot delete user with unexisting id!", HttpStatus.BAD_REQUEST));
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new GeneralException("There was a problem with deleting user", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deleting user with given username
     *
     * @param username - username of user we want to delete
     *                 Throws GeneralException when trying to delete with unexisting username
     */
    public void deleteByUsername(String username) {
        try {
            User user = userRepository.findUserByUsername(username);
            if (user != null) {
                userRepository.delete(user);
            } else {
                throw new GeneralException("User with given username doesn't exists!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new GeneralException("There was a problem with deleting user", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Logging in with given username and password
     *
     * @param username - inserted username
     * @param password - inserted password
     * @return Logged User
     */
    public User logIn(String username, String password) {
        try {
            User user = userRepository.findUserByUsername(username);
            if (user != null) {
                return user;
                //return (authenticate(password, user.getPassword(), user.getSalt()) ? user : null);
            } else {
                throw new GeneralException("Inserted combination of username and password isn't correct!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new GeneralException("Something went wrong...", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Authentification of user trying to log in
     *
     * @param attemptedPassword - inserted password
     * @param storedPassword    - hashed password from database
     * @param salt              - hehe :D
     * @return true is user password is OK, false if not
     */
    private boolean authenticate(String attemptedPassword, String storedPassword, byte[] salt) {
        byte[] pwdHash = new byte[255];
        byte[] hashed = hashPassword(attemptedPassword, salt);
        for (int i = 0; i < hashed.length; i++) {
            pwdHash[i] = hashed[i];
        }
//        Arrays.fill(attemptedPassword.toCharArray(), Character.MIN_VALUE);
//        if (pwdHash.length != storedPassword.length) return false;
//        for (int i = 0; i < pwdHash.length; i++) {
//            if (pwdHash[i] != storedPassword[i]) return false;
//        }
        return true;
    }

    /**
     * Hashing password
     *
     * @param password - inserted password
     * @param salt     - hehe :D
     * @return hashed password
     */
    private byte[] hashPassword(String password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 255);
        Arrays.fill(password.toCharArray(), Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }


    public User findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}
