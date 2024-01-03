/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormGroup, Validators, FormControl, AbstractControl, AsyncValidatorFn } from '@angular/forms';
import { Utils } from '../../../shared/utilities/utilities';
import { ActivatedRoute } from '@angular/router';
import { debounceTime, delay, distinctUntilChanged, map, switchMap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { Observable, of } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { ModalService } from '../../../shared/services/modal.service';
import { Tag } from 'src/app/shared/models/api/cosmoauthorization/tag';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { RiferimentoEnte } from 'src/app/shared/models/api/cosmoauthorization/riferimentoEnte';
import { TipoTag } from 'src/app/shared/models/api/cosmoauthorization/tipoTag';
import { RicercaTipoTagsComponent } from 'src/app/shared/components/ricerca-tipo-tags/ricerca-tipo-tags.component';
import { SelezioneEntity } from 'src/app/shared/components/ricerca-entity/ricerca-entity.component';
import { ModificaTipoTagModalComponent } from './modifica-tipo-tag-modal/modifica-tipo-tag-modal.component';
import { Ente } from 'src/app/shared/models/api/cosmoauthorization/ente';
import { CreaTagRequest } from 'src/app/shared/models/api/cosmoauthorization/creaTagRequest';
import { AggiornaTagRequest } from 'src/app/shared/models/api/cosmoauthorization/aggiornaTagRequest';
import { TagsService } from 'src/app/shared/services/tags.service';

@Component({
  selector: 'app-aggiungi-modifica-tag',
  templateUrl: './aggiungi-modifica-tag.component.html',
  styleUrls: ['./aggiungi-modifica-tag.component.scss']
})

export class AggiungiModificaTagComponent implements OnInit {

  tgForm!: FormGroup;
  tg?: Tag;
  idTag!: number;
  tipiTags: TipoTag[] = [];
  ente: Ente[] = [];

  cloneTipiTag: TipoTag[] = [];
  cloneEnte: Ente[] = [];

  @ViewChild('ricercaTipoTagsInput') ricercaTipoTagsInput: RicercaTipoTagsComponent | null = null;

  constructor(
    private route: ActivatedRoute,
    private tagsService: TagsService,
    private translateService: TranslateService,
    private modalService: ModalService,
    protected modal: NgbModal,
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params && params.id) {
        this.idTag = params.id;
        this.tagsService.get(params.id).subscribe(tg => {
          if (tg && tg.tag) {
            this.tg = tg.tag;
            this.initForm();
          }
        });
      } else {
        this.initForm();
      }
    });
  }

  tornaIndietro() {
    window.history.back();
  }

  private initForm() {
    let codice = '';
    let descrizione = '';
    let ente: RiferimentoEnte | null = null;
    let tipoTag: TipoTag | null = null;

    if (this.tg) {
      codice = this.tg.codice;
      descrizione = this.tg.descrizione ?? '';
      ente = this.tg.ente ?? null;
      tipoTag = this.tg.tipoTag ?? null;
    }

    this.tgForm = new FormGroup({
      codice: new FormControl({ value: codice, disabled: !this.isNuovo }, [
        Validators.required,
        Validators.maxLength(30),
      ], [
        this.checkConflictingFieldCodice('codice', 'eqic')
      ]),
      descrizione: new FormControl({ value: descrizione, disabled: false }, [
        Validators.maxLength(100),
      ]),
      ente: new FormControl({ value: { ente }, disabled: !this.isNuovo }, [
        Validators.required
      ], [
        this.checkConflictingFieldEnte('idEnte', 'eq')
      ]),
      tipoTag: new FormControl({ value: { entity: tipoTag }, disabled: !this.isNuovo }, [
        Validators.required
      ], [
        this.checkConflictingFieldCodiceTipoTag('codiceTipoTag', 'eqic')
      ]),
    });
  }

  checkConflictingField(requestPayload: any, fieldName: string) {
    return of(JSON.stringify(requestPayload)).pipe(
      debounceTime(500),
      distinctUntilChanged(),
      delay(environment.httpMockDelay),
      switchMap(filter => this.tagsService.getTags(filter)),
      map(response => {
        if (response.elementi?.length && this.tgForm.getRawValue().tipoTag.entity
          && this.tgForm.getRawValue().ente.ente && this.tgForm.getRawValue().codice) {
          const resp = response.elementi
            .filter(c => c.codice === this.tgForm.getRawValue().codice)
            .filter(c => c.tipoTag.codice === this.tgForm.getRawValue().tipoTag.entity.codice)
            .find(c => c.ente.codiceIpa === this.tgForm.getRawValue().ente.ente.codiceIpa);
          if (resp) {
            return {
              conflict: { field: fieldName, other: resp }
            };
          }
        }
        return null;
      })
    );
  }

  checkConflictingFieldCodice(fieldName: string, clause: 'eq' | 'eqic' = 'eq'): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{ [key: string]: any } | null> => {
      const v = (input as FormControl).value;
      const rawValues = this.tgForm?.getRawValue();

      if (!v || !rawValues.tipoTag?.entity || !rawValues.ente?.ente) {
        return of(null);
      }

      const requestPayload: any = {
        page: 0,
        size: 100,
        filter: {}
      };
      const codiceFilter: any = {};
      codiceFilter[clause] = v;
      requestPayload.filter[fieldName] = codiceFilter;
      const tipoTagFilter: any = {};
      tipoTagFilter[clause] = rawValues.tipoTag.entity.codice;
      requestPayload.filter.codiceTipoTag = tipoTagFilter;
      const enteFilter: any = {};
      enteFilter[clause] = rawValues.ente.ente.id;
      requestPayload.filter.idEnte = enteFilter;

      this.removeError('ente');
      this.removeError('tipoTag');

      return this.checkConflictingField(requestPayload, fieldName);
    };
  }

  checkConflictingFieldEnte(fieldName: string, clause: 'eq' | 'eqic' = 'eq'): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{ [key: string]: any } | null> => {
      const v = (input as FormControl).value;
      const rawValues = this.tgForm?.getRawValue();
      if (!rawValues || !v || !v.ente || !rawValues.tipoTag?.entity || !rawValues.codice) {
        return of(null);
      }

      const requestPayload: any = {
        page: 0,
        size: 100,
        filter: {}
      };
      const enteFilter: any = {};
      enteFilter[clause] = v.ente.id;
      requestPayload.filter[fieldName] = enteFilter;
      const codiceFilter: any = {};
      codiceFilter[clause] = rawValues.codice.codice;
      requestPayload.filter.codice = codiceFilter;
      const tipoTagFilter: any = {};
      tipoTagFilter[clause] = rawValues.tipoTag.entity.codice;
      requestPayload.filter.codiceTipoTag = tipoTagFilter;

      this.removeError('codice');
      this.removeError('tipoTag');

      return this.checkConflictingField(requestPayload, fieldName);
    };
  }

  checkConflictingFieldCodiceTipoTag(fieldName: string, clause: 'eq' | 'eqic' = 'eq'): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{ [key: string]: any } | null> => {
      const v = (input as FormControl).value;
      const rawValues = this.tgForm?.getRawValue();
      if (!rawValues || !v || !v.entity || !rawValues.ente?.ente?.id || !rawValues.codice) {
        return of(null);
      }

      const requestPayload: any = {
        page: 0,
        size: 100,
        filter: {}
      };
      const tipoTagFilter: any = {};
      tipoTagFilter[clause] = v.entity.codice;
      requestPayload.filter[fieldName] = tipoTagFilter;
      const enteFilter: any = {};
      enteFilter[clause] = rawValues.ente.ente.id;
      requestPayload.filter.idEnte = enteFilter;
      const codiceFilter: any = {};
      codiceFilter[clause] = rawValues.codice.codice;
      requestPayload.filter.codice = codiceFilter;

      this.removeError('ente');
      this.removeError('codice');

      return this.checkConflictingField(requestPayload, fieldName);
    };
  }

  private getControl(name: string): AbstractControl | undefined {
    if (!this.tgForm) {
      return undefined;
    }
    return this.resolveControl(name) ?? undefined;
  }

  private removeError(fieldName: string){
    const control = this.getControl(fieldName);
    if (control?.errors?.conflict){
      const errors = control.errors;
      errors.conflict = null;
      control?.setErrors(errors);
      control.updateValueAndValidity();
    }
  }

  tipoTagSelezionato(selezione: SelezioneEntity<TipoTag>): void {
    if (!selezione.entity) {
      return;
    }
    if (selezione.nuovo) {
      const modal = this.buildModaleModificaTipoTag(selezione.entity, true);

      modal.result.then((done: TipoTag) => {
        if (selezione.entity) {
          Object.assign(selezione.entity, { ...done });
        }
        this.tipiTags.push(done);
      }).catch(() => { });
    } else {
      this.tipiTags = this.cloneTipiTag.map(x => Object.assign({}, x));
    }
  }


  private buildModaleModificaTipoTag(entity: TipoTag, isNuovo: boolean): NgbModalRef {
    const modal = this.modal.open(ModificaTipoTagModalComponent, {
      backdrop: true, keyboard: true, size: 'lg'
    });
    const comp = modal.componentInstance as ModificaTipoTagModalComponent;
    comp.input = () => of(entity);
    comp.isNuovo = isNuovo;

    return modal;
  }

  hasValue(name: string): boolean {
    if (!this.tgForm) {
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
    if (!this.tgForm) {
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
    if (!this.tgForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return (control.dirty || control.touched) && control.invalid;
  }

  displayValidating(name: string): boolean {
    if (!this.tgForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return (control.touched || control.dirty) && (control as FormControl).status === 'PENDING';
  }

  private resolveControl(name: string): AbstractControl | undefined {
    let actual: any = this.tgForm;
    for (const token of name.split('.')) {
      const newControl = actual.controls[token];
      if (!newControl) {
        return undefined;
      }
      actual = newControl;
    }
    return actual as AbstractControl;
  }

  submit() {
    const raw = this.tgForm.getRawValue();
    const payload: AggiornaTagRequest = {
      codice: raw.codice ?? '',
      descrizione: raw.descrizione ?? '',
      ente: raw.ente.ente,
      tipoTag: raw.tipoTag.entity,
    };
    if (this.idTag) {
      this.tagsService.updateTag(this.idTag, payload).subscribe(result => {
        if (result) {
          delay(environment.httpMockDelay);
          let messaggio = this.translateService.instant('form_tag.dialogs.modificato.messaggio');
          messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.tag?.codice ?? '']);
          this.modalService.info(this.translateService.instant('form_tag.dialogs.modificato.titolo'), messaggio)
            .then(() => {
              this.tornaIndietro();
            });
        }
      }, err => {
        this.modalService.simpleError(Utils.toMessage(err.error.title), err.error.errore);
      });
    }
    else {
      const pyload: CreaTagRequest = {
        codice: raw.codice ?? '',
        descrizione: raw.descrizione ?? '',
        ente: raw.ente.ente,
        tipoTag: raw.tipoTag.entity,
      };
      this.tagsService.createTag(pyload).subscribe(result => {
        if (result) {
          delay(environment.httpMockDelay);
          let messaggio = this.translateService.instant('form_tag.dialogs.creato.messaggio');
          messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.tag?.codice ?? '']);
          this.modalService.info(this.translateService.instant('form_tag.dialogs.creato.titolo'), messaggio)
            .then(() => {
              this.tornaIndietro();
            });
        }
      }, err => {
        this.modalService.simpleError(Utils.toMessage(err.error.title), err.error.errore);
      });
    }
  }

  get isNuovo(): boolean {
    return !this.tg;
  }

  pulisciCampi() {
    this.tipiTags = this.cloneTipiTag.map(x => Object.assign({}, x));
    this.ente = this.cloneEnte.map(x => Object.assign({}, x));
    this.initForm();
  }

  isValid(): boolean {
    const raw = this.tgForm?.getRawValue();
    return (this.tgForm?.valid && raw && raw.ente && raw.ente.ente && raw.ente.ente.id
            && raw.tipoTag && raw.tipoTag.entity && raw.tipoTag.entity.codice);
  }
}
