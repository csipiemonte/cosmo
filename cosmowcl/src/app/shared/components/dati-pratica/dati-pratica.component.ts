/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit } from '@angular/core';
import { Pratica } from '../../models/api/cosmobusiness/pratica';
import { VariabileProcesso } from '../../models/api/cosmobusiness/variabileProcesso';
import { PraticheService } from '../../services/pratiche.service';

@Component({
  selector: 'app-dati-pratica',
  templateUrl: './dati-pratica.component.html',
  styleUrls: ['./dati-pratica.component.scss']
})
export class DatiPraticaComponent implements OnInit {

  @Input() pratica!: Pratica;
  @Input() formCodice!: string;
  variabiliProcesso!: VariabileProcesso[];

  constructor( private praticheService: PraticheService ) {

  }

  ngOnInit(): void {
    const processId = this.pratica?.linkPratica?.split('/');
    if (processId && processId.length === 3 ){

      if (this.pratica.dataFinePratica){
        this.praticheService.getHistoryVariabiliProcesso(processId[2], true).subscribe(response => {
          this.variabiliProcesso = response.variabili || [];
        });
      } else{
        this.praticheService.getVariabiliProcesso(processId[2]).subscribe(response => {
          this.variabiliProcesso = response.variabili || [];
        });
      }
    }
  }

}
