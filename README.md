# TP8 Spring RestController – Banque Comptes API

A simple Spring Boot 3 RESTful API exposing CRUD operations for bank accounts (`Compte`) with support for both JSON and XML payloads, in-memory H2 database persistence, automatic sample data initialization, and OpenAPI/Swagger UI documentation.

## Table of Contents
1. Overview
2. Features
3. Tech Stack
4. Architecture & Components
5. Domain Model
6. REST API Endpoints
7. Content Negotiation (JSON & XML)
8. Getting Started
9. Running & Development
10. H2 Database Console
11. OpenAPI / Swagger UI
12. Example Requests
13. Project Structure
14. Testing
15. Troubleshooting
16. Next Improvements

---
## 1. Overview
This project demonstrates the use of `@RestController` in Spring Boot to build a basic bank account service. It stores accounts with a balance, creation date, and account type (courant / épargne) using an in-memory H2 database. It supports both JSON and XML input/output via Spring's content negotiation.

## 2. Features
- CRUD operations on `Compte` entities
- Content negotiation: JSON and XML (`Accept` / `Content-Type` headers)
- In-memory H2 database (auto-created at startup)
- Sample data inserted at application startup (`CommandLineRunner`)
- OpenAPI 3 documentation & Swagger UI via `springdoc-openapi`
- Clean separation: entity, repository, controller layers
- Lombok for boilerplate reduction (getters/setters, constructors)

## 3. Tech Stack
- Java 17
- Spring Boot 3.5.x
- Spring Web
- Spring Data JPA
- H2 Database (in-memory)
- Lombok
- springdoc-openapi (Swagger UI)
- Maven (wrapper included)
- JUnit 5 (basic context load test)

## 4. Architecture & Components
Layered approach:
- Controller: `CompteController` exposes REST endpoints under `/banque`
- Repository: `CompteRepository` extends `JpaRepository`
- Entity: `Compte` mapped with JPA annotations
- Enum: `TypeCompte` defines allowed account types
- Bootstrap: `Application` seeds random accounts at startup

## 5. Domain Model
`Compte` fields:
- `Long id` (auto-generated)
- `double solde`
- `Date dateCreation` (stored as date only)
- `TypeCompte type` (enum: `COURANT`, `EPARGNE`)

Example JSON entity:
```json
{
  "id": 1,
  "solde": 4520.75,
  "dateCreation": "2025-11-10",
  "type": "EPARGNE"
}
```

## 6. REST API Endpoints
Base path: `/banque`

| Method | Path | Description | Produces | Consumes |
|--------|------|-------------|----------|----------|
| GET | `/comptes` | List all comptes | JSON, XML | – |
| GET | `/comptes/{id}` | Get compte by id | JSON, XML | – |
| POST | `/comptes` | Create new compte | JSON, XML | JSON, XML |
| PUT | `/comptes/{id}` | Update compte by id | JSON, XML | JSON, XML |
| DELETE | `/comptes/{id}` | Delete compte | – | – |

HTTP Status codes:
- `200 OK` – success (GET/PUT/DELETE)
- `201 Created` – (Not explicitly set: currently returns 200 from POST; could be improved)
- `404 Not Found` – resource missing

## 7. Content Negotiation (JSON & XML)
Set headers:
- Request JSON: `Content-Type: application/json`
- Request XML: `Content-Type: application/xml`
- Response preference: `Accept: application/json` or `Accept: application/xml`

XML example body:
```xml
<Compte>
  <solde>5000.0</solde>
  <dateCreation>2025-11-10</dateCreation>
  <type>COURANT</type>
</Compte>
```

## 8. Getting Started
### Prerequisites
- JDK 17 installed (`java -version`)
- Maven (not required globally; uses wrapper)

### Clone Repository
```bash
git clone <your-repo-url>
cd "TP8  Spring RestController"
```
(Adjust path if your folder name contains spaces.)

## 9. Running & Development
On Windows (CMD):
```cmd
mvnw.cmd clean spring-boot:run
```
Or build the jar:
```cmd
mvnw.cmd clean package
java -jar target\demo-0.0.1-SNAPSHOT.jar
```
Server runs on: `http://localhost:8082`

## 10. H2 Database Console
URL: `http://localhost:8082/h2-console`
- JDBC URL: `jdbc:h2:mem:banque`
- User: `sa`
- Password: (empty)
Ensure app is running before accessing.

## 11. OpenAPI / Swagger UI
Swagger UI (springdoc):
- `http://localhost:8082/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8082/v3/api-docs`

## 12. Example Requests
Using `curl` (Windows 10+ has curl by default):

List comptes (JSON):
```cmd
curl -H "Accept: application/json" http://localhost:8082/banque/comptes
```

Get compte by id:
```cmd
curl -H "Accept: application/json" http://localhost:8082/banque/comptes/1
```

Create compte (JSON):
```cmd
curl -X POST http://localhost:8082/banque/comptes ^
  -H "Content-Type: application/json" -H "Accept: application/json" ^
  -d "{\"solde\":2500.0,\"dateCreation\":\"2025-11-10\",\"type\":\"COURANT\"}"
```

Update compte:
```cmd
curl -X PUT http://localhost:8082/banque/comptes/1 ^
  -H "Content-Type: application/json" -H "Accept: application/json" ^
  -d "{\"solde\":9999.0,\"dateCreation\":\"2025-11-10\",\"type\":\"EPARGNE\"}"
```

Delete compte:
```cmd
curl -X DELETE http://localhost:8082/banque/comptes/2
```

XML create example:
```cmd
curl -X POST http://localhost:8082/banque/comptes ^
  -H "Content-Type: application/xml" -H "Accept: application/xml" ^
  -d "<Compte><solde>3000.0</solde><dateCreation>2025-11-10</dateCreation><type>EPARGNE</type></Compte>"
```

## 13. Project Structure
```
src/
  main/
    java/com/example/demo/
      Application.java                # Bootstraps app & seeds data
      controllers/CompteController.java  # REST endpoints
      entities/Compte.java               # JPA entity
      entities/TypeCompte.java           # Enum for account type
      repositories/CompteRepository.java # Spring Data JPA repository
    resources/
      application.properties             # Configuration (port, H2, JPA)
  test/
    java/com/example/demo/ApplicationTests.java # Context load test
```


## 14. Troubleshooting
| Issue | Possible Cause | Fix |
|-------|----------------|-----|
| H2 console not loading | App not running | Start app first |
| 404 on endpoint | Wrong URL path | Prefix with `/banque` |
| XML not returned | Missing `Accept: application/xml` | Add correct header |
| Swagger UI 404 | App not started or dependency missing | Check dependency and restart |
| Port already in use | Another service on 8082 | Change `server.port` in `application.properties` |

## 15. Next Improvements
- Return `201 Created` on POST with `Location` header
- Add validation (Bean Validation annotations) & error handling
- Implement paging & sorting for listing comptes
- Add DTO layer & ModelMapper to decouple internal entities
- Enhance test suite (unit + integration + JSON/XML assertions)
- Add Dockerfile for containerized deployment
- Security (Spring Security / JWT) for protected endpoints
- Use `LocalDate` instead of `Date`

---
### License
Educational / academic use. Add a specific license if publishing publicly.

### Author
Abderrahmane El Badouri

# Captures d'image
<img width="972" height="534" alt="Screenshot 2025-11-10 220802" src="https://github.com/user-attachments/assets/d00a13b6-eb50-49ac-8f59-15427127ebed" />
H2 Database

<img width="1893" height="1054" alt="Screenshot 2025-11-10 221718" src="https://github.com/user-attachments/assets/eb378913-6bc8-4b76-8e85-7f959f1cb75e" />
Premier Lancement Swagger

<img width="1763" height="593" alt="Screenshot 2025-11-10 222946" src="https://github.com/user-attachments/assets/967c7655-7d4f-45ef-8479-eb6936cc4e73" />

<img width="1754" height="593" alt="Screenshot 2025-11-10 223027" src="https://github.com/user-attachments/assets/92745ffe-13d3-4ba4-83e4-b27118636cf3" />
<img width="1759" height="747" alt="Screenshot 2025-11-10 223355" src="https://github.com/user-attachments/assets/37fd08fd-2a26-415c-a126-09c50f20a3f8" />
<img width="1774" height="928" alt="Screenshot 2025-11-10 223447" src="https://github.com/user-attachments/assets/e83a55c3-2e25-4b6b-88b0-e3cfbcf9ba6b" />
Testes d'apis 
