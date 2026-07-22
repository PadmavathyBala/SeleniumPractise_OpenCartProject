# BRD Requirements Extraction — etd-airlines_1

**Run ID:** 2026-07-22T17-30-27Z  
**Target:** `etd-airlines_1`  
**Source inputs:** `02_discovery.md`, `03_business_process.md`  
**Agent:** `brd-requirements-extraction`

## 1. Extraction approach
- Requirements below are reverse-engineered only from the supplied discovery and business-process findings.
- Each requirement is traceable to one or more discovery sections (`Dx`) and business-process sections (`BPx`).
- **Priority** is analyst-assigned based on operational centrality in the discovered workflows.
- **Confidence** is inherited from the underlying source material: **Confirmed**, **Inferred**, **Assumed**, or **Unresolved**.

## 2. Traceability key
### Discovery references
- **D2** Application overview and scope
- **D3** Users, roles, and permissions
- **D4** Modules and user-visible features
- **D5** Screens, navigation, and UI behavior
- **D6.1-D6.3** Data model and reporting outputs
- **D7** API contracts
- **D8.1-D8.9** Business workflows
- **D9** Business rules and validations
- **D10** Integrations and data flow
- **D11** Notifications, errors, and operational behavior
- **D12** Constraints, assumptions, and unresolved questions

### Business-process references
- **BP1-BP9** Process 1 through Process 9 in `03_business_process.md`
- **BP-CR** Cross-process business rules
- **BP-OPS** Operational scenarios to carry into the BRD
- **BP-FU** Notes and issues for BRD follow-up

## 3. Requirement summary
| Category | Count |
|---|---:|
| Business requirements (BR) | 9 |
| Functional requirements (FR) | 16 |
| Non-functional requirements (NFR) | 7 |
| Business rules (BRULE) | 14 |
| Data requirements (DR) | 5 |
| Integration requirements (INT) | 3 |
| Reporting requirements (RPT) | 2 |
| Security requirements (SEC) | 3 |
| Audit requirements (AUD) | 2 |
| **Total** | **61** |

## 4. Structured requirement catalog

### 4.1 Business requirements
| ID | Priority | Confidence | Requirement | Traceability |
|---|---|---|---|---|
| BR-001 | High | Confirmed | The system shall provide a live departure-operations board for monitoring current flights, ETD position, delay state, and readiness for action. | D2, D4, D8.1; BP1, BP-OPS |
| BR-002 | High | Confirmed | The system shall allow operations users to search, filter, and prioritize flights so they can focus on relevant or urgent departures. | D4, D5, D8.2; BP2, Journey B |
| BR-003 | High | Confirmed | The system shall allow a user to register a new flight for operational monitoring. | D4, D8.3, D9; BP3, Journey C |
| BR-004 | High | Confirmed | The system shall allow a user to revise ETD and capture disruption context so the operational board reflects the latest expected departure plan. | D4, D8.4, D9; BP4, Journey B |
| BR-005 | High | Confirmed | The system shall allow a user to cancel a flight and retain explanatory notes in the operational record. | D4, D8.5, D9; BP5, Journey D |
| BR-006 | High | Confirmed | The system shall allow a flight to be closed out as departed when it has left operational control. | D4, D8.6, D9; BP6, BP7 |
| BR-007 | Medium | Confirmed | The system shall provide current operational statistics and visual summaries for supervisor oversight. | D4, D6.3, D8.8; BP8, Journey A |
| BR-008 | Low | Confirmed | When operated in demo or training mode, the system shall seed sample flights and simulate live operational changes. | D2, D8.9, D10, D12; BP9, BP7 |
| BR-009 | Low | Unresolved | If record deletion remains in scope, the system shall support permanent removal of a flight record under a defined business process and control model. | D8.7, D12; BP7, BP-FU |

### 4.2 Functional requirements
| ID | Priority | Confidence | Requirement | Traceability |
|---|---|---|---|---|
| FR-001 | High | Confirmed | On application load, the system shall initialize the dashboard and immediately retrieve the current flight list and statistics. | D8.1; BP1 |
| FR-002 | High | Confirmed | The dashboard shall display KPI values for total flights, on-time flights, delayed flights, cancelled flights, on-time percentage, and average delay. | D4, D6.3, D8.1; BP1 |
| FR-003 | High | Confirmed | The dashboard shall display each flight with flight number, airline, route, gate, scheduled departure, ETD, delay minutes, status, delay reason, and row-level actions. | D4, D6.1; BP1 |
| FR-004 | Medium | Confirmed | The interface shall display a live-status badge and last-update timestamp for the current board refresh. | D5, D8.1; BP1 |
| FR-005 | High | Confirmed | The system shall execute free-text search against flight number, airline, origin, and destination using the flights endpoint. | D5, D7, D8.2; BP2 |
| FR-006 | Medium | Confirmed | The dashboard shall allow the loaded flight list to be filtered by status without changing stored flight data. | D4, D5, D8.2; BP2 |
| FR-007 | Medium | Confirmed | The dashboard shall allow the loaded flight list to be sorted by ETD, scheduled departure, largest delay, or flight number. | D4, D5, D8.2; BP2 |
| FR-008 | High | Confirmed | The Add Flight form shall capture flight number, airline, aircraft type, origin, destination, gate, and scheduled departure. | D4, D5, D8.3; BP3 |
| FR-009 | High | Confirmed | When a flight is created successfully, the system shall set estimated departure equal to scheduled departure, assign status `SCHEDULED`, confirm success in the UI, and refresh the dashboard. | D8.3, D11; BP3 |
| FR-010 | High | Confirmed | The ETD update flow shall present a modal containing flight context and fields for a new ETD, optional delay reason, and optional delay notes. | D5, D8.4; BP4 |
| FR-011 | High | Confirmed | After a valid ETD update, the system shall save the revised ETD and immediately recalculate the flight status. | D7, D8.4, D9; BP4, BP-CR |
| FR-012 | High | Confirmed | The cancellation flow shall allow optional notes, set the flight status to `CANCELLED`, store the notes on the flight record, and refresh the board. | D7, D8.5, D9; BP5 |
| FR-013 | High | Confirmed | The manual departure flow shall require confirmation, set the flight status to `DEPARTED`, record actual departure time, and refresh the board. | D7, D8.6; BP6 |
| FR-014 | Medium | Confirmed | Automatic status recomputation shall be able to move a flight to `DEPARTED` when current time is past ETD. | D8.6, D9; BP6, BP-CR |
| FR-015 | Medium | Confirmed | The Statistics view shall lazily load and render current status, delay-reason, and airline-distribution charts from aggregated flight data. | D4, D5, D8.8; BP8 |
| FR-016 | Low | Confirmed | The current implementation shall allow a user to delete a flight permanently after a confirmation prompt. | D4, D8.7, D11; BP7 |

### 4.3 Non-functional requirements
| ID | Priority | Confidence | Requirement | Traceability |
|---|---|---|---|---|
| NFR-001 | High | Confirmed | The active dashboard or statistics view shall auto-refresh every 15 seconds to support near-real-time operational monitoring. | D4, D8.1, D8.8; BP1, BP8 |
| NFR-002 | Medium | Confirmed | Dashboard search shall use a 300 ms debounce before issuing a new search request. | D5, D8.2; BP2 |
| NFR-003 | Low | Confirmed | In demo or training mode, the simulator shall recalculate statuses every 30 seconds and may introduce random delay changes between UI refresh cycles. | D2, D8.9; BP9 |
| NFR-004 | High | Confirmed | The solution shall provide structured user-visible error handling for validation failures, not-found conditions, and unexpected server errors. | D11, D10; BP-CR |
| NFR-005 | Medium | Confirmed | The solution shall expose application health and monitoring endpoints for operational observability. | D7, D11; BP-OPS |
| NFR-006 | High | Unresolved | The target solution shall define explicit business timezone handling for date-time entry, display, serialization, and validation. | D10, D12; BP4, BP-FU |
| NFR-007 | High | Unresolved | The target solution shall define persistence and restart-recovery expectations for operational records and statistics. | D2, D12; BP1, BP-FU |

### 4.4 Business rules
| ID | Priority | Confidence | Requirement | Traceability |
|---|---|---|---|---|
| BRULE-001 | High | Confirmed | Flight number shall be required and shall match 2-3 uppercase letters followed by 1-4 digits. | D9; BP3 |
| BRULE-002 | High | Confirmed | Airline shall be required when creating a flight. | D9; BP3 |
| BRULE-003 | High | Confirmed | Origin and destination shall each be required 3-letter uppercase IATA codes. | D9; BP3 |
| BRULE-004 | High | Confirmed | Scheduled departure shall be required when creating a flight. | D9; BP3 |
| BRULE-005 | High | Confirmed | The system shall reject a create request when the same flight number already exists for the same scheduled departure timestamp. | D9; BP3 |
| BRULE-006 | High | Confirmed | A new ETD value shall be required for ETD revision. | D9; BP4 |
| BRULE-007 | High | Confirmed | The system shall reject an ETD value that is more than 5 minutes in the past relative to server time. | D9; BP4 |
| BRULE-008 | Medium | Confirmed | Calculated delay minutes shall never be negative, even when ETD is earlier than scheduled departure. | D6.1, D9; BP4 |
| BRULE-009 | High | Confirmed | A flight shall be considered delayed only when ETD is at least 15 minutes later than scheduled departure. | D6.1, D9; BP4, BP-CR |
| BRULE-010 | High | Confirmed | Terminal statuses `CANCELLED`, `DIVERTED`, and `DEPARTED` shall be preserved during automatic status recomputation. | D9; BP-CR |
| BRULE-011 | High | Confirmed | If current time is past ETD, computed status shall become `DEPARTED`. | D9; BP-CR |
| BRULE-012 | High | Confirmed | If current time is within 30 minutes of ETD, a non-delayed flight shall become `BOARDING` while a delayed flight shall remain `DELAYED`. | D9; BP-CR |
| BRULE-013 | High | Confirmed | Outside the boarding window, the computed status shall be `DELAYED` for delayed flights and `SCHEDULED` for on-time flights. | D9; BP-CR |
| BRULE-014 | Medium | Confirmed | Cancellation notes shall be stored in the `delayNotes` field because no dedicated cancellation-reason field exists in the current implementation. | D9, D12; BP5 |

### 4.5 Data requirements
| ID | Priority | Confidence | Requirement | Traceability |
|---|---|---|---|---|
| DR-001 | High | Confirmed | Each flight record shall store flight number, airline, aircraft type, origin, destination, gate, scheduled departure, estimated departure, actual departure, status, delay reason, delay notes, and last updated timestamp. | D6.1; BP1-BP6 |
| DR-002 | Medium | Confirmed | The system shall assign a UUID when a flight is saved without an existing identifier. | D6.1; BP3 |
| DR-003 | High | Confirmed | Supported flight statuses shall be `SCHEDULED`, `BOARDING`, `DEPARTED`, `DELAYED`, `CANCELLED`, and `DIVERTED`. | D6.2; BP6, BP-FU |
| DR-004 | Medium | Confirmed | Supported delay reasons shall be Weather, Air Traffic Control, Mechanical Issue, Crew Availability, Security, Late Arriving Aircraft, Fueling, Catering, Baggage Handling, and Other. | D6.2, D8.4; BP4 |
| DR-005 | Medium | Confirmed | Statistics data shall provide totalFlights, onTimeCount, delayedCount, cancelledCount, onTimePercentage, averageDelayMinutes, countByStatus, countByDelayReason, and countByAirline. | D6.3, D8.8; BP8 |

### 4.6 Integration requirements
| ID | Priority | Confidence | Requirement | Traceability |
|---|---|---|---|---|
| INT-001 | High | Confirmed | The browser UI shall integrate with the backend through REST/JSON endpoints under `/api`, including flight-management and statistics retrieval operations. | D7, D10; BP1-BP8 |
| INT-002 | High | Confirmed | Error responses consumed by the UI shall expose a top-level `message` and, for validation failures, optional `fieldErrors`. | D10, D11; BP3, BP4 |
| INT-003 | Low | Confirmed | The current implementation shall allow cross-origin access to `/api/**` from any origin. | D10; BP-FU |

### 4.7 Reporting requirements
| ID | Priority | Confidence | Requirement | Traceability |
|---|---|---|---|---|
| RPT-001 | Medium | Confirmed | The solution shall report current KPI values for total flights, on-time flights, delayed flights, cancelled flights, on-time percentage, and average delay. | D4, D6.3, D8.8; BP1, BP8 |
| RPT-002 | Medium | Confirmed | The solution shall provide visual reports showing flight distribution by status, delay reason, and airline. | D4, D6.3, D8.8; BP8 |

### 4.8 Security requirements
| ID | Priority | Confidence | Requirement | Traceability |
|---|---|---|---|---|
| SEC-001 | High | Unresolved | The target solution shall define authorization boundaries for viewing flights and performing create, update, cancel, depart, and delete actions. | D3, D12; BP1-BP8 |
| SEC-002 | Medium | Unresolved | The target solution shall define whether operator and supervisor responsibilities require distinct roles or approval controls. | D3, D12; BP1, BP8 |
| SEC-003 | Medium | Unresolved | The target solution shall define the acceptable cross-origin browser access policy before production deployment. | D10, D12; BP8 |

### 4.9 Audit requirements
| ID | Priority | Confidence | Requirement | Traceability |
|---|---|---|---|---|
| AUD-001 | High | Unresolved | The target solution shall define and implement an audit trail for ETD changes, cancellations, departures, and deletions. | D12; BP4-BP8, BP-FU |
| AUD-002 | High | Unresolved | The target solution shall define retention, recovery, and historical-record expectations for deleted and restarted operational data. | D8.7, D12; BP7, BP-FU |

## 5. Business process to requirement mapping
| Business process | Primary requirement IDs |
|---|---|
| BP1 Monitor current departures | BR-001, FR-001, FR-002, FR-003, FR-004, NFR-001, RPT-001 |
| BP2 Search, filter, and prioritize flights | BR-002, FR-005, FR-006, FR-007, NFR-002 |
| BP3 Register a new flight for monitoring | BR-003, FR-008, FR-009, BRULE-001, BRULE-002, BRULE-003, BRULE-004, BRULE-005, DR-001, DR-002 |
| BP4 Update ETD and record delay context | BR-004, FR-010, FR-011, BRULE-006, BRULE-007, BRULE-008, BRULE-009, DR-004, AUD-001 |
| BP5 Cancel a flight | BR-005, FR-012, BRULE-010, BRULE-014, AUD-001 |
| BP6 Mark a flight departed | BR-006, FR-013, FR-014, BRULE-010, BRULE-011, BRULE-012, BRULE-013, DR-001 |
| BP7 Delete a flight record | BR-009, FR-016, AUD-002, SEC-001 |
| BP8 Review operational statistics | BR-007, FR-015, NFR-005, DR-005, RPT-001, RPT-002 |
| BP9 Simulate live operational activity | BR-008, NFR-003 |

## 6. Discovery findings with the strongest downstream impact
- **Access control gap:** No implemented auth/authorization translated into unresolved security requirements `SEC-001` to `SEC-003`.
- **Durability gap:** In-memory-only storage translated into unresolved non-functional and audit requirements `NFR-007`, `AUD-002`.
- **Audit gap:** No history for ETD, cancel, depart, or delete translated into `AUD-001`.
- **Delete ambiguity:** Confirmed technical capability but unresolved business legitimacy translated into `BR-009` and `FR-016`.
- **Time-handling ambiguity:** Local browser entry plus UTC server serialization translated into `NFR-006`.
- **DIVERTED gap:** Supported status exists in data model, but no end-user workflow was found; this remains a BRD follow-up item rather than a confirmed requirement.

## 7. Open BRD follow-up items
1. Decide whether permanent delete is a valid business operation or only a demo/admin convenience.
2. Decide whether flights may auto-transition to `DEPARTED` or must always be explicitly confirmed.
3. Define the business workflow, actors, and rules for `DIVERTED` flights.
4. Define role segregation, authorization, and approval requirements.
5. Define persistence, audit retention, recovery, and historical reporting expectations.
6. Define timezone and operational-calendar rules for data entry, validation, display, and reporting.
