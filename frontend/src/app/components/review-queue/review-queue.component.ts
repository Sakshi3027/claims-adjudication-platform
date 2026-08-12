import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Subscription, switchMap, timer } from 'rxjs';
import { ClaimService } from '../../services/claim.service';
import { ClaimResponse } from '../../models/claim.model';

/**
 * Polls the review queue every 10s rather than a true push connection -
 * this is the honest starting point (Kafka/websocket-driven live updates
 * would replace the timer() with a stream subscription later, same
 * Observable contract downstream).
 */
@Component({
  selector: 'app-review-queue',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './review-queue.component.html',
  styleUrl: './review-queue.component.css'
})
export class ReviewQueueComponent implements OnInit, OnDestroy {

  queue: ClaimResponse[] = [];
  loading = true;
  private pollSub?: Subscription;

  constructor(private claimService: ClaimService) {}

  ngOnInit(): void {
    this.pollSub = timer(0, 10_000)
      .pipe(switchMap(() => this.claimService.reviewQueue()))
      .subscribe({
        next: (claims) => {
          this.queue = claims;
          this.loading = false;
        },
        error: () => {
          this.loading = false;
        }
      });
  }

  ngOnDestroy(): void {
    this.pollSub?.unsubscribe();
  }
}
