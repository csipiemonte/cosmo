/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { FieldNames } from '../../field-names';
import { ParentControl } from '../parent-control';

@Component({
  selector: 'app-simpleform-dropdown',
  templateUrl: './dropdown.component.html',
  styleUrls: ['./dropdown.component.scss', '../controls.component.scss']
})
export class DropdownComponent extends ParentControl implements OnInit {

  constructor() {
    super();
   }

  ngOnInit(): void {
    if (!this.formField.id || this.formField.id.trim().length === 0) {
      this.formField.id = FieldNames.RADIO + '-' + this.getUniqueId();
    }
    if (this.formField.options) {
      for (const option of this.formField.options) {
        if (!option.id || option.id.trim().length === 0) {
          option.id = this.formField.id + '-option-' + this.getUniqueId();
        }
      }
    }
  }

}
