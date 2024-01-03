/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-riassunto',
  templateUrl: './riassunto.component.html',
  styleUrls: ['./riassunto.component.scss'],
  encapsulation: ViewEncapsulation.ShadowDom
})
export class RiassuntoComponent implements OnInit {

  @Input() riassunto = '';

  constructor() { }

  ngOnInit(): void {
  }

}
