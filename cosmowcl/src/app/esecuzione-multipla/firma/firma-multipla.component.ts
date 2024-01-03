/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { AfterViewInit, Component, EventEmitter, Output, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin, from, of } from 'rxjs';
import { delay, finalize, map, mergeMap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { EsecuzioneMultiplaService } from '../esecuzione-multipla.service';
import { AttivitaEseguibileMassivamente } from 'src/app/shared/models/api/cosmopratiche/attivitaEseguibileMassivamente';
import { CosmoTableColumnSize, CosmoTableComponent,
  ICosmoTableColumn, ICosmoTablePageRequest, ICosmoTableReloadContext, ICosmoTableStatusSnapshot } from 'ngx-cosmo-table';
import { TranslateService } from '@ngx-translate/core';
import { DocumentoDTO, TipoContenutoDocumentoEnum, TipoContenutoDocumentoFirmatoEnum } from 'src/app/shared/models/documento/documento.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FirmaDigitaleComponent } from 'src/app/form-logici/firma-digitale/firma-digitale.component';
import { ProfiloFEQ } from 'src/app/shared/models/api/cosmoauthorization/profiloFEQ';
import { SecurityService } from 'src/app/shared/services/security.service';
import { DocumentiTask } from 'src/app/shared/models/api/cosmoecm/documentiTask';
import { DocumentiService } from 'src/app/shared/components/consultazione-documenti/services/documenti.service';
import { TipiDocumentoObbligatori } from 'src/app/form-logici/gestione-documenti/models/tipi-documento-obbligatori.models';
import { PraticheService } from 'src/app/shared/services/pratiche.service';
import { Constants } from 'src/app/shared/constants/constants';
import { MessaggioControlliObbligatori } from 'src/app/shared/models/messaggi-controlli-obbligatori/messaggio-controlli-obbligatori';
import { ParametroFormLogico } from 'src/app/shared/models/api/cosmopratiche/parametroFormLogico';
import { ConsultazioneDocumentiComponent } from 'src/app/shared/components/consultazione-documenti/consultazione-documenti.component';
import { EsitoApprovazioneRifiuto } from 'src/app/shared/components/approvazione-rifiuto/approvazione-rifiuto.component';
import { EsecuzioneMultiplaRifiutoFirmaRequest } from 'src/app/shared/models/api/cosmoecm/esecuzioneMultiplaRifiutoFirmaRequest';
import { AsyncTaskModalService } from 'src/app/shared/services/async-task-modal.service';
import { OperazioneAsincronaWrapper } from 'src/app/shared/models/async';
import { ModalService } from 'src/app/shared/services/modal.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { SpinnerVisibilityService } from 'ng-http-loader';
import { NomeFunzionalita } from 'src/app/shared/enums/nome-funzionalita';
import { Documento } from 'src/app/shared/models/api/cosmoecm/documento';
import { Attivita } from 'src/app/shared/models/api/cosmopratiche/attivita';
import { Task } from 'src/app/shared/models/api/cosmopratiche/task';
import { TipoPratica } from 'src/app/shared/models/api/cosmopratiche/tipoPratica';
import { HelperService } from 'src/app/shared/services/helper.service';

export interface PraticheFirmabili {
  idPratica: number;
  firmabile: boolean;
  idAttivita: number;
}

@Component({
  selector: 'app-firma-multipla',
  templateUrl: './firma-multipla.component.html',
  styleUrls: ['./firma-multipla.component.scss']
})
export class FirmaMultiplaComponent implements  AfterViewInit {

  @Output() tabActivated = new EventEmitter<string>();
  loading = 0;
  loadingError: any | null = null;

  tasks: AttivitaEseguibileMassivamente[] = [];
  rowClasses: {[key: number]: string} = {};
  rowErrors: {[key: number]: string} = {};
  lazyLoaded: {[key: string]: true} = {};
  tabAttivo?: string;
  cfUser!: string;
  listaDocDaFirmareObbligatori: TipiDocumentoObbligatori[] = [];
  formKey?: string;
  public parametri: ParametroFormLogico[] | undefined = [];
  public daFirmare!: boolean;
  messaggiObbligatori: MessaggioControlliObbligatori[] = [];
  tipiDocumentiObblFuoriContesto = false;
  listaDocObbligatori: TipiDocumentoObbligatori[] = [];
  documentiTask: DocumentiTask[] = [];
  firmabile: PraticheFirmabili[] = [];
  caricamentoInCorso = true;
  note = '';
  approvazione?: boolean;
  erroreFea = false;
  codiceTipoPratica?: string;

  mandareAvantiProcesso = false;
  checkMandareAvanti = true;

  columns: ICosmoTableColumn[] = [{
      name: 'oggetto_pratica', label: 'Pratica', field: 'pratica.oggetto', canSort: true, canHide: false,
    }, {
      name: 'nome_attivita', label: 'Attivita\'', field: 'attivita.nome', canSort: true, canHide: false,
    }, {
      name: 'funzionalita', label: 'Funzionalita\'', valueExtractor: row => 'FIRMA DOCUMENTI', canSort: true,
    }, {
      name: 'azioni', label: '',
      canHide: false,
      canSort: false,
      applyTemplate: true,
      size: CosmoTableColumnSize.XXS
    },
  ];

  @ViewChild('table') table: CosmoTableComponent | null = null;
  @ViewChildren('docs') docs: QueryList<ConsultazioneDocumentiComponent> | null = null;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private spinner: SpinnerVisibilityService,
    private esecuzioneMultiplaService: EsecuzioneMultiplaService,
    private translateService: TranslateService,
    private modal: NgbModal,
    private securityService: SecurityService,
    private documentiService: DocumentiService,
    private praticheService: PraticheService,
    private asyncTaskModalService: AsyncTaskModalService,
    private modalService: ModalService,
    public helperService: HelperService) {
    this.activateTab(null, this.getStartingTab());

    this.activatedRoute.queryParams.subscribe(params => {
      this.mandareAvantiProcesso = JSON.parse(params.mandareAvantiProcesso);
    });

    this.loading++;

    this.setFormKey(this.esecuzioneMultiplaService.getSelectedTasks());

    if (this.formKey){
      this.praticheService.getForms(this.formKey).pipe(finalize(() => this.loading--)).subscribe(formLogico => {
        this.parametri = formLogico.funzionalita?.filter(f => f.codice === 'FIRMA-DOCUMENTI')[0].parametri;
        const daFirmare = this.parametri?.filter(p => p.chiave === 'APPOSIZIONE_FIRMA');
        if (!!daFirmare && daFirmare?.length > 0 && daFirmare[0].valore) {
          this.daFirmare = JSON.parse(daFirmare[0].valore);
        }

        const listaDoc = this.parametri?.filter(p => p.chiave === Constants.FORM_LOGICI.TIPI_DOC_DA_FIRMARE_OBBLIGATORI_KEY);
        if (!!listaDoc && listaDoc?.length > 0 && listaDoc[0].valore) {
          const c = JSON.parse(listaDoc[0].valore as string);
          c?.tipi_documento.forEach((tipoDocumentoDaFirmareObbligatorio: TipiDocumentoObbligatori) => {
            this.listaDocDaFirmareObbligatori.push(tipoDocumentoDaFirmareObbligatorio);
          });
        }
        this.decodificaCodiceTipiDocumento(this.listaDocDaFirmareObbligatori);
      });
    }
  }

  ngAfterViewInit(): void {
    this.refresh();
  }

  selectableStatusProvider = (item: AttivitaEseguibileMassivamente) => {
    if (this.firmabile.length !== this.tasks.length) {
      return false;
    }
    const praticaFirmabile = this.firmabile.filter(f => f.idPratica === item.pratica?.id);
    return praticaFirmabile.length > 0 ? praticaFirmabile[0].firmabile : false;

  }

  private setTipoPratica(selected: AttivitaEseguibileMassivamente[]){

    let notError = true;

    selected.forEach(task => {
      if (this.codiceTipoPratica && task.pratica?.tipo?.codice !== this.codiceTipoPratica) {
        notError = false;
        if (task.pratica?.id) {
          this.rowErrors[task.pratica.id] = 'Form diverso dai task delle altre pratiche';
        }
      } else {
        this.codiceTipoPratica = task.pratica?.tipo?.codice;
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

  private setFormKey(selected: AttivitaEseguibileMassivamente[]){

    let notError = true;

    selected.forEach(task => {
      if (this.formKey &&  task.attivita?.formKey !== this.formKey) {
        notError = false;
        if (task.attivita?.id) {
          this.rowErrors[task.attivita.id] = 'Form diverso dai task delle altre pratiche';
        }
      } else {
        this.formKey = task.attivita?.formKey ?? '';
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

  rowClassProvider = (row: AttivitaEseguibileMassivamente, status: ICosmoTableStatusSnapshot | null) => {
    this.isLoadingDocuments();
    const v = this.rowClasses[row.attivita?.id ?? 0];
    if (v) {
      return 'table-' + v;
    }
    return '';
  }

  get abilitaForm(): boolean {
    return this.table?.anyChecked ?? false;
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
      this.securityService.principal$.subscribe(newPrincipal => {
        this.cfUser = newPrincipal.codiceFiscale;
      });
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

  activateTab(event: Event | null, tabRef: string, inner = false) {
    if (!tabRef) {
      return;
    }
    this.lazyLoaded[tabRef] = true;
    const prev = this.tabAttivo;
    this.tabAttivo = tabRef;

    if (inner && prev !== tabRef) {
      this.tabActivated.next(tabRef);
    }

    if (event) {
      event.stopPropagation();
    }
  }

  activateTabInner(event: Event | null, tabRef: string) {
    this.activateTab(event, tabRef, true);
  }

  private getStartingTab(): string {
    return 'docs';
  }

  get documentiSelezionati(): DocumentiTask[] {
    if (!this.table) {
      return [];
    }
    const sel: DocumentiTask[] = [];
    const tableSelected = this.table.getStatusSnapshot()?.checkedItems ?? [];
    this.esecuzioneMultiplaService.setSelectedTasks(tableSelected);
    tableSelected.forEach(p => {
      const documents = this.docs?.filter(d => d.idPratica === p.pratica.id)[0];
      if (!!documents) {
        sel.push({documenti: documents.listaDocsSoloFirmabile, attivita: p.attivita});
      }
    });
    return sel;
  }


  selectionChangeHandler(row: any): void {
    this.checkMandareAvanti = true;
    this.spinner.show();
    this.esecuzioneMultiplaService.getValidation(NomeFunzionalita.FIRMA_DOCUMENTI, row)
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



  creazioneModaleFirmaDocumenti(documentiTask: DocumentiTask[], result: EsitoApprovazioneRifiuto){
    const data = this.helperService.searchHelperRef(this.activatedRoute);
    const modalRef = this.modal.open(FirmaDigitaleComponent, { size: 'xl', backdrop: 'static' });
    const documentiDaFirmare: DocumentoDTO[] = [];
    documentiTask.forEach(dt => {
      dt.documenti?.forEach(d => {
        documentiDaFirmare.push(this.documentiService.mapDocumento(d));

      });
    });
    modalRef.componentInstance.documenti = documentiDaFirmare;
    modalRef.componentInstance.profiliFeqAbilitati = this.profiliFeqAbilitati(documentiDaFirmare);
    modalRef.componentInstance.documentiTask = documentiTask;
    modalRef.componentInstance.isMassivo = true;
    modalRef.componentInstance.note = result.note;
    modalRef.componentInstance.mandareAvantiProcesso = this.mandareAvantiProcesso;
    modalRef.componentInstance.codiceTipoPratica = this.codiceTipoPratica;
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = 'firma-digitale-firma-multipla';

    modalRef.result.then(() => {
    }, (reason) => {
      if ('finish_to_sign_docs' === reason) {
        this.table?.uncheckAll();
        this.refresh();
      }
    });
  }

  private isLoadingDocuments() {
    let loading = false;
    this.docs?.changes.subscribe(() => {
      this.docs?.toArray().forEach(x => {
        if (x.loading > 0) {
          loading = true;
        }
      });
    });
    if (!loading && this.firmabile.length === this.tasks.length) {
      this.controlError();
    }

  }

  private controlError() {
    this.firmabile.forEach(f => {
      if (!f.firmabile) {
        this.rowErrors[f.idAttivita] = 'Non firmabile';
      }
    });
    this.caricamentoInCorso = false;
  }

  isFirmabile(event: any) {
    this.firmabile.push(event);
  }

  profiliFeqAbilitati(docsDaFirmare: DocumentoDTO[]): string[] {
    const output: string[] = [];

    const profiliFeq: ProfiloFEQ[] = [];

    docsDaFirmare.forEach(docDaFirmare => {

      if (!docDaFirmare.contenutoFirmato && docDaFirmare.contenutoOriginale) {
        docDaFirmare.contenutoOriginale.formatoFile?.formatoFileProfiloFeqTipoFirma?.forEach(profiloFeqTipoFirma => {
          if (!docDaFirmare.contenutoOriginale?.tipoFirma ||
            profiloFeqTipoFirma.tipoFirma.codice === docDaFirmare.contenutoOriginale?.tipoFirma?.codice) {
            profiliFeq.push(profiloFeqTipoFirma.profiloFeq);
          }
        });
      } else if (docDaFirmare.contenutoFirmato?.infoVerificaFirme?.find(i => i.codiceFiscaleFirmatario !== this.cfUser)) {
        docDaFirmare.contenutoFirmato.formatoFile?.formatoFileProfiloFeqTipoFirma?.forEach(profiloFeqTipoFirma => {
          if (profiloFeqTipoFirma.tipoFirma.codice === docDaFirmare.contenutoFirmato?.tipoFirma?.codice) {
            profiliFeq.push(profiloFeqTipoFirma.profiloFeq);
          }
        });
      }
    });

    profiliFeq.reduce((prev, curr) => prev.set(curr.codice, (prev.get(curr.codice) || 0) + 1), new Map()).forEach((value, key) => {
      if (value === docsDaFirmare.length) {
        output.push(key);
      }
    });

    return output;

  }

  private decodificaCodiceTipiDocumento(listaObbligatoria: TipiDocumentoObbligatori[]) {
    this.documentiService.getTipiDocumento(listaObbligatoria.map(lista => lista.codice)).subscribe(tipiDocumento => {
      if (tipiDocumento) {
        tipiDocumento.forEach(tipoDocumento => {
          const tipoDocObbl = listaObbligatoria.find(o => o.codice === tipoDocumento.codice);
          if (tipoDocObbl) {
            tipoDocObbl.descrizione = tipoDocumento.descrizione;
          }
        });
      }
    });
  }

  sendMessages(docObbligatori: MessaggioControlliObbligatori[]): void {
    if (this.messaggiObbligatori.length === 0) {
      this.messaggiObbligatori = docObbligatori;
    } else {
      const nuoviMessaggi: MessaggioControlliObbligatori[] = [];

      docObbligatori.forEach( docObbligatorio => {
          const nuovi = this.messaggiObbligatori
          .filter(messaggioObbligatorio => docObbligatorio.messaggio.split(':')[0] === messaggioObbligatorio.messaggio.split(':')[0] );
          if (nuovi.length === 0){
            nuoviMessaggi.push(docObbligatorio);
          }
      });

      this.messaggiObbligatori.forEach( messaggioObbligatorio => {
        const nuovi = docObbligatori
          .filter(docObbligatorio => messaggioObbligatorio.messaggio.split(':')[0] === docObbligatorio.messaggio.split(':')[0]);
        nuovi.forEach(nuovo => nuoviMessaggi.push(nuovo));
      });

      const messaggio = this.translateService.instant('errori.cfg_tipi_documenti_obbligatori_errata');

      const messaggiConfig = this.messaggiObbligatori
        .filter(messaggioObbligatorio => messaggio.split(':')[0] === messaggioObbligatorio.messaggio.split(':')[0]);
      messaggiConfig.forEach(messaggioConfig => nuoviMessaggi.push(messaggioConfig));

      this.messaggiObbligatori = nuoviMessaggi;
    }
  }

  conferma(result: EsitoApprovazioneRifiuto) {
    if (result.approvazione) {
      this.creazioneModaleFirmaDocumenti(this.documentiSelezionati, result);
    } else {
      const selected = this.table?.getStatusSnapshot()?.checkedItems;
      if (!selected?.length) {
        return;
      }
      const payload: EsecuzioneMultiplaRifiutoFirmaRequest = {
        note: result.note,
        tasks: selected,
        mandareAvantiProcesso: this.mandareAvantiProcesso
      };
      this.esecuzioneMultiplaService.postEsecuzioneMultiplaRifiutoFirma(payload)
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

}

