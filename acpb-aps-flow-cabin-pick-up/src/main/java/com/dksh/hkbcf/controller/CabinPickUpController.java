package com.dksh.hkbcf.controller;

import com.dksh.hkbcf.pojo.APSAPICommonResponse;
import com.dksh.hkbcf.service.CabinPickUpAPIService;
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

@Slf4j
@RestController
public class CabinPickUpController {

    @Autowired
    CabinPickUpAPIService cabinPickUpAPIService;

    // 3.13. CPVACS Enquiry MPS Vehicle Status
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnquiryVehicleStatusRequest{
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String bookingId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnquiryVehicleStatusResponse extends APSAPICommonResponse {
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String cabinId;
        private String mpsRecordId;
        private String warehouseId;
        private String areaId;
        private String vehicleImgUrl;
        private String vehicleImg;
        private String parkingRecordId;
        private String parkInDatetime;
        private String remark;
        private String bookingId;

        // Brian 2025-05-12 3.13
        private String parkingFrom;
        private String parkingTo;
        private String primaryVehicleRegion;
    }
    // 3.13. CPVACS Enquiry MPS Vehicle Status
    @PostMapping("/enquiryVehicleStatus")
    public EnquiryVehicleStatusResponse enquiryVehicleStatus(@RequestBody EnquiryVehicleStatusRequest request) {
        return cabinPickUpAPIService.enquiryVehicleStatus(request);
    }

    // 3.28. MPS Request BOSS Validate Driver Info
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidateDriverInfoRequest{
        private String bookingId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String identifyType;
        private String identifyNumber;
        private String eventDate;
        private String eventTime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidateDriverInfoResponse extends APSAPICommonResponse {
        private String validate;
    }

    @PostMapping("/requestBOSSValidateDriverInfo")
    public ValidateDriverInfoResponse validateDriverInfo(@RequestBody ValidateDriverInfoRequest request) {
        return cabinPickUpAPIService.validateDriverInfo(request);
    }


    // 3.14. CPVACS Enquiry Vehicle Outstanding Parking Fee
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnquiryParkingFeeRequest{
        private String bookingId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String checkoutDatetime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnquiryParkingFeeResponse extends APSAPICommonResponse {
        private BigDecimal oustandingBalance;
    }

    @PostMapping("/enquiryParkingFee")
    public EnquiryParkingFeeResponse enquiryParkingFee(@RequestBody EnquiryParkingFeeRequest request) {
        return cabinPickUpAPIService.enquiryParkingFee(request);
    }


    // 3.15. CPVACS Confirm MPS Pick Up Vehicle
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConfirmPickUpVehicleRequest{
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String confirmPickupDatetime;
        private String bookingId;
        private String parkingRecordId;
        private String mpsRecordId;
        private String remark;
        private BigDecimal amount;
        private Integer paymentStatus;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConfirmPickUpVehicleResponse extends APSAPICommonResponse {
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String cabinId;
        private String mpsRecordId;
        private String warehouseId;
        private String areaId;
        private String vehicleImgUrl;
        private String vehicleImg;
        private String parkingRecordId;
        private String parkInDateTime;
        private Integer vehicleStatus;
        private String remark;
        private String bookingId;        
        // Brian 2025-05-12 3.15
        private String primaryVehicleRegion;
    }
  // 3.15. CPVACS Confirm MPS Pick Up Vehicle
    @PostMapping("/confirmPickUpVehicle")
    public ConfirmPickUpVehicleResponse confirmPickUpVehicle(@RequestBody ConfirmPickUpVehicleRequest request) {
        return cabinPickUpAPIService.confirmPickUpVehicle(request);
    }

    // 3.16. MPS Notify CPVACS/BOSS MPS Pick Up Complete
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotifyPickUpCompleteRequest{
        private String cabinId;
        private String bookingId;
        private String parkingRecordId;
        private String mpsRecordId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String completePickUpTime;
        private String remark;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class NotifyPickUpCompleteResponse extends APSAPICommonResponse {}

    @PostMapping("/notifyPickUpComplete")
    public NotifyPickUpCompleteResponse notifyPickUpComplete(@RequestBody NotifyPickUpCompleteRequest request) {
        return cabinPickUpAPIService.notifyPickUpComplete(request);
    }
}
