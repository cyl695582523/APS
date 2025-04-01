package com.dksh.hkbcf.boss.client;

import feign.Headers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "bossClient")
public interface BOSSClient {
    @Data
    @Builder(builderMethodName = "baseBuilder")
    @NoArgsConstructor
    @AllArgsConstructor
    class CommonResponse<T>{
        private Body<T> body;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Body<T>{
            private Integer code;
            private String message;
            private T data;

        }

        private String error;
        private String interface_num;
        private String message;
        private Meta meta;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Meta{
            private List<ParamErrors> param_errors;

            @Data
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class ParamErrors{
                private String param;
                private String error;
            }
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class IntApsBoss001Request{
        private String date;
        private Integer offset;
        private Integer batchCount;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class IntApsBoss001Response{
        private Integer total;
        private List<Booking> bookings;

        @lombok.Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Booking{
            private String bookingId;
            private String bookingType;
            private String vehicleHongkong;
            private String vehicleMainland;
            private String vehicleMacao;
            private String vehicleType;
            private String parkingFrom;
            private String parkingTo;
            private String lastModifiedTime;
            private String bookingStatus;
        }
    }

    @PostMapping(value = "/aps/batch/retrieve-bookings", headers = {"x-api-key=${bossClient.apiKey}"})
    CommonResponse<IntApsBoss001Response> intApsBoss001(@RequestBody IntApsBoss001Request request);

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class IntApsBoss004Request{
        private String bookingId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String identifyType;
        private String identifyNumber;
        private String eventDate;
        private String eventTime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class IntApsBoss004Response{
        private String validate;
    }

    @PostMapping(value = "/aps/booking/{bookingId}/validate", headers = {"x-api-key=${bossClient.apiKey}"})
    CommonResponse<IntApsBoss004Response> intApsBoss004(@PathVariable("bookingId") String bookingId, @RequestBody IntApsBoss004Request request);

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class IntApsBoss005Request{
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

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class MoreInfo{
            private String cabinNo;
            private String ickNo;
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    class IntApsBoss005Response{}

    @PostMapping(value = "/aps/booking/{bookingId}/update-vehicle-tracking-data", headers = {"x-api-key=${bossClient.apiKey}"})
    CommonResponse<IntApsBoss005Response> intApsBoss005(@PathVariable("bookingId") String bookingId, @RequestBody IntApsBoss005Request request);

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class IntApsBoss007Request{
        private String bookingId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String eventDate;
        private String eventTime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class IntApsBoss007Response{
        private BigDecimal outstandingBalance;
        private String gateOpeningFlag;
    }

    @PostMapping(value = "/aps/booking/{bookingId}/arrears", headers = {"x-api-key=${bossClient.apiKey}"})
    CommonResponse<IntApsBoss007Response> intApsBoss007(@PathVariable("bookingId") String bookingId, @RequestBody IntApsBoss007Request request);

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class IntApsBoss008Request{
        private String bookingId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private BigDecimal amount;
        private String status;
    }

    @Data
    @Builder
    @AllArgsConstructor
    class IntApsBoss008Response{}

    @PostMapping(value = "/aps/booking/{bookingId}/payment-result", headers = {"x-api-key=${bossClient.apiKey}"})
    CommonResponse<IntApsBoss008Response> intApsBoss008(@PathVariable("bookingId") String bookingId, @RequestBody IntApsBoss008Request request);

}
