package com.code4life.springboot.villa.divillafajar.common;

import org.springframework.stereotype.Component;

@Component
public class TennisCoach implements Coach {

    @Override
    public String getDailyWorkout() {
        return "Main bentengan kek gituh";
    }
}
