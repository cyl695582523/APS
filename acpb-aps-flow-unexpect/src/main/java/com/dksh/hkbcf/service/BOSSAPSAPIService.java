package com.dksh.hkbcf.service;

import com.dksh.hkbcf.controller.BOSSAPSController;
import com.dksh.hkbcf.pojo.BOSSAPSAPICommonResponse;

public interface BOSSAPSAPIService {
    BOSSAPSAPICommonResponse<BOSSAPSController.BulkExportBookingResponse> bulkExportBooking(BOSSAPSController.BulkExportBookingRequest request);
    BOSSAPSAPICommonResponse<BOSSAPSController.ChangeBookingResponse> changeBooking(BOSSAPSController.ChangeBookingRequest request);
}
