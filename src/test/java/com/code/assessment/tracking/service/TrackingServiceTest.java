package com.code.assessment.tracking.service;

import com.code.assessment.tracking.dto.TrackingDocument;
import com.code.assessment.tracking.repository.TrackingRepository;
import com.code.assessment.tracking.util.TrackingNumberGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.OffsetDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class TrackingServiceTest {
    @Mock
    private TrackingRepository repository;

    @InjectMocks
    private TrackingService service;

    @Test
    void generateTrackingNumber_shouldReturnSavedDocument() {
        String generatedNumber = "TEST123456789012";

        try (MockedStatic<TrackingNumberGenerator> mockedStatic = mockStatic(TrackingNumberGenerator.class)) {
            mockedStatic.when(TrackingNumberGenerator::generateTrackingNumber).thenReturn(generatedNumber);

            when(repository.existsById(generatedNumber)).thenReturn(Mono.just(false));

            TrackingDocument savedDoc = new TrackingDocument();
            savedDoc.setTrackingNumber(generatedNumber);
            savedDoc.setOrigin("A");
            savedDoc.setDestination("B");
            savedDoc.setWeight(1.0);
            savedDoc.setCustomerId("cid");
            savedDoc.setCustomerName("cname");
            savedDoc.setCustomerSlug("cslug");
            savedDoc.setCreatedAt("2025-05-24T12:00:00Z");

            when(repository.save(any(TrackingDocument.class))).thenReturn(Mono.just(savedDoc)); // ✅ FIXED

            StepVerifier.create(service.generateTrackingNumber(
                            "A", "B", 1.0, OffsetDateTime.now(), "cid", "cname", "cslug"))
                    .expectNextMatches(doc -> doc.getTrackingNumber().equals(generatedNumber))
                    .verifyComplete();
        }
    }

    @Test
    void generateUniqueNumber_shouldRetryIfTrackingNumberExists() {
        String first = "DUPLICATE123";
        String second = "UNIQUE456";

        try (MockedStatic<TrackingNumberGenerator> mockedStatic = mockStatic(TrackingNumberGenerator.class)) {
            mockedStatic.when(TrackingNumberGenerator::generateTrackingNumber)
                    .thenReturn(first)
                    .thenReturn(second);

            when(repository.existsById(first)).thenReturn(Mono.just(true));
            when(repository.existsById(second)).thenReturn(Mono.just(false));

            TrackingDocument savedDoc = new TrackingDocument();
            savedDoc.setTrackingNumber(second);
            savedDoc.setOrigin("A");
            savedDoc.setDestination("B");
            savedDoc.setWeight(1.0);
            savedDoc.setCustomerId("cid");
            savedDoc.setCustomerName("cname");
            savedDoc.setCustomerSlug("cslug");
            savedDoc.setCreatedAt("2025-05-24T12:00:00Z"); // ✅ Fixed raw value

            when(repository.save(any(TrackingDocument.class))).thenReturn(Mono.just(savedDoc));

            StepVerifier.create(service.generateTrackingNumber(
                            "A", "B", 1.0, OffsetDateTime.now(), "cid", "cname", "cslug"))
                    .expectNextMatches(doc -> doc.getTrackingNumber().equals(second))
                    .verifyComplete();
        }
    }
}
