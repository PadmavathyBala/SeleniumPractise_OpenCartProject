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
