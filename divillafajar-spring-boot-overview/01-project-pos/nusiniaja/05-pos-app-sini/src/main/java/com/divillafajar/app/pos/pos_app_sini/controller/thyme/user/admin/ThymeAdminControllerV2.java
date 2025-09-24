package com.divillafajar.app.pos.pos_app_sini.controller.thyme.user.admin;

import com.divillafajar.app.pos.pos_app_sini.exception.DuplicationErrorException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientContactDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.session.UserSessionLog;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.dto.UserDTO;
import com.divillafajar.app.pos.pos_app_sini.model.client.location.CreateClientLocationRequestModel;
import com.divillafajar.app.pos.pos_app_sini.model.user.UserLogedInModel;
import com.divillafajar.app.pos.pos_app_sini.service.client.ClientAddressService;
import com.divillafajar.app.pos.pos_app_sini.service.client.ClientService;
import com.divillafajar.app.pos.pos_app_sini.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("v2/admin")
public class ThymeAdminControllerV2 {
    private final ClientAddressService clientAddressService;
    private final ClientService clientService;
    private final LocaleResolver localeResolver;
    private final UserService userService;
    private final MessageSource messageSource;

    public ThymeAdminControllerV2(ClientService clientService, LocaleResolver localeResolver,
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
            @RequestParam(name = "add", required = false) Boolean add,
            //@RequestParam(name = "lang", required = false) String lang,
            Model model, HttpSession session
    ) {
        //System.out.println("showAdminHome V2 IS CALLED = " + pid);
        UserSessionLog userLogInfo = (UserSessionLog) session.getAttribute("userLogInfo");
        //pid = userLogInfo.getUsername();
        /*
        if (lang != null) {
            Locale locale = new Locale(lang);
            localeResolver.setLocale(request, response, LocaleContextHolder.getLocale()));
        }

         */

        //ClientDTO client = clientService.getClientDetails(pid);
        //List<ClientAddressEntity> storesLocation = client.getClientAddresses();
        model.addAttribute("isAdd", add);
        model.addAttribute("pid",userLogInfo.getUsername());
        model.addAttribute("activePage", "dashboard");
        //model.addAttribute("clientType", client.getClientType());
        //model.addAttribute("clientName", client.getClientName());
        //model.addAttribute("ourStores", storesLocation);
        //System.out.println("val lang client.clientName= " + model.getAttribute("client"));
        //return "super/clients/home/home-client";
        return "pages/v1/admin/index-admin";
    }

    @GetMapping("/add/location")
    public String showAddLocationForm(
            @RequestParam(name = "pid", required = false) String pid,
            @RequestParam(name = "clientName", required = false) String clientName,
          @RequestParam(name = "add", required = false) Boolean add, Model model) {
        System.out.println("showAddLocationForm CALLED = "+clientName+pid);
        model.addAttribute("pid", pid);
        model.addAttribute("clientName", clientName);
        model.addAttribute("isAdd", add);
        model.addAttribute("activePage", "location");
        model.addAttribute("activeSub", "addLocation");
        System.out.println("showAddClientForm is called");
        return "pages/v1/admin/add-location";
    }

    @PostMapping("/add/location")
    public String addLokasi(
            @ModelAttribute CreateClientLocationRequestModel createClientLocationRequestModel,
            @RequestParam(name = "pid", required = false) String pid,
            RedirectAttributes redirectAttributes, HttpSession session,
            Model model, Locale locale
    ) {
        String msg = "false";
        String labelLocation = messageSource.getMessage("label.location", null, LocaleContextHolder.getLocale());
        String successMessage = messageSource.getMessage("label.addSuccessfully", null, LocaleContextHolder.getLocale());
        String msgAddFailed = messageSource.getMessage("label.addFailed", null, LocaleContextHolder.getLocale());
        String errorClientLocationAlreadyExist = messageSource.getMessage("modal.errorClientLocationAlreadyExist", null, LocaleContextHolder.getLocale());
        String unexpectedError = messageSource.getMessage("modal.errorUnexpected", null, LocaleContextHolder.getLocale());
        System.out.println("addLokasi IS CALLED -- "+createClientLocationRequestModel.getAddressName());
        try {
            UserSessionLog userLog =  (UserSessionLog)session.getAttribute("userLogInfo");
            UserDTO storedUser = userService.getUser(userLog.getUserPid());

            ClientDTO targetClient = clientService.getClientDetails(userLog.getClientPid());
            //cek apa nama busines sudah pernah dipalai
            System.out.println("storedUser=="+storedUser.getFirstName());
            ClientAddressDTO nuLocation = new ClientAddressDTO();
            BeanUtils.copyProperties(createClientLocationRequestModel,nuLocation);
            System.out.println("nuLocation=="+createClientLocationRequestModel.getLocationCategory());
            System.out.println("nuLocation=="+createClientLocationRequestModel.getGuestCapacity());
            System.out.println("nuLocation=="+nuLocation.getLocationCategory());
            System.out.println("nuLocation=="+nuLocation.getGuestCapacity());
            ClientAddressDTO storedAddress = clientAddressService.addNewStore(storedUser, targetClient, nuLocation);
            model.addAttribute("successMessage", labelLocation+" "+successMessage+"<br>");
        } catch (DuplicationErrorException ex) {
            model.addAttribute("errorMessage", labelLocation+" "+msgAddFailed+"<br>"+ex.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        /*
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

             */
        return "redirect:/v2/admin/home";
    }

}
