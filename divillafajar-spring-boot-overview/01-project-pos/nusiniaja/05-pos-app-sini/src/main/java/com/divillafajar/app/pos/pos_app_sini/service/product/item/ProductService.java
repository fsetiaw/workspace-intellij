package com.divillafajar.app.pos.pos_app_sini.service.product.item;

import com.divillafajar.app.pos.pos_app_sini.io.entity.product.ProductDTO;
import com.divillafajar.app.pos.pos_app_sini.model.item.CreateItemRequestModel;

public interface ProductService {
	ProductDTO addNewProduct(CreateItemRequestModel createItemRequestModel);
}
