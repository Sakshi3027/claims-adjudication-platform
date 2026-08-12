package com.sakshi.claims.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * A claim as submitted for adjudication. This is intentionally a plain
 * data holder - it doesn't know anything about how it gets evaluated or
 * persisted. That logic lives in RuleEvaluator and the Mongo repository
 * layer respectively.
 */
public class Claim {

    private String id;
    private String claimNumber;
    private String memberId;
    private ClaimType claimType;
    private List<String> diagnosisCodes;
    private List<LineItem> lineItems;
    private Instant submittedAt;
    private ClaimStatus status;
    private boolean priorAuthOnFile;

    public Claim() {
        // needed for Mongo/Jackson deserialization
    }

    public Claim(String claimNumber, String memberId, ClaimType claimType,
                 List<String> diagnosisCodes, List<LineItem> lineItems,
                 boolean priorAuthOnFile) {
        this.claimNumber = claimNumber;
        this.memberId = memberId;
        this.claimType = claimType;
        this.diagnosisCodes = diagnosisCodes;
        this.lineItems = lineItems;
        this.priorAuthOnFile = priorAuthOnFile;
        this.submittedAt = Instant.now();
        this.status = ClaimStatus.PENDING;
    }

    public BigDecimal totalAmount() {
        if (lineItems == null || lineItems.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return lineItems.stream()
                .map(LineItem::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean requiresPriorAuth() {
        return lineItems != null && lineItems.stream().anyMatch(LineItem::priorAuthRequired);
    }

    // --- getters / setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(ClaimType claimType) {
        this.claimType = claimType;
    }

    public List<String> getDiagnosisCodes() {
        return diagnosisCodes;
    }

    public void setDiagnosisCodes(List<String> diagnosisCodes) {
        this.diagnosisCodes = diagnosisCodes;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public boolean isPriorAuthOnFile() {
        return priorAuthOnFile;
    }

    public void setPriorAuthOnFile(boolean priorAuthOnFile) {
        this.priorAuthOnFile = priorAuthOnFile;
    }
}
