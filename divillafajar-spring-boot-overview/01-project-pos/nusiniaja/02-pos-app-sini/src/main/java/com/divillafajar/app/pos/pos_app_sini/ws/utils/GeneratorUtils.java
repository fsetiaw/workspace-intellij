package com.divillafajar.app.pos.pos_app_sini.ws.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;
@Component
public class GeneratorUtils {
    private final MyStringUtils myStringUtils;
    public GeneratorUtils(MyStringUtils myStringUtils) {
        this.myStringUtils=myStringUtils;
    }

    public String generatePubUserId(int length) {
        return myStringUtils.generateRandomString(length);
    }
}

