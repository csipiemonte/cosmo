/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { forkJoin } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { ApplicazioneEsternaConValidita } from 'src/app/shared/models/api/cosmoauthorization/applicazioneEsternaConValidita';
import { FunzionalitaApplicazioneEsternaConValidita } from 'src/app/shared/models/api/cosmoauthorization/funzionalitaApplicazioneEsternaConValidita';
import { AppEsterneService } from 'src/app/shared/services/app-esterne.service';
import { ModalService } from 'src/app/shared/services/modal.service';

@Component({
  selector: 'app-gestisci-funzionalita-non-principale',
  templateUrl: './gestisci-funzionalita-non-principale.component.html',
  styleUrls: ['./gestisci-funzionalita-non-principale.component.scss']
})
export class GestisciFunzionalitaNonPrincipaleComponent implements OnInit {

  loading = 0;
  loadingError: any | null = null;

  idApp!: number;
  idFunzionalita!: number;
  funzionalita!: FunzionalitaApplicazioneEsternaConValidita;
  applicazione!: ApplicazioneEsternaConValidita;

  descrizioneApp!: string;
  iconaApp!: string;
  url: string | null = null;
  descrizioneFunzionalita: string | null = null;
  inizioValidita: string | null = null;
  fineValidita: string | null = null;

  title!: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private modalService: ModalService,
    private translateService: TranslateService,
    private appEsterneService: AppEsterneService) { }

  ngOnInit(): void {

    this.route.params.subscribe(param => {
      this.idApp = param.idApp ? +param.idApp : 0;
      this.idFunzionalita = param.idFunz ? +param.idFunz : 0;

      this.refresh();
    });
  }

  refresh() {
    this.loading++;
    this.loadingError = null;

    if (this.idFunzionalita) {

      this.title = this.translateService.instant('app_esterne.modifica_funzionalita');

      forkJoin({
        applicazione: this.appEsterneService.getAppEsternaDaAssociare(this.idApp),
        funzionalita: this.appEsterneService.getSingolaFunzionalita(this.idApp, this.idFunzionalita)
      })
        .pipe(
          finalize(() => {
            this.loading--;
          })
        )
        .subscribe(response => {
          this.applicazione = response.applicazione;
          this.funzionalita = response.funzionalita;

          this.init();

        }, failure => {
          this.loadingError = failure;
        });
    } else {

      this.title = this.translateService.instant('app_esterne.aggiungi_funzionalita');

      this.appEsterneService.getAppEsternaDaAssociare(this.idApp)
        .pipe(
          finalize(() => {
            this.loading--;
          })
        ).subscribe(response => {
          this.applicazione = response;
          this.init();
        }, failure => {
          this.loadingError = failure;
        });
    }
  }
  init() {
    this.descrizioneApp = this.applicazione.funzionalitaPrincipale?.descrizione ?? '';
    this.iconaApp = this.applicazione.icona;
    this.url = this.funzionalita && this.funzionalita.url ? this.funzionalita.url : null;
    this.descrizioneFunzionalita = this.funzionalita && this.funzionalita.descrizione ? this.funzionalita.descrizione : null;
    this.inizioValidita = this.funzionalita && this.funzionalita.campiTecnici.dtIniVal ? this.funzionalita.campiTecnici.dtIniVal : null;
    this.fineValidita = this.funzionalita && this.funzionalita.campiTecnici.dtFineVal ? this.funzionalita.campiTecnici.dtFineVal : null;
  }

  tornaIndietro() {
    this.router.navigate(['back'], { relativeTo: this.route });
  }

  salva(appForm: FormGroup) {

    this.descrizioneFunzionalita = appForm.value.descrizione;
    this.url = appForm.value.url;
    this.inizioValidita = appForm.value.inizioValidita;
    this.fineValidita = appForm.value.fineValidita;

    if (this.descrizioneFunzionalita && this.url && this.inizioValidita) {
      if (this.idFunzionalita && this.idFunzionalita > 0 && this.funzionalita) {

        this.funzionalita.descrizione = this.descrizioneFunzionalita;
        this.funzionalita.url = this.url;
        this.funzionalita.campiTecnici = {
          dtIniVal: this.inizioValidita ? new Date(this.inizioValidita).toISOString()
            : new Date().toISOString(),
          dtFineVal: this.fineValidita ? new Date(this.fineValidita).toISOString() : undefined
        };

        this.appEsterneService.aggiornaFunzionalita(this.idApp, this.idFunzionalita, this.funzionalita).subscribe(response => {
          this.appEsterneService.setReloadMenu(true);
          this.tornaIndietro();
        }, error => {
          this.modalService.error(this.translateService.instant('app_esterne.modifica_funzionalita'),
            this.translateService.instant('errori.aggiornamento_funzionalita'), error.error.errore,
            this.translateService.instant('common.ok'))
            .then(() => { })
            .catch(() => { });
        }
        );
      } else {
        const funzionalitaNuova: FunzionalitaApplicazioneEsternaConValidita = {
          principale: false,
          descrizione: this.descrizioneFunzionalita,
          url: this.url,
          campiTecnici: {
            dtIniVal: this.inizioValidita ? new Date(this.inizioValidita).toISOString()
              : new Date().toISOString(),
            dtFineVal: this.fineValidita ? new Date(this.fineValidita).toISOString() : undefined
          }
        };

        this.appEsterneService.salvaFunzionalita(this.idApp, funzionalitaNuova).subscribe(
          response => {
            this.appEsterneService.setReloadMenu(true);
            this.tornaIndietro();
          },
          error => {
            this.modalService.error(this.translateService.instant('app_esterne.creazione_funzionalita'),
              this.translateService.instant('errori.creazione_funzionalita'), error.error.errore,
              this.translateService.instant('common.ok'))
              .then(() => { })
              .catch(() => { });
          });
      }
    }

  }

}
