/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { CosmoTableComponent, CosmoTableSortDirection, ICosmoTableColumn, ICosmoTablePageRequest, ICosmoTablePageResponse } from 'ngx-cosmo-table';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { Tag } from '../shared/models/api/cosmoauthorization/tag';
import { UserInfoWrapper } from '../shared/models/user/user-info';
import { GruppiService } from '../shared/services/gruppi.service';
import { SecurityService } from '../shared/services/security.service';
import { TagsService } from '../shared/services/tags.service';
import { UtentiService } from '../shared/services/utenti.service';
import { Utils } from '../shared/utilities/utilities';

@Component({
  selector: 'app-info-utente',
  templateUrl: './info-utente.component.html',
  styleUrls: ['./info-utente.component.scss'],
  providers: [TagsService]
})
export class InfoUtenteComponent implements OnInit {

  tagsColumns: ICosmoTableColumn[] = [{
    label: 'Tipo tag', field: 'tipoTag', canSort: true, canHide: false,
    valueExtractor: (row: Tag) => row.tipoTag ? row.tipoTag.descrizione ?? row.tipoTag?.codice : '--'
  }, {
    label: 'Valore', field: 'descrizione', canSort: true,
  }];

  gruppiColumns: ICosmoTableColumn[] = [{
    label: 'Nome gruppo', field: 'nome', canSort: false, canHide: false,
  }, {
    label: 'Descrizione', field: 'descrizione', canSort: false,
  }, {
    label: 'Codice', field: 'codice', canSort: false,
  }];

  idUtente: number | undefined = undefined;
  idEnte: number | undefined = undefined;

  loading = 0;
  loadingError: any | null = null;

  @ViewChild('tagsTable') tagsTable: CosmoTableComponent | null = null;
  @ViewChild('gruppiTable') gruppiTable: CosmoTableComponent | null = null;

  constructor(
    private securityService: SecurityService,
    private tagService: TagsService,
    private gruppiService: GruppiService,
    private utentiService: UtentiService) { }


  tagsDataProvider = (input: ICosmoTablePageRequest) => {
    const payload = {
      page: input.page ?? 0,
      size: input.size ?? 10,
      sort: input.sort?.length ? (input.sort[0]?.direction === CosmoTableSortDirection.DESCENDING ?
        '-' : '+' + input.sort[0]?.property) : undefined,
      ...this.getFilterPayload(input),
    };

    const output: Observable<ICosmoTablePageResponse> = this.tagService.getTags(JSON.stringify(payload)).pipe(
      map(response => {
        return {
          content: response.elementi ?? [],
          number: response.pageInfo?.page,
          numberOfElements: response.elementi?.length,
          size: response.pageInfo?.pageSize,
          totalElements: response.pageInfo?.totalElements,
          totalPages: response.pageInfo?.totalPages,
        };
      })
    );

    return output;
  }

  gruppiDataProvider = (input: ICosmoTablePageRequest) => {

    const payload = {
      page: input.page ?? 0,
      size: input.size ?? 10,
      sort: input.sort?.length ? (input.sort[0]?.direction === CosmoTableSortDirection.DESCENDING ?
        '-' : '+' + input.sort[0]?.property) : undefined,
      ...this.getFilterPayload(input),
    };

    const output: Observable<ICosmoTablePageResponse> = this.gruppiService.getGruppi(JSON.stringify(payload)).pipe(
      map(response => {
        return {
          content: response.gruppi ?? [],
          number: response.pageInfo?.page,
          numberOfElements: response.gruppi?.length,
          size: response.pageInfo?.pageSize,
          totalElements: response.pageInfo?.totalElements,
          totalPages: response.pageInfo?.totalPages,
        };
      })
    );
    return output;
  }

  ngOnInit(): void {

    this.loading++;
    this.loadingError = null;

    this.securityService.getCurrentUser().subscribe(response => {
      this.utentiService.getUtenteByCodiceFiscale(response.codiceFiscale).pipe(
        finalize(() => {
          this.loading--;
        })
      ).subscribe(response2 => {
        this.idUtente = response2?.id;
        this.idEnte = response?.ente?.id;
        this.refresh();
      }, failure => {
        this.loadingError = failure;
      });
    }, failure => {
      this.loadingError = failure;
      this.loading--;
    });
  }

  refresh(inBackground = false) {
    if (this.tagsTable) {
      this.tagsTable.refresh(inBackground);
    }
    if (this.gruppiTable) {
      this.gruppiTable.refresh(inBackground);
    }
  }

  tornaIndietro() {
    window.history.back();
  }

  getFilterPayload(input: ICosmoTablePageRequest): any {
    const output: any = {
      filter: {
        idUtente: {
          eq: Utils.require(this.idUtente, 'idUtente')
        },
        idEnte: {
          eq: Utils.require(this.idEnte, 'idEnte')
        }
      },
    };

    if (input.query) {
      output.filter.fullText = {
        ci: input.query
      };
    }

    return output;
  }

}
