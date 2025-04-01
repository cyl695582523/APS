package com.dksh.hkbcf.controller;

import com.dksh.hkbcf.pojo.APSAPICommonResponse;
import com.dksh.hkbcf.service.CabinDropOffAPIService;
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
public class CabinDropOffController {

    @Autowired
    CabinDropOffAPIService cabinDropOffAPIService;

    // 3.8.	MPS Notify CPVACS Vehicle Arrived at Drop Off Cabin Door
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotifyCPVACSVehicleArrivedCabinDoorRequest{
        private String cabinId;
        private Integer loop;
        private Integer vehicleBottomHeight;
        private Integer vehicleStatus;
        private String arriveCabinDoorDatetime;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class NotifyCPVACSVehicleArrivedCabinDoorResponse extends APSAPICommonResponse {}

    @PostMapping("/notifyCPVACSVehicleArrivedCabinDoor")
    public NotifyCPVACSVehicleArrivedCabinDoorResponse notifyCPVACSVehicleArrivedCabinDoor(@RequestBody NotifyCPVACSVehicleArrivedCabinDoorRequest request) {
        return cabinDropOffAPIService.notifyCPVACSVehicleArrivedCabinDoor(request);
    }

    // 3.9.	CPVACS Send MPS Arrived Cabin Vehicle Information
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendMPSArrivedCabinVehicleInfoRequest{
        private String cabinId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String lpnCaptureDatetime;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class SendMPSArrivedCabinVehicleInfoResponse extends APSAPICommonResponse {}

    @PostMapping("/sendMPSArrivedCabinVehicleInfo")
    public SendMPSArrivedCabinVehicleInfoResponse sendMPSArrivedCabinVehicleInfo(@RequestBody SendMPSArrivedCabinVehicleInfoRequest request) {
        return cabinDropOffAPIService.sendMPSArrivedCabinVehicleInfo(request);
    }

    // 3.10. CPVACS Enquiry MPS Cabin Status
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnquiryMPSCabinStatusRequest{
        private String cabinId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnquiryMPSCabinStatusResponse extends APSAPICommonResponse {
        private String cabinId;
        private String assignedVehicleHongkong;
        private String assignedVehicleMainland;
        private String assignedVehicleMacao;
        private String assignedDatetime;
        private String bookingId;
    }

    @PostMapping("/enquiryMPSCabinStatus")
    public EnquiryMPSCabinStatusResponse enquiryMPSCabinStatus(@RequestBody EnquiryMPSCabinStatusRequest request) {
        return cabinDropOffAPIService.enquiryMPSCabinStatus(request);
    }


    // 3.11. CPVACS Inform MPS Ready Cabin
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InformMPSCabinReadyRequest{
        private String cabinId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String bookingId;
        private String parkingRecordId;
        private String parkInDatetime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InformMPSCabinReadyResponse extends APSAPICommonResponse {
        private String cabinId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String bookingId;
        private String parkingRecordId;
    }

    @PostMapping("/informMPSCabinReady")
    public InformMPSCabinReadyResponse informMPSCabinReady(@RequestBody InformMPSCabinReadyRequest request) {
        return cabinDropOffAPIService.informMPSCabinReady(request);
    }

    // 3.12. MPS Notify CPVACS/BOSS MPS Drop Off Complete
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotifyDropOffCompleteRequest{
        private String cabinId;
        private String bookingId;
        private String parkingRecordId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String completeDropOffTime;
        private String remark;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class NotifyDropOffCompleteResponse extends APSAPICommonResponse {}

    @PostMapping("/notifyDropOffComplete")
    public NotifyDropOffCompleteResponse notifyDropOffComplete(@RequestBody NotifyDropOffCompleteRequest request) {
        return cabinDropOffAPIService.notifyDropOffComplete(request);
    }
}
