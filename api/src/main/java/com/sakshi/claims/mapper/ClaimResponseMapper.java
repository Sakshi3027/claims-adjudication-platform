package com.sakshi.claims.mapper;

import com.sakshi.claims.document.AdjudicationResultDocument;
import com.sakshi.claims.document.ClaimDocument;
import com.sakshi.claims.document.LineItemDocument;
import com.sakshi.claims.dto.AuditEventResponse;
import com.sakshi.claims.dto.ClaimResponse;
import com.sakshi.claims.audit.AuditEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ClaimResponseMapper {

    public ClaimResponse toResponse(ClaimDocument doc) {
        BigDecimal total = doc.getLineItems() == null ? BigDecimal.ZERO : doc.getLineItems().stream()
                .map(LineItemDocument::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        AdjudicationResultDocument result = doc.getAdjudicationResult();
        ClaimResponse.AdjudicationResultResponse resultResponse = result == null ? null
                : new ClaimResponse.AdjudicationResultResponse(
                        result.getOutcome(), result.getMatchedRuleIds(), result.getReason(), result.getEvaluatedAt());

        return new ClaimResponse(
                doc.getId(),
                doc.getClaimNumber(),
                doc.getMemberId(),
                doc.getClaimType(),
                doc.getStatus(),
                total,
                doc.getSubmittedAt(),
                resultResponse
        );
    }

    public List<AuditEventResponse> toAuditResponses(List<AuditEvent> events) {
        return events.stream()
                .map(e -> new AuditEventResponse(
                        e.eventType().name(), e.actor().actorId(), e.actor().role(), e.timestamp(), e.reason()))
                .toList();
    }
}
