package com.sakshi.claims.service;

import com.sakshi.claims.audit.Actor;
import com.sakshi.claims.audit.AuditEvent;
import com.sakshi.claims.audit.AuditEventType;
import com.sakshi.claims.audit.AuditTrail;
import com.sakshi.claims.document.ClaimDocument;
import com.sakshi.claims.domain.AdjudicationResult;
import com.sakshi.claims.domain.Claim;
import com.sakshi.claims.dto.ClaimSubmissionRequest;
import com.sakshi.claims.dto.LineItemRequest;
import com.sakshi.claims.evaluator.RuleEvaluator;
import com.sakshi.claims.mapper.ClaimMapper;
import com.sakshi.claims.repository.ClaimRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * The one place that knows the full lifecycle of a claim: save it,
 * run it through the rule engine, persist the result, and write every
 * step to the audit trail. Controllers stay thin and just call into this.
 */
@Service
public class AdjudicationService {

    private final ClaimRepository claimRepository;
    private final ClaimMapper claimMapper;
    private final RuleEvaluator ruleEvaluator;
    private final AuditTrail auditTrail;

    public AdjudicationService(ClaimRepository claimRepository,
                                ClaimMapper claimMapper,
                                RuleEvaluator ruleEvaluator,
                                AuditTrail auditTrail) {
        this.claimRepository = claimRepository;
        this.claimMapper = claimMapper;
        this.ruleEvaluator = ruleEvaluator;
        this.auditTrail = auditTrail;
    }

    public ClaimDocument submitAndAdjudicate(ClaimSubmissionRequest request) {
        Claim claim = buildDomainClaim(request);
        ClaimDocument doc = claimMapper.toDocument(claim);
        doc = claimRepository.save(doc);

        auditTrail.record(AuditEvent.of(doc.getId(), AuditEventType.SUBMITTED, Actor.system(),
                "Claim " + doc.getClaimNumber() + " submitted for adjudication."));

        AdjudicationResult result = ruleEvaluator.evaluate(claim);
        claimMapper.applyResult(doc, result);
        doc = claimRepository.save(doc);

        auditTrail.record(AuditEvent.of(doc.getId(), AuditEventType.RULE_EVALUATED, Actor.system(), result.reason()));

        if (result.outcome().name().equals("MANUAL_REVIEW")) {
            auditTrail.record(AuditEvent.of(doc.getId(), AuditEventType.FLAGGED, Actor.system(), result.reason()));
        }

        return doc;
    }

    public ClaimDocument getClaim(String id) {
        return claimRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No claim found with id " + id));
    }

    public List<ClaimDocument> reviewQueue() {
        return claimRepository.findByStatus("FLAGGED");
    }

    public List<ClaimDocument> allClaims() {
        return claimRepository.findAll();
    }

    public ClaimDocument override(String claimId, String actorId, String newOutcome, String reason) {
        ClaimDocument doc = getClaim(claimId);
        String previousOutcome = doc.getAdjudicationResult() != null
                ? doc.getAdjudicationResult().getOutcome()
                : "NONE";

        doc.getAdjudicationResult().setOutcome(newOutcome);
        doc.setStatus("ADJUDICATED");
        doc = claimRepository.save(doc);

        auditTrail.record(AuditEvent.of(claimId, AuditEventType.MANUAL_OVERRIDE,
                new Actor(actorId, "AUDITOR"),
                "Overridden from " + previousOutcome + " to " + newOutcome + ": " + reason));

        return doc;
    }

    private Claim buildDomainClaim(ClaimSubmissionRequest request) {
        Claim claim = new Claim(
                request.claimNumber(),
                request.memberId(),
                com.sakshi.claims.domain.ClaimType.valueOf(request.claimType()),
                request.diagnosisCodes(),
                request.lineItems().stream()
                        .map(this::toLineItem)
                        .toList(),
                request.priorAuthOnFile()
        );
        return claim;
    }

    private com.sakshi.claims.domain.LineItem toLineItem(LineItemRequest req) {
        return new com.sakshi.claims.domain.LineItem(req.procedureCode(), req.amount(), req.priorAuthRequired());
    }
}
