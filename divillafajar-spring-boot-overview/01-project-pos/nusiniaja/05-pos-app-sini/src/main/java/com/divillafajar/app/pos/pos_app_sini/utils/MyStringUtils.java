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

    /**
     * 🔧 Memotong nama file agar panjang total URL tidak melebihi batas (misal 255)
     */
    public String adjustFileNameLength(String baseUrlPath, String fileName, int maxTotalLength) {
        int totalLength = (baseUrlPath + fileName).length();

        if (totalLength <= maxTotalLength) {
            return fileName;
        }

        // 🔹 Hitung sisa karakter untuk nama file
        int allowedLength = maxTotalLength - baseUrlPath.length();
        if (allowedLength <= 0) return fileName.substring(0, Math.min(fileName.length(), 10)); // fallback aman

        // 🔹 Pisahkan nama dan ekstensi
        int dotIndex = fileName.lastIndexOf('.');
        String namePart = (dotIndex > 0) ? fileName.substring(0, dotIndex) : fileName;
        String extPart = (dotIndex > 0) ? fileName.substring(dotIndex) : "";

        // 🔹 Hitung sisa panjang nama yang bisa dipakai
        int allowedNameLength = allowedLength - extPart.length();
        if (allowedNameLength < 0) allowedNameLength = 0;

        // 🔹 Potong nama jika perlu
        if (namePart.length() > allowedNameLength) {
            namePart = namePart.substring(0, allowedNameLength);
        }

        String adjusted = namePart + extPart;
        System.out.println("✂️ Nama file dipotong: " + fileName + " ➜ " + adjusted);
        return adjusted;
    }
}
