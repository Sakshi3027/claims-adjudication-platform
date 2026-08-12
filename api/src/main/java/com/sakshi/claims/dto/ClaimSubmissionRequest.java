package com.sakshi.claims.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ClaimSubmissionRequest(
        @NotBlank String claimNumber,
        @NotBlank String memberId,
        @NotBlank String claimType,
        List<String> diagnosisCodes,
        @NotEmpty @Valid List<LineItemRequest> lineItems,
        boolean priorAuthOnFile
) {
}
