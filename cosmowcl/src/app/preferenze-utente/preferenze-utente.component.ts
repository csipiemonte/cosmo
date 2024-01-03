/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { DatePipe } from '@angular/common';
import {
  Component,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import {
  ActivatedRoute,
  Router,
} from '@angular/router';

import {
  CosmoTableColumnSize,
  CosmoTableComponent,
  CosmoTableFormatter,
  ICosmoTableColumn,
} from 'ngx-cosmo-table';
import { Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { ModalService } from 'src/app/shared/services/modal.service';

import { TranslateService } from '@ngx-translate/core';

import {
  CertificatiService,
} from '../shared/components/certificati-firma/certificati/certificati.service';
import { TipoSegnalibroEnum } from '../shared/enums/tipo-segnalibro';
import { Preferenza } from '../shared/models/api/cosmo/preferenza';
import {
  CertificatoFirma,
} from '../shared/models/api/cosmoauthorization/certificatoFirma';
import {
  TipoSegnalibro,
} from '../shared/models/api/cosmoauthorization/tipoSegnalibro';
import { IdentitaUtente } from '../shared/models/api/cosmoecm/identitaUtente';
import {
  ValorePreferenzeUtente,
} from '../shared/models/preferenze/valore-preferenze-utente.model';
import {
  PreferenzeUtenteService,
} from '../shared/services/preferenze-utente.service';
import { SegnalibroService } from '../shared/services/segnalibro.service';
import { Utils } from '../shared/utilities/utilities';
import { ActiveToast, IndividualConfig, ToastrService } from 'ngx-toastr';
import { Title } from '@angular/platform-browser';
import { DateUtils } from '../shared/utilities/date-utils';

@Component({
  selector: 'app-preferenze-utente',
  templateUrl: './preferenze-utente.component.html',
  styleUrls: ['./preferenze-utente.component.scss']
})
export class PreferenzeUtenteComponent implements OnInit, OnDestroy {

  private preferenzeUtenteSubscription: Subscription | null = null;

  loading = 0;
  loadingError: any | null = null;
  loadingCertificatiError: any | null = null;

  certificatiFirmaServer!: CertificatoFirma[];
  certificatiFirma!: CertificatoFirma[];

  certificatoQuery: string | null = null;

  all = true;
  creaTask = true;
  annullaTask = true;
  assegnaTask = true;
  commento = true;
  condividiPratica = true;
  smistamentoDocumenti = true;
  elaborazioneDocumenti = true;

  allEmail = true;
  creaTaskEmail = true;
  annullaTaskEmail = true;
  assegnaTaskEmail = true;
  commentoEmail = true;
  condividiPraticaEmail = true;
  smistamentoDocumentiEmail = true;
  elaborazioneDocumentiEmail = true;

  preferenzeUtente!: Preferenza;
  segnalibri?: [{ tipo?: string; nome?: string; segnalibro?: any; }];
  private actaSegnalibro?: TipoSegnalibro;



  posizioneToast?: string;

  @ViewChild('table') table: CosmoTableComponent | null = null;

  certificatiColumns: ICosmoTableColumn[] = [
    {
      name: 'descrizione', label: 'Descrizione', field: 'descrizione', canSort: true, canFilter: true, defaultFilter: true
    },
    {
      name: 'ente certificatore', label: 'Ente certificatore', field: 'enteCertificatore.descrizione', canSort: true
    },
    {
      name: 'tipo credenziali', label: 'Tipo credenziali', field: 'tipoCredenzialiFirma.descrizione', canSort: true
    },
    {
      name: 'data scadenza', label: 'Data scadenza', canSort: true,
      formatters: [{
        formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy',
       }, {
        format: (raw: any) => raw ?? '--',
      }],
      valueExtractor: row => DateUtils.parse(row.dataScadenza)
    },
    { name: 'azioni', canHide: false, canSort: false, applyTemplate: true, size: CosmoTableColumnSize.XXS }
  ];

  segnalibriColumns: ICosmoTableColumn[] = [
    {
      name: 'tipo', label: 'Tipo', field: 'tipo', canSort: true, canHide: false,
    },
    {
      name: 'nome', label: 'Nome', field: 'nome', canSort: true, canHide: false,
    },
    {
      name: 'segnalibro', label: 'Segnalibro', canSort: true, canHide: false,
      valueExtractor: row => this.setSegnalibroValue(row),
    },
    { name: 'azioni', canHide: false, canSort: false, applyTemplate: true, size: CosmoTableColumnSize.XXS }
  ];

  private setSegnalibroValue(row: { tipo?: string; nome?: string; segnalibro?: any; }) {
    if (row.tipo === this.actaSegnalibro?.descrizione) {
      const acta = row.segnalibro as IdentitaUtente;
      return this.setActaSegnalibroValue(acta);
    }
    return '--';
  }

  private setActaSegnalibroValue(segnalibro: IdentitaUtente): string {
    if (segnalibro) {
      return segnalibro.codiceNodo + '; ' + segnalibro.codiceStruttura + '; ' + segnalibro.codiceAOO;
    }
    return '--';
  }

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private datePipe: DatePipe,
    private toastrService: ToastrService,
    private translateService: TranslateService,
    private modalService: ModalService,
    private certificatiService: CertificatiService,
    private preferenzeUtenteService: PreferenzeUtenteService,
    private segnalibroService: SegnalibroService) {
  }

  ngOnInit(): void {
    this.segnalibroService.getTipiSegnalibro().subscribe(response => {
      this.actaSegnalibro = response.tipiSegnalibro?.find(tipo => tipo.codice === TipoSegnalibroEnum.COLLOCAZIONE_ACTA);
    });

    this.preferenzeUtenteSubscription = this.preferenzeUtenteService.subscribePreferenze.subscribe(preferenze => {
      this.preferenzeUtente = preferenze;
      this.setPreferenze();
      this.refresh();
    });
  }

  ngOnDestroy(): void {
    if (this.preferenzeUtenteSubscription) {
      this.preferenzeUtenteSubscription.unsubscribe();
    }
  }

  private setPreferenze() {
    if (this.preferenzeUtente && this.preferenzeUtente.valore) {
      const valorePreferenzeUtente = JSON.parse(this.preferenzeUtente.valore) as ValorePreferenzeUtente;
      this.setNotifiche(valorePreferenzeUtente);
      this.setSegnalibri(valorePreferenzeUtente);
      this.setPosizioneToast(valorePreferenzeUtente);
    }
  }

  private setNotifiche(valorePreferenzeUtente: ValorePreferenzeUtente) {
    if (valorePreferenzeUtente && valorePreferenzeUtente.ricezioneNotifiche) {
      this.all = valorePreferenzeUtente.ricezioneNotifiche.cosmo.all ?? true;
      this.creaTask = valorePreferenzeUtente.ricezioneNotifiche.cosmo.creaTask ?? true;
      this.annullaTask = valorePreferenzeUtente.ricezioneNotifiche.cosmo.annullaTask ?? true;
      this.assegnaTask = valorePreferenzeUtente.ricezioneNotifiche.cosmo.assegnaTask ?? true;
      this.commento = valorePreferenzeUtente.ricezioneNotifiche.cosmo.commento ?? true;
      this.condividiPratica = valorePreferenzeUtente.ricezioneNotifiche.cosmo.condividiPratica ?? true;
      this.smistamentoDocumenti = valorePreferenzeUtente.ricezioneNotifiche.cosmo.smistamentoDocumenti ?? true;
      this.elaborazioneDocumenti = valorePreferenzeUtente.ricezioneNotifiche.cosmo.elaborazioneDocumenti ?? true;
    } else {
      this.all = true;
      this.creaTask = true;
      this.annullaTask = true;
      this.assegnaTask = true;
      this.commento = true;
      this.condividiPratica = true;
      this.smistamentoDocumenti = true;
      this.elaborazioneDocumenti = true;
    }
    if (valorePreferenzeUtente && valorePreferenzeUtente.ricezioneNotifiche?.email) {
      this.allEmail = valorePreferenzeUtente.ricezioneNotifiche.email?.all ?? true;
      this.creaTaskEmail = valorePreferenzeUtente.ricezioneNotifiche.email?.creaTask ?? true;
      this.annullaTaskEmail = valorePreferenzeUtente.ricezioneNotifiche.email?.annullaTask ?? true;
      this.assegnaTaskEmail = valorePreferenzeUtente.ricezioneNotifiche.email?.assegnaTask ?? true;
      this.commentoEmail = valorePreferenzeUtente.ricezioneNotifiche.email?.commento ?? true;
      this.condividiPraticaEmail = valorePreferenzeUtente.ricezioneNotifiche.email?.condividiPratica ?? true;
      this.smistamentoDocumentiEmail = valorePreferenzeUtente.ricezioneNotifiche.email?.smistamentoDocumenti ?? true;
      this.elaborazioneDocumentiEmail = valorePreferenzeUtente.ricezioneNotifiche.email?.elaborazioneDocumenti ?? true;
    } else {
      this.allEmail = true;
      this.creaTaskEmail = true;
      this.annullaTaskEmail = true;
      this.assegnaTaskEmail = true;
      this.commentoEmail = true;
      this.condividiPraticaEmail = true;
      this.smistamentoDocumentiEmail = true;
      this.elaborazioneDocumentiEmail = true;
    }
  }

  private setSegnalibri(valorePreferenzeUtente: ValorePreferenzeUtente) {
    if (valorePreferenzeUtente && valorePreferenzeUtente.segnalibri) {
      this.segnalibri = valorePreferenzeUtente.segnalibri;
    }
  }

  private setPosizioneToast(valorePreferenzeUtente: ValorePreferenzeUtente) {
    this.posizioneToast = valorePreferenzeUtente.posizioneToast;
  }

  refresh() {
    this.loading++;
    // this.loadingError = null;
    this.loadingCertificatiError = null;

    this.certificatiService.getCertificati()
      .pipe(
        finalize(() => {
          this.loading--;
        })
      )
      .subscribe(response => {
        this.certificatiFirmaServer = response;
        this.certificatiFirma = response;
      }, failure => {
        // this.loadingError = failure;
        this.loadingCertificatiError = failure;
      });

  }

  salvaRicezioneNotifiche() {
    if (this.preferenzeUtente && this.preferenzeUtente.valore) {

      const valorePreferenzeUtente = JSON.parse(this.preferenzeUtente.valore) as ValorePreferenzeUtente;

      if (valorePreferenzeUtente) {
        if (valorePreferenzeUtente.ricezioneNotifiche) {

          valorePreferenzeUtente.ricezioneNotifiche.email = {
            all: this.allEmail ?? undefined,
            creaTask: this.creaTaskEmail ?? undefined,
            assegnaTask: this.assegnaTaskEmail ?? undefined,
            annullaTask: this.annullaTaskEmail ?? undefined,
            commento: this.commentoEmail ?? undefined,
            condividiPratica: this.condividiPraticaEmail ?? undefined,
            smistamentoDocumenti: this.smistamentoDocumentiEmail ?? undefined,
            elaborazioneDocumenti: this.elaborazioneDocumentiEmail ?? undefined
          };

          valorePreferenzeUtente.ricezioneNotifiche.cosmo = {
            all: this.all ?? undefined,
            creaTask: this.creaTask ?? undefined,
            assegnaTask: this.assegnaTask ?? undefined,
            annullaTask: this.annullaTask ?? undefined,
            commento: this.commento ?? undefined,
            condividiPratica: this.condividiPratica ?? undefined,
            smistamentoDocumenti: this.smistamentoDocumenti ?? undefined,
            elaborazioneDocumenti: this.elaborazioneDocumenti ?? undefined
          };

        } else {
          valorePreferenzeUtente.ricezioneNotifiche = {
            email: {
              all: this.allEmail ?? undefined,
              creaTask: this.creaTaskEmail ?? undefined,
              assegnaTask: this.assegnaTaskEmail ?? undefined,
              annullaTask: this.annullaTaskEmail ?? undefined,
              commento: this.commentoEmail ?? undefined,
              condividiPratica: this.condividiPraticaEmail ?? undefined,
              smistamentoDocumenti: this.smistamentoDocumentiEmail ?? undefined,
              elaborazioneDocumenti: this.elaborazioneDocumentiEmail ?? undefined
            },
            cosmo: {
              all: this.all ?? undefined,
              creaTask: this.creaTask ?? undefined,
              assegnaTask: this.assegnaTask ?? undefined,
              annullaTask: this.annullaTask ?? undefined,
              commento: this.commento ?? undefined,
              condividiPratica: this.condividiPratica ?? undefined,
              smistamentoDocumenti: this.smistamentoDocumenti ?? undefined,
              elaborazioneDocumenti: this.elaborazioneDocumenti ?? undefined
            }
          };
        }
        this.preferenzeUtente.valore = JSON.stringify(valorePreferenzeUtente);
        this.preferenzeUtenteService.aggiorna(this.preferenzeUtente);
      }
    } else {
      const ricezioneNotifiche = {
        cosmo: {
          all: this.all ?? undefined,
          creaTask: this.creaTask ?? undefined,
          assegnaTask: this.assegnaTask ?? undefined,
          annullaTask: this.annullaTask ?? undefined,
          commento: this.commento ?? undefined,
          condividiPratica: this.condividiPratica ?? undefined,
          smistamentoDocumenti: this.smistamentoDocumenti ?? undefined,
          elaborazioneDocumenti: this.elaborazioneDocumenti ?? undefined
        },
        email: {
          all: this.allEmail ?? undefined,
          creaTask: this.creaTaskEmail ?? undefined,
          assegnaTask: this.assegnaTaskEmail ?? undefined,
          annullaTask: this.annullaTaskEmail ?? undefined,
          commento: this.commentoEmail ?? undefined,
          condividiPratica: this.condividiPraticaEmail ?? undefined,
          smistamentoDocumenti: this.smistamentoDocumentiEmail ?? undefined,
          elaborazioneDocumenti: this.elaborazioneDocumentiEmail ?? undefined
        }
      };
      this.preferenzeUtente = {
        versione: '1.0',
        valore: JSON.stringify({ ricezioneNotifiche })
      };
      this.preferenzeUtenteService.salva(this.preferenzeUtente);
    }
  }

  resetRicezioneNotifiche() {
    if (this.preferenzeUtente && this.preferenzeUtente.valore) {
      const valorePreferenzeUtente = JSON.parse(this.preferenzeUtente.valore) as ValorePreferenzeUtente;
      this.setNotifiche(valorePreferenzeUtente);
    } else{
      this.posizioneToast = undefined;
    }
  }


  salvaPosizioneToast() {
    if (this.preferenzeUtente && this.preferenzeUtente.valore) {

      const valorePreferenzeUtente = JSON.parse(this.preferenzeUtente.valore) as ValorePreferenzeUtente;

      if (valorePreferenzeUtente) {
        valorePreferenzeUtente.posizioneToast = this.posizioneToast;

        this.preferenzeUtente.valore = JSON.stringify(valorePreferenzeUtente);
        this.preferenzeUtenteService.aggiorna(this.preferenzeUtente);
      }
    } else {
      if (this.posizioneToast){
        const posizioneToast = this.posizioneToast;

        this.preferenzeUtente = {
          versione: '1.0',
          valore: JSON.stringify({ posizioneToast })
        };
        this.preferenzeUtenteService.salva(this.preferenzeUtente);
      }

    }
  }

  resetPosizioneToast() {
    if (this.preferenzeUtente && this.preferenzeUtente.valore) {
      const valorePreferenzeUtente = JSON.parse(this.preferenzeUtente.valore) as ValorePreferenzeUtente;
      this.setPosizioneToast(valorePreferenzeUtente);
    }
  }

  cancellaRicerca() {
    this.certificatoQuery = null;
    this.certificatiFirma = this.certificatiFirmaServer;
  }

  ricercaCertificati(): CertificatoFirma[] {
    if (!this.certificatiFirma) { return []; }
    if (!this.certificatoQuery) { return this.certificatiFirma; }

    return this.certificatiFirma = this.certificatiFirmaServer.filter(certificatoFirma => {
      if (this.certificatoQuery) {
        return (certificatoFirma.descrizione.toLowerCase().indexOf(this.certificatoQuery.toLowerCase()) !== -1);
      }
    });
  }

  aggiungiCertificato() {
    this.router.navigate(['nuovo'], { relativeTo: this.route });
  }

  modificaCertificato(row: CertificatoFirma) {
    this.router.navigate(['modifica', row.id], { relativeTo: this.route });
  }

  eliminaCertificato(row: CertificatoFirma) {

    if (!row.id) {
      throw Error('Certificato non eliminabile');
    }

    let messaggio = this.translateService.instant('common.eliminazione_messaggio');
    messaggio = Utils.parseAndReplacePlaceholders(messaggio, [row.descrizione]);

    this.modalService.confermaRifiuta(
      this.translateService.instant('firma_digitale.eliminazione_certificato'),
      messaggio
    ).then(proceed => {

      this.certificatiService.deleteCertificato(row.id || 0).subscribe(
        response => {
          this.modalService.info(
            this.translateService.instant('firma_digitale.eliminazione_certificato'),
            this.translateService.instant('firma_digitale.eliminazione_certificato_msg'))
            .then(() => {
              this.refresh();
            }
            ).catch(() => { });

        },
        error => {

          const messaggioErrore = Utils.parseAndReplacePlaceholders(
            this.translateService.instant('errori.eliminazione_certificazione_msg'),
            [row.descrizione]);

          this.modalService.error(this.translateService.instant('firma_digitale.eliminazione_certificato'),
            messaggioErrore, error.error.errore)
            .then(() => { })
            .catch(() => { });
        });


    })
      .catch(() => { });

  }

  eliminaSegnalibro(row: { tipo?: string; nome?: string; segnalibro?: any; }) {
    if (this.preferenzeUtente && this.preferenzeUtente.valore) {
      const valorePreferenzeUtente = JSON.parse(this.preferenzeUtente.valore) as ValorePreferenzeUtente;

      if (valorePreferenzeUtente.segnalibri) {
        const found = valorePreferenzeUtente.segnalibri.find(segnalibro => segnalibro.nome === row.nome && segnalibro.tipo === row.tipo);
        if (found) {
          const index: number = valorePreferenzeUtente.segnalibri.indexOf(found);
          if (index !== -1) {
            valorePreferenzeUtente.segnalibri.splice(index, 1);
            this.preferenzeUtente.valore = JSON.stringify(valorePreferenzeUtente);
            this.preferenzeUtenteService.aggiorna(this.preferenzeUtente);
          }
        }
      }
    }
  }

  turnThemAllOn(id: string, masterValue: boolean) {
    if (masterValue) {
      if (id === 'toggleAll') {
        this.creaTask = true;
        this.assegnaTask = true;
        this.annullaTask = true;
        this.commento = true;
        this.condividiPratica = true;
        this.smistamentoDocumenti = true;
        this.elaborazioneDocumenti = true;
      } else if (id === 'toggleAllEmail') {
        this.creaTaskEmail = true;
        this.assegnaTaskEmail = true;
        this.annullaTaskEmail = true;
        this.commentoEmail = true;
        this.condividiPraticaEmail = true;
        this.smistamentoDocumentiEmail = true;
        this.elaborazioneDocumentiEmail = true;
      }
    }
  }

  turnThemAllVarCosmo() {
      if (this.creaTask && this.assegnaTask && this.annullaTask && this.commento && this.condividiPratica
        && this.smistamentoDocumenti && this.elaborazioneDocumenti) {
        this.all = true;
      }else{
        this.all = false;
      }
  }

  turnThemAllVarEmailCosmo() {
    if (this.creaTaskEmail && this.assegnaTaskEmail && this.annullaTaskEmail && this.commentoEmail && this.condividiPraticaEmail
       && this.smistamentoDocumentiEmail && this.elaborazioneDocumentiEmail) {
      this.allEmail = true;
    }else{
      this.allEmail = false;
    }
}

  setPosition(position: string){
    this.posizioneToast = position;

    this.toastrService.clear();

    let positionClass = '';
    let message = '';
    switch (position) {
      case 'TOP-LEFT':
        positionClass = 'toast-top-left';
        message = this.translateService.instant('preferenze.posizione_toast.top_left');
        break;
      case 'TOP-RIGHT':
        positionClass = 'toast-top-right';
        message = this.translateService.instant('preferenze.posizione_toast.top_right');
        break;
      case 'BOTTOM-LEFT':
        positionClass = 'toast-bottom-left';
        message = this.translateService.instant('preferenze.posizione_toast.bottom_left');
        break;
      case 'BOTTOM-RIGHT':
        positionClass = 'toast-bottom-right';
        message = this.translateService.instant('preferenze.posizione_toast.bottom_right');
        break;
      default:
        positionClass = 'toast-top-right';
        message = this.translateService.instant('preferenze.posizione_toast.top_left');
        break;
    }

    this.toastrService.info(message, this.translateService.instant('preferenze.posizione_toast.esempio_notifica'), {
      timeOut: 3000,
      disableTimeOut: false,
      tapToDismiss: true,
      positionClass
    });
  }
}
