/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-buttons-pratiche',
  templateUrl: './buttons-pratiche.component.html',
  styleUrls: ['./buttons-pratiche.component.scss']
})
export class ButtonsPraticheComponent implements OnInit {


  constructor(
     private router: Router
  ) { }

  ngOnInit()  {
  }

  goToCreazionePratica() {
     this.router.navigate(['crea-pratica']);
  }

  goToElencoLavorazioni() {
    this.router.navigate(['elenco-lavorazioni']);
  }

  goToEsecuzioneMultipla() {
    this.router.navigate(['esecuzione-multipla']);
  }

  goToCaricamentoPratiche() {
    this.router.navigate(['caricamento-pratiche']);
  }
}
