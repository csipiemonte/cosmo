/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ModaleParentComponent } from 'src/app/modali/modale-parent-component';
import { DocumentiService } from 'src/app/shared/components/consultazione-documenti/services/documenti.service';
import { AnteprimaContenutoDocumento } from 'src/app/shared/models/api/cosmoecm/anteprimaContenutoDocumento';
import { ContenutoDocumento } from 'src/app/shared/models/api/cosmoecm/contenutoDocumento';
import { Documento } from 'src/app/shared/models/api/cosmoecm/documento';
import { HelperService } from 'src/app/shared/services/helper.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { Utils } from 'src/app/shared/utilities/utilities';

@Component({
  selector: 'app-anteprima-documento-modal',
  templateUrl: './anteprima-documento-modal.component.html',
  styleUrls: ['./anteprima-documento-modal.component.scss']
})
export class AnteprimaDocumentoModalComponent extends ModaleParentComponent implements OnInit {

  pageIndex = 0;
  documento?: Documento;
  contenuto?: ContenutoDocumento;
  contenutoFirmato?: ContenutoDocumento;
  loaded = false;
  codicePagina!: string;
  codiceTab!: string;
  codiceModale!: string;


  constructor(
    public activeModal: NgbActiveModal,
    private documentiService: DocumentiService,
    private modalService: ModalService,
    public helperService: HelperService
  ) {
    super(helperService);
  }



  ngOnInit(): void {
    this.setModalName(this.codiceModale);
    this.searchHelperModale(this.codicePagina, this.codiceTab);
  }

  hasLoaded(): void {
    this.loaded = true;
  }

  get supportaPreview(): boolean {
    if (this.isImage) {
      return false;
    }
    return this.contenuto?.formatoFile?.supportaAnteprima ?? false;
  }

  get supportaDownload(): boolean {
    if (this.isImage) {
      return false;
    }

    if (this.contenutoFirmato){
      return !!(this.contenutoFirmato?.id ?? 0);
    }else{
      return !!(this.contenuto?.id ?? 0);
    }
  }

  get anteprimaCorrente(): AnteprimaContenutoDocumento | null {
    return this.anteprime[this.pageIndex] ?? null;
  }

  get anteprime(): AnteprimaContenutoDocumento[] {
    if (this.contenuto?.anteprime?.length) {
      return this.contenuto.anteprime.sort((a1, a2) => a1.id - a2.id);
    }
    return [];
  }

  get numeroAnteprime(): number {
    return this.contenuto?.anteprime?.length ?? 0;
  }

  get hasNext(): boolean {
    return this.pageIndex < (this.numeroAnteprime - 1);
  }

  get hasPrevious(): boolean {
    return this.pageIndex > 0;
  }

  get isImage(): boolean {
    if (this.contenutoFirmato){
      return this.contenutoFirmato?.formatoFile?.mimeType?.startsWith('image/') ?? false;
    }else{
      return this.contenuto?.formatoFile?.mimeType?.startsWith('image/') ?? false;

    }
  }

  download(): void {
    const idContenuto = this.contenutoFirmato ? this.contenutoFirmato?.id : this.contenuto?.id;

    this.documentiService.downloadContenutoDocumento(this.documento?.id || 0, idContenuto || 0).subscribe(
      res => {
        this.activeModal.close('download');
        Utils.download(res);
      },
      err => this.modalService.simpleError(Utils.toMessage(err), err.error.errore)
    );
  }

  preview(): void {
    this.documentiService.previewContenutoDocumento(this.documento?.id || 0, this.contenuto?.id || 0).subscribe(
      res => {
        this.activeModal.close('preview');
        Utils.preview(res);
      },
      err => this.modalService.simpleError(Utils.toMessage(err), err.error.errore)
    );
  }
}
