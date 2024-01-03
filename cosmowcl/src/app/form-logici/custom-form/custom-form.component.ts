/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  Component,
  Injector,
  OnInit,
  ViewChild,
} from '@angular/core';
import { Router } from '@angular/router';
import { NGXLogger } from 'ngx-logger';
import { ModalService } from 'src/app/shared/services/modal.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { TranslateService } from '@ngx-translate/core';
import { FunzionalitaParentComponent } from '../funzionalita-parent.component';
import { FlowableOutput } from '../models/flowable-output-model';
import { CustomFormComponentElement } from '../../shared/components/form-formio/models/custom-form-component-element';
import { CustomFormElementType } from '../../shared/components/form-formio/models/custom-form-element-type';
import { CustomFormField } from '../../shared/components/form-formio/models/custom-form-field';
import { FormFormioComponent } from 'src/app/shared/components/form-formio/form-formio.component';

@Component({
  selector: 'app-custom-form',
  templateUrl: './custom-form.component.html',
  styleUrls: ['./custom-form.component.scss']
})
export class CustomFormComponent extends FunzionalitaParentComponent implements OnInit {

  formCodice!: string;
  components: CustomFormField[] = [];
  payload: FlowableOutput[] = [];

  @ViewChild('formFormio') formFormio: FormFormioComponent | null = null;

  constructor(
    public injector: Injector,
    private logger: NGXLogger,
    private router: Router,
    private modalService: ModalService,
    private translateService: TranslateService,
  ) {
    super(injector);

    if (this.parametri?.get('CODICE_CUSTOM_FORM')) {
      this.formCodice = '' + this.parametri.get('CODICE_CUSTOM_FORM');
    } else {
      this.showModalAndGoHome();
    }
  }

  ngOnInit(): void {
  }

  public isValid(): boolean {
    if (this.isSubtask) {
      return true;
    }
    return this.formFormio?.isValid() || false;
  }

  public isChanged(): boolean {
      return this.dataChanged;
  }

  public createComponents(components: CustomFormField[]){
    this.components = components;
    this.createPayloadAndSend();
  }

  beforeSave() {
    this.createPayloadAndSend();
  }

  public createPayloadAndSend(){
    if (this.readOnly) {
      return;
    }

    const oldPayload = this.payload;
    this.payload = [];
    this.createPayload(this.components);
    this.sendData({ payload: this.payload });
    this.dataChanged = this.compareOldAndNewPayload(oldPayload, this.payload);
  }

  private createPayload(components: CustomFormField[]) {

    components.forEach((component: CustomFormField) => {

      if (component.type === CustomFormElementType.DATAGRID){
        this.getVariables(component);

      }
      else if (component.components) {
        this.createPayload(component.components);

      } else if (component.columns) {
        component.columns.forEach((column: CustomFormComponentElement) => {
          this.createPayload(column.components);
        });

      } else if (component.rows && component.rows instanceof Array) {
        component.rows.forEach((row: CustomFormComponentElement[]) => {
          row.forEach(element => {
            this.createPayload(element.components);
          });
        });

      } else {
        this.getVariables(component);
      }
    });
  }

  private getVariables(field: any) {
    const variable: FlowableOutput = {
      name: field.key,
      value: Utils.getPropertyValue(this.formFormio?.submission.data, field.key)
    };

    if (variable.value === ''){
      variable.value = undefined;
    }

    const index = this.payload.findIndex(elem => {
      return elem.name === field.key;
    });

    if (index > -1) {
      this.payload[index] = variable;
    } else {
      if (field.type === CustomFormElementType.SELECT) {
        if (!variable.name.endsWith('_url')) {
          variable.name = variable.name + '_value';
          this.payload.push(variable);
        }
      } else {
        this.payload.push(variable);
      }
    }
  }

  private showModalAndGoHome(): void {
    this.logger.error(
      'nessuno o piu\' di un custom form esistenti per il form codice [{key}]'
        .replace('{key}', this.formCodice));
    const messaggioErrore = this.translateService.instant('errori.reperimento_dati');

    this.modalService.error(messaggioErrore, messaggioErrore).then(() => {
      this.router.navigate(['/home']);
    })
      .catch(() => { });
  }


  private compareOldAndNewPayload(oldPayload: FlowableOutput[], newPayload: FlowableOutput[]): boolean{
    let changed = false;

    if (oldPayload.length === 0 && newPayload.length === 0) {
      return false;
    } else if (oldPayload.length === 0 && newPayload.length !== 0){
      return true;
    } else if (oldPayload.length !== 0 && newPayload.length === 0){
      return true;
    }

    for (const oldElement of oldPayload) {
      const esiste = newPayload.find(newElement => oldElement.name === newElement.name);
      if (!esiste || JSON.stringify(oldElement.value) !== JSON.stringify(esiste.value)) {
        changed = true;
        break;
      }
    }

    if (changed) {
      return changed;
    }

    for (const newElement of newPayload) {
      const esiste = oldPayload.find(oldElement => newElement.name === oldElement.name);
      if (!esiste || JSON.stringify(newElement.value) !== JSON.stringify(esiste.value)) {
        changed = true;
        break;
      }
    }
    return changed;
  }
}
