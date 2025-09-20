package com.divillafajar.app.pos.pos_app_sini.io.entity.client.area;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Time;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ClientAreaEntity")
@Table(name = "area")
public class ClientAreaEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 5013540749111870107L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pub_id", updatable = false, nullable = false)
    private String pubId;

    @PrePersist
    public void generateId() {
        if (pubId == null) {
            pubId = UUID.randomUUID().toString(); // UUID jadi string
        }
    }

    @Column(name = "area_name", length = 99, nullable = false)
    private String areaName;

    @Column(name = "alias", length = 255, nullable = true)
    private String alias;

    @Column(name = "location")
    private String location;

    @Column(name = "reservation_type")
    private String reservationType;

    @Column(name = "cooling_system")
    private String coolingSystem;

    @Column(name = "room_type")
    private String room_type;

    @Column(name = "room_function")
    private String roomFunction;

    @Column(name = "starting_operation_time", nullable = true)
    private Time startingOperationTime;

    @Column(name = "duration_operation_hour", nullable = true)
    private Long durationOperationHour;

    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL)
    private List<GuestTableEntity> tables;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH })
    @JoinColumn(name = "client_address_id")
    private ClientAddressEntity clientAddress;
}
