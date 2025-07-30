package com.divillafajar.app.pos.pos_app_sini.ws.utils;

public class MyStringUtils {
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
