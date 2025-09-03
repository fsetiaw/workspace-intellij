package com.divillafajar.app.pos.pos_app_sini.io.entity.client.area;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Time;
import java.util.List;

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

    @Column(name = "area_name", length = 99, nullable = false)
    private String areaName;

    @Column(name = "area_desc", length = 255, nullable = true)
    private String areaDesc;

    @Column(name = "outdoor")
    private Boolean outdoor;

    @Column(name = "non_smoking")
    private Boolean nonSmoking;

    @Column(name = "open_time", nullable = true)
    private Time openTime;

    @Column(name = "close_time", nullable = true)
    private Time closeTime;

    @Column(name = "ac")
    private Boolean ac;

    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL)
    private List<GuestTableEntity> tables;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "client_address_id")
    private ClientAddressEntity clientAddress;
}
