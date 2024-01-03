/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { DatePipe } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { CosmoTableColumnSize, CosmoTableComponent, CosmoTableFormatter, ICosmoTableColumn} from 'ngx-cosmo-table';
import { Observable, of } from 'rxjs';
import { finalize, map, mergeMap, tap } from 'rxjs/operators';
import { DeadLetterJob } from 'src/app/shared/models/api/cosmobusiness/deadLetterJob';
import { TaskAutomaticiInErroreService } from 'src/app/shared/services/task-automatici-in-errore.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { SpinnerVisibilityService } from 'ng-http-loader';
import { DateUtils } from 'src/app/shared/utilities/date-utils';

@Component({
  selector: 'app-task-automatici-in-errore',
  templateUrl: './task-automatici-in-errore.component.html',
  styleUrls: ['./task-automatici-in-errore.component.scss']
})
export class TaskAutomaticiInErroreComponent implements OnInit {

  @ViewChild('table') table: CosmoTableComponent | null = null;

  jobs: DeadLetterJob[] = [];

  loading = 0;
  loadingError: any | null = null;

  columns: ICosmoTableColumn[] = [{
    name: 'descrizioneEnte', label: 'Ente', field: 'descrizioneEnte', canSort: true,
    valueExtractor: row => row.descrizioneEnte ?? ''
  }, {
    label: 'Tipologia Pratica', field: 'tipoPratica', canSort: true, canFilter: true, defaultFilter: true,
    valueExtractor: row => row.tipoPratica ?? ''
  }, {
    label: 'Oggetto Pratica', field: 'oggettoPratica', canSort: true, canFilter: true, defaultFilter: true,
    valueExtractor: row => row.oggettoPratica ?? ''
  }, {
    label: 'Data', field: 'data', canSort: true,
    formatters: [{
      formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy HH:mm:ss',
     }, {
      format: (raw: any) => raw ?? '--',
    }],
    valueExtractor: row => DateUtils.parse(row.data)
  }, {
    label: 'Retry', field: 'tentativi', canSort: false,
    valueExtractor: row => row.tentativi ?? ''
  }, {
    name: 'info',
    canHide: false,
    canSort: false,
    applyTemplate: true,
    size: CosmoTableColumnSize.XXS
  }
  , {
    name: 'azioni',
    canHide: false,
    canSort: false,
    applyTemplate: true,
    size: CosmoTableColumnSize.XXS
  }
  ];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private taskAutomaticiInErroreService: TaskAutomaticiInErroreService,
    private datePipe: DatePipe,
    private modalService: ModalService,
    private translateService: TranslateService,

  ) { }

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {

    this.loading++;
    this.loadingError = null;

    this.taskAutomaticiInErroreService.getQueue()
    .pipe(
      finalize(() => {
        this.loading--;
      })
    )
    .subscribe(response => {
      this.jobs = response.deadLetterJobs ?? [];
    }, failure => {
      this.loadingError = failure;
    });
  }

  move(row: DeadLetterJob){

    this.taskAutomaticiInErroreService.postJobs(row.id, {action: 'move'}).subscribe(
      () => {
        this.modalService.simpleInfo(this.translateService.instant('task_automatici_in_errore.inviato')).finally(() => this.refresh());
      },
      error => {
        this.modalService.simpleError(this.translateService.instant('errori.richiesta_esecuzione_task')).finally(() => this.refresh());
      }
    );
  }



}
