This project implements a simplified e-commerce backend using Spring Boot microservices, designed as part of the Körber Java Microservices Assignment.

The system consists of two independent microservices:
	•	Inventory Service – Manages product inventory with batch-level expiry handling
	•	Order Service – Places orders and reserves inventory in real time

Both services communicate via REST APIs and use H2 in-memory database with Liquibase (for inventory service) for schema and data initialization.
Key Design Decisions
	•	Microservice architecture
	•	REST-based inter-service communication
	•	Factory Design Pattern in Inventory allocation logic
	•	Liquibase for DB schema & CSV data loading
	•	H2 in-memory database for simplicity
	•	DTO-based API responses (no entity leakage)
Get Inventory - 
http://localhost:8084/inventory/1005
response : 
{
    "productId": 1005,
    "productName": "Smartwatch",
    "batches": [
        {
            "batchId": 5,
            "quantity": 0,
            "expiryDate": "2026-03-31"
        },
        {
            "batchId": 7,
            "quantity": 0,
            "expiryDate": "2026-04-24"
        },
        {
            "batchId": 2,
            "quantity": 11,
            "expiryDate": "2026-05-30"
        }
    ]
}
Reserve Inventory - 
POST /inventory/reserve
{
  "productId": 1005,
  "quantity": 60
}
response : 
{
    "orderId": 1,
    "productId": 1005,
    "productName": "Smartwatch",
    "quantity": 60,
    "status": "PLACED",
    "orderDate": "2025-12-15",
    "reservedBatchIds": [
        5,
        7,
        2
    ]
}
tech Stack
	•	Java 17
	•	Spring Boot
	•	Spring Data JPA
	•	RestTemplate
	•	Liquibase
	•	H2 Database
	•	Lombok
	•	Maven
