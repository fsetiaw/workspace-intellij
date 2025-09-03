package com.divillafajar.app.pos.pos_app_sini.controller.thyme.user.manager;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;
import com.divillafajar.app.pos.pos_app_sini.service.client.ClientAddressService;
import com.divillafajar.app.pos.pos_app_sini.service.client.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/manager")
public class ThymeManagerController {

    private final ClientService clientService;
    private final ClientAddressService clientAddressService;

    public ThymeManagerController(ClientService clientService,
                                  ClientAddressService clientAddressService) {
        this.clientService=clientService;
        this.clientAddressService=clientAddressService;
    }

    @GetMapping("/home")
    public String showMgrHome(@RequestParam(name = "aid", required = true) Long aid,
            @RequestParam(name = "pid", required = true) String pid,
            @RequestParam(name = "add", required = false) Boolean add, Model model) {
        System.out.println("showAdminHome IS CALLED = " + pid);
        ClientDTO client = clientService.getClientDetails(pid);
        ClientAddressDTO store = clientAddressService.getStore(aid);
        System.out.println("clientName = " + client.getClientName());

        model.addAttribute("isAdd", add);
        model.addAttribute("pid",pid);
        model.addAttribute("client", client);
        model.addAttribute("store", store);
        System.out.println("val isAdd = " + model.getAttribute("isAdd"));
        System.out.println("val pid = " + model.getAttribute("pid"));
        System.out.println("val store name = " + store.getAddressName());
        //return "super/clients/home/home-client";
        return "manager/index-mgm";
    }
}
