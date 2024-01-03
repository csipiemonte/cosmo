/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { environment } from 'src/environments/environment';
import { ContenutoDocumento } from '../../models/api/cosmoecm/contenutoDocumento';
import { DocumentoDTO } from '../../models/documento/documento.model';
import { AnteprimaDocumentoModalComponent } from '../modals/anteprima-documento-modal/anteprima-documento-modal.component';
import { HelperService } from '../../services/helper.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-template-formato-documenti',
  templateUrl: './template-formato-documenti.component.html',
  styleUrls: ['./template-formato-documenti.component.scss']
})
export class TemplateFormatoDocumentiComponent implements OnInit {

  @Input() documento!: DocumentoDTO;
  @Input() codiceModale!: string;

  constructor(
    private modal: NgbModal,
    private helperService: HelperService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
  }

  iconaFormato(contenuto: ContenutoDocumento): string | null {
    if (!contenuto.formatoFile?.icona) {
      return null;
    }
    return environment.deployContextPath +
      '/assets/cwwcl/i/icons/filetypes/' +
      contenuto?.formatoFile?.icona + '.png';
  }

  clickIcona(contenuto?: ContenutoDocumento): void {
    if (!contenuto || !this.hasAnteprima(contenuto)) {
      return;
    }

    // apri il modale AnteprimaDocumentoModalComponent
    const data = this.helperService.searchHelperRef(this.route);
    const modalRef = this.modal.open(AnteprimaDocumentoModalComponent, { size: 'xl', backdrop: true, keyboard: true });
    modalRef.componentInstance.contenuto = contenuto;
    modalRef.componentInstance.contenutoFirmato = this.documento.contenutoFirmato;
    modalRef.componentInstance.documento = this.documento;
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = this.codiceModale;
    modalRef.result.then(() => {}, () => {});
  }

  hasAnteprima(contenuto?: ContenutoDocumento): boolean {
    return !!(contenuto?.anteprime?.length);
  }
}
