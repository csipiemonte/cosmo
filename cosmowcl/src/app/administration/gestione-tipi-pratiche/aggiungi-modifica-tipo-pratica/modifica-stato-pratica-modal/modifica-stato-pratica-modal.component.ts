/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { AbstractControl, AsyncValidatorFn, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { Observable, of } from 'rxjs';
import { debounceTime, delay, distinctUntilChanged, map, switchMap } from 'rxjs/operators';
import { AbstractReactiveFormComponent } from 'src/app/shared/components/proto/abstract-reactive-form.component';
import { Constants } from 'src/app/shared/constants/constants';
import { StatoPratica } from 'src/app/shared/models/api/cosmopratiche/statoPratica';
import { PraticheService } from 'src/app/shared/services/pratiche.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-modifica-stato-pratica-modal',
  templateUrl: './modifica-stato-pratica-modal.component.html',
  styleUrls: ['./modifica-stato-pratica-modal.component.scss']
})
export class ModificaStatoPraticaModalComponent extends AbstractReactiveFormComponent<void, StatoPratica | null, StatoPratica>
  implements OnInit {

  input?: () => Observable<StatoPratica>;
  entity?: StatoPratica;
  isNuovo = false;
  stati: StatoPratica[] = [];

  loading = 0;

  classi: { codice: string, descrizione: string }[] = [
    { codice: 'primary', descrizione: 'Colore primario' },
    { codice: 'success', descrizione: 'Colore di successo' },
    { codice: 'warning', descrizione: 'Colore di avviso' },
    { codice: 'danger', descrizione: 'Colore di errore' },
    { codice: 'dark', descrizione: 'Colore scuro' },
    { codice: 'light', descrizione: 'Colore chiaro' },
    { codice: 'secondary', descrizione: 'Colore secondario' },
  ];

  constructor(
    protected logger: NGXLogger,
    protected route: ActivatedRoute,
    protected securityService: SecurityService,
    private praticheService: PraticheService,
    protected modal: NgbActiveModal
  ) {
    super(logger, route, securityService);
  }

  cancel(): void {
    this.modal.dismiss({ reason: 'canceled' });
  }

  protected loadData(routeParams: any, inputData?: any): Observable<StatoPratica | null> {
    if (!this.input) {
      throw new Error('No entity source provided to edit modal');
    }

    return this.input();
  }

  protected buildForm(routeParams: any, inputData?: never, loadedData?: StatoPratica | null): FormGroup | Observable<FormGroup> {
    const form = new FormGroup({
      codice: new FormControl({ value: loadedData?.codice ?? '', disabled: !this.isNuovo }, [
        Validators.required,
        Validators.maxLength(255),
        Validators.pattern(Constants.PATTERNS.CODICE),
      ], [
        this.checkConflictingFieldCodice('codice')
      ]),
      descrizione: new FormControl({ value: loadedData?.descrizione ?? '', disabled: false }, [
        Validators.required,
        Validators.maxLength(500),
      ]),
      classe: new FormControl({ value: { entity: { codice: loadedData?.classe ?? 'primary' } }, disabled: false }, [
        Validators.maxLength(100),
      ]),
    });

    return form;
  }
  protected buildPayload(formValue: any): StatoPratica {
    return {
      ...formValue,
      classe: formValue?.classe?.entity?.codice
    };
  }

  protected onSubmit(payload: StatoPratica): Observable<any> {
    this.modal.close(payload);
    return of(null);
  }

  checkConflictingFieldCodice(fieldName: string): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{ [key: string]: any } | null> => {
      const v = (input as FormControl).value;
      if (!v) {
        return of(null);
      }

      return of(null).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(() => this.praticheService.listStatiPratica()),
        map(response => {
          if (response.length > 0 && response.find(single => single.codice === v)) {
            return {
              conflict: { field: fieldName, other: response.find(single => single.codice === v) }
            };
          }
          if (this.stati.length > 0 && this.stati.find(single => single.codice === v)) {
            return {
              conflict: { field: fieldName, other: this.stati.find(single => single.codice === v) }
            };
          }
          return null;
        })
      );
    };
  }
}
