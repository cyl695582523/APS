package com.dksh.hkbcf;

import com.dksh.hkbcf.boss.client.BOSSClient;
import com.dksh.hkbcf.controller.BOSSAPSController;
import com.dksh.hkbcf.pojo.BOSSAPSAPICommonResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import feign.BaseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.time.Clock;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static com.dksh.hkbcf.util.ObjectMapperUtil.objectMapper;

@SpringBootApplication
@Slf4j
public class Main {
    public static void main(String[] args) {

        // for testing time zone
        //System.setProperty("user.timezone", "GMT+8");
/*

        log.info(Calendar.getInstance().getTimeZone().getDisplayName());
        log.info(TimeZone.getDefault().getDisplayName());
        log.info(TimeZone.getDefault().getID());
        log.info(Clock.systemDefaultZone().instant().toString());

        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));

        log.info(Calendar.getInstance().getTimeZone().getDisplayName());
        log.info(TimeZone.getDefault().getDisplayName());
        log.info(TimeZone.getDefault().getID());
        log.info(Clock.systemDefaultZone().instant().toString());
*/

        // for generating boss booking testing record
/*
        MY4577 00:03
        PN4731 00:11
        SM6948 00:22
        XS7540 00:32
        PB2088 00:44

        UG1314 00:22
        */
/*
        List<BOSSClient.IntApsBoss001Response.Booking> bookings = new LinkedList<>();

        BOSSClient.IntApsBoss001Response.Booking booking = null;

        String baseDate = "2024/05/20";
        Integer bookingIdStartFrom = 1;
        String bookingIdFormat = "OBS-"+baseDate.replaceAll("/","").replace("2024","24")+"%04d";
        String bookingType = "T1";
        List<String> vehicleHongkongs = Stream.of(
                "MY4577",
                        "PN4731",
                        "SM6948",
                        "XS7540",
                        "UG1314")
                .collect(Collectors.toList());
        String vehicleMainland = "";
        String vehicleMacao = "";
        String vehicleType = "L1";
        String parkingDateFromFormat = baseDate+" %02d:00:00";
        String parkingDateToFormat = baseDate+" %02d:59:59";
        Integer parkingTimeStartFrom = 0;
        Integer parkingTimeStartTo = 23;
        String lastModifiedTime = "2024/05/16 17:08:57";
        String bookingStatus = "CONFIRM";

        for(int i=parkingTimeStartFrom;i<=parkingTimeStartTo;i++){

            for(String vehicleHongkong:vehicleHongkongs){
                booking = new BOSSClient.IntApsBoss001Response.Booking();

                booking.setBookingType(bookingType);
                booking.setVehicleMainland(vehicleMainland);
                booking.setVehicleMacao(vehicleMacao);
                booking.setVehicleType(vehicleType);
                booking.setLastModifiedTime(lastModifiedTime);
                booking.setBookingStatus(bookingStatus);

                booking.setBookingId(String.format(bookingIdFormat, bookingIdStartFrom++));
                booking.setVehicleHongkong(vehicleHongkong);
                booking.setParkingFrom(String.format(parkingDateFromFormat, i));
                booking.setParkingTo(String.format(parkingDateToFormat, i));

                log.info(booking.getBookingId());
                log.info(booking.getParkingFrom());
                log.info(booking.getParkingTo());
                log.info(booking.toString());

                bookings.add(booking);
            }

        }

        log.info(bookings.toString());

        try {
            String jsonArray = objectMapper.writeValueAsString(bookings);
            log.info(jsonArray);
            log.info(bookings.size()+"");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
*/


        /*
        bookingId
        vehicleHongkong
        parkingFrom
        parkingTo

        00:00:00 -> 00:59:59
        01:00:00 -> 00:59:59

                        {
                    "bookingId": "OBS-0000005009",
                    "bookingType": "T1",
                    "vehicleHongkong": "UG1314",
                    "vehicleMainland": "",
                    "vehicleMacao": "",
                    "vehicleType": "L1",
                    "parkingFrom": "2024/01/23 00:00:00",
                    "parkingTo": "2024/01/23 01:00:00",
                    "lastModifiedTime": "2024/05/16 17:08:57",
                    "bookingStatus": "CONFIRM"
                },

         */

        SpringApplication.run(Main.class, args);
    }
}