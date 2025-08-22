package com.thuongmaidientu.until;

import java.security.MessageDigest;

public class HashUtil {
    public static String sha256(String token) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(token.getBytes("UTF-8"));
        StringBuilder hex = new StringBuilder();
        for (byte b : hash) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}
