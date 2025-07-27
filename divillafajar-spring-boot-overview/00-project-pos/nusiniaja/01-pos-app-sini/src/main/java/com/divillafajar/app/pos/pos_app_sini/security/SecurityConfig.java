package com.divillafajar.app.pos.pos_app_sini.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    // Add support for JDBC
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("MANAGER") //create employee
                        .requestMatchers(HttpMethod.PUT, "/api/users").hasRole("MANAGER") //update employee
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers("welcome/**", "users/register/**").permitAll()
                        .anyRequest().authenticated()
            )
            .formLogin(form ->
                    form
                            .loginPage("/login")  //sesuai path
                            .loginProcessingUrl("/authenticateTheUser")
                            .permitAll()
            )
        ;
        //use http Basic Authntication
        http.httpBasic(Customizer.withDefaults());

        //Disable SCRF
        http.csrf(csrf -> csrf.disable());

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
