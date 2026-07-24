# Step 4 — Structured Requirements Extraction

## Business Requirements (BR)
- **BR-001**: The organization shall maintain a live operational view of outbound flights including schedule, estimated departure, status, and delay context. **[Confirmed]** Source: `index.html:137-150`, `dashboard.js:24-35`.
- **BR-002**: The organization shall allow operations staff to create new flight records for departure management. **[Confirmed]** Source: `FlightController.java:42-46`, `index.html:193-243`.
- **BR-003**: The organization shall allow authorized operators (business role assumed; technical authorization absent) to modify ETD and operational state transitions. **[Inferred]** Source chain: `dashboard.js:111-158`, `FlightService.java:65-100`.
- **BR-004**: The organization shall provide aggregate operational statistics for tactical monitoring. **[Confirmed]** Source: `StatisticsService.java:28-83`.
- **BR-005**: The system should support auditability of state changes with retained actor identity and timestamps beyond current runtime session. **[Assumed]** Rationale: common airline ops expectation; not implemented.

## Functional Requirements (FR)
- **FR-001**: The system shall expose a flight listing endpoint supporting optional text search. **[Confirmed]** `FlightController.java:29-35`.
- **FR-002**: The system shall return individual flight details by identifier. **[Confirmed]** `FlightController.java:37-40`.
- **FR-003**: The system shall create flights from validated request payloads and return HTTP 201. **[Confirmed]** `FlightController.java:42-46`, `CreateFlightRequest.java:14-35`.
- **FR-004**: The system shall reject duplicate flight-number/scheduled-time combinations. **[Confirmed]** `FlightService.java:43-50`.
- **FR-005**: The system shall allow ETD updates with optional delay reason and notes. **[Confirmed]** `FlightController.java:48-52`, `UpdateEtdRequest.java:13-28`.
- **FR-006**: The system shall reject ETD updates more than five minutes in the past. **[Confirmed]** `FlightService.java:68-71`.
- **FR-007**: The system shall recalculate operational status after ETD updates according to status engine rules. **[Confirmed]** `FlightService.java:81-84`, `EtdCalculationService.java:20-41`.
- **FR-008**: The system shall support canceling a flight and storing cancellation notes. **[Confirmed]** `FlightController.java:54-59`, `FlightService.java:88-93`.
- **FR-009**: The system shall support marking a flight as departed and recording actual departure timestamp. **[Confirmed]** `FlightService.java:95-100`.
- **FR-010**: The system shall support deleting flights by identifier. **[Confirmed]** `FlightController.java:66-70`.
- **FR-011**: The system shall provide a statistics endpoint with totals, percentages, and grouped counts. **[Confirmed]** `StatisticsController.java:22-25`, `StatisticsService.java:32-83`.
- **FR-012**: The dashboard shall auto-refresh operational data every 15 seconds while active. **[Confirmed]** `app.js:6-31`.
- **FR-013**: The simulator shall execute a 30-second periodic cycle to update statuses and optionally add delays. **[Confirmed]** `FlightSimulationService.java:96-127`.
- **FR-014**: The UI shall present row-level actions for ETD edit, depart, cancel, and delete. **[Confirmed]** `dashboard.js:111-158`.

## Business Rules (BRULE)
- **BRULE-001**: Delay minutes shall be computed as max(0, ETD - scheduled departure) in minutes. **[Confirmed]** `Flight.java:51-57`.
- **BRULE-002**: A flight is considered delayed when delay is 15 minutes or greater. **[Confirmed]** `Flight.java:60-64`.
- **BRULE-003**: Terminal statuses (CANCELLED, DIVERTED, DEPARTED) shall not be overwritten by status recomputation. **[Confirmed]** `EtdCalculationService.java:21-25`.
- **BRULE-004**: Flights within 30 minutes of ETD shall be BOARDING unless delayed, in which case DELAYED remains. **[Confirmed]** `EtdCalculationService.java:35-38`.
- **BRULE-005**: On-time percentage denominator includes all flights; cancelled flights are excluded from on-time count logic. **[Confirmed]** `StatisticsService.java:35-47`.

## Non-Functional Requirements (NFR)
- **NFR-001**: The system shall provide near-real-time situational updates through 15-second client polling. **[Confirmed]** `app.js:6-31`.
- **NFR-002**: The system shall expose health/metrics endpoints for operational monitoring. **[Confirmed]** `application.properties:18-19`.
- **NFR-003**: The system shall maintain thread-safe in-memory data operations under concurrent access. **[Confirmed]** `FlightRepository.java:18`, `FlightRepository.java:20-40`.
- **NFR-004**: The system shall persist flight and change history across restarts for continuity. **[Unresolved]** Not implemented in current architecture.

## Integration Requirements (INT)
- **INT-001**: The web frontend shall integrate with backend REST services under `/api` using JSON payloads. **[Confirmed]** `api.js:5-37`.
- **INT-002**: The backend shall allow cross-origin requests to `/api/**` with GET/POST/PUT/DELETE/OPTIONS methods. **[Confirmed]** `WebConfig.java:19-23`.
- **INT-003**: The ETD engine shall integrate with external weather/ATC feeds to improve delay forecasting. **[Assumed]** Mentioned as future-state in `README.md:103`, not implemented.

## Data Requirements (DR)
- **DR-001**: Each flight record shall maintain id, flight identity, route, gate, aircraft, schedule/estimated/actual times, status, delay reason/notes, and last-updated timestamp. **[Confirmed]** `Flight.java:12-26`.
- **DR-002**: Validation rules shall enforce formats for flight number and IATA codes. **[Confirmed]** `CreateFlightRequest.java:14-28`.

## Reporting Requirements (RR)
- **RR-001**: The system shall present status distribution, delay-reason distribution, and airline distribution visual analytics. **[Confirmed]** `statistics.js:25-98`.
- **RR-002**: The system shall export KPI reports in downloadable format. **[Unresolved]** No export mechanism found.

## Security Requirements (SR)
- **SR-001**: Access to mutation operations shall require authenticated and role-authorized users. **[Unresolved]** No auth implementation found.
- **SR-002**: API CORS policy shall be restricted to approved origins in production. **[Assumed]** Current config uses wildcard origin.

## Audit Requirements (AR)
- **AR-001**: The system shall capture who changed ETD/status and when, with historical retention. **[Unresolved]** No actor identity/audit trail implemented.
