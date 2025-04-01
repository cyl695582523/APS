package com.dksh.hkbcf.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ickClient")
public interface ICKClient {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ick1Request{
        private String startTime;
        private String endTime;
//        private String format;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ick1Response{
        private Integer code;
        private String message;
        private Data data;

        @lombok.Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Data{
            private Integer total;
            private List<Event> events;

            @lombok.Data
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Event{
                private String vehicleHongkong;
                private String vehicleMainland;
                private String vehicleMacao;
                private String eventDate;
                private String eventTime;
                private Integer length;
                private Integer width;
                private Integer height;
                private Integer secondSearchFlag;
                private Integer clearanceFlag;
            }
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ick1Ver1Response{
        private Boolean success;
        private String message;
        private Integer code;
        private Result result;
        private Long timestamp;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Result{
            private Integer total;
            private List<Event> events;

            @lombok.Data
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Event{
                private String vehicleHongkong;
                private String vehicleMainland;
                private String vehicleMacao;
                private String eventDate;
                private String eventTime;
                private Integer length;
                private Integer width;
                private Integer height;
                private Integer secondSearchFlag;
                private Integer clearanceFlag;
                private String more;
            }
        }
    }

    @PostMapping(value = "/ick1", headers = {"x-api-key=${ickClient.apiKey}"})
    Ick1Response ick1default(@RequestBody Ick1Request request);

/*    @PostMapping(value = "/ick1", headers = {"x-api-key=${ickClient.apiKey}"})
    Ick1Ver1Response ick1ver1(@RequestBody Ick1Request request);*/
    @PostMapping(value = "/cpvacs/ickCheck/list")
    Ick1Response ick1ver1(@RequestBody Ick1Request request);

    @PostMapping(value = "/cpvacs/ickCheck/list")
    Ick1Ver1Response ick1ver1(@RequestParam(value="startTime") String startTime,
                              @RequestParam(value="endTime") String endTime);

}
