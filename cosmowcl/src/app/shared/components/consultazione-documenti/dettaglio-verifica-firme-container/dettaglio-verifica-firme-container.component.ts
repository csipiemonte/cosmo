/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ModaleParentComponent } from 'src/app/modali/modale-parent-component';
import { InfoVerificaFirma } from 'src/app/shared/models/api/cosmoecm/infoVerificaFirma';
import { HelperService } from 'src/app/shared/services/helper.service';

@Component({
  selector: 'app-dettaglio-verifica-firme-container',
  templateUrl: './dettaglio-verifica-firme-container.component.html',
  styleUrls: ['./dettaglio-verifica-firme-container.component.scss']
})
export class DettaglioVerificaFirmeContainerComponent  extends ModaleParentComponent implements OnInit {

  @Input() firme: InfoVerificaFirma[] | null = null;
  @Input() layout: string | null = null;
  codicePagina!: string;
  codiceTab!: string;
  codiceModale!: string;

  constructor(
    public modal: NgbActiveModal,
    public helperService: HelperService) {
    super(helperService);
  }

  ngOnInit(): void {
    this.setModalName(this.codiceModale);
    this.searchHelperModale(this.codicePagina, this.codiceTab);
  }

  get render(): boolean {
    return !!(this.firme?.length);
  }

  get small(): boolean {
    return 'small' === this.layout;
  }

}
