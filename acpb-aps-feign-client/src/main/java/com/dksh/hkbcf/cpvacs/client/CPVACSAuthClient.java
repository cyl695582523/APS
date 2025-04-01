package com.dksh.hkbcf.cpvacs.client;

import com.dksh.hkbcf.mps.config.MPSClientConfiguration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "cpvacsAuthClient")
public interface CPVACSAuthClient {

    @Data
    @Builder(builderMethodName = "baseBuilder")
    @NoArgsConstructor
    @AllArgsConstructor
    class CommonResponse<T>{
        private String resultCode;
        private String resultMsg;
        private String requestId;
        private String sysDatetime;
        private T data;
    }

    // 2.1. 授權
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class LoginRequest{
        private String username;
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class LoginResponse{
        private String accessToken;
        private String tokenType;
        private Integer expiresIn;
        private List<String> scope;
    }

    @PostMapping(value = "/server/auth/client/login", headers = {"x-api-key=${bossClient.apiKey}"})
    CommonResponse<LoginResponse> login(@RequestBody LoginRequest request);

}
