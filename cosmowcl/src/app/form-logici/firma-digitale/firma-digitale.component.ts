/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { DatePipe } from '@angular/common';
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {  Idle } from '@ng-idle/core';
import { TranslateService } from '@ngx-translate/core';
import { CosmoTableColumnSize, CosmoTableComponent, CosmoTableFormatter, ICosmoTableColumn, } from 'ngx-cosmo-table';
import { from, Subscription } from 'rxjs';
import { debounceTime, mergeMap } from 'rxjs/operators';
import { EsecuzioneMultiplaService } from 'src/app/esecuzione-multipla/esecuzione-multipla.service';
import { ModaleParentComponent } from 'src/app/modali/modale-parent-component';
import { CertificatiFirmaComponent } from 'src/app/shared/components/certificati-firma/certificati-firma.component';
import { CertificatiService } from 'src/app/shared/components/certificati-firma/certificati/certificati.service';
import { Constants } from 'src/app/shared/constants/constants';
import { CertificatoFirma } from 'src/app/shared/models/api/cosmoauthorization/certificatoFirma';
import { AttivitaEseguibileMassivamente } from 'src/app/shared/models/api/cosmoecm/attivitaEseguibileMassivamente';
import { DocumentiTask } from 'src/app/shared/models/api/cosmoecm/documentiTask';
import { Documento } from 'src/app/shared/models/api/cosmoecm/documento';
import { EsecuzioneMultiplaFirmaRequest } from 'src/app/shared/models/api/cosmoecm/esecuzioneMultiplaFirmaRequest';
import { FirmaRequest } from 'src/app/shared/models/api/cosmoecm/firmaRequest';
import { RichiestaOTPRequest } from 'src/app/shared/models/api/cosmoecm/richiestaOTPRequest';
import { DocumentoDTO } from 'src/app/shared/models/documento/documento.model';
import { FirmaDigitaleService } from 'src/app/shared/services/firma-digitale.service';
import { HelperService } from 'src/app/shared/services/helper.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { NotificationsWebsocketService } from 'src/app/shared/services/notifications-websocket.service';
import { RisultatiFirmaModalService } from 'src/app/shared/services/risultati-firma-modal.service';
import { DateUtils } from 'src/app/shared/utilities/date-utils';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-firma-digitale',
  templateUrl: './firma-digitale.component.html',
  styleUrls: ['./firma-digitale.component.scss']
})
export class FirmaDigitaleComponent extends ModaleParentComponent implements OnInit, OnDestroy {

  @ViewChild('certificatiFirma') certificatiFirma!: CertificatiFirmaComponent;

  @ViewChild('table') table: CosmoTableComponent | null = null;

  private notificationSubcritpion: Subscription | null = null;

  staFirmando = false;
  documenti: DocumentoDTO[] = [];
  profiliFeqAbilitati: string[] = [];
  documentiTask: DocumentiTask[] = [];
  codiceTipoPratica?: string;
  tasks: AttivitaEseguibileMassivamente[] = [];
  isMassivo = false;
  rowClasses: {[key: number]: string} = {};
  rowErrors: {[key: number]: string} = {};
  note: string | undefined = '';
  mandareAvantiProcesso = false;
  idAttivita = 0;
  codicePagina!: string;
  codiceTab!: string;
  codiceModale!: string;

  messageMapping: { [k: string]: string } =
    { '=0': 'Nessun documento selezionato', '=1': 'Hai selezionato un documento', other: 'Hai selezionato # documenti' };

  columns: ICosmoTableColumn[] = [
    {
      name: 'tipologia', label: 'tipologia',
      valueExtractor: row => row.tipo.descrizione ? row.tipo.descrizione : '--', canSort: true
    },
    {
      name: 'titolo_nome_file', label: 'titolo/nome file',
      valueExtractor: row => row.titolo ? row.titolo : row.contenutoEffettivo.nomeFile ? row.contenutoEffettivo.nomeFile : '--',
      canSort: true
    },
    {
      name: 'ultima_modifica', label: 'ultima modifica',
      formatters: [{
        formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy',
       }, {
        format: (raw: any) => raw ?? '--',
      }],
      valueExtractor: row => DateUtils.parse(row.ultimaModifica),
      canSort: true
    },
    { name: 'formato', label: 'formato', applyTemplate: true },
    { name: 'alert', canHide: false, canSort: false, applyTemplate: true, size: CosmoTableColumnSize.XXS }
  ];

  otp!: string;

  numeroDocumento = 0;
  totaleDocumenti = 0;
  percentualeFirma = 0;


  constructor(
    public modal: NgbActiveModal,
    private datePipe: DatePipe,
    private firmaDigitaleService: FirmaDigitaleService,
    private notificationsWebsocketService: NotificationsWebsocketService,
    private translateService: TranslateService,
    private modalService: ModalService,
    private certificatiService: CertificatiService,
    private esecuzioneMultiplaService: EsecuzioneMultiplaService,
    private risultatiFirmaModalService: RisultatiFirmaModalService,
    protected router: Router,
    protected activatedRoute: ActivatedRoute,
    public helperService: HelperService,
    private idle: Idle) {
      super(helperService);
  }

  get mocked(): boolean {
    return environment?.firmaDigitale?.mock ?? false;
  }

  ngOnInit(): void {
    this.totaleDocumenti = this.documenti.length;
    this.notificationSubcritpion = this.notificationsWebsocketService
      .whenEvent(Constants.APPLICATION_EVENTS.DOCUMENTI_FIRMATI)
      .pipe(
        debounceTime(1000)
      )
      .subscribe(e => {
        this.numeroDocumento = e.payload.numeroDocumento;
        if (this.numeroDocumento < this.totaleDocumenti) {
          this.percentualeFirma = this.numeroDocumento * 100 / this.totaleDocumenti;
        } else {
          this.percentualeFirma = 100;
        }
      });
    this.setModalName(this.codiceModale);
    this.searchHelperModale(this.codicePagina, this.codiceTab);
  }

  ngOnDestroy(): void {
    if (this.notificationSubcritpion) {
      this.notificationSubcritpion.unsubscribe();
    }
    if (!this.idle.isRunning() && !this.isMassivo) {
      this.idle.watch();
    }
  }

  richiediOTP() {
    const enteCertificatore = this.certificatiFirma.impostazioniFirmaComp.enteCertificatore?.codice ?? '';
    const richiesta: RichiestaOTPRequest = {
      alias: this.certificatiFirma.certificatoFirmaForm.get('username')?.value,
      pin: this.certificatiFirma.certificatoFirmaForm.get('pin')?.value,
      codiceEnteCertificatore: enteCertificatore,
      password: this.certificatiFirma.certificatoFirmaForm.get('password')?.value
    };

    this.firmaDigitaleService.richiediOTP(richiesta).subscribe(
      response => {
        this.modalService.info(this.translateService.instant('firma_digitale.richiesta_OTP'),
          this.translateService.instant('firma_digitale.richiesta_OTP_OK'))
          .then(() => { })
          .catch(() => { });
      },
      error => {
        this.modalService.error(this.translateService.instant('firma_digitale.richiesta_OTP'),
          this.translateService.instant('errori.impossibile_richiedere_OTP'),
          error.error.errore)
          .then(() => { })
          .catch(() => { });
      }
    );
  }


  get disabilitaRichiediOTP() {
    if (this.presenteScegliSempre()) {
      return true;
    }

    if (this.certificatiFirma?.certificatoFirmaForm) {
      return !this.certificatiFirma.certificatoFirmaForm.get('username')?.value ||
        !this.certificatiFirma.certificatoFirmaForm.get('pin')?.value ||
        this.certificatiFirma.impostazioniFirmaComp?.tipoOTP?.codice !== Constants.IMPOSTAZIONI_FIRMA.TIPI_OTP.SMS ||
        !this.certificatiFirma.impostazioniFirmaComp?.enteCertificatore?.codice ||
        (this.certificatiFirma?.canManagePasswordField && !this.certificatiFirma.certificatoFirmaForm.get('password')?.value);
    }
    return false;
  }

  disabilitaFirma() {
    return this.staFirmando || this.percentualeFirma === 100 || !this.certificatiFirma
      || this.disabilitaFirmaCertificato()
      || (this.certificatiFirma.certificatoFirmaForm.get('scadenza')?.value && new Date(this.certificatiFirma.certificatoFirmaForm.get('scadenza')?.value) < new Date())
      || this.certificatiFirma.impostazioniFirmaComp.profiloFEQ?.nonValidoInApposizione
      || this.certificatiFirma.impostazioniFirmaComp.sceltaMarcaTemporale?.nonValidoInApposizione
      || this.certificatiFirma.impostazioniFirmaComp.tipoCredenzialiFirma?.nonValidoInApposizione
      || this.certificatiFirma.impostazioniFirmaComp.tipoOTP?.nonValidoInApposizione
      || (this.certificatiFirma.impostazioniFirmaComp.profiloFEQ && this.profiliFeqAbilitati.length > 0 &&
        !this.profiliFeqAbilitati.includes(this.certificatiFirma.impostazioniFirmaComp.profiloFEQ?.codice))
      || !this.otp
      || (this.certificatiFirma.canManagePasswordField && !this.certificatiFirma.certificatoFirmaForm.get('password')?.value)
      ;
  }

  disabilitaFirmaCertificato() {
    return !this.certificatiFirma.certificatoFirmaForm.valid ||
      (!this.certificatiFirma.impostazioniFirmaComp || (this.certificatiFirma.impostazioniFirmaComp &&
        !this.certificatiFirma.impostazioniFirmaComp.enteCertificatore
        || !this.certificatiFirma.impostazioniFirmaComp.tipoCredenzialiFirma ||
        !this.certificatiFirma.impostazioniFirmaComp.tipoOTP || !this.certificatiFirma.impostazioniFirmaComp.profiloFEQ ||
        !this.certificatiFirma.impostazioniFirmaComp.sceltaMarcaTemporale));
  }

  firmaMassiva(certificato: CertificatoFirma) {

    const payload: EsecuzioneMultiplaFirmaRequest = {
      documentiTask: this.documentiTask,
      otp: this.otp,
      tasks: this.esecuzioneMultiplaService.getSelectedTasks(),
      certificato,
      note: this.note,
      mandareAvantiProcesso: this.mandareAvantiProcesso
    };

    this.staFirmando = true;
    this.esecuzioneMultiplaService.postEsecuzioneMultiplaFirma(payload)
    .pipe(
      mergeMap(() =>
          from(this.risultatiFirmaModalService.open({
            totaleDocumenti: this.documentiTask.length,
            showMessages: false,
            tasks: this.esecuzioneMultiplaService.getSelectedTasks()
          }).result)
        )
      )
      .subscribe(
        result => {
          if (certificato?.id){
            this.certificatiService.salvaCertificatoUltimoUtilizzato(certificato.id)
            .subscribe(() => {}, (err) => {
              this.modalService.simpleError((Utils.toMessage(err.error.title), err.error.errore).finally().catch());
            });
          }
          this.elaboraRisultatoFirma(result);
          if (!this.idle.isRunning()) {
          this.idle.watch();
        }
    });
  }

  elaboraRisultatoFirma(risultato: Array<string>): void {

    this.table?.uncheckAll();
    if (risultato.length === 0) {
      this.modalService.simpleInfo('Tutte le attività sono state elaborate con successo.').finally(() => {
        this.staFirmando = false;
        this.modal.dismiss('finish_to_sign_docs');
        if (this.isMassivo) {
          this.router.navigate(['home'], {relativeTo: this.activatedRoute});
        } else {
          this.table?.refresh();
        }
      });

      return;

    } else {
      this.modalService.simpleError('Si sono verificati degli errori nell\'elaborazione di '
      + risultato.length + ' pratiche.').finally(() => {
        this.staFirmando = false;
        this.table?.refresh();
      });
    }

    this.table?.refresh();
  }

  elaboraRisultato(risultato: any[]): void {
    let numSuccesso = 0;
    let numErrori = 0;

    this.table?.uncheckAll();

    for (const entry of risultato) {
      if (entry.successo) {
        numSuccesso ++;
        this.rowClasses[entry.task.attivita.id] = 'success';
        this.esecuzioneMultiplaService.removeSelectedTask(entry.task);
      } else {
        numErrori ++;
        this.rowClasses[entry.task.attivita.id] = 'danger';
        this.rowErrors[entry.task.attivita.id] = entry.errore.message;
      }
    }

    this.tasks = this.esecuzioneMultiplaService.getSelectedTasks();
    if (this.tasks.length <= 0) {
      this.modalService.simpleInfo('Tutte le attività sono state elaborate con successo.').finally(() => {
        this.staFirmando = false;
        this.modal.dismiss('finish_to_sign_docs');
        if (this.isMassivo) {
          this.router.navigate(['home'], {relativeTo: this.activatedRoute});
        } else {
          this.table?.refresh();
        }
      });

      return;
    } else if (!numErrori) {
      this.modalService.simpleInfo(numSuccesso + ' attività sono state elaborate con successo.').finally(() => {
        this.staFirmando = false;
        this.table?.refresh();
      });

    } else {
      this.modalService.simpleError('Si sono verificati degli errori nell\'elaborazione di ' + numErrori + ' attività.').finally(() => {
        this.staFirmando = false;
        this.table?.refresh();
      });
    }

    this.table?.refresh();
  }

  firma() {
    const certificato = this.creaCertificato();
    if (certificato) {

      if (certificato.id) {
        this.firmaDocumenti(certificato);

      } else {
        this.modalService.scegli(
          this.translateService.instant('firma_digitale.firma_documenti'),
          this.translateService.instant('firma_digitale.certificato_non_salvato'),
          [
            { testo: this.translateService.instant('firma_digitale.salva_firma'),
            valore: 'primoBottone', classe: 'btn-outline-primary', defaultFocus: true },
            { testo: this.translateService.instant('firma_digitale.firma_senza_salvare'),
            valore: 'secondoBottone', classe: 'btn-primary' }
          ]
        ).then(proceed => {
            if ('primoBottone' === proceed) {
              if (certificato.descrizione) {
                this.certificatiService.salvaCertificato(certificato).subscribe(
                  (certificatoFirma) => {
                    this.modalService.info(this.translateService.instant('firma_digitale.saltavaggio_certificato'),
                      this.translateService.instant('firma_digitale.salvataggio_certificato_msg'))
                      .then(() => {
                        this.firmaDocumenti(certificatoFirma);
                      }
                      ).catch(() => { });
                  },
                  error => {
                    const messaggioErrore = Utils.parseAndReplacePlaceholders(
                      this.translateService.instant('errori.creazione_certificazione_msg'),
                      [certificato.descrizione ?? 'Certificato']);

                    this.modalService.error(
                      this.translateService.instant('firma_digitale.saltavaggio_certificato'),
                      messaggioErrore,
                      error.error.errore
                    ).then(() => { }).catch(() => { });
                  });
              } else {
                this.modalService.error(
                  this.translateService.instant('firma_digitale.firma_documenti'),
                  this.translateService.instant('firma_digitale.indicare_nome_certificato')
                ).then(() => { }).catch(() => { });
              }
            } else{
              this.firmaDocumenti(certificato);
            }

          })
          .catch(() => { });
      }
    }

  }

  creaCertificato() {
    if (this.certificatiFirma.certificatoFirmaForm.valid && this.certificatiFirma.impostazioniFirmaComp &&
      this.certificatiFirma.impostazioniFirmaComp.enteCertificatore &&
      this.certificatiFirma.impostazioniFirmaComp.tipoCredenzialiFirma &&
      this.certificatiFirma.impostazioniFirmaComp.tipoOTP && this.certificatiFirma.impostazioniFirmaComp.profiloFEQ &&
      this.certificatiFirma.impostazioniFirmaComp.sceltaMarcaTemporale) {

      const certificato: CertificatoFirma = {
        descrizione: this.certificatiFirma.certificatiFirmaSelezionata ?
          this.certificatiFirma.certificatiFirmaSelezionata.descrizione ?? this.certificatiFirma.certificatiFirmaSelezionata : '',
        username: this.certificatiFirma.certificatoFirmaForm.get('username')?.value,
        pin: this.certificatiFirma.certificatoFirmaForm.get('pin')?.value,
        password: this.certificatiFirma.certificatoFirmaForm.get('password')?.value,
        dataScadenza: this.certificatiFirma.certificatoFirmaForm.get('scadenza')?.value
          ? new Date(this.certificatiFirma.certificatoFirmaForm.get('scadenza')?.value).toISOString() : undefined,
        enteCertificatore: this.certificatiFirma.impostazioniFirmaComp.enteCertificatore,
        profiloFEQ: this.certificatiFirma.impostazioniFirmaComp.profiloFEQ,
        sceltaMarcaTemporale: this.certificatiFirma.impostazioniFirmaComp.sceltaMarcaTemporale,
        tipoCredenzialiFirma: this.certificatiFirma.impostazioniFirmaComp.tipoCredenzialiFirma,
        tipoOTP: this.certificatiFirma.impostazioniFirmaComp.tipoOTP
      };

      if (this.certificatiFirma?.certificatiFirmaSelezionata?.id) {
        certificato.id = this.certificatiFirma.certificatiFirmaSelezionata.id;
      }
      return certificato;
    }
  }

  firmaDocumenti(certificatoFirma: CertificatoFirma) {
    if (this.isMassivo) {
      this.firmaMassiva(certificatoFirma);
    } else {
      if (this.idle.isRunning()) {
        this.idle.stop();
      }

      const docsDaFirmare: Array<Documento> = [];
      this.documenti.forEach(documento => {
        docsDaFirmare.push({
          idPratica: documento.idPratica,
          idDocumentoExt: documento.idDocumentoExt,
          titolo: documento.titolo,
          tipo: documento.tipo,
          descrizione: documento.descrizione,
          idDocumentoParentExt: documento.idDocumentoParentExt,
          id: documento.id,
          autore: documento.autore,
          stato: documento.stato,
          contenuti: documento.contenuti,
          ultimaModifica: documento.ultimaModifica,
          smistamento: documento.smistamento
        });
      });

      const body: FirmaRequest = {
        documenti: docsDaFirmare,
        otp: this.otp,
        certificato: certificatoFirma,
        idAttivita: this.idAttivita
      };

      this.staFirmando = true;
      this.firmaDigitaleService.firmaDocumenti(body)
      .pipe(
        mergeMap(() =>
          from(this.risultatiFirmaModalService.open({
            totaleDocumenti: this.totaleDocumenti,
            showMessages: false,
            ids: body.documenti?.map( d => d.id),
            tasks: []
          }).result)
        )
      )
      .subscribe(
        result => {
        this.elaboraRisultatoFirma(result);
        if (certificatoFirma.id){
          this.certificatiService.salvaCertificatoUltimoUtilizzato(certificatoFirma.id)
          .subscribe(() => {}, (err) => {
            this.modalService.simpleError((Utils.toMessage(err.error.title), err.error.errore)).finally().catch();
          });
        }
        if (!this.idle.isRunning()) {
          this.idle.watch();
        }
    });
    }
  }

  presenteScegliSempre(): boolean {
    if (!this.certificatiFirma?.impostazioniFirmaComp) {
      return false;
    }
    if ('SCEGLI' === this.certificatiFirma.impostazioniFirmaComp?.tipoCredenzialiFirma?.codice) {
      return true;
    }
    if ('SCEGLI' === this.certificatiFirma.impostazioniFirmaComp?.profiloFEQ?.codice) {
      return true;
    }
    if ('SCEGLI' === this.certificatiFirma.impostazioniFirmaComp?.sceltaMarcaTemporale?.codice) {
      return true;
    }
    return false;
  }

}

