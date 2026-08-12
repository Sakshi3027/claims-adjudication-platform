package com.sakshi.claims.evaluator;

import com.sakshi.claims.domain.AdjudicationOutcome;
import com.sakshi.claims.domain.Claim;

/**
 * Corresponds to the "Claim flagged when prior auth is missing" scenario
 * in prior_authorization.feature.
 */
public class PriorAuthMissingRule implements AdjudicationRule {

    @Override
    public String ruleId() {
        return "RULE-014";
    }

    @Override
    public boolean applies(Claim claim) {
        return claim.requiresPriorAuth() && !claim.isPriorAuthOnFile();
    }

    @Override
    public AdjudicationOutcome outcome() {
        return AdjudicationOutcome.MANUAL_REVIEW;
    }

    @Override
    public String reason(Claim claim) {
        return "Prior authorization is required for at least one line item on this claim, "
                + "but none is on file.";
    }
}
