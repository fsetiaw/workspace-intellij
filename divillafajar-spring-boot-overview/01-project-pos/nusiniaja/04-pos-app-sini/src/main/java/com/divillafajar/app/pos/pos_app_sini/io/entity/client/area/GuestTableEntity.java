package com.divillafajar.app.pos.pos_app_sini.io.entity.client.area;

import com.divillafajar.app.pos.pos_app_sini.io.entity.order.OrderEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "GuestTableEntity")
@Table(name = "guest_table")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuestTableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "capacity", nullable = false)
    private Long capacity;

    @Column(name = "status",nullable = false)
    private String status;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "area_id")
    private ClientAreaEntity area;

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
    private List<OrderEntity> orders;

}
