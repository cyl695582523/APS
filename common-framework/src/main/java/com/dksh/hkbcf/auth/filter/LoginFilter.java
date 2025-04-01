package com.dksh.hkbcf.auth.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dksh.hkbcf.auth.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class LoginFilter extends OncePerRequestFilter {

    private final UserDetailsService users;

    public LoginFilter(UserDetailsService users) {
        this.users = users;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String token = authHeader.substring(7);
            try{
                final DecodedJWT jwt = JwtUtil.getDecodedJWT(token);
                UserDetails userDetails = users.loadUserByUsername(jwt.getSubject());
                Authentication authentication = new UsernamePasswordAuthenticationToken(jwt,
                        userDetails.getPassword(), userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JWTVerificationException ex){
                response.getWriter().write(ex.getMessage());
            } catch (Exception ex){
                chain.doFilter(request, response);
            }
        }
        chain.doFilter(request, response);

    }
}