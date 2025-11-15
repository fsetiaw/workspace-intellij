package com.divillafajar.app.pos.pos_app_sini.controller.thyme.user.manager;

import com.divillafajar.app.pos.pos_app_sini.config.properties.CustomDefaultProperties;
import com.divillafajar.app.pos.pos_app_sini.exception.DuplicationErrorException;
import com.divillafajar.app.pos.pos_app_sini.global.AppGlobals;
import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.space.SpaceAreaEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.space.dto.SpaceAreaDTO;
import com.divillafajar.app.pos.pos_app_sini.io.projection.CategorySummaryProjection;
import com.divillafajar.app.pos.pos_app_sini.io.projection.ProductItemSummaryProjectionDTO;
import com.divillafajar.app.pos.pos_app_sini.io.projection.ProductWithCategoryPathDTO;
import com.divillafajar.app.pos.pos_app_sini.io.projection.area.AreaSummaryProjection;
import com.divillafajar.app.pos.pos_app_sini.model.item.CreateItemRequestModel;
import com.divillafajar.app.pos.pos_app_sini.model.product.ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId;
import com.divillafajar.app.pos.pos_app_sini.service.area.AreaService;
import com.divillafajar.app.pos.pos_app_sini.service.image.ImageStorageService;
import com.divillafajar.app.pos.pos_app_sini.service.product.category.ProductCategoryService;
import com.divillafajar.app.pos.pos_app_sini.service.product.item.ProductService;
import com.divillafajar.app.pos.pos_app_sini.utils.TelegramNotifier;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/v1/manager/manage/area")
@RequiredArgsConstructor
@SessionAttributes({"targetAddress","globals","filter"})
public class ThymeManagerAreaController {

    private final AreaService areaService;
	private final ProductCategoryService categoryService;
    private final CustomDefaultProperties props;
	private final ProductService productService;
	private final MessageSource messageSource;
    private final ImageStorageService imageService;
    private final TelegramNotifier telegramNotifier;
	private final AppGlobals appGlobals;

    @GetMapping
    public String showSpaceHome(
            @RequestParam(name = "activePage", required = true) String activePage,
            @RequestParam(name = "activeSub", required = false) String activeSub,
            Model model, HttpSession session
    ) {
	    ClientAddressDTO dto = (ClientAddressDTO) model.getAttribute("targetAddress");
		boolean hasArea = areaService.locationHasArea(dto.getPubId());
        boolean hasGuestArea=false, hasSubArea=false;
	    AreaSummaryProjection areaSummary=null;
        if(hasArea)
	        areaSummary = areaService.getAreaSummary(dto.getPubId());
		if(areaSummary!=null && areaSummary.getTotalEndChild()>0) {
			hasSubArea = true;
			System.out.println("areaSummary="+areaSummary.getTotalTopParent());
			System.out.println("areaSummary="+areaSummary.getTotalEndChild());
			System.out.println("areaSummary="+areaSummary.getClientAddressId());
			System.out.println("areaSummary="+areaSummary.getTotalEndChildNoGuestArea());
		}
	        //hasGuestArea = areaService.locationHasItemProduct(dto.getPubId());
        //AreaSummaryProjection categorySummary =  areaService.get(dto.getPubId());
	    ProductItemSummaryProjectionDTO productItemSummary = productService.getSummaryProductItem(dto.getPubId());

        model.addAttribute("hasArea",hasArea);
        model.addAttribute("hasGuestArea",hasGuestArea);
	    model.addAttribute("globals", appGlobals.getAll());
		model.addAttribute("activePage",activePage);
        model.addAttribute("activeSub",activeSub);
        model.addAttribute("areaSummary",areaSummary);
	    model.addAttribute("productItemSummary",productItemSummary);
        return "pages/v1/manager/space/index-space";
    }

    @GetMapping("/space")
    public String showAreaHome(
            @RequestParam(name = "activePage", required = true) String activePage,
            @RequestParam(name = "activeSub", required = true) String activeSub,
            Model model, HttpSession session
    ) {
        List<ProductCategoryDTO> orderList = new ArrayList<>();
        ClientAddressDTO dto = (ClientAddressDTO) model.getAttribute("targetAddress");
		List<SpaceAreaDTO> orderListAreaAndSubArea = areaService.getAreaAndSubAreaByClientAddressPubId(dto.getPubId());
        //List<ProductCategoryDTO> orderListCategoryAnsSub = categoryService.getCategoryAndSubCategoryByClientAddressPubId(dto.getPubId());
        model.addAttribute("orderListAreaAndSubArea",orderListAreaAndSubArea);
        model.addAttribute("activePage",activePage);
        model.addAttribute("activeSub",activeSub);
        return "pages/v1/manager/space/area/index-area";
    }
}
