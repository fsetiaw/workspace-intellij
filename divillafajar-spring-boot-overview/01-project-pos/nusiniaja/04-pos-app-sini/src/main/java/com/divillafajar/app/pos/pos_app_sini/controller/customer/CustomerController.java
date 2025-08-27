package com.divillafajar.app.pos.pos_app_sini.controller.customer;

import com.divillafajar.app.pos.pos_app_sini.config.properties.CustomDefaultProperties;
import com.divillafajar.app.pos.pos_app_sini.exception.user.CreateUserException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.session.UserSessionLog;
import com.divillafajar.app.pos.pos_app_sini.repo.session.UserSessionLogRepository;
import com.divillafajar.app.pos.pos_app_sini.model.customer.CustomerLoginRequestModel;
import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.dto.CustomerDTO;
import com.divillafajar.app.pos.pos_app_sini.service.customer.CustomerService;
import com.divillafajar.app.pos.pos_app_sini.service.user.UserService;
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

    @GetMapping("/session-expired")
    public String showSessionExpired(Model model) {
        System.out.println("showSessionExpired is CALLED =");
        return "test";
    }

    @GetMapping("/login")
    public String showLoginForm(
            @RequestParam(value = "clientId", required = false) Long clientId,
            @RequestParam(value = "table", required = false) String table,
            Model theModel,
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {
        System.out.println("customer Login Controller /loginForm is called");
        System.out.println("table = "+table);
        if (authentication != null && authentication.isAuthenticated()) {
            /*
            ** cek apa session idnya masih ada di DB
             */
            HttpSession session = request.getSession(true);
            if(session!=null) {
                String sessionId=session.getId();
                Optional<UserSessionLog> activeSession = sessionLogRepo.findBySessionIdAndStatus(sessionId, "ACTIVE");
                if (activeSession.isPresent()) {
                    String activeRole = activeSession.get().getRole();
                    return "customer/home";
                } else {
                    /*
                     *  sessionExist tidak ditemukan di DB (kasus bila tipa2 data di db hilang/corrupt
                     *  -> sudah di handle SessionValidationFilter
                     */
                }
            }

            //return "customer/home";
        }
        //theModel.addAttribute("theDate", java.time.LocalDateTime.now());

        CustomerLoginRequestModel custModel = new CustomerLoginRequestModel();
        custModel.setClientId(clientId);
        custModel.setTable(table);
        System.out.println("custModel client id = "+custModel.getClientId());
        System.out.println("custModel table = "+custModel.getTable());
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

            // Prep Param TheCustomer biar bisa diakses di  customAuthenticationSuccessHandler
            request.setAttribute("theCustomer", theCustomer);

            CustomerDTO customerDTO = new CustomerDTO();
            BeanUtils.copyProperties(theCustomer,customerDTO);

            try {
                CustomerDTO savedCust = custService.loginCustomer(customerDTO);

                /*
                ** START AUTHENTICATION
                 */
                UsernamePasswordAuthenticationToken authRequest =
                        new UsernamePasswordAuthenticationToken(savedCust.getPhoneNumber(), customDefaultProperties.getCustomerPwd());
                Authentication authentication = authenticationManager.authenticate(authRequest);
                // set ke SecurityContext agar user dianggap sudah login
                HttpSession session = request.getSession(true);
                request.setAttribute("clientId", theCustomer.getClientId());
                request.setAttribute("table", theCustomer.getTable());
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

                    AuthenticatedCustomerModel nuCust = new AuthenticatedCustomerModel();
                    System.out.println("the Customer username = "+theCustomer.getUsername());
                    System.out.println("the Customer alias= "+theCustomer.getAliasName());
                    System.out.println("the Customer clientId= "+theCustomer.getClientId());
                    System.out.println("the Customer yable= "+theCustomer.getTable());

                    nuCust.setName(theCustomer.getAliasName());
                    nuCust.setPhone(theCustomer.getUsername());
                    nuCust.setTable(theCustomer.getTable());
                    nuCust.setClientId(theCustomer.getClientId());
                    session.setAttribute("loggedInCustomer",nuCust);

                     */
                } catch (IOException | ServletException e) {
                    // Bisa log error atau redirect ke error page
                    e.printStackTrace();
                    request.removeAttribute("theCustomer");
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
            }
            catch (CreateUserException ce) {
                model.addAttribute("error", ce.getMessage());
                //lanjurt return to loginPage-form
            }
            catch (AuthenticationException e) {
                model.addAttribute("error", "Invalid username or password");
                //return "main-login"; // kembali ke halaman login jika gagal
            }

        }
        return  "customer/loginPage-form";
    }


}

