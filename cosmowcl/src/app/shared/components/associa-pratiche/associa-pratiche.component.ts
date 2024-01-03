/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit } from '@angular/core';
import { TipoRelazionePraticaPratica } from '../../models/api/cosmopratiche/tipoRelazionePraticaPratica';

@Component({
  selector: 'app-associa-pratiche',
  templateUrl: './associa-pratiche.component.html',
  styleUrls: ['./associa-pratiche.component.scss']
})
export class AssociaPraticheComponent implements OnInit {

  @Input() messaggiErroriConfigurazione: string[] = [];
  @Input() defaultTipoAssociazione!: string;
  @Input() descrizioneInfo!: string;

  @Input() tipiRelazionePratica: TipoRelazionePraticaPratica[] = [];
  tipoRelazioneSelezionata?: TipoRelazionePraticaPratica;

  constructor() { }

  ngOnInit(): void {
    if (this.defaultTipoAssociazione && this.tipiRelazionePratica.length > 0 ){
      const found = this.tipiRelazionePratica.find(item => item.codice === this.defaultTipoAssociazione);
      if (found){
        this.tipoRelazioneSelezionata = found;
      }
    }
  }

}
