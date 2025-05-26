package com.dksh.hkbcf.controller;

import com.dksh.hkbcf.pojo.APSAPICommonResponse;
import com.dksh.hkbcf.service.EntryAPIService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class EntryController {

    @Autowired
    EntryAPIService entryAPIService;

    // 3.3.	CPVACS Enquiry CPA-ICK Information
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnquiryCpaIckInfoRequest{
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String enquiryDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnquiryCpaIckInfoResponse extends APSAPICommonResponse {
        private IckInfo ickInfo;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class IckInfo{
            private String vehicleHongkong;
            private String vehicleMainland;
            private String vehicleMacao;
            private Integer length;
            private Integer width;
            private Integer height;
            private Integer secondSearchFlag;
            private Integer clearanceFlag;
            private String secondSearchEventDate;
            private String secondSearchEventTime;

            // Brian 2025-03-10
            private String bookingId;
            private Integer handicapped;
            private String primaryVehicleRegion;
            
        }
    }
    // 3.3. CPVACS Enquiry CPA-ICK Information
    @PostMapping("/enquiryCpaIckInfo")
    public EnquiryCpaIckInfoResponse enquiryCpaIckInfo(@RequestBody EnquiryCpaIckInfoRequest request) {
        return entryAPIService.enquiryCpaIckInfo(request);
    }

    // 3.5.	CPVACS Update CPA-ICK Information
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCpaIckInfoRequest{
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private Integer length;
        private Integer width;
        private Integer height;
        private Integer secondSearchFlag;
        private Integer clearanceFlag;
        private String secondSearchEventDate;
        private String secondSearchEventTime;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class UpdateCpaIckInfoResponse extends APSAPICommonResponse {}

    @PostMapping("/updateCpaIckInfo")
    public UpdateCpaIckInfoResponse updateCpaIckInfo(@RequestBody UpdateCpaIckInfoRequest request) {
        return entryAPIService.updateCpaIckInfo(request);
    }

    // 3.6.	CPVACS Request MPS Cabin
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestMPSCabinRequest{
        private String parkId;
        private String gateId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private Integer length;
        private Integer width;
        private Integer height;

        private String bookingId;        
        private Integer handicapped;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestMPSCabinResponse extends APSAPICommonResponse {
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private Integer status;
        private String cabinId;
        private String cabinName;
        private String assignedDatetime;
        private String bookingId;
        private String remark;
        // Brian 2025-03-10
        private String primaryVehicleRegion;
    }

    @PostMapping("/requestMPSCabin") //3.6 CPVACS Request MPS Cabin
    public RequestMPSCabinResponse requestMPSCabin(@RequestBody RequestMPSCabinRequest request) {
        return entryAPIService.requestMPSCabin(request);
    }
        
    // 3.7.	CPVACS notify Boss Vehicle Enter ACPB
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotifyBOSSEnterRequest{
        private String bookingId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String remark;
        private String entryDatetime;
        private String parkingRecordId;
        private String laneCode;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class NotifyBOSSEnterResponse extends APSAPICommonResponse {}

    @PostMapping("/notifyBOSSEnter")
    public NotifyBOSSEnterResponse notifyBOSSEnter(@RequestBody NotifyBOSSEnterRequest request) {
        return entryAPIService.notifyBOSSEnter(request);
    }

    // Update CPVACS Cabin Available Status
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCPVCASCabinAvailableStatusRequest {
        private Integer isCabinAvailable;
        private String sysDatetime;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class UpdateCPVCASCabinAvailableStatusResponse extends APSAPICommonResponse {}

    @PostMapping("/updateCPVCASCabinAvailableStatus")
    public UpdateCPVCASCabinAvailableStatusResponse updateCPVCASCabinAvailableStatus(@RequestBody UpdateCPVCASCabinAvailableStatusRequest request) {
        return entryAPIService.updateCPVCASCabinAvailableStatus(request);
    }
}
