/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import {  Router } from '@angular/router';
import { forkJoin, from, of } from 'rxjs';
import { delay, finalize, map, mergeMap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { EsecuzioneMultiplaService } from '../esecuzione-multipla.service';
import { AttivitaEseguibileMassivamente } from 'src/app/shared/models/api/cosmopratiche/attivitaEseguibileMassivamente';
import { CosmoTableColumnSize, CosmoTableComponent,
  ICosmoTableColumn, ICosmoTablePageRequest, ICosmoTableReloadContext, ICosmoTableStatusSnapshot } from 'ngx-cosmo-table';
import { NGXLogger } from 'ngx-logger';
import { EsitoApprovazioneRifiuto } from 'src/app/shared/components/approvazione-rifiuto/approvazione-rifiuto.component';
import { EsecuzioneMultiplaApprovaRequest } from 'src/app/shared/models/api/cosmobusiness/esecuzioneMultiplaApprovaRequest';
import { AsyncTaskModalService } from 'src/app/shared/services/async-task-modal.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { ModalService } from 'src/app/shared/services/modal.service';
import { TranslateService } from '@ngx-translate/core';
import { OperazioneAsincronaWrapper } from 'src/app/shared/models/async';
import { SpinnerVisibilityService } from 'ng-http-loader';
import { NomeFunzionalita } from 'src/app/shared/enums/nome-funzionalita';

@Component({
  selector: 'app-approvazione-multipla',
  templateUrl: './approvazione-multipla.component.html',
  styleUrls: ['./approvazione-multipla.component.scss']
})
export class ApprovazioneMultiplaComponent implements OnInit {

  loading = 0;
  loadingError: any | null = null;

  tasks: AttivitaEseguibileMassivamente[] = [];
  rowClasses: {[key: number]: string} = {};
  rowErrors: {[key: number]: string} = {};

  note = '';
  approvazione?: boolean;

  mandareAvantiProcesso?: boolean;
  checkMandareAvanti = true;

  columns: ICosmoTableColumn[] = [{
      name: 'oggetto_pratica', label: 'Pratica', field: 'pratica.oggetto', canSort: true, canHide: false,
    }, {
      name: 'nome_attivita', label: 'Attivita\'', field: 'attivita.nome', canSort: true, canHide: false,
    }, {
      name: 'funzionalita', label: 'Funzionalita\'', field: 'funzionalita.codice', canSort: true,
    }, {
      name: 'azioni', label: '',
      canHide: false,
      canSort: false,
      applyTemplate: true,
      size: CosmoTableColumnSize.XXS
    },
  ];

  @ViewChild('table') table: CosmoTableComponent | null = null;

  constructor(
    private logger: NGXLogger,
    private router: Router,
    private spinner: SpinnerVisibilityService,
    private esecuzioneMultiplaService: EsecuzioneMultiplaService,
    private asyncTaskModalService: AsyncTaskModalService,
    private modalService: ModalService,
    private translateService: TranslateService,
  ) {}

  rowClassProvider = (row: AttivitaEseguibileMassivamente, status: ICosmoTableStatusSnapshot | null) => {
    const v = this.rowClasses[row.attivita?.id ?? 0];
    if (v) {
      return 'table-' + v;
    }
    return '';
  }

  get abilitaForm(): boolean {
    return this.table?.anyChecked ?? false;
  }

  ngOnInit() {
    this.refresh();
  }

  refresh() {
    this.loading ++;
    this.loadingError = null;

    forkJoin({
      tasks: of(this.esecuzioneMultiplaService.getSelectedTasks()),
    }).pipe(
      delay(environment.httpMockDelay),
      finalize(() => {
        this.loading --;
      }),
    ).subscribe(data => {
      this.tasks = data.tasks;

      if (!this.tasks.length) {
        this.router.navigate(['esecuzione-multipla']);
      }
    }, failure => {
      this.loadingError = failure;
    });
  }

  dataProvider = (input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext) => {
    return of(this.tasks).pipe(
      map(result => ({
        content: result ?? [],
        number: 0,
        numberOfElements: result?.length ?? 0,
        size: result?.length ?? 0,
        totalElements: result?.length ?? 0,
        totalPages: 1,
      }))
    );
  }

  selectionChangeHandler(row: any): void {
    this.checkMandareAvanti = true;
    this.spinner.show();
    this.esecuzioneMultiplaService.getValidation(NomeFunzionalita.APPROVAZIONE, row)
    .pipe(finalize(() => {
      this.spinner.hide();
      this.checkMandareAvanti = false;
    }))
    .subscribe(response => {
      const nonValido = response.find(singleElement => !singleElement.validazione);
      if (nonValido) {
        this.mandareAvantiProcesso = false;
      } else {
        this.mandareAvantiProcesso = true;
      }
    });
  }

  conferma(result: EsitoApprovazioneRifiuto) {
    const selected = this.table?.getStatusSnapshot()?.checkedItems;
    if (!selected?.length) {
      return;
    }

    const payload: EsecuzioneMultiplaApprovaRequest = {
      approvazione: result.approvazione,
      note: result.note,
      tasks: selected,
      mandareAvantiProcesso: this.mandareAvantiProcesso ?? true
    };

    this.esecuzioneMultiplaService.postEsecuzioneMultiplaApprova(payload)
    .pipe(
      mergeMap(operazione =>
         from(this.asyncTaskModalService.open({
           taskUUID: operazione.uuid,
           maxTaskDepth: 0,
        }).result)
      )
    )
    .subscribe(
      (task: OperazioneAsincronaWrapper<string>) => {
       this.elaboraRisultato(JSON.parse(task.risultato ?? ''));
      },
      error => {
        this.modalService.error(
          this.translateService.instant('errori.completamento_task'),
          Utils.toMessage(error) ?? this.translateService.instant('errori.completamento_task'),
          error.error.errore
        ).then(() => {}).catch(() => {});
      }
    );
  }

  elaboraRisultato(risultato: any[]): void {
    let numSuccesso = 0;
    let numErrori = 0;

    this.table?.uncheckAll();

    for (const entry of risultato) {
      if (entry.successo) {
        numSuccesso ++;
        this.rowClasses[entry.task.attivita.id] = 'success';
        this.esecuzioneMultiplaService.removeSelectedTask(entry.task);
      } else {
        numErrori ++;
        this.rowClasses[entry.task.attivita.id] = 'danger';
        this.rowErrors[entry.task.attivita.id] = entry.errore.message;
      }
    }

    this.tasks = this.esecuzioneMultiplaService.getSelectedTasks();
    if (this.tasks.length <= 0) {
      this.modalService.simpleInfo('Tutte le attività sono state elaborate con successo.').finally(() => {
        this.router.navigate(['home']);
      });

      return;
    } else if (!numErrori) {
      this.modalService.simpleInfo(numSuccesso + ' attività sono state elaborate con successo.').finally(() => {
        this.table?.refresh();
      });

    } else {
      this.modalService.simpleError('Si sono verificati degli errori nell\'elaborazione di ' + numErrori + ' attività.').finally(() => {
        this.table?.refresh();
      });
    }

    this.table?.refresh();
  }

  showError(row: AttivitaEseguibileMassivamente): void {
    const err = this.rowErrors[row.attivita?.id ?? 0];
    if (!err) {
      return;
    }

    this.modalService.error('Dettagli errore', err);
  }
}
