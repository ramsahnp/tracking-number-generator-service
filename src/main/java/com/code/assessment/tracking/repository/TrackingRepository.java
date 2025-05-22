package com.code.assessment.tracking.repository;

import com.code.assessment.tracking.dto.TrackingDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TrackingRepository extends ReactiveMongoRepository<TrackingDocument, String> {
    Mono<Boolean> existsByTrackingNumber(String trackingNumber);
}
