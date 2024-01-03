/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { FieldNames } from '../../field-names';
import { ParentControl } from '../parent-control';

@Component({
  selector: 'app-simpleform-checkbox',
  templateUrl: './checkbox.component.html',
  styleUrls: ['./checkbox.component.scss']
})
export class CheckboxComponent extends ParentControl implements OnInit {

  constructor() {
    super();

   }

  ngOnInit(): void {
    if (!this.formField.id || this.formField.id.trim().length === 0) {
      this.formField.id = FieldNames.CHECKBOX + '-' + this.getUniqueId();
    }
  }

}
