Feature: High dollar claims always route to manual review
  Regardless of what else is true about a claim, anything over the
  auto-approval dollar threshold gets a human's eyes on it. This is a
  blanket safety net sitting on top of the more specific rules.

  Scenario: High dollar claim is routed to manual review
    Given a claim for procedure code "77067" with amount "6200.00" that does not require prior authorization
    And prior authorization is on file
    When the claim is adjudicated
    Then the outcome should be "MANUAL_REVIEW"
    And the reason should mention "5000"

  Scenario: Claim under the threshold is not flagged for dollar amount
    Given a claim for procedure code "77067" with amount "800.00" that does not require prior authorization
    And no prior authorization is on file
    When the claim is adjudicated
    Then the outcome should be "APPROVED"
