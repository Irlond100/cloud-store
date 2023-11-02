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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.dude.cloudstore.dto.ErrorResponse;
import ru.dude.cloudstore.dto.HeaderNameHolder;
import ru.dude.cloudstore.entities.UserAuthority;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Value("${app.web.security.public}")
    public String[] PUBLIC_URIS;

    @Value("${app.secret-key}")
    private String secretKey;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void doFilterInternal(HttpServletRequest httpServletRequest,
                                 @NonNull HttpServletResponse httpServletResponse,
                                 @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
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
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                httpServletResponse.getWriter().write(objectMapper.writeValueAsString(new ErrorResponse(e.getMessage())));
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
        for (String publicUri : PUBLIC_URIS) {
            if (antPathMatcher.match(publicUri, requestURI))
                return true;
        }
        return false;
    }

    public boolean validateJwt(String jwt) {
        Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
        return true;
    }


    public Set<UserAuthority> getAuthorities() {
        return Collections.EMPTY_SET;
    }

    public String getUsername(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
        return claims.getSubject();
    }
}