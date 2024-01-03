/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { FieldNames } from '../../field-names';
import { ParentControl } from '../parent-control';

@Component({
  selector: 'app-simpleform-text',
  templateUrl: './text.component.html',
  styleUrls: ['./text.component.scss', '../controls.component.scss']
})
export class TextComponent extends ParentControl implements OnInit {

  type = '';

  constructor() {
    super();

  }

  ngOnInit(): void {
    if (!this.formField.id || this.formField.id.trim().length === 0) {
      this.formField.id = FieldNames.TEXT + '-' + this.getUniqueId();
    }
    // console.log(this.controls);
    switch (this.formField.type) {
      case FieldNames.PASSWORD:
        this.type = 'password';
        break;
      case FieldNames.INTEGER_NUMBER:
        this.type = 'number';
        break;
      case FieldNames.DECIMAL_NUMBER:
        this.type = 'number';
        break;
      default:
        this.type = 'text';
    }
    console.log('CONTROLS ARE', this.controls);
  }

}
