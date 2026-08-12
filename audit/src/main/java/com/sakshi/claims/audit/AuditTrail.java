package com.sakshi.claims.audit;

import java.util.List;

/**
 * Write-mostly log for a claim's audit history. Append-only by design -
 * there's intentionally no update() or delete() method on this interface.
 */
public interface AuditTrail {

    void record(AuditEvent event);

    List<AuditEvent> historyFor(String claimId);
}
