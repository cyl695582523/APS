package com.dksh.hkbcf.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.sql.Time;
import java.time.Instant;


@Entity
@Table(name = "ick_vehicle_record")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IckVehicleRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer ickVehicleRecordId;

    @Column
    String vehicleHongkong;

    @Column
    String vehicleMainland;

    @Column
    String vehicleMacao;

    @Column
    Integer height;

    @Column
    Integer length;

    @Column
    Integer width;

    @Column(columnDefinition = "TINYINT")
    Integer secondSearchFlag;

    @Column(columnDefinition = "TINYINT")
    Integer clearanceFlag;

    @Column
    Date secondSearchEventDate;

    @Column
    Time secondSearchEventTime;

    @Column
    @CreationTimestamp(source = SourceType.DB)
    Instant dateAdded;

    @Column
    @UpdateTimestamp(source = SourceType.DB)
    Instant dateModified;

    @Column
    Integer ickVehicleRecordSyncEventId;

    @Column
    Integer ickVehicleRecordProcessEventId;

//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumns({
//            @JoinColumn(name="vehicleHongkong")
////            @JoinColumn(referencedColumnName="vehicleMainland"),
////            @JoinColumn(referencedColumnName="vehicleMacao"),
////            @JoinColumn(referencedColumnName="height"),
////            @JoinColumn(referencedColumnName="length"),
////            @JoinColumn(referencedColumnName="width")
//    })
//    IckVehicle ickVehicle;
//
}
