package com.fschoen.parlorplace.backend.security;

import com.fschoen.parlorplace.backend.entity.transience.UserDetailsImplementation;
import com.fschoen.parlorplace.backend.utility.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import java.util.Date;

@Component
public class JwtUtils {

    @Value("${fschoen.parlorplace.jwtSecret}")
    private String jwtSecret;

    @Value("${fschoen.parlorplace.jwtExpirationMs}")
    private int jwtExpirationMs;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    public String generateJwtToken(UserDetailsImplementation userPrincipal) {
        return generateTokenFromUsername(userPrincipal.getUsername());
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            LOGGER.error(Messages.getExceptionExplanationMessage("authorization.signature.invalid"), e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error(Messages.getExceptionExplanationMessage("authorization.token.invalid"), e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error(Messages.getExceptionExplanationMessage("authorization.token.expired"), e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error(Messages.getExceptionExplanationMessage("authorization.token.unsupported"), e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error(Messages.getExceptionExplanationMessage("authorization.token.empty"), e.getMessage());
        }

        return false;
    }

}
