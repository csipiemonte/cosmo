/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-info-modal',
  template: `
 <div class="modal-header">
    <h5 class="modal-title" id="modal-title">{{titolo}}</h5>
    <button type="button" class="close" aria-label="Close button" aria-describedby="modal-title" (click)="activeModal.dismiss('Cross click')">
      <span aria-hidden="true">&times;</span>
    </button>
  </div>
  <div class="modal-body" *ngIf="messaggio">
    <ng-container *ngIf="showException && eccezione; else noException">
      <div class="alert alert-danger">
        <p>
          {{messaggio}}
          &nbsp;
          <a style="background-color: transparent; border-color: transparent;" type="button" data-toggle="collapse" aria-expanded="false" data-target="#exception" aria-controls="exception">
            <span class="span-noexpanded fas fa-angle-down"></span>
            <span class="span-expanded fas fa-angle-up"></span>
          </a>
          <span class="collapse ml-3 mt-1 span-exception" id="exception">
            <br>{{eccezione}}
          </span>
        </p>
      </div>
    </ng-container>
    <ng-template #noException>
      <p class="alert alert-danger">{{messaggio}}</p>
    </ng-template>

  </div>
  <div class="modal-footer">
    <button ngbAutoFocus class="btn btn-primary btn-sm" type="button" (click)="activeModal.close('ok click')">{{bottone}}</button>
  </div>
`,
  styles: [`
  a[aria-expanded="false"] .span-expanded{
    display:none;
  }
  a[aria-expanded="true"] .span-noexpanded{
    display:none;
  }
  .span-exception {
    font-size: 10px;
    word-wrap: break-word;
    line-height: initial;
  }
  `]
})

export class ErrorModalComponent {

  titolo: string | null = null;
  messaggio: string | null = null;
  eccezione: string | null = null;
  bottone: string | null = null;


  constructor(public activeModal: NgbActiveModal) {
  }



  get showException(): boolean{
    if (environment) {
      return environment.enableExceptionMessage;
    }
    return false;
  }

}
