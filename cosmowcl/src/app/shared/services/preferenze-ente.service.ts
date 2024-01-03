/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ApiUrls } from '../utilities/apiurls';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { Observable, ReplaySubject } from 'rxjs';
import { ConfigurazioniService } from 'src/app/shared/services/configurazioni.service';
import { Constants } from 'src/app/shared/constants/constants';
import { SecurityService } from 'src/app/shared/services/security.service';
import { Preferenza } from '../models/api/cosmoauthorization/preferenza';
import { share, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})

export class PreferenzeEnteService {
  private preferenzeSalvate!: Preferenza;
  private idEnteSalvato!: number;
  private versioneSalvata!: string;
  private preferenzeEnteSubject = new ReplaySubject<Preferenza>(1);

  private getPreferenzeObservable?: Observable<Preferenza>;

  constructor(private logger: NGXLogger,
              private http: HttpClient,
              private configurazioniService: ConfigurazioniService,
              private securityService: SecurityService) {
    this.logger.debug('Creo preferenze ente service');
  }

  get subscribePreferenze() {
    this.setPreferenze( );
    return this.preferenzeEnteSubject.asObservable();
  }

  setPreferenze() {
    if (!this.preferenzeSalvate) {
      this.securityService.principal$.subscribe(newPrincipal => {
        this.configurazioniService.getConfigurazioneByChiave(Constants.PARAMETRI.PREFERENZE_ENTE.VERSIONE).subscribe(parameter => {
          if (parameter && newPrincipal && newPrincipal.ente) {
            this.idEnteSalvato = newPrincipal.ente?.id ?? 0;
            this.versioneSalvata = parameter ?? '';
            this.getPreferenze(this.idEnteSalvato, this.versioneSalvata).subscribe(preferenze => {
              this.preferenzeSalvate = preferenze;
              this.preferenzeEnteSubject.next(this.preferenzeSalvate);
            });
          }
        });
      });
    }
  }

  salva(preferenzeEnte: Preferenza) {
    this.salvaPreferenze(this.idEnteSalvato, preferenzeEnte).subscribe(
      preferenze => {
        this.preferenzeSalvate = preferenze;
        this.preferenzeEnteSubject.next(this.preferenzeSalvate);
      }
    );
  }

  aggiorna(preferenzeEnte: Preferenza) {
    this.aggiornaPreferenze(this.idEnteSalvato, preferenzeEnte).subscribe(
      preferenze => {
        this.preferenzeSalvate = preferenze;
        this.preferenzeEnteSubject.next(this.preferenzeSalvate);
      }
    );
  }

  getPreferenzeSalvate(): Preferenza {
    return this.preferenzeSalvate;
  }

  /** GET: Restituisce le preferenze dell'ente. */
  getPreferenze(idEnte: number, versione: string): Observable<Preferenza> {
    if (this.getPreferenzeObservable) {
      return this.getPreferenzeObservable;
    }

    let params: HttpParams = new HttpParams();
    params = params.set('idEnte', idEnte.toString());
    params = params.set('versione', versione);

    this.getPreferenzeObservable = this.http.get<Preferenza>(ApiUrls.PREFERENZE_ENTE, {params}).pipe(
      tap(() => this.getPreferenzeObservable = undefined),
      share()
    );

    return this.getPreferenzeObservable;
  }

  /**
   * POST: Salva le preferenze dell'ente.
   *
   * @param preferenze preferenze da salvare.
   * @returns Observable<PreferenzeEnte> le preferenze salvate.
   */

  salvaPreferenze(idEnte: number, preferenze: Preferenza): Observable<Preferenza> {
    let params: HttpParams = new HttpParams();
    params = params.set('idEnte', idEnte.toString());
    preferenze.versione = this.versioneSalvata;
    return this.http.post<Preferenza>(ApiUrls.PREFERENZE_ENTE, preferenze, {params});
  }

  /**
   * PUT: Aggiorna le preferenze dell'ente.
   *
   * @param preferenze preferenze da aggiornare.
   * @returns Observable<PreferenzeEnte> le preferenze aggiornate.
   */

  aggiornaPreferenze(idEnte: number, preferenze: Preferenza): Observable<Preferenza> {
    let params: HttpParams = new HttpParams();
    params = params.set('idEnte', idEnte.toString());
    return this.http.put<Preferenza>(ApiUrls.PREFERENZE_ENTE, preferenze, {params});
  }

}
