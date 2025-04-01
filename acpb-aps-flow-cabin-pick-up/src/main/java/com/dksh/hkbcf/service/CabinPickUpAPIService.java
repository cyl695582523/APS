package com.dksh.hkbcf.service;

import com.dksh.hkbcf.controller.CabinPickUpController;

public interface CabinPickUpAPIService {
    CabinPickUpController.EnquiryVehicleStatusResponse enquiryVehicleStatus(CabinPickUpController.EnquiryVehicleStatusRequest request);
    CabinPickUpController.ValidateDriverInfoResponse validateDriverInfo(CabinPickUpController.ValidateDriverInfoRequest request);
    CabinPickUpController.EnquiryParkingFeeResponse enquiryParkingFee(CabinPickUpController.EnquiryParkingFeeRequest request);
    CabinPickUpController.ConfirmPickUpVehicleResponse confirmPickUpVehicle(CabinPickUpController.ConfirmPickUpVehicleRequest request);
    CabinPickUpController.NotifyPickUpCompleteResponse notifyPickUpComplete(CabinPickUpController.NotifyPickUpCompleteRequest request);
}
