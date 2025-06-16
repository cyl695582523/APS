package com.dksh.hkbcf.mps.client;

import com.dksh.hkbcf.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@FeignClient(name = "mpsClient")
public interface MPSClient {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class CommonResponse<T>{
        private String msg;
        private Integer code;
        private T data;
        private Long timestamp;
    }

    @Data
    @Builder(builderMethodName = "baseBuilder")
    @NoArgsConstructor
    @AllArgsConstructor
    class DataCommonResponse{
        @Builder.Default private Integer resultCode = 0;
        @Builder.Default private String resultMessage = "error";
        @Builder.Default private String sysDatetime = TimeUtil.format(Instant.now(), "yyyy-MM-dd HH:mm:ss");
    }

    //2.1.预先分配停车入口
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class AllocationMPSCabinRequest{
        private String parkId;
        private String gateId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private Integer height;
        private Integer length;
        private Integer width;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class AllocationMPSCabinResponse extends DataCommonResponse{
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private Integer status;
        private String cabinId;
        private String cabinName;
        private String assignedDatetime;
        private String bookingId;
        private String remark;

    }
    @PostMapping(value = "/allocationMPSCabin", headers = {"x-api-key=${mpsClient.apiKey}"})
    CommonResponse<AllocationMPSCabinResponse> allocationMPSCabin(@RequestBody AllocationMPSCabinRequest request);

    //2.2.取消预先分配停车入口
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class CancelAllocationMpsCabinRequest{
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
    }

    @Data
    @Builder
    @AllArgsConstructor
    class CancelAllocationMpsCabinResponse extends DataCommonResponse{}
    @PostMapping(value = "/cancelAllocationMpsCabin", headers = {"x-api-key=${mpsClient.apiKey}"})
    CommonResponse<CancelAllocationMpsCabinResponse> cancelAllocationMpsCabin(@RequestBody CancelAllocationMpsCabinRequest request);

    //2.3.道闸车牌识别（申请分配停车入口）
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class RequestAllocationMpsCabinRequest{
        private String parkId;
        private String gateId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private Integer height;
        private Integer length;
        private Integer width;
        private String bookingId;
        private Integer handicapped;
    }
    

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class RequestAllocationMpsCabinResponse extends DataCommonResponse{
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private Integer status;
        private String cabinId;
        private String cabinName;
        private String assignedDatetime;
        private String bookingId;
        private String remark;
    }

    @PostMapping(value = "/requestAllocationMpsCabin", headers = {"x-api-key=${mpsClient.apiKey}"})//2.3.道闸车牌识别（申请分配停车入口）
    CommonResponse<RequestAllocationMpsCabinResponse> requestAllocationMpsCabin(@RequestBody RequestAllocationMpsCabinRequest request);

    //2.4.查询预约车辆活动信息
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class QueryReservationVehicleInfoRequest{
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
    class QueryReservationVehicleInfoResponse extends DataCommonResponse{
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
            private String bookingId;
            private String parkingRecordId;
            private String mpsRecordId;
            private String status;
            private String checkpoint;
            private String location;
            private List<MoreInfo> moreInfo;
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
    }
    @PostMapping(value = "/queryReservationVehicleInfo", headers = {"x-api-key=${mpsClient.apiKey}"})
    CommonResponse<QueryReservationVehicleInfoResponse> queryReservationVehicleInfo(@RequestBody QueryReservationVehicleInfoRequest request);

    //2.5.修改第三方预约信息
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class ChangeBookingInfoRequest{
        private String bookingId;
        private String status;
        private String bookingType;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String vehicleType;
        private String parkingFrom;
        private String parkingToDatetime;
        private String lastModifiedTime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class ChangeBookingInfoResponse extends DataCommonResponse{
        private String bookingId;

    }
    @PostMapping(value = "/changeBookingInfo", headers = {"x-api-key=${mpsClient.apiKey}"})
    CommonResponse<ChangeBookingInfoResponse> changeBookingInfo(@RequestBody ChangeBookingInfoRequest request);

    //2.6.大昌APS接口提供到达停车入口车辆信息
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class SendMPSArrivedCabinVehicleInfoRequest{
        private String cabinId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String lpnCaptureDatetime;
    }

    @Data
    @Builder
    @AllArgsConstructor
    class SendMPSArrivedCabinVehicleInfoResponse extends DataCommonResponse{}
    @PostMapping(value = "/sendMPSArrivedCabinVehicleInfo", headers = {"x-api-key=${mpsClient.apiKey}"})
    CommonResponse<SendMPSArrivedCabinVehicleInfoResponse> sendMPSArrivedCabinVehicleInfo(@RequestBody SendMPSArrivedCabinVehicleInfoRequest request);

    //2.7.查询入口状态
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class EnquiryMPSCabinStatusRequest{
        private String cabinId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class EnquiryMPSCabinStatusResponse extends DataCommonResponse{
        private String cabinId;
        private String assignedVehicleHongkong;
        private String assignedVehicleMainland;
        private String assignedVehicleMacao;
        private String assignedDatetime;
        private String bookingId;
        // // 2025-05-09 tbc
        private String mode;
        private String status;

    }

    // 2.7. 查询入口状态
    @PostMapping(value = "/enquiryMPSCabinStatus", headers = {"x-api-key=${mpsClient.apiKey}"})
    CommonResponse<EnquiryMPSCabinStatusResponse> enquiryMPSCabinStatus(@RequestBody EnquiryMPSCabinStatusRequest request);

    //2.8.打开停车入口
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class OpenParkingEntryRequest{
        private String cabinId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String bookingId;
        private String parkingRecordId;
        private String parkInDateTime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class OpenParkingEntryResponse extends DataCommonResponse{
        private String cabinId;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String bookingId;
        private String parkingRecordId;
    }

    @PostMapping(value = "/openParkingEntry", headers = {"x-api-key=${mpsClient.apiKey}"})
    CommonResponse<OpenParkingEntryResponse> openParkingEntry(@RequestBody OpenParkingEntryRequest request);

    //2.9.查询库内车辆信息
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class QueryVehicleInfoRequest{
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String bookingId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class QueryVehicleInfoResponse extends DataCommonResponse{
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String cabinId;
        private String mpsRecordId;
        private String warehouseId;
        private String areaId;
        private String vehicleImgUrl;
        private String vehicleImg;
        private String parkingRecordId;
        private String parkInDatetime;
        private String remark;
        private String bookingId;
        // Brian 2025-05-12 3.13
        private String parkingFrom;
        private String parkingTo;        
    }

    @PostMapping(value = "/queryVehicleInfo", headers = {"x-api-key=${mpsClient.apiKey}"})
    CommonResponse<QueryVehicleInfoResponse> queryVehicleInfo(@RequestBody QueryVehicleInfoRequest request);

    //2.10.支付成功后下发取车
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class PaySuccessPickUpRequest{
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String confirmPickupDatetime;
        private String bookingId;
        private String parkingRecordId;
        private String mpsRecordId;
        private String remark;
        private BigDecimal amount;
        private Integer paymentStatus;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class PaySuccessPickUpResponse extends DataCommonResponse{
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String cabinId;
        private String mpsRecordId;
        private String warehouseId;
        private String areaId;
        private String vehicleImgUrl;
        private String vehicleImg;
        private String parkingRecordId;
        private String parkInDateTime;
        private Integer vehicleStatus;
        private String remark;
        private String bookingId;
    }

    @PostMapping(value = "/paySuccessPickUp", headers = {"x-api-key=${mpsClient.apiKey}"})
    CommonResponse<PaySuccessPickUpResponse> paySuccessPickUp(@RequestBody PaySuccessPickUpRequest request);

    //2.11.大昌APS接口通知停车入口对讲机开启
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class OpenInterPhoneRequest{
        private String deviceId;
        private String cabinId;
        private String turnOnDatetime;
    }

    @Data
    @Builder
    @AllArgsConstructor
    class OpenInterPhoneResponse extends DataCommonResponse{}

    @PostMapping(value = "/openInterPhone", headers = {"x-api-key=${mpsClient.apiKey}"})
    CommonResponse<OpenInterPhoneResponse> openInterPhone(@RequestBody OpenInterPhoneRequest request);

    //2.12.大昌APS接口更新道闸状态
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateMPSGateStatusRequest{
        private String gateId;
        private String gateUpdateDatetime;
        private Integer gateStatus;
        private String vehicleHongkong;
        private String vehicleMainland;
        private String vehicleMacao;
        private String bookingId;
        private String parkingRecordId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    class UpdateMPSGateStatusResponse extends DataCommonResponse{}

    @PostMapping(value = "/updateMPSGateStatus", headers = {"x-api-key=${mpsClient.apiKey}"})
    CommonResponse<UpdateMPSGateStatusResponse> updateMPSGateStatus(@RequestBody UpdateMPSGateStatusRequest request);


    // 2.15. CPVACS 故障上报
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class NotifyAlarmRequest {
        //YF API 2.15, CPVACS will not provide recover Time, malfunction Duration and malfunction status in Alerts calling.
        private Integer id;
        private String malfunctionInfo;
        private String deviceName;
        private String malfunctionTime;
        private String recoverTime;
        private Long malfunctionDuration;
        private Integer malfunctionStatus;
    }

    @Data
    @Builder
    @NoArgsConstructor
    class NotifyAlarmResponse extends DataCommonResponse {}

    @PostMapping(value = "/notifyAlarm", headers = {"x-api-key=${mpsClient.apiKey}"})
    CommonResponse<NotifyAlarmResponse> notifyAlarm(@RequestBody NotifyAlarmRequest request);
}
