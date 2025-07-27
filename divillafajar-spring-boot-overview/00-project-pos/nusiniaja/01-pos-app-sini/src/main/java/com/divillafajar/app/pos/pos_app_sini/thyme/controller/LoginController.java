package com.divillafajar.app.pos.pos_app_sini.thyme.controller;

import com.divillafajar.app.pos.pos_app_sini.ws.model.customer.CustomerLoginRequestModel;
import com.divillafajar.app.pos.pos_app_sini.ws.service.customer.CustomerService;
import com.divillafajar.app.pos.pos_app_sini.ws.service.user.UserService;
import jakarta.validation.Valid;
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
        System.out.println("USER Controller /showUserLoginForm is called");
        CustomerLoginRequestModel custModel = new CustomerLoginRequestModel();
        theModel.addAttribute("customer",custModel);
        return  "main-login";
    }

    @GetMapping("/customer")
    public String showLoginForm(Model theModel) {
        //theModel.addAttribute("theDate", java.time.LocalDateTime.now());
        System.out.println("Login Controller /loginForm is called");
        CustomerLoginRequestModel custModel = new CustomerLoginRequestModel();
        theModel.addAttribute("customer",custModel);
        return  "customer/loginPage-form";
    }



    @PostMapping("customer/processLoginForm")
    public String processForm(
            @Valid @ModelAttribute("customer") CustomerLoginRequestModel theCustomer,
            BindingResult bindingResult) {
        System.out.println("theCustomer = "+theCustomer.getAliasName());
        System.out.println("theCustomer = "+theCustomer.getPhoneNumber());
        if(bindingResult.hasErrors()) {
            System.out.println("ADA ERROR");
            return "customer/loginPage-form";
        }
        else {
            System.out.println("LANJUT HOME");
            return "home";
        }


        /*
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(theCustomer,customerDTO);
        //
        // cek apa cust sudah ada, bila belum maka create baru
        //
        customerDTO = cs.createOrGetCustomer(customerDTO);



        //alihkan ke main page
        return "customer/processLoginForm";

         */
    }
}
