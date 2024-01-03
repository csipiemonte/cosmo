/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, forwardRef } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { RicercaEntityComponent, SelezioneEntity } from '../ricerca-entity/ricerca-entity.component';
import { TipologiaFunzionalitaFormLogico } from '../../models/api/cosmobusiness/tipologiaFunzionalitaFormLogico';
import { GestioneIstanzeFunzionalitaFormLogiciService } from 'src/app/administration/gestione-istanze-funzionalita-form-logici/gestione-istanze-funzionalita-form-logici.service';

@Component({
  selector: 'app-ricerca-funzionalita-form-logico',
  templateUrl: '../ricerca-entity/ricerca-entity.component.html',
  styleUrls: ['../ricerca-entity/ricerca-entity.component.scss'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => RicercaFunzionalitaFormLogicoComponent),
    multi: true
  }]
})
export class RicercaFunzionalitaFormLogicoComponent extends RicercaEntityComponent<TipologiaFunzionalitaFormLogico> {

  constructor(
    private logger: NGXLogger,
    private istanzeFunzionalitaFormLogiciService: GestioneIstanzeFunzionalitaFormLogiciService,
  ) {
    super();
    this.logger.trace('building component');
  }

  getEntityDescriptionSingular(): string {
    return 'funzionalità';
  }

  format(input: SelezioneEntity<TipologiaFunzionalitaFormLogico>): string {
    return input?.entity?.descrizione ?? '--';
  }

  getIcon(option: SelezioneEntity<TipologiaFunzionalitaFormLogico>): string | null {
    return 'fas fa-object-ungroup';
  }

  isEnriched(entry: SelezioneEntity<TipologiaFunzionalitaFormLogico>): boolean {
    return !!(entry?.entity?.descrizione);
  }

  clean(entity: TipologiaFunzionalitaFormLogico): Partial<TipologiaFunzionalitaFormLogico> {
    return {
      ...entity,
    };
  }

  search(term: string | null): Observable<SelezioneEntity<TipologiaFunzionalitaFormLogico>[]> {
    return this.istanzeFunzionalitaFormLogiciService.getTipologieIstanzeFunzionalita().pipe(
      map(response => {
        return (response ?? [])
        .filter(i => !term?.trim()?.length
         || (((i.codice ?? '') + (i.descrizione ?? '')).toUpperCase().indexOf(term.trim().toUpperCase()) !== -1))
        .map(i => {
          return { entity: this.clean(i) };
        });
      })
    );
  }

  enrich(entry: SelezioneEntity<TipologiaFunzionalitaFormLogico>): Observable<SelezioneEntity<TipologiaFunzionalitaFormLogico> | null> {
    if (!entry?.entity?.codice) {
      return of(null);
    }
    return this.istanzeFunzionalitaFormLogiciService.getTipologieIstanzeFunzionalita().pipe(map(i => {
      return { entity: i.find(c => c.codice === entry?.entity?.codice) };
    }));
  }
}
