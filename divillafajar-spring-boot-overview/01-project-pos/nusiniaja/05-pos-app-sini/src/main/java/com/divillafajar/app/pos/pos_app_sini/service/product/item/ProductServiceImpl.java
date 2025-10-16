package com.divillafajar.app.pos.pos_app_sini.service.product.item;

import com.divillafajar.app.pos.pos_app_sini.io.entity.product.ProductDTO;
import com.divillafajar.app.pos.pos_app_sini.model.item.CreateItemRequestModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
	@Override
	public ProductDTO addNewProduct(CreateItemRequestModel createItemRequestModel) {
		ProductDTO retVal = new ProductDTO();
		System.out.println("ngantuk = "+createItemRequestModel.getName());
		System.out.println("ngantuk = "+createItemRequestModel.getDesc());
		System.out.println("ngantuk = "+createItemRequestModel.getPath());
		return retVal;
	}
}
