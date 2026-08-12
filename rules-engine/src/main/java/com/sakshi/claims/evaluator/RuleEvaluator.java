package com.sakshi.claims.evaluator;

import com.sakshi.claims.domain.AdjudicationOutcome;
import com.sakshi.claims.domain.AdjudicationResult;
import com.sakshi.claims.domain.Claim;

import java.util.ArrayList;
import java.util.List;

/**
 * Runs a claim through every configured rule and produces a single result.
 *
 * Current approach is deliberately simple: any rule that applies and
 * resolves to something other than APPROVED wins and short-circuits
 * evaluation, on the theory that "needs manual review" or "denied" should
 * never get silently overridden by a later rule finding it clean. If we
 * ever need weighted/priority-based rule resolution instead of first-match,
 * this is the place it'd go - but starting simple until there's a real
 * case for more.
 */
public class RuleEvaluator {

    private final List<AdjudicationRule> rules;

    public RuleEvaluator(List<AdjudicationRule> rules) {
        this.rules = rules;
    }

    public AdjudicationResult evaluate(Claim claim) {
        List<String> matched = new ArrayList<>();

        for (AdjudicationRule rule : rules) {
            if (rule.applies(claim)) {
                matched.add(rule.ruleId());
                if (rule.outcome() != AdjudicationOutcome.APPROVED) {
                    return AdjudicationResult.of(rule.outcome(), matched, rule.reason(claim));
                }
            }
        }

        if (!matched.isEmpty()) {
            return AdjudicationResult.of(AdjudicationOutcome.APPROVED, matched,
                    "All matched rules resolved to approval.");
        }

        return AdjudicationResult.of(AdjudicationOutcome.APPROVED, matched,
                "No rules matched; claim approved by default.");
    }
}
