package com.divillafajar.app.pos.pos_app_sini.service.product.category;

import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryDTO;
import com.divillafajar.app.pos.pos_app_sini.io.projection.CategorySummaryProjection;
import com.divillafajar.app.pos.pos_app_sini.model.product.CategorySearchResultModel;
import com.divillafajar.app.pos.pos_app_sini.model.product.ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId;

import java.util.List;


public interface ProductCategoryService {
    ProductCategoryDTO addNewProductCategory(String categoryName, String pAid) throws Exception;
    ProductCategoryDTO updateProductCategory(Long categoryId, String categoryName, String pAid);
    ProductCategoryDTO addSubProductCategory(Long parentId, String categoryName, String pAid);
    List<ProductCategoryDTO> getCategoryAndSubCategoryByClientAddressPubId(String pAid);
	void deleteCategory(Long catId);
	List<CategorySearchResultModel> searchCategory(String pAid, String kword);
    List<ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId> getPathToEachEndChildCategoryByClientAddressPubId(String pAid);
    List<ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId> getPathToEachEndChildCategoryByClientAddressPubId(String pAid, String Filter);
	boolean locationHasCategoryProduct(String pAid);
    boolean locationHasItemProduct(String pAid);
	void createDefaultCategory(String lang, String clientAddressPubId);
    void resetCategoryByClientAddress(String clientAddressPubId);
	Boolean isCategoryHasAnItemProduct(long catId);
    CategorySummaryProjection getSummaryProductCategory(String clientAddressPubId);
}
