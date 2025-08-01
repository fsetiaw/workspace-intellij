package com.divillafajar.app.pos.pos_app_sini.config.security;

import com.divillafajar.app.pos.pos_app_sini.config.security.jwt.JwtAuthFilter;
import com.divillafajar.app.pos.pos_app_sini.ws.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    public SecurityConfig(@Lazy JwtUtil jwtUtil,
                          @Lazy UserDetailsService uds,
                          CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = uds;
        this.customAuthenticationSuccessHandler=customAuthenticationSuccessHandler;
    }
    //@Autowired
    //private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    // Add support for JDBC
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }






    /*
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("SecurityFilterChain START");
        JwtAuthFilter jwtFilter = new JwtAuthFilter(jwtUtil, userDetailsService);
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/home","/api/customer").hasRole("CUSTOMER")
                        .requestMatchers("/api/customer/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("MANAGER") //create employee
                        .requestMatchers(HttpMethod.PUT, "/api/users").hasRole("MANAGER") //update employee
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(

                                "/customer/login","/customer-home","/customer/home","/customer-login","/customer/processLoginForm",
                                "/api/login",
                                "/api/users/register/**",      //sign up
                                "/swagger-ui.html",         // untuk versi lama
                                "/swagger-ui/**",           // UI assets
                                "/v3/api-docs",             // root OpenAPI
                                "/v3/api-docs/**",          // jika endpoint dibagi
                                "/swagger-resources/**",    // kalau masih digunakan
                                "/webjars/**"           //kebutuhan swagger
                        ).permitAll()
                        .anyRequest().authenticated()
            )
            .formLogin(form ->
                    form
                            .loginPage("/login")  //sesuai path
                            .loginProcessingUrl("/authenticateTheUser")
                            .successHandler(customAuthenticationSuccessHandler)
                            //.defaultSuccessUrl("/home",true)
                            .permitAll()
            )
            .logout(logout -> logout.permitAll()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        ;
        //use http Basic Authntication
        http.httpBasic(Customizer.withDefaults());

        //Disable SCRF
        http.csrf(csrf -> csrf.disable());
        System.out.println("SecurityFilterChain END");
        return http.build();
    }
     */

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
                        .requestMatchers("/home","/api/customer").hasRole("CUSTOMER")
                        .requestMatchers("/api/customer/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("MANAGER") //create employee
                        .requestMatchers(HttpMethod.PUT, "/api/users").hasRole("MANAGER") //update employee
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(
                                "/login", //user login form
                                "/customer/login", //customer login form
                                "/customer-login", //redirect page->versi autosubmit login via main-login (unused)
                                "/customer/processLoginForm",  //process login
                                "/customer/home",  //customer home
                                "/swagger-ui/**", "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticateTheUser")
                        .successHandler(customAuthenticationSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout.permitAll())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }


/*
@Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails cust = User.builder()
                .username("cust")
                .password("{noop}cust")
                .roles("CUSTOMER")
                .build();

        UserDetails me = User.builder()
                .username("saya")
                .password("{noop}saya")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(me);
    }
 */

}
