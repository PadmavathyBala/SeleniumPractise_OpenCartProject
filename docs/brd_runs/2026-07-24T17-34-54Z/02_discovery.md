# Step 2 — Discovery Findings

## Application overview
1. The application is a Spring Boot + browser UI system for managing airline estimated departure times and status changes for flights. **[Confirmed]** Citation: `README.md:3-5`, `src/main/java/com/airlines/etd/EtdApplication.java:11-17`, `src/main/resources/static/index.html:13-18`.
2. The backend exposes REST JSON endpoints under `/api` for flight operations and statistics retrieval. **[Confirmed]** Citation: `src/main/java/com/airlines/etd/controller/FlightController.java:20-70`, `src/main/java/com/airlines/etd/controller/StatisticsController.java:13-25`.
3. The runtime data store is an in-memory, thread-safe repository, not a persistent database. **[Confirmed]** Citation: `src/main/java/com/airlines/etd/repository/FlightRepository.java:12-19`.

## Modules and features
4. Flight lifecycle operations include listing, searching, retrieving, creating, updating ETD, canceling, marking departed, and deleting flights. **[Confirmed]** Citation: `FlightController.java:29-70`, `api.js:29-36`.
5. A statistics module aggregates total, on-time, delayed, cancelled, average delay, counts by status/reason/airline. **[Confirmed]** Citation: `StatisticsService.java:28-83`, `statistics.js:14-98`.
6. A scheduler simulates live operational changes every 30 seconds and can auto-inject delay events. **[Confirmed]** Citation: `FlightSimulationService.java:96-127`.
7. Initial system startup seeds 15 flights and pre-applies realistic delays to two flights. **[Confirmed]** Citation: `FlightSimulationService.java:35-84`, `README.md:51-54`.

## User roles and actors
8. A primary operations actor updates ETD values, marks departures, cancels, and deletes flights from the dashboard action controls. **[Confirmed]** Citation: `dashboard.js:111-158`, `index.html:149-150`.
9. A planning/dispatch actor can add new flights via the Add Flight tab and required input fields. **[Confirmed]** Citation: `index.html:193-243`, `addFlight.js:18-42`.
10. A monitoring actor can view KPIs and charts without editing data. **[Inferred]** Evidence chain: read-only visuals in KPI/charts (`index.html:58-93`, `statistics.js:25-98`) and separate mutation actions only in dashboard/add-flight controls (`dashboard.js:131-201`, `addFlight.js:18-49`).
11. Authentication and role enforcement are not implemented; actor separation is functional, not security-backed. **[Confirmed]** Citation: absence of security config in codebase; permissive CORS in `WebConfig.java:19-23`; README extension note `README.md:102`.

## Screens and navigation
12. UI has three tabbed screens: Dashboard, Statistics, Add Flight. **[Confirmed]** Citation: `index.html:31-49`.
13. Dashboard provides filters by search text, status, and sort mode. **[Confirmed]** Citation: `index.html:95-127`, `dashboard.js:50-73`.
14. Dashboard table exposes status, delay, reason, and action buttons per flight row. **[Confirmed]** Citation: `index.html:137-150`, `dashboard.js:93-128`.
15. ETD updates happen via a modal form capturing new ETD, delay reason, and notes. **[Confirmed]** Citation: `index.html:250-295`, `dashboard.js:166-201`.
16. Statistics tab uses Chart.js for status, delay-reason, and airline visualizations. **[Confirmed]** Citation: `index.html:164-190`, `statistics.js:25-98`.
17. Add Flight tab performs client-side normalization (uppercase for flight number and IATA fields) before submit. **[Confirmed]** Citation: `addFlight.js:20-24`.

## Business workflows
18. Core workflow: list flights -> optionally filter/sort/search -> execute action -> refresh KPI/table data. **[Confirmed]** Citation: `dashboard.js:24-35`, `dashboard.js:50-82`, `dashboard.js:139-158`.
19. ETD workflow: user submits new ETD -> backend validates -> backend recalculates status -> updated flight returned to UI. **[Confirmed]** Citation: `dashboard.js:178-199`, `FlightService.java:65-85`, `EtdCalculationService.java:20-41`.
20. Create-flight workflow: user enters required data -> backend validates format/presence -> duplicate-time rule evaluated -> flight saved with default SCHEDULED status and ETD = scheduled departure. **[Confirmed]** Citation: `CreateFlightRequest.java:14-35`, `FlightService.java:42-63`.
21. Live operations workflow: every 30 seconds system recomputes status and may simulate new delay with 20% probability. **[Confirmed]** Citation: `FlightSimulationService.java:96-127`.

## Inputs, outputs, and data
22. Input constraints: flight number must match 2-3 uppercase letters + 1-4 digits; origin and destination must be 3-letter uppercase IATA codes; scheduled departure required. **[Confirmed]** Citation: `CreateFlightRequest.java:14-35`.
23. ETD update input requires new estimated departure timestamp; delay reason and notes are optional. **[Confirmed]** Citation: `UpdateEtdRequest.java:13-28`.
24. Output payload includes computed `delayMinutes`, `delayed`, and display strings for status/reason. **[Confirmed]** Citation: `FlightDTO.java:34-55`.
25. Error outputs include structured JSON with timestamp, status, message, and field errors for validation failures. **[Confirmed]** Citation: `GlobalExceptionHandler.java:25-53`.

## Business rules and validations
26. New ETD cannot be more than 5 minutes in the past. **[Confirmed]** Citation: `FlightService.java:68-71`.
27. Duplicate prevention rule checks same flight number with same scheduled departure. **[Confirmed]** Citation: `FlightService.java:43-50`.
28. Delay classification threshold is 15+ minutes after scheduled time. **[Confirmed]** Citation: `Flight.java:60-64`.
29. Status engine behavior: terminal statuses (cancelled/diverted/departed) are preserved; otherwise status depends on ETD proximity and delay state. **[Confirmed]** Citation: `EtdCalculationService.java:21-41`.
30. Cancelling a flight sets status to CANCELLED and stores notes. **[Confirmed]** Citation: `FlightService.java:88-93`.
31. Departing a flight sets status to DEPARTED and actual departure to current time. **[Confirmed]** Citation: `FlightService.java:95-100`.

## Integrations and interfaces
32. Software interface is browser->REST over HTTP JSON under same app origin path `/api`. **[Confirmed]** Citation: `api.js:5-37`, `index.html:304-310`.
33. No external weather, ATC, or aircraft data integration is implemented; references are future-state comments. **[Confirmed]** Citation: `EtdCalculationService.java:11-13`, `README.md:103`.
34. Health and observability interface exists via Spring Actuator health/info/metrics exposure. **[Confirmed]** Citation: `application.properties:18-19`, `README.md:68`.

## Notifications, reports, and analytics
35. User notifications are local UI alerts and toasts (success, warning, danger) for operation outcomes. **[Confirmed]** Citation: `addFlight.js:34-47`, `dashboard.js:145-161`, `app.js:33-53`.
36. Reporting is operational dashboard analytics, not downloadable/export reports. **[Inferred]** Evidence chain: only chart/KPI endpoints and rendering in UI (`StatisticsController.java:22-25`, `statistics.js:14-98`), no export endpoints found.

## Error scenarios
37. Not-found flight IDs return 404 with message `Flight not found`. **[Confirmed]** Citation: `FlightNotFoundException.java:8-10`, `GlobalExceptionHandler.java:20-23`.
38. Validation failures return 400 with per-field details. **[Confirmed]** Citation: `GlobalExceptionHandler.java:25-34`.
39. Unexpected exceptions return 500 with generic error message. **[Confirmed]** Citation: `GlobalExceptionHandler.java:41-45`.

## Nonfunctional and operational observations
40. Auto-refresh cadence is 15 seconds on the active tab, with statistics-tab also refreshing dashboard KPI cache. **[Confirmed]** Citation: `app.js:6-30`.
41. CORS policy is fully open for `/api/**`, indicating broad accessibility but weak origin restrictions. **[Confirmed]** Citation: `WebConfig.java:19-23`.
42. Persistence across restarts is unavailable due to in-memory store. **[Inferred]** Evidence chain: ConcurrentHashMap repository with no persistence adapter (`FlightRepository.java:18-25`, `README.md:100`).

## Unresolved / assumed areas
43. Regulatory, compliance, and audit retention requirements are not present in code/docs. **[Unresolved]** No direct artifacts found.
44. SLA targets (response time/availability) are not specified in measurable terms. **[Unresolved]** No explicit targets in code/config/docs.
45. Hardware interface needs are likely standard web hosting only, but no formal hardware profile exists. **[Assumed]** Derived from web app architecture in `README.md:8-23`.
46. Real production user-role definitions and organizational ownership are not documented. **[Unresolved]** No org model file present.
