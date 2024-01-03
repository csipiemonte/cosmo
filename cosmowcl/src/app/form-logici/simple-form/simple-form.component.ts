/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Injector, OnInit, ViewChild } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { FormArray, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { ModalService } from 'src/app/shared/services/modal.service';
import { TranslateService } from '@ngx-translate/core';
import { FunzionalitaParentComponent } from 'src/app/form-logici/funzionalita-parent.component';
import { FlowableOutput } from 'src/app/form-logici/models/flowable-output-model';
import { FieldNames } from 'src/app/simple-form/field-names';
import { FormField } from 'src/app/simple-form/models/form-field.model';
import { SimpleFormContainerComponent } from 'src/app/simple-form/simple-form-container/simple-form-container.component';

@Component({
  selector: 'app-simple-form',
  templateUrl: './simple-form.component.html',
  styleUrls: ['./simple-form.component.scss']
})
export class SimpleFormComponent extends FunzionalitaParentComponent implements OnInit {

  readonly FieldNames = FieldNames;

  formKey = '';
  formFields!: FormField[];
  payload: FlowableOutput[] = [];

  @ViewChild('simpleForm') simpleForm: SimpleFormContainerComponent | null = null;

  constructor(
    private logger: NGXLogger,
    private router: Router,
    private modalService: ModalService,
    private translateService: TranslateService,
    public injector: Injector
  ) {
    super(injector);
    if (this.parametri.get('FORM-KEY')) {
      this.formKey = this.parametri.get('FORM-KEY') as string;
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
    return this.simpleForm?.simpleFormGroup?.valid ?? true;
  }

  public isWip(): boolean {
    if (this.readOnly || this.isSubtask) {
      return false;
    }
    return !!this.simpleForm && this.simpleForm.simpleFormGroup && !this.simpleForm.simpleFormGroup.valid;
  }

  public isChanged(): boolean {
    return this.dataChanged;
  }

  createPayloadAndSend(input: {dynamicControls: FormArray, initial: boolean}) {
    if (this.readOnly) {
      return;
    }
    const oldPayload = this.payload;
    this.payload = [];
    this.createPayload(input.dynamicControls);
    this.sendData({ payload: this.payload });
    this.dataChanged = input.initial ? false : this.compareOldAndNewPayload(oldPayload, this.payload);
  }

  private createPayload(dynamicControls: FormArray){
    for (const control of dynamicControls.controls) {
      const formGroup = control as FormGroup;
      this.simpleForm?.simpleForm?.fields.forEach(field => {
        if (formGroup.controls[field.id]) {
          const variable: FlowableOutput = {
            name: field.id,
            value: formGroup.controls[field.id].value
          };
          const index = this.payload.findIndex(elem => {
            return elem.name === field.id;
          });
          if (index > -1) {
            this.payload[index] = variable;
          } else {
            this.payload.push(variable);
          }
        }
      });
    }
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

    if (changed){
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


  showModalAndGoHome(): void {
    this.logger.error(
      'nessuno o piu\' di un form definition esistenti per la form key [{key}]'
        .replace('{key}', this.formKey));
    const messaggioErrore = this.translateService.instant('errori.reperimento_dati');

    this.modalService.error(messaggioErrore, messaggioErrore).then(() => {
      this.router.navigate(['/home']);
    })
    .catch(() => { });
  }
}
