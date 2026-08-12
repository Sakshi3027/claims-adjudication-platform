# ⚖️ Claims Adjudication & Audit Compliance Platform

> Enterprise claims adjudication engine — Spring Boot + MongoDB rules engine, Cucumber/Gherkin business-rule specs, and an Angular auditor dashboard with a full immutable audit trail.

**GitHub:** https://github.com/Sakshi3027/claims-adjudication-platform  
**Stack:** Java 21 · Spring Boot · MongoDB · Cucumber · Angular · RxJS · Docker

---

## What It Does

This is the internal tooling a health insurance payer would use to adjudicate claims automatically and stay auditable while doing it. A claim comes in, gets checked against a set of business rules (prior auth required? amount over the manual-review threshold?), and either gets approved, denied, or flagged for a human to look at. Every one of those decisions gets written to an append-only audit log, because in this domain you eventually have to answer to an auditor asking why a claim was decided a certain way six months ago.

The core idea driving the tech choices: the people who actually own the business rules — compliance analysts, not engineers — should be able to read (and eventually author) those rules directly, as Gherkin scenarios, rather than having them buried in Java conditionals.

---

## Architecture

```
Claim submitted (REST API)
        ↓
┌─────────────────────┐
│   RULES ENGINE       │  Framework-agnostic. Evaluates a claim against
│   (plain Java)        │  every configured rule, first-match wins.
└─────────┬─────────────┘
          ↓
┌─────────────────────┐
│   MONGODB             │  Claim persisted as a document — variable line
│   (claims collection) │  items, optional attachments, no rigid schema.
└─────────┬─────────────┘
          ↓
┌─────────────────────┐
│   AUDIT TRAIL          │  Every submission, rule match, flag, and
│   (append-only)         │  manual override written as a new event —
│                          │  nothing is ever edited or deleted.
└─────────┬─────────────┘
          ↓
┌─────────────────────┐
│   ANGULAR DASHBOARD    │  Analyst view (submit + see outcome) and
│   (RxJS)                │  Auditor view (review queue + audit history).
└─────────────────────┘
```

---

## Adjudication Rules

| Rule | Trigger | Outcome |
|------|---------|---------|
| `RULE-014` — Prior auth missing | Line item requires prior authorization and none is on file | Manual review |
| `RULE-022` — High dollar threshold | Total claim amount exceeds $5,000 | Manual review |

Each rule maps directly to a `.feature` file — the Gherkin scenario a compliance analyst reads *is* the spec the rule engine is tested against, not a separate description of it.

---

## Modules

| Module | Description |
|--------|-------------|
| `rules-engine/` | Claim domain model + adjudication rules. No Spring dependency — testable and reusable on its own. |
| `audit/` | Append-only `AuditEvent`/`AuditTrail` contracts, plus an in-memory implementation for tests. |
| `api/` | Spring Boot REST layer — submit, list, review queue, audit trail, manual override. MongoDB persistence via Spring Data. |
| `bdd-tests/` | Cucumber feature files + step definitions, running directly against the rules engine. |
| `frontend/` | Angular dashboard — claim submission (analyst view) and review queue + audit trail (auditor view). |

---

## Running Locally

**Full stack with Docker Compose:**

```bash
docker compose up --build
```

- Frontend: http://localhost:4200
- API: http://localhost:8080/api/claims

**Or run pieces individually:**

```bash
# Backend (needs Mongo on localhost:27017)
mvn -pl rules-engine,audit,api -am spring-boot:run -pl api

# Rule engine + BDD specs only, no Mongo needed
mvn -pl rules-engine,audit,bdd-tests -am test

# Frontend only (expects the API on localhost:8080)
cd frontend && npm install && npm start
```

**Submit a claim directly:**

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

That comes back flagged for manual review, with the reason pointing at the missing prior authorization — visible in `/api/claims/queue` and traceable end to end via `/api/claims/{id}/audit-trail`.

---

## Tech Stack

- **Spring Boot 3 / Java 21** — REST API, framework-agnostic rules engine module
- **MongoDB (Spring Data)** — claims and audit events as documents, no rigid relational schema forcing variable claim shapes
- **Cucumber / Gherkin** — business rules as executable, human-readable specs
- **Angular 17 (standalone components) + RxJS** — analyst and auditor dashboards
- **Docker / Docker Compose** — Mongo + API + frontend, one command to run the whole stack

---

## Status

Backend: 13/13 tests passing across all four Java modules. Frontend builds clean. Runs fully locally via Docker Compose — not yet deployed to a public URL.

---

## Relationship to ClaimStream & HealthClaim Copilot

This sits in the same claims-processing project family as [ClaimStream](https://github.com/Sakshi3027/claimstream) (streaming ingestion) and [HealthClaim Copilot](https://github.com/Sakshi3027/healthclaim-copilot) (RAG-based claims analysis). Where those two focus on ingesting and querying claims data at scale, this one focuses on the transactional side — deciding what happens to an individual claim, and proving why, after the fact.

---

Built by [Sakshi Chavan](https://github.com/Sakshi3027)  
AI Engineer | LangGraph • Databricks • RAG | MS Data Science | https://www.linkedin.com/in/sakshi-v-chavan | https://medium.com/@SakshiChavan
