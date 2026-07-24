# Step 6 — Review Findings (Senior BA)

Total findings: **7**

1. **Section 3.1 UC-001 Alternative Flow**
   - **Issue:** Listed as "TBD"; alternate behavior is known (no matching flights message).
   - **Recommended revision:** Replace with explicit alternate flow: when filters yield zero records, system displays no-match state and retains filter controls.

2. **Section 5.1 Performance**
   - **Issue:** Requirement "system should be fast" is non-testable and unlabeled.
   - **Recommended revision:** Reframe as unresolved measurable target (e.g., no stated SLA in source) and explicitly tag Unresolved rather than asserting vague quality.

3. **Section 7 Success Criteria**
   - **Issue:** "Reduce delay handling friction quickly" is vague and lacks confidence label precision.
   - **Recommended revision:** Mark this as Assumed with explicit note that quantitative targets are unavailable from code and require stakeholder definition.

4. **Appendix C Issues List completeness**
   - **Issue:** Appendix C does not consolidate all Assumed/Unresolved items surfaced across sections (security, persistence, export reporting, roles, audit, hardware assumptions).
   - **Recommended revision:** Expand Appendix C into a complete consolidated issues register with source section references.

5. **Requirement traceability readiness**
   - **Issue:** Draft references requirement IDs but does not consistently cite source evidence in feature-level requirement statements.
   - **Recommended revision:** Add concise citations to requirement bullets in Section 3 and key sections where Confirmed labels are used.

6. **Use-case schema completeness consistency**
   - **Issue:** Some use-case fields are abbreviated prose and do not clearly carry confidence labels per field (especially Includes/Priority/Frequency in places).
   - **Recommended revision:** Normalize every use-case field to explicit label format.

7. **Business value framing for delete action**
   - **Issue:** Delete function is described functionally but not framed with business controls/risk (irreversible action).
   - **Recommended revision:** Add business rule/notes requiring explicit operator confirmation and flag missing soft-delete/audit capability as unresolved risk.
