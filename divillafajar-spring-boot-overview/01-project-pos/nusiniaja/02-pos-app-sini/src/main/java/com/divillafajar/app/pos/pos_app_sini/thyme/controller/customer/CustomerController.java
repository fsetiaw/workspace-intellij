package com.divillafajar.app.pos.pos_app_sini.thyme.controller.customer;

import com.divillafajar.app.pos.pos_app_sini.config.security.CustomDefaultProperties;
import com.divillafajar.app.pos.pos_app_sini.ws.model.customer.CustomerLoginRequestModel;
import com.divillafajar.app.pos.pos_app_sini.ws.model.shared.dto.CustomerDTO;
import com.divillafajar.app.pos.pos_app_sini.ws.service.customer.CustomerService;
import com.divillafajar.app.pos.pos_app_sini.ws.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("customer")
public class CustomerController {

    private final CustomDefaultProperties customDefaultProperties;
    private final UserService userService;
    private final CustomerService custService;
    private AuthenticationManager authenticationManager;

    public CustomerController(UserService userService, CustomerService custService,
                              AuthenticationManager authenticationManager, CustomDefaultProperties customDefaultProperties) {
        this.userService=userService;
        this.custService=custService;
        this.authenticationManager=authenticationManager;
        this.customDefaultProperties=customDefaultProperties;
    }

    @GetMapping("/home")
    public String showCustomerHome() {
        System.out.println("showCustomerHome is CALLED");
        return "customer/home";
    }

    @GetMapping("/login")
    public String showLoginForm(Model theModel) {
        //theModel.addAttribute("theDate", java.time.LocalDateTime.now());
        System.out.println("Login Controller /loginForm is called");
        CustomerLoginRequestModel custModel = new CustomerLoginRequestModel();
        theModel.addAttribute("customer",custModel);
        return  "customer/loginPage-form";
    }

    @GetMapping("/test")
    public String showTestPage() {
        return  "customer/test";
    }



    @PostMapping("/processLoginForm")
    public String processForm(
            @Valid @ModelAttribute("customer") CustomerLoginRequestModel theCustomer,
            BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            System.out.println("ADA ERROR");
            return "customer/loginPage-form";
        }
        else {
            System.out.println("LANJUT HOME 1");
            CustomerDTO customerDTO = new CustomerDTO();
            BeanUtils.copyProperties(theCustomer,customerDTO);
            CustomerDTO savedCust = custService.loginCustomer(customerDTO);
            System.out.println("savedCust = "+savedCust.getPhoneNumber());
            try {
                System.out.println("PIT 1 ="+customDefaultProperties.getCustomerPwd());
                UsernamePasswordAuthenticationToken authRequest =
                        new UsernamePasswordAuthenticationToken(savedCust.getPhoneNumber(), customDefaultProperties.getCustomerPwd());
                System.out.println("PIT 2");
                Authentication authentication = authenticationManager.authenticate(authRequest);
                System.out.println("PIT 3");
                // set ke SecurityContext agar user dianggap sudah login
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("PIT 4");
                System.out.println("REDIRECT TO customer-login");
                /*
                **  OPSI LANGSUNG REDIRECT
                 */
                return "redirect:/customer/home";
                /*
                ** OPSI KALO MO LEWAT AUTOSUBMIT LOGIN PAGE
                 */
                //return "redirect:/customer-login?nohape="+savedCust.getPhoneNumber()+"&pwd="+customDefaultProperties.getCustomerPwd(); // arahkan ke halaman home jika berhasil login
            } catch (AuthenticationException e) {
                System.out.println("ADA ERROR");
                System.out.println("e="+e.getMessage());
                System.out.println("e="+e.toString());
                model.addAttribute("error", "Invalid username or password");
                return "main-login"; // kembali ke halaman login jika gagal
            }
            //return "home";
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
