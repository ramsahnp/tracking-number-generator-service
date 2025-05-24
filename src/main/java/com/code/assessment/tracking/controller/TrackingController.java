package com.code.assessment.tracking.controller;

import com.code.assessment.tracking.dto.TrackingDocument;
import com.code.assessment.tracking.service.TrackingService;
import com.code.assessment.tracking.util.TrackingNumberGenerator;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.time.OffsetDateTime;
import java.util.Map;

@RestController
@RequestMapping("/next-tracking-number")
@Validated
public class TrackingController {

    private TrackingService trackingService;

    @Autowired
    public TrackingController(TrackingService trackingService){
        this.trackingService = trackingService;
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> generateUniqueNumber(
            @RequestParam @Pattern(regexp = "^[A-Z]{2}$") String origin_country_id,
            @RequestParam @Pattern(regexp = "^[A-Z]{2}$") String destination_country_id,
            @RequestParam @DecimalMin("0.001") @DecimalMax("9999.999") double weight,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime created_at,
            @RequestParam @NotBlank String customer_id,
            @RequestParam @NotBlank String customer_name,
            @RequestParam @NotBlank String customer_slug
    ) {
        String trackingNumber = TrackingNumberGenerator.generateTrackingNumber();
        Map<String, String> response = Map.of(
                "tracking_number", trackingNumber,
                "created_at", created_at.toString()
        );
        return ResponseEntity.ok(response);
    }

    // This method is writer just to showcase database interaction
    @GetMapping("/save")
    public Mono<TrackingDocument> getTrackingNumberAndSaveToDatabase(
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
