/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, AbstractControl, AsyncValidatorFn } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { NGXLogger } from 'ngx-logger';
import { Observable, forkJoin, of } from 'rxjs';
import { delay, tap, map, debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { AbstractReactiveFormComponent } from 'src/app/shared/components/proto/abstract-reactive-form.component';
import { CreaCredenzialiSigilloElettronicoRequest } from 'src/app/shared/models/api/cosmoecm/creaCredenzialiSigilloElettronicoRequest';
import { CredenzialiSigilloElettronico } from 'src/app/shared/models/api/cosmoecm/credenzialiSigilloElettronico';
import { ModalService } from 'src/app/shared/services/modal.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { SigilloElettronicoService } from 'src/app/shared/services/sigillo-elettronico.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-aggiungi-modifica-sigillo-elettronico',
  templateUrl: './aggiungi-modifica-sigillo-elettronico.component.html',
  styleUrls: ['./aggiungi-modifica-sigillo-elettronico.component.scss']
})
export class AggiungiModificaSigilloElettronicoComponent extends
 AbstractReactiveFormComponent<void, CredenzialiSigilloElettronico | null, CreaCredenzialiSigilloElettronicoRequest> implements OnInit {

showDelegatedPassword = false;
showOtpPwd = false;

  constructor(
    protected logger: NGXLogger,
    protected route: ActivatedRoute,
    protected router: Router,
    protected securityService: SecurityService,
    protected translateService: TranslateService,
    protected sigilloElettronicoService: SigilloElettronicoService,
    protected modalService: ModalService,
    private modal: NgbModal
    ) {
      super(logger, route, securityService);
    }

  get isNuovo(): boolean {
    return !this.loadedData?.id;
  }

  protected loadData(routeParams: any, inputData?: any): Observable<CredenzialiSigilloElettronico | null> {
    const idSigillo = routeParams?.id ?? undefined;
    return forkJoin({
      sigilloElettronico: idSigillo ? this.sigilloElettronicoService.getSigilloElettronicoId(idSigillo) : of(null)
    }).pipe(map(elem => {
      return elem.sigilloElettronico;
    }
    ));
  }

  protected buildForm(routeParams: any, inputData?: never, loadedData?: CredenzialiSigilloElettronico | null):
   FormGroup | Observable<FormGroup> {
    const form = new FormGroup({
      alias: new FormControl({value: loadedData?.alias ?? '', disabled: !this.isNuovo}, [
        Validators.required,
        Validators.maxLength(100)
      ], [this.checkConflictingField('alias')]),
      utente: new FormControl({value: loadedData?.utente ?? '', disabled: false}, [
        Validators.required,
        Validators.maxLength(100)
      ], []),
      delegatedDomain: new FormControl({value: loadedData?.delegatedDomain ?? '', disabled: false}, [
        Validators.required,
        Validators.maxLength(100)
      ], []),
      delegatedUser: new FormControl({value: loadedData?.delegatedUser ?? '', disabled: false}, [
        Validators.required,
        Validators.maxLength(100)
      ], []),
      delegatedPassword: new FormControl({value: loadedData?.delegatedPassword ?? '', disabled: false}, [
        Validators.required,
        Validators.maxLength(100)
      ], []),
      otpPwd: new FormControl({ value: loadedData?.otpPwd ?? '', disabled: false }, [
        Validators.required,
        Validators.maxLength(100)
      ]),
      tipoHsm: new FormControl({ value: loadedData?.tipoHsm ?? '', disabled: false }, [
        Validators.required,
        Validators.maxLength(100)
      ]),
      tipoOtpAuth: new FormControl({ value: loadedData?.tipoOtpAuth ?? '', disabled: false }, [
        Validators.required,
        Validators.maxLength(100)
      ])
    });

    return form;
  }

  protected buildPayload(formValue: any): CreaCredenzialiSigilloElettronicoRequest {
    return {
      alias: formValue.alias,
      utente: formValue.utente,
      delegatedDomain: formValue.delegatedDomain,
      delegatedUser: formValue.delegatedUser,
      delegatedPassword: formValue.delegatedPassword,
      otpPwd: formValue.otpPwd,
      tipoHsm: formValue.tipoHsm,
      tipoOtpAuth: formValue.tipoOtpAuth
    };
  }

  protected onSubmit(payload: CreaCredenzialiSigilloElettronicoRequest): Observable<any> {
    let out: Observable<CredenzialiSigilloElettronico> | null = null;
    const crea = this.isNuovo;
    if (crea) {
      // crea nuovo
      out = this.sigilloElettronicoService.creaSigilloElettronico(payload);

    } else {
      // modifica esistente
      out = this.sigilloElettronicoService.aggiornaSigilloElettronico(
        Utils.require(this.loadedData?.id), payload);
    }

    return out.pipe(
      delay(environment.httpMockDelay),
      tap(result => {

        let messaggio = this.translateService.instant(
          'sigillo_elettronico.dialogs.' + (crea ? 'creato' : 'modificato') + '.messaggio');

        messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.alias ?? '']);

        this.modalService.info(this.translateService.instant(
          'sigillo_elettronico.dialogs.' + (crea ? 'creato' : 'modificato') + '.titolo'),
          messaggio).then(
            () => {
              this.tornaIndietro();
            }
          ).catch(() => { });
      }, err => {
        this.modalService.simpleError(Utils.toMessage(err), err.error.errore);
      })
    );
  }

  tornaIndietro() {
    window.history.back();
  }

  mostraDelegatedPassword(){
    this.showDelegatedPassword = !this.showDelegatedPassword;
  }

  mostraOtpPwd(){
    this.showOtpPwd = !this.showOtpPwd;
  }

  checkConflictingField(fieldName: string, clause: 'eq' | 'eqic' = 'eq'): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{[key: string]: any} | null> => {

      const v = (input as FormControl).value;
      if (!v) {
        return of(null);
      }

      const requestPayload: any = {
        page: 0,
        size: 1,
        fields: 'id,' + fieldName,
        filter: { id: this.loadedData ? { ne: this.loadedData.id } : undefined },
      };
      const fieldFilter: any = {};
      fieldFilter[clause] = v;
      requestPayload.filter[fieldName] = fieldFilter;

      return of(JSON.stringify(requestPayload)).pipe(

        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(filter => this.sigilloElettronicoService.getSigilloElettronico(filter)),
        map(response => {
          if (response?.sigilliElettronici?.length) {
            return {
              conflict: { field: fieldName, other: response.sigilliElettronici[0] }
            };
          }
          return null;
        })
      );
    };
  }

}
