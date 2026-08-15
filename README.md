# Dairy ERP and Settlement System

A full-stack Dairy ERP and Settlement System designed to manage farmer records, milk collection, ledger accounting, payments, feed sales, cash advances, settlements, and operational reporting.

The backend is implemented using Spring Boot and PostgreSQL, with JWT-based authentication, role-based authorization, Flyway migrations, Swagger/OpenAPI documentation, and automated tests.

## Features

- Farmer registration and management
- Milk collection with FAT and SNF-based pricing
- Configurable milk rate cards
- Automatic farmer ledger accounting
- Cash advance tracking
- Feed sale tracking
- Farmer payment tracking
- Settlement generation with credit/debit aggregation
- Farmer statement generation
- Dashboard summary APIs
- JWT authentication
- ADMIN and OPERATOR role-based authorization
- Bean validation and centralized exception handling
- Flyway database migrations
- Swagger/OpenAPI documentation
- Automated service and security tests

## Core Accounting Flow

Milk collection creates a CREDIT entry in the farmer ledger.

Cash advances, feed sales, and payments create DEBIT entries.

Settlement calculation is based on:

```text
Net Settlement = Total Credits - Total Debits