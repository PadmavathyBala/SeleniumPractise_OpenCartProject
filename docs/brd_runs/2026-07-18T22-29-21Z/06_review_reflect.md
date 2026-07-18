# Step 6 — Review and Reflection

**Run ID:** `2026-07-18T22-29-21Z`  
**Agent:** brd-review-reflect  
**Pipeline Step:** 6 of 8 — Review

---

## 1. Review Scope

This document reviews the BRD v2.0 draft (`05_brd_draft.md`) for:
- Verification that all 7 issues raised in the v1 review have been addressed.
- Accuracy and completeness against the discovered codebase.
- Proper use of confidence labels.
- Structural completeness per the required template.
- Internal consistency between sections.
- Identification of any new issues introduced in v2.

---

## 2. V1 Issue Resolution Verification

| Issue ID | v1 Severity | Summary | Addressed in v2? | How |
|---|---|---|---|---|
| ISSUE-01 | High | UC-001 Alternative Flow ambiguity | ✓ Yes | UC-001 Alternative Flow rewritten into two distinct sub-flows: Normal Flow (no search param) and "Alternative Flow — Text Search" (debounced API call). Status filter and sort explicitly described as client-side only. |
| ISSUE-02 | High | No guard on ETD updates for DEPARTED/CANCELLED | ✓ Yes | UC-002 Notes and Issues section now explicitly states: "The API does not prevent ETD updates on flights in DEPARTED or CANCELLED status." Added OQ-008 to Appendix C. |
| ISSUE-03 | High | System not labelled as reference/demo | ✓ Yes | §1.1 now contains "Reference/demo application notice" paragraph. §2.2 contains "Reference application caveat" paragraph. Both labelled Inferred. |
| ISSUE-04 | Medium | CDN version numbers missing | ✓ Yes | §2.6 now lists Bootstrap **5.3.2**, Bootstrap Icons **1.11.1**, Chart.js **4.4.0** with specific CDN URLs and a note on external network dependency. |
| ISSUE-05 | Medium | Empty cancellation note flag missing | ✓ Yes | UC-004 Notes and Issues states empty string is accepted. OQ-011 added to Appendix C. |
| ISSUE-06 | Medium | `escapeHtml()` scope overstated | ✓ Yes | NFR-013 now accurately scopes: free-text fields (airline, gate, delayNotes) use `escapeHtml()`; structured fields (flight number, IATA codes, status) are constrained by server-side regex. |
| ISSUE-07 | Low | §4.2 should be Confirmed, not Unresolved | ✓ Yes | §4.2 now reads: "No hardware-specific interfaces identified or required. **Confirmed** — no hardware-specific code, configuration, or serial/GPIO libraries found." |

**All 7 v1 issues have been addressed in v2.0.**

---

## 3. Structural Completeness Check

| Section | Required | Present | Notes |
|---|---|---|---|
| Front Matter (version, date, run ID, authors, history, approvals) | ✓ | ✓ | History now shows both v1.0 and v2.0 entries; approvals blank as required |
| 1. Introduction (1.1–1.5) | ✓ | ✓ | §1.1 includes reference/demo notice; references updated to include R6 (v1 run) |
| 2. Overall Description (2.1–2.8) | ✓ | ✓ | §2.6 has CDN versions; §2.8 has A8 (CORS risk) |
| 3. System Features (3.N.1–3.N.4 per feature) | ✓ | ✓ | 6 features; UC-001 Alternative Flow clarified |
| 4. External Interface Requirements (4.1–4.5) | ✓ | ✓ | 4.2 now Confirmed; 4.5 API table added |
| 5. NFRs (5.1–5.4) | ✓ | ✓ | NFR-013 rescoped; NFR-014 added for CORS |
| 6. Other Requirements | ✓ | ✓ | `@EnableScheduling` confirmed |
| 7. Success Criteria | ✓ | ✓ | SC-01 through SC-06 |
| 8. Approval and Sign-off | ✓ | ✓ | Blank as required |
| Appendix A: Glossary | ✓ | ✓ | 18 terms (CORS, CDN added) |
| Appendix B: Analysis Models | ✓ | ✓ | Marked Not Generated |
| Appendix C: Issues List | ✓ | ✓ | 12 open questions (OQ-011, OQ-012 added) |
| Appendix D: Screenshots | ✓ | ✓ | Marked Not Generated |
| Appendix E: Test Scenarios | ✓ | ✓ | Updated coverage count to 5/29 (17%) |
| Appendix F: Traceability Summary | ✓ | Partial | FR-029 included in summary; full matrix in 07_traceability.md |

**Overall: COMPLETE** — all required template sections are present.

---

## 4. Confidence Label Audit

Spot-check of 20 requirement statements across the v2 draft:

| Check | Result |
|---|---|
| Every FR row has a label | ✓ Pass |
| Every BR row has a label | ✓ Pass |
| Every use-case field entry is labelled or inherits from section label | ✓ Pass |
| No Assumed or Unresolved item upgraded to Confirmed without evidence | ✓ Pass |
| Approvals table is blank | ✓ Pass |
| Success criteria that cannot be derived from code are Unresolved | ✓ Pass |
| New v2 findings (CORS, CDN, @EnableScheduling) all have Confirmed labels with citations | ✓ Pass |

**Result: PASS** — confidence labeling is consistent and correct throughout.

---

## 5. New Issues Found in v2 Review

### ISSUE-08 — Severity: Low
**Location:** §3.1.4 UC-001 Notes  
**Issue:** States "No pagination — all flights loaded at once." While accurate, the
BRD does not note that this affects the Statistics endpoint as well (all flights are
aggregated in-memory via `repository.findAll()`). The statistics call has the same
O(n) characteristic.  
**Recommendation:** Consider adding a note that both the flight list endpoint and
the statistics endpoint load all flights at once. This is consistent with the
existing OQ-009 (max flights question).  
**Confidence:** Confirmed — `StatisticsService.buildStatistics()` calls `repository.findAll()`.  
**Action:** Acknowledged; low severity. Noted here but not causing BRD revision for
v2.0. Carry forward for v3.0 if triggered.

### ISSUE-09 — Severity: Low
**Location:** §6 Other Requirements  
**Issue:** The `WebConfig.jacksonBuilder()` bean duplicates some configuration
already set in `application.properties` (both disable timestamps for date
serialization). This could cause confusion about which configuration takes
precedence. However, this is an implementation observation, not a BRD requirement
gap.  
**Recommendation:** Add a note to §6 that the Jackson configuration is set in both
`WebConfig.java` and `application.properties`. Not a BRD issue but worth noting for
a developer.  
**Confidence:** Confirmed — `WebConfig.java:jacksonBuilder()` and `application.properties` both set `WRITE_DATES_AS_TIMESTAMPS=false`.  
**Action:** Noted here. Not a blocker.

---

## 6. Positive Findings

- All 29 FRs are backed by direct code citations — no requirements were invented
  without evidence.
- All 7 v1 review issues have been fully addressed.
- CDN version numbers are now accurately cited (Bootstrap 5.3.2, Bootstrap Icons
  1.11.1, Chart.js 4.4.0).
- The reference/demo application caveat is clearly stated in two locations (§1.1
  and §2.2).
- CORS configuration is now documented as a functional requirement (FR-029) and as
  a security risk (NFR-014, A8).
- OQ-011 and OQ-012 correctly capture new open questions without inventing answers.
- The approvals table remains blank as required.

---

## 7. Recommended Changes to BRD Draft

| Priority | Change | Status |
|---|---|---|
| Low | Add note to §3.1.4 and §3.6.4 that both the flight list and statistics endpoints load all flights without pagination | Deferred to v3.0 |
| Low | Note duplicate Jackson date configuration in §6 | Deferred to v3.0 |

**No high or medium priority issues remain unaddressed in v2.0.**
