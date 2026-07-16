# Step 1 — Analysis Plan (brd-planning)

**Run ID:** `2026-07-16T03-46-24Z`  
**Agent:** brd-planning  
**Scope:** `etd-airlines_1` subdirectory

---

## 1. Application Business Purpose

The application is the **ETD Airlines Departure Operations System** — a Spring Boot + HTML/JavaScript web application that helps airport departure operations staff manage flight schedules, estimated times of departure (ETD), delays, cancellations, and real-time flight status tracking for a single departure terminal.

---

## 2. Analysis Objectives

The following 9 objectives will guide the analysis:

1. **Business Purpose** — Identify who uses the system, what business problem it solves, and what the expected business outcomes are.
2. **User Roles and Actors** — Identify all system users, their permissions, and how they interact with the system.
3. **Modules and Features** — Map all functional modules present in the code.
4. **Business Workflows** — Trace key workflows from trigger event to business outcome.
5. **Business Rules and Validations** — Extract all explicit and implicit rules enforced by the system.
6. **Integrations and Data Flows** — Document all data inputs, outputs, and any external integration points.
7. **Assumptions, Gaps, and Open Questions** — Catalogue any behavior that is inferred rather than directly confirmed.
8. **BRD Generation** — Produce a complete 23-section BRD from the discovery and business-process outputs.
9. **BRD Review** — Perform a senior BA review for completeness, clarity, and testability; revise as needed.

---

## 3. Available Sources

The following source types are **present and inspectable** in the repository:

| Source Type | Location | Contents |
|-------------|----------|----------|
| Java source code | `src/main/java/com/airlines/etd/` | Controllers, services, repository, models, DTOs, exceptions, config |
| Frontend code | `src/main/resources/static/` | index.html, CSS, JS modules (api.js, app.js, dashboard.js, statistics.js, addFlight.js) |
| Application configuration | `src/main/resources/application.properties` | Server port, Jackson config, actuator, logging |
| Unit tests | `src/test/java/com/airlines/etd/FlightServiceTest.java` | 2 test scenarios covering flight creation and ETD update |
| Build descriptor | `pom.xml` | Maven dependencies (Spring Boot 3.2, Spring Validation, Actuator) |
| Project documentation | `README.md` | Architecture, REST API table, project layout, extension suggestions |
| Agent definition files | `agents/` | BRD pipeline agent instructions (not business content) |

The following source types are **NOT present** in this repository:

- Database schema / migration scripts (uses in-memory store only)
- OpenAPI / Swagger specification
- Jira or any external requirements management artefacts
- Existing BRD or requirements documents
- Screenshots or UI mockups
- Application log files
- Deployment / infrastructure configuration
- Authentication/authorization configuration (no Spring Security present)

---

## 4. Source Inspection Sequence

The Discovery agent should read sources in this order for maximum efficiency:

1. `README.md` — understand stated purpose and REST API
2. `model/` — understand domain entities and enumerations
3. `dto/` — understand API request/response contracts
4. `controller/` — map REST endpoints to user operations
5. `service/` — extract business logic, workflows, and rules
6. `repository/` — understand data access patterns
7. `exception/` — document error scenarios
8. `config/` — note any cross-cutting configuration
9. `src/main/resources/static/index.html` — identify UI screens and navigation
10. `src/main/resources/static/js/` — identify user interactions, validation, and rendering rules
11. `src/main/resources/application.properties` — note runtime constraints
12. `src/test/java/` — identify tested behaviors and acceptance criteria
13. `pom.xml` — note technology dependencies and version constraints

---

## 5. Expected BRD Structure (23 Sections)

The final BRD will follow this exact structure:

1. Document Purpose
2. Executive Summary
3. Business Background
4. Business Objectives
5. Scope
6. Stakeholders
7. User Roles
8. Current-State Overview
9. Business Processes
10. Business Requirements (BR-###)
11. Functional Requirements (FR-###)
12. Business Rules (BRULE-###)
13. Data Requirements
14. Integration Requirements (INT-###)
15. Non-Functional Requirements (NFR-###)
16. Assumptions
17. Constraints
18. Dependencies
19. Risks
20. Out-of-Scope Items
21. Open Questions
22. Acceptance Criteria
23. Traceability Matrix (placeholder; filled by Traceability agent)

---

## 6. Confidence Labeling Convention

All findings, processes, and requirements shall carry one of the following labels:

| Label | Meaning |
|-------|---------|
| **Confirmed** | Directly evidenced by code, config, or documentation with a specific citation |
| **Inferred** | Logically implied by confirmed evidence but not explicitly stated |
| **Assumed** | Reasonable assumption with no direct evidence; must be validated with stakeholders |
| **Unresolved** | Contradictory signals or completely absent evidence; requires clarification |

---

## 7. Known Constraints

- No external integrations exist in the current codebase (weather, ATC, etc. are simulated or placeholders).
- No authentication or authorization is present; the system currently grants full operator access to anyone with browser access.
- Data is non-persistent; all flight data is lost on application restart.
- The system is designed as a reference/training application, but will be documented as if it were a production-intent system, noting implementation gaps as Assumed or Unresolved requirements.

---

## 8. Open Questions (Pre-Discovery)

1. Who are the named stakeholders for this system?
2. Are there other user roles beyond a generic "operator" (e.g., gate agent, supervisor, viewer-only)?
3. Are there SLA or performance requirements not expressed in the code?
4. Is there a planned migration from in-memory to a persistent database?
5. Are there accessibility (WCAG) requirements for the UI?

---

*Plan complete. Saved to `docs/brd_runs/2026-07-16T03-46-24Z/01_planning.md`.*
