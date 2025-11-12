package com.divillafajar.app.pos.pos_app_sini.io.entity.space.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO untuk GuestAreaEntity (bagian dari SpaceAreaDTO).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestAreaDTO {
	private Long id;
	private String name;
	private Boolean deleted;
}
