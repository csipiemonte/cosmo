/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { ParentControl } from '../parent-control';

@Component({
  selector: 'app-simpleform-horizontal-line',
  templateUrl: './horizontal-line.component.html',
  styleUrls: ['./horizontal-line.component.scss']
})
export class HorizontalLineComponent extends ParentControl implements OnInit {

  constructor() {
    super();
   }

  ngOnInit(): void {
  }

}
