package com.sakshi.claims.audit;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryAuditTrailTest {

    @Test
    void recordsEventsInOrderPerClaim() {
        AuditTrail trail = new InMemoryAuditTrail();

        trail.record(AuditEvent.of("CLM-1", AuditEventType.SUBMITTED, Actor.system(), "Claim submitted"));
        trail.record(AuditEvent.of("CLM-1", AuditEventType.RULE_EVALUATED, Actor.system(), "Rule RULE-014 matched"));
        trail.record(AuditEvent.of("CLM-2", AuditEventType.SUBMITTED, Actor.system(), "Different claim"));

        List<AuditEvent> history = trail.historyFor("CLM-1");

        assertEquals(2, history.size());
        assertEquals(AuditEventType.SUBMITTED, history.get(0).eventType());
        assertEquals(AuditEventType.RULE_EVALUATED, history.get(1).eventType());
    }

    @Test
    void returnsEmptyListForUnknownClaim() {
        AuditTrail trail = new InMemoryAuditTrail();
        assertTrue(trail.historyFor("nonexistent").isEmpty());
    }
}
