/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  Directive,
  Injector,
  OnDestroy,
  OnInit,
} from '@angular/core';

import { NGXLogger } from 'ngx-logger';
import {
  Observable,
  of,
  Subscription,
} from 'rxjs';
import { map } from 'rxjs/operators';

import { Attivita } from '../shared/models/api/cosmobusiness/attivita';
import { Pratica } from '../shared/models/api/cosmobusiness/pratica';
import { Task } from '../shared/models/api/cosmobusiness/task';
import {
  VariabileProcesso,
} from '../shared/models/api/cosmobusiness/variabileProcesso';
import {
  FormLogiciDataFromComponent,
} from '../shared/models/form-logici/form-logici-data.model';
import { FunzionalitaMultiIstanzaService } from '../shared/services/funzionalitaMultiIstanza.service';
import { PraticheService } from '../shared/services/pratiche.service';
import {
  TabBadge,
  TabLifecycleCallback,
} from './models/tab-status.models';
import { FormLogiciService } from './services/form-logici.service';

@Directive()
export class FunzionalitaParentComponent implements OnInit, OnDestroy {

  public parametri: Map<string, string> = new Map<string, string>();
  public task!: Task;
  public variabiliProcesso: VariabileProcesso[] = [];
  public pratica!: Pratica;
  public idPratica?: number;
  public childTask: Task | undefined;
  public daFirmare!: boolean;
  public readOnly = false;
  public docObbligatori!: boolean;
  public tabName!: string;
  public attivita?: Attivita;
  public idAttivita?: number;
  public dataChanged = false;
  public multiIstanzaData: Map<string, FormLogiciDataFromComponent> = new Map<string, FormLogiciDataFromComponent>();
  public dirty = false;
  private formLogiciService: FormLogiciService;
  public praticheService: PraticheService;
  private parentLogger: NGXLogger;
  public funzionalitaMultiIstanzaService: FunzionalitaMultiIstanzaService;
  public multiple!: Subscription;
  public funzMultiIstanza = false;

  constructor(public injector: Injector) {
    this.parentLogger = this.injector.get<NGXLogger>('logger' as any);
    this.formLogiciService = this.injector.get<FormLogiciService>('formLogiciService' as any);
    this.praticheService = this.injector.get<PraticheService>('praticheService' as any);
    this.parametri = this.injector.get<Map<string, string>>('parametri' as any);
    this.task = this.injector.get<Task>('task' as any);
    this.variabiliProcesso = this.injector.get<VariabileProcesso[]>('variabiliProcesso' as any);
    this.pratica = this.injector.get<Pratica>('pratica' as any);
    this.childTask = this.injector.get<Task>('childTask' as any);
    this.daFirmare = this.injector.get<boolean>('daFirmare' as any);
    this.readOnly = this.injector.get<boolean>('readOnly' as any);
    this.docObbligatori = this.injector.get<boolean>('docObbligatori' as any);
    this.attivita = this.injector.get<Attivita>('attivita' as any);
    this.tabName = this.injector.get<string>('tabName' as any);
    this.funzionalitaMultiIstanzaService = this.injector.get<FunzionalitaMultiIstanzaService>('funzionalitaMultiIstanzaService' as any);
    this.funzMultiIstanza = this.injector.get<boolean>('funzMultiIstanza' as any);
    if (this.funzMultiIstanza) {
      this.multiple = this.funzionalitaMultiIstanzaService.getMultiIstanzaData().subscribe(mi => {
        this.multiIstanzaData = mi;
      });
    }
  }
  ngOnDestroy(): void {
    if (this.multiple) {
      this.funzionalitaMultiIstanzaService.setMultiIstanzaData(new Map<string, FormLogiciDataFromComponent>());
      this.multiple.unsubscribe();
    }
  }

  patchVariabili(variabiliProcesso: VariabileProcesso[]): Observable<VariabileProcesso[]> {
    if (!this.task?.processInstanceId) {
      throw new Error('Invalid or empty processInstanceId');
    }
    if (!variabiliProcesso?.length) {
      return of([]);
    }

    variabiliProcesso.forEach(element => {
      const existing = this.variabiliProcesso.find(c => c.name === element.name);
      if (existing) {
        existing.value = element.value;
      } else {
        this.variabiliProcesso.push({ ...element });
      }
    });

    return this.praticheService.putVariabiliProcesso(this.task.processInstanceId, variabiliProcesso).pipe(
      map(response => response.variabili ?? [])
    );
  }

  get isSubtask(): boolean {
    return !!this.childTask;
  }

  ngOnInit(): void {}

  public isValid(): boolean {
    return true;
  }

  public isWip(): boolean {
    return false;
  }

  public isChanged(): boolean{
    return this.dataChanged;
  }

  public getBadges(): TabBadge | TabBadge[] | null {
    return null;
  }

  public beforeConfirm(): TabLifecycleCallback {
    this.parentLogger.debug(`[tab ${this.tabName}] - beforeConfirm: nothing to do, go on`);
  }

  public beforeSave(): TabLifecycleCallback {
    this.parentLogger.debug(`[tab ${this.tabName}] - beforeSave: nothing to do, go on`);
  }

  markDirty(): void {
    this.dirty = true;
  }

  markPristine(): void {
    this.dirty = false;
  }

  protected sendData(data: FormLogiciDataFromComponent) {
    let ret: FormLogiciDataFromComponent = {payload: {}};
    const multiIstanzaData: Map<string, FormLogiciDataFromComponent> = new Map<string, FormLogiciDataFromComponent>();
    if (this.funzMultiIstanza) {
      multiIstanzaData.set(this.tabName,
        this.setMultiIstanzaPayload( data, this.multiIstanzaData.get(this.tabName) ?? {payload: []}));
      this.funzionalitaMultiIstanzaService.setMultiIstanzaData(multiIstanzaData);
      ret = multiIstanzaData.get(this.tabName) ?? {};
    } else {
      ret = data;
    }
    this.formLogiciService.setFormLogicoData({
      ...ret,
      tabName: this.tabName
    });
  }

  private setMultiIstanzaPayload(data: FormLogiciDataFromComponent, storico: FormLogiciDataFromComponent): FormLogiciDataFromComponent {
    const ret: FormLogiciDataFromComponent = {payload: []};
    if (Array.isArray(ret.payload)) {
      if (Array.isArray(data.payload)) {
        for (const elem of data.payload) {
          if (!ret.payload.find(c  => c.name === elem.name )) {
            ret.payload.push({name: elem.name, value: elem.value});
          }
        }
      }
      if (Array.isArray(storico.payload)){
        for (const elem of storico.payload) {
          if (!ret.payload.find(c  => c.name === elem.name )) {
            ret.payload.push({name: elem.name, value: elem.value});
          }
        }
      }
    }
    return ret;
  }

}
