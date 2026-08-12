import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClaimService } from '../../services/claim.service';
import { ClaimResponse, ClaimSubmissionRequest, LineItemRequest } from '../../models/claim.model';

@Component({
  selector: 'app-claim-submission',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './claim-submission.component.html',
  styleUrl: './claim-submission.component.css'
})
export class ClaimSubmissionComponent {

  claimNumber = '';
  memberId = '';
  claimType: 'MEDICAL' | 'PHARMACY' | 'DENTAL' = 'MEDICAL';
  diagnosisCodesInput = '';
  priorAuthOnFile = false;

  lineItems: LineItemRequest[] = [
    { procedureCode: '', amount: 0, priorAuthRequired: false }
  ];

  submitting = false;
  lastResult: ClaimResponse | null = null;
  errorMessage: string | null = null;

  constructor(private claimService: ClaimService) {}

  addLineItem(): void {
    this.lineItems.push({ procedureCode: '', amount: 0, priorAuthRequired: false });
  }

  removeLineItem(index: number): void {
    this.lineItems.splice(index, 1);
  }

  submit(): void {
    this.errorMessage = null;
    this.lastResult = null;
    this.submitting = true;

    const request: ClaimSubmissionRequest = {
      claimNumber: this.claimNumber,
      memberId: this.memberId,
      claimType: this.claimType,
      diagnosisCodes: this.diagnosisCodesInput
        .split(',')
        .map(code => code.trim())
        .filter(code => code.length > 0),
      lineItems: this.lineItems,
      priorAuthOnFile: this.priorAuthOnFile
    };

    this.claimService.submit(request).subscribe({
      next: (result) => {
        this.lastResult = result;
        this.submitting = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.error ?? 'Something went wrong submitting the claim.';
        this.submitting = false;
      }
    });
  }
}
