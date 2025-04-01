package com.dksh.hkbcf.service;

import com.dksh.hkbcf.boss.client.BOSSClient;
import com.dksh.hkbcf.mps.client.MPSClient;
import com.dksh.hkbcf.controller.ExitController;
import com.dksh.hkbcf.pojo.BOSSAPSAPICommonResponse;
import com.dksh.hkbcf.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class ExitAPIServiceImpl implements ExitAPIService{

    @Autowired
    BOSSClient bossClient;

    @Override
    public ExitController.NotifyBOSSExitResponse notifyBOSSExit(ExitController.NotifyBOSSExitRequest req1) {
        BOSSClient.IntApsBoss005Request req2 = ObjectMapperUtil.clone(req1, BOSSClient.IntApsBoss005Request.class);
        // eventId and moreInfo.ickNo tbc
        req2.setCheckpoint("BarrierGate");
        String location = "MainExitGate";
        if(req1.getLaneCode()!=null && req1.getLaneCode().equals("exit_0")) location = "MainExitGate0";
        else if(req1.getLaneCode()!=null && req1.getLaneCode().equals("exit_1")) location = "MainExitGate1";
        else if(req1.getLaneCode()!=null && req1.getLaneCode().equals("exit_2")) location = "MainExitGate2";
        req2.setLocation(location);
        req2.setStatus("pass");
        req2.setEventDate(req1.getExitDatetime().split(" ")[0].replaceAll("-","/"));
        req2.setEventTime(req1.getExitDatetime().split(" ")[1]);
        req2.setEventId(Instant.now().toEpochMilli()+"");
        BOSSClient.CommonResponse<BOSSClient.IntApsBoss005Response> res2 = null;
        if(req1.getBookingId()!=null)
                res2 = bossClient.intApsBoss005(req1.getBookingId(), req2);
        return ExitController.NotifyBOSSExitResponse.builder().build();
    }
}
