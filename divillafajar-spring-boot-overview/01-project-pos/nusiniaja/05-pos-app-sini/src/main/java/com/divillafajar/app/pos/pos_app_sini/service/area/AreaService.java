package com.divillafajar.app.pos.pos_app_sini.service.area;

import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.area.dto.ClientAreaDTO;
import com.divillafajar.app.pos.pos_app_sini.io.projection.CategorySummaryProjection;
import com.divillafajar.app.pos.pos_app_sini.io.projection.area.AreaSummaryProjection;

import java.util.List;
import java.util.Optional;

public interface AreaService {
	boolean locationHasArea(String pAid);
	AreaSummaryProjection getAreaAndSubAreaByClientAddressPubId(String clientAddressPubId);
	ProductCategoryDTO addNewMainArea(String categoryName, String pAid) throws Exception;
}
