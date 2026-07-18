# Step 7 — Traceability Matrix

**Run ID:** `2026-07-18T22-29-21Z`  
**Agent:** brd-traceability  
**Pipeline Step:** 7 of 8 — Trace  
**Supersedes:** `docs/brd_runs/2026-07-18T21-08-41Z/07_traceability.md`

All citations refer to files under `etd-airlines_1/src/`.  
Changes from v1 are marked **[NEW v2]**.

---

## 1. Functional Requirements Traceability

| FR-ID | Requirement Summary | Source File(s) | Class / Method | Test Coverage | Confidence |
|---|---|---|---|---|---|
| FR-001 | Create new flight with required fields | `controller/FlightController.java` | `FlightController.create()` | TS-001 | Confirmed |
| FR-002 | Flight number regex validation | `dto/CreateFlightRequest.java` | `@Pattern` on `flightNumber` | None | Confirmed |
| FR-003 | Origin/destination IATA validation | `dto/CreateFlightRequest.java` | `@Pattern` on `origin`, `destination` | None | Confirmed |
| FR-004 | Duplicate flight number + time rejected | `service/FlightService.java` | `FlightService.createFlight()` lines 43–50 | None | Confirmed |
| FR-005 | New flight ETD = STD, delay = 0 | `service/FlightService.java` | `FlightService.createFlight()` line 60 | TS-001 | Confirmed |
| FR-006 | UUID auto-assigned on creation | `repository/FlightRepository.java` | `FlightRepository.save()` line 22 — `UUID.randomUUID()` | TS-001 | Confirmed |
| FR-007 | List all flights with optional text search | `controller/FlightController.java`, `service/FlightService.java` | `FlightController.listAll()`, `FlightService.searchFlights()` | None | Confirmed |
| FR-008 | Get single flight by ID; 404 on miss | `controller/FlightController.java`, `exception/FlightNotFoundException.java` | `FlightController.getOne()`, `FlightNotFoundException` | None | Confirmed |
| FR-009 | Delete flight by ID; 404 on miss | `controller/FlightController.java`, `service/FlightService.java` | `FlightController.delete()`, `FlightService.deleteFlight()` | None | Confirmed |
| FR-010 | Update ETD with optional reason + notes | `controller/FlightController.java`, `dto/UpdateEtdRequest.java` | `FlightController.updateEtd()` | TS-002 | Confirmed |
| FR-011 | New ETD must not be >5 min in past | `service/FlightService.java` | `FlightService.updateEtd()` lines 68–71 | None | Confirmed |
| FR-012 | Status auto-recomputed after ETD update | `service/FlightService.java`, `service/EtdCalculationService.java` | `FlightService.updateEtd()` line 82 → `computeStatus()` | TS-002 | Confirmed |
| FR-013 | Delay threshold = 15 minutes | `model/Flight.java` | `Flight.isDelayed()` | TS-002 | Confirmed |
| FR-014 | Delay reasons: 10 categories defined | `model/DelayReason.java` | `DelayReason` enum | TS-002 (WEATHER used) | Confirmed |
| FR-015 | Estimated additional delay per reason | `service/EtdCalculationService.java` | `EtdCalculationService.estimateAdditionalDelay()` lines 49–60 | None | Confirmed |
| FR-016 | `applyDelay()` bumps ETD, sets status=DELAYED | `service/EtdCalculationService.java` | `EtdCalculationService.applyDelay()` lines 67–73 | None (used in seed) | Confirmed |
| FR-017 | Status lifecycle: SCHEDULED→BOARDING→DEPARTED | `service/EtdCalculationService.java` | `EtdCalculationService.computeStatus()` lines 31–40 | None | Confirmed |
| FR-018 | Delayed flight shows DELAYED not BOARDING | `service/EtdCalculationService.java` | `EtdCalculationService.computeStatus()` lines 36, 40 | None | Confirmed |
| FR-019 | Terminal states not recomputed | `service/EtdCalculationService.java` | `EtdCalculationService.computeStatus()` lines 21–25 — entry guard | None | Confirmed |
| FR-020 | Mark departed + record ATD | `service/FlightService.java` | `FlightService.markDeparted()` lines 96–99 | None | Confirmed |
| FR-021 | Cancel flight with optional note | `service/FlightService.java` | `FlightService.cancelFlight()` lines 88–93 | None | Confirmed |
| FR-022 | Aggregated statistics computation | `service/StatisticsService.java`, `dto/StatisticsDTO.java` | `StatisticsService.buildStatistics()` | None | Confirmed |
| FR-023 | Statistics available at GET /api/statistics | `controller/StatisticsController.java` | `StatisticsController.getStatistics()` | None | Confirmed |
| FR-024 | Statistics rendered as charts in UI | `static/js/statistics.js` | `Statistics.renderStatusChart()`, `renderReasonChart()`, `renderAirlineChart()` | None | Confirmed |
| FR-025 | Dashboard tab with KPIs and flight table | `static/js/dashboard.js`, `static/index.html` | `Dashboard.init()`, `Dashboard.render()`, `updateKpis()` | None | Confirmed |
| FR-026 | Status filter (client-side) + sort (client-side) + search box (API call) | `static/js/dashboard.js` | `Dashboard.render()` statusFilter/sortBy client-side; `init()` debounce for search API call | None | Confirmed |
| FR-027 | 15-second auto-refresh | `static/js/app.js` | `App.startAutoRefresh()`, `REFRESH_INTERVAL_MS=15000` | None | Confirmed |
| FR-028 | Add Flight tab with form + default time | `static/js/addFlight.js`, `static/index.html` | `AddFlight.init()`, `AddFlight.submit()` | None | Confirmed |
| FR-029 | CORS wildcard on /api/** | `main/java/com/airlines/etd/config/WebConfig.java` | `WebConfig.addCorsMappings()` lines 18–23 | None | Confirmed — **[NEW v2]** |

---

## 2. Non-Functional Requirements Traceability

| NFR-ID | Requirement Summary | Source File | Evidence | Confidence |
|---|---|---|---|---|
| NFR-001 | Thread-safe data store | `repository/FlightRepository.java` | `ConcurrentHashMap<String, Flight>` line 18 | Confirmed |
| NFR-002 | ISO-8601 date serialization | `src/main/resources/application.properties` | `spring.jackson.serialization.write-dates-as-timestamps=false` | Confirmed |
| NFR-003 | UTC timezone for all datetimes | `src/main/resources/application.properties` | `spring.jackson.time-zone=UTC` | Confirmed |
| NFR-004 | Actuator endpoints exposed | `src/main/resources/application.properties` | `management.endpoints.web.exposure.include=health,info,metrics` | Confirmed |
| NFR-005 | Default port 8080 | `src/main/resources/application.properties` | `server.port=8080` | Confirmed |
| NFR-006 | No authentication/authorisation; CORS open | `pom.xml`, `config/WebConfig.java` | No `spring-boot-starter-security`; `allowedOrigins("*")` — **[v2 update]** | Confirmed |
| NFR-007 | Non-persistent in-memory store | `repository/FlightRepository.java` | `private final Map<String, Flight> flights = new ConcurrentHashMap<>()` line 18 | Confirmed |
| NFR-008 | Java 17 and Maven 3.6+ required | `pom.xml`, `README.md` | `<java.version>17</java.version>` | Confirmed |
| NFR-013 | `escapeHtml()` applied to free-text fields only | `static/js/dashboard.js`, `dto/CreateFlightRequest.java` | `escapeHtml()` on `airline`, `gate`; regex constraints on `flightNumber`, `origin`, `destination` — **[v2 update]** | Confirmed |
| NFR-014 | CORS wildcard is a security risk | `config/WebConfig.java` | `allowedOrigins("*")` line 20 — **[NEW v2]** | Confirmed |

---

## 3. Use Case to Requirement Mapping

| Use Case | FRs Covered | BRs Covered |
|---|---|---|
| UC-001 View/Filter Flight Board | FR-007, FR-025, FR-026, FR-027 | BR-01, BR-02, BR-03, BR-04, BR-05 |
| UC-002 Update Flight ETD | FR-010, FR-011, FR-012, FR-013, FR-014 | BR-06, BR-07, BR-08, BR-09 |
| UC-003 Mark Flight Departed | FR-020 | BR-10, BR-11 |
| UC-004 Cancel Flight | FR-021 | BR-12, BR-13, BR-14 |
| UC-005 Add New Flight | FR-001, FR-002, FR-003, FR-004, FR-005, FR-006 | BR-15, BR-16, BR-17, BR-18, BR-19 |
| UC-006 View Statistics | FR-022, FR-023, FR-024 | BR-20, BR-21, BR-22 |

---

## 4. Partially Traced or Untraced Items

| Item | Status | Notes |
|---|---|---|
| `FlightRepository.findByOrigin()` | Untraced | Method exists; no service calls it. No FR covers this. |
| `FlightRepository.findByDestination()` | Untraced | Same as above. |
| `FlightRepository.findByStatus()` | Untraced — **[NEW v2]** | Method exists; no service calls it. `StatisticsService` uses `findAll()` and filters in-stream. Appears to be scaffolding for future features. |
| `FlightRepository.count()` | Untraced — **[NEW v2]** | Method exists; not called by any service. Scaffolding for future use. |
| `FlightRepository.deleteAll()` | Untraced — **[NEW v2]** | Method exists; not called by production code. Likely present for test teardown. |
| `FlightStatus.DIVERTED` | Untraced to a use case | Enum value exists; no code path sets it via API/service. OQ-002. |

---

## 5. Test Coverage Heatmap

| FR-ID | Unit Test | Notes |
|---|---|---|
| FR-001 | ✓ TS-001 | createFlightAssignsIdAndDefaults |
| FR-002 | ✗ | No test for invalid flight number pattern |
| FR-003 | ✗ | No test for invalid IATA code |
| FR-004 | ✗ | No test for duplicate rejection |
| FR-005 | ✓ TS-001 | ETD=STD, delay=0 assertions |
| FR-006 | ✓ TS-001 | assertNotNull(created.getId()) |
| FR-007 | ✗ | No test for list/search |
| FR-008 | ✗ | No test for get-by-ID or 404 |
| FR-009 | ✗ | No test for delete |
| FR-010 | ✓ TS-002 | updateEtdMarksFlightAsDelayed |
| FR-011 | ✗ | No test for past-ETD validation |
| FR-012 | ✓ TS-002 | Status recomputed (isDelayed=true implies DELAYED) |
| FR-013 | ✓ TS-002 | 45 min > 15 min → isDelayed() = true |
| FR-014 | ✓ TS-002 | WEATHER reason stored |
| FR-015 | ✗ | No test for `estimateAdditionalDelay()` |
| FR-016 | ✗ | No test for `applyDelay()` |
| FR-017 | ✗ | No test for status lifecycle |
| FR-018 | ✗ | No test for DELAYED status |
| FR-019 | ✗ | No test for terminal state guards |
| FR-020 | ✗ | No test for markDeparted |
| FR-021 | ✗ | No test for cancelFlight |
| FR-022 | ✗ | No test for statistics |
| FR-023 | ✗ | No controller tests |
| FR-024 | ✗ | No UI tests |
| FR-025 | ✗ | No UI tests |
| FR-026 | ✗ | No UI tests |
| FR-027 | ✗ | No UI tests |
| FR-028 | ✗ | No UI tests |
| FR-029 | ✗ | No test for CORS configuration |

**Test coverage: 5/29 FRs have any test coverage (17%)**  
**Confirmed** — single test class with 2 test methods covering FR-001, FR-005, FR-006, FR-010, FR-012, FR-013, FR-014.
