/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormioComponent } from '@formio/angular';
import { NGXLogger } from 'ngx-logger';
import { FormioDefaultRenderOptions } from '../../configuration/formio-config';
import { CustomCallbackResponse } from '../../models/api/cosmobusiness/customCallbackResponse';
import { VariabileProcesso } from '../../models/api/cosmobusiness/variabileProcesso';
import { CustomFormService } from '../../services/customForm.service';
import { Utils } from '../../utilities/utilities';
import { CustomFormComponentElement } from './models/custom-form-component-element';
import { CustomFormElementType } from './models/custom-form-element-type';
import { CustomFormField } from './models/custom-form-field';
import { Italian } from 'flatpickr/dist/l10n/it';

@Component({
  selector: 'app-form-formio',
  templateUrl: './form-formio.component.html',
  styleUrls: ['./form-formio.component.scss']
})
export class FormFormioComponent implements OnInit {


  @Input() readOnly = false;
  @Input() formCodice!: string;
  @Input() variabiliProcesso: VariabileProcesso[] = [];

  @Output() compEmitter: EventEmitter<CustomFormField[]> = new EventEmitter<CustomFormField[]>();

  @ViewChild('formioComponent') formioComponent: FormioComponent | null = null;

  formJson: object | undefined;
  submission = { data: {} };
  submissionKeys: string[] = [];
  components: any;
  parsedCustomForm: any;
  formReady = false;

  renderOptions = FormioDefaultRenderOptions;

  constructor(
    private logger: NGXLogger,
    private customFormService: CustomFormService) { }

  ngOnInit(): void {
    this.renderOptions.readOnly = this.readOnly;

    this.customFormService.get(this.formCodice).subscribe(cf => {
      if (cf) {
        this.parsedCustomForm = JSON.parse(cf.customForm ?? '');
        this.components = Utils.getPropertyValue(this.parsedCustomForm, 'components');
        this.initializeForm();
        this.compEmitter.emit(this.components);
      }
    });
  }

  onChange(event: any) {
    if (event.type && event.type === 'change') {
      return;
    }
    this.compEmitter.emit(this.components);
  }

  public isValid(): boolean {
    return this.formioComponent?.formio.checkValidity();
  }

  private initializeForm(): void {
    // save current variables
    const context = {};
    this.variabiliProcesso.forEach(variabile => {
      if (variabile.name) {
        Utils.setPropertyValue(context, variabile.name, variabile.value);
      }
    });
    Utils.setPropertyValue(this.submission.data, 'context', context);

    // save initial values of components,
    // optionally overriding variables values
    if (this.components) {
      this.setComponentsValue(this.components);
      this.formJson = this.parsedCustomForm;
    }

    setTimeout(() => {
      this.logger.info('custom form is now ready');
      this.formReady = true;
    }, 500);
  }

  private setComponentsValue(components: CustomFormField[]) {

    components.forEach(component => {
      if (component.type === CustomFormElementType.DATAGRID) {
        this.setValue(component);

      } else if (component.components) {
        this.setComponentsValue(component.components);

      } else if (component.columns) {
        component.columns.forEach((column: CustomFormComponentElement) => {
          this.setComponentsValue(column.components);
        });

      } else if (component.rows && component.rows instanceof Array) {
        component.rows.forEach((row: CustomFormComponentElement[]) => {
          row.forEach(element => {
            this.setComponentsValue(element.components);
          });
        });

      } else {
        if (component.type === CustomFormElementType.SELECT) {
          this.creatSelectValues(component);

        } else {
          this.setValue(component);
        }
      }
    });
  }

  private creatSelectValues(field: CustomFormField) {

    const initialValue = this.getInitialValue(field.key + '_value', this.variabiliProcesso);
    if (initialValue) {
      Utils.setPropertyValue(this.submission.data, field.key, initialValue);
    }

    if (field.dataSrc === 'url') {
      const url = this.creaUrl(field.key);
      if (url) {
        Utils.setPropertyValue(this.submission.data, field.key + '_url', url);
      }
    }

    this.submissionKeys.push(field.key + '_value');
  }

  private creaUrl(fieldkey: string) {
    const value = this.variabiliProcesso.find(vp => vp.name === fieldkey);

    if (value && value.value && (value.value as CustomCallbackResponse).url) {
      return (value.value as CustomCallbackResponse).url;
    }
    return null;
  }

  private getInitialValue(fieldKey: string, variabiliProcesso: VariabileProcesso[]) {
    const found: VariabileProcesso | undefined = variabiliProcesso.find(variabile => fieldKey === variabile.name);
    return found && found.value ? found.value : null;
  }

  private setValue(component: CustomFormField){

    if (component.type === CustomFormElementType.DATE_TIME) {
      component.customOptions = {locale: Italian};
    }
    const initialValue = this.getInitialValue(component.key, this.variabiliProcesso);

    if (initialValue) {
      Utils.setPropertyValue(this.submission.data, component.key, initialValue);
    }
    this.submissionKeys.push(component.key);
  }
}
