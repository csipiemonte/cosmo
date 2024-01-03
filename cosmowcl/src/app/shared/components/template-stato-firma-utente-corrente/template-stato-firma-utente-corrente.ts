/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Task } from '../../models/api/cosmobusiness/task';
import { EsitoVerificaFirma } from '../../models/api/cosmoecm/esitoVerificaFirma';
import { DocumentoDTO, TipoContenutoDocumentoEnum, TipoContenutoDocumentoFirmatoEnum } from '../../models/documento/documento.model';

@Component({
  selector: 'app-template-stato-firma-utente-corrente',
  templateUrl: './template-stato-firma-utente-corrente.html',
  styleUrls: ['./template-stato-firma-utente-corrente.scss']
})
export class TemplateStatoFirmaUtenteCorrenteComponent implements OnInit {

  @Input() documento!: DocumentoDTO;
  @Input() cfUtenteCorrente!: string;
  @Input() task!: Task;
  constructor(
    private modal: NgbModal
  ) { }

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
    return this.getEsitoVerificaFirma(document) != null &&
    this.hasFirmaUtenteCorrente(document);
  }

  esitoVerificaFirma(document: DocumentoDTO): boolean | null {
    const esito = this.getEsitoVerificaFirma(document);
    if (esito === null || !esito) {
      return null;
    }
    return esito.codice === 'VALIDA';
  }

  hasFirmaUtenteCorrente(document: DocumentoDTO): boolean {
    const res = document.contenuti?.filter(contenuti => contenuti.tipo?.codice === TipoContenutoDocumentoEnum.FIRMATO 
      && contenuti.tipoContenutoFirmato?.codice === TipoContenutoDocumentoFirmatoEnum.FIRMA_DIGITALE &&
      contenuti.infoVerificaFirme?.find(infoVerifiche => infoVerifiche.codiceFiscaleFirmatario === this.cfUtenteCorrente &&
        infoVerifiche.dataApposizione && this.task.createTime &&
        new Date(infoVerifiche.dataApposizione).toLocaleString().localeCompare(new Date(this.task.createTime).toLocaleString()) > 0));
    return res && res.length > 0 ? true : false;
  }

}
