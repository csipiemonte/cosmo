/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, Pipe, PipeTransform, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
// tslint:disable-next-line:max-line-length
import { CosmoTableColumnSize, CosmoTableComponent, CosmoTableSortDirection, ICosmoTableColumn, ICosmoTablePageRequest, ICosmoTablePageResponse, ICosmoTableReloadContext } from 'ngx-cosmo-table';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { ModalStatus } from 'src/app/shared/enums/modal-status';
import { FormatoFile } from 'src/app/shared/models/api/cosmoecm/formatoFile';
import { FormatoFileService } from 'src/app/shared/services/formato-file.service';

@Component({
  selector: 'app-modifica-formato-file-modal',
  templateUrl: './modifica-formato-file-modal.component.html',
  styleUrls: ['./modifica-formato-file-modal.component.scss']
})


export class ModificaFormatoFileModalComponent implements OnInit{

  loading = 0;
  loadingError: any = null;
  formatiFileOnDB: FormatoFile[] = [];
  formatiFile: FormatoFile[] = [];
  formatiFileGrouped: FormatoFile[] = [];

  @ViewChild('tableFormatoFileAggiunti') tableFormatoFileAggiunti?: CosmoTableComponent;
  @ViewChild('tableFormatiFile') tableFormatiFile?: CosmoTableComponent;

  constructor(private formatoFileService: FormatoFileService,
              public modal: NgbActiveModal) {}


  columnsFormatoFileAggiunti: ICosmoTableColumn[] = [
    {
      label: 'Codice', field: 'codice', canSort: false, canHide: false, canFilter: true, defaultFilter: true
    },
    {
      label: 'Mime-Type', field: 'mimeType', canSort: false, canFilter: true, defaultFilter: true
    },
    {
      label: 'Descrizione', field: 'descrizione', canSort: false, canFilter: true, defaultFilter: true
    },
    {
    name: 'azioni', label: 'Azioni',
    canHide: false,
    canSort: false,
    applyTemplate: true,
    size: CosmoTableColumnSize.XXS
  },
  ];

  columnsFormatiFile = [
    {
      label: 'Codice', field: 'codice', canSort: false, canHide: false
    },
    {
      label: 'Mime-Type', field: 'mimeType', canSort: false
    },
    {
      label: 'Descrizione', field: 'descrizione', canSort: false
    }, {
    name: 'azioni', label: 'Azioni',
    canHide: false,
    canSort: false,
    applyTemplate: true,
    size: CosmoTableColumnSize.XXS
  },
  ];

  ngOnInit(): void {
    this.formatiFile.forEach(a => {
      if (a.descrizione){
        if (!this.formatiFileGrouped.find(b => a.descrizione && b.descrizione?.localeCompare(a.descrizione.split(' (')[0]) === 0)) {
          a.descrizione = a.descrizione.split(' (')[0];
          a.codice = a.descrizione;
          a.mimeType = 'raggruppato';
          this.formatiFileGrouped.push(a);
        }
      } else {
        this.formatiFileGrouped.push(a);
      }
    });

    this.formatiFile = this.formatiFileGrouped;

  }

  dataProviderFormatiFile = (input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext) => {

    const payload: any = {
      page: input.page ?? 0,
      size: input.size ?? 10,
      sort: input.sort?.length ? (input.sort[0]?.direction === CosmoTableSortDirection.DESCENDING ?
        '-' : '+' + input.sort[0]?.property) : undefined,
      ...this.getFilterPayload(input),
    };

    return this.formatoFileService.getGroupedFormatiFile(JSON.stringify(payload)).pipe(
      // finalize(() => this.loading --),
      map(response => {
        return {
          content: response.formatiFile ?? [],
          number: response.pageInfo?.page,
          numberOfElements: response.formatiFile?.length,
          size: response.pageInfo?.pageSize,
          totalElements: response.pageInfo?.totalElements,
          totalPages: response.pageInfo?.totalPages,
        };
      })
    );

  }

  getFilterPayload(input: ICosmoTablePageRequest): any {
    const output: any = {
      filter: {},
    };

    if (input.query) {
      output.filter.fullText = {
        ci: input.query
      };
    }

    return output;
  }

  cancel(): void {
    this.modal.close();
  }

  rimuoviFormatoFile(row: FormatoFile) {
    const toRemove = this.formatiFile.find(ff => ff.codice === row.codice);
    if (!toRemove) {
      return;
    }
    this.formatiFile.splice(this.formatiFile.indexOf(toRemove), 1);

    setTimeout(() => {
      if (this.tableFormatoFileAggiunti) {
        this.tableFormatoFileAggiunti.refresh();
      }
    }, 1);
  }


isFormatoFileSelezionato(row: FormatoFile): boolean {
  if (!this.formatiFile?.length) {
    return false;
  }
  return !!this.formatiFile.find(ff => ff.codice === row.codice);
}

selezionaFormatoFile(row: FormatoFile): void {
  this.formatiFile.push({
    ...row
  });

  setTimeout(() => {
    if (this.tableFormatoFileAggiunti) {
      this.tableFormatoFileAggiunti.refresh();
    }
  }, 1);
}

salva()
 {
  this.modal.close(this.formatiFile);
}



}
