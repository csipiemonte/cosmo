/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { forkJoin, Observable, of } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { DocumentiService } from 'src/app/shared/components/consultazione-documenti/services/documenti.service';
import { AbstractReactiveFormComponent } from 'src/app/shared/components/proto/abstract-reactive-form.component';
import { TipoPratica } from 'src/app/shared/models/api/cosmobusiness/tipoPratica';
import { TrasformazioneDatiPratica } from 'src/app/shared/models/api/cosmopratiche/trasformazioneDatiPratica';
import { Decodifica } from 'src/app/shared/models/decodifica';
import { SecurityService } from 'src/app/shared/services/security.service';

@Component({
  selector: 'app-modifica-trasformazione-modal',
  templateUrl: './modifica-trasformazione-modal.component.html',
  styleUrls: ['./modifica-trasformazione-modal.component.scss']
})
export class ModificaTrasformazioneModalComponent
  extends AbstractReactiveFormComponent<void, TrasformazioneDatiPratica | null, TrasformazioneDatiPratica>
  implements OnInit {

  input?: () => Observable<TrasformazioneDatiPratica>;
  entity?: TrasformazioneDatiPratica;
  isNuovo = false;
  padre?: TipoPratica;

  opzioniCodiceFase: Decodifica[] = [
    { codice: 'beforeProcessStart', valore: 'Prima di avviare il processo' },
    { codice: 'afterProcessStart', valore: 'Dopo aver avviato il processo' },
  ];

  loading = 0;

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

  cancel(): void {
    this.modal.dismiss({ reason: 'canceled' });
  }

  protected loadData(routeParams: any, inputData?: any): Observable<TrasformazioneDatiPratica | null> {
    if (!this.input) {
      throw new Error('No entity source provided to edit modal');
    }
    return forkJoin({
      entity: this.input(),
    }).pipe(
      tap(loaded => {
        // nothing to load
      }),
      map(loaded => loaded.entity)
    );
  }

  protected buildForm(routeParams: any, inputData?: never, loadedData?: TrasformazioneDatiPratica | null)
    : FormGroup | Observable<FormGroup> {

    return new FormGroup({
      codiceFase: new FormControl({value: loadedData?.codiceFase ?? 'beforeProcessStart', disabled: false}, [
        Validators.required,
      ]),
      ordine: new FormControl({value: loadedData?.ordine ?? 1, disabled: false}, [
        Validators.required,
      ]),
      descrizione: new FormControl({value: loadedData?.descrizione ?? '', disabled: false}, [
        Validators.required,
        Validators.maxLength(255),
      ]),
      definizione: new FormControl({value: loadedData?.definizione ?? '', disabled: false}, [
        Validators.required,
      ]),
      obbligatoria: new FormControl({value: loadedData?.obbligatoria ?? true, disabled: false}),
    });
  }

  protected buildPayload(formValue: any): TrasformazioneDatiPratica {
    return {
      ...formValue,
    };
  }

  protected onSubmit(payload: TrasformazioneDatiPratica): Observable<any> {
    this.modal.close(payload);
    return of(null);
  }

}
