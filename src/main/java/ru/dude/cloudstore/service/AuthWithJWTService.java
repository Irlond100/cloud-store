package ru.dude.cloudstore.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.dude.cloudstore.dto.AuthRequest;
import ru.dude.cloudstore.entities.User;
import ru.dude.cloudstore.model.TokenResponse;
import ru.dude.cloudstore.repository.UserEntityRepository;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Data
public class AuthWithJWTService {
    private final PasswordEncoder passwordEncoder;
    private final UserEntityRepository userEntityRepository;

    @Value("${app.secret-key}")
    private String secretKey;

    public TokenResponse login(AuthRequest authRequest) throws RuntimeException {
        User user;
        try {
            user = loadUserByUsername(getUsernameEncoded(authRequest.username()));
            final var userEncodedPassword = user.getPassword();
            final var authRequestPassword = authRequest.password();
            if (passwordEncoder.matches(authRequestPassword, userEncodedPassword)) {
                final var jwt = generateJwt(user);
                return new TokenResponse(jwt);
            } else {
                throw new BadCredentialsException("bad");
            }
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("bad");
        }
    }

    private String getUsernameEncoded(String originalString) {
        return originalString;
    }

    public String generateJwt(UserDetails userDetails) {
        String username = userDetails.getUsername();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3_600_000);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .claim("roles", userDetails.getAuthorities())
                .compact();
    }

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository.findByUsername(username)
                .orElseThrow();
    }
}
