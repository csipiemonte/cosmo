/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, HostListener, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { Observable, Subject } from 'rxjs';
import { ModaleParentComponent } from 'src/app/modali/modale-parent-component';
import { Constants } from 'src/app/shared/constants/constants';
import { ComponentCanDeactivate } from 'src/app/shared/guards/can-deactivate.guard';
import { CaricamentoPraticheService } from 'src/app/shared/services/caricamento-pratiche.service';
import { ConfigurazioniService } from 'src/app/shared/services/configurazioni.service';
import { HelperService } from 'src/app/shared/services/helper.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { FileTypeUtils } from 'src/app/shared/utilities/file-type-utils';

@Component({
  selector: 'app-aggiungi-pratiche-documenti-modal',
  templateUrl: './aggiungi-pratiche-documenti-modal.component.html',
  styleUrls: ['./aggiungi-pratiche-documenti-modal.component.scss']
})
export class AggiungiPraticheDocumentiModalComponent extends ModaleParentComponent implements OnInit, ComponentCanDeactivate {

  pratica!: boolean;
  file: File | null = null;
  uploading = false;
  uploadingDesc = '';
  uploadingProgress = 0;
  errorSize = false;
  errorType = false;
  errorZipPresente = false;

  maxSize = 0;
  loading = 0;
  loadingError: any | null = null;
  uploadCancelSignal?: Subject<unknown>;
  folderName!: string;
  documentiInseriti!: string[];

  codicePagina!: string;
  codiceTab!: string;
  codiceModale!: string;

  constructor(
    private configurazioneService: ConfigurazioniService,
    private translateService: TranslateService,
    private caricamentoPraticheService: CaricamentoPraticheService,
    private modalService: ModalService,
    public modal: NgbActiveModal,
    public helperService: HelperService
  ) {
    super(helperService);
  }
  @HostListener('window:beforeunload')
  canDeactivate(): Observable<boolean> | boolean {
    // insert logic to check if there are pending changes here;
    // returning true will navigate without confirmation
    // returning false will show a confirm dialog before navigating away
    if (this.file) {
      return false;
    }
    return true;
  }


  ngOnInit(): void {
    this.loading++;
    this.loadingError = null;


    this.configurazioneService.getConfigurazioneByChiave(this.pratica ? Constants.PARAMETRI.EXCEL_PRATICHE_MAX_SIZE :
    Constants.PARAMETRI.ZIP_MAX_SIZE)
    .subscribe(
      response => {
        this.maxSize = +(response ?? 0);
        this.loading--;
      },
      error => {
        this.loading--;
        this.loadingError = error.error;
      }
    );
    this.setModalName(this.codiceModale);
    this.searchHelperModale(this.codicePagina, this.codiceTab);
  }

  uploadFile(files: FileList) {

    if (files && files.item(0)) {
      const file = files.item(0);
      this.errorSize = false;
      this.errorType = false;
      this.errorZipPresente = false;
      if (file) {
        this.file = file;
        if (file.size > (this.maxSize)){
          this.errorSize = true;
        }
        if ((this.pratica && !FileTypeUtils.isFoglioDiCalcolo(this.file.type))
            || (!this.pratica && !FileTypeUtils.isArchivioZip(this.file.type))){
          this.errorType = true;
        }

        if (!this.pratica && this.documentiInseriti.find(elem => elem === file.name)){
          this.errorZipPresente = true;
        }
      }
    }
  }

  aggiungiPratiche(){

    if (this.file){
      const formData = new FormData();

      formData.append('payload', this.file, encodeURIComponent(this.file.name));

      this.caricamentoPraticheService.uploadPratiche(formData).subscribe(
        response => {
          const result: Partial<File> = {name: this.file?.name, size: this.file?.size};
          this.modal.close({file: result, pathfile: response.folderName});
          this.modalService.simpleInfo(response.esito?.title ?? '').finally().catch();
        },
        error => {
          const messaggioErrore = error.error.title ? error.error.title : this.translateService.instant('caricamento_pratiche.errore_caricamento_pratiche');
          this.modal.dismiss();
          this.modalService.error(this.translateService.instant('caricamento_pratiche.titolo'),
            messaggioErrore, error.error.errore)
            .then()
            .catch(() => { });
        }
      );

    }
  }

  aggiungiDocumento(){
    if (this.file){
      const formData = new FormData();

      formData.append('payload', this.file, encodeURIComponent(this.file.name));

      const cancelSub = new Subject();
      this.uploadCancelSignal = cancelSub;

      this.uploading = true;
      this.uploadingDesc = '';
      this.uploadingProgress = 0;
      this.caricamentoPraticheService.uploadDocumentiZipWithUploadSession(this.file,
        this.folderName ?? '',
        {
        cancellationSignal: cancelSub,
          onProgress: (percentage, desc) => {
            this.uploadingDesc = desc;
            this.uploadingProgress = Math.round(percentage);
          }
      })
      .subscribe(response => {
        const result: Partial<File> = {name: this.file?.name, size: this.file?.size};
        this.uploading = false;
        this.uploadingDesc = '';
        this.uploadingProgress = 0;
        this.modal.close({file: result});
        this.modalService.info(this.translateService.instant('caricamento_pratiche.titolo'), this.translateService.instant('caricamento_pratiche.caricato_zip')).then().catch();

        },
        failure => {
          this.uploading = false;
          this.uploadingDesc = '';
          this.uploadingProgress = 0;
          if ('canceled' === failure) {
            return;
          }
          this.modal.dismiss();
          const messaggioErrore = failure.error.title ? failure.error.title : this.translateService.instant('caricamento_pratiche.errore_caricamento_pratiche');
          this.modalService.error(this.translateService.instant('errori.caricamento_file'),
          messaggioErrore, failure.error.errore)
          .then(() => { })
          .catch(() => { });
        }
      );

    }

  }

  pulisciCampi(){
    this.errorSize = false;
    this.errorType = false;
    this.file = null;
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


}
