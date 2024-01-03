/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
  ViewChild,
} from '@angular/core';

import { Pratica } from '../shared/models/api/cosmopratiche/pratica';
import { TabsDettaglio } from '../shared/models/api/cosmopratiche/tabsDettaglio';
import { TabsDettaglioService } from '../shared/services/tabs-dettaglio.service';
import { TabSingoloComponent } from './tab-singolo/tab-singolo.component';

@Component({
  selector: 'app-tabs-pratica',
  templateUrl: './tabs-pratica.component.html',
  styleUrls: ['./tabs-pratica.component.scss']
})
export class TabsPraticaComponent implements OnInit, OnDestroy {

  @Input() tabAttivo?: string;
  @Input() pratica!: Pratica;
  @Input() codiceForm!: string;
  @Input() embedded!: boolean;

  @Output() tabActivated = new EventEmitter<string>();

  @ViewChild('tabSingolo') tabSingoloComponent: TabSingoloComponent | null = null;

  dettTab: TabsDettaglio[] = [];
  idPratica!: number;
  lazyLoaded: { [key: string]: true } = {};

  constructor(
    private tabsDettaglioService: TabsDettaglioService) {
  }

  get isEsterna(): boolean {
    return this.pratica?.esterna ?? false;
  }

  ngOnDestroy(): void {
    // NOP
  }

  ngOnInit(): void {

    if (this.pratica.tipo?.codice) {
      this.getTabsDettaglioCodicePratica(this.pratica.tipo?.codice);
    }
    // tslint:disable-next-line: no-non-null-assertion
    this.idPratica = this.pratica.id!;

  }

  cleanTabRef(raw: string): string {
    if (!raw) {
      return raw;
    }
    return raw.replace('#', '');
  }

  getTabsDettaglioCodicePratica(codiceTipoPratica: string) {
    this.tabsDettaglioService.getTabsDettaglioCodiceTipoPratica(codiceTipoPratica).subscribe(data => {
      this.dettTab = data;
      if (this.dettTab.length === 0) {
        this.setEmptyConfig();
      }else if(this.embedded){
        this.tabAttivo= this.dettTab[0].codice;
      }
    });
  }

  tabActivatedEmitter(raw: string) {
    if(this.embedded){
      this.tabAttivo=raw;
    }
    this.tabActivated.next(raw);
  }

  setLazyLoaded(event: { [key: string]: true }) {
    this.lazyLoaded = event;
  }

  setEmptyConfig() {
    this.tabsDettaglioService.getTabsDettaglio().subscribe(response =>

      {
        response.forEach(element => {
          this.dettTab.push({
            codice: element.codice,
            descrizione: element.descrizione,
            ordine: response.indexOf(element),
          });
        });
        if(this.embedded){
          this.tabAttivo=this.dettTab[0].codice;
        }
      }
    );
  }

}
