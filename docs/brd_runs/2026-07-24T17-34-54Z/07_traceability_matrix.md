# Step 7 — Traceability Matrix

| Requirement ID | Source-code component | Observed application behavior | Business process | Business requirement | Acceptance criterion | Test scenario |
|---|---|---|---|---|---|---|
| BR-001 | `index.html`, `dashboard.js` | Dashboard shows flight operational fields and status context | BP-001 Monitor Dashboard | Maintain live outbound operational view | Given dashboard load, when flights are returned, then table displays route/times/status/delay/reason columns | TS-UI-001 (manual UI check), mapped to dashboard rendering paths |
| BR-002 | `FlightController.create`, `addFlight.js` | Users can submit new flight data via Add Flight tab | BP-002 Create Flight | Allow creation of new flight records | Given valid payload, when POST /api/flights called, then response is 201 with created flight | TS-001 (`createFlightAssignsIdAndDefaults`) |
| BR-003 | no direct source — inferred | UI exposes mutation actions; service updates ETD/status | BP-003 Manage ETD/State | Allow operators to modify ETD/status | Given existing flight, when ETD is updated, then new ETD and computed status are returned | TS-002 (`updateEtdMarksFlightAsDelayed`) |
| BR-004 | `StatisticsService.buildStatistics` | Aggregate metrics generated for status/reason/airline | BP-004 Produce Statistics | Provide aggregated operational statistics | Given flights in repository, when GET /api/statistics called, then totals and grouped maps are returned | TS-STAT-001 (unresolved automated test) |
| BR-005 | no direct source — assumed | Audit persistence expectation not implemented | Cross-cutting governance | Support retained state-change auditability | Given ETD/status update, then actor/timestamp/history should be queryable beyond runtime | Unresolved (no test present) |
| FR-001 | `FlightController.listAll` | GET /api/flights lists flights with optional search | BP-001 | Live flight board retrieval | Request without search returns sorted flights; with search returns filtered list | TS-API-001 (unresolved automated test) |
| FR-002 | `FlightController.getOne` | GET /api/flights/{id} returns one flight or not-found | BP-001/BP-003 | Single-flight detail retrieval | Existing ID returns flight payload; unknown ID returns 404 | TS-API-002 (unresolved automated test) |
| FR-003 | `FlightController.create`, `CreateFlightRequest` | POST create performs validation and returns created flight | BP-002 | New flight creation | Valid request returns 201 and populated id | TS-001 |
| FR-004 | `FlightService.createFlight` | Duplicate flight number + schedule rejected | BP-002 | Prevent duplicate operational entries | Posting duplicate flight/time returns 400 with message | TS-API-003 (unresolved automated test) |
| FR-005 | `FlightController.updateEtd`, `FlightService.updateEtd` | ETD can be updated with optional reason/notes | BP-003 | Maintain ETD accuracy | PUT /etd updates estimatedDeparture and optional reason/notes | TS-002 |
| FR-006 | `FlightService.updateEtd` | ETD too far in past rejected | BP-003 | Enforce temporal validity | If new ETD < now-5min then 400 error | TS-API-004 (unresolved automated test) |
| FR-007 | `EtdCalculationService.computeStatus` | Status recalculated after ETD updates | BP-003 | Keep status aligned with ETD/time | Updated ETD leads to status derived by computation rules | TS-002 partial coverage |
| FR-008 | `FlightService.cancelFlight` | Cancel sets CANCELLED and stores notes | BP-003 | Manage cancellation operations | POST /cancel sets status CANCELLED and persists note | TS-API-005 (unresolved automated test) |
| FR-009 | `FlightService.markDeparted` | Depart marks status and actual departure time | BP-003 | Record departure completion | POST /depart sets DEPARTED and actualDeparture not null | TS-API-006 (unresolved automated test) |
| FR-010 | `FlightController.delete`, `FlightService.deleteFlight` | Delete removes record by id | BP-003 | Remove obsolete flight record | DELETE returns 204 and subsequent lookup fails | TS-API-007 (unresolved automated test) |
| FR-011 | `StatisticsController.getStatistics` | Stats endpoint returns aggregated DTO | BP-004 | Support statistics dashboard | GET /api/statistics returns total, onTime, delayed, cancelled and grouping maps | TS-STAT-001 (unresolved automated test) |
| FR-012 | `app.js` | Auto-refresh timer executes every 15s | BP-001 | Maintain near-live dashboard | Active dashboard tab triggers refresh each interval | TS-UI-002 (manual/automation pending) |
| FR-013 | `FlightSimulationService.simulateLiveUpdates` | Scheduled 30s status and random delay simulation | BP-003 operational simulation | Keep demo dataset dynamically changing | Scheduler run recomputes statuses and occasionally applies delay | TS-SVC-001 (unresolved automated test) |
| FR-014 | `dashboard.js` | Row actions: edit/depart/cancel/delete | BP-003 | Provide operational action controls | Action buttons invoke corresponding API methods | TS-UI-003 (manual/automation pending) |
| BRULE-001 | `Flight.getDelayMinutes` | Delay calculation uses non-negative minute diff | BP-001/BP-003 | Delay metric consistency | ETD earlier than schedule yields 0; later yields positive minutes | TS-001 partial |
| BRULE-002 | `Flight.isDelayed` | Delay threshold set at 15 minutes | BP-003/BP-004 | Uniform delayed classification | Delay >=15 sets delayed=true | TS-002 |
| BRULE-003 | `EtdCalculationService.computeStatus` | Terminal statuses preserved | BP-003 | Prevent invalid status override | CANCELLED/DIVERTED/DEPARTED input status remains unchanged | TS-SVC-002 (unresolved test) |
| BRULE-004 | `EtdCalculationService.computeStatus` | Within 30 min -> BOARDING unless delayed | BP-003 | Boarding-state consistency | ETD within 30 min yields BOARDING if not delayed else DELAYED | TS-SVC-003 (unresolved test) |
| BRULE-005 | `StatisticsService.buildStatistics` | On-time excludes cancelled from on-time count | BP-004 | KPI calculation consistency | On-time metric does not include cancelled flights | TS-STAT-002 (unresolved test) |
| NFR-001 | `app.js` | 15-second polling behavior | BP-001 | Near-live user awareness | Refresh interval configured at 15000ms | TS-UI-002 |
| NFR-002 | `application.properties` | health/info/metrics exposure | Support operations | Operational observability | /actuator/health available with details | TS-OPS-001 (unresolved test) |
| NFR-003 | `FlightRepository` | concurrent map used for storage | Cross-cutting data service | Thread-safe in-memory operations | Concurrent saves/reads do not corrupt map structure | TS-SVC-004 (unresolved test) |
| NFR-004 | no direct source — unresolved | Persistence across restart missing | Cross-cutting | Require continuity beyond runtime | Restart should retain prior records (currently not met) | Gap test (expected fail) |
| INT-001 | `api.js`, controllers | Frontend consumes backend JSON APIs | BP-001 to BP-004 | Browser-backend interoperability | API requests/responses succeed for supported verbs | TS-INT-001 (unresolved test) |
| INT-002 | `WebConfig.addCorsMappings` | CORS allows all origins/methods for /api | Cross-cutting | Cross-origin API accessibility | OPTIONS/GET/POST/PUT/DELETE accepted per config | TS-INT-002 (unresolved test) |
| INT-003 | no direct source — assumed | External delay feeds absent | Future integration | Real data integration expected | Weather/ATC feed should influence ETD in production | Unresolved |
| DR-001 | `Flight` model | Flight schema fields maintained | BP-002/BP-003 | Complete operational flight record | Created/updated flights include required fields | TS-001, TS-002 |
| DR-002 | `CreateFlightRequest` | Input format and required-field validation | BP-002 | Data quality constraints | Invalid patterns return validation errors | TS-API-008 (unresolved test) |
| RR-001 | `statistics.js` | Status/reason/airline charts rendered | BP-004 | Visual operational reporting | Stats tab shows three visual groupings | TS-UI-004 (manual/automation pending) |
| RR-002 | no direct source — unresolved | No export reporting capability | Reporting gap | Downloadable report generation | User can export data (currently absent) | Gap test (expected fail) |
| SR-001 | no direct source — unresolved | No auth guard on mutating APIs | Security governance | Restrict write operations by identity/role | Unauthorized users should be blocked (not implemented) | Security gap test |
| SR-002 | no direct source — assumed | Open CORS likely non-production stance | Security hardening | Restrict API origins in production | Only approved origins allowed | Security hardening test |
| AR-001 | no direct source — unresolved | No actor-level change history persisted | Audit governance | Track who/when for changes | Every state change has immutable audit record | Audit gap test |

## Untraceable or weakly traceable notes
- All Confirmed requirements were traceable to source components.
- Inferred/Assumed/Unresolved requirements intentionally use non-source markers to avoid fabricated citations.
- Several acceptance criteria currently lack automated tests; these are flagged as unresolved test coverage gaps.
