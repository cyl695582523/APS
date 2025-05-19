package com.dksh.hkbcf.cpvacs.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "cpvacsServiceClient")
public interface CPVACSServiceClient {

    @Data
    @Builder(builderMethodName = "baseBuilder")
    @NoArgsConstructor
    @AllArgsConstructor
    class CommonResponse<T>{
        private String resultCode;
        private String resultMsg;
        private String requestId;
        private String sysDatetime;
        private T data;
    }

    @Data
    @Builder(builderMethodName = "baseBuilder")
    @NoArgsConstructor
    @AllArgsConstructor
    class ListResponse<T>{
        private String resultCode;
        private String resultMsg;
        private String requestId;
        private String sysDatetime;
        private List<T> data;
    }

    // 1. Cabin Area - Drop Off Vehicle : Turn On CAS/LPR (2.3. 車輛到達車庫入口)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class Cpvacs1Request{
        private String cabinId;
        private Integer loop;
        private Integer vehicleBottomHeight;
        private Integer vehicleStatus;
        private String arriveCabinDoorDatetime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class Cpvacs1Response{
        private String requestId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private Integer ticketType;
        private Long entryTime;
        private Long arrivedTime;
        private String status;
        private String remain;
        private String imagePath;
    }

    @PostMapping(value = "/server/lane/entry/cabin/", headers = {"x-api-key=${bossClient.apiKey}"})
    CommonResponse<Cpvacs1Response> cpvacs1(@RequestBody Cpvacs1Request request, @RequestHeader("Authorization") String accessToken);

    // 2. Cabin Area - Drop Off Vehicle : Update Assigned Cabin Information (2.4. 車輛已經確認進入車庫)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class Cpvacs2Request{
        private String cabinId;
        private String bookingId;
        private String parkingRecordId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String completeDropOffTime;
        private String remark;
    }

    @Data
    @Builder
    @AllArgsConstructor
    class Cpvacs2Response{}

    @PutMapping(value = "/server/lane/entry/cabin/", headers = {"x-api-key=${bossClient.apiKey}"})
    CommonResponse<Cpvacs2Response> cpvacs2(@RequestBody Cpvacs2Request request, @RequestHeader("Authorization") String accessToken);

    // 3. Cabin Area - Pick Up : Update Queuing/Assigned Cabin on Kiosk & LCD Display (2.8. 更新 LCD-車庫分配信息清除)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class Cpvacs3Request{
        private String cabinId;
        private String bookingId;
        private String parkingRecordId;
        private String mpsRecordId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String completePickUpTime;
        private String remark;
    }

    @Data
    @Builder
    @AllArgsConstructor
    class Cpvacs3Response{}

    @PostMapping(value = "/server/lcd/cabin/clear/", headers = {"x-api-key=${bossClient.apiKey}"})
    CommonResponse<Cpvacs3Response> cpvacs3(@RequestBody Cpvacs3Request request, @RequestHeader("Authorization") String accessToken);


    // 4. Cabin Area - Other Cases : Capture Vehicle at Cabin by CAS (2.6. 車輛已到達車庫出口)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class Cpvacs4Request{
        private String cabinId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class Cpvacs4Response{
        private String requestId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private Integer ticketType;
        private Long exitTime;
        private Long arrivedTime;
        private String status;
        private String remain;
        private String imagePath;
    }

    @PostMapping(value = "/server/lane/exit/cabin/", headers = {"x-api-key=${bossClient.apiKey}"})
    CommonResponse<Cpvacs4Response> cpvacs4(@RequestBody Cpvacs4Request request, @RequestHeader("Authorization") String accessToken);


    // 5. Cabin Area - Other Cases : Update Assigned Cabin Info on Kiosk & Signage LCD Display (2.7. 更新 LCD-車庫分配信息展示)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class Cpvacs5Request{
        private String cabinId;
        private String parkingRecordId;
        private String bookingId;
        private String mpsRecordId;
        private String warehouseId;
        private String areaId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String confirmPickupDatetime;
        private String parkInDatetime;
        private String remark;
    }

    @Data
    @Builder
    @AllArgsConstructor
    class Cpvacs5Response{}

    @PostMapping(value = "/server/lcd/cabin/assign/", headers = {"x-api-key=${bossClient.apiKey}"})
    CommonResponse<Cpvacs5Response> cpvacs5(@RequestBody Cpvacs5Request request, @RequestHeader("Authorization") String accessToken);

    // 8. Update 32-inch LCD Display Message
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class Cpvacs8Request {
        private String cabinId;
        private String parkingDisplayId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    class Cpvacs8Response {}

    @PostMapping(value = "/server/lcd/update32DisplayMsg", headers = {"x-api-key=${bossClient.apiKey}"})
    CommonResponse<Cpvacs8Response> cpvacs8(@RequestBody Cpvacs8Request request, @RequestHeader("Authorization") String accessToken);

    // 6. Other Cases : Check the Status of CAS/LPR

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class Cpvacs6Response{
        private List<LprCameras> lprCameras;
        private List<Leds> leds;
        private List<Lcds> lcds;
        private List<Barriers> barriers;
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class LprCameras{
            private List<Cameras> cameras;
            private String laneCode;

            @Data
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Cameras{
                private String cameraNo;
                private String description;
                private Integer status;
            }
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Leds{
            private String ledNo;
            private String description;
            private Integer status;
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Lcds{
            private String lcdNo;
            private String description;
            private Integer status;
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Barriers{
            private String barrierNo;
            private String description;
            private Integer status;
        }
    }

    @GetMapping(value = "/server/device/status/", headers = {"x-api-key=${bossClient.apiKey}"})
    CommonResponse<Cpvacs6Response> cpvacs6(@RequestParam(value="deviceId") String deviceId, @RequestParam(value="deviceType") String deviceType, @RequestHeader("Authorization") String accessToken);


    // 7a. Booking : Retrieve vehicle's entry and exit records (2.9. 根据BookingID查詢入場信息)

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class Cpvacs7aResponse{
        private String requestId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private Integer ticketType;
        private String parkingRecordId;
        private String bookingId;
        private Long entryTime;
        private Long arrivedTime;
        private String status;
        private String remain;
        private String imagePath;
        private String laneCode;
    }

    @GetMapping(value = "/server/lane/booking/entry/", headers = {"x-api-key=${bossClient.apiKey}"})
    ListResponse<Cpvacs7aResponse> cpvacs7a(@RequestParam(value="bookingId") String bookingId, @RequestHeader("Authorization") String accessToken);


    // 7b. Booking : Retrieve vehicle's entry and exit records (2.10. 根据BookingID查詢離場信息)

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class Cpvacs7bResponse{
        private String requestId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private Integer ticketType;
        private String parkingRecordId;
        private String bookingId;
        private Long exitTime;
        private Long arrivedTime;
        private String status;
        private String remain;
        private String imagePath;
        private String laneCode;
    }

    @GetMapping(value = "/server/lane/booking/exit/", headers = {"x-api-key=${bossClient.apiKey}"})
    ListResponse<Cpvacs7bResponse> cpvacs7b(@RequestParam(value="bookingId") String bookingId, @RequestHeader("Authorization") String accessToken);

}
