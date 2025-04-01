package com.dksh.hkbcf.controller;

import com.dksh.hkbcf.exception.exception.ClientException;
import com.dksh.hkbcf.exception.exception.MPSClientException;
import com.dksh.hkbcf.pojo.APSAPICommonResponse;
import com.dksh.hkbcf.pojo.BOSSAPSAPICommonResponse;
import com.dksh.hkbcf.service.BOSSAPSAPIService;
import feign.FeignException;
import feign.Response;
import feign.Util;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.dksh.hkbcf.util.ObjectMapperUtil.objectMapper;

@Slf4j
@RestController
public class BOSSAPSController {


    @ControllerAdvice(assignableTypes = {BOSSAPSController.class})
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public class BOSSAPSExceptionHandler {
/*
    @ResponseBody
    @ExceptionHandler(value= {Exception.class})
    public APSAPICommonResponse handleException(Exception ex) {
        log.error("error log: {}",ex.getMessage(),ex);
        return APSAPICommonResponse.baseBuilder()
                .resultCode(0)
                .resultMessage("System Error")
                .build();
    }
*/

        @ResponseBody
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(value= {RuntimeException.class, Exception.class})
        public BOSSAPSAPICommonResponse handleException(Exception ex, HttpServletResponse response) {

//            log.info("response2.status: "+response2.status());
            log.error("error log: {}",ex.getMessage(),ex);
            return BOSSAPSAPICommonResponse.baseBuilder()
                    .code(10001)
                    .message(ex.getMessage())
                    .build();

        }

        @ResponseBody
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(value= {MPSClientException.class})
        public MPSClientException handleMPSClientException(MPSClientException ex, HttpServletResponse response) {
/*            log.error("error log: {}",ex.getMessage(),ex);
            return BOSSAPSAPICommonResponse.baseBuilder()
                    .code(10001)
                    .message(ex.getMessage())
                    .build();
            */
            log.error("handleClientException");
            log.error("error log: {}",ex.getMessage(),ex);
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ex;


        }

        @ResponseBody
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(value= {FeignException.BadRequest.class})
        public <K,V> HashMap<K,V> handleFeignExceptionBadRequest(FeignException.BadRequest ex) {

            log.error("handleFeignExceptionBadRequest");
            log.error("error log: {}",ex.getMessage(),ex);

            try{
                var v = objectMapper.readValue(ex.responseBody().orElse(ByteBuffer.allocate(512000)).array(), HashMap.class);
                HashMap<K, V> layer1Map = new LinkedHashMap<>();

                Object code = 0;
                if(v.get("code").toString().equals("200")) code = 0;
                else if(v.get("code").toString().equals("500")) code = 10001;
                else code = v.get("code");

//                layer1Map.put((K)"code",(V)v.get("code"));
                layer1Map.put((K)"code",(V)code);
                layer1Map.put((K)"message",(V)v.get("msg"));
                if(v.containsKey("data")) {
//                    var body = objectMapper.readValue(v.get("data").toString().getBytes(), HashMap.class);
                    layer1Map.put((K)"data",(V)v.get("data"));
//                    layer1Map.put((K)"data",(V)body);
                }

//                v.remove("code");
//                v.remove("msg");
//                v.remove("timestamp");
                return layer1Map;


            }
            catch (Throwable throwable){
                throwable.printStackTrace();
//                throw new ClientException(ex.getMessage());
            }

//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        @ResponseBody
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(value= {NullPointerException.class})
        public BOSSAPSAPICommonResponse handleNullPointerException(Exception ex) {
            log.error("error log: {}",ex.getMessage(),ex);
            return BOSSAPSAPICommonResponse.baseBuilder()
                    .code(10001)
                    .message("record not found")
                    .build();
        }
    }

    @Autowired
    BOSSAPSAPIService bossApsApiService;

    // 3.20. CPVACS/BOSS Enquiry Vehicle Tracking Information
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BulkExportBookingRequest{
        private String date;
        private Integer offset;
        private Integer batchCount;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BulkExportBookingResponse {
        private Integer total;
        private List<Event> events;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Event{
            private String bookingId;
            private String eventId;
            private String vehicleHongkong;
            private String vehicleMainland;
            private String vehicleMacao;
            private String checkpoint;
            private String location;
            private String status;
            private MoreInfo moreInfo;
            private String remark;
            private String eventDate;
            private String eventTime;
            private String parkingRecordId;
            private String mpsRecordId;

            @Data
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class MoreInfo{
                private String cabinNo;
                private String ickNo;
            }
        }
    }

    @PostMapping("/bulkExportBooking")
    public BOSSAPSAPICommonResponse<BulkExportBookingResponse> bulkExportBooking(@RequestBody BulkExportBookingRequest request) {
        return bossApsApiService.bulkExportBooking(request);
    }

    // 3.21. BOSS Request Change Booking Information
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangeBookingRequest{
        private String bookingId;
        private String status;
        private String bookingType;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String vehicleType;
        private String parkingFrom;
        private String parkingTo;
        private String lastModifiedTime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangeBookingResponse{
        private String bookingId;
    }

    @PostMapping("/changeBooking")
    public BOSSAPSAPICommonResponse<ChangeBookingResponse> changeBooking(@RequestBody ChangeBookingRequest request) {
        return bossApsApiService.changeBooking(request);
    }

}
