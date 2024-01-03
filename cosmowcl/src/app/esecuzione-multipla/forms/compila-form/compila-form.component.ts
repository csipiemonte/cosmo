/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FlowableOutput } from 'src/app/form-logici/models/flowable-output-model';
import { FormFormioComponent } from 'src/app/shared/components/form-formio/form-formio.component';
import { CustomFormComponentElement } from 'src/app/shared/components/form-formio/models/custom-form-component-element';
import { CustomFormElementType } from 'src/app/shared/components/form-formio/models/custom-form-element-type';
import { CustomFormField } from 'src/app/shared/components/form-formio/models/custom-form-field';
import { VariabileProcesso } from 'src/app/shared/models/api/cosmobusiness/variabileProcesso';
import { AttivitaEseguibileMassivamente } from 'src/app/shared/models/api/cosmopratiche/attivitaEseguibileMassivamente';
import { Utils } from 'src/app/shared/utilities/utilities';
import { EsecuzioneMultiplaVariabiliProcessoRequest } from 'src/app/shared/models/api/cosmobusiness/esecuzioneMultiplaVariabiliProcessoRequest';
import { EsecuzioneMultiplaService } from '../../esecuzione-multipla.service';
import { mergeMap } from 'rxjs/operators';
import { from } from 'rxjs';
import { AsyncTaskModalService } from 'src/app/shared/services/async-task-modal.service';
import { OperazioneAsincronaWrapper } from 'src/app/shared/models/async';
import { ModalService } from 'src/app/shared/services/modal.service';
import { TranslateService } from '@ngx-translate/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormArray, FormGroup } from '@angular/forms';
import { SimpleFormContainerComponent } from 'src/app/simple-form/simple-form-container/simple-form-container.component';
import { ModaleParentComponent } from 'src/app/modali/modale-parent-component';
import { HelperService } from 'src/app/shared/services/helper.service';

@Component({
  selector: 'app-compila-form',
  templateUrl: './compila-form.component.html',
  styleUrls: ['./compila-form.component.scss']
})
export class CompilaFormComponent extends ModaleParentComponent implements OnInit {

  formKey?: string;
  formType?: string;
  codiceForm?: string;
  tasks: AttivitaEseguibileMassivamente[] = [];
  mandareAvantiProcesso?: boolean;

  rowClasses: { [key: number]: string } = {};
  rowErrors: { [key: number]: string } = {};

  variabiliProcesso: VariabileProcesso[] = [];
  codicePagina!: string;
  codiceTab!: string;
  codiceModale!: string;

  @ViewChild('formFormio') formFormio: FormFormioComponent | null = null;
  @ViewChild('simpleForm') simpleForm: SimpleFormContainerComponent | null = null;

  constructor(
    public modal: NgbActiveModal,
    private route: ActivatedRoute,
    private router: Router,
    private modalService: ModalService,
    private translateService: TranslateService,
    private esecuzioneMultiplaService: EsecuzioneMultiplaService,
    private asyncTaskModalService: AsyncTaskModalService,
    public helperService: HelperService) {
    super(helperService);
  }

  ngOnInit(): void {
    this.setModalName(this.codiceModale);
    this.searchHelperModale(this.codicePagina, this.codiceTab);
  }

  disabilita() {

    if (this.formType === 'custom') {
      return !this.formFormio?.isValid();
    } else if (this.formType === 'simple') {
      return  !this.simpleForm?.isValid();
    } else {
      return true;
    }
  }

  salva() {
    const payload: EsecuzioneMultiplaVariabiliProcessoRequest = {
      tasks: this.tasks,
      variabiliProcesso: this.variabiliProcesso,
      mandareAvantiProcesso: this.mandareAvantiProcesso ?? true
    };
    this.esecuzioneMultiplaService.postEsecuzioneMultiplaVariabiliProcesso(payload)
      .pipe(
        mergeMap(operazione =>
          from(this.asyncTaskModalService.open({
            taskUUID: operazione.uuid,
            maxTaskDepth: 0,
          }).result)
        )
      )
      .subscribe(
        (task: OperazioneAsincronaWrapper<string>) => {
          this.elaboraRisultato(JSON.parse(task.risultato ?? ''));
        },
        error => {
          this.sendError(error);
        });
  }

  salvaVariabiliSimpleForm(dynamicControls: FormArray) {
    this.variabiliProcesso = [];
    this.creaVariabiliSimpleForm(dynamicControls);
  }

  private creaVariabiliSimpleForm(dynamicControls: FormArray) {

    for (const control of dynamicControls.controls) {
      const formGroup = control as FormGroup;
      this.simpleForm?.simpleForm?.fields.forEach(field => {
        if (formGroup.controls[field.id]) {
          const variable: FlowableOutput = {
            name: field.id,
            value: formGroup.controls[field.id].value
          };
          const index = this.variabiliProcesso.findIndex(elem => {
            return elem.name === field.id;
          });
          if (index > -1) {
            this.variabiliProcesso[index] = variable;
          } else {
            this.variabiliProcesso.push(variable);
          }
        }
      });
    }
  }

  salvaVariabiliDaFormio(components: CustomFormField[]) {
    this.variabiliProcesso = [];
    this.creaVariabiliFormio(components);
  }

  private creaVariabiliFormio(components: CustomFormField[]) {

    components.forEach((component: CustomFormField) => {

      if (component.type === CustomFormElementType.DATAGRID) {
        this.getVariables(component);
      }
      else if (component.components) {
        this.creaVariabiliFormio(component.components);

      } else if (component.columns) {
        component.columns.forEach((column: CustomFormComponentElement) => {
          this.creaVariabiliFormio(column.components);
        });

      } else if (component.rows && component.rows instanceof Array) {
        component.rows.forEach((row: CustomFormComponentElement[]) => {
          row.forEach(element => {
            this.creaVariabiliFormio(element.components);
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

    const index = this.variabiliProcesso.findIndex(elem => {
      return elem.name === field.key;
    });

    if (index > -1) {
      this.variabiliProcesso[index] = variable;
    } else {
      if (field.type === CustomFormElementType.SELECT) {
        if (!variable.name.endsWith('_url')) {
          variable.name = variable.name + '_value';
          this.variabiliProcesso.push(variable);
        }
      } else {
        this.variabiliProcesso.push(variable);
      }
    }
  }

  private elaboraRisultato(risultato: any[]): void {
    let numSuccesso = 0;
    let numErrori = 0;

    for (const entry of risultato) {
      if (entry.successo) {
        numSuccesso++;
        this.rowClasses[entry.task.attivita.id] = 'success';
        this.esecuzioneMultiplaService.removeSelectedTask(entry.task);
      } else {
        numErrori++;
        this.rowClasses[entry.task.attivita.id] = 'danger';
        this.rowErrors[entry.task.attivita.id] = entry.errore.message;
      }
    }

    this.tasks = this.esecuzioneMultiplaService.getSelectedTasks();
    if (this.tasks.length <= 0) {
      this.modalService.simpleInfo('Tutte le attività sono state elaborate con successo.').finally(() => {
        this.modal.dismiss('finish');
        this.router.navigate(['home'], { relativeTo: this.route });
      });

    } else if (!numErrori) {
      const messaggio = numSuccesso === 1
        ? numSuccesso + ' attività e\' stata elaborata con successo.'
        : numSuccesso + ' attività sono state elaborate con successo.';
      this.modalService.simpleInfo(messaggio).finally(() => {
        this.modal.dismiss('finish');
      });

    } else {
      this.modalService.simpleError('Si sono verificati degli errori nell\'elaborazione di ' + numErrori + ' attività.').finally(() => {
        this.modal.dismiss('finish');
      });
    }
  }

  sendError(error: any){
    this.modalService.error(
      this.translateService.instant('errori.completamento_task'),
      Utils.toMessage(error) ?? this.translateService.instant('errori.completamento_task'),
      error.error.errore
    ).then(() => {
      this.modal.dismiss('finish');
      this.router.navigate(['home']);
    }).catch(() => { });
  }
}
