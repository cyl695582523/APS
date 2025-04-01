package com.dksh.hkbcf.service;

import com.dksh.hkbcf.controller.BookingController;

public interface BookingAPIService {
    BookingController.FetchCreateBOSSBookingResponse fetchCreateBOSSBooking(BookingController.FetchCreateBOSSBookingRequest request) throws Exception;
}
