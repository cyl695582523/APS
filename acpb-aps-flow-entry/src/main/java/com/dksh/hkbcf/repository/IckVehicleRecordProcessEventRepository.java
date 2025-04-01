package com.dksh.hkbcf.repository;

import com.dksh.hkbcf.model.IckVehicleRecordProcessEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IckVehicleRecordProcessEventRepository extends JpaRepository<IckVehicleRecordProcessEvent, Long> {

}
