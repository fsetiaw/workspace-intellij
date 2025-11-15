package com.divillafajar.app.pos.pos_app_sini.utils;

import com.divillafajar.app.pos.pos_app_sini.io.entity.space.SpaceAreaEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.space.dto.SimpleSpaceAreaDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.space.dto.SpaceAreaDTO;

public class SpaceAreaMapper {
	public static SpaceAreaDTO toDTO(SpaceAreaEntity entity) {
		if (entity == null) return null;

		return SpaceAreaDTO.builder()
				.id(entity.getId())
				.name(entity.getName())
				.indentLevel(entity.getIndentLevel())
				.parent(entity.getParent() != null ?
						new SimpleSpaceAreaDTO(entity.getParent().getId(), entity.getParent().getName()) : null)
				.children(entity.getChildren() != null ?
						entity.getChildren().stream()
								.map(child -> new SimpleSpaceAreaDTO(child.getId(), child.getName()))
								.toList() : null)
				.clientAddressId(entity.getClientAddress() != null ? entity.getClientAddress().getId() : null)
				.clientAddressPubId(entity.getClientAddress() != null ? entity.getClientAddress().getPubId() : null)
				.clientAddressName(entity.getClientAddress() != null ? entity.getClientAddress().getAddressName() : null)
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.createdBy(entity.getCreatedBy())
				.updatedBy(entity.getUpdatedBy())
				.deleted(entity.getDeleted())
				.path(entity.getFullPathName())
				.build();
	}
}
