package com.dksh.hkbcf.service;

import com.dksh.hkbcf.mps.client.MPSClient;
import com.dksh.hkbcf.controller.BookingController;
import com.dksh.hkbcf.util.ObjectMapperUtil;
import com.dksh.hkbcf.boss.client.BOSSClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
@Slf4j
@Service
public class BookingAPIServiceImpl implements BookingAPIService{

    @Autowired
    BOSSClient bossClient;

    @Autowired
    MPSClient mpsClient;

    @Override
    public BookingController.FetchCreateBOSSBookingResponse fetchCreateBOSSBooking(BookingController.FetchCreateBOSSBookingRequest req1) throws Exception {

        //req3.setEventDate(req1.getCompleteDropOffTime().split(" ")[0].replaceAll("-","/"));

        BOSSClient.CommonResponse<BOSSClient.IntApsBoss001Response> res2 = bossClient.intApsBoss001(
                BOSSClient.IntApsBoss001Request.builder()
                        .date(req1.getDate().replaceAll("-","/"))
                        // for boss mock server to match example
                        .offset(req1.getOffset())
                        .batchCount(req1.getBatchCount())
                        .build()
        );

        // throw diff error b4 return, implement later
        // 1. bossClient return res2.error
        // 2. wrong request
        // 3. other unexpected error
//        if(true) throw new Exception();

        // case for boss mock server
/*
        BookingController.FetchCreateBOSSBookingResponse res1 = BookingController.FetchCreateBOSSBookingResponse.builder()
                .totalCount(res2.getBody().getData().getTotal())
                .bookingInfo(res2.getBody().getData().getBookings().stream().skip(req1.getOffset()).limit(req1.getBatchCount()).map(booking ->
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

        // normal case with non mock server
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

        return res1;
    }
}
