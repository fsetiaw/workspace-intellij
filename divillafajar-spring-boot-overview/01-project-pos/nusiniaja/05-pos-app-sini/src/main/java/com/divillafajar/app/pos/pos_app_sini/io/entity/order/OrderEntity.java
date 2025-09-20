package com.divillafajar.app.pos.pos_app_sini.io.entity.order;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.area.GuestTableEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.GuestEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "OrderEntity")
@Table(name = "orders")
public class OrderEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -3742784919609967201L;

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

    // Relasi ke Guest (siapa yang pesan/meja dipakai)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false)
    private GuestEntity guest;

    // Relasi ke Table (meja mana yang dipakai)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private GuestTableEntity table;

    // Contoh kolom tambahan
    @Column(name = "order_time", nullable = false)
    private LocalDateTime orderTime = LocalDateTime.now();

    @Column(name = "status", length = 50, nullable = false)
    private String status = "ACTIVE"; // contoh: ACTIVE, COMPLETED, CANCELLED
}
