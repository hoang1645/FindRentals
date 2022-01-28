package com.midterm.findrentals;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SimplifiedSHA256HexDigest {
    public static final String hexadecimalDigest(String message) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        digest.update(messageBytes);

        byte[] byteDigest = digest.digest();
        StringBuilder hexDigest = new StringBuilder();
        for (int i = 0; i < byteDigest.length; i++)
            hexDigest.append(String.format("%02x", byteDigest[i]));
        return hexDigest.toString();
    }
}
