package com.dksh.hkbcf.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;


@Entity
@Table(name = "ick_vehicle_record_process_event")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IckVehicleRecordProcessEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer ickVehicleRecordProcessEventId;

    @Column
    Instant eventDateStart;

    @Column
    Instant eventDateEnd;

    @Column
    @CreationTimestamp(source = SourceType.DB)
    Instant dateAdded;

    @Column
    @UpdateTimestamp(source = SourceType.DB)
    Instant dateModified;
}
