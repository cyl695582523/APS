package com.dksh.hkbcf.auth.service;

import com.dksh.hkbcf.auth.controller.AuthController;

public interface AuthAPIService {

     AuthController.GrantTokenResponse grantToken(AuthController.GrantTokenRequest request) throws Exception;

     AuthController.RefreshTokenResponse refreshToken();
}
