package com.sakshi.claims.domain;

import java.math.BigDecimal;

/**
 * One billed procedure/service within a claim. Claims can have anywhere
 * from one to a dozen+ of these, which is part of why we're storing
 * claims as documents rather than trying to normalize this into its
 * own relational table.
 */
public record LineItem(String procedureCode, BigDecimal amount, boolean priorAuthRequired) {
}
