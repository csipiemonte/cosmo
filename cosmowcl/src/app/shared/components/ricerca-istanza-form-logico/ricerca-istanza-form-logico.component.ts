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
import { IstanzaFunzionalitaFormLogico } from '../../models/api/cosmobusiness/istanzaFunzionalitaFormLogico';
import { GestioneFormLogiciService } from '../../services/gestione-form-logici.service';

@Component({
  selector: 'app-ricerca-istanza-form-logico',
  templateUrl: '../ricerca-entity/ricerca-entity.component.html',
  styleUrls: ['../ricerca-entity/ricerca-entity.component.scss'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => RicercaIstanzaFormLogicoComponent),
    multi: true
  }]
})
export class RicercaIstanzaFormLogicoComponent extends RicercaEntityComponent<IstanzaFunzionalitaFormLogico> {

  constructor(
    private logger: NGXLogger,
    private service: GestioneFormLogiciService,
  ) {
    super();
    this.logger.trace('building component');
  }

  getEntityDescriptionSingular(): string {
    return 'istanza';
  }

  format(input: SelezioneEntity<IstanzaFunzionalitaFormLogico>): string {
    return input?.entity?.descrizione ?? '--';
  }

  getIcon(option: SelezioneEntity<IstanzaFunzionalitaFormLogico>): string | null {
    return 'fas fa-object-ungroup';
  }

  isEnriched(entry: SelezioneEntity<IstanzaFunzionalitaFormLogico>): boolean {
    return !!(entry?.entity?.descrizione);
  }

  clean(entity: IstanzaFunzionalitaFormLogico): Partial<IstanzaFunzionalitaFormLogico> {
    return {
      ...entity,
      parametri: []
    };
  }

  search(term: string | null): Observable<SelezioneEntity<IstanzaFunzionalitaFormLogico>[]> {
    if (!term?.length) {
      return of([]);
    }
    const request: any = {
      page: 0,
      size: 5,
      filter: {},
      sort: 'descrizione,codice'
    };
    if (term?.length) {
      request.filter.descrizione = {ci : term};
    }
    return this.service.getIstanze(JSON.stringify(request)).pipe(
      map(response => {
        return (response.istanze ?? []).map(i => {
          return { entity: this.clean(i) };
        });
      })
    );
  }

  enrich(entry: SelezioneEntity<IstanzaFunzionalitaFormLogico>): Observable<SelezioneEntity<IstanzaFunzionalitaFormLogico> | null> {
    if (!entry?.entity?.id) {
      return of(null);
    }
    return this.service.getIstanza(entry.entity.id).pipe(map(i => {
      return { entity: i };
    }));
  }
}
