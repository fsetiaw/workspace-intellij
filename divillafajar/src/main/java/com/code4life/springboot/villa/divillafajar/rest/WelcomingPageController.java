package com.code4life.springboot.villa.divillafajar.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomingPageController {

    @GetMapping("/")
    public String welcome() {
        return "Hello Me" ;
    }





}
