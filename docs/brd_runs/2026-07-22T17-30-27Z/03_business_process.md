# BRD Business Process Translation — etd-airlines_1

**Run ID:** 2026-07-22T17-30-27Z  
**Target:** `etd-airlines_1`  
**Source:** `02_discovery.md`

## 1. Business context and operating model
- **Business purpose (Confirmed):** The application supports airline or airport departure-operations staff in monitoring planned departures, adjusting estimated departure times, recording disruptions, and reviewing current operational performance.
- **Operating style (Confirmed):** The product behaves as a live operations board rather than a customer-facing booking system. It is optimized for frequent review and rapid action on a current-day flight list.
- **Delivery context (Inferred):** The current implementation appears to represent a demo/reference operations console where simulated flight events stand in for real operational feeds.
- **Control environment (Confirmed):** In the current implementation, any user who can open the interface can perform all operational actions; no role segregation or approval workflow is enforced.

## 2. Business actors and responsibilities
| Actor | Responsibility | Confidence |
|---|---|---|
| Operations controller / gate operations agent | Monitor flights, search for a flight, update ETD, mark departures, cancel flights, and create ad hoc flight records. | Inferred |
| Operations supervisor / analyst | Review KPI cards and charts to understand punctuality, delay mix, and airline distribution. | Inferred |
| Any browser user in the current implementation | Can access all pages and execute all actions because no access controls are implemented. | Confirmed |
| Simulated operational feed | Seeds sample flights and introduces periodic status recalculation and random delays to mimic live operations. | Inferred |

## 3. End-to-end user journeys
### Journey A — Monitor the live departure board
1. User opens the application.
2. Dashboard data and KPI metrics load immediately.
3. User reviews current flights, statuses, delays, and last refresh time.
4. System auto-refreshes active monitoring views every 15 seconds.
5. User drills into a specific flight or takes an operational action when needed.

### Journey B — Investigate and manage a specific disruption
1. User searches by flight number, airline, origin, or destination.
2. User filters or sorts the list to focus on delayed, boarding, or imminent flights.
3. User opens the ETD update dialog for the impacted flight.
4. User records a revised ETD plus optional delay reason and notes.
5. System recalculates the operational status and refreshes the dashboard.

### Journey C — Add an unscheduled or newly tracked flight
1. User opens the Add Flight tab.
2. User enters the flight master data and planned departure.
3. System validates required fields and rejects duplicates.
4. If accepted, the flight appears on the dashboard with an initial scheduled state.

### Journey D — Close out or remove a flight
1. User selects Mark Departed, Cancel Flight, or Delete from the dashboard.
2. System asks for confirmation or notes, depending on the action.
3. System applies the state change and refreshes the operational view.
4. For delete, the record is permanently removed in the current implementation.

## 4. UI flow to business process mapping
| UI location | User action | Business process | Business outcome | Confidence |
|---|---|---|---|---|
| Dashboard tab | View KPI cards and flight table | Monitor current departures | Current operational state is visible for action and oversight. | Confirmed |
| Dashboard search bar | Enter search text | Search and identify flights | User narrows the working set to relevant flights. | Confirmed |
| Dashboard filters and sort controls | Filter by status; sort list | Prioritize operational work | User focuses on urgent or relevant flights. | Confirmed |
| Dashboard row action: Update ETD | Open modal and submit ETD update | Manage flight delay / ETD revision | Flight timing and status reflect the latest expected departure plan. | Confirmed |
| Dashboard row action: Mark Departed | Confirm departure | Close operational tracking for a departed flight | Flight status becomes departed and actual departure is recorded. | Confirmed |
| Dashboard row action: Cancel Flight | Enter notes and confirm cancellation | Record cancellation | Flight is removed from active operations and marked cancelled. | Confirmed |
| Dashboard row action: Delete | Confirm deletion | Remove flight record | Record is permanently removed from the current store. | Confirmed |
| Statistics tab | View charts | Review operational performance | Supervisor sees aggregate punctuality and disruption patterns. | Confirmed |
| Add Flight tab | Submit flight form | Register a new flight | A new operational flight record becomes visible for monitoring. | Confirmed |

## 5. Business processes and use cases

### Process 1 — Monitor current departures
**Confidence:** Confirmed  
**Actors:** Operations controller / gate operations agent (Inferred); any browser user in the current implementation (Confirmed)  
**Description:** The system shall present a live operational view of current departures so users can understand the current flight portfolio, delay position, and readiness for action.  
**Trigger:** User opens the application or returns to the Dashboard tab.  
**Preconditions:** The application is running; flight records exist or the dashboard can return an empty list; the user can access the browser UI.  
**Postconditions:** The user sees the latest available flights, KPI summaries, and the last update time.  
**Normal Flow:**
1. System initializes the dashboard when the application loads.
2. System retrieves current flight data and statistics.
3. System updates KPI cards for total flights, on-time flights, delayed flights, cancelled flights, on-time percentage, and average delay.
4. System renders the flight table with timing, route, gate, status, delay reason, and action controls.
5. System stamps the page with the latest refresh time.
6. System refreshes the active monitoring view automatically every 15 seconds.
**Alternative Flow:**
1. If no flights are available, the dashboard still loads and shows an empty operational board.
2. If the user switches to the Statistics tab, statistics refresh behavior continues for the active tab.
**Exceptions:**
1. If the flight or statistics request fails, the UI shows an error toast and the board is not refreshed successfully.
**Business Rules:**
1. The live board is a monitoring tool for departure operations, not a booking or passenger servicing workflow.
2. KPI calculations reflect the current in-memory flight set only.
3. The last update timestamp must show when the board was most recently refreshed.
**Special Requirements:**
1. Dashboard refresh must support near-real-time operational monitoring.
2. The interface is single-page and tab-based.
**Notes and Issues:**
1. In the current implementation, all users share the same unrestricted view and action set.
2. Because data is in-memory only, the board is not a durable operational record.

### Process 2 — Search, filter, and prioritize flights
**Confidence:** Confirmed  
**Actors:** Operations controller / gate operations agent (Inferred); any browser user in the current implementation (Confirmed)  
**Description:** The system shall help users quickly locate flights and prioritize operational attention using search, filter, and sort controls.  
**Trigger:** User enters search text or changes dashboard filter/sort controls.  
**Preconditions:** The dashboard is loaded and flight data is available to query or display.  
**Postconditions:** The displayed working list is narrowed or reordered to support operational triage.  
**Normal Flow:**
1. User enters text into the dashboard search field.
2. System waits for a short debounce interval.
3. System sends the search term to the flight list endpoint.
4. Server matches the search term against flight number, origin, destination, and airline.
5. System displays the returned list.
6. User optionally filters the list by status.
7. User optionally sorts the list by ETD, scheduled departure, delay magnitude, or flight number.
**Alternative Flow:**
1. User can sort or filter after an unfiltered full load without entering search text.
2. Search executes server-side, while status filtering and sorting execute client-side after records are loaded.
**Exceptions:**
1. If the search request fails, the UI cannot present refreshed results and should notify the user.
**Business Rules:**
1. Search uses case-insensitive substring matching.
2. Search fields are limited to flight number, airline, origin, and destination.
3. Status filter and sort do not change the underlying flight records.
**Special Requirements:**
1. Search should respond quickly enough for live operational use.
2. Search uses a 300 ms debounce to reduce unnecessary traffic.
**Notes and Issues:**
1. Search criteria do not include gate, aircraft type, delay reason, or notes.

### Process 3 — Register a new flight for monitoring
**Confidence:** Confirmed  
**Actors:** Operations controller / gate operations agent (Inferred); any browser user in the current implementation (Confirmed)  
**Description:** The system shall allow a user to create a new flight record so the departure can be tracked operationally.  
**Trigger:** User submits the Add Flight form.  
**Preconditions:** User can access the Add Flight tab and enter valid flight data.  
**Postconditions:** A new flight is stored and appears on the operational board with an initial scheduled state.  
**Normal Flow:**
1. User opens the Add Flight tab.
2. System prepopulates scheduled departure to approximately one hour from the current browser time.
3. User enters flight number, airline, aircraft type, origin, destination, gate, and scheduled departure.
4. Browser normalizes flight number, origin, and destination to uppercase.
5. System validates the submission.
6. If valid, system creates the flight with estimated departure equal to scheduled departure.
7. System assigns an initial status of SCHEDULED.
8. System confirms success through an inline success alert and a toast.
9. System refreshes the dashboard so the flight becomes visible in monitoring views.
**Alternative Flow:**
1. A user may create a flight even if similar airline or route values already exist, provided the uniqueness rule is not violated.
**Exceptions:**
1. If required fields are missing or malformed, the system returns a validation error with field details.
2. If the same flight number already exists for the same scheduled departure, the system rejects the create request.
**Business Rules:**
1. Flight number is required and must match 2–3 uppercase letters followed by 1–4 digits.
2. Airline is required.
3. Origin and destination are required and must be 3-letter uppercase IATA codes.
4. Scheduled departure is required.
5. Duplicate flights are not permitted for the same flight number and scheduled departure timestamp.
**Special Requirements:**
1. Creation feedback must be visible immediately in the UI.
2. New flights should be ready for dashboard monitoring without a full page reload.
**Notes and Issues:**
1. No approval, dual-control, or role restriction exists for creating flights.
2. Newly created flights are not persisted beyond application runtime.

### Process 4 — Update ETD and record delay context
**Confidence:** Confirmed  
**Actors:** Operations controller / gate operations agent (Inferred); any browser user in the current implementation (Confirmed)  
**Description:** The system shall allow a user to revise a flight’s estimated departure time and capture optional disruption context so the operational board reflects the latest expectation.  
**Trigger:** User selects Update ETD from a dashboard row and submits the ETD modal.  
**Preconditions:** A target flight exists and can be opened from the dashboard.  
**Postconditions:** The flight record stores the revised ETD and optional delay context, and the displayed status is recalculated.  
**Normal Flow:**
1. User selects Update ETD for a flight.
2. System opens a modal showing current flight and route context.
3. User enters a new estimated departure time.
4. User optionally selects a delay reason.
5. User optionally enters delay notes.
6. System validates the ETD request.
7. System saves the revised ETD and optional context.
8. System recalculates the flight status based on current time and delay state.
9. System refreshes the dashboard and acknowledges the update.
**Alternative Flow:**
1. If no delay reason or delay notes are provided, the ETD update can still proceed.
2. If the revised ETD is earlier than scheduled departure, delay minutes are treated as zero rather than negative.
**Exceptions:**
1. If the new ETD is missing, the request is rejected.
2. If the new ETD is more than 5 minutes in the past relative to server time, the request is rejected.
3. If the flight no longer exists, the system returns not found.
**Business Rules:**
1. New ETD is mandatory for ETD revision.
2. ETD cannot be set more than 5 minutes in the past relative to server time.
3. Delay minutes are never negative.
4. A flight is considered delayed only when ETD is at least 15 minutes after scheduled departure.
5. Supported delay reasons are Weather, Air Traffic Control, Mechanical Issue, Crew Availability, Security, Late Arriving Aircraft, Fueling, Catering, Baggage Handling, and Other.
6. Estimated delay defaults exist by delay reason in the domain logic, although the manual ETD update flow submits an explicit ETD value.
**Special Requirements:**
1. The modal must show enough route/flight context to reduce operator error.
2. Status recalculation must happen immediately after update.
**Notes and Issues:**
1. Time handling may be confusing because browser inputs are local while server serialization is in UTC.
2. No audit trail exists for ETD changes.

### Process 5 — Cancel a flight
**Confidence:** Confirmed  
**Actors:** Operations controller / gate operations agent (Inferred); any browser user in the current implementation (Confirmed)  
**Description:** The system shall allow a user to mark a flight as cancelled and retain explanatory notes.  
**Trigger:** User selects Cancel Flight from the dashboard and confirms via prompt input.  
**Preconditions:** A target flight exists on the dashboard.  
**Postconditions:** The flight status becomes CANCELLED and cancellation notes are retained in the flight record.  
**Normal Flow:**
1. User selects Cancel Flight for a flight.
2. System opens a browser prompt asking for notes.
3. User enters optional notes.
4. System updates the flight status to CANCELLED.
5. System stores the entered notes in the delay notes field.
6. System refreshes the dashboard and confirms success.
**Alternative Flow:**
1. Cancellation can proceed even if the user provides no notes.
**Exceptions:**
1. If the flight does not exist, the request fails with a not-found response.
2. If the cancel request fails for another reason, the UI shows an error toast.
**Business Rules:**
1. Cancellation is a terminal status preserved by automatic status computation.
2. There is no dedicated cancellation-reason field; cancellation notes reuse the delay notes field.
**Special Requirements:**
1. The UI uses a simple browser prompt rather than a structured cancellation form.
**Notes and Issues:**
1. Whether cancellation should require a formal reason code is not defined.
2. No audit or approval control exists for cancellation.

### Process 6 — Mark a flight departed
**Confidence:** Confirmed  
**Actors:** Operations controller / gate operations agent (Inferred); any browser user in the current implementation (Confirmed)  
**Description:** The system shall allow a user or automatic timing rule to close a flight as departed once it has left operational control.  
**Trigger:** User selects Mark Departed, or system time passes the flight’s ETD.  
**Preconditions:** A target flight exists and is not already in a preserved terminal state.  
**Postconditions:** The flight status becomes DEPARTED; a manual departure action also records actual departure time.  
**Normal Flow:**
1. User selects Mark Departed from the dashboard.
2. System asks for confirmation.
3. User confirms the action.
4. System sets the flight status to DEPARTED.
5. System records actual departure as the current server time.
6. System refreshes the dashboard and confirms success.
**Alternative Flow:**
1. During automated status recomputation, a flight may become DEPARTED automatically when current time is past ETD.
**Exceptions:**
1. If the flight does not exist, the action fails with not found.
2. If the depart request fails, the UI shows an error toast.
**Business Rules:**
1. DEPARTED is a terminal status preserved by later automatic status computation.
2. If current time is past ETD, computed status becomes DEPARTED.
**Special Requirements:**
1. Departure marking must be fast enough for operational close-out use.
**Notes and Issues:**
1. It is unresolved whether automatic post-ETD departure is acceptable business behavior or whether manual confirmation should be mandatory.
2. Automatically computed departure does not necessarily capture actual departure time the same way as the manual action.

### Process 7 — Delete a flight record
**Confidence:** Confirmed  
**Actors:** Any browser user in the current implementation (Confirmed)  
**Description:** The system currently allows permanent removal of a flight record from the operational list.  
**Trigger:** User selects Delete from the dashboard and confirms the action.  
**Preconditions:** A target flight exists.  
**Postconditions:** The flight record is removed from the repository and no longer appears in dashboard or statistics results.  
**Normal Flow:**
1. User selects Delete for a flight.
2. System asks for confirmation.
3. User confirms deletion.
4. System removes the flight record permanently.
5. System refreshes the dashboard and confirms success.
**Alternative Flow:**
1. User cancels the confirmation dialog and the record remains unchanged.
**Exceptions:**
1. If the flight does not exist, the delete request fails with not found.
2. If the delete request fails, the UI shows an error toast.
**Business Rules:**
1. Delete is a hard delete with no recovery or restore process in the current implementation.
2. Successful delete returns no content to the client.
**Special Requirements:**
1. Deletion currently relies on a simple confirmation dialog only.
**Notes and Issues:**
1. It is unresolved whether deletion is a true business operation or merely a demo/admin convenience.
2. Hard delete conflicts with typical operational recordkeeping expectations because there is no audit history or recovery path.

### Process 8 — Review operational statistics
**Confidence:** Confirmed  
**Actors:** Operations supervisor / analyst (Inferred); any browser user in the current implementation (Confirmed)  
**Description:** The system shall summarize the current flight portfolio into operational KPIs and charts so supervisors can assess punctuality and disruption patterns.  
**Trigger:** User opens the Statistics tab or the system refreshes the active statistics view.  
**Preconditions:** The application is running and statistics can be calculated from the current flight set.  
**Postconditions:** User sees aggregated counts, rates, and distributions for the current operational data set.  
**Normal Flow:**
1. User opens the Statistics tab.
2. System lazily loads statistics data.
3. System calculates current totals, on-time count, delayed count, cancelled count, on-time percentage, and average delay.
4. System groups flights by status, delay reason, and airline.
5. System renders a status doughnut chart, delay-reason bar chart, and airline bar chart.
6. While the Statistics tab is active, the system refreshes the view periodically.
**Alternative Flow:**
1. If there are no flights, rate-based measures return zero rather than failing.
**Exceptions:**
1. If statistics retrieval fails, the UI shows an error toast.
**Business Rules:**
1. On-time percentage is calculated as on-time flights divided by total flights, or zero when no flights exist.
2. Average delay is calculated only across delayed flights and rounded to one decimal place.
3. On-time counts exclude cancelled flights and include non-delayed, non-cancelled flights regardless of other statuses.
**Special Requirements:**
1. Visual summaries should support rapid supervisor interpretation.
**Notes and Issues:**
1. Statistics reflect only the current in-memory data set and are not historical trend analytics.

### Process 9 — Simulate live operational activity
**Confidence:** Confirmed  
**Actors:** Simulated operational feed (Inferred); any browser user observing the live board (Confirmed)  
**Description:** The system shall seed and periodically adjust flights to simulate a live operational environment for demonstration and testing.  
**Trigger:** Application startup and each 30-second simulation interval.  
**Preconditions:** The application starts successfully.  
**Postconditions:** Initial sample flights exist, statuses are recalculated regularly, and occasional random delays appear in the dataset.  
**Normal Flow:**
1. On startup, system creates 15 sample flights across multiple carriers and airports.
2. System initializes preset delays for at least two flights.
3. Every 30 seconds, system recalculates status for all flights.
4. On each cycle, system may select a scheduled, on-time flight and add a random delay.
5. Refreshed operational data becomes visible on the UI during the next front-end refresh cycle.
**Alternative Flow:**
1. If no eligible flight exists for random delay injection, the cycle completes with status recomputation only.
**Exceptions:**
1. No business-facing exception path is defined for simulation failures.
**Business Rules:**
1. Random delay injection occurs with a 20% chance on each simulation cycle.
2. The simulator affects only flights that are still on-time and scheduled when selecting a random delay candidate.
3. Preset sample delays include weather and late-arriving-aircraft scenarios.
**Special Requirements:**
1. This process supports demonstration realism rather than production-grade feed integration.
**Notes and Issues:**
1. The simulation service appears to stand in for real external airline, ATC, or weather data feeds.
2. This reinforces the inference that the application is currently a demo/reference tool.

## 6. Cross-process business rules
| Business Rule | Confidence |
|---|---|
| Terminal statuses CANCELLED, DIVERTED, and DEPARTED are preserved during automatic status recalculation. | Confirmed |
| If current time is within 30 minutes of ETD, a non-delayed flight moves to BOARDING; a delayed flight remains DELAYED. | Confirmed |
| Otherwise, a flight is SCHEDULED when on time and DELAYED when the delay threshold is met. | Confirmed |
| DIVERTED exists as a supported status, but no end-user workflow was discovered to set or manage it. | Confirmed / Unresolved |
| All create, update, cancel, depart, and delete actions currently occur without authentication or role-based approval. | Confirmed |
| Business errors and validation failures return user-visible messages through the UI toast/alert pattern. | Confirmed |

## 7. Operational scenarios to carry into the BRD
1. **Routine live monitoring:** Operations staff use the dashboard as a rolling departure board refreshed throughout the day.
2. **Disruption handling:** Staff update ETD and annotate reason/notes when a flight slips.
3. **Flight close-out:** Staff or automated timing rules move flights to departed when no longer active.
4. **Cancellation management:** Staff can remove a flight from active operations by marking it cancelled.
5. **Exception administration:** Staff can permanently delete a flight, though business justification is unresolved.
6. **Supervisor oversight:** Supervisors review real-time punctuality and disruption mix via dashboard KPIs and charts.
7. **Demo/training mode:** Seeded flights and simulated delays provide a realistic sandbox for showing the workflow.

## 8. Notes and Issues for BRD follow-up
1. **Unresolved:** Define whether delete should exist in the target business process and, if so, under what controls.
2. **Unresolved:** Define whether flights may auto-transition to DEPARTED or must always be confirmed by an operator.
3. **Unresolved:** Define the business process, actor, and rules for DIVERTED flights.
4. **Unresolved:** Define authorization boundaries and role segregation for create/update/cancel/delete actions.
5. **Unresolved:** Define persistence, audit retention, recovery, and historical reporting expectations.
6. **Confirmed constraint:** Current time handling mixes local browser datetime entry with UTC server serialization and may require explicit business timezone rules.
7. **Confirmed constraint:** The present implementation is not suitable as a durable operational system because records disappear on restart.
