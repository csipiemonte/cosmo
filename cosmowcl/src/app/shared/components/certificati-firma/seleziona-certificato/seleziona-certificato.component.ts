/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CertificatiService } from '../certificati/certificati.service';
import { CosmoTableComponent, ICosmoTableColumn, CosmoTableColumnSize, CosmoTableFormatter } from 'ngx-cosmo-table';
import { finalize } from 'rxjs/operators';
import { DatePipe } from '@angular/common';
import { CertificatoFirma } from 'src/app/shared/models/api/cosmoauthorization/certificatoFirma';
import { AggiungiCertificatoModalComponent } from '../aggiungi-certificato-modal/aggiungi-certificato-modal.component';
import { ModaleParentComponent } from 'src/app/modali/modale-parent-component';
import { HelperService } from 'src/app/shared/services/helper.service';
import { ActivatedRoute } from '@angular/router';
import { DateUtils } from 'src/app/shared/utilities/date-utils';

@Component({
  selector: 'app-seleziona-certificato',
  templateUrl: './seleziona-certificato.component.html',
  styleUrls: ['./seleziona-certificato.component.scss']
})
export class SelezionaCertificatoComponent extends ModaleParentComponent implements OnInit {

  loading = 0;
  loadingError: any | null = null;
  loadingCertificatiError: any | null = null;

  certificatiFirmaServer!: CertificatoFirma[];
  certificatiFirma!: CertificatoFirma[];

  certificatoSelezionato: CertificatoFirma | null = null;
  codiceTipoPratica = '';

  codicePagina!: string;
  codiceTab!: string;

  @ViewChild('table') table: CosmoTableComponent | null = null;

  certificatiColumns: ICosmoTableColumn[] = [
    {
      name: 'descrizione', label: 'Descrizione', field: 'descrizione', canSort: true, canFilter: true, defaultFilter: true
    },
    {
      name: 'ente certificatore', label: 'Ente certificatore', field: 'enteCertificatore.descrizione', canSort: true
    },
    {
      name: 'tipo credenziali', label: 'Tipo credenziali', field: 'tipoCredenzialiFirma.descrizione', canSort: true
    },
    {
      name: 'data scadenza', label: 'Data scadenza', canSort: true,
      formatters: [{
        formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy',
       }, {
        format: (raw: any) => raw ?? '--',
      }],
      valueExtractor: raw => DateUtils.parse(raw.dataScadenza)
    },
    { name: 'azioni', canHide: false, canSort: false, applyTemplate: true, size: CosmoTableColumnSize.XXS }
  ];

  constructor(
    public modal: NgbActiveModal,
    private ngbModal: NgbModal,
    private certificatiService: CertificatiService,
    private datePipe: DatePipe,
    public helperService: HelperService,
    private route: ActivatedRoute,
  )
  { super(helperService);
    this.setModalName('seleziona-certificato');
  }

  ngOnInit(): void {
    this.refresh();
    this.searchHelperModale(this.codicePagina, this.codiceTab);
  }

  refresh() {
    this.loading++;
    // this.loadingError = null;
    this.loadingCertificatiError = null;

    this.certificatiService.getCertificati()
      .pipe(
        finalize(() => {
          this.loading--;
        })
      )
      .subscribe(response => {
        this.certificatiFirmaServer = response;
        this.certificatiFirma = response;
        this.table?.refresh(false);
      }, failure => {
        // this.loadingError = failure;
        this.loadingCertificatiError = failure;
      });

  }

  selectionChangeHandler(row: any[]): void {
    // NOP
  }


  get selectedItems(): any[] {
    return this.table?.getStatusSnapshot()?.checkedItems ?? [];
  }

  get canProceed(): boolean {
      return this.selectedItems?.length > 0;
  }

  proceed(): void {
    if (!this.canProceed) {
      return;
    }

    if (this.selectedItems?.length === 1) {
      this.modal.close(this.selectedItems[0]);
    }else{
      throw new Error('Invalid selected items: ' + this.selectedItems.length);
    }

  }

  aggiungiCertificato(){
    const data = this.helperService.searchHelperRef(this.route);
    const modal = this.ngbModal.open(AggiungiCertificatoModalComponent, {backdrop: 'static', size: 'xl'});
    modal.componentInstance.codiceTipoPratica = this.codiceTipoPratica;
    modal.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modal.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modal.result.then(cert => this.modal.close(cert)).catch(() => this.refresh());

  }



}
