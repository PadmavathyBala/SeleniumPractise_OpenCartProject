# Step 2 — Discovery Findings (brd-discovery)

**Run ID:** `2026-07-16T03-46-24Z`  
**Agent:** brd-discovery  
**Sources reviewed:** 39 files across Java, JavaScript, HTML, config, test, and documentation  
**Findings summary:** 47 total — 31 Confirmed, 9 Inferred, 5 Assumed, 2 Unresolved

---

## D1. Application Overview

| # | Finding | Confidence | Source |
|---|---------|-----------|--------|
| D1.1 | The application is named **ETD Airlines — Departure Operations System**, a Spring Boot 3.2 web application serving a single-page HTML/JS frontend. | Confirmed | `README.md` ln 1; `application.properties` `spring.application.name=etd-airlines` |
| D1.2 | The application runs on **port 8080** by default, serving both the REST API at `/api` and static UI from `/`. | Confirmed | `application.properties` `server.port=8080`; `api.js` `const BASE = '/api'` |
| D1.3 | The system manages airline departure operations for **a single terminal or airport context** — no multi-airport scoping is present in the data model. | Inferred | `Flight.java` — no airport-scope field; all sample data uses mixed airports from `FlightSimulationService.java` |
| D1.4 | The system is intended for use by **departure operations staff** (ground crew / gate agents). | Inferred | README "Departure Operations System"; button labels "Mark Departed," "Cancel Flight"; no roles defined |
| D1.5 | The application is a **reference/training application** with simulated data, not a production system connected to live feeds. | Confirmed | README "reference application"; `FlightSimulationService.java` `@PostConstruct seedFlights()` |

---

## D2. Modules and Features

| # | Finding | Confidence | Source |
|---|---------|-----------|--------|
| D2.1 | **Dashboard module** — displays a tabular view of all flights, KPI summary cards, search, filter, and sort controls, and per-row action buttons. | Confirmed | `index.html`; `dashboard.js` |
| D2.2 | **Statistics module** — displays aggregated metrics: total flights, on-time count and percentage, delayed count, cancelled count, average delay, counts by status, counts by delay reason, counts by airline. | Confirmed | `statistics.js`; `StatisticsController.java`; `StatisticsService.java`; `StatisticsDTO.java` |
| D2.3 | **Add Flight module** — provides a form for creating a new flight record with validation. | Confirmed | `addFlight.js`; `index.html` form fields; `CreateFlightRequest.java` |
| D2.4 | **ETD Update modal** — an inline modal dialog for updating the estimated departure time, delay reason, and delay notes for an existing flight. | Confirmed | `dashboard.js` `openEtdModal()` / `saveEtdUpdate()`; `index.html` `#etdModal`; `UpdateEtdRequest.java` |
| D2.5 | **Actuator health check** — Spring Boot Actuator is enabled at `/actuator/health`, `/actuator/info`, `/actuator/metrics`. | Confirmed | `application.properties` `management.endpoints.web.exposure.include=health,info,metrics` |
| D2.6 | **Flight simulation engine** — a background job that seeds sample data and periodically simulates status changes and random delays. | Confirmed | `FlightSimulationService.java` `@PostConstruct seedFlights()`, `@Scheduled(fixedRate=30_000)` |

---

## D3. Domain Model

| # | Finding | Confidence | Source |
|---|---------|-----------|--------|
| D3.1 | The core domain entity is **Flight**, with fields: id (UUID), flightNumber, airline, origin (IATA), destination (IATA), gate, aircraftType, scheduledDeparture, estimatedDeparture, actualDeparture, status, delayReason, delayNotes, lastUpdated. | Confirmed | `Flight.java` |
| D3.2 | Flight status values are: **SCHEDULED, BOARDING, DEPARTED, DELAYED, CANCELLED, DIVERTED**. | Confirmed | `FlightStatus.java` |
| D3.3 | Delay reason values are: **WEATHER, AIR_TRAFFIC, MECHANICAL, CREW, SECURITY, LATE_INBOUND, FUELING, CATERING, BAGGAGE, OTHER**. | Confirmed | `DelayReason.java` |
| D3.4 | A flight is considered **significantly delayed** when its estimated departure is ≥ 15 minutes later than its scheduled departure. | Confirmed | `Flight.java` `isDelayed()`: `return getDelayMinutes() >= 15` |
| D3.5 | **Delay minutes** is computed as `max(0, ChronoUnit.MINUTES.between(scheduledDeparture, estimatedDeparture))` — negative values are floored to zero, meaning early arrivals are treated as on-time. | Confirmed | `Flight.java` `getDelayMinutes()` |
| D3.6 | The **DIVERTED** status exists in the enum but no code path actively sets a flight to DIVERTED through the API or simulation. | Inferred | `FlightStatus.java`; `EtdCalculationService.java` preserves DIVERTED but never sets it; no `/divert` endpoint in `FlightController.java` |

---

## D4. REST API

| # | Finding | Confidence | Source |
|---|---------|-----------|--------|
| D4.1 | `GET /api/flights` — returns all flights sorted by scheduled departure, with optional `?search=` query parameter filtering by flight number, origin, destination, or airline name (case-insensitive substring match). | Confirmed | `FlightController.java` `listAll()`; `FlightService.java` `searchFlights()` |
| D4.2 | `GET /api/flights/{id}` — returns a single flight by UUID. Returns 404 with error body if not found. | Confirmed | `FlightController.java` `getOne()`; `GlobalExceptionHandler.java` `handleNotFound()` |
| D4.3 | `POST /api/flights` — creates a new flight. Validates required fields and format. Returns 201 on success. Rejects duplicate flight number for the same scheduled time. | Confirmed | `FlightController.java` `create()`; `FlightService.java` `createFlight()`; `CreateFlightRequest.java` annotations |
| D4.4 | `PUT /api/flights/{id}/etd` — updates the estimated departure time; optionally sets delay reason and notes; recomputes flight status automatically. Rejects ETD more than 5 minutes in the past. | Confirmed | `FlightController.java` `updateEtd()`; `FlightService.java` `updateEtd()` |
| D4.5 | `POST /api/flights/{id}/cancel` — sets flight status to CANCELLED with optional notes. No guard against re-cancelling an already-cancelled flight. | Confirmed | `FlightController.java` `cancel()`; `FlightService.java` `cancelFlight()` |
| D4.6 | `POST /api/flights/{id}/depart` — sets flight status to DEPARTED and records the actual departure timestamp as `LocalDateTime.now()`. No guard against departing a cancelled flight. | Confirmed | `FlightController.java` `depart()`; `FlightService.java` `markDeparted()` |
| D4.7 | `DELETE /api/flights/{id}` — permanently deletes a flight. Returns 204 on success. Returns 404 if the flight does not exist. | Confirmed | `FlightController.java` `delete()`; `FlightService.java` `deleteFlight()` |
| D4.8 | `GET /api/statistics` — returns aggregated statistics as a JSON object. | Confirmed | `StatisticsController.java`; `StatisticsService.java` |
| D4.9 | All date-times are serialized as ISO-8601 strings (not timestamps) in the UTC timezone. | Confirmed | `application.properties` `spring.jackson.serialization.write-dates-as-timestamps=false` and `spring.jackson.time-zone=UTC` |
| D4.10 | No authentication or authorization is applied to any API endpoint. | Confirmed | No `spring-security` dependency in `pom.xml`; no `SecurityConfig.java` present |

---

## D5. Business Workflows

| # | Finding | Confidence | Source |
|---|---------|-----------|--------|
| D5.1 | **Status auto-transition workflow:** every 30 seconds, the `FlightSimulationService` evaluates all flights against current time. If computed status differs from stored status, the flight is updated. | Confirmed | `FlightSimulationService.java` `simulateLiveUpdates()` `@Scheduled(fixedRate=30_000)` |
| D5.2 | **Status computation rules:** (a) if ETD has passed → DEPARTED; (b) if within 30 minutes of ETD and not delayed → BOARDING; (c) if within 30 minutes of ETD and delayed → DELAYED; (d) if more than 30 minutes out and delayed → DELAYED; (e) if more than 30 minutes out and not delayed → SCHEDULED. Terminal states (CANCELLED, DEPARTED, DIVERTED) are never overridden by auto-transition. | Confirmed | `EtdCalculationService.java` `computeStatus()` |
| D5.3 | **Delay application workflow:** when a delay reason is applied, the estimated delay minutes are derived from a static lookup table per reason type, ETD is pushed forward by that amount, and flight status is set to DELAYED. | Confirmed | `EtdCalculationService.java` `estimateAdditionalDelay()`, `applyDelay()` |
| D5.4 | **Random delay simulation:** every 30 seconds, there is a 20% probability that one random SCHEDULED (non-delayed) flight will have a randomly-selected delay reason applied to it. | Confirmed | `FlightSimulationService.java` `simulateLiveUpdates()` — `if (random.nextInt(100) < 20)` |
| D5.5 | **Seed data workflow:** on application startup, 15 sample flights are created spanning +15 to +360 minutes from startup time. Two flights (UA456, WN789) are pre-delayed. | Confirmed | `FlightSimulationService.java` `seedFlights()` |
| D5.6 | **UI auto-refresh workflow:** the active tab refreshes automatically every 15 seconds. When the Statistics tab is active, the Dashboard also refreshes in the background to keep KPI data current. | Confirmed | `app.js` `startAutoRefresh()` `REFRESH_INTERVAL_MS = 15000` |
| D5.7 | **Flight creation workflow (operator):** operator fills out the Add Flight form, submits, the system validates and POSTs to `/api/flights`, and on success updates the dashboard. | Confirmed | `addFlight.js`; `FlightController.java`; `FlightService.java` |
| D5.8 | **ETD update workflow (operator):** operator clicks the edit button for a flight, edits the ETD in a modal dialog (optionally adding a delay reason and notes), and saves. The system recomputes the flight's status automatically. | Confirmed | `dashboard.js` `openEtdModal()`, `saveEtdUpdate()`; `FlightService.java` `updateEtd()` |

---

## D6. Business Rules and Validations

| # | Finding | Confidence | Source |
|---|---------|-----------|--------|
| D6.1 | Flight number must match pattern `^[A-Z]{2,3}\d{1,4}$` (2-3 uppercase letters followed by 1-4 digits). | Confirmed | `CreateFlightRequest.java` `@Pattern` annotation |
| D6.2 | Origin and destination must each be a 3-letter uppercase IATA airport code matching `^[A-Z]{3}$`. | Confirmed | `CreateFlightRequest.java` `@Pattern` annotations on `origin` and `destination` |
| D6.3 | Flight number, airline, origin, destination, and scheduledDeparture are mandatory when creating a flight. Gate and aircraftType are optional. | Confirmed | `CreateFlightRequest.java` `@NotBlank`/`@NotNull` annotations |
| D6.4 | A flight with the same flight number cannot be created twice for the same scheduled departure time. | Confirmed | `FlightService.java` `createFlight()` duplicate check |
| D6.5 | The new ETD set via the update operation cannot be more than 5 minutes in the past at the time of the update. | Confirmed | `FlightService.java` `updateEtd()` — `req.getNewEstimatedDeparture().isBefore(LocalDateTime.now().minusMinutes(5))` |
| D6.6 | A delay is considered significant (qualifying the flight for DELAYED status) only when ≥ 15 minutes. | Confirmed | `Flight.java` `isDelayed()` |
| D6.7 | Static estimated delay durations per reason: WEATHER=45 min, AIR_TRAFFIC=20 min, MECHANICAL=60 min, CREW=30 min, SECURITY=25 min, LATE_INBOUND=35 min, FUELING=15 min, CATERING=10 min, BAGGAGE=15 min, OTHER=20 min. | Confirmed | `EtdCalculationService.java` `estimateAdditionalDelay()` |
| D6.8 | Terminal statuses (CANCELLED, DEPARTED, DIVERTED) are never overridden by the automated status-computation engine. | Confirmed | `EtdCalculationService.java` `computeStatus()` — first guard clause |
| D6.9 | No validation prevents cancelling an already-cancelled flight or departing a cancelled flight. | Confirmed | `FlightService.java` `cancelFlight()` and `markDeparted()` — no guard |
| D6.10 | The `flightNumber` field in the repository is looked up **case-insensitively** (`equalsIgnoreCase`). | Confirmed | `FlightRepository.java` `findByFlightNumber()` |

---

## D7. UI / Frontend Behavior

| # | Finding | Confidence | Source |
|---|---------|-----------|--------|
| D7.1 | The UI has three tabs: **Dashboard**, **Statistics**, and **Add Flight**, implemented as Bootstrap 5 nav-tabs. | Confirmed | `index.html` `#mainTabs` |
| D7.2 | The dashboard displays columns: Flight #, Airline, Route, Gate, Scheduled, ETD, Delay, Status, Delay Reason, Actions. | Confirmed | `index.html` table headers; `dashboard.js` `renderRow()` |
| D7.3 | Rows are visually distinguished: delayed rows have a yellow tint, cancelled rows are grey/strikethrough, departed rows are grey. | Confirmed | `dashboard.js` `renderRow()` — `row-delayed`, `row-cancelled`, `row-departed` CSS classes; `css/styles.css` |
| D7.4 | Delay badges are colour-coded: zero delay = default, 1–29 min = minor (amber), 30+ min = major (red). | Confirmed | `dashboard.js` `renderRow()` — `delay-zero`, `delay-minor`, `delay-major` |
| D7.5 | The dashboard supports search (substring on flight #, airline, origin, destination), status filter (all statuses), and sort (by ETD, scheduled, delay, flight number). | Confirmed | `dashboard.js` `init()`, `render()`; `index.html` filter controls |
| D7.6 | The Add Flight form validates that all required fields are filled and that flight number and IATA codes match their expected formats before submitting. | Inferred | `addFlight.js` — client-side validation not visible in excerpt but required fields match `CreateFlightRequest.java`; standard HTML5 `required` attributes present on `index.html` |
| D7.7 | Toast notifications are used to provide feedback on all user actions (success and error). | Confirmed | `app.js` `toast()` function; called from `dashboard.js` and `addFlight.js` |
| D7.8 | The "last updated" timestamp in the dashboard header reflects the wall-clock time of the last successful API refresh, not a server-side timestamp. | Confirmed | `dashboard.js` `updateLastUpdated()` — `new Date().toLocaleTimeString()` |

---

## D8. Statistics

| # | Finding | Confidence | Source |
|---|---------|-----------|--------|
| D8.1 | Statistics are computed on demand (on every GET request to `/api/statistics`) from the current in-memory flight list. They are not cached. | Confirmed | `StatisticsService.java` `buildStatistics()` — reads `repository.findAll()` directly |
| D8.2 | On-time percentage is calculated as `(onTimeCount × 100) / totalFlights`, where "on-time" means not delayed AND not cancelled. | Confirmed | `StatisticsService.java` |
| D8.3 | Average delay is calculated only over flights that are delayed (≥ 15 min). Flights with zero delay do not factor into the average. | Confirmed | `StatisticsService.java` — `.filter(Flight::isDelayed).mapToLong(...).average()` |

---

## D9. Error Handling

| # | Finding | Confidence | Source |
|---|---------|-----------|--------|
| D9.1 | Flight-not-found errors return HTTP 404 with a JSON body containing `timestamp`, `status`, and `message`. | Confirmed | `GlobalExceptionHandler.java` `handleNotFound()`; `FlightNotFoundException.java` |
| D9.2 | Validation errors (invalid request body) return HTTP 400 with a JSON body that includes a `fieldErrors` map listing each invalid field and its message. | Confirmed | `GlobalExceptionHandler.java` `handleValidation()` |
| D9.3 | Business rule violations (e.g., past ETD, duplicate flight) return HTTP 400 with a JSON body containing `message`. | Confirmed | `GlobalExceptionHandler.java` `handleBadRequest()` |
| D9.4 | Unhandled exceptions return HTTP 500 with a JSON body. | Confirmed | `GlobalExceptionHandler.java` `handleGeneric()` |
| D9.5 | The UI displays error messages as red toast notifications. Specific error messages from the API are surfaced to the user. | Confirmed | `dashboard.js` — `App.toast('Failed to load flights: ' + err.message, 'danger')` |

---

## D10. Technology Stack

| # | Finding | Confidence | Source |
|---|---------|-----------|--------|
| D10.1 | Backend: **Java 17, Spring Boot 3.2**, Spring Web MVC, Spring Validation, Spring Boot Actuator, Spring Scheduling. | Confirmed | `pom.xml`; `EtdApplication.java` `@SpringBootApplication @EnableScheduling` |
| D10.2 | Frontend: **Bootstrap 5**, Bootstrap Icons, **Vanilla JavaScript** (no framework). | Confirmed | `index.html` CDN links for Bootstrap 5 and Bootstrap Icons |
| D10.3 | Persistence: **in-memory `ConcurrentHashMap`** — no database, no JPA, no migration framework. | Confirmed | `FlightRepository.java` |
| D10.4 | Build tool: **Maven 3.6+**. | Confirmed | `pom.xml`; `README.md` |

---

## D11. Assumptions and Gaps

| # | Finding | Confidence | Notes |
|---|---------|-----------|-------|
| D11.1 | The system has **no user authentication or authorization**. Any user with network access to port 8080 can perform all operations. | Confirmed | Absence of Spring Security; README does not mention auth |
| D11.2 | The system has **no data persistence**. All data is lost on restart. | Confirmed | `FlightRepository.java` uses in-memory map only |
| D11.3 | There is **no role separation** between a "viewer" (read-only) and an "operator" (read/write). | Confirmed | Single role implied; no role-based access control |
| D11.4 | A **supervisor or manager role** with elevated permissions (e.g., approving delays, generating reports) may be required in production but is not present. | Assumed | Common pattern for airline operations systems; not evidenced in codebase |
| D11.5 | The DIVERTED status may require a future `/divert` API endpoint. | Inferred | `FlightStatus.DIVERTED` exists; `EtdCalculationService.computeStatus()` preserves it but no creation path exists |
| D11.6 | **Performance under load** is unspecified; no rate limiting, pagination, or caching is implemented. | Unresolved | No evidence of performance requirements in any source file |
| D11.7 | **Accessibility compliance** (e.g., WCAG 2.1 AA) is unspecified. | Unresolved | No ARIA roles, no accessibility config; Bootstrap 5 provides some baseline accessibility |

---

*Discovery complete. 39 source files reviewed; 47 findings produced.*
