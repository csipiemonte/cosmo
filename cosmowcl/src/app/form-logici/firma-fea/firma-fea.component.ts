/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { DatePipe } from '@angular/common';
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Idle } from '@ng-idle/core';
import { TranslateService } from '@ngx-translate/core';
import { CosmoTableComponent, ICosmoTableColumn, CosmoTableColumnSize, CosmoTableFormatter } from 'ngx-cosmo-table';
import { Subscription, from } from 'rxjs';
import { debounceTime, mergeMap } from 'rxjs/operators';
import { ModaleParentComponent } from 'src/app/modali/modale-parent-component';
import { PreviewDocumentoModalComponent
} from 'src/app/shared/components/modals/preview-documento-modal/preview-documento-modal.component';
import { Constants } from 'src/app/shared/constants/constants';
import { Pratica } from 'src/app/shared/models/api/cosmobusiness/pratica';
import { DocumentiTask } from 'src/app/shared/models/api/cosmoecm/documentiTask';
import { FirmaFeaRequest } from 'src/app/shared/models/api/cosmoecm/firmaFeaRequest';
import { TemplateDocumento } from 'src/app/shared/models/api/cosmoecm/templateDocumento';
import { AttivitaEseguibileMassivamente } from 'src/app/shared/models/api/cosmopratiche/attivitaEseguibileMassivamente';
import { OperazioneAsincronaWrapper } from 'src/app/shared/models/async';
import { DocumentoDTO, TipoContenutoDocumentoEnum,
  TipoContenutoDocumentoFirmatoEnum } from 'src/app/shared/models/documento/documento.model';
import { ValorePreferenzeEnte } from 'src/app/shared/models/preferenze/valore-preferenze-ente.model';
import { AsyncTaskModalService } from 'src/app/shared/services/async-task-modal.service';
import { FeaService } from 'src/app/shared/services/fea.service';
import { HelperService } from 'src/app/shared/services/helper.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { NotificationsWebsocketService } from 'src/app/shared/services/notifications-websocket.service';
import { PreferenzeEnteService } from 'src/app/shared/services/preferenze-ente.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { TemplateFeaService } from 'src/app/shared/services/template-fea.service';
import { DateUtils } from 'src/app/shared/utilities/date-utils';
import { Utils } from 'src/app/shared/utilities/utilities';

interface TemplateDocumentoDTO  { documento: DocumentoDTO; coordinataX?: number; coordinataY?: number; pagina?: number; action?: boolean; }

@Component({
  selector: 'app-firma-fea',
  templateUrl: './firma-fea.component.html',
  styleUrls: ['./firma-fea.component.scss']
})
export class FirmaFeaComponent extends ModaleParentComponent implements OnInit, OnDestroy {


  @ViewChild('table') table: CosmoTableComponent | null = null;

  private notificationSubcritpion: Subscription | null = null;
  private preferenzeEnteSubcritpion: Subscription | null = null;
  private iconaFea: string | null = null;
  idEnte !: number;
  staFirmando = false;
  documenti: TemplateDocumentoDTO[] = [];
  profiliFeqAbilitati: string[] = [];
  documentiTask: DocumentiTask[] = [];
  tasks: AttivitaEseguibileMassivamente[] = [];
  isMassivo = false;
  rowClasses: {[key: number]: string} = {};
  rowErrors: {[key: number]: string} = {};
  note: string | undefined = '';
  pratica: Pratica | undefined = undefined;
  mandareAvantiProcesso = false;
  idAttivita = 0;
  firmaFea = false;
  public loading = 0;
  public loadingError: any | null = null;
  codicePagina!: string;
  codiceTab!: string;
  codiceModale!: string;

  messageMapping: { [k: string]: string } =
    { '=0': 'Nessun documento selezionato', '=1': 'Hai selezionato un documento', other: 'Hai selezionato # documenti' };

  columns: ICosmoTableColumn[] = [
    {
      name: 'tipologia', label: 'tipologia',
      valueExtractor: row => row.documento.tipo?.descrizione ? row.documento.tipo.descrizione : '--', canSort: true
    },
    {
      name: 'titolo_nome_file', label: 'titolo/nome file',
      valueExtractor: row => row.documento.titolo ? row.documento.titolo :
      row.documento.contenutoEffettivo?.nomeFile ? row.documento.contenutoEffettivo.nomeFile : '--',
      canSort: true
    },
    {
      name: 'ultima_modifica', label: 'ultima modifica',
      formatters: [{
        formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy',
       }, {
        format: (raw: any) => raw ?? '--',
      }],
      valueExtractor: row => DateUtils.parse(row.documento.ultimaModifica),
       canSort: true
    },
    { name: 'formato', label: 'formato', applyTemplate: true },
    { name: 'riquadroFirma', label: 'riquadro firma', canHide: false, canSort: false, applyTemplate: true,
     size: CosmoTableColumnSize.XXS },
    { name: 'azioni', label: 'azione', canHide: false, canSort: false, applyTemplate: true, size: CosmoTableColumnSize.XXS }
  ];

  otp!: string;

  numeroDocumento = 0;
  totaleDocumenti = 0;
  percentualeFirma = 0;

  constructor(
    public modal: NgbActiveModal,
    private modalRef: NgbModal,
    private datePipe: DatePipe,
    private firmaFeaService: FeaService,
    private notificationsWebsocketService: NotificationsWebsocketService,
    private templateFeaService: TemplateFeaService,
    private preferenzeEnteService: PreferenzeEnteService,
    private translateService: TranslateService,
    private securityService: SecurityService,
    private modalService: ModalService,
    private asyncTaskModalService: AsyncTaskModalService,
    private idle: Idle,
    public helperService: HelperService,
    public route: ActivatedRoute) {
    super(helperService);
  }



  ngOnInit(): void {

    this.totaleDocumenti = this.documenti.length;

    this.preferenzeEnteSubcritpion = this.preferenzeEnteService.subscribePreferenze.subscribe(
      pref => {
        if (pref?.valore) {
          const valore = JSON.parse(pref.valore) as ValorePreferenzeEnte;
          this.iconaFea = valore.iconaFea ?? null;
        }

        if (this.iconaFea === null || this.iconaFea === ''){
          this.modalService.simpleError(this.translateService.instant('firma_fea.errore_icona_fea')).finally(() => this.modal.dismiss());
        }

      }
    );

    this.securityService.principal$.subscribe(
      elem => {
        this.idEnte = elem.ente?.id ?? 0;
        this.refresh();
      }
    );


    this.notificationSubcritpion = this.notificationsWebsocketService
      .whenEvent(Constants.APPLICATION_EVENTS.DOCUMENTI_FIRMATI)
      .pipe(
        debounceTime(1000)
      )
      .subscribe(e => {
        this.numeroDocumento = e.payload.numeroDocumento;
        if (this.numeroDocumento < this.totaleDocumenti) {
          this.percentualeFirma = this.numeroDocumento * 100 / this.totaleDocumenti;
        } else {
          this.percentualeFirma = 100;
        }
      });
    this.setModalName(this.codiceModale);
    this.searchHelperModale(this.codicePagina, this.codiceTab);
  }

  ngOnDestroy(): void {
    if (this.notificationSubcritpion) {
      this.notificationSubcritpion.unsubscribe();
    }
    if (!this.idle.isRunning() && !this.isMassivo) {
      this.idle.watch();
    }
    if (this.preferenzeEnteSubcritpion) {
      this.preferenzeEnteSubcritpion.unsubscribe();
    }

  }

  refresh(){
    const firstPayload = {
      filter: {
        idPratica: {eq: this.pratica?.id},
        idEnte: {eq: this.idEnte},
        codiceTipoPratica: {eq: this.pratica?.tipo?.codice},
        codiciTipoDocumento: [...this.documenti.map(valore => valore.documento.tipo?.codice)]
      }
    };


    this.loading++;


    this.templateFeaService.search(JSON.stringify(firstPayload), true).subscribe(
      template => {

        for (const doc of this.documenti){
          const templ = template.templateFea?.find(el => el.tipoDocumento?.codice === doc.documento.tipo?.codice
            && !doc.documento.contenuti?.find(contenuto => contenuto.tipo?.codice === TipoContenutoDocumentoEnum.FIRMATO
              && contenuto.tipoContenutoFirmato?.codice === TipoContenutoDocumentoFirmatoEnum.FEA));
          if (templ){

            doc.action = false;
            doc.coordinataX = templ.coordinataX;
            doc.coordinataY = templ.coordinataY;
            doc.pagina = templ.pagina;

          }
        }


        if (this.documenti.find(elemento => !elemento.coordinataX || !elemento.coordinataY)){

        const payload = {
          filter: {
            idEnte: {eq: this.idEnte},
            codiceTipoPratica: {eq: this.pratica?.tipo?.codice},
            codiciTipoDocumento: [...this.documenti.filter(x => !x.coordinataX || !x.coordinataY).map(temp => temp.documento.tipo?.codice)]
          }
        };
        this.loading++;
        this.templateFeaService.search(JSON.stringify(payload), true).subscribe(
          template2 => {
            this.documenti.filter(x => !x.coordinataX || !x.coordinataY).forEach(elem => {
                const templ2 = template2.templateFea?.find(el => el.tipoDocumento?.codice === elem.documento.tipo?.codice &&
                    !elem.documento.contenuti?.find(contenuto => contenuto.tipo?.codice === TipoContenutoDocumentoEnum.FIRMATO
                      && contenuto.tipoContenutoFirmato?.codice === TipoContenutoDocumentoFirmatoEnum.FEA));
                if (templ2){

                  elem.action = false;
                  elem.coordinataX = templ2.coordinataX;
                  elem.coordinataY = templ2.coordinataY;
                  elem.pagina = templ2.pagina;

                }
              }
            );
            this.loading--;


          },
          () => this.loading--
        );
        }
        this.loading--;
  },
  () => this.loading--
  );
  }

  richiediOTP() {

    this.firmaFeaService.richiediOtp().subscribe(
      () => {
        this.modalService.info(this.translateService.instant('firma_fea.richiesta_OTP'),
          this.translateService.instant('firma_fea.richiesta_OTP_OK'))
          .then(() => { })
          .catch(() => { });
      },
      error => {
        this.modalService.error(this.translateService.instant('firma_fea.richiesta_OTP'),
          this.translateService.instant('errori.impossibile_richiedere_OTP'),
          error.error.errore)
          .then(() => { })
          .catch(() => { });
      }
    );
  }

  modifica(row: TemplateDocumentoDTO){
    if ( row?.documento?.contenutoEffettivo && this.iconaFea !== null && this.iconaFea !== ''){
      this.apriVisualizzatore(row);
    }


  }


  get disabledOtp() {
    return this.documenti.find(elem => !elem.coordinataX || !elem.coordinataY);
  }



  disabilitaFirma() {
    return this.staFirmando || this.percentualeFirma === 100 || !this.otp || this.iconaFea === null || this.iconaFea === '';
  }

  firma() {

    if (this.idle.isRunning()) {
      this.idle.stop();
    }
    const templateDocumentiDaFirmare: Array<TemplateDocumento> = [];
    this.documenti.forEach( template => {
      templateDocumentiDaFirmare.push({
          documento: {
            idPratica: template.documento.idPratica,
            idDocumentoExt: template.documento.idDocumentoExt,
            titolo: template.documento.titolo,
            tipo: template.documento.tipo,
            descrizione: template.documento.descrizione,
            idDocumentoParentExt: template.documento.idDocumentoParentExt,
            id: template.documento.id,
            autore: template.documento.autore,
            stato: template.documento.stato,
            contenuti: template.documento.contenuti,
            ultimaModifica: template.documento.ultimaModifica,
            smistamento: template.documento.smistamento
          },
          coordinataX: template.coordinataX ?? 0,
          coordinataY: template.coordinataY ?? 0,
          pagina: template.pagina ?? 0,
      });
    });

    const payload: FirmaFeaRequest = {
      templateDocumenti: templateDocumentiDaFirmare,
      otp: this.otp,
      iconaFea: this.iconaFea ?? ''
    };
    this.firmaFeaService.firma(payload).pipe(
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
      if (!this.idle.isRunning()) {
        this.idle.watch();
      }
      },
      error => {
        this.staFirmando = false;
        this.modalService.error(
          this.translateService.instant('errori.completamento_task'),
          Utils.toMessage(error) ?? this.translateService.instant('errori.completamento_task'),
          error.error.errore
        ).then(() => {
        this.modal.dismiss('finish_to_sign_docs');
        }).catch(() => {});
      }
    );
  }

  apriVisualizzatore(template: TemplateDocumentoDTO) {
    const data = this.helperService.searchHelperRef(this.route);
    const modalRef = this.modalRef.open(PreviewDocumentoModalComponent, { size: 'xl', backdrop: 'static' });

    modalRef.componentInstance.doc = template.documento;
    modalRef.componentInstance.contenuto = template.documento.contenutoEffettivo;
    modalRef.componentInstance.riquadroFirma = true;
    modalRef.componentInstance.textLayer = true;
    modalRef.componentInstance.ignoreKeys = true;
    modalRef.componentInstance.pageViewMode = 'single';
    modalRef.componentInstance.showZoomButtons = false;
    modalRef.componentInstance.zoom = 'page-fit';
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = 'firma-fea-aggiungi-firma';

    modalRef.result.then(done => {
    },
    (reason) => {
      if (reason.type === 'infoPageAndAxis') {

        template.coordinataX = reason.done.x;
        template.coordinataY = reason.done.y;
        template.pagina = reason.done.page;

      }
    }).catch(() => { });
  }

  elaboraRisultato(risultato: any[]): void {
    let numSuccesso = 0;
    let numErrori = 0;


    this.table?.uncheckAll();

    for (const entry of risultato) {
      if (entry.successo) {
        numSuccesso ++;
        this.rowClasses[entry.templateDocumento.documento.id] = 'success';
      } else {
        numErrori ++;
        this.rowClasses[entry.templateDocumento.documento.id] = 'danger';
        this.rowErrors[entry.templateDocumento.documento.id] = entry.errore.message;
      }
    }

    if (numErrori <= 0) {
      this.modalService.simpleInfo('Tutte le attività sono state elaborate con successo.').finally(() => {
        this.staFirmando = false;
        this.modal.dismiss('finish_to_sign_docs');
        this.table?.refresh();
      });

      return;
    } else if (!numErrori) {
      this.modalService.simpleInfo(numSuccesso + ' attività sono state elaborate con successo.').finally(() => {
        this.staFirmando = false;
        this.table?.refresh();
      });

    } else {
      this.modalService.simpleError('Si sono verificati degli errori nell\'elaborazione di ' + numErrori + ' attività.').finally(() => {
        this.staFirmando = false;
        this.table?.refresh();
      });
    }

    this.table?.refresh();
  }


}


