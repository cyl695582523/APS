package com.dksh.hkbcf.exception.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderMethodName = "baseBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class MPSClientException extends RuntimeException {
    private Integer status = 0;
    private Integer code = 0;
    private String message = "error";
    private Object data = null;
}
