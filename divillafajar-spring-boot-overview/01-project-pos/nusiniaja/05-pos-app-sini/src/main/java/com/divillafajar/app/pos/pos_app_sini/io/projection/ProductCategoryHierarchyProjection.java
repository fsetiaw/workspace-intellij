package com.divillafajar.app.pos.pos_app_sini.io.projection;

public interface ProductCategoryHierarchyProjection {
	Long getId();
	String getName();
	Long getClientAddressId();
	Long getParentId();
	Long getIndentLevel();
}
