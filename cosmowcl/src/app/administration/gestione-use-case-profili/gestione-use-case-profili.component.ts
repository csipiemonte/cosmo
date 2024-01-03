/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { CosmoTableColumnSize, CosmoTableComponent, CosmoTableSortDirection, ICosmoTableColumn, ICosmoTablePageRequest,
  ICosmoTableReloadContext } from 'ngx-cosmo-table';
import { map } from 'rxjs/operators';
import { Profilo } from 'src/app/shared/models/api/cosmoauthorization/profilo';
import { ModalService } from 'src/app/shared/services/modal.service';
import { ProfiliService } from 'src/app/shared/services/profili.service';
import { Utils } from 'src/app/shared/utilities/utilities';

@Component({
  selector: 'app-gestione-use-case-profili',
  templateUrl: './gestione-use-case-profili.component.html',
  styleUrls: ['./gestione-use-case-profili.component.scss']
})
export class GestioneUseCaseProfiliComponent implements OnInit {

  @ViewChild('table') table: CosmoTableComponent | null = null;

  columns: ICosmoTableColumn[] = [{
    label: 'Codice', field: 'codice', canSort: false, canHide: false,
  }, {
    label: 'Descrizione', field: 'descrizione', canSort: false,
  }, {
    name: 'azioni', label: 'Azioni', canHide: false, canSort: false, applyTemplate: true, size: CosmoTableColumnSize.XXS
  }];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private modalService: ModalService,
    private translateService: TranslateService,
    private profiliService: ProfiliService) { }

  ngOnInit(): void {
  }

  refresh(inBackground = false) {
    if (this.table) {
      this.table.refresh(inBackground);
    }
  }

  dataProvider = (input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext) => {

    const payload: any = {
      filter: {
        assegnabile: {
          defined: true,
          eq: true,
        }
      },
      page: input.page ?? 0,
      size: input.size ?? 10,
      sort: input.sort?.length ? (input.sort[0]?.direction === CosmoTableSortDirection.DESCENDING ?
        '-' : '+' + input.sort[0]?.property) : undefined,
    };

    return this.profiliService.getProfili(JSON.stringify(payload)).pipe(
        map(response => {
          return {
            content: response.profili ?? [],
            number: response.pageInfo?.page,
            numberOfElements: response.profili?.length,
            size: response.pageInfo?.pageSize,
            totalElements: response.pageInfo?.totalElements,
            totalPages: response.pageInfo?.totalPages,
          };
        })
      );

  }

  modifica(profilo: Profilo) {
    this.router.navigate(['modifica', profilo.id], { relativeTo: this.route });
  }

  aggiungi() {
    this.router.navigate(['nuovo'], { relativeTo: this.route });
  }

  elimina(profilo: Profilo) {
    this.modalService.scegli(
      this.translateService.instant('use_case_profili.dialogs.elimina.titolo'),
      [{
        testo: Utils.parseAndReplacePlaceholders(
          this.translateService.instant('common.eliminazione_messaggio'), [profilo.descrizione ?? '']),
        classe: 'text-danger'
      }], [
      { testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt' },
      { testo: 'Annulla', dismiss: true, defaultFocus: true }
    ]
    ).then(proceed => {
      this.profiliService.deleteProfilo(Utils.require(profilo.id, 'id')).subscribe(() => {
        this.refresh(false);
      }, failure => this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore));
    }).catch(() => {});
  }
}
