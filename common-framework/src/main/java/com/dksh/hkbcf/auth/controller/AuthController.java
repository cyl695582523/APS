package com.dksh.hkbcf.auth.controller;

import com.dksh.hkbcf.auth.service.AuthAPIService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Slf4j
@RestController
public class AuthController {

    @Autowired
    AuthAPIService authAPIService;

    public record GrantTokenRequest(String username, String password){}
    //@Builder
    public record GrantTokenResponse(String accessToken, Instant expiresIn){}

    @PostMapping("/grantToken")
    public GrantTokenResponse grantToken(@RequestBody GrantTokenRequest request) throws Exception {
        return authAPIService.grantToken(request);
    }

    public record RefreshTokenResponse(String accessToken, Instant expiresIn){}

    @PostMapping("/refreshToken")
    public RefreshTokenResponse refreshToken() {
        return authAPIService.refreshToken();
    }

}
