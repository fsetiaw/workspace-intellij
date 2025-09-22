package com.divillafajar.app.pos.pos_app_sini.controller.thyme.user.superuser;

import com.divillafajar.app.pos.pos_app_sini.exception.client.ClientAlreadyExistException;
import com.divillafajar.app.pos.pos_app_sini.exception.user.CreateUserException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.address.dto.AddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientContactEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientContactDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.employee.EmployeeEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.employee.EmploymentEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.session.UserSessionLog;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserEntity;
import com.divillafajar.app.pos.pos_app_sini.model.client.ClientDetailsResponseModel;
import com.divillafajar.app.pos.pos_app_sini.model.client.CreateClientRequestModel;
import com.divillafajar.app.pos.pos_app_sini.service.client.ClientAddressService;
import com.divillafajar.app.pos.pos_app_sini.service.client.ClientContactService;
import com.divillafajar.app.pos.pos_app_sini.service.client.ClientService;
import com.divillafajar.app.pos.pos_app_sini.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("v1/superuser")
@RequiredArgsConstructor
public class ThymeSuperuserControllerV1 {

    private final UserService userService;
    private final ClientService clientService;
    private final LocaleResolver localeResolver;
    private final MessageSource messageSource;
    private final ClientAddressService clientAddressService;
    private final ClientContactService clientContactService;
/*
    public ThymeSuperuserControllerV1(UserService userService, ClientService clientService,
                                      LocaleResolver localeResolver,MessageSource messageSource) {
        this.userService=userService;
        this.clientService=clientService;
        this.localeResolver=localeResolver;
        this.messageSource=messageSource;
    }

 */

    @GetMapping("/home")
    public String showHome(HttpServletRequest request,
               HttpServletResponse response,
               HttpSession session,
               //@RequestParam(name = "lang", required = false) String lang,
               Model model) {
        System.out.println("GOTO MASTER HOME V1");
        /*
        GET TOTAL CLIENTS
         */
        List<ClientDTO> listClients = Optional.ofNullable(clientService.getAllClients())
                .orElse(Collections.emptyList());
        int size=0;

        model.addAttribute("listClients", listClients);
        UserSessionLog userLogInfo = (UserSessionLog) session.getAttribute("userLogInfo");
        model.addAttribute("userLogInfo", userLogInfo);
        //return "form/add-new-client";

        return "pages/v1/index";
    }

    @GetMapping("/clients")
    public String gotoIndexClients(@RequestParam(name = "add", required = false) Boolean add, Model model) {
        List<ClientDTO> clients = clientService.getAllClients();
        model.addAttribute("isAdd", add);
        model.addAttribute("ourClients", clients);
        model.addAttribute("activePage", "dashboard");
        System.out.println("model attr val = " + model.getAttribute("isAdd"));
        System.out.println("activePage attr val = " + model.getAttribute("activePage"));
        return "pages/v1/clients/index-clients";
    }
    @GetMapping("/clients/add")
    public String showAddClientForm(@RequestParam(name = "add", required = false) Boolean add, Model model) {
        model.addAttribute("isAdd", add);
        model.addAttribute("activePage", "clients");
        model.addAttribute("activeSub", "add");
        System.out.println("showAddClientForm is called");
        return "pages/v1/clients/add-clients";
    }

    @GetMapping("/clients/upd")
    public String showUpdateClientForm(@RequestParam(name = "pid", required = true) String pid, Model model) {
        System.out.println("showUpdateClientForm is called = "+pid);
        ClientDTO targetClient = clientService.getClientDetails(pid);
        ClientAddressEntity clientAddress = targetClient.getClientAddresses().getFirst();
        //ClientAddressDTO clientAddressDTO = new ClientAddressDTO();
        //BeanUtils.copyProperties(clientAddress,clientAddressDTO);
        //
        ClientContactDTO contact = clientContactService.findClientContactByAddressId(clientAddress.getId());
        //biar tidak passing idnya
        clientAddress.setId(null);
        contact.setId(null);
        model.addAttribute("clientContact",contact);
        model.addAttribute("clientAddress",clientAddress);
        model.addAttribute("targetClient",targetClient);
        model.addAttribute("pid", pid);
        model.addAttribute("activePage", "clients");
        model.addAttribute("activeSub", "upd");

        return "pages/v1/clients/update-clients";
    }


    @GetMapping("/clients/home")
    public String showClientHome(@RequestParam(name = "pid", required = true) String pid,
                  @RequestParam(name = "lang", required = false) String lang,
                  Model model) {
        System.out.println("showCclientHome IS CALLED = " + pid);
        System.out.println("showCclientHome lang = " + lang);
        model.addAttribute("client", clientService.getClientDetails(pid));
        return "super/clients/home/home-client";
    }

    @PostMapping("/clients")
    public String addClients(@ModelAttribute CreateClientRequestModel createClientRequestModel, Model model, Locale locale
    ) {
        String msg = "false";
        String labelClient = messageSource.getMessage("label.client", null, locale);
        String successMessage = messageSource.getMessage("label.addSuccessfully", null, locale);
        String msgAddFailed = messageSource.getMessage("label.addFailed", null, locale);
        String errorClientAlreadyExist = messageSource.getMessage("modal.errorClientAlreadyExist", null, locale);
        String unexpectedError = messageSource.getMessage("modal.errorUnexpected", null, locale);
        //System.out.println("ADD CLIENT IS CALLED");

        try {
            ClientDetailsResponseModel returnVal = new ClientDetailsResponseModel();

            ClientDTO client = new ClientDTO();
            BeanUtils.copyProperties(createClientRequestModel,client);
            /*
            SET ClientType bila diluar pilihan
             */
            if(!createClientRequestModel.getOtherField().isEmpty() &&
                    !createClientRequestModel.getOtherField().isBlank()) {
                client.setClientType(createClientRequestModel.getOtherField());
            }
            client.setStatus("ok");

            AddressDTO address = new AddressDTO();
            BeanUtils.copyProperties(createClientRequestModel,address);
            address.setRecipientName(createClientRequestModel.getContactName());

            ClientAddressEntity clientAddress = new ClientAddressEntity();
            BeanUtils.copyProperties(createClientRequestModel,clientAddress);

            ClientContactDTO pic = new ClientContactDTO();
            BeanUtils.copyProperties(createClientRequestModel,pic);


            ClientDTO createdClient = clientService.createClientWithBasicFiture(client,address,pic);
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
            model.addAttribute("activePage", "dashboard");
            //model.addAttribute("activeSub", "addClient");
        }
        //model.addAttribute("isAdd", false);
        //return "super/clients/index-clients";
        return "pages/v1/clients/index-clients";
    }

    @PutMapping("/clients/{pid}/{aid}")
    public String updateClient(
            @PathVariable("pid") String pid,
            @PathVariable("aid") String aid,
            @ModelAttribute CreateClientRequestModel updateClientRequestModel,
            Model model,
            Locale locale) {
        System.out.println("updateClient is called -> "+pid +" -> "+aid);
        String labelClient = messageSource.getMessage("label.client", null, locale);
        String successMessage = messageSource.getMessage("label.updateSuccessfully", null, locale);
        String msgUpdateFailed = messageSource.getMessage("label.updateFailed", null, locale);
        String unexpectedError = messageSource.getMessage("modal.errorUnexpected", null, locale);

        try {
            ClientDTO client = new ClientDTO();
            BeanUtils.copyProperties(updateClientRequestModel, client);
            //client.setId(clientId); // pastikan ID diset biar tahu mana yang diupdate

            AddressDTO address = new AddressDTO();
            BeanUtils.copyProperties(updateClientRequestModel, address);

            ClientContactDTO pic = new ClientContactDTO();
            BeanUtils.copyProperties(updateClientRequestModel, pic);

            ClientDTO updatedClient = clientService.updateBasicFitureClient(pid, aid, client, address, pic);
            model.addAttribute("successMessage", labelClient + " " + successMessage);

        } catch (Exception ex) {
            model.addAttribute("errorMessage", labelClient + " " + msgUpdateFailed + "<br>" + unexpectedError);
        } finally {
            List<ClientDTO> clients = clientService.getAllClients();
            model.addAttribute("ourClients", clients);
            model.addAttribute("toast-delay", 1000);
            model.addAttribute("activePage", "dashboard");
        }

        return "pages/v1/clients/index-clients";
    }
}
