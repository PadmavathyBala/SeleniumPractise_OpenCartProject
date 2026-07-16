# Step 6 — Review Findings (brd-review-reflect)

**Run ID:** `2026-07-16T03-46-24Z`  
**Agent:** brd-review-reflect  
**Input:** 05_authoring_draft.md  
**Total findings:** 18

---

## Review Summary

The first BRD draft is structurally sound and well-evidenced. All 23 sections are present, confidence labels are consistently applied, and the requirements are predominantly in business language. The following 18 findings are raised for resolution before the document can be considered final.

---

## Findings

### RF-001 — Section 7 (User Roles): Viewer and Supervisor roles need clearer framing

**Affected section:** Section 7  
**Issue:** The "Viewer" and "Supervisor" roles are listed with Assumed confidence and described as "Not implemented." Including unimplemented roles without a clear statement of intent — whether they are in-scope future requirements or merely noted for completeness — risks reader confusion. A reader might assume these are current-state roles.  
**Recommended revision:** Add an explicit note to Section 7 stating that Viewer and Supervisor roles are out-of-scope for the current system and are identified as future requirements. Cross-reference OOS-002.

---

### RF-002 — FR-006 / FR-018: Auto-refresh interval should be expressed as a testable constraint

**Affected section:** Section 11.1 (FR-006), Section 11.3 (FR-018)  
**Issue:** FR-006 states "auto-refresh every 15 seconds" and FR-018 states the same for Statistics. While the 15-second value is Confirmed from the code, these should be stated as constraints with a tolerance, or at minimum with the exact value, so testers know what to measure.  
**Recommended revision:** Revise to: "The departure board shall auto-refresh at a fixed interval of 15 seconds (±1 second tolerance) when it is the active tab." Apply the same phrasing to FR-018.

---

### RF-003 — FR-011: Circular reference to "Business Rules" without IDs is untestable

**Affected section:** Section 11.2 (FR-011)  
**Issue:** FR-011 references "the status transition rules defined in Business Rules" without citing specific BRULE IDs, making it impossible to determine precisely which rules apply or to test FR-011 in isolation.  
**Recommended revision:** Revise to: "…applying the status transition rules defined in BRULE-001, BRULE-002, BRULE-003, BRULE-004, and BRULE-006."

---

### RF-004 — Section 12 (BRULE-005): "More than 5 minutes in the past" is ambiguous

**Affected section:** Section 12 (BRULE-005)  
**Issue:** The rule states the ETD "must not be more than 5 minutes earlier than the current system time." The word "earlier" combined with "more than 5 minutes" could be misread. The code check is `isBefore(LocalDateTime.now().minusMinutes(5))`, meaning values between now−5 min and now are allowed.  
**Recommended revision:** Revise to: "A new ETD shall not be set to a value more than 5 minutes before the system time at the moment of the update request. ETDs between (now − 5 minutes) and the future are accepted."

---

### RF-005 — Section 12 (BRULE-004): Delay threshold should specify "inclusive"

**Affected section:** Section 12 (BRULE-004)  
**Issue:** "At least 15 minutes" is mathematically unambiguous, but the code uses `>= 15`. The BRD should explicitly say "greater than or equal to 15 minutes" or "15 minutes or more" to remove any room for implementation drift.  
**Recommended revision:** Revise to: "A flight is classified as delayed when its estimated departure is 15 minutes or more later than its scheduled departure."

---

### RF-006 — Section 11 (FR-015): Delay row highlight description is inconsistent with actual UI

**Affected section:** Section 11.2 (FR-015)  
**Issue:** FR-015 states cancelled rows are "grey with strike-through appearance." The code uses CSS class `row-cancelled` but no explicit strikethrough is confirmed in the discovery findings — the code only adds a row class, not a `text-decoration: line-through`. Overstating confirmed CSS behaviour introduces a false Confirmed requirement.  
**Recommended revision:** Revise to: "The system shall visually differentiate flight rows by status: delayed rows shall be highlighted in amber (class `row-delayed`), cancelled and departed rows shall be displayed in muted/grey styling (classes `row-cancelled`, `row-departed`)." Add confidence label Inferred for the exact visual appearance since styles.css was not fully read during discovery.

---

### RF-007 — Section 19 (Risks): R-002 has no mitigation strategy

**Affected section:** Section 19 (R-002)  
**Issue:** R-002 (no authentication, any network user can modify data) is rated High/High but the risk table has no mitigation or recommended action. For a BRD, noting the recommended mitigation aligns with standard risk documentation practice.  
**Recommended revision:** Add a "Mitigation" column to the risk table. For R-002: "Mitigation: Implement Spring Security with role-based access control before production deployment (see OOS-002 / future BR)."

---

### RF-008 — Section 21 (OQ-007): Open question about guard conditions should be a proposed business rule

**Affected section:** Section 21 (OQ-007)  
**Issue:** "Should re-cancelling or departing a cancelled flight be prevented?" is framed as an open question, but the discovery confirmed this gap exists. A senior BA would typically raise this as a proposed business rule that needs stakeholder sign-off rather than leaving it purely as an open question.  
**Recommended revision:** Promote to a proposed BRULE (e.g., BRULE-015 [Proposed — Unresolved]: "The system should prevent an operator from marking a CANCELLED flight as DEPARTED or re-cancelling a CANCELLED flight."). Retain OQ-007 with a cross-reference to the proposed rule.

---

### RF-009 — Section 22 (Acceptance Criteria): AC-002 search criterion uses only one example

**Affected section:** Section 22 (AC-002)  
**Issue:** AC-002 tests only flight number matching ("AA"). The requirement also covers airline name, origin, and destination. A single example criterion is insufficient to validate all four search fields.  
**Recommended revision:** Split AC-002 into four sub-criteria or expand it: "…Given the operator enters a search term, then only flights where the flight number, airline name, origin airport code, or destination airport code contains the search term (case-insensitive) are displayed. Example: entering 'DFW' returns flights where origin or destination is 'DFW' or airline/flight number contains 'DFW'."

---

### RF-010 — Section 10 (BR-002): "Authorised" implies authentication that does not exist

**Affected section:** Section 10 (BR-002)  
**Issue:** BR-002 says "authorised operations staff" but Sections 17 (C-002) and 16 (A-002) both confirm there is no authentication or authorisation. Using "authorised" without qualification implies a control that does not exist.  
**Recommended revision:** Revise to: "The system shall allow operations staff to create, update, cancel, and remove flight records. Note: no authorisation control is currently implemented; this requirement will be enforced via role-based access control in a future release (see OOS-002)."

---

### RF-011 — Section 14 (INT-003): "Designed to support" is not a testable requirement

**Affected section:** Section 14 (INT-003)  
**Issue:** "The system shall be designed to support future integration…" is not testable. How would a tester verify that a future-facing design intent is met?  
**Recommended revision:** Either demote INT-003 to Section 21 (Open Questions / Future Requirements) or rephrase as a verifiable constraint: "The ETD calculation logic shall be encapsulated in a dedicated service component (`EtdCalculationService`) with a clear interface, enabling future replacement of static delay estimates with live data feeds."

---

### RF-012 — Section 15 (NFR-005): "Sufficient time" is not a testable SLA

**Affected section:** Section 15 (NFR-005)  
**Issue:** "Within a time sufficient for external monitoring to distinguish UP from DOWN status" is vague and untestable.  
**Recommended revision:** Mark as Unresolved with a note: "The maximum acceptable health-check response time is not defined. A target of 2 seconds at the 95th percentile is proposed pending stakeholder confirmation." This acknowledges the gap without fabricating a number.

---

### RF-013 — Section 4 (Business Objectives): BO-001 lacks a definition of "accurate and continuously updated"

**Affected section:** Section 4 (BO-001)  
**Issue:** "Accurate, continuously updated view" is not measurable. What does "continuously" mean — within 1 second, within 30 seconds, within 15 seconds?  
**Recommended revision:** Revise to: "Provide departure operations staff with an automatically refreshed view of all flight departures, updated at most every 15 seconds under normal operating conditions."

---

### RF-014 — Section 13 (DATA-003): Persistence constraint should appear in both Data Requirements and Constraints

**Affected section:** Section 13 (DATA-003) and Section 17  
**Issue:** DATA-003 and C-001 say nearly the same thing. The duplication is not inherently wrong, but the statements use slightly different wording which could lead to inconsistency if one is updated without the other.  
**Recommended revision:** In Section 13, retain DATA-003 as the data-layer statement. In Section 17 (C-001), cross-reference DATA-003 explicitly: "Data is non-persistent; see DATA-003."

---

### RF-015 — Missing alternate flow: what happens when all flights match a search/filter?

**Affected section:** Section 9 (BP-001 alternate flows)  
**Issue:** BP-001 alternate flows describe filtering but do not cover the case where zero flights match. FR-001 also does not address this. The UI does handle this (displays "No flights match"), but the BRD should capture it.  
**Recommended revision:** Add to BP-001 alternate flows and add FR-001a (or a sub-point): "If no flights match the active search term or filter, the departure board shall display a clear message indicating no results were found, rather than an empty table."

---

### RF-016 — Section 22 (AC-014): Acceptance criterion arithmetic error

**Affected section:** Section 22 (AC-014)  
**Issue:** AC-014 states: "10 flights where 3 are delayed and 1 is cancelled → on-time=6." By BRULE-012, on-time = not delayed AND not cancelled = 10 − 3 delayed − 1 cancelled = 6. This is correct. However, the criterion should also verify on-time% = 60%, not simply state it. The stated criterion should explicitly test that 6/10 × 100 = 60.0%.  
**Recommended revision:** Revise to: "…the statistics page shows total=10, on-time count=6, on-time percentage=60.0%, delayed=3, cancelled=1." (No change needed on the arithmetic, but clarify the percentage is exactly 60.0%.)

---

### RF-017 — Section 20 (OOS-010): Divert functionality is understated as out-of-scope

**Affected section:** Section 20 (OOS-010)  
**Issue:** DIVERTED is a live status in the system's enum and is preserved by the lifecycle engine, but there is no API endpoint to set it. This is a confirmed gap (D3.6, D11.5) and should be more explicitly framed: it is not merely out-of-scope, it is an incomplete feature — the status exists but is unreachable by operators.  
**Recommended revision:** Add an open question (OQ-010): "The DIVERTED status is defined in the system but cannot be set via any current API endpoint. Stakeholders should confirm whether a `/divert` endpoint is required as part of the next release." Cross-reference OOS-010.

---

### RF-018 — Section 23 (Traceability Matrix): Placeholder must clearly reference the output file

**Affected section:** Section 23  
**Issue:** The traceability matrix is a placeholder with a note referencing 07_traceability_matrix.md. The final BRD should embed the full matrix rather than referencing an external file, so that the BRD is a self-contained document.  
**Recommended revision:** In the revised BRD (06b), leave Section 23 as a placeholder with a clear instruction: "After Step 7 (Traceability), this section will be replaced with the full matrix from 07_traceability_matrix.md." In the final BRD (08), the matrix shall be embedded in full.

---

*Review complete. 18 findings produced. Saved to 06_review_findings.md.*
