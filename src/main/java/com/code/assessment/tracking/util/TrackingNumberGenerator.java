package com.code.assessment.tracking.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TrackingNumberGenerator {
    private static final AtomicLong counter = new AtomicLong();
    public static String generateTrackingNumber() {
        String saltInput = UUID.randomUUID().toString()
                + System.nanoTime()
                + Thread.currentThread().getId()
                + counter.getAndIncrement();

        String trackingNumber = hashAndTrim(saltInput, 16).toUpperCase();

        return trackingNumber.length() > 16
                ? trackingNumber.substring(0, 16)
                : String.format("%-16s", trackingNumber).replace(' ', 'X');
    }
    private static String hashAndTrim(String input, int length) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            String base36 = new java.math.BigInteger(1, hashBytes).toString(36).toUpperCase();
            return base36.length() > length ? base36.substring(0, length) : base36;
        } catch (Exception e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }
}
