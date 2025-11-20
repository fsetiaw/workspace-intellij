package com.divillafajar.app.pos.pos_app_sini.io.entity.space.unit;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.space.SpaceAreaEntity;
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
@Table(name = "area_unit")
public class AreaUnitEntity {

	@PrePersist
	protected void onCreate() {
		if (this.createdAt == null)
			this.createdAt = LocalDateTime.now();
		if (this.deleted == null)
			this.deleted = false;
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// ============================
	// 🔹 Relation to Space Area
	// ============================
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "space_area_id", nullable = false)
	private SpaceAreaEntity spaceArea;

	// ============================
	// 🔹 Unit Basic Info
	// ============================
	@Column(nullable = false, length = 255)
	private String name;  // contoh: "Cabin A", "Room 1", "Table 12"

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(nullable = false, length = 255)
	private String area_type; //contoh room, balkon, kamar mandi

	@Column(length = 100)
	private String code;  // optional unique code

	@Column(length = 25)
	private String capacity_unit;  // optional unique code

	@Column
	private Integer capacity; // kapasitas: tamu/meja/orang

	@Column(name = "size_m2")
	private Double sizeM2; // luas (optional)

	@Column
	private Integer floor;   // optional: lantai/unit level

	// ============================
	// 🔹 Operational Status
	// ============================
	@Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Boolean isActive = true;

	@Column(name = "is_available", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Boolean isAvailable = true;

	@Column(name = "blocked_from")
	private LocalDateTime blockedFrom;

	@Column(name = "blocked_until")
	private LocalDateTime blockedUntil;

	@Column(length = 100)
	private String blockedDesc;  // keterangan bloced , maintenance

	// ============================
	// 🔹 Client Address
	// ============================
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_address_id")
	private ClientAddressEntity clientAddress;

	// ============================
	// 🔹 Metadata
	// ============================
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "created_by", length = 100)
	private String createdBy;

	@Column(name = "updated_by", length = 100)
	private String updatedBy;

	@Column(name = "deleted", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Boolean deleted = false;

}
