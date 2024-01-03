/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, ReplaySubject } from 'rxjs';
import { ApiUrls } from '../utilities/apiurls';
import { NGXLogger } from 'ngx-logger';
import { Preferenza } from '../models/api/cosmoauthorization/preferenza';
import { ModalService } from './modal.service';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
  providedIn: 'root'
})
export class PreferenzeUtenteService {
  private preferenzeSalvate!: Preferenza;
  private preferenzeUtenteSubject = new ReplaySubject<Preferenza>(1);


  constructor(
    private logger: NGXLogger,
    private http: HttpClient,
    private modalService: ModalService,
    private translateService: TranslateService) {
    this.logger.debug('Creo preferenze utente service');
  }

  get subscribePreferenze() {
    this.setPreferenze();
    return this.preferenzeUtenteSubject.asObservable();
  }

  setPreferenze() {
    if (!this.preferenzeSalvate) {
      this.getPreferenze().subscribe(preferenze => {
        this.preferenzeSalvate = preferenze;
        this.preferenzeUtenteSubject.next(this.preferenzeSalvate);

      });
    }
  }

  getPreferenzeSalvate(): Preferenza {
    return this.preferenzeSalvate;
  }

  salva(preferenzeUtente: Preferenza) {
    this.salvaPreferenze(preferenzeUtente).subscribe(
      preferenze => {
        this.preferenzeSalvate = preferenze;
        this.preferenzeUtenteSubject.next(this.preferenzeSalvate);

        this.modaleSalvataggioOK();
      }
    );
  }

  salvaSenzaModale(preferenzeUtente: Preferenza) {
    this.salvaPreferenze(preferenzeUtente).subscribe(
      preferenze => {
        this.preferenzeSalvate = preferenze;
        this.preferenzeUtenteSubject.next(this.preferenzeSalvate);
      }
    );
  }

  aggiorna(preferenzeUtente: Preferenza) {
    this.aggiornaPreferenze(preferenzeUtente).subscribe(
      preferenze => {
        this.preferenzeSalvate = preferenze;
        this.preferenzeUtenteSubject.next(this.preferenzeSalvate);

        this.modaleSalvataggioOK();
      }
    );
  }

  aggiornaSenzaModale(preferenzeUtente: Preferenza) {
    this.aggiornaPreferenze(preferenzeUtente).subscribe(
      preferenze => {
        this.preferenzeSalvate = preferenze;
        this.preferenzeUtenteSubject.next(this.preferenzeSalvate);
      }
    );
  }

  private modaleSalvataggioOK(){
    this.modalService.info(
      this.translateService.instant('preferenze.salvataggio_preferenze_utente'),
      this.translateService.instant('preferenze.preferenze_salvataggio_ok'))
      .then(() => {})
      .catch(() => { });
  }

  /** GET: Restituisce le preferenze dell'utente. */
  getPreferenze(): Observable<Preferenza> {
    return this.http.get<Preferenza>(ApiUrls.PREFERENZE_UTENTE);
  }

  /**
   * POST: Salva le preferenze dell'utente.
   *
   * @param preferenze preferenze da salvare.
   * @returns Observable<PreferenzeUtente> le preferenze salvate.
   */

  salvaPreferenze(preferenze: Preferenza): Observable<Preferenza> {
    return this.http.post<Preferenza>(ApiUrls.PREFERENZE_UTENTE, preferenze);
  }

  /**
   * PUT: Aggiorna le preferenze dell'utente.
   *
   * @param preferenze preferenze da aggiornare.
   * @returns Observable<PreferenzeUtente> le preferenze aggiornate.
   */

  aggiornaPreferenze(preferenze: Preferenza): Observable<Preferenza> {
    return this.http.put<Preferenza>(ApiUrls.PREFERENZE_UTENTE, preferenze);
  }
}
