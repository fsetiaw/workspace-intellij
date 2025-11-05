package com.divillafajar.app.pos.pos_app_sini.controller.thyme.bin;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.area.dto.ClientAreaDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;
import com.divillafajar.app.pos.pos_app_sini.model.guest.GuestAreaRequestModel;
import com.divillafajar.app.pos.pos_app_sini.service.area.ZoneAreaService;
import com.divillafajar.app.pos.pos_app_sini.service.client.ClientAddressService;
import com.divillafajar.app.pos.pos_app_sini.service.client.ClientService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/manager")
public class ThymeManagerController {

    private final ClientService clientService;
    private final ClientAddressService clientAddressService;
    private final ZoneAreaService zoneAreaService;

    public ThymeManagerController(ClientService clientService,
              ZoneAreaService zoneAreaService, ClientAddressService clientAddressService) {
        this.clientService=clientService;
        this.clientAddressService=clientAddressService;
        this.zoneAreaService=zoneAreaService;
    }

    @GetMapping("/home")
    public String showMgrHome(@RequestParam(name = "aid", required = true) Long aid,
            @RequestParam(name = "pid", required = true) String pid,
            @RequestParam(name = "add", required = false) Boolean add,
            @RequestParam(name = "target", required = false) String target,
            Model model) {
        System.out.println("showMgrHome IS CALLED pid = " + pid);
        System.out.println("showMgrHome IS CALLED aid= " + aid);
        ClientDTO client = clientService.getClientDetails(pid);
        ClientAddressDTO store = clientAddressService.getStore(aid);
        System.out.println("clientName = " + client.getClientName());
        model.addAttribute("target", target);
        model.addAttribute("aid", aid); //address id
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

    @PostMapping("/client/area")
    public String addGuestArea(@RequestParam(name = "addressId", required = true) Long addressId,
               @RequestParam(name = "clientPubId", required = true) String clientPubId,
                @ModelAttribute GuestAreaRequestModel guestAreaRequestModel,
            Model model) {

        String returnVal="space/index-space-area";

        ClientDTO client = clientService.getClientDetails(clientPubId);
        ClientAddressDTO store = clientAddressService.getStore(addressId);


        Optional<ClientAreaDTO> storedArea = zoneAreaService.getClientByAreaNameOrAlias(
                guestAreaRequestModel.getAreaName(),
                guestAreaRequestModel.getAlias()
        );

        Optional<List<ClientAreaDTO>> listOfArea= zoneAreaService.getAllAreaByAddressId(addressId);
        if(storedArea.isPresent()) {
            //Nama Area sudah ada
            model.addAttribute("msg","Nama Area: "+guestAreaRequestModel.getAreaName()+" sudah digunakan");
            model.addAttribute("clientError", "true");
        }
        else {


            //if (listOfArea.isEmpty() || listOfArea.get().isEmpty()) {
                // list kosong atau Optional memang tidak ada data, lanjut add
                ClientAreaDTO nuArea = new ClientAreaDTO();
                BeanUtils.copyProperties(guestAreaRequestModel,nuArea);
                zoneAreaService.addClientNewZoneArea(nuArea,addressId);

                //setelah selai diinput
                listOfArea= zoneAreaService.getAllAreaByAddressId(addressId);
            /*
            } else {
                // listOfArea ada isinya
                List<ClientAreaDTO> areas = listOfArea.get();
                areas.forEach(area -> System.out.println(area.getAreaName()));
            }

             */
        }

        model.addAttribute("addressId",addressId);
        model.addAttribute("clientPubId",clientPubId);
        model.addAttribute("listOfAreaOptional",listOfArea);
        System.out.println("Add Guest Area CALLED");
        System.out.println(guestAreaRequestModel.getAreaName());
        System.out.println(guestAreaRequestModel.getAlias());
        System.out.println(guestAreaRequestModel.getLocation());
        System.out.println(guestAreaRequestModel.getReservationType());
        System.out.println(guestAreaRequestModel.getCoolingSystem());
        System.out.println(guestAreaRequestModel.getRoomType());


        return returnVal;
    }


    @GetMapping("/client/area")
    public String goToIndexArea(@RequestParam(name = "addressId", required = true) Long addressId,
               @RequestParam(name = "clientPubId", required = true) String clientPubId,
               Model model) {

        String returnVal="space/index-space-area";
        Optional<List<ClientAreaDTO>> listOfArea= zoneAreaService.getAllAreaByAddressId(addressId);
        model.addAttribute("addressId",addressId);
        model.addAttribute("clientPubId",clientPubId);
        model.addAttribute("listOfAreaOptional",listOfArea);
        System.out.println("Add Guest Area CALLED");
        return returnVal;
    }
}
