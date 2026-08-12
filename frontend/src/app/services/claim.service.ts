import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuditEventResponse, ClaimResponse, ClaimSubmissionRequest } from '../models/claim.model';

@Injectable({ providedIn: 'root' })
export class ClaimService {

  private readonly baseUrl = `${environment.apiBaseUrl}/claims`;

  constructor(private http: HttpClient) {}

  submit(request: ClaimSubmissionRequest): Observable<ClaimResponse> {
    return this.http.post<ClaimResponse>(this.baseUrl, request);
  }

  listAll(): Observable<ClaimResponse[]> {
    return this.http.get<ClaimResponse[]>(this.baseUrl);
  }

  getOne(id: string): Observable<ClaimResponse> {
    return this.http.get<ClaimResponse>(`${this.baseUrl}/${id}`);
  }

  reviewQueue(): Observable<ClaimResponse[]> {
    return this.http.get<ClaimResponse[]>(`${this.baseUrl}/queue`);
  }

  auditTrail(id: string): Observable<AuditEventResponse[]> {
    return this.http.get<AuditEventResponse[]>(`${this.baseUrl}/${id}/audit-trail`);
  }

  override(id: string, actorId: string, newOutcome: string, reason: string): Observable<ClaimResponse> {
    return this.http.post<ClaimResponse>(`${this.baseUrl}/${id}/override`, {
      actorId, newOutcome, reason
    });
  }
}
