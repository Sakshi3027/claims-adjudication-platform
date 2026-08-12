package com.sakshi.claims.document;

import java.time.Instant;
import java.util.List;

public class AdjudicationResultDocument {

    private String outcome;
    private List<String> matchedRuleIds;
    private String reason;
    private Instant evaluatedAt;

    public AdjudicationResultDocument() {
    }

    public AdjudicationResultDocument(String outcome, List<String> matchedRuleIds, String reason, Instant evaluatedAt) {
        this.outcome = outcome;
        this.matchedRuleIds = matchedRuleIds;
        this.reason = reason;
        this.evaluatedAt = evaluatedAt;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public List<String> getMatchedRuleIds() {
        return matchedRuleIds;
    }

    public void setMatchedRuleIds(List<String> matchedRuleIds) {
        this.matchedRuleIds = matchedRuleIds;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getEvaluatedAt() {
        return evaluatedAt;
    }

    public void setEvaluatedAt(Instant evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }
}
