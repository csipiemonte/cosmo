/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, EventEmitter, Injector, Input, OnInit, Output } from '@angular/core';
import { SimpleForm } from '../models/simple-form.model';
import { FieldNames } from '../field-names';
import { SimpleFormService } from '../services/simple-form.service';
import { FormArray, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { FormBuilder } from '@angular/forms';
import { FormField } from '../models/form-field.model';
import { VariabileProcesso } from 'src/app/shared/models/api/cosmobusiness/variabileProcesso';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-simple-form-container',
  templateUrl: './simple-form-container.component.html',
  styleUrls: ['./simple-form-container.component.scss']
})
export class SimpleFormContainerComponent implements OnInit {

  readonly FieldNames = FieldNames;

  @Input() variabiliProcesso: VariabileProcesso[] = [];
  @Input() readOnly = false;
  @Input() formKey?: string;

  @Output() errorMessage = new EventEmitter<void>();
  @Output() emitter = new EventEmitter<{dynamicControls: FormArray, initial: boolean}>();

  formFields!: FormField[];
  simpleForm: SimpleForm | null = null;
  formReady = false;

  simpleFormGroup = this.fb.group({
    dynamicControls: this.fb.array([])
  });

  get dynamicControls() {
    return this.simpleFormGroup.get('dynamicControls') as FormArray;
  }

  constructor(
    private simpleFormService: SimpleFormService,
    private fb: FormBuilder,
    public injector: Injector) { }

  ngOnInit(): void {
    if (this.formKey){

      this.simpleFormService.getSimpleFormFromFormKey(this.formKey)
      .pipe(
        finalize(() => {
          this.formReady = true;
        })
      )
      .subscribe(simpleForm => {
      this.simpleForm = simpleForm;
      this.formFields = this.simpleForm.fields.filter(field => {
        return FieldNames.HEADLINE_WITH_LINE !== field.type &&
          FieldNames.HORIZONTAL_LINE !== field.type &&
          FieldNames.SPACER !== field.type;
      });
      this.initializeForm(this.variabiliProcesso);

      this.emitter.emit({dynamicControls: this.dynamicControls, initial: true});
      this.onFormChange();
      }, () => {
        this.errorMessage.emit();
      });
    } else{
      this.errorMessage.emit();
    }
  }

  initializeForm(variabiliProcesso: VariabileProcesso[]): void {
    if (this.simpleForm) {
      this.simpleForm.fields.forEach(field => {
        const validatorFnArr: ValidatorFn[] = [];
        if (field.required && !this.readOnly) {
          validatorFnArr.push(Validators.required);
        }
        if (this.readOnly) {
          field.readOnly = true;
        }
        this.dynamicControls.push(this.fb.group({
          ['' + field.id]: [this.getInitialValue(
            field.id, variabiliProcesso), validatorFnArr]
        }));
      });
    }
  }

  private getInitialValue(fieldId: string, variabiliProcesso: VariabileProcesso[]) {
    const found: VariabileProcesso | undefined = variabiliProcesso.find(variabile => fieldId === variabile.name);
    return found && found.value ? found.value : '';
  }

  onFormChange(): void {
    this.simpleFormGroup.valueChanges.subscribe(() => {
      this.emitter.emit({dynamicControls: this.dynamicControls, initial: false});
    });
  }

  getFormGroupFromIndex(index: number): FormGroup {
    return this.dynamicControls.controls[index] as FormGroup;
  }

  public isValid(): boolean {
    if (this.formReady){
      return this.simpleFormGroup?.valid ?? false;
    }else{
      return false;
    }
  }
}
