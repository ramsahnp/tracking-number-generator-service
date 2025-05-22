package com.code.assessment.tracking.service;

import com.code.assessment.tracking.dto.TrackingDocument;
import com.code.assessment.tracking.repository.TrackingRepository;
import com.code.assessment.tracking.util.TrackingNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrackingServiceTest {
    private TrackingRepository repository;
    private TrackingService service;

    @BeforeEach
    void setUp() {
        repository = mock(TrackingRepository.class);
        service = new TrackingService(repository);
    }

    @Test
    void testGenerateTrackingNumber_successfulFlow() {
        String origin = "IN";
        String dest = "US";
        double weight = 1.234;
        OffsetDateTime createdAt = OffsetDateTime.now();
        String customerId = UUID.randomUUID().toString();
        String customerName = "John Doe";
        String customerSlug = "john-doe";

        String generatedTracking = "INUSJO123456789012";
        when(repository.existsById(generatedTracking)).thenReturn(Mono.just(false));
        when(repository.save(any(TrackingDocument.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        mockStaticGenerator(generatedTracking);

        Mono<TrackingDocument> result = service.generateTrackingNumber(
                origin, dest, weight, createdAt, customerId, customerName, customerSlug
        );

        StepVerifier.create(result)
                .assertNext(doc -> {
                    assertEquals(generatedTracking, doc.getTrackingNumber());
                    assertEquals(origin, doc.getOrigin());
                    assertEquals(dest, doc.getDestination());
                    assertEquals(weight, doc.getWeight());
                    assertEquals(customerId, doc.getCustomerId());
                    assertEquals(customerName, doc.getCustomerName());
                    assertEquals(customerSlug, doc.getCustomerSlug());
                })
                .verifyComplete();

        verify(repository).existsById(generatedTracking);
        verify(repository).save(any(TrackingDocument.class));
    }

    private void mockStaticGenerator(String... trackingNumbers) {
        final int[] index = {0};
        mockStatic(TrackingNumberGenerator.class).when(() ->
                TrackingNumberGenerator.generateTrackingNumber(anyString(), anyString(), anyString())
        ).thenAnswer(invocation -> trackingNumbers[index[0]++]);
    }
}
