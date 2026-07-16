# Step 3 — Business Process Analysis (brd-business-process)

**Run ID:** `2026-07-16T03-46-24Z`  
**Agent:** brd-business-process  
**Input:** Discovery findings (02_discovery.md)  
**Output:** 12 business processes

---

## Overview

The ETD Airlines Departure Operations System supports a set of interconnected business processes centred on managing the lifecycle of a flight from initial scheduling through actual departure. The following processes are identified from the Discovery findings, each translated from technical observations into business-language descriptions.

---

## BP-001 — View Departure Board

**Confidence:** Confirmed  
**Actors:** Departure Operations Staff (Operator)  
**Preconditions:** The system is running and has at least one flight record.

**Main Workflow:**
1. The operator opens the Departure Board dashboard in a web browser.
2. The system displays all flights sorted by scheduled departure time, including KPI summary cards (total flights, on-time count and percentage, delayed count, average delay, cancelled count).
3. For each flight, the board shows: flight number, airline, route (origin → destination), gate assignment, scheduled departure, estimated departure, delay in minutes, current status, and delay reason.

**Alternate Workflows:**
- The operator enters a search term; the board filters to flights whose number, airline, origin, or destination contains that term.
- The operator selects a status filter; only flights in that status are shown.
- The operator selects a different sort order (by ETD, scheduled time, delay magnitude, or flight number).

**Business Outcome:** The operator has an accurate, up-to-date view of all departures and can quickly identify delayed or problematic flights.

**Notes:** The board auto-refreshes every 15 seconds without operator action.

---

## BP-002 — Register a New Flight

**Confidence:** Confirmed  
**Actors:** Departure Operations Staff (Operator)  
**Preconditions:** The system is running. The operator has the flight details to hand.

**Main Workflow:**
1. The operator navigates to the Add Flight tab.
2. The operator enters: flight number, airline name, origin airport (IATA), destination airport (IATA), scheduled departure date/time, and optionally gate and aircraft type.
3. The operator submits the form.
4. The system validates all inputs.
5. The system checks that the same flight number does not already exist for the same scheduled departure time.
6. The system registers the flight with status SCHEDULED and estimated departure equal to the scheduled departure.
7. The operator receives a confirmation notification and the dashboard updates.

**Alternate Workflows:**
- If any required field is missing or incorrectly formatted, the system returns a specific validation message and the flight is not created.
- If a duplicate flight number/time combination exists, the system rejects the creation with an explanatory message.

**Exceptions:** None defined beyond validation failures.

**Business Outcome:** A new flight is added to the operational roster and is visible on the departure board.

---

## BP-003 — Update Estimated Departure Time (ETD)

**Confidence:** Confirmed  
**Actors:** Departure Operations Staff (Operator)  
**Preconditions:** The flight exists and is in an active status (not DEPARTED, CANCELLED, or DIVERTED).

**Main Workflow:**
1. The operator locates the flight on the departure board.
2. The operator opens the ETD update dialog for that flight.
3. The operator enters the new estimated departure time and optionally selects a delay reason and adds free-text notes.
4. The operator saves the update.
5. The system validates that the new ETD is not more than 5 minutes in the past.
6. The system records the new ETD, delay reason, and notes.
7. The system automatically recomputes the flight's status based on the new ETD relative to the current time.
8. The operator receives a success confirmation and the dashboard updates.

**Alternate Workflows:**
- If the new ETD is more than 5 minutes in the past, the system rejects the update with an error message.

**Business Outcome:** The departure board reflects the latest estimated departure, enabling accurate passenger and operations communication.

---

## BP-004 — Cancel a Flight

**Confidence:** Confirmed  
**Actors:** Departure Operations Staff (Operator)  
**Preconditions:** The flight exists and has a current record in the system.

**Main Workflow:**
1. The operator locates the flight on the departure board.
2. The operator selects the cancel action for that flight.
3. The operator optionally enters a cancellation note.
4. The operator confirms the action.
5. The system sets the flight status to CANCELLED and records the cancellation note.
6. The operator receives a confirmation notification and the dashboard updates.

**Alternate Workflows:**
- The operator does not provide a cancellation note; the system proceeds without one.

**Exceptions:** No current guard prevents cancelling an already-cancelled flight.

**Business Outcome:** The flight is permanently marked as cancelled on the departure board, ensuring passengers and staff are informed.

---

## BP-005 — Mark a Flight as Departed

**Confidence:** Confirmed  
**Actors:** Departure Operations Staff (Operator)  
**Preconditions:** The flight exists and has physically departed the gate.

**Main Workflow:**
1. The operator locates the flight on the departure board.
2. The operator selects the "Mark Departed" action.
3. The operator confirms the action.
4. The system sets the flight status to DEPARTED and records the actual departure timestamp.
5. The operator receives a success notification and the dashboard updates.

**Alternate Workflows:**
- The operator cancels the confirmation dialog; no change is made.

**Exceptions:** No current guard prevents marking a cancelled flight as departed.

**Business Outcome:** The departure board accurately reflects that the aircraft has left the gate, closing out the flight record for operational purposes.

---

## BP-006 — Remove a Flight Record

**Confidence:** Confirmed  
**Actors:** Departure Operations Staff (Operator)  
**Preconditions:** The flight exists in the system.

**Main Workflow:**
1. The operator locates the flight on the departure board.
2. The operator selects the delete action.
3. The operator confirms the permanent deletion.
4. The system permanently removes the flight from the roster.
5. The dashboard updates to reflect the removal.

**Alternate Workflows:**
- The operator cancels the confirmation; no change is made.

**Business Outcome:** Erroneous or test flight records can be removed from the operational roster.

---

## BP-007 — Monitor Departure Statistics

**Confidence:** Confirmed  
**Actors:** Departure Operations Staff (Operator); Operations Management (Assumed)  
**Preconditions:** The system is running and has flight data.

**Main Workflow:**
1. The user navigates to the Statistics tab.
2. The system displays: total flights, on-time count with percentage of total, delayed count, cancelled count, average delay in minutes, breakdown by flight status, breakdown by delay reason, breakdown by airline.
3. The statistics page auto-refreshes every 15 seconds.

**Business Outcome:** Management and operations staff can assess departure performance at a glance, identifying patterns in delay causes or airline performance.

---

## BP-008 — Automated Status Lifecycle Management

**Confidence:** Confirmed  
**Actors:** System (automated background process)  
**Preconditions:** The system is running. At least one flight exists with a non-terminal status.

**Main Workflow:**
1. Every 30 seconds, the system evaluates every active flight against the current time.
2. For each flight, the system computes the appropriate status:
   - If the ETD has passed → the flight transitions to DEPARTED.
   - If the ETD is within 30 minutes and the flight is on time → the flight transitions to BOARDING.
   - If the ETD is within 30 minutes and the flight is delayed → status remains DELAYED.
   - If the ETD is more than 30 minutes away and the flight is delayed → status is DELAYED.
   - If the ETD is more than 30 minutes away and the flight is on time → status is SCHEDULED.
3. Flights already in terminal states (CANCELLED, DEPARTED, DIVERTED) are not changed.
4. Any status change is persisted in the in-memory store.

**Business Outcome:** The departure board remains current without requiring manual operator intervention for routine status progressions.

---

## BP-009 — Automated Delay Simulation (Reference/Training Context)

**Confidence:** Confirmed  
**Actors:** System (automated background process)  
**Preconditions:** The system is running; at least one SCHEDULED, non-delayed flight exists.

**Main Workflow:**
1. Every 30 seconds, the system executes with a 20% probability of introducing a simulated delay.
2. If triggered, one random SCHEDULED non-delayed flight is selected.
3. A random delay reason is selected.
4. The system adds the standard estimated delay duration for that reason to the flight's ETD and sets the status to DELAYED.

**Business Outcome (Training context):** Simulates real-world operational conditions for training purposes without requiring live data feeds.

---

## BP-010 — Apply a Categorised Delay

**Confidence:** Confirmed  
**Actors:** Departure Operations Staff (Operator, via ETD update); System (automated simulation)  
**Preconditions:** The flight exists and is active.

**Main Workflow:**
1. A delay reason category is selected (Weather, Air Traffic Control, Mechanical Issue, Crew Availability, Security, Late Arriving Aircraft, Fueling Delay, Catering, Baggage Handling, or Other).
2. The system determines the standard estimated additional delay for that category.
3. The system pushes the flight's estimated departure forward by that duration.
4. The system records the delay reason and any associated notes.
5. The flight status is set to DELAYED.

**Standard Delay Durations:**
| Reason | Standard Added Delay |
|--------|----------------------|
| Weather | 45 min |
| Air Traffic Control | 20 min |
| Mechanical Issue | 60 min |
| Crew Availability | 30 min |
| Security | 25 min |
| Late Arriving Aircraft | 35 min |
| Fueling Delay | 15 min |
| Catering | 10 min |
| Baggage Handling | 15 min |
| Other | 20 min |

**Business Outcome:** Delays are categorised for reporting and performance analytics; departure times are updated to reflect operational reality.

---

## BP-011 — System Health Monitoring

**Confidence:** Confirmed  
**Actors:** System Administrator / DevOps (Assumed)  
**Preconditions:** The system is running.

**Main Workflow:**
1. An external monitoring tool calls `GET /actuator/health`.
2. The system returns a JSON status object (UP or DOWN) with component details.
3. Additional endpoints (`/actuator/info`, `/actuator/metrics`) provide version and runtime metrics.

**Business Outcome:** Operational uptime can be verified and monitored externally; alerts can be triggered on DOWN status.

---

## BP-012 — System Initialisation with Seed Data

**Confidence:** Confirmed  
**Actors:** System (startup process)  
**Preconditions:** Application is starting.

**Main Workflow:**
1. On startup, the system creates 15 sample flights spread across +15 to +360 minutes from startup time.
2. Two flights (UA456 with Weather delay; WN789 with Late Inbound delay) are pre-seeded with delays for realism.
3. The departure board is immediately operational with populated data.

**Business Outcome (Reference context):** Users immediately encounter a realistic, pre-populated departure board without manual data entry; supports training and demonstration scenarios.

---

*Business process analysis complete. 12 business processes identified.*
