package com.code.assessment.tracking.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrackingNumberGeneratorTest {
    @Test
    void testGenerateTrackingNumber_BasicFormat() {
        String origin = "IN";
        String destination = "US";
        String customerSlug = "flipkart";

        String trackingNumber = TrackingNumberGenerator.generateTrackingNumber(origin, destination, customerSlug);

        assertNotNull(trackingNumber, "Tracking number should not be null");
        assertTrue(trackingNumber.matches("^[A-Z0-9]{1,16}$"), "Tracking number should match pattern ^[A-Z0-9]{1,16}$");
        assertEquals(16, trackingNumber.length(), "Tracking number should be exactly 16 characters");
    }

    @Test
    void testGenerateTrackingNumber_SlugWithSpecialCharacters() {
        String trackingNumber = TrackingNumberGenerator.generateTrackingNumber("IN", "US", "flip-kart_123");

        assertNotNull(trackingNumber);
        assertTrue(trackingNumber.matches("^[A-Z0-9]{1,16}$"));
    }

    @Test
    void testGenerateTrackingNumber_ShortPrefixPadding() {
        String trackingNumber = TrackingNumberGenerator.generateTrackingNumber("I", "N", "z");

        assertNotNull(trackingNumber);
        assertTrue(trackingNumber.matches("^[A-Z0-9]{1,16}$"));

        // First 4 characters should be prefix padded
        String prefix = trackingNumber.substring(0, 4);
        assertEquals(4, prefix.length());
        assertTrue(prefix.matches("^[A-Z0-9]{4}$"));
    }

    @Test
    void testGenerateTrackingNumber_UniqueOutput() {
        String tn1 = TrackingNumberGenerator.generateTrackingNumber("IN", "US", "flipkart");
        String tn2 = TrackingNumberGenerator.generateTrackingNumber("IN", "US", "flipkart");

        assertNotEquals(tn1, tn2, "Two sequential tracking numbers should be different");
    }

    @Test
    void testGenerateTrackingNumber_UppercaseEnforcement() {
        String trackingNumber = TrackingNumberGenerator.generateTrackingNumber("in", "us", "flipkart");

        assertEquals(trackingNumber.toUpperCase(), trackingNumber, "Tracking number should be all uppercase");
    }
}
