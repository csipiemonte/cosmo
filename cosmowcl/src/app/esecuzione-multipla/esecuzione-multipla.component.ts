/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { delay, finalize } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { EsecuzioneMultiplaService } from './esecuzione-multipla.service';
import { ICosmoTableActionDispatchingContext, ICosmoTableAction, CosmoTableActionActivationCondition, ICosmoTableStatusSnapshot } from 'ngx-cosmo-table';
import { NGXLogger } from 'ngx-logger';
import { AttivitaEseguibileMassivamente } from '../shared/models/api/cosmopratiche/attivitaEseguibileMassivamente';
import { RicercaPraticheComponent } from '../shared/components/ricerca-pratiche/ricerca-pratiche.component';
import { Pratica } from '../shared/models/api/cosmopratiche/pratica';
import { ModalService } from '../shared/services/modal.service';
import { Utils } from '../shared/utilities/utilities';
import { PraticheService } from '../shared/services/pratiche.service';
import { Constants } from '../shared/constants/constants';
import { TranslateService } from '@ngx-translate/core';
import { SpinnerVisibilityService } from 'ng-http-loader';
import { Attivita } from '../shared/models/api/cosmopratiche/attivita';

@Component({
  selector: 'app-esecuzione-multipla',
  templateUrl: './esecuzione-multipla.component.html',
  styleUrls: ['./esecuzione-multipla.component.scss']
})
export class EsecuzioneMultiplaComponent implements OnInit {

  private codiceFunzionalitaApprovazione = 'APPROVAZIONE';
  private codiceFunzionalitaFirma = 'FIRMA-DOCUMENTI';
  private codiceFunzionalitaCustomForm = 'CUSTOM-FORM';
  private codiceFunzionalitaSimpleForm = 'SIMPLE-FORM';

  loading = 0;
  loadingError: any | null = null;


  @ViewChild('ricercaPraticheComponent') table: RicercaPraticheComponent | null = null;

  actions: ICosmoTableAction[] = [{
    name: 'start',
    label: 'Avvia esecuzione multipla',
    displayClass: 'primary btn-sm',
    activationCondition: CosmoTableActionActivationCondition.DYNAMIC
  }];

  constructor(
    private logger: NGXLogger,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private esecuzioneMultiplaService: EsecuzioneMultiplaService,
    private spinner: SpinnerVisibilityService,
    private translateService: TranslateService,
    private praticheService: PraticheService,
    private modalService: ModalService
  ) { }

  selectableStatusProvider = (item: Pratica) => {
    return !!(item.attivita?.length ?? 0);
  }

  get fetching(): boolean {
    return !this.table || (this.table.loading > 0);
  }

  get checkedItems(): AttivitaEseguibileMassivamente[] {
    return this.table?.selectedItems ?? [];
  }

  get somePresent(): boolean {
    return (this.table?.selectedItems?.length || 0) > 0;
  }

  get someSelected(): boolean {
    return (this.table?.selectedItems?.length || 0) > 0;
  }

  get singleTypeSelected(): boolean {
    return this.someSelected;
  }

  ngOnInit() {
    this.refresh();
  }

  refresh() {
    this.loading++;
    this.loadingError = null;

    forkJoin({
      tasks: of(null),
    }).pipe(
      delay(environment.httpMockDelay),
      finalize(() => {
        this.loading--;
      }),
    ).subscribe(data => {
      // NOP
    }, failure => {
      this.loadingError = failure;
    });
  }

  allowSearch = (payload: any) => {
    if (!payload?.filter?.taskMassivo?.length) {
      return false;
    }
    return true;
  }

  filterAdapter = (filter: any) => {
    return {
      ...filter,
      esecuzioneMultipla: true,
    };
  }

  actionsStatusProvider = (action: ICosmoTableAction, status: ICosmoTableStatusSnapshot | null) => {
    if (action.name === 'start') {
      return this.someSelected;
    } else {
      return false;
    }
  }

  onAction(context: ICosmoTableActionDispatchingContext) {

    if (context.action.name === 'start') {
      this.proceed(context);
    } else {
      this.logger.error('unknown action: ' + context.action.name);
    }
  }

  proceed(context: ICosmoTableActionDispatchingContext) {
    try {
      this.doProceed(context);
    } catch (err) {
      this.modalService.simpleError(Utils.toMessage(err));
    }
  }

  doProceed(context: ICosmoTableActionDispatchingContext) {
    const selectedPratiche: Pratica[] = context.selectedItems;
    const mapped: AttivitaEseguibileMassivamente[] = [];

    const filters = this.table?.lastSearchedFilter;

    const nomeAttivitaFiltro = filters.taskMassivo;

    let funzionalita: string | undefined;
    const attivita = selectedPratiche.map(prat => prat.attivita?.find(elem => elem.nome === nomeAttivitaFiltro))
                      .filter(att => att !== undefined);
    const formKey = this.setFormKey(attivita as Attivita[]);

    if (formKey){
    this.spinner.show();
    this.praticheService.getForms(formKey).pipe(finalize(() => this.spinner.hide())).subscribe( formLogico => {

        try{

          for (const el of selectedPratiche) {
            for (const att of el.attivita ?? []) {

              if (att.nome !== nomeAttivitaFiltro) {
                continue;
              }
              for (const funz of att.funzionalita ?? []) {
                if (funz.codiceFunzionalita !== this.codiceFunzionalitaApprovazione
                  && funz.codiceFunzionalita !== this.codiceFunzionalitaFirma
                  && funz.codiceFunzionalita !== this.codiceFunzionalitaCustomForm
                  && funz.codiceFunzionalita !== this.codiceFunzionalitaSimpleForm) {
                  continue;
                }

                if (funz.codiceFunzionalita === this.codiceFunzionalitaFirma){
                const parametri = formLogico.funzionalita?.filter(f => f.codice === 'FIRMA-DOCUMENTI')[0].parametri ?? [];

                const feaParam = parametri.find(form => form.chiave === Constants.SCELTA_FIRMA_FEA);
                if (feaParam?.valore){
                    const firmaFea = JSON.parse(feaParam.valore) as boolean;
                    if (firmaFea){
                      continue;
                    }
                  }
                }
                if (!funzionalita || funzionalita === funz.codiceFunzionalita ) {
                  if (funz.esecuzioneMassiva) {
                    funzionalita = funz.codiceFunzionalita;

                    mapped.push({
                      pratica: el,
                      attivita: att,
                      funzionalita: {
                        id: funz.idIstanzaFormLogico,
                        codice: funz.codiceFunzionalita
                      }
                    });
                  }
                } else if (att.funzionalita?.find(f => f.codiceFunzionalita === funzionalita)) {
                  continue;
                } else if (funzionalita && (funzionalita === this.codiceFunzionalitaApprovazione
                    || funzionalita === this.codiceFunzionalitaFirma || funzionalita === this.codiceFunzionalitaSimpleForm
                    || funzionalita === this.codiceFunzionalitaCustomForm) && funz.codiceFunzionalita !== funzionalita) {
                      throw new Error('Verificare che per il task selezionato sia presente una ed una sola funzionalita\' eseguibile massivamente');
                }
              }
            }
          }

          if (!funzionalita) {
            throw new Error('Verificare che per il task selezionato sia presente una ed una sola funzionalita\' eseguibile massivamente');
          }

          this.esecuzioneMultiplaService.setSelectedTasks(mapped);

          if (funzionalita === 'APPROVAZIONE') {
            this.router.navigate(['approvazione'], {
              relativeTo: this.activatedRoute
            });
          } else if (funzionalita === 'FIRMA-DOCUMENTI') {
            this.router.navigate(['firma'], {
              relativeTo: this.activatedRoute
            });
          } else if (funzionalita === 'CUSTOM-FORM') {
            this.router.navigate(['forms', 'custom'], {
              relativeTo: this.activatedRoute
            });
          } else if (funzionalita === 'SIMPLE-FORM') {
            this.router.navigate(['forms', 'simple'], {
              relativeTo: this.activatedRoute
            });
          } else {
            throw new Error('funzionalita\' non implementata');
          }
      }catch (err){
        this.modalService.simpleError(Utils.toMessage(err));
      }

      });
    }


  }

  private setFormKey(selected: Attivita[]){

    let formKey = null;
    if (selected){
      for (const task of selected){
        if (formKey &&  task.formKey !== formKey) {
          this.modalService.error(
            this.translateService.instant('errori.esecuzione_multipla'),
            'Form incongruenti'
          ).then(() => {
          }).catch(() => { });
          return null;
        } else {
          formKey = task.formKey ?? null;
        }
      }
    }
    return formKey;
  }

}
