package com.code.assessment.tracking.util;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TrackingNumberGeneratorTest {
    @Test
    void generateTrackingNumber_shouldReturn16CharUppercase() {
        String trackingNumber = TrackingNumberGenerator.generateTrackingNumber();

        assertNotNull(trackingNumber, "Tracking number should not be null");
        assertEquals(16, trackingNumber.length(), "Tracking number must be 16 characters long");
        assertTrue(trackingNumber.matches("^[A-Z0-9]+$"), "Tracking number must be uppercase alphanumeric");
    }

    @RepeatedTest(100)
    void generateTrackingNumber_shouldGenerateUniqueValues() {
        Set<String> generated = new HashSet<>();

        for (int i = 0; i < 10000; i++) {
            String trackingNumber = TrackingNumberGenerator.generateTrackingNumber();
            assertFalse(generated.contains(trackingNumber), "Duplicate tracking number generated: " + trackingNumber);
            generated.add(trackingNumber);
        }
    }

    @Test
    void generateTrackingNumber_shouldNotContainLowerCaseOrSpecialChars() {
        String tn = TrackingNumberGenerator.generateTrackingNumber();
        assertTrue(tn.matches("^[A-Z0-9]{16}$"), "Tracking number must be strictly uppercase alphanumeric, 16 characters");
    }
}
