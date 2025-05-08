package com.dksh.hkbcf.schedule;

import com.dksh.hkbcf.client.ICKClient;
import com.dksh.hkbcf.enums.ICKClearanceStatus;
import com.dksh.hkbcf.mps.client.MPSClient;
import com.dksh.hkbcf.model.IckVehicle;
import com.dksh.hkbcf.model.IckVehicleRecord;
import com.dksh.hkbcf.model.IckVehicleRecordProcessEvent;
import com.dksh.hkbcf.model.IckVehicleRecordSyncEvent;
import com.dksh.hkbcf.repository.IckVehicleRecordProcessEventRepository;
import com.dksh.hkbcf.repository.IckVehicleRecordRepository;
import com.dksh.hkbcf.repository.IckVehicleRecordSyncEventRepository;
import com.dksh.hkbcf.repository.IckVehicleRepository;
import com.dksh.hkbcf.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ICKVehicleRecordSchedule {

    @Autowired
    ICKClient ickClient;

    @Autowired
    MPSClient mpsClient;

    // Brian 2025-05-08 For save ick_vehicle
    @Autowired
    IckVehicleRepository ickVehicleRepository;

    @Autowired
    IckVehicleRecordRepository ickVehicleRecordRepository;

    @Autowired
    IckVehicleRecordSyncEventRepository ickVehicleRecordSyncEventRepository;

    @Autowired
    IckVehicleRecordProcessEventRepository ickVehicleRecordProcessEventRepository;



    @Autowired Environment env;

    //@Scheduled(fixedRate = 60000, initialDelay = 1000)
    @Transactional
    public void sync (){

        Instant eventDateStart = Instant.now();

        // get ick record from ick by calling ick api
        IckVehicleRecordSyncEvent ickVehicleRecordSyncEvent1 = ickVehicleRecordSyncEventRepository.findFirstByOrderByIckVehicleRecordSyncEventIdDesc();

        Instant requestStartTimeInstant = ickVehicleRecordSyncEvent1!=null?ickVehicleRecordSyncEvent1.getRequestEndTime().plusSeconds(1):
                TimeUtil.parse(env.getProperty("ick.sync.initial.start-time"), "yyyy/MM/dd HH:mm:ss");
        String requestStartTimeStr = ickVehicleRecordSyncEvent1!=null?TimeUtil.format(requestStartTimeInstant, "yyyy/MM/dd HH:mm:ss"):
                env.getProperty("ick.sync.initial.start-time");

        // delay for mps doesn't completed as well
        Instant requestEndTimeInstant = Instant.now().minusSeconds(2);
        String requestEndTimeStr = TimeUtil.format(requestEndTimeInstant, "yyyy/MM/dd HH:mm:ss");

        ICKClient.Ick1Request ick1Request = ICKClient.Ick1Request.builder()
                .startTime(requestStartTimeStr)
                .endTime(requestEndTimeStr)
                .build();
        
        // Get Data from ICK
        ICKClient.Ick1Response ick1Response = ickClient.ick1ver1(ick1Request);
        
        // save ick_vehicle_record
        List<IckVehicleRecord> ickVehicleRecordList1 = ickVehicleRecordRepository.saveAll(ick1Response.getData().getEvents().stream().map(event ->
                IckVehicleRecord.builder()
                        .vehicleHongkong(event.getVehicleHongkong())
                        .vehicleMainland(event.getVehicleMainland())
                        .vehicleMacao(event.getVehicleMacao())
                        .secondSearchEventDate(TimeUtil.parseDate(event.getEventDate(), "yyyy/MM/dd"))
                        .secondSearchEventTime(TimeUtil.parseTime(event.getEventTime(), "HH:mm:ss"))
                        .length(event.getLength())
                        .width(event.getWidth())
                        .height(event.getHeight())
                        .secondSearchFlag(event.getSecondSearchFlag())
                        .clearanceFlag(event.getClearanceFlag())
                        .bookingId(event.getBookingId())
                        .handicapped(event.getHandicapped())
                        .primaryVehicleRegion(event.getPrimaryVehicleRegion())
                        .build()).collect(Collectors.toList()));

        Instant eventDateEnd = Instant.now();

        // save ick_vehicle_record_sync_event
        IckVehicleRecordSyncEvent ickVehicleRecordSyncEvent2 = ickVehicleRecordSyncEventRepository.save(IckVehicleRecordSyncEvent.builder()
                        .eventDateStart(eventDateStart)
                        .eventDateEnd(eventDateEnd)
                        .requestStartTime(requestStartTimeInstant)
                        .requestEndTime(requestEndTimeInstant)
                .build());

        log.info(ickVehicleRecordSyncEvent2.getIckVehicleRecordSyncEventId().toString());

        // update ick_vehicle_record
        ickVehicleRecordList1.forEach(
                ickVehicleRecord -> ickVehicleRecord.setIckVehicleRecordSyncEventId(ickVehicleRecordSyncEvent2.getIckVehicleRecordSyncEventId())
        );

    }

//    @Scheduled(fixedRate = 120000, initialDelay = 10000)
    //@Scheduled(fixedRate = 60000, initialDelay = 1000)
    @Transactional
    public void process (){
        
        Instant eventDateStart = Instant.now();

        // find all pending record in ick_vehicle_record
        List<IckVehicleRecordRepository.PendingRecordDto> PendingRecordDtoList = ickVehicleRecordRepository.findAllPendingRecords();

        // mps flow
        PendingRecordDtoList.forEach(
                dto -> {

                    // no defination for checking new ick record therefore compare secondSearchEventDate and secondSearchEventTime >= 2hrs
                    Boolean isNewIckRecord = dto.iv() == null || (TimeUtil.toInstant(dto.iv().getSecondSearchEventDate().toLocalDate(), dto.iv().getSecondSearchEventTime().toLocalTime()).plus(Duration.ofHours(2))
                            .compareTo(TimeUtil.toInstant(dto.ivr().getSecondSearchEventDate().toLocalDate(), dto.ivr().getSecondSearchEventTime().toLocalTime())) < 0);

                    Boolean isInSecondSearch = dto.ivr().getSecondSearchFlag()==1;

                    ICKClearanceStatus ickClearanceStatus = ICKClearanceStatus.valueOf(dto.ivr().getClearanceFlag());

//                    Boolean isClearance = dto.ivr().getClearanceFlag()==ICKClearanceStatus.CHECKED.value();

                    Boolean isSecondSearchFlagUpdated = dto.iv()==null?null:dto.iv().getSecondSearchFlag() != dto.ivr().getSecondSearchFlag();

                    Boolean isClearanceFlagUpdated = dto.iv()==null?null:dto.iv().getClearanceFlag() != dto.ivr().getClearanceFlag();
/*

                    log.info(dto.toString());
                    log.info(isNewIckRecord.toString());
                    log.info(isInSecondSearch.toString());
                    log.info(
                            Optional.ofNullable(isSecondSearchFlagUpdated)
                                    .map(Objects::toString)
                                    .toString()
                    );
*/

                    /*
                    isNewIckRecord
                        isInSecondSearch
                            isClearance==1
                                nothing
                            isClearance==2
                                allocate
                        !isInSecondSearch
                            allocate
                     */

                    if(
                            (isNewIckRecord && isInSecondSearch && ickClearanceStatus==ICKClearanceStatus.CHECKED) ||
                            (isNewIckRecord && !isInSecondSearch)
                    )
                        mpsClient.allocationMPSCabin(MPSClient.AllocationMPSCabinRequest.builder()
                                        .parkId("1")
                                        .gateId("1")
                                        .vehicleHongkong(dto.ivr().getVehicleHongkong())
                                        .vehicleMainland(dto.ivr().getVehicleMainland())
                                        .vehicleMacao(dto.ivr().getVehicleMacao())
                                        .height(dto.ivr().getHeight())
                                        .length(dto.ivr().getLength())
                                        .width(dto.ivr().getWidth())
                                .build());
                    /*
                    !isNewIckRecord
                        isSecondSearchFlagUpdated
                            isInSecondSearch
                                isClearance==1
                                    cancel
                                isClearance==2
                                    allocate
                            !isInSecondSearch
                                allocate
                     */

                    else if(
                            (!isNewIckRecord && isSecondSearchFlagUpdated && isInSecondSearch && ickClearanceStatus==ICKClearanceStatus.UNCHECKED)
                    )
                        mpsClient.cancelAllocationMpsCabin(MPSClient.CancelAllocationMpsCabinRequest.builder()
                                .vehicleHongkong(dto.ivr().getVehicleHongkong())
                                .vehicleMainland(dto.ivr().getVehicleMainland())
                                .vehicleMacao(dto.ivr().getVehicleMacao())
                                .build());

                    else if(
                            (!isNewIckRecord && isSecondSearchFlagUpdated && isInSecondSearch && ickClearanceStatus==ICKClearanceStatus.CHECKED) ||
                            (!isNewIckRecord && isSecondSearchFlagUpdated && !isInSecondSearch)
                    )
                        mpsClient.allocationMPSCabin(MPSClient.AllocationMPSCabinRequest.builder()
                                .parkId("1")
                                .gateId("1")
                                .vehicleHongkong(dto.ivr().getVehicleHongkong())
                                .vehicleMainland(dto.ivr().getVehicleMainland())
                                .vehicleMacao(dto.ivr().getVehicleMacao())
                                .height(dto.ivr().getHeight())
                                .length(dto.ivr().getLength())
                                .width(dto.ivr().getWidth())
                                .build());


                    /*
                    isNewIckRecord
                    isInSecondSearch
                    isSecondSearchFlagUpdated
                    isClearance -> ickClearanceStatus
                    isClearanceFlagUpdated

                    !isNewIckRecord
                        !isSecondSearchFlagUpdated
                            isInSecondSearch
                                isClearanceFlagUpdated
                                    isClearance==1
                                        cancel
                                    isClearance==2
                                        allocate
                            !isInSecondSearch
                                nothing
                     */

                    else if(
                            (!isNewIckRecord && !isSecondSearchFlagUpdated && isInSecondSearch && isClearanceFlagUpdated && ickClearanceStatus==ICKClearanceStatus.UNCHECKED)
                    )
                        mpsClient.cancelAllocationMpsCabin(MPSClient.CancelAllocationMpsCabinRequest.builder()
                                .vehicleHongkong(dto.ivr().getVehicleHongkong())
                                .vehicleMainland(dto.ivr().getVehicleMainland())
                                .vehicleMacao(dto.ivr().getVehicleMacao())
                                .build());

                    else if(
                            (!isNewIckRecord && !isSecondSearchFlagUpdated && isInSecondSearch && isClearanceFlagUpdated && ickClearanceStatus==ICKClearanceStatus.CHECKED)
                    )
                        mpsClient.allocationMPSCabin(MPSClient.AllocationMPSCabinRequest.builder()
                                .parkId("1")
                                .gateId("1")
                                .vehicleHongkong(dto.ivr().getVehicleHongkong())
                                .vehicleMainland(dto.ivr().getVehicleMainland())
                                .vehicleMacao(dto.ivr().getVehicleMacao())
                                .height(dto.ivr().getHeight())
                                .length(dto.ivr().getLength())
                                .width(dto.ivr().getWidth())
                                .build());



                }
        );
        // save ick_vehicle_record_process_event
        Instant eventDateEnd = Instant.now();

        IckVehicleRecordProcessEvent ickVehicleRecordProcessEvent = ickVehicleRecordProcessEventRepository.save(IckVehicleRecordProcessEvent.builder()
                .eventDateStart(eventDateStart)
                .eventDateEnd(eventDateEnd)
                .build());

        PendingRecordDtoList.forEach(
                dto -> {

                    // save or update ick_vehicle
                    IckVehicle ickVehicle = dto.iv()==null?new IckVehicle():dto.iv();

                    ickVehicle.setVehicleHongkong(dto.ivr().getVehicleHongkong());
                    ickVehicle.setVehicleMainland(dto.ivr().getVehicleMainland());
                    ickVehicle.setVehicleMacao(dto.ivr().getVehicleMacao());
                    ickVehicle.setHeight(dto.ivr().getHeight());
                    ickVehicle.setLength(dto.ivr().getLength());
                    ickVehicle.setWidth(dto.ivr().getWidth());
                    ickVehicle.setSecondSearchFlag(dto.ivr().getSecondSearchFlag());
                    ickVehicle.setClearanceFlag(dto.ivr().getClearanceFlag());
                    ickVehicle.setSecondSearchEventDate(dto.ivr().getSecondSearchEventDate());
                    ickVehicle.setSecondSearchEventTime(dto.ivr().getSecondSearchEventTime());
                    ickVehicle.setIckVehicleRecordSyncEventId(dto.ivr().getIckVehicleRecordSyncEventId());
                    ickVehicle.setIckVehicleRecordProcessEventId(ickVehicleRecordProcessEvent.getIckVehicleRecordProcessEventId());
                    // Brian 2025-05-08 For save ick_vehicle
                    ickVehicle.setBookingId(dto.ivr().getBookingId());
                    ickVehicle.setHandicapped(dto.ivr().getHandicapped());
                    ickVehicle.setPrimaryVehicleRegion(dto.ivr().getPrimaryVehicleRegion());

                    // Brian 2025-05-08 For save ick_vehicle
                    ickVehicleRepository.save(ickVehicle);
                    
                    // update ick_vehicle_record
                    dto.ivr().setIckVehicleRecordProcessEventId(ickVehicleRecordProcessEvent.getIckVehicleRecordProcessEventId());
                    
                }
        );
    }
}
