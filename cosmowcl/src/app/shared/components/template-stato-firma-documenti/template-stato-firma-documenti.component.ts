/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DettaglioVerificaFirmeContainerComponent } from 'src/app/shared/components/consultazione-documenti/dettaglio-verifica-firme-container/dettaglio-verifica-firme-container.component';
import { EsitoVerificaFirma } from '../../models/api/cosmoecm/esitoVerificaFirma';
import { DocumentoDTO } from '../../models/documento/documento.model';
import { ActivatedRoute } from '@angular/router';
import { HelperService } from '../../services/helper.service';

@Component({
  selector: 'app-template-stato-firma-documenti',
  templateUrl: './template-stato-firma-documenti.component.html',
  styleUrls: ['./template-stato-firma-documenti.component.scss']
})
export class TemplateStatoFirmaDocumentiComponent implements OnInit {

  @Input() documento!: DocumentoDTO;
  @Input() codiceModale!: string;

  constructor(
    private modal: NgbModal,
    private helperService: HelperService,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
  }


  getEsitoVerificaFirma(document: DocumentoDTO): EsitoVerificaFirma | null {
    const esito = document?.ultimoContenutoFirmaDigitale?.esitoVerificaFirma;
    if (esito && esito.codice !== 'NON_VERIFICATA') {
      return esito;
    }
    return null;
  }

  hasFirma(document: DocumentoDTO): boolean {
    return this.getEsitoVerificaFirma(document) != null;
  }

  esitoVerificaFirma(document: DocumentoDTO): boolean | null {
    const esito = this.getEsitoVerificaFirma(document);
    if (esito === null || !esito) {
      return null;
    }
    return esito.codice === 'VALIDA';
  }

  apriModaleDettaglioFirma(documento: DocumentoDTO) {
    const data = this.helperService.searchHelperRef(this.route);
    const modalRef = this.modal.open(DettaglioVerificaFirmeContainerComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.firme = documento.ultimoContenutoFirmaDigitale?.infoVerificaFirme;
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = this.codiceModale;
    modalRef.result.then(() => {
    }, (reason) => { }
    );
  }
}
