package com.midterm.findrentals;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SimplifiedSHA256HexDigest {
    public static final String hexadecimalDigest(String message) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(message.getBytes(StandardCharsets.UTF_8));
        return digest.digest().toString();
    }
}
