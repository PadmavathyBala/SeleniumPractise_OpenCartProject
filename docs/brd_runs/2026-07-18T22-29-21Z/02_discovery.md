# Step 2 — Discovery Document

**Run ID:** `2026-07-18T22-29-21Z`  
**Agent:** brd-discovery  
**Pipeline Step:** 2 of 8 — Discover  
**Supersedes:** `docs/brd_runs/2026-07-18T21-08-41Z/02_discovery.md`

New findings compared with v1 are marked **[NEW v2]**.

---

## 1. Repository Layout (etd-airlines_1)

```
etd-airlines_1/
├── pom.xml                                         ← Maven build descriptor
├── README.md                                       ← Project documentation
├── agents/                                         ← Agent instruction files (pipeline meta)
│   ├── Grounding Documents/
│   │   ├── Business Requirements Document Template.doc
│   │   └── Sample BRD_template.pdf
│   └── *.agent.md
├── src/
│   ├── main/
│   │   ├── java/com/airlines/etd/
│   │   │   ├── EtdApplication.java               ← @SpringBootApplication + @EnableScheduling
│   │   │   ├── config/WebConfig.java             ← CORS + Jackson configuration
│   │   │   ├── controller/
│   │   │   │   ├── FlightController.java
│   │   │   │   └── StatisticsController.java
│   │   │   ├── dto/
│   │   │   │   ├── CreateFlightRequest.java
│   │   │   │   ├── FlightDTO.java
│   │   │   │   ├── StatisticsDTO.java
│   │   │   │   └── UpdateEtdRequest.java
│   │   │   ├── exception/
│   │   │   │   ├── FlightNotFoundException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── model/
│   │   │   │   ├── DelayReason.java
│   │   │   │   ├── Flight.java
│   │   │   │   └── FlightStatus.java
│   │   │   ├── repository/
│   │   │   │   └── FlightRepository.java
│   │   │   └── service/
│   │   │       ├── EtdCalculationService.java
│   │   │       ├── FlightService.java
│   │   │       ├── FlightSimulationService.java
│   │   │       └── StatisticsService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           ├── css/styles.css
│   │           ├── index.html
│   │           └── js/
│   │               ├── addFlight.js
│   │               ├── api.js
│   │               ├── app.js
│   │               ├── dashboard.js
│   │               └── statistics.js
│   └── test/
│       └── java/com/airlines/etd/
│           └── FlightServiceTest.java
```

Total source files discovered: **36** (excluding Eclipse metadata and agent files)

---

## 2. Technology Stack

| Component | Technology | Version | Evidence |
|---|---|---|---|
| Runtime language | Java | 17 | `pom.xml` `<java.version>17</java.version>` — **Confirmed** |
| Application framework | Spring Boot | 3.2.5 | `pom.xml` parent `spring-boot-starter-parent:3.2.5` — **Confirmed** |
| Web layer | Spring MVC (REST) | (Spring Boot managed) | `@RestController` in controllers — **Confirmed** |
| Validation | Jakarta Bean Validation | (Spring Boot managed) | `@NotBlank`, `@Pattern`, `@NotNull` in DTOs — **Confirmed** |
| Monitoring | Spring Actuator | (Spring Boot managed) | `pom.xml` dependency; endpoints exposed in `application.properties` — **Confirmed** |
| Build tool | Maven | 3.6+ (stated in README) | `pom.xml` present — **Confirmed** |
| UI framework | Bootstrap | **5.3.2** [NEW v2] | `index.html` CDN: `bootstrap@5.3.2` — **Confirmed** |
| UI icons | Bootstrap Icons | **1.11.1** [NEW v2] | `index.html` CDN: `bootstrap-icons@1.11.1` — **Confirmed** |
| Chart library | Chart.js | **4.4.0** [NEW v2] | `index.html` CDN: `chart.js@4.4.0` — **Confirmed** |
| Front-end language | Vanilla JavaScript (ES2017+) | N/A | `js/*.js` files — **Confirmed** |
| Data persistence | In-memory (`ConcurrentHashMap`) | N/A | `FlightRepository.java` line 18 — **Confirmed** |
| IDE metadata | Eclipse 2023-12 | N/A | `.project`, `.classpath`, `.settings/` — **Confirmed** |

---

## 3. Domain Model

### 3.1 Flight (Entity)

**Source:** `model/Flight.java` — **Confirmed**

| Field | Type | Notes |
|---|---|---|
| `id` | `String` (UUID) | Auto-assigned by repository on first save |
| `flightNumber` | `String` | IATA-style identifier (e.g. `AA101`) |
| `airline` | `String` | Airline name (free text) |
| `origin` | `String` | 3-letter IATA airport code |
| `destination` | `String` | 3-letter IATA airport code |
| `gate` | `String` | Gate identifier (alphanumeric, optional) |
| `aircraftType` | `String` | Aircraft type description (optional) |
| `scheduledDeparture` | `LocalDateTime` | Original scheduled departure |
| `estimatedDeparture` | `LocalDateTime` | Current best estimate; starts = scheduled |
| `actualDeparture` | `LocalDateTime` | Set when flight is marked departed |
| `status` | `FlightStatus` | Current operational status enum |
| `delayReason` | `DelayReason` | Categorised reason (nullable) |
| `delayNotes` | `String` | Free-text notes on delay/cancellation |
| `lastUpdated` | `LocalDateTime` | Updated on ETD/status change |

Computed properties (not stored):
- `getDelayMinutes()` — `max(0, estimatedDeparture − scheduledDeparture)` in minutes
- `isDelayed()` — returns `true` when `delayMinutes >= 15`

### 3.2 FlightStatus (Enum)

**Source:** `model/FlightStatus.java` — **Confirmed**

| Value | Display Name |
|---|---|
| `SCHEDULED` | Scheduled |
| `BOARDING` | Boarding |
| `DEPARTED` | Departed |
| `DELAYED` | Delayed |
| `CANCELLED` | Cancelled |
| `DIVERTED` | Diverted |

### 3.3 DelayReason (Enum)

**Source:** `model/DelayReason.java` — **Confirmed**

| Value | Display Name | Est. Delay (min) |
|---|---|---|
| `WEATHER` | Weather | 45 |
| `AIR_TRAFFIC` | Air Traffic Control | 20 |
| `MECHANICAL` | Mechanical Issue | 60 |
| `CREW` | Crew Availability | 30 |
| `SECURITY` | Security | 25 |
| `LATE_INBOUND` | Late Arriving Aircraft | 35 |
| `FUELING` | Fueling Delay | 15 |
| `CATERING` | Catering | 10 |
| `BAGGAGE` | Baggage Handling | 15 |
| `OTHER` | Other | 20 |

Delay estimates sourced from `EtdCalculationService.estimateAdditionalDelay()` — **Confirmed**

---

## 4. REST API Catalogue

**Source:** `controller/FlightController.java`, `controller/StatisticsController.java` — **Confirmed**

| # | Method | Path | Purpose | Success Code |
|---|---|---|---|---|
| 1 | GET | `/api/flights` | List all flights; optional `?search=` filter | 200 |
| 2 | GET | `/api/flights/{id}` | Get single flight by UUID | 200 |
| 3 | POST | `/api/flights` | Create a new flight | 201 |
| 4 | PUT | `/api/flights/{id}/etd` | Update estimated departure time | 200 |
| 5 | POST | `/api/flights/{id}/cancel` | Cancel a flight | 200 |
| 6 | POST | `/api/flights/{id}/depart` | Mark flight as departed | 200 |
| 7 | DELETE | `/api/flights/{id}` | Delete a flight record | 204 |
| 8 | GET | `/api/statistics` | Aggregated operational statistics | 200 |
| 9 | GET | `/actuator/health` | Spring Boot health check | 200 |

Also exposed (from `application.properties`): `/actuator/info`, `/actuator/metrics`

### 4.1 Request Payload — CreateFlightRequest

**Source:** `dto/CreateFlightRequest.java` — **Confirmed**

| Field | Type | Validation |
|---|---|---|
| `flightNumber` | String | `@NotBlank`; regex `^[A-Z]{2,3}\d{1,4}$` |
| `airline` | String | `@NotBlank` |
| `origin` | String | `@NotBlank`; regex `^[A-Z]{3}$` |
| `destination` | String | `@NotBlank`; regex `^[A-Z]{3}$` |
| `gate` | String | Optional |
| `aircraftType` | String | Optional |
| `scheduledDeparture` | LocalDateTime | `@NotNull` |

### 4.2 Request Payload — UpdateEtdRequest

**Source:** `dto/UpdateEtdRequest.java` — **Confirmed**

| Field | Type | Validation |
|---|---|---|
| `newEstimatedDeparture` | LocalDateTime | `@NotNull` |
| `delayReason` | DelayReason | Optional |
| `delayNotes` | String | Optional |

---

## 5. Service Inventory

| Service | Responsibility | Source File |
|---|---|---|
| `FlightService` | CRUD, search, business rule enforcement | `service/FlightService.java` |
| `EtdCalculationService` | Status computation, delay estimation, delay application | `service/EtdCalculationService.java` |
| `FlightSimulationService` | Seed data on startup; periodic status/delay simulation | `service/FlightSimulationService.java` |
| `StatisticsService` | Aggregate counts, percentages, averages | `service/StatisticsService.java` |

---

## 6. UI Screens / Tabs

**Source:** `static/index.html`, `static/js/*.js` — **Confirmed**

| Tab | Module | Key Functions |
|---|---|---|
| Dashboard | `dashboard.js` | Flight table with filters/sort; KPI panel; ETD update modal; depart/cancel/delete actions |
| Statistics | `statistics.js` | Status doughnut chart; delay-reason bar chart; airline bar chart (Chart.js) |
| Add Flight | `addFlight.js` | New flight form with client-side and server-side validation |

UI auto-refreshes every **15 seconds** — **Confirmed** (`app.js` `REFRESH_INTERVAL_MS = 15000`)  
Backend simulation runs every **30 seconds** — **Confirmed** (`FlightSimulationService.java` `@Scheduled(fixedRate = 30_000)`)

---

## 7. Seed Data

**Source:** `FlightSimulationService.seedFlights()` — **Confirmed**

15 flights pre-loaded on startup covering:
- Airlines: American Airlines, Delta Air Lines, United Airlines, Southwest Airlines, JetBlue Airways, Alaska Airlines, Frontier
- US airports: DFW, LAX, ATL, JFK, ORD, SFO, DAL, PHX, MIA, DEN, SEA, EWR, DTW, MDW, BWI, LHR (London Heathrow as single international route)
- 2 flights pre-delayed: UA456 (WEATHER), WN789 (LATE_INBOUND)

---

## 8. Error Handling

**Source:** `exception/GlobalExceptionHandler.java` — **Confirmed**

| Exception | HTTP Status | Notes |
|---|---|---|
| `FlightNotFoundException` | 404 | Flight ID not found |
| `MethodArgumentNotValidException` | 400 | Bean validation failure; field errors returned |
| `IllegalArgumentException` | 400 | Business rule violation |
| `Exception` (fallback) | 500 | Generic server error |

---

## 9. Configuration Summary

**Source:** `application.properties` — **Confirmed**

| Setting | Value | Purpose |
|---|---|---|
| `server.port` | 8080 | HTTP listener |
| `spring.application.name` | `etd-airlines` | Application identifier |
| `spring.jackson.serialization.write-dates-as-timestamps` | `false` | ISO-8601 date serialization |
| `spring.jackson.time-zone` | UTC | All timestamps in UTC |
| `logging.level.root` | INFO | Root log level |
| `logging.level.com.airlines.etd` | DEBUG | Application log level |
| `management.endpoints.web.exposure.include` | `health,info,metrics` | Actuator endpoints |

### 9.1 CORS Configuration [NEW v2]

**Source:** `config/WebConfig.java` lines 18–23 — **Confirmed**

| Setting | Value | Note |
|---|---|---|
| Path pattern | `/api/**` | All API endpoints |
| Allowed origins | `*` (all) | Open to any origin |
| Allowed methods | `GET, POST, PUT, DELETE, OPTIONS` | Full REST method set |
| Allowed headers | `*` (all) | Unrestricted |

**Security note:** Allowing all origins is appropriate for a local demo but constitutes
a security risk in a production environment. See NFR-006 and OQ-012.

### 9.2 Scheduling [NEW v2]

**Source:** `EtdApplication.java` line 12 — **Confirmed**

`@EnableScheduling` is declared on the main application class, enabling the
`@Scheduled` annotation used by `FlightSimulationService`.

---

## 10. Repository Methods — Usage Analysis [NEW v2]

**Source:** `repository/FlightRepository.java` — **Confirmed**

| Method | Called By | Status |
|---|---|---|
| `save()` | `FlightService`, `FlightSimulationService`, `EtdCalculationService` | Active |
| `findById()` | `FlightService` | Active |
| `findByFlightNumber()` | `FlightService.createFlight()` | Active (duplicate check) |
| `findAll()` | `FlightService`, `StatisticsService`, `FlightSimulationService` | Active |
| `findByStatus()` | None | **Unused** — no service calls this method |
| `findByOrigin()` | None | **Unused** — no service calls this method |
| `findByDestination()` | None | **Unused** — no service calls this method |
| `deleteById()` | `FlightService.deleteFlight()` | Active |
| `count()` | None | **Unused** — not called by any service |
| `deleteAll()` | None — present for test teardown use | **Unused in production code** |

Note: `findByStatus()`, `findByOrigin()`, `findByDestination()`, `count()`, and
`deleteAll()` appear to be scaffolding methods prepared for future features.

---

## 11. Test Coverage

**Source:** `src/test/java/com/airlines/etd/FlightServiceTest.java` — **Confirmed**

| Test Method | What It Tests |
|---|---|
| `createFlightAssignsIdAndDefaults` | Flight creation: ID assignment, ETD=scheduled, delayMinutes=0 |
| `updateEtdMarksFlightAsDelayed` | ETD update: delay calculation, `isDelayed()` flag, reason captured |

No integration tests, no controller tests, no simulation tests present.
