package com.divillafajar.app.pos.pos_app_sini.controller.ws.product.category;

import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.model.product.RequestItemSubItemModel;
import com.divillafajar.app.pos.pos_app_sini.service.product.category.ProductCategoryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/manager/product")
@RequiredArgsConstructor
public class CategoryRestController {

    private final ProductCategoryService categoryService;

    @PostMapping("/category")
    public ResponseEntity<ProductCategoryDTO> addNewCategory(
            HttpSession session,
            @RequestBody RequestItemSubItemModel dto) {
        System.out.println("Rest Controller AddNewCategory");
        ClientAddressDTO address = (ClientAddressDTO) session.getAttribute("targetAddress");
        System.out.println("showCategoryHome dto = "+address.getPubId());
        ProductCategoryDTO retVal = new ProductCategoryDTO();
        try {
            retVal.setId(222L);
            retVal.setName(dto.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //ProductCategoryDTO updated = categoryService.update(id, dto);
        System.out.println("returning");
        //return ResponseEntity.ok(retVal);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
    }

    // Update category pakai session-based auth
    @PutMapping("/category/{id}")
    public ResponseEntity<ProductCategoryDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody RequestItemSubItemModel dto) {
        System.out.println("Rest Controller updateCategory");
        ProductCategoryDTO retVal = new ProductCategoryDTO();
        retVal.setId(id);
        retVal.setName(dto.getName());
        //ProductCategoryDTO updated = categoryService.update(id, dto);

        return ResponseEntity.ok(retVal);
    }
    /*
    public ResponseEntity<ProductCategoryDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody ProductCategoryDTO dto) {
        System.out.println("Rest Controller updateCategory");
        ProductCategoryDTO retVal = new ProductCategoryDTO();
        retVal.setId(id);
        retVal.setName(dto.getName());
        //ProductCategoryDTO updated = categoryService.update(id, dto);
        return ResponseEntity.ok(retVal);

        //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        //        .body(null);


    }
     */
}