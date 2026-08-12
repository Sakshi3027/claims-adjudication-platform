package com.sakshi.claims.audit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory implementation, mainly for unit tests and for running
 * the rules-engine/audit modules without a Mongo instance around. The
 * real implementation (backed by Mongo) lives in the api module.
 */
public class InMemoryAuditTrail implements AuditTrail {

    private final Map<String, List<AuditEvent>> events = new ConcurrentHashMap<>();

    @Override
    public void record(AuditEvent event) {
        events.computeIfAbsent(event.claimId(), id -> new ArrayList<>()).add(event);
    }

    @Override
    public List<AuditEvent> historyFor(String claimId) {
        return Collections.unmodifiableList(events.getOrDefault(claimId, List.of()));
    }
}
