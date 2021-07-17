package com.fschoen.parlorplace.backend.security;

import com.fschoen.parlorplace.backend.entity.transience.UserDetailsImplementation;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    @Value("${fschoen.parlorplace.jwtSecret}")
    private String jwtSecret;

    @Value("${fschoen.parlorplace.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(UserDetailsImplementation userPrincipal) {
        return generateTokenFromUsername(userPrincipal.getUsername());
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(getKey(jwtSecret))
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey(jwtSecret)).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey(jwtSecret)).build().parseClaimsJws(authToken);
            return true;
        } catch (SecurityException e) {
            log.error(Messages.getExceptionExplanationMessage("authorization.signature.invalid"), e.getMessage());
        } catch (MalformedJwtException e) {
            log.error(Messages.getExceptionExplanationMessage("authorization.token.invalid"), e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error(Messages.getExceptionExplanationMessage("authorization.token.expired"), e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error(Messages.getExceptionExplanationMessage("authorization.token.unsupported"), e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error(Messages.getExceptionExplanationMessage("authorization.token.empty"), e.getMessage());
        }

        return false;
    }

    private static SecretKey getKey(String jwtSecret) {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

}
