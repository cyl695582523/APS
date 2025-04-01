package com.dksh.hkbcf.exception.handler;

import com.dksh.hkbcf.exception.exception.ClientException;
import com.dksh.hkbcf.pojo.APSAPICommonResponse;
import jakarta.websocket.DecodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {
/*
    @ResponseBody
    @ExceptionHandler(value= {Exception.class})
    public APSAPICommonResponse handleException(Exception ex) {
        log.error("error log: {}",ex.getMessage(),ex);
        return APSAPICommonResponse.baseBuilder()
                .resultCode(0)
                .resultMessage("System Error")
                .build();
    }
*/

    @ResponseBody
    @ExceptionHandler(value= {RuntimeException.class, Exception.class, ClientException.class})
    public APSAPICommonResponse handleClientException(Exception ex) {
        log.error("error log: {}",ex.getMessage(),ex);
        return APSAPICommonResponse.baseBuilder()
                .resultCode(0)
                .resultMessage(ex.getMessage())
                .build();
    }
    @ResponseBody
    @ExceptionHandler(value= {NullPointerException.class})
    public APSAPICommonResponse handleNullPointerException(Exception ex) {
        log.error("error log: {}",ex.getMessage(),ex);
        return APSAPICommonResponse.baseBuilder()
                .resultCode(0)
                .resultMessage("record not found")
                .build();
    }
    @ResponseBody
    @ExceptionHandler(value= {UsernameNotFoundException.class})
    public APSAPICommonResponse handleUsernameNotFoundException(Exception ex) {
        log.error("error log: {}",ex.getMessage(),ex);
        return APSAPICommonResponse.baseBuilder()
                .resultCode(0)
                .resultMessage("user not found")
                .build();
    }
}