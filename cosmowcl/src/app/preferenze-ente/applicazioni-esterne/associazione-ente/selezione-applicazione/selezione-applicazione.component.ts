/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { CosmoTableColumnSize, CosmoTableComponent, ICosmoTableColumn } from 'ngx-cosmo-table';
import { ModaleParentComponent } from 'src/app/modali/modale-parent-component';
import { ApplicazioneEsternaConValidita } from 'src/app/shared/models/api/cosmoauthorization/applicazioneEsternaConValidita';
import { HelperService } from 'src/app/shared/services/helper.service';

@Component({
  selector: 'app-selezione-applicazione',
  templateUrl: './selezione-applicazione.component.html',
  styleUrls: ['./selezione-applicazione.component.scss']
})
export class SelezioneApplicazioneComponent extends ModaleParentComponent implements OnInit {

  @ViewChild('table') table: CosmoTableComponent | null = null;

  appNonAssociate: ApplicazioneEsternaConValidita[] = [];
  appDaAggiungere!: ApplicazioneEsternaConValidita;
  codicePagina!: string;
  codiceTab!: string;
  codiceModale!: string;

  columns: ICosmoTableColumn[] = [
    { name: 'icona', label: 'Icona', canHide: false, canSort: false, applyTemplate: true, size: CosmoTableColumnSize.XXS },
    {
      name: 'descrizione', label: 'Descrizione', field: 'descrizione', canSort: true, canFilter: true, defaultFilter: true
    },
  ];

  constructor(
    public modal: NgbActiveModal,
    public helperService: HelperService) {
      super(helperService);
     }

  ngOnInit(): void {
    this.setModalName(this.codiceModale);
    this.searchHelperModale(this.codicePagina, this.codiceTab);
  }

  getLogo(icona: string) {
    if (icona) {
      return 'data:image/png;base64,' + icona;
    }
  }

  selectionChangeHandler(appDaAggiungere: ApplicazioneEsternaConValidita) {
    this.appDaAggiungere = appDaAggiungere;
  }

  aggiungiApplicazione() {
    this.modal.close(this.appDaAggiungere);
  }
}
