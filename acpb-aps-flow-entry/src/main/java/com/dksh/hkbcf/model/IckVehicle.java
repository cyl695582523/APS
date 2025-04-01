package com.dksh.hkbcf.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.sql.Time;
import java.time.Instant;


@Entity
@Table(name = "ick_vehicle")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IckVehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer ickVehicleId;

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

    // Brian 2025-03-10
    @Column
    String bookingId;

    @Column
    Integer handicapped;

    @Column
    String primaryVehicleRegion;

    /*
    String bookingId
    String primaryVehicleRegion
    Integer handicapped

     */
}
