/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  AfterViewInit,
  Component,
  ElementRef,
  ViewChild,
} from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-show-raw-data-modal',
  template: `
 <div class="modal-header">
    <h5 class="modal-title" id="modal-title">{{titolo}}</h5>
    <button type="button" class="close" aria-label="Close button" aria-describedby="modal-title" (click)="activeModal.dismiss('Cross click')">
      <span aria-hidden="true">&times;</span>
    </button>
  </div>
  <div class="modal-body">
    <ngx-json-viewer [json]="data" [depth]="3"></ngx-json-viewer>
  </div>
  <div class="modal-footer">
    <button class="btn btn-outline-primary btn-sm" type="button" (click)="activeModal.close('ok click')">Close</button>
  </div>
`
})

export class ShowRawDataModalComponent {

  titolo: string | null = null;
  data: any = null;

  constructor(public activeModal: NgbActiveModal) { }


}
