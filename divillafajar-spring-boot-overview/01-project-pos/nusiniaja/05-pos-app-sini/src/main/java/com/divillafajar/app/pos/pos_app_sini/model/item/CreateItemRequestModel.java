package com.divillafajar.app.pos.pos_app_sini.model.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemRequestModel {
	String name;
	String description;
	String path;
	String clientAddressPubId;
	String username;
}
