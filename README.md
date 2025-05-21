# Scalable Tracking Number Generator API

## Overview

A Spring Boot RESTful API that generates unique and regex-compliant tracking numbers for parcel shipments.

## Endpoint

**GET** `/next-tracking-number`

### Query Parameters:

- `origin_country_id`
- `destination_country_id`
- `weight`
- `created_at`
- `customer_id`
- `customer_name`
- `customer_slug`

### Response

```json
{
  "tracking_number": "USCA123456789ABC",
  "created_at": "2025-05-21T10:17:30Z"
}

```
URL-Encode Special Characters
When using Postman or accessing the API via browser query parameters, make sure to encode certain characters:

| Character | Description                     | URL-Encoded |
| --------- | ------------------------------- | ----------- |
| `+`       | Timezone offset                 | `%2B`       |
| `:`       | Time separator                  | `%3A`       |
| `-`       | OK unencoded, but `%2D` is safe |  `%2D`      |

E.G:
GET http://localhost:8080/next-tracking-number?
origin_country_id=US
&
destination_country_id=IN
&
weight=1.235
&
created_at=2022-12-01T14%3A45%3A00%2B05%3A30
&
customer_id=de619854-b59b-425e-9db4-943979e1bd49
&
customer_name=RedBox%20Logistics
&
customer_slug=redbox-logistics






