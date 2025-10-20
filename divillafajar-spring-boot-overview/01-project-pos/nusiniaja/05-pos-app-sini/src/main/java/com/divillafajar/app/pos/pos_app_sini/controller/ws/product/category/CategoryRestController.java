package com.divillafajar.app.pos.pos_app_sini.controller.ws.product.category;

import com.divillafajar.app.pos.pos_app_sini.exception.DuplicationErrorException;
import com.divillafajar.app.pos.pos_app_sini.exception.GenericCustomErrorException;
import com.divillafajar.app.pos.pos_app_sini.exception.category.CategoryHasSubCategoryException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.projection.ProductWithCategoryPathDTO;
import com.divillafajar.app.pos.pos_app_sini.model.product.CategorySearchResultModel;
import com.divillafajar.app.pos.pos_app_sini.model.product.CreateSubCategoryProductRespModel;
import com.divillafajar.app.pos.pos_app_sini.model.product.RequestItemSubItemModel;
import com.divillafajar.app.pos.pos_app_sini.model.product.UpdateCategoryProductRespModel;
import com.divillafajar.app.pos.pos_app_sini.service.client.ClientAddressService;
import com.divillafajar.app.pos.pos_app_sini.service.product.category.ProductCategoryService;
import com.divillafajar.app.pos.pos_app_sini.service.product.item.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@RestController
@RequestMapping("api/v1/manager/product")
@RequiredArgsConstructor
public class CategoryRestController {

    private final ProductCategoryService categoryService;
    private MessageSource messageSource;
    private final ClientAddressService clientAddressService;
    private final ProductService productService;


    @PostMapping("/category/{pubAid}")
    public ResponseEntity<?> addNewCategory( //defaultnya <CreateSubCategoryProductRespModel>
             @PathVariable String pubAid,
            @RequestBody RequestItemSubItemModel dto) {
        System.out.println("Rest Controller AddNewCategory="+pubAid);
        //ClientAddressDTO address = (ClientAddressDTO) session.getAttribute("targetAddress");
        //ClientAddressDTO address = clientAddressService.getStore(pubAid);
        CreateSubCategoryProductRespModel retVal = new CreateSubCategoryProductRespModel();
        try {
            ProductCategoryDTO added =  categoryService.addNewProductCategory(dto.getName(), pubAid);
            retVal.setId(added.getId());
            retVal.setName(added.getName());
            retVal.setParentId(null);
            retVal.setClientAddressPubId(pubAid);
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

    @DeleteMapping("/category/{id}")
    public ResponseEntity<?> deleteCategory( //defaultnya <CreateSubCategoryProductRespModel>
                                             HttpSession session,
                                             @PathVariable Long id) {
        System.out.println("Rest Controller deleteCategory id="+id);
        ClientAddressDTO address = (ClientAddressDTO) session.getAttribute("targetAddress");
        CreateSubCategoryProductRespModel retVal = new CreateSubCategoryProductRespModel();
		try {
			categoryService.deleteCategory(id);
		} catch (CategoryHasSubCategoryException e) {
			System.out.println("Rest Controller deleteCategory msg = "+e.getMessage());
			Map<String, Object> body = new HashMap<>();
			body.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(body);
		}
		catch (Exception e) {
			Map<String, Object> body = new HashMap<>();
			body.put("message", messageSource.getMessage("modal.errorUnexpected", null, Locale.getDefault()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(body);
		}
        return ResponseEntity.ok(retVal);

    }

	@GetMapping("/category/search/{kword}")
	public ResponseEntity<?> showSearchCategoryResult(
			@RequestParam(name = "activePage", required = false) String activePage,
			@RequestParam(name = "activeSub", required = false) String activeSub,
			@PathVariable("kword") String kword,
			RedirectAttributes redirectAttributes,
			HttpServletRequest request,
			Model model, HttpSession session
	) {
		System.out.println("showSearchCategoryResult is called = "+kword);
		ClientAddressDTO address = (ClientAddressDTO) session.getAttribute("targetAddress");
		List<ProductCategoryDTO> retVal = new ArrayList<>();
		List<CategorySearchResultModel> searchResult = new ArrayList<>();
		searchResult = categoryService.searchCategory(address.getPubId(),kword);
		if(searchResult!=null && searchResult.size()>0) {
			for(CategorySearchResultModel cat : searchResult) {
				System.out.println("cat.getPath(="+cat.getPath());
				ProductCategoryDTO dto = new ProductCategoryDTO();
				dto.setIndentLevel(0L);
				String kategori = cat.getPath();
				String lastPart = kategori.substring(kategori.lastIndexOf(">") + 1);
				System.out.println("lastPart="+lastPart);
				dto.setName(lastPart);
				int idx = kategori.lastIndexOf('>');
				String beforeLastPart = (idx != -1) ? kategori.substring(0, idx) : kategori;
				System.out.println("beforeLastPart="+beforeLastPart);
				dto.setPath(beforeLastPart+">");
				dto.setId(cat.getId());
				retVal.add(dto);
			}
		}
		return ResponseEntity.ok(retVal);
	}

    @PostMapping("/category/sub")
    public ResponseEntity<CreateSubCategoryProductRespModel> addSubCategory(
            HttpSession session,
            @RequestBody RequestItemSubItemModel dto) {
        System.out.println("Rest Controller addSubCategory");
        ClientAddressDTO address = (ClientAddressDTO) session.getAttribute("targetAddress");
        System.out.println("addSubCategory address = "+address.getPubId());
        System.out.println("addSubCategory dto = "+dto.getName());
        System.out.println("addSubCategory dto = "+dto.getParentId());
        //System.out.println("addSubCategory indent = "+dto.getIndentLevel());

        CreateSubCategoryProductRespModel retVal = new CreateSubCategoryProductRespModel();
        try {
            ProductCategoryDTO added =  categoryService.addSubProductCategory(dto.getParentId(), dto.getName(), address.getPubId());
            retVal.setId(added.getId());
            retVal.setName(added.getName());
            retVal.setParentId(added.getParent().getId());
            retVal.setIndentLevel(added.getIndentLevel());
            System.out.println("added indent = "+added.getIndentLevel());
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

    @GetMapping("/category/{catId}/search/{kword}")
    public ResponseEntity<?> showSearchItemCategoryResult(
            @RequestParam(name = "activePage", required = false) String activePage,
            @RequestParam(name = "activeSub", required = false) String activeSub,
            @RequestParam(name = "adrPubId", required = false) String adrPubId,
            @PathVariable("catId") Long catId,
            @PathVariable("kword") String kword,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            Model model, HttpSession session
    ) {
        System.out.println(catId+" ---showSearchItemCategoryResult is called = "+kword);
        System.out.println(activePage+" --- "+activeSub);
        System.out.println(adrPubId);
        List<ProductWithCategoryPathDTO> retVal = new ArrayList<>();
        ClientAddressDTO address = (ClientAddressDTO) session.getAttribute("targetAddress");
        List<ProductWithCategoryPathDTO> listItem = productService.getListProduct(adrPubId,catId, kword);
        if(listItem!=null)
            return ResponseEntity.ok(listItem);
        return ResponseEntity.ok(retVal);
    }
}