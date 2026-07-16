# Step 5 — BRD First Draft (brd-authoring, Mode 1)

**Run ID:** `2026-07-16T03-46-24Z`  
**Agent:** brd-authoring  
**Input:** 01_planning.md, 02_discovery.md, 03_business_process.md, 04_requirements_extraction.md  
**Document version:** 1.0 — First Draft

---

# Business Requirements Document
## ETD Airlines — Departure Operations System

| Field | Value |
|-------|-------|
| Document ID | BRD-ETD-001 |
| Version | 1.0 (Draft) |
| Date | 2026-07-16 |
| Run ID | 2026-07-16T03-46-24Z |
| Source | Reverse-engineered from `etd-airlines_1` codebase |
| Status | Draft — under review |

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
| BO-001 | Provide departure operations staff with an accurate, continuously updated view of all flight departures. | Confirmed |
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

| Role | Description | Permissions | Confidence |
|------|-------------|------------|-----------|
| Operator | Departure operations staff member who manages flights and views statistics | Full create/read/update/cancel/depart/delete access to all flights | Confirmed (all UI actions available without role check) |
| Viewer | Read-only access to the departure board and statistics | Not implemented | Assumed (common requirement for airline systems; no evidence in codebase) |
| Supervisor | Elevated permissions (e.g., approving cancellations, exporting reports) | Not implemented | Assumed |
| System Monitor | External monitoring tool calling health endpoints | Read-only health/metrics access | Inferred (Actuator endpoints) |

---

## Section 8 — Current-State Overview

The system is a Spring Boot 3.2 application with a vanilla JavaScript single-page frontend, backed by a thread-safe in-memory repository (ConcurrentHashMap). On startup, it seeds 15 sample flights and pre-applies two delays. A scheduled job runs every 30 seconds to transition flight statuses automatically and randomly introduce additional delays.

The frontend auto-refreshes every 15 seconds, polling the REST API. The UI provides three main views: a departure board (Dashboard), a statistics page (Statistics), and a flight creation form (Add Flight).

There is no persistent storage, no authentication, and no external integrations. The system is suitable for demonstration and training use but requires the extensions listed in Section 20 for production use.

---

## Section 9 — Business Processes

Refer to document `03_business_process.md` for the full process descriptions. Summary:

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

---

## Section 10 — Business Requirements

| ID | Requirement | Confidence |
|----|-------------|-----------|
| BR-001 | The system shall provide departure operations staff with a real-time view of all scheduled, active, and completed flights for the current departure session. | Confirmed |
| BR-002 | The system shall allow authorised operations staff to create, update, cancel, and remove flight records during a departure session. | Confirmed |
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
| FR-006 | The departure board shall auto-refresh every 15 seconds when it is the active tab. | Confirmed |

### 11.2 Flight Management

| ID | Requirement | Confidence |
|----|-------------|-----------|
| FR-007 | The system shall allow an operator to create a new flight record by providing: flight number, airline, origin, destination, scheduled departure, and optionally gate and aircraft type. | Confirmed |
| FR-008 | The system shall assign a system-generated unique identifier to every new flight record. | Confirmed |
| FR-009 | The system shall initialise a newly created flight with status SCHEDULED and estimated departure equal to scheduled departure. | Confirmed |
| FR-010 | The system shall allow an operator to update the estimated departure time of a flight through an inline modal dialog, with optional delay reason and free-text delay notes. | Confirmed |
| FR-011 | The system shall automatically recompute the flight status after every ETD update, applying the status transition rules defined in Business Rules. | Confirmed |
| FR-012 | The system shall allow an operator to cancel a flight with an optional free-text cancellation note. | Confirmed |
| FR-013 | The system shall allow an operator to mark a flight as departed, recording the system timestamp as the actual departure time. | Confirmed |
| FR-014 | The system shall allow an operator to permanently delete a flight record. | Confirmed |
| FR-015 | The system shall visually differentiate flight rows: delayed rows highlighted in amber, cancelled rows in grey, departed rows in grey. | Confirmed |
| FR-016 | The system shall display delay badges colour-coded by severity: no delay = neutral, 1–29 minutes = amber (minor), 30+ minutes = red (major). | Confirmed |

### 11.3 Statistics

| ID | Requirement | Confidence |
|----|-------------|-----------|
| FR-017 | The system shall provide a Statistics view displaying: total flights, on-time count, on-time percentage, delayed count, cancelled count, average delay, count by status, count by delay reason, and count by airline. | Confirmed |
| FR-018 | The Statistics view shall auto-refresh every 15 seconds when it is the active tab. | Confirmed |

### 11.4 Notifications and Feedback

| ID | Requirement | Confidence |
|----|-------------|-----------|
| FR-019 | The system shall display a transient toast notification confirming the outcome (success or failure) of every operator action. | Confirmed |
| FR-020 | The system shall surface the specific error message from the backend in the failure notification. | Confirmed |

### 11.5 Automated Lifecycle

| ID | Requirement | Confidence |
|----|-------------|-----------|
| FR-021 | The system shall automatically evaluate and update the status of all active flights every 30 seconds. | Confirmed |
| FR-022 | The system shall not automatically change the status of flights in terminal states (CANCELLED, DEPARTED, DIVERTED). | Confirmed |

### 11.6 Health

| ID | Requirement | Confidence |
|----|-------------|-----------|
| FR-023 | The system shall expose a health-check endpoint (`GET /actuator/health`) returning the application's operational status. | Confirmed |
| FR-024 | The system shall expose metrics and info actuator endpoints for operational monitoring. | Confirmed |

---

## Section 12 — Business Rules

| ID | Rule | Confidence |
|----|------|-----------|
| BRULE-001 | A flight whose ETD has passed the current time shall automatically transition to DEPARTED. | Confirmed |
| BRULE-002 | A not-delayed flight whose ETD is within 30 minutes of the current time shall transition to BOARDING. | Confirmed |
| BRULE-003 | A delayed flight whose ETD is within 30 minutes of the current time shall remain DELAYED (not BOARDING). | Confirmed |
| BRULE-004 | A flight is classified as delayed when its estimated departure is ≥ 15 minutes later than its scheduled departure. | Confirmed |
| BRULE-005 | A new ETD submitted by an operator must not be more than 5 minutes earlier than the current system time. | Confirmed |
| BRULE-006 | Terminal statuses (CANCELLED, DEPARTED, DIVERTED) shall not be overridden by the automated lifecycle engine. | Confirmed |
| BRULE-007 | Flight number must match: 2–3 uppercase letters followed by 1–4 digits. | Confirmed |
| BRULE-008 | Origin and destination must each be exactly 3 uppercase letters (IATA code format). | Confirmed |
| BRULE-009 | A flight number may not be registered twice for the same scheduled departure date and time. | Confirmed |
| BRULE-010 | When a delay reason is applied, the standard estimated delay duration for that reason is added to the current ETD. | Confirmed |
| BRULE-011 | Standard delay durations: Weather=45 min; ATC=20 min; Mechanical=60 min; Crew=30 min; Security=25 min; Late Inbound=35 min; Fueling=15 min; Catering=10 min; Baggage=15 min; Other=20 min. | Confirmed |
| BRULE-012 | On-time % = (non-delayed AND non-cancelled flights) ÷ total flights × 100. | Confirmed |
| BRULE-013 | Average delay is computed only over flights with delay ≥ 15 minutes. | Confirmed |
| BRULE-014 | Flight number duplicate checks shall be case-insensitive. | Confirmed |

---

## Section 13 — Data Requirements

| ID | Requirement | Confidence |
|----|-------------|-----------|
| DATA-001 | Each flight record shall contain: UUID, flight number, airline, origin, destination, gate (optional), aircraft type (optional), scheduled departure, estimated departure, actual departure (nullable), status, delay reason (nullable), delay notes (nullable), last updated timestamp. | Confirmed |
| DATA-002 | All datetime values shall be UTC, serialised as ISO-8601 strings. | Confirmed |
| DATA-003 | Flight data is retained in memory for the current application session only; it is not persisted across restarts. | Confirmed |
| DATA-004 | Each flight record is uniquely identified by a system-generated UUID. | Confirmed |

---

## Section 14 — Integration Requirements

| ID | Requirement | Confidence |
|----|-------------|-----------|
| INT-001 | The system shall expose a RESTful JSON API at `/api` to support integration with other operational systems. | Confirmed |
| INT-002 | The system shall expose Actuator endpoints for integration with external health-monitoring platforms. | Confirmed |
| INT-003 | The system shall be designed to support future integration with live weather, ATC, and aircraft-tracking feeds to replace static delay estimation. | Inferred |

---

## Section 15 — Non-Functional Requirements

| ID | Requirement | Confidence |
|----|-------------|-----------|
| NFR-001 | Concurrent read/write operations on the flight repository shall not produce data corruption. | Confirmed |
| NFR-002 | The backend shall use Java 17+ and Spring Boot 3.2+. | Confirmed |
| NFR-003 | The frontend shall function in a modern web browser without plugins. | Confirmed |
| NFR-004 | All API error responses shall include `status`, `message`, and `timestamp` fields. | Confirmed |
| NFR-005 | The health-check endpoint shall respond within a time sufficient for external monitoring to classify the system as UP or DOWN. | Assumed |
| NFR-006 | The system shall be launchable via `mvn spring-boot:run` on Java 17+ with Maven 3.6+. | Confirmed |
| NFR-007 | Application events shall be logged at INFO level; ETD-system-specific events at DEBUG level. | Confirmed |
| NFR-008 | The system shall apply CORS policy to control cross-origin API access. | Inferred |
| NFR-009 | Unhandled exceptions shall return a structured error response; stack traces shall not be exposed to API clients. | Confirmed |
| NFR-010 | API pagination requirements for large flight lists are unresolved and shall be addressed before production deployment. | Unresolved |

---

## Section 16 — Assumptions

| # | Assumption | Confidence |
|---|------------|-----------|
| A-001 | The system serves a single terminal's departure operations; multi-terminal support is not required for the current scope. | Inferred |
| A-002 | All users of the system in its current form are trusted operators; authentication is deferred to a future enhancement. | Inferred |
| A-003 | The reference/training application will be extended with persistent storage before production use. | Inferred (README "Extending the Application") |
| A-004 | Delay duration estimates (BRULE-011) are placeholders; production versions will derive durations from operational data. | Confirmed (EtdCalculationService Javadoc) |
| A-005 | The server clock is considered the authoritative time source for all ETD calculations. | Inferred |

---

## Section 17 — Constraints

| # | Constraint | Confidence |
|---|------------|-----------|
| C-001 | Data is non-persistent; application restart clears all flight data. | Confirmed |
| C-002 | No user authentication or authorisation is implemented; the system is accessible to any user with network access to port 8080. | Confirmed |
| C-003 | No external integrations exist; delay durations are based on static estimates. | Confirmed |
| C-004 | The DIVERTED flight status has no creation path through the API in the current implementation. | Confirmed |

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

| # | Risk | Likelihood | Impact | Confidence |
|---|------|-----------|--------|-----------|
| R-001 | Data loss on application restart; in-memory store provides no recovery. | High | High | Confirmed |
| R-002 | No authentication means any network user can modify or delete all flight data. | High | High | Confirmed |
| R-003 | Static delay estimates may not reflect real-world conditions; operational decisions based on these could be inaccurate. | High | Medium | Confirmed |
| R-004 | No pagination could cause performance degradation with a large number of flight records. | Medium | Medium | Inferred |
| R-005 | The DIVERTED status is not actionable through the API; flights cannot be marked as diverted by operators. | Medium | Medium | Confirmed |

---

## Section 20 — Out-of-Scope Items

The following items are explicitly outside the current system scope and are noted as future requirements:

| # | Item |
|---|------|
| OOS-001 | Persistent database storage (e.g., Spring Data JPA + PostgreSQL) |
| OOS-002 | User authentication and role-based access control (e.g., Spring Security) |
| OOS-003 | Integration with live weather, ATC, and aircraft-tracking data feeds |
| OOS-004 | Multi-terminal or multi-airport management |
| OOS-005 | Passenger notification systems (SMS, email, display boards) |
| OOS-006 | Mobile application |
| OOS-007 | Regulatory or compliance reporting |
| OOS-008 | Real-time push notifications (WebSockets, SSE) replacing the polling model |
| OOS-009 | Audit log of all ETD changes |
| OOS-010 | Divert flight functionality (no `/divert` endpoint) |

---

## Section 21 — Open Questions

| # | Question | Priority | Confidence |
|---|----------|---------|-----------|
| OQ-001 | Who are the named business stakeholders responsible for approving this BRD? | High | Unresolved |
| OQ-002 | Are there named user roles beyond a generic "Operator" (e.g., Gate Agent, Supervisor, Viewer)? | High | Unresolved |
| OQ-003 | What are the SLA or response-time requirements for production deployment? | High | Unresolved |
| OQ-004 | Is there a planned timeline for migrating from in-memory to persistent storage? | High | Unresolved |
| OQ-005 | Are there accessibility compliance requirements (e.g., WCAG 2.1 AA)? | Medium | Unresolved |
| OQ-006 | Should the DIVERTED status be supported through a dedicated operator action? | Medium | Unresolved |
| OQ-007 | Should re-cancelling an already-cancelled flight or departing a cancelled flight be prevented? | Medium | Unresolved |
| OQ-008 | Are there requirements for an audit trail of ETD changes (who changed what and when)? | Medium | Unresolved |
| OQ-009 | Should delay duration estimates be configurable by administrators rather than hard-coded? | Low | Inferred |

---

## Section 22 — Acceptance Criteria

| AC ID | Links to | Criterion | Confidence |
|-------|---------|-----------|-----------|
| AC-001 | FR-001, FR-002 | Given the system has flights, when the operator views the Dashboard, then all flights are displayed in rows with all required columns populated, and KPI cards reflect current counts. | Confirmed |
| AC-002 | FR-003 | Given the operator enters "AA" in the search box, then only flights with "AA" in the flight number, airline, origin, or destination are displayed. | Confirmed |
| AC-003 | FR-006, FR-018 | Given the operator is on the Dashboard tab, when 15 seconds elapse, then the flight list refreshes automatically without operator interaction. | Confirmed |
| AC-004 | FR-007, BRULE-007, BRULE-008 | Given the operator submits a flight with flight number "ABC1" (invalid — too many letters), then the system rejects the creation with a validation error. | Confirmed |
| AC-005 | FR-009, BR-003 | Given a new flight is created, then its initial status is SCHEDULED and its ETD equals its scheduled departure. | Confirmed |
| AC-006 | FR-010, FR-011, BRULE-004, BRULE-005 | Given an operator updates a flight's ETD to 30 minutes later than scheduled, then the flight status changes to DELAYED and delay minutes = 30. | Confirmed |
| AC-007 | BRULE-005 | Given an operator submits a new ETD that is 10 minutes in the past, then the system rejects the update with an appropriate error message. | Confirmed |
| AC-008 | BRULE-009 | Given flight AA123 exists with scheduled departure at T, when an operator tries to create AA123 with the same scheduled departure T, then the system rejects the creation. | Confirmed |
| AC-009 | FR-012 | Given an operator cancels a flight with the note "crew unavailable", then the flight status changes to CANCELLED and the note is stored. | Confirmed |
| AC-010 | FR-013 | Given an operator marks a flight as departed, then the flight status changes to DEPARTED and the actual departure timestamp is recorded. | Confirmed |
| AC-011 | FR-021, BRULE-001 | Given a flight whose ETD has passed, when the 30-second lifecycle job runs, then the flight status is set to DEPARTED. | Confirmed |
| AC-012 | FR-021, BRULE-002 | Given a non-delayed flight whose ETD is 20 minutes from now, when the lifecycle job runs, then the flight status is BOARDING. | Confirmed |
| AC-013 | FR-021, BRULE-006 | Given a CANCELLED flight, when the lifecycle job runs, then the flight status remains CANCELLED. | Confirmed |
| AC-014 | FR-017, BRULE-012, BRULE-013 | Given 10 flights where 3 are delayed and 1 is cancelled, the statistics page shows: total=10, on-time=6, on-time%=60, delayed=3, cancelled=1, and average delay is computed only over the 3 delayed flights. | Confirmed |
| AC-015 | FR-023 | Given the system is running, a GET to `/actuator/health` returns HTTP 200 with status "UP". | Confirmed |
| AC-016 | NFR-001 | Given concurrent API requests to create and update flights, no data corruption or NullPointerException occurs. | Confirmed |
| AC-017 | NFR-009 | Given an unexpected server error, the API returns HTTP 500 with a JSON body containing `status`, `message`, and `timestamp`; no stack trace is included. | Confirmed |

---

## Section 23 — Traceability Matrix

*Placeholder — to be populated by the brd-traceability agent in Step 7.*

| Source Component | Observed Behaviour | Business Process | Business Req | Functional Req | Acceptance Criterion | Test Scenario |
|-----------------|-------------------|-----------------|-------------|----------------|---------------------|---------------|
| (See 07_traceability_matrix.md) | | | | | | |

---

*BRD first draft complete. All 23 sections present. Saved to 05_authoring_draft.md.*
