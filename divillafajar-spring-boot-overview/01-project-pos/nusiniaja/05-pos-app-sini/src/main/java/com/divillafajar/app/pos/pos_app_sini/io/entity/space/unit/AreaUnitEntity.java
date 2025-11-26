package com.divillafajar.app.pos.pos_app_sini.io.entity.space.unit;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.space.SpaceAreaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

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
		if (this.isActive == null)
			this.isActive = false;
		if (this.isAvailable == null)
			this.isAvailable = false;
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
	// 🔹 Accommodation Unit Info
	// ============================

	// Basic
	@Column(name = "path")
	private String path;

	@Column(name = "area_type")
	private String areaType;

	@Column(name = "name")
	private String name;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	// Accommodation details
	@Column(name = "tfloor")
	private String tfloor;

	@Column(name = "troom")
	private String troom;

	@Column(name = "tliving")
	private String tliving;

	@Column(name = "tbath")
	private String tbath;

	@Column(name = "tarea")
	private String tArea;

	// Feature Facilities (List<String> as JSON)
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "json")
	private List<String> featureFacilities;

	@Column(name = "other_feature")
	private String otherFeature;

	// General/Standard Facilities
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "json")
	private List<String> generalFacilities;

	@Column(name = "other_standard")
	private String otherStandard;

	// On Demand Facilities
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "json")
	private List<String> onDemandFacilities;

	@Column(name = "other_on_demand")
	private String otherOnDemand;

	// Add-on Service
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "json")
	private List<String> addOnService;

	@Column(name = "other_add_on")
	private String otherAddOn;

	@Column(length = 25)
	private String capacity_unit;  // satuan : orang / meja / kursi

	// kapasitas: tamu/meja/orang
	@Column
	private Integer capacity; //adult capacity kalo untuk akomodasi

	@Column(name = "child_age_restriction")
	private Integer chileAgeRestriction;


	// ============================
	// 🔹 Operational Status
	// ============================
	@Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Boolean isActive;

	@Column(name = "is_available", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Boolean isAvailable;

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
