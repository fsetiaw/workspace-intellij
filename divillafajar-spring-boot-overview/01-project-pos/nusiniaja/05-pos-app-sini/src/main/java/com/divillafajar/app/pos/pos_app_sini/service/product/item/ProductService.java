package com.divillafajar.app.pos.pos_app_sini.service.product.item;

import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductWithCategoryPathDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.product.ProductDTO;
import com.divillafajar.app.pos.pos_app_sini.model.item.CreateItemRequestModel;

import java.util.List;

public interface ProductService {
	ProductDTO addNewProduct(Long catId, ClientAddressDTO dto, CreateItemRequestModel createItemRequestModel);
	List<ProductWithCategoryPathDTO> getListProduct(String clietnAddressPubId, Long categoryId);
}
