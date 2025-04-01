package com.dksh.hkbcf.pojo;

import com.dksh.hkbcf.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder(builderMethodName = "baseBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class BOSSAPSAPICommonResponse<T> {
    @Builder.Default private Integer code = 0;
    @Builder.Default private String message = "OK";
    @Builder.Default private T data = null;
}
