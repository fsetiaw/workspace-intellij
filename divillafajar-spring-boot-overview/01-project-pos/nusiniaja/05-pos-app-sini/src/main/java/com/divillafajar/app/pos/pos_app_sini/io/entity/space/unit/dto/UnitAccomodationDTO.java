package com.divillafajar.app.pos.pos_app_sini.io.entity.space.unit.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UnitAccomodationDTO {
	// ============================
	// 🔹 Primary ID
	// ============================
	private Long id;

	// ============================
	// 🔹 Relations (use ID only)
	// ============================
	private Long spaceAreaId;
	private Long clientAddressId;

	// ============================
	// 🔹 Basic
	// ============================
	private String path;
	private String name;
	private String description;

	// ============================
	// 🔹 Accommodation details
	// ============================
	private String tfloor;
	private String troom;
	private String tliving;
	private String tbath;
	private String tArea;

	// ============================
	// 🔹 Facilities (JSON List<String>)
	// ============================
	private List<String> featureFacilities;
	private String otherFeature;

	private List<String> generalFacilities;
	private String otherStandard;

	private List<String> onDemandFacilities;
	private String otherOnDemand;

	private List<String> addOnService;
	private String otherAddOn;

	// ============================
	// 🔹 Capacity
	// ============================
	private String capacityUnit;
	private Integer capacity;
	private Integer chileAgeRestriction;

	// ============================
	// 🔹 Operational Status
	// ============================
	private Boolean isActive;
	private Boolean isAvailable;

	private LocalDateTime blockedFrom;
	private LocalDateTime blockedUntil;
	private String blockedDesc;

	// ============================
	// 🔹 Metadata
	// ============================
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String createdBy;
	private String updatedBy;
	private Boolean deleted;
}
