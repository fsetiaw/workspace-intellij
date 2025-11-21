package com.divillafajar.app.pos.pos_app_sini.model.area.unit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateUnitAreaRequestModel {
	private Long spaceAreaId;
	private String name;  // contoh: "Cabin A", "Room 1", "Table 12"
	private String description;
	private String area_type; //contoh room, balkon, kamar mandi
	private String path;
	private String clientAddressPubId;
	private String username;
}
