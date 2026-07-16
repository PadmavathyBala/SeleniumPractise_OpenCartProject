# Step 7 — Traceability Matrix (brd-traceability)

**Run ID:** `2026-07-16T03-46-24Z`  
**Agent:** brd-traceability  
**Input:** 06b_authoring_revised.md (revised BRD), 02_discovery.md (source citations)  
**Requirements traced:** 59 total  
**Fully traced:** 54 (Confirmed source)  
**No direct source (Inferred/Assumed):** 5

---

## Legend

| Column | Description |
|--------|-------------|
| Source Component | Java class, JS module, or config file where the behaviour originates (from 02_discovery.md) |
| Observed Behaviour | What the code does, in technical terms |
| Business Process | BP-### from 03_business_process.md |
| Business Requirement | BR-### from revised BRD |
| Functional / Other Req | FR-###, BRULE-###, NFR-###, INT-###, DATA-### |
| Acceptance Criterion | AC-### |
| Test Scenario | Proposed test scenario |

---

## Business Requirements

| Req ID | Source Component | Observed Behaviour | Business Process | Business Req | Acceptance Criterion | Test Scenario |
|--------|-----------------|-------------------|-----------------|-------------|---------------------|---------------|
| BR-001 | `FlightController.java`, `dashboard.js`, `app.js` | GET /api/flights returns all flights; UI renders them every 15 s | BP-001 | BR-001 | AC-001, AC-003 | Verify all flights appear on dashboard load; verify auto-refresh |
| BR-002 | `FlightController.java`, `FlightService.java` | POST/PUT/DELETE /api/flights endpoints available with no auth check | BP-002–BP-006 | BR-002 | AC-004–AC-010 | Create/update/cancel/depart/delete a flight; confirm changes are reflected |
| BR-003 | `FlightSimulationService.java`, `EtdCalculationService.java` | @Scheduled job evaluates computeStatus() every 30 s and persists changes | BP-008 | BR-003 | AC-011, AC-012, AC-013 | Advance system time; confirm status transitions |
| BR-004 | `DelayReason.java`, `EtdCalculationService.java`, `FlightService.java` | DelayReason enum with 10 values; applyDelay() records reason | BP-010 | BR-004 | AC-006 | Update ETD with a delay reason; confirm reason stored and reflected in stats |
| BR-005 | `StatisticsController.java`, `StatisticsService.java` | GET /api/statistics aggregates on-time%, avg delay, counts | BP-007 | BR-005 | AC-014 | Load statistics page; verify all aggregated fields |
| BR-006 | `FlightService.java` `markDeparted()` | Sets actualDeparture = LocalDateTime.now() | BP-005 | BR-006 | AC-010 | Mark flight departed; confirm actualDeparture is recorded |
| BR-007 | `CreateFlightRequest.java`, `GlobalExceptionHandler.java` | @NotBlank/@NotNull/@Pattern annotations; 400 on violation | BP-002 | BR-007 | AC-004, AC-008 | Submit invalid flight data; confirm 400 with field errors |
| BR-008 | `application.properties`, Spring Boot Actuator | management.endpoints.web.exposure.include=health,info,metrics | BP-011 | BR-008 | AC-015 | GET /actuator/health; confirm HTTP 200 with status UP |

---

## Functional Requirements — Dashboard

| Req ID | Source Component | Observed Behaviour | Business Process | Functional Req | Acceptance Criterion | Test Scenario |
|--------|-----------------|-------------------|-----------------|----------------|---------------------|---------------|
| FR-001 | `index.html`, `dashboard.js` `renderRow()` | Table with 10 columns rendered per flight | BP-001 | FR-001 | AC-001 | Load dashboard; verify all 10 column headers and data present |
| FR-002 | `dashboard.js` `updateKpis()`, `StatisticsService.java` | KPI cards updated with totalFlights, onTimeCount, etc. | BP-001, BP-007 | FR-002 | AC-001 | Load dashboard; verify 5 KPI cards populated |
| FR-003 | `FlightController.java` `listAll()`, `FlightService.java` `searchFlights()`, `dashboard.js` | ?search= param filters on flightNumber, airline, origin, destination | BP-001 | FR-003 | AC-002 | Enter "DFW" in search; only DFW flights visible |
| FR-004 | `dashboard.js` `render()` `statusFilter` | Client-side filter applied to allFlights array by status | BP-001 | FR-004 | AC-001 | Select "DELAYED" filter; only delayed flights shown |
| FR-005 | `dashboard.js` `render()` sort switch | Sort by ETD/scheduled/delay/flightNumber | BP-001 | FR-005 | AC-001 | Change sort to "Delay"; most-delayed flight appears first |
| FR-006 | `app.js` `startAutoRefresh()` `REFRESH_INTERVAL_MS = 15000` | setInterval fires refresh every 15 000 ms | BP-001 | FR-006 | AC-003 | Wait 15 s; confirm new data loaded without interaction |
| FR-025 | `dashboard.js` `render()` — `tbody.innerHTML = '<tr><td colspan="10" … >No flights match…'` | Empty-result message rendered when filtered list is empty | BP-001 | FR-025 | AC-018 | Enter search term with no matches; confirm message displayed |

---

## Functional Requirements — Flight Management

| Req ID | Source Component | Observed Behaviour | Business Process | Functional Req | Acceptance Criterion | Test Scenario |
|--------|-----------------|-------------------|-----------------|----------------|---------------------|---------------|
| FR-007 | `FlightController.java` `create()`, `CreateFlightRequest.java` | POST /api/flights with required + optional fields | BP-002 | FR-007 | AC-004, AC-005 | Create valid flight; confirm appears in dashboard |
| FR-008 | `FlightRepository.java` `save()` — `UUID.randomUUID()` | UUID assigned on first save | BP-002 | FR-008 | AC-005 | Create flight; confirm id field is non-null UUID |
| FR-009 | `FlightService.java` `createFlight()` — `setStatus(SCHEDULED)`, `setEstimatedDeparture(scheduledDeparture)` | Default status and ETD set on creation | BP-002 | FR-009 | AC-005 | Create flight; confirm status=SCHEDULED and ETD=scheduled |
| FR-010 | `FlightController.java` `updateEtd()`, `dashboard.js` `openEtdModal()` | PUT /api/flights/{id}/etd with newEstimatedDeparture, delayReason, delayNotes | BP-003 | FR-010 | AC-006, AC-007 | Open ETD modal; update time and reason; confirm saved |
| FR-011 | `FlightService.java` `updateEtd()` → `etdService.computeStatus()` | Status recomputed after every ETD update | BP-003 | FR-011 | AC-006 | Update ETD 30 min later; confirm status=DELAYED |
| FR-012 | `FlightController.java` `cancel()`, `FlightService.java` `cancelFlight()` | POST /api/flights/{id}/cancel sets status=CANCELLED | BP-004 | FR-012 | AC-009 | Cancel flight with note; confirm status=CANCELLED and note stored |
| FR-013 | `FlightController.java` `depart()`, `FlightService.java` `markDeparted()` | POST /api/flights/{id}/depart sets status=DEPARTED, actualDeparture=now() | BP-005 | FR-013 | AC-010 | Mark departed; confirm status=DEPARTED and actualDeparture set |
| FR-014 | `FlightController.java` `delete()`, `FlightService.java` `deleteFlight()` | DELETE /api/flights/{id} removes record; 204 response | BP-006 | FR-014 | AC-001 | Delete flight; confirm it no longer appears on dashboard |
| FR-015 | `dashboard.js` `renderRow()` — `row-delayed`, `row-cancelled`, `row-departed` classes | CSS classes applied based on flight.status and flight.delayed | BP-001 | FR-015 | AC-001 | Delayed flight row has amber styling; cancelled/departed have grey styling |
| FR-016 | `dashboard.js` `renderRow()` — `delay-zero`, `delay-minor`, `delay-major` | Delay badge class driven by delayMinutes thresholds | BP-001 | FR-016 | AC-001 | Flight delayed 10 min shows amber badge; 30+ min shows red badge |

---

## Functional Requirements — Statistics

| Req ID | Source Component | Observed Behaviour | Business Process | Functional Req | Acceptance Criterion | Test Scenario |
|--------|-----------------|-------------------|-----------------|----------------|---------------------|---------------|
| FR-017 | `StatisticsService.java` `buildStatistics()`, `StatisticsController.java`, `statistics.js` | All 9 statistics fields computed from in-memory flights | BP-007 | FR-017 | AC-014 | Load statistics; verify total, onTime, onTimePct, delayed, cancelled, avgDelay, byStatus, byReason, byAirline |
| FR-018 | `app.js` `startAutoRefresh()` — Statistics branch | Dashboard also refreshed when Statistics tab active | BP-007 | FR-018 | AC-003 | Switch to Statistics tab; wait 15 s; confirm data refreshes |

---

## Functional Requirements — Notifications

| Req ID | Source Component | Observed Behaviour | Business Process | Functional Req | Acceptance Criterion | Test Scenario |
|--------|-----------------|-------------------|-----------------|----------------|---------------------|---------------|
| FR-019 | `app.js` `toast()`, called from `dashboard.js`, `addFlight.js` | Bootstrap Toast shown after every action | BP-002–BP-006 | FR-019 | AC-001 | Perform any operator action; confirm toast appears and auto-dismisses |
| FR-020 | `dashboard.js` — `App.toast('Failed to load: ' + err.message)` | err.message from API surfaced in toast | BP-002–BP-006 | FR-020 | AC-017 | Trigger an API error; confirm specific error message is visible in toast |

---

## Functional Requirements — Automated Lifecycle

| Req ID | Source Component | Observed Behaviour | Business Process | Functional Req | Acceptance Criterion | Test Scenario |
|--------|-----------------|-------------------|-----------------|----------------|---------------------|---------------|
| FR-021 | `FlightSimulationService.java` `simulateLiveUpdates()` `@Scheduled(fixedRate=30_000)` | Scheduled job evaluates all flights every 30 s | BP-008 | FR-021 | AC-011, AC-012 | Verify status changes occur within 30 s of ETD threshold crossing |
| FR-022 | `EtdCalculationService.java` `computeStatus()` — first guard clause | CANCELLED/DEPARTED/DIVERTED returned unchanged | BP-008 | FR-022 | AC-013 | Cancel flight; wait 30 s; confirm status still CANCELLED |

---

## Functional Requirements — Health

| Req ID | Source Component | Observed Behaviour | Business Process | Functional Req | Acceptance Criterion | Test Scenario |
|--------|-----------------|-------------------|-----------------|----------------|---------------------|---------------|
| FR-023 | `application.properties` `management.endpoints.web.exposure.include=health` | Spring Boot Actuator exposes /actuator/health | BP-011 | FR-023 | AC-015 | GET /actuator/health; expect 200 {"status":"UP"} |
| FR-024 | `application.properties` — `include=health,info,metrics` | Metrics and info endpoints exposed | BP-011 | FR-024 | AC-015 | GET /actuator/metrics and /actuator/info; expect 200 |

---

## Business Rules

| Req ID | Source Component | Observed Behaviour | Business Process | Business Rule | Acceptance Criterion | Test Scenario |
|--------|-----------------|-------------------|-----------------|---------------|---------------------|---------------|
| BRULE-001 | `EtdCalculationService.java` `computeStatus()` — `now.isAfter(etd)` → DEPARTED | Status set to DEPARTED when ETD is past | BP-008 | BRULE-001 | AC-011 | Set ETD to past; run lifecycle job; confirm DEPARTED |
| BRULE-002 | `EtdCalculationService.java` — `now.plusMinutes(30).isAfter(etd) && !isDelayed` → BOARDING | BOARDING when within 30 min and not delayed | BP-008 | BRULE-002 | AC-012 | Set non-delayed flight ETD to now+20 min; run job; confirm BOARDING |
| BRULE-003 | `EtdCalculationService.java` — `isDelayed() && within 30 min` → DELAYED | DELAYED persists even within 30 min if delayed | BP-008 | BRULE-003 | AC-012 | Set delayed flight ETD to now+20 min; confirm stays DELAYED |
| BRULE-004 | `Flight.java` `isDelayed()` — `getDelayMinutes() >= 15` | isDelayed=true when ETD ≥ 15 min after scheduled | BP-010 | BRULE-004 | AC-006 | Set delay to 15 min; confirm isDelayed=true; set to 14 min; confirm false |
| BRULE-005 | `FlightService.java` `updateEtd()` — `isBefore(now.minusMinutes(5))` → exception | ETD rejected if more than 5 min in the past | BP-003 | BRULE-005 | AC-007 | Submit ETD = now−10 min; expect 400 error |
| BRULE-006 | `EtdCalculationService.java` `computeStatus()` — guard for CANCELLED/DIVERTED/DEPARTED | Terminal statuses not overridden | BP-008 | BRULE-006 | AC-013 | Cancel flight; run lifecycle; status stays CANCELLED |
| BRULE-007 | `CreateFlightRequest.java` `@Pattern(regexp="^[A-Z]{2,3}\\d{1,4}$")` | Regex validation on flightNumber | BP-002 | BRULE-007 | AC-004 | Submit "12AB" and "ABCDE1" as flight numbers; both rejected |
| BRULE-008 | `CreateFlightRequest.java` `@Pattern(regexp="^[A-Z]{3}$")` on origin and destination | 3-letter uppercase IATA enforced | BP-002 | BRULE-008 | AC-004 | Submit origin="DFWX"; expect 400 error |
| BRULE-009 | `FlightService.java` `createFlight()` — `findByFlightNumber(…).ifPresent(duplicate check)` | Duplicate flight number + scheduled time rejected | BP-002 | BRULE-009 | AC-008 | Create AA123 at T; re-create AA123 at T; expect 400 |
| BRULE-010 | `EtdCalculationService.java` `applyDelay()` — `etd.plusMinutes(additionalMinutes)` | ETD pushed forward by standard duration for reason | BP-010 | BRULE-010 | AC-006 | Apply WEATHER delay; confirm ETD pushed 45 min |
| BRULE-011 | `EtdCalculationService.java` `estimateAdditionalDelay()` switch | Static lookup table for 10 delay reasons | BP-010 | BRULE-011 | AC-006 | Apply each delay reason; confirm ETD change matches expected minutes |
| BRULE-012 | `StatisticsService.java` — `onTime = !isDelayed && status != CANCELLED` | On-time count excludes delayed and cancelled | BP-007 | BRULE-012 | AC-014 | Set up known mix; verify on-time count and % |
| BRULE-013 | `StatisticsService.java` — `.filter(Flight::isDelayed).mapToLong(…).average()` | Average delay only over isDelayed=true flights | BP-007 | BRULE-013 | AC-014 | Mix of delayed/on-time; verify avg delay ignores on-time flights |
| BRULE-014 | `FlightRepository.java` `findByFlightNumber()` — `equalsIgnoreCase(flightNumber)` | Duplicate check is case-insensitive | BP-002 | BRULE-014 | AC-008 | Create AA123; try creating aa123 same time; expect 400 |
| BRULE-015 | *No direct source — Unresolved* | No guard prevents cancelling a CANCELLED flight or departing a CANCELLED flight | — | BRULE-015 (Proposed) | — | Stakeholder decision required (OQ-007) |

---

## Non-Functional Requirements

| Req ID | Source Component | Observed Behaviour | Req | Acceptance Criterion | Test Scenario |
|--------|-----------------|-------------------|-----|---------------------|---------------|
| NFR-001 | `FlightRepository.java` — `ConcurrentHashMap` | Thread-safe concurrent access | NFR-001 | AC-016 | Concurrent create/update requests; verify no exceptions |
| NFR-002 | `pom.xml` — `java.version=17`, `spring-boot.version=3.2.x` | Java 17 + Spring Boot 3.2 declared in POM | NFR-002 | — | Build on Java 17; run on Java 21; confirm success |
| NFR-003 | `index.html` — Bootstrap 5 CDN, no framework deps | Single HTML file loads all deps from CDN | NFR-003 | — | Open in Chrome/Firefox/Edge; confirm UI loads without plugin |
| NFR-004 | `GlobalExceptionHandler.java` `errorBody()` | All error responses include status, message, timestamp | NFR-004 | AC-017 | Trigger 404, 400, 500; verify response shape |
| NFR-005 | *No direct source — Unresolved* | No response-time SLA defined | NFR-005 | — | Pending stakeholder confirmation (OQ-003) |
| NFR-006 | `README.md` — "mvn spring-boot:run"; `pom.xml` | Launchable from CLI | NFR-006 | — | Run `mvn spring-boot:run`; confirm http://localhost:8080 responds |
| NFR-007 | `application.properties` — `logging.level.root=INFO`, `logging.level.com.airlines.etd=DEBUG` | Log levels configured | NFR-007 | — | Run app; verify INFO events logged; DEBUG visible for etd package |
| NFR-008 | `WebConfig.java` (present; CORS config not fully reviewed) | CORS config present | NFR-008 | — | *No direct source — inferred from WebConfig.java presence* |
| NFR-009 | `GlobalExceptionHandler.java` `handleGeneric()` | Catches Exception; returns 500; no stack trace in body | NFR-009 | AC-017 | Trigger NPE via edge case; confirm 500 JSON, no stack trace |
| NFR-010 | *No direct source — Unresolved* | No pagination in FlightController | NFR-010 | — | Unresolved; no test scenario defined until requirement is adopted |

---

## Integration Requirements

| Req ID | Source Component | Observed Behaviour | Req | Acceptance Criterion | Test Scenario |
|--------|-----------------|-------------------|-----|---------------------|---------------|
| INT-001 | `FlightController.java`, `StatisticsController.java` | REST API at /api with JSON responses | INT-001 | AC-001–AC-018 | All existing flight API acceptance criteria serve as INT-001 tests |
| INT-002 | `application.properties`, Spring Boot Actuator | Actuator endpoints exposed | INT-002 | AC-015 | GET /actuator/health, /actuator/metrics; confirm responses |
| INT-003 | `EtdCalculationService.java` — clear interface, Javadoc noting "real airline system" | Service encapsulation enables future replacement | INT-003 | — | *No direct source — inferred* (design intent from Javadoc and README) |

---

## Data Requirements

| Req ID | Source Component | Observed Behaviour | Req | Acceptance Criterion | Test Scenario |
|--------|-----------------|-------------------|-----|---------------------|---------------|
| DATA-001 | `Flight.java` — all 14 fields with types | All required fields present in domain model | DATA-001 | AC-005, AC-010 | Create flight; retrieve it; verify all 14 fields present |
| DATA-002 | `application.properties` — `write-dates-as-timestamps=false`, `time-zone=UTC` | ISO-8601 UTC datetimes in JSON | DATA-002 | AC-010 | Create flight with known UTC time; verify response serialises as ISO-8601 string |
| DATA-003 | `FlightRepository.java` — `ConcurrentHashMap` with no persistence | Data cleared on restart | DATA-003 | — | Restart app; confirm no flights present |
| DATA-004 | `FlightRepository.java` `save()` — UUID.randomUUID() | UUID used as primary key | DATA-004 | AC-005 | Create flight; confirm id is a valid UUID v4 |

---

## Untraceable Requirements

The following requirements have no direct source-code citation and are marked accordingly:

| Req ID | Reason |
|--------|--------|
| BRULE-015 | Proposed/Unresolved — no guard code exists; the *absence* of code is the evidence. Stakeholder sign-off required. |
| NFR-005 | Unresolved — no performance SLA in any source file. |
| NFR-008 | Inferred — `WebConfig.java` is present but its content was not reviewed in detail during discovery. |
| NFR-010 | Unresolved — no pagination code. The absence is the finding. |
| INT-003 | Inferred — design intent documented in Javadoc and README, not a current code contract. |

---

*Traceability matrix complete. 54 requirements fully traced; 5 flagged as inferred/assumed/unresolved with no direct source.*
