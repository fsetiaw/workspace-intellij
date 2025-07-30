package com.divillafajar.app.pos.pos_app_sini.thyme.controller;

import com.divillafajar.app.pos.pos_app_sini.ws.model.customer.CustomerLoginRequestModel;
import com.divillafajar.app.pos.pos_app_sini.ws.service.customer.CustomerService;
import com.divillafajar.app.pos.pos_app_sini.ws.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
        //theModel.addAttribute("theDate", java.time.LocalDateTime.now());
        System.out.println("Login Controller /showUserLoginForm is called");
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
        System.out.println("showHome is CALLED");
        return "home";
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
