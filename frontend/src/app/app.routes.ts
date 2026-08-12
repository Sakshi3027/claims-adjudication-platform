import { Routes } from '@angular/router';
import { ClaimSubmissionComponent } from './components/claim-submission/claim-submission.component';
import { ReviewQueueComponent } from './components/review-queue/review-queue.component';
import { ClaimDetailComponent } from './components/claim-detail/claim-detail.component';

export const routes: Routes = [
  { path: '', redirectTo: 'submit', pathMatch: 'full' },
  { path: 'submit', component: ClaimSubmissionComponent },
  { path: 'queue', component: ReviewQueueComponent },
  { path: 'claims/:id', component: ClaimDetailComponent }
];
