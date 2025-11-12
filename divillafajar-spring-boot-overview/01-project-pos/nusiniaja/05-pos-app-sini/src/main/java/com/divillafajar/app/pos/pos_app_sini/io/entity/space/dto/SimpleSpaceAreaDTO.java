package com.divillafajar.app.pos.pos_app_sini.io.entity.space.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO ringkas untuk nested area (parent/children).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleSpaceAreaDTO {
	private Long id;
	private String name;
}