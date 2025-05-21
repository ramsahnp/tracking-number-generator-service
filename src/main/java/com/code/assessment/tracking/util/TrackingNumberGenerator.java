package com.code.assessment.tracking.util;

import java.util.concurrent.ThreadLocalRandom;

public class TrackingNumberGenerator {
    public static String generate(String origin, String destination, String customerSlug) {
        String prefix = origin + destination;
        int hash = Math.abs(customerSlug.hashCode());
        int random = ThreadLocalRandom.current().nextInt(10000, 99999);

        String candidate = (prefix + hash + random).toUpperCase().replaceAll("[^A-Z0-9]", "");
        return candidate.length() > 16 ? candidate.substring(0, 16) : candidate;
    }
}
