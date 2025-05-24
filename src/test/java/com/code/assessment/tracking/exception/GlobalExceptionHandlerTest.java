package com.code.assessment.tracking.exception;

import com.code.assessment.tracking.controller.TrackingController;
import com.code.assessment.tracking.dto.TrackingDocument;
import com.code.assessment.tracking.service.TrackingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;

@WebFluxTest(controllers = TrackingController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private TrackingService trackingService;
    private TrackingDocument mockDoc;

    @BeforeEach
    void setUp() {
        mockDoc = new TrackingDocument();
        mockDoc.setTrackingNumber("INUSCUS123456");
        mockDoc.setCreatedAt("2025-05-22T14:30:00Z");
        mockDoc.setOrigin("IN");
        mockDoc.setDestination("US");
        mockDoc.setWeight(1.5);
        mockDoc.setCustomerId(UUID.randomUUID().toString());
        mockDoc.setCustomerName("John Doe");
        mockDoc.setCustomerSlug("john-doe");
    }

    @Test
    void shouldReturnBadRequest_save_ForConstraintViolation() {
        Mockito.when(trackingService.generateTrackingNumber(any(), any(), anyDouble(), any(), any(), any(), any()))
                .thenReturn(Mono.just(mockDoc));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/next-tracking-number/save")
                        .queryParam("origin_country_id", "INN")
                        .queryParam("destination_country_id", "US")
                        .queryParam("weight", "1.5")
                        .queryParam("created_at", mockDoc.getCreatedAt())
                        .queryParam("customer_id", mockDoc.getCustomerId())
                        .queryParam("customer_name", "John Doe")
                        .queryParam("customer_slug", "john-doe")
                        .build())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$['getTrackingNumberAndSaveToDatabase.origin_country_id']")
                .isEqualTo("must match \"^[A-Z]{2}$\"")
                .consumeWith(response -> {
                    System.out.println("Response body: " + new String(response.getResponseBody()));
                });
    }

    @Test
    void shouldReturnBadRequest_ForConstraintViolation() {
        Mockito.when(trackingService.generateTrackingNumber(any(), any(), anyDouble(), any(), any(), any(), any()))
                .thenReturn(Mono.just(mockDoc));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/next-tracking-number")
                        .queryParam("origin_country_id", "INN") // Invalid (3 letters)
                        .queryParam("destination_country_id", "US")
                        .queryParam("weight", "1.5")
                        .queryParam("created_at", mockDoc.getCreatedAt())
                        .queryParam("customer_id", mockDoc.getCustomerId())
                        .queryParam("customer_name", "John Doe")
                        .queryParam("customer_slug", "john-doe")
                        .build())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.['generateUniqueNumber.origin_country_id']")
                .isEqualTo("must match \"^[A-Z]{2}$\"");
    }
}
