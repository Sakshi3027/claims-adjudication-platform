package com.sakshi.claims.domain;

import java.time.Instant;
import java.util.List;

/**
 * The result of running a claim through the rule evaluator. Keeping
 * matchedRuleIds and a human-readable reason on here (rather than just
 * a yes/no outcome) is what makes the audit trail actually useful -
 * an auditor six months from now needs to know *why*, not just *what*.
 */
public record AdjudicationResult(
        AdjudicationOutcome outcome,
        List<String> matchedRuleIds,
        String reason,
        Instant evaluatedAt
) {
    public static AdjudicationResult of(AdjudicationOutcome outcome, List<String> matchedRuleIds, String reason) {
        return new AdjudicationResult(outcome, matchedRuleIds, reason, Instant.now());
    }
}
