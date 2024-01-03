/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  Component,
  HostListener,
  OnInit,
} from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';

import { NGXLogger } from 'ngx-logger';
import {
  forkJoin,
  Observable,
  of,
  Subject,
  throwError,
} from 'rxjs';
import {
  debounceTime,
  filter,
  finalize,
  map,
  mergeMap,
  tap,
} from 'rxjs/operators';
import {
  DocumentiService,
} from 'src/app/shared/components/consultazione-documenti/services/documenti.service';
import { Constants } from 'src/app/shared/constants/constants';
import {
  AggiornaDocumentoRequest,
} from 'src/app/shared/models/api/cosmoecm/aggiornaDocumentoRequest';
import {
  CreaDocumentoRequest,
} from 'src/app/shared/models/api/cosmoecm/creaDocumentoRequest';
import {
  TipoDocumento,
} from 'src/app/shared/models/api/cosmoecm/tipoDocumento';
import { DocumentoDTO } from 'src/app/shared/models/documento/documento.model';
import {
  ContentUploadResult,
} from 'src/app/shared/models/files/content-upload-result';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { ModalService } from 'src/app/shared/services/modal.service';
import {
  NotificationsWebsocketService,
} from 'src/app/shared/services/notifications-websocket.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import {
  TipiDocumentiService,
} from 'src/app/shared/services/tipi-documenti/tipi-documenti.service';
import { FileTypeUtils } from 'src/app/shared/utilities/file-type-utils';
import { Utils } from 'src/app/shared/utilities/utilities';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { ModaleParentComponent } from 'src/app/modali/modale-parent-component';
import { HelperService } from 'src/app/shared/services/helper.service';

@Component({
  selector: 'app-aggiungi-documento',
  templateUrl: './aggiungi-documento.component.html',
  styleUrls: ['./aggiungi-documento.component.scss']
})
export class AggiungiDocumentoComponent extends ModaleParentComponent implements OnInit {

  idPratica!: number;
  documento?: DocumentoDTO;
  parent?: DocumentoDTO;
  tipologieDocumentoCaricabili: TipoDocumento[] = [];
  tipologieAllegatiCaricabili: TipoDocumento[] = [];
  erroreConfigurazione!: string;

  documentoForm!: FormGroup;
  currentUser!: UserInfoWrapper;

  file: File | null = null;
  fileMimeDescription?: string;

  tipologieDocumento: TipoDocumento[] = [];

  loading = 0;
  loadingError: any | null = null;

  uploading = false;
  uploadingDesc = '';
  uploadingProgress = 0;
  uploadCancelSignal?: Subject<unknown>;

  dimensioniFileCorrette = true;

  codicePagina!: string;
  codiceTab!: string;
  codiceModale!: string;

  constructor(
    public modal: NgbActiveModal,
    private logger: NGXLogger,
    private securityService: SecurityService,
    private translateService: TranslateService,
    private modalService: ModalService,
    private documentiService: DocumentiService,
    private tipiDocumentiService: TipiDocumentiService,
    private notificationsWebsocketService: NotificationsWebsocketService,
    public helperService: HelperService) {
      super(helperService);
     }


  @HostListener('blur', ['$event.target']) onBlur(target: any) {
    console.log(`onBlur(): ${new Date()} - ${JSON.stringify(target)}`);
  }

  ngOnInit(): void {
    this.refresh();
    if (this.idPratica) {
      this.notificationsWebsocketService
        .whenEvent(Constants.APPLICATION_EVENTS.DOCUMENTI_CARICATI)
        .pipe(
          filter(e => e.payload.idPratica === this.idPratica),
          debounceTime(1000)
        )
        .subscribe(e => {
          // TODO sostituire con BusService
          this.documentiService.setCercaDocumenti(true);
        });
    }
    this.setModalName(this.codiceModale);
    this.searchHelperModale(this.codicePagina, this.codiceTab);
  }

  refresh() {
    this.loading++;
    this.loadingError = null;

    const tipiPratiche = this.documento?.tipo?.principali && this.documento?.tipo?.principali.length > 0
                       ? this.documento?.tipo?.principali[0].codice : this.parent?.tipo?.codice;

    forkJoin({
      user: this.securityService.getCurrentUser(),
      tipologieDocumento: this.tipiDocumentiService.getTipiDocumenti(this.idPratica, tipiPratiche ?? null)
    }).pipe(
      finalize(() => {
        this.loading--;
      }),
      tap(data => {
        if (this.parent && !(data.tipologieDocumento?.length)) {
          throw new Error('Nessuna tipologia di allegato disponibile per il documento selezionato.');
        }
      })
    )
      .subscribe(data => {
        this.currentUser = data.user;

        const tipologieConf = this.parent ? this.tipologieAllegatiCaricabili : this.tipologieDocumentoCaricabili;
        this.controllaTipiDocumenti(data.tipologieDocumento, tipologieConf, !!this.parent);
        if (!!this.parent) {
          this.tipologieDocumento = (data.tipologieDocumento ?? [])
          .filter(tipo => tipologieConf.length === 0 || tipologieConf.some(tipoCaricabile => tipoCaricabile.codice === tipo.codice
            && tipo.principali?.find(p => tipoCaricabile.principali && tipoCaricabile.principali.find(pp => p.codice === pp.codice))));
        } else {
          this.tipologieDocumento = (data.tipologieDocumento ?? [])
          .filter(tipo => tipologieConf.length === 0 || tipologieConf.some(tipoCaricabile => tipoCaricabile.codice === tipo.codice));
        }
        this.tipologieDocumento.sort((e1, e2) => (e1.descrizione ?? '')?.localeCompare((e2.descrizione ?? '')));
        this.initForm();

      }, failure => {
        this.loadingError = failure;
      });
  }

  nop() {
    // NOP
  }

  initForm() {

    let autore = null;
    if (this.currentUser) {
      autore = this.currentUser.nome + ' ' + this.currentUser.cognome;
    }

    let tipologiaIniziale = null;

    if (this.documento && this.documento.tipo) {
      const found = this.tipologieDocumento.find(tipo => tipo.codice === this.documento?.tipo?.codice);
      tipologiaIniziale = found ?? null;

    } else {
      tipologiaIniziale = this.tipologieDocumento?.length === 1 ? this.tipologieDocumento[0] : null;
    }

    this.documentoForm = new FormGroup({
      titolo: new FormControl(this.documento && this.documento.titolo ? this.documento.titolo : null),
      descrizione: new FormControl(this.documento && this.documento.descrizione ? this.documento?.descrizione : null),
      tipologia: new FormControl(tipologiaIniziale, [Validators.required]),
      autore: new FormControl(this.documento && this.documento.autore ? this.documento.autore : autore)
    });

    if (this.documento) {
      this.documentoForm.controls.tipologia.disable();
    }
  }


  controlloDimensioniFile() {
    this.dimensioniFileCorrette = this.checkDimensioniFile(this.file);
  }

  resolve(ctrlName: string): AbstractControl | null {
    return this.documentoForm.get(ctrlName);
  }

  displayErrors(ctrlName: string): boolean {
    const ctrl = this.resolve(ctrlName);
    if (ctrl) {
      return ctrl && ctrl.invalid && (ctrl.dirty || ctrl.touched || this.documentoForm?.touched);
    }
    return false;
  }

  getErrors(ctrlName: string): any {
    const ctrl = this.resolve(ctrlName);
    if (ctrl) {
      return ctrl.errors;
    }
    return null;
  }

  uploadFile(files: FileList) {
    if (this.uploading) {
      return;
    }

    if (files && files.item(0)) {
      const file = files.item(0);

      this.dimensioniFileCorrette = this.checkDimensioniFile(file);
      if (file) {
        this.file = file;
        this.fileMimeDescription = FileTypeUtils.getMimeDescription(file.type) || undefined;
      }
    }
  }

  cancellaFile() {
    this.file = null;
    this.fileMimeDescription = undefined;
  }

  cancellaMetadatiDocumento() {
    if (this.documento) {
      this.documento.titolo = undefined;
      this.documento.autore = undefined;
      this.documento.descrizione = undefined;
    }
  }

  pulisciCampi() {
    this.cancellaFile();
    this.cancellaMetadatiDocumento();
    this.initForm();
    this.dimensioniFileCorrette = this.checkDimensioniFile(this.file);
  }

  disableButton() {
    if (this.uploading) {
      return true;
    }
    if (!this.dimensioniFileCorrette) {
      return true;
    }
    if (this.documento && this.documento.id) {
      return !this.documentoForm.getRawValue().tipologia;
    } else {
      return (this.documentoForm && !this.documentoForm.valid) || !this.file || this.file.size === 0;
    }
  }

  public checkDimensioniFile(file: File | null): boolean {
    const tipologiaDocumentoSelezionata = this.documentoForm.getRawValue().tipologia;

    if (tipologiaDocumentoSelezionata && tipologiaDocumentoSelezionata.dimensioneMassima && file
      && (file.size / (1024 * 1024)) > tipologiaDocumentoSelezionata.dimensioneMassima) {
      return false;
    }
    return true;
  }

  private uploadFileToFileShare(): Observable<ContentUploadResult> {
    if (!this.file) {
      return throwError('file non presente');
    }

    const cancelSub = new Subject();
    this.uploadCancelSignal = cancelSub;

    // more than 1MB: use upload session
    if (this.file.size > 1024 * 1024) {
      return of(null).pipe(
        tap(() => {
          this.uploading = true;
          this.uploadingDesc = '';
          this.uploadingProgress = 0;
          this.documentoForm.disable();
        }),
        // tslint:disable-next-line: no-non-null-assertion
        mergeMap(() => this.documentiService.uploadFileWithUploadSession(this.file!, {
          cancellationSignal: cancelSub,
          onProgress: (percentage, desc) => {
            this.uploadingDesc = desc;
            this.uploadingProgress = Math.round(percentage);
          }
        })),
        map(result => ({
          uploadUUID: result.uploadUUID,
        })),
        finalize(() => {
          this.uploading = false;
          this.uploadingDesc = '';
          this.uploadingProgress = 0;
          this.documentoForm.enable();
        }),
      );
    }

    const formData = new FormData();

    // TODO: trovare un altro modo per inviare correttamente il nome del file anche con caratteri speciali
    formData.append('payload', this.file, encodeURIComponent(this.file.name));
    formData.append('maxSize', this.documentoForm.getRawValue().tipologia.dimensioneMassima ?? null);

    return this.documentiService.uploadFile(formData).pipe(
      map(result => ({
        uploadUUID: result.metadata.fileUUID,
      })),
    );
  }

  aggiungiDocumento() {
    if (!this.file) {
      return;
    }

    let uploaded: ContentUploadResult | null = null;

    // esegui upload del file
    this.uploadFileToFileShare().pipe(
      // verifica che ci sia l'UUID di upload
      tap(uploadResponse => {
        if (!uploadResponse?.uploadUUID) {
          throw new Error('Errore nel caricamento del file: UUID del file non presente');
        }
        uploaded = uploadResponse;
      }),
      // costruisci payload per creazione documento
      map(uploadResponse => {
        const payload: CreaDocumentoRequest = {
          titolo: this.documentoForm.get('titolo')?.value,
          descrizione: this.documentoForm.get('descrizione')?.value,
          codiceTipo: this.documentoForm.get('tipologia')?.value?.codice,
          autore: this.documentoForm.get('autore')?.value,
          uuidFile: uploadResponse.uploadUUID,
          parentId: this.parent ? this.parent.id + '' : undefined,
        };
        return payload;
      }),
      // invia request per creazione documento
      mergeMap(payload =>
        this.documentiService.salvaDocumento(this.idPratica, { documenti: [payload] })
      )
    ).subscribe(() => {
      // popup e aggiornamento lista documenti da consultare
      this.modalService.info(
        this.translateService.instant('documenti.carica_documento'),
        this.translateService.instant('documenti.carimento_documento')
      ).then(() => { }
      ).catch(() => { });

      // TODO sostituire con BusService
      this.documentiService.setCercaDocumenti(true);
      this.modal.close();

    }, failure => {
      if ('canceled' === failure) {
        return;
      }
      // mostra modale di errore
      this.logger.error('Errore nel caricamento del file', failure);
      this.erroreFile(failure);
      if (uploaded) {
        // elimina il file eventualmente caricato
        this.documentiService.deleteFile(uploaded.uploadUUID).subscribe();
      }
    });
  }

  private erroreFile(failure: any) {

    const messaggioErrore = Utils.toMessage(failure, Utils.parseAndReplacePlaceholders(
      this.translateService.instant('errori.caricamento_file_msg'),
      [this.file?.name ?? 'nessun file presente']));

    this.modalService.error(this.translateService.instant('errori.caricamento_file'),
      messaggioErrore, failure.error.errore)
      .then(() => { })
      .catch(() => { });
  }

  modificaDocumento() {
    if (!this.documento || !this.documento.id) {
      return;
    }

    const raw = this.documentoForm.getRawValue();
    const payload: AggiornaDocumentoRequest = {
      titolo: raw.titolo ?? null,
      descrizione: raw.descrizione ?? null,
      codiceTipo: raw.tipologia.codice,
      autore: raw.autore ?? null,
    };

    this.documentiService.aggiornaDocumento(this.documento.id, payload).subscribe(response => {

      // TODO sostituire con BusService
      this.documentiService.setCercaDocumenti(true);
      this.modal.close();

      this.modalService.info(
        this.translateService.instant('documenti.modifica_documento'),
        this.translateService.instant('documenti.aggiornamento_documento')
      ).then(() => { }
      ).catch(() => { });


    }, error => {
      this.modalService.error(this.translateService.instant('documenti.modifica_documento'),
        this.translateService.instant('errori.aggiornamento_documento_msg'), error.error.errore)
        .then(() => { })
        .catch(() => { });
    });
  }

  annullaCaricamento(): void {
    if (!this.uploading) {
      return;
    }

    if (this.uploadCancelSignal) {
      this.uploadCancelSignal.next();
      this.uploadCancelSignal = undefined;
    }
  }

  private controllaTipiDocumenti(tipiDoc: TipoDocumento[], tipologieConf: TipoDocumento[], isAllegato: boolean) {

    let nonEsiste: TipoDocumento | undefined;
    if (isAllegato) {
      nonEsiste = tipologieConf.find(tipoCaricabile => !tipiDoc.some(tipo => tipo.codice === tipoCaricabile.codice
         && tipo.principali?.find(p => tipoCaricabile.principali && tipoCaricabile.principali.find(pp => p.codice === pp.codice))));
    } else {
      nonEsiste = tipologieConf.find(tipoCaricabile => !tipiDoc.some(tipo => tipo.codice === tipoCaricabile.codice));
    }


    if (nonEsiste) {
      this.erroreConfigurazione = isAllegato ?
        this.translateService.instant('errori.cfg_tipi_allegati_caricabili_errata') :
        this.translateService.instant('errori.cfg_tipi_documenti_caricabili_errata');
    }

  }

  controlliSceltaTipologia() {
    this.controlloDimensioniFile();
  }
}
