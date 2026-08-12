import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ClaimService } from '../../services/claim.service';
import { AuditEventResponse, ClaimResponse } from '../../models/claim.model';

@Component({
  selector: 'app-claim-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './claim-detail.component.html',
  styleUrl: './claim-detail.component.css'
})
export class ClaimDetailComponent implements OnInit {

  claim: ClaimResponse | null = null;
  auditTrail: AuditEventResponse[] = [];
  loading = true;

  overrideActorId = '';
  overrideOutcome: 'APPROVED' | 'DENIED' = 'APPROVED';
  overrideReason = '';
  overriding = false;

  private claimId!: string;

  constructor(private route: ActivatedRoute, private claimService: ClaimService) {}

  ngOnInit(): void {
    this.claimId = this.route.snapshot.paramMap.get('id')!;
    this.load();
  }

  load(): void {
    this.loading = true;
    this.claimService.getOne(this.claimId).subscribe(claim => {
      this.claim = claim;
      this.loading = false;
    });
    this.claimService.auditTrail(this.claimId).subscribe(events => {
      this.auditTrail = events;
    });
  }

  submitOverride(): void {
    this.overriding = true;
    this.claimService.override(this.claimId, this.overrideActorId, this.overrideOutcome, this.overrideReason)
      .subscribe({
        next: () => {
          this.overriding = false;
          this.overrideReason = '';
          this.load();
        },
        error: () => {
          this.overriding = false;
        }
      });
  }
}
