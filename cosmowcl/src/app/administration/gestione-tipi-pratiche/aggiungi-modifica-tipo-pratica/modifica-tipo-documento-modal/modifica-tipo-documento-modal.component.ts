/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, AsyncValidatorFn, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { forkJoin, Observable, of } from 'rxjs';
import { debounceTime, delay, distinctUntilChanged, map, mergeMap, switchMap, tap } from 'rxjs/operators';
import { DocumentiService } from 'src/app/shared/components/consultazione-documenti/services/documenti.service';
import { AbstractReactiveFormComponent } from 'src/app/shared/components/proto/abstract-reactive-form.component';
import { SelezioneEntity } from 'src/app/shared/components/ricerca-entity/ricerca-entity.component';
import { RicercaTipoDocumentoComponent } from 'src/app/shared/components/ricerca-tipo-documento/ricerca-tipo-documento.component';
import { Constants } from 'src/app/shared/constants/constants';
import { ModalStatus } from 'src/app/shared/enums/modal-status';
import { FormatoFile } from 'src/app/shared/models/api/cosmobusiness/formatoFile';
import { TipoDocumento } from 'src/app/shared/models/api/cosmopratiche/tipoDocumento';
import { SecurityService } from 'src/app/shared/services/security.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';
import { ModificaFormatoFileModalComponent } from './modifica-formato-file-modal/modifica-formato-file-modal.component';


@Component({
  selector: 'app-modifica-tipo-documento-modal',
  templateUrl: './modifica-tipo-documento-modal.component.html',
  styleUrls: ['./modifica-tipo-documento-modal.component.scss']
})
export class ModificaTipoDocumentoModalComponent extends AbstractReactiveFormComponent<void, TipoDocumento | null, TipoDocumento>
 implements OnInit {

  input?: () => Observable<TipoDocumento>;
  entity?: TipoDocumento;
  isNuovo = false;
  allegati: TipoDocumento[] = [];
  padre?: TipoDocumento;
  tuttiTipiDocumento: TipoDocumento[] = [];
  newDocWarn?: string;
  tipiDocumento: TipoDocumento[] = [];
  tipiAllegato: TipoDocumento[] = [];
  entityPadre?: TipoDocumento;
  formatiFile?: Array<FormatoFile> = [];
  loading = 0;
  codiceTipoPratica: string | null = null;

  @ViewChild('ricercaTipoDocumentoInput') ricercaTipoDocumentoInput: RicercaTipoDocumentoComponent | null = null;
  @ViewChild('defaultfocus') defaultfocus: ElementRef | null = null;


  constructor(
    protected logger: NGXLogger,
    protected route: ActivatedRoute,
    protected securityService: SecurityService,
    protected modal: NgbActiveModal,
    protected modalService: NgbModal,
    protected documentiService: DocumentiService,
  ) {
    super(logger, route, securityService);
  }

  get showAllegati(): boolean {
    return !this.padre;
  }


  ricercaTipoDocumentoFilter = (f: TipoDocumento) => f.principali?.length === 0
    || (f.principali && f.principali.length > 0 && f.principali.find(princ => princ.codice === this.form?.getRawValue()?.codice) &&
      !this.allegati.some(c => c.codice === f.codice))


  promptNewTipoDocumento = (results: SelezioneEntity<TipoDocumento>[], term: string, newEntity: TipoDocumento) => {
    const r = newEntity?.codice && !this.tuttiTipiDocumento.some(c => c.codice === newEntity.codice);
    if (!r && !results?.length && term?.length) {
      this.newDocWarn = 'Un tipo documento con codice\'' + newEntity.codice + '\' esiste già e non è associabile al tipo di documento corrente.';
    } else {
      this.newDocWarn = undefined;
    }
    return r;
  }

  cancel(): void {
    if (!!this.padre) {
      this.modal.dismiss({ type: ModalStatus.CONTINUA, done: this.buildModaleModificaTipoDocumentoPadre()});
    } else {
      this.modal.dismiss({ type: ModalStatus.CANCELED });
    }
  }

  protected loadData(routeParams: any, inputData?: any): Observable<TipoDocumento | null> {
    if (!this.input) {
      throw new Error('No entity source provided to edit modal');
    }
    return this.input().pipe(

      mergeMap(tipoDocumento => { return this.documentiService
        .ricercaTipiDocumento(tipoDocumento.codice ? [tipoDocumento.codice] : [], false, this.padre?.codice ?? null, this.codiceTipoPratica)
        .pipe(map(tipiDocumento => {
          return {
            tipoDocumento,
            tipiDocumento
          };
      }));
      }),
      tap(loaded => {
        this.tuttiTipiDocumento = loaded.tipiDocumento;
        this.allegati = loaded.tipoDocumento?.allegati ?? [];
      }),
      map(loaded => loaded.tipoDocumento)
    );

  }

  protected buildForm(routeParams: any, inputData?: never, loadedData?: TipoDocumento | null): FormGroup | Observable<FormGroup> {
    return new FormGroup({
      codice: new FormControl({value: loadedData?.codice ?? '', disabled: !this.isNuovo}, [
        Validators.required,
        Validators.maxLength(255),
        Validators.pattern(Constants.PATTERNS.CODICE),
      ], [
        this.checkConflictingFieldCodice('codice')
      ]),
      descrizione: new FormControl({value: loadedData?.descrizione ?? '', disabled: false}, [
        Validators.required,
        Validators.maxLength(500),
      ]),
      codiceStardas: new FormControl({value: loadedData?.codiceStardas ?? '', disabled: false}, [
        Validators.maxLength(200),
      ]),
      dimensioneMassima: new FormControl({value: loadedData?.dimensioneMassima ?? '', disabled: false}, [
        Validators.min(0)
      ]),
      firmabile: new FormControl({value: loadedData?.firmabile ?? false, disabled: false}),
    });
  }



  protected buildPayload(formValue: any): TipoDocumento {
    return {
      ...formValue,
      codicePadre: this.padre?.codice,
      allegati: this.allegati
    };
  }

  protected onSubmit(payload: TipoDocumento): Observable<any> {
    if (!!this.padre && this.entityPadre) {
      this.entityPadre.allegati = this.entityPadre.allegati ?? [];
      const index = this.entityPadre.allegati.findIndex(f => f.codice === payload.codice);
      if (!!this.formatiFile) {
        Utils.setPropertyValue(payload, 'formatiFile', this.formatiFile);
      }
      if (index >= 0) {
        this.entityPadre.allegati[index] = payload;
      } else {
        this.entityPadre.allegati.push(payload);
      }
      this.modal.dismiss({type: ModalStatus.CONTINUA, done: this.buildModaleModificaTipoDocumentoPadre()});
    } else {
      this.buildTipoDocumentoOnModalActions();
      this.modal.close(this.entityPadre);
    }
    return of(null);

  }

  modificaTipoDocumento(entity: TipoDocumento): void {
    this.buildTipoDocumentoOnModalActions();
    this.modal.dismiss({type: ModalStatus.CONTINUA, done: this.buildModaleModificaTipoDocumento(entity, false)});
  }

  eliminaTipoDocumento(entity: TipoDocumento): void {
    this.allegati = this.allegati.filter(c => c.codice !== entity.codice);
    if (this.entityPadre?.allegati) {
      this.entityPadre.allegati = this.allegati;
    }
  }

  buildTipoDocumentoOnModalActions() {
    const rawValues = this.form?.getRawValue();
    if (this.entityPadre) {
      this.entityPadre.descrizione = rawValues.descrizione;
      this.entityPadre.codiceStardas = rawValues.codiceStardas;
      this.entityPadre.dimensioneMassima = rawValues.dimensioneMassima;
      this.entityPadre.firmabile = rawValues.firmabile;
    }
  }

  private buildModaleModificaTipoDocumento(entity: TipoDocumento, isNuovo: boolean): NgbModalRef {
    const modal = this.modalService.open(ModificaTipoDocumentoModalComponent, {
      backdrop : true,
      keyboard : true,
      size: 'lg',

    });

    const comp = modal.componentInstance as ModificaTipoDocumentoModalComponent;
    if (!entity.formatiFile) {
      Utils.setPropertyValue(entity, 'formatiFile', this.formatiFile);
    }
    comp.input = () => of(entity);

    comp.isNuovo = isNuovo;
    comp.padre = {
      ...this.entity,
      ...this.form?.getRawValue()?.codice,
      allegati: this.allegati
    };
    comp.tipiAllegato = this.allegati;
    comp.entityPadre = this.entityPadre;
    comp.codiceTipoPratica = this.codiceTipoPratica;
    return modal;
  }

  private buildModaleModificaTipoDocumentoPadre(): NgbModalRef {
    const modal = this.modalService.open(ModificaTipoDocumentoModalComponent, {
      backdrop: true, keyboard: true, size: 'lg'
    });
    const comp = modal.componentInstance as ModificaTipoDocumentoModalComponent;
    comp.input = () => of(this.entityPadre ?? {} as TipoDocumento);

    comp.isNuovo = false;
    comp.tipiDocumento = this.tuttiTipiDocumento;
    comp.entityPadre = this.entityPadre;
    comp.tipiAllegato = this.allegati;
    comp.codiceTipoPratica = this.codiceTipoPratica;
    return modal;
  }

  tipoDocumentoSelezionato(selezione: SelezioneEntity<TipoDocumento>): void {
    if (!selezione.entity) {
      return;
    }

    this.newDocWarn = undefined;
    setTimeout(() => this.ricercaTipoDocumentoInput?.clear(), 200);
    this.buildTipoDocumentoOnModalActions();
    this.modal.dismiss({type: ModalStatus.CONTINUA, done: this.buildModaleModificaTipoDocumento(selezione.entity, !!selezione.nuovo)});
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
        switchMap(filter => this.documentiService.ricercaTipiDocumento([v], false, this.padre?.codice ?? null, this.codiceTipoPratica)),
        map(response => {
          if (response.length > 0 && response.find(single => single.codice === v)) {
            if (this.padre) {
              return {
                conflict: { field: fieldName, other: response.find(single => single.codice === v), additionalText: 'Un documento principale non può essere un allegato' }
              };
            } else {
              return {
                conflict: { field: fieldName, other: response.find(single => single.codice === v) }
              };
            }
          }
          if (!this.padre && this.tipiDocumento.length > 0 && this.tipiDocumento.find(single => single.codice === v)) {
            return {
              conflict: { field: fieldName, other: this.tipiDocumento.find(single => single.codice === v) }
            };
          }
          if (this.padre && this.tipiAllegato.length > 0 && this.tipiAllegato.find(single => single.codice === v)) {
            return {
              conflict: { field: fieldName, other: this.tipiAllegato.find(single => single.codice === v) }
            };
          }
          return null;
        })
      );
    };
  }

  openFormatiFileModal() {
    const modale = this.buildModaleModificaFormatoFile();
    modale.result.then(done => {
      if (!!this.padre) {
        this.formatiFile = done;
      } else {
        Utils.setPropertyValue(this.entityPadre, 'formatiFile', done);
      }

    });
  }

  private buildModaleModificaFormatoFile(): NgbModalRef {
    const modal = this.modalService.open(ModificaFormatoFileModalComponent, {
      backdrop : true,
      keyboard : true,
      size: 'xl',

    });
    const comp = modal.componentInstance as ModificaFormatoFileModalComponent;
    const ff = this.formatiFile && this.formatiFile?.length > 0 ? this.formatiFile : this.loadedData?.formatiFile;
    comp.formatiFile = ff ?? [];

    return modal;
  }

  checkCodiceStardas() {
    return this.form?.getRawValue().codiceStardas.length <= 0;
  }
}
