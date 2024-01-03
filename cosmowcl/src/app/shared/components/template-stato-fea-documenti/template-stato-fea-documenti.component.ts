/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DocumentoDTO, TipoContenutoDocumentoFirmatoEnum } from '../../models/documento/documento.model';
import { DettaglioInformazioniFeaComponent } from '../consultazione-documenti/dettaglio-informazioni-fea/dettaglio-informazioni-fea.component';
import { HelperService } from '../../services/helper.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-template-stato-fea-documenti',
  templateUrl: './template-stato-fea-documenti.component.html',
  styleUrls: ['./template-stato-fea-documenti.component.scss']
})
export class TemplateStatoFeaDocumentiComponent implements OnInit {


  @Input() documento!: DocumentoDTO;
  @Input() codiceModale!: string;

  constructor(
    private modal: NgbModal,
    private helperService: HelperService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
  }
  hasFirma(): boolean {
    return this.documento?.contenuti?.
    find(contenuto => contenuto.tipoContenutoFirmato?.codice === TipoContenutoDocumentoFirmatoEnum.FEA) ? true : false;
  }


  apriModaleDettaglioFirma() {
    const data = this.helperService.searchHelperRef(this.route);
    const modalRef = this.modal.open(DettaglioInformazioniFeaComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.documento = this.documento;
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = this.codiceModale;
    modalRef.result.then(() => {
    }, (reason) => { }
    );
  }

}
