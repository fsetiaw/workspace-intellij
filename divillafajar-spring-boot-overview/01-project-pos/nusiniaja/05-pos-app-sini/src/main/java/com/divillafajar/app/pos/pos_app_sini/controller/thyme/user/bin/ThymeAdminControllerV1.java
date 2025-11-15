package com.divillafajar.app.pos.pos_app_sini.controller.thyme.user.bin;

import com.divillafajar.app.pos.pos_app_sini.common.enums.LineOfBusinessEnum;
import com.divillafajar.app.pos.pos_app_sini.exception.DuplicationErrorException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientContactDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;
import com.divillafajar.app.pos.pos_app_sini.model.client.location.CreateClientLocationRequestModel;
import com.divillafajar.app.pos.pos_app_sini.service.client.ClientAddressService;
import com.divillafajar.app.pos.pos_app_sini.service.client.ClientService;
import com.divillafajar.app.pos.pos_app_sini.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("v1/admin/client")
public class ThymeAdminControllerV1 {
    private final ClientAddressService clientAddressService;
    private final ClientService clientService;
    private final LocaleResolver localeResolver;
    private final UserService userService;
    private final MessageSource messageSource;

    public ThymeAdminControllerV1(ClientService clientService, LocaleResolver localeResolver,
                  UserService userService, ClientAddressService clientAddressService, MessageSource messageSource) {
        this.clientService=clientService;
        this.localeResolver=localeResolver;
        this.messageSource=messageSource;
        this.userService=userService;
        this.clientAddressService=clientAddressService;
    }
/*
    @GetMapping("/home")
    public String showHome(HttpSession session, Model model) {
        System.out.println("GOTO MASTER HOME");
        UserSessionLog userLogInfo = (UserSessionLog) session.getAttribute("userLogInfo");
        model.addAttribute("userLogInfo", userLogInfo);
        //return "form/add-new-client";

        return "admin/index";
    }

 */

    @GetMapping("/home")
    public String showAdminHome(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(name = "pid", required = true) String pid,
            @RequestParam(name = "add", required = false) Boolean add,
            //@RequestParam(name = "lang", required = false) String lang,
            Model model
    ) {
        System.out.println("showAdminHome IS CALLED = " + pid);
        /*
        if (lang != null) {
            Locale locale = new Locale(lang);
            localeResolver.setLocale(request, response, locale);
        }

         */

        ClientDTO client = clientService.getClientDetails(pid);
        List<ClientAddressEntity> storesLocation = client.getClientAddresses();
        model.addAttribute("isAdd", add);
        model.addAttribute("pid",pid);
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("clientType", client.getClientType());
        model.addAttribute("clientName", client.getClientName());
        model.addAttribute("ourStores", storesLocation);
        //System.out.println("val lang client.clientName= " + model.getAttribute("client"));
        //return "super/clients/home/home-client";
        return "pages/v1/client-admin/index-admin";
    }

    @GetMapping("/location/add")
    public String showAddLocationForm(
            @RequestParam(name = "pid", required = true) String pid,
            @RequestParam(name = "clientName", required = true) String clientName,
          @RequestParam(name = "add", required = false) Boolean add, Model model) {
        System.out.println("showAddLocationForm CALLED = "+clientName+pid);
        LineOfBusinessEnum[] lobList = LineOfBusinessEnum.values();
        System.out.println("lob size = "+lobList.length);
        // 👉 Cara print satu per satu
        for (LineOfBusinessEnum lob : lobList) {
            System.out.println("LOB = " + lob.name() + " | key = " + lob.getI18nKey());
        }

        model.addAttribute("lobList", lobList);
        model.addAttribute("pid", pid);
        model.addAttribute("clientName", clientName);
        model.addAttribute("isAdd", add);
        model.addAttribute("activePage", "location");
        model.addAttribute("activeSub", "addLocation");
        System.out.println("showAddClientForm is called");
        return "pages/v1/client-admin/add-location";
    }

    @PostMapping("/location/add")
    public String addLokasiCabang(
            @ModelAttribute CreateClientLocationRequestModel createClientLocationRequestModel,
            @RequestParam(name = "pid", required = true) String pid,
            RedirectAttributes redirectAttributes,
            Model model, Locale locale
    ) {
        String msg = "false";
        String labelLocation = messageSource.getMessage("label.location", null, locale);
        String successMessage = messageSource.getMessage("label.addSuccessfully", null, locale);
        String msgAddFailed = messageSource.getMessage("label.addFailed", null, locale);
        String errorClientLocationAlreadyExist = messageSource.getMessage("modal.errorClientLocationAlreadyExist", null, locale);
        String unexpectedError = messageSource.getMessage("modal.errorUnexpected", null, locale);
        System.out.println("ADD CLIENT IS CALLED V!");
        System.out.println("ADD CLIENT PID="+pid);

        try {
            ClientDTO ownerClient = clientService.getClientDetails(pid);
            System.out.println("pit 2");
            ClientAddressDTO address = new ClientAddressDTO();
            BeanUtils.copyProperties(createClientLocationRequestModel,address);
            System.out.println("pit 3");
            ClientContactDTO pic = new ClientContactDTO();
            BeanUtils.copyProperties(createClientLocationRequestModel,pic);

            System.out.println("pit 4");
            clientAddressService.addNewStoreOrBranch(ownerClient.getClientAddresses().getFirst().getId(),address, pic);
            System.out.println("pit 5");

            redirectAttributes.addFlashAttribute("successMessage",
                    labelLocation+" "+successMessage);
            //model.addAttribute("successMessage", labelLocation+" "+successMessage);
            //BeanUtils.copyProperties(createdClient,returnVal);
            System.out.println("pit 6");
            //return ResponseEntity.status(HttpStatus.CREATED).body(returnVal);
        } catch (DuplicationErrorException ex) {
            // gagal server / unexpected error
            System.out.println("berhasilditangkap");

            //model.addAttribute("msg",ex.getMessage());
            //model.addAttribute("clientError", "true");
            //model.addAttribute("errorMessage", labelLocation+" "+msgAddFailed+"<br>"+errorClientLocationAlreadyExist);
            redirectAttributes.addFlashAttribute("errorMessage",
                    labelLocation + " " + msgAddFailed + "<br>" + errorClientLocationAlreadyExist);
        //} catch (
        //        CreateUserException ex) {
            // gagal server / unexpected error
        //    model.addAttribute("errorMessage", labelLocation+" "+msgAddFailed+"<br>"+unexpectedError);
        } catch (Exception ex) {
            // gagal server / unexpected error
            redirectAttributes.addFlashAttribute("errorMessage",
                    labelLocation + " " + msgAddFailed + "<br>" + unexpectedError);
            //model.addAttribute("errorMessage", labelLocation+" "+msgAddFailed+"<br>"+unexpectedError);
        }
        finally {
            //model.addAttribute("pid", pid);
            List<ClientDTO> clients = clientService.getAllClients();
            //model.addAttribute("ourClients", clients);
            redirectAttributes.addFlashAttribute("ourClients", clients);
            redirectAttributes.addFlashAttribute("toastDelay", 1500);

            //model.addAttribute("toast-delay", 1000);
        }
        //model.addAttribute("isAdd", false);
        //return "super/clients/index-clients";
        //return "pages/v1/client-admin/index-admin";
        return "redirect:/v1/admin/client/home?pid=" + pid + "&add=false";
    }

}
