package com.dksh.hkbcf.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;


@Entity
@Table(name = "ick_vehicle_record_sync_event")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IckVehicleRecordSyncEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer ickVehicleRecordSyncEventId;

    @Column
    Instant eventDateStart;

    @Column
    Instant eventDateEnd;

    @Column
    Instant requestStartTime;

    @Column
    Instant requestEndTime;

    @Column
    @CreationTimestamp(source = SourceType.DB)
    Instant dateAdded;

    @Column
    @UpdateTimestamp(source = SourceType.DB)
    Instant dateModified;
}
