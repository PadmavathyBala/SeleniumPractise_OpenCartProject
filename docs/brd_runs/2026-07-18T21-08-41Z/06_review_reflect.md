# Step 6 — Review and Reflection

**Run ID:** `2026-07-18T21-08-41Z`  
**Agent:** brd-review-reflect  
**Pipeline Step:** 6 of 8 — Review

---

## 1. Review Scope

This document reviews the BRD draft (`05_brd_draft.md`) for:
- Accuracy and completeness against the discovered codebase.
- Proper use of confidence labels.
- Structural completeness per the required template.
- Internal consistency between sections.
- Identification of missing content, hallucinations, or unsupported claims.

---

## 2. Structural Completeness Check

| Section | Required | Present | Notes |
|---|---|---|---|
| Front Matter (version, date, run ID, authors, history, approvals) | ✓ | ✓ | Approvals blank as required |
| 1. Introduction (1.1–1.5) | ✓ | ✓ | All subsections present |
| 2. Overall Description (2.1–2.8) | ✓ | ✓ | All subsections present |
| 3. System Features (3.N.1–3.N.4 per feature) | ✓ | ✓ | 6 features covered with full use-case schema |
| 4. External Interface Requirements (4.1–4.5) | ✓ | ✓ | 4.2 and 4.5 marked Unresolved appropriately |
| 5. NFRs (5.1–5.4) | ✓ | ✓ | All subsections present |
| 6. Other Requirements | ✓ | ✓ | IDE/Maven notes |
| 7. Success Criteria | ✓ | ✓ | Items SC-01 to SC-06; unquantified items Unresolved |
| 8. Approval and Sign-off | ✓ | ✓ | Blank as required |
| Appendix A: Glossary | ✓ | ✓ | 16 terms defined |
| Appendix B: Analysis Models | ✓ | ✓ | Marked Unresolved/Not Generated |
| Appendix C: Issues List | ✓ | ✓ | 10 open questions |
| Appendix D: Screenshots | ✓ | ✓ | Marked Unresolved/Not Generated |
| Appendix E: Test Scenarios | ✓ | ✓ | 2 confirmed tests listed; gaps noted |
| Appendix F: Traceability Matrix | ✓ | Partial | Summary in §05; full matrix in 07_traceability.md |

**Overall: COMPLETE** — all required template sections are present.

---

## 3. Confidence Label Audit

Spot-check of 20 randomly selected requirement statements across the draft:

| Check | Result |
|---|---|
| Every FR row has a label | ✓ Pass |
| Every BR row has a label | ✓ Pass |
| Every use-case field entry is labelled or inherits from section label | ✓ Pass |
| No Assumed or Unresolved item upgraded to Confirmed without evidence | ✓ Pass |
| Approvals table is blank | ✓ Pass |
| Success criteria that cannot be derived from code are Unresolved | ✓ Pass |

**Result: PASS** — confidence labeling is consistent and correct throughout.

---

## 4. Accuracy Issues Found

### ISSUE-01 — Severity: High
**Location:** §3.1.4 UC-001 — "Alternative Flow"  
**Issue:** States "a debounced (300 ms) API call is sent with the search term" — this
is accurate for the search box (`dashboard.js:debounce(refresh, 300)`). However, the
status filter and sort dropdown changes trigger `render()` (client-side only, no API
call). The description implies only the search box triggers an API call, which is
correct — but the phrasing could be read as suggesting all filter changes call the
API.  
**Recommendation:** Clarify that status filter and sort order operate on already-
fetched data client-side; only search term changes trigger a new API call.  
**Confidence of finding:** Confirmed — `dashboard.js:init()` lines for statusFilter
and sortBy listeners call `render`, not `refresh`.

### ISSUE-02 — Severity: High
**Location:** §3.2 — Feature: ETD/Delay Management, UC-002 Preconditions  
**Issue:** States "Flight exists in SCHEDULED, BOARDING, or DELAYED status (Inferred
— no guard in code)". This is accurate but the parenthetical note should be elevated
to the Issues List (Appendix C) as OQ-007 is related. The use case does not call
out that DEPARTED and CANCELLED flights can also receive ETD updates via API (no
server-side guard).  
**Recommendation:** Add an explicit Unresolved note: "The API does not prevent ETD
updates on DEPARTED or CANCELLED flights."  
**Confidence of finding:** Confirmed — `FlightService.updateEtd()` has no status
guard before updating ETD.

### ISSUE-03 — Severity: High
**Location:** §2.3 Business Objectives — BO-6  
**Issue:** BO-6 states "Support easy deployment in an Eclipse-based Java development
environment for demonstration purposes". While accurate, this frames the system
primarily as a demo/learning artefact. This conflicts with describing it as a
production departure management tool. The BRD should acknowledge both contexts
explicitly.  
**Recommendation:** Add a clarifying note in §1.1 or §2.2 that the application is
described in its README as a "reference application" and may require hardening before
production use.

### ISSUE-04 — Severity: Medium
**Location:** §2.6 Operating Environment  
**Issue:** Client dependencies listed as "Bootstrap 5 (CDN), Bootstrap Icons (CDN),
Chart.js (CDN)" — no version numbers specified for CDN-loaded libraries. The actual
CDN URLs in `index.html` contain specific version strings that should be cited.  
**Recommendation:** Add version numbers for CDN dependencies (to be read from
`index.html` CDN URLs) and note that these are external dependencies whose
availability is not under the system's control.  
**Confidence of finding:** Confirmed — versions are in CDN URLs in `index.html`.

### ISSUE-05 — Severity: Medium
**Location:** §3.4.4 UC-004 Notes and Issues  
**Issue:** States "Empty string note is a valid input (passes the `!== null` guard)".
While technically true, this may be a defect (an empty cancellation note provides no
useful information). This should be flagged as OQ-011 in Appendix C.  
**Recommendation:** Add OQ-011 to Appendix C: "Should an empty cancellation note be
allowed, or should a minimum note length be required?"

### ISSUE-06 — Severity: Medium
**Location:** §5.2 Security — NFR-015  
**Issue:** States "HTML output is escaped client-side for display fields (Confirmed
— `dashboard.js:escapeHtml()`)". The `escapeHtml()` function is applied to `airline`
and `gate` fields, but not necessarily to all fields. A quick review shows `origin`,
`destination`, `flightNumber`, and status fields are rendered directly. However,
those fields are constrained by server-side regex validation, reducing (but not
eliminating) XSS risk.  
**Recommendation:** Revise NFR-015 to "Client-side `escapeHtml()` is applied to
free-text fields (airline, gate, delayNotes); structured fields (flight number, IATA
codes) are constrained by server-side regex validation."

### ISSUE-07 — Severity: Low
**Location:** §4.2 Hardware Interfaces  
**Issue:** Marked as "Unresolved — no hardware-specific interfaces identified." This
is correct. However, it should also be Confirmed (not Unresolved) that no hardware
interfaces were found, which is a positive finding.  
**Recommendation:** Change to "No hardware interfaces identified or required.
**Confirmed** — no hardware-specific code or configuration found."

---

## 5. Missing Items

| Item | Severity | Recommendation |
|---|---|---|
| FR for `FlightRepository.findByOrigin()` and `findByDestination()` | Low | These methods exist in the repository but are not used by any service. Mark as Unresolved internal methods with no active use case. |
| FR for `WebConfig.java` CORS/web configuration | Low | `WebConfig.java` was not analysed in detail. Check for CORS configuration that could affect API access from non-localhost origins. |
| No mention of `@EnableScheduling` | Low | The simulation service uses `@Scheduled` — `@EnableScheduling` must be present somewhere (likely on `EtdApplication.java`). Verify and cite. |

---

## 6. Positive Findings

- All 28 FRs are backed by direct code citations — no requirements were invented
  without evidence.
- The distinction between the demo simulation (BP-07) and real business processes
  (BP-01 to BP-06) is clearly flagged.
- The incomplete test coverage gap (Appendix E) is honestly reported rather than
  overstated.
- All Success Criteria that cannot be quantified from code are correctly labelled
  Unresolved.
- The approvals table is blank, as required — no agent invented stakeholder names.

---

## 7. Recommended Changes to BRD Draft

| Priority | Change |
|---|---|
| High | Clarify UC-001 Alternative Flow — search vs. filter API call distinction |
| High | Add explicit Unresolved note about ETD updates on DEPARTED/CANCELLED flights |
| High | Add "reference application / demo" caveat to §1.1 or §2.2 |
| Medium | Add CDN version numbers to §2.6 |
| Medium | Add OQ-011 to Appendix C (empty cancellation note) |
| Medium | Refine NFR-015 scope of `escapeHtml()` |
| Low | Revise §4.2 from Unresolved to Confirmed (no HW interfaces) |
| Low | Note unused `findByOrigin()`/`findByDestination()` repository methods |
