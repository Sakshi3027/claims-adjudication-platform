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

Early — currently scaffolding the module structure and domain models.
Not deployed yet.

## Structure

```
rules-engine/   core adjudication logic, framework-agnostic
api/            Spring Boot REST layer, MongoDB repositories
bdd-tests/      Cucumber feature files + step definitions
audit/          append-only audit event logging
frontend/       Angular dashboard (analyst + auditor views)
```

More detail on setup and running it locally coming as each piece lands.
