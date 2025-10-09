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
            //@RequestParam(name = "pAid", required = true) String pAid,
            //HttpServletRequest request,
            Model model, HttpSession session
    ) {
        System.out.println("showCategoryHome HOME");

        ClientAddressDTO dto = (ClientAddressDTO) session.getAttribute("targetAddress");
        System.out.println("showCategoryHome dto = "+dto.getPubId());

        model.addAttribute("activePage",activePage);
        model.addAttribute("activeSub",activeSub);
        return "pages/v1/manager/product/index-product";
    }




    @GetMapping("/cat")
    public String showCategoryHome(
            @RequestParam(name = "activePage", required = true) String activePage,
            @RequestParam(name = "activeSub", required = true) String activeSub,
            //@RequestParam(name = "pAid", required = true) String pAid,
            //HttpServletRequest request,
            Model model, HttpSession session
    ) {
        System.out.println("showCategoryHome HOME");
        Integer toastDuration = (Integer) session.getAttribute("toastShortTimeout");
        model.addAttribute("toastShortTimeout", toastDuration);
        toastDuration = (Integer) session.getAttribute("toastMediumTimeout");
        model.addAttribute("toastMediumTimeout", toastDuration);
        toastDuration = (Integer) session.getAttribute("toastLongTimeout");
        model.addAttribute("toastLongTimeout", toastDuration);
        List<ProductCategoryDTO> orderList = new ArrayList<>();
        ClientAddressDTO dto = (ClientAddressDTO) session.getAttribute("targetAddress");
        List<ProductCategoryDTO> orderListCategoryAnsSub = categoryService.getCategoryAndSubCategoryByClientAddressPubId(dto.getPubId());
        if(orderListCategoryAnsSub==null)
            System.out.println("is >NUll");

        for (ProductCategoryDTO category : orderListCategoryAnsSub) {
            if(category.getParent()==null) {
                System.out.println("TOP Category: " + category.getName()+" level = "+category.getIndentLevel());
            }
            else {
                System.out.println("SUB Category: " + category.getName()+" level = "+category.getIndentLevel());
                System.out.println("PARENT Category: " + category.getParentId());
            }
        }




        model.addAttribute("orderListCategoryAnsSub",orderListCategoryAnsSub);
        model.addAttribute("activePage",activePage);
        model.addAttribute("activeSub",activeSub);
        return "pages/v1/manager/product/cat/index-category";
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
