/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { CosmoTableColumnSize, CosmoTableFormatter, ICosmoTableColumn } from 'ngx-cosmo-table';
import { finalize } from 'rxjs/operators';
import { PraticaInRelazione } from '../../models/api/cosmopratiche/praticaInRelazione';
import { ModalService } from '../../services/modal.service';
import { RelazioniPraticheService } from '../../services/relazioni-pratiche.service';
import { DateUtils } from '../../utilities/date-utils';

@Component({
  selector: 'app-pratiche-associate',
  templateUrl: './pratiche-associate.component.html',
  styleUrls: ['./pratiche-associate.component.scss']
})
export class PraticheAssociateComponent implements OnInit {

  @Input() idPratica?: number;
  @Input() isAssociazioneTask = false;

  loading = 0;
  loadingError: any | null = null;
  loaded = false;

  pratiche: PraticaInRelazione[] = [];

  columns: ICosmoTableColumn[] = [
    {
      name: 'tipo_associazione', label: 'Tipo Associazione', canSort: true,
      valueExtractor: row => row.tipoRelazione.descrizione ? row.tipoRelazione.descrizione : '--'
    },
    {
      name: 'dataCreazionePratica', label: 'Apertura pratica', field: 'dataCreazionePratica', canSort: true,
      formatters: [
        { formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy' },
        { format: (raw: any) => raw ?? '--', }
      ],
      valueExtractor: raw => DateUtils.parse(raw.pratica.dataCreazionePratica),
    },
    {
      name: 'oggetto', label: 'Oggetto', field: 'pratica.oggetto', canSort: true,
      canFilter: true, defaultFilter: true, applyTemplate: true
    },
    {
      name: 'tipoPratica', label: 'Tipologia', field: 'pratica.tipo.descrizione', serverField: 'tipologia', canSort: true,
      canHide: false,
    },
    {
      name: 'stato', label: 'Stato', field: 'pratica.stato.descrizione', serverField: 'stato', canSort: true,
      applyTemplate: true
    },
    { name: 'azioni', canHide: false, canSort: false, applyTemplate: true, size: CosmoTableColumnSize.XXS }
  ];

  constructor(
    private router: Router,
    private relazioniPraticheService: RelazioniPraticheService,
    private modalService: ModalService,
    private translateService: TranslateService) { }

  ngOnInit(): void {
    this.refresh();
  }

  refresh() {
    this.loading++;
    this.loadingError = null;

    this.relazioniPraticheService.listRelazioniDellaPratica(this.idPratica ?? 0)
      .pipe(
        finalize(() => {
          this.loading--;
        })
      )
      .subscribe(
        response => { this.pratiche = response; },
        err => this.loadingError = err
      );
  }

  goToDettaglio(row: PraticaInRelazione) {
    if (!row || !row.pratica || !row.pratica.id) {
      return;
    }
    this.router.navigate(['./pratica', row.pratica.id]);
  }

  eliminaRelazione(row: PraticaInRelazione) {
    if (!this.idPratica || !row || !row.pratica || !row.pratica.id || !row.tipoRelazione
      || !row.tipoRelazione.codice || !this.isAssociazioneTask) {
      return;
    }

    this.relazioniPraticheService.creaAggiornaRelazioni(this.idPratica, [row.pratica.id], row.tipoRelazione.codice)
      .subscribe(
        response => {
          this.modalService.info(this.translateService.instant('form_logici.associazioni.associa_pratica'),
            this.translateService.instant('form_logici.associazioni.dissociazione_riuscita'))
            .then(() => { this.refresh(); })
            .catch(() => { });
        },
        error => {
          this.modalService.error(this.translateService.instant('form_logici.associazioni.associa_pratica'),
            this.translateService.instant('errori.dissociazione_pratiche'),
            error.error.errore)
            .then(() => { })
            .catch(() => { });
        }
      );
  }

  isAnnullata(p: any): boolean {
    return !!p.dataCancellazione;
  }

  getBadgeClass(p: PraticaInRelazione) {

    if (this.isAnnullata(p)) {
      return 'danger';
    }

    if (p.pratica.stato?.classe?.length) {
      return p.pratica.stato.classe;
    }

    return 'primary';
  }

  getStatusText(p: PraticaInRelazione) {
    if (this.isAnnullata(p)) {
      return 'Annullata';
    }

    return p.pratica.stato?.descrizione;
  }

}
