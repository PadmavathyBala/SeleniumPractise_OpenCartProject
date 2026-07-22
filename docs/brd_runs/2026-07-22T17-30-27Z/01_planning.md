# BRD Analysis Plan — etd-airlines_1

**Run ID:** 2026-07-22T17-30-27Z  
**Target:** `etd-airlines_1`  
**Prepared by:** `brd-planning`  
**Repository path:** `/home/runner/work/SeleniumPractise_OpenCartProject/SeleniumPractise_OpenCartProject/etd-airlines_1`

## 1. Planning Objective
Create a structured reverse-engineering plan for the ETD Airlines application so downstream agents can extract a complete Business Requirements Document (BRD) from the implementation artifacts that are actually present in the repository.

## 2. High-Level Application Snapshot
ETD Airlines is a Spring Boot 3 / Java 17 airline departure-operations reference application with a Bootstrap/vanilla JavaScript front end. It manages flights, estimated departure times (ETD), operational statuses, delay reasons, dashboard statistics, and live simulation updates. The application appears intended for internal airline/airport operations monitoring rather than public passenger self-service.

## 3. Sources Available vs. Not Present

### Available sources to inspect
- **Source code:** Java backend under `src/main/java/com/airlines/etd`
- **UI artifacts:** HTML/CSS/JS under `src/main/resources/static`
- **Configuration:** `pom.xml`, `application.properties`, `WebConfig.java`
- **Tests:** `src/test/java/com/airlines/etd/FlightServiceTest.java`
- **Existing narrative documentation:** `README.md`
- **Operational error contract:** `GlobalExceptionHandler.java`
- **Domain enums and DTOs:** model and dto packages

### Not present in this repo
- **Database schema / migrations:** none; repository is in-memory only
- **OpenAPI / Swagger / formal API spec:** not found
- **User manuals / SOPs / training docs:** not found beyond README
- **Screenshots / design mockups:** not found
- **Production logs / sample log files:** not found
- **External interface contracts:** no weather/ATC/feed schemas present
- **Authentication / authorization design:** not implemented
- **Persistent audit/history model:** not implemented
- **Explicit BRD / requirements baseline:** not found beyond README prose and code behavior

## 4. Key Components to Analyze

### Backend modules
- **Entry point:** `EtdApplication.java` enables Spring Boot and scheduled tasks
- **Controllers:**
  - `FlightController` for CRUD and operational flight actions
  - `StatisticsController` for dashboard aggregates
- **Services:**
  - `FlightService` orchestrates core flight lifecycle operations
  - `EtdCalculationService` contains delay and status rules
  - `FlightSimulationService` seeds sample data and simulates live updates every 30 seconds
  - `StatisticsService` computes KPIs and breakdowns
- **Repository:** `FlightRepository` is thread-safe, in-memory, and non-persistent
- **Domain model:** `Flight`, `FlightStatus`, `DelayReason`
- **DTOs / validation:** `CreateFlightRequest`, `UpdateEtdRequest`, `FlightDTO`, `StatisticsDTO`
- **Error handling/config:** `GlobalExceptionHandler`, `FlightNotFoundException`, `WebConfig`

### Front-end modules
- **Shell/UI layout:** `static/index.html`
- **API client:** `static/js/api.js`
- **Dashboard behavior:** `static/js/dashboard.js`
- **Statistics visualization:** `static/js/statistics.js`
- **Flight creation workflow:** `static/js/addFlight.js`
- **Bootstrap/init/autorefresh/toasts:** `static/js/app.js`

## 5. Test Structure
- **Unit tests present:** `FlightServiceTest`
- **Coverage focus:** creating flights, default values, ETD updates, delay classification
- **Gaps in automated coverage:** controllers, UI behavior, simulation scheduler, statistics aggregation, validation edge cases, error responses, and end-to-end workflows
- **Implication for BRD work:** downstream agents must rely heavily on implementation reading and README behavior, not broad automated specification coverage

## 6. Domain Artifacts Already Visible
- **Primary business entity:** Flight
- **Flight attributes:** flight number, airline, origin, destination, gate, aircraft type, scheduled/estimated/actual departure, status, delay reason, delay notes, last updated
- **Operational statuses:** Scheduled, Boarding, Departed, Delayed, Cancelled, Diverted
- **Delay taxonomy:** Weather, ATC, Mechanical, Crew, Security, Late Inbound, Fueling, Catering, Baggage, Other
- **Business outputs:** live dashboard, KPI counts, charts, filtered flight list, ETD edits, cancellations, departures, deletion, search results
- **Seed/sample operating dataset:** 15 predefined flights with simulated evolving delays

## 7. Analysis Plan for Downstream BRD Work

### Phase 1 — Confirm business purpose and scope
**Goal:** Define what business problem this application solves and what scope boundaries are implied.

Activities:
1. Read `README.md`, `index.html`, controller routes, and service comments.
2. Determine whether the system supports dispatch/operations control, monitoring, delay management, and reporting.
3. Record in-scope capabilities vs. obviously out-of-scope functions (booking, ticketing, crew rostering, baggage systems, etc.).

Expected outputs:
- Business purpose statement
- Product scope statement
- In-scope / out-of-scope list

### Phase 2 — Identify users, actors, and stakeholders
**Goal:** Infer user roles from UI actions and backend capabilities.

Activities:
1. Inspect dashboard actions, add-flight forms, and statistics views.
2. Infer operational personas from allowed actions:
   - operations agent/controller (create/update/cancel/depart flights)
   - operations supervisor/manager (monitor KPIs and delays)
   - analyst/viewer role is possible but not enforced technically
3. Note lack of authentication/authorization as an implementation gap, not proof of a single-role business model.

Expected outputs:
- Actor catalog with responsibilities
- Role assumptions and confidence level
- Permissions matrix inferred from current implementation

### Phase 3 — Analyze modules and features
**Goal:** Break the solution into business-facing and supporting modules.

Activities:
1. Map backend endpoints to front-end features.
2. Catalog major features:
   - flight listing/searching/filtering/sorting
   - flight creation
   - ETD update with reason/notes
   - mark departed
   - cancel flight
   - delete flight
   - KPI/statistics dashboard
   - live simulation/auto-refresh
   - validation/error messaging
3. Separate core business features from technical support features (CORS, scheduling, JSON date config).

Expected outputs:
- Feature inventory
- Module-to-feature traceability table
- Candidate BRD functional requirement groups

### Phase 4 — Identify business workflows
**Goal:** Document end-to-end operational flows represented by the code.

Activities:
1. Trace primary user journeys across UI and API:
   - monitor current departures
   - search/filter flights
   - add a new flight
   - revise ETD due to disruption
   - cancel a flight
   - mark a flight as departed
   - review operational statistics
2. Trace automated workflow:
   - seed flights on startup
   - periodic status recalculation
   - random delay simulation
3. Capture trigger, input, system behavior, and resulting state changes for each flow.

Expected outputs:
- Workflow catalog
- Step-by-step process narratives
- Candidate swimlanes for actor/system interactions

### Phase 5 — Extract business rules and validations
**Goal:** Turn hard-coded rules into explicit business requirements.

Activities:
1. Review validation annotations and service logic.
2. Extract rules such as:
   - flight number format must match 2–3 uppercase letters plus 1–4 digits
   - origin/destination must be 3-letter IATA codes
   - scheduled departure is required when creating a flight
   - duplicate flight number at same scheduled departure is rejected
   - ETD cannot be more than 5 minutes in the past
   - delay threshold is 15+ minutes
   - within 30 minutes of ETD, status becomes Boarding unless delayed
   - cancelled/diverted/departed statuses are terminal for auto-computation
3. Capture derived/statistical rules and exception behaviors.

Expected outputs:
- Business rules register
- Validation rules table
- Error/exception scenarios

### Phase 6 — Identify integrations and data flows
**Goal:** Describe how data moves through the application and what external integrations are real vs. simulated.

Activities:
1. Map browser → REST API → service → repository → DTO/UI flow.
2. Document internal scheduled simulation as a pseudo-integration replacing real operational feeds.
3. Identify declared-but-not-implemented external dependencies (weather, ATC, aircraft tracking) from code comments and README.
4. Note actuator health endpoint as operational support, not core business workflow.

Expected outputs:
- Context/data flow summary
- Integration inventory with status (`implemented`, `simulated`, `future/placeholder`)
- Data lifecycle notes for flight state transitions

### Phase 7 — Capture assumptions, gaps, and open questions
**Goal:** Make limitations explicit before BRD authoring.

Activities:
1. Record inferred assumptions from missing artifacts.
2. Flag missing decisions, including:
   - who is authorized to perform destructive actions
   - whether delete is a real business action or admin/test convenience
   - whether diverted flights should be operationally managed via UI
   - timezone handling expectations for multi-airport operations
   - persistence/audit/history requirements
   - distinction between internal operations use and customer-facing use
3. Note that statistics and seed data may be demonstration-oriented rather than production requirements.

Expected outputs:
- Assumptions list
- Risks/ambiguities list
- Open questions for stakeholders

### Phase 8 — Generate the BRD
**Goal:** Produce the business-facing BRD using discovered evidence.

Recommended BRD structure:
1. Executive summary
2. Business objective / problem statement
3. Scope
4. Stakeholders and actors
5. Current-state observations inferred from implementation
6. Functional requirements by feature area
7. Business rules and validations
8. Process/workflow narratives
9. Data/entities and important fields
10. Reporting and KPIs
11. Integrations and external dependencies
12. Non-functional observations (refresh cadence, concurrency, validation, usability)
13. Assumptions, constraints, gaps, open questions
14. Traceability appendix back to code artifacts

### Phase 9 — Review BRD for completeness
**Goal:** Verify the BRD is evidence-based and not over-claimed.

Checklist:
- Every major UI function is reflected in a requirement or workflow
- Every API action is accounted for
- Inferred roles are marked as inferred where not explicit
- Missing artifacts are explicitly called out
- Business rules are tied to code or validation evidence
- Demo/simulation behavior is distinguished from production intent
- Open questions are preserved instead of guessed away

## 8. Initial Feature/Workflow Inventory for Follow-on Agents
- View departure dashboard with live refresh
- Search flights by flight number, airline, origin, destination
- Filter flights by status
- Sort by ETD, scheduled time, delay, or flight number
- View KPIs: total, on-time, delayed, cancelled, average delay, on-time %
- Update ETD with optional delay reason and notes
- Cancel a flight with notes
- Mark a flight departed
- Delete a flight record
- Create a new flight
- View charts by status, delay reason, and airline
- Auto-seeded flights and scheduled status/delay simulation

## 9. Specific Evidence Pointers for Discovery Agents
- **Business purpose / architecture:** `README.md`
- **User-visible workflows/UI:** `src/main/resources/static/index.html`
- **API contract surface:** `src/main/java/com/airlines/etd/controller/FlightController.java`, `StatisticsController.java`
- **Core lifecycle rules:** `src/main/java/com/airlines/etd/service/FlightService.java`, `EtdCalculationService.java`
- **Simulation / pseudo-real-time behavior:** `src/main/java/com/airlines/etd/service/FlightSimulationService.java`
- **Metrics/KPIs:** `src/main/java/com/airlines/etd/service/StatisticsService.java`
- **Validation rules:** `src/main/java/com/airlines/etd/dto/CreateFlightRequest.java`, `UpdateEtdRequest.java`
- **Error response behaviors:** `src/main/java/com/airlines/etd/exception/GlobalExceptionHandler.java`
- **Existing test assertions:** `src/test/java/com/airlines/etd/FlightServiceTest.java`

## 10. Planning Summary
This repository contains enough implementation evidence to produce a solid inferred BRD from code, UI, validation rules, and README documentation. The main caution is that several business aspects are implied rather than explicitly specified: user roles, production integrations, persistence, auditability, and authorization. Downstream agents should clearly distinguish confirmed behavior from inference.
