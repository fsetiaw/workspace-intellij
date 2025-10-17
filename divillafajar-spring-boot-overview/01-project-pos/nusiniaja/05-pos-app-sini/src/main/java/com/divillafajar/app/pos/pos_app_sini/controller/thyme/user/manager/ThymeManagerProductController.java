package com.divillafajar.app.pos.pos_app_sini.controller.thyme.user.manager;

import com.divillafajar.app.pos.pos_app_sini.common.enums.BootstrapColorEnum;
import com.divillafajar.app.pos.pos_app_sini.config.properties.CustomDefaultProperties;
import com.divillafajar.app.pos.pos_app_sini.exception.DuplicationErrorException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductWithCategoryPathDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.model.item.CreateItemRequestModel;
import com.divillafajar.app.pos.pos_app_sini.service.product.category.ProductCategoryService;
import com.divillafajar.app.pos.pos_app_sini.service.product.item.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

@Controller
@RequestMapping("/v1/manager/manage/product")
@RequiredArgsConstructor
@SessionAttributes({"targetAddress","toastShortTimeout","toastMediumTimeout","toastLongTimeout"})
public class ThymeManagerProductController {

    private final ProductCategoryService categoryService;
    private final CustomDefaultProperties props;
	private final ProductService productService;
	private final MessageSource messageSource;

    @GetMapping
    public String showProdHome(
            @RequestParam(name = "activePage", required = true) String activePage,
            @RequestParam(name = "activeSub", required = false) String activeSub,
            Model model, HttpSession session
    ) {
        System.out.println("showCategoryHome HOME");
	    ClientAddressDTO dto = (ClientAddressDTO) model.getAttribute("targetAddress");
	    model.addAttribute("activePage",activePage);
        model.addAttribute("activeSub",activeSub);
        return "pages/v1/manager/product/index-product";
    }




    @GetMapping("/cat")
    public String showCategoryHome(
            @RequestParam(name = "activePage", required = true) String activePage,
            @RequestParam(name = "activeSub", required = true) String activeSub,
            Model model, HttpSession session
    ) {
        System.out.println("showCategoryHome HOME");
        List<ProductCategoryDTO> orderList = new ArrayList<>();
        ClientAddressDTO dto = (ClientAddressDTO) model.getAttribute("targetAddress");
        List<ProductCategoryDTO> orderListCategoryAnsSub = categoryService.getCategoryAndSubCategoryByClientAddressPubId(dto.getPubId());
        model.addAttribute("orderListCategoryAnsSub",orderListCategoryAnsSub);
        model.addAttribute("activePage",activePage);
        model.addAttribute("activeSub",activeSub);
        return "pages/v1/manager/product/cat/index-category";
    }

    @GetMapping("/item")
    public String showProductItemHome(
            @RequestParam(name = "activePage", required = true) String activePage,
            @RequestParam(name = "activeSub", required = true) String activeSub,
            //RedirectAttributes redirectAttributes,
            //HttpServletRequest request,
            Model model, HttpSession session
    ) {
        System.out.println("showProductItemHome HOME!!!");
        ClientAddressDTO dto = (ClientAddressDTO) model.getAttribute("targetAddress");
        List<String> listEndCategory = categoryService.getPathToEachEndChildCategoryByClientAddressPubId(dto.getPubId());
        listEndCategory.forEach(cat -> {
            System.out.println(cat);
        });
        model.addAttribute("listEndCategory",listEndCategory);
        model.addAttribute("activePage",activePage);
        model.addAttribute("activeSub",activeSub);

        return "pages/v1/manager/product/item/index-item.html";
    }

	@GetMapping("/item/{categoryId}")
	public String showCategoryItemHome(
			@RequestParam(name = "activePage", required = true) String activePage,
			@RequestParam(name = "activeSub", required = true) String activeSub,
			@RequestParam(name = "path", required = true) String path,
			@PathVariable Long categoryId,
			Model model, HttpSession session
	) {
		System.out.println("showCategoryItemHome HOME - Catid ="+categoryId+"-"+path);
		String trimPath = "";
		StringTokenizer st = new StringTokenizer(path,"/");
		while(st.hasMoreTokens()) {
			String tkn = st.nextToken();
			StringTokenizer st1 = new StringTokenizer(tkn,"~");
			trimPath = trimPath+st1.nextToken();
			if(st.hasMoreTokens()) {
				trimPath=trimPath+" / ";
			}
		}
		String pathCategory = "";
		String targetCategoryName = "";
		if(!StringUtils.isBlank(trimPath)) {
			st = new StringTokenizer(trimPath,"/");
			while(st.hasMoreTokens()) {
				String tkn = st.nextToken().trim();
				pathCategory = pathCategory + tkn;
				if(st.hasMoreTokens()) {
					pathCategory=pathCategory+"/";
				}
				else {
					//last token
					targetCategoryName=tkn.trim();
				}
			}
		}
		ClientAddressDTO dto = (ClientAddressDTO) model.getAttribute("targetAddress");
		List<ProductWithCategoryPathDTO> listItem = productService.getListProduct(dto.getPubId(),categoryId);
		model.addAttribute("pathCategory",pathCategory);
		model.addAttribute("path",path);
		model.addAttribute("targetCategoryName",targetCategoryName);
		model.addAttribute("trimPath",trimPath);
		model.addAttribute("activePage",activePage);
		model.addAttribute("activeSub",activeSub);
		if(listItem!=null) {
			model.addAttribute("listItem",listItem);
		}
		else {
			model.addAttribute("listItem",new ArrayList<>());
		}
		return "pages/v1/manager/product/item/home-category-item";
	}

	@PostMapping("/item/{categoryId}")
	public String addNewItemCategory(
			@RequestParam(name = "activePage", required = true) String activePage,
			@RequestParam(name = "activeSub", required = true) String activeSub,
			//@RequestParam(name = "path", required = true) String path,
			@ModelAttribute CreateItemRequestModel createItemRequestModel,
			@PathVariable Long categoryId,
			Locale locale,
			RedirectAttributes redirectAttributes,
			Model model, HttpSession session
	) {
		System.out.println("addNewItemCategory HOME - Catid ="+categoryId);
		try {
			ClientAddressDTO dto = (ClientAddressDTO) model.getAttribute("targetAddress");
			productService.addNewProduct(categoryId, dto, createItemRequestModel);
			String successMessage = messageSource.getMessage("label.item", null, locale)+" "+messageSource.getMessage("label.addSuccessfully", null, locale);
			redirectAttributes.addFlashAttribute("successMessage", successMessage);

		} catch (DuplicationErrorException e) {
			String errorMessage = messageSource.getMessage("err.itemAlreadyExistUnderCategory", null, locale)+" "+e.getMessage();
			redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
		}
		catch (Exception e) {
			String errorMessage = messageSource.getMessage("modal.errorUnexpected", null, locale);
			redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
		}
		redirectAttributes.addAttribute("activePage",activePage);
		redirectAttributes.addAttribute("activeSub",activeSub);
		redirectAttributes.addAttribute("path",createItemRequestModel.getPath());
		return "redirect:/v1/manager/manage/product/item/"+categoryId;

	}

    /*
    ** NGGA JADi DIPAKE, PAKI YG RestControl untuk CRUD
    **
    *

    @PostMapping("/category")
    public String addProdCat(@ModelAttribute("targetAddress") ClientAddressDTO targetAddress, Model model) {
        System.out.println("manage.addProdCat is called");
        return "redirect:/v1/manager/home?pid="+targetAddress.getPubId();
    }

    @PutMapping("/category/{id}")
    public String editProdCat(@ModelAttribute("targetAddress") ClientAddressDTO targetAddress,
                              @PathVariable("id") long id, Model model) {
        System.out.println("manage.editProdCat is called");
        return "redirect:/v1/manager/home?pid="+targetAddress.getPubId();
    }

     */
}
