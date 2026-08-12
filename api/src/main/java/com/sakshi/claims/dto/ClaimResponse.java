package com.sakshi.claims.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ClaimResponse(
        String id,
        String claimNumber,
        String memberId,
        String claimType,
        String status,
        BigDecimal totalAmount,
        Instant submittedAt,
        AdjudicationResultResponse adjudicationResult
) {
    public record AdjudicationResultResponse(
            String outcome,
            List<String> matchedRuleIds,
            String reason,
            Instant evaluatedAt
    ) {
    }
}
