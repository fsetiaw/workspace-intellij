package com.divillafajar.app.pos.pos_app_sini.controller.thyme.user.manager;

import com.divillafajar.app.pos.pos_app_sini.config.properties.CustomDefaultProperties;
import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.service.product.category.ProductCategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/v1/manager/manage/product")
@RequiredArgsConstructor
@SessionAttributes({"targetAddress","toastShortTimeout","toastMediumTimeout","toastLongTimeout"})
public class ThymeManagerProductController {

    private final ProductCategoryService categoryService;
    private final CustomDefaultProperties props;

    @GetMapping
    public String showProdHome(
            @RequestParam(name = "activePage", required = true) String activePage,
            @RequestParam(name = "activeSub", required = false) String activeSub,
            Model model, HttpSession session
    ) {
        System.out.println("showCategoryHome HOME");
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
            //HttpServletRequest request,
            Model model, HttpSession session
    ) {
        System.out.println("showProductItemHome HOME!!!");
        ClientAddressDTO dto = (ClientAddressDTO) model.getAttribute("targetAddress");
        System.out.println("showProductItemHome dto = "+dto.getPubId());
        List<String> listEndCategory = categoryService.getPathToEachEndChildCategoryByClientAddressPubId(dto.getPubId());
        listEndCategory.forEach(cat -> {
            System.out.println(cat);
        });
        model.addAttribute("listEndCategory",listEndCategory);
        model.addAttribute("activePage",activePage);
        model.addAttribute("activeSub",activeSub);
        return "pages/v1/manager/product/item/index-item.html";
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
