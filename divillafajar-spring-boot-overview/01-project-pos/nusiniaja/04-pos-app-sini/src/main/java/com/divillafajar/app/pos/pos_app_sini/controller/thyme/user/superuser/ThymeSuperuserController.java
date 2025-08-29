package com.divillafajar.app.pos.pos_app_sini.controller.thyme.user.superuser;

import com.divillafajar.app.pos.pos_app_sini.exception.client.ClientAlreadyExistException;
import com.divillafajar.app.pos.pos_app_sini.exception.model.GenericErrorResponse;
import com.divillafajar.app.pos.pos_app_sini.exception.user.CreateUserException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.address.dto.AddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientContactDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;
import com.divillafajar.app.pos.pos_app_sini.model.client.ClientDetailsResponseModel;
import com.divillafajar.app.pos.pos_app_sini.model.client.CreateClientAndContactPersonRequestModel;
import com.divillafajar.app.pos.pos_app_sini.model.client.CreateClientRequestModel;
import com.divillafajar.app.pos.pos_app_sini.service.client.ClientService;
import com.divillafajar.app.pos.pos_app_sini.service.user.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("superuser")
public class ThymeSuperuserController {

    private final UserService userService;
    private final ClientService clientService;

    public ThymeSuperuserController(UserService userService, ClientService clientService) {
        this.userService=userService;
        this.clientService=clientService;
    }

    @GetMapping("/home")
    public String showHome(Model model) {
        //model.addAttribute("userForm", new UserForm());
        //return "form/add-new-client";

        return "super/index.html";
    }

    @GetMapping("/clients")
    public String showAllClients(@RequestParam(name = "add", required = false) Boolean add, Model model) {
        List<ClientDTO> clients = clientService.getAllClients();
        model.addAttribute("isAdd", add);
        model.addAttribute("ourClients", clients);
        System.out.println("model attr val = " + model.getAttribute("isAdd"));
        return "super/clients/index-clients";
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
