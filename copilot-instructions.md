# Copilot custom instructions for this repo

This repository uses a 4-stage BRD (Business Requirements Document) pipeline,
implemented as Copilot custom agents in `.github/agents/`. All agents in this
pipeline share these rules:

- Follow the Asana BRD template structure exactly: Executive Summary, Project
  Objectives, Project Scope, Business Requirements, Key Stakeholders, Project
  Constraints, Cost-Benefit Analysis.
- Every claim must be traceable to source code or a cited Jira issue key.
  Never invent budgets, timelines, sponsor names, or ROI figures — state
  plainly when something is "not determinable from code" instead.
- Do not describe implementation details as business requirements unless the
  intended business behavior is actually supported by code, tests,
  configuration, or an explicit Jira issue.
- When a Jira change request's behavior is not yet present in the code, label
  it as PLANNED, not current behavior.

# Copilot custom instructions — BRD reverse-engineering pipeline (v2)

Append this to `.github/copilot-instructions.md` (or keep as a separate
section if you already have instructions from an earlier pipeline in this
repo — agent names in this set are prefixed distinctly and won't collide).

This repo uses an 8-step, 7-agent BRD reverse-engineering pipeline, based
on: Plan → Discover → Interpret → Extract → Draft → Review → Trace →
Validate. All agents in this pipeline share these rules.

## Confidence labeling (mandatory on every requirement)

Every requirement, rule, or workflow description produced anywhere in this
pipeline must be tagged with exactly one of:
- **Confirmed** — directly supported by code, UI, database schema, API
  contract, tests, or existing documentation. Cite the specific file,
  class, method, or document.
- **Inferred** — derived from multiple technical observations that
  together imply a behavior, but no single source states it directly.
  Explain the inference chain briefly.
- **Assumed** — a reasonable guess where evidence is thin; requires
  stakeholder confirmation before being treated as final.
- **Unresolved** — insufficient evidence exists to say anything reliable;
  state this plainly rather than guessing.

Never state a requirement without one of these four labels. Never upgrade
an Assumed or Unresolved item to Confirmed without new evidence.

## BRD document structure (used by the Authoring Agent)

1. Document purpose
2. Executive summary
3. Business background
4. Business objectives
5. Scope
6. Stakeholders
7. User roles
8. Current-state overview
9. Business processes
10. Business requirements (BR-###)
11. Functional requirements (FR-###)
12. Business rules (BRULE-###)
13. Data requirements
14. Integration requirements (INT-###)
15. Non-functional requirements (NFR-###)
16. Assumptions
17. Constraints
18. Dependencies
19. Risks
20. Out-of-scope items
21. Open questions
22. Acceptance criteria
23. Traceability matrix

## Execution log and intermediate drafts (mandatory — required for eval)

The orchestrator creates one run folder per pipeline execution at:
`docs/brd_runs/<run-id>/` where `<run-id>` is a UTC timestamp like
`2026-07-15T14-30-00Z`.

Every agent in this pipeline MUST, using its file-write tool:
1. On starting, APPEND a row to `docs/brd_runs/<run-id>/00_execution_log.md`:
   `| <UTC timestamp> | <agent-name> | <model> | STARTED | — |`
2. On finishing, save its full output to its own numbered file in the same
   folder (see file names in each agent's own instructions below), then
   APPEND a completion row:
   `| <UTC timestamp> | <agent-name> | <model> | COMPLETED | output ~<N> words; <one-line summary> |`
3. If an agent cannot complete its task (missing tool access, no data
   found, etc.), it still APPENDS a row with status `FAILED` or
   `PARTIAL` and a one-line reason — never silently skip logging.

The log file is append-only. No agent overwrites earlier rows. This log,
plus every numbered draft file, is the eval artifact for this pipeline —
treat writing it as part of the task, not optional bookkeeping.
