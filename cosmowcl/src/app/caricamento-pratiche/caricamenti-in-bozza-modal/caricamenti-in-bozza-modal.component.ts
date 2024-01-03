/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ICosmoTableColumn, CosmoTableFormatter, CosmoTableColumnSize, CosmoTableComponent
  , ICosmoTableReloadContext, ICosmoTablePageRequest } from 'ngx-cosmo-table';
import { CaricamentoPratica } from 'src/app/shared/models/api/cosmopratiche/caricamentoPratica';
import { DateUtils } from 'src/app/shared/utilities/date-utils';
import { AggiungiCaricamentoPraticaModalComponent
} from '../aggiungi-caricamento-pratica-modal/aggiungi-caricamento-pratica-modal.component';
import { map } from 'rxjs/operators';
import { CaricamentoPraticheService } from 'src/app/shared/services/caricamento-pratiche.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { TranslateService } from '@ngx-translate/core';
import { Utils } from 'src/app/shared/utilities/utilities';
import { ModaleParentComponent } from 'src/app/modali/modale-parent-component';
import { HelperService } from 'src/app/shared/services/helper.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-caricamenti-in-bozza-modal',
  templateUrl: './caricamenti-in-bozza-modal.component.html',
  styleUrls: ['./caricamenti-in-bozza-modal.component.scss']
})
export class CaricamentiInBozzaModalComponent extends ModaleParentComponent implements OnInit {

  @ViewChild('table') table: CosmoTableComponent | null = null;

  loading = 0;
  loadingError: any | null = null;

  columnsCaricamentoInBozza: ICosmoTableColumn[] = [{
    label: 'Nome file', field: 'nomeFile', canSort: false, canHide: false,
  }, {
    name: 'dataInserimento', label: 'Data Inserimento', field: 'dtInserimento', canSort: false,
    formatters: [{
      formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy',
     }, {
      format: (raw: any) => raw ?? '--',
    }],
    valueExtractor: raw => DateUtils.parse(raw.dtInserimento),
  }, {
    name: 'dataUltimaModifica', label: 'Data Ultima Modifica', field: 'dtUltimaModifica', canSort: false,
    formatters: [{
      formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy',
     }, {
      format: (raw: any) => raw ?? '--',
    }],
    valueExtractor: raw => DateUtils.parse(raw.dtUltimaModifica),
  },
  {
    name: 'azioni', label: 'Azioni',
    canHide: false,
    canSort: false,
    applyTemplate: true,
    size: CosmoTableColumnSize.XXS
  }
  ];

  codicePagina!: string;
  codiceTab!: string;
  codiceModale!: string;

  constructor(
    private caricamentoPraticheService: CaricamentoPraticheService,
    private modalService: ModalService,
    private translateService: TranslateService,
    private ngbModal: NgbModal,
    public modal: NgbActiveModal,
    public helperService: HelperService,
    public route: ActivatedRoute,
  ) {
    super(helperService);
  }

  ngOnInit(): void {
    this.setModalName(this.codiceModale);
    this.searchHelperModale(this.codicePagina, this.codiceTab);
  }

  continuaCaricamento(caricamentoPratica: CaricamentoPratica){
    const data = this.helperService.searchHelperRef(this.route);
    const modalRef = this.ngbModal.open(AggiungiCaricamentoPraticaModalComponent, {backdrop: 'static', keyboard: false, size: 'xl'});
    modalRef.componentInstance.folderName = caricamentoPratica.pathFile;
    modalRef.componentInstance.caricamentoPratica = caricamentoPratica;
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = 'caricamento-file-caricamento-in-bozza';
    modalRef.result.then(() => this.table?.refresh()).catch();
  }

  elimina(caricamentoPratica: CaricamentoPratica){
      this.modalService.scegli(
      this.translateService.instant('common.eliminazione_utente_titolo'),
      [{
        testo: Utils.parseAndReplacePlaceholders(this.translateService.instant('common.eliminazione_messaggio'),
          ['questo caricamento pratica']),
        classe: 'text-danger'
      }], [
        {testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt'},
        {testo: 'Annulla', dismiss: true, defaultFocus: true}
      ]
      ).then(() => {
        if (caricamentoPratica && caricamentoPratica.id){
          this.caricamentoPraticheService.eliminaCaricamentoPratica(caricamentoPratica.id?.toString()).subscribe(() => {
          this.table?.refresh();
          }, failure => {
            this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore);
          });
        }
      }).catch(() => {});
  }

  dataProviderCaricamentoInBozza = (input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext) => {

    const payload = {
      page: input.page ?? 0,
      size: input.size ?? 10
    };
    return this.caricamentoPraticheService.getCaricamentiInBozza(JSON.stringify(payload)).pipe(
      map(response => ({
        content: response.caricamentoPratiche ?? [],
        number: response.pageInfo?.page,
        numberOfElements: response.caricamentoPratiche?.length,
        size: response.pageInfo?.pageSize,
        totalElements: response.pageInfo?.totalElements,
        totalPages: response.pageInfo?.totalPages,
      }))
    );
  }


}
