/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { EsitoVerificaFirma } from '../../models/api/cosmoecm/esitoVerificaFirma';
import { DocumentoDTO } from '../../models/documento/documento.model';
import { DettaglioInformazioniSigilloElettronicoComponent } from '../consultazione-documenti/dettaglio-informazioni-sigillo-elettronico/dettaglio-informazioni-sigillo-elettronico.component';
import { SigilloDocumento } from '../../models/api/cosmoecm/sigilloDocumento';
import { ActivatedRoute } from '@angular/router';
import { HelperService } from '../../services/helper.service';

@Component({
  selector: 'app-template-stato-sigillo-elettronico-documenti',
  templateUrl: './template-stato-sigillo-elettronico-documenti.component.html',
  styleUrls: ['./template-stato-sigillo-elettronico-documenti.component.scss']
})
export class TemplateStatoSigilloElettronicoDocumentiComponent implements OnInit {

  @Input() documento!: DocumentoDTO;
  @Input() codiceModale!: string;

  constructor(
    private modal: NgbModal,
    private helperService: HelperService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
  }


  getEsitoVerificaFirma(document: DocumentoDTO): EsitoVerificaFirma | null {
    const esito = document?.ultimoContenutoSigilloElettronico?.esitoVerificaFirma;
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
    const modalRef = this.modal.open(DettaglioInformazioniSigilloElettronicoComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.firme = documento.ultimoContenutoSigilloElettronico?.infoVerificaFirme;
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = this.codiceModale;
    modalRef.result.then(() => {
    }, (reason) => { }
    );
  }

  public sortByDtInserimento(x: Array<SigilloDocumento>): Array<SigilloDocumento> {
    return x.sort((a: SigilloDocumento, b: SigilloDocumento) => {
      return ((a.dtInserimento ?? '') > (b.dtInserimento ?? '') ? -1 : 1);
    });
  }

  public getActualStatus() {
    const sortedSealed = this.sortByDtInserimento(this.documento.sigillo ?? []);
    return sortedSealed && sortedSealed.length > 0 ? sortedSealed[0].codiceStato : '';
  }



}
