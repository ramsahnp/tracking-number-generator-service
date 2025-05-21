package com.code.assessment.tracking.service;

import com.code.assessment.tracking.model.TrackingDocument;
import com.code.assessment.tracking.repository.TrackingRepository;
import com.code.assessment.tracking.util.TrackingNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@Service
public class TrackingService {
    private TrackingRepository repository;

    @Autowired
    public TrackingService(TrackingRepository repository) {
        this.repository = repository;
    }
    public Mono<TrackingDocument> generateTrackingNumber(String origin, String dest, double weight,
                                                         OffsetDateTime createdAt, String customerId,
                                                         String customerName, String customerSlug) {
        System.out.println("createdAt=====>>"+createdAt);
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
        String candidate = TrackingNumberGenerator.generate(origin, dest, slug);
        return repository.existsById(candidate)
                .flatMap(exists -> exists
                        ? generateUniqueNumber(origin, dest, slug)
                        : Mono.just(candidate));
    }


}
