package com.code.assessment.tracking.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "tracking_numbers")
@NoArgsConstructor
@AllArgsConstructor
public class TrackingDocument {
    @Id
    private String trackingNumber;
    private String createdAt;

    @JsonIgnore
    private String origin;
    @JsonIgnore
    private String destination;
    @JsonIgnore
    private double weight;
    @JsonIgnore
    private String customerId;
    @JsonIgnore
    private String customerName;
    @JsonIgnore
    private String customerSlug;
}
