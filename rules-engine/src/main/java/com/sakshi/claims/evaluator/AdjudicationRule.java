package com.sakshi.claims.evaluator;

import com.sakshi.claims.domain.AdjudicationOutcome;
import com.sakshi.claims.domain.Claim;

/**
 * A single adjudication rule. Each rule is deliberately narrow - one
 * condition, one outcome - so they can be composed and so a compliance
 * analyst reading the corresponding Gherkin scenario can map it back to
 * exactly one implementation without wading through a giant if/else block.
 */
public interface AdjudicationRule {

    String ruleId();

    boolean applies(Claim claim);

    AdjudicationOutcome outcome();

    String reason(Claim claim);
}
