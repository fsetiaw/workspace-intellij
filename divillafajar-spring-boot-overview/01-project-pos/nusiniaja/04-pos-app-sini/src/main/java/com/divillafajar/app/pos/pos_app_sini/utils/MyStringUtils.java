package com.divillafajar.app.pos.pos_app_sini.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class MyStringUtils {

    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final int ITERATION = 10000;
    private final int KEY_LENGTH = 256;

    String generateRandomString(int length) {
        StringBuilder returnVal = new StringBuilder(length);
        for(int i=0;i<length;i++) {
            returnVal.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return new String(returnVal);
    }

    public static String[] splitLastWord(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new String[]{"", ""};
        }

        String[] parts = input.trim().split("\\s+");
        String last = parts[parts.length - 1];

        StringBuilder rest = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            rest.append(parts[i]).append(" ");
        }
        if(!rest.toString().isBlank()) {
            return new String[]{rest.toString().trim(), last};
        }
        else {
            return new String[]{last, ""};
        }

    }

    public static String cleanPhoneNumber(String input) {
        if (input == null) {
            return "";
        }

        // Hapus semua karakter yang bukan digit (0-9)
        return input.replaceAll("[^\\d]", "");
    }
}
