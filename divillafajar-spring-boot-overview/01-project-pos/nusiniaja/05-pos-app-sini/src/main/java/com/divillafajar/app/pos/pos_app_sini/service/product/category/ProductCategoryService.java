package com.divillafajar.app.pos.pos_app_sini.service.product.category;

import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryDTO;

import java.util.List;


public interface ProductCategoryService {
    ProductCategoryDTO addNewProductCategory(String categoryName, String pAid) throws Exception;
    ProductCategoryDTO updateProductCategory(Long categoryId, String categoryName, String pAid);
    ProductCategoryDTO addSubProductCategory(Long parentId, String categoryName, String pAid);
    List<ProductCategoryDTO> getCategoryAndSubCategoryByClientAddressPubId(String pAid);
	void deleteCategory(Long catId);
}
