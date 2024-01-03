/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  Component,
  Injector,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';

import { SpinnerVisibilityService } from 'ng-http-loader';
import { NGXLogger } from 'ngx-logger';
import {
  from,
  Observable,
  of,
  Subscription,
} from 'rxjs';
import {
  catchError,
  finalize,
  map,
  mergeMap,
  tap,
} from 'rxjs/operators';
import {
  IdentitaUtenteActaComponent,
} from 'src/app/identita-utente-acta/identita-utente-acta.component';
import {
  RicercaActaComponent,
} from 'src/app/ricerca-acta/ricerca-acta.component';
import { RicercaActaService } from 'src/app/ricerca-acta/ricerca-acta.service';
import {
  AggiungiDocumentoComponent,
} from 'src/app/shared/components/consultazione-documenti/aggiungi-documento/aggiungi-documento.component';
import {
  ConsultazioneDocumentiComponent,
} from 'src/app/shared/components/consultazione-documenti/consultazione-documenti.component';
import { Constants } from 'src/app/shared/constants/constants';
import { TipoSegnalibroEnum } from 'src/app/shared/enums/tipo-segnalibro';
import {
  Preferenza,
} from 'src/app/shared/models/api/cosmoauthorization/preferenza';
import {
  TipoSegnalibro,
} from 'src/app/shared/models/api/cosmoauthorization/tipoSegnalibro';
import {
  TipoDocumento,
} from 'src/app/shared/models/api/cosmobusiness/tipoDocumento';
import {
  IdentitaUtente,
} from 'src/app/shared/models/api/cosmoecm/identitaUtente';
import {
  MessaggioControlliObbligatori,
  TipoMessaggio,
} from 'src/app/shared/models/messaggi-controlli-obbligatori/messaggio-controlli-obbligatori';
import {
  ValorePreferenzeUtente,
} from 'src/app/shared/models/preferenze/valore-preferenze-utente.model';
import { ModalService } from 'src/app/shared/services/modal.service';
import {
  PreferenzeUtenteService,
} from 'src/app/shared/services/preferenze-utente.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { SegnalibroService } from 'src/app/shared/services/segnalibro.service';
import {
  TipiDocumentiService,
} from 'src/app/shared/services/tipi-documenti/tipi-documenti.service';
import { Utils } from 'src/app/shared/utilities/utilities';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';

import { FunzionalitaParentComponent } from '../funzionalita-parent.component';
import {
  TabBadge,
  TabLifecycleCallback,
} from '../models/tab-status.models';
import {
  TipiDocumentoObbligatori,
} from './models/tipi-documento-obbligatori.models';
import { HelperService } from 'src/app/shared/services/helper.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-gestione-documenti',
  templateUrl: './gestione-documenti.component.html',
  styleUrls: ['./gestione-documenti.component.scss'],
  providers: [RicercaActaService]
})
export class GestioneDocumentiComponent extends FunzionalitaParentComponent implements OnInit, OnDestroy {

  abilitaRicercaActa = false;

  idPratica?: number;
  listaDocObbligatori: TipiDocumentoObbligatori[] = [];
  messaggiObbligatori: MessaggioControlliObbligatori[] = [];
  optionalCheck?: string;
  tipiDocumentiObblFuoriContesto = false;
  totDocCaricati?: number;
  tipiDocNonEliminabili: TipoDocumento[] = [];
  tipiAllNonEliminabili: TipoDocumento[] = [];
  tipiDocCaricabili: TipoDocumento[] = [];
  tipiAllCaricabili: TipoDocumento[] = [];
  firmeDocumentiValide = false;
  controlloValiditaFirme = false;
  attesaElaborazioneDocumenti = true;
  verificaDataDocObbligatori = false;

  private preferenzeUtenteSubscription!: Subscription;
  private tipoSegnalibro?: TipoSegnalibro;
  private preferenzeUtente?: Preferenza;

  @ViewChild('consultazioneDocumenti') consultazioneDocumentiComponent?: ConsultazioneDocumentiComponent;

  constructor(
    private logger: NGXLogger,
    public injector: Injector,
    private modal: NgbModal,
    private translateService: TranslateService,
    private modalService: ModalService,
    private tipiDocumentiService: TipiDocumentiService,
    private ricercaActaService: RicercaActaService,
    private preferenzeUtenteService: PreferenzeUtenteService,
    private segnalibroService: SegnalibroService,
    private spinner: SpinnerVisibilityService,
    private securityService: SecurityService,
    private helperService: HelperService,
    private route: ActivatedRoute,
  ) {
    super(injector);

    this.securityService.hasUseCases(['RICERCA_ACTA'], true).subscribe(result => {
      if (!result) {
        this.abilitaRicercaActa = false;
      } else if (this.parametri && this.parametri.get(Constants.FORM_LOGICI.ABILITA_RICERCA_ACTA_KEY)) {
        this.abilitaRicercaActa =
          JSON.parse(this.parametri.get(Constants.FORM_LOGICI.ABILITA_RICERCA_ACTA_KEY) as string) as boolean;
      }
    });

    if (this.parametri && this.parametri.get(Constants.FORM_LOGICI.APPOSIZIONE_FIRMA_KEY)) {
      this.daFirmare = JSON.parse(this.parametri.get(Constants.FORM_LOGICI.APPOSIZIONE_FIRMA_KEY) as string) as boolean;
    }
    if (this.parametri && this.parametri.get(Constants.FORM_LOGICI.TIPI_DOC_NON_RIMOVIBILI_KEY)) {
      const parsedTipiNonEliminabili = JSON.parse(this.parametri.get(Constants.FORM_LOGICI.TIPI_DOC_NON_RIMOVIBILI_KEY) as string);
      parsedTipiNonEliminabili?.tipi_documento.forEach((tipiDocNonEliminabili: TipoDocumento) => {
        this.tipiDocNonEliminabili.push(tipiDocNonEliminabili);
      });
      (parsedTipiNonEliminabili?.tipi_allegato ?? []).forEach((tipiAllNonEliminabili: TipoDocumento) => {
        this.tipiAllNonEliminabili.push(tipiAllNonEliminabili);
      });
    }
    if (this.parametri && this.parametri.get(Constants.FORM_LOGICI.TIPI_DOC_CARICABILI_KEY)) {
      const parsedTipiCaricabili = JSON.parse(this.parametri.get(Constants.FORM_LOGICI.TIPI_DOC_CARICABILI_KEY) as string);
      (parsedTipiCaricabili?.tipi_documento ?? []).forEach((tipiDocCaricabili: TipoDocumento) => {
        this.tipiDocCaricabili.push(tipiDocCaricabili);
      });
      (parsedTipiCaricabili?.tipi_allegato ?? []).forEach((tipiAllCaricabili: TipoDocumento) => {
        this.tipiAllCaricabili.push(tipiAllCaricabili);
      });
    }

    if (this.parametri && this.parametri.get(Constants.FORM_LOGICI.TIPI_DOC_OBBLIGATORI_KEY)) {

      const parsedKey = JSON.parse(this.parametri.get(Constants.FORM_LOGICI.TIPI_DOC_OBBLIGATORI_KEY) as string);

      this.tipiDocumentiService.getTipiDocumentiAll(this.pratica.tipo?.codice ?? '').subscribe(tipiDoc => {

        if (tipiDoc) {
          this.controlloTipiDocFuoriContesto(tipiDoc);
        }
      });
      if (!this.tipiDocumentiObblFuoriContesto) {
        parsedKey?.tipi_documento.forEach((tipoDocumentoObbligatorio: TipiDocumentoObbligatori) => {
          this.listaDocObbligatori.push(tipoDocumentoObbligatorio);
        });
      }
    }

    if (this.parametri && this.parametri?.get(Constants.FORM_LOGICI.CONTROLLO_VALIDITA_FIRMA_KEY)) {
      this.controlloValiditaFirme = JSON.parse(
        this.parametri?.get(Constants.FORM_LOGICI.CONTROLLO_VALIDITA_FIRMA_KEY) as string) as boolean;
    }

    if (this.parametri && this.parametri.get(Constants.FORM_LOGICI.VERIFICA_DATA_DOC_OBBLIGATORI)){
      this.verificaDataDocObbligatori = JSON.parse(
        this.parametri?.get(Constants.FORM_LOGICI.VERIFICA_DATA_DOC_OBBLIGATORI) as string) as boolean;
    }

    this.idPratica = this.pratica?.id;
  }

  aggiungiDocumento(idPratica: number) {
    const data = this.helperService.searchHelperRef(this.route);
    const modalRef = this.modal.open(AggiungiDocumentoComponent, { size: 'lg' /*, backdrop: 'static'*/ });
    modalRef.componentInstance.idPratica = idPratica;
    modalRef.componentInstance.tipologieDocumentoCaricabili = this.tipiDocCaricabili;
    modalRef.componentInstance.tipologieAllegatiCaricabili = this.tipiAllCaricabili;
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = 'aggiungi-documento';
  }

  ngOnInit(): void {
    this.segnalibroService.getTipiSegnalibro().subscribe(response => {
      this.tipoSegnalibro = response.tipiSegnalibro?.find(tipo => tipo.codice === TipoSegnalibroEnum.COLLOCAZIONE_ACTA);
    });

    this.preferenzeUtenteSubscription = this.preferenzeUtenteService.subscribePreferenze.subscribe(preferenze => {
      this.preferenzeUtente = preferenze;
    });
  }

  ngOnDestroy(): void {
    if (this.preferenzeUtenteSubscription) {
      this.preferenzeUtenteSubscription.unsubscribe();
    }
  }

  public beforeConfirm(): TabLifecycleCallback {
    if (this.optionalCheck && this.parametri && this.parametri.get(Constants.FORM_LOGICI.TIPI_DOC_OBBLIGATORI_KEY)) {
      const parsedKey = JSON.parse(this.parametri.get(Constants.FORM_LOGICI.TIPI_DOC_OBBLIGATORI_KEY) as string);

      if (parsedKey.messaggio_prima_della_conferma_per_docs_facoltativi && parsedKey.messaggio_prima_della_conferma_per_docs_facoltativi === 'true') {
        let subjectMsg = this.translateService.instant('form_logici.documenti_facoltativi_non_inseriti');
        subjectMsg = Utils.parseAndReplacePlaceholders(subjectMsg, [this.optionalCheck]);

        return from(this.modalService.confermaRifiuta(this.translateService.instant('common.conferma'), subjectMsg))
          .pipe(
            catchError(err => of(false)),
            map(result => result !== false)
          );
      }
    }
  }

  public isValid(): boolean {
    if (this.isSubtask) {
      return true;
    }
    const docObbligatoriMancanti = this.messaggiObbligatori?.find(docObbl => docObbl.tipoMessaggio === TipoMessaggio.ERROR);

    let ret = !docObbligatoriMancanti;
    if (this.controlloValiditaFirme) {
      ret = ret && this.firmeDocumentiValide;
    }
    return ret;
  }

  public isWip(): boolean {
    if (this.readOnly || this.isSubtask) {
      return false;
    }
    const docObbligatoriMancanti = this.messaggiObbligatori?.find(docObbl => docObbl.tipoMessaggio === TipoMessaggio.ERROR);
    return !!docObbligatoriMancanti;
  }

  sendMessages(docObbligatori: MessaggioControlliObbligatori[]): void {
    if (!this.isSubtask) {
      if (this.messaggiObbligatori.length === 0) {
        this.messaggiObbligatori = docObbligatori;
      } else {
        const nuoviMessaggi: MessaggioControlliObbligatori[] = [];

        docObbligatori.forEach(docObbligatorio => {
          const nuovi = this.messaggiObbligatori
            .filter(messaggioObbligatorio => docObbligatorio.messaggio.split(':')[0] === messaggioObbligatorio.messaggio.split(':')[0]);
          if (nuovi.length === 0) {
            nuoviMessaggi.push(docObbligatorio);
          }
        });

        this.messaggiObbligatori.forEach(messaggioObbligatorio => {
          const nuovi = docObbligatori
            .filter(docObbligatorio => messaggioObbligatorio.messaggio.split(':')[0] === docObbligatorio.messaggio.split(':')[0]);
          nuovi.forEach(nuovo => {
            if (!nuoviMessaggi.find(v => v.messaggio === nuovo.messaggio)){
              nuoviMessaggi.push(nuovo);
            }
          });
        });

        const messaggio = this.translateService.instant('errori.cfg_tipi_documenti_obbligatori_errata');

        const messaggiConfig = this.messaggiObbligatori
          .filter(messaggioObbligatorio => messaggio.split(':')[0] === messaggioObbligatorio.messaggio.split(':')[0]);
        messaggiConfig.forEach(messaggioConfig => nuoviMessaggi.push(messaggioConfig));

        this.messaggiObbligatori = nuoviMessaggi;
      }
      this.sendToParent(docObbligatori);
    }
  }

  sendToParent(docObbligatori: MessaggioControlliObbligatori[]) {
    const docFacoltativiMancanti = docObbligatori.find(docObbl => docObbl.tipoMessaggio === TipoMessaggio.WARNING);
    this.optionalCheck = docFacoltativiMancanti?.tipiDocumento?.join(',');

    this.sendData({});
  }

  private controlloTipiDocFuoriContesto(tipiDocumenti: TipoDocumento[]) {
    for (const tipoDocumentoObbligatorio of this.listaDocObbligatori) {
      const controlTipiDocContext = tipiDocumenti.find(x => x.codice === tipoDocumentoObbligatorio.codice);
      if (!controlTipiDocContext) {
        this.tipiDocumentiObblFuoriContesto = true;
        let subjectMsg = this.translateService.instant('errori.cfg_tipi_documenti_obbligatori_errata');
        subjectMsg = Utils.parseAndReplacePlaceholders(subjectMsg, [this.pratica.tipo?.descrizione ?? '']);
        this.messaggiObbligatori.push({ messaggio: subjectMsg, tipoMessaggio: TipoMessaggio.ERROR });
        break;
      }
    }
  }

  getTotDocumentiCaricati(totaleDocumenti: number) {
    return this.totDocCaricati = totaleDocumenti;
  }

  public getBadges(): TabBadge[] | null {
    const out: TabBadge[] = [];
    const mandatory = this.messaggiObbligatori.find(msg => msg.tipoMessaggio === TipoMessaggio.ERROR)?.tipiDocumento?.length ?? 0;
    const optional = this.messaggiObbligatori.find(msg => msg.tipoMessaggio === TipoMessaggio.WARNING)?.tipiDocumento?.length ?? 0;
    if (mandatory > 0) {
      out.push({
        class: 'danger',
        text: mandatory.toString(),
        tooltip: this.translateService.instant('common.obbligatori')
      });
    }
    if (optional > 0) {
      out.push({
        class: 'warning',
        text: optional.toString(),
        tooltip: this.translateService.instant('common.facoltativi')
      });
    }
    if (this.totDocCaricati && (+this.totDocCaricati > 0)) {
      out.push({
        class: 'info',
        text: this.totDocCaricati.toString(),
        tooltip: this.translateService.instant('common.caricati')
      });
    }
    return out.length > 0 ? out : null;
  }

  private getIdentitaSegnalibro(): IdentitaUtente | null {
    if (this.preferenzeUtente && this.preferenzeUtente.valore && this.tipoSegnalibro) {
      const valorePreferenzeUtente = JSON.parse(this.preferenzeUtente.valore) as ValorePreferenzeUtente;
      if (valorePreferenzeUtente && valorePreferenzeUtente.segnalibri) {
        const collocazioneActa = valorePreferenzeUtente.segnalibri
          .find(segnalibro => segnalibro.tipo === this.tipoSegnalibro?.descrizione);

        if (collocazioneActa) {
          this.logger.warn('IL SEGNALIBRO DI ACTA E\' TRA I PREFERITI, USARE QUESTO');
          // usare *** collocazioneActa.segnalibro *** l'esito del modale per risposta per l'identita' dell'utente
          return collocazioneActa.segnalibro as IdentitaUtente;
        } else {
          this.logger.warn('NON C\'E\' IL SEGNALIBRO DI ACTA');
          return null;
        }
      } else {
        this.logger.warn('NON CI SONO SEGNALIBRI');
        return null;
      }
    } else {
      this.logger.warn('NON CI SONO PREFERENZE');
      return null;
    }
  }

  private selezionaIdentitaActa(data: ActivatedRoute | null): Observable<IdentitaUtente | null> {
    this.spinner.show();
    return of(null).pipe(
      tap(() => this.spinner.show()),
      mergeMap(() => this.ricercaActaService.getIdentitaUtente()),
      tap(() => this.spinner.hide()),
      finalize(() => this.spinner.hide()),
      mergeMap(response => {
        if (!response?.length) {
          return of(null);
        } else if (response.length === 1) {
          return of(response[0]);
        } else {
          const modalRef = this.modal.open(IdentitaUtenteActaComponent, { size: 'lg' /*, backdrop: 'static'*/ });
          modalRef.componentInstance.identitaUtente = response;
          modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
          modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
          return from(modalRef.result.then(closed => {
            // usare l'esito del modale per risposta per l'identita' dell'utente
            return closed;
          }).catch(() => {
            return null;
          }));
        }
      })
    );
  }

  aggiungiDocumentoActa() {
    const data = this.helperService.searchHelperRef(this.route);
    const identitaSegnalibro: IdentitaUtente | null = this.getIdentitaSegnalibro();

    of(identitaSegnalibro).pipe(
      mergeMap(identitaFinora => identitaFinora ? of(identitaFinora) : this.selezionaIdentitaActa(data))
    ).subscribe(identita => {
      if (!identita) {
        // nessuna identita' selezionata oppure operazione cancellata
        return;
      }

      // apri modale di ricerca in acta
      const modalRef = this.modal.open(RicercaActaComponent, { size: 'xl' /*, backdrop: 'static'*/ });
      const component = modalRef.componentInstance as RicercaActaComponent;
      modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
      modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
      component.configure(this.pratica, identita);

      return from(modalRef.result.then(() => {
        this.consultazioneDocumentiComponent?.refresh(false);
      }).catch(() => {
        return null;
      }));
    }, error => {
      const msg = error.error.title ?? 'Errore nella selezione dell\' identita\' dell\'utente';
      this.modalService.error('Selezione identita\' utente', msg, error.error.errore)
        .then(() => { })
        .catch(() => { });
    });
  }

  areAllDocsSignaturesValid(validitaFirmeDocumenti: boolean) {
    this.firmeDocumentiValide = validitaFirmeDocumenti;
  }

  areAllDocsProcessed(attesaElaborazioneDocumenti: boolean) {
    this.attesaElaborazioneDocumenti = attesaElaborazioneDocumenti;
  }

}
