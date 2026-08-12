package com.sakshi.claims.audit;

import java.time.Instant;

/**
 * One entry in a claim's audit trail. These are never updated or deleted
 * once written - if a decision changes, that's a new event, not an edit
 * to an old one. That's the whole point: an auditor needs to see the full
 * history, not just the current state.
 */
public record AuditEvent(
        String claimId,
        AuditEventType eventType,
        Actor actor,
        Instant timestamp,
        String reason
) {
    public static AuditEvent of(String claimId, AuditEventType eventType, Actor actor, String reason) {
        return new AuditEvent(claimId, eventType, actor, Instant.now(), reason);
    }
}
