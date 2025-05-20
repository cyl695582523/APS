package com.dksh.hkbcf.service;

import com.dksh.hkbcf.boss.client.BOSSClient;
import com.dksh.hkbcf.cpvacs.client.CPVACSAuthClient;
import com.dksh.hkbcf.cpvacs.client.CPVACSServiceClient;
import com.dksh.hkbcf.mps.client.MPSClient;
import com.dksh.hkbcf.controller.CabinDropOffController;
import com.dksh.hkbcf.util.ObjectMapperUtil;
import com.dksh.hkbcf.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.Instant;

@Slf4j
@Service
public class CabinDropOffAPIServiceImpl implements CabinDropOffAPIService{

    @Autowired
    MPSClient mpsClient;

    @Autowired
    BOSSClient bossClient;

    @Autowired
    CPVACSServiceClient cpvacsServiceClient;

    @Autowired
    CPVACSAuthClient cpvacsAuthClient;

    @Override
    public CabinDropOffController.NotifyCPVACSVehicleArrivedCabinDoorResponse notifyCPVACSVehicleArrivedCabinDoor(CabinDropOffController.NotifyCPVACSVehicleArrivedCabinDoorRequest req1) {
        CPVACSAuthClient.CommonResponse<CPVACSAuthClient.LoginResponse> authRes = cpvacsAuthClient.login(CPVACSAuthClient.LoginRequest.builder()
                .username("demoApp")
                .password("123456")
        .build());

        CPVACSServiceClient.Cpvacs1Request req2 = CPVACSServiceClient.Cpvacs1Request.builder()
                .cabinId(req1.getCabinId())
                .vehicleStatus(req1.getVehicleStatus())
                .vehicleBottomHeight(req1.getVehicleBottomHeight())
                .loop(req1.getLoop())
                .arriveCabinDoorDatetime(TimeUtil.parseMilli(req1.getArriveCabinDoorDatetime(), "yyyy-MM-dd HH:mm:ss")+"")
                .build();

        CPVACSServiceClient.CommonResponse<CPVACSServiceClient.Cpvacs1Response> res2 = cpvacsServiceClient.cpvacs1(req2, "Bearer "+authRes.getData().getAccessToken());
        return CabinDropOffController.NotifyCPVACSVehicleArrivedCabinDoorResponse.builder().build();
    }

    @Override
    public CabinDropOffController.SendMPSArrivedCabinVehicleInfoResponse sendMPSArrivedCabinVehicleInfo(CabinDropOffController.SendMPSArrivedCabinVehicleInfoRequest req1) {
//        MPSClient.CommonResponse<MPSClient.SendMPSArrivedCabinVehicleInfoResponse> res2 = mpsClient.sendMPSArrivedCabinVehicleInfo(ObjectMapperUtil.clone(req1, MPSClient.SendMPSArrivedCabinVehicleInfoRequest.class));
        return CabinDropOffController.SendMPSArrivedCabinVehicleInfoResponse.builder().build();
    }

    @Override
    public CabinDropOffController.EnquiryMPSCabinStatusResponse enquiryMPSCabinStatus(CabinDropOffController.EnquiryMPSCabinStatusRequest req1) {
        MPSClient.CommonResponse<MPSClient.EnquiryMPSCabinStatusResponse> res2 = mpsClient.enquiryMPSCabinStatus(ObjectMapperUtil.clone(req1, MPSClient.EnquiryMPSCabinStatusRequest.class));
        // Call MPS 2.13 API to get primary vehicle region
        MPSClient.CommonResponse<MPSClient.EnquiryPrimaryVehicleResponse> primaryVehicleRes = mpsClient.enquiryPrimaryVehicle(
            MPSClient.EnquiryPrimaryVehicleRequest.builder()
                .bookingId(res2.getData().getBookingId())
                .vehicleHongkong(res2.getData().getAssignedVehicleHongkong())
                .vehicleMainland(res2.getData().getAssignedVehicleMainland())
                .vehicleMacao(res2.getData().getAssignedVehicleMacao())
                .build()
        );

        CabinDropOffController.EnquiryMPSCabinStatusResponse response = ObjectMapperUtil.clone(res2.getData(), CabinDropOffController.EnquiryMPSCabinStatusResponse.class);
        if (primaryVehicleRes != null && primaryVehicleRes.getData() != null) {
            response.setPrimaryVehicleRegion(primaryVehicleRes.getData().getPrimaryVehicleRegion());
        }
        return response;
        
    }

    @Override
    public CabinDropOffController.InformMPSCabinReadyResponse informMPSCabinReady(CabinDropOffController.InformMPSCabinReadyRequest req1) {
//        MPSClient.CommonResponse<MPSClient.OpenParkingEntryResponse> res2 = mpsClient.openParkingEntry(ObjectMapperUtil.clone(req1, MPSClient.OpenParkingEntryRequest.class));
        MPSClient.CommonResponse<MPSClient.SendMPSArrivedCabinVehicleInfoResponse> res2 = mpsClient.sendMPSArrivedCabinVehicleInfo(ObjectMapperUtil.clone(req1, MPSClient.SendMPSArrivedCabinVehicleInfoRequest.class));

        BOSSClient.IntApsBoss005Request req3 = ObjectMapperUtil.clone(req1, BOSSClient.IntApsBoss005Request.class);
        req3.setCheckpoint("Cabin");
        req3.setLocation("CabinParking");
        req3.setStatus("arrival");

        req3.setEventDate(TimeUtil.format(Instant.now(), "yyyy/MM/dd"));
        req3.setEventTime(TimeUtil.format(Instant.now(), "HH:mm:ss"));

        req3.setMoreInfo(BOSSClient.IntApsBoss005Request.MoreInfo.builder().cabinNo(req1.getCabinId()).build());
        req3.setEventId(Instant.now().toEpochMilli()+"");
        BOSSClient.CommonResponse<BOSSClient.IntApsBoss005Response> res3 = bossClient.intApsBoss005(req1.getBookingId(), req3);
        // Call MPS 2.13 API to get primary vehicle region
        MPSClient.CommonResponse<MPSClient.EnquiryPrimaryVehicleResponse> primaryVehicleRes = mpsClient.enquiryPrimaryVehicle(
            MPSClient.EnquiryPrimaryVehicleRequest.builder()
                .bookingId(req1.getBookingId())
                .vehicleHongkong(req1.getVehicleHongkong())
                .vehicleMainland(req1.getVehicleMainland())
                .vehicleMacao(req1.getVehicleMacao())
                .build()
        );

        CabinDropOffController.InformMPSCabinReadyResponse response = ObjectMapperUtil.clone(req1, CabinDropOffController.InformMPSCabinReadyResponse.class);
        if (primaryVehicleRes != null && primaryVehicleRes.getData() != null) {
            response.setPrimaryVehicleRegion(primaryVehicleRes.getData().getPrimaryVehicleRegion());
        }
        return response;
    // return ObjectMapperUtil.clone(req1, CabinDropOffController.InformMPSCabinReadyResponse.class);
    }

    @Override
    public CabinDropOffController.NotifyDropOffCompleteResponse notifyDropOffComplete(CabinDropOffController.NotifyDropOffCompleteRequest req1) {
        CPVACSAuthClient.CommonResponse<CPVACSAuthClient.LoginResponse> authRes = cpvacsAuthClient.login(CPVACSAuthClient.LoginRequest.builder()
                .username("demoApp")
                .password("123456")
                .build());

        CPVACSServiceClient.Cpvacs2Request req2 = CPVACSServiceClient.Cpvacs2Request.builder()
                .cabinId(req1.getCabinId())
                .parkingRecordId(req1.getParkingRecordId())
                .bookingId(req1.getBookingId())
                .vehicleHongkong(req1.getVehicleHongkong())
                .vehicleMacao(req1.getVehicleMacao())
                .vehicleMainland(req1.getVehicleMainland())
                .completeDropOffTime(TimeUtil.parseMilli(req1.getCompleteDropOffTime(), "yyyy-MM-dd HH:mm:ss")+"")
                .remark(req1.getRemark())
                .build();


        CPVACSServiceClient.CommonResponse<CPVACSServiceClient.Cpvacs2Response> res2 = cpvacsServiceClient.cpvacs2(req2, "Bearer "+authRes.getData().getAccessToken());

        BOSSClient.IntApsBoss005Request req3 = ObjectMapperUtil.clone(req1, BOSSClient.IntApsBoss005Request.class);
        // eventId tbc
        req3.setCheckpoint("Cabin");
        req3.setLocation("CabinParking");
        req3.setStatus("park");
        req3.setEventDate(req1.getCompleteDropOffTime().split(" ")[0].replaceAll("-","/"));
        req3.setEventTime(req1.getCompleteDropOffTime().split(" ")[1]);
        req3.setMoreInfo(BOSSClient.IntApsBoss005Request.MoreInfo.builder().cabinNo(req1.getCabinId()).build());
        req3.setEventId(Instant.now().toEpochMilli()+"");
        BOSSClient.CommonResponse<BOSSClient.IntApsBoss005Response> res3 = bossClient.intApsBoss005(req1.getBookingId(), req3);
        return CabinDropOffController.NotifyDropOffCompleteResponse.builder().build();
    }

}
