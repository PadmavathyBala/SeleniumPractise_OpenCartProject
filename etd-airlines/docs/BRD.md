# Estimated Time of Departure (ETD) Management System Business Requirements Document
_Final Version — Proofread and presentation-polished for stakeholder delivery._

## Executive Summary
This BRD documents the currently evidenced business capability of an Estimated Time of Departure (ETD) management system for airline operations users. The system supports the management of flight departure records through REST APIs and a browser-based dashboard, including the ability to create, view, search, update, cancel, delete, mark flights as departed, and review operational statistics (`pom.xml`; `controller/FlightController.java`; `controller/StatisticsController.java`; `static/js/api.js`; `static/js/app.js`).

The business problem addressed is the need for a centralized way to maintain current ETD information, track delays, and monitor departure status across flights. The primary target users appear to be flight operations, dispatch, station, and monitoring users who need timely visibility into departure changes. The business rationale evidenced in the implementation is improved operational visibility, more consistent status handling, and faster access to current ETD information through auto-refreshing interfaces and API-based access.

The current implementation also includes periodic UI refresh and backend simulation behavior (`static/js/app.js`; `service/FlightSimulationService.java#simulateLiveUpdates`). This capability is documented as an environment-supporting feature whose production purpose and usage boundaries require business confirmation.

## Project Objectives
1. Enable authorized users to maintain flight ETD records in one place through create, view, search, update, cancel, delete, and mark-as-departed actions.
2. Apply a consistent ETD status model and rule-based status calculation across managed flights.
3. Support delay tracking through calculated delay minutes, a delay threshold, standardized delay reasons, and optional notes.
4. Provide operational visibility through summary statistics and dashboard views.
5. Provide timely access to current ETD information through REST endpoints and an auto-refreshing browser interface.
6. Support operational monitoring of the application through health, info, and metrics endpoints as technical operability features.

### Success Criteria Evidenced in the Implementation
- Users can perform the ETD lifecycle actions exposed by the API and UI.
- Current flight data can be refreshed automatically in the browser.
- Statistics can be retrieved from the system.

## Project Scope
### In Scope
- Flight record management for scheduled departures.
- Flight lookup and search by flight number, airline, origin, and destination.
- ETD updates with delay metadata and status recalculation.
- Flight cancellation and departure confirmation.
- Flight deletion.
- Operational statistics and dashboard reporting.
- Browser-based access to ETD data through REST-integrated frontend components.
- Automatic UI refresh for current data visibility.
- Technical operability endpoints for health, info, and metrics.
- Seeded flight data and periodic simulation updates currently present in the implementation.

### Out of Scope or Not Evidenced in Code
- Persistent database storage.
- Authentication and authorization controls.
- Confirmed business workflow for diverted flights.
- Budget, delivery timeline, named sponsor, SLAs, retention policy, and ROI.
- Audit trail requirements.
- Production deployment boundaries for simulation behavior.

### Scope Clarifications
- Health, info, and metrics endpoints are treated as technical operability features, not primary end-user business functions.
- Simulation behavior is treated as a currently delivered capability whose intended business use requires confirmation.

## Business Requirements
### Functional Requirements
1. The system shall allow users to create a flight departure record with the data elements evidenced in the implementation.
2. The system shall allow users to view flight departure records individually and in list form.
3. The system shall allow users to search for flights by flight number, airline, origin, and destination.
4. The system shall allow users to update ETD-related flight information.
5. The system shall calculate and store delay-related information, including delay minutes where supported by the implementation.
6. The system shall support delay classification using standardized delay reasons and optional notes.
7. The system shall apply a consistent status model and recalculate status based on ETD-related updates.
8. The system shall allow users to cancel a flight record.
9. The system shall allow users to mark a flight as departed.
10. The system shall allow users to delete a flight record.
11. The system shall return a not-found response if the record does not exist.
12. The system shall provide summary statistics for operational visibility.
13. The system shall provide browser-based access to ETD data through frontend components integrated with REST APIs.
14. The user interface shall refresh current data periodically to support near-real-time visibility.
15. The system shall expose health, info, and metrics endpoints for technical monitoring.
16. Health endpoint details are configured to always show, based on the current implementation evidence.
17. The current implementation includes seeded flight data and periodic simulated updates.

## Key Stakeholders
- Flight operations and dispatch users.
- Operations managers and analysts.
- Dashboard users and station staff.
- Technical support and platform operators.

## Project Constraints
- The current implementation evidence does not show persistent database storage.
- The current implementation evidence does not show authentication or authorization controls.
- The current implementation includes simulation behavior whose production usage boundaries are not yet confirmed.
- Technical operability features are present, but nonfunctional targets and governance expectations are not defined in the available material.

## Cost-Benefit Analysis
### Evidenced Qualitative Benefits
- Centralized management of ETD records.
- Faster visibility into ETD and delay changes.
- More consistent status handling.
- Improved operational monitoring through dashboard statistics and technical endpoints.
- Reduced reliance on manual tracking for current departure status.

### Items Requiring Business Confirmation
- Budget.
- Delivery timeline.
- Quantified cost savings.
- Revenue impact.
- ROI.
- Payback period.

## Assumptions and Open Questions
### Assumptions Based on Available Implementation Evidence
- This BRD documents only capabilities evidenced in the available implementation and supporting artifacts.
- Primary users appear to be flight operations, dispatch, station, and monitoring users.
- Technical monitoring endpoints are treated as operability features rather than primary end-user business functions.
- UI auto-refresh and backend simulation are documented as delivered capabilities; their intended production use requires business confirmation.

### Business Ownership and Governance
- Who is the business sponsor, product owner, and final approver for this capability?
- Who has approval authority for scope, requirements, and release decisions?
- Which team owns operational support for health, info, and metrics monitoring?

### Success Measures and Business Case
- What business KPIs, ROI targets, budget, and delivery timeline apply to this initiative?
- What measurable business targets define success, such as update timeliness, user adoption, data accuracy, or reduced manual tracking?
- What service levels, response-time expectations, and reporting frequencies are required?
- What implementation and operating costs should be included?
- What measurable business benefits are expected, and how will they be tracked?
- What ROI threshold or business-case approval criteria apply?

### Scope, Process, and Operational Use
- Should diverted flights be in business scope? If yes, what triggers, workflows, and users govern that status?
- Is the "Diverted" status part of the approved business process? If yes, what event, rule, or user action sets that status?
- Is the simulation capability intended for production operations, demo or training use only, or non-production test environments only?
- Should simulation be enabled only in demo or non-production contexts, or is it intended to support live operational scenarios?
- Are there environment-specific constraints for production versus demo or test usage?

### Roles, Permissions, and Controls
- What are the exact role and permission boundaries for create, update, cancel, mark-as-departed, and delete actions?
- Which user roles, if any, are permitted to delete flight records rather than retain them for traceability?
- Should cancellation, departure confirmation, and deletion require reason capture or user confirmation?
- What security expectations apply, including authentication, authorization, transport security, and access logging?
- What environment controls are required to enable or disable simulation behavior?

### Data, Validation, and Auditability
- What data fields are mandatory versus optional when creating or updating a flight?
- What validation rules apply to ETD changes, delay reasons, notes, and status transitions?
- Should the system preserve historical ETD changes, or only the latest state?
- What auditability is required for create, update, cancel, depart, and delete actions?
- What data retention and archival rules apply to flight records and status history?

### Reporting and Statistics
- How is on-time performance calculated?
- What population is used for statistics: all flights, active flights only, a date range, or another defined subset?
- Are cancelled flights included or excluded from on-time and delay calculations?
- Are diverted flights included or excluded from statistics?
- What are the exact definitions of each dashboard metric and reporting field?

### Compliance and External Constraints
- Are there regulatory, audit, airline-partner, or information-security constraints that must be applied?
- What uptime or availability target is required?
- What maximum acceptable latency is required for API responses and dashboard refreshes?
