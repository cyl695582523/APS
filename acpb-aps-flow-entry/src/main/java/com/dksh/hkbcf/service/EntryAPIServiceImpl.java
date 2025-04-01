package com.dksh.hkbcf.service;

import com.dksh.hkbcf.boss.client.BOSSClient;
import com.dksh.hkbcf.mps.client.MPSClient;
import com.dksh.hkbcf.controller.EntryController;
import com.dksh.hkbcf.model.IckVehicle;
import com.dksh.hkbcf.model.IckVehicleRecord;
import com.dksh.hkbcf.pojo.APSAPICommonResponse;
import com.dksh.hkbcf.pojo.BOSSAPSAPICommonResponse;
import com.dksh.hkbcf.repository.IckVehicleRecordRepository;
import com.dksh.hkbcf.repository.IckVehicleRepository;
import com.dksh.hkbcf.util.ObjectMapperUtil;
import com.dksh.hkbcf.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;

@Service
public class EntryAPIServiceImpl implements EntryAPIService{

    @Autowired
    IckVehicleRepository ickVehicleRepository;

    @Autowired
    IckVehicleRecordRepository ickVehicleRecordRepository;

    @Autowired
    MPSClient mpsClient;

    @Autowired
    BOSSClient bossClient;

    @Override
    public EntryController.EnquiryCpaIckInfoResponse enquiryCpaIckInfo(EntryController.EnquiryCpaIckInfoRequest req1) {
        // null
        // ''
        IckVehicle ickVehicle = ickVehicleRepository.findAllICKRecords(
                req1.getVehicleHongkong()==null?"":req1.getVehicleHongkong(),
                req1.getVehicleMainland()==null?"":req1.getVehicleMainland(),
                req1.getVehicleMacao()==null?"":req1.getVehicleMacao()
        );

        return EntryController.EnquiryCpaIckInfoResponse.builder()
                .ickInfo(EntryController.EnquiryCpaIckInfoResponse.IckInfo.builder()
                        .vehicleHongkong(ickVehicle.getVehicleHongkong())
                        .vehicleMacao(ickVehicle.getVehicleMacao())
                        .vehicleMainland(ickVehicle.getVehicleMainland())
                        .length(ickVehicle.getLength())
                        .width(ickVehicle.getWidth())
                        .height(ickVehicle.getHeight())
                        .secondSearchFlag(ickVehicle.getSecondSearchFlag())
                        .clearanceFlag(ickVehicle.getClearanceFlag())
                        .secondSearchEventDate(TimeUtil.format(ickVehicle.getSecondSearchEventDate(),"yyyy-MM-dd"))
                        .secondSearchEventTime(TimeUtil.format(ickVehicle.getSecondSearchEventTime(),"HH:mm:ss"))
                        // Brian 2025-03-10
                        .bookingId(ickVehicle.getBookingId())
                        .handicapped(ickVehicle.getHandicapped())
                        .primaryVehicleRegion(ickVehicle.getPrimaryVehicleRegion())                        
                        .build())
                .build();
    }


    @Override
    public EntryController.UpdateCpaIckInfoResponse updateCpaIckInfo(EntryController.UpdateCpaIckInfoRequest req1) {
        ickVehicleRecordRepository.save(IckVehicleRecord.builder()
                .vehicleHongkong(req1.getVehicleHongkong())
                .vehicleMainland(req1.getVehicleMainland())
                .vehicleMacao(req1.getVehicleMacao())
                .length(req1.getLength())
                .width(req1.getWidth())
                .height(req1.getHeight())
                .secondSearchEventDate(TimeUtil.parseDate(req1.getSecondSearchEventDate(), "yyyy-MM-dd"))
                .secondSearchEventTime(TimeUtil.parseTime(req1.getSecondSearchEventTime(), "HH:mm:ss"))
                .secondSearchFlag(req1.getSecondSearchFlag())
                .clearanceFlag(req1.getClearanceFlag())
                .build());

        return EntryController.UpdateCpaIckInfoResponse.builder().build();
    }

    @Override
    public EntryController.RequestMPSCabinResponse requestMPSCabin(EntryController.RequestMPSCabinRequest req1) {
        MPSClient.CommonResponse<MPSClient.RequestAllocationMpsCabinResponse> res2 = mpsClient.requestAllocationMpsCabin(ObjectMapperUtil.clone(req1, MPSClient.RequestAllocationMpsCabinRequest.class));

        EntryController.RequestMPSCabinResponse res1 = null;

        if(res2.getData()==null){
            res1 = EntryController.RequestMPSCabinResponse.builder().build();
            res1.setResultCode(0);
            res1.setResultMessage(res2.getMsg());
        }
        else
            res1 = ObjectMapperUtil.clone(res2.getData(), EntryController.RequestMPSCabinResponse.class);

        return res1;
    }

    @Override
    public EntryController.NotifyBOSSEnterResponse notifyBOSSEnter(EntryController.NotifyBOSSEnterRequest req1) {
        BOSSClient.IntApsBoss005Request req2 = ObjectMapperUtil.clone(req1, BOSSClient.IntApsBoss005Request.class);
        // eventId and moreInfo.ickNo tbc
        req2.setCheckpoint("BarrierGate");
        req2.setLocation("MainEntryGate");
        req2.setStatus("pass");
        req2.setEventDate(req1.getEntryDatetime().split(" ")[0].replaceAll("-","/"));
        req2.setEventTime(req1.getEntryDatetime().split(" ")[1]);
        req2.setEventId(Instant.now().toEpochMilli()+"");
        BOSSClient.CommonResponse<BOSSClient.IntApsBoss005Response> res2 = null;
        if(req1.getBookingId()!=null) {
            res2 = bossClient.intApsBoss005(req1.getBookingId(), req2);
        }
        return EntryController.NotifyBOSSEnterResponse.builder().build();
    }
}
