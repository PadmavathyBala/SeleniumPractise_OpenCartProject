# Business Requirements Document (Draft)

## Front Matter
- **Document Version and Date:** v0.1-draft / 2026-07-24
- **Authors:** BRD reverse-engineering pipeline run `2026-07-24T17-34-54Z`

### Document History
| Version | Date | Author | Change Summary |
|---|---|---|---|
| v0.1-draft | 2026-07-24 | Pipeline `2026-07-24T17-34-54Z` | Initial reverse-engineered draft |

### Approvals (blank by policy)
| Stakeholder | Title | Signature | Date |
|---|---|---|---|

## 1. Introduction
### 1.1 Purpose of the Document
Define business and functional requirements for ETD Airlines Departure Operations from implemented behavior.

### 1.2 Document Conventions, Terms, and Definitions
- **Confirmed**: Directly evidenced in source/test/config/doc.
- **Inferred**: Derived from multiple technical observations.
- **Assumed**: Reasonable but unverified expectation.
- **Unresolved**: Insufficient evidence.

### 1.3 Intended Audience and Reading Suggestions
Operations leadership, product owners, QA, and engineering teams.

### 1.4 Scope
#### 1.4.1 Project Scope
**In Scope:** Flight monitoring, ETD updates, status transitions, statistics dashboard, manual flight creation.
**Out of Scope:** Authentication/authorization, persistent data storage, external weather/ATC integration, exportable reporting.

#### 1.4.2 BRD Scope
Current-state reverse engineering only.

### 1.5 References
- Repo: `etd-airlines_1`
- Run ID: `2026-07-24T17-34-54Z`
- Key sources: README, backend Java services/controllers/models, static UI JS/HTML, tests.

## 2. Overall Description
### 2.1 Executive Summary
ETD Airlines is a web-based departure operations system for managing flight ETD and status.

### 2.2 Business Background / Current-State Overview
The application centralizes flight records and near-live updates for tactical departure decisions. **[Inferred]**

### 2.3 Business Objectives
- Improve visibility into departure readiness. **[Inferred]**
- Reduce manual coordination latency with shared dashboard. **[Assumed]**

### 2.4 Product Features
- Dashboard with KPIs and action controls. **[Confirmed]**
- Add Flight workflow. **[Confirmed]**
- ETD simulation updates. **[Confirmed]**
- Statistics charts. **[Confirmed]**

### 2.5 Stakeholders and User Classes
| Role | Department | Responsibilities | Confidence |
|---|---|---|---|
| Operations Controller | Flight Ops | Update ETD, depart, cancel, delete | Inferred |
| Dispatcher | Scheduling | Create flights | Inferred |
| Operations Monitor | OCC | View KPIs and charts | Inferred |

### 2.6 Operating Environment
Spring Boot 3.2.5, Java 17, browser UI (Bootstrap, vanilla JS), in-memory repository. **[Confirmed]**

### 2.7 Design and Implementation Constraints
No persistence layer; no security enforcement; simulator-driven pseudo-live state. **[Confirmed]**

### 2.8 Assumptions, Dependencies, and Risks
Security, audit logging, and KPI target values require stakeholder definition. **[Unresolved]**

## 3. System Features
### 3.1 Feature: Flight Monitoring Dashboard
#### 3.1.1 Description
Flight dashboard shall show all relevant operational fields and support filter/sort/search. **[Confirmed]**
#### 3.1.2 Business Rules
- BRULE-001, BRULE-002, BRULE-005 apply. **[Confirmed]**
#### 3.1.3 Functional Requirements
- FR-001, FR-011, FR-012, FR-014. **[Confirmed]**
#### 3.1.4 Use Case(s)
- **Use Case ID:** UC-001 **[Confirmed]**
- **Use Case Name:** Monitor Dashboard **[Confirmed]**
- **Actors:** Operations Monitor **[Inferred]**
- **Description:** View active departure board and KPIs. **[Confirmed]**
- **Trigger:** Dashboard opened or refresh clicked. **[Confirmed]**
- **Preconditions:** System online. **[Confirmed]**
- **Postconditions:** Current view displayed. **[Confirmed]**
- **Normal Flow:** Load flights and stats; user filters/sorts. **[Confirmed]**
- **Alternative Flow:** TBD. **[Unresolved]**
- **Exceptions:** API failures show toast. **[Confirmed]**
- **Includes:** FR-001, FR-011. **[Confirmed]**
- **Priority:** High **[Inferred]**
- **Frequency of Use:** Continuous during operations **[Inferred]**
- **Business Rules:** BRULE-001/002/005 **[Confirmed]**
- **Special Requirements:** Near-real-time refresh. **[Confirmed]**
- **Assumptions:** Single shared operations screen acceptable. **[Assumed]**
- **Notes and Issues:** No historical trend view. **[Unresolved]**

### 3.2 Feature: Flight Creation
#### 3.2.1 Description
Users create new flights for monitoring. **[Confirmed]**
#### 3.2.2 Business Rules
BRULE-001 baseline timing and duplicate-prevention rule FR-004. **[Confirmed]**
#### 3.2.3 Functional Requirements
FR-003, FR-004. **[Confirmed]**
#### 3.2.4 Use Case(s)
- **Use Case ID:** UC-002 **[Confirmed]**
- **Use Case Name:** Create Flight **[Confirmed]**
- **Actors:** Dispatcher **[Inferred]**
- **Description:** Enter and submit flight data. **[Confirmed]**
- **Trigger:** Create Flight button pressed. **[Confirmed]**
- **Preconditions:** Required fields valid. **[Confirmed]**
- **Postconditions:** Flight created with SCHEDULED status and ETD=schedule. **[Confirmed]**
- **Normal Flow:** Input -> validate -> save -> success message. **[Confirmed]**
- **Alternative Flow:** Optional gate/aircraft omitted. **[Confirmed]**
- **Exceptions:** Validation and duplicate errors return 400. **[Confirmed]**
- **Includes:** FR-003, FR-004. **[Confirmed]**
- **Priority:** High **[Inferred]**
- **Frequency of Use:** As new departures arise **[Inferred]**
- **Business Rules:** Format rules on flight and IATA fields. **[Confirmed]**
- **Special Requirements:** Uppercase normalization in UI. **[Confirmed]**
- **Assumptions:** Manual entry remains primary ingestion channel. **[Assumed]**
- **Notes and Issues:** No bulk upload option. **[Unresolved]**

### 3.3 Feature: ETD and Status Management
#### 3.3.1 Description
Operators update ETD and state transitions. **[Confirmed]**
#### 3.3.2 Business Rules
BRULE-002, BRULE-003, BRULE-004. **[Confirmed]**
#### 3.3.3 Functional Requirements
FR-005, FR-006, FR-007, FR-008, FR-009, FR-010, FR-013. **[Confirmed]**
#### 3.3.4 Use Case(s)
- **Use Case ID:** UC-003 **[Confirmed]**
- **Use Case Name:** Manage ETD and Flight State **[Confirmed]**
- **Actors:** Operations Controller **[Inferred]**
- **Description:** Adjust ETD, cancel, depart, or delete a flight. **[Confirmed]**
- **Trigger:** Action buttons selected for a flight row. **[Confirmed]**
- **Preconditions:** Flight exists. **[Confirmed]**
- **Postconditions:** Persisted updated state. **[Confirmed]**
- **Normal Flow:** Open modal -> submit ETD -> validate -> recompute status -> refresh dashboard. **[Confirmed]**
- **Alternative Flow:** Cancel/depart/delete direct actions. **[Confirmed]**
- **Exceptions:** Not found, invalid ETD, generic errors. **[Confirmed]**
- **Includes:** FR-005 through FR-010. **[Confirmed]**
- **Priority:** High **[Inferred]**
- **Frequency of Use:** Frequent during active operations **[Inferred]**
- **Business Rules:** ETD cannot be >5 minutes in past. **[Confirmed]**
- **Special Requirements:** User feedback via alerts/toasts. **[Confirmed]**
- **Assumptions:** Browser confirm/prompt controls are acceptable for critical actions. **[Assumed]**
- **Notes and Issues:** Deletion is irreversible. **[Confirmed]**

### 3.4 Feature: Operational Statistics
#### 3.4.1 Description
Provide summarized operational analytics. **[Confirmed]**
#### 3.4.2 Business Rules
On-time and average delay calculations follow service logic. **[Confirmed]**
#### 3.4.3 Functional Requirements
FR-011, RR-001. **[Confirmed]**
#### 3.4.4 Use Case(s)
- **Use Case ID:** UC-004 **[Confirmed]**
- **Use Case Name:** View Statistics **[Confirmed]**
- **Actors:** Operations Monitor, Leadership Viewer **[Inferred]**
- **Description:** Render KPI and chart analytics. **[Confirmed]**
- **Trigger:** Statistics tab shown. **[Confirmed]**
- **Preconditions:** Flight data exists. **[Confirmed]**
- **Postconditions:** Visual statistics displayed. **[Confirmed]**
- **Normal Flow:** Request stats -> render charts. **[Confirmed]**
- **Alternative Flow:** No delay reasons -> informational text. **[Confirmed]**
- **Exceptions:** Toast on API error. **[Confirmed]**
- **Includes:** FR-011, RR-001. **[Confirmed]**
- **Priority:** Medium **[Inferred]**
- **Frequency of Use:** Periodic monitoring **[Inferred]**
- **Business Rules:** Count groupings by status/reason/airline. **[Confirmed]**
- **Special Requirements:** Chart.js compatibility in browser. **[Confirmed]**
- **Assumptions:** Metrics are sufficient without export. **[Assumed]**
- **Notes and Issues:** No historical trends. **[Unresolved]**

## 4. External Interface Requirements
### 4.1 User Interface
Single-page tabbed UI with dashboard, statistics, and add-flight views. **[Confirmed]**

### 4.2 Hardware Interfaces
Standard browser-capable endpoint. **[Assumed]**

### 4.3 Software Interfaces
REST JSON API and actuator endpoints. **[Confirmed]**

### 4.4 Communications Interfaces
HTTP over local/network transport. **[Inferred]**

### 4.5 Reporting Requirements
RR-001 implemented via charts; RR-002 unresolved export capability. **[Confirmed/Unresolved]**

## 5. Other Nonfunctional Requirements
### 5.1 Performance
System should be fast for dashboard users. **[Unresolved]**

### 5.2 Security
No authentication present; SR-001 unresolved. **[Confirmed/Unresolved]**

### 5.3 Data Requirements
DR-001, DR-002; persistence continuity unresolved (NFR-004). **[Confirmed/Unresolved]**

### 5.4 Software Quality Attributes
Thread-safe in-memory operations and test coverage for core service operations. **[Confirmed]**

## 6. Other Requirements
Audit requirement AR-001 remains unresolved.

## 7. Success Criteria
- Stakeholders can monitor departures and respond in real time. **[Inferred]**
- The team reduces delay handling friction quickly. **[Assumed]**

## 8. Approval and Sign-off
| Stakeholder | Title | Signature | Date |
|---|---|---|---|

## Appendix A: Glossary
- ETD: Estimated Time of Departure.
- IATA: International Air Transport Association airport code.
- OCC: Operations Control Center.

## Appendix B: Analysis Models
Not Generated in this pipeline run. **[Unresolved]**

## Appendix C: Issues List
- Missing measurable performance SLAs.
- Missing authentication/authorization.

## Appendix D: Screenshots, Mock-Ups
Not Generated in this pipeline run. **[Unresolved]**

## Appendix E: Test Scenarios
- `FlightServiceTest.createFlightAssignsIdAndDefaults` validates create defaults. **[Confirmed]**
- `FlightServiceTest.updateEtdMarksFlightAsDelayed` validates ETD update delay classification. **[Confirmed]**

## Appendix F: Traceability Matrix
Placeholder — populated in Step 7.
