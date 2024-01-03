/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormGroup, FormControl, Validators, AsyncValidatorFn, AbstractControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal, NgbTypeahead } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { NGXLogger } from 'ngx-logger';
import { Observable, Subject, forkJoin, of } from 'rxjs';
import { delay, tap, debounceTime, distinctUntilChanged, switchMap, map } from 'rxjs/operators';
import { PreviewDocumentoModalComponent } from 'src/app/shared/components/modals/preview-documento-modal/preview-documento-modal.component';
import { AbstractReactiveFormComponent } from 'src/app/shared/components/proto/abstract-reactive-form.component';
import { Constants } from 'src/app/shared/constants/constants';
import { ComponentCanDeactivate } from 'src/app/shared/guards/can-deactivate.guard';
import { Ente } from 'src/app/shared/models/api/cosmoauthorization/ente';
import { CreaTemplateFeaRequest } from 'src/app/shared/models/api/cosmoecm/creaTemplateFeaRequest';
import { TemplateFea } from 'src/app/shared/models/api/cosmoecm/templateFea';
import { ConfigurazioneMessaggioNotifica } from 'src/app/shared/models/api/cosmonotifications/configurazioneMessaggioNotifica';
import { ConfigurazioneMessaggioNotificaRequest
} from 'src/app/shared/models/api/cosmonotifications/configurazioneMessaggioNotificaRequest';
import { TipoPratica } from 'src/app/shared/models/api/cosmonotifications/tipoPratica';
import { EntiService } from 'src/app/shared/services/enti.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { ApiUrls } from 'src/app/shared/utilities/apiurls';
import { environment } from 'src/environments/environment';
import { GestioneTipiPraticheService } from '../../gestione-tipi-pratiche/gestione-tipi-pratiche.service';
import { ConfigurazioniMessaggiNotificheService } from 'src/app/shared/services/configurazioni-messaggi-notifiche.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { TipiNotificheService } from 'src/app/shared/services/tipi-notifiche.service';
import { TipoNotifica } from 'src/app/shared/models/api/cosmonotifications/tipoNotifica';

@Component({
  selector: 'app-aggiungi-modifica-configurazione-messaggio-notifica',
  templateUrl: './aggiungi-modifica-configurazione-messaggio-notifica.component.html',
  styleUrls: ['./aggiungi-modifica-configurazione-messaggio-notifica.component.scss']
})
export class AggiungiModificaConfigurazioneMessaggioNotificaComponent extends AbstractReactiveFormComponent<void,
 ConfigurazioneMessaggioNotifica | null, ConfigurazioneMessaggioNotificaRequest> implements OnInit, ComponentCanDeactivate {

  tipiMessaggi: TipoNotifica[] = [];

  constructor(
    protected logger: NGXLogger,
    protected route: ActivatedRoute,
    protected router: Router,
    protected securityService: SecurityService,
    protected entiService: EntiService,
    protected tipiPraticheService: GestioneTipiPraticheService,
    protected configurazioniMessaggiNotificheService: ConfigurazioniMessaggiNotificheService,
    protected tipiNotificheService: TipiNotificheService,
    protected translateService: TranslateService,
    protected modalService: ModalService,
    private modal: NgbModal
    ) {
      super(logger, route, securityService);
    }

  get isNuovo(): boolean {
    return !this.loadedData?.id;
  }

  get ente(): Ente {
    return this.form?.getRawValue().identificativo?.ente?.ente ? this.form?.getRawValue().identificativo?.ente?.ente : null;
  }

  get tipoPratica(): TipoPratica {
    return this.form?.getRawValue().identificativo?.tipoPratica?.entity ?
    this.form?.getRawValue().identificativo?.tipoPratica.entity : null;
  }

  protected loadData(routeParams: any, inputData?: any): Observable<ConfigurazioneMessaggioNotifica | null> {
    const idConfigurazione = routeParams?.id ?? undefined;
    return forkJoin({
      configurazioneMessaggio: idConfigurazione ? this.configurazioniMessaggiNotificheService
      .getConfigurazioneMessaggioNotifica(idConfigurazione) : of(null),
      tipiMessaggi: this.tipiNotificheService.getTipiNotifiche()
    }).pipe(map(elem => {
      this.tipiMessaggi = elem.tipiMessaggi;
      return elem.configurazioneMessaggio;
    }
    ));
  }

  protected buildForm(routeParams: any, inputData?: never, loadedData?: ConfigurazioneMessaggioNotifica | null):
   FormGroup | Observable<FormGroup> {
    const form = new FormGroup({
      identificativo: new FormGroup({
        ente: new FormControl({value: {ente: loadedData?.ente ?? null}, disabled: false}, [
          Validators.required
        ], []),
        tipoPratica: new FormControl({value: {entity: loadedData?.tipoPratica ?? null}, disabled: false}, [], []),
        tipoMessaggio: new FormControl({value: loadedData?.tipoMessaggio ? this.tipiMessaggi
          .find(elem => elem.codice === loadedData?.tipoMessaggio?.codice) : null, disabled: false}, [Validators.required]),
      }, [], [this.checkAlreadyExistsConfigurazione()]),
      testo: new FormControl({value: loadedData?.testo ?? '', disabled: false}, [Validators.required])
    });
    (form.controls?.identificativo as FormGroup)?.controls?.ente.valueChanges.subscribe(
      () => {
        (form.controls?.identificativo as FormGroup)?.controls?.tipoPratica.patchValue(null);
      }
    );



    return form;
  }

  protected buildPayload(formValue: any): ConfigurazioneMessaggioNotificaRequest {
    return {
      codiceTipoMessaggio: formValue.identificativo.tipoMessaggio.codice,
      idEnte: formValue.identificativo.ente.ente.id,
      codiceTipoPratica: formValue.identificativo.tipoPratica?.entity?.codice ?? null,
      testo: formValue.testo
    };
  }

  protected onSubmit(payload: ConfigurazioneMessaggioNotificaRequest): Observable<any> {
    let out: Observable<ConfigurazioneMessaggioNotifica> | null = null;
    const crea = this.isNuovo;
    if (crea) {
      // crea nuovo
      out = this.configurazioniMessaggiNotificheService.creaConfigurazioneMessaggioNotifica(payload);

    } else {
      // modifica esistente
      out = this.configurazioniMessaggiNotificheService.aggiornaConfigurazioneMessaggioNotifica(
        Utils.require(this.loadedData?.id), payload);
    }

    return out.pipe(
      delay(environment.httpMockDelay),
      tap(result => {

        let messaggio = this.translateService.instant(
          'configurazioni_messaggi_notifiche.dialogs.' + (crea ? 'creato' : 'modificato') + '.messaggio');

        messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.tipoMessaggio.descrizione ?? '']);

        this.modalService.info(this.translateService.instant(
          'configurazioni_messaggi_notifiche.dialogs.' + (crea ? 'creato' : 'modificato') + '.titolo'),
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


  checkAlreadyExistsConfigurazione(): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{ [key: string]: any } | null> => {

      const controls = (input as FormGroup).controls;
      if (!controls || !controls?.tipoMessaggio?.value?.codice || !controls?.ente?.value?.ente?.id ) {
        return of(null);
      }

      const requestPayload: any = {
        filter: {
           id: this.loadedData ? { ne: this.loadedData.id } : undefined ,
           idEnte: {eq: controls?.ente?.value?.ente?.id},
           codiceTipoMessaggio: {eq: controls?.tipoMessaggio?.value?.codice},
           codiceTipoPratica:  controls?.tipoPratica?.value?.entity?.codice ? {eq: controls?.tipoPratica?.value?.entity?.codice }
            : {defined: false}
        }
      };


      return of(JSON.stringify(requestPayload)).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(val => this.configurazioniMessaggiNotificheService.getConfigurazioniMessaggiNotifiche(val)),
        map(response => {
          if (response.configurazioniMessaggi?.find(elem => (!this.loadedData || this.loadedData?.id !== elem.id))) {
            return {
              conflict: { field: 'configurazione' }
            };
          }

          return null;
        })
      );
    };
  }



  tornaIndietro() {
    window.history.back();
  }

  annullaModifiche(){
    this.form?.get('tipoPratica')?.disable();
    this.pulisciCampi();
  }


  get allValid(): boolean{

    return (this.form?.valid && this.ente != null) ?? false;
  }

  formatTipoMessaggio(tipoMessaggio: TipoNotifica){
    return (tipoMessaggio.descrizione.length > 60) ? (tipoMessaggio.descrizione.slice(0, 60) + '...') : (tipoMessaggio.descrizione);
  }




}
