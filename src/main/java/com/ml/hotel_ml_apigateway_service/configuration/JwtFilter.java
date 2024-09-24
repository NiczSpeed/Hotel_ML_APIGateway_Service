package com.ml.hotel_ml_apigateway_service.configuration;

import com.ml.hotel_ml_apigateway_service.model.DeprecatedToken;
import com.ml.hotel_ml_apigateway_service.repository.DeprecatedTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Component
public class JwtFilter extends OncePerRequestFilter {

    Logger logger = Logger.getLogger(getClass().getName());

    //    @Value("${security.jwt.secret.key}")
    private String secretKey = "bUl4RGJBRm11VVlTdlZTeDRhM0pQdlBmODJCcHpxN0NtSXhEYkFGbXVVWVN2VlN4NGEzSlB2UGY4MkJwenE3Qw==";

    @Autowired
    private DeprecatedTokenRepository decryptedTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            logger.info("JWT Token cannot be found!");
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String token = header.substring(7);
            if (checkIfTokenIsDeprecated(token)) {
                logger.info("JWT Token cannot be found!");
                filterChain.doFilter(request, response);
                return;
            }
            String username = extractUsername(token);
            List<String> roles = extractRoles(token);
            List<SimpleGrantedAuthority> authorities = roles
                    .stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .toList();
            if (!username.isBlank() && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        new User(username, "", authorities),
                        null,
                        authorities
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            logger.info("JWT token could not be verified");
        }
        filterChain.doFilter(request, response);
    }


    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    private List<String> extractRoles(String token) {
        return extractAllClaims(token).get("roles", List.class);
    }

    private boolean checkIfTokenIsDeprecated(String token) {
        for (DeprecatedToken deprecatedToken : decryptedTokenRepository.findAll()) {
            if (deprecatedToken.getToken().equals(token)) {
                return true;
            }
        }
        return false;
    }


}
