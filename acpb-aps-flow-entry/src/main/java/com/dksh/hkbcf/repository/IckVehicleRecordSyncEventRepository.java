package com.dksh.hkbcf.repository;

import com.dksh.hkbcf.model.IckVehicleRecordSyncEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IckVehicleRecordSyncEventRepository extends JpaRepository<IckVehicleRecordSyncEvent, Long> {
    IckVehicleRecordSyncEvent findFirstByOrderByIckVehicleRecordSyncEventIdDesc();

}
