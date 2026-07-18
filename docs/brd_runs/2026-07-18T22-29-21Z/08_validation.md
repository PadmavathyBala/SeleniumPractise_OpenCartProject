# Step 8 — Validation Report

**Run ID:** `2026-07-18T22-29-21Z`  
**Agent:** brd-workflow-orchestrator (validation pass)  
**Pipeline Step:** 8 of 8 — Validate

---

## 1. Pipeline Completion Check

| Step | File | Status | Word Count (approx.) |
|---|---|---|---|
| 1 — Plan | `01_planning.md` | ✓ COMPLETE | ~460 |
| 2 — Discover | `02_discovery.md` | ✓ COMPLETE | ~1 200 |
| 3 — Interpret | `03_business_process.md` | ✓ COMPLETE | ~950 |
| 4 — Extract | `04_requirements_extraction.md` | ✓ COMPLETE | ~1 500 |
| 5 — Draft BRD | `05_brd_draft.md` | ✓ COMPLETE | ~6 200 |
| 6 — Review | `06_review_reflect.md` | ✓ COMPLETE | ~700 |
| 7 — Trace | `07_traceability.md` | ✓ COMPLETE | ~650 |
| 8 — Validate | `08_validation.md` | ✓ COMPLETE | ~420 |
| Execution Log | `00_execution_log.md` | ✓ PRESENT | — |

**All 8 pipeline outputs are present.**

---

## 2. V1 Issue Resolution Confirmation

All 7 issues identified in `docs/brd_runs/2026-07-18T21-08-41Z/06_review_reflect.md`
are confirmed resolved in this v2 run:

| Issue ID | Severity | Summary | Resolution Status |
|---|---|---|---|
| ISSUE-01 | High | UC-001 Alternative Flow ambiguity | ✓ RESOLVED — UC-001 Normal Flow and Text Search Alternative Flow now clearly separated |
| ISSUE-02 | High | No guard on ETD for DEPARTED/CANCELLED | ✓ RESOLVED — OQ-007/OQ-008 documented; UC-002 Notes explicitly calls out the missing guard |
| ISSUE-03 | High | System not labelled as reference/demo | ✓ RESOLVED — §1.1 and §2.2 both contain reference/demo caveat paragraphs |
| ISSUE-04 | Medium | CDN version numbers missing | ✓ RESOLVED — §2.6 now lists Bootstrap 5.3.2, Bootstrap Icons 1.11.1, Chart.js 4.4.0 |
| ISSUE-05 | Medium | Empty cancellation note flag | ✓ RESOLVED — UC-004 Notes and OQ-011 added |
| ISSUE-06 | Medium | `escapeHtml()` scope overstated | ✓ RESOLVED — NFR-013 accurately scopes free-text vs. structured fields |
| ISSUE-07 | Low | §4.2 Unresolved vs. Confirmed | ✓ RESOLVED — §4.2 changed to Confirmed positive statement |

---

## 3. Confidence Label Validation

| Check | Result |
|---|---|
| Every requirement carries exactly one label (Confirmed/Inferred/Assumed/Unresolved) | ✓ PASS |
| No Assumed/Unresolved items promoted to Confirmed without new evidence | ✓ PASS |
| All Confirmed items have a file/method citation | ✓ PASS |
| All Inferred items have an inference chain explanation | ✓ PASS |
| Approvals tables are blank (no fabricated names or signatures) | ✓ PASS |
| Success criteria without quantitative targets marked Unresolved | ✓ PASS |
| New v2 findings (CORS, CDN versions, @EnableScheduling) have Confirmed labels with citations | ✓ PASS |

---

## 4. BRD Template Compliance

| Template Element | Required | Present |
|---|---|---|
| Document version and date | ✓ | ✓ |
| Run ID as "Author" | ✓ | ✓ |
| Document history table (both v1 and v2 entries) | ✓ | ✓ |
| Blank approvals table | ✓ | ✓ |
| §1 Introduction (1.1–1.5) | ✓ | ✓ |
| Confidence labels defined in §1.2 | ✓ | ✓ |
| §2 Overall Description (2.1–2.8) | ✓ | ✓ |
| §3 System Features with use-case schema per feature | ✓ | ✓ |
| Use case schema: ID, Name, Actors, Description, Trigger, Pre/Postconditions, Flows, Exceptions, Includes, Priority, Frequency, BRs, Special Req, Assumptions, Notes | ✓ | ✓ |
| §4 External Interface Requirements (4.1–4.5) | ✓ | ✓ |
| §5 NFRs (5.1–5.4) | ✓ | ✓ |
| §6 Other Requirements | ✓ | ✓ |
| §7 Success Criteria | ✓ | ✓ |
| §8 Approval and Sign-off | ✓ | ✓ (blank) |
| Appendix A: Glossary | ✓ | ✓ (18 terms) |
| Appendix B: Analysis Models | ✓ | ✓ (marked Not Generated) |
| Appendix C: Issues List | ✓ | ✓ (12 OQs: OQ-001–OQ-012) |
| Appendix D: Screenshots | ✓ | ✓ (marked Not Generated) |
| Appendix E: Test Scenarios | ✓ | ✓ (5/29 FRs covered) |
| Appendix F: Traceability Matrix | ✓ | ✓ (in 07_traceability.md) |

**Template compliance: FULL**

---

## 5. Factual Accuracy Spot-Check

Five claims from the v2 BRD draft verified against the codebase:

| Claim | File Checked | Verdict |
|---|---|---|
| "Bootstrap 5.3.2 from CDN" | `static/index.html` line 7 — `bootstrap@5.3.2` | ✓ Accurate |
| "CORS allows all origins on /api/**" | `config/WebConfig.java` lines 19–22 — `allowedOrigins("*")`, path `/api/**` | ✓ Accurate |
| "@EnableScheduling on EtdApplication" | `EtdApplication.java` line 12 — `@EnableScheduling` | ✓ Accurate |
| "findByStatus() is not called by any service" | Verified against `FlightService.java`, `StatisticsService.java`, `FlightSimulationService.java` — none call `findByStatus()` | ✓ Accurate |
| "ETD validation: not >5 min in past" | `service/FlightService.java` line 68 — `.isBefore(LocalDateTime.now().minusMinutes(5))` | ✓ Accurate |

---

## 6. Open Issues Summary

| Category | Count | Status |
|---|---|---|
| v1 review issues (ISSUE-01 to ISSUE-07) | 7 | ✓ All resolved in v2 |
| v2 new review issues (ISSUE-08, ISSUE-09) | 2 | Open — low severity; deferred to v3.0 |
| Open questions (OQ-001 to OQ-012) | 12 | Open — require human stakeholder input |

---

## 7. Final Pipeline Verdict

| Criterion | Result |
|---|---|
| All 8 pipeline steps completed | ✓ PASS |
| Execution log is present and append-only | ✓ PASS |
| All intermediate files written | ✓ PASS |
| BRD template fully complied with | ✓ PASS |
| Confidence labels correct throughout | ✓ PASS |
| No hallucinated requirements | ✓ PASS |
| No fabricated stakeholder sign-offs | ✓ PASS |
| All 7 v1 review issues resolved | ✓ PASS |
| v2 review issues documented (2 low-severity items) | ✓ PASS |
| Traceability matrix covers all 29 FRs | ✓ PASS |

**OVERALL VERDICT: PIPELINE COMPLETE — BRD v2.0 READY FOR HUMAN REVIEW**

The reverse-engineered BRD v2.0 for `etd-airlines_1` is complete and ready for
stakeholder review. All requirements are evidence-backed. All 7 issues from the
version 1 review have been resolved. Two low-severity observations and 12 open
questions remain for human follow-up. No high or medium priority issues are
outstanding.
