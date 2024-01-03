/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit,  ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { forkJoin, Observable, of } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { AbstractReactiveFormComponent } from 'src/app/shared/components/proto/abstract-reactive-form.component';
import { SelezioneEntity } from 'src/app/shared/components/ricerca-entity/ricerca-entity.component';
import { RicercaTipoTagsComponent } from 'src/app/shared/components/ricerca-tipo-tags/ricerca-tipo-tags.component';
import { Constants } from 'src/app/shared/constants/constants';
import { TipoTag } from 'src/app/shared/models/api/cosmoauthorization/tipoTag';
import { SecurityService } from 'src/app/shared/services/security.service';
import { TagsService } from 'src/app/shared/services/tags.service';

@Component({
  selector: 'app-modifica-tipo-tag-modal',
  templateUrl: './modifica-tipo-tag-modal.component.html',
  styleUrls: ['./modifica-tipo-tag-modal.component.scss']
})
export class ModificaTipoTagModalComponent extends AbstractReactiveFormComponent<void, TipoTag | null, TipoTag>
implements OnInit {

  input?: () => Observable<TipoTag>;
  entity?: TipoTag;
  isNuovo = false;
  tuttiTipiTag: TipoTag[] = [];
  newTagWarn?: string;
  payload: TipoTag[] = [];

  loading = 0;

  @ViewChild('ricercaTipoTagsInput') ricercaTipoTagsInput: RicercaTipoTagsComponent | null = null;

  constructor(
    protected logger: NGXLogger,
    protected route: ActivatedRoute,
    protected securityService: SecurityService,
    protected modal: NgbActiveModal,
    protected modalService: NgbModal,
    protected tagsService: TagsService,
  ) {
    super(logger, route, securityService);
  }

  ricercaTipoTagsFilter = (f: TipoTag) =>
    !!f.descrizione && f.descrizione === this.form?.getRawValue()?.descrizione

  promptNewTipoTag = (results: SelezioneEntity<TipoTag>[], term: string, newEntity: TipoTag) => {
    const r = newEntity?.descrizione && !this.tuttiTipiTag.some(c => c.descrizione === newEntity.descrizione);
    if (!r && !results?.length && term?.length) {
      this.newTagWarn = 'Un tipo tag con codice\'' + newEntity.codice + '\' esiste già e non è associabile al tipo di tag corrente.';
    } else {
      this.newTagWarn = undefined;
    }
    return r;
  }

  cancel(): void {
    this.modal.dismiss({ reason: 'canceled' });
  }

  protected loadData(routeParams: any, inputData?: any): Observable<TipoTag | null> {
    if (!this.input) {
      throw new Error('No entity source provided to edit modal');
    }
    return forkJoin({
      entity: this.input(),
      tipiTags: this.tagsService.getTipiTag()
    }).pipe(
      tap(loaded => {
        this.tuttiTipiTag = loaded.tipiTags;
      }),
      map(loaded => loaded.entity)
    );
  }

 protected buildForm(routeParams: any, inputData?: never, loadedData?: TipoTag | null): FormGroup | Observable<FormGroup> {
    return new FormGroup({
      codice: new FormControl({value: loadedData?.codice ?? '', disabled: !this.isNuovo}, [
        Validators.required,
        Validators.maxLength(30),
        Validators.pattern(Constants.PATTERNS.CODICE),
      ]),
      descrizione: new FormControl({value: loadedData?.descrizione ?? '', disabled: false}, [
        Validators.required,
        Validators.maxLength(100),

      ]),
      label: new FormControl({value: loadedData?.label ?? '', disabled: false}, [
        Validators.maxLength(100),

      ])
    });
  }

  protected buildPayload(formValue: any): TipoTag {
    return {
      ...formValue,
    };
  }

  protected onSubmit(payload: TipoTag): Observable<any> {
    this.modal.close(payload);
    return of(null);
  }

}
