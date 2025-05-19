package com.dksh.hkbcf.controller;

import com.dksh.hkbcf.pojo.APSAPICommonResponse;
import com.dksh.hkbcf.service.OtherAPIService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class OtherController {

    @Autowired
    OtherAPIService otherAPIService;

    // 3.27. CPVACS Update MPS Gate Status
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateMPSGateStatusRequest{
        private String gateId;
        private String gateUpdateDatetime;
        private Integer gateStatus;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String bookingId;
        private String parkingRecordId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class UpdateMPSGateStatusResponse extends APSAPICommonResponse {}

    @PostMapping("/updateMPSGateStatus")
    public UpdateMPSGateStatusResponse updateMPSGateStatus(@RequestBody UpdateMPSGateStatusRequest request) {
        return otherAPIService.updateMPSGateStatus(request);
    }

    // 3.28. MPS Request CPVACS CAS/LPRs status
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestCPVACSCasInfoRequest{
        private String deviceId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestCPVACSCasInfoResponse extends APSAPICommonResponse {
        private Integer totalCount;
        private List<DeviceInfo> deviceInfo;
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class DeviceInfo{
            private String deviceId;
            private Integer deviceStatus;
            private String deviceCheckDatetime;
        }
    }

    @PostMapping("/requestCPVACSCasInfo")
    public RequestCPVACSCasInfoResponse requestCPVACSCasInfo(@RequestBody RequestCPVACSCasInfoRequest request) {
        return otherAPIService.requestCPVACSCasInfo(request);
    }

}