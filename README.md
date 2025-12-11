# Customer Account Tracker

Spring Boot application implementing a simple **Customer Account Tracker** for a bank.

It supports:

- Creating an account with customer details
- Editing customer details
- Fetching customers and accounts
- Transferring funds between accounts
- Checking account balance

## Tech Stack

- Java 11
- Spring Boot 2.7.x
- Spring Web
- Spring Data JPA (Hibernate)
- H2 in-memory database
- JUnit 5, Spring Boot Test, Mockito
- Lombok

## Building & Running

```bash
# From the project root
mvn clean package

# Run the app
mvn spring-boot:run
