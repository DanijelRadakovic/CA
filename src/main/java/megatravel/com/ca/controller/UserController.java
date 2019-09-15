package megatravel.com.ca.controller;

import megatravel.com.ca.converter.UserConverter;
import megatravel.com.ca.domain.dto.auth.AuthenticationRequestDTO;
import megatravel.com.ca.domain.dto.auth.AuthenticationResponseDTO;
import megatravel.com.ca.domain.dto.auth.LoggedUserDTO;
import megatravel.com.ca.domain.dto.auth.RegisterDTO;
import megatravel.com.ca.domain.rbac.User;
import megatravel.com.ca.security.TokenUtils;
import megatravel.com.ca.service.UserService;
import megatravel.com.ca.util.exception.GeneralException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenUtils tokenUtils;

    /**
     * Endpoint for request for getting use by id
     *
     * @param id - given id through url
     * @return user with given ID with OK, or BAD_REQUEST if snot found with given ID
     */
    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        try {
            User user = userService.findById(id);
            logger.info("userId={} action=get status=success", id);
            return new ResponseEntity<>(UserConverter.fromRegisteringEntity(user), HttpStatus.OK);
        } catch (GeneralException e) {
            logger.warn("userId={} action=get status=failure", id);
            return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
        } catch (Exception e1) {
            logger.error("userId={} action=get status=failure message={}", id, e1.getMessage());
            return new ResponseEntity<>(e1.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint for getting all users
     *
     * @return list of users, or BAD_REQUEST if something went wrong
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            logger.info("action=getAllUsers status=success");
            return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("action=getAllUsers status=failure message={}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * POST /api/user/auth
     * Authenticates a user in the system.
     *
     * @param authenticationRequest DTO with user's login credentials
     * @return ResponseEntity with a AuthenticationResponseDTO, containing user data and his JSON Web Token
     */
    @PostMapping("/auth")
    public ResponseEntity<Object> login(@Valid @RequestBody AuthenticationRequestDTO authenticationRequest) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword());

            authenticationManager.authenticate(authToken);

            UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            LoggedUserDTO user = UserConverter.fromLoggedEntity(userService.findByUsername(userDetails.getUsername()));
            String token = tokenUtils.generateToken(userDetails);

            logger.info("username={} action=login status=success", authenticationRequest.getUsername());
            return new ResponseEntity<>(new AuthenticationResponseDTO(user, token), HttpStatus.OK);
        } catch (Exception e) {
            logger.info("username={} action=login status=failure", authenticationRequest.getUsername());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Endpoint for registering new user
     *
     * @param registerDTO - all required data for new user
     * @return user if successfully registered, or message if error happened
     */
    @PostMapping("register")
    public ResponseEntity<Object> register(@RequestBody RegisterDTO registerDTO) {
        try {
            User user = userService.register(UserConverter.toRegisteringEntity(registerDTO));
            logger.info("username={} action=signUp status=success", registerDTO.getUsername());
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (GeneralException e) {
            logger.warn("username={} action=signUp status=failure", registerDTO.getUsername());
            return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
        } catch (Exception e1) {
            logger.error("username={} action=signUp status=failure message={}",
                    registerDTO.getUsername(), e1.getMessage());
            return new ResponseEntity<>(e1.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
