Feature: Prior authorization enforcement
  Compliance analysts define which procedures require prior authorization.
  A claim billing one of those procedures without an authorization on file
  should never be silently paid - it needs a human to look at it.

  Scenario: Claim sent to manual review when prior auth is missing
    Given a claim for procedure code "99213" with amount "125.00" that requires prior authorization
    And no prior authorization is on file
    When the claim is adjudicated
    Then the outcome should be "MANUAL_REVIEW"
    And the reason should mention "prior authorization"

  Scenario: Claim is not flagged for prior auth when authorization is on file
    Given a claim for procedure code "99213" with amount "125.00" that requires prior authorization
    And prior authorization is on file
    When the claim is adjudicated
    Then the outcome should be "APPROVED"

  Scenario: Claim approved when nothing requires prior authorization
    Given a claim for procedure code "99396" with amount "180.00" that does not require prior authorization
    And no prior authorization is on file
    When the claim is adjudicated
    Then the outcome should be "APPROVED"
