package com.dksh.hkbcf.exception.handler;

import com.dksh.hkbcf.exception.exception.ClientException;
import com.dksh.hkbcf.pojo.BOSSAPSAPICommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
//@ControllerAdvice(assignableTypes = {BookingBOSSAPSController.class})
public class BOSSAPSExceptionHandler {
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
    public BOSSAPSAPICommonResponse handleClientException(Exception ex) {
        log.error("error log: {}",ex.getMessage(),ex);
        return BOSSAPSAPICommonResponse.baseBuilder()
                .code(10001)
                .message(ex.getMessage())
                .build();
    }
    @ResponseBody
    @ExceptionHandler(value= {NullPointerException.class})
    public BOSSAPSAPICommonResponse handleNullPointerException(Exception ex) {
        log.error("error log: {}",ex.getMessage(),ex);
        return BOSSAPSAPICommonResponse.baseBuilder()
                .code(10001)
                .message("record not found")
                .build();
    }
}