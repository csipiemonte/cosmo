/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, HostListener, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { ComponentCanDeactivate } from 'src/app/shared/guards/can-deactivate.guard';
import { HelperService } from 'src/app/shared/services/helper.service';
import { FileTypeUtils } from 'src/app/shared/utilities/file-type-utils';
import { HelperImportRequest } from 'src/app/shared/models/api/cosmonotifications/helperImportRequest';
import { ModalService } from 'src/app/shared/services/modal.service';
import { Utils } from 'src/app/shared/utilities/utilities';

@Component({
  selector: 'app-upload-file-modal',
  templateUrl: './upload-file-modal.component.html',
  styleUrls: ['./upload-file-modal.component.scss']
})
export class UploadFileModalComponent implements ComponentCanDeactivate{


  file: File | null = null;
  fileMimeDescription?: string;
  errorSize = false;
  errorType = false;
  @Input() title!: string;
  @Input() maxSize!: number;
  @Input() mimetypeList!: Array<string>;

  constructor(
    public modal: NgbActiveModal,
    private helperService: HelperService,
    private modalService: ModalService
  ) { }

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

  nop() {
    // NOP
  }

  uploadFile(files: FileList) {
    if (files && files.item(0)) {
      const file = files.item(0);
      this.errorSize = false;
      this.errorType = false;


      if (file) {
        this.file = file;
        this.fileMimeDescription = FileTypeUtils.getMimeDescription(file.type) || undefined;
        if (file.size > (this.maxSize)){
          this.errorSize = true;
        }
        if (this.mimetypeList && this.mimetypeList.length > 0) {
          // verifica mimetype
          this.errorType = !this.mimetypeList.find(mt => mt === this.file?.type);

        }
      }
    }
  }

  cancellaFile() {
    this.file = null;
    this.fileMimeDescription = undefined;
  }

  pulisciCampi(){
    this.errorSize = false;
    this.errorType = false;
    this.file = null;
  }

  importa() {
    if (this.file) {
      const reader = new FileReader();
      reader.readAsDataURL(this.file);
      reader.onload = () => {
        const resStr = (reader.result as string).split(',')[1];
        const payload: HelperImportRequest = {file: resStr};
        this.helperService.importa(
          payload
        ).subscribe(result => {
          if (result.status === 'OK') {
            this.modalService.info(
              'Procedura completata',
              'Importazione eseguita correttamente'
              ).then(() => {this.modal.close(); }).catch(() => {});
          } else {
            this.modalService.simpleError(Utils.toMessage(result.reason), result.reason);
          }
        }, failure => {
          this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore);
        });
        reader.onerror = (error: ProgressEvent<FileReader>) => {
          this.modalService.simpleError(Utils.toMessage(error));
        };
     };
    }
  }
}
