package com.divillafajar.app.pos.pos_app_sini.controller.thyme;

import com.divillafajar.app.pos.pos_app_sini.config.properties.CustomDefaultProperties;
import com.divillafajar.app.pos.pos_app_sini.exception.user.CreateUserException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.address.dto.AddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientContactDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.session.UserSessionLog;
import com.divillafajar.app.pos.pos_app_sini.model.client.CreateClientRequestModel;
import com.divillafajar.app.pos.pos_app_sini.model.customer.CustomerLoginRequestModel;
import com.divillafajar.app.pos.pos_app_sini.repo.users.UsersRepo;
import com.divillafajar.app.pos.pos_app_sini.service.client.ClientService;
import com.divillafajar.app.pos.pos_app_sini.service.customer.CustomerService;
import com.divillafajar.app.pos.pos_app_sini.service.user.UserService;
import com.divillafajar.app.pos.pos_app_sini.utils.GeneratorUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class LoginController {
    //@Autowired
    private final CustomerService cs;
    private final UserService us;
    private final CustomDefaultProperties customDefaultProperties;
    private final LocaleResolver localeResolver;
    private final MessageSource messageSource;
    private final ClientService clientService;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    @GetMapping("/login")
    public String showUserLoginForm(HttpSession session, Model theModel) {
        System.out.println("showUserLoginForm CALLED");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()
                && !(auth.getPrincipal() instanceof String && auth.getPrincipal().equals("anonymousUser"))) {
            System.out.println("User sudah login: " + auth.getName());
            UserSessionLog userLogInfo = (UserSessionLog) session.getAttribute("userLogInfo");
            theModel.addAttribute("userLogInfo", userLogInfo);
            return "super/index";
        } else {
            System.out.println("User belum login");
            CustomerLoginRequestModel custModel = new CustomerLoginRequestModel();
            theModel.addAttribute("customer",custModel);
            theModel.addAttribute("prefixContextPath", contextPath);
            return  "main-login";
        }

    }

    /*
    ** Set after login successfull, sesuai SecurityConfig
    ** sesuai securityConfig .defaultSuccessUrl("/home",true)
     */
    @GetMapping("/home")
    public String showHome() {
        System.out.println("showHome CALLED");
        return "home";
    }
    /*
    @GetMapping("/master/home")
    public String showSuperHome() {
        System.out.println("showHome superadmin hpme CALLED");
        //return "super/home";
        return "super/index";
    }

     */

    @GetMapping("/custom-logout")
    public String logMeOut() {
        System.out.println("logout CALLED");
        return "/custom-logout";
    }

    @GetMapping(path = "/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] getQrCode() throws Exception {
        BufferedImage qrImage = GeneratorUtils.generateQRCodeImage(
                "http://192.168.68.136:8080/customer/login?table=1", 500, 500, new File("src/main/resources/static/images/noLogo.png"));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "png", baos);
        return baos.toByteArray();
    }

    @GetMapping("/invalid-url")
    public String handleInvalidUrl(HttpServletRequest request, Model model) {
        System.out.println("invalid-url is CALLED");
        return "customer/loginPage-form";
    }

    @GetMapping("/session-expired")
    public String handleSessionExpired() {
        System.out.println("/session-expired is CALLED");
        return "session-expired";
    }
    @GetMapping("/something-wrong")
    public String handleSomethingWrong() {
        System.out.println("/something-wrong is CALLED");
        return "something-wrong";
    }

    @GetMapping("/customer-home")
    public String showCustomerHome() {
        System.out.println("showCustomerHome is CALLED");
        return "customer-home";
    }

    @GetMapping("/customer-login")
    public String showCustLogin(@RequestParam("nohape") String nohape,
                                @RequestParam("pwd") String pwd,
                                Model model) {
        System.out.println("showCustLogin is CALLED");
        model.addAttribute("username",nohape);
        model.addAttribute("password",pwd);

        return "customer/customer-login";
    }

    @GetMapping("/registrasi")
    public String showFormRegistrasi(Model model) {
        System.out.println("showFormRegistrasi is CALLED");
        //model.addAttribute("userForm", new UserForm());
        //return "form/add-new-client";
        //return "pages/v1/form-add-new-client";
        //return  "customer/loginPage-form";
        return  "pages/v1/user/registrasi";
    }

    @PostMapping("/registrasi")
    public String processRegistrasi(@ModelAttribute CreateClientRequestModel createClientRequestModel, Model model, Locale locale
    ) {
        System.out.println("processRegistrasi is CALLED=>"+createClientRequestModel.getUsername());
        String msg = "false";
        String labelClient = messageSource.getMessage("label.client", null, locale);
        String successMessage = messageSource.getMessage("label.addSuccessfully", null, locale);
        String msgAddFailed = messageSource.getMessage("label.addFailed", null, locale);
        String errorClientAlreadyExist = messageSource.getMessage("modal.errorClientAlreadyExist", null, locale);
        String unexpectedError = messageSource.getMessage("modal.errorUnexpected", null, locale);


        ClientDTO clientDTO = new ClientDTO();
        BeanUtils.copyProperties(createClientRequestModel,clientDTO);

        AddressDTO addressDTO = new AddressDTO();
        BeanUtils.copyProperties(createClientRequestModel,addressDTO);

        ClientContactDTO contactDTO = new ClientContactDTO();
        BeanUtils.copyProperties(createClientRequestModel,contactDTO);
        try {
            clientService.createClientAdmin(clientDTO, addressDTO, contactDTO);
        }
        catch(CreateUserException e) {
            model.addAttribute("errorMessage", labelClient+" "+msgAddFailed+"<br>"+e.getMessage());
        }
        catch(Exception e) {
            model.addAttribute("errorMessage", labelClient+" "+msgAddFailed+"<br>"+unexpectedError);
        }

        return  "pages/v1/clients/registrasi";
    }

}
