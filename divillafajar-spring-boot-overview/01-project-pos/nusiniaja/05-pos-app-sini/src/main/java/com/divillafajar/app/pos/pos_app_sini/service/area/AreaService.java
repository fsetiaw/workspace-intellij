package com.divillafajar.app.pos.pos_app_sini.service.area;

import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.area.dto.ClientAreaDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.space.dto.SpaceAreaDTO;
import com.divillafajar.app.pos.pos_app_sini.io.projection.CategorySummaryProjection;
import com.divillafajar.app.pos.pos_app_sini.io.projection.area.AreaSummaryProjection;
import com.divillafajar.app.pos.pos_app_sini.model.product.ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId;

import java.util.List;
import java.util.Optional;

public interface AreaService {
	boolean locationHasArea(String pAid);
	AreaSummaryProjection getAreaSummary(String clientAddressPubId);
	ProductCategoryDTO addNewMainArea(String areaName, String pAid) throws Exception;
	List<SpaceAreaDTO> getAreaAndSubAreaByClientAddressPubId(String pAid);
    SpaceAreaDTO addSubMainArea(Long parentId, String areaName, String pAid);
    List<ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId> getPathToEachEndChildCategoryByClientAddressPubId(String pAid);
    List<ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId> getPathToEachEndChildCategoryByClientAddressPubId(String pAid, String filter);
}
