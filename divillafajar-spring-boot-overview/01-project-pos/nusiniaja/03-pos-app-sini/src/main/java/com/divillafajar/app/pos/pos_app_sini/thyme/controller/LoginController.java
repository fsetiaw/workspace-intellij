package com.divillafajar.app.pos.pos_app_sini.thyme.controller;

import com.divillafajar.app.pos.pos_app_sini.ws.model.customer.CustomerLoginRequestModel;
import com.divillafajar.app.pos.pos_app_sini.ws.service.customer.CustomerService;
import com.divillafajar.app.pos.pos_app_sini.ws.service.user.UserService;
import com.divillafajar.app.pos.pos_app_sini.ws.utils.GeneratorUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

@Controller
//@RequestMapping("welcome")
public class LoginController {
    //@Autowired
    private final CustomerService cs;
    private final UserService us;
    public LoginController(CustomerService cs, UserService us) {
        this.cs=cs;
        this.us=us;
    }


    @GetMapping("/login")
    public String showUserLoginForm(Model theModel) {
        System.out.println("showUserLoginForm CALLED");
        CustomerLoginRequestModel custModel = new CustomerLoginRequestModel();
        theModel.addAttribute("customer",custModel);
        return  "main-login";
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

}
