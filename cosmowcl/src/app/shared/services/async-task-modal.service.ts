/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { TaskModalComponent, TaskModalOptions } from '../components/task-modal/task-modal.component';

@Injectable()
export class AsyncTaskModalService {
  private isOpen = 0;

  constructor(
    protected modalService: NgbModal,
  ) {
    // NOP
  }

  open(options: TaskModalOptions): NgbModalRef {

    this.isOpen ++;
    const ngbModalOptions: NgbModalOptions = {
      backdrop : 'static',
      keyboard : false,
      size: 'lg',
      windowClass: 'task-modal',
    };
    const modalRef = this.modalService.open(TaskModalComponent, ngbModalOptions);
    (modalRef.componentInstance as TaskModalComponent).initialize(options);
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
