package com.sakshi.claims.mapper;

import com.sakshi.claims.document.AdjudicationResultDocument;
import com.sakshi.claims.document.ClaimDocument;
import com.sakshi.claims.document.LineItemDocument;
import com.sakshi.claims.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converts between the Mongo-mapped documents (which need to be Spring
 * Data friendly - getters/setters, no-arg constructors) and the plain
 * domain types the rules-engine actually operates on. Keeping this
 * translation in one place means the rules-engine module never has to
 * know Mongo exists.
 */
@Component
public class ClaimMapper {

    public Claim toDomain(ClaimDocument doc) {
        Claim claim = new Claim(
                doc.getClaimNumber(),
                doc.getMemberId(),
                ClaimType.valueOf(doc.getClaimType()),
                doc.getDiagnosisCodes(),
                doc.getLineItems() == null ? List.of() : doc.getLineItems().stream()
                        .map(li -> new LineItem(li.getProcedureCode(), li.getAmount(), li.isPriorAuthRequired()))
                        .toList(),
                doc.isPriorAuthOnFile()
        );
        claim.setId(doc.getId());
        claim.setSubmittedAt(doc.getSubmittedAt());
        if (doc.getStatus() != null) {
            claim.setStatus(ClaimStatus.valueOf(doc.getStatus()));
        }
        return claim;
    }

    public ClaimDocument toDocument(Claim claim) {
        ClaimDocument doc = new ClaimDocument();
        doc.setId(claim.getId());
        doc.setClaimNumber(claim.getClaimNumber());
        doc.setMemberId(claim.getMemberId());
        doc.setClaimType(claim.getClaimType().name());
        doc.setDiagnosisCodes(claim.getDiagnosisCodes());
        doc.setLineItems(claim.getLineItems().stream()
                .map(li -> new LineItemDocument(li.procedureCode(), li.amount(), li.priorAuthRequired()))
                .toList());
        doc.setSubmittedAt(claim.getSubmittedAt());
        doc.setStatus(claim.getStatus().name());
        doc.setPriorAuthOnFile(claim.isPriorAuthOnFile());
        return doc;
    }

    public void applyResult(ClaimDocument doc, AdjudicationResult result) {
        doc.setAdjudicationResult(new AdjudicationResultDocument(
                result.outcome().name(),
                result.matchedRuleIds(),
                result.reason(),
                result.evaluatedAt()
        ));
        doc.setStatus(switch (result.outcome()) {
            case APPROVED, DENIED -> ClaimStatus.ADJUDICATED.name();
            case MANUAL_REVIEW -> ClaimStatus.FLAGGED.name();
        });
    }
}
