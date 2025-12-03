# **Product API — Mechhive Assignment**

This project implements a high-performance product and transaction API with strict <100ms response times, full currency conversion, fee enrichment, Redis-backed caching, and clean modular architecture. It follows engineering principles (SOLID, Clean Architecture, DRY, SoC) and is designed for real-world maintainability and extensibility.

## 🔑 Key Decisions Recap
**Product caching uses key-per-item approach** -
Because it scales better for large catalogs and enables fast random access.

**Currency caching uses Redis Hashes** -
Because we want constant-time lookup for a single rate without reading the entire rate map.

**Enrichment pipeline** -
Because it cleanly supports multiple enrichment steps and future features.

**Strict input validation**- 
Because it prevents domain pollution and makes the API more predictable.

**Custom global exception handler** -
Because it ensures consistent errors and good UX without over-exposing internals.

## 🏗️ Running the Project
1. Clone the repo
2. Start via Docker
   `docker compose up --build`
3. Or use the Makefile


API is now available at:
http://localhost:8080

OpenAPI:
http://localhost:8080/swagger

## 🧪 Running Tests

**Unit Tests**

Run `./mvnw test` from your local directory

## Example request body
### Request: POST `/transactions`

Request body:
````
{
  "productIds": [
    1, 4, 5
  ],
  "currency": "USD"
}
````

### Request: GET `/products?currency=eur`