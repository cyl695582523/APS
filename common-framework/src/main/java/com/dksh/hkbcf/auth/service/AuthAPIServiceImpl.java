package com.dksh.hkbcf.auth.service;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.dksh.hkbcf.auth.controller.AuthController.*;
import com.dksh.hkbcf.auth.util.JwtUtil;
import com.dksh.hkbcf.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.stream.Collectors;

@Service
public class AuthAPIServiceImpl implements AuthAPIService{

    @Autowired
    UserDetailsService users;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public GrantTokenResponse grantToken(GrantTokenRequest request) throws Exception {

        UserDetails userDetails = users.loadUserByUsername(request.username());
        if (userDetails == null || !passwordEncoder.matches(request.password(), userDetails.getPassword())) {
            throw new Exception("wrong password");
        }
        JwtUtil.JwtPair jwtPair = JwtUtil.generate(userDetails.getUsername(), userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return ObjectMapperUtil.clone(jwtPair, GrantTokenResponse.class);

    }

    public RefreshTokenResponse refreshToken() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = users.loadUserByUsername(((DecodedJWT)authentication.getPrincipal()).getSubject());

        JwtUtil.JwtPair jwtPair = JwtUtil.generate(userDetails.getUsername(), userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return ObjectMapperUtil.clone(jwtPair, RefreshTokenResponse.class);

    }
}
