package com.divillafajar.app.pos.pos_app_sini.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubCategoryProductRespModel {
    private Long id;
    private String name;
    private Long parentId;
    private Long indentLevel;
    private String clientAddressPubId;
    private String message;
	private String username;
}
