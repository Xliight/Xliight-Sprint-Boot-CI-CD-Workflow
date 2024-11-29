package com.ouitrips.app.utils;

import java.security.SecureRandom;

public class RandomStringUtils {
    private static final SecureRandom SecureRandom = new SecureRandom();
    private static final SecureRandom random = new SecureRandom();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private RandomStringUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
    public static String generateRandomSixDigits() {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    public static String generateRandomStringSecurity() {
        StringBuilder sb = new StringBuilder(100);
        for (int i = 0; i < 100; i++) {
            int randomIndex = SecureRandom.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
    public static Integer generateRandomNumber(Integer max) {

        int randomNumber = random.nextInt(max);
        return randomNumber;
    }

}
