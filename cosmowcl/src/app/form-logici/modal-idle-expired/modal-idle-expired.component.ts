/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of, throwError } from 'rxjs';
import { delay, finalize, map, mergeMap } from 'rxjs/operators';
import { Lock } from 'src/app/shared/models/api/cosmobusiness/lock';
import { FormLogiciContext } from 'src/app/shared/models/form-logici/form-logici-context.model';
import { LockService } from 'src/app/shared/services/lock.service';
import { PraticheService } from 'src/app/shared/services/pratiche.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { Utils } from 'src/app/shared/utilities/utilities';

@Component({
  selector: 'app-modal-idle-expired',
  templateUrl: './modal-idle-expired.component.html',
  styleUrls: ['./modal-idle-expired.component.scss']
})
export class ModalIdleExpiredComponent implements OnInit, OnDestroy {

  context: FormLogiciContext | null = null;
  principalCF: string | null = null;
  reattempted = false;
  previousLock!: Lock;
  attemptingRelock = false;
  otherLock: Lock | null = null;
  relockFailed = false;
  relockFailedUnrecoverable = false;
  relockSuccess = false;
  relockError: string | null = null;
  checksumModifica: string | null = null;

  timers: NodeJS.Timer[] = [];
  gotoHomepageCooldown = 10;
  retryCooldown = 0;

  constructor(
    public modal: NgbActiveModal,
    private securityService: SecurityService,
    private lockService: LockService,
    private praticheService: PraticheService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.securityService.principal$.subscribe(principal => {
      this.principalCF = principal?.codiceFiscale;
    });

    this.timers.push(setInterval(() => {
      if (this.gotoHomepageCooldown > 0) { this.gotoHomepageCooldown --; }
      if (this.retryCooldown > 0) { this.retryCooldown --; }
    }, 1000));
  }

  ngOnDestroy(): void {
    this.timers.forEach(t => clearInterval(t));
    this.securityService.principal$.subscribe(principal => {
      this.principalCF = principal?.codiceFiscale;
    });
  }

  reset(previousLock: Lock, context: FormLogiciContext) {
    this.context = context;
    this.previousLock = previousLock;
    this.reattempted = false;
    this.checksumModifica = null;
    this.getChecksumModifica().subscribe(checksumModifica => this.checksumModifica = checksumModifica);
  }

  changed() {
    this.cdr.detectChanges();
  }

  reattempt() {
    if (this.attemptingRelock) {
      return;
    }
    this.reattempted = true;
    this.attemptingRelock = true;
    this.relockError = null;
    this.otherLock = null;

    this.getChecksumModifica().pipe(
      map(newChecksum => {
        if (newChecksum !== this.checksumModifica) {
          this.relockFailedUnrecoverable = true;
          throw new Error('Il task e\' stato lavorato da qualcun altro mentre eri inattivo. I dati su questa pagina non sono piu\' aggiornati.');
        }
      }),
      mergeMap(() => this.retryLock()),
      delay(3000),
      finalize(() => {
        this.attemptingRelock = false;
        this.changed();
      })
    ).subscribe(result => {
      if (result.acquired) {
        this.relockFailed = false;
        this.relockSuccess = true;
        setTimeout(() => {
          this.modal.close({
            relocked: true,
            lock: result.acquired
          });
        }, 2000);
      } else {
        this.relockSuccess = false;
        this.relockFailed = true;
        if (result.existing) {
          this.otherLock = result.existing;
        }
        this.retryCooldown = 10;
      }
    }, failure => {
      this.relockSuccess = false;
      this.relockFailed = true;
      this.otherLock = null;
      this.relockError = Utils.toMessage(failure);
      this.retryCooldown = 5;
    });
  }

  goHome() {
    this.modal.dismiss('go_home');
  }

  private retryLock(): Observable<{
    existing: Lock | null;
    acquired: Lock | null;
  }> {
    if (!this.previousLock?.codiceRisorsa?.length) {
      return throwError('No previous lock to reattempt.');
    }
    return this.lockService.acquireOrGet(this.previousLock.codiceRisorsa).pipe(map(lockResult => {
      return {
        ...lockResult
      };
    }));
  }

  private getChecksumModifica(): Observable<string> {
    const ids = [];
    if (this.context?.task?.id) {
      ids.push(this.context.task.id);
    }
    if (this.context?.childTask?.id) {
      ids.push(this.context.childTask.id);
    }
    return this.praticheService.getChecksumModifica(ids);
  }
}
