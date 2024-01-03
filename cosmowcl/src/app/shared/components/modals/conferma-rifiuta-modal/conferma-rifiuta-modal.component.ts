/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-conferma-rifiuta-modal',
  template: `
 <div class="modal-header">
    <h5 class="modal-title" id="modal-title">{{titolo}}</h5>
    <button type="button" class="close" aria-label="Close button" aria-describedby="modal-title" (click)="activeModal.dismiss('Cross click')">
      <span aria-hidden="true">&times;</span>
    </button>
  </div>
  <div class="modal-body" *ngIf="messaggio">
      <p>{{messaggio}}</p>
  </div>
  <div class="modal-footer">
    <button class="btn btn-outline-primary btn-sm" type="button" (click)="activeModal.close('ok click')">{{primoBottone}}</button>
    <button  ngbAutoFocus class="btn btn-primary btn-sm" type="button" data-dismiss="modal" (click)="activeModal.dismiss('cancel click')">{{secondoBottone}}</button>
  </div>
`
})

export class ConfermaRifiutaModalComponent {

  titolo: string | null = null;
  messaggio: string | null = null;
  primoBottone: string | null = null;
  secondoBottone: string | null = null;

  constructor(public activeModal: NgbActiveModal) { }

}
