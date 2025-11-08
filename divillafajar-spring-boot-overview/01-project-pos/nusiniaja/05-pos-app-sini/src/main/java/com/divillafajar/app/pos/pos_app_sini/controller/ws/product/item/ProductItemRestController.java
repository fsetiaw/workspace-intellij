package com.divillafajar.app.pos.pos_app_sini.controller.ws.product.item;

import com.divillafajar.app.pos.pos_app_sini.io.entity.product.ProductDTO;
import com.divillafajar.app.pos.pos_app_sini.model.item.UpdateItemRequestModel;
import com.divillafajar.app.pos.pos_app_sini.service.product.item.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/manager/product/item")
@RequiredArgsConstructor
public class ProductItemRestController {

    private final ProductService productService;
	private final ModelMapper modelMapper;

	@PutMapping("/{itemId}")
	public ResponseEntity<?> editItem(@PathVariable Long itemId, @RequestBody UpdateItemRequestModel dto) {
		System.out.println("===REST EDIT ITEM CONTROLLER====");
		System.out.println("Input Nama: " + dto.getName());
		System.out.println("Input Deskripsi: " + dto.getDescription());
		System.out.println("Input Harga: " + dto.getPrice());
		UpdateItemRequestModel retVal = new UpdateItemRequestModel();
        ProductDTO updated = productService.updateProduct(itemId, dto);
		System.out.println("ID: " + itemId);
		System.out.println("Nama: " + dto.getName());
		System.out.println("Deskripsi: " + dto.getDescription());
		System.out.println("Harga: " + dto.getPrice());
		//System.out.println("Stok: " + dto.getStock());
        //retVal.setName("Updated Name");
        //retVal.setDescription("Updated Desc");
        //retVal.setPrice(21212D);
        retVal = modelMapper.map(updated, UpdateItemRequestModel.class);
		//retVal = modelMapper.map(dto, UpdateItemRequestModel.class);
		return ResponseEntity.ok(retVal);
	}

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable Long itemId) {
        System.out.println("===REST DELETE ITEM CONTROLLER====");
        UpdateItemRequestModel retVal = new UpdateItemRequestModel();
        return ResponseEntity.ok(retVal);
    }
}


