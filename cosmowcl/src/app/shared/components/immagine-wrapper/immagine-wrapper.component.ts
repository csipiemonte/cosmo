/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, EventEmitter, Input, OnInit, Output, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-immagine-wrapper',
  templateUrl: './immagine-wrapper.component.html',
  styleUrls: ['./immagine-wrapper.component.scss'],
})
export class ImmagineWrapperComponent implements OnInit {

  @Input() src?: string;
  @Input() class?: string;
  @Output() loaded = new EventEmitter<void>();

  isImgLoaded = false;

  constructor() { }

  ngOnInit(): void {
  }

  hasLoaded(): void {
    this.isImgLoaded = true;
    this.loaded.next();
  }

}
