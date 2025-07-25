package com.divillafajar.app.pos.pos_app_sini.thyme.controller;

import com.divillafajar.app.pos.pos_app_sini.ws.model.customer.CustomerLoginRequestModel;
import com.divillafajar.app.pos.pos_app_sini.ws.service.customer.CustomerService;
import com.divillafajar.app.pos.pos_app_sini.ws.model.shared.dto.CustomerDTO;
import com.divillafajar.app.pos.pos_app_sini.ws.service.user.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("welcome")
public class LoginController {
    //@Autowired
    private final CustomerService cs;
    private final UserService us;
    public LoginController(CustomerService cs, UserService us) {
        this.cs=cs;
        this.us=us;
    }

    @GetMapping("/login")
    public String showLoginForm(Model theModel) {
        //theModel.addAttribute("theDate", java.time.LocalDateTime.now());
        System.out.println("Login Controller /loginForm is called");
        CustomerLoginRequestModel custModel = new CustomerLoginRequestModel();
        theModel.addAttribute("customer",custModel);
        return  "customer/loginPage-form";
    }



    @PostMapping("/processLoginForm")
    public String processForm(@ModelAttribute("customer") CustomerLoginRequestModel theCustomer) throws Exception{

        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(theCustomer,customerDTO);
        //cek apa cust sudah ada, bila belum maka create baru
        customerDTO = cs.createOrGetCustomer(customerDTO);



        //alihkan ke main page
        return "customer/processLoginForm";
    }
}
