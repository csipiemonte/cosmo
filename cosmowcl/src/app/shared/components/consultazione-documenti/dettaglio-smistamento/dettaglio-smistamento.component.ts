/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit } from '@angular/core';
import { ClipboardService } from 'ngx-clipboard';
import { DocumentoDTO } from 'src/app/shared/models/documento/documento.model';

@Component({
  selector: 'app-dettaglio-smistamento',
  templateUrl: './dettaglio-smistamento.component.html',
  styleUrls: ['./dettaglio-smistamento.component.scss']
})
export class DettaglioSmistamentoComponent implements OnInit {

  constructor(private clipboardService: ClipboardService) { }

  infoCopied: any[] = [];

  @Input() documentoDTO: DocumentoDTO = { idPratica: -1};

  ngOnInit(): void {

    this.documentoDTO.smistamento?.infoAggiuntive?.forEach(info => {
      this.infoCopied.push({
        chiave: info.chiave,
        copied: false
      });
    });
  }

  findInfo(infoKey: string) {
    return this.infoCopied.find(info => info.chiave === infoKey);
  }

  copyInfo(infoKey: string): void {
    const valore = this.documentoDTO.smistamento?.infoAggiuntive?.find(info => info.chiave === infoKey)?.valore;
    this.clipboardService.copy(valore ? valore : '');
    this.findInfo(infoKey).copied = true;
    setTimeout(() => this.findInfo(infoKey).copied = false, 3000);
  }

}
