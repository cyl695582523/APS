package com.dksh.hkbcf.repository;

import com.dksh.hkbcf.model.IckVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IckVehicleRepository extends JpaRepository<IckVehicle, Long> {
     IckVehicle findByVehicleHongkongAndVehicleMainlandAndVehicleMacaoAllIgnoreCase(String vehicleHongkong, String vehicleMainland, String vehicleMacao);
//     @Query("select iv from IckVehicle iv where (LOWER(iv.vehicleHongkong) = LOWER(:vehicleHongkong) and iv.vehicleHongkong<>'') or (LOWER(iv.vehicleMainland) = LOWER(:vehicleMainland) and iv.vehicleMainland<>'') or (LOWER(iv.vehicleMacao) = LOWER(:vehicleMacao) and iv.vehicleMacao<>'') order by iv.secondSearchEventDate desc, iv.secondSearchEventTime desc limit 1")
     @Query("select iv from IckVehicle iv where (LOWER(iv.vehicleHongkong) = LOWER(:vehicleHongkong) or ((iv.vehicleHongkong='' or iv.vehicleHongkong is null) and :vehicleHongkong='')) and (LOWER(iv.vehicleMainland) = LOWER(:vehicleMainland) or ((iv.vehicleMainland='' or iv.vehicleMainland is null) and :vehicleMainland='')) and (LOWER(iv.vehicleMacao) = LOWER(:vehicleMacao) or ((iv.vehicleMacao='' or iv.vehicleMacao is null) and :vehicleMacao='')) order by iv.secondSearchEventDate desc, iv.secondSearchEventTime desc limit 1")
     IckVehicle findAllICKRecords(@Param("vehicleHongkong") String vehicleHongkong,
                                  @Param("vehicleMainland") String vehicleMainland,
                                  @Param("vehicleMacao") String vehicleMacao);
}
