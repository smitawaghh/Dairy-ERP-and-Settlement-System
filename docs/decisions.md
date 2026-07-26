# Architecture Decisions

## Database

PostgreSQL was selected because it provides ACID transactions,
strong relational modeling, and excellent Spring Boot support.

## Database Versioning

Flyway is used instead of Hibernate schema generation to ensure
repeatable, version-controlled database migrations.

## Project Structure

The application follows a layered architecture:

Controller
↓

Service
↓

Repository
↓

Database