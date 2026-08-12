package com.sakshi.claims.mapper;

import com.sakshi.claims.document.ClaimDocument;
import com.sakshi.claims.domain.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClaimMapperTest {

    private final ClaimMapper mapper = new ClaimMapper();

    @Test
    void roundTripsClaimThroughDocumentForm() {
        Claim claim = new Claim(
                "CLM-9001", "MBR-9", ClaimType.MEDICAL,
                List.of("E11.9"),
                List.of(new LineItem("99213", new BigDecimal("125.00"), true)),
                false
        );

        ClaimDocument doc = mapper.toDocument(claim);
        assertEquals("CLM-9001", doc.getClaimNumber());
        assertEquals("MEDICAL", doc.getClaimType());
        assertEquals(1, doc.getLineItems().size());

        Claim roundTripped = mapper.toDomain(doc);
        assertEquals(claim.getClaimNumber(), roundTripped.getClaimNumber());
        assertEquals(claim.getClaimType(), roundTripped.getClaimType());
        assertEquals(0, claim.totalAmount().compareTo(roundTripped.totalAmount()));
    }

    @Test
    void appliesManualReviewResultAsFlaggedStatus() {
        Claim claim = new Claim("CLM-9002", "MBR-9", ClaimType.MEDICAL,
                List.of(), List.of(), false);
        ClaimDocument doc = mapper.toDocument(claim);

        AdjudicationResult result = AdjudicationResult.of(
                AdjudicationOutcome.MANUAL_REVIEW, List.of("RULE-014"), "Prior auth missing");
        mapper.applyResult(doc, result);

        assertEquals("FLAGGED", doc.getStatus());
        assertEquals("MANUAL_REVIEW", doc.getAdjudicationResult().getOutcome());
    }

    @Test
    void appliesApprovedResultAsAdjudicatedStatus() {
        Claim claim = new Claim("CLM-9003", "MBR-9", ClaimType.MEDICAL,
                List.of(), List.of(), false);
        ClaimDocument doc = mapper.toDocument(claim);

        AdjudicationResult result = AdjudicationResult.of(
                AdjudicationOutcome.APPROVED, List.of(), "No rules matched");
        mapper.applyResult(doc, result);

        assertEquals("ADJUDICATED", doc.getStatus());
    }
}
