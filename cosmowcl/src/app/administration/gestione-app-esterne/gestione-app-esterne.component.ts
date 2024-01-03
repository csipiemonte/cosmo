/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { CosmoTableColumnSize, CosmoTableComponent, ICosmoTableColumn } from 'ngx-cosmo-table';
import { finalize } from 'rxjs/operators';
import { MessaggioModale } from 'src/app/shared/components/modals/due-opzioni/due-opzioni.component';
import { ApplicazioneEsternaConValidita } from 'src/app/shared/models/api/cosmoauthorization/applicazioneEsternaConValidita';
import { AppEsterneService } from 'src/app/shared/services/app-esterne.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { Utils } from 'src/app/shared/utilities/utilities';

@Component({
  selector: 'app-gestione-app-esterne',
  templateUrl: './gestione-app-esterne.component.html',
  styleUrls: ['./gestione-app-esterne.component.scss']
})
export class GestioneAppEsterneComponent implements OnInit {

  loading = 0;
  loadingError: any | null = null;
  loaded = false;

  applicazioni: ApplicazioneEsternaConValidita[] = [];

  columns: ICosmoTableColumn[] = [
    { name: 'icona', label: 'Icona', canSort: false, applyTemplate: true, size: CosmoTableColumnSize.XXS },
    {
      name: 'descrizione', label: 'Descrizione', field: 'descrizione', canSort: true, canFilter: true, defaultFilter: true
    },
    {
      name: 'numero_enti_associati', label: 'Numero enti associati', field: 'numEntiAssociati', canSort: false,
    },
    { name: 'azioni', canHide: false, canSort: false, applyTemplate: true, size: CosmoTableColumnSize.XXS }
  ];

  @ViewChild('table') table: CosmoTableComponent | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private appEsterneService: AppEsterneService,
    private modalService: ModalService,
    private translateService: TranslateService,
  ) {
  }

  ngOnInit(): void {
    this.refresh();
  }

  refresh() {

    this.loading++;
    this.loadingError = null;

    this.appEsterneService.getAllApp()
      .pipe(
        finalize(() => {
          this.loading--;
        })
      )
      .subscribe(response => {
        this.applicazioni = response;
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
    this.router.navigate(['nuovo'], { relativeTo: this.route });
  }

  modificaAssociazioneApplicazione(app: ApplicazioneEsternaConValidita) {
    this.router.navigate(['modifica', app.id], { relativeTo: this.route });
  }

  eliminaAssociazioneApplicazione(app: ApplicazioneEsternaConValidita) {
    if (app.associataEnti) {
      const modalText: MessaggioModale[] = [];
      modalText.push({
        testo: this.translateService.instant('app_esterne.app_associata_enti')
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
            this.appEsterneService.eliminaApplicazione(app.id || 0).subscribe(response => {
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
            this.appEsterneService.eliminaApplicazione(app.id || 0).subscribe(response => {
              this.refresh();
              this.appEsterneService.setReloadMenu(true);
            });
          }
        ).catch(() => { });

    }
  }
}
