# Step 1 — Planning Document

**Run ID:** `2026-07-18T21-08-41Z`  
**Agent:** brd-planning  
**Pipeline Step:** 1 of 8 — Plan

---

## 1. Objective

Produce a reverse-engineered Business Requirements Document (BRD) for the
`etd-airlines_1` subdirectory of the repository
`PadmavathyBala/SeleniumPractise_OpenCartProject`.

Data source: **codebase only** — no Jira tickets, no external stakeholder
interviews, no production telemetry.

---

## 2. Scope

### In Scope

- All files under `etd-airlines_1/` including:
  - Java source (`src/main/java/`)
  - Static web assets (`src/main/resources/static/`)
  - Build descriptor (`pom.xml`)
  - Application configuration (`application.properties`)
  - Unit tests (`src/test/java/`)
  - `README.md`
- Agent instruction files in `etd-airlines_1/agents/` (used as pipeline
  configuration meta-data only; their contents are not part of the BRD subject
  matter)

### Out of Scope

- `etd-airlines/` subdirectory (a structurally identical earlier version;
  differences, if any, are not analysed in this run)
- `OpenCartV001/` Selenium test project
- Any Jira tickets, production databases, or live infrastructure
- Grounding documents (`agents/Grounding Documents/`) — referenced as
  structural templates only

---

## 3. Discovery Work Plan

| # | Area | Artefacts to Examine |
|---|---|---|
| 1 | Domain model | `model/Flight.java`, `model/FlightStatus.java`, `model/DelayReason.java` |
| 2 | Data layer | `repository/FlightRepository.java` |
| 3 | Business logic | `service/FlightService.java`, `service/EtdCalculationService.java`, `service/FlightSimulationService.java`, `service/StatisticsService.java` |
| 4 | REST API contract | `controller/FlightController.java`, `controller/StatisticsController.java` |
| 5 | Request/response payloads | `dto/CreateFlightRequest.java`, `dto/UpdateEtdRequest.java`, `dto/FlightDTO.java`, `dto/StatisticsDTO.java` |
| 6 | Error handling | `exception/FlightNotFoundException.java`, `exception/GlobalExceptionHandler.java` |
| 7 | Configuration | `application.properties`, `pom.xml`, `config/WebConfig.java` |
| 8 | UI (browser client) | `static/index.html`, `static/js/app.js`, `static/js/dashboard.js`, `static/js/statistics.js`, `static/js/addFlight.js`, `static/js/api.js` |
| 9 | Tests | `test/java/com/airlines/etd/FlightServiceTest.java` |
| 10 | Project documentation | `README.md` |

---

## 4. Expected Outputs

| File | Content |
|---|---|
| `00_execution_log.md` | Append-only agent execution log |
| `01_planning.md` | This document |
| `02_discovery.md` | Catalogue of all artefacts, APIs, models |
| `03_business_process.md` | Business-process narratives and swim-lane descriptions |
| `04_requirements_extraction.md` | Labelled requirement items (Confirmed/Inferred/Assumed/Unresolved) |
| `05_brd_draft.md` | Full BRD following the merged template structure |
| `06_review_reflect.md` | Reviewer findings and recommended fixes |
| `07_traceability.md` | FR-to-code traceability matrix |
| `08_validation.md` | Final pipeline validation report |

---

## 5. Confidence-Label Rules (applied throughout)

| Label | Meaning |
|---|---|
| **Confirmed** | Directly supported by code, UI, config, tests, or docs. Citation required. |
| **Inferred** | Derived from multiple observations that together imply a behaviour. Inference chain explained. |
| **Assumed** | Reasonable guess where evidence is thin. Requires stakeholder confirmation. |
| **Unresolved** | Insufficient evidence. Stated plainly. |

---

## 6. Known Constraints

- No persistence layer — all data is in-memory (`ConcurrentHashMap`).
  **Confirmed** (`FlightRepository.java` line 14).
- No authentication or role-based access control found in the codebase.
  **Confirmed** (no Spring Security dependency in `pom.xml`).
- Simulation data only; no real ATC/weather integration.
  **Confirmed** (comment in `EtdCalculationService.java`).
- Single test class with 2 tests; no integration tests.
  **Confirmed** (`FlightServiceTest.java`).
