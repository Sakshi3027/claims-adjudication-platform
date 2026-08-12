package com.sakshi.claims.repository;

import com.sakshi.claims.document.AuditEventDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AuditEventRepository extends MongoRepository<AuditEventDocument, String> {

    List<AuditEventDocument> findByClaimIdOrderByTimestampAsc(String claimId);
}
