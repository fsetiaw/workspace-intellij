package com.divillafajar.app.pos.pos_app_sini.thyme.controller.customer;

import com.divillafajar.app.pos.pos_app_sini.config.security.CustomDefaultProperties;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserSessionLog;
import com.divillafajar.app.pos.pos_app_sini.repo.session.UserSessionLogRepository;
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
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("customer")
public class CustomerController {

    private final CustomDefaultProperties customDefaultProperties;
    private final UserService userService;
    private final CustomerService custService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final UserSessionLogRepository sessionLogRepo;
    //private final AuthenticatedCustomerModel authenticatedCustomerModel;
    public CustomerController(UserService userService, CustomerService custService,
                              AuthenticationManager authenticationManager,
                              CustomDefaultProperties customDefaultProperties,
                              AuthenticationSuccessHandler customAuthenticationSuccessHandler,
                              UserSessionLogRepository sessionLogRepo
                              //AuthenticatedCustomerModel authenticatedCustomerModel
                              ) {
        this.userService=userService;
        this.custService=custService;
        this.authenticationManager=authenticationManager;
        this.customDefaultProperties=customDefaultProperties;
        this.customAuthenticationSuccessHandler=customAuthenticationSuccessHandler;
        this.sessionLogRepo=sessionLogRepo;
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
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {
        System.out.println("customer Login Controller /loginForm is called");
        System.out.println("table = "+table);
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("user sudah auntehenticated");
            /*
            ** cek apa session idnya masih ada di DB
             */
            HttpSession session = request.getSession(true);
            if(session!=null) {
                System.out.println("user auntehenticated session= "+session.getId());
                String sessionId=session.getId();
                Optional<UserSessionLog> logOpt = sessionLogRepo.findBySessionIdAndStatus(sessionId, "ACTIVE");
                if (logOpt.isPresent()) {
                    System.out.println("logOpt ditemukan");
                    // Jika session log ditemukan
                    //UserSessionLog log = logOpt.get();
                    //log.setStatus("LOGOUT");
                    //log.setLogoutTime(LocalDateTime.now());
                    //sessionLogRepo.save(log);
                    return "customer/home";
                } else {
                    // session tidak valid di DB → logout user
                    System.out.println("logOpt tidak ditemukan");
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
                    logoutHandler.logout(request, response, auth);

                    // redirect ke login dengan parameter expired
                    //response.sendRedirect("/login?expired");
                }
            }

            //return "customer/home";
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
        System.out.println("/processLoginForm");
        theCustomer.setPassword(customDefaultProperties.getCustomerPwd());
        if(bindingResult.hasErrors()) {
            System.out.println("bindingResult.hasErrors()");
            System.out.println(bindingResult.getAllErrors());
            //return "customer/loginPage-form";
        }
        else {

            CustomerDTO customerDTO = new CustomerDTO();
            BeanUtils.copyProperties(theCustomer,customerDTO);
            CustomerDTO savedCust = custService.loginCustomer(customerDTO);
            try {
                /*
                ** START AUTHENTICATION
                 */
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
                    System.out.println("customAuthenticationSuccessHandler is CALLED");
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

