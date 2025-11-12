package com.divillafajar.app.pos.pos_app_sini.io.projection.area;

public interface AreaSummaryProjection {
	Long getClientAddressId();
	Long getTotalTopParent();
	Long getTotalEndChild();
	Long getTotalEndChildNoGuestArea();
}
