package com.code.assessment.tracking.util;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TrackingNumberGenerator {
    private static final AtomicLong counter = new AtomicLong();
    public static String generateTrackingNumber(String origin, String destination, String customerSlug) {
        String prefix = (origin + destination + customerSlug).toUpperCase().replaceAll("[^A-Z0-9]", "");
        prefix = prefix.length() > 4 ? prefix.substring(0, 4) : String.format("%-4s", prefix).replace(' ', 'X');
        long timestamp = System.currentTimeMillis() % 1_000_000; // 6 digits max
        long count = counter.getAndIncrement() % 1_000; // 3 digits max
        int customerHash = Math.abs(customerSlug.hashCode()) % 1000;// Customer hash trimmed to 3 digits
        String uuidPart = Long.toString(Math.abs(UUID.randomUUID().getMostSignificantBits()), 36).toUpperCase();
        uuidPart = uuidPart.length() > 5 ? uuidPart.substring(0, 5) : String.format("%-5s", uuidPart).replace(' ', 'X');

        // Final tracking number
        String trackingNumber = (prefix +
                String.format("%06d", timestamp) +
                String.format("%03d", count) +
                String.format("%03d", customerHash) +
                uuidPart).toUpperCase();

        // Ensure final string is max 16 chars and matches ^[A-Z0-9]{1,16}$
        trackingNumber = trackingNumber.replaceAll("[^A-Z0-9]", "");
        return trackingNumber.length() > 16 ? trackingNumber.substring(0, 16) : trackingNumber;
    }
}
