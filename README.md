# Ticket Reservation System

Learning Java concurrency, Spring Boot, and PostgreSQL through a ticket booking API.

## Learning Goals

- Database concurrency: pessimistic/optimistic locking, deadlock prevention
- Transaction management and isolation levels
- PostgreSQL partial indexes and constraints
- Spring async processing and scheduled tasks

## Stack

Java 25 • Spring Boot 4.0 • PostgreSQL • Docker • JPA • Maven

## Structure

```
src/main/java/com/example/practicepostgres/
├── model/          # Event, Seat, Reservation, Venue
├── repository/     # Spring Data JPA with locking queries
├── service/        # Transaction management and business logic
├── controller/     # REST endpoints
└── DataLoader      # Sample data
```

## Run

```bash
./mvnw spring-boot:run
```
