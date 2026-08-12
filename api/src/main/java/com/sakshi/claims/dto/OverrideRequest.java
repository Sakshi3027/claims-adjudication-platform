package com.sakshi.claims.dto;

import jakarta.validation.constraints.NotBlank;

public record OverrideRequest(
        @NotBlank String actorId,
        @NotBlank String newOutcome,
        @NotBlank String reason
) {
}
