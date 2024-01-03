/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Injector, OnDestroy } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { NGXLogger } from 'ngx-logger';
import { forkJoin, Observable, of, Subscription } from 'rxjs';
import { delay, finalize, tap } from 'rxjs/operators';
import { OperazioneAsincrona } from 'src/app/shared/models/api/cosmobusiness/operazioneAsincrona';
import { TipoDocumento } from 'src/app/shared/models/api/cosmobusiness/tipoDocumento';
import { RichiediGenerazioneReportRequest } from 'src/app/shared/models/api/cosmoecm/richiediGenerazioneReportRequest';
import { OperazioneAsincronaWrapper } from 'src/app/shared/models/async';
import { Decodifica } from 'src/app/shared/models/decodifica';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { AsyncTaskService } from 'src/app/shared/services/async-task.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { ReportService } from 'src/app/shared/services/report.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { TipiDocumentiService } from 'src/app/shared/services/tipi-documenti/tipi-documenti.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { FunzionalitaParentReactiveComponent } from '../funzionalita-parent-reactive.component';
import { TabBadge } from '../models/tab-status.models';
import { VariabileProcesso } from 'src/app/shared/models/api/cosmobusiness/variabileProcesso';
import { environment } from 'src/environments/environment';

export const Parametri = {
  REPORT_GENERABILI: 'REPORT_GENERABILI',
};

interface ReportGenerabiliParameter {
  id: number;
  codiceTemplate: string;
  descrizione: string;
  mappaturaParametri: string;
  codiceTipoDocumento?: string;
  nomeFile?: string;
  titolo?: string;
  formato?: string;
  sovrascrivi?: boolean;
  input?: ReportGenerabiliInputParameter[];
}

interface ReportGenerabiliInputParameter {
  titolo?: string;
  placeholder?: string;
  variabile?: string;
  codiceParametro?: string;
  lunghezzaMassima?: number;
  obbligatorio?: boolean;
}

interface TaskGenerazioneReport {
  tipo: ReportGenerabiliParameter;
  formato?: Decodifica;
  request: RichiediGenerazioneReportRequestExt;
  taskId: string;
  status: OperazioneAsincrona | OperazioneAsincronaWrapper<unknown>;
  finished: boolean;
  completed?: boolean;
  failed?: boolean;
  stepDescription?: string;
  errorMessage?: string;
}

interface RichiediGenerazioneReportRequestExt extends RichiediGenerazioneReportRequest {
  idReport: number;
}

@Component({
  selector: 'app-generazione-report',
  templateUrl: './generazione-report.component.html',
  styleUrls: ['./generazione-report.component.scss']
})
// tslint:disable-next-line:max-line-length
export class GenerazioneReportComponent extends FunzionalitaParentReactiveComponent<ReportGenerabiliParameter[], any, RichiediGenerazioneReportRequestExt> implements OnDestroy {

  cacheVariabili: VariabileProcesso[] = [];

  reportGenerabili?: ReportGenerabiliParameter[];
  tipiDocumento: TipoDocumento[] = [];
  principal?: UserInfoWrapper;
  formati: Decodifica[] = [
    { codice: 'PDF', valore: 'PDF (.pdf)' },
    { codice: 'DOCX', valore: 'Word (.docx)' }
  ];

  tasks: TaskGenerazioneReport[] = [];

  subscriptions: Subscription[] = [];

  editorConfigs: any[] = [];

  constructor(
    public injector: Injector,
    protected logger: NGXLogger,
    protected tipiDocumentiService: TipiDocumentiService,
    protected securityService: SecurityService,
    protected reportService: ReportService,
    protected asyncTaskService: AsyncTaskService,
    protected modalService: ModalService
  ) {
    super(injector, logger);
    this.cacheVariabili = [...this.variabiliProcesso];
  }

  public getBadges(): TabBadge | TabBadge[] | null {
    const out: TabBadge[] = [];

    const numFailed = this.tasks.filter(c => c.failed).length;
    if (numFailed > 0) {
      out.push({ text: '' + numFailed, class: 'danger', icon: 'fas fa-exclamation-triangle' });
      return out;
    }

    return null;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => {
      if (!s.closed) {
        s.unsubscribe();
      }
    });
  }

  refresh() {
    this.reset();
  }

  public get tipoSelezionato(): ReportGenerabiliParameter | null {
    const cod = this.form?.getRawValue()?.reportSelezionato;
    return cod ?? null;
  }

  public get singoloTipo(): boolean {
    return (this.reportGenerabili?.length ?? 0) === 1;
  }

  public get singoloTipoDocumento(): boolean {
    return !!(this.tipoSelezionato?.codiceTipoDocumento?.length);
  }

  public get valoreForzatoParametroSovrascrivi(): boolean {
    return this.tipoSelezionato?.sovrascrivi === true || this.tipoSelezionato?.sovrascrivi === false;
  }

  public isValid(): boolean {
    if (this.loading > 0) {
      return false;
    }
    if (this.isSubtask) {
      return true;
    }
    if (this.isWip()) {
      return false;
    }
    return true;
  }

  public isWip(): boolean {
    return !!this.tasks.find(x => !x.finished);
  }

  protected getInputData(): Observable<ReportGenerabiliParameter[] | null> {
    return of(this.parseParametri());
  }

  protected loadData(inputData?: any): Observable<any> {
    return forkJoin({
      tipiDocumento: this.tipiDocumentiService.getTipiDocumenti(this.idPratica!, null),
      principal: this.securityService.getCurrentUser(),
    }).pipe(
      tap(data => {
        this.tipiDocumento = data.tipiDocumento;
        this.principal = data.principal;
      })
    );
  }

  protected buildForm(inputData?: ReportGenerabiliParameter[], loadedData?: any): FormGroup | Observable<FormGroup> {
    let startingType: ReportGenerabiliParameter | null = null;
    if ((this.reportGenerabili?.length ?? 0) === 1) {
      startingType = this.reportGenerabili![0];
    }
    const form = new FormGroup({
      reportSelezionato: new FormControl({ value: startingType, disabled: ((this.reportGenerabili?.length ?? 0) < 2) }, [
        Validators.required
      ]),
      tipoDocumento: new FormControl({ value: null, disabled: true }, []),
      titolo: new FormControl({ value: null, disabled: true }, [
        Validators.required,
        Validators.maxLength(255)
      ]),
      nomeFile: new FormControl({ value: null, disabled: true }, [
        Validators.required,
        Validators.maxLength(255)
      ]),
      formato: new FormControl({ value: null, disabled: true }, [
        Validators.required,
      ]),
      sovrascrivi: new FormControl({ value: false, disabled: true }, []),
      input: new FormArray([]),
    });

    const onReportSelezionato = (nuovoReportSelezionato: ReportGenerabiliParameter) => {
      form.patchValue({
        titolo: nuovoReportSelezionato?.titolo ?? nuovoReportSelezionato?.descrizione ?? '',
        nomeFile: nuovoReportSelezionato?.nomeFile ?? nuovoReportSelezionato?.titolo ?? '',
        formato: this.formati.find(c => c.codice === (nuovoReportSelezionato?.formato ?? 'PDF').toUpperCase()),
        sovrascrivi: nuovoReportSelezionato?.sovrascrivi ?? false,
      });

      const dependentControls = ['tipoDocumento', 'titolo', 'nomeFile', 'formato', 'sovrascrivi'];
      if (nuovoReportSelezionato?.codiceTemplate?.length) {
        dependentControls.forEach(dc => form.get(dc)?.enable());
      } else {
        dependentControls.forEach(dc => form.get(dc)?.disable());
      }

      if (nuovoReportSelezionato?.codiceTipoDocumento) {
        const tipo = this.tipiDocumento.find(c => c.codice === nuovoReportSelezionato.codiceTipoDocumento);
        if (tipo) {
          form.patchValue({ tipoDocumento: tipo });
          form.get('tipoDocumento')?.disable();
        }
      }

      if (nuovoReportSelezionato?.sovrascrivi === true || nuovoReportSelezionato?.sovrascrivi === false) {
        form.patchValue({ sovrascrivi: nuovoReportSelezionato?.sovrascrivi });
        form.get('sovrascrivi')?.disable();
      }

      const inputControlsArray = form.get('input') as FormArray;
      inputControlsArray.clear();
      this.editorConfigs = [];
      if (nuovoReportSelezionato?.input?.length) {
        for (const input of nuovoReportSelezionato.input) {
          let startingVal = '';
          if (input.variabile) {
            const corrVar = this.cacheVariabili.find(v => v.name === input.variabile);
            if (corrVar) {
              startingVal = corrVar.value + '';
            }
          }

          inputControlsArray.push(
            new FormControl({ value: startingVal, disabled: false }, [
              ...(input.obbligatorio ? [Validators.required] : []),
              Validators.maxLength(input.lunghezzaMassima ?? 1000)
            ])
          );
          this.editorConfigs.push({
            maxlength: input.lunghezzaMassima ?? null,
            placeholder: input.placeholder ?? input.titolo ?? '',
          });
        }
      }
    };

    if (startingType) {
      onReportSelezionato(startingType);
    }
    form.get('reportSelezionato')!.valueChanges.subscribe(onReportSelezionato);

    return form;
  }

  protected buildPayload(formValue: any): RichiediGenerazioneReportRequestExt {
    if (!this.idPratica) {
      throw new Error('IdPratica non presente');
    }

    const selez = (formValue.reportSelezionato as ReportGenerabiliParameter);

    const req: RichiediGenerazioneReportRequestExt = {
      idReport: selez?.id,
      idPratica: this.idPratica,
      codiceTemplate: formValue.reportSelezionato?.codiceTemplate,
      codiceTipoDocumento: formValue.tipoDocumento?.codice,
      mappaturaParametri: formValue.reportSelezionato?.mappaturaParametri,
      nomeFile: formValue.nomeFile?.trim(),
      titolo: formValue.titolo?.trim(),
      formato: formValue.formato?.codice,
      sovrascrivi: formValue.sovrascrivi ?? false,
      inputUtente: []
    };

    let i = 0;
    for (const input of (selez?.input ?? [])) {
      if (!input.codiceParametro) {
        continue;
      }
      req.inputUtente?.push({
        codiceParametro: input.codiceParametro,
        variabile: input.variabile,
        valore: formValue.input[i++]
      });
    }

    return req;
  }

  parseParametri(): ReportGenerabiliParameter[] {
    const raw = this.parametri?.get(Parametri.REPORT_GENERABILI) ?? undefined;
    if (!raw) {
      throw new Error('Tab non configurato correttamente - il parametro ' + Parametri.REPORT_GENERABILI + ' non è presente');
    }
    let parsed: ReportGenerabiliParameter[];
    try {
      parsed = JSON.parse(raw);
    } catch (err) {
      throw new Error('Tab non configurato correttamente - il parametro ' + Parametri.REPORT_GENERABILI + ' non è un JSON valido');
    }

    let inc = 0;
    parsed.forEach(report => {
      report.id = inc++;
    });

    this.reportGenerabili = parsed;

    return parsed;
  }

  public submitAnteprima() {

    const payloadOrig = this.buildPayload(this.form?.getRawValue());
    const payload: RichiediGenerazioneReportRequest = this.trasformaPayloadPerGenerazione(payloadOrig);

    if (!this.form?.valid || !payload) {
      throw new Error('Form data is not valid.');
    }

    this.reportService.richiediGenerazioneReportAnteprima(payload).pipe(
      delay(environment.httpMockDelay)
    ).subscribe(res => {
      Utils.previewFromBuffer(res.body!);
    }, failure => {
      this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore);
    });
  }

  protected trasformaPayloadPerGenerazione(payloadOrig: RichiediGenerazioneReportRequestExt): RichiediGenerazioneReportRequest {
    const payload: RichiediGenerazioneReportRequest = {
      ...{
        ...payloadOrig,
        idReport: undefined,
        inputUtente: payloadOrig.inputUtente?.map(i => ({
          ...i,
          valore: Utils.jasperize(i.valore)
        }))
      }
    };
    return payload;
  }

  protected onSubmit(payloadOrig: RichiediGenerazioneReportRequestExt): Observable<any> {
    const payload: RichiediGenerazioneReportRequest = this.trasformaPayloadPerGenerazione(payloadOrig);

    if (!this.form?.valid || !payload) {
      throw new Error('Form data is not valid.');
    }

    // salva le variabili
    const inputVariabili = payloadOrig.inputUtente?.filter(c => !!c.variabile?.length);
    if (inputVariabili?.length) {
      const payloadVariabili: VariabileProcesso[] = inputVariabili.map(v => ({
        name: v.variabile!, value: v.valore
      } as unknown as VariabileProcesso));

      this.patchVariabili(payloadVariabili).subscribe(() => {
        this.cacheVariabili = this.variabiliProcesso;
      }, failure => {
        this.modalService.simpleError('Errore nel salvataggio delle variabili: ' + Utils.toMessage(failure), failure.error.errore);
      });
    }

    return this.reportService.richiediGenerazioneReportAsincrona(payload).pipe(
      tap(response => {

        if (!this.singoloTipo) {
          this.form!.patchValue({
            reportSelezionato: null,
            tipoDocumento: null,
            titolo: '',
            nomeFile: '',
            formato: null,
            sovrascrivi: false,
          });
          this.form?.markAsPristine();
          this.form?.markAsUntouched();
        }

        const task: TaskGenerazioneReport = {
          tipo: this.reportGenerabili?.find(c => c.id === payloadOrig.idReport)!,
          formato: this.formati?.find(c => c.codice === payload.formato),
          request: payloadOrig,
          status: response,
          taskId: response.uuid,
          finished: false,
        };
        this.tasks.unshift(task);

        this.modalService.info('Elaborazione avviata', 'La richiesta di elaborazione del report è stata inserita in coda.');

        // associa un watcher al task asincrono
        const watcher = this.asyncTaskService.watcher(response.uuid);

        // gestisco le subscription manualmente per evitare i memory leaks.
        const watcherSubs: Subscription[] = [];
        const addSub = (x: Subscription) => {
          watcherSubs.push(x);
          this.subscriptions.push(x);
        };

        addSub(watcher.updated.subscribe(newStatus => {
          task.status = newStatus;
          if (newStatus.steps?.length) {
            task.stepDescription = newStatus.steps[newStatus.steps.length - 1].nome ?? undefined;
          }
        }));

        addSub(watcher.completed.subscribe(newStatus => {
          task.completed = true;
          task.stepDescription = 'completato';
        }));

        addSub(watcher.failed.subscribe(newStatus => {
          task.failed = true;
          task.stepDescription = 'non riuscito';
          task.errorMessage = newStatus.errore;
        }));

        // cleanup delle subscription quando il task e' terminato
        addSub(watcher.finalized
          .pipe(
            finalize(() => {
              setTimeout(() => {
                this.sendData({});
              }, 500);
            }))
          .subscribe(() => {
            task.finished = true;
            // delay perche' potrebbe emettere ancora il risultato
            setTimeout(() => {
              watcherSubs.forEach(s => s.unsubscribe());
            }, 500);
          }));

        // avvia il watcher via polling
        watcher.start();
      }));
  }

  preview(task: TaskGenerazioneReport): void {
    const resultUrl = (task?.status?.risultato as any)?.url;
    if (!resultUrl?.length) {
      return;
    }
    Utils.preview(resultUrl);
  }

  disabilita(){
    return !this.allValid || this.tasks.filter(task => !task.finished).length>0;
  }

}
