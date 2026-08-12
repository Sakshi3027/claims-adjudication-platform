package com.sakshi.claims.evaluator;

import com.sakshi.claims.domain.AdjudicationOutcome;
import com.sakshi.claims.domain.Claim;

import java.math.BigDecimal;

/**
 * Anything over the threshold gets a human's eyes on it regardless of what
 * else is true about the claim. This is a pretty common real-world pattern -
 * dollar-amount thresholds as a blanket safety net on top of the more
 * specific rules.
 */
public class HighDollarReviewRule implements AdjudicationRule {

    private static final BigDecimal THRESHOLD = new BigDecimal("5000");

    @Override
    public String ruleId() {
        return "RULE-022";
    }

    @Override
    public boolean applies(Claim claim) {
        return claim.totalAmount().compareTo(THRESHOLD) > 0;
    }

    @Override
    public AdjudicationOutcome outcome() {
        return AdjudicationOutcome.MANUAL_REVIEW;
    }

    @Override
    public String reason(Claim claim) {
        return "Total claim amount of " + claim.totalAmount() + " exceeds the $" + THRESHOLD
                + " auto-approval threshold.";
    }
}
