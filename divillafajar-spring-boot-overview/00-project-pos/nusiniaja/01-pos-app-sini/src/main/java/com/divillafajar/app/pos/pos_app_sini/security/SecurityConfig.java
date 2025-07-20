package com.divillafajar.app.pos.pos_app_sini.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
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


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.PUT, "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/**").hasRole("ADMIN")
            );
        //use http Basic Authntication
        http.httpBasic(Customizer.withDefaults());

        //Disable SCRF
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }


}
