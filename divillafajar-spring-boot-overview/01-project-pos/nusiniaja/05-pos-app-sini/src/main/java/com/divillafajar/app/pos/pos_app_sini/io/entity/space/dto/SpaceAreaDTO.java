package com.divillafajar.app.pos.pos_app_sini.io.entity.space.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO untuk SpaceAreaEntity — bisa digunakan untuk response API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpaceAreaDTO {

	private Long id;
	private String name;
	private Long indentLevel;

	// Parent dan children pakai versi ringkas agar hindari recursive loop
	private SimpleSpaceAreaDTO parent;
	private List<SimpleSpaceAreaDTO> children;

	// Relasi ke client address (cukup pakai id dan nama kalau perlu)
	private Long clientAddressId;
	private String clientAddressPubId;
	private String clientAddressName;

	private List<GuestAreaDTO> guestAreas;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private String createdBy;
	private String updatedBy;

	private Boolean deleted;

	// Opsional helper (bisa diisi di mapping layer)
	private String fullPathName;
}
