/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit } from '@angular/core';
import { DocumentoDTO } from 'src/app/shared/models/documento/documento.model';

@Component({
  selector: 'app-info-documento',
  templateUrl: './info-documento.component.html',
  styleUrls: ['./info-documento.component.scss']
})
export class InfoDocumentoComponent implements OnInit {

  @Input() documento: DocumentoDTO = { idPratica: -1};

  constructor() { }

  ngOnInit(): void {
  }

  getShaFile(): string{

    if (this.documento.contenutoEffettivo && this.documento.contenutoEffettivo.shaFile){
      return this.documento.contenutoEffettivo.shaFile;
    }

    return '--';
  }
}
