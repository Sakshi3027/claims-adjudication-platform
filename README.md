# Claims Adjudication & Audit Compliance Platform

A claims adjudication system built around a fairly simple idea: the people who
actually understand the business rules (compliance analysts, not engineers)
should be able to write those rules in plain language, and have the system
adjudicate claims against them automatically — with a full audit trail so
every decision can be traced back to exactly why it was made.

This is loosely modeled on how claims processing works at health insurance
payers: a claim comes in, gets checked against a set of rules (prior auth
required? diagnosis covered? amount over a manual-review threshold?), and
either gets auto-approved, auto-denied, or flagged for a human to look at.
Every one of those decisions gets logged immutably, because in this domain
you eventually have to answer to an auditor asking "why was this claim
approved six months ago."

## Why this stack

- **Spring Boot + MongoDB** — claims aren't uniform records. Different claim
  types (medical, pharmacy, dental) have different shapes, variable numbers
  of line items, optional attachments. Trying to force that into a rigid
  relational schema gets ugly fast. Mongo's document model fits the data as
  it actually looks.
- **Cucumber/Gherkin** — instead of just using it for tests, the rule
  definitions themselves are written as Gherkin scenarios. A compliance
  analyst can read (and in theory, help author) a `.feature` file without
  knowing Java. The same file drives the automated test suite.
- **Angular** — the frontend is really two internal dashboards (one for
  analysts submitting/reviewing claims, one for auditors working a review
  queue), which is a pretty standard enterprise-admin-tool shape that
  Angular's service/DI structure fits well.

## Status

Backend (rules engine, MongoDB persistence, REST API, audit trail, Cucumber
specs) is built and passing 13 tests across four modules. Angular frontend
is built with two working views. Not deployed to a public URL yet - runs
locally via Docker Compose for now.

## Structure

```
rules-engine/   core adjudication logic, framework-agnostic
api/            Spring Boot REST layer, MongoDB repositories
bdd-tests/      Cucumber feature files + step definitions
audit/          append-only audit event logging
frontend/       Angular dashboard (analyst + auditor views)
```

## Running it locally

The whole stack (MongoDB + API + frontend) via Docker Compose:

```bash
docker compose up --build
```

Then:
- Frontend: http://localhost:4200
- API directly: http://localhost:8080/api/claims

Or run pieces individually:

```bash
# Backend only (needs a Mongo instance on localhost:27017)
mvn -pl rules-engine,audit,api -am spring-boot:run -pl api

# Just the rule engine + BDD specs, no Mongo needed
mvn -pl rules-engine,audit,bdd-tests -am test

# Frontend only (expects the API on localhost:8080)
cd frontend && npm install && npm start
```

## Example: submitting a claim

```bash
curl -X POST http://localhost:8080/api/claims \
  -H "Content-Type: application/json" \
  -d '{
    "claimNumber": "CLM-2026-00841",
    "memberId": "MBR-55210",
    "claimType": "MEDICAL",
    "diagnosisCodes": ["E11.9"],
    "lineItems": [{"procedureCode": "99213", "amount": 125.00, "priorAuthRequired": true}],
    "priorAuthOnFile": false
  }'
```

That should come back flagged for manual review, with the reason pointing
at the missing prior authorization - and you'll be able to see it show up
in `/api/claims/queue` and pull its full audit trail from
`/api/claims/{id}/audit-trail`.
