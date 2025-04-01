package com.dksh.hkbcf.controller;

import com.dksh.hkbcf.pojo.APSAPICommonResponse;
import com.dksh.hkbcf.service.BookingAPIService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class BookingController {

    @Autowired
    BookingAPIService bookingAPIService;

    // 3.19. MPS Create Booking Information
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FetchCreateBOSSBookingRequest{
        private String date;
        private Integer offset;
        private Integer batchCount;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FetchCreateBOSSBookingResponse extends APSAPICommonResponse {
        private Integer totalCount;
        private List<BookingInfo> bookingInfo;
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class BookingInfo{
            private String vehicleHongkong;
            private String vehicleMainland;
            private String vehicleMacao;
            private String vehicleType;
            private String bookingId;
            private String bookingType;
            private String parkingFrom;
            private String parkingTo;
            private String lastModifiedTime;
            private String bookingStatus;
        }
    }

    @PostMapping("/fetchCreateBOSSBooking")
    public FetchCreateBOSSBookingResponse fetchCreateBOSSBooking(@RequestBody FetchCreateBOSSBookingRequest request) throws Exception {
        return bookingAPIService.fetchCreateBOSSBooking(request);
    }

}
