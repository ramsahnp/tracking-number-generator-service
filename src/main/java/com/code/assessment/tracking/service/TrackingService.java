package com.code.assessment.tracking.service;

import com.code.assessment.tracking.dto.TrackingDocument;
import com.code.assessment.tracking.repository.TrackingRepository;
import com.code.assessment.tracking.util.TrackingNumberGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Service
public class TrackingService {
    private static final Logger logger= LoggerFactory.getLogger(TrackingService.class);
    private TrackingRepository repository;

    @Autowired
    public TrackingService(TrackingRepository repository) {
        this.repository = repository;
    }
    public Mono<TrackingDocument> generateTrackingNumber(String origin, String dest, double weight,
                                                         OffsetDateTime createdAt, String customerId,
                                                         String customerName, String customerSlug) {
        return generateUniqueNumber(origin, dest, customerSlug)
                .map(trackingNumber -> {
                    TrackingDocument doc = new TrackingDocument();
                    doc.setTrackingNumber(trackingNumber);
                    doc.setCreatedAt(createdAt.toString());
                    doc.setDestination(dest);
                    doc.setCustomerId(customerId);
                    doc.setOrigin(origin);
                    doc.setWeight(weight);
                    doc.setCustomerName(customerName);
                    doc.setCustomerSlug(customerSlug);
                    return doc;
                }).flatMap(repository::save);
    }

    private Mono<String> generateUniqueNumber(String origin, String dest, String slug) {
        String candidate = TrackingNumberGenerator.generateTrackingNumber(origin, dest, slug);
        logger.info("Tracking Number: {}, timestamp: {}", candidate, LocalDateTime.now());
        return repository.existsById(candidate)
                .flatMap(exists -> exists
                        ? generateUniqueNumber(origin, dest, slug)
                        : Mono.just(candidate));
    }


}
