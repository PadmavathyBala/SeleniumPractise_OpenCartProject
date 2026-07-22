# BRD Discovery Findings — etd-airlines_1

**Run ID:** 2026-07-22T17-30-27Z  
**Target:** `etd-airlines_1`  
**Discovery agent:** `brd-discovery`

## 1. Sources reviewed
- 29 implementation/document files were reviewed across backend, frontend, config, tests, and README.
- Live behavior was also spot-checked through `/actuator/health`, `/api/statistics`, `/api/flights`, and representative error responses while the app was running locally.

## 2. Application overview and scope
- **Confirmed:** The application is an airline departure-operations dashboard for managing Estimated Time of Departure (ETD), flight status, delays, and operational statistics rather than customer booking or ticketing. **Evidence:** `etd-airlines_1/README.md:1-5`, `etd-airlines_1/src/main/resources/static/index.html:13-25`, `etd-airlines_1/src/main/java/com/airlines/etd/EtdApplication.java:7-18`
- **Confirmed:** The delivered architecture is a browser UI (Bootstrap + vanilla JS) calling a Spring Boot REST backend backed by an in-memory repository. **Evidence:** `etd-airlines_1/README.md:6-23`, `etd-airlines_1/pom.xml:28-58`, `etd-airlines_1/src/main/java/com/airlines/etd/repository/FlightRepository.java:11-71`
- **Confirmed:** The system includes a demo/live-simulation mode: it seeds 15 flights at startup, applies two preset delays, recomputes statuses every 30 seconds, and may add random delays. **Evidence:** `etd-airlines_1/README.md:49-55`, `etd-airlines_1/src/main/java/com/airlines/etd/service/FlightSimulationService.java:35-129`
- **Confirmed:** The current implementation has no persistence beyond process memory, so data is lost on restart. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/repository/FlightRepository.java:11-18`, `etd-airlines_1/README.md:98-105`
- **Confirmed:** The current implementation has no authentication, authorization, or role-based UI/API restrictions. **Evidence:** `etd-airlines_1/README.md:100-104`, `etd-airlines_1/pom.xml:28-49`, `etd-airlines_1/src/main/resources/static/index.html:30-310`, `etd-airlines_1/src/main/java/com/airlines/etd/controller/FlightController.java:19-71`
- **Inferred:** The intended business audience is airline/airport operations staff who monitor and adjust departures, with supervisors using statistics and operators performing flight actions.
- **Confirmed:** Obvious out-of-scope capabilities include reservations, passenger servicing, ticketing, payments, loyalty, crew rostering, baggage tracing, and persistent audit history; none were found in inspected code or dependencies. **Evidence:** `etd-airlines_1/pom.xml:28-49`, `etd-airlines_1/src/main/java/com/airlines/etd/**`, `etd-airlines_1/src/main/resources/static/**`, `etd-airlines_1/README.md:96-105`

## 3. Users, roles, and permissions
| Finding | Confidence | Evidence |
|---|---|---|
| Any user who can load the UI can view dashboard data, statistics, and the add-flight form. | Confirmed | `etd-airlines_1/src/main/resources/static/index.html:30-310` |
| Any user who can load the UI can trigger ETD updates, departure marking, cancellation, deletion, and flight creation because all actions are exposed in the browser and there is no auth layer. | Confirmed | `etd-airlines_1/src/main/resources/static/js/dashboard.js:131-201`, `etd-airlines_1/src/main/resources/static/js/addFlight.js:18-49`, `etd-airlines_1/src/main/java/com/airlines/etd/controller/FlightController.java:42-69` |
| An “operations controller/agent” role is implied by the ability to create flights and manage ETD, cancellations, and departures. | Inferred | UI and controller capabilities above |
| A “supervisor/manager/analyst” role is implied by KPI and chart views, but is not enforced separately. | Inferred | `etd-airlines_1/src/main/resources/static/index.html:163-190`, `etd-airlines_1/src/main/resources/static/js/statistics.js:14-98`, `etd-airlines_1/src/main/java/com/airlines/etd/service/StatisticsService.java:28-85` |
| No permission matrix is implemented server-side. | Confirmed | `etd-airlines_1/pom.xml:28-49`, `etd-airlines_1/src/main/java/com/airlines/etd/controller/FlightController.java:19-71` |

## 4. Modules and user-visible features
| Module / feature | Finding | Confidence | Evidence |
|---|---|---|---|
| Dashboard | Shows KPI cards for total flights, on-time flights, delayed flights, cancelled flights, on-time %, and average delay. | Confirmed | `etd-airlines_1/src/main/resources/static/index.html:57-93`, `etd-airlines_1/src/main/resources/static/js/dashboard.js:37-48` |
| Dashboard | Supports free-text search, status filtering, sorting, and manual refresh. | Confirmed | `etd-airlines_1/src/main/resources/static/index.html:95-131`, `etd-airlines_1/src/main/resources/static/js/dashboard.js:12-30`, `etd-airlines_1/src/main/resources/static/js/dashboard.js:50-82` |
| Dashboard | Displays a flight table with flight, airline, route, gate, scheduled time, ETD, delay, status, delay reason, and actions. | Confirmed | `etd-airlines_1/src/main/resources/static/index.html:133-160`, `etd-airlines_1/src/main/resources/static/js/dashboard.js:84-129` |
| Flight actions | Each table row exposes Update ETD, Mark Departed, Cancel Flight, and Delete. | Confirmed | `etd-airlines_1/src/main/resources/static/js/dashboard.js:110-127`, `etd-airlines_1/src/main/resources/static/js/dashboard.js:131-163` |
| Statistics | Provides charts for flights by status, delays by reason, and flights by airline. | Confirmed | `etd-airlines_1/src/main/resources/static/index.html:163-190`, `etd-airlines_1/src/main/resources/static/js/statistics.js:25-98` |
| Add Flight | Provides a form for flight number, airline, aircraft type, origin, destination, gate, and scheduled departure. | Confirmed | `etd-airlines_1/src/main/resources/static/index.html:193-245`, `etd-airlines_1/src/main/resources/static/js/addFlight.js:18-27` |
| Notifications | Uses Bootstrap toasts for most action outcomes and an inline alert box on the Add Flight tab. | Confirmed | `etd-airlines_1/src/main/resources/static/js/app.js:33-53`, `etd-airlines_1/src/main/resources/static/js/addFlight.js:29-48`, `etd-airlines_1/src/main/resources/static/index.html:198-199`, `etd-airlines_1/src/main/resources/static/index.html:300-301` |
| Live updates | UI auto-refreshes every 15 seconds and backend simulation runs every 30 seconds. | Confirmed | `etd-airlines_1/README.md:51-55`, `etd-airlines_1/src/main/resources/static/js/app.js:6-31`, `etd-airlines_1/src/main/java/com/airlines/etd/service/FlightSimulationService.java:96-128` |
| Diverted operations | DIVERTED exists as a domain status and can be charted/styled if present, but no dedicated UI action or REST endpoint was found to set a flight to diverted. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/model/FlightStatus.java:6-23`, `etd-airlines_1/src/main/resources/static/css/styles.css:112-117`, `etd-airlines_1/src/main/java/com/airlines/etd/controller/FlightController.java:29-69`, `etd-airlines_1/src/main/resources/static/index.html:105-123` |

## 5. Screens, navigation, and UI behavior
- **Confirmed:** The UI is a single-page tabbed interface with three tabs: Dashboard, Statistics, and Add Flight. **Evidence:** `etd-airlines_1/src/main/resources/static/index.html:30-52`
- **Confirmed:** The header shows a live badge and a “Last update” timestamp. **Evidence:** `etd-airlines_1/src/main/resources/static/index.html:13-25`, `etd-airlines_1/src/main/resources/static/js/dashboard.js:203-206`
- **Confirmed:** Dashboard search uses a 300 ms debounce before re-querying the backend. **Evidence:** `etd-airlines_1/src/main/resources/static/js/dashboard.js:12-13`, `etd-airlines_1/src/main/resources/static/js/dashboard.js:230-236`
- **Confirmed:** Search is sent to the server, while status filtering and sorting are applied client-side after the flight list is loaded. **Evidence:** `etd-airlines_1/src/main/resources/static/js/dashboard.js:24-30`, `etd-airlines_1/src/main/resources/static/js/dashboard.js:50-82`, `etd-airlines_1/src/main/java/com/airlines/etd/controller/FlightController.java:29-35`
- **Confirmed:** Statistics charts are loaded lazily when the Statistics tab is shown. **Evidence:** `etd-airlines_1/src/main/resources/static/js/statistics.js:8-12`
- **Confirmed:** Add Flight defaults scheduled departure to one hour from the current browser time. **Evidence:** `etd-airlines_1/src/main/resources/static/js/addFlight.js:10-16`
- **Confirmed:** Cancel and Delete actions use browser prompt/confirm dialogs rather than custom forms. **Evidence:** `etd-airlines_1/src/main/resources/static/js/dashboard.js:143-155`

## 6. Data model and outputs
### 6.1 Core entity
| Field / derived value | Finding | Confidence | Evidence |
|---|---|---|---|
| Flight ID | A UUID is assigned on save if the flight has no ID. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/repository/FlightRepository.java:20-26` |
| Flight master data | Flight stores flight number, airline, origin, destination, gate, and aircraft type. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/model/Flight.java:12-18` |
| Schedule fields | Flight stores scheduled, estimated, and actual departure timestamps. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/model/Flight.java:19-21` |
| Operational state | Flight stores status, delay reason, delay notes, and last updated timestamp. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/model/Flight.java:22-25` |
| Derived delay | Delay minutes are computed as max(0, minutes between scheduled and estimated departure). | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/model/Flight.java:47-57` |
| Derived delay flag | `isDelayed()` becomes true only at 15 minutes or more. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/model/Flight.java:59-64` |
| API projection | REST responses expose display-friendly labels, derived delay fields, and lastUpdated in addition to raw domain values. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/dto/FlightDTO.java:15-76` |

### 6.2 Enumerations and taxonomies
- **Confirmed:** Supported statuses are `SCHEDULED`, `BOARDING`, `DEPARTED`, `DELAYED`, `CANCELLED`, and `DIVERTED`. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/model/FlightStatus.java:6-23`
- **Confirmed:** Supported delay reasons are Weather, Air Traffic Control, Mechanical Issue, Crew Availability, Security, Late Arriving Aircraft, Fueling, Catering, Baggage Handling, and Other. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/model/DelayReason.java:6-27`, `etd-airlines_1/src/main/resources/static/index.html:268-282`

### 6.3 Reporting outputs
- **Confirmed:** Statistics output includes totalFlights, onTimeCount, delayedCount, cancelledCount, onTimePercentage, averageDelayMinutes, countByStatus, countByDelayReason, and countByAirline. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/dto/StatisticsDTO.java:11-49`, `etd-airlines_1/src/main/java/com/airlines/etd/service/StatisticsService.java:28-85`
- **Confirmed:** On-time percentage is calculated as `onTime / totalFlights * 100`, returning 0 when no flights exist. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/service/StatisticsService.java:38-48`
- **Confirmed:** Average delay is computed only across delayed flights and rounded to one decimal place. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/service/StatisticsService.java:49-55`

## 7. API contracts
| Endpoint | Behavior | Confidence | Evidence |
|---|---|---|---|
| `GET /api/flights?search=` | Returns all flights or a filtered list of `FlightDTO` objects. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/controller/FlightController.java:29-35`, `etd-airlines_1/README.md:58-67` |
| `GET /api/flights/{id}` | Returns one `FlightDTO` or a 404 error if not found. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/controller/FlightController.java:37-40`, `etd-airlines_1/src/main/java/com/airlines/etd/exception/GlobalExceptionHandler.java:20-23` |
| `POST /api/flights` | Accepts `CreateFlightRequest`, validates required fields, and returns 201 with a created `FlightDTO`. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/controller/FlightController.java:42-46`, `etd-airlines_1/src/main/java/com/airlines/etd/dto/CreateFlightRequest.java:14-35` |
| `PUT /api/flights/{id}/etd` | Accepts `UpdateEtdRequest`, validates ETD presence, recomputes status, and returns updated `FlightDTO`. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/controller/FlightController.java:48-52`, `etd-airlines_1/src/main/java/com/airlines/etd/service/FlightService.java:65-86`, `etd-airlines_1/src/main/java/com/airlines/etd/dto/UpdateEtdRequest.java:11-28` |
| `POST /api/flights/{id}/cancel` | Accepts optional JSON `{notes}` and returns the cancelled `FlightDTO`. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/controller/FlightController.java:54-59` |
| `POST /api/flights/{id}/depart` | Marks the flight departed and returns the updated `FlightDTO`. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/controller/FlightController.java:61-64`, `etd-airlines_1/src/main/java/com/airlines/etd/service/FlightService.java:95-100` |
| `DELETE /api/flights/{id}` | Performs hard deletion and returns HTTP 204. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/controller/FlightController.java:66-69`, `etd-airlines_1/src/main/java/com/airlines/etd/service/FlightService.java:102-107` |
| `GET /api/statistics` | Returns one aggregated `StatisticsDTO`. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/controller/StatisticsController.java:22-25` |
| `GET /actuator/health` | Exposes application health data via Spring Boot Actuator. | Confirmed | `etd-airlines_1/README.md:67-68`, `etd-airlines_1/src/main/resources/application.properties:17-19` |

## 8. Business workflows
### 8.1 Monitor current departures
1. **Confirmed:** On page load, the app initializes Dashboard, Statistics, and Add Flight modules and immediately refreshes dashboard data. **Evidence:** `etd-airlines_1/src/main/resources/static/js/app.js:9-16`
2. **Confirmed:** The dashboard fetches flights and statistics, updates KPI cards, renders the current table, and stamps the last update time. **Evidence:** `etd-airlines_1/src/main/resources/static/js/dashboard.js:24-35`
3. **Confirmed:** Auto-refresh repeats every 15 seconds for the active dashboard/statistics tab. **Evidence:** `etd-airlines_1/src/main/resources/static/js/app.js:18-31`

### 8.2 Search, filter, and sort flights
1. **Confirmed:** Search text is sent to `GET /api/flights?search=` after debounce. **Evidence:** `etd-airlines_1/src/main/resources/static/js/dashboard.js:12-13`, `etd-airlines_1/src/main/resources/static/js/dashboard.js:24-28`
2. **Confirmed:** The server matches query text against flight number, origin, destination, and airline using case-insensitive substring logic. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/service/FlightService.java:109-123`
3. **Confirmed:** After loading results, the browser can filter by status and sort by ETD, scheduled departure, largest delay, or flight number. **Evidence:** `etd-airlines_1/src/main/resources/static/js/dashboard.js:50-72`

### 8.3 Create a new flight
1. **Confirmed:** The user enters flight number, airline, aircraft type, origin, destination, gate, and scheduled departure on the Add Flight tab. **Evidence:** `etd-airlines_1/src/main/resources/static/index.html:195-245`
2. **Confirmed:** The browser uppercases flight number, origin, and destination before sending the request. **Evidence:** `etd-airlines_1/src/main/resources/static/js/addFlight.js:19-27`
3. **Confirmed:** Server-side creation sets estimated departure equal to scheduled departure and initial status to `SCHEDULED`. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/service/FlightService.java:52-63`
4. **Confirmed:** Success is acknowledged with an inline success alert, a toast, and dashboard refresh. **Evidence:** `etd-airlines_1/src/main/resources/static/js/addFlight.js:32-41`

### 8.4 Update ETD
1. **Confirmed:** The user opens an ETD modal from a dashboard row, sees current route/flight details, and can edit ETD, reason, and notes. **Evidence:** `etd-airlines_1/src/main/resources/static/js/dashboard.js:166-176`, `etd-airlines_1/src/main/resources/static/index.html:250-299`
2. **Confirmed:** The request payload contains `newEstimatedDeparture`, optional `delayReason`, and optional `delayNotes`. **Evidence:** `etd-airlines_1/src/main/resources/static/js/dashboard.js:178-194`, `etd-airlines_1/src/main/java/com/airlines/etd/dto/UpdateEtdRequest.java:11-28`
3. **Confirmed:** After ETD update, status is recomputed immediately from current time and new delay state. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/service/FlightService.java:81-85`, `etd-airlines_1/src/main/java/com/airlines/etd/service/EtdCalculationService.java:20-41`

### 8.5 Cancel a flight
1. **Confirmed:** Cancellation is triggered from the dashboard using a browser prompt for notes. **Evidence:** `etd-airlines_1/src/main/resources/static/js/dashboard.js:147-152`
2. **Confirmed:** Server-side cancellation sets status to `CANCELLED` and stores the provided notes in `delayNotes`. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/service/FlightService.java:88-93`

### 8.6 Mark a flight departed
1. **Confirmed:** Departure can be triggered from the dashboard after a confirmation dialog. **Evidence:** `etd-airlines_1/src/main/resources/static/js/dashboard.js:142-146`
2. **Confirmed:** Server-side departure sets status to `DEPARTED` and records `actualDeparture` as the current server time. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/service/FlightService.java:95-100`
3. **Confirmed:** A flight can also become `DEPARTED` automatically once the current time is after ETD, even without a manual depart action. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/service/EtdCalculationService.java:30-33`, `etd-airlines_1/src/main/java/com/airlines/etd/service/FlightSimulationService.java:101-109`

### 8.7 Delete a flight
1. **Confirmed:** Delete requires only a browser confirm dialog and then removes the record from the repository. **Evidence:** `etd-airlines_1/src/main/resources/static/js/dashboard.js:153-157`, `etd-airlines_1/src/main/java/com/airlines/etd/service/FlightService.java:102-107`
2. **Confirmed:** Delete is a hard delete with no recovery/audit flow in the implementation. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/repository/FlightRepository.java:60-70`, `etd-airlines_1/src/main/java/com/airlines/etd/service/FlightService.java:102-107`

### 8.8 Review statistics
1. **Confirmed:** Statistics are aggregated directly from all current in-memory flights. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/service/StatisticsService.java:28-85`
2. **Confirmed:** The UI renders status as a doughnut chart, delay reasons as a bar chart, and flights by airline as a horizontal bar chart. **Evidence:** `etd-airlines_1/src/main/resources/static/js/statistics.js:25-98`

### 8.9 Seed and simulate operations
1. **Confirmed:** Startup seeds 15 named flights across multiple US carriers and airports. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/service/FlightSimulationService.java:35-70`
2. **Confirmed:** `UA456` starts weather-delayed and `WN789` starts late-inbound delayed. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/service/FlightSimulationService.java:71-81`, `etd-airlines_1/README.md:51-53`
3. **Confirmed:** Every 30 seconds the simulator recomputes statuses for all flights and has a 20% chance to add a delay to a random on-time scheduled flight. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/service/FlightSimulationService.java:96-128`

## 9. Business rules and validations
| Rule | Confidence | Evidence |
|---|---|---|
| Flight number is required and must match 2–3 uppercase letters followed by 1–4 digits. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/dto/CreateFlightRequest.java:14-17` |
| Airline is required. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/dto/CreateFlightRequest.java:19-20` |
| Origin and destination are required and must each be a 3-letter uppercase IATA code. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/dto/CreateFlightRequest.java:22-28` |
| Scheduled departure is required when creating a flight. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/dto/CreateFlightRequest.java:33-35` |
| Duplicate flights are rejected when the same flight number already exists for the same scheduled departure timestamp. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/service/FlightService.java:42-50` |
| New ETD is required when updating ETD. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/dto/UpdateEtdRequest.java:13-15` |
| New ETD cannot be more than 5 minutes in the past relative to server time. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/service/FlightService.java:68-71` |
| Delay minutes are never negative even if estimated departure is earlier than scheduled. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/model/Flight.java:47-57` |
| A flight is considered delayed only at 15+ minutes. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/model/Flight.java:59-64` |
| Terminal statuses `CANCELLED`, `DIVERTED`, and `DEPARTED` are preserved by automatic status computation. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/service/EtdCalculationService.java:20-25` |
| If current time is past ETD, computed status becomes `DEPARTED`. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/service/EtdCalculationService.java:27-33` |
| If current time is within 30 minutes of ETD, status becomes `BOARDING` unless the flight is delayed, in which case it remains `DELAYED`. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/service/EtdCalculationService.java:35-38` |
| Otherwise, computed status is `DELAYED` for delayed flights and `SCHEDULED` for on-time flights. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/service/EtdCalculationService.java:40-41` |
| Estimated additional delay defaults are 45 weather, 20 air traffic, 60 mechanical, 30 crew, 25 security, 35 late inbound, 15 fueling, 10 catering, 15 baggage, 20 other minutes. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/service/EtdCalculationService.java:43-60` |
| Cancellation notes are stored in `delayNotes`; there is no separate cancellation-reason field. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/service/FlightService.java:88-93`, `etd-airlines_1/src/main/java/com/airlines/etd/model/Flight.java:23-25` |
| “On time” statistics exclude cancelled flights and include any non-delayed non-cancelled flights, regardless of other statuses. | Confirmed | `etd-airlines_1/src/main/java/com/airlines/etd/service/StatisticsService.java:34-48` |

## 10. Integrations and data flow
- **Confirmed:** Browser-to-server integration is plain REST/JSON using `fetch()` to `/api`. **Evidence:** `etd-airlines_1/src/main/resources/static/js/api.js:4-37`
- **Confirmed:** Error payloads are expected to expose `message` and optionally `fieldErrors`, which the UI concatenates for display. **Evidence:** `etd-airlines_1/src/main/resources/static/js/api.js:12-23`, `etd-airlines_1/src/main/java/com/airlines/etd/exception/GlobalExceptionHandler.java:25-39`
- **Confirmed:** Jackson is configured for Java time support and non-timestamp date serialization, with app timezone set to UTC. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/config/WebConfig.java:25-30`, `etd-airlines_1/src/main/resources/application.properties:8-10`
- **Confirmed:** CORS is open to any origin for `/api/**`. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/config/WebConfig.java:17-23`
- **Confirmed:** There are no real external airline/ATC/weather integrations in the implementation; those sources are mentioned only as future/real-world placeholders. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/service/EtdCalculationService.java:11-13`, `etd-airlines_1/README.md:98-104`
- **Inferred:** The simulation service is standing in for live operational feeds in this reference/demo application.

## 11. Notifications, error scenarios, and operational behavior
- **Confirmed:** Not-found errors return HTTP 404 with `timestamp`, `status`, and `message`. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/exception/GlobalExceptionHandler.java:20-23`
- **Confirmed:** Bean-validation failures return HTTP 400 with a generic `Validation failed` message plus per-field errors. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/exception/GlobalExceptionHandler.java:25-34`
- **Confirmed:** Business-rule violations such as duplicate flight or invalid past ETD return HTTP 400. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/service/FlightService.java:42-50`, `etd-airlines_1/src/main/java/com/airlines/etd/service/FlightService.java:68-71`, `etd-airlines_1/src/main/java/com/airlines/etd/exception/GlobalExceptionHandler.java:36-39`
- **Confirmed:** Unhandled exceptions return HTTP 500 with the exception message included in the response text. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/exception/GlobalExceptionHandler.java:41-45`
- **Confirmed:** The frontend surfaces failed loads, failed actions, and failed ETD updates via danger toasts. **Evidence:** `etd-airlines_1/src/main/resources/static/js/dashboard.js:32-34`, `etd-airlines_1/src/main/resources/static/js/dashboard.js:159-160`, `etd-airlines_1/src/main/resources/static/js/dashboard.js:198-200`, `etd-airlines_1/src/main/resources/static/js/statistics.js:20-22`
- **Confirmed:** The app exposes actuator `health`, `info`, and `metrics` endpoints for operational monitoring. **Evidence:** `etd-airlines_1/src/main/resources/application.properties:17-19`

## 12. Constraints, assumptions, and unresolved questions
- **Confirmed:** Repository storage is in-memory only and therefore unsuitable for durable operational recordkeeping without replacement. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/repository/FlightRepository.java:11-18`, `etd-airlines_1/README.md:98-105`
- **Confirmed:** No audit/history trail exists for ETD edits, cancellations, departures, or deletions. **Evidence:** `etd-airlines_1/src/main/java/com/airlines/etd/model/Flight.java:19-25`, `etd-airlines_1/README.md:100-104`
- **Confirmed:** No explicit timezone conversion UX exists; browser inputs are local `datetime-local` values while server serialization is configured in UTC. **Evidence:** `etd-airlines_1/src/main/resources/static/index.html:231-232`, `etd-airlines_1/src/main/resources/static/index.html:265-266`, `etd-airlines_1/src/main/resources/static/js/dashboard.js:217-223`, `etd-airlines_1/src/main/resources/application.properties:8-10`
- **Unresolved:** Whether deletion is a real business operation or only a demo/admin convenience is not stated.
- **Unresolved:** Whether departed flights should be set only by explicit operator confirmation, or whether automatic post-ETD departure is acceptable business behavior, is not documented.
- **Unresolved:** There is no defined business process for `DIVERTED` flights despite the status existing in the model.
- **Unresolved:** Required authorization boundaries for create/update/cancel/delete actions are not specified.
- **Unresolved:** Persistence, audit retention, and recovery expectations for operational history are not specified.
- **Inferred:** The current seed data, random delays, and relaxed CORS suggest this is a reference/demo app rather than a production-ready operations platform.

## 13. Coverage summary
- **Confirmed findings:** 49
- **Inferred findings:** 5
- **Assumed findings:** 0
- **Unresolved findings:** 5
