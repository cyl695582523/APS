package com.dksh.hkbcf.service;

import com.dksh.hkbcf.controller.OtherController;

public interface OtherAPIService {
    OtherController.UpdateMPSGateStatusResponse updateMPSGateStatus(OtherController.UpdateMPSGateStatusRequest request);
    OtherController.RequestCPVACSCasInfoResponse requestCPVACSCasInfo(OtherController.RequestCPVACSCasInfoRequest request);
}
