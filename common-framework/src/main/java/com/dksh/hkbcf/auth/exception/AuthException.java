package com.dksh.hkbcf.auth.exception;

import com.dksh.hkbcf.pojo.APSAPICommonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@Component
public class AuthException implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        response.setStatus();
//        response.getWriter().write(authException.getMessage());


//        RestError re = new RestError(HttpStatus.UNAUTHORIZED.toString(), "Authentication failed");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        OutputStream responseStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(responseStream, APSAPICommonResponse.baseBuilder()
                .resultCode(0)
                .resultMessage(authException.getMessage())
                .build());
        log.info(APSAPICommonResponse.baseBuilder()
                .resultCode(0)
                .resultMessage(authException.getMessage())
                .build().toString());
        responseStream.flush();
    }
}