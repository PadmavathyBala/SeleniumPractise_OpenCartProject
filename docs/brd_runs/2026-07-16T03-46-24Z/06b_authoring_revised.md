# Step 6b — BRD Revised Draft (brd-authoring, Mode 2)

**Run ID:** `2026-07-16T03-46-24Z`  
**Agent:** brd-authoring (revision pass)  
**Input:** 05_authoring_draft.md, 06_review_findings.md  
**Findings addressed:** 18 of 18  
**Document version:** 1.1 (Revised)

---

# Business Requirements Document
## ETD Airlines — Departure Operations System

| Field | Value |
|-------|-------|
| Document ID | BRD-ETD-001 |
| Version | 1.1 (Revised) |
| Date | 2026-07-16 |
| Run ID | 2026-07-16T03-46-24Z |
| Source | Reverse-engineered from `etd-airlines_1` codebase |
| Status | Revised — pending traceability |
| Review findings addressed | RF-001 through RF-018 (all 18) |

---

## Revision Notes (addressing 06_review_findings.md)

| Finding | Resolution |
|---------|-----------|
| RF-001 | Section 7: added explicit note that Viewer/Supervisor are future requirements; cross-referenced OOS-002 |
| RF-002 | FR-006 and FR-018: added ±1 second tolerance to auto-refresh statements |
| RF-003 | FR-011: explicit BRULE IDs listed |
| RF-004 | BRULE-005: reworded for clarity; added the boundary condition explicitly |
| RF-005 | BRULE-004: revised to "15 minutes or more" |
| RF-006 | FR-015: confidence label changed to Inferred for visual appearance; description updated |
| RF-007 | Section 19: added Mitigation column |
| RF-008 | OQ-007: promoted to proposed BRULE-015 (Unresolved); OQ-007 cross-referenced |
| RF-009 | AC-002: expanded to cover all four search fields |
| RF-010 | BR-002: "authorised" qualified with note about future auth |
| RF-011 | INT-003: reframed as an architectural constraint instead of vague design intent |
| RF-012 | NFR-005: marked Unresolved with a proposed target |
| RF-013 | BO-001: "continuously updated" replaced with "at most every 15 seconds" |
| RF-014 | DATA-003 and C-001: added cross-reference |
| RF-015 | BP-001 alternate flow and new FR-025 added for empty-results case |
| RF-016 | AC-014: clarified on-time% is exactly 60.0% |
| RF-017 | OQ-010 added re: DIVERTED status; OOS-010 cross-referenced |
| RF-018 | Section 23 placeholder instruction clarified |

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

The current system provides a self-contained reference implementation of the core departure operations workflow. It is designed to be used in an Eclipse/Maven development environment and serves as a training and demonstration platform. In a production deployment, it would be extended with persistent storage, user authentication, and integration with live data feeds.

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

- Persistent data storage (no database).
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
| Departure Operations Staff | Primary users; manage flights daily | Confirmed (primary audience of the UI) |
| Operations Management | Consumers of statistics and performance data | Inferred (statistics feature exists; no named stakeholder) |
| System Administrators / DevOps | Deploy and monitor the system | Inferred (Actuator endpoints; README deployment instructions) |
| Application Developers | Extend and maintain the system | Confirmed (README "Extending the Application") |
| Airline Training Department | Use the system for staff training | Inferred (README identifies it as a "reference application") |

---

## Section 7 — User Roles

> **Note (RF-001):** The Viewer and Supervisor roles described below are **not implemented** in the current system. They are identified as future requirements and are out of scope for the current release (see OOS-002). They are listed here to inform future design decisions.

| Role | Description | Permissions | Confidence |
|------|-------------|------------|-----------|
| Operator | Departure operations staff member who manages flights and views statistics | Full create/read/update/cancel/depart/delete access to all flights | Confirmed |
| Viewer | Read-only access to the departure board and statistics — **future requirement** | Read-only (not yet implemented) | Assumed |
| Supervisor | Elevated permissions — **future requirement** | Elevated (not yet implemented) | Assumed |
| System Monitor | External monitoring tool calling health endpoints | Read-only health/metrics access | Inferred |

---

## Section 8 — Current-State Overview

The system is a Spring Boot 3.2 application with a vanilla JavaScript single-page frontend, backed by a thread-safe in-memory repository (ConcurrentHashMap). On startup, it seeds 15 sample flights and pre-applies two delays. A scheduled job runs every 30 seconds to transition flight statuses automatically and randomly introduce additional delays.

The frontend auto-refreshes every 15 seconds, polling the REST API. The UI provides three main views: a departure board (Dashboard), a statistics page (Statistics), and a flight creation form (Add Flight).

There is no persistent storage, no authentication, and no external integrations. The system is suitable for demonstration and training use but requires the extensions listed in Section 20 for production use.

---

## Section 9 — Business Processes

| BP ID | Process Name | Actors |
|-------|-------------|--------|
| BP-001 | View Departure Board | Operator |
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

**BP-001 alternate flows (updated per RF-015):**
- Search/filter produces no results: the system displays a clear "No flights match the current filters" message instead of an empty table.
- Auto-refresh occurs every 15 seconds without operator action.

Full process descriptions are in `03_business_process.md`.

---

## Section 10 — Business Requirements

| ID | Requirement | Confidence |
|----|-------------|-----------|
| BR-001 | The system shall provide departure operations staff with a real-time view of all scheduled, active, and completed flights for the current departure session. | Confirmed |
| BR-002 | The system shall allow operations staff to create, update, cancel, and remove flight records during a departure session. Note: no authorisation control is currently implemented; access control will be enforced in a future release via role-based access control (see OOS-002). | Confirmed |
| BR-003 | The system shall maintain the current operational status of every flight and update it automatically based on scheduled and estimated departure times. | Confirmed |
| BR-004 | The system shall allow operations staff to record and categorise the reason for any flight delay, so that performance data can be aggregated for operational analysis. | Confirmed |
| BR-005 | The system shall provide aggregated departure performance statistics to support operational decision-making and management reporting. | Confirmed |
| BR-006 | The system shall record the actual departure time when a flight physically leaves the gate. | Confirmed |
| BR-007 | The system shall prevent data entry errors by validating all flight information before accepting a new flight record. | Confirmed |
| BR-008 | The system shall support monitoring of its own operational health by an external monitoring tool. | Confirmed |

---

## Section 11 — Functional Requirements

### 11.1 Dashboard and Search

| ID | Requirement | Confidence |
|----|-------------|-----------|
| FR-001 | The system shall display a departure board listing all flights with the following columns: Flight Number, Airline, Route (Origin → Destination), Gate, Scheduled Departure, Estimated Departure, Delay (minutes), Status, and Delay Reason. | Confirmed |
| FR-002 | The system shall display KPI summary cards showing: total flights, on-time count with percentage, delayed count with average delay, and cancelled count. | Confirmed |
| FR-003 | The system shall provide a free-text search on the departure board that filters flights by flight number, airline name, origin, or destination (case-insensitive substring match). | Confirmed |
| FR-004 | The system shall provide a status filter on the departure board that limits the displayed flights to a single selected status. | Confirmed |
| FR-005 | The system shall provide sort options: by Estimated Departure (default), Scheduled Departure, Delay magnitude, and Flight Number. | Confirmed |
| FR-006 | The departure board shall auto-refresh at a fixed interval of 15 seconds (±1 second tolerance) when it is the active tab. | Confirmed |
| FR-025 | When no flights match the active search term or filter, the departure board shall display a clear message indicating no results were found, rather than rendering an empty table. | Confirmed |

### 11.2 Flight Management

| ID | Requirement | Confidence |
|----|-------------|-----------|
| FR-007 | The system shall allow an operator to create a new flight record by providing: flight number, airline, origin, destination, scheduled departure, and optionally gate and aircraft type. | Confirmed |
| FR-008 | The system shall assign a system-generated unique identifier to every new flight record. | Confirmed |
| FR-009 | The system shall initialise a newly created flight with status SCHEDULED and estimated departure equal to scheduled departure. | Confirmed |
| FR-010 | The system shall allow an operator to update the estimated departure time of a flight through an inline modal dialog, with optional delay reason and free-text delay notes. | Confirmed |
| FR-011 | The system shall automatically recompute the flight status after every ETD update, applying the status transition rules defined in BRULE-001, BRULE-002, BRULE-003, BRULE-004, and BRULE-006. | Confirmed |
| FR-012 | The system shall allow an operator to cancel a flight with an optional free-text cancellation note. | Confirmed |
| FR-013 | The system shall allow an operator to mark a flight as departed, recording the system timestamp as the actual departure time. | Confirmed |
| FR-014 | The system shall allow an operator to permanently delete a flight record. | Confirmed |
| FR-015 | The system shall visually differentiate flight rows by status: delayed rows shall be highlighted in amber, cancelled and departed rows shall be displayed in muted/grey styling. | Inferred |
| FR-016 | The system shall display delay badges colour-coded by severity: no delay = neutral, 1–29 minutes = amber (minor), 30+ minutes = red (major). | Confirmed |

### 11.3 Statistics

| ID | Requirement | Confidence |
|----|-------------|-----------|
| FR-017 | The system shall provide a Statistics view displaying: total flights, on-time count, on-time percentage, delayed count, cancelled count, average delay (minutes over delayed flights only), count by status, count by delay reason, and count by airline. | Confirmed |
| FR-018 | The Statistics view shall auto-refresh at a fixed interval of 15 seconds (±1 second tolerance) when it is the active tab. | Confirmed |

### 11.4 Notifications and Feedback

| ID | Requirement | Confidence |
|----|-------------|-----------|
| FR-019 | The system shall display a transient toast notification confirming the outcome (success or failure) of every operator action. | Confirmed |
| FR-020 | The system shall surface the specific error message returned by the backend in the failure notification. | Confirmed |

### 11.5 Automated Lifecycle

| ID | Requirement | Confidence |
|----|-------------|-----------|
| FR-021 | The system shall automatically evaluate and update the status of all active flights every 30 seconds. | Confirmed |
| FR-022 | The system shall not automatically change the status of flights in terminal states (CANCELLED, DEPARTED, DIVERTED). | Confirmed |

### 11.6 Health

| ID | Requirement | Confidence |
|----|-------------|-----------|
| FR-023 | The system shall expose a health-check endpoint (`GET /actuator/health`) returning the application's operational status. | Confirmed |
| FR-024 | The system shall expose metrics and info actuator endpoints (`/actuator/metrics`, `/actuator/info`) for operational monitoring. | Confirmed |

---

## Section 12 — Business Rules

| ID | Rule | Confidence |
|----|------|-----------|
| BRULE-001 | A flight whose ETD has passed the current time shall automatically transition to DEPARTED. | Confirmed |
| BRULE-002 | A flight that is not delayed and whose ETD is within 30 minutes of the current time shall transition to BOARDING. | Confirmed |
| BRULE-003 | A delayed flight whose ETD is within 30 minutes of the current time shall remain DELAYED (not transition to BOARDING). | Confirmed |
| BRULE-004 | A flight is classified as delayed when its estimated departure is **15 minutes or more** later than its scheduled departure. | Confirmed |
| BRULE-005 | A new ETD shall not be set to a value more than 5 minutes before the system time at the moment of the update request. ETD values between (now − 5 minutes) inclusive and any future time are accepted. | Confirmed |
| BRULE-006 | Terminal statuses (CANCELLED, DEPARTED, DIVERTED) shall not be overridden by the automated lifecycle engine. | Confirmed |
| BRULE-007 | Flight number must match: 2–3 uppercase letters followed by 1–4 digits (e.g., AA123, UAL1234). | Confirmed |
| BRULE-008 | Origin and destination must each be exactly 3 uppercase letters (IATA code format, e.g., DFW, LAX). | Confirmed |
| BRULE-009 | A flight number may not be registered twice for the same scheduled departure date and time. The duplicate check is case-insensitive. | Confirmed |
| BRULE-010 | When a delay reason is applied, the standard estimated delay duration for that reason is added to the current ETD. | Confirmed |
| BRULE-011 | Standard delay durations: Weather=45 min; ATC=20 min; Mechanical=60 min; Crew=30 min; Security=25 min; Late Inbound=35 min; Fueling=15 min; Catering=10 min; Baggage=15 min; Other=20 min. Note: these are static estimates; production systems should derive durations from operational data. | Confirmed |
| BRULE-012 | On-time performance percentage = (count of flights that are not delayed AND not cancelled) ÷ total flights × 100. | Confirmed |
| BRULE-013 | Average delay is computed only over flights with a delay of 15 minutes or more. On-time and cancelled flights are excluded. | Confirmed |
| BRULE-014 | Flight number duplicate checks shall be case-insensitive. | Confirmed |
| BRULE-015 *(Proposed — Unresolved)* | The system should prevent an operator from marking a CANCELLED flight as DEPARTED, or from re-cancelling an already-CANCELLED flight. See OQ-007. | Unresolved |

---

## Section 13 — Data Requirements

| ID | Requirement | Confidence |
|----|-------------|-----------|
| DATA-001 | Each flight record shall contain: UUID, flight number, airline, origin (IATA), destination (IATA), gate (optional), aircraft type (optional), scheduled departure (UTC datetime), estimated departure (UTC datetime), actual departure (UTC datetime, nullable), status (enum), delay reason (enum, nullable), delay notes (text, nullable), last updated (UTC datetime). | Confirmed |
| DATA-002 | All datetime values shall be stored and transmitted in UTC, serialised as ISO-8601 strings. | Confirmed |
| DATA-003 | Flight data is retained in memory for the current application session only; it is not persisted across application restarts. See also C-001. | Confirmed |
| DATA-004 | Each flight record is uniquely identified by a system-generated UUID. | Confirmed |

---

## Section 14 — Integration Requirements

| ID | Requirement | Confidence |
|----|-------------|-----------|
| INT-001 | The system shall expose a RESTful JSON API at path prefix `/api` to support integration with other operational systems or client applications. | Confirmed |
| INT-002 | The system shall expose Actuator endpoints (`/actuator/health`, `/actuator/info`, `/actuator/metrics`) for integration with external health-monitoring platforms. | Confirmed |
| INT-003 | The ETD calculation logic shall be encapsulated in a dedicated service component (`EtdCalculationService`) with a clear interface, enabling future replacement of static delay estimates with live weather, ATC, and aircraft-tracking data feeds. | Confirmed |

---

## Section 15 — Non-Functional Requirements

| ID | Requirement | Confidence |
|----|-------------|-----------|
| NFR-001 | Concurrent read/write operations on the flight repository shall not produce data corruption or race conditions. | Confirmed |
| NFR-002 | The backend shall be implemented using Java 17 or later and Spring Boot 3.2 or later. | Confirmed |
| NFR-003 | The frontend shall function in a modern web browser without requiring any browser plugin or local installation beyond the browser itself. | Confirmed |
| NFR-004 | All API error responses shall include a machine-readable `status` code, a human-readable `message`, and a `timestamp`. | Confirmed |
| NFR-005 | The maximum acceptable health-check response time has not been formally defined. A target of 2 seconds at the 95th percentile is proposed; stakeholder confirmation is required before this target is adopted as a binding requirement. | Unresolved |
| NFR-006 | The system shall be launchable via `mvn spring-boot:run` on any machine with Java 17+ and Maven 3.6+. | Confirmed |
| NFR-007 | Application events shall be logged at INFO level; ETD-system-specific events at DEBUG level. | Confirmed |
| NFR-008 | The system shall apply a CORS policy controlling which origins may access the API. | Inferred |
| NFR-009 | Unhandled exceptions shall return a structured error response to API clients; stack traces shall not be exposed. | Confirmed |
| NFR-010 | API pagination is not currently implemented. Its requirement for production-scale use is unresolved and shall be addressed before production deployment. | Unresolved |

---

## Section 16 — Assumptions

| # | Assumption | Confidence |
|---|------------|-----------|
| A-001 | The system serves a single terminal's departure operations; multi-terminal support is not required for the current scope. | Inferred |
| A-002 | All users of the system in its current form are trusted operators; authentication is deferred to a future enhancement (see OOS-002). | Inferred |
| A-003 | The reference/training application will be extended with persistent storage before production use (see OOS-001). | Inferred |
| A-004 | Delay duration estimates (BRULE-011) are placeholders; production versions will derive durations from operational data. | Confirmed |
| A-005 | The server clock is the authoritative time source for all ETD calculations. | Inferred |

---

## Section 17 — Constraints

| # | Constraint | Mitigation | Confidence |
|---|------------|-----------|-----------|
| C-001 | Data is non-persistent; application restart clears all flight data. See also DATA-003. | Swap in-memory repository for Spring Data JPA + database before production deployment. | Confirmed |
| C-002 | No user authentication or authorisation is implemented; any user with network access to port 8080 can perform all operations. | Implement Spring Security with role-based access control before production deployment (see OOS-002). | Confirmed |
| C-003 | No external integrations exist; delay durations are based on static estimates. | Integrate live weather/ATC feeds via EtdCalculationService interface (see INT-003, OOS-003). | Confirmed |
| C-004 | The DIVERTED flight status has no creation path through the API in the current implementation. | Add a `/divert` endpoint in a future release (see OOS-010, OQ-010). | Confirmed |

---

## Section 18 — Dependencies

| # | Dependency | Confidence |
|---|-----------|-----------|
| DEP-001 | Java 17 or later runtime environment. | Confirmed |
| DEP-002 | Maven 3.6 or later build tool. | Confirmed |
| DEP-003 | Spring Boot 3.2 (spring-boot-starter-web, spring-boot-starter-validation, spring-boot-starter-actuator). | Confirmed |
| DEP-004 | Bootstrap 5 and Bootstrap Icons (CDN). | Confirmed |
| DEP-005 | Network connectivity to Bootstrap CDN for the frontend to load correctly. | Inferred |

---

## Section 19 — Risks

| # | Risk | Likelihood | Impact | Mitigation | Confidence |
|---|------|-----------|--------|-----------|-----------|
| R-001 | Data loss on application restart; in-memory store provides no recovery. | High | High | Replace with persistent database before production (C-001). | Confirmed |
| R-002 | No authentication means any network user can modify or delete all flight data. | High | High | Implement Spring Security with RBAC before production deployment (C-002, OOS-002). | Confirmed |
| R-003 | Static delay estimates may not reflect real-world conditions; operational decisions based on these may be inaccurate. | High | Medium | Replace with live data feed integration (C-003, INT-003). | Confirmed |
| R-004 | No pagination could cause performance degradation with a large number of flight records. | Medium | Medium | Implement API pagination and investigate caching strategies (NFR-010). | Inferred |
| R-005 | The DIVERTED status is unreachable by operators through any current API endpoint. | Medium | Medium | Add `/divert` endpoint in a future release (C-004, OQ-010). | Confirmed |

---

## Section 20 — Out-of-Scope Items

| # | Item |
|---|------|
| OOS-001 | Persistent database storage (e.g., Spring Data JPA + PostgreSQL) |
| OOS-002 | User authentication and role-based access control (e.g., Spring Security) — see RF-001 |
| OOS-003 | Integration with live weather, ATC, and aircraft-tracking data feeds |
| OOS-004 | Multi-terminal or multi-airport management |
| OOS-005 | Passenger notification systems (SMS, email, display boards) |
| OOS-006 | Mobile application |
| OOS-007 | Regulatory or compliance reporting |
| OOS-008 | Real-time push notifications (WebSockets, SSE) replacing the polling model |
| OOS-009 | Audit log of ETD changes (who changed what and when) |
| OOS-010 | Divert flight functionality (see OQ-010: DIVERTED status exists in the model but no `/divert` endpoint is implemented) |

---

## Section 21 — Open Questions

| # | Question | Priority | Confidence |
|---|----------|---------|-----------|
| OQ-001 | Who are the named business stakeholders responsible for approving this BRD? | High | Unresolved |
| OQ-002 | Are there named user roles beyond a generic "Operator" (e.g., Gate Agent, Supervisor, Viewer)? | High | Unresolved |
| OQ-003 | What are the SLA or response-time requirements for production deployment? | High | Unresolved |
| OQ-004 | Is there a planned timeline for migrating from in-memory to persistent storage? | High | Unresolved |
| OQ-005 | Are there accessibility compliance requirements (e.g., WCAG 2.1 AA)? | Medium | Unresolved |
| OQ-006 | Should the DIVERTED status be supported through a dedicated operator action (`/depart` equivalent for diversions)? | Medium | Unresolved |
| OQ-007 | Should the system prevent re-cancelling an already-CANCELLED flight, or prevent marking a CANCELLED flight as DEPARTED? (See proposed BRULE-015.) | Medium | Unresolved |
| OQ-008 | Are there requirements for an audit trail of ETD changes (who changed what and when)? | Medium | Unresolved |
| OQ-009 | Should delay duration estimates (BRULE-011) be administrator-configurable rather than hard-coded? | Low | Inferred |
| OQ-010 | The DIVERTED status is defined in the system (FlightStatus enum) but cannot be set via any current API endpoint. Should a `/divert` endpoint be added in the next release? (See OOS-010, C-004.) | Medium | Unresolved |

---

## Section 22 — Acceptance Criteria

| AC ID | Links to | Criterion | Confidence |
|-------|---------|-----------|-----------|
| AC-001 | FR-001, FR-002 | Given the system has flights, when the operator views the Dashboard, then all flights are displayed in rows with all required columns populated, and KPI cards reflect current counts. | Confirmed |
| AC-002 | FR-003 | Given the operator enters a search term, then only flights where the flight number, airline name, origin airport code, or destination airport code contains the term (case-insensitive) are displayed. Example: entering "DFW" shows all flights where origin, destination, airline, or flight number contains "DFW"; entering "AA" shows all flights containing "AA" in any of those fields. | Confirmed |
| AC-003 | FR-006, FR-018 | Given the operator is on the Dashboard (or Statistics) tab, when 15 seconds (±1 s) elapse, then the flight list refreshes automatically without operator interaction. | Confirmed |
| AC-004 | FR-007, BRULE-007, BRULE-008 | Given the operator submits a flight with flight number "ABCD12" (four letters — invalid format), then the system rejects the creation with a validation error stating the flight number format requirement. | Confirmed |
| AC-005 | FR-009, BR-003 | Given a new flight is created, then its initial status is SCHEDULED and its ETD equals its scheduled departure time. | Confirmed |
| AC-006 | FR-010, FR-011, BRULE-004, BRULE-005 | Given an operator updates a flight's ETD to 30 minutes later than scheduled, then the flight status changes to DELAYED and delay minutes = 30. | Confirmed |
| AC-007 | BRULE-005 | Given an operator submits a new ETD that is more than 5 minutes before the current system time, then the system rejects the update with an error message stating the constraint. | Confirmed |
| AC-008 | BRULE-009, BRULE-014 | Given flight AA123 exists with scheduled departure at time T, when an operator creates flight "aa123" (lowercase) with the same scheduled departure T, then the system rejects the creation as a duplicate (case-insensitive check). | Confirmed |
| AC-009 | FR-012 | Given an operator cancels a flight with the note "crew unavailable", then the flight status changes to CANCELLED and the note is stored and retrievable. | Confirmed |
| AC-010 | FR-013 | Given an operator marks a flight as departed, then the flight status changes to DEPARTED and the actual departure timestamp is recorded as a UTC ISO-8601 value. | Confirmed |
| AC-011 | FR-021, BRULE-001 | Given a flight whose ETD has passed the current system time, when the 30-second lifecycle job runs, then the flight status is set to DEPARTED. | Confirmed |
| AC-012 | FR-021, BRULE-002 | Given a non-delayed flight whose ETD is 20 minutes from now, when the lifecycle job runs, then the flight status is BOARDING. | Confirmed |
| AC-013 | FR-021, BRULE-006 | Given a CANCELLED flight, when the lifecycle job runs, then the flight status remains CANCELLED. | Confirmed |
| AC-014 | FR-017, BRULE-012, BRULE-013 | Given 10 flights where 3 are delayed and 1 is cancelled, the statistics page shows: total=10, on-time count=6, on-time percentage=60.0%, delayed count=3, cancelled count=1, and the average delay is computed only over the 3 delayed flights. | Confirmed |
| AC-015 | FR-023 | Given the system is running, a GET to `/actuator/health` returns HTTP 200 with a JSON body containing `status: "UP"`. | Confirmed |
| AC-016 | NFR-001 | Given concurrent API requests to create and update flights, no data corruption or concurrency exception occurs. | Confirmed |
| AC-017 | NFR-009 | Given an unexpected server error, the API returns HTTP 500 with a JSON body containing `status`, `message`, and `timestamp`; no stack trace is included in the response body. | Confirmed |
| AC-018 | FR-025 | Given the operator enters a search term that matches no flights, then the departure board displays a "No flights match the current filters" message instead of an empty table. | Confirmed |

---

## Section 23 — Traceability Matrix

*This section will be replaced with the full traceability matrix from `07_traceability_matrix.md` in the final assembled BRD (`08_final_BRD.md`). The matrix links each source-code component → observed behaviour → business process → business requirement → functional requirement → acceptance criterion → test scenario for all requirements in this document.*

See `07_traceability_matrix.md` for the complete matrix.

---

*BRD revision complete. All 18 review findings addressed. Version 1.1 saved to 06b_authoring_revised.md.*
