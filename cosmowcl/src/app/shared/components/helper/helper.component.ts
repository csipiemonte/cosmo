/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { Helper } from '../../models/api/cosmonotifications/helper';
import { HelperService } from '../../services/helper.service';
import { HelperModalComponent } from '../modals/helper-modal/helper-modal.component';

@Component({
  selector: 'app-helper',
  templateUrl: './helper.component.html',
  styleUrls: ['./helper.component.scss']
})
export class HelperComponent implements OnInit{

  @Input() helper!: Helper;
  @Input() parentRoute!: ActivatedRoute;
  @Input() childrenRoute!: ActivatedRoute[];
  @Input() type: HelperType = HelperType.CLASSIC;
  html!: string;
  title!: string;

  constructor(
    protected logger: NGXLogger,
    protected route: ActivatedRoute,
    protected router: Router,
    protected helperService: HelperService,
    private modal: NgbModal) {

  }

  ngOnInit(): void {
  }

  openHelperPopup() {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      size: 'xl',
    };
    const modalRef = this.modal.open(HelperModalComponent, ngbModalOptions);

    modalRef.componentInstance.html = this.helper.html;
    modalRef.componentInstance.title = this.helper.codiceModale ? this.helper.codiceModale.descrizione :
      this.helper.codiceTab?.descrizione ? this.helper.codiceTab?.descrizione : this.helper.codicePagina?.descrizione;
  }
}

export enum HelperType {
  CLASSIC = 'CLASSIC',
  MODAL = 'MODAL'
}

