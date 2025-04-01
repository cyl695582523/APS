package com.dksh.hkbcf.repository;

import com.dksh.hkbcf.model.IckVehicle;
import com.dksh.hkbcf.model.IckVehicleRecord;
import lombok.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Repository
public interface IckVehicleRecordRepository extends JpaRepository<IckVehicleRecord, Long> {

    record PendingRecordDto (IckVehicleRecord ivr, IckVehicle iv){}
    @Query("select NEW com.dksh.hkbcf.repository.IckVehicleRecordRepository$PendingRecordDto(ivr, iv) from IckVehicleRecord ivr left join IckVehicle iv on (ivr.vehicleHongkong = iv.vehicleHongkong or (ivr.vehicleHongkong is null and iv.vehicleHongkong is null)) and (ivr.vehicleMainland = iv.vehicleMainland or (ivr.vehicleMainland is null and iv.vehicleMainland is null)) and (ivr.vehicleMacao = iv.vehicleMacao or (ivr.vehicleMacao is null and iv.vehicleMacao is null)) and (ivr.height = iv.height or (ivr.height is null and iv.height is null)) and (ivr.length = iv.length or (ivr.length is null and iv.length is null)) and (ivr.width = iv.width or (ivr.width is null and iv.width is null)) where ivr.ickVehicleRecordId in ( select max(ickVehicleRecordId) from IckVehicleRecord where ickVehicleRecordProcessEventId is null group by vehicleHongkong,vehicleMainland,vehicleMacao,height,length,width)")
    List<PendingRecordDto> findAllPendingRecords();



}
