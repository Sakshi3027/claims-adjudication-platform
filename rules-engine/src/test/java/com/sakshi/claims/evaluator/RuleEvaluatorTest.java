package com.sakshi.claims.evaluator;

import com.sakshi.claims.domain.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RuleEvaluatorTest {

    private final RuleEvaluator evaluator = new RuleEvaluator(
            List.of(new PriorAuthMissingRule(), new HighDollarReviewRule())
    );

    @Test
    void flagsClaimWhenPriorAuthMissing() {
        Claim claim = new Claim(
                "CLM-1001", "MBR-1", ClaimType.MEDICAL,
                List.of("E11.9"),
                List.of(new LineItem("99213", new BigDecimal("125.00"), true)),
                false // no prior auth on file
        );

        AdjudicationResult result = evaluator.evaluate(claim);

        assertEquals(AdjudicationOutcome.MANUAL_REVIEW, result.outcome());
        assertTrue(result.matchedRuleIds().contains("RULE-014"));
        assertTrue(result.reason().toLowerCase().contains("prior auth"));
    }

    @Test
    void approvesCleanClaimWithNoFlags() {
        Claim claim = new Claim(
                "CLM-1002", "MBR-2", ClaimType.MEDICAL,
                List.of("Z00.00"),
                List.of(new LineItem("99396", new BigDecimal("180.00"), false)),
                false
        );

        AdjudicationResult result = evaluator.evaluate(claim);

        assertEquals(AdjudicationOutcome.APPROVED, result.outcome());
    }

    @Test
    void sendsHighDollarClaimsToManualReviewRegardlessOfOtherRules() {
        Claim claim = new Claim(
                "CLM-1003", "MBR-3", ClaimType.MEDICAL,
                List.of("C50.911"),
                List.of(new LineItem("77067", new BigDecimal("6200.00"), false)),
                true // prior auth is on file, shouldn't matter here
        );

        AdjudicationResult result = evaluator.evaluate(claim);

        assertEquals(AdjudicationOutcome.MANUAL_REVIEW, result.outcome());
        assertTrue(result.matchedRuleIds().contains("RULE-022"));
    }
}
