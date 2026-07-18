# Step 1 — Planning Document

**Run ID:** `2026-07-18T22-29-21Z`  
**Agent:** brd-planning  
**Pipeline Step:** 1 of 8 — Plan  
**Supersedes:** `docs/brd_runs/2026-07-18T21-08-41Z/01_planning.md`

---

## 1. Objective

Produce a **version 2** reverse-engineered Business Requirements Document (BRD) for the
`etd-airlines_1` subdirectory of the repository
`PadmavathyBala/SeleniumPractise_OpenCartProject`.

This run incorporates all corrections and additions identified in the version 1
review document (`docs/brd_runs/2026-07-18T21-08-41Z/06_review_reflect.md`).

Data source: **codebase only** — no Jira tickets, no external stakeholder
interviews, no production telemetry.

---

## 2. Version 1 Issues to Address

The following open issues from `06_review_reflect.md` of run `2026-07-18T21-08-41Z`
are the primary drivers of this version 2 run:

| Issue ID | Severity | Summary | Action in v2 |
|---|---|---|---|
| ISSUE-01 | High | UC-001 Alternative Flow ambiguity — search vs. filter API call | Rewrite Alternative Flow to clearly separate cases |
| ISSUE-02 | High | No server-side guard on ETD update for DEPARTED/CANCELLED flights | Add explicit Unresolved note in UC-002 and Appendix C |
| ISSUE-03 | High | System not labelled as a reference/demo application | Add caveat in §1.1 and §2.2 |
| ISSUE-04 | Medium | CDN dependency version numbers missing from §2.6 | Add versions read from `index.html` CDN URLs |
| ISSUE-05 | Medium | Empty cancellation note should be flagged | Add OQ-011 to Appendix C |
| ISSUE-06 | Medium | `escapeHtml()` scope was overstated in NFR-015 | Narrow scope to free-text fields only |
| ISSUE-07 | Low | §4.2 should be Confirmed (no HW), not Unresolved | Change to Confirmed positive statement |

Additionally, three missing items noted in v1 review §5 are addressed:
- `WebConfig.java` CORS configuration documented
- `@EnableScheduling` on `EtdApplication.java` confirmed and cited
- Unused `findByOrigin()`, `findByDestination()`, and `findByStatus()` repository
  methods documented in traceability

---

## 3. Scope

### In Scope

- All files under `etd-airlines_1/` including:
  - Java source (`src/main/java/`)
  - Static web assets (`src/main/resources/static/`)
  - Build descriptor (`pom.xml`)
  - Application configuration (`application.properties`)
  - Unit tests (`src/test/java/`)
  - `README.md`
- Agent instruction files in `etd-airlines_1/agents/` (pipeline
  configuration meta-data only; not part of the BRD subject matter)

### Out of Scope

- `etd-airlines/` subdirectory (structurally similar predecessor version;
  differences not analysed in this run)
- `OpenCartV001/` Selenium test project
- Any Jira tickets, production databases, or live infrastructure
- Grounding documents (`agents/Grounding Documents/`) — referenced as
  structural templates only

---

## 4. Discovery Work Plan

| # | Area | Artefacts to Examine |
|---|---|---|
| 1 | Domain model | `model/Flight.java`, `model/FlightStatus.java`, `model/DelayReason.java` |
| 2 | Data layer | `repository/FlightRepository.java` (incl. unused methods) |
| 3 | Business logic | `service/FlightService.java`, `service/EtdCalculationService.java`, `service/FlightSimulationService.java`, `service/StatisticsService.java` |
| 4 | REST API contract | `controller/FlightController.java`, `controller/StatisticsController.java` |
| 5 | Request/response payloads | `dto/CreateFlightRequest.java`, `dto/UpdateEtdRequest.java`, `dto/FlightDTO.java`, `dto/StatisticsDTO.java` |
| 6 | Error handling | `exception/FlightNotFoundException.java`, `exception/GlobalExceptionHandler.java` |
| 7 | Configuration | `application.properties`, `pom.xml`, `config/WebConfig.java` (CORS) |
| 8 | Application entry point | `EtdApplication.java` (`@EnableScheduling`) |
| 9 | UI (browser client) | `static/index.html` (CDN versions), `static/js/app.js`, `static/js/dashboard.js`, `static/js/statistics.js`, `static/js/addFlight.js`, `static/js/api.js` |
| 10 | Tests | `test/java/com/airlines/etd/FlightServiceTest.java` |
| 11 | Project documentation | `README.md` |

---

## 5. Expected Outputs

| File | Content |
|---|---|
| `00_execution_log.md` | Append-only agent execution log |
| `01_planning.md` | This document |
| `02_discovery.md` | Catalogue of all artefacts, APIs, models (updated) |
| `03_business_process.md` | Business-process narratives and swim-lane descriptions (updated) |
| `04_requirements_extraction.md` | Labelled requirement items (updated; FR-029 added) |
| `05_brd_draft.md` | Full BRD v2.0 with all v1 issues resolved |
| `06_review_reflect.md` | Reviewer findings (v2 pass) |
| `07_traceability.md` | FR-to-code traceability matrix (updated for FR-029) |
| `08_validation.md` | Final pipeline validation report |

---

## 6. Confidence-Label Rules (applied throughout)

| Label | Meaning |
|---|---|
| **Confirmed** | Directly supported by code, UI, config, tests, or docs. Citation required. |
| **Inferred** | Derived from multiple observations that together imply a behaviour. Inference chain explained. |
| **Assumed** | Reasonable guess where evidence is thin. Requires stakeholder confirmation. |
| **Unresolved** | Insufficient evidence. Stated plainly. |

---

## 7. Known Constraints (re-confirmed from v1)

- No persistence layer — all data is in-memory (`ConcurrentHashMap`).
  **Confirmed** (`FlightRepository.java` line 18).
- No authentication or role-based access control found in the codebase.
  **Confirmed** (no Spring Security dependency in `pom.xml`).
- Simulation data only; no real ATC/weather integration.
  **Confirmed** (comment in `EtdCalculationService.java` line 11).
- Single test class with 2 tests; no integration tests.
  **Confirmed** (`FlightServiceTest.java`).
- CORS configured to allow all origins on `/api/**`.
  **Confirmed** (`config/WebConfig.java` line 19 — `allowedOrigins("*")`).
