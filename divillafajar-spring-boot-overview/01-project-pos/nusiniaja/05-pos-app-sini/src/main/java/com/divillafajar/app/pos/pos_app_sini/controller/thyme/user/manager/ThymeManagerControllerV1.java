package com.divillafajar.app.pos.pos_app_sini.controller.thyme.user.manager;

import com.divillafajar.app.pos.pos_app_sini.config.properties.CustomDefaultProperties;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.area.dto.ClientAreaDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;
import com.divillafajar.app.pos.pos_app_sini.model.guest.GuestAreaRequestModel;
import com.divillafajar.app.pos.pos_app_sini.service.area.ZoneAreaService;
import com.divillafajar.app.pos.pos_app_sini.service.client.ClientAddressService;
import com.divillafajar.app.pos.pos_app_sini.service.client.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/v1/manager")
@RequiredArgsConstructor
/*
@SessionAttributes({"toastShortTimeout","toastMediumTimeout","toastLongTimeout"})
value ini pindah ke appGlobal
 */
@SessionAttributes({"targetAddress","toastShortTimeout","toastMediumTimeout","toastLongTimeout"})
public class ThymeManagerControllerV1 {

    private final ClientService clientService;
    private final ClientAddressService clientAddressService;
    private final ZoneAreaService zoneAreaService;
    private final CustomDefaultProperties props;


    @GetMapping("/home")
    public String showMgrHome(
            @RequestParam(name = "pid", required = true) String pid,
            //@RequestParam(name = "targetAddress", required = true) ClientAddressDTO targetAddress,
            Model model, HttpSession session
    ) {
        ClientAddressDTO addressInfo = clientAddressService.getStore(pid);
        System.out.println("STARTING MANAGER HOME = "+addressInfo.getAddressName());
        System.out.println("STARTING MANAGER HOME = "+addressInfo.getId());
        model.addAttribute("targetAddress", addressInfo);
        model.addAttribute("toastShortTimeout", props.getToastShortTimeout());
        model.addAttribute("toastMediumTimeout", props.getToastMediumTimeout());
        model.addAttribute("toastLongTimeout", props.getToastLongTimeout());
        model.addAttribute("activePage", "dashboard");
        return "pages/v1/manager/index-manager";
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
            ClientAreaDTO nuArea = new ClientAreaDTO();
            BeanUtils.copyProperties(guestAreaRequestModel,nuArea);
            zoneAreaService.addClientNewZoneArea(nuArea,addressId);

            //setelah selai diinput
            listOfArea= zoneAreaService.getAllAreaByAddressId(addressId);
        }

        model.addAttribute("addressId",addressId);
        model.addAttribute("clientPubId",clientPubId);
        model.addAttribute("listOfAreaOptional",listOfArea);
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
