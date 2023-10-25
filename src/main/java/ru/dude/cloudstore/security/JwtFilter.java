package ru.dude.cloudstore.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.dude.cloudstore.config.SecurityConfig;
import ru.dude.cloudstore.dto.ErrorResponse;
import ru.dude.cloudstore.dto.HeaderNameHolder;
import ru.dude.cloudstore.entities.UserAuthority;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    String secret = "tmNDKSlBzap3CxsSg0yYffBHF0t8CgnwxZiqI+WBUBPc0kineRBj8jFLI08hNCCj0MmnZq7hIhZaUCjd5Zr0QkjkMKooiOIOIOIKLK";
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void doFilterInternal(HttpServletRequest httpServletRequest,
                                 @NonNull HttpServletResponse httpServletResponse,
                                 @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (!isPublicUri(httpServletRequest.getRequestURI())) {
            try {
                String jwt = getJwt(httpServletRequest);
                if (StringUtils.hasText(jwt)
                        && validateJwt(jwt)) {
                    final var authentication = new UsernamePasswordAuthenticationToken(
                            getUsername(jwt), null, getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                final var objectMapper = new ObjectMapper();
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpServletResponse.setContentType("application/json");
                httpServletResponse.getWriter().write(objectMapper.writeValueAsString(new ErrorResponse(e.getMessage(), 1L)));
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    public String getJwt(HttpServletRequest request) throws SignatureException {
        String authHeader = request.getHeader(HeaderNameHolder.TOKEN_HEADER_NAME);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        } else {
            throw new SignatureException("wrong jwt is passed");
        }
    }


    private boolean isPublicUri(String requestURI) {
        for (String publicUri : SecurityConfig.PUBLIC_URIS) {
            if (antPathMatcher.match(publicUri, requestURI))
                return true;
        }
        return false;
    }

    public boolean validateJwt(String jwt) {
        Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt);
        return true;
    }


    public Set<UserAuthority> getAuthorities() {
        return Collections.EMPTY_SET;
    }

    public String getUsername(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(jwt)
                .getBody();
        return claims.getSubject();
    }
}