package com.sakshi.claims.service;

import com.sakshi.claims.audit.AuditEvent;
import com.sakshi.claims.audit.AuditTrail;
import com.sakshi.claims.document.AuditEventDocument;
import com.sakshi.claims.repository.AuditEventRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The real AuditTrail implementation, backed by the append-only
 * audit_events Mongo collection. Note there's no update/delete path here
 * on purpose - see AuditTrail for why.
 */
@Component
public class MongoAuditTrail implements AuditTrail {

    private final AuditEventRepository repository;

    public MongoAuditTrail(AuditEventRepository repository) {
        this.repository = repository;
    }

    @Override
    public void record(AuditEvent event) {
        AuditEventDocument doc = new AuditEventDocument();
        doc.setClaimId(event.claimId());
        doc.setEventType(event.eventType().name());
        doc.setActorId(event.actor().actorId());
        doc.setActorRole(event.actor().role());
        doc.setTimestamp(event.timestamp());
        doc.setReason(event.reason());
        repository.save(doc);
    }

    @Override
    public List<AuditEvent> historyFor(String claimId) {
        return repository.findByClaimIdOrderByTimestampAsc(claimId).stream()
                .map(doc -> new AuditEvent(
                        doc.getClaimId(),
                        com.sakshi.claims.audit.AuditEventType.valueOf(doc.getEventType()),
                        new com.sakshi.claims.audit.Actor(doc.getActorId(), doc.getActorRole()),
                        doc.getTimestamp(),
                        doc.getReason()
                ))
                .toList();
    }
}
