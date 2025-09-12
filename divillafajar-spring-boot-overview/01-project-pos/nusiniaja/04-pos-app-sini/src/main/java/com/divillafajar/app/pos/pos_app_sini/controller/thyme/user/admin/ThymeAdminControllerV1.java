package com.divillafajar.app.pos.pos_app_sini.controller.thyme.user.admin;

import com.divillafajar.app.pos.pos_app_sini.exception.client.ClientAlreadyExistException;
import com.divillafajar.app.pos.pos_app_sini.exception.user.CreateUserException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.address.dto.AddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientContactDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;
import com.divillafajar.app.pos.pos_app_sini.model.client.ClientDetailsResponseModel;
import com.divillafajar.app.pos.pos_app_sini.model.client.CreateClientRequestModel;
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

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("v1/admin")
public class ThymeAdminControllerV1 {

    private final ClientService clientService;
    private final LocaleResolver localeResolver;
    private final UserService userService;
    private final MessageSource messageSource;

    public ThymeAdminControllerV1(ClientService clientService, LocaleResolver localeResolver,
                  UserService userService, MessageSource messageSource) {
        this.clientService=clientService;
        this.localeResolver=localeResolver;
        this.messageSource=messageSource;
        this.userService=userService;
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
        System.out.println("showAddLocationForm CALLED = "+clientName);
        model.addAttribute("pid", pid);
        model.addAttribute("clientName", clientName);
        model.addAttribute("isAdd", add);
        model.addAttribute("activePage", "loaction");
        model.addAttribute("activeSub", "addLocation");
        System.out.println("showAddClientForm is called");
        return "pages/v1/client-admin/add-location";
    }

    @PostMapping("/location/add")
    public String addClients(@ModelAttribute CreateClientRequestModel createClientRequestModel, Model model, Locale locale
    ) {
        String msg = "false";
        String labelClient = messageSource.getMessage("label.client", null, locale);
        String successMessage = messageSource.getMessage("label.addSuccessfully", null, locale);
        String msgAddFailed = messageSource.getMessage("label.addFailed", null, locale);
        String errorClientAlreadyExist = messageSource.getMessage("modal.errorClientAlreadyExist", null, locale);
        String unexpectedError = messageSource.getMessage("modal.errorUnexpected", null, locale);
        System.out.println("ADD CLIENT IS CALLED");

        try {
            ClientDetailsResponseModel returnVal = new ClientDetailsResponseModel();

            ClientDTO client = new ClientDTO();
            BeanUtils.copyProperties(createClientRequestModel,client);
            client.setStatus("ok");

            AddressDTO address = new AddressDTO();
            BeanUtils.copyProperties(createClientRequestModel,address);

            ClientContactDTO pic = new ClientContactDTO();
            BeanUtils.copyProperties(createClientRequestModel,pic);


            ClientDTO createdClient = clientService.createClient(client,address,pic);
            model.addAttribute("successMessage", labelClient+" "+successMessage);
            //BeanUtils.copyProperties(createdClient,returnVal);

            //return ResponseEntity.status(HttpStatus.CREATED).body(returnVal);
        } catch (
                ClientAlreadyExistException ex) {
            // gagal server / unexpected error

            //model.addAttribute("msg",ex.getMessage());
            //model.addAttribute("clientError", "true");
            model.addAttribute("errorMessage", labelClient+" "+msgAddFailed+"<br>"+errorClientAlreadyExist);
        } catch (
                CreateUserException ex) {
            // gagal server / unexpected error
            model.addAttribute("errorMessage", labelClient+" "+msgAddFailed+"<br>"+unexpectedError);
        } catch (Exception ex) {
            // gagal server / unexpected error
            model.addAttribute("errorMessage", labelClient+" "+msgAddFailed+"<br>"+unexpectedError);
        }
        finally {
            List<ClientDTO> clients = clientService.getAllClients();
            model.addAttribute("ourClients", clients);
            model.addAttribute("toast-delay", 1000);
        }
        //model.addAttribute("isAdd", false);
        //return "super/clients/index-clients";
        return "pages/v1/client-admin/index-admin";
    }

}
