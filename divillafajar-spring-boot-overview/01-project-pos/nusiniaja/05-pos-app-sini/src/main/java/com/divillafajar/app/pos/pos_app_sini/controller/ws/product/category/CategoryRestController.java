package com.divillafajar.app.pos.pos_app_sini.controller.ws.product.category;

import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryDTO;
import com.divillafajar.app.pos.pos_app_sini.service.product.category.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/manager/product")
@RequiredArgsConstructor
public class CategoryRestController {

    private final ProductCategoryService categoryService;

    //public CategoryRestController(CategoryService categoryService) {
    //    this.categoryService = categoryService;
    //}

    // Update category pakai session-based auth
    @PutMapping("/category/{id}")
    public ResponseEntity<ProductCategoryDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody ProductCategoryDTO dto) {
        System.out.println("Rest Controller updateCategory");
        ProductCategoryDTO retVal = new ProductCategoryDTO();
        retVal.setId(id);
        retVal.setName(dto.getName());
        //ProductCategoryDTO updated = categoryService.update(id, dto);
        return ResponseEntity.ok(retVal);
        /*
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);

         */
    }
}