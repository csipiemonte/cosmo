/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, Validators, FormControl, AsyncValidatorFn, AbstractControl } from '@angular/forms';
import { forkJoin, Observable, of } from 'rxjs';
import { ModalService } from 'src/app/shared/services/modal.service';
import { TranslateService } from '@ngx-translate/core';
import { SecurityService } from 'src/app/shared/services/security.service';
import { environment } from 'src/environments/environment';
import { Fruitore } from 'src/app/shared/models/api/cosmoauthorization/fruitore';
import { FruitoriService } from 'src/app/shared/services/fruitori.service';
import { AbstractReactiveFormComponent } from 'src/app/shared/components/proto/abstract-reactive-form.component';
import { NGXLogger } from 'ngx-logger';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { SchemaAutenticazioneFruitore } from 'src/app/shared/models/api/cosmoauthorization/schemaAutenticazioneFruitore';
import { Utils } from 'src/app/shared/utilities/utilities';
import { debounceTime, delay, distinctUntilChanged, map, switchMap, tap } from 'rxjs/operators';
import { EndpointFruitore } from 'src/app/shared/models/api/cosmoauthorization/endpointFruitore';
import { CreaEndpointFruitoreRequest } from 'src/app/shared/models/api/cosmoauthorization/creaEndpointFruitoreRequest';
import { OperazioneFruitore } from 'src/app/shared/models/api/cosmoauthorization/operazioneFruitore';
import { Constants } from 'src/app/shared/constants/constants';


@Component({
  selector: 'app-modifica-endpoint-fruitore',
  templateUrl: './modifica-endpoint-fruitore.component.html',
  styleUrls: ['./modifica-endpoint-fruitore.component.scss']
})
export class ModificaEndpointFruitoreComponent
  extends AbstractReactiveFormComponent<EndpointFruitore, EndpointFruitore, CreaEndpointFruitoreRequest>
  implements OnInit {

  idFruitore?: number;
  existing?: EndpointFruitore;

  fruitoreForm!: FormGroup;
  fruitore?: Fruitore | null = null;

  operazioni: OperazioneFruitore[] = [];
  schemiAutenticazione: SchemaAutenticazioneFruitore[] = [];
  metodiHttp = ['GET', 'DELETE', 'PATCH', 'POST', 'PUT'];
  tipiEndpoint = ['REST', 'SOAP'];

  constructor(
    protected logger: NGXLogger,
    protected route: ActivatedRoute,
    protected router: Router,
    protected fruitoriService: FruitoriService,
    protected modalService: ModalService,
    protected translateService: TranslateService,
    protected securityService: SecurityService,
    public modal: NgbActiveModal,
    protected cdr: ChangeDetectorRef,
  ) {
    super(logger, route, securityService);
  }

  protected reloadOnRouteParams(): boolean {
    // essendo un modale non deve dipendere dai route params
    return false;
  }

  protected getInputData(): Observable<EndpointFruitore | null | undefined> {
    return of(this.existing);
  }

  protected loadData(routeParams: any, inputData?: any): Observable<any> {
    return forkJoin({
      fruitore: this.fruitoriService.getFruitore(Utils.require(this.idFruitore)),
      operazioni: this.fruitoriService.getOperazioniFruitore()
    }).pipe(
      tap(data => {
        this.operazioni = data.operazioni;
        this.fruitore = data.fruitore;
        this.schemiAutenticazione = (data.fruitore.schemiAutenticazione ?? []).filter(sa => !sa.inIngresso);
      })
    );
  }

  protected buildForm(routeParams: any, inputData?: EndpointFruitore): FormGroup {
    const form = new FormGroup({
      operazione: new FormControl({
        value: inputData?.operazione ?
          this.operazioni.find(o => o.codice === inputData.operazione.codice) ?? null : null,
        disabled: false
      }, [
        Validators.required,
      ], [
        this.checkEndpointForOperationDoesNotExistAlready()
      ]),
      schemaAutenticazione: new FormControl({
        value: (inputData?.schemaAutenticazione?.id) ?
          this.schemiAutenticazione.find(o => o.id === inputData.schemaAutenticazione?.id) ?? null : null,
        disabled: false
      }, []),
      codiceTipo: new FormControl({value: inputData?.codiceTipoEndpoint ?? 'REST', disabled: false}, [
        Validators.required,
      ]),
      endpoint: new FormControl({value: inputData?.endpoint ?? '', disabled: false}, [
        Validators.maxLength(1000),
        Validators.required,
        Validators.pattern(Constants.PATTERNS.URL_ABSOLUTE_OR_RELATIVE),
      ]),
      metodoHttp: new FormControl({value: inputData?.metodoHttp ?? 'POST', disabled: false}, [
        this.requiredIf(() => this.restSelected),
      ]),
      codiceDescrittivo: new FormControl({value: inputData?.codiceDescrittivo ?? '', disabled: false}, [
        Validators.maxLength(100)
      ], [
        this.checkEndpointForCodiceDescrittivoDoesNotExistAlready()
      ]),
    });
    this.cdr.detectChanges();
    return form;
  }

  protected buildPayload(raw: any): CreaEndpointFruitoreRequest {

    const out: CreaEndpointFruitoreRequest = {
      codiceOperazione: raw?.operazione?.codice,
      codiceTipo: raw?.codiceTipo,
      endpoint: raw?.endpoint,
      metodoHttp: this.restSelected ? raw?.metodoHttp : null,
      idSchemaAutenticazione: raw?.schemaAutenticazione?.id,
      codiceDescrittivo: raw?.codiceDescrittivo,
    };
    return out;
  }

  protected onSubmit(payload: CreaEndpointFruitoreRequest): Observable<EndpointFruitore> {
    let out: Observable<EndpointFruitore> | null = null;
    const idFruitore = Utils.require(this.idFruitore);

    if (!this.existing) {
      // crea nuovo
      out = this.fruitoriService.creaEndpointFruitore(
        idFruitore, payload);
    } else {
      // modifica esistente
      out = this.fruitoriService.aggiornaEndpointFruitore(
        idFruitore, Utils.require(this.existing.id), payload);
    }

    return out.pipe(
      delay(environment.httpMockDelay),
      tap(() => {
        this.modal.close();
      }, err => {
        this.modalService.simpleError(Utils.toMessage(err), err.error.errore);
      })
    );
  }

  get restSelected(): boolean {
    return this.getValue('codiceTipo') === 'REST';
  }

  get soapSelected(): boolean {
    return this.getValue('codiceTipo') === 'SOAP';
  }

  checkEndpointForOperationDoesNotExistAlready(): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{[key: string]: any} | null> => {
      const v = (input as FormControl).value?.codice;
      if (!v) {
        return of(null);
      }

      return of(v).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(value => this.fruitoriService.getFruitore(Utils.require(this.idFruitore)).pipe(
          map(fruitore => {
            if (!!fruitore.endpoints?.find(e => !e.operazione?.personalizzabile && e.operazione?.codice === value
              && (!this.existing || e.id !== this.existing?.id))) {
              return { conflict: true };
            }
            return null;
          })
        ))
      );
    };
  }

  checkEndpointForCodiceDescrittivoDoesNotExistAlready(): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{[key: string]: any} | null> => {
      const v = (input as FormControl).value;
      if (!v) {
        return of(null);
      }

      return of(v).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(value => this.fruitoriService.getFruitore(Utils.require(this.idFruitore)).pipe(
          map(fruitore => {
            if (!!fruitore.endpoints?.find(e => e.operazione?.personalizzabile && e.codiceDescrittivo === value
              && (!this.existing || e.id !== this.existing?.id))) {
              return { conflict: true };
            }
            return null;
          })
        ))
      );
    };
  }

}
