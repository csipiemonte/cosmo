/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-modal-idle-warning',
  templateUrl: './modal-idle-warning.component.html',
  styleUrls: ['./modal-idle-warning.component.scss']
})
export class ModalIdleWarningComponent implements OnInit {

  timingOut = true;
  timeout = environment.idle.beforeTimeout;

  constructor(public modal: NgbActiveModal,
              private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
  }

  reset(timeout?: number) {
    this.timingOut = true;
    this.timeout = timeout ?? environment.idle.beforeTimeout;
  }

  changed() {
    this.cdr.detectChanges();
  }
}
