package com.divillafajar.app.pos.pos_app_sini.io.projection.area;

public interface SpaceAreaHierarchyProjection {
	Long getId();
	String getName();
	Long getClientAddressId();
	Long getParentId();
	Long getIndentLevel();
}
