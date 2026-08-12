package com.sakshi.claims.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record LineItemRequest(
        @NotBlank String procedureCode,
        @NotNull @PositiveOrZero BigDecimal amount,
        boolean priorAuthRequired
) {
}
