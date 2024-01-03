/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { DatePipe } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { CosmoTableColumnSize, CosmoTableComponent, CosmoTableFormatter, ICosmoTableColumn } from 'ngx-cosmo-table';
import { forkJoin } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { MessaggioModale } from 'src/app/shared/components/modals/due-opzioni/due-opzioni.component';
import { ApplicazioneEsterna } from 'src/app/shared/models/api/cosmoauthorization/applicazioneEsterna';
import { FunzionalitaApplicazioneEsterna } from 'src/app/shared/models/api/cosmoauthorization/funzionalitaApplicazioneEsterna';
import { FunzionalitaApplicazioneEsternaConValidita } from 'src/app/shared/models/api/cosmoauthorization/funzionalitaApplicazioneEsternaConValidita';
import { AppEsterneService } from 'src/app/shared/services/app-esterne.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { DateUtils } from 'src/app/shared/utilities/date-utils';
import { Utils } from 'src/app/shared/utilities/utilities';

@Component({
  selector: 'app-funzionalita',
  templateUrl: './funzionalita.component.html',
  styleUrls: ['./funzionalita.component.scss']
})
export class FunzionalitaComponent implements OnInit {


  loading = 0;
  loadingError: any | null = null;

  @ViewChild('table') table: CosmoTableComponent | null = null;

  idApp = 0;
  funzionalita: FunzionalitaApplicazioneEsternaConValidita[] = [];
  funzionalitaPrincipale: FunzionalitaApplicazioneEsterna | undefined;

  columns: ICosmoTableColumn[] = [
    {
      name: 'descrizione', label: 'Descrizione', field: 'descrizione', canSort: true, canFilter: true, defaultFilter: true
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
    private modalService: ModalService,
    private translateService: TranslateService,
    private appEsterneService: AppEsterneService,
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(param => {
      if (param.idApp) {
        this.idApp = +param.idApp;

        this.refresh();

      }
    });
  }

  refresh() {
    this.loading++;
    this.loadingError = null;

    forkJoin({
      funzionalita: this.appEsterneService.getFunzionalita(this.idApp),
      appEsterna: this.appEsterneService.getAppEsternaDaAssociare(this.idApp)
    })
      .pipe(
        finalize(() => {
          this.loading--;
        })
      ).subscribe(response => {
        this.funzionalita = response.funzionalita;
        this.funzionalitaPrincipale = response.appEsterna.funzionalitaPrincipale;
      }, failure => {
        this.loadingError = failure;
      });
  }

  tornaIndietro() {
    this.router.navigate(['back'], { relativeTo: this.route });
  }

  aggiungiFunzionalita() {
    this.router.navigate(['nuova'], { relativeTo: this.route });

  }

  modificaFunzionalita(funzionalita: FunzionalitaApplicazioneEsternaConValidita) {
    this.router.navigate([funzionalita.id], { relativeTo: this.route });

  }

  eliminaFunzionalita(funzionalita: FunzionalitaApplicazioneEsternaConValidita) {
    if (funzionalita.associataUtenti) {
      const modalText: MessaggioModale[] = [];
      modalText.push({
        testo: this.translateService.instant('app_esterne.funzionalita_associata_utenti')
      });

      this.modalService.scegli(
        this.translateService.instant('app_esterne.elimina_funzionalita'),
        modalText,
        [
          { testo: this.translateService.instant('common.si'), valore: 'primoBottone', classe: 'btn-outline-primary' },
          { testo: this.translateService.instant('common.no'), valore: 'secondoBottone', classe: 'btn-primary', defaultFocus: true }
        ])
        .then(proceed => {
          if ('primoBottone' === proceed) {
            this.appEsterneService.eliminaFunzionalita(this.idApp, funzionalita.id || 0).subscribe(response => {
              this.refresh();
              this.appEsterneService.setReloadMenu(true);
            });
          }
        })
        .catch(() => { });
    } else {
      let messaggio = this.translateService.instant('common.eliminazione_messaggio');
      messaggio = Utils.parseAndReplacePlaceholders(messaggio, [funzionalita.descrizione]);

      this.modalService.confermaRifiuta(
        this.translateService.instant('app_esterne.elimina_funzionalita'),
        messaggio
      ).then(
        () => {
          this.appEsterneService.eliminaFunzionalita(this.idApp, funzionalita.id || 0).subscribe(response => {
            this.refresh();
            this.appEsterneService.setReloadMenu(true);
          });
        }
      ).catch(() => { });

    }
  }
}
