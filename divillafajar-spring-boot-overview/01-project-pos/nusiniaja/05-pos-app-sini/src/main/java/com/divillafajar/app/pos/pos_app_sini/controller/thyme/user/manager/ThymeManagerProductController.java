package com.divillafajar.app.pos.pos_app_sini.controller.thyme.user.manager;

import com.divillafajar.app.pos.pos_app_sini.config.properties.CustomDefaultProperties;
import com.divillafajar.app.pos.pos_app_sini.exception.DuplicationErrorException;
import com.divillafajar.app.pos.pos_app_sini.global.AppGlobals;
import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.product.ProductEntity;
import com.divillafajar.app.pos.pos_app_sini.io.projection.ProductWithCategoryPathDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.model.item.CreateItemRequestModel;
import com.divillafajar.app.pos.pos_app_sini.model.product.ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId;
import com.divillafajar.app.pos.pos_app_sini.service.image.ImageStorageService;
import com.divillafajar.app.pos.pos_app_sini.service.product.category.ProductCategoryService;
import com.divillafajar.app.pos.pos_app_sini.service.product.item.ProductService;
import com.divillafajar.app.pos.pos_app_sini.utils.TelegramNotifier;
import jakarta.servlet.http.HttpServletRequest;
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

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/v1/manager/manage/product")
@RequiredArgsConstructor
@SessionAttributes({"targetAddress","globals","filter"})
public class ThymeManagerProductController {

    private final ProductCategoryService categoryService;
    private final CustomDefaultProperties props;
	private final ProductService productService;
	private final MessageSource messageSource;
    private final ImageStorageService imageService;
    private final TelegramNotifier telegramNotifier;
	private final AppGlobals appGlobals;

    @GetMapping
    public String showProdHome(
            @RequestParam(name = "activePage", required = true) String activePage,
            @RequestParam(name = "activeSub", required = false) String activeSub,
            Model model, HttpSession session
    ) {
	    ClientAddressDTO dto = (ClientAddressDTO) model.getAttribute("targetAddress");
		boolean hasCategory = categoryService.locationHasCategoryProduct(dto.getPubId());
        boolean hasItem=false;
        if(hasCategory)
            hasItem = categoryService.locationHasItemProduct(dto.getPubId());
	    model.addAttribute("hasCategory",hasCategory);
        model.addAttribute("hasItem",hasItem);
	    model.addAttribute("globals", appGlobals.getAll());
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
        List<ProductCategoryDTO> orderList = new ArrayList<>();
        ClientAddressDTO dto = (ClientAddressDTO) model.getAttribute("targetAddress");
        List<ProductCategoryDTO> orderListCategoryAnsSub = categoryService.getCategoryAndSubCategoryByClientAddressPubId(dto.getPubId());
		model.addAttribute("orderListCategoryAnsSub",orderListCategoryAnsSub);
        model.addAttribute("activePage",activePage);
        model.addAttribute("activeSub",activeSub);
        return "pages/v1/manager/product/cat/index-category";
    }

    @Transactional
	@PostMapping("/cat/use-default")
	public String createDefaultCategory(
			@RequestParam(required = false) String lang,
			@RequestParam(name = "activePage", required = true) String activePage,
			@RequestParam(name = "activeSub", required = true) String activeSub,
            RedirectAttributes redirectAttributes,
			Model model, HttpSession session
	) {
		List<ProductCategoryDTO> orderList = new ArrayList<>();
		ClientAddressDTO dto = (ClientAddressDTO) model.getAttribute("targetAddress");
        categoryService.createDefaultCategory(lang, dto.getPubId());
        dto.setUsedDefaultCategory(true);
		List<ProductCategoryDTO> orderListCategoryAnsSub = categoryService.getCategoryAndSubCategoryByClientAddressPubId(dto.getPubId());
        model.addAttribute("orderListCategoryAnsSub",orderListCategoryAnsSub);
        redirectAttributes.addAttribute("activePage",activePage);
        redirectAttributes.addAttribute("activeSub",activeSub);
        model.addAttribute("globals", appGlobals.getAll());
        model.addAttribute("targetAddress", dto);
		//return "pages/v1/manager/product/cat/index-category";
        return "redirect:/v1/manager/manage/product/cat";
	}

    @Transactional
    @PostMapping("/cat/reset-categories")
    public String resetClientAddressCategoris(
            @RequestParam(required = false) String lang,
            @RequestParam(name = "activePage", required = true) String activePage,
            @RequestParam(name = "activeSub", required = true) String activeSub,
            RedirectAttributes redirectAttributes,
            Model model, HttpSession session
    ) {
        List<ProductCategoryDTO> orderList = new ArrayList<>();
        ClientAddressDTO dto = (ClientAddressDTO) model.getAttribute("targetAddress");
        categoryService.resetCategoryByClientAddress(dto.getPubId());
        dto.setUsedDefaultCategory(false);
        List<ProductCategoryDTO> orderListCategoryAnsSub = categoryService.getCategoryAndSubCategoryByClientAddressPubId(dto.getPubId());
        model.addAttribute("orderListCategoryAnsSub",orderListCategoryAnsSub);
        redirectAttributes.addAttribute("activePage",activePage);
        redirectAttributes.addAttribute("activeSub",activeSub);
        model.addAttribute("globals", appGlobals.getAll());
        model.addAttribute("targetAddress", dto);
        //return "pages/v1/manager/product/cat/index-category";
        return "redirect:/v1/manager/manage/product/cat";
    }

    @GetMapping("/item")
    public String showProductItemHome(
            @RequestParam(name = "activePage", required = true) String activePage,
            @RequestParam(name = "activeSub", required = true) String activeSub,
            @RequestParam(name = "filter", required = false) String filter,
            //RedirectAttributes redirectAttributes,
            //HttpServletRequest request,
            Model model, HttpSession session
    ) {
        ClientAddressDTO dto = (ClientAddressDTO) model.getAttribute("targetAddress");


        List<ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId> listRecord = new ArrayList<>();
        //pettama kali sampi laman
        if(filter==null) {
            model.addAttribute("filter","noFilter");
            listRecord = categoryService.getPathToEachEndChildCategoryByClientAddressPubId(dto.getPubId());
        }
        else {
            model.addAttribute("filter",filter);
            listRecord = categoryService.getPathToEachEndChildCategoryByClientAddressPubId(dto.getPubId(), filter);
        }
        List<String> listEndCategory = new ArrayList<>();
        List<Long> listCategoryTotItem = new ArrayList<>();
        if(listRecord!=null) {

            for(ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId singleton : listRecord) {
                listEndCategory.add(singleton.getFullPath());
                listCategoryTotItem.add(singleton.getTotalProducts());
            }
        }
        /*
        listEndCategory.forEach(cat -> {
            System.out.println(cat);
        });
        listCategoryTotItem.forEach(cat -> {
            System.out.println(cat);
        });

         */
        model.addAttribute("listEndCategory",listEndCategory);
        model.addAttribute("listCategoryTotItem",listCategoryTotItem);
        model.addAttribute("activePage",activePage);
        model.addAttribute("activeSub",activeSub);
        return "pages/v1/manager/product/item/index-item";
    }

    @PostMapping("/item/upload-image/{id}")
    @Transactional
    public ResponseEntity<?> uploadProductImage(
            @PathVariable("id") Long productId,
            @RequestParam("file") MultipartFile file,
            Model model
    ) {
        try {
            telegramNotifier.sendMessage("🧪 *uploadProductImage*\n\n" + "UPLOAD GAMBAR NIH");
            // 🔹 Validasi file kosong
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File kosong");
            }
            ClientAddressDTO dto = (ClientAddressDTO) model.getAttribute("targetAddress");
            String thumbUrl = imageService.saveProductImage(productId,file, dto.getPubId());
            /*
            // 🔹 Ambil entity produk
            ProductEntity product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan"));

            // 🔹 Buat folder upload kalau belum ada
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 🔹 Simpan file ke folder lokal
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileName = "product_" + productId + "_" + System.currentTimeMillis() + "_" + originalFilename;
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // 🔹 Simpan URL ke database (sesuai struktur entity kamu)
            String imageUrl = "/uploads/product/" + fileName;
            product.setImageUrl(imageUrl);
            product.setThumbnailUrl(imageUrl); // nanti bisa diganti thumbnail berbeda
            productRepository.save(product);

            // 🔹 Kembalikan respons sesuai format FilePond

             */
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "url", thumbUrl
            ));

        //} catch (IOException e) {
        //    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        //            .body("Gagal upload gambar: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Response error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }



	@GetMapping("/item/{categoryId}")
	public String showCategoryItemHome(
			@RequestParam(name = "activePage", required = true) String activePage,
			@RequestParam(name = "activeSub", required = true) String activeSub,
			@RequestParam(name = "path", required = true) String path,
			@PathVariable Long categoryId,
			Model model, HttpSession session
	) {
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
		List<ProductWithCategoryPathDTO> listItem = productService.getListProduct(dto.getPubId(),categoryId, null);
		model.addAttribute("pathCategory",pathCategory);
		model.addAttribute("path",path);
		model.addAttribute("targetCategoryName",targetCategoryName);
		model.addAttribute("trimPath",trimPath);
		model.addAttribute("activePage",activePage);
		model.addAttribute("activeSub",activeSub);
        model.addAttribute("categoryId",categoryId);
        model.addAttribute("adrPubId",dto.getPubId());
		model.addAttribute("toastShortTimeout",appGlobals.get("toastShortTimeout"));
		model.addAttribute("toastMediumTimeout",appGlobals.get("toastMediumTimeout"));
		model.addAttribute("toastLongTimeout",appGlobals.get("toastLongTimeout"));
		//"","",""
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


    /**********************************************STOCK************************************************************/
    @GetMapping("/stock")
    public String showStockHomeDashboard(
            @RequestParam(name = "activePage", required = true) String activePage,
            @RequestParam(name = "activeSub", required = false) String activeSub,
            Model model, HttpSession session
    ) {
        ClientAddressDTO dto = (ClientAddressDTO) model.getAttribute("targetAddress");
        boolean hasCategory = categoryService.locationHasCategoryProduct(dto.getPubId());
        model.addAttribute("hasCategory",hasCategory);
        model.addAttribute("activePage",activePage);
        model.addAttribute("activeSub",activeSub);
        return "pages/v1/manager/product/stock/index-stock";
    }
}
