package com.dksh.hkbcf.cpvacs.service;

import com.dksh.hkbcf.cpvacs.client.CPVACSAuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CPVACSAuthServiceImpl implements CPVACSAuthService {

    @Autowired
    CPVACSAuthClient cpvacsAuthClient;

    @Override
    public String login() {
        CPVACSAuthClient.CommonResponse<CPVACSAuthClient.LoginResponse> res1 = cpvacsAuthClient.login(CPVACSAuthClient.LoginRequest.builder()
                        .username("demoApp")
                        .password("123456")
                .build());
        return res1.getData().getAccessToken();
    }
}
