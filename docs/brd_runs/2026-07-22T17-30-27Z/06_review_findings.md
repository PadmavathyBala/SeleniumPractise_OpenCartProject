# BRD Review Findings

**Run ID:** 2026-07-22T17-30-27Z  
**Reviewed document:** `05_authoring_draft.md`  
**Reviewer:** brd-review-reflect

## Overall Assessment
The draft is well-structured and generally follows the required template, with good evidence tagging and no invented approval names or fabricated screenshot/diagram descriptions. However, it is **not yet sign-off ready** because several core business-control topics remain undefined and some current-state observations are written as if they were approved requirements.

## Severity Summary
| Severity | Count |
|---|---:|
| High | 3 |
| Medium | 4 |
| Low | 2 |
| **Total** | **9** |

## High Severity Findings
| ID | Affected Section / IDs | Issue | Recommended Revision |
|---|---|---|---|
| H-01 | §2.5; UC-001 to UC-009 actors; SEC-001; SEC-002; OR-004; SC-003; OQ-001 | **Critical role/permission model missing.** The draft repeatedly notes that any browser user can perform all write actions, but it never defines the intended business roles, approval boundaries, or action-level permissions for create/update/cancel/depart/delete. For an operations system, this is a major completeness and control gap. | Add a stakeholder-approved role matrix covering each user class, permitted actions, approval needs, and any segregation-of-duties rules. Update all use cases so actors and exceptions reflect that approved model rather than inferred titles only. |
| H-02 | §2.7 C-001/C-002; §2.8 A-005/A-006/A-008; §3.4-§3.9 notes/issues; NFR-007; AUD-001; AUD-002; OR-001; OR-003; SC-004 | **Operational record governance is unresolved.** Persistence, recovery, retention, delete control, and audit expectations are acknowledged as gaps, but the BRD still reads partly like an operational target-state document. Without a defined source-of-record policy, the business cannot assess acceptability of ETD changes, cancellations, departures, or deletes. | Either (a) explicitly scope the product as demo/training only throughout the BRD, or (b) add stakeholder-defined requirements for persistence, audit trail, retention, recovery, and delete governance before treating the BRD as approval-ready. |
| H-03 | §1.2.2 “Demo / simulation mode”; §2.1; BO-006; §3.8; BR-008; NFR-003; UC-008; SC-006; OQ-002; OQ-012 | **Simulation behavior is overstated as conditional.** The draft says “when operated in demo or training mode,” but the implementation evidence shows seeding and simulation always run on startup and every 30 seconds (`FlightSimulationService.java:35-129`). No mode switch or environment toggle is cited. This is an unsupported claim about conditional behavior. | Revise current-state wording to state that the **current implementation always seeds and simulates**. If the desired business intent is environment-specific simulation, add that as a future requirement instead of describing it as existing behavior. |

## Medium Severity Findings
| ID | Affected Section / IDs | Issue | Recommended Revision |
|---|---|---|---|
| M-01 | DR-003; §3.6; OR-002; OQ-006 | **`DIVERTED` is listed as a supported status, but no business workflow exists for it.** The status is present in the enum/UI styling, yet the BRD does not define who sets it, under what conditions, what downstream effects follow, or how it appears operationally. This is a completeness and consistency gap in the state model. | Add a dedicated diverted-flight feature/use case/business rules section, or explicitly remove/defer `DIVERTED` from the supported-state list until stakeholders confirm the workflow. |
| M-02 | BR-009; FR-014; FR-016; RPT-003; NFR-011; OR-001 to OR-005; SC-003 to SC-005 | **Current-state observations and open questions are mixed into the normative requirement set.** Examples include “No historical exports… were discovered” (RPT-003) and unresolved policy items written as “shall define.” This weakens testability and makes the traceability matrix harder to interpret because not every “requirement” is actually a business requirement. | Separate the document into: (1) confirmed current-state observations, (2) approved business requirements, and (3) open questions/decision items. Move negative observations to notes/issues appendices, and keep only testable target-state requirements in the requirement catalog. |
| M-03 | RPT-003; NFR-011; Appendix E coverage-gap bullets; other “absent/not found” claims | **Some Confirmed or quasi-confirmed statements lack strong primary-source citations.** Several absence-based findings say “absent from inspected code and README” or “no SLA artifact found” without naming the searched files/sections precisely. This falls short of the pipeline’s traceability standard for Confirmed statements. | Where absence is important, cite the exact inspected artifact set (for example, README section plus controller/service/UI files searched) or downgrade the statement to Inferred/Unresolved if the search boundary cannot be shown precisely. |
| M-04 | §7 SC-001 to SC-006 | **Success Criteria are not yet measurable or approvable.** The section correctly avoids fabricating numbers, but every criterion remains Assumed or Unresolved and none are objectively testable business outcomes. That makes the BRD incomplete for approval use. | Keep the current caveat, but add a clear “pending stakeholder definition” note and replace these with measurable acceptance criteria once business owners define freshness, security, reporting, durability, and adoption targets. |

## Low Severity Findings
| ID | Affected Section / IDs | Issue | Recommended Revision |
|---|---|---|---|
| L-01 | §2.5; UC-001 to UC-009 actor fields | **Some inferred actor names may read as invented business titles.** Terms such as “operations controller / gate operations agent” and “operations supervisor / analyst” are plausible, but the draft does not cite any business artifact that uses those exact role names. | Rename them to generic placeholders (for example, “flight operations user” and “supervisory user”) or explicitly state that the titles are illustrative pending stakeholder validation. |
| L-02 | Appendix B; Appendix D; Appendix F | **The template is followed, but the draft remains incomplete because key appendices are placeholders/not generated.** This is acceptable for the pipeline stage, yet the document should not be mistaken for a final BRD package. | Mark the draft status more explicitly as “review draft / not final” until the traceability matrix is completed and any required analysis models or screenshots are either produced or formally waived. |

## Fabrication / Unsupported-Claim Check
- **Approvals table:** No invented stakeholder names, titles, signatures, or dates found. This is correct.
- **Appendix B / D:** No fabricated diagram or screenshot descriptions found. The draft properly marks them as not generated.
- **Unsupported claims found:** The main unsupported claim is the conditional “demo/training mode” wording in H-03.
- **Missing/weak citations found:** See M-03 for absence-based statements that need tighter traceability.

## Final Recommendation
Accept the draft as a **strong review draft**, but do **not** treat it as sign-off ready until the high-severity governance/scope issues and medium-severity requirement-structure issues are resolved.
