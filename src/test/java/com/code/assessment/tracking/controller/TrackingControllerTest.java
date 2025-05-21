package com.code.assessment.tracking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TrackingControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void shouldReturnTrackingNumber() {
        webTestClient.get().uri(uriBuilder ->
                        uriBuilder.path("/next-tracking-number")
                                .queryParam("origin_country_id", "US")
                                .queryParam("destination_country_id", "IN")
                                .queryParam("weight", "1.234")
                                .queryParam("created_at", "2025-05-21T10:15:30+00:00")
                                .queryParam("customer_id", "de619854-b59b-425e-9db4-943979e1bd49")
                                .queryParam("customer_name", "RedBox Logistics")
                                .queryParam("customer_slug", "redbox-logistics")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.trackingNumber").exists()
                .jsonPath("$.createdAt").exists();
    }
}
