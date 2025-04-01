package com.dksh.hkbcf.service;

import com.dksh.hkbcf.controller.EntryController;

public interface EntryAPIService {

    EntryController.EnquiryCpaIckInfoResponse enquiryCpaIckInfo(EntryController.EnquiryCpaIckInfoRequest request);
    EntryController.UpdateCpaIckInfoResponse updateCpaIckInfo(EntryController.UpdateCpaIckInfoRequest request);
    EntryController.RequestMPSCabinResponse requestMPSCabin(EntryController.RequestMPSCabinRequest request);
    EntryController.NotifyBOSSEnterResponse notifyBOSSEnter(EntryController.NotifyBOSSEnterRequest request);
}
