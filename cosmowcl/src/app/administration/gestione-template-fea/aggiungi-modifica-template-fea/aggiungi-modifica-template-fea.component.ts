/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NGXLogger } from 'ngx-logger';
import { AbstractReactiveFormComponent } from 'src/app/shared/components/proto/abstract-reactive-form.component';
import { ComponentCanDeactivate } from 'src/app/shared/guards/can-deactivate.guard';
import { TemplateFea } from 'src/app/shared/models/api/cosmoecm/templateFea';
import { EntiService } from 'src/app/shared/services/enti.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { GestioneTipiPraticheService } from '../../gestione-tipi-pratiche/gestione-tipi-pratiche.service';
import { TipiDocumentiService } from 'src/app/shared/services/tipi-documenti/tipi-documenti.service';
import { TranslateService } from '@ngx-translate/core';
import { TemplateFeaService } from 'src/app/shared/services/template-fea.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { Observable, forkJoin, of } from 'rxjs';
import { AbstractControl, AsyncValidatorFn, FormControl, FormGroup, Validators } from '@angular/forms';
import { Utils } from 'src/app/shared/utilities/utilities';
import { CreaTemplateFeaRequest } from 'src/app/shared/models/api/cosmoecm/creaTemplateFeaRequest';
import { debounceTime, delay, distinctUntilChanged, map, switchMap, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { Ente } from 'src/app/shared/models/api/cosmoauthorization/ente';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PreviewDocumentoModalComponent } from 'src/app/shared/components/modals/preview-documento-modal/preview-documento-modal.component';
import { ApiUrls } from 'src/app/shared/utilities/apiurls';
import { TipoPratica } from 'src/app/shared/models/api/cosmoecm/tipoPratica';
import { Constants } from 'src/app/shared/constants/constants';

@Component({
  selector: 'app-aggiungi-modifica-template-fea',
  templateUrl: './aggiungi-modifica-template-fea.component.html',
  styleUrls: ['./aggiungi-modifica-template-fea.component.scss']
})
export class AggiungiModificaTemplateFeaComponent extends AbstractReactiveFormComponent<void, TemplateFea | null, CreaTemplateFeaRequest>
implements OnInit, ComponentCanDeactivate {

  isConfiguratore = false;
  nomeFile: string | null = null;
  file: File | null = null;
  maxSize!: number;
  errore: string | null = null;
  coordinataX!: number | undefined;
  coordinataY!: number | undefined;
  pagina!: number | undefined;
  infoHtml: string | null = null;
  showPage = false;

  constructor(
    protected logger: NGXLogger,
    protected route: ActivatedRoute,
    protected router: Router,
    protected securityService: SecurityService,
    protected entiService: EntiService,
    protected tipiPraticheService: GestioneTipiPraticheService,
    protected tipiDocumentiService: TipiDocumentiService,
    protected translateService: TranslateService,
    protected templateFeaService: TemplateFeaService,
    protected modalService: ModalService,
    private modal: NgbModal
    ) {
      super(logger, route, securityService);
    }

  get isNuovo(): boolean {
    return !this.loadedData?.id;
  }

  get ente(): Ente {
    return this.form?.getRawValue().ente?.ente ? this.form?.getRawValue().ente?.ente : null;
  }

  get tipoPratica(): TipoPratica {
    return this.form?.getRawValue().tipoPratica?.entity ? this.form?.getRawValue().tipoPratica.entity : null;
  }

  protected loadData(routeParams: any, inputData?: any): Observable<TemplateFea | null> {
    const idTemplateFea = routeParams?.id ?? undefined;
    return forkJoin({
      templateFea: idTemplateFea ? this.templateFeaService.get(idTemplateFea) : of(null)
    }).pipe(map(elem => {
      return elem.templateFea;
    }
    ));
  }

  protected buildForm(routeParams: any, inputData?: never, loadedData?: TemplateFea | null): FormGroup | Observable<FormGroup> {
    const form = new FormGroup({
      ente: new FormControl({value: {ente: loadedData?.ente}, disabled: false}, [
        Validators.required
      ], []),
      tipoPratica: new FormControl({value: {entity: loadedData?.tipoPratica}, disabled: false}, [
        Validators.required
      ], []),
      tipoDocumento: new FormControl({value: {entity: loadedData?.tipoDocumento}, disabled: false}, [
        Validators.required
      ], [this.checkAlreadyExistsTipoPraticaTipoDocumento('codiceTipoDocumento')]),
      descrizione: new FormControl({ value: loadedData?.descrizione ?? '', disabled: false }, [
        Validators.required,
        Validators.maxLength(500),
      ]),
      pagina: new FormControl({ value: loadedData?.pagina ?? '', disabled: false }, [
        Validators.required, Validators.min(1), Validators.pattern('^[0-9]*')
      ])
    });
    form.controls?.ente.valueChanges.subscribe(
      () => {
          form.controls.tipoPratica.patchValue(null);
          form.controls.tipoDocumento.patchValue(null);
      }
    );

    form.controls?.tipoPratica.valueChanges.subscribe(
      () => {
          form.controls.tipoDocumento.patchValue(null);
      }
    );

    this.coordinataX = loadedData?.coordinataX ?? undefined;
    this.coordinataY = loadedData?.coordinataY ?? undefined;
    this.pagina = loadedData?.pagina ?? undefined;
    if (!this.loadedData?.caricatoDaTemplate) {
      if (this.coordinataX) {
        this.infoHtml = 'coordinata x: ' + this.coordinataX + '<br>coordinata y: ' + this.coordinataY;
        this.showPage = true;
      }
    } else {
      if (this.coordinataX) {
        this.infoHtml = 'pagina: ' + this.pagina + '<br>coordinata x: ' + this.coordinataX + '<br>coordinata y: ' + this.coordinataY;
      }
    }



    this.securityService.getCurrentUser().subscribe(utente => {
    if (utente && utente.profilo && utente.profilo?.codice === Constants.CODICE_PROFILO_CONFIGURAZIONE){
      this.isConfiguratore = true;
      if (this.isNuovo){
        this.entiService.getEnte(utente.ente?.id as number).subscribe(result => {
          form.controls.ente.patchValue({ente: result.ente});
        });
      }
    }});


    return form;
  }

  protected buildPayload(formValue: any): CreaTemplateFeaRequest {
    return {
      descrizione: formValue.descrizione,
      idEnte: formValue.ente.ente.id,
      codiceTipoPratica: formValue.tipoPratica.entity.codice,
      codiceTipoDocumento: formValue.tipoDocumento.entity.codice,
      pagina: this.showPage ? formValue.pagina : this.pagina,
      coordinataX: this.coordinataX ?? 0,
      coordinataY: this.coordinataY ?? 0,
      caricatoDaTemplate: !this.showPage
    };
  }

  protected onSubmit(payload: CreaTemplateFeaRequest): Observable<any> {
    let out: Observable<TemplateFea> | null = null;
    const crea = this.isNuovo;
    if (crea) {
      // crea nuovo
      out = this.templateFeaService.create(payload);

    } else {
      // modifica esistente
      out = this.templateFeaService.update(
        Utils.require(this.loadedData?.id), payload);
    }

    return out.pipe(
      delay(environment.httpMockDelay),
      tap(result => {

        let messaggio = this.translateService.instant(
          'template_fea.dialogs.' + (crea ? 'creato' : 'modificato') + '.messaggio');

        messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.descrizione ?? '']);

        this.modalService.info(this.translateService.instant(
          'template_fea.dialogs.' + (crea ? 'creato' : 'modificato') + '.titolo'),
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

  onChange(event: any) {
    const ext = event[0].name.split('.').pop() as string;
    if (event[0].size === 0 || !'pdf'.match(ext.toLocaleLowerCase())) {

      this.setErrorFile(this.translateService.instant('errori.formato_file_non_valido'));
      return;
    }
    this.nomeFile = event[0].name;
    this.file = event[0];
    this.errore = null;
    if (this.file && this.file?.size > 0) {
      this.apriVisualizzatore();
    }
  }

  private setErrorFile(errore: string) {
    this.nomeFile = null;
    this.file = null;
    this.errore = errore;
    this.infoHtml = null;
    this.coordinataX = undefined;
    this.coordinataY = undefined;
    this.pagina = undefined;
  }

  apriVisualizzatore() {
    const modalRef = this.modal.open(PreviewDocumentoModalComponent, { size: 'xl', backdrop: 'static' });
    if (!this.file) {
      modalRef.componentInstance.url = this.getTemplatePdf();
      modalRef.componentInstance.templateType = 'void';
    } else {
      modalRef.componentInstance.file = this.file;
    }
    modalRef.componentInstance.riquadroFirma = true;
    modalRef.componentInstance.textLayer = true;
    modalRef.componentInstance.ignoreKeys = true;
    modalRef.componentInstance.pageViewMode = 'single';
    modalRef.componentInstance.showZoomButtons = false;
    modalRef.componentInstance.zoom = 'page-fit';

    modalRef.result.then(done => {
      this.file = null;
    },
    (reason) => {
      if (reason.type === 'infoPageAndAxis') {
        this.showPage = false;
        this.form?.controls?.pagina.clearValidators();
        this.coordinataX = reason.done.x;
        this.coordinataY = reason.done.y;
        this.pagina = reason.done.page;
        this.infoHtml = 'pagina: ' + this.pagina + '<br>coordinata x: ' + this.coordinataX + '<br>coordinata y: ' + this.coordinataY;
      } else if (reason.type === 'infoAxis') {
        this.coordinataX = reason.done.x;
        this.coordinataY = reason.done.y;
        this.showPage = true;
        this.form?.controls?.pagina.setValidators([Validators.required, Validators.min(1), Validators.pattern('^[0-9]*')]);
        this.infoHtml = 'coordinata x: ' + this.coordinataX + '<br>coordinata y: ' + this.coordinataY;
      }
      this.form?.controls?.pagina.updateValueAndValidity();
      this.file = null;
    }).catch(() => { });

  }

  tornaIndietro() {
    window.history.back();
  }

  annullaModifiche(){
    this.form?.get('tipoPratica')?.disable();
    this.form?.get('tipoDocumento')?.disable();
    this.pulisciCampi();
    if (this.loadedData) {
      this.coordinataX = this.loadedData?.coordinataX;
      this.coordinataY = this.loadedData?.coordinataY;
      this.pagina = this.loadedData?.pagina;
      this.infoHtml = 'pagina: ' + this.pagina + '<br>coordinata x: ' + this.coordinataX + '<br>coordinata y: ' + this.coordinataY;
    } else {
      this.coordinataX = undefined;
      this.coordinataY = undefined;
      this.pagina = undefined;
      this.errore = null;
      this.infoHtml = null;
    }
    this.file = null;
    this.showPage = false;
    this.form?.controls?.pagina.clearValidators();
    this.form?.controls?.pagina.updateValueAndValidity();
  }

  private getTemplatePdf(){
    return ApiUrls.TEMPLATE.replace('{file}', 'template_pdf_vuoto.pdf');
  }

  get allValid(): boolean{
    let positions = !!this.coordinataX && !!this.coordinataY;
    if (!this.showPage) {
      positions = positions && !!this.pagina;
    }
    return (this.form?.valid && positions) ?? false;
  }

  checkAlreadyExistsTipoPraticaTipoDocumento(fieldName: string, clause: 'eq' | 'eqic' = 'eq'): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{ [key: string]: any } | null> => {

      let v = (input as FormControl).value;
      v = v?.entity ? v.entity : null;

      if (!v) {
        return of(null);
      }

      const form = this.form?.getRawValue();

      const requestPayload: any = {
        filter: { codiceTipoDocumento: this.loadedData ? { ne: this.loadedData.tipoDocumento?.codice } : undefined },
      };
      const fieldFilter: any = {};
      fieldFilter[clause] = v.codice;
      requestPayload.filter[fieldName] = fieldFilter;


      return of(JSON.stringify(requestPayload)).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(val => this.templateFeaService.search(val)),
        map(response => {
          if (response.templateFea?.find(elem => (!this.loadedData || this.loadedData?.tipoDocumento?.codice !== elem.tipoDocumento?.codice)
          && elem.tipoPratica?.codice === form.tipoPratica?.entity?.codice)) {
            return {
              conflict: { field: 'tipoDocumento' }
            };
          }

          return null;
        })
      );
    };
  }



}
