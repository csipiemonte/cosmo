/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Location } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, ComponentFactoryResolver, HostListener, Injector, OnDestroy, OnInit, Type, ViewContainerRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { from, isObservable, Observable, of, Subscription, throwError } from 'rxjs';
import { PraticheService } from '../shared/services/pratiche.service';
import { Pratica } from '../shared/models/api/cosmobusiness/pratica';
import { FormLogiciConfig } from '../shared/models/form-logici/form-logici-config.model';
import { FormLogiciData } from '../shared/models/form-logici/form-logici-data.model';
import { ModalService } from '../shared/services/modal.service';
import { FormLogiciService } from './services/form-logici.service';
import { TabsService } from './services/tabs.service';
import { Lock } from '../shared/models/api/cosmobusiness/lock';
import { LockService } from '../shared/services/lock.service';
import { SecurityService } from '../shared/services/security.service';
import { NGXLogger } from 'ngx-logger';
import { catchError, concatMap, filter, finalize, first, map, mergeMap, share } from 'rxjs/operators';
import { DEFAULT_INTERRUPTSOURCES, Idle } from '@ng-idle/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ModalIdleWarningComponent } from './modal-idle-warning/modal-idle-warning.component';
import { ModalIdleExpiredComponent } from './modal-idle-expired/modal-idle-expired.component';
import { environment } from 'src/environments/environment';
import { Attivita } from '../shared/models/api/cosmopratiche/attivita';
import { FormLogiciContext } from '../shared/models/form-logici/form-logici-context.model';
import { Utils } from '../shared/utilities/utilities';
import { AsyncTaskModalService } from '../shared/services/async-task-modal.service';
import { OperazioneAsincrona } from '../shared/models/api/cosmobusiness/operazioneAsincrona';
import { VariabileProcesso } from '../shared/models/api/cosmobusiness/variabileProcesso';
import { Task } from '../shared/models/api/cosmobusiness/task';
import { FunzionalitaFormLogico } from '../shared/models/api/cosmopratiche/funzionalitaFormLogico';
import { AsyncTaskService } from '../shared/services/async-task.service';
import { SpinnerVisibilityService } from 'ng-http-loader';
import { BusService } from '../shared/services/bus.service';
import { FunzionalitaParentComponent } from './funzionalita-parent.component';
import { TabBadge, TabLifecycleCallback } from './models/tab-status.models';
import { CustomFormService } from '../shared/services/customForm.service';
import { FunzionalitaMultiIstanzaService } from '../shared/services/funzionalitaMultiIstanza.service';

@Component({
  selector: 'app-form-logici',
  templateUrl: './form-logici.component.html',
  styleUrls: ['./form-logici.component.scss']
})
export class FormLogiciComponent implements OnInit, OnDestroy {

  tabAttivo!: string;

  context: FormLogiciContext;
  pratica!: Pratica;
  funzionalita!: FunzionalitaFormLogico[];
  parametri: Map<string, Map <string, string>> = new Map<string, Map <string, string>>();
  formLogici: FormLogiciConfig[] = [];
  variabiliProcesso: VariabileProcesso[] = [];
  task!: Task;
  childTask: Task | undefined;
  attivita?: Attivita;
  childAttivita?: Attivita;
  daFirmare = false;
  isValid = false;
  docObbligatori = false;

  variables: FormLogiciData[] = [];

  formSubscription!: Subscription;

  principalCF: string | null = null;
  ownLock: Lock | null = null;
  otherLock: Lock | null = null;
  readOnly = true;
  timer: NodeJS.Timeout | null = null;
  renewingLock = false;
  isTaskVisible = false;

  idleWarningModal: NgbModalRef | null = null;
  idleExpiredModal: NgbModalRef | null = null;

  subscriptions: Subscription[] = [];
  tabs: {config: FormLogiciConfig, instance: FunzionalitaParentComponent}[] = [];

  @HostListener('window:unload', [ '$event' ])
  unloadHandler(event: Event) {
    this.releaseLock(true);
  }

  @HostListener('window:beforeunload', [ '$event' ])
  beforeUnloadHandler(event: Event) {
    this.releaseLock(true);
  }

  constructor(
    private logger: NGXLogger,
    private securityService: SecurityService,
    private location: Location,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private tabsService: TabsService,
    private modalService: ModalService,
    private translateService: TranslateService,
    private viewContainer: ViewContainerRef,
    private factoryResolver: ComponentFactoryResolver,
    private formLogiciService: FormLogiciService,
    private praticheService: PraticheService,
    private lockService: LockService,
    private modal: NgbModal,
    private idle: Idle,
    private asyncTaskService: AsyncTaskService,
    private asyncTaskModalService: AsyncTaskModalService,
    private spinnerVisibilityService: SpinnerVisibilityService,
    private busService: BusService,
    private funzionalitaMultiIstanzaService: FunzionalitaMultiIstanzaService
  ) {

    const resolver = this.activatedRoute.snapshot.data.formLogiciContext as FormLogiciContext;
    this.context = resolver;

    this.securityService.principal$.subscribe(principal => {
      this.principalCF = principal?.codiceFiscale;
    });

    if (!resolver || resolver instanceof HttpErrorResponse) {
      const messaggioErrore = resolver?.error?.messaggio ?? resolver?.error?.exception ??
        this.translateService.instant('errori.reperimento_dati');

      logger.error('errore nel reperimento dei dati - step 1', resolver);
      this.modalService.error(this.translateService.instant('errori.reperimento_dati'), messaggioErrore,
        resolver.error.exception)
        .then(() => this.tornaIndietro()).catch(() => { });
    } else {

      if (resolver.error) {
        logger.error('errore nel reperimento dei dati - step 2');
        this.modalService.error(this.translateService.instant('errori.reperimento_dati'), resolver.error)
          .then(() => this.tornaIndietro()).catch(() => { });
      } else {
        const idTask = resolver.childTask?.id ?? resolver.task?.id ?? '';
        // TODO spostare nel resolver
        this.praticheService.getVisibilitaByTask(idTask).subscribe(() => {
          this.isTaskVisible = true;
        }, err => {
          this.modalService.simpleError(err.error.title, err.error.errore)
          .then(() => this.router.navigate(['home']));
        });
      }
      this.setTabs(resolver);
    }
  }

  get timeBeforeIdleWarning(): number {
    return environment.idle.beforeWarning;
  }

  get timeBeforeIdleExpire(): number {
    return environment.idle.beforeTimeout;
  }

  get isLocked(): boolean {
    return !this.ownLock &&
       !!this.otherLock;
  }

  get isLockedByOther(): boolean {
    return this.isLocked && (!this.principalCF || this.principalCF !== this.otherLock?.utente?.codiceFiscale);
  }

  get isLockedByMe(): boolean {
    return this.isLocked && (!!this.principalCF && this.principalCF === this.otherLock?.utente?.codiceFiscale);
  }

  get isSubtask(): boolean {
    return !!this.childTask;
  }

  ngOnInit() {
    this.addFormsSenzaPayload();
    this.formSubscription = this.formLogiciService.getFormLogicoData().subscribe(formLogicoData => {
      if (formLogicoData.tabName) {
        const variable = this.variables.find(v => v.tabName === formLogicoData.tabName);
        if (variable) {
          this.variables[this.variables.indexOf(variable)] = formLogicoData;
        } else {
          this.variables.push(formLogicoData);
        }
      }

      // se tutti i form hanno risposto (inviando quindi il payload) si può attivare il tasto conferma
      this.isValid = (this.formLogici.length > 0 &&
        this.areVariablesAndFormLogiciBalanced() &&
        this.areAllTabsValid()) ? true : false;
    });

    this.timer = setInterval(() => this.checkLockStatus(), 5000);

    this.subscribeIdleNotifications();

    this.activatedRoute.queryParams.subscribe(routeData => {
      const newTab = routeData.tab;
      if (newTab && this.tabAttivo && newTab !== this.cleanTabRef(this.tabAttivo)) {
        this.activateTab(null, newTab);
      }
    });

    this.subscriptions.push(this.busService.getLoggingOut().subscribe(() => {
      this.idle.stop();
      this.releaseLock().subscribe();
    }));
  }

  private getStartingForm(): FormLogiciConfig {
    const activeTabRef = this.activatedRoute.snapshot.queryParams.tab;
    if (activeTabRef) {
      return this.formLogici.find(f =>
        this.cleanTabRef(activeTabRef) === this.cleanTabRef(f.ref)) ?? this.formLogici[0];
    }

    if (this.isSubtask) {
      return this.formLogici.find(f =>
        'collabora' === this.cleanTabRef(f.ref)) ?? this.formLogici[0];
    }

    return this.formLogici[0];
  }

  activateTab(event: Event | null, tabRef: string) {
    if (!tabRef) {
      return;
    }

    if (this.context.wizard && !this.readOnly && !this.isSubtask){
      this.salvaDatiCambioTab();
    }

    this.tabAttivo = this.formLogici.find(f => this.cleanTabRef(tabRef) === this.cleanTabRef(f.ref))?.ref ?? this.tabAttivo;
    this.router.navigate(
      [],
      {
        relativeTo: this.activatedRoute,
        queryParams: {
          tab: this.cleanTabRef(tabRef)

        },
        queryParamsHandling: 'merge',
    });
    if (event) {
      event.preventDefault();
      event.stopPropagation();
    }
  }

  private salvaDatiCambioTab(){
    this.activatedRoute.queryParams.pipe(first()).subscribe(value => {
      let tab: {config: FormLogiciConfig, instance: FunzionalitaParentComponent} | undefined;

      if (value.tab){
        tab = this.tabs.find(t => t.config.ref === '#' + value.tab);
      } else{
        tab = this.tabs[0];
      }

      if (tab && tab.instance.isChanged()){
        this.logger.debug('Salvo dati al cambio tab');
        this.salva();
        tab.instance.dataChanged = false;
      }
    });
  }

  private checkLockStatus() {
    if (this.renewingLock) {
      this.logger.warn('lock renewal still in progress');
      return;
    }
    if (!this.ownLock) {
      return;
    }

    if (this.ownLock?.dataScadenza) {
      const ttl = this.lockService.ttl(this.ownLock);
      if (ttl !== null && ttl < 60 * 1000) {
        this.logger.debug('lock sul processo in scadenza, rinnovo');

        this.renewingLock = true;
        this.lockService.renewLock(this.ownLock)
          .pipe(finalize(() => this.renewingLock = false))
          .subscribe(newLock => {
            this.logger.debug('lock rinnovato fino a ' + newLock?.dataScadenza);
            this.ownLock = newLock;
          }, failure => {
            this.logger.warn('erorre nel rinnovo del lock', failure);
            // TODO RELOAD IN READONLY MODE
          });
      }
    }
  }

  private areAllTabsValid(): boolean {
    // NEW
    return !this.tabs.find(t => !(t.instance?.isValid()));
  }

  private get watchInactivity(): boolean {
    return !!this.ownLock;
  }

  disableTab(formLogico: FormLogiciConfig): boolean {
    if (this.context.wizard && !this.readOnly && !this.isSubtask){

      if (this.tabs.length === 0 || this.tabs[0].config.id === formLogico.id){
        return false;
      }

      let isValid = this.tabs[0].instance.isValid();

      for (let i = 1; i < this.tabs.length; i++){
        if (this.tabs[i].config.id === formLogico.id){
          break;
        }

        if (isValid){
          isValid = this.tabs[i].instance.isValid();
        }
      }
      return !isValid;
    }else{
      return false;
    }
  }

  subscribeIdleNotifications() {
    if (!this.watchInactivity) {
      return;
    }

    this.idle.setIdle(this.timeBeforeIdleWarning);
    this.idle.setTimeout(this.timeBeforeIdleExpire);
    this.idle.setInterrupts(DEFAULT_INTERRUPTSOURCES);

    this.subscriptions.push(this.idle.onIdleStart.subscribe(() => {
      this.logger.debug('IDLE - You\'ve gone idle!');
      this.idleWarningStart();
    }));

    this.subscriptions.push(this.idle.onTimeoutWarning.subscribe((countdown: number) => {
      this.logger.debug('IDLE - You will time out in ' + countdown + ' seconds!');
      this.idleWarningTick(countdown);
    }));

    this.subscriptions.push(this.idle.onIdleEnd.subscribe(() => {
      this.logger.debug('IDLE - No longer idle.');
      this.idleWarningInterrupted();
    }));

    this.subscriptions.push(this.idle.onTimeout.subscribe(() => {
      this.logger.debug('IDLE - Timed out!');
      this.idleWarningExpired();
    }));

    this.idle.watch();
  }

  idleWarningTick(countdown: number) {
    if (this.idleWarningModal) {
      this.idleWarningModal.componentInstance.timeout = countdown;
      this.idleWarningModal.componentInstance.changed();
    }
  }

  idleWarningStart() {
    const modalRef = this.modal.open(ModalIdleWarningComponent, { size: 'lg', backdrop: 'static', keyboard: false });
    modalRef.componentInstance.reset(this.timeBeforeIdleExpire);
    this.idleWarningModal = modalRef;
  }

  idleWarningInterrupted() {
    if (this.idleWarningModal) {
      this.idleWarningModal.componentInstance.timingOut = false;
      this.idleWarningModal.componentInstance.changed();
      setTimeout(() => {
        if (this.idleWarningModal) {
          this.idleWarningModal.close('no_longer_idle');
          this.idleWarningModal = null;
        }
      }, 2000);
    }
  }

  idleWarningExpired() {
    this.idle.stop(); // remember to start again if need to rewatch

    if (this.idleWarningModal) {
      this.idleWarningModal.close('expired');
      this.idleWarningModal = null;
    }

    // actively release the lock
    const lockBackup = this.ownLock;

    this.releaseLock().pipe(
      catchError(err => {
        this.logger.error('error releasing lock', err);
        return of(null);
      })
    ).subscribe(() => {

      // lock released (or failed, don't care). opening modal
      const modalRef = this.modal.open(ModalIdleExpiredComponent, { size: 'lg', backdrop: 'static', keyboard: false });
      modalRef.componentInstance.reset(lockBackup, this.context);
      this.idleExpiredModal = modalRef;

      modalRef.result.then(closed => {
        this.logger.debug('modal closed', closed);
        this.idleExpiredModalClosed(closed);
      }, dismissed => {
        this.logger.debug('modal dismissed', dismissed);
        // vai alla home
        this.router.navigate(['/home']);
      });
    });
  }

  idleExpiredModalClosed(result: any) {
    if (result && result.relocked === true && result.lock) {
      const newLock = result.lock;
      this.ownLock = newLock;

      this.idle.watch();

    } else {
      // vai alla home
      this.router.navigate(['/home']);
    }
  }

  ngOnDestroy(): void {
    this.idle.stop();
    this.subscriptions.forEach(s => s.unsubscribe());

    if (this.viewContainer) {
      this.tabsService.destroyContainer(this.viewContainer);
    }
    this.formLogiciService.setFormLogicoData({});
    if (this.formSubscription) {
      this.formSubscription.unsubscribe();
    }

    if (this.timer) {
      clearInterval(this.timer);
    }

    this.releaseLock();
  }

  releaseLock(beacon = false): Observable<any> {
    if (this.ownLock) {
      const l = this.ownLock;
      this.ownLock = null;

      if (beacon && navigator?.sendBeacon) {
        try {
          this.lockService.releaseLockViaBeacon(l.codiceRisorsa, l.codiceOwner);
        } catch (err) {
          this.logger.warn('error releasing lock via beacon', err);
        }
      }

      const obs = this.lockService.releaseLock(l.codiceRisorsa, l.codiceOwner).pipe(share());

      obs.subscribe(() => {
        this.logger.debug('lock released', l);
      }, failure => {
        this.logger.error('error releasing lock', failure);
      });

      return obs;

    }

    return of(null);
  }

  tornaIndietro() {
    this.formLogiciService.setFormLogicoData({});
    this.location.back();
  }

  addFormsSenzaPayload() {
    this.formLogici.forEach(formLogico => {
      if (!formLogico.returnOutput) {
        this.variables.push({
          tabName: formLogico.tabName,
        });
      }
    });
  }

  conferma() {
    if (this.readOnly) {
      return;
    }

    this.askTabs(i => i.beforeConfirm()).pipe(
      mergeMap(goOn => {
        if (!goOn) {
          return of(false);
        }

        if (this.watchInactivity) {
          this.idle.stop();
        }

        this.doConferma();
        return of(true);
      }),
    ).subscribe();
  }

  salva(): void {
    if (this.readOnly) {
      return;
    }

    this.askTabs(i => i.beforeSave()).pipe(
      mergeMap(goOn => {
        if (!goOn) {
          return of(false);
        }
        if (this.watchInactivity) {
          this.idle.stop();
        }
        return this.doSave().pipe(
          mergeMap(operazione => this.waitWithSpinner(operazione)),
          finalize(() => {
            if (this.watchInactivity) {
              this.idle.watch();
            }
          })
        );
      }),
    ).subscribe(
      () => {
        // NOP
      },
      error => {
        this.modalService.error(
          this.translateService.instant('errori.salvataggio_task'),
          Utils.toMessage(error) ?? this.translateService.instant('errori.salvataggio_task'),
          error.error.errore
        ).then(() => {}).catch(() => { });
      }
    );
  }

  confermaCollaborazione(): void {
    if (this.readOnly) {
      return;
    }
    if (this.watchInactivity) {
      this.idle.stop();
    }

    const variabili = this.buildPayloadVariabili();
    this.praticheService.confermaLavorazioneTask(
      Utils.require(this.pratica?.id, 'pratica.id'),
      Utils.require(this.childAttivita?.id, 'childAttivita.id'),
      variabili,
      this.ownLock ?? undefined
    )
    .pipe(
      mergeMap(operazione =>
         from(this.asyncTaskModalService.open({ taskUUID: operazione.uuid }).result)
      )
    )
    .subscribe(
      () => {
       this.router.navigate(['home']);
      },
      error => {
        if (this.watchInactivity) {
          this.idle.watch();
        }

        this.modalService.error(
          this.translateService.instant('errori.completamento_task'),
          Utils.toMessage(error) ?? this.translateService.instant('errori.completamento_task'),
          error.error.errore
        ).then(() => {}).catch(() => {});
      }
    );
  }

  private buildPayloadVariabili(): VariabileProcesso[] {
    const variabili: VariabileProcesso[] = [];
    this.variables.forEach(variable => {
      if (variable.tabName && !this.getConfigurazione(variable.tabName)?.returnOutput) {
        return;
      }
      if (variable.payload && Object.keys(variable.payload).length > 0) {
        if (Array.isArray(variable.payload)) {
          for (const elem of variable.payload) {
            variabili.push({name: elem.name, value: elem.value});
          }
        } else {
          variabili.push({name: variable.payload.name, value: variable.payload.value});
        }
      }
    });
    return variabili;
  }

  private doSave(): Observable<OperazioneAsincrona> {
    if (this.readOnly) {
      return throwError('Save not allowed in readonly mode');
    }
    if (!(this.pratica?.id) || !(this.attivita?.id)) {
      return throwError('IdPratica oppure idAttivita mancante');
    }

    const variabili = this.buildPayloadVariabili();

    if (this.isSubtask) {
      return this.praticheService.salvaLavorazioneTask(
        Utils.require(this.pratica?.id, 'pratica.id'),
        Utils.require(this.childAttivita?.id, 'childAttivita.id'),
        variabili,
        this.ownLock ?? undefined
      );
    } else {
      return this.praticheService.salvaLavorazioneTask(
        this.pratica.id,
        this.attivita.id,
        variabili,
        this.ownLock ?? undefined
      );
    }
  }

  private setTitle(formLogici: FormLogiciConfig[]): void {
    formLogici.forEach(formLogico => {
      let param = this.parametri.get(formLogico.tabName);
      if (!param) {
        param = this.parametri.get(formLogico.description);
      }
      if (param?.get('TITOLO-TAB') && (param?.get('TITOLO-TAB') as string).length > 0) {
        formLogico.title = param?.get('TITOLO-TAB') as string;
      }
    });
  }

  private waitWithSpinner(op: OperazioneAsincrona): Observable<any> {
    if (!op?.uuid) {
      return throwError('No operation UUID');
    }

    let interval: NodeJS.Timer | undefined;
    return of(op.uuid).pipe(
      mergeMap(uuid => {
        const watcher = this.asyncTaskService.watcher(uuid);
        watcher.start();

        // Questo serve perche' le request asincrone che sono in cascata
        // col pipe corrente forzano un clear dello stato di visibilita'
        // anche se e' stato chiamato show().
        interval = setInterval(() => this.spinnerVisibilityService.show(), 100);

        return watcher.result;
      }),
      finalize(() => {
        if (interval) {
          clearInterval(interval);
        }
        this.spinnerVisibilityService.hide();
      })
    );
  }

  cleanTabRef(raw: string): string {
    if (!raw) {
      return raw;
    }
    return raw.replace('#', '');
  }

  refreshFromInfoPratica(): void {
    // qualcosa e' stato cambiato dal componente info pratica, devo ricaricare.

    const current = ['tasks', this.task.id];
    const currentParams = {
      ...(this.activatedRoute.snapshot.queryParams ?? {})
    };
    this.router.navigateByUrl('/loading', {skipLocationChange: true}).then(() => {
      this.router.navigate([...current], {
        queryParams: currentParams
      });
    });
  }

  private getCurrentFormLogicoCfgByRef() {
    const formLogicoCfg = this.formLogici.find(d => d.ref === this.tabAttivo);
    return formLogicoCfg ? formLogicoCfg : this.getStartingForm();
  }

  private doConferma(){
    this.task.action = 'complete';

    const variabili = this.buildPayloadVariabili();

    this.praticheService.confermaLavorazioneTask(
      Utils.require(this.pratica?.id, 'pratica.id'),
      Utils.require(this.attivita?.id, 'attivita.id'),
      variabili,
      this.ownLock ?? undefined
    )
    .pipe(
      mergeMap(operazione =>
         from(this.asyncTaskModalService.open({ taskUUID: operazione.uuid }).result)
      )
    )
    .subscribe(
      () => {
       this.router.navigate(['home']);
      },
      error => {
        if (this.watchInactivity) {
          this.idle.watch();
        }

        this.modalService.error(
          this.translateService.instant('errori.completamento_task'),
          Utils.toMessage(error) ?? this.translateService.instant('errori.completamento_task'),
          error.error.errore
        ).then(() => {}).catch(() => {});
      }
    );
  }

  private setTabs(resolver: any) {
    this.parametri = Utils.require(resolver.parametri, 'parametri');
    this.funzionalita = Utils.require(resolver.funzionalita, 'funzionalita');
    this.pratica = Utils.require(resolver.pratica, 'pratica');
    this.task = Utils.require(resolver.task, 'task');
    this.childTask = resolver.childTask;
    this.formLogici = Utils.require(resolver.formLogici, 'formLogici');
    this.tabsService.formLogici = Utils.require(resolver.formLogici, 'formLogici');
    this.variabiliProcesso = Utils.require(resolver.variabiliProcesso, 'variabiliProcesso');
    this.ownLock = resolver.ownLock ?? null;
    this.otherLock = resolver.otherLock ?? null;
    this.readOnly = resolver.readOnly ?? false;
    this.attivita = resolver.attivita;
    this.childAttivita = resolver.childAttivita;
    this.tabAttivo = this.getStartingForm().ref;
    this.setTitle(this.formLogici);

    this.tabsService.onContainerCreated((container: ViewContainerRef, config: FormLogiciConfig) => {
      const component: Type<any> = config.formLogico;
      this.viewContainer = container;
      const injector = Injector.create({ providers: [
        { provide: 'formLogiciService', useValue: this.formLogiciService },
        { provide: 'praticheService', useValue: this.praticheService },
        { provide: 'funzionalitaMultiIstanzaService', useValue: this.funzionalitaMultiIstanzaService },
        { provide: 'formLogico', useValue: config },
        { provide: 'tabName', useValue: config.tabName },
        { provide: 'idPratica', useValue: this.pratica?.id },
        { provide: 'pratica', useValue: this.pratica },
        { provide: 'task', useValue: this.task },
        { provide: 'childTask', useValue: this.childTask },
        { provide: 'attivita', useValue: this.attivita },
        { provide: 'idAttivita', useValue: this.attivita?.id },
        { provide: 'childAttivita', useValue: this.childAttivita },
        { provide: 'parametri', useValue: this.getFilteredParameters(config) },
        { provide: 'variabiliProcesso', useValue: this.variabiliProcesso },
        { provide: 'daFirmare', useValue: this.daFirmare },
        { provide: 'readOnly', useValue: this.readOnly },
        { provide: 'docObbligatori', useValue: this.docObbligatori},
        { provide: 'logger', useValue: this.logger},
        { provide: 'funzMultiIstanza', useValue: config.multiIstanza},
      ] });

      const factory = this.factoryResolver.resolveComponentFactory(component);
      if (this.viewContainer) {
        const instance = this.viewContainer.createComponent(factory, 0, injector).instance as FunzionalitaParentComponent;
        instance.idPratica = this.pratica.id;
        instance.idAttivita = this.attivita?.id;
        instance.tabName = Utils.require(config.tabName);
        this.tabs.push({instance, config});
      }
    });

    this.tabsService.onContainerDestroyed((container: ViewContainerRef) => {
      this.viewContainer = container;
      if (this.viewContainer) {
        this.viewContainer.clear();
      }
    });
  }

  private askTabs(extractor: (instance: FunzionalitaParentComponent) => TabLifecycleCallback): Observable<boolean> {
    if (!this.tabs.length) {
      return of(true);
    }
    const endMarker = {};

    return from([...this.tabs, endMarker]).pipe(
      concatMap((tab: any) => {
        if (tab === endMarker) {
          return of(endMarker);
        }
        const tabResult = extractor(tab.instance);
        return isObservable(tabResult) ? tabResult : of(tabResult);
      }),
      catchError(err => {
        this.logger.warn('error from tab callback', err);
        return of(false);
      }),
      filter(v => v === false || v === endMarker),
      first(),
      map(v => v === endMarker),
    );
  }

  getConfigurazione(ref: string): FormLogiciConfig | null {
    return this.formLogici.find(c => c.tabName === ref) ?? null;
  }

  getInstance(form: FormLogiciConfig): FunzionalitaParentComponent | null {
    return ((this.tabs ?? []).find(t => t.config.ref === form.ref)?.instance) ?? null;
  }

  getBadges(form: FormLogiciConfig): TabBadge[] {
    const instance = this.getInstance(form);
    let result: TabBadge[] = [];
    if (instance) {
      const instanceBadges = instance.getBadges();
      if (instanceBadges) {
        result = Array.isArray(instanceBadges) ? instanceBadges : [instanceBadges];
      }
      if (instance.isWip()) {
        result.push({class: 'warning'});
      }
    }

    return result;
  }

  getLatestData(form: FormLogiciConfig): FormLogiciData | null {
    return this.variables.find(i => i.tabName === form.tabName) ?? null;
  }

  /* in caso di funzionalita singola, viene fatto il match con il tabName della configurazione
     in caso contrario invece, si fa il match con la description, inserita ad hoc nel resolver */
  private getFilteredParameters(config: FormLogiciConfig) {
     const parametersByTabName = this.parametri.get(config.tabName);
     if (parametersByTabName) {
       return parametersByTabName;
     } else {
      return this.parametri.get(config.description);
     }
  }

  private areVariablesAndFormLogiciBalanced() {
    let numberOfMI = this.formLogici.filter(fl => fl.multiIstanza === true).length;
    if (numberOfMI > 0) {
      --numberOfMI;
    }
    return (this.variables.length === (this.formLogici.length - numberOfMI));
  }

}
