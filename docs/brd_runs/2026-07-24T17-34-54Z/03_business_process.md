# Step 3 — Business Process Translation

## Process BP-001: Monitor Departure Operations Dashboard
- **Actors:** Operations Monitor, Flight Operations Supervisor **[Inferred]**
- **Description:** Monitor live flight performance via KPIs, filters, and table status to identify flights requiring intervention. **[Confirmed]**
- **Trigger:** User opens dashboard or auto-refresh interval executes. **[Confirmed]**
- **Preconditions:** Application is running; flights exist in repository. **[Confirmed]**
- **Postconditions:** Updated operational snapshot visible for decision-making. **[Inferred]**
- **Normal Flow:**
  1. System loads flights and aggregate statistics. **[Confirmed]**
  2. User filters/searches/sorts list. **[Confirmed]**
  3. System displays status, delay, and reason indicators. **[Confirmed]**
- **Alternative Flow:**
  - No flights match selected filters; system displays no-match message. **[Confirmed]**
- **Exceptions:**
  - API call failure shows toast error. **[Confirmed]**
- **Business Rules:** Delay badge and status classifications follow server-calculated delay/status logic. **[Confirmed]**
- **Special Requirements:** Refresh interval supports near-live situational awareness every 15 seconds. **[Confirmed]**
- **Notes and Issues:** No role-based restriction on view/edit actions. **[Confirmed]**

## Process BP-002: Create New Flight Record
- **Actors:** Dispatcher / Schedule Coordinator **[Inferred]**
- **Description:** Create a new outbound flight so it appears in operations monitoring and downstream ETD management. **[Confirmed]**
- **Trigger:** User submits Add Flight form. **[Confirmed]**
- **Preconditions:** Required fields provided in valid format. **[Confirmed]**
- **Postconditions:** New flight saved with default status SCHEDULED and ETD equal to scheduled departure. **[Confirmed]**
- **Normal Flow:**
  1. User enters flight details and submits. **[Confirmed]**
  2. System validates required and pattern-constrained fields. **[Confirmed]**
  3. System checks duplicate rule for flight number/time combination. **[Confirmed]**
  4. System persists and returns created flight. **[Confirmed]**
- **Alternative Flow:**
  - Optional fields (gate, aircraft type) omitted; creation still succeeds. **[Confirmed]**
- **Exceptions:**
  - Validation failure returns field-level 400 errors. **[Confirmed]**
  - Duplicate rule violation returns 400 with business message. **[Confirmed]**
- **Business Rules:** Flight number and IATA formats are enforced; scheduled departure mandatory. **[Confirmed]**
- **Special Requirements:** UI uppercases specific fields before submission. **[Confirmed]**
- **Notes and Issues:** No explicit business ownership metadata captured (e.g., terminal/team). **[Unresolved]**

## Process BP-003: Manage ETD and Operational Status
- **Actors:** Operations Controller **[Inferred]**
- **Description:** Update ETD, mark departures, and cancel flights to maintain accurate operational state. **[Confirmed]**
- **Trigger:** Row action buttons (edit ETD, depart, cancel, delete). **[Confirmed]**
- **Preconditions:** Target flight exists. **[Confirmed]**
- **Postconditions:** Flight status and departure attributes reflect latest operational decision. **[Confirmed]**
- **Normal Flow:**
  1. User selects a flight action. **[Confirmed]**
  2. For ETD change, user submits new ETD and optional reason/notes. **[Confirmed]**
  3. System validates ETD is not >5 minutes in past. **[Confirmed]**
  4. System recalculates status from ETD/time rules. **[Confirmed]**
- **Alternative Flow:**
  - User marks departed directly; system stores actual departure timestamp. **[Confirmed]**
  - User cancels flight with optional note; status becomes CANCELLED. **[Confirmed]**
- **Exceptions:**
  - Missing/nonexistent ID returns not found error. **[Confirmed]**
- **Business Rules:**
  - Terminal statuses persist in status computation.
  - Delay threshold = 15 minutes.
  - Within 30 minutes of ETD, status shifts to BOARDING unless delayed. **[Confirmed]**
- **Special Requirements:** Operation outcome must be surfaced to user via toast/alert feedback. **[Confirmed]**
- **Notes and Issues:** Delete is irreversible and has only browser confirmation prompt. **[Confirmed]**

## Process BP-004: Produce Operational Statistics
- **Actors:** Operations Monitor, Leadership Viewer **[Inferred]**
- **Description:** Aggregate current flight dataset into KPI and chart metrics for tactical decision support. **[Confirmed]**
- **Trigger:** Statistics tab opened or dashboard refresh initiated. **[Confirmed]**
- **Preconditions:** Flight data available in repository. **[Confirmed]**
- **Postconditions:** Updated metrics rendered (status mix, delay reasons, airline distribution). **[Confirmed]**
- **Normal Flow:**
  1. System calculates counts and percentages from all flights. **[Confirmed]**
  2. System returns structured statistics payload. **[Confirmed]**
  3. UI renders charts/KPIs. **[Confirmed]**
- **Alternative Flow:**
  - No delay reasons exist; reason panel displays informational text. **[Confirmed]**
- **Exceptions:**
  - Stats retrieval failure surfaces toast. **[Confirmed]**
- **Business Rules:** On-time excludes cancelled flights; average delay computed from delayed flights only. **[Confirmed]**
- **Special Requirements:** Visual insight only; no export/download/report file generation found. **[Inferred]**
- **Notes and Issues:** Historical trend analytics not implemented. **[Unresolved]**
