package megatravel.com.pki.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for working with JSON Web Token.
 */
@Component
public class TokenUtils {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expiration}")
    private Long expiration;

    private static final String AUDIENCE_WEB = "web";

    /**
     * Generates JSON Web Token from username.
     *
     * @param userDetails user details
     * @return String with JSON Web Token
     */
    public String generateToken(UserDetails userDetails) {
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        final Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userDetails.getUsername());
        claims.put("audience", AUDIENCE_WEB);
        claims.put("created", generateCurrentDate());
        claims.put("authorities", authorities);
        return generateToken(claims);
    }

    /**
     * Generates JSON Web Token based on claims.
     *
     * @param claims maps of claims
     * @return String with JSON Web Token
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    /**
     * Decodes JSON Web Token and returns username.
     *
     * @param token JSON Web Token
     * @return String with username
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                username = null;
            } else {
                username = claims.getSubject();
            }
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * Gets claims form JSON Web Token.
     *
     * @param token JSON Web Token
     * @return Claims
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * Validates JSON Web Token.
     *
     * @param token       JSON Web Token
     * @param userDetails user details
     * @return true if token is valid
     */
    boolean validateToken(String token, UserDetails userDetails) {
        //final UserDetailsImpl user = (UserDetailsImpl) userDetails;
        final String username = getUsernameFromToken(token);
        final Date created = getCreatedDateFromToken(token);
        return username.equals(userDetails.getUsername()) && !(isTokenExpired(token));// && !(isCreatedBeforeLastPasswordReset(created, user.getLastPasswordReset()));
    }

    /**
     * Gets creation date of JSON Web Token.
     *
     * @param token JSON Web Token
     * @return token creation date
     */
    private Date getCreatedDateFromToken(String token) {
        Date createdDate;
        try {
            final Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                createdDate = null;
            } else {
                createdDate = new Date((Long) claims.get("created"));
            }
        } catch (Exception e) {
            createdDate = null;
        }
        return createdDate;
    }

    /**
     * Gets expiration date of JSON Web Token.
     *
     * @param token JSON Web Token
     * @return token expiration date
     */
    private Date getExpirationDateFromToken(String token) {
        Date expirationDate;
        try {
            final Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                expirationDate = null;
            } else {
                expirationDate = claims.getExpiration();
            }
        } catch (Exception e) {
            expirationDate = null;
        }
        return expirationDate;
    }

    /**
     * Checks if JSON Web Token is expired.
     *
     * @param token JSON Web Token
     * @return true if token is expired
     */
    private boolean isTokenExpired(String token) {
        final Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate == null || expirationDate.before(generateCurrentDate());
    }

    /**
     * Checks if JSON Web Token is created before password reset.
     *
     * @param created           JSON Web Token creation date
     * @param lastPasswordReset last password reset date
     * @return true if token was created before password was reset
     */
    private boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return lastPasswordReset != null && created.before(lastPasswordReset);
    }

    /**
     * Generates current date.
     *
     * @return current date
     */
    private Date generateCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * Generates expiration date.
     *
     * @return expiration date
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }
}
