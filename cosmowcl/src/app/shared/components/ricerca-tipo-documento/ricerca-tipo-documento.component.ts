/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, forwardRef, Input } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { RicercaEntityComponent, SelezioneEntity } from '../ricerca-entity/ricerca-entity.component';
import { Utils } from '../../utilities/utilities';
import { TipoDocumento } from '../../models/api/cosmopratiche/tipoDocumento';
import { DocumentiService } from 'src/app/shared/components/consultazione-documenti/services/documenti.service';
import { TipiDocumentiService } from '../../services/tipi-documenti/tipi-documenti.service';

@Component({
  selector: 'app-ricerca-tipo-documento',
  templateUrl: '../ricerca-entity/ricerca-entity.component.html',
  styleUrls: ['../ricerca-entity/ricerca-entity.component.scss'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => RicercaTipoDocumentoComponent),
    multi: true
  }]
})
export class RicercaTipoDocumentoComponent extends RicercaEntityComponent<TipoDocumento> {

  @Input() filter?: (input: TipoDocumento) => boolean;
  @Input() promptNew?: (results: SelezioneEntity<TipoDocumento>[], term: string, newEntity: TipoDocumento) => boolean;
  @Input() tipiDocumenti: TipoDocumento[] = [];
  @Input() codicePadre: string | null = null;
  @Input() codiceTipoPratica: string | null = null;
  @Input() onlyByTipoPratica = false;

  constructor(
    private logger: NGXLogger,
    private documentiService: DocumentiService,
    private tipiDocumentiService: TipiDocumentiService
  ) {
    super();
    this.logger.trace('building component');
  }

  getEntityDescriptionSingular(): string {
    return 'tipo documento';
  }

  format(input: SelezioneEntity<TipoDocumento>): string {
    return input?.entity?.descrizione ?? '--';
  }

  getIcon(option: SelezioneEntity<TipoDocumento>): string | null {
    return 'fas fa-file';
  }

  isEnriched(entry: SelezioneEntity<TipoDocumento>): boolean {
    return !!(entry?.entity?.descrizione);
  }

  clean(entity: TipoDocumento): TipoDocumento {
    return {
      ...entity,
      codice: Utils.require(entity.codice)
    };
  }

  buildNewFromText(text: string): TipoDocumento {
    let codice = text.replace(/[^a-zA-Z0-9_]+/gi, '-');
    while (codice.startsWith('-')) {
      codice = codice.substr(1);
    }
    while (codice.endsWith('-')) {
      codice = codice.substr(0, codice.length - 1);
    }

    return {
      codice,
      descrizione: text,
    };
  }

  search(term: string | null): Observable<SelezioneEntity<TipoDocumento>[]> {
      if (this.onlyByTipoPratica && this.codiceTipoPratica) {
      return this.tipiDocumentiService.getTipiDocumentiAll(this.codiceTipoPratica ?? '').pipe(
        map(response => {
          this.tipiDocumenti.forEach(tipiDocumenti => {
            if (!response.some(t => tipiDocumenti.codice === t.codice)) {
              response.push(tipiDocumenti);
            }
          });
          return (response ?? [])
            .filter(i => !(term?.trim()?.length) || (i.codice + ' ' + i.descrizione).toUpperCase().includes(term.trim().toUpperCase()))
            .filter(i => this.filter ? this.filter(i) : true)
            .sort((e1, e2) => (e1.descrizione ?? '')?.localeCompare((e2.descrizione ?? '')))
            .slice(0, 5)
            .map(i => {
              return { entity: this.clean(i) };
            });
        })
      );
    } else {
      return this.documentiService.ricercaTipiDocumento([], false, this.codicePadre, this.codiceTipoPratica).pipe(
        map(response => {
          this.tipiDocumenti.forEach(tipiDocumenti => {
            if (!response.some(t => tipiDocumenti.codice === t.codice)) {
              response.push(tipiDocumenti);
            }
          });

          return (response ?? [])
            .filter(i => !(term?.trim()?.length) || (i.codice + ' ' + i.descrizione).toUpperCase().includes(term.trim().toUpperCase()))
            .filter(i => this.filter ? this.filter(i) : true)
            .sort((e1, e2) => (e1.descrizione ?? '')?.localeCompare((e2.descrizione ?? '')))
            .slice(0, 5)
            .map(i => {
              return { entity: this.clean(i) };
            });

        })
      );
    }

  }

  displayNewOption(results: SelezioneEntity<TipoDocumento>[], term: string, e: TipoDocumento): boolean {
    return !!(term?.trim()?.length) && !results.some(c => c.entity?.codice === term)
      && !this.tipiDocumenti.some(i => i.codice === term);
  }

  enrich(entry: SelezioneEntity<TipoDocumento>): Observable<SelezioneEntity<TipoDocumento> | null> {
    if (!entry?.entity?.codice) {
      return of(null);
    }

    return this.documentiService.ricercaTipiDocumento([], false, this.codicePadre, this.codiceTipoPratica).pipe(map(response => {
      return { entity: response.find(c => c.codice === entry.entity?.codice) };
    }));
  }

  setDisabled(isDisable: boolean) {
    this.disabled = isDisable;
  }

}
