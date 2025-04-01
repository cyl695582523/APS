package com.dksh.hkbcf.controller;

import com.dksh.hkbcf.pojo.APSAPICommonResponse;
import com.dksh.hkbcf.pojo.BOSSAPSAPICommonResponse;
import com.dksh.hkbcf.service.ExitAPIService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
public class ExitController {

    @Autowired
    ExitAPIService exitAPIService;


    // 3.18. CPVACS notify Boss Vehicle Exit ACPB
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotifyBOSSExitRequest{
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String exitDatetime;
        private String bookingId;
        private String parkingRecordId;
        private String remark;
        private String laneCode;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class NotifyBOSSExitResponse extends APSAPICommonResponse {}

    @PostMapping("/notifyBOSSExit")
    public NotifyBOSSExitResponse notifyBOSSExit(@RequestBody NotifyBOSSExitRequest request) {
        return exitAPIService.notifyBOSSExit(request);
    }
}
