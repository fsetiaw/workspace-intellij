package com.divillafajar.app.pos.pos_app_sini.controller.thyme.user;

import com.divillafajar.app.pos.pos_app_sini.exception.client.ClientAlreadyExistException;
import com.divillafajar.app.pos.pos_app_sini.exception.user.EmailAlreadyRegisterException;
import com.divillafajar.app.pos.pos_app_sini.exception.user.UserAlreadyExistException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.dto.UserDTO;
import com.divillafajar.app.pos.pos_app_sini.model.user.UserRegistrationRequestModel;
import com.divillafajar.app.pos.pos_app_sini.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Controller
@RequestMapping("user")
@RequiredArgsConstructor
public class ThymeUserController {

    private final UserService userService;
    private final LocaleResolver localeResolver;
    private final MessageSource messageSource;
    /*
    @GetMapping("/add/client")
    public String showTestForm(Model model) {
        //model.addAttribute("userForm", new UserForm());
        //return "form/add-new-client";
        return "main-index-dark";
    }

     */

    @GetMapping("/registrasi")
    public String showFormRegistrasi(Model model) {
        return  "pages/v1/user/registrasi";
    }

    @PostMapping("/registrasi")
    public String processFormRegistrasi(
            @ModelAttribute UserRegistrationRequestModel userRegistrationRequestModel,
            Model model,
            Locale locale
    ) {
        System.out.println("processFormRegistrasi is CALLED");
        String labelUser = messageSource.getMessage("label.user", null, locale);
        String successMessage = messageSource.getMessage("label.addSuccessfully", null, locale);
        String failedMessage = messageSource.getMessage("label.addFailed", null, locale);
        String unexpectedError = messageSource.getMessage("modal.errorUnexpected", null, locale);
        UserDTO retVal = new UserDTO();
        UserDTO newUser = new UserDTO();
        ClientDTO newClient = new ClientDTO();
        try {
            BeanUtils.copyProperties(userRegistrationRequestModel, newUser);
            BeanUtils.copyProperties(userRegistrationRequestModel, newClient);
            newClient.setClientName(userRegistrationRequestModel.getBusinessName());
            newClient.setClientType(userRegistrationRequestModel.getClientBusinessField());
            /*
            Karena ini registrasi user baru, maka rolenya ROLE_ADMIN
             */
            retVal = userService.createUser("ROLE_ADMIN", newUser, newClient);
            model.addAttribute("successMessage", labelUser+" "+successMessage+"<br>");
        } catch (EmailAlreadyRegisterException ex) {
            model.addAttribute("errorMessage", labelUser+" "+failedMessage+"<br>"+ex.getMessage());
        } catch (UserAlreadyExistException ex) {
            model.addAttribute("errorMessage", labelUser+" "+failedMessage+"<br>"+ex.getMessage());
        } catch (Exception ex) {
            // gagal server / unexpected error
            model.addAttribute("errorMessage", labelUser+" "+failedMessage+"<br>"+ex.getMessage());
        }
        return  "pages/v1/user/registrasi";
    }
}
