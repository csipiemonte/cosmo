/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Injector, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { GestioneTipiPraticheService } from 'src/app/administration/gestione-tipi-pratiche/gestione-tipi-pratiche.service';
import { SelezionaPraticaModalComponent } from 'src/app/shared/components/modals/seleziona-pratica-modal/seleziona-pratica-modal.component';
import { PraticheAssociateComponent } from 'src/app/shared/components/pratiche-associate/pratiche-associate.component';
import { Constants } from 'src/app/shared/constants/constants';
import { TipoRelazionePraticaPratica } from 'src/app/shared/models/api/cosmopratiche/tipoRelazionePraticaPratica';
import { MessaggioControlliObbligatori, TipoMessaggio } from 'src/app/shared/models/messaggi-controlli-obbligatori/messaggio-controlli-obbligatori';
import { ModalService } from 'src/app/shared/services/modal.service';
import { RelazioniPraticheService } from 'src/app/shared/services/relazioni-pratiche.service';
import { FunzionalitaParentComponent } from '../funzionalita-parent.component';
import { TipologiaStatiPratica } from '../models/tipologie-stati-pratica.model';
import { HelperService } from 'src/app/shared/services/helper.service';

@Component({
  selector: 'app-associazione-pratiche',
  templateUrl: './associazione-pratiche.component.html',
  styleUrls: ['./associazione-pratiche.component.scss']
})
export class AssociazionePraticheComponent extends FunzionalitaParentComponent implements OnInit {


  @ViewChild('praticheAssociate') praticheAssociate: PraticheAssociateComponent | null = null;

  idPratica!: number;
  tipiRelazionePratica: TipoRelazionePraticaPratica[] = [];

  tipologieStatiPraticaDaAssociare: TipologiaStatiPratica[] = [];
  tipiAssociazione: string[] = [];
  defaultTipoAssociazione!: string;
  descrizioneInfo!: string;
  messaggiErroriConfigurazione: MessaggioControlliObbligatori[] = [];

  constructor(
    public injector: Injector,
    private modal: NgbModal,
    private relazioniPraticheService: RelazioniPraticheService,
    private gestioneTipiPraticheService: GestioneTipiPraticheService,
    private modalService: ModalService,
    private translateService: TranslateService,
    public helperService: HelperService,
    public route: ActivatedRoute) {
    super(injector);

    if (this.parametri && this.parametri.get(Constants.FORM_LOGICI.TIPOLOGIE_STATI_PRATICA_DA_ASSOCIARE)) {
      const tipologiePraticaDaAssociare = JSON.parse(this.parametri
        .get(Constants.FORM_LOGICI.TIPOLOGIE_STATI_PRATICA_DA_ASSOCIARE) as string);

      tipologiePraticaDaAssociare?.tipologieStatiPratica.forEach((tipologiaStato: TipologiaStatiPratica) => {
        this.tipologieStatiPraticaDaAssociare.push(tipologiaStato);
      });

      tipologiePraticaDaAssociare?.tipiAssociazione.forEach((tipoAssociazione: string) => {
        this.tipiAssociazione.push(tipoAssociazione);
      });

      if (tipologiePraticaDaAssociare && tipologiePraticaDaAssociare.descrizioneInfo) {
        this.descrizioneInfo = tipologiePraticaDaAssociare.descrizioneInfo;
      }

      if (tipologiePraticaDaAssociare && tipologiePraticaDaAssociare.defaultTipoAssociazione) {
        this.defaultTipoAssociazione = tipologiePraticaDaAssociare.defaultTipoAssociazione;
      }
    }
  }

  ngOnInit(): void {
    if (this.pratica && this.pratica.id) {
      this.idPratica = this.pratica.id;
    }

    this.relazioniPraticheService.listTipiRelazioniPratica().subscribe(
      response => this.tipiRelazionePratica = response
    );
  }

  cercaPratiche() {
    if (!this.pratica || !this.tipiRelazionePratica || this.tipiRelazionePratica.length === 0) {
      return;
    }

    this.messaggiErroriConfigurazione = [];

    const ngbModalOptions: NgbModalOptions = {
      backdrop: true,
      keyboard: true,
      size: 'xl',
    };

    this.checkTipiStatiPratica();

    if (this.tipiAssociazione.length > 0) {
      this.tipiAssociazione.every(item => {
        const found = this.tipiRelazionePratica.find(tipo => tipo.codice === item);
        if (!found) {
          this.messaggiErroriConfigurazione.push({ messaggio:
            this.translateService.instant('errori.tipi_associazione_selezionabili'), tipoMessaggio: TipoMessaggio.WARNING });
          return false;
        }
        return true;
      });

      const filtered = this.tipiRelazionePratica.filter(item => this.tipiAssociazione.includes(item.codice));
      if (filtered) {
        this.tipiRelazionePratica = filtered;
      }
    }

    if (this.defaultTipoAssociazione) {
      const found = this.tipiRelazionePratica.find(item => item.codice === this.defaultTipoAssociazione);
      if (!found) {
        this.messaggiErroriConfigurazione.push({ messaggio:
          this.translateService.instant('errori.tipi_associazione_default'), tipoMessaggio: TipoMessaggio.WARNING });
      }
    }

    const modalRef = this.modal.open(SelezionaPraticaModalComponent, ngbModalOptions);
    const data = this.helperService.searchHelperRef(this.route);
    modalRef.componentInstance.idPratica = this.pratica.id;
    modalRef.componentInstance.tipiRelazionePratica = this.tipiRelazionePratica;
    modalRef.componentInstance.tipoRicerca = 'associazione';
    modalRef.componentInstance.tipologieStatiPraticaDaAssociare = this.tipologieStatiPraticaDaAssociare;
    modalRef.componentInstance.descrizioneInfo = this.descrizioneInfo;
    modalRef.componentInstance.defaultTipoAssociazione = this.defaultTipoAssociazione;
    modalRef.componentInstance.messaggiErroriConfigurazione = this.messaggiErroriConfigurazione;
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = 'seleziona-pratica';

    modalRef.result.then((close) => {

      if (close.esitoRisultato === Constants.OK_MODAL) {
        this.associaPratiche(close);
      }
    });
  }

  associaPratiche(close: any) {

    if (!close || !close.praticheDaAssociare || close.praticheDaAssociare.length === 0
      || !close.tipoRelazionePratica || !close.tipoRelazionePratica.codice) {
      this.modalService.error(this.translateService.instant('form_logici.associazioni.associa_pratica'),
        this.translateService.instant('errori.associazione_pratiche'))
        .then(() => { })
        .catch(() => { });

    } else {
      this.relazioniPraticheService
        .creaAggiornaRelazioni(this.idPratica, close.praticheDaAssociare, close.tipoRelazionePratica.codice)
        .subscribe(
          response => {
            this.modalService.info(this.translateService.instant('form_logici.associazioni.associa_pratica'),
              this.translateService.instant('form_logici.associazioni.associazione_riuscita'))
              .then(() => {
                if (this.praticheAssociate){
                  this.praticheAssociate.refresh();
                }
              });
          },
          error => {
            this.modalService.error(this.translateService.instant('form_logici.associazioni.associa_pratica'),
              this.translateService.instant('errori.associazione_pratiche'),
              error.error.errore)
              .then(() => { })
              .catch(() => { });
          }
        );
    }

  }

  checkTipiStatiPratica() {
    this.gestioneTipiPraticheService.listTipiPraticaByEnte().subscribe(tipi => {

      if (tipi && tipi.length > 0) {

        this.tipologieStatiPraticaDaAssociare.forEach(tipologiaStati => {
          const foundTipologia = tipi.find(item => item.codice === tipologiaStati.tipologia);
          if (foundTipologia) {

            if (!this.messaggiErroriConfigurazione
                .find(item => item.messaggio === this.translateService.instant('errori.stati_pratiche_associabili'))) {
              this.praticheService.listStatiPraticaByTipo(foundTipologia.codice).subscribe(stati => {

                tipologiaStati.stati.every(stato => {
                  const foundStato = stati.find(item => item.codice === stato);
                  if (!foundStato) {
                    this.messaggiErroriConfigurazione.push({ messaggio:
                      this.translateService.instant('errori.stati_pratiche_associabili'), tipoMessaggio: TipoMessaggio.WARNING });
                    return false;
                  }
                  return true;
                });
              });
            }

          } else {
            if (!this.messaggiErroriConfigurazione
                .find(item => item.messaggio === this.translateService.instant('errori.tipologie_pratiche_associabili'))) {

              this.messaggiErroriConfigurazione.push({ messaggio:
                this.translateService.instant('errori.tipologie_pratiche_associabili'), tipoMessaggio: TipoMessaggio.WARNING });
            }
          }
        });
      }
    });
  }
}
