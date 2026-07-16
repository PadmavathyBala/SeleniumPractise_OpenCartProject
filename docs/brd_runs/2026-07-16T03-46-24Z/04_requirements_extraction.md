# Step 4 — Requirements Extraction (brd-requirements-extraction)

**Run ID:** `2026-07-16T03-46-24Z`  
**Agent:** brd-requirements-extraction  
**Input:** 02_discovery.md, 03_business_process.md  
**Output summary:** BR: 8, FR: 24, BRULE: 14, NFR: 10, INT: 3

---

## 4.1 Business Requirements (BR)

| ID | Requirement | Confidence | Source (BP/Discovery) |
|----|-------------|-----------|----------------------|
| BR-001 | The system shall provide departure operations staff with a real-time view of all scheduled, active, and completed flights for the current departure session. | Confirmed | BP-001; D2.1 |
| BR-002 | The system shall allow authorised operations staff to create, update, cancel, and remove flight records during a departure session. | Confirmed | BP-002, BP-003, BP-004, BP-006; D4.3–D4.7 |
| BR-003 | The system shall maintain the current operational status of every flight and update it automatically based on scheduled and estimated departure times. | Confirmed | BP-008; D5.1, D5.2 |
| BR-004 | The system shall allow operations staff to record and categorise the reason for any flight delay, so that performance data can be aggregated for operational analysis. | Confirmed | BP-010; D3.3, D6.7 |
| BR-005 | The system shall provide aggregated departure performance statistics to support operational decision-making and management reporting. | Confirmed | BP-007; D2.2, D8.1–D8.3 |
| BR-006 | The system shall record the actual departure time when a flight physically leaves the gate. | Confirmed | BP-005; D4.6 |
| BR-007 | The system shall prevent data entry errors by validating all flight information before accepting a new flight record. | Confirmed | BP-002; D6.1–D6.4 |
| BR-008 | The system shall support monitoring of its own operational health by an external monitoring tool. | Confirmed | BP-011; D2.5 |

---

## 4.2 Functional Requirements (FR)

### Dashboard and Search

| ID | Requirement | Confidence | Source |
|----|-------------|-----------|--------|
| FR-001 | The system shall display a departure board listing all flights with the following columns: Flight Number, Airline, Route (Origin → Destination), Gate, Scheduled Departure, Estimated Departure, Delay (minutes), Status, and Delay Reason. | Confirmed | BP-001; D7.2 |
| FR-002 | The system shall display KPI summary cards showing: total flights, on-time count with percentage, delayed count with average delay, and cancelled count. | Confirmed | BP-001; D2.2; `dashboard.js` `updateKpis()` |
| FR-003 | The system shall provide a free-text search on the departure board that filters flights by flight number, airline name, origin, or destination (case-insensitive substring match). | Confirmed | BP-001; D4.1; D7.5 |
| FR-004 | The system shall provide a status filter on the departure board that limits the displayed flights to a single selected status. | Confirmed | BP-001; D7.5 |
| FR-005 | The system shall provide sort options on the departure board: by Estimated Departure (default), Scheduled Departure, Delay magnitude, and Flight Number. | Confirmed | BP-001; D7.5 |
| FR-006 | The departure board shall auto-refresh every 15 seconds when it is the active tab. | Confirmed | BP-001; D5.6; `app.js` `REFRESH_INTERVAL_MS = 15000` |

### Flight Management

| ID | Requirement | Confidence | Source |
|----|-------------|-----------|--------|
| FR-007 | The system shall allow an operator to create a new flight record by providing: flight number, airline, origin, destination, scheduled departure, and optionally gate and aircraft type. | Confirmed | BP-002; D4.3; D6.1–D6.3 |
| FR-008 | The system shall assign a system-generated unique identifier to every new flight record. | Confirmed | D3.1; `FlightRepository.java` `UUID.randomUUID()` |
| FR-009 | The system shall initialise a newly created flight with status SCHEDULED and estimated departure equal to scheduled departure. | Confirmed | BP-002; `FlightService.java` `createFlight()` |
| FR-010 | The system shall allow an operator to update the estimated departure time of a flight through an inline modal dialog, with optional delay reason and free-text delay notes. | Confirmed | BP-003; D4.4; D7.4 |
| FR-011 | The system shall automatically recompute the flight status after every ETD update, applying the status transition rules defined in BRULE-001 through BRULE-006. | Confirmed | BP-003, BP-008; D5.2 |
| FR-012 | The system shall allow an operator to cancel a flight with an optional free-text cancellation note. | Confirmed | BP-004; D4.5 |
| FR-013 | The system shall allow an operator to mark a flight as departed, recording the system timestamp as the actual departure time. | Confirmed | BP-005; D4.6 |
| FR-014 | The system shall allow an operator to permanently delete a flight record. | Confirmed | BP-006; D4.7 |
| FR-015 | The system shall provide visual differentiation of flight rows on the departure board: delayed rows highlighted in amber, cancelled rows in grey with strike-through appearance, departed rows in grey. | Confirmed | BP-001; D7.3 |
| FR-016 | The system shall display delay badges colour-coded by severity: no delay = neutral, 1–29 minutes = amber (minor), 30+ minutes = red (major). | Confirmed | BP-001; D7.4 |

### Statistics

| ID | Requirement | Confidence | Source |
|----|-------------|-----------|--------|
| FR-017 | The system shall provide a Statistics view displaying: total flights, on-time count, on-time percentage, delayed count, cancelled count, average delay (minutes over delayed flights only), count by status, count by delay reason, and count by airline. | Confirmed | BP-007; D8.1–D8.3 |
| FR-018 | The Statistics view shall auto-refresh every 15 seconds when it is the active tab. | Confirmed | D5.6; `app.js` |

### Notifications and Feedback

| ID | Requirement | Confidence | Source |
|----|-------------|-----------|--------|
| FR-019 | The system shall display a transient toast notification confirming the outcome (success or failure) of every operator action. | Confirmed | BP-002–BP-006; D7.7 |
| FR-020 | The system shall surface the specific error message returned by the backend to the operator in the failure toast notification. | Confirmed | D9.5; `dashboard.js` |

### Automated Lifecycle

| ID | Requirement | Confidence | Source |
|----|-------------|-----------|--------|
| FR-021 | The system shall automatically evaluate and update the status of all active flights every 30 seconds based on the current time and each flight's ETD. | Confirmed | BP-008; D5.1 |
| FR-022 | The system shall not automatically change the status of flights in terminal states (CANCELLED, DEPARTED, DIVERTED). | Confirmed | BP-008; D6.8 |

### Health

| ID | Requirement | Confidence | Source |
|----|-------------|-----------|--------|
| FR-023 | The system shall expose a health-check endpoint (`GET /actuator/health`) that returns the application's operational status for use by external monitoring tools. | Confirmed | BP-011; D2.5 |
| FR-024 | The system shall expose metrics and info actuator endpoints (`/actuator/metrics`, `/actuator/info`) for operational monitoring. | Confirmed | D2.5; `application.properties` |

---

## 4.3 Business Rules (BRULE)

| ID | Rule | Confidence | Source |
|----|------|-----------|--------|
| BRULE-001 | A flight whose ETD has passed the current time shall automatically transition to DEPARTED status. | Confirmed | D5.2; `EtdCalculationService.computeStatus()` |
| BRULE-002 | A flight that is not delayed and whose ETD is within 30 minutes of the current time shall transition to BOARDING status. | Confirmed | D5.2 |
| BRULE-003 | A flight that is delayed (ETD ≥ 15 minutes later than scheduled) and whose ETD is within 30 minutes of the current time shall remain in DELAYED status (not transition to BOARDING). | Confirmed | D5.2 |
| BRULE-004 | A flight is classified as delayed when its estimated departure is at least 15 minutes later than its scheduled departure. | Confirmed | D6.6; `Flight.isDelayed()` |
| BRULE-005 | A new ETD submitted by an operator must not be more than 5 minutes earlier than the current system time. | Confirmed | D6.5; `FlightService.updateEtd()` |
| BRULE-006 | Terminal flight statuses (CANCELLED, DEPARTED, DIVERTED) shall not be altered by the automated status lifecycle process. | Confirmed | D6.8 |
| BRULE-007 | A flight number must conform to the format: 2 to 3 uppercase letters followed by 1 to 4 digits (e.g., AA123, UAL1234). | Confirmed | D6.1 |
| BRULE-008 | Origin and destination airport codes must each be exactly 3 uppercase letters conforming to IATA airport code format. | Confirmed | D6.2 |
| BRULE-009 | A flight number may not be registered twice for the same scheduled departure date and time. | Confirmed | D6.4; `FlightService.createFlight()` |
| BRULE-010 | When a delay reason category is applied to a flight, the system shall add the standard estimated delay duration for that category to the current ETD. | Confirmed | D6.7; `EtdCalculationService.applyDelay()` |
| BRULE-011 | Standard estimated delay durations by category are: Weather = 45 min; Air Traffic Control = 20 min; Mechanical = 60 min; Crew = 30 min; Security = 25 min; Late Arriving Aircraft = 35 min; Fueling = 15 min; Catering = 10 min; Baggage = 15 min; Other = 20 min. | Confirmed | D6.7 |
| BRULE-012 | The on-time performance percentage is calculated as: (count of flights that are not delayed AND not cancelled) ÷ (total flights) × 100. | Confirmed | D8.2; `StatisticsService.buildStatistics()` |
| BRULE-013 | The average delay statistic is calculated only over flights classified as delayed (≥ 15 min delay). On-time and cancelled flights are excluded from this average. | Confirmed | D8.3 |
| BRULE-014 | Flight number lookups in the duplicate-check shall be case-insensitive. | Confirmed | D6.10; `FlightRepository.findByFlightNumber()` |

---

## 4.4 Data Requirements

| # | Requirement | Confidence | Source |
|---|-------------|-----------|--------|
| DATA-001 | Each flight record shall contain: unique system ID (UUID), flight number, airline name, origin IATA code, destination IATA code, gate assignment (optional), aircraft type (optional), scheduled departure (datetime), estimated departure (datetime), actual departure (datetime, nullable), status, delay reason (nullable), delay notes (nullable), last updated timestamp. | Confirmed | D3.1; `Flight.java` |
| DATA-002 | All datetime values shall be stored and transmitted in UTC, serialised as ISO-8601 strings. | Confirmed | D4.9; `application.properties` |
| DATA-003 | The system shall retain all flight records in memory for the duration of the application session. Data is not persisted across restarts. | Confirmed | D11.2; `FlightRepository.java` |
| DATA-004 | Flight records shall be uniquely keyed by their system-generated UUID. | Confirmed | D3.1; `FlightRepository.java` |

---

## 4.5 Integration Requirements (INT)

| ID | Requirement | Confidence | Source |
|----|-------------|-----------|--------|
| INT-001 | The system shall expose a RESTful JSON API over HTTP at path prefix `/api` to support integration with other operational systems or client applications. | Confirmed | D4.1–D4.8; `FlightController.java` |
| INT-002 | The system shall expose Spring Boot Actuator endpoints (`/actuator/health`, `/actuator/info`, `/actuator/metrics`) for integration with external health-monitoring platforms. | Confirmed | D2.5 |
| INT-003 | The system shall support integration with real weather, ATC, and aircraft-tracking data feeds to replace the static delay estimation model used in the current reference implementation. | Inferred | README.md "Extending the Application"; `EtdCalculationService.java` Javadoc "In a real airline system…" |

---

## 4.6 Non-Functional Requirements (NFR)

| ID | Requirement | Confidence | Source |
|----|-------------|-----------|--------|
| NFR-001 | The system shall be thread-safe; concurrent read and write operations on the flight repository shall not produce data corruption or race conditions. | Confirmed | D10.3; `FlightRepository.java` uses `ConcurrentHashMap` |
| NFR-002 | The system backend shall be implemented using Java 17 or later and Spring Boot 3.2 or later. | Confirmed | D10.1; `pom.xml` |
| NFR-003 | The system frontend shall function in a modern web browser without requiring any browser plugin or local installation beyond the browser itself. | Confirmed | D10.2; CDN-loaded Bootstrap 5, vanilla JS |
| NFR-004 | All API response bodies for error conditions shall include a machine-readable `status` code, a human-readable `message`, and a `timestamp`. | Confirmed | D9.1–D9.4; `GlobalExceptionHandler.java` |
| NFR-005 | The system shall respond to the health-check endpoint within a time sufficient for external monitoring to distinguish UP from DOWN status. | Assumed | BP-011; No SLA defined in codebase |
| NFR-006 | The system shall be deployable from the command line using `mvn spring-boot:run` on any machine with Java 17+ and Maven 3.6+. | Confirmed | README.md |
| NFR-007 | The system shall log application events at INFO level and ETD-airlines-specific events at DEBUG level. | Confirmed | D10.1; `application.properties` |
| NFR-008 | The system shall support CORS configuration to allow the frontend to communicate with the backend when served from the same origin or a configured cross-origin. | Inferred | `WebConfig.java` present; explicit CORS policy not reviewed in detail |
| NFR-009 | The system shall handle all unhandled exceptions without exposing stack traces to API clients; instead, a structured error response shall be returned. | Confirmed | D9.4; `GlobalExceptionHandler.handleGeneric()` |
| NFR-010 | API pagination for large flight lists is not currently implemented; its requirement for production-scale usage is unresolved. | Unresolved | D11.6; no pagination in `FlightController` |

---

*Requirements extraction complete. BR: 8, FR: 24, BRULE: 14, NFR: 10, INT: 3, DATA: 4*
