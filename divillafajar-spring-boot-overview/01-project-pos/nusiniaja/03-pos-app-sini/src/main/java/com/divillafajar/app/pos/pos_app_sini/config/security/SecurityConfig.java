package com.divillafajar.app.pos.pos_app_sini.config.security;

import com.divillafajar.app.pos.pos_app_sini.config.filter.SessionValidationFilter;
import com.divillafajar.app.pos.pos_app_sini.config.security.jwt.JwtAuthFilter;
import com.divillafajar.app.pos.pos_app_sini.repo.session.UserSessionLogRepository;
import com.divillafajar.app.pos.pos_app_sini.ws.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.sql.DataSource;
import java.time.Duration;
import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Autowired
    private UserSessionLogRepository sessionLogRepo;

    @Autowired
    private SessionValidationFilter sessionValidationFilter;

    @Autowired
    private CustomLogoutHandler customLogoutHandler;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;


    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    public SecurityConfig(@Lazy JwtUtil jwtUtil,
                          @Lazy UserDetailsService uds,
                          CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
                          CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = uds;
        this.customAuthenticationSuccessHandler=customAuthenticationSuccessHandler;
        this.customAccessDeniedHandler=customAccessDeniedHandler;
    }

    // Add support for JDBC
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        System.out.println("AuthenticationManager CALLED");
        return authConfig.getAuthenticationManager();
    }


    @Bean
    @Order(1) // harus lebih tinggi dari yang formLogin
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        JwtAuthFilter jwtFilter = new JwtAuthFilter(jwtUtil, userDetailsService);
        System.out.println("apiFilterChain");
        http
                .securityMatcher("/api/**") // hanya untuk endpoint api
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login", "/api/users/register/**").permitAll()
                        .requestMatchers("/customer/session-expired","/session-expired").hasAnyRole("EMPLOYEE","MANAGER","ADMIN","CUSTOMER")
                        .requestMatchers("/customer/home","/api/customer","/session-expired").hasRole("CUSTOMER")
                        .requestMatchers("/api/customer/**").hasRole("CUSTOMER")
                        .requestMatchers("/api/users/**").hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    @Order(2) // lebih rendah prioritasnya
    public SecurityFilterChain formFilterChain(HttpSecurity http) throws Exception {
        System.out.println("formFilterChain");
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/customer/session-expired","/session-expired").hasAnyRole("EMPLOYEE","MANAGER","ADMIN","CUSTOMER")
                        .requestMatchers("/customer/home","/api/customer","/session-expired").hasRole("CUSTOMER")
                        .requestMatchers("/api/customer/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/home").hasAnyRole("EMPLOYEE","MANAGER","ADMIN","CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("MANAGER") //create employee
                        .requestMatchers(HttpMethod.PUT, "/api/users").hasRole("MANAGER") //update employee
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(
                                "/qrcode","/invalid-url",
                                "/login","/custom-logout", //user login form
                                "/customer/login", //customer login form
                                "/customer-login", //redirect page->versi autosubmit login via main-login (unused)
                                "/customer/processLoginForm",  //process login
                                "/swagger-ui/**", "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                /*
                .formLogin(form -> form
                        .loginPage("/customer/login")
                        .loginProcessingUrl("/authenticateTheUser")
                        .successHandler(customAuthenticationSuccessHandler)
                        .permitAll()
                )

                 */
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler(customLogoutHandler) // pakai custom logout handler
                        .logoutSuccessHandler(customLogoutSuccessHandler)
                        /*
                        .addLogoutHandler((request, response, auth) -> {
                            if (request.getSession(false) != null) {
                                String sessionId = request.getSession().getId();
                                sessionLogRepo.findBySessionIdAndStatus(sessionId, "ACTIVE").ifPresent(log -> {
                                    log.setStatus("LOGOUT");
                                    log.setLogoutTime(LocalDateTime.now());
                                    sessionLogRepo.save(log);
                                });
                            }
                        })
                         */
                        .permitAll()
                )
                //.logout(logout -> logout.permitAll())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess
                        //.invalidSessionUrl("/invalid-url")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        //.expiredUrl("/session-expired")

                        //.expiredUrl("/login?expired")
                        .sessionRegistry(sessionRegistry()))
                        //sess.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                ;
        http.addFilterBefore(sessionValidationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }


}
