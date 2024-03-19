package com.example.feelsun.config.jwt;

import com.example.feelsun.config.auth.PrincipalUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String salt;

    public static final String TOKEN_PREFIX = "Bearer ";

    private Key secretKey;

    // 만료시간 1시간
    private final Long accessExp = 1000L * 60 * 60;

    // 만료시간 7일
    private final Long refreshExp = 1000L * 60 * 60 * 24 * 7;

    private final PrincipalUserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }

    // 리프래쉬 토큰 생성
    public String createRefreshToken(String identity) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(identity)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshExp))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 리프래쉬 토큰 검증
    public boolean validateRefreshToken(String token) {
        try {
            Jws<Claims> claims = Jwts
                    .parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰 생성
    public String createToken(String identity, String role, String nickname) {
        Claims claims = Jwts.claims().setSubject(identity);
        claims.put("role", role);
        claims.put("nickname", nickname);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessExp))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 권한 정보 획득 + Spring Security 인증 과정에서 권한 확인을 위한 기능
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getIdentity(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Token 에 담겨있는 유저 Identity get
    public String getIdentity(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Authorization Header 를 통해 인증
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    // Token 검증
    public boolean validateToken(String token) {
        try {
            // Bearer 검증 + equalsIgnoreCase()를 사용하여 대소문자 구분없이 비교
            if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
                return false;
            } else {
                token = token.split(" ")[1].trim();
            }

            Jws<Claims> claims = Jwts
                    .parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            // 만료되었는지 확인 -> 만료라면 false, 만료 전이라면 true
            return !claims.getBody().getExpiration().before(new Date());

        } catch (Exception e) {
            return false;
        }
    }
}
