/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  ChangeDetectorRef,
  Component,
  OnInit,
} from '@angular/core';
import {
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import {
  ActivatedRoute,
  Router,
} from '@angular/router';

import { NGXLogger } from 'ngx-logger';
import {
  Observable,
  of,
} from 'rxjs';
import {
  delay,
  tap,
} from 'rxjs/operators';
import {
  AbstractReactiveFormComponent,
} from 'src/app/shared/components/proto/abstract-reactive-form.component';
import { Constants } from 'src/app/shared/constants/constants';
import {
  CreaSchemaAutenticazioneFruitoreRequest,
} from 'src/app/shared/models/api/cosmoauthorization/creaSchemaAutenticazioneFruitoreRequest';
import {
  Fruitore,
} from 'src/app/shared/models/api/cosmoauthorization/fruitore';
import {
  SchemaAutenticazioneFruitore,
} from 'src/app/shared/models/api/cosmoauthorization/schemaAutenticazioneFruitore';
import {
  TipoSchemaAutenticazioneFruitore,
} from 'src/app/shared/models/api/cosmoauthorization/tipoSchemaAutenticazioneFruitore';
import { FruitoriService } from 'src/app/shared/services/fruitori.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-modifica-schemi-auth-fruitore',
  templateUrl: './modifica-schemi-auth-fruitore.component.html',
  styleUrls: ['./modifica-schemi-auth-fruitore.component.scss']
})
export class ModificaSchemiAutorizzazioneFruitoreComponent
  extends AbstractReactiveFormComponent<SchemaAutenticazioneFruitore, SchemaAutenticazioneFruitore, CreaSchemaAutenticazioneFruitoreRequest>
  implements OnInit {

  idFruitore?: number;
  existing?: SchemaAutenticazioneFruitore;

  fruitoreForm!: FormGroup;
  fruitore?: Fruitore | null = null;

  tipologie: TipoSchemaAutenticazioneFruitore[] = [{
    codice: 'BASIC', descrizione: 'Basic authentication'
  }, {
    codice: 'API_MANAGER', descrizione: 'CSI Api Manager'
  }, {
    codice: 'TOKEN', descrizione: 'Token authentication'
  }, {
    codice: 'DIGEST', descrizione: 'Digest signature'
  }];

  contentTypes = [{
    codice: 'JSON', descrizione: 'JSON'
  }, {
    codice: 'FORM', descrizione: 'Multipart form data'
  }];

  metodiHttp = [{
    codice: 'POST', descrizione: 'POST'
  }, {
    codice: 'GET', descrizione: 'GET'
  }, {
    codice: 'PUT', descrizione: 'PUT'
  }];

  apiManagerExposures = [{
    codice: 'https://endpoint/token', descrizione: 'Esposizione di produzione'
  }, {
    codice: 'https://tst-endpoint/token', descrizione: 'Esposizione di test'
  }];

  passwordIsDefault = false;
  clientSecretIsDefault = false;

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

  protected getInputData(): Observable<SchemaAutenticazioneFruitore | null | undefined> {
    return of(this.existing);
  }

  get tipologieApplicabili(): TipoSchemaAutenticazioneFruitore[] {
    if (this.inIngressoSelected) {
      return this.tipologie.filter(o => ['BASIC', 'DIGEST'].includes(o.codice));
    } else if (this.inUscitaSelected) {
      return this.tipologie.filter(o => ['BASIC', 'API_MANAGER', 'TOKEN'].includes(o.codice));
    }
    return this.tipologie;
  }

  protected buildForm(routeParams: any, inputData?: SchemaAutenticazioneFruitore): FormGroup {
    const form = new FormGroup({
      tipo: new FormControl({value: inputData?.tipo ?
          this.tipologie.find(t => t.codice === inputData?.tipo.codice) ?? null : null, disabled: false}, [
        Validators.required,
        this.requireValueIf((val: any) => val?.codice === 'BASIC' || val?.codice === 'DIGEST', () => this.inIngressoSelected),
        // tslint:disable-next-line: max-line-length
        this.requireValueIf((val: any) => val?.codice === 'BASIC' || val?.codice === 'TOKEN' || val?.codice === 'API_MANAGER', () => this.inUscitaSelected)
      ]),
      username: new FormControl({value: inputData?.credenziali?.username ?? '', disabled: false}, [
        Validators.maxLength(1000),
        this.requiredIf(() => this.basicSelected)
      ]),
      password: new FormControl({value: inputData?.credenziali?.password ?? '', disabled: false}, [
        Validators.maxLength(1000),
        this.requiredIf(() => this.basicSelected)
      ]),
      clientId: new FormControl({value: inputData?.credenziali?.clientId ?? '', disabled: false}, [
        Validators.maxLength(1000),
        this.requiredIf(() => this.tokenSelected || this.digestSelected || this.apiMgrSelected)
      ]),
      clientSecret: new FormControl({value: inputData?.credenziali?.clientSecret ?? '', disabled: false}, [
        Validators.maxLength(1000),
        this.requiredIf(() => this.tokenSelected || this.digestSelected || this.apiMgrSelected)
      ]),
      inIngresso: new FormControl({value: inputData?.inIngresso ?? false, disabled: false}, [
        Validators.required
      ]),
      tokenEndpoint: new FormControl({value: inputData?.tokenEndpoint ?? '', disabled: false}, [
        Validators.maxLength(1000),
        Validators.pattern(Constants.PATTERNS.URL_ABSOLUTE_OR_RELATIVE),
      ]),
      mappaturaRichiestaToken: new FormControl({value: inputData?.mappaturaRichiestaToken ?? '', disabled: false}, [
        Validators.maxLength(2000),
      ]),
      mappaturaOutputToken: new FormControl({value: inputData?.mappaturaOutputToken ?? '', disabled: false}, [
        Validators.maxLength(2000),
      ]),
      metodoRichiestaToken: new FormControl({value: inputData?.metodoRichiestaToken ?? 'POST', disabled: false}, [
        Validators.maxLength(20),
      ]),
      contentTypeRichiestaToken: new FormControl({value: inputData?.contentTypeRichiestaToken ?? 'JSON', disabled: false}, [
        Validators.maxLength(100),
      ]),
      nomeHeader: new FormControl({value: inputData?.nomeHeader ?? '', disabled: false}, [
        Validators.maxLength(1000),
      ]),
      formatoHeader: new FormControl({value: inputData?.formatoHeader ?? '', disabled: false}, [
        Validators.maxLength(1000),
      ]),
    });

    form.controls.tipo.valueChanges.subscribe(() => {
      (form.controls.username as FormControl).updateValueAndValidity();
      (form.controls.password as FormControl).updateValueAndValidity();
      (form.controls.clientId as FormControl).updateValueAndValidity();
      (form.controls.clientSecret as FormControl).updateValueAndValidity();
      (form.controls.tokenEndpoint as FormControl).updateValueAndValidity();
      (form.controls.mappaturaRichiestaToken as FormControl).updateValueAndValidity();
      (form.controls.mappaturaOutputToken as FormControl).updateValueAndValidity();
      (form.controls.metodoRichiestaToken as FormControl).updateValueAndValidity();
      (form.controls.contentTypeRichiestaToken as FormControl).updateValueAndValidity();
      (form.controls.nomeHeader as FormControl).updateValueAndValidity();
      (form.controls.formatoHeader as FormControl).updateValueAndValidity();

      this.cdr.detectChanges();
    });

    form.controls.inIngresso.valueChanges.subscribe(() => {
      (form.controls.tipo as FormControl).updateValueAndValidity();
      this.cdr.detectChanges();
    });

    if (this.existing?.credenziali?.password?.length) {
      this.passwordIsDefault = true;
    }

    if (this.existing?.credenziali?.clientSecret?.length) {
      this.clientSecretIsDefault = true;
    }

    this.cdr.detectChanges();
    return form;
  }

  protected buildPayload(raw: any): CreaSchemaAutenticazioneFruitoreRequest {
    const isBasic = raw?.tipo?.codice === 'BASIC';
    const isToken = raw?.tipo?.codice === 'TOKEN';
    const isDigest = raw?.tipo?.codice === 'DIGEST';
    const isApiMgr = raw?.tipo?.codice === 'API_MANAGER';

    return {
      codiceTipo: raw.tipo?.codice,
      inIngresso: raw.inIngresso,
      credenziali: {
        username: isBasic ? raw.username : null,
        password: isBasic && !this.passwordIsDefault ? raw.password : null,
        clientId: isToken || isDigest || isApiMgr ? raw.clientId : null,
        clientSecret: isToken || isDigest || isApiMgr && !this.clientSecretIsDefault ? raw.clientSecret : null,
      },
      tokenEndpoint: raw.tokenEndpoint?.trim(),
      mappaturaRichiestaToken: raw.mappaturaRichiestaToken?.trim(),
      mappaturaOutputToken: raw.mappaturaOutputToken?.trim(),
      metodoRichiestaToken: raw.metodoRichiestaToken,
      contentTypeRichiestaToken: raw.contentTypeRichiestaToken,
      nomeHeader: raw.nomeHeader,
      formatoHeader: raw.formatoHeader,
    };
  }

  protected onSubmit(payload: CreaSchemaAutenticazioneFruitoreRequest): Observable<SchemaAutenticazioneFruitore> {
    let out: Observable<SchemaAutenticazioneFruitore> | null = null;
    const idFruitore = Utils.require(this.idFruitore);

    if (!this.existing) {
      // crea nuovo
      out = this.fruitoriService.creaSchemaAutenticazioneFruitore(
        idFruitore, payload);
    } else {
      // modifica esistente
      out = this.fruitoriService.aggiornaSchemaAutenticazioneFruitore(
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

  get inIngressoSelected(): boolean {
    return this.getValue('inIngresso') === true;
  }

  get inUscitaSelected(): boolean {
    return this.getValue('inIngresso') === false;
  }

  get basicSelected(): boolean {
    return this.getValue('tipo')?.codice === 'BASIC';
  }

  get tokenSelected(): boolean {
    return this.getValue('tipo')?.codice === 'TOKEN';
  }

  get digestSelected(): boolean {
    return this.getValue('tipo')?.codice === 'DIGEST';
  }

  get apiMgrSelected(): boolean {
    return this.getValue('tipo')?.codice === 'API_MANAGER';
  }

  checkPasswordEdit(e: Event) {
    if (this.passwordIsDefault) {
      this.passwordIsDefault = false;
      this.form?.controls.password.setValue('');
    }
  }

  checkSecretEdit(e: Event) {
    if (this.clientSecretIsDefault) {
      this.clientSecretIsDefault = false;
      this.form?.controls.clientSecret.setValue('');
    }
  }
}
