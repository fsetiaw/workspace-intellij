package com.code4life.springboot.villa.divillafajar.security;

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
        UserDetails me = User.builder()
                .username("me")
                .password("{noop}me")
                .roles("EMP").build();

        UserDetails me1 = User.builder()
                .username("me1")
                .password("{noop}me")
                .roles("MGR").build();

        UserDetails me2 = User.builder()
                .username("me2")
                .password("{noop}me")
                .roles("ADM").build();

        return new InMemoryUserDetailsManager(me,me1,me2);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET,"/api/emp").hasRole("EMP")
                        .requestMatchers(HttpMethod.GET,"/api/emp/**").hasRole("EMP")
                        .requestMatchers(HttpMethod.POST,"/api/emp").hasRole("MGR")
                        .requestMatchers(HttpMethod.PUT,"/api/emp").hasRole("MGR")
                        .requestMatchers(HttpMethod.DELETE,"/api/emp/**").hasRole("ADM")
            );

        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf-> csrf.disable());

        return http.build();
    }
}
