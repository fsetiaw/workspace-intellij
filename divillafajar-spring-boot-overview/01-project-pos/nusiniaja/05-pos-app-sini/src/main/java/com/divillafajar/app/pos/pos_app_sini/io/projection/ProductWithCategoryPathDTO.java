package com.divillafajar.app.pos.pos_app_sini.io.projection;

public interface ProductWithCategoryPathDTO {
	Long getId();
	String getName();
	String getDescription();
    String getStatus();
	String getPrice();
	Long getStockQuantity();
	Long getMinStockAlert();
	String getCategoryName();
	String getCategoryPath();
	String getTopCategoryName();
	String getThumbnailUrl();
	String getImageUrl();
}
