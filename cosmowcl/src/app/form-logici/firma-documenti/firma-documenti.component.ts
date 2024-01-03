/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Injector, OnInit } from '@angular/core';
import { Constants } from 'src/app/shared/constants/constants';
import { MessaggioControlliObbligatori, TipoMessaggio } from 'src/app/shared/models/messaggi-controlli-obbligatori/messaggio-controlli-obbligatori';
import { FunzionalitaParentComponent } from '../funzionalita-parent.component';
import { TipiDocumentoObbligatori } from '../gestione-documenti/models/tipi-documento-obbligatori.models';
import { DocumentiService } from 'src/app/shared/components/consultazione-documenti/services/documenti.service';
import { SimpleFlowableOutput } from '../models/simple-flowable-output.model';
import { TabBadge, TabLifecycleCallback } from '../models/tab-status.models';
import { TranslateService } from '@ngx-translate/core';
import { Utils } from 'src/app/shared/utilities/utilities';
import { ModalService } from 'src/app/shared/services/modal.service';
import { catchError, map } from 'rxjs/operators';
import { from, of } from 'rxjs';
import { TipiDocumentiService } from 'src/app/shared/services/tipi-documenti/tipi-documenti.service';
import { TipoDocumento } from 'src/app/shared/models/api/cosmobusiness/tipoDocumento';

export const Variabili = {
  RIS_FIRMA: 'ris-firma'
};

@Component({
  selector: 'app-firma-documenti',
  templateUrl: './firma-documenti.component.html',
  styleUrls: ['./firma-documenti.component.scss']
})

export class FirmaDocumentiComponent extends FunzionalitaParentComponent implements OnInit {

  note = '';
  approvazione?: boolean;

  siButton: string | null = null;
  noButton: string | null = null;

  variabileRisultato = Variabili.RIS_FIRMA;
  variabileNote = 'note';

  idPratica: number | undefined;
  listaDocObbligatori: TipiDocumentoObbligatori[] = [];
  listaDocDaFirmareObbligatori: TipiDocumentoObbligatori[] = [];
  messaggiObbligatori: MessaggioControlliObbligatori[] = [];
  numeroDocumentiDaFirmare?: number;
  loaded = false;
  firma?: boolean;
  firmaFea!: boolean;
  optionalCheck?: string;
  tipiDocumentiObblFuoriContesto = false;
  warningSignatureBeforeConfirm: MessaggioControlliObbligatori | undefined;
  controlloValiditaFirme = false;
  firmeUtenteValide = false;
  attesaElaborazioneDocumenti = true;

  constructor(
    public injector: Injector,
    private documentiService: DocumentiService,
    private translateService: TranslateService,
    private modalService: ModalService,
    private tipiDocumentiService: TipiDocumentiService
  ) {
    super(injector);

    this.parseParametri();

    if (this.parametri && this.parametri.get('APPOSIZIONE_FIRMA')) {
      this.daFirmare = JSON.parse(this.parametri.get('APPOSIZIONE_FIRMA') as string) as boolean;
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
        this.decodificaCodiceTipiDocumento(this.listaDocObbligatori);
      }
    }
    if (this.parametri && this.parametri.get(Constants.FORM_LOGICI.TIPI_DOC_DA_FIRMARE_OBBLIGATORI_KEY)) {
      const parsedKey = JSON.parse(this.parametri.get(Constants.FORM_LOGICI.TIPI_DOC_DA_FIRMARE_OBBLIGATORI_KEY) as string);
      parsedKey?.tipi_documento.forEach((tipoDocumentoDaFirmareObbligatorio: TipiDocumentoObbligatori) => {
        this.listaDocDaFirmareObbligatori.push(tipoDocumentoDaFirmareObbligatorio);
      });
      this.decodificaCodiceTipiDocumento(this.listaDocDaFirmareObbligatori);
    }
    if (this.parametri && this.parametri?.get(Constants.FORM_LOGICI.CONTROLLO_VALIDITA_FIRMA_KEY)) {
      this.controlloValiditaFirme = JSON.parse(
        this.parametri?.get(Constants.FORM_LOGICI.CONTROLLO_VALIDITA_FIRMA_KEY) as string) as boolean;
    }
    if (this.parametri && this.parametri.get(Constants.SCELTA_FIRMA_FEA)) {
      this.firmaFea = JSON.parse(this.parametri.get(Constants.SCELTA_FIRMA_FEA) as string) as boolean;
    }
    this.idPratica = this.pratica?.id;
  }

  public getBadges(): TabBadge[] | null {
    const out: TabBadge[] = [];

    const mandatory = this.listaDocDaFirmareObbligatori.filter(docs => docs.obbligatorio === 'true' ).length ?? 0;
    const optional = this.listaDocDaFirmareObbligatori.filter(docs => docs.obbligatorio === 'false' ).length ?? 0;

    if (this.messaggiObbligatori.length > 0 && mandatory > 0) {
      out.push({ class: 'danger',
                 text: mandatory.toString(),
                 tooltip: this.translateService.instant('common.obbligatori')});
    }
    if (this.messaggiObbligatori.length > 0 && optional > 0) {
      out.push({ class: 'warning',
                 text: optional.toString(),
                 tooltip: this.translateService.instant('common.facoltativi')});
    }
    return out.length > 0 ? out : null;
  }

  public isValid(): boolean {
    if (this.isSubtask) {
      return true;
    }
    const docObbligatoriMancanti = this.messaggiObbligatori?.find(docObbl => docObbl.tipoMessaggio === TipoMessaggio.ERROR);
    let ret = false;
    if (this.controlloValiditaFirme) {
      if (this.approvazione === true) {
        ret = !docObbligatoriMancanti && this.firmeUtenteValide;
      } else {
        ret = this.note.trim().length > 0;
      }
    } else {
      ret = (this.approvazione === true && !docObbligatoriMancanti) || (this.approvazione === false && this.note.trim().length > 0);
    }
    return ret;
  }

  public isWip(): boolean {
    return !this.readOnly && !this.isSubtask && !this.isValid();
  }

  public beforeConfirm(): TabLifecycleCallback {
    if (this.approvazione === false && this.note.trim().length > 0){
      return true;
    }

    let subjectMsg = '';
    if (this.optionalCheck) {
      subjectMsg = this.translateService.instant('form_logici.documenti_facoltativi_non_firmati');
      subjectMsg = Utils.parseAndReplacePlaceholders(subjectMsg, [this.optionalCheck]);
    }
    if (this.warningSignatureBeforeConfirm) {
      subjectMsg = subjectMsg + ' ' + this.warningSignatureBeforeConfirm.messaggio;
    }

    if (subjectMsg.length === 0) {
      return true;
    }
    return from(this.modalService.confermaRifiuta(this.translateService.instant('common.conferma'), subjectMsg))
      .pipe(
        catchError(err => of(false)),
        map(result => result !== false)
      );
  }

  ngOnInit(): void {
  }

  sendMessages(docObbligatori: MessaggioControlliObbligatori[]): void {
    if (this.messaggiObbligatori.length === 0) {
      this.messaggiObbligatori = docObbligatori;
    } else {
      const nuoviMessaggi: MessaggioControlliObbligatori[] = [];

      docObbligatori.forEach( docObbligatorio => {
          const nuovi = this.messaggiObbligatori
          .filter(messaggioObbligatorio => docObbligatorio.messaggio.split(':')[0] === messaggioObbligatorio.messaggio.split(':')[0] );
          if (nuovi.length === 0){
            nuoviMessaggi.push(docObbligatorio);
          }
      });

      this.messaggiObbligatori.forEach( messaggioObbligatorio => {
        const nuovi = docObbligatori
          .filter(docObbligatorio => messaggioObbligatorio.messaggio.split(':')[0] === docObbligatorio.messaggio.split(':')[0]);
        nuovi.forEach(nuovo => nuoviMessaggi.push(nuovo));
      });

      const messaggio = this.translateService.instant('errori.cfg_tipi_documenti_obbligatori_errata');

      const messaggiConfig = this.messaggiObbligatori
        .filter(messaggioObbligatorio => messaggio.split(':')[0] === messaggioObbligatorio.messaggio.split(':')[0]);
      messaggiConfig.forEach(messaggioConfig => nuoviMessaggi.push(messaggioConfig));

      this.messaggiObbligatori = nuoviMessaggi;
    }
    this.sendToParent(docObbligatori);
  }

  sendToParent(docObbligatori: MessaggioControlliObbligatori[]) {
    const docFacoltativiMancanti = docObbligatori.filter(docObbl => docObbl.tipoMessaggio === TipoMessaggio.WARNING);
    this.warningSignatureBeforeConfirm = docFacoltativiMancanti.find(d => !d.tipiDocumento);
    docFacoltativiMancanti.forEach(dfm => this.optionalCheck = dfm.tipiDocumento?.join(','));

    this.sendData({
      payload: this.creaVariabile(true),
    });
  }

  getNumeriDocumentiDaFirmare(numDoumentiDaFirmare: number) {
    this.numeroDocumentiDaFirmare = numDoumentiDaFirmare;
  }

  private decodificaCodiceTipiDocumento(listaObbligatoria: TipiDocumentoObbligatori[]) {
    this.documentiService.getTipiDocumento(listaObbligatoria.map(lista => lista.codice)).subscribe(tipiDocumento => {
      if (tipiDocumento) {
        tipiDocumento.forEach(tipoDocumento => {
          const tipoDocObbl = listaObbligatoria.find(o => o.codice === tipoDocumento.codice);
          if (tipoDocObbl) {
            tipoDocObbl.descrizione = tipoDocumento.descrizione;
          }
        });
      }
    });
  }

  private creaVariabile(firma?: boolean): SimpleFlowableOutput[] {
    const flowableOutput: SimpleFlowableOutput[] = [];
    if (firma === true) {
      flowableOutput.push({ name: Variabili.RIS_FIRMA, value: '' + firma });
    }
    return flowableOutput;
  }

  parseParametri(): void {

    const raw = this.parametri?.get('O_FIRMA') ?? undefined;
    if (!raw) {
      throw new Error('Tab non configurato correttamente');
    }
    let parsed;
    try {
      parsed = JSON.parse(raw);
    } catch (err) {
      throw new Error('Tab non configurato correttamente - il parametro O_FIRMA non è un JSON valido');
    }

    const variabileRisultato = parsed.variabileRisultatoFirma;
    if (!variabileRisultato?.trim().length) {
      throw new Error('Tab non configurato correttamente - campo variabileRisultato del parametro O_FIRMA non presente');
    }

    const variabileNote = parsed.variabileNoteFirma;
    if (!variabileNote?.trim().length) {
      throw new Error('Tab non configurato correttamente - campo variabileNote del parametro O_FIRMA non presente');
    }

    this.variabileRisultato = variabileRisultato.trim();
    this.variabileNote = variabileNote.trim();

    if (this.variabiliProcesso && this.variabiliProcesso.length > 0) {
      this.variabiliProcesso.forEach(variabile => {
        if (Utils.isNotBlank(variabile?.value) && this.variabileRisultato === variabile.name) {
          this.approvazione = (variabile.value as any) === 'true';
          this.loaded = true;
        }
        if (Utils.isNotBlank(variabile?.value) && this.variabileNote === variabile.name) {
          this.note = variabile.value as any as string;
          this.loaded = true;
        }
      });
    }
    if (this.parametri?.get('SI_BUTTON')){
      this.siButton =  this.parametri.get('SI_BUTTON') as string;
    }

    if (this.parametri?.get('NO_BUTTON')){
      this.noButton = this.parametri.get('NO_BUTTON') as string;
    }

    this.approvaRifiuta();
  }

  rifiuta(note: string) {
    this.datiModificati(true, note);
    this.approvazione = false;
    this.noteChanged(note);
  }

  accetta(note: string) {
    this.datiModificati(true, note);
    this.approvazione = true;
    this.noteChanged(note);
  }

  noteChanged(note: string) {
    this.note = note;
    this.markDirty();
    this.approvaRifiuta();
  }

  private datiModificati(approvazione: boolean, note: string) {
    if (approvazione !== this.approvazione || note !== this.note) {
      this.dataChanged = true;
    } else {
      this.dataChanged = false;
    }
  }

  approvaRifiuta() {
    const flowableOutput: SimpleFlowableOutput[] = [];
    if (this.approvazione === true || this.approvazione === false) {
      flowableOutput.push({ name: this.variabileRisultato, value: '' + this.approvazione });
    }
    flowableOutput.push({ name: this.variabileNote, value: this.note });

    this.sendData({
      payload: flowableOutput,
    });
  }

  private controlloTipiDocFuoriContesto(tipiDocumenti: TipoDocumento[]) {
    for (const tipoDocumentoObbligatorio of this.listaDocObbligatori){
      const controlTipiDocContext = tipiDocumenti.find(x => x.codice === tipoDocumentoObbligatorio.codice);
      if (!controlTipiDocContext ) {
        this.tipiDocumentiObblFuoriContesto = true;
        let subjectMsg = this.translateService.instant('errori.cfg_tipi_documenti_obbligatori_errata');
        subjectMsg = Utils.parseAndReplacePlaceholders(subjectMsg, [this.pratica.tipo?.descrizione ?? '']);
        this.messaggiObbligatori.push({messaggio: subjectMsg, tipoMessaggio: TipoMessaggio.ERROR});
        break;
      }
    }
  }

  areUserSignaturesAllValid(validitaFirmeUtente: boolean) {
    this.firmeUtenteValide = validitaFirmeUtente;
  }

  areAllDocsProcessed(attesaElaborazioneDocumenti: boolean) {
    this.attesaElaborazioneDocumenti = attesaElaborazioneDocumenti;
  }

}
