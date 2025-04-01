package com.dksh.hkbcf.service;

import com.dksh.hkbcf.controller.ExitController;
import com.dksh.hkbcf.pojo.BOSSAPSAPICommonResponse;

public interface ExitAPIService {
    ExitController.NotifyBOSSExitResponse notifyBOSSExit(ExitController.NotifyBOSSExitRequest request);
}
