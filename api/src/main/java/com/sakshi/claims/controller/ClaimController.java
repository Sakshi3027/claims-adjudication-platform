package com.sakshi.claims.controller;

import com.sakshi.claims.audit.AuditTrail;
import com.sakshi.claims.document.ClaimDocument;
import com.sakshi.claims.dto.*;
import com.sakshi.claims.mapper.ClaimResponseMapper;
import com.sakshi.claims.service.AdjudicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final AdjudicationService adjudicationService;
    private final AuditTrail auditTrail;
    private final ClaimResponseMapper responseMapper;

    public ClaimController(AdjudicationService adjudicationService,
                            AuditTrail auditTrail,
                            ClaimResponseMapper responseMapper) {
        this.adjudicationService = adjudicationService;
        this.auditTrail = auditTrail;
        this.responseMapper = responseMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClaimResponse submit(@Valid @RequestBody ClaimSubmissionRequest request) {
        ClaimDocument doc = adjudicationService.submitAndAdjudicate(request);
        return responseMapper.toResponse(doc);
    }

    @GetMapping
    public List<ClaimResponse> listAll() {
        return adjudicationService.allClaims().stream().map(responseMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ClaimResponse getOne(@PathVariable String id) {
        return responseMapper.toResponse(adjudicationService.getClaim(id));
    }

    @GetMapping("/queue")
    public List<ClaimResponse> reviewQueue() {
        return adjudicationService.reviewQueue().stream().map(responseMapper::toResponse).toList();
    }

    @GetMapping("/{id}/audit-trail")
    public List<AuditEventResponse> auditTrail(@PathVariable String id) {
        return responseMapper.toAuditResponses(auditTrail.historyFor(id));
    }

    @PostMapping("/{id}/override")
    public ClaimResponse override(@PathVariable String id, @Valid @RequestBody OverrideRequest request) {
        ClaimDocument doc = adjudicationService.override(id, request.actorId(), request.newOutcome(), request.reason());
        return responseMapper.toResponse(doc);
    }
}
