package io.nobt.core.domain;

import java.security.SecureRandom;

public final class ShortURL {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generate() {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            builder.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return builder.toString();
    }
}
