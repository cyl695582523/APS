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
public class APSAPICommonResponse {
    @Builder.Default private Integer resultCode = 1;
    @Builder.Default private String resultMessage = "success";
    @Builder.Default private String sysDatetime = TimeUtil.format(Instant.now(), "yyyy-MM-dd HH:mm:ss");
}
