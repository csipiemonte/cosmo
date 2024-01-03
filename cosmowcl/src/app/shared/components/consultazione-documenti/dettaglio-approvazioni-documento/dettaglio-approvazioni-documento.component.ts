/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit } from '@angular/core';
import { DocumentoDTO } from 'src/app/shared/models/documento/documento.model';

@Component({
  selector: 'app-dettaglio-approvazioni-documento',
  templateUrl: './dettaglio-approvazioni-documento.component.html',
  styleUrls: ['./dettaglio-approvazioni-documento.component.scss']
})
export class DettaglioApprovazioniDocumentoComponent implements OnInit {

  @Input() documentoDTO: DocumentoDTO = { idPratica: -1};

  constructor() { }

  ngOnInit(): void {

  }

}
