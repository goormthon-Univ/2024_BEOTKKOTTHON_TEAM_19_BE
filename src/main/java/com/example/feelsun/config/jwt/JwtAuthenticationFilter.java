package com.example.feelsun.config.jwt;

import com.example.feelsun.config.auth.PrincipalUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtProvider.resolveToken(request);

        if (token != null && jwtProvider.validateToken(token)) {
            try {
                token = token.split(" ")[1].trim();
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (SignatureException e) {
                request.setAttribute("exception", "유효하지 않은 JWT 토큰 서명입니다.");
            } catch (MalformedJwtException e) {
                request.setAttribute("exception", "손상된 JWT 토큰입니다.");
            } catch (ExpiredJwtException e) {
                request.setAttribute("exception", "만료된 JWT 토큰입니다.");
            } catch (UnsupportedJwtException e) {
                request.setAttribute("exception", "지원하지 않는 JWT 토큰입니다.");
            } catch (IllegalArgumentException e) {
                request.setAttribute("excepiton","JWT 토큰 내에 정보가 없습니다.");
            }
        }

        filterChain.doFilter(request, response);
    }

}
