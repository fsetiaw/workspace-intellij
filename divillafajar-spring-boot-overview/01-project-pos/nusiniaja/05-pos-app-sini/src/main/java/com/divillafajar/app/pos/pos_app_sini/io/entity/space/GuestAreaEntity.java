package com.divillafajar.app.pos.pos_app_sini.io.entity.space;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "guest_area")
public class GuestAreaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔹 Nama area tamu (misal: "Meja 1", "Outdoor 2", "Private Room 1")
    @Column(nullable = false, length = 255)
    private String name;

    // 🔹 Jenis meja atau area (misal: "Table", "Sofa", "Room")
    @Column(name = "type", length = 100)
    private String type;

    // 🔹 Jumlah kursi di area tamu
    @Column(name = "seat_count")
    private Integer seatCount;

    // 🔹 Flag fasilitas
    @Column(name = "has_ac", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean hasAc = false;

    @Column(name = "is_smoking", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isSmoking = false;

    @Column(name = "is_outdoor", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isOutdoor = false;

    @Column(name = "has_wc", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean hasWc = false;

    // 🔹 Relasi ke SpaceArea (setiap guest area milik satu space)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_area_id")
    private SpaceAreaEntity spaceArea;

    // 🔹 Relasi opsional ke ClientAddress (kalau mau langsung tahu lokasi)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_address_id")
    private ClientAddressEntity clientAddress;

    // === Metadata ===
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "deleted", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean deleted = false;

	@Column(columnDefinition = "JSON")
	private String operationHours;

    // === Helper ===
    @Transient
    public String getDescription() {
        StringBuilder desc = new StringBuilder();

        if (seatCount != null)
            desc.append(seatCount).append(" seats");

        if (Boolean.TRUE.equals(hasAc))
            desc.append(" • AC");
        if (Boolean.TRUE.equals(isSmoking))
            desc.append(" • Smoking");
        if (Boolean.TRUE.equals(isOutdoor))
            desc.append(" • Outdoor");
        if (Boolean.TRUE.equals(hasWc))
            desc.append(" • WC");

        return desc.toString();
    }
}
