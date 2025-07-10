package com.divillafajar.disiniaja.possini.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class MainSecurityConfig {

    // add support or jdbc
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        /*
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        //define quer to retrieve a user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select username,password,enabled from users where username=?"
        );
        //deginae quer to retrievr the authority
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select username,authority from authorities where username=?"
        );
        return jdbcUserDetailsManager;
        */
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/").hasRole("EMPLOYEE")
                                .requestMatchers("/leaders/**").hasRole("MANAGER")
                                .requestMatchers("/systems/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/disiniLoginPage")
                                .loginProcessingUrl("/autehnticateTheUser")
                                .permitAll()
                )
                .logout(logout -> logout.permitAll()
                )
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/access-denied")
                );;

        return httpSecurity.build();
    }

    /*
    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails me = User.builder()
                .username("me").password("{noop}me")
                .roles("EMPLOYEE").build();

        UserDetails you = User.builder()
                .username("you").password("{noop}you")
                .roles("EMPLOYEE","MANAGER").build();

        UserDetails we = User.builder()
                .username("we").password("{noop}we")
                .roles("EMPLOYEE","MANAGER","ADMIN").build();

        return new InMemoryUserDetailsManager(me,you,we);
    }

     */
}
