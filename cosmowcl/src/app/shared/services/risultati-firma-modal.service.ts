/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { RisultatiFirmaModalComponent, RisultatiFirmaOptions } from '../components/risultati-firma-modal/risultati-firma-modal.component';

@Injectable()
export class RisultatiFirmaModalService {
  private isOpen = 0;

  constructor(
    protected modalService: NgbModal,
  ) {
    // NOP
  }

  open(options: RisultatiFirmaOptions): NgbModalRef {

    this.isOpen ++;
    const ngbModalOptions: NgbModalOptions = {
      backdrop : 'static',
      keyboard : false,
      size: 'lg',
      windowClass: 'task-modal',
    };
    const modalRef = this.modalService.open(RisultatiFirmaModalComponent, ngbModalOptions);
    (modalRef.componentInstance as RisultatiFirmaModalComponent).initialize(options);
    modalRef.result.then(
      () => {
        this.isOpen --;
      },
      () => {
        this.isOpen --;
      }
    );
    return modalRef;
  }
}
