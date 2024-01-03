/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit } from '@angular/core';
import { DocumentoDTO } from '../../models/documento/documento.model';
import { StatoDocumentoEnum } from '../../models/stato-documento/stato-documento.model';

@Component({
  selector: 'app-template-alert-documenti',
  templateUrl: './template-alert-documenti.component.html',
  styleUrls: ['./template-alert-documenti.component.scss']
})
export class TemplateAlertDocumentiComponent implements OnInit {

  @Input() documento!: DocumentoDTO;

  constructor() { }

  ngOnInit(): void {
  }

  isErrored(document: DocumentoDTO): boolean {
    switch (document.stato?.codice) {
      case StatoDocumentoEnum.ELABORATO:
      case StatoDocumentoEnum.ACQUISITO:
      case StatoDocumentoEnum.IN_ELABORAZIONE: return false;
      default: return true;
    }
  }

  isPending(document: DocumentoDTO): boolean {
    switch (document.stato?.codice) {
      case StatoDocumentoEnum.ACQUISITO:
      case StatoDocumentoEnum.IN_ELABORAZIONE: return true;
      default: return false;
    }
  }

  isGiusto(document: DocumentoDTO): boolean {
    return StatoDocumentoEnum.ELABORATO === document.stato?.codice;
  }

}
