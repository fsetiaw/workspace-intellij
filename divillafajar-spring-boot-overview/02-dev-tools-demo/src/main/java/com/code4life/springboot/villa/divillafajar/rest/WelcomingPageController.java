package com.code4life.springboot.villa.divillafajar.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomingPageController {

    //inject custom propertiess
    @Value("${coach.name}")
    private String coachName;

    @Value("${team.name}")
    private String teamName;

    @GetMapping("/")
    public String welcome() {
        return "Hello Me Again" ;
    }

    @GetMapping("/team/info")
    public String getInfo() {
        return "Nama Pelatih = "+coachName+", Nama Team = "+teamName ;
    }



}
