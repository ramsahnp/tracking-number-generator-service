package com.code.assessment.tracking.controller;

import com.code.assessment.tracking.dto.TrackingDocument;
import com.code.assessment.tracking.service.TrackingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;

@WebFluxTest(TrackingController.class)
class TrackingControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TrackingService trackingService;

    private TrackingDocument mockDoc;

    @BeforeEach
    void setUp() {
        mockDoc = new TrackingDocument();
        mockDoc.setTrackingNumber("INUSCUS123456");
        mockDoc.setCreatedAt("2025-05-22T14:30:10Z");
        mockDoc.setOrigin("IN");
        mockDoc.setDestination("US");
        mockDoc.setWeight(1.5);
        mockDoc.setCustomerId(UUID.randomUUID().toString());
        mockDoc.setCustomerName("John Doe");
        mockDoc.setCustomerSlug("john-doe");
    }

    @ParameterizedTest
    @ValueSource(strings = {"2025-05-22T14:30:10Z", "2024-11-15T08:15:10-05:10"})
    void testGenerateUniqueNumber_Success(String createdAt) {
        mockDoc.setCreatedAt(OffsetDateTime.parse(createdAt).toString());
        Mockito.when(trackingService.generateTrackingNumber(any(), any(), anyDouble(), any(), any(), any(), any()))
                .thenReturn(Mono.just(mockDoc));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/next-tracking-number")
                        .queryParam("origin_country_id", "IN")
                        .queryParam("destination_country_id", "US")
                        .queryParam("weight", "1.5")
                        .queryParam("created_at", mockDoc.getCreatedAt())
                        .queryParam("customer_id", mockDoc.getCustomerId())
                        .queryParam("customer_name", "John Doe")
                        .queryParam("customer_slug", "john-doe")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map.class)
                .consumeWith(response -> {
                    Map<String, String> body = response.getResponseBody();
                    assertThat(body).isNotNull();
                    assertThat(body.get("tracking_number")).matches("^[A-Z0-9]{1,16}$");
                    assertThat(body.get("created_at")).isEqualTo(mockDoc.getCreatedAt());
                });
    }

    @Test
    void testGenerateUniqueNumber_exception() {
        Mockito.when(trackingService.generateTrackingNumber(any(), any(), anyDouble(), any(), any(), any(), any()))
                .thenReturn(Mono.just(mockDoc));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/next-tracking-number")
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
                .expectBody();
    }

    @Test
    void testGetTrackingNumber_exception() {
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
                .expectBody();
    }

    @Test
    void testGetTrackingNumber_success() {
        Mockito.when(trackingService.generateTrackingNumber(any(), any(), anyDouble(), any(), any(), any(), any()))
                .thenReturn(Mono.just(mockDoc));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/next-tracking-number/save")
                        .queryParam("origin_country_id", "IN")
                        .queryParam("destination_country_id", "US")
                        .queryParam("weight", "1.5")
                        .queryParam("created_at", mockDoc.getCreatedAt())
                        .queryParam("customer_id", mockDoc.getCustomerId())
                        .queryParam("customer_name", "John Doe")
                        .queryParam("customer_slug", "john-doe")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody();
    }

}
