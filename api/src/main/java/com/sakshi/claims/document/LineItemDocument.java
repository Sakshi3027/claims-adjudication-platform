package com.sakshi.claims.document;

import java.math.BigDecimal;

public class LineItemDocument {

    private String procedureCode;
    private BigDecimal amount;
    private boolean priorAuthRequired;

    public LineItemDocument() {
    }

    public LineItemDocument(String procedureCode, BigDecimal amount, boolean priorAuthRequired) {
        this.procedureCode = procedureCode;
        this.amount = amount;
        this.priorAuthRequired = priorAuthRequired;
    }

    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isPriorAuthRequired() {
        return priorAuthRequired;
    }

    public void setPriorAuthRequired(boolean priorAuthRequired) {
        this.priorAuthRequired = priorAuthRequired;
    }
}
