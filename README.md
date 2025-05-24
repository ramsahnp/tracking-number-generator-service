📦 Spring Boot And MongoDB Tracking Number Generator App

A scalable **Spring Boot** application that generates globally unique tracking numbers, even in clustered environments. Designed to support logistics operations by ensuring high-entropy, unique, and verifiable identifiers.

---
## 📄 Code Assessment

Download and review the [Scalable Tracking Number Generator API Test Paper](src/main/resources/Code Assessment_-Scalable Tracking Number Generator API_Test_Paper.pdf).

## 🔢 Tracking Number Format

- **Base36 Encoded (16 characters):**
    - Digits: `0-9` (values 0-9)
    - Letters: `A-Z` (values 10-35)
- **Maximum Value Supported:** ~7.959 × 10²⁴
    - Example: `INUS468624000480`

---

How to run locally:

1.install java17 or above and mongo db
2.download this project/clone from github:https://github.com/ramsahnp/tracking-number-generator-service
3.run mvn clean install
4.java -jar tracking-number-generator-0.0.1-SNAPSHOT.jar


## 🌍 Live Deployment

This application is deployed on **Google App Engine** and accessible globally.

### 🔗 Public URL:
http://34.45.155.115:8080/next-tracking-number?origin_country_id=IN&destination_country_id=US&weight=1.2344545&created_at=2025-05-22T14:30:00Z&customer_id=123e4567-e89b-12d3-a456-426614174000&customer_name=John%20Doe&customer_slug=john-doe

Response: {"trackingNumber": "INUS468624000480", "createdAt": "2025-05-22T14:30Z"}

``
URL-Encode Special Characters
When using Postman or accessing the API via browser query parameters, make sure to encode certain characters:

| Character | Description                     | URL-Encoded |
| --------- | ------------------------------- | ----------- |
| `+`       | Timezone offset                 | `%2B`       |
| `:`       | Time separator                  | `%3A`       |
| `-`       | OK unencoded, but `%2D` is safe |  `%2D`      |




