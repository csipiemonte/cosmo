/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, AsyncValidatorFn, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { Observable, of } from 'rxjs';
import { debounceTime, delay, distinctUntilChanged, switchMap, map} from 'rxjs/operators';
import { Profilo } from 'src/app/shared/models/api/cosmoauthorization/profilo';
import { UseCase } from 'src/app/shared/models/api/cosmoauthorization/useCase';
import { ModalService } from 'src/app/shared/services/modal.service';
import { ProfiliService } from 'src/app/shared/services/profili.service';
import { UseCaseService } from 'src/app/shared/services/usecase.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-aggiungi-modifica-use-case-profili',
  templateUrl: './aggiungi-modifica-use-case-profili.component.html',
  styleUrls: ['./aggiungi-modifica-use-case-profili.component.scss']
})
export class AggiungiModificaUseCaseProfiliComponent implements OnInit {

  profilo: Profilo | null = null;
  profiloForm!: FormGroup;

  loading = 0;
  loadingError: any | null = null;

  useCasesDisponibili: Array<Profilo> = [];
  useCases: Array<UseCase> = [];
  dragAndDropIsTouched = false;

  constructor(
    private route: ActivatedRoute,
    private modalService: ModalService,
    private translateService: TranslateService,
    private profiliService: ProfiliService,
    private useCaseService: UseCaseService) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {

      this.loading++;
      this.loadingError = null;

      if (params.id) {
        this.profiliService.getProfilo(params.id).subscribe(response => {

          this.loading--;

          if (response.profilo) {
            this.profilo = response.profilo;
            this.refresh();
          } else {
            this.modalService.simpleInfo(this.translateService.instant('use_case_profili.dialogs.nessun_profilo.titolo'))
              .finally(() => window.history.back());
          }
        }, error => {
          this.loading--;
          this.loadingError = error;
        });
      } else {
        this.loading--;
        this.refresh();
      }

    });
  }

  get isNuovo(): boolean {
    return !this.profilo;
  }

  refresh() {

    this.useCaseService.getUseCases('').subscribe(response => {
      this.useCases = Object.assign([], this.profilo ? this.profilo.useCases : []);
      if (response.useCases && response.useCases.length > 0) {
        this.useCasesDisponibili = response.useCases.filter(useCase => !this.useCases.find(useCaseP => useCaseP.codice === useCase.codice));
      }
    }, error => {
      this.useCases = Object.assign([], this.profilo ? this.profilo.useCases : []);
      this.useCasesDisponibili = [];
    });

    this.profiloForm = new FormGroup({
      codice: new FormControl({ value: this.profilo ? this.profilo.codice : '', disabled: !this.isNuovo },
        [Validators.required, Validators.maxLength(100)],
        [ this.checkConflictingFieldCodice('codice', 'eqic')]
        ),
      descrizione: new FormControl(this.profilo ? this.profilo.descrizione : null, Validators.maxLength(255)),
    });
  }


  checkConflictingFieldCodice(fieldName: string, clause: 'eq' | 'eqic' = 'eq'): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{[key: string]: any} | null> => {
      const v = (input as FormControl).value;
      if (!v) {
        return of(null);
      }

      const requestPayload: any = {
        page: 0,
        size: 1,
        fields: 'id,descrizione,' + fieldName,
        filter: { codice: this.profilo ? { ne: this.profilo.codice } : undefined },
      };
      const fieldFilter: any = {};
      fieldFilter[clause] = v;
      requestPayload.filter[fieldName] = fieldFilter;

      return of(JSON.stringify(requestPayload)).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(filter => this.profiliService.getProfili(filter)),
        map(response => {
          if (response && response.profili?.length) {
            return {
              conflict: { field: fieldName, other: response.profili[0] }
            };
          }
          return null;
        })
      );
    };
  }

  hasValue(name: string): boolean {
    if (!this.profiloForm) {
      return false;
    }
    const control = this.resolveControl(name);

    if (!control) {
      return false;
    }
    return Utils.isNotBlank(control.value);
  }

  hasError(name: string, type: string): any {
    return this.getError(name, type) !== null;
  }

  getError(name: string, type: string): any {
    if (!this.profiloForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return control.errors && control.errors[type] ?
      (control.errors[type] ?? true) : null;
  }

  displayInvalid(name: string): boolean {
    if (!this.profiloForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return (control.dirty || control.touched) && control.invalid;
  }

  displayValidating(name: string): boolean {
    if (!this.profiloForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return (control.touched || control.dirty) && (control as FormControl).status === 'PENDING';
  }

  private resolveControl(name: string): AbstractControl | undefined {
    let actual: any = this.profiloForm;
    for (const token of name.split('.')) {
      const newControl = actual.controls[token];
      if (!newControl) {
        return undefined;
      }
      actual = newControl;
    }
    return actual as AbstractControl;
  }

  tornaIndietro() {
    window.history.back();
  }

  drop(event: CdkDragDrop<string[]>) {
    this.dragAndDropIsTouched = true;
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
    }
  }

  salva() {

    if (!this.profilo || !this.profilo.id) {
      this.profilo = {
        codice: this.profiloForm.getRawValue().codice,
        descrizione: this.profiloForm.getRawValue().descrizione,
        useCases: this.useCases
      };
      this.profiliService.salvaProfilo(this.profilo).subscribe(response => {
        let messaggio = this.translateService.instant('use_case_profili.dialogs.creato.messaggio');
        messaggio = Utils.parseAndReplacePlaceholders(messaggio, [this.profilo?.descrizione ?? this.profilo?.codice ?? '']);
        this.modalService.info(this.translateService.instant('use_case_profili.dialogs.creato.titolo'), messaggio)
          .then(() => {
            this.tornaIndietro();
          });
      }, error => {
        this.modalService.simpleError(Utils.toMessage(error.error.title), error.error.errore);
      });
    } else {

      this.profilo.descrizione = this.profiloForm.getRawValue().descrizione;
      this.profilo.useCases = this.useCases;

      this.profiliService.aggiornaProfilo(this.profilo.id, this.profilo).subscribe(response => {
        let messaggio = this.translateService.instant('use_case_profili.dialogs.modificato.messaggio');
        messaggio = Utils.parseAndReplacePlaceholders(messaggio, [this.profilo?.descrizione ?? this.profilo?.codice ?? '']);
        this.modalService.info(this.translateService.instant('use_case_profili.dialogs.modificato.titolo'), messaggio)
          .then(() => {
            this.tornaIndietro();
          });
      }, error => {
        this.modalService.simpleError(Utils.toMessage(error.error.title), error.error.errore);
      });
    }

  }

  pulisciCampi() {
    this.refresh();
  }
}
