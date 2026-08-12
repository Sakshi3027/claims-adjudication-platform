package com.sakshi.claims.repository;

import com.sakshi.claims.document.ClaimDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ClaimRepository extends MongoRepository<ClaimDocument, String> {

    Optional<ClaimDocument> findByClaimNumber(String claimNumber);

    List<ClaimDocument> findByStatus(String status);
}
