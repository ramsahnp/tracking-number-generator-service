package com.code.assessment.tracking.controller;

import com.code.assessment.tracking.model.TrackingDocument;
import com.code.assessment.tracking.service.TrackingService;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.time.OffsetDateTime;

@RestController
@RequestMapping("/next-tracking-number")
public class TrackingController {
    @Autowired
    private TrackingService trackingService;

    @GetMapping
    public Mono<TrackingDocument> getTrackingNumber(
            @RequestParam @Pattern(regexp = "^[A-Z]{2}$") String origin_country_id,
            @RequestParam @Pattern(regexp = "^[A-Z]{2}$") String destination_country_id,
            @RequestParam @DecimalMin("0.001") @DecimalMax("9999.999") double weight,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime created_at,
            @RequestParam @NotBlank String customer_id,
            @RequestParam @NotBlank String customer_name,
            @RequestParam @NotBlank String customer_slug
    ) {
        return trackingService.generateTrackingNumber(
                origin_country_id.toUpperCase(),
                destination_country_id.toUpperCase(),
                weight,
                created_at,
                customer_id,
                customer_name,
                customer_slug
        );
    }
}
