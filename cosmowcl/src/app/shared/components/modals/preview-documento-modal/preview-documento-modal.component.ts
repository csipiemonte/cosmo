/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, Input, TemplateRef } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { ContenutoDocumento } from 'src/app/shared/models/api/cosmoecm/contenutoDocumento';
import { DocumentoDTO } from 'src/app/shared/models/documento/documento.model';
import { DocumentiService } from '../../consultazione-documenti/services/documenti.service';
import { ModaleParentComponent } from 'src/app/modali/modale-parent-component';
import { HelperService } from 'src/app/shared/services/helper.service';

@Component({
  selector: 'app-preview-documento-modal',
  templateUrl: './preview-documento-modal.component.html',
  styleUrls: ['./preview-documento-modal.component.scss']
})
export class PreviewDocumentoModalComponent extends ModaleParentComponent implements OnInit{

  @Input() doc!: DocumentoDTO;
  @Input() contenuto!: ContenutoDocumento;
  @Input() file!: File;
  @Input() url!: string;
  @Input() textLayer = true;
  @Input() ignoreKeyboard = false;
  @Input() ignoreKeys = [];
  @Input() riquadroFirma = false;
  @Input() zoom: string | number | undefined;
  @Input() showZoomButtons = true;
  @Input() pageViewMode!: 'single' | 'book' | 'multiple' | 'infinite-scroll';
  @Input() templateType!: 'classic' | 'void';
  body !: any;
  loadingError: any = null;
  loading = true;
  codicePagina!: string;
  codiceTab!: string;
  codiceModale!: string;

  constructor(public modal: NgbActiveModal,
              public documentiService: DocumentiService,
              public translateService: TranslateService,
              public helperService: HelperService) {
    super(helperService);
  }


  ngOnInit(): void {
    /* ad oggi: precedenza alla tipologia file: se non e' presente, verifico i documenti su index */
    if (this.file && this.file.size > 0) {
      this.body = this.file;
      this.loading = false;
    }else if (this.url && this.url.length > 0) {
      this.body = this.url;
      this.loading = false;
    } else {
      this.documentiService.getContenutoIndex(this.doc.id || 0, this.contenuto.id || 0).subscribe(
        res => {
          this.loading = false;
          this.body = res;
        },
        error => {
          this.loading = false;
          this.loadingError = this.translateService.instant('errori.preview_file');
        }
      );
    }
    this.setModalName(this.codiceModale);
    this.searchHelperModale(this.codicePagina, this.codiceTab);
  }



}
