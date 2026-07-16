# Step 8 — Final BRD (brd-workflow-orchestrator, Validate + Assemble)

**Run ID:** `2026-07-16T03-46-24Z`  
**Agent:** brd-workflow-orchestrator (Validate step)

---

## Orchestrator Validation Report (Step 8)

### 8.1 — Review Findings Verification

All 18 findings from `06_review_findings.md` have been checked against the revised BRD (`06b_authoring_revised.md`):

| Finding | Addressed? | Evidence |
|---------|-----------|---------|
| RF-001 | ✅ Yes | Section 7 now contains explicit note that Viewer/Supervisor are future requirements; cross-reference to OOS-002 present |
| RF-002 | ✅ Yes | FR-006 and FR-018 now read "15 seconds (±1 second tolerance)" |
| RF-003 | ✅ Yes | FR-011 now lists BRULE-001, BRULE-002, BRULE-003, BRULE-004, BRULE-006 explicitly |
| RF-004 | ✅ Yes | BRULE-005 now states boundary: "(now − 5 minutes) inclusive and any future time are accepted" |
| RF-005 | ✅ Yes | BRULE-004 now says "15 minutes or more" |
| RF-006 | ✅ Yes | FR-015 confidence changed to Inferred; "strike-through" removed; CSS class names referenced |
| RF-007 | ✅ Yes | Section 19 now has a Mitigation column with remediation actions for all five risks |
| RF-008 | ✅ Yes | BRULE-015 (Proposed — Unresolved) added; OQ-007 cross-referenced |
| RF-009 | ✅ Yes | AC-002 now covers all four search fields with an explicit example |
| RF-010 | ✅ Yes | BR-002 no longer uses "authorised" without qualification; note about future RBAC added |
| RF-011 | ✅ Yes | INT-003 reframed as architectural encapsulation constraint with specific class reference |
| RF-012 | ✅ Yes | NFR-005 marked Unresolved with proposed 2 s target and stakeholder confirmation note |
| RF-013 | ✅ Yes | BO-001 now reads "updated at most every 15 seconds under normal operating conditions" |
| RF-014 | ✅ Yes | DATA-003 cross-references C-001; C-001 cross-references DATA-003 |
| RF-015 | ✅ Yes | FR-025 added; BP-001 alternate flow updated with empty-results case; AC-018 added |
| RF-016 | ✅ Yes | AC-014 now explicitly states on-time%=60.0% |
| RF-017 | ✅ Yes | OQ-010 added re: DIVERTED status; OOS-010 cross-references OQ-010 |
| RF-018 | ✅ Yes | Section 23 placeholder clarified with instruction to embed matrix in final BRD |

**Unresolved review findings: 0**

---

### 8.2 — Traceability Matrix Verification

All requirement IDs in `07_traceability_matrix.md` have been checked against the revised BRD:

| Requirement Category | IDs in BRD | IDs in Matrix | Mismatches |
|----------------------|-----------|---------------|-----------|
| BR (Business Requirements) | BR-001 to BR-008 | BR-001 to BR-008 | None |
| FR (Functional Requirements) | FR-001 to FR-025 (with gaps: no FR-026+) | FR-001 to FR-025 | None |
| BRULE (Business Rules) | BRULE-001 to BRULE-015 | BRULE-001 to BRULE-015 | None |
| NFR | NFR-001 to NFR-010 | NFR-001 to NFR-010 | None |
| INT | INT-001 to INT-003 | INT-001 to INT-003 | None |
| DATA | DATA-001 to DATA-004 | DATA-001 to DATA-004 | None |

**Mismatches found: 0**

---

### 8.3 — Summary

- **Run ID:** `2026-07-16T03-46-24Z`
- **All 7 specialist agents invoked (8 total invocations):** brd-planning, brd-discovery, brd-business-process, brd-requirements-extraction, brd-authoring ×2 (Mode 1: draft; Mode 2: revision), brd-review-reflect, brd-traceability ✅
- **Review findings resolved:** 18/18 ✅
- **Traceability mismatches:** 0 ✅
- **Unresolved items (by design):** 5 requirements remain Unresolved pending stakeholder input (BRULE-015, NFR-005, NFR-010, and open questions OQ-001 through OQ-010). These are documented transparently and do not represent pipeline failures.
- **Status:** VALIDATED

---

---

# Final Business Requirements Document
## ETD Airlines — Departure Operations System

| Field | Value |
|-------|-------|
| Document ID | BRD-ETD-001 |
| Version | 1.1 Final |
| Date | 2026-07-16 |
| Run ID | 2026-07-16T03-46-24Z |
| Source | Reverse-engineered from `etd-airlines_1` codebase |
| Status | Final |

---

## Section 1 — Document Purpose

This Business Requirements Document (BRD) reverse-engineers the business requirements of the **ETD Airlines Departure Operations System** from its source code. The document is intended to:

1. Provide a complete record of what the system currently does, expressed in business language.
2. Surface implicit business rules and workflows that exist in the codebase but were not previously documented.
3. Identify gaps, assumptions, and open questions that require stakeholder validation.
4. Serve as a baseline for planning enhancements, testing, and compliance reviews.

All requirements carry a confidence label — **Confirmed**, **Inferred**, **Assumed**, or **Unresolved** — reflecting the degree of evidence available in the codebase.

---

## Section 2 — Executive Summary

The ETD Airlines Departure Operations System is a web-based application that enables departure operations staff at an airline terminal to manage the lifecycle of flights from initial scheduling through actual departure. The system provides a real-time departure board, the ability to update estimated departure times, categorise delays, cancel or mark flights as departed, and view aggregated performance statistics.

The current implementation is a reference/training application built with Spring Boot 3.2 and a vanilla HTML/JavaScript frontend. It uses an in-memory data store and includes a simulation engine that periodically introduces realistic delays and status transitions for demonstration purposes.

Key capabilities:
- Real-time departure board with search, filter, and sort
- Flight management: create, update ETD, cancel, depart, delete
- Delay categorisation across 10 standard delay reason types
- Automated status lifecycle management (scheduled → boarding → departed)
- Aggregated statistics: on-time performance, average delay, breakdowns by status/reason/airline
- Spring Boot Actuator health and metrics endpoints

---

## Section 3 — Business Background

Airline departure operations require continuous monitoring and management of scheduled flights. Estimated Time of Departure (ETD) is a critical data point communicated to passengers, gate staff, ground crews, and connecting operations. Delays must be tracked and categorised not only for immediate operational purposes (informing passengers and adjusting gate assignments) but also for longer-term performance analysis and regulatory reporting.

The current system provides a self-contained reference implementation of the core departure operations workflow. It is designed for use in an Eclipse/Maven development environment and serves as a training and demonstration platform. In a production deployment, it would be extended with persistent storage, user authentication, and integration with live data feeds.

---

## Section 4 — Business Objectives

| # | Objective | Confidence |
|---|-----------|-----------|
| BO-001 | Provide departure operations staff with an automatically refreshed view of all flight departures, updated at most every 15 seconds under normal operating conditions. | Confirmed |
| BO-002 | Enable staff to record, categorise, and communicate flight delays in a timely manner. | Confirmed |
| BO-003 | Automate routine flight status transitions to reduce manual update burden. | Confirmed |
| BO-004 | Provide operational management with departure performance statistics. | Confirmed |
| BO-005 | Serve as a reference implementation for building a production-ready departure management system. | Confirmed |

---

## Section 5 — Scope

### In Scope

- Management of flight records for a single departure session (in-memory, non-persistent).
- Departure board with real-time status, ETD updates, delay categorisation.
- Flight CRUD operations: create, read, update (ETD), cancel, depart, delete.
- Automated status lifecycle engine (30-second interval).
- Aggregated statistics for the current session's flights.
- RESTful JSON API for all operations.
- Single-page web frontend (Dashboard, Statistics, Add Flight tabs).
- Spring Boot Actuator health and metrics endpoints.

### Out of Scope (see Section 20)

- Persistent data storage.
- User authentication and role-based access control.
- Integration with live weather, ATC, or aircraft-tracking systems.
- Multi-terminal or multi-airport management.
- Passenger notifications or communications.
- Mobile application.
- Regulatory reporting.
- Divert flight functionality (see OOS-010 and OQ-010).

---

## Section 6 — Stakeholders

| Role | Interest | Confidence |
|------|---------|-----------|
| Departure Operations Staff | Primary users; manage flights daily | Confirmed |
| Operations Management | Consumers of statistics and performance data | Inferred |
| System Administrators / DevOps | Deploy and monitor the system | Inferred |
| Application Developers | Extend and maintain the system | Confirmed |
| Airline Training Department | Use the system for staff training | Inferred |

---

## Section 7 — User Roles

> **Note:** The Viewer and Supervisor roles below are **not implemented** in the current system. They are identified as future requirements and are out of scope for the current release (see OOS-002).

| Role | Description | Permissions | Confidence |
|------|-------------|------------|-----------|
| Operator | Departure operations staff member who manages flights and views statistics | Full create/read/update/cancel/depart/delete — no access control enforced | Confirmed |
| Viewer | Read-only access to departure board and statistics — **future requirement** | Read-only (not yet implemented) | Assumed |
| Supervisor | Elevated permissions — **future requirement** | Elevated (not yet implemented) | Assumed |
| System Monitor | External monitoring tool calling health endpoints | Read-only health/metrics | Inferred |

---

## Section 8 — Current-State Overview

The system is a Spring Boot 3.2 application with a vanilla JavaScript single-page frontend, backed by a thread-safe in-memory repository (ConcurrentHashMap). On startup, 15 sample flights are seeded and two are pre-delayed. A scheduled job runs every 30 seconds to transition flight statuses automatically and randomly introduce additional delays (20% probability per cycle).

The frontend auto-refreshes every 15 seconds by polling the REST API. Three main views are provided: Dashboard, Statistics, and Add Flight.

There is no persistent storage, no authentication, and no external integrations. The system is suitable for demonstration and training but requires the enhancements in Section 20 before production use.

---

## Section 9 — Business Processes

| BP ID | Process Name | Actors |
|-------|-------------|--------|
| BP-001 | View Departure Board (including empty-results alternate flow) | Operator |
| BP-002 | Register a New Flight | Operator |
| BP-003 | Update Estimated Departure Time (ETD) | Operator |
| BP-004 | Cancel a Flight | Operator |
| BP-005 | Mark a Flight as Departed | Operator |
| BP-006 | Remove a Flight Record | Operator |
| BP-007 | Monitor Departure Statistics | Operator, Management |
| BP-008 | Automated Status Lifecycle Management | System |
| BP-009 | Automated Delay Simulation | System |
| BP-010 | Apply a Categorised Delay | Operator / System |
| BP-011 | System Health Monitoring | System Monitor |
| BP-012 | System Initialisation with Seed Data | System |

Full process descriptions including preconditions, alternate flows, and exceptions are in `03_business_process.md`.

---

## Section 10 — Business Requirements

| ID | Requirement | Confidence |
|----|-------------|-----------|
| BR-001 | The system shall provide departure operations staff with a real-time view of all scheduled, active, and completed flights for the current departure session. | Confirmed |
| BR-002 | The system shall allow operations staff to create, update, cancel, and remove flight records. Note: no authorisation control is currently implemented; this will be enforced via RBAC in a future release (see OOS-002). | Confirmed |
| BR-003 | The system shall maintain the current operational status of every flight and update it automatically based on scheduled and estimated departure times. | Confirmed |
| BR-004 | The system shall allow operations staff to record and categorise flight delays for performance aggregation. | Confirmed |
| BR-005 | The system shall provide aggregated departure performance statistics for operational decision-making. | Confirmed |
| BR-006 | The system shall record the actual departure time when a flight leaves the gate. | Confirmed |
| BR-007 | The system shall validate all flight information before accepting a new flight record. | Confirmed |
| BR-008 | The system shall support external monitoring of its own operational health. | Confirmed |

---

## Section 11 — Functional Requirements

### 11.1 Dashboard and Search

| ID | Requirement | Confidence |
|----|-------------|-----------|
| FR-001 | Display departure board with columns: Flight Number, Airline, Route, Gate, Scheduled Departure, ETD, Delay (min), Status, Delay Reason, Actions. | Confirmed |
| FR-002 | Display KPI summary cards: total flights, on-time count + %, delayed count + avg delay, cancelled count. | Confirmed |
| FR-003 | Provide free-text search filtering on flight number, airline, origin, and destination (case-insensitive substring). | Confirmed |
| FR-004 | Provide status filter to limit displayed flights to a single selected status. | Confirmed |
| FR-005 | Provide sort options: ETD (default), Scheduled Departure, Delay magnitude, Flight Number. | Confirmed |
| FR-006 | Auto-refresh the departure board at a fixed interval of 15 seconds (±1 s) when it is the active tab. | Confirmed |
| FR-025 | Display a "No flights match the current filters" message when search or filter yields no results. | Confirmed |

### 11.2 Flight Management

| ID | Requirement | Confidence |
|----|-------------|-----------|
| FR-007 | Allow creation of a new flight with required fields (flight number, airline, origin, destination, scheduled departure) and optional fields (gate, aircraft type). | Confirmed |
| FR-008 | Assign a system-generated UUID to every new flight record. | Confirmed |
| FR-009 | Initialise new flights with status SCHEDULED and ETD equal to scheduled departure. | Confirmed |
| FR-010 | Allow ETD update via an inline modal with optional delay reason and delay notes. | Confirmed |
| FR-011 | Automatically recompute flight status after every ETD update per BRULE-001, BRULE-002, BRULE-003, BRULE-004, and BRULE-006. | Confirmed |
| FR-012 | Allow flight cancellation with an optional free-text note. | Confirmed |
| FR-013 | Allow marking a flight as departed, recording the actual departure timestamp. | Confirmed |
| FR-014 | Allow permanent deletion of a flight record. | Confirmed |
| FR-015 | Visually differentiate flight rows by status: delayed = amber, cancelled/departed = grey. | Inferred |
| FR-016 | Display colour-coded delay badges: 0 min = neutral, 1–29 min = amber, 30+ min = red. | Confirmed |

### 11.3 Statistics

| ID | Requirement | Confidence |
|----|-------------|-----------|
| FR-017 | Provide Statistics view with: total, on-time count + %, delayed count, cancelled count, avg delay, counts by status/reason/airline. | Confirmed |
| FR-018 | Auto-refresh Statistics at 15 seconds (±1 s) when it is the active tab. | Confirmed |

### 11.4 Notifications and Feedback

| ID | Requirement | Confidence |
|----|-------------|-----------|
| FR-019 | Display a transient toast notification for every operator action (success or failure). | Confirmed |
| FR-020 | Surface the specific backend error message in failure notifications. | Confirmed |

### 11.5 Automated Lifecycle

| ID | Requirement | Confidence |
|----|-------------|-----------|
| FR-021 | Automatically evaluate and update status of all active flights every 30 seconds. | Confirmed |
| FR-022 | Do not automatically change status of CANCELLED, DEPARTED, or DIVERTED flights. | Confirmed |

### 11.6 Health

| ID | Requirement | Confidence |
|----|-------------|-----------|
| FR-023 | Expose `GET /actuator/health` returning the application's operational status. | Confirmed |
| FR-024 | Expose `/actuator/metrics` and `/actuator/info` for operational monitoring. | Confirmed |

---

## Section 12 — Business Rules

| ID | Rule | Confidence |
|----|------|-----------|
| BRULE-001 | A flight whose ETD has passed the current time shall automatically transition to DEPARTED. | Confirmed |
| BRULE-002 | A not-delayed flight whose ETD is within 30 minutes of the current time shall transition to BOARDING. | Confirmed |
| BRULE-003 | A delayed flight whose ETD is within 30 minutes of the current time shall remain DELAYED. | Confirmed |
| BRULE-004 | A flight is classified as delayed when its ETD is **15 minutes or more** later than its scheduled departure. | Confirmed |
| BRULE-005 | A new ETD must not be set to more than 5 minutes before the system time. ETD values between (now − 5 min) inclusive and the future are accepted. | Confirmed |
| BRULE-006 | Terminal statuses (CANCELLED, DEPARTED, DIVERTED) shall not be overridden by the automated lifecycle engine. | Confirmed |
| BRULE-007 | Flight number must match: 2–3 uppercase letters followed by 1–4 digits. | Confirmed |
| BRULE-008 | Origin and destination must each be exactly 3 uppercase IATA letters. | Confirmed |
| BRULE-009 | A flight number cannot be registered twice for the same scheduled departure time (case-insensitive check). | Confirmed |
| BRULE-010 | When a delay reason is applied, the standard delay duration for that reason is added to the current ETD. | Confirmed |
| BRULE-011 | Standard delay durations: Weather=45; ATC=20; Mechanical=60; Crew=30; Security=25; Late Inbound=35; Fueling=15; Catering=10; Baggage=15; Other=20 minutes. (Placeholders; replace with operational data for production.) | Confirmed |
| BRULE-012 | On-time % = (non-delayed AND non-cancelled flights) ÷ total × 100. | Confirmed |
| BRULE-013 | Average delay computed only over flights with delay ≥ 15 minutes. | Confirmed |
| BRULE-014 | Duplicate flight number checks are case-insensitive. | Confirmed |
| BRULE-015 *(Proposed — Unresolved)* | The system should prevent marking a CANCELLED flight as DEPARTED or re-cancelling it. See OQ-007. | Unresolved |

---

## Section 13 — Data Requirements

| ID | Requirement | Confidence |
|----|-------------|-----------|
| DATA-001 | Each flight: UUID, flight number, airline, origin, destination, gate (opt), aircraft type (opt), scheduled departure (UTC), ETD (UTC), actual departure (UTC, nullable), status, delay reason (nullable), delay notes (nullable), last updated (UTC). | Confirmed |
| DATA-002 | All datetimes: UTC timezone, ISO-8601 string serialisation. | Confirmed |
| DATA-003 | Data is in-memory only; not persisted across restarts. See also C-001. | Confirmed |
| DATA-004 | Each record uniquely identified by a system-generated UUID. | Confirmed |

---

## Section 14 — Integration Requirements

| ID | Requirement | Confidence |
|----|-------------|-----------|
| INT-001 | RESTful JSON API at `/api` supporting all flight and statistics operations. | Confirmed |
| INT-002 | Actuator endpoints (`/actuator/health`, `/actuator/info`, `/actuator/metrics`) for external monitoring. | Confirmed |
| INT-003 | ETD calculation logic encapsulated in `EtdCalculationService` with a clear interface enabling future replacement of static estimates with live data feeds. | Confirmed |

---

## Section 15 — Non-Functional Requirements

| ID | Requirement | Confidence |
|----|-------------|-----------|
| NFR-001 | Concurrent repository operations shall not produce data corruption. | Confirmed |
| NFR-002 | Backend: Java 17+, Spring Boot 3.2+. | Confirmed |
| NFR-003 | Frontend operable in a modern web browser without plugins. | Confirmed |
| NFR-004 | All API error responses include `status`, `message`, and `timestamp`. | Confirmed |
| NFR-005 | Health-check response time SLA: **Unresolved** — proposed target 2 s at 95th percentile; requires stakeholder confirmation. | Unresolved |
| NFR-006 | Launchable via `mvn spring-boot:run` on Java 17+ with Maven 3.6+. | Confirmed |
| NFR-007 | Application events logged at INFO; etd-package events at DEBUG. | Confirmed |
| NFR-008 | CORS policy applied to API. | Inferred |
| NFR-009 | Unhandled exceptions return structured error responses; no stack traces exposed to clients. | Confirmed |
| NFR-010 | API pagination: **Unresolved** — not implemented; requirement for production scale is open. | Unresolved |

---

## Section 16 — Assumptions

| # | Assumption | Confidence |
|---|------------|-----------|
| A-001 | System serves a single terminal; multi-terminal not required. | Inferred |
| A-002 | All current users are trusted operators; authentication deferred to future release. | Inferred |
| A-003 | Persistent storage will be added before production use. | Inferred |
| A-004 | BRULE-011 delay durations are placeholders for production data. | Confirmed |
| A-005 | Server clock is the authoritative time source for all calculations. | Inferred |

---

## Section 17 — Constraints

| # | Constraint | Mitigation | Confidence |
|---|------------|-----------|-----------|
| C-001 | Data is non-persistent; restart clears all data. See DATA-003. | Integrate persistent database. | Confirmed |
| C-002 | No authentication; full access to any network-reachable user. | Implement Spring Security + RBAC. | Confirmed |
| C-003 | No external integrations; static delay estimates only. | Integrate live feeds via INT-003. | Confirmed |
| C-004 | DIVERTED status has no creation path in the current API. | Add `/divert` endpoint. | Confirmed |

---

## Section 18 — Dependencies

| # | Dependency | Confidence |
|---|-----------|-----------|
| DEP-001 | Java 17+ runtime. | Confirmed |
| DEP-002 | Maven 3.6+ build tool. | Confirmed |
| DEP-003 | Spring Boot 3.2 (web, validation, actuator). | Confirmed |
| DEP-004 | Bootstrap 5 + Bootstrap Icons (CDN). | Confirmed |
| DEP-005 | Network access to Bootstrap CDN. | Inferred |

---

## Section 19 — Risks

| # | Risk | Likelihood | Impact | Mitigation | Confidence |
|---|------|-----------|--------|-----------|-----------|
| R-001 | Data loss on restart. | High | High | Persistent DB. | Confirmed |
| R-002 | No auth: any user can modify all data. | High | High | Spring Security + RBAC before production. | Confirmed |
| R-003 | Static delay estimates inaccurate. | High | Medium | Live data feed integration. | Confirmed |
| R-004 | No pagination; performance risk at scale. | Medium | Medium | Implement pagination + caching. | Inferred |
| R-005 | DIVERTED status unreachable by operators. | Medium | Medium | Add `/divert` endpoint. | Confirmed |

---

## Section 20 — Out-of-Scope Items

| # | Item |
|---|------|
| OOS-001 | Persistent database storage |
| OOS-002 | User authentication and RBAC |
| OOS-003 | Live weather/ATC/aircraft-tracking integrations |
| OOS-004 | Multi-terminal/multi-airport management |
| OOS-005 | Passenger notification systems |
| OOS-006 | Mobile application |
| OOS-007 | Regulatory/compliance reporting |
| OOS-008 | Real-time push (WebSockets/SSE) |
| OOS-009 | ETD change audit log |
| OOS-010 | Divert flight functionality (DIVERTED status unreachable; see OQ-010) |

---

## Section 21 — Open Questions

| # | Question | Priority |
|---|----------|---------|
| OQ-001 | Who are the named stakeholders responsible for approving this BRD? | High |
| OQ-002 | Are there defined user roles beyond "Operator"? | High |
| OQ-003 | What are the SLA/response-time requirements for production? | High |
| OQ-004 | Timeline for migrating from in-memory to persistent storage? | High |
| OQ-005 | Accessibility compliance requirements (WCAG 2.1 AA)? | Medium |
| OQ-006 | Should DIVERTED be supported through a dedicated operator action? | Medium |
| OQ-007 | Should re-cancelling or departing a CANCELLED flight be prevented? (See BRULE-015.) | Medium |
| OQ-008 | Is an audit trail of ETD changes required? | Medium |
| OQ-009 | Should BRULE-011 delay durations be admin-configurable? | Low |
| OQ-010 | Should a `/divert` endpoint be added in the next release? (See C-004, OOS-010.) | Medium |

---

## Section 22 — Acceptance Criteria

| AC ID | Links to | Criterion | Confidence |
|-------|---------|-----------|-----------|
| AC-001 | FR-001, FR-002 | Dashboard shows all flights with all columns populated; KPI cards reflect current counts. | Confirmed |
| AC-002 | FR-003 | Search on "DFW" returns only flights with DFW in flight number, airline, origin, or destination; case-insensitive. | Confirmed |
| AC-003 | FR-006, FR-018 | Dashboard/Statistics auto-refreshes within 15 s (±1 s) without operator interaction. | Confirmed |
| AC-004 | FR-007, BRULE-007, BRULE-008 | Submitting flight number "ABCD12" or origin "DFWX" returns HTTP 400 with field-level validation error. | Confirmed |
| AC-005 | FR-009, BR-003 | New flight has status=SCHEDULED and ETD=scheduledDeparture. | Confirmed |
| AC-006 | FR-010, FR-011, BRULE-004, BRULE-005 | Updating ETD to +30 min results in status=DELAYED and delayMinutes=30. | Confirmed |
| AC-007 | BRULE-005 | Submitting ETD = now−10 min returns HTTP 400 with constraint message. | Confirmed |
| AC-008 | BRULE-009, BRULE-014 | Creating "aa123" at same scheduled time as existing "AA123" returns HTTP 400 (case-insensitive duplicate). | Confirmed |
| AC-009 | FR-012 | Cancelling a flight with note "crew unavailable" sets status=CANCELLED and stores note. | Confirmed |
| AC-010 | FR-013 | Marking a flight departed sets status=DEPARTED and records actualDeparture as UTC ISO-8601 timestamp. | Confirmed |
| AC-011 | FR-021, BRULE-001 | Flight with past ETD transitions to DEPARTED within 30 s of lifecycle job. | Confirmed |
| AC-012 | FR-021, BRULE-002 | Non-delayed flight with ETD=now+20 min transitions to BOARDING; delayed equivalent remains DELAYED. | Confirmed |
| AC-013 | FR-021, BRULE-006 | CANCELLED flight status unchanged after lifecycle job runs. | Confirmed |
| AC-014 | FR-017, BRULE-012, BRULE-013 | 10 flights (3 delayed, 1 cancelled) → total=10, onTime=6, onTimePct=60.0%, delayed=3, cancelled=1, avgDelay from delayed only. | Confirmed |
| AC-015 | FR-023 | GET /actuator/health returns HTTP 200 `{"status":"UP"}`. | Confirmed |
| AC-016 | NFR-001 | Concurrent create/update requests produce no data corruption or exceptions. | Confirmed |
| AC-017 | NFR-009 | Unexpected server error returns HTTP 500 JSON with status/message/timestamp; no stack trace in body. | Confirmed |
| AC-018 | FR-025 | Search with no matching flights shows "No flights match the current filters." | Confirmed |

---

## Section 23 — Traceability Matrix

*Full traceability matrix follows. Source: `07_traceability_matrix.md`.*

### 23.1 Business Requirements Traceability

| Req ID | Source Component | Observed Behaviour | Business Process | Acceptance Criterion | Test Scenario |
|--------|-----------------|-------------------|-----------------|---------------------|---------------|
| BR-001 | `FlightController.java`, `dashboard.js`, `app.js` | GET /api/flights; UI renders every 15 s | BP-001 | AC-001, AC-003 | All flights appear on load; auto-refresh occurs |
| BR-002 | `FlightController.java`, `FlightService.java` | POST/PUT/DELETE endpoints, no auth check | BP-002–BP-006 | AC-004–AC-010 | Create/update/cancel/depart/delete flights |
| BR-003 | `FlightSimulationService.java`, `EtdCalculationService.java` | @Scheduled job every 30 s; computeStatus() | BP-008 | AC-011–AC-013 | Status transitions within 30 s |
| BR-004 | `DelayReason.java`, `EtdCalculationService.java` | 10-value enum; applyDelay() records reason + adjusts ETD | BP-010 | AC-006 | Apply delay; confirm reason and ETD change |
| BR-005 | `StatisticsController.java`, `StatisticsService.java` | GET /api/statistics aggregates all metrics | BP-007 | AC-014 | Statistics page shows correct aggregated values |
| BR-006 | `FlightService.java` `markDeparted()` | actualDeparture = LocalDateTime.now() | BP-005 | AC-010 | Mark departed; actualDeparture recorded |
| BR-007 | `CreateFlightRequest.java`, `GlobalExceptionHandler.java` | Validation annotations; 400 on failure | BP-002 | AC-004, AC-008 | Invalid inputs rejected with 400 |
| BR-008 | `application.properties`, Spring Boot Actuator | health endpoint exposed | BP-011 | AC-015 | GET /actuator/health returns UP |

### 23.2 Functional Requirements Traceability

| Req ID | Source Component | Observed Behaviour | Business Process | Acceptance Criterion | Test Scenario |
|--------|-----------------|-------------------|-----------------|---------------------|---------------|
| FR-001 | `index.html`, `dashboard.js` `renderRow()` | 10-column table | BP-001 | AC-001 | All columns visible and populated |
| FR-002 | `dashboard.js` `updateKpis()` | 5 KPI cards populated | BP-001, BP-007 | AC-001 | KPI values correct after load |
| FR-003 | `FlightController.java` `listAll()`, `FlightService.java` `searchFlights()` | ?search= case-insensitive substring filter | BP-001 | AC-002 | Search returns only matching flights |
| FR-004 | `dashboard.js` `render()` statusFilter | Client-side status filter | BP-001 | AC-001 | Status filter hides non-matching rows |
| FR-005 | `dashboard.js` `render()` sort switch | 4 sort options | BP-001 | AC-001 | Sort changes row order correctly |
| FR-006 | `app.js` `REFRESH_INTERVAL_MS=15000` | setInterval 15 000 ms | BP-001 | AC-003 | New data loaded after 15 s |
| FR-007 | `FlightController.java` `create()`, `CreateFlightRequest.java` | POST with required/optional fields | BP-002 | AC-004, AC-005 | Valid flight created; invalid rejected |
| FR-008 | `FlightRepository.java` `UUID.randomUUID()` | UUID assigned on save | BP-002 | AC-005 | id field is non-null UUID |
| FR-009 | `FlightService.java` `createFlight()` | status=SCHEDULED; ETD=scheduledDeparture | BP-002 | AC-005 | Defaults confirmed on creation |
| FR-010 | `FlightController.java` `updateEtd()`, `dashboard.js` `saveEtdUpdate()` | PUT with ETD + optional reason/notes | BP-003 | AC-006, AC-007 | ETD update succeeds/fails per rules |
| FR-011 | `FlightService.java` `updateEtd()` → `computeStatus()` | Status recomputed post-update | BP-003 | AC-006 | Status changes after ETD update |
| FR-012 | `FlightController.java` `cancel()` | POST /cancel; CANCELLED status | BP-004 | AC-009 | Flight cancelled with note |
| FR-013 | `FlightController.java` `depart()` | POST /depart; DEPARTED + actualDeparture | BP-005 | AC-010 | Flight departed; timestamp recorded |
| FR-014 | `FlightController.java` `delete()` | DELETE; 204 response | BP-006 | AC-001 | Flight removed; no longer visible |
| FR-015 | `dashboard.js` `renderRow()` CSS classes | row-delayed/row-cancelled/row-departed applied | BP-001 | AC-001 | Rows visually differentiated by status |
| FR-016 | `dashboard.js` `renderRow()` delay badge classes | delay-zero/minor/major thresholds | BP-001 | AC-001 | Badges colour-coded by delay magnitude |
| FR-017 | `StatisticsService.java` `buildStatistics()` | 9 statistics fields | BP-007 | AC-014 | All stats fields correct |
| FR-018 | `app.js` Statistics branch refresh | 15 000 ms refresh when Statistics active | BP-007 | AC-003 | Stats refresh in 15 s |
| FR-019 | `app.js` `toast()`, `dashboard.js`, `addFlight.js` | Bootstrap Toast on every action | BP-002–BP-006 | AC-001 | Toast visible after each action |
| FR-020 | `dashboard.js` err.message in toast | API error message shown in toast | BP-002–BP-006 | AC-017 | Error message from API surfaces in UI |
| FR-021 | `FlightSimulationService.java` `@Scheduled(fixedRate=30_000)` | 30-second job | BP-008 | AC-011, AC-012 | Status transitions in ≤30 s |
| FR-022 | `EtdCalculationService.java` first guard clause | Terminal statuses unchanged | BP-008 | AC-013 | CANCELLED flight unchanged after job |
| FR-023 | `application.properties` actuator health | /actuator/health exposed | BP-011 | AC-015 | 200 {"status":"UP"} |
| FR-024 | `application.properties` include=metrics,info | /actuator/metrics, /actuator/info | BP-011 | AC-015 | Both endpoints return 200 |
| FR-025 | `dashboard.js` `render()` empty tbody message | Empty message when 0 filtered results | BP-001 | AC-018 | No-results message visible |

### 23.3 Business Rules Traceability

| Req ID | Source Component | Observed Behaviour | Acceptance Criterion | Test Scenario |
|--------|-----------------|-------------------|---------------------|---------------|
| BRULE-001 | `EtdCalculationService.java` — `now.isAfter(etd)` → DEPARTED | Auto-DEPARTED when ETD past | AC-011 | ETD in past; lifecycle job → DEPARTED |
| BRULE-002 | `EtdCalculationService.java` — within 30 min, not delayed → BOARDING | BOARDING rule | AC-012 | ETD=now+20; not delayed → BOARDING |
| BRULE-003 | `EtdCalculationService.java` — within 30 min, delayed → DELAYED | DELAYED persists near departure | AC-012 | ETD=now+20; delayed → DELAYED |
| BRULE-004 | `Flight.java` `isDelayed()` — `>= 15` | 15-min threshold | AC-006 | 15 min delay = delayed; 14 min = not |
| BRULE-005 | `FlightService.java` — `isBefore(now.minusMinutes(5))` | Past-ETD guard | AC-007 | ETD=now−10 → 400 error |
| BRULE-006 | `EtdCalculationService.java` guard clause | Terminal states immutable | AC-013 | CANCELLED unchanged by lifecycle |
| BRULE-007 | `CreateFlightRequest.java` `@Pattern` on flightNumber | Format validation | AC-004 | Invalid format → 400 |
| BRULE-008 | `CreateFlightRequest.java` `@Pattern` on origin/destination | IATA code validation | AC-004 | Non-IATA code → 400 |
| BRULE-009 | `FlightService.java` duplicate check | Duplicate flight+time rejected | AC-008 | Duplicate → 400 |
| BRULE-010 | `EtdCalculationService.java` `applyDelay()` | ETD pushed by standard duration | AC-006 | WEATHER applied; ETD +45 min |
| BRULE-011 | `EtdCalculationService.java` `estimateAdditionalDelay()` switch | 10-reason lookup table | AC-006 | Each reason → correct delta |
| BRULE-012 | `StatisticsService.java` on-time calculation | Excludes delayed and cancelled | AC-014 | On-time% = correct value |
| BRULE-013 | `StatisticsService.java` `.filter(isDelayed)` | Average over delayed only | AC-014 | Avg delay excludes on-time flights |
| BRULE-014 | `FlightRepository.java` `equalsIgnoreCase` | Case-insensitive lookup | AC-008 | aa123 ≡ AA123 in duplicate check |
| BRULE-015 | *No direct source — Unresolved* | No guard exists; gap confirmed | — | Stakeholder decision required (OQ-007) |

### 23.4 Non-Functional, Integration, and Data Requirements Traceability

| Req ID | Source Component | Observed Behaviour | Acceptance Criterion | Notes |
|--------|-----------------|-------------------|---------------------|-------|
| NFR-001 | `FlightRepository.java` ConcurrentHashMap | Thread-safe concurrent access | AC-016 | Confirmed |
| NFR-002 | `pom.xml` java.version=17 | Java 17 + Spring Boot 3.2 | — | Confirmed |
| NFR-003 | `index.html` CDN-only deps | Browser-native operation | — | Confirmed |
| NFR-004 | `GlobalExceptionHandler.java` `errorBody()` | Structured error response | AC-017 | Confirmed |
| NFR-005 | *No source — Unresolved* | No SLA defined | — | Pending stakeholder (OQ-003) |
| NFR-006 | README, pom.xml | CLI launchable | — | Confirmed |
| NFR-007 | `application.properties` log levels | INFO/DEBUG levels | — | Confirmed |
| NFR-008 | `WebConfig.java` (present, not fully reviewed) | CORS config present | — | Inferred |
| NFR-009 | `GlobalExceptionHandler.java` handleGeneric | 500 structured response | AC-017 | Confirmed |
| NFR-010 | *No source — Unresolved* | No pagination | — | Pending (OQ-003) |
| INT-001 | `FlightController.java`, `StatisticsController.java` | REST API | AC-001–AC-018 | Confirmed |
| INT-002 | `application.properties` actuator | Actuator endpoints | AC-015 | Confirmed |
| INT-003 | `EtdCalculationService.java` Javadoc, README | Encapsulated interface | — | Inferred (design intent) |
| DATA-001 | `Flight.java` 14 fields | All fields present | AC-005, AC-010 | Confirmed |
| DATA-002 | `application.properties` Jackson UTC + ISO-8601 | ISO-8601 UTC datetime | AC-010 | Confirmed |
| DATA-003 | `FlightRepository.java` ConcurrentHashMap | In-memory only | — | Confirmed |
| DATA-004 | `FlightRepository.java` UUID.randomUUID() | UUID primary key | AC-005 | Confirmed |

---

## Appendix — Pipeline Run Artefacts

| File | Contents |
|------|---------|
| `00_execution_log.md` | Step-by-step execution log with timestamps, agents, and statuses |
| `01_planning.md` | Analysis plan: objectives, available sources, BRD structure |
| `02_discovery.md` | 47 findings across application overview, modules, model, API, workflows, rules, UI, statistics, errors, technology, and gaps |
| `03_business_process.md` | 12 business processes in business language |
| `04_requirements_extraction.md` | Structured requirements: BR×8, FR×24+1, BRULE×15, NFR×10, INT×3, DATA×4 |
| `05_authoring_draft.md` | BRD v1.0 first draft (23 sections) |
| `06_review_findings.md` | 18 review findings from senior BA review |
| `06b_authoring_revised.md` | BRD v1.1 revised draft, all 18 findings addressed |
| `07_traceability_matrix.md` | Full traceability matrix: 54 fully traced, 5 inferred/unresolved |
| `08_final_BRD.md` | This document: validated BRD v1.1 Final with embedded traceability |

---

*Run ID `2026-07-16T03-46-24Z` complete. Status: VALIDATED. 0 unresolved review findings. 0 traceability mismatches.*
