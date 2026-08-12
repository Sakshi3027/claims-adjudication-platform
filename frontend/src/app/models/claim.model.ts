export interface LineItemRequest {
  procedureCode: string;
  amount: number;
  priorAuthRequired: boolean;
}

export interface ClaimSubmissionRequest {
  claimNumber: string;
  memberId: string;
  claimType: 'MEDICAL' | 'PHARMACY' | 'DENTAL';
  diagnosisCodes: string[];
  lineItems: LineItemRequest[];
  priorAuthOnFile: boolean;
}

export interface AdjudicationResultResponse {
  outcome: 'APPROVED' | 'DENIED' | 'MANUAL_REVIEW';
  matchedRuleIds: string[];
  reason: string;
  evaluatedAt: string;
}

export interface ClaimResponse {
  id: string;
  claimNumber: string;
  memberId: string;
  claimType: string;
  status: string;
  totalAmount: number;
  submittedAt: string;
  adjudicationResult: AdjudicationResultResponse | null;
}

export interface AuditEventResponse {
  eventType: string;
  actorId: string;
  actorRole: string;
  timestamp: string;
  reason: string;
}
