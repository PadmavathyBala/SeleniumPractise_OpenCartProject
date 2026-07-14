# ETD Airlines — Departure Operations System

A complete Spring Boot + HTML/JavaScript reference application for managing
airline Estimated Time of Departure (ETD) operations.

## Architecture

```
+-----------------------------------------------------+
|  Browser (HTML / Bootstrap 5 / Vanilla JS)          |
|  - Dashboard, Statistics, Add Flight tabs           |
|  - Auto-refresh every 15s                           |
+-----------------------------------------------------+
                       |  REST / JSON
                       v
+-----------------------------------------------------+
|  Spring Boot 3.2 (Java 17)                          |
|  - Controllers  (FlightController, StatisticsCtrl)  |
|  - Services     (Flight, ETD calc, Simulation,Stats)|
|  - Repository   (in-memory, thread-safe)            |
|  - Scheduled job updates flights every 30s          |
+-----------------------------------------------------+
```

## Requirements

- Java 17 or later
- Maven 3.6+  (or use the Maven bundled with Eclipse)
- Eclipse IDE 2023-12 or later (recommended: Eclipse IDE for Enterprise Java)

## Importing into Eclipse

1. Unzip `etd-airlines.zip` to a folder of your choice.
2. In Eclipse: **File → Import → Maven → Existing Maven Projects**
3. Browse to the unzipped folder and click **Finish**.
4. Eclipse will download dependencies (first run takes ~1–2 minutes).
5. Once the build is green, right-click `EtdApplication.java`
   → **Run As → Java Application** (or Spring Boot App).

## Running from the Command Line

```bash
cd etd-airlines
mvn spring-boot:run
```

Then open **http://localhost:8080** in a browser.

## What You'll See

- A dashboard pre-populated with 15 sample flights from various US carriers.
- Two flights start with delays (UA456 weather, WN789 late inbound).
- Every 30 seconds the backend simulates status changes and may add a new delay.
- Every 15 seconds the UI auto-refreshes.

## REST API

| Method | Path                              | Purpose                          |
|--------|-----------------------------------|----------------------------------|
| GET    | `/api/flights`                    | List flights (`?search=` filter) |
| GET    | `/api/flights/{id}`               | Get one flight                   |
| POST   | `/api/flights`                    | Create a flight                  |
| PUT    | `/api/flights/{id}/etd`           | Update estimated departure       |
| POST   | `/api/flights/{id}/cancel`        | Cancel a flight                  |
| POST   | `/api/flights/{id}/depart`        | Mark a flight as departed        |
| DELETE | `/api/flights/{id}`               | Delete a flight                  |
| GET    | `/api/statistics`                 | Aggregated statistics            |
| GET    | `/actuator/health`                | Spring Boot health check         |

## Project Layout

```
etd-airlines/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/airlines/etd/
│   │   │   ├── EtdApplication.java           — Spring Boot entry point
│   │   │   ├── controller/                   — REST controllers
│   │   │   ├── service/                      — Business logic
│   │   │   ├── repository/                   — In-memory store
│   │   │   ├── model/                        — Domain objects + enums
│   │   │   ├── dto/                          — Request/response payloads
│   │   │   ├── exception/                    — Error handling
│   │   │   └── config/                       — Web/CORS/Jackson config
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/                       — HTML + CSS + JS
│   └── test/
│       └── java/com/airlines/etd/
│           └── FlightServiceTest.java
├── .project   .classpath   .settings/        — Eclipse metadata
└── README.md
```

## Extending the Application

Realistic next steps if you want to grow it:

- Swap the in-memory `FlightRepository` for Spring Data JPA + PostgreSQL.
- Replace polling with Server-Sent Events or WebSockets for real-time pushes.
- Add Spring Security with role-based access (operator vs. viewer).
- Plug in a real weather/ATC feed in `EtdCalculationService`.
- Add an audit log so every ETD change is traceable.

## Troubleshooting

**Port 8080 is already in use** — change `server.port` in
`src/main/resources/application.properties`.

**Maven download fails behind a corporate proxy** — configure `~/.m2/settings.xml`
with your proxy settings, then in Eclipse run **Project → Maven → Update Project**.

**Build errors right after import** — right-click the project →
**Maven → Update Project → OK**. This forces Eclipse to re-resolve dependencies.
