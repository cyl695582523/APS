package com.dksh.hkbcf.service;

import com.dksh.hkbcf.boss.client.BOSSClient;
import com.dksh.hkbcf.controller.BOSSAPSController;
import com.dksh.hkbcf.cpvacs.client.CPVACSAuthClient;
import com.dksh.hkbcf.cpvacs.client.CPVACSServiceClient;
import com.dksh.hkbcf.mps.client.MPSClient;
import com.dksh.hkbcf.pojo.BOSSAPSAPICommonResponse;
import com.dksh.hkbcf.util.ObjectMapperUtil;
import com.dksh.hkbcf.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BOSSAPSAPIServiceImpl implements BOSSAPSAPIService{

    @Autowired
    BOSSClient bossClient;

    @Autowired
    MPSClient mpsClient;

    @Autowired
    CPVACSServiceClient cpvacsServiceClient;

    @Autowired
    CPVACSAuthClient cpvacsAuthClient;

    @Override
    public BOSSAPSAPICommonResponse<BOSSAPSController.BulkExportBookingResponse> bulkExportBooking(BOSSAPSController.BulkExportBookingRequest req1) {
        /*
         * 1. Retrieve booking info from MPS
         * 2. Retrieve entry and exit record from CPB           
         * 3. Retrieve PVR from MPS
         * 4. Response
         */
        if(req1.getVehicleHongkong()==null) req1.setVehicleHongkong("");
        if(req1.getVehicleMainland()==null) req1.setVehicleMainland("");
        if(req1.getVehicleMacao()==null) req1.setVehicleMacao("");

        MPSClient.QueryReservationVehicleInfoRequest req2 = MPSClient.QueryReservationVehicleInfoRequest.builder()
                .date(req1.getDate().replaceAll("/","-"))
                .offset(0)
                .batchCount(65535)
                .vehicleHongkong(req1.getVehicleHongkong())
                .vehicleMainland(req1.getVehicleMainland())
                .vehicleMacao(req1.getVehicleMacao())
                .build();

        // calling 2.4.查询预约车辆活动信息 /queryReservationVehicleInfo 
        MPSClient.CommonResponse<MPSClient.QueryReservationVehicleInfoResponse> res2 = mpsClient.queryReservationVehicleInfo(req2);

        // return empty response if no booking info
        if(res2.getData().getBookingInfo()==null || res2.getData().getBookingInfo().size()==0){
            return BOSSAPSAPICommonResponse.<BOSSAPSController.BulkExportBookingResponse>baseBuilder()
                    .data(BOSSAPSController.BulkExportBookingResponse.builder()
//                        .total(res2.getData().getTotalCount())
//                        .events(eventList)
                            .total(0)
                            .events(new ArrayList<>())
                            .build())
                    .build();
        }

        // call cpb for entry and exit record and return to BOSS
        CPVACSAuthClient.CommonResponse<CPVACSAuthClient.LoginResponse> authRes = cpvacsAuthClient.login(CPVACSAuthClient.LoginRequest.builder()
                .username("demoApp")
                .password("123456") 
                .build());

        CPVACSServiceClient.ListResponse<CPVACSServiceClient.Cpvacs7aResponse> res3 = cpvacsServiceClient.cpvacs7a(res2.getData().getBookingInfo().stream().map(bookingInfo -> bookingInfo.getBookingId()).distinct().collect(Collectors.joining(",")), "Bearer "+authRes.getData().getAccessToken());

        CPVACSServiceClient.ListResponse<CPVACSServiceClient.Cpvacs7bResponse> res4 = cpvacsServiceClient.cpvacs7b(res2.getData().getBookingInfo().stream().map(bookingInfo -> bookingInfo.getBookingId()).distinct().collect(Collectors.joining(",")), "Bearer "+authRes.getData().getAccessToken());

        //count
        req1.getOffset(); // boss required
        req1.getBatchCount(); // boss required
        res2.getData().getTotalCount(); // mps returned
        res3.getData().size(); // cpb returned
        res4.getData().size(); // cpb returned

        Integer total = res2.getData().getTotalCount()+res3.getData().size()+res4.getData().size();

        // Get PVR for each booking
        Map<String, String> bookingPvrMap = new HashMap<>();
        for (MPSClient.QueryReservationVehicleInfoResponse.BookingInfo bookingInfo : res2.getData().getBookingInfo()) {
            MPSClient.CommonResponse<MPSClient.EnquiryPrimaryVehicleResponse> primaryVehicleRes = mpsClient.enquiryPrimaryVehicle(
                MPSClient.EnquiryPrimaryVehicleRequest.builder()
                    .bookingId(bookingInfo.getBookingId())
                    .vehicleHongkong(bookingInfo.getVehicleHongkong())
                    .vehicleMainland(bookingInfo.getVehicleMainland())
                    .vehicleMacao(bookingInfo.getVehicleMacao())
                    .build()
            );
            if (primaryVehicleRes != null && primaryVehicleRes.getData() != null) {
                bookingPvrMap.put(bookingInfo.getBookingId(), primaryVehicleRes.getData().getPrimaryVehicleRegion());
            }
        }

        List<BOSSAPSController.BulkExportBookingResponse.Event> eventList =
                res2.getData().getBookingInfo().stream().map(bookingInfo -> {
                    // return ObjectMapperUtil.clone(bookingInfo, ExitController.BulkExportBookingResponse.Event.class);
                    return BOSSAPSController.BulkExportBookingResponse.Event.builder()
                            .vehicleHongkong(bookingInfo.getVehicleHongkong())
                            .vehicleMainland(bookingInfo.getVehicleMainland())
                            .vehicleMacao(bookingInfo.getVehicleMacao())
                            .bookingId(bookingInfo.getBookingId())
                            .parkingRecordId(bookingInfo.getParkingRecordId())
                            .mpsRecordId(bookingInfo.getMpsRecordId())
                            .status(bookingInfo.getStatus())
                            .checkpoint(bookingInfo.getCheckpoint())
                            .location(bookingInfo.getLocation())
                            .remark(bookingInfo.getRemark())
                            .eventDate(bookingInfo.getEventDate()==null?null:bookingInfo.getEventDate().replaceAll("-","/").split(" ")[0])
                            .eventTime(bookingInfo.getEventTime())
                            /*                                    .moreInfo(ExitController.BulkExportBookingResponse.Event.MoreInfo.builder()
                                            .cabinNo(bookingInfo.getMoreInfo().getCabinNo())
                                            .ickNo(bookingInfo.getMoreInfo().getIckNo())
                                            .build())*/                                            
                            .primaryVehicleRegion(bookingPvrMap.get(bookingInfo.getBookingId()))
                            .build();
                }).collect(Collectors.toList());


        eventList.addAll(res3.getData().stream().map(cpbEntryBookingInfo->{
            BOSSAPSController.BulkExportBookingResponse.Event event = eventList.stream().filter(mpsBookingInfo->{
               return cpbEntryBookingInfo.getBookingId().equals(mpsBookingInfo.getBookingId());
            }).findFirst().orElse(null);

            if(event==null) return null;

            return BOSSAPSController.BulkExportBookingResponse.Event.builder()
                    .vehicleHongkong(event.getVehicleHongkong())
                    .vehicleMainland(event.getVehicleMainland())
                    .vehicleMacao(event.getVehicleMacao())
                    .bookingId(event.getBookingId())
                    .parkingRecordId(event.getParkingRecordId())
                    .mpsRecordId(event.getMpsRecordId())
                    .status("pass")
                    .checkpoint("BarrierGate")
                    .location("BarrierGate21")
                    .remark(event.getRemark())
                    .eventDate(TimeUtil.formatMilli(cpbEntryBookingInfo.getEntryTime()==null?cpbEntryBookingInfo.getArrivedTime():cpbEntryBookingInfo.getEntryTime(), "yyyy/MM/dd"))
                    .eventTime(TimeUtil.formatMilli(cpbEntryBookingInfo.getEntryTime()==null?cpbEntryBookingInfo.getArrivedTime():cpbEntryBookingInfo.getEntryTime(), "HH:mm:ss"))
                    // Brian 2025-05-15 
                    .primaryVehicleRegion(event.getPrimaryVehicleRegion())
                    .build();
        }).toList());

        eventList.addAll(res4.getData().stream().map(cpbExitBookingInfo->{
            BOSSAPSController.BulkExportBookingResponse.Event event = eventList.stream().filter(mpsBookingInfo->{
               if(mpsBookingInfo==null) return false;
               return cpbExitBookingInfo.getBookingId().equals(mpsBookingInfo.getBookingId());
            }).findFirst().orElse(null);

            if(event==null) return null;

            String location = "MainExitGate";
            if(cpbExitBookingInfo.getLaneCode()!=null && cpbExitBookingInfo.getLaneCode().equals("exit_0")) location = "MainExitGate0";
            else if(cpbExitBookingInfo.getLaneCode()!=null && cpbExitBookingInfo.getLaneCode().equals("exit_1")) location = "MainExitGate1";
            else if(cpbExitBookingInfo.getLaneCode()!=null && cpbExitBookingInfo.getLaneCode().equals("exit_2")) location = "MainExitGate2";

            return BOSSAPSController.BulkExportBookingResponse.Event.builder()
                    .vehicleHongkong(event.getVehicleHongkong())
                    .vehicleMainland(event.getVehicleMainland())
                    .vehicleMacao(event.getVehicleMacao())
                    .bookingId(event.getBookingId())
                    .parkingRecordId(event.getParkingRecordId())
                    .mpsRecordId(event.getMpsRecordId())
                    .status("pass")
                    .checkpoint("BarrierGate")
//                    .location("MainExitGate")
                    .location(location)
                    .remark(event.getRemark())
                    .eventDate(TimeUtil.formatMilli(cpbExitBookingInfo.getExitTime()==null?cpbExitBookingInfo.getArrivedTime():cpbExitBookingInfo.getExitTime(), "yyyy/MM/dd"))
                    .eventTime(TimeUtil.formatMilli(cpbExitBookingInfo.getExitTime()==null?cpbExitBookingInfo.getArrivedTime():cpbExitBookingInfo.getExitTime(), "HH:mm:ss"))
                    .primaryVehicleRegion(event.getPrimaryVehicleRegion())
                    .build();
        }).toList());

        Integer code = 0;
        if(res2.getCode()==200) code = 0;
        else if(res2.getCode()==500) code = 10001;
        else code = res2.getCode();

        return BOSSAPSAPICommonResponse.<BOSSAPSController.BulkExportBookingResponse>baseBuilder()
                .code(code)
                .message(res2.getMsg())
                .data(BOSSAPSController.BulkExportBookingResponse.builder()
//                        .total(res2.getData().getTotalCount())
//                        .events(eventList)
                        .total(total)
                        .events(eventList.stream().skip(req1.getOffset()).limit(req1.getBatchCount()).collect(Collectors.toList()))
                        .build())
                .build();
    }

    @Override
    public BOSSAPSAPICommonResponse<BOSSAPSController.ChangeBookingResponse> changeBooking(BOSSAPSController.ChangeBookingRequest req1) {
        req1.setParkingFrom(req1.getParkingFrom().replaceAll("/","-"));
        req1.setParkingTo(req1.getParkingTo().replaceAll("/","-"));

        MPSClient.ChangeBookingInfoRequest req2 = ObjectMapperUtil.clone(req1, MPSClient.ChangeBookingInfoRequest.class);
/*

        String status = null;
        if(req1.getStatus()!=null && req1.getStatus().equals("add"))
            status = "2";
        else if(req1.getStatus()!=null && req1.getStatus().equals("update"))
            status = "1";
        else if(req1.getStatus()!=null && req1.getStatus().equals("cancel"))
            status = "0";

        req2.setStatus(status);
*/

        req2.setLastModifiedTime(req1.getLastModifiedTime().replaceAll("-","/"));
        req2.setParkingToDatetime(req1.getParkingTo());

        MPSClient.CommonResponse<MPSClient.ChangeBookingInfoResponse> res2 = mpsClient.changeBookingInfo(req2);

        Integer code = 0;
        if(res2.getCode()==200) code = 0;
        else if(res2.getCode()==500) code = 10001;
        else code = res2.getCode();

        return BOSSAPSAPICommonResponse.<BOSSAPSController.ChangeBookingResponse>baseBuilder()
                .code(code)
                .message(res2.getMsg())
                .data(BOSSAPSController.ChangeBookingResponse.builder()
                        .bookingId(res2.getData().getBookingId()).build())
                .build();
    }
}
