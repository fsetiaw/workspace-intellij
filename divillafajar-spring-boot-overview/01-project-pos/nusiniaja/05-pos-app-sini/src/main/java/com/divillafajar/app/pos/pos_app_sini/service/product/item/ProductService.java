package com.divillafajar.app.pos.pos_app_sini.service.product.item;

import com.divillafajar.app.pos.pos_app_sini.io.projection.ProductItemSummaryProjectionDTO;
import com.divillafajar.app.pos.pos_app_sini.io.projection.ProductWithCategoryPathDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.product.ProductDTO;
import com.divillafajar.app.pos.pos_app_sini.model.item.CreateItemRequestModel;
import com.divillafajar.app.pos.pos_app_sini.model.item.UpdateItemRequestModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
	ProductDTO addNewProduct(Long catId, ClientAddressDTO dto, CreateItemRequestModel createItemRequestModel);
    ProductDTO updateProduct(Long itemId, UpdateItemRequestModel updateItemRequestModel);
    List<ProductWithCategoryPathDTO> getListProduct(String clietnAddressPubId, Long categoryId, String keywordOptional);
	ProductItemSummaryProjectionDTO getSummaryProductItem(String clietnAddressPubId);
    void softDeleteProduct(Long itemId);
}
