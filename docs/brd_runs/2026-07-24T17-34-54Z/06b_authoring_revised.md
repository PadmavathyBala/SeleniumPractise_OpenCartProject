# Business Requirements Document (Revised)

## Front Matter
- **Document Version and Date:** v0.2-revised / 2026-07-24
- **Authors:** BRD reverse-engineering pipeline run `2026-07-24T17-34-54Z`

### Document History
| Version | Date | Author | Change Summary |
|---|---|---|---|
| v0.1-draft | 2026-07-24 | Pipeline `2026-07-24T17-34-54Z` | Initial reverse-engineered draft |
| v0.2-revised | 2026-07-24 | Pipeline `2026-07-24T17-34-54Z` | Resolved Step 6 review findings; strengthened schema consistency and issue consolidation |

### Approvals (blank by policy)
| Stakeholder | Title | Signature | Date |
|---|---|---|---|

## 1. Introduction
### 1.1 Purpose of the Document
Capture the current-state business and functional requirements of ETD Airlines Departure Operations by reverse-engineering implemented behavior from source, UI, tests, and configuration artifacts. **[Confirmed]** Citations: `README.md:3-5`, `FlightController.java:20-70`, `index.html:31-312`.

### 1.2 Document Conventions, Terms, and Definitions
- **Confirmed**: Directly supported by source/test/config/doc evidence with citation.
- **Inferred**: Derived from multiple observations.
- **Assumed**: Reasonable but unverified business expectation.
- **Unresolved**: Not enough evidence.
- **ETD**: Estimated Time of Departure.
- **OCC**: Operations Control Center.

### 1.3 Intended Audience and Reading Suggestions
Audience: operations leadership, business analysts, product owners, QA, and engineering implementers. Read sections 2-3 first for business intent and feature behavior, then sections 4-5 for interface/NFR constraints, and appendix C for open risks. **[Inferred]**

### 1.4 Scope
#### 1.4.1 Project Scope
- **In Scope:** flight monitoring; flight creation; ETD/status updates; cancellation/departure operations; dashboard statistics. **[Confirmed]** Citations: `FlightController.java:29-70`, `StatisticsService.java:28-83`, `index.html:31-245`.
- **Out of Scope:** authentication/authorization, durable persistence, external operational feed integration, downloadable reports, generated diagrams/screenshots. **[Confirmed]** Citations: `WebConfig.java:19-23`, `FlightRepository.java:18-25`, `README.md:100-104`.

#### 1.4.2 BRD Scope
This BRD describes current implemented behavior plus explicit gaps and assumptions for stakeholder confirmation. **[Confirmed]**

### 1.5 References
- Repository: `/etd-airlines_1`
- Run ID: `2026-07-24T17-34-54Z`
- Source files: controllers/services/models/repository/dto/exception/config/UI scripts/tests/README.

## 2. Overall Description
### 2.1 Executive Summary
ETD Airlines provides a lightweight operations control interface where staff monitor departures, update ETDs, and assess delay patterns through dashboard analytics. **[Confirmed]** Citations: `index.html:55-190`, `dashboard.js:24-82`, `statistics.js:14-98`.

### 2.2 Business Background / Current-State Overview
The system appears designed for tactical, same-session visibility rather than enterprise record retention, using in-memory storage and periodic simulation to emulate live conditions. **[Inferred]** Chain: `FlightRepository.java:18-25`, `FlightSimulationService.java:96-127`.

### 2.3 Business Objectives
- Provide near-real-time awareness of departure operations. **[Confirmed]** `app.js:6-31`, `FlightSimulationService.java:96-127`.
- Enable rapid correction of ETD and status information by operations staff. **[Confirmed]** `dashboard.js:111-201`, `FlightService.java:65-100`.
- Support comparative operational insights by status, delay reason, and airline. **[Confirmed]** `StatisticsService.java:57-83`.
- Define formal security, audit, and performance targets for production readiness. **[Assumed]**

### 2.4 Product Features
1. Live Dashboard Monitoring. **[Confirmed]**
2. Flight Record Creation. **[Confirmed]**
3. ETD/Status Management. **[Confirmed]**
4. Operational Statistics Visualization. **[Confirmed]**

### 2.5 Stakeholders and User Classes
| Role | Department | Responsibilities | Confidence | Evidence |
|---|---|---|---|---|
| Operations Controller | OCC / Flight Ops | Update ETD, mark departures, cancel/delete flights | Inferred | `dashboard.js:111-158` |
| Dispatcher | Scheduling | Create new flights and basic departure attributes | Inferred | `addFlight.js:18-42`, `index.html:193-243` |
| Operations Monitor | OCC | Observe KPIs/charts and monitor exceptions | Inferred | `index.html:58-93`, `statistics.js:14-98` |
| Engineering Support | IT | Maintain service/runtime observability and logs | Inferred | `application.properties:13-19` |

### 2.6 Operating Environment
- Java 17 runtime. **[Confirmed]** `pom.xml:22-25`.
- Spring Boot 3.2.5 backend. **[Confirmed]** `pom.xml:9-12`.
- Browser UI with Bootstrap and Chart.js. **[Confirmed]** `index.html:7-8`, `index.html:304-306`.
- In-memory thread-safe repository (ConcurrentHashMap). **[Confirmed]** `FlightRepository.java:18`.

### 2.7 Design and Implementation Constraints
- No durable database storage. **[Confirmed]** `FlightRepository.java:18-25`.
- No identity/role access enforcement. **[Confirmed]** (no security layer found).
- Delay simulation is probabilistic and not tied to live aviation feeds. **[Confirmed]** `FlightSimulationService.java:111-127`.

### 2.8 Assumptions, Dependencies, and Risks
- **Assumption:** Operators share a trusted network context; current open CORS and no auth may be acceptable only in non-production. **[Assumed]**
- **Dependency:** Production deployment would require security and persistent data layer. **[Inferred]**
- **Risk:** Irreversible delete without audit could remove operational evidence. **[Confirmed]** `dashboard.js:154-156`, `FlightService.java:102-107`.

## 3. System Features
### 3.1 Feature: Flight Monitoring Dashboard
#### 3.1.1 Description
The system shall display a searchable, sortable flight board with KPI cards and status indicators for active operational monitoring. **[Confirmed]** Citations: `index.html:58-157`, `dashboard.js:50-82`.

#### 3.1.2 Business Rules
- **BRULE-001** Delay minutes = max(0, ETD - scheduled). **[Confirmed]** `Flight.java:51-57`.
- **BRULE-002** Delay threshold is >=15 minutes. **[Confirmed]** `Flight.java:60-64`.
- **BRULE-005** On-time metrics exclude cancelled flights from on-time count. **[Confirmed]** `StatisticsService.java:35-47`.

#### 3.1.3 Functional Requirements
- **FR-001** List flights with optional text search. **[Confirmed]** `FlightController.java:29-35`.
- **FR-011** Provide statistics payload for KPI/chart rendering. **[Confirmed]** `StatisticsController.java:22-25`.
- **FR-012** Auto-refresh dashboard every 15 seconds. **[Confirmed]** `app.js:6-31`.
- **FR-014** Present row-level action controls for ETD/state operations. **[Confirmed]** `dashboard.js:111-127`.

#### 3.1.4 Use Case(s)
- **Use Case ID:** UC-001 **[Confirmed]**
- **Use Case Name:** Monitor Departure Dashboard **[Confirmed]**
- **Actors:** Operations Monitor **[Inferred]**
- **Description:** View current departure operations and identify flights needing intervention. **[Confirmed]**
- **Trigger:** User lands on Dashboard tab or clicks Refresh. **[Confirmed]**
- **Preconditions:** Backend reachable; at least zero flights in dataset. **[Confirmed]**
- **Postconditions:** Current KPI/table state is visible with latest retrieved data. **[Confirmed]**
- **Normal Flow:**
  1. System requests flights and statistics. **[Confirmed]**
  2. User applies search/status/sort filters. **[Confirmed]**
  3. System renders filtered table and KPI cards. **[Confirmed]**
- **Alternative Flow:** If filters return no rows, system displays "No flights match the current filters." **[Confirmed]** `dashboard.js:75-77`.
- **Exceptions:** API failure results in toast "Failed to load flights". **[Confirmed]** `dashboard.js:33-34`.
- **Includes:** FR-001, FR-011, FR-012. **[Confirmed]**
- **Priority:** High **[Inferred]**
- **Frequency of Use:** Continuous during operations window **[Inferred]**
- **Business Rules:** BRULE-001, BRULE-002, BRULE-005 **[Confirmed]**
- **Special Requirements:** Last-update timestamp visible in navbar. **[Confirmed]** `index.html:23-24`, `dashboard.js:203-205`.
- **Assumptions:** Dashboard metrics are interpreted by trained operations staff. **[Assumed]**
- **Notes and Issues:** Historical trends absent. **[Unresolved]**

### 3.2 Feature: Flight Record Creation
#### 3.2.1 Description
The system shall capture new outbound flight records with required operational fields and validation controls. **[Confirmed]** `CreateFlightRequest.java:14-35`, `index.html:199-233`.

#### 3.2.2 Business Rules
- Flight number format: 2-3 letters plus 1-4 digits. **[Confirmed]**
- Origin/Destination format: 3-letter IATA codes. **[Confirmed]**
- Duplicate flight number + scheduled time combination rejected. **[Confirmed]**

#### 3.2.3 Functional Requirements
- **FR-003** Create flight via validated request and return created entity. **[Confirmed]**
- **FR-004** Prevent duplicate flight/time combinations. **[Confirmed]**

#### 3.2.4 Use Case(s)
- **Use Case ID:** UC-002 **[Confirmed]**
- **Use Case Name:** Create Flight **[Confirmed]**
- **Actors:** Dispatcher **[Inferred]**
- **Description:** Add a departure into the operational board. **[Confirmed]**
- **Trigger:** User clicks "Create Flight". **[Confirmed]**
- **Preconditions:** Required fields supplied; request syntactically valid. **[Confirmed]**
- **Postconditions:** Flight persisted with SCHEDULED status and ETD=scheduled departure. **[Confirmed]** `FlightService.java:59-62`.
- **Normal Flow:** Input details -> submit -> server validates -> save -> success alert/toast -> dashboard refresh. **[Confirmed]**
- **Alternative Flow:** Gate and aircraft omitted; system still accepts request. **[Confirmed]**
- **Exceptions:** Validation or duplicate checks return 400 and error details. **[Confirmed]**
- **Includes:** FR-003, FR-004. **[Confirmed]**
- **Priority:** High **[Inferred]**
- **Frequency of Use:** As schedule entries are created **[Inferred]**
- **Business Rules:** See BRULE-001 baseline timing and format constraints. **[Confirmed]**
- **Special Requirements:** UI normalizes flight number/IATA fields to uppercase pre-submit. **[Confirmed]** `addFlight.js:20-24`.
- **Assumptions:** Manual single-flight creation is sufficient for current process. **[Assumed]**
- **Notes and Issues:** No bulk ingestion interface. **[Unresolved]**

### 3.3 Feature: ETD and Flight State Management
#### 3.3.1 Description
The system shall support ETD updates and explicit state transitions (depart/cancel/delete) per flight record. **[Confirmed]** `dashboard.js:139-158`, `FlightService.java:65-107`.

#### 3.3.2 Business Rules
- **BRULE-003** Terminal statuses remain unchanged during recomputation. **[Confirmed]**
- **BRULE-004** Within 30 minutes of ETD -> BOARDING unless delayed. **[Confirmed]**
- ETD update cannot be more than 5 minutes in the past. **[Confirmed]**

#### 3.3.3 Functional Requirements
- **FR-005** Update ETD with optional reason/notes. **[Confirmed]**
- **FR-006** Reject stale ETD updates (>5 min in past). **[Confirmed]**
- **FR-007** Recompute status after ETD update. **[Confirmed]**
- **FR-008** Cancel flight and store notes. **[Confirmed]**
- **FR-009** Mark departed with actual departure timestamp. **[Confirmed]**
- **FR-010** Delete flight by identifier. **[Confirmed]**
- **FR-013** Scheduled simulator updates statuses and introduces delays. **[Confirmed]**

#### 3.3.4 Use Case(s)
- **Use Case ID:** UC-003 **[Confirmed]**
- **Use Case Name:** Manage ETD and Flight State **[Confirmed]**
- **Actors:** Operations Controller **[Inferred]**
- **Description:** Keep departure data accurate by processing tactical updates and state actions. **[Confirmed]**
- **Trigger:** User invokes edit/depart/cancel/delete action. **[Confirmed]**
- **Preconditions:** Flight exists; operator has process authority (business-level, not technically enforced). **[Inferred]**
- **Postconditions:** Updated flight state stored and reflected in dashboard. **[Confirmed]**
- **Normal Flow:**
  1. User selects action.
  2. For ETD update, user enters new ETD and optional context.
  3. System validates ETD and recalculates status.
  4. Dashboard refreshes with confirmation toast.
  **[Confirmed]**
- **Alternative Flow:**
  - Depart action sets DEPARTED and actual time. **[Confirmed]**
  - Cancel action sets CANCELLED and stores note. **[Confirmed]**
- **Exceptions:**
  - Invalid ETD -> 400 with message.
  - Missing flight -> 404.
  - Generic server error -> 500.
  **[Confirmed]**
- **Includes:** FR-005, FR-006, FR-007, FR-008, FR-009, FR-010. **[Confirmed]**
- **Priority:** High **[Inferred]**
- **Frequency of Use:** Frequent during active departures **[Inferred]**
- **Business Rules:** BRULE-002/003/004 + stale ETD limit. **[Confirmed]**
- **Special Requirements:** Deletion requires explicit browser confirmation prompt. **[Confirmed]** `dashboard.js:154-156`.
- **Assumptions:** Browser confirmation is an acceptable control in current-state environment. **[Assumed]**
- **Notes and Issues:** Missing soft delete and audit trail create operational governance risk. **[Unresolved]**

### 3.4 Feature: Operational Statistics and Insights
#### 3.4.1 Description
The system shall calculate and display summary analytics for flight status and delay patterns. **[Confirmed]** `StatisticsService.java:28-83`.

#### 3.4.2 Business Rules
- Status counts include all known statuses with zero defaults. **[Confirmed]** `StatisticsService.java:57-64`.
- Delay-reason counts only include flights with non-null reason values. **[Confirmed]** `StatisticsService.java:67-73`.

#### 3.4.3 Functional Requirements
- **FR-011** Statistics endpoint returns aggregate metrics.
- **RR-001** UI renders charts for status/reason/airline distributions.
**[Confirmed]**

#### 3.4.4 Use Case(s)
- **Use Case ID:** UC-004 **[Confirmed]**
- **Use Case Name:** Review Operations Statistics **[Confirmed]**
- **Actors:** Operations Monitor, Leadership Viewer **[Inferred]**
- **Description:** Review aggregate metrics to detect delay concentration and airline impact. **[Inferred]**
- **Trigger:** User opens statistics tab or refresh cycle runs on stats view. **[Confirmed]**
- **Preconditions:** Statistics API reachable; flight data available. **[Confirmed]**
- **Postconditions:** Current aggregate charts rendered for review. **[Confirmed]**
- **Normal Flow:** API call -> transform dataset -> render charts. **[Confirmed]**
- **Alternative Flow:** If no delay reasons, show informational placeholder text. **[Confirmed]**
- **Exceptions:** Retrieval failures shown by danger toast. **[Confirmed]**
- **Includes:** FR-011, RR-001. **[Confirmed]**
- **Priority:** Medium **[Inferred]**
- **Frequency of Use:** Recurring during operations review intervals **[Inferred]**
- **Business Rules:** Grouping and calculation logic from StatisticsService. **[Confirmed]**
- **Special Requirements:** Chart.js availability in browser runtime. **[Confirmed]**
- **Assumptions:** Visual dashboard is sufficient for reporting consumers. **[Assumed]**
- **Notes and Issues:** Export reporting and historical trend drill-down not implemented. **[Unresolved]**

## 4. External Interface Requirements
### 4.1 User Interface
Tabbed single-page UI with dashboard table/actions, statistics charts, add-flight form, and ETD modal. **[Confirmed]** `index.html:31-312`.

### 4.2 Hardware Interfaces
No dedicated hardware interface beyond standard workstation/mobile browser device. **[Assumed]**

### 4.3 Software Interfaces (APIs, integrations)
- REST endpoints `/api/flights` and `/api/statistics`. **[Confirmed]** `FlightController.java`, `StatisticsController.java`.
- Actuator endpoint `/actuator/health` and additional info/metrics. **[Confirmed]** `README.md:68`, `application.properties:18-19`.
- External weather/ATC integration absent in current implementation. **[Confirmed]**

### 4.4 Communications Interfaces
HTTP/JSON request-response between browser and backend under same application origin path. **[Confirmed]** `api.js:5-37`.

### 4.5 Reporting Requirements
- **RR-001** Visual dashboard metrics by status/reason/airline. **[Confirmed]**
- **RR-002** Exportable reports. **[Unresolved]** No export feature found.

## 5. Other Nonfunctional Requirements
### 5.1 Performance
- **NFR-001** UI refresh cadence every 15 seconds provides near-live monitoring. **[Confirmed]**
- **NFR-005** Quantitative response-time SLA (e.g., p95 latency) is not defined in source artifacts and must be stakeholder-specified before testable enforcement. **[Unresolved]**

### 5.2 Security
- **SR-001** Authenticated/authorized write access is required for production-grade control but currently absent. **[Unresolved]**
- **SR-002** CORS should be restricted to approved origins for production; current wildcard indicates non-hardened config. **[Assumed]**

### 5.3 Data Requirements
- **DR-001** Flight record schema includes operational and delay metadata fields. **[Confirmed]**
- **DR-002** Request-level validation enforces identifier formatting and mandatory fields. **[Confirmed]**
- **NFR-004** Persist data across restarts is currently unmet. **[Unresolved]**

### 5.4 Software Quality Attributes
- Thread-safe in-memory repository operations. **[Confirmed]**
- Standardized REST error structure for validation/not-found/server errors. **[Confirmed]**
- Unit test coverage exists for core create and ETD update paths, but broader coverage is not evidenced. **[Confirmed/Inferred]**

## 6. Other Requirements
- **AR-001** Capture actor identity and change history for ETD/state operations. **[Unresolved]**
- **INT-003** Integrate real operational feeds (weather/ATC) for delay causality and forecasting. **[Assumed]**

## 7. Success Criteria
1. Users can view up-to-date departure board information and execute ETD/state actions from one interface. **[Confirmed]** Citations: `index.html:55-160`, `dashboard.js:111-201`.
2. Operations can review status, delay, and airline distribution in the statistics tab. **[Confirmed]** `statistics.js:14-98`.
3. Quantitative business KPI targets (e.g., delay reduction %, handling-time reduction, SLA values) are not present in source artifacts and require stakeholder definition. **[Unresolved]**
4. "Friction reduction" outcomes are plausible but not measurable from code artifacts alone. **[Assumed]**

## 8. Approval and Sign-off
| Stakeholder | Title | Signature | Date |
|---|---|---|---|

## Appendix A: Glossary
- **ETD**: Estimated Time of Departure.
- **IATA**: International Air Transport Association code standard.
- **OCC**: Operations Control Center.
- **Delayed Flight**: Delay >= 15 minutes from scheduled departure.

## Appendix B: Analysis Models
Not Generated in this run (no diagramming tool invocation in 8-step pipeline). **[Unresolved / Not Generated]**

## Appendix C: Issues List (Consolidated Assumed/Unresolved Register)
| ID | Section | Item | Confidence | Impact | Required Follow-up |
|---|---|---|---|---|---|
| ISS-001 | 2.5 | Formal user-role definitions and permissions matrix not documented | Unresolved | Access governance ambiguity | Define enterprise role model |
| ISS-002 | 2.8 / 5.2 | Authentication and authorization absent | Unresolved | Unauthorized write-risk in production | Implement identity + RBAC |
| ISS-003 | 5.3 | Data persistence across restarts not implemented | Unresolved | Operational continuity/data loss risk | Add persistent store |
| ISS-004 | 6 | Audit trail of who changed ETD/status missing | Unresolved | Compliance/forensics gap | Implement audit logging |
| ISS-005 | 4.5 | Exportable reports unavailable | Unresolved | Manual reporting overhead | Add export/report module |
| ISS-006 | 5.1 / 7 | Quantitative SLA and KPI targets not specified | Unresolved | Cannot objectively measure performance/success | Stakeholder KPI workshop |
| ISS-007 | 4.2 | Hardware/environment baseline inferred only | Assumed | Deployment planning uncertainty | Publish deployment architecture |
| ISS-008 | 3.3 | Browser confirmation used for irreversible delete | Assumed/Unresolved | Human error risk | Add soft-delete and recovery controls |
| ISS-009 | 6 | External weather/ATC integration desired but absent | Assumed | Forecast quality limitation | Define integration roadmap |

## Appendix D: Screenshots, Mock-Ups
Not Generated in this run. **[Unresolved / Not Generated]**

## Appendix E: Test Scenarios
1. **TS-001** Create Flight defaults verification.
   - Source test: `src/test/java/com/airlines/etd/FlightServiceTest.java` method `createFlightAssignsIdAndDefaults`.
   - Coverage: FR-003, BRULE-001.
   - Confidence: **Confirmed**.
2. **TS-002** ETD update delay classification verification.
   - Source test: `FlightServiceTest.updateEtdMarksFlightAsDelayed`.
   - Coverage: FR-005, FR-007, BRULE-002.
   - Confidence: **Confirmed**.
3. Additional automated tests for cancellation/departure/delete/error handling/statistics are not evidenced in repository test suite. **[Unresolved]**

## Appendix F: Traceability Matrix
Placeholder — populated in Step 7 and merged in final assembled BRD.
