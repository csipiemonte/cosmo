/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Injector, OnInit, ViewChild } from '@angular/core';
import { Tag } from 'src/app/shared/models/api/cosmoauthorization/tag';
import { SecurityService } from 'src/app/shared/services/security.service';
import { TagsService } from 'src/app/shared/services/tags.service';
import { FunzionalitaParentComponent } from '../funzionalita-parent.component';
import { TabLifecycleCallback } from '../models/tab-status.models';
import { Pratica } from 'src/app/shared/models/api/cosmopratiche/pratica';
import { forkJoin } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { Utils } from 'src/app/shared/utilities/utilities';
import { CosmoTableColumnSize, CosmoTableComponent, ICosmoTableColumn, ICosmoTableItemDroppedContext } from 'ngx-cosmo-table';
import { MessaggioControlliObbligatori, TipoMessaggio } from 'src/app/shared/models/messaggi-controlli-obbligatori/messaggio-controlli-obbligatori';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-assegnazione-tags',
  templateUrl: './assegnazione-tags.component.html',
  styleUrls: ['./assegnazione-tags.component.scss']
})
export class AssegnazioneTagsComponent extends FunzionalitaParentComponent implements OnInit {

messaggiObbligatori: MessaggioControlliObbligatori[] = [];
columnsAssociabili: ICosmoTableColumn[] = [
{
  name: 'iconaTag',
  canHide: false, canSort: false,
  applyTemplate: true, size: CosmoTableColumnSize.XXS
},
{
  name: 'tagAssociabili', label: 'Tag associabili', canSort: true, canHide: false, valueExtractor: (row: Tag) => this.getInfoTag(row)
}
];

columnsAssociati: ICosmoTableColumn[] = [
  {
    name: 'iconaTag',
    canHide: false, canSort: false,
    applyTemplate: true, size: CosmoTableColumnSize.XXS
  },
  {
    name: 'tagAssociabili', label: 'Tag associati', canSort: true, canHide: false, valueExtractor: (row: Tag) => this.getInfoTag(row)
  }
  ];

loading = 0;
loadingError: any | null = null;
loaded = false;
tags: Array<Tag> = [];
tagsDisponibili: Array<Tag> = [];
dragAndDropIsTouched = false;

@ViewChild('table1') table1: CosmoTableComponent | null = null;
@ViewChild('table2') table2: CosmoTableComponent | null = null;

constructor(
  public injector: Injector,
  private tagsService: TagsService,
  private securityService: SecurityService,
  private translateService: TranslateService
) {
  super(injector);
}

ngOnInit(): void {
  this.refresh();
}

getInfoTag(row: any) {
  return (row.tipoTag?.descrizione ?? '--') +  ' - ' + row.descrizione;
}

refresh() {
  this.loading++;
  this.loadingError = null;

  this.securityService.getCurrentUser().subscribe(cu => {

    const payloadDisponibili: any = {
      filter: {
        idEnte: {
          eq: cu.ente?.id
        }
      }
    };
    const payloadRelPratica = Utils.clone(payloadDisponibili);
    payloadRelPratica.filter.idPratica = {eq: this.idPratica};

    forkJoin({
      tagDisp: this.tagsService.getTags(JSON.stringify(payloadDisponibili)),
      tagPratica: this.tagsService.getTags(JSON.stringify(payloadRelPratica))
    }).pipe(
      finalize(() => {
        this.loading--;
        this.update();
        this.checkWarning();
      })
    ).subscribe(res => {
      this.tags = res.tagPratica.elementi ?? [];
      if (res.tagDisp.elementi) {
        res.tagDisp.elementi.forEach(td => {
          if (!this.tags.find(t => t.id === td.id)) {
            this.tagsDisponibili.push(td);
          }
        });
      }
    });
  });
}

public update() {
  this.sendData({});
}

public isWip(): boolean {
  return this.tags.length === 0;
}

public isValid(): boolean {
  return !this.isWip();
}

beforeSave(): TabLifecycleCallback {
  this.aggiornaPratica();
}

beforeConfirm(): TabLifecycleCallback {
  this.aggiornaPratica();
}

aggiornaPratica() {
  const p: Pratica = this.pratica;
  p.tags = this.tags.map( i => i.id) as number[];
  this.praticheService.aggiornaPratica(this.pratica?.id ?? 0, p).subscribe();
}

onItemDropped(eventPayload: ICosmoTableItemDroppedContext) {
  const tableId = eventPayload.fromComponent?.tableId;
  if ( tableId === 'table1') {
    this.tagsDisponibili.splice(this.tagsDisponibili.indexOf(eventPayload.item), 1);
    this.tags.splice(eventPayload.event.currentIndex, 0, eventPayload.item);
  } else {
    this.tags.splice(this.tags.indexOf(eventPayload.item), 1);
    this.tagsDisponibili.splice(eventPayload.event.currentIndex, 0, eventPayload.item);
  }
  this.checkWarning();

  setTimeout(() => this.table1?.refresh(), 500);
  setTimeout(() => {this.table2?.refresh(); this.update(); }, 500);
}

checkWarning() {
  this.messaggiObbligatori.pop();
  const unassignedTags = this.tags.filter(t => t.utenti?.length === 0).map(r => r.descrizione);
  if (unassignedTags.length > 0) {
    let subjectMsg = this.translateService.instant('errori.tag_non_associati_a_utenti');
    subjectMsg = Utils.parseAndReplacePlaceholders(subjectMsg, [unassignedTags.toString().toUpperCase()]);
    this.messaggiObbligatori.push({messaggio: subjectMsg,
                                    tipoMessaggio: TipoMessaggio.WARNING});
  }
}

}
