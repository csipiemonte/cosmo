/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { ParentControl } from '../parent-control';

@Component({
  selector: 'app-simpleform-spacer',
  templateUrl: './spacer.component.html',
  styleUrls: ['./spacer.component.scss']
})
export class SpacerComponent extends ParentControl implements OnInit {

  constructor() {
    super();
   }

  ngOnInit(): void {
  }

}
