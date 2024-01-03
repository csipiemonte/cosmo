/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-tab-singolo',
  templateUrl: './tab-singolo.component.html',
  styleUrls: ['./tab-singolo.component.scss']
})
export class TabSingoloComponent implements OnInit {

  @Input() condizioneLista = true;
  @Input() nomeTabAttivo!: string;
  @Input() id!: string;
  @Input() ariaControls!: string;
  @Input() descrizioneTab!: string;
  @Input() condizioneElemento = true;

  @Input() tabAttivo?: string;
  @Output() tabActivated = new EventEmitter<string>();
  @Output() lazyLoadedEmitter = new EventEmitter<{[key: string]: true}>();

  lazyLoaded: {[key: string]: true} = {};


  constructor() { }

  ngOnInit(): void {
    if (this.tabAttivo && !this.lazyLoaded[this.tabAttivo]) {
      this.activateTab(null, this.tabAttivo);
    }
  }

  activateTab(event: Event | null, tabRef: string, inner = false) {
    if (!tabRef) {
      return;
    }
    this.lazyLoaded[tabRef] = true;
    this.lazyLoadedEmitter.next(this.lazyLoaded);
    const prev = this.tabAttivo;
    this.tabAttivo = tabRef;

    if (inner && prev !== tabRef) {
      this.tabActivated.next(tabRef);
    }

    if (event) {
      event.stopPropagation();
    }
  }


  activateTabInner(event: Event | null, tabRef: string) {
    this.activateTab(event, tabRef, true);
  }
}
