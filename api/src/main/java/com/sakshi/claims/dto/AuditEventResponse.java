package com.sakshi.claims.dto;

import java.time.Instant;

public record AuditEventResponse(
        String eventType,
        String actorId,
        String actorRole,
        Instant timestamp,
        String reason
) {
}
