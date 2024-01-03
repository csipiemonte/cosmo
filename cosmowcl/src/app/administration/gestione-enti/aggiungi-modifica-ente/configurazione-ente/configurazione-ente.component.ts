/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { CosmoTableColumnSize, CosmoTableComponent, ICosmoTableColumn } from 'ngx-cosmo-table';
import { finalize } from 'rxjs/operators';
import { MessaggioModale } from 'src/app/shared/components/modals/due-opzioni/due-opzioni.component';
import { ConfigurazioneEnte } from 'src/app/shared/models/api/cosmoauthorization/configurazioneEnte';
import { ConfigurazioneEnteService } from 'src/app/shared/services/configurazione-ente.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { AggiungiModificaConfigurazioneEnteModalComponent } from './aggiungi-modifica-configurazione-ente-modal/aggiungi-modifica-configurazione-ente-modal.component';

@Component({
  selector: 'app-configurazione-ente',
  templateUrl: './configurazione-ente.component.html',
  styleUrls: ['./configurazione-ente.component.scss']
})
export class ConfigurazioneEnteComponent implements OnInit {

  loading = 0;
  loadingError: any = null;

  @ViewChild('table') table: CosmoTableComponent | null = null;
  @Input() idEnte?: number;

  columns: ICosmoTableColumn[] = [
    { label: 'Chiave', field: 'chiave', canFilter: true, defaultFilter: true, canSort: true, canHide: false },
    { label: 'Valore', field: 'valore', canFilter: true, defaultFilter: true, canSort: true, canHide: false },
    { label: 'Descrizione', field: 'descrizione', canFilter: true, defaultFilter: true, canSort: true, canHide: false },
    { label: 'Azioni', name: 'azioni', canHide: false, canSort: false, applyTemplate: true, size: CosmoTableColumnSize.XXS }
  ];

  configurazioni: ConfigurazioneEnte[] = [];

  constructor(
    private configurazioneEnteService: ConfigurazioneEnteService,
    private modalService: ModalService,
    private translateService: TranslateService,
    private modal: NgbModal, ) { }

  ngOnInit(): void {
    this.refresh();
  }

  refresh() {
    this.loading++;
    this.loadingError = null;

    this.configurazioneEnteService.getConfigurazioniEnte(this.idEnte)
      .pipe(
        finalize(() => {
          this.loading--;
        })
      )
      .subscribe(response => {
        this.configurazioni = response;
      }, failure => {
        this.loadingError = failure;
      });
  }

  aggiungiConfigurazione() {

    const modalRef = this.modal.open(AggiungiModificaConfigurazioneEnteModalComponent);
    modalRef.componentInstance.idEnte = this.idEnte;

    modalRef.result.then(closed => {
      if (closed === 'OK') {
        this.refresh();
      }
    });
  }

  modificaConfigurazione(row: ConfigurazioneEnte) {
    const modalRef = this.modal.open(AggiungiModificaConfigurazioneEnteModalComponent);
    modalRef.componentInstance.idEnte = this.idEnte;
    modalRef.componentInstance.configurazione = row;

    modalRef.result.then(closed => {
      if (closed === 'OK') {
        this.refresh();
      }
    });
  }

  eliminaConfigurazione(row: ConfigurazioneEnte) {
    let messaggio = this.translateService.instant('common.eliminazione_messaggio');
    messaggio = Utils.parseAndReplacePlaceholders(messaggio, [row.descrizione ?? row.chiave]);

    this.modalService.confermaRifiuta(
      this.translateService.instant('configurazione_ente.eliminazione_configurazione'),
      messaggio
    ).then(
      () => {
        this.configurazioneEnteService.deleteConfigurazioneEnte(row.chiave, this.idEnte)
          .subscribe(response => {
            this.refresh();
          }, error => {
            this.modalService.error(this.translateService.instant('configurazione_ente.eliminazione_configurazione'),
              this.translateService.instant('errori.eliminazione_nuova_configurazione_ente'), error.error.errore)
              .then(() => { })
              .catch(() => { });
          });
      }
    ).catch(() => { });

  }
}
