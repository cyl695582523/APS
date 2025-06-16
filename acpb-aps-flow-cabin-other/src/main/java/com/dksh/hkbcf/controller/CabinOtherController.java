package com.dksh.hkbcf.controller;

import com.dksh.hkbcf.pojo.APSAPICommonResponse;
import com.dksh.hkbcf.service.CabinOtherAPIService;
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
public class CabinOtherController {

    @Autowired
    CabinOtherAPIService cabinOtherAPIService;

    // 3.22. CPVACS Notify MPS Intercom Turned on
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotifyMPSIntercomTurnedOnRequest{
        private String deviceId;
        private String cabinId;
        private String turnOnDatetime;
    }
    @Data
    @Builder
    @AllArgsConstructor
    public static class NotifyMPSIntercomTurnedOnResponse extends APSAPICommonResponse {}

    @PostMapping("/notifyMPSIntercomTurnedOn")
    public NotifyMPSIntercomTurnedOnResponse notifyMPSIntercomTurnedOn(@RequestBody NotifyMPSIntercomTurnedOnRequest request) {
        return cabinOtherAPIService.notifyMPSIntercomTurnedOn(request);
    }

    // 3.23. MPS Request CPVACS Cabin Vehicle Information
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestCPVACSCabinVehicleInfoRequest{
        private String deviceId;
        private String cabinId;
        private String warehouseId;
        private String areaId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestCPVACSCabinVehicleInfoResponse extends APSAPICommonResponse{
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String capture;
        private String captureDatetime;

    }

    @PostMapping("/requestCPVACSCabinVehicleInfo")
    public RequestCPVACSCabinVehicleInfoResponse requestCPVACSCabinVehicleInfo(@RequestBody RequestCPVACSCabinVehicleInfoRequest request) {
        return cabinOtherAPIService.requestCPVACSCabinVehicleInfo(request);
    }

    // 3.24	MPS Send CPVACS Assigned Pick Up Cabin Information
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendAssignedPickupCabinRequest{
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String confirmPickupDatetime;
        private String bookingId;
        private String parkingRecordId;
        private String mpsRecordId;
        private String cabinId;
        private String warehouseId;
        private String areaId;
        private String parkInDatetime;
        private String remark;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class SendAssignedPickupCabinResponse extends APSAPICommonResponse {}

    @PostMapping("/sendAssignedPickupCabin")
    public SendAssignedPickupCabinResponse sendAssignedPickupCabin(@RequestBody SendAssignedPickupCabinRequest request) {
        return cabinOtherAPIService.sendAssignedPickupCabin(request);
    }

    // 3.25. MPS Notify BOSS Vehicle Retrieve from MPS
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotifyVehicleRetrieveRequest{
        private String cabinId;
        private String bookingId;
        private String parkingRecordId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String vehicleRetrieveDatetime;
        private String remark;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class NotifyVehicleRetrieveResponse extends APSAPICommonResponse {}

    @PostMapping("/notifyVehicleRetrieve")
    public NotifyVehicleRetrieveResponse notifyVehicleRetrieve(@RequestBody NotifyVehicleRetrieveRequest request) {
        return cabinOtherAPIService.notifyVehicleRetrieve(request);
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateDropOffDisplayMsgRequest {
        private String cabinId;
        private String parkingDisplayId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class UpdateDropOffDisplayMsgResponse extends APSAPICommonResponse {}
    // 3.31. MPS Update Guidance Monitor Outside Parking Cabin Message
    @PostMapping("/updateDropOffDisplayMsg")
    public UpdateDropOffDisplayMsgResponse updateDropOffDisplayMsg(@RequestBody UpdateDropOffDisplayMsgRequest request) {
        return cabinOtherAPIService.updateDropOffDisplayMsg(request);
    }


}