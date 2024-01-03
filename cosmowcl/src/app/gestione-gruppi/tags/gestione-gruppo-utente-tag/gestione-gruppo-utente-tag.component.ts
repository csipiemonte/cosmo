/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CosmoTableColumnSize, CosmoTableComponent, CosmoTableSortDirection,
  ICosmoTableColumn, ICosmoTablePageRequest, ICosmoTablePageResponse, ICosmoTableReloadContext } from 'ngx-cosmo-table';
import { forkJoin, Observable } from 'rxjs';
import { finalize, map, mergeMap } from 'rxjs/operators';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { SecurityService } from 'src/app/shared/services/security.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { NGXLogger } from 'ngx-logger';
import { TagsService } from 'src/app/shared/services/tags.service';
import { Tag } from 'src/app/shared/models/api/cosmoauthorization/tag';
import { GruppiService } from 'src/app/shared/services/gruppi.service';
import { AggiornaGruppoRequest } from 'src/app/shared/models/api/cosmoauthorization/aggiornaGruppoRequest';
import { Gruppo } from 'src/app/shared/models/api/cosmoauthorization/gruppo';
import { ModalService } from 'src/app/shared/services/modal.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-gestione-gruppo-utente-tag',
  templateUrl: './gestione-gruppo-utente-tag.component.html',
  styleUrls: ['./gestione-gruppo-utente-tag.component.scss']
})
export class GestioneGruppoUtenteTagComponent implements OnInit {

  idUtente = 0;
  idGruppo = 0;
  gruppo?: Gruppo;
  tagUtente: Tag[] = [];
  cloneTagUtente: Tag[] = [];

  loading = 0;
  loadingError: any = null;

  @ViewChild('tableTagUtenteAggiunti') tableTagUtenteAggiunti?: CosmoTableComponent;
  @ViewChild('tableTagEnte') tableTagEnte?: CosmoTableComponent;

  constructor(private route: ActivatedRoute,
              private securityService: SecurityService,
              private logger: NGXLogger,
              private tagsService: TagsService,
              private gruppiService: GruppiService,
              private modalService: ModalService,
              private translateService: TranslateService) {}

  columnsTagUtente: ICosmoTableColumn[] = [{
    label: 'Tipo tag', field: 'tipoTag.descrizione', canSort: false, canHide: false, canFilter: true, defaultFilter: true
  }, {
    label: 'Codice', field: 'codice', canSort: false, canFilter: true, defaultFilter: true
  }, {
    label: 'Valore', field: 'descrizione', canSort: false, canFilter: true, defaultFilter: true
  }, {
    name: 'azioni', label: 'Azioni',
    canHide: false,
    canSort: false,
    applyTemplate: true,
    size: CosmoTableColumnSize.XXS
  },
  ];

  columnsTagEnte = [{
    label: 'Tipo tag', field: 'tipoTag.descrizione', canSort: false, canHide: false,
  }, {
    label: 'Codice', field: 'codice', canSort: false,
  }, {
    label: 'Valore', field: 'descrizione', canSort: false,
  }, {
    name: 'azioni', label: 'Azioni',
    canHide: false,
    canSort: false,
    applyTemplate: true,
    size: CosmoTableColumnSize.XXS
  },
  ];

  ngOnInit(): void {
    this.loading++;
    this.securityService.getCurrentUser().subscribe(cu => {
      if (cu) {
        this.idUtente = this.route.snapshot.params.idUtente ?? 0;
        this.idGruppo = this.route.snapshot.params.idGruppo ?? 0;
        const payload = {
          page: 0,
          size: 10,
          fields: 'tipoTag,codice,descrizione,id',
          ...this.getFilterPayload(cu)
        };
        this.logger.debug('loading page with search params', payload);
        forkJoin({
          gruppo: this.gruppiService.getGruppo(this.idGruppo),
          tags: this.tagsService.getTags(JSON.stringify(payload))
        }).pipe(
          finalize(() => {
            this.loading--;
          })
        )
        .subscribe( s => {
          this.gruppo = s.gruppo.gruppo;
          this.tagUtente = s.tags.elementi ?? [];
          this.cloneTagUtente = this.tagUtente.map(x => Object.assign({}, x));
        },
        err => {
          this.loadingError = err;
        });
      }
    });

  }

  getFilterPayload(utente: UserInfoWrapper): any {
    const output: any = {
      filter: {
        idEnte: {
          eq: Utils.require(utente?.ente?.id, 'idEnte')
        },
        idUtente: {
          eq: Utils.require(this.idUtente, 'idUtente')
        },
        idGruppo: {
          eq: Utils.require(this.idGruppo, 'idGruppo')
        }
      }
    };

    return output;
  }

  tornaIndietro() {
    window.history.back();
  }

  rimuoviTag(tag: Tag): void {
    const toRemove = this.tagUtente.find(u => u.id === tag.id);
    if (!toRemove) {
      return;
    }
    this.tagUtente.splice(this.tagUtente.indexOf(toRemove), 1);

    setTimeout(() => {
      if (this.tableTagUtenteAggiunti) {
        this.tableTagUtenteAggiunti.refresh();
      }
    }, 1);
  }

  dataProviderTagEnte = (input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext) => {
    const output: Observable<ICosmoTablePageResponse> = this.securityService.getCurrentUser().pipe(
      mergeMap(utente => {
        const payload: any = {
          page: input.page ?? 0,
          size: input.size ?? 10,
          sort: input.sort?.length ? (input.sort[0]?.direction === CosmoTableSortDirection.DESCENDING ?
            '-' : '+' + input.sort[0]?.property) : undefined,
          filter: {
            idEnte: {
              eq: utente.ente?.id
            },
          }
        };
        if (Utils.isNotBlank(input.query)) {
          payload.filter.codiceTipoTag = { ci: input.query?.trim() };
        }

        return this.tagsService.getTags(JSON.stringify(payload));
      }),
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

  isTagSelezionato(tag: Tag): boolean {
    if (!this.tagUtente?.length) {
      return false;
    }
    return !!this.tagUtente.find(t => t.id === tag.id);
  }

  selezionaTag(tag: Tag): void {
    this.tagUtente.push({
      ...tag
    });
    setTimeout(() => {
      if (this.tableTagUtenteAggiunti) {
        this.tableTagUtenteAggiunti.refresh();
      }
    }, 1);
  }

  pulisciCampi() {

    this.tagUtente = this.cloneTagUtente.map(x => Object.assign({}, x));
    setTimeout(() => {
      if (this.tableTagUtenteAggiunti) {
        this.tableTagUtenteAggiunti.refresh();
      }
    }, 1);
  }

  salva(){

    const out: AggiornaGruppoRequest = {
      nome: this.gruppo?.nome ?? '',
      codice: this.gruppo?.codice ?? '',
      descrizione: this.gruppo?.descrizione ?? '',
      idUtenti: this.gruppo?.utenti?.map(u => u.id) as number[],
      utenteTag: this.idUtente,
      codiciTipologiePratiche: this.gruppo?.tipologiePratiche?.map(tp => tp.codice) as string[],
      idTags: this.tagUtente.map(x => x.id) as number[]
    };
    this.gruppiService.aggiornaGruppo(this.idGruppo, out).subscribe(
      result => {
      let messaggio = this.translateService.instant('gruppi.dialogs.modificata_relazione.messaggio');
      messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.gruppo?.nome ?? '']);
      this.modalService.info(this.translateService.instant('gruppi.dialogs.modificata_relazione.titolo'),
      messaggio).then(
        () => {
          this.tornaIndietro();
        }
      ).catch(() => { });
  },
  error => {
    this.modalService.simpleError(Utils.toMessage(error), error.error.errore).then().catch(() => { });
  }
);


  }
}
