/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, HostListener, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, Validators, FormControl, AbstractControl, AsyncValidatorFn } from '@angular/forms';
import { combineLatest, forkJoin, Observable, of } from 'rxjs';
import { ModalService } from 'src/app/shared/services/modal.service';
import { TranslateService } from '@ngx-translate/core';
import { Utils } from 'src/app/shared/utilities/utilities';
import { ComponentCanDeactivate } from 'src/app/shared/guards/can-deactivate.guard';
import { SecurityService } from 'src/app/shared/services/security.service';
import { debounceTime, delay, distinctUntilChanged, map, mergeMap, switchMap } from 'rxjs/operators';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { environment } from 'src/environments/environment';
import { Constants } from 'src/app/shared/constants/constants';
import { Fruitore } from 'src/app/shared/models/api/cosmoauthorization/fruitore';
import { FruitoriService } from 'src/app/shared/services/fruitori.service';
import { AutorizzazioneFruitore } from 'src/app/shared/models/api/cosmoauthorization/autorizzazioneFruitore';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { EntiService } from 'src/app/shared/services/enti.service';
import { Ente } from 'src/app/shared/models/api/cosmoauthorization/ente';
import { CreaFruitoreRequest } from 'src/app/shared/models/api/cosmoauthorization/creaFruitoreRequest';
import { AggiornaFruitoreRequest } from 'src/app/shared/models/api/cosmoauthorization/aggiornaFruitoreRequest';

/*
 * ATTENZIONE - TODO - c'e' un sacco di boilerplate in questo componente.
 * occorre estendere AbstractReactiveFormComponent e rimuovere il codice comune,
 * come esempio si puo' usare aggiungi-modifica-form-logico.component.ts
 * oppure modifica-endpoint-fruitore.component.ts
 */
@Component({
  selector: 'app-aggiungi-modifica-fruitore',
  templateUrl: './aggiungi-modifica-fruitore.component.html',
  styleUrls: ['./aggiungi-modifica-fruitore.component.scss']
})
export class AggiungiModificaFruitoreComponent implements OnInit, ComponentCanDeactivate {

  debug = environment.debug;
  idFruitore?: number | null = null;
  fruitoreForm!: FormGroup;
  fruitore?: Fruitore | null = null;
  dragAndDropIsTouched = false;
  principal?: UserInfoWrapper;

  autorizzazioniDisponibili: Array<AutorizzazioneFruitore> = [];
  autorizzazioni: Array<AutorizzazioneFruitore> = [];

  entiDisponibili: Ente[] = [];
  enti: Ente[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fruitoriService: FruitoriService,
    private entiService: EntiService,
    private modalService: ModalService,
    private translateService: TranslateService,
    private securityService: SecurityService,
  ) {}

  // @HostListener allows us to also guard against browser refresh, close, etc.
  @HostListener('window:beforeunload')
  canDeactivate(): Observable<boolean> | boolean {
    // insert logic to check if there are pending changes here;
    // returning true will navigate without confirmation
    // returning false will show a confirm dialog before navigating away
    if (this.dragAndDropIsTouched || this.fruitoreForm?.dirty) {
      return false;
    }
    return true;
  }

  get isNuovo(): boolean {
    return !this.fruitore;
  }

  get formValid(): boolean {
    if (!this.fruitoreForm?.valid) {
      return false;
    }
    if (!this.enti.length) {
      return false;
    }
    return true;
  }

  ngOnInit(): void {
    combineLatest([this.securityService.getCurrentUser(), this.route.params]).pipe(
      map(latest => {
        return {
          principal: latest[0],
          idFruitore: latest[1]?.id ? +latest[1].id : undefined,
        };
      }),
      mergeMap(data => {
        return forkJoin({
          fruitoreCurrent: data.idFruitore ? this.fruitoriService.getFruitore(data.idFruitore) : of(null),
          authDisponibili: this.fruitoriService.getAutorizzazioniFruitori(JSON.stringify({
            sort: 'descrizione', page: 0, limit: 1000
          })),
          entiDisponibili: this.entiService.getEnti(JSON.stringify({
            sort: 'nome', page: 0, limit: 1000
          })),
        }).pipe(
          map(moreData => {
            return {
              ...data,
              ...moreData,
              autorizzazioniDisponibili: moreData.authDisponibili.autorizzazioni,
              entiDisponibili: moreData.entiDisponibili.enti,
            };
          })
        );
      })
    ).subscribe(loaded => {
      this.principal = loaded.principal;
      this.idFruitore = loaded.idFruitore ?? undefined;

      this.fruitore = loaded.fruitoreCurrent ?? undefined;
      this.autorizzazioni = loaded.fruitoreCurrent?.autorizzazioni ?? [];
      this.autorizzazioniDisponibili = (loaded.autorizzazioniDisponibili ?? []).filter(
        a => !this.autorizzazioni.find(a2 => a2.codice === a.codice));

      this.enti = (loaded.entiDisponibili ?? []).filter(
        e => !!(this.fruitore?.enti ?? []).find(e2 => e2?.ente?.id === e.id));

      this.entiDisponibili = (loaded.entiDisponibili ?? []).filter(
        e => !(this.fruitore?.enti ?? []).find(e2 => e2?.ente?.id === e.id));

      this.initForm();
    });
  }

  private initForm() {
    let nomeApp = '';
    let apiManagerId = '';
    let url = '';

    if (this.fruitore) {
      nomeApp = this.fruitore.nomeApp ?? '';
      apiManagerId = this.fruitore.apiManagerId ?? '';
      url = this.fruitore.url ?? '';
    }

    this.fruitoreForm = new FormGroup({
      anagrafica: new FormGroup({
        nomeApp: new FormControl({value: nomeApp, disabled: false}, [
          Validators.required,
          Validators.maxLength(50),
        ], [
          this.checkConflictingFieldFruitore('nomeApp', 'eqic')
        ]),
        apiManagerId: new FormControl({value: apiManagerId, disabled: !this.isNuovo}, [
          Validators.required,
          Validators.maxLength(50),
        ], [
          this.checkConflictingFieldFruitore('apiManagerId', 'eqic')
        ]),
        url: new FormControl({value: url, disabled: false}, [
          Validators.required,
          Validators.pattern(Constants.PATTERNS.URL_WITH_PROTOCOL),
          Validators.maxLength(1000),
        ]),
      }),
    });
  }

  drop(event: CdkDragDrop<string[]>) {
    this.fruitoreForm.markAsDirty();

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

  tornaIndietro() {
    // this.router.navigate(['back'], { relativeTo: this.route });
    window.history.back();
  }

  onSubmit() {
    // e' una modifica dell'utente
    if (this.fruitore) {
      this.submitAggiorna();
    }
    // e' un nuovo utente
    else {
      this.submitCrea();
    }
  }

  clearDirty(): void {
    this.dragAndDropIsTouched = false;
    if (this.fruitoreForm) {
      this.fruitoreForm.markAsPristine();
    }
  }

  isValid(name: string): boolean {
    if (!this.fruitoreForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return control.valid;
  }

  isInvalid(name: string): boolean {
    if (!this.fruitoreForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return control.invalid;
  }

  isTouched(name: string): boolean {
    if (!this.fruitoreForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return control.touched;
  }

  isDirty(name: string): boolean {
    if (!this.fruitoreForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return control.dirty;
  }

  isValidating(name: string): boolean {
    if (!this.fruitoreForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return (control as FormControl).status === 'PENDING';
  }

  displayValidating(name: string): boolean {
    if (!this.fruitoreForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return (control.touched || control.dirty) && (control as FormControl).status === 'PENDING';
  }

  displayInvalid(name: string): boolean {
    if (!this.fruitoreForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return (control.dirty || control.touched) && control.invalid;
  }

  getError(name: string, type: string): any {
    if (!this.fruitoreForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return control.errors && control.errors[type] ?
      (control.errors[type] ?? true) : null;
  }

  hasError(name: string, type: string): any {
    return this.getError(name, type) !== null;
  }

  hasValue(name: string): boolean {
    if (!this.fruitoreForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return Utils.isNotBlank(control.value);
  }

  getValue(name: string): any {
    if (!this.fruitoreForm) {
      return null;
    }
    const control = this.resolveControl(name) as FormControl;
    return control.value;
  }

  getControl(name: string): AbstractControl | undefined {
    if (!this.fruitoreForm) {
      return undefined;
    }
    return this.resolveControl(name) ?? undefined;
  }

  private resolveControl(name: string): AbstractControl | undefined {
    let actual: any = this.fruitoreForm;
    for (const token of name.split('.')) {
      const newControl = actual.controls[token];
      if (!newControl) {
        return undefined;
      }
      actual = newControl;
    }
    return actual as AbstractControl;
  }

  checkConflictingFieldFruitore(fieldName: string, clause: 'eq' | 'eqic' = 'eq'): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{[key: string]: any} | null> => {
      const v = (input as FormControl).value;
      if (!v) {
        return of(null);
      }

      const requestPayload: any = {
        page: 0,
        size: 1,
        fields: 'id,nomeApp,' + fieldName,
        filter: { id: this.idFruitore ? { ne: this.idFruitore } : undefined },
      };
      const fieldFilter: any = {};
      fieldFilter[clause] = v;
      requestPayload.filter[fieldName] = fieldFilter;

      return of(JSON.stringify(requestPayload)).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(filter => this.fruitoriService.getFruitori(filter)),
        map(response => {
          if (response.fruitori?.length) {
            return {
              conflict: {
                field: fieldName,
                otherValue: (response.fruitori[0] as any)[fieldName],
                otherId: response.fruitori[0].id,
                otherName: response.fruitori[0].nomeApp,
              }
            };
          }
          return null;
        })
      );
    };
  }

  buildPayloadCreazione(): CreaFruitoreRequest {
    if (!this.fruitoreForm) {
      throw new Error('Form non trovato');
    }
    const raw = this.fruitoreForm.getRawValue();

    const payload: CreaFruitoreRequest = {
      nomeApp: raw.anagrafica?.nomeApp,
      apiManagerId: raw.anagrafica?.apiManagerId,
      url: raw.anagrafica?.url,
      autorizzazioni: this.buildPayloadAutorizzazioni(),
      idEnti: this.buildPayloadEnti(),
    };

    return payload;
  }

  buildPayloadModifica(): AggiornaFruitoreRequest {
    if (!this.fruitoreForm) {
      throw new Error('Form non trovato');
    }
    const raw = this.fruitoreForm.getRawValue();

    const payload: AggiornaFruitoreRequest = {
      nomeApp: raw.anagrafica?.nomeApp,
      url: raw.anagrafica?.url,
      autorizzazioni: this.buildPayloadAutorizzazioni(),
      idEnti: this.buildPayloadEnti(),
    };

    return payload;
  }

  buildPayloadAutorizzazioni(): Array<string> {
    return (this.autorizzazioni ?? []).map(a => a.codice);
  }

  buildPayloadEnti(): Array<number> {
    return (this.enti ?? []).map(a => Utils.require(a.id, 'id ente'));
  }

  submitCrea() {
    this.fruitoriService.creaFruitore(this.buildPayloadCreazione()).subscribe(
      result => {
        this.clearDirty();
        let messaggio = this.translateService.instant('fruitori.dialogs.creato.messaggio');
        messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result?.nomeApp ?? '']);
        this.modalService.info(this.translateService.instant('fruitori.dialogs.creato.titolo'),
          messaggio).then(
            () => {
              this.tornaIndietro();
            }
          ).catch(() => { });
      },
      error => {
        this.modalService.simpleError(Utils.toMessage(error), error.error.errore).then().catch(() => {});
      }
    );
  }

  submitAggiorna() {
    if (!this.idFruitore) {
      return;
    }
    this.fruitoriService.aggiornaFruitore(this.idFruitore, this.buildPayloadModifica()).subscribe(
      result => {
        this.clearDirty();
        let messaggio = this.translateService.instant('fruitori.dialogs.modificato.messaggio');
        messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.nomeApp ?? '']);
        this.modalService.info(this.translateService.instant('fruitori.dialogs.modificato.titolo'),
          messaggio).then(
            () => {
              this.tornaIndietro();
            }
          ).catch(() => { });
      },
      error => {
        this.modalService.simpleError(Utils.toMessage(error), error.error.errore).then().catch(() => {});
      }
    );
  }
}
