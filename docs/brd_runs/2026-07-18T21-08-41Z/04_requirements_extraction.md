# Step 4 — Requirements Extraction

**Run ID:** `2026-07-18T21-08-41Z`  
**Agent:** brd-requirements-extraction  
**Pipeline Step:** 4 of 8 — Extract

All items carry a confidence label. Citations are to files in `etd-airlines_1/src/`.

---

## 1. Functional Requirements

### Feature Group A — Flight Data Management

| FR-ID | Requirement | Confidence | Citation |
|---|---|---|---|
| FR-001 | The system shall allow an operator to create a new flight record by supplying: flight number, airline name, origin airport (IATA), destination airport (IATA), gate (optional), aircraft type (optional), and scheduled departure datetime. | Confirmed | `controller/FlightController.java:create()`, `dto/CreateFlightRequest.java` |
| FR-002 | The flight number field shall be validated against the pattern `^[A-Z]{2,3}\d{1,4}$` (e.g., `AA101`, `UAL1234`). | Confirmed | `dto/CreateFlightRequest.java` `@Pattern` annotation |
| FR-003 | The origin and destination fields shall each be validated as exactly 3 uppercase letters (`^[A-Z]{3}$`). | Confirmed | `dto/CreateFlightRequest.java` `@Pattern` on `origin` and `destination` |
| FR-004 | The system shall reject creation of a flight if an existing flight has the same flight number and the same scheduled departure time (duplicate prevention). | Confirmed | `service/FlightService.java:createFlight()` duplicate check |
| FR-005 | On successful creation, the initial estimated departure shall equal the scheduled departure, and delay minutes shall be 0. | Confirmed | `service/FlightService.java:createFlight()`, `FlightServiceTest.createFlightAssignsIdAndDefaults` |
| FR-006 | On successful creation, the system shall auto-assign a unique UUID as the flight identifier. | Confirmed | `repository/FlightRepository.java:save()` — `UUID.randomUUID()` |
| FR-007 | The system shall allow an operator to retrieve a list of all flights, optionally filtered by a free-text search across flight number, origin, destination, and airline name. | Confirmed | `controller/FlightController.java:listAll()`, `service/FlightService.java:searchFlights()` |
| FR-008 | The system shall allow retrieval of a single flight by its UUID. A non-existent ID shall return HTTP 404 with a descriptive error message. | Confirmed | `controller/FlightController.java:getOne()`, `exception/FlightNotFoundException.java` |
| FR-009 | The system shall allow permanent deletion of a flight record by UUID. Deleting a non-existent flight shall return HTTP 404. | Confirmed | `controller/FlightController.java:delete()`, `service/FlightService.java:deleteFlight()` |

### Feature Group B — ETD Update and Delay Management

| FR-ID | Requirement | Confidence | Citation |
|---|---|---|---|
| FR-010 | The system shall allow an operator to update a flight's estimated departure time (ETD), providing an optional delay reason (from a fixed enumeration) and optional free-text notes. | Confirmed | `controller/FlightController.java:updateEtd()`, `dto/UpdateEtdRequest.java` |
| FR-011 | The new ETD shall not be set to a datetime more than 5 minutes in the past. Such requests shall be rejected with HTTP 400. | Confirmed | `service/FlightService.java:updateEtd()` validation |
| FR-012 | After an ETD update, the flight's status shall be automatically recomputed based on the new ETD and the current server time. | Confirmed | `service/FlightService.java:updateEtd()` → `EtdCalculationService.computeStatus()` |
| FR-013 | A flight with an ETD that is 15 or more minutes later than its scheduled departure shall be considered "delayed". | Confirmed | `model/Flight.java:isDelayed()` — threshold of 15 minutes |
| FR-014 | The supported delay reasons are: WEATHER, AIR_TRAFFIC, MECHANICAL, CREW, SECURITY, LATE_INBOUND, FUELING, CATERING, BAGGAGE, OTHER. | Confirmed | `model/DelayReason.java` |
| FR-015 | Each delay reason has an associated estimated additional delay in minutes used by automated delay application: WEATHER=45, AIR_TRAFFIC=20, MECHANICAL=60, CREW=30, SECURITY=25, LATE_INBOUND=35, FUELING=15, CATERING=10, BAGGAGE=15, OTHER=20. | Confirmed | `service/EtdCalculationService.estimateAdditionalDelay()` |
| FR-016 | When a delay is applied via `EtdCalculationService.applyDelay()`, the ETD shall be advanced by the reason's estimated delay, the reason and notes stored, and the status set to DELAYED. | Confirmed | `service/EtdCalculationService.applyDelay()` |

### Feature Group C — Flight Status Lifecycle

| FR-ID | Requirement | Confidence | Citation |
|---|---|---|---|
| FR-017 | The flight status shall follow this computed lifecycle: SCHEDULED → BOARDING (within 30 minutes of ETD, no significant delay) → DEPARTED (ETD passed). | Confirmed | `service/EtdCalculationService.computeStatus()` |
| FR-018 | A significantly delayed flight (≥15 min) shall display status DELAYED instead of BOARDING or SCHEDULED when the status is recomputed. | Confirmed | `service/EtdCalculationService.computeStatus()` |
| FR-019 | Once a flight reaches CANCELLED, DIVERTED, or DEPARTED status, the status computation shall not change it further (terminal states). | Confirmed | `service/EtdCalculationService.computeStatus()` — guard at method entry |
| FR-020 | An operator shall be able to explicitly mark a flight as DEPARTED. The system shall record the actual departure datetime at the moment of this action. | Confirmed | `service/FlightService.java:markDeparted()` — `actualDeparture = LocalDateTime.now()` |
| FR-021 | An operator shall be able to cancel a flight with an optional cancellation note. The status shall be set to CANCELLED. | Confirmed | `service/FlightService.java:cancelFlight()` |

### Feature Group D — Statistics and Reporting

| FR-ID | Requirement | Confidence | Citation |
|---|---|---|---|
| FR-022 | The system shall provide aggregated statistics including: total flights, on-time count, delayed count, cancelled count, on-time percentage, average delay in minutes (across delayed flights), flight counts by status, flight counts by delay reason, and flight counts by airline. | Confirmed | `service/StatisticsService.buildStatistics()`, `dto/StatisticsDTO.java` |
| FR-023 | The statistics endpoint shall be available at `GET /api/statistics`. | Confirmed | `controller/StatisticsController.java` |
| FR-024 | The UI shall display statistics as visual charts: a doughnut chart by status, a bar chart by delay reason, and a horizontal bar chart by airline. | Confirmed | `static/js/statistics.js` |

### Feature Group E — User Interface

| FR-ID | Requirement | Confidence | Citation |
|---|---|---|---|
| FR-025 | The UI shall provide a Dashboard tab showing a filterable, sortable flight table with per-flight KPI counters (total, on-time, delayed, cancelled). | Confirmed | `static/js/dashboard.js`, `static/index.html` |
| FR-026 | The dashboard shall support filtering by flight status and sorting by ETD, scheduled time, delay size, or flight number. | Confirmed | `static/js/dashboard.js:render()` — statusFilter, sortBy controls |
| FR-027 | The dashboard shall display the "last updated" time and shall auto-refresh every 15 seconds. | Confirmed | `static/js/app.js` `REFRESH_INTERVAL_MS = 15000` |
| FR-028 | The UI shall provide an "Add Flight" tab with a form containing all required and optional fields, with default scheduled departure set to 1 hour from now. | Confirmed | `static/js/addFlight.js:init()` |

---

## 2. Non-Functional Requirements

| NFR-ID | Category | Requirement | Confidence | Citation |
|---|---|---|---|---|
| NFR-001 | Concurrency | The flight data store shall be thread-safe to handle concurrent HTTP requests. | Confirmed | `repository/FlightRepository.java` — `ConcurrentHashMap` |
| NFR-002 | API | All datetime values in REST responses shall be serialized as ISO-8601 strings, not numeric timestamps. | Confirmed | `application.properties` — `spring.jackson.serialization.write-dates-as-timestamps=false` |
| NFR-003 | API | All datetime values shall be in UTC. | Confirmed | `application.properties` — `spring.jackson.time-zone=UTC` |
| NFR-004 | Observability | The system shall expose Spring Actuator endpoints for health, info, and metrics. | Confirmed | `application.properties` — `management.endpoints.web.exposure.include=health,info,metrics` |
| NFR-005 | Port | The application shall start on port 8080 by default, configurable via `server.port`. | Confirmed | `application.properties` |
| NFR-006 | Security | The current version has no authentication or authorisation mechanism. | Confirmed | No Spring Security dependency in `pom.xml`; no security configuration found |
| NFR-007 | Persistence | The current version is non-persistent; all data is lost on restart. | Confirmed | `FlightRepository.java` — `ConcurrentHashMap` with no database backing |
| NFR-008 | Runtime | The application requires Java 17 and Maven 3.6+. | Confirmed | `pom.xml` `<java.version>17</java.version>`, README |

---

## 3. Open Questions / Unresolved Items

| OQ-ID | Question | Confidence |
|---|---|---|
| OQ-001 | What user roles exist and what access-control rules apply? The code has no RBAC. | Unresolved |
| OQ-002 | Is the DIVERTED status reachable via any UI or API action? No code path sets it. | Unresolved |
| OQ-003 | Are there any SLA targets (e.g., ETD update latency, dashboard refresh rate) beyond the 15 s UI polling? | Unresolved |
| OQ-004 | Should the system persist data across restarts? README suggests JPA as a "next step". | Assumed (no) |
| OQ-005 | Is the AA200 DFW→LHR route (international) an intentional scope indicator or a demo artefact? | Assumed (demo artefact) |
| OQ-006 | Should duplicate detection also cover the same flight number on different days? Current logic only matches same time. | Unresolved |
| OQ-007 | What happens when a flight is marked DEPARTED and then the operator attempts to cancel it? No guard in code. | Unresolved |
