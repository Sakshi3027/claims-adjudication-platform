package com.sakshi.claims.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "claims")
public class ClaimDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String claimNumber;

    private String memberId;
    private String claimType;
    private List<String> diagnosisCodes;
    private List<LineItemDocument> lineItems;
    private Instant submittedAt;
    private String status;
    private boolean priorAuthOnFile;
    private AdjudicationResultDocument adjudicationResult;

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

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public List<String> getDiagnosisCodes() {
        return diagnosisCodes;
    }

    public void setDiagnosisCodes(List<String> diagnosisCodes) {
        this.diagnosisCodes = diagnosisCodes;
    }

    public List<LineItemDocument> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItemDocument> lineItems) {
        this.lineItems = lineItems;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPriorAuthOnFile() {
        return priorAuthOnFile;
    }

    public void setPriorAuthOnFile(boolean priorAuthOnFile) {
        this.priorAuthOnFile = priorAuthOnFile;
    }

    public AdjudicationResultDocument getAdjudicationResult() {
        return adjudicationResult;
    }

    public void setAdjudicationResult(AdjudicationResultDocument adjudicationResult) {
        this.adjudicationResult = adjudicationResult;
    }
}
