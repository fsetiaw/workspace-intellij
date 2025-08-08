package com.divillafajar.app.pos.pos_app_sini.thyme.controller.customer;

import com.divillafajar.app.pos.pos_app_sini.config.security.CustomDefaultProperties;
import com.divillafajar.app.pos.pos_app_sini.ws.model.customer.AuthenticatedCustomerModel;
import com.divillafajar.app.pos.pos_app_sini.ws.model.customer.CustomerLoginRequestModel;
import com.divillafajar.app.pos.pos_app_sini.ws.model.shared.dto.CustomerDTO;
import com.divillafajar.app.pos.pos_app_sini.ws.service.customer.CustomerService;
import com.divillafajar.app.pos.pos_app_sini.ws.service.user.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("customer")
public class CustomerController {

    private final CustomDefaultProperties customDefaultProperties;
    private final UserService userService;
    private final CustomerService custService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationSuccessHandler customAuthenticationSuccessHandler;
    //private final AuthenticatedCustomerModel authenticatedCustomerModel;
    public CustomerController(UserService userService, CustomerService custService,
                              AuthenticationManager authenticationManager,
                              CustomDefaultProperties customDefaultProperties,
                              AuthenticationSuccessHandler customAuthenticationSuccessHandler
                              //AuthenticatedCustomerModel authenticatedCustomerModel
                              ) {
        this.userService=userService;
        this.custService=custService;
        this.authenticationManager=authenticationManager;
        this.customDefaultProperties=customDefaultProperties;
        this.customAuthenticationSuccessHandler=customAuthenticationSuccessHandler;
        //this.authenticatedCustomerModel=authenticatedCustomerModel;
    }

    @GetMapping("/home")
    public String showCustomerHome(Model model, HttpSession session) {
        System.out.println("showCustomerHome is CALLED ="+session.getId());
        return "customer/home";
    }

    @GetMapping("/login")
    public String showLoginForm(
            @RequestParam(value = "table", required = false) String table,
            Model theModel,
            Authentication authentication) {
        System.out.println("customer Login Controller /loginForm is called");
        System.out.println("table = "+table);
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("user sudah auntehenticated");
            return "customer/home";
        }
        //theModel.addAttribute("theDate", java.time.LocalDateTime.now());

        CustomerLoginRequestModel custModel = new CustomerLoginRequestModel();
        System.out.println("custModel table = "+custModel.getTable());
        custModel.setTable(table);
        System.out.println("custModel table2 = "+custModel.getTable());
        theModel.addAttribute("customer",custModel);

        return  "customer/loginPage-form";
    }

    @GetMapping("/test")
    public String showTestPage() {
        return  "customer/test";
    }



    @PostMapping("/processLoginForm")
    public void processForm(
            @Valid @ModelAttribute("customer") CustomerLoginRequestModel theCustomer,
            BindingResult bindingResult,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {
        System.out.println("theCustomer.getUsername()="+theCustomer.getUsername());
        System.out.println("theCustomer.getAliasName()="+theCustomer.getAliasName());
        theCustomer.setPassword(customDefaultProperties.getCustomerPwd());
        if(bindingResult.hasErrors()) {
            System.out.println("bindingResult.hasErrors()");
            System.out.println(bindingResult.getAllErrors());
            //return "customer/loginPage-form";
        }
        else {

            CustomerDTO customerDTO = new CustomerDTO();
            BeanUtils.copyProperties(theCustomer,customerDTO);
            System.out.println("customerDTO.getUsername()="+customerDTO.getUsername());
            CustomerDTO savedCust = custService.loginCustomer(customerDTO);
            System.out.println("savedCust.getUsername()="+savedCust.getUsername());
            try {
                UsernamePasswordAuthenticationToken authRequest =
                        new UsernamePasswordAuthenticationToken(savedCust.getPhoneNumber(), customDefaultProperties.getCustomerPwd());
                Authentication authentication = authenticationManager.authenticate(authRequest);
                // set ke SecurityContext agar user dianggap sudah login
                HttpSession session = request.getSession(true);
                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                        SecurityContextHolder.getContext());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                /*
                ** Panggil Custom Handler
                 */
                try {
                    customAuthenticationSuccessHandler.onAuthenticationSuccess(request,
                            response, authentication);

                    /*
                    ** sukses aunthenticated, create session
                     */
                    AuthenticatedCustomerModel nuCust = new AuthenticatedCustomerModel();
                    System.out.println("the Customer username = "+theCustomer.getUsername());
                    System.out.println("the Customer alias= "+theCustomer.getAliasName());
                    nuCust.setName(theCustomer.getAliasName());
                    nuCust.setPhone(theCustomer.getUsername());
                    nuCust.setTable(theCustomer.getTable());
                    session.setAttribute("loggedInCustomer",nuCust);
                } catch (IOException | ServletException e) {
                    // Bisa log error atau redirect ke error page
                    e.printStackTrace();
                    model.addAttribute("error", "Terjadi kesalahan saat login.");
                    //return "main-login"; // atau redirect ke halaman error khusus
                }
                //HttpSession session = request.getSession(false); // false = jangan buat session kalau belum ada
                if(session != null) {
                    System.out.println("Session ID: " + session.getId());
                }
                else {
                    System.out.println("No session");
                }


                /*
                **  OPSI LANGSUNG REDIRECT
                 */
                //return "redirect:/customer/home";
                /*
                ** OPSI KALO MO LEWAT AUTOSUBMIT LOGIN PAGE
                 */
                //return "redirect:/customer-login?nohape="+savedCust.getPhoneNumber()+"&pwd="+customDefaultProperties.getCustomerPwd(); // arahkan ke halaman home jika berhasil login
            } catch (AuthenticationException e) {
                model.addAttribute("error", "Invalid username or password");
                //return "main-login"; // kembali ke halaman login jika gagal
            }
            //return "home";
        }
    }


}

