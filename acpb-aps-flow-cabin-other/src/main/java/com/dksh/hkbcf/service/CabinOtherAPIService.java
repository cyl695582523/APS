package com.dksh.hkbcf.service;

import com.dksh.hkbcf.controller.CabinOtherController;

public interface CabinOtherAPIService {
     CabinOtherController.NotifyMPSIntercomTurnedOnResponse notifyMPSIntercomTurnedOn(CabinOtherController.NotifyMPSIntercomTurnedOnRequest request);
     CabinOtherController.RequestCPVACSCabinVehicleInfoResponse requestCPVACSCabinVehicleInfo(CabinOtherController.RequestCPVACSCabinVehicleInfoRequest request);
     CabinOtherController.SendAssignedPickupCabinResponse sendAssignedPickupCabin(CabinOtherController.SendAssignedPickupCabinRequest request);
     CabinOtherController.NotifyVehicleRetrieveResponse notifyVehicleRetrieve(CabinOtherController.NotifyVehicleRetrieveRequest request);

     // 3.30  MPS update Guidance Monitor Outside Parking Cabin Message
     CabinOtherController.UpdateDropOffDisplayMsgResponse updateDropOffDisplayMsg(CabinOtherController.UpdateDropOffDisplayMsgRequest request);
     
}
