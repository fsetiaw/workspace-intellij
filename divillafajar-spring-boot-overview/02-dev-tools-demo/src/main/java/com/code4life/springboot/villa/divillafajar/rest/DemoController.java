package com.code4life.springboot.villa.divillafajar.rest;

import com.code4life.springboot.villa.divillafajar.common.Coach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    private Coach myCoach;

    /*


    @Autowired
    public DemoController(Coach theCoach) {
        myCoach=theCoach;
    }
    */

    @Autowired
    public void setCoach(Coach thisCoach) {
        myCoach=thisCoach;
    }

    @GetMapping("/dailyWorkout")
    public String getDailyWorkout() {
        return myCoach.getDailyWorkout();
    }
}
