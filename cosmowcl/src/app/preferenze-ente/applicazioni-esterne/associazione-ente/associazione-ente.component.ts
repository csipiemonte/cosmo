/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { DatePipe } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { CosmoTableColumnSize, CosmoTableComponent, CosmoTableFormatter, ICosmoTableColumn } from 'ngx-cosmo-table';
import { forkJoin } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { MessaggioModale } from 'src/app/shared/components/modals/due-opzioni/due-opzioni.component';
import { ApplicazioneEsternaConValidita } from 'src/app/shared/models/api/cosmoauthorization/applicazioneEsternaConValidita';
import { AppEsterneService } from 'src/app/shared/services/app-esterne.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { SelezioneApplicazioneComponent } from './selezione-applicazione/selezione-applicazione.component';
import { HelperService } from 'src/app/shared/services/helper.service';
import { DateUtils } from 'src/app/shared/utilities/date-utils';

@Component({
  selector: 'app-associazione-ente',
  templateUrl: './associazione-ente.component.html',
  styleUrls: ['./associazione-ente.component.scss']
})
export class AssociazioneEnteComponent implements OnInit {

  loading = 0;
  loadingError: any | null = null;

  @ViewChild('table') table: CosmoTableComponent | null = null;

  applicazioniOnServer: ApplicazioneEsternaConValidita[] = [];
  applicazioni: ApplicazioneEsternaConValidita[] = [];

  applicazioniNonAssociate: ApplicazioneEsternaConValidita[] = [];

  columns: ICosmoTableColumn[] = [
    { name: 'icona', label: 'Icona', canSort: false, applyTemplate: true, size: CosmoTableColumnSize.XXS },
    {
      name: 'descrizione', label: 'Descrizione', field: 'descrizione', canSort: true, canFilter: true, defaultFilter: true,
      valueExtractor: row => row.funzionalitaPrincipale.descrizione ? row.funzionalitaPrincipale.descrizione : '--'
    },
    {
      name: 'inizio_validita', label: 'Inizio validita\'', canSort: true,
      formatters: [{
        formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy',
       }, {
        format: (raw: any) => raw ?? '--',
      }],
      valueExtractor: row => DateUtils.parse(row.campiTecnici.dtIniVal)
    },
    {
      name: 'fine_validita', label: 'Fine validita\'', canSort: true,
      formatters: [{
        formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy',
       }, {
        format: (raw: any) => raw ?? '--',
      }],
      valueExtractor: row => DateUtils.parse(row.campiTecnici.dtFineVal)
    },
    { name: 'azioni', canHide: false, canSort: false, applyTemplate: true, size: CosmoTableColumnSize.XXS }
  ];

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private datePipe: DatePipe,
    private modal: NgbModal,
    private modalService: ModalService,
    private translateService: TranslateService,
    private appEsterneService: AppEsterneService,
    private helperService: HelperService,
  ) { }

  ngOnInit(): void {
    this.refresh();
  }

  refresh() {

    this.loading++;
    this.loadingError = null;

    forkJoin({
      appAssociate: this.appEsterneService.getAppAssociateEnte(true),
      appNonAssociate: this.appEsterneService.getAppAssociateEnte(false)
    })
      .pipe(
        finalize(() => {
          this.loading--;
        })
      )
      .subscribe(response => {

        this.applicazioniOnServer = response.appAssociate.slice();
        this.applicazioni = response.appAssociate;
        this.applicazioniNonAssociate = response.appNonAssociate;

      }, failure => {
        this.loadingError = failure;
      });
  }

  getLogo(icona: string) {
    if (icona) {
      return 'data:image/png;base64,' + icona;
    }
  }

  tornaIndietro() {
    this.router.navigate(['back'], { relativeTo: this.route });
  }

  aggiungiApplicazione() {
    const data = this.helperService.searchHelperRef(this.route);
    const modalRef = this.modal.open(SelezioneApplicazioneComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.appNonAssociate = this.applicazioniNonAssociate;
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = 'aggiungi-applicazione';

    modalRef.result.then(closedResult => {
      if (closedResult && closedResult.length === 1 && closedResult[0].id) {
        this.router.navigate(['associa-dissocia', closedResult[0].id], { relativeTo: this.route });
      }
    });
  }

  modificaAssociazioneApplicazione(app: ApplicazioneEsternaConValidita) {
    this.router.navigate(['associa-dissocia', app.id], { relativeTo: this.route });
  }

  eliminaAssociazioneApplicazione(app: ApplicazioneEsternaConValidita) {
    if (app.associataUtenti) {
      const modalText: MessaggioModale[] = [];
      modalText.push({
        testo: this.translateService.instant('app_esterne.app_associata_utenti')
      });

      this.modalService.scegli(
        this.translateService.instant('app_esterne.elimina_app'),
        modalText,
        [
          { testo: this.translateService.instant('common.si'), valore: 'primoBottone', classe: 'btn-outline-primary' },
          { testo: this.translateService.instant('common.no'), valore: 'secondoBottone', classe: 'btn-primary', defaultFocus: true }
        ])
        .then(proceed => {
          if ('primoBottone' === proceed) {
            this.appEsterneService.eliminaApplicazioneAssociata(app.id || 0).subscribe(response => {
              this.refresh();
              this.appEsterneService.setReloadMenu(true);
            });
          }
        })
        .catch(() => { });
    } else {
      let messaggio = this.translateService.instant('common.eliminazione_messaggio');
      messaggio = Utils.parseAndReplacePlaceholders(messaggio, [app.descrizione]);

      this.modalService.confermaRifiuta(
        this.translateService.instant('app_esterne.elimina_app'),
        messaggio
      ).then(
          () => {
            this.appEsterneService.eliminaApplicazioneAssociata(app.id || 0).subscribe(response => {
              this.refresh();
              this.appEsterneService.setReloadMenu(true);
            });
          }
        ).catch(() => { });

    }
  }
}

