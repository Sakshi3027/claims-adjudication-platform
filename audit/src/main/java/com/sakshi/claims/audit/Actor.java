package com.sakshi.claims.audit;

/**
 * Who (or what) triggered an audit event. System-generated events (the
 * rule engine flagging a claim) use "system" as the actorId - a human
 * override always carries a real analyst/auditor id.
 */
public record Actor(String actorId, String role) {

    public static Actor system() {
        return new Actor("system", "SYSTEM");
    }
}
