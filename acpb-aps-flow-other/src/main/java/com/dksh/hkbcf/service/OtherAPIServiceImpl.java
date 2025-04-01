package com.dksh.hkbcf.service;

import com.dksh.hkbcf.cpvacs.client.CPVACSAuthClient;
import com.dksh.hkbcf.cpvacs.client.CPVACSServiceClient;
import com.dksh.hkbcf.enums.DeviceList;
import com.dksh.hkbcf.exception.exception.ClientException;
import com.dksh.hkbcf.mps.client.MPSClient;
import com.dksh.hkbcf.controller.OtherController;
import com.dksh.hkbcf.util.ObjectMapperUtil;
import com.dksh.hkbcf.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Collection;

@Service
public class OtherAPIServiceImpl implements OtherAPIService {

    @Autowired
    MPSClient mpsClient;

    @Autowired
    CPVACSServiceClient cpvacsServiceClient;

    @Autowired
    CPVACSAuthClient cpvacsAuthClient;

    @Override
    public OtherController.UpdateMPSGateStatusResponse updateMPSGateStatus(OtherController.UpdateMPSGateStatusRequest req1) {
        MPSClient.CommonResponse<MPSClient.UpdateMPSGateStatusResponse> res2 = mpsClient.updateMPSGateStatus(ObjectMapperUtil.clone(req1, MPSClient.UpdateMPSGateStatusRequest.class));
        return OtherController.UpdateMPSGateStatusResponse.builder().build();
    }

    @Override
    public OtherController.RequestCPVACSCasInfoResponse requestCPVACSCasInfo(OtherController.RequestCPVACSCasInfoRequest req1) {
        CPVACSAuthClient.CommonResponse<CPVACSAuthClient.LoginResponse> authRes = cpvacsAuthClient.login(CPVACSAuthClient.LoginRequest.builder()
                .username("demoApp")
                .password("123456")
                .build());

//        String deviceType = DeviceList.idOf(req1.getDeviceId()).type();
/*

        String deviceType = Optional.ofNullable(DeviceList.idOf(req1.getDeviceId()))
                .map(it-> it.type())
                .orElse("");
*/

        String deviceType = req1.getDeviceId()==null||req1.getDeviceId().equals("")?"":
                (DeviceList.idOf(req1.getDeviceId())==null?"0":DeviceList.idOf(req1.getDeviceId()).type());

        String deviceId = deviceType.equals("")?"":req1.getDeviceId();

        CPVACSServiceClient.CommonResponse<CPVACSServiceClient.Cpvacs6Response> res1 = cpvacsServiceClient.cpvacs6(deviceId, deviceType, "Bearer "+authRes.getData().getAccessToken());

        res1.getData().getLprCameras().get(0).getCameras().size();
        res1.getData().getLeds().size();
        res1.getData().getLcds().size();
        res1.getData().getBarriers().size();

        List<CPVACSServiceClient.Cpvacs6Response.LprCameras.Cameras> cameras = res1.getData().getLprCameras().stream().map(lprCamera -> {
            return lprCamera.getCameras();
        }).flatMap(Collection::stream).toList();

        List<OtherController.RequestCPVACSCasInfoResponse.DeviceInfo> deviceInfos = new LinkedList<>();

        deviceInfos.addAll(cameras.stream().map(camera -> {
            return OtherController.RequestCPVACSCasInfoResponse.DeviceInfo.builder()
                    .deviceId(camera.getCameraNo())
                    .deviceStatus(camera.getStatus())
                    .deviceCheckDatetime(TimeUtil.format(Instant.now(), "yyyy-MM-dd HH:mm:ss"))
                    .build();
        }).toList());

        deviceInfos.addAll(res1.getData().getLeds().stream().map(led -> {
            return OtherController.RequestCPVACSCasInfoResponse.DeviceInfo.builder()
                    .deviceId(led.getLedNo())
                    .deviceStatus(led.getStatus())
                    .deviceCheckDatetime(TimeUtil.format(Instant.now(), "yyyy-MM-dd HH:mm:ss"))
                    .build();
        }).toList());

        deviceInfos.addAll(res1.getData().getLcds().stream().map(lcd -> {
            return OtherController.RequestCPVACSCasInfoResponse.DeviceInfo.builder()
                    .deviceId(lcd.getLcdNo())
                    .deviceStatus(lcd.getStatus())
                    .deviceCheckDatetime(TimeUtil.format(Instant.now(), "yyyy-MM-dd HH:mm:ss"))
                    .build();
        }).toList());

        deviceInfos.addAll(res1.getData().getBarriers().stream().map(barrier -> {
            return OtherController.RequestCPVACSCasInfoResponse.DeviceInfo.builder()
                    .deviceId(barrier.getBarrierNo())
                    .deviceStatus(barrier.getStatus())
                    .deviceCheckDatetime(TimeUtil.format(Instant.now(), "yyyy-MM-dd HH:mm:ss"))
                    .build();
        }).toList());

        /*

        BookingController.FetchCreateBOSSBookingResponse res1 = BookingController.FetchCreateBOSSBookingResponse.builder()
                .totalCount(res2.getBody().getData().getTotal())
                .bookingInfo(res2.getBody().getData().getBookings().stream().map(booking ->
                        BookingController.FetchCreateBOSSBookingResponse.BookingInfo.builder()
                                .bookingId(booking.getBookingId())
                                .bookingType(booking.getBookingType())
                                .vehicleHongkong(booking.getVehicleHongkong())
                                .vehicleMainland(booking.getVehicleMainland())
                                .vehicleMacao(booking.getVehicleMacao())
                                .vehicleType(booking.getVehicleType())
                                .parkingFrom(booking.getParkingFrom())
                                .parkingTo(booking.getParkingTo())
                                .lastModifiedTime(booking.getLastModifiedTime())
                                .bookingStatus(booking.getBookingStatus())
                                .build()).collect(Collectors.toList()))
                .build();

         */
/*
        return OtherController.RequestCPVACSCasInfoResponse.builder()
                .totalCount(res2.getData().getTotalCount())
                .deviceInfo(res2.getData().getDeviceInfo().stream().map(deviceInfo ->
                                OtherController.RequestCPVACSCasInfoResponse.DeviceInfo.builder()
                                        .deviceId(deviceInfo.getDeviceId())
                                        .deviceStatus(deviceInfo.getDeviceStatus())
                                        .deviceCheckDatetime(deviceInfo.getDeviceCheckDatetime())
                                        .build()).collect(Collectors.toList()))
                .build();*/
        return OtherController.RequestCPVACSCasInfoResponse.builder()
                .totalCount(deviceInfos.size())
                .deviceInfo(deviceInfos)
                .build();

    }
}
