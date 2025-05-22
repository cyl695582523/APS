package com.dksh.hkbcf.service;

import com.dksh.hkbcf.boss.client.BOSSClient;
import com.dksh.hkbcf.cpvacs.client.CPVACSAuthClient;
import com.dksh.hkbcf.cpvacs.client.CPVACSServiceClient;
import com.dksh.hkbcf.mps.client.MPSClient;
import com.dksh.hkbcf.controller.CabinOtherController;
import com.dksh.hkbcf.util.ObjectMapperUtil;
import com.dksh.hkbcf.util.TimeUtil;
import com.dksh.hkbcf.pojo.APSAPICommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CabinOtherAPIServiceImpl implements CabinOtherAPIService{

    @Autowired
    MPSClient mpsClient;

    @Autowired
    BOSSClient bossClient;

    @Autowired
    CPVACSServiceClient cpvacsServiceClient;

    @Autowired
    CPVACSAuthClient cpvacsAuthClient;

    @Override
    public CabinOtherController.NotifyMPSIntercomTurnedOnResponse notifyMPSIntercomTurnedOn(CabinOtherController.NotifyMPSIntercomTurnedOnRequest req1) {
//        MPSClient.CommonResponse<MPSClient.OpenInterPhoneResponse> res2 = mpsClient.openInterPhone(ObjectMapperUtil.clone(req1, MPSClient.OpenInterPhoneRequest.class));
        return CabinOtherController.NotifyMPSIntercomTurnedOnResponse.builder().build();
    }

    @Override
    public CabinOtherController.RequestCPVACSCabinVehicleInfoResponse requestCPVACSCabinVehicleInfo(CabinOtherController.RequestCPVACSCabinVehicleInfoRequest req1) {
        CPVACSAuthClient.CommonResponse<CPVACSAuthClient.LoginResponse> authRes = cpvacsAuthClient.login(CPVACSAuthClient.LoginRequest.builder()
                .username("demoApp")
                .password("123456")
                .build());

        CPVACSServiceClient.CommonResponse<CPVACSServiceClient.Cpvacs4Response> res2 = cpvacsServiceClient.cpvacs4(ObjectMapperUtil.clone(req1, CPVACSServiceClient.Cpvacs4Request.class), "Bearer "+authRes.getData().getAccessToken());
        return CabinOtherController.RequestCPVACSCabinVehicleInfoResponse.builder()
                .vehicleHongkong(res2.getData().getVehicleHongkong())
                .vehicleMacao(res2.getData().getVehicleMacao())
                .vehicleMainland(res2.getData().getVehicleMainland())
                .capture(res2.getData().getImagePath())
                .captureDatetime(TimeUtil.format(Instant.now(), "yyyy-MM-dd HH:mm:ss"))
                .build();
    }

    @Override
    public CabinOtherController.SendAssignedPickupCabinResponse sendAssignedPickupCabin(CabinOtherController.SendAssignedPickupCabinRequest req1) {
        CPVACSAuthClient.CommonResponse<CPVACSAuthClient.LoginResponse> authRes = cpvacsAuthClient.login(CPVACSAuthClient.LoginRequest.builder()
                .username("demoApp")
                .password("123456")
                .build());
        /*
        {
   "cabinId":"",
   "parkingRecordId":"",
   "bookingId":"",
   "mpsRecordId":"",
   "warehouseId":"",
   "areId":"",
   "vehicleHongkong":"",
   "vehicleMainland":"",
   "vehicleMacao":"",
   "confirmPickupDatetime":1713335471000,
   "parkInDatetime":1713335471000,
   "remark":""
}
         */

        CPVACSServiceClient.Cpvacs5Request req2 = CPVACSServiceClient.Cpvacs5Request.builder()
                .cabinId(req1.getCabinId())
                .parkingRecordId(req1.getParkingRecordId())
                .bookingId(req1.getBookingId())
                .mpsRecordId(req1.getMpsRecordId())
                .warehouseId(req1.getWarehouseId())
                .areaId(req1.getAreaId())
                .vehicleHongkong(req1.getVehicleHongkong())
                .vehicleMacao(req1.getVehicleMacao())
                .vehicleMainland(req1.getVehicleMainland())
                .confirmPickupDatetime(TimeUtil.parseMilli(req1.getConfirmPickupDatetime(), "yyyy-MM-dd HH:mm:ss")+"")
                .parkInDatetime(TimeUtil.parseMilli(req1.getParkInDatetime(), "yyyy-MM-dd HH:mm:ss")+"")
                .remark(req1.getRemark())
                .build();


        CPVACSServiceClient.CommonResponse<CPVACSServiceClient.Cpvacs5Response> res2 = cpvacsServiceClient.cpvacs5(req2, "Bearer "+authRes.getData().getAccessToken());
        return CabinOtherController.SendAssignedPickupCabinResponse.builder().build();
    }

    @Override
    public CabinOtherController.NotifyVehicleRetrieveResponse notifyVehicleRetrieve(CabinOtherController.NotifyVehicleRetrieveRequest req1) {
        BOSSClient.IntApsBoss005Request req2 = ObjectMapperUtil.clone(req1, BOSSClient.IntApsBoss005Request.class);
        // eventId tbc
        req2.setCheckpoint("Cabin");
        req2.setLocation("CabinParking");
        req2.setStatus("retrieval");
        req2.setEventDate(req1.getVehicleRetrieveDatetime().split(" ")[0].replaceAll("-","/"));
        req2.setEventTime(req1.getVehicleRetrieveDatetime().split(" ")[1]);
        req2.setMoreInfo(BOSSClient.IntApsBoss005Request.MoreInfo.builder().cabinNo(req1.getCabinId()).build());
        req2.setEventId(Instant.now().toEpochMilli()+"");
        BOSSClient.CommonResponse<BOSSClient.IntApsBoss005Response> res2 = bossClient.intApsBoss005(req1.getBookingId(), req2);
        return CabinOtherController.NotifyVehicleRetrieveResponse.builder().build();
    }

    @Override
    public CabinOtherController.UpdateDropOffDisplayMsgResponse updateDropOffDisplayMsg(CabinOtherController.UpdateDropOffDisplayMsgRequest req1) {
        CPVACSAuthClient.CommonResponse<CPVACSAuthClient.LoginResponse> authRes = cpvacsAuthClient.login(CPVACSAuthClient.LoginRequest.builder()
                .username("demoApp")
                .password("123456")
                .build());

        // Call CPVACS service to update the 32-inch LCD display message
        CPVACSServiceClient.CommonResponse<CPVACSServiceClient.Cpvacs8Response> res2 = cpvacsServiceClient.cpvacs8(
            CPVACSServiceClient.Cpvacs8Request.builder()
                .cabinId(req1.getCabinId())
                .parkingDisplayId(req1.getParkingDisplayId())
                .build(),
            "Bearer " + authRes.getData().getAccessToken()
        );

        return CabinOtherController.UpdateDropOffDisplayMsgResponse.builder().build();
    }
}
