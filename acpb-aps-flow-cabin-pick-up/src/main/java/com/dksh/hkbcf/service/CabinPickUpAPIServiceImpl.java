package com.dksh.hkbcf.service;

import com.dksh.hkbcf.boss.client.BOSSClient;
import com.dksh.hkbcf.cpvacs.client.CPVACSAuthClient;
import com.dksh.hkbcf.cpvacs.client.CPVACSServiceClient;
import com.dksh.hkbcf.mps.client.MPSClient;
import com.dksh.hkbcf.controller.CabinPickUpController;
import com.dksh.hkbcf.util.ObjectMapperUtil;
import com.dksh.hkbcf.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;

@Service
@PropertySource(value="classpath:examples.properties", encoding="UTF-8")
public class CabinPickUpAPIServiceImpl implements CabinPickUpAPIService{

    @Autowired
    MPSClient mpsClient;

    @Autowired
    BOSSClient bossClient;

    @Autowired
    CPVACSServiceClient cpvacsServiceClient;

    @Autowired
    CPVACSAuthClient cpvacsAuthClient;

    @Autowired
    Environment env;

    @Override
    public CabinPickUpController.EnquiryVehicleStatusResponse enquiryVehicleStatus(CabinPickUpController.EnquiryVehicleStatusRequest req1) {
        MPSClient.CommonResponse<MPSClient.QueryVehicleInfoResponse> res2 = mpsClient.queryVehicleInfo(ObjectMapperUtil.clone(req1, MPSClient.QueryVehicleInfoRequest.class));
        return ObjectMapperUtil.clone(res2.getData(), CabinPickUpController.EnquiryVehicleStatusResponse.class);
    }

    @Override
    public CabinPickUpController.ValidateDriverInfoResponse validateDriverInfo(CabinPickUpController.ValidateDriverInfoRequest req1) {
        BOSSClient.IntApsBoss004Request req2 = ObjectMapperUtil.clone(req1, BOSSClient.IntApsBoss004Request.class);
        // input fake id number for aa testing, real time read from txt file
        String identifyNumber = req1.getIdentifyNumber();
        String identifyType = req1.getIdentifyType();

        Boolean demoMode = Boolean.parseBoolean(env.getProperty("boss.intApsBoss004.demoMode"));
//        Boolean demoMode = Boolean.getBoolean("true");
        if(demoMode) {
            try {
//                identifyNumber = new String(Files.readAllBytes(Paths.get("classpath:demo-identify-number.txt")));
                //D:\\DKSH\\project\\cpvacs\\aps\\lanuch\\demo-identify-number.txt
                identifyNumber = new String(Files.readAllBytes(Paths.get(env.getProperty("boss.intApsBoss004.identifyNumber.datasource.path"))));
                identifyType = new String(Files.readAllBytes(Paths.get(env.getProperty("boss.intApsBoss004.identifyType.datasource.path"))));
//                identifyNumber = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("demo-identify-number.txt").toURI())));
            } catch (Exception e) {
                try {
                    throw new Exception(e);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        req2.setIdentifyNumber(identifyNumber);
        req2.setIdentifyType(identifyType);
        req2.setEventDate(req1.getEventDate().replaceAll("-","/"));
        BOSSClient.CommonResponse<BOSSClient.IntApsBoss004Response> res2 = bossClient.intApsBoss004(req1.getBookingId(), req2);
        return CabinPickUpController.ValidateDriverInfoResponse.builder().validate(res2.getBody().getData().getValidate()).build();
    }

    @Override
    public CabinPickUpController.EnquiryParkingFeeResponse enquiryParkingFee(CabinPickUpController.EnquiryParkingFeeRequest req1) {
        BOSSClient.IntApsBoss007Request req2 = ObjectMapperUtil.clone(req1, BOSSClient.IntApsBoss007Request.class);
        req2.setEventDate(req1.getCheckoutDatetime().split(" ")[0].replaceAll("-","/"));
        req2.setEventTime(req1.getCheckoutDatetime().split(" ")[1]);
        BOSSClient.CommonResponse<BOSSClient.IntApsBoss007Response> res2 = bossClient.intApsBoss007(req1.getBookingId(), req2);
        return CabinPickUpController.EnquiryParkingFeeResponse.builder().oustandingBalance(res2.getBody().getData().getOutstandingBalance()).build();
    }

    @Override
    public CabinPickUpController.ConfirmPickUpVehicleResponse confirmPickUpVehicle(CabinPickUpController.ConfirmPickUpVehicleRequest req1) {
        MPSClient.CommonResponse<MPSClient.PaySuccessPickUpResponse> res3 = mpsClient.paySuccessPickUp(ObjectMapperUtil.clone(req1, MPSClient.PaySuccessPickUpRequest.class));

        if(req1.getAmount().compareTo(BigDecimal.ZERO)>0){
            BOSSClient.IntApsBoss008Request req2 = ObjectMapperUtil.clone(req1, BOSSClient.IntApsBoss008Request.class);
            req2.setStatus(req1.getPaymentStatus()==1?"success":"failed");
            BOSSClient.CommonResponse<BOSSClient.IntApsBoss008Response> res2 = bossClient.intApsBoss008(req1.getBookingId(), req2);
        }

        return ObjectMapperUtil.clone(res3.getData(), CabinPickUpController.ConfirmPickUpVehicleResponse.class);
    }

    @Override
    public CabinPickUpController.NotifyPickUpCompleteResponse notifyPickUpComplete(CabinPickUpController.NotifyPickUpCompleteRequest req1) {
        CPVACSAuthClient.CommonResponse<CPVACSAuthClient.LoginResponse> authRes = cpvacsAuthClient.login(CPVACSAuthClient.LoginRequest.builder()
                .username("demoApp")
                .password("123456")
                .build());

        CPVACSServiceClient.Cpvacs3Request req3 = CPVACSServiceClient.Cpvacs3Request.builder()
                .cabinId(req1.getCabinId())
                .parkingRecordId(req1.getParkingRecordId())
                .bookingId(req1.getBookingId())
                .mpsRecordId(req1.getMpsRecordId())
                .vehicleHongkong(req1.getVehicleHongkong())
                .vehicleMacao(req1.getVehicleMacao())
                .vehicleMainland(req1.getVehicleMainland())
                .completePickUpTime(TimeUtil.parseMilli(req1.getCompletePickUpTime(), "yyyy-MM-dd HH:mm:ss")+"")
                .remark(req1.getRemark())
                .build();


        //.completeDropOffTime(TimeUtil.parseMilli(req1.getCompleteDropOffTime(), "yyyy-MM-dd HH:mm:ss")+"")

        CPVACSServiceClient.CommonResponse<CPVACSServiceClient.Cpvacs3Response> res2 = cpvacsServiceClient.cpvacs3(req3, "Bearer "+authRes.getData().getAccessToken());

        BOSSClient.IntApsBoss005Request req2 = ObjectMapperUtil.clone(req1, BOSSClient.IntApsBoss005Request.class);
        // eventId tbc
        req2.setCheckpoint("Cabin");
        req2.setLocation("CabinRetrieval");
        req2.setStatus("retrieval");
        req2.setEventDate(req1.getCompletePickUpTime().split(" ")[0].replaceAll("-","/"));
        req2.setEventTime(req1.getCompletePickUpTime().split(" ")[1]);
        req2.setMoreInfo(BOSSClient.IntApsBoss005Request.MoreInfo.builder().cabinNo(req1.getCabinId()).build());
        req2.setEventId(Instant.now().toEpochMilli()+"");
        BOSSClient.CommonResponse<BOSSClient.IntApsBoss005Response> res3 = bossClient.intApsBoss005(req1.getBookingId(), req2);
        return CabinPickUpController.NotifyPickUpCompleteResponse.builder().build();
    }
}
