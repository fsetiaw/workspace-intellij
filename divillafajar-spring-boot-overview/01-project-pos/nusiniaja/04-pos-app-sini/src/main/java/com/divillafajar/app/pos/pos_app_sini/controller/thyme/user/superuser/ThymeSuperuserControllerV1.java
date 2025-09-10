package com.divillafajar.app.pos.pos_app_sini.controller.thyme.user.superuser;

import com.divillafajar.app.pos.pos_app_sini.exception.client.ClientAlreadyExistException;
import com.divillafajar.app.pos.pos_app_sini.exception.user.CreateUserException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.address.dto.AddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientContactDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.session.UserSessionLog;
import com.divillafajar.app.pos.pos_app_sini.model.client.ClientDetailsResponseModel;
import com.divillafajar.app.pos.pos_app_sini.model.client.CreateClientRequestModel;
import com.divillafajar.app.pos.pos_app_sini.service.client.ClientService;
import com.divillafajar.app.pos.pos_app_sini.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("v1/superuser")
public class ThymeSuperuserControllerV1 {

    private final UserService userService;
    private final ClientService clientService;
    private LocaleResolver localeResolver;

    public ThymeSuperuserControllerV1(UserService userService, ClientService clientService,
                                      LocaleResolver localeResolver) {
        this.userService=userService;
        this.clientService=clientService;
        this.localeResolver=localeResolver;
    }

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
    public String showAllClients(@RequestParam(name = "add", required = false) Boolean add, Model model) {
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
        model.addAttribute("activeSub", "addClient");
        System.out.println("showAddClientForm is called");
        return "pages/v1/clients/add-clients";
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
    public String addClients(@ModelAttribute CreateClientRequestModel createClientRequestModel, Model model) {
        String msg = "false";
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
            model.addAttribute("clientAdded", "true");
            //BeanUtils.copyProperties(createdClient,returnVal);

            //return ResponseEntity.status(HttpStatus.CREATED).body(returnVal);
        } catch (
        ClientAlreadyExistException ex) {
            // gagal server / unexpected error

            model.addAttribute("msg",ex.getMessage());
            model.addAttribute("clientError", "true");
        } catch (
        CreateUserException ex) {
            // gagal server / unexpected error
            model.addAttribute("msg",ex.getMessage());
            model.addAttribute("clientError", "true");
        } catch (Exception ex) {
            // gagal server / unexpected error
            model.addAttribute("msg",ex.getMessage());
            model.addAttribute("clientError", "true");
        }
        finally {
            List<ClientDTO> clients = clientService.getAllClients();
            model.addAttribute("ourClients", clients);
        }
        //model.addAttribute("isAdd", false);
        return "super/clients/index-clients";
        //return "redirect:super/clients/index-clients";
    }
}
