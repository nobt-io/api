package io.nobt.logging;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    public static String createMD5Hash(String input) {
        MessageDigest md = getMD5Instance();
        md.update(input.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuilder sb = new StringBuilder();
        for (byte singleByte : byteData) {
            sb.append(Integer.toString((singleByte & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    private static MessageDigest getMD5Instance() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

}
