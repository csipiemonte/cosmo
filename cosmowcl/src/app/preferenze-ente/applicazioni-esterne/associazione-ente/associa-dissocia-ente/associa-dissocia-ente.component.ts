/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { finalize } from 'rxjs/operators';
import { ApplicazioneEsternaConValidita } from 'src/app/shared/models/api/cosmoauthorization/applicazioneEsternaConValidita';
import { AppEsterneService } from 'src/app/shared/services/app-esterne.service';
import { ModalService } from 'src/app/shared/services/modal.service';

@Component({
  selector: 'app-associa-dissocia-ente',
  templateUrl: './associa-dissocia-ente.component.html',
  styleUrls: ['./associa-dissocia-ente.component.scss']
})
export class AssociaDissociaEnteComponent implements OnInit {

  loading = 0;
  loadingError: any | null = null;

  idApp!: number;

  appDaAssociare!: ApplicazioneEsternaConValidita;

  descrizioneApp!: string;
  iconaApp!: string;
  url: string | null = null;
  descrizioneFunzionalita: string | null = null;
  inizioValidita: string | null = null;
  fineValidita: string | null = null;

  title!: string;
  funzionalitaBool!: boolean;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private modalService: ModalService,
    private translateService: TranslateService,
    private appEsterneService: AppEsterneService) { }

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

    this.appEsterneService.getAppEsternaDaAssociare(this.idApp || 0)
      .pipe(
        finalize(() => {
          this.loading--;
        })
      ).subscribe(response => {
        this.appDaAssociare = response;

        this.funzionalitaBool = this.appDaAssociare.funzionalitaPrincipale ? true : false;
        this.title = this.appDaAssociare.funzionalitaPrincipale ? this.translateService.instant('app_esterne.modifica_applicazione')
          : this.translateService.instant('app_esterne.aggiungi_applicazione');
        this.init();
      }, failure => {
        this.loadingError = failure;
      });
  }

  init() {
    this.descrizioneApp = this.appDaAssociare.descrizione;
    this.iconaApp = this.appDaAssociare.icona;
    this.url = this.appDaAssociare.funzionalitaPrincipale ? this.appDaAssociare.funzionalitaPrincipale.url : null;
    this.descrizioneFunzionalita = this.appDaAssociare.funzionalitaPrincipale
      ? this.appDaAssociare.funzionalitaPrincipale.descrizione : null;
    this.inizioValidita = this.appDaAssociare.campiTecnici && this.appDaAssociare.campiTecnici.dtIniVal
      ? this.appDaAssociare.campiTecnici.dtIniVal : null;
    this.fineValidita = this.appDaAssociare.campiTecnici && this.appDaAssociare.campiTecnici.dtFineVal
      ? this.appDaAssociare.campiTecnici.dtFineVal : null;

  }

  tornaIndietro() {
    this.router.navigate(['back'], { relativeTo: this.route });
  }

  salva(appForm: FormGroup) {
    this.descrizioneFunzionalita = appForm.value.descrizione;
    this.url = appForm.value.url;
    this.inizioValidita = appForm.value.inizioValidita;
    this.fineValidita = appForm.value.fineValidita;

    if (this.appDaAssociare && this.descrizioneFunzionalita && this.url && this.inizioValidita) {
      this.appDaAssociare.campiTecnici = {
        dtIniVal: this.inizioValidita ? new Date(this.inizioValidita).toISOString()
          : new Date().toISOString(),
        dtFineVal: this.fineValidita ? new Date(this.fineValidita).toISOString() : undefined
      };
      this.appDaAssociare.funzionalitaPrincipale = {
        descrizione: this.descrizioneFunzionalita,
        url: this.url,
        principale: true
      };

      this.appEsterneService.associaDissociaAppEnte(this.appDaAssociare.id || 0, this.appDaAssociare).subscribe(
        response => {
          this.appEsterneService.setReloadMenu(true);
          this.tornaIndietro();
        },
        error => {
          this.modalService.error(this.translateService.instant('app_esterne.associazione_applicazioni'),
            this.translateService.instant('errori.errore_associazione_applicazioni'), error.error.errore,
            this.translateService.instant('common.ok'))
            .then(() => { })
            .catch(() => { });
        });
    }
  }

}
