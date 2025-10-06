package com.divillafajar.app.pos.pos_app_sini.controller.ws.product.category;

import com.divillafajar.app.pos.pos_app_sini.exception.DuplicationErrorException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.model.product.CreateSubCategoryProductRespModel;
import com.divillafajar.app.pos.pos_app_sini.model.product.RequestItemSubItemModel;
import com.divillafajar.app.pos.pos_app_sini.model.product.UpdateCategoryProductRespModel;
import com.divillafajar.app.pos.pos_app_sini.service.product.category.ProductCategoryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/manager/product")
@RequiredArgsConstructor
public class CategoryRestController {

    private final ProductCategoryService categoryService;
    private MessageSource messageSource;

    @PostMapping("/category")
    public ResponseEntity<?> addNewCategory( //defaultnya <CreateSubCategoryProductRespModel>
            HttpSession session,
            @RequestBody RequestItemSubItemModel dto) {
        System.out.println("Rest Controller AddNewCategory");
        ClientAddressDTO address = (ClientAddressDTO) session.getAttribute("targetAddress");
        CreateSubCategoryProductRespModel retVal = new CreateSubCategoryProductRespModel();
        try {
            ProductCategoryDTO added =  categoryService.addNewProductCategory(dto.getName(), address.getPubId());
            retVal.setId(added.getId());
            retVal.setName(added.getName());
            retVal.setParentId(null);
            retVal.setClientAddressPubId(address.getPubId());
        } catch(DuplicationErrorException e) {
            Map<String, Object> body = new HashMap<>();
            body.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(body);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
        return ResponseEntity.ok(retVal);

    }

    @PostMapping("/category/sub")
    public ResponseEntity<CreateSubCategoryProductRespModel> addSubCategory(
            HttpSession session,
            @RequestBody RequestItemSubItemModel dto) {
        System.out.println("Rest Controller addSubCategory");
        ClientAddressDTO address = (ClientAddressDTO) session.getAttribute("targetAddress");
        System.out.println("showCategoryHome address = "+address.getPubId());
        System.out.println("showCategoryHome dto = "+dto.getName());
        System.out.println("showCategoryHome dto = "+dto.getParentId());
        CreateSubCategoryProductRespModel retVal = new CreateSubCategoryProductRespModel();
        try {
            ProductCategoryDTO added =  categoryService.addSubProductCategory(dto.getParentId(), dto.getName(), address.getPubId());
            retVal.setId(added.getId());
            retVal.setName(added.getName());
            retVal.setParentId(added.getParent().getId());
            retVal.setClientAddressPubId(address.getPubId());

        } catch(DuplicationErrorException e) {
            System.out.println("Cought agai = "+HttpStatus.CONFLICT);
            retVal.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(retVal);
        } catch (Exception e) {
            System.out.println("controller error ok");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
        //ProductCategoryDTO updated = categoryService.update(id, dto);
        System.out.println("returning ok");
        return ResponseEntity.ok(retVal);

    }

    // Update category pakai session-based auth
    @PutMapping("/category/{id}")
    public ResponseEntity<?> updateCategory( //originalnya <UpdateCategoryProductRespModel>
            @PathVariable Long id,
            @RequestBody RequestItemSubItemModel dto,
            HttpSession session
        ) {
        System.out.println("Rest Controller updateCategory");
        System.out.println("dto="+dto.getId());
        System.out.println("dto="+dto.getName());
        System.out.println("dto="+dto.getIndentLevel());
        UpdateCategoryProductRespModel retVal = new UpdateCategoryProductRespModel();
        ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO();
        ClientAddressDTO address = (ClientAddressDTO) session.getAttribute("targetAddress");
        try {
            productCategoryDTO = categoryService.updateProductCategory(dto.getId(), dto.getName(), address.getPubId());

        } catch(DuplicationErrorException e) {
            Map<String, Object> body = new HashMap<>();
            body.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(body);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }

        retVal.setIndentLevel(dto.getIndentLevel());
        System.out.println("name = "+retVal.getId());
        System.out.println("name = "+retVal.getIndentLevel());
        System.out.println("name = "+retVal.getName());
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