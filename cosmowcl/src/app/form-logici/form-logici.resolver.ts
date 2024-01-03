/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
} from '@angular/router';

import { SpinnerVisibilityService } from 'ng-http-loader';
import {
  forkJoin,
  Observable,
  of,
} from 'rxjs';
import {
  finalize,
  map,
  mergeMap,
  tap,
} from 'rxjs/operators';
import { NomeFunzionalita } from 'src/app/shared/enums/nome-funzionalita';
import {
  FormLogiciConfig,
} from 'src/app/shared/models/form-logici/form-logici-config.model';
import {
  FormLogiciContext,
} from 'src/app/shared/models/form-logici/form-logici-context.model';
import { PraticheService } from 'src/app/shared/services/pratiche.service';

import { TranslateService } from '@ngx-translate/core';

import {
  FunzionalitaFormLogico,
} from '../shared/models/api/cosmopratiche/funzionalitaFormLogico';
import { LockService } from '../shared/services/lock.service';
import { SecurityService } from '../shared/services/security.service';
import { Utils } from '../shared/utilities/utilities';
import { CustomFormComponent } from './custom-form/custom-form.component';
import { CodiceMultiIstanza } from '../shared/enums/codice-multi-istanza';
import { ModalService } from '../shared/services/modal.service';

@Injectable()
export class FormLogiciResolver implements Resolve<FormLogiciContext> {
  constructor(
    private praticheService: PraticheService,
    private translateService: TranslateService,
    private securityService: SecurityService,
    private spinnerService: SpinnerVisibilityService,
    private lockService: LockService,
    private modalService: ModalService
  ) { }

  resolve(route: ActivatedRouteSnapshot): Observable<FormLogiciContext> {
    this.spinnerService.show();
    return this.resolveInner(route).pipe(
      finalize(() => this.spinnerService.hide())
    );
  }

  resolveInner(route: ActivatedRouteSnapshot): Observable<FormLogiciContext> {

    return of(route.params).pipe(
      tap(params => {
        if (!params.id) {
          throw new Error(this.translateService.instant('errori.link_task_non_valido'));
        }
      }),
      // carico in parallelo i dati del task e dell'utente corrente
      mergeMap(params => {
        return forkJoin({
          utenteCorrente: this.securityService.getCurrentUser(),
          attivita: this.praticheService.getAttivitaByTaskId(params.id),
          task: this.praticheService.getTask(params.id),
          params: of(params),
        });
      }),
      // se ho un parentTaskId sono in un subtask e devo caricare il parent
      mergeMap(data => {
        if (!data.task.parentTaskId) {
          return of({
            ...data,
            childTask: undefined,
            childAttivita: undefined,
          });
        }

        return forkJoin({
          parentTask: this.praticheService.getTask(data.task.parentTaskId),
          parentAttivita: this.praticheService.getAttivitaByTaskId(data.task.parentTaskId),
        }).pipe(map(additionalData => {
          return {
            ...data,
            task: additionalData.parentTask,
            childTask: data.task,
            attivita: additionalData.parentAttivita,
            childAttivita: data.attivita,
          };
        }));
      }),
      // controllo di avere i dati necessari
      tap(data => {
        if (!(data.utenteCorrente && data.task && data.task.formKey && data.task.processInstanceId)) {
          throw new Error(this.translateService.instant('errori.dati_inviati_non_completi'));
        }
      }),
      // se tutto va bene carico anche dati pratica e variabili di processo
      mergeMap(data => {
        data.attivita.id = Utils.require(data.attivita.id);
        data.task.processInstanceId = Utils.require(data.task.processInstanceId);
        return forkJoin({
          strutturaFormLogicoCurrent: this.praticheService.getFormsAttivita(data.attivita.id),
          praticaCurrent: this.praticheService.getInstance(data.task.processInstanceId),
          variabiliProcessoCurrent: this.praticheService.getVariabiliProcesso(data.task.processInstanceId)
        }).pipe(map(moreData => {
          return {
            ...data,
            ...moreData
          };
        }));
      }),
      // verifico di avere tutti i dati di processo
      tap(data => {
        if (!data.strutturaFormLogicoCurrent){
          this.modalService.simpleError( this.translateService.instant('errori.form_logico_inesistente'));
          throw new Error(this.translateService.instant('errori.form_logico_inesistente'));
        }
        if (!(data.strutturaFormLogicoCurrent?.funzionalita
          && data.variabiliProcessoCurrent
          && data.praticaCurrent.processo?.businessKey)
        ) {
          this.modalService.simpleError(this.translateService.instant('errori.dati_inviati_non_completi'));
          throw new Error(this.translateService.instant('errori.dati_inviati_non_completi'));
        }
      }),
      // elaboro i dati
      map(data => {
        return {
          ...data,
          funzionalita: data.strutturaFormLogicoCurrent.funzionalita,
          wizard: data.strutturaFormLogicoCurrent.wizard ?? false,
          variabiliProcesso: data.variabiliProcessoCurrent.variabili ?? [],
          instance: data.praticaCurrent.processo
        };
      }),
      // carico anche i dati della pratica
      mergeMap(data => {
      data.instance.businessKey = Utils.require(data.instance.businessKey);
      return this.praticheService.getPratica(+data.instance.businessKey, false)
        .pipe(map(pratica => {
          return {
            ...data,
            pratica
          };
      }));
      }),
      // verifico di avere tutti i dati di processo e della pratica
      tap(data => {
        if (!(data.funzionalita && data.pratica)) {
          throw new Error(this.translateService.instant('errori.dati_inviati_non_completi'));
        }
      }),
      // verifico se esiste un lock sulla pratica con LockService
      mergeMap(data => {
        return this.lockService.acquireOrGet(`@task(${data.task.id})`).pipe(map(lockResult => {
          return {
            ...data,
            ownLock: lockResult.acquired,
            otherLock: lockResult.existing,
            readOnly: (!lockResult.acquired),
          };
        }));
      }),
      // elaboro risultato finale
      map(data => {
        let funzionalita = data.funzionalita ?? [];

        if (data.childTask) {
          funzionalita = this.buildFunzionalitaPerCollaborazione(funzionalita);
        }

        const o: FormLogiciContext = {
          funzionalita,
          parametri: this.buildParametri(funzionalita),
          formLogici: this.getFormLogici(funzionalita),
          pratica: data.pratica,
          variabiliProcesso: data.variabiliProcesso,
          task: data.task,
          childTask: data.childTask,
          attivita: data.attivita,
          childAttivita: data.childAttivita,
          ownLock: data.ownLock,
          otherLock: data.otherLock,
          readOnly: data.readOnly,
          wizard: data.wizard
        };
        return o;
      })
    );
  }

  private buildFunzionalitaPerCollaborazione(funzionalita: FunzionalitaFormLogico[]): FunzionalitaFormLogico[]{

      const collaborazione = funzionalita.find(funz => funz.codice === 'COLLABORAZIONE');
      if (collaborazione) {
        // tslint:disable-next-line: max-line-length
        const formDisponibili = JSON.parse(collaborazione.parametri?.find(parametro => parametro.chiave === 'FORM_DISPONIBILI')?.valore ?? '{}') as {codiceFunzionalita: string}[];

        if (formDisponibili && formDisponibili.length > 0 ){
          const newFunzionalita: FunzionalitaFormLogico[] = [];
          funzionalita.forEach(funz => {
            if (formDisponibili.some(form => funz.codice === form.codiceFunzionalita || funz.codice === 'COLLABORAZIONE' )){
              newFunzionalita.push(funz);
            }
          });
          funzionalita = newFunzionalita;
        }

      }
      return funzionalita;
  }

  private buildParametri(funzionalita: FunzionalitaFormLogico[]): Map<string, Map<string, string>> {

    const parametriIS = this.buildParametriIstanzaSingola(funzionalita.filter(sf => !sf.multiIstanza));
    const parametriIM = this.buildParametriIstanzaMultipla(funzionalita.filter(sf => sf.multiIstanza));
    return new Map<string, Map <string, string>>([...parametriIS, ...parametriIM]);
  }

  getFormLogici(funzionalita: FunzionalitaFormLogico[]): FormLogiciConfig[] {
    const formLogici: FormLogiciConfig[] = [];

    funzionalita.forEach(singleFunz => {
      switch (singleFunz.codice) {
        case NomeFunzionalita.CONSULTAZIONE_DOCUMENTI: {
          formLogici.push(
            FormLogiciConfig.CONSULTAZIONE_DOCUMENTI);
          break;
        }
        case NomeFunzionalita.APPROVAZIONE: {
          formLogici.push(
            FormLogiciConfig.APPROVAZIONE);
          break;
        }
        case NomeFunzionalita.PIANIFICAZIONE_ATTIVITA: {
          formLogici.push(
            FormLogiciConfig.PIANIFICAZIONE_ATTIVITA);
          break;
        }
        case NomeFunzionalita.GESTIONE_DOCUMENTI: {
          formLogici.push(
            FormLogiciConfig.GESTIONE_DOCUMENTI);
          break;
        }
        case NomeFunzionalita.COMMENTI: {
          formLogici.push(
            FormLogiciConfig.COMMENTI
          );
          break;
        }
        case NomeFunzionalita.COLLABORAZIONE: {
          formLogici.push(
            FormLogiciConfig.COLLABORAZIONE);
          break;
        }
        case NomeFunzionalita.SIMPLE_FORM: {
          formLogici.push(
            FormLogiciConfig.SIMPLE_FORM
          );
          break;
        }
        case NomeFunzionalita.FIRMA_DOCUMENTI: {
          formLogici.push(
            FormLogiciConfig.FIRMA_DOCUMENTI
          );
          break;
        }
        case NomeFunzionalita.ATTIVA_SISTEMA_ESTERNO: {
          formLogici.push(
            FormLogiciConfig.ATTIVA_SISTEMA_ESTERNO
          );
          break;
        }
        case NomeFunzionalita.ASSOCIAZIONE_PRATICHE: {
          formLogici.push(
            FormLogiciConfig.ASSOCIAZIONE_PRATICHE
          );
          break;
        }
        case NomeFunzionalita.GENERAZIONE_REPORT: {
          formLogici.push(
            FormLogiciConfig.GENERAZIONE_REPORT
          );
          break;
        }
        case NomeFunzionalita.CUSTOM_FORM: {
          const ccf = singleFunz.parametri?.find(param => param.chiave === 'CODICE_CUSTOM_FORM')?.valore ?? '';
          formLogici.push(new FormLogiciConfig(
            CustomFormComponent,
            ccf,
            'Custom form',
            '#' + ccf,
            ccf + '-tab',
            NomeFunzionalita.CUSTOM_FORM, true, true));
          break;
        }
        case NomeFunzionalita.CREAZIONE_PRATICA: {
          formLogici.push(
            FormLogiciConfig.CREAZIONE_PRATICA
          );
          break;
        }
        case NomeFunzionalita.SCELTA: {
          formLogici.push(
            FormLogiciConfig.SCELTA
          );
          break;
        }
        case NomeFunzionalita.ASSEGNAZIONE_TAGS: {
          formLogici.push(
            FormLogiciConfig.ASSEGNAZIONE_TAGS
          );
          break;
        }
      }
    });
    return formLogici;
  }


  private buildParametriIstanzaSingola(fis: FunzionalitaFormLogico[]): Map<string, Map<string, string>> {
    const parametri = new Map<string, Map<string, string>>();
    fis?.forEach(singleFunzionalita => {
      const parametriFunzionalita = new Map<string, string>();
      if (singleFunzionalita && singleFunzionalita.parametri) {
        singleFunzionalita.parametri.forEach(parametro => {
          parametriFunzionalita.set( parametro.chiave ?? '', parametro.valore ?? '');
        });
        parametri.set(singleFunzionalita.codice ?? '', parametriFunzionalita);
      }
    });
    return parametri;
  }

  private buildParametriIstanzaMultipla(fim: FunzionalitaFormLogico[]): Map<string, Map<string, string>> {
    const parametri = new Map<string, Map<string, string>>();
    fim?.forEach(singleFunzionalita => {
      const parametriFunzionalita = new Map<string, string>();
      const codiceMultiIstanza = this.getCodiceMultiIstanza(singleFunzionalita.codice ?? '');
      if (singleFunzionalita && singleFunzionalita.parametri) {
        const key = singleFunzionalita.parametri.find(x => x.chiave === codiceMultiIstanza)?.valore;
        singleFunzionalita.parametri.forEach(parametro => {
          parametriFunzionalita.set( parametro.chiave ?? '', parametro.valore ?? '');
        });
        parametri.set(key ?? '', parametriFunzionalita);
      }
    });
    return parametri;

  }

  private getCodiceMultiIstanza(codiceFunzionalita: string) {
    if (codiceFunzionalita === NomeFunzionalita.CUSTOM_FORM) {
      return CodiceMultiIstanza.CUSTOM_FORM;
    }
    throw new Error('codiceFunzionalita: ' + codiceFunzionalita + ' non gestito');
  }
}
