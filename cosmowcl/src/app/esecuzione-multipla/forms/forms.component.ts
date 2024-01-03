/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { SpinnerVisibilityService } from 'ng-http-loader';
// tslint:disable-next-line: max-line-length
import { CosmoTableColumnSize, CosmoTableComponent, ICosmoTableColumn, ICosmoTablePageRequest, ICosmoTableReloadContext, ICosmoTableStatusSnapshot } from 'ngx-cosmo-table';
import { forkJoin, of } from 'rxjs';
import { delay, finalize, map } from 'rxjs/operators';
import { NomeFunzionalita } from 'src/app/shared/enums/nome-funzionalita';
import { AttivitaEseguibileMassivamente } from 'src/app/shared/models/api/cosmopratiche/attivitaEseguibileMassivamente';
import { ModalService } from 'src/app/shared/services/modal.service';
import { PraticheService } from 'src/app/shared/services/pratiche.service';
import { environment } from 'src/environments/environment';
import { EsecuzioneMultiplaService } from '../esecuzione-multipla.service';
import { CompilaFormComponent } from './compila-form/compila-form.component';
import { HelperService } from 'src/app/shared/services/helper.service';

@Component({
  selector: 'app-forms',
  templateUrl: './forms.component.html',
  styleUrls: ['./forms.component.scss']
})
export class FormsComponent implements OnInit {

  loading = 0;
  loadingError: any | null = null;

  tasks: AttivitaEseguibileMassivamente[] = [];
  rowClasses: { [key: number]: string } = {};
  rowErrors: { [key: number]: string } = {};

  formType?: string;
  formKey?: string;
  idAttivita?: number;
  mandareAvantiProcesso?: boolean;
  checkMandareAvanti = true;

  @ViewChild('table') table: CosmoTableComponent | null = null;

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

  rowClassProvider = (row: AttivitaEseguibileMassivamente, status: ICosmoTableStatusSnapshot | null) => {
    const v = this.rowClasses[row.attivita?.id ?? 0];
    if (v) {
      return 'table-' + v;
    }
    return '';
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

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private modal: NgbModal,
    private spinner: SpinnerVisibilityService,
    private modalService: ModalService,
    private translateService: TranslateService,
    private esecuzioneMultiplaService: EsecuzioneMultiplaService,
    private praticheService: PraticheService,
    private helperService: HelperService) {

      this.route.params.subscribe(params => {
        this.formType = params.type;
      });
    }

  ngOnInit(): void {
    this.refresh();
  }

  refresh() {
    this.loading++;
    this.loadingError = null;

    forkJoin({
      tasks: of(this.esecuzioneMultiplaService.getSelectedTasks()),
    }).pipe(
      delay(environment.httpMockDelay),
      finalize(() => {
        this.loading--;
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

  get abilitaForm(): boolean {
    return this.table?.anyChecked ?? false;
  }

  selectionChangeHandler(row: any): void {
    this.checkMandareAvanti = true;
    this.spinner.show();

    const task = this.formType === 'custom' ? NomeFunzionalita.CUSTOM_FORM : NomeFunzionalita.SIMPLE_FORM;

    this.esecuzioneMultiplaService.getValidation(task, row)
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

  showError(row: AttivitaEseguibileMassivamente): void {
    const err = this.rowErrors[row.attivita?.id ?? 0];
    if (!err) {
      return;
    }

    this.modalService.error('Dettagli errore', err);
  }


  conferma() {
    const selected = this.table?.getStatusSnapshot()?.checkedItems;
    if (!selected?.length) {
      return;
    }

    this.setFormKey(selected);

    if (this.formKey) {
      this.praticheService.getForms(this.formKey).subscribe(formLogico => {
        let codiceForm: string | undefined;

        if (this.formType === 'custom') {
          const parametri = formLogico.funzionalita?.find(f => f.codice === 'CUSTOM-FORM')?.parametri;
          codiceForm = parametri?.find(p => p.chiave === 'CODICE_CUSTOM_FORM')?.valore;
        } else if (this.formType === 'simple'){
          const parametri = formLogico.funzionalita?.find(f => f.codice === 'SIMPLE-FORM')?.parametri;
          codiceForm = parametri?.find(p => p.chiave === 'FORM-KEY')?.valore;
        }

        if (codiceForm) {
          this.creazioneModale(selected, codiceForm);
        } else {
          this.modalService.error(
            this.translateService.instant('errori.esecuzione_multipla'),
            'Nessun form da compilare associato'
          ).then(() => {
            this.router.navigate(['esecuzione-multipla']);
          }).catch(() => { });
        }
      }, error => {

        this.modalService.error(
          this.translateService.instant('errori.esecuzione_multipla'),
          'Nessun form da compilare associato',
          error.error.errore
        ).then(() => {
          this.router.navigate(['esecuzione-multipla']);
        }).catch(() => { });
      });
    }

  }

  private setFormKey(selected: AttivitaEseguibileMassivamente[]){

    let notError = true;

    selected.forEach(task => {
      if (this.formKey &&  task.attivita?.formKey !== this.formKey) {
        notError = false;
        if (task.attivita?.id) {
          this.rowErrors[task.attivita.id] = 'Form diverso dai task delle altre pratiche';
        }
      } else {
        this.formKey = task.attivita?.formKey;
      }
    });

    if (!notError){

      this.modalService.error(
        this.translateService.instant('errori.esecuzione_multipla'),
        'Form incongruenti'
      ).then(() => {
      }).catch(() => { });
      return;
    }
  }

  creazioneModale(tasks: AttivitaEseguibileMassivamente[], codiceForm: string) {
    const data = this.helperService.searchHelperRef(this.route);
    const modalRef = this.modal.open(CompilaFormComponent, { size: 'xl', backdrop: 'static' });
    modalRef.componentInstance.formType = this.formType;
    modalRef.componentInstance.tasks = tasks;
    modalRef.componentInstance.codiceForm = codiceForm;
    modalRef.componentInstance.mandareAvantiProcesso = this.mandareAvantiProcesso;
    modalRef.componentInstance.formKey = this.formKey;
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = 'compila-form';

    modalRef.result.then(
      () => { },
      (reason) => {
        if ('finish' === reason) {
          this.table?.uncheckAll();
          this.refresh();
        }
      });
  }


}
