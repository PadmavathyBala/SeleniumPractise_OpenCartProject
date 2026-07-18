# Step 3 — Business Process Analysis

**Run ID:** `2026-07-18T22-29-21Z`  
**Agent:** brd-business-process  
**Pipeline Step:** 3 of 8 — Interpret  
**Supersedes:** `docs/brd_runs/2026-07-18T21-08-41Z/03_business_process.md`

Changes from v1 are marked **[v2 update]**.

---

## 1. System Context

The ETD Airlines Departure Operations System is a **single-airport departure board
management tool** intended to assist airport operations staff (gate agents, operations
supervisors) in tracking and updating estimated departure times for flights.

**Confirmed** — inferred from the application name, README description, domain model
fields (gate, departure times), and pre-loaded US domestic / one international route
data (`FlightSimulationService.seedFlights()`).

**[v2 update — ISSUE-03 addressed]** The `README.md` describes this as a
**reference/demonstration application**. The project structure (Eclipse metadata,
seed data with fictional flights, simulation job) confirms it is not currently a
production system. This BRD documents the *implemented behaviour*; any deployment to
a production airport environment would require additional hardening (authentication,
persistence, HA, ATC integration).

The system operates as a stateless, in-memory web application. There is no user
authentication, multi-tenancy, or persistent data store.  
**Confirmed** — no Spring Security in `pom.xml`; `FlightRepository` uses
`ConcurrentHashMap`.

---

## 2. Identified Business Processes

### BP-01: Flight Lifecycle Management

**Confidence:** Confirmed  
**Source:** `FlightService.java`, `FlightStatus.java`, `FlightController.java`

A flight progresses through a defined lifecycle from creation to departure or
cancellation:

```
CREATE → SCHEDULED
       ↓ (within 30 min of ETD, no delay)
    BOARDING
       ↓ (ETD passed)
    DEPARTED
       ↓ (explicit cancel action)
    CANCELLED

SCHEDULED/BOARDING
       ↓ (ETD updated, delay ≥ 15 min)
    DELAYED
       ↓ (delay resolved or depart action)
    DEPARTED / BOARDING
```

Terminal states: **DEPARTED**, **CANCELLED**.  
**DIVERTED** exists as an enum value but has no code path that sets it automatically;
it would require a manual ETD update or direct data manipulation — **Unresolved** (no
setter path found).

---

### BP-02: ETD Update (Delay Management)

**Confidence:** Confirmed  
**Source:** `FlightService.updateEtd()`, `EtdCalculationService.computeStatus()`

**Actors (Inferred):** Gate Agent / Operations Supervisor

**Trigger:** Operational event (weather, mechanical, ATC, etc.) causes a departure
delay.

**Flow:**
1. Operator selects a flight in the Dashboard.
2. Operator clicks "Update ETD" (clock icon button).
3. A modal dialog opens pre-populated with the current ETD, delay reason, and notes.
4. Operator sets a new `estimatedDeparture`, selects a `delayReason`, and enters
   optional `delayNotes`.
5. Front-end sends `PUT /api/flights/{id}/etd` with the new payload.
6. Server validates: new ETD must not be more than 5 minutes in the past
   (`FlightService.java` line 68).
7. Server recomputes flight status via `EtdCalculationService.computeStatus()`.
8. Updated `FlightDTO` (with new delay minutes and status) is returned.
9. Dashboard auto-refreshes to show the updated row.

**Business Rule:** A flight's ETD cannot be set to a time more than 5 minutes in the
past — **Confirmed** (`FlightService.updateEtd()` validation check).

**Business Rule:** A flight is considered "significantly delayed" when
`estimatedDeparture − scheduledDeparture ≥ 15 minutes` — **Confirmed**
(`Flight.isDelayed()` method).

**[v2 update — ISSUE-02 addressed]** The API endpoint `PUT /api/flights/{id}/etd`
applies the ETD update regardless of the flight's current status. There is no
server-side guard preventing an ETD update on a DEPARTED or CANCELLED flight.
**Confirmed** — `FlightService.updateEtd()` does not check `flight.getStatus()`
before updating. This is documented as OQ-007 (carried forward) and may represent
a defect. Stakeholder clarification is required.

---

### BP-03: Flight Departure Recording

**Confidence:** Confirmed  
**Source:** `FlightService.markDeparted()`, `FlightController.depart()`

**Actors (Inferred):** Gate Agent

**Trigger:** Aircraft pushes back / gate agent confirms physical departure.

**Flow:**
1. Operator clicks the "depart" (aeroplane icon) button on a flight row.
2. Browser shows a confirmation dialog.
3. Front-end sends `POST /api/flights/{id}/depart`.
4. Server sets `status = DEPARTED` and `actualDeparture = now()`.
5. Dashboard refreshes; the row is visually styled as departed.

**Note:** No guard prevents departing an already-cancelled or already-departed flight.
**Inferred** — `markDeparted()` does not check current status before updating.

---

### BP-04: Flight Cancellation

**Confidence:** Confirmed  
**Source:** `FlightService.cancelFlight()`, `FlightController.cancel()`

**Actors (Inferred):** Gate Agent / Operations Supervisor

**Trigger:** Flight is operationally cancelled (airline decision, ground stop, etc.)

**Flow:**
1. Operator clicks the "cancel" (X-octagon icon) button.
2. Browser prompts for an optional cancellation note.
3. Front-end sends `POST /api/flights/{id}/cancel` with optional `notes` body.
4. Server sets `status = CANCELLED` and stores `delayNotes`.
5. Row appears with strikethrough/muted styling in the dashboard.

**[v2 update — ISSUE-05 addressed]** The `notes` field passed to `cancelFlight()` is
optional; an empty string passes the null check in `FlightService.cancelFlight()`.
There is no minimum-length validation for the cancellation note. See OQ-011.

---

### BP-05: New Flight Entry

**Confidence:** Confirmed  
**Source:** `FlightService.createFlight()`, `FlightController.create()`,
`addFlight.js`, `CreateFlightRequest.java`

**Actors (Inferred):** Operations Supervisor / Dispatcher

**Trigger:** A new flight needs to be added to the departure board.

**Flow:**
1. Operator navigates to the "Add Flight" tab.
2. Form fields: Flight Number, Airline, Origin, Destination, Gate (opt.),
   Aircraft Type (opt.), Scheduled Departure (defaults to 1 hour from now).
3. Front-end submits `POST /api/flights`.
4. Server validates format (flight number regex, IATA code format, required fields).
5. Server checks for duplicate: same flight number + same scheduled time is rejected.
6. On success: `201 Created` returned; dashboard refreshes; success alert shown.
7. On validation failure: `400 Bad Request` with field-level error detail.

**Business Rule:** Flight number must match pattern `^[A-Z]{2,3}\d{1,4}$` (e.g.,
`AA101`, `UAL1234`) — **Confirmed** (`CreateFlightRequest.java` `@Pattern`).

**Business Rule:** Origin and destination must be valid 3-letter IATA codes
(`^[A-Z]{3}$`) — **Confirmed** (`CreateFlightRequest.java`).

**Business Rule:** Duplicate flight number at the same scheduled time is rejected —
**Confirmed** (`FlightService.createFlight()` duplicate check).

---

### BP-06: Operational Statistics Monitoring

**Confidence:** Confirmed  
**Source:** `StatisticsService.buildStatistics()`, `StatisticsController.java`,
`statistics.js`

**Actors (Inferred):** Operations Supervisor / Manager

**Trigger:** User navigates to the Statistics tab, or auto-refresh fires.

**Flow:**
1. Browser calls `GET /api/statistics`.
2. Server aggregates all in-memory flights into counts by status, delay reason,
   and airline; computes on-time percentage and average delay.
3. Front-end renders:
   - Status doughnut chart (Chart.js)
   - Delay reason bar chart
   - Airline flight-count horizontal bar chart

**Key Metrics Computed:**
- `totalFlights`
- `onTimeCount` (not delayed, not cancelled)
- `delayedCount` (isDelayed = true)
- `cancelledCount`
- `onTimePercentage`
- `averageDelayMinutes` (average across delayed flights only)
- `countByStatus` (map)
- `countByDelayReason` (map)
- `countByAirline` (map)

---

## 3. Automated Background Process

### BP-07: Live Simulation (Scheduled Job)

**Confidence:** Confirmed  
**Source:** `FlightSimulationService.simulateLiveUpdates()` — `@Scheduled(fixedRate=30_000)`

This process runs every 30 seconds on the server:
1. Recomputes the status of every flight based on current clock vs ETD.
2. With 20% probability per cycle, randomly selects one non-delayed, scheduled flight
   and applies a random delay with a reason labeled "Auto-simulated delay".

This is a **demo/reference** behaviour, not a production business requirement. It
exists to make the dashboard appear live during demonstrations.

**Note for BRD:** This process should be flagged as a simulation artefact.
Business stakeholders would replace it with real ATC/weather feed integration.
**Inferred** — comment in `EtdCalculationService.java` line 11: *"In a real airline
system this would integrate with weather, ATC, and aircraft tracking feeds."*

**[v2 update]** `@EnableScheduling` enabling this job is declared on
`EtdApplication.java` line 12 — **Confirmed**.

---

## 4. User Roles (Inferred)

No authentication or role model exists in the code. The following roles are
**Inferred** from the UI affordances and business context:

| Role | Capabilities Implied by UI |
|---|---|
| Gate Agent | Update ETD, Mark Departed, Cancel Flight |
| Operations Supervisor | All Gate Agent capabilities + Add/Delete Flights |
| Manager / Viewer | View Dashboard and Statistics (read-only) |

All three roles would access the same single-page application through the same URL
with no access control. Role separation requires future implementation.
**Assumed** — no RBAC code exists.
