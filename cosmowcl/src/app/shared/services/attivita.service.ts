/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AssegnaAttivitaRequest } from '../models/api/cosmobusiness/assegnaAttivitaRequest';
import { ApiUrls } from '../utilities/apiurls';
import { Utils } from '../utilities/utilities';

@Injectable()
export class AttivitaService {

  constructor(
    private http: HttpClient
  ) {
    // NOP
  }

  assegnaAttivita(idPratica: number, idAttivita: number, idUtente?: number, idGruppo?: number, keepAssignement?: boolean) {
    Utils.require(idPratica, 'idPratica');
    Utils.require(idAttivita, 'idAttivita');
    Utils.require(idUtente ?? idGruppo, 'idUtente o idGruppo');

    const request: AssegnaAttivitaRequest = {
      esclusivo: true,
      mantieniAssegnazione: keepAssignement,
      assegnazioni: [
        idGruppo ? { idGruppo, tipologia: 'candidate' } :
          { idUtente, tipologia: 'assignee' }
      ]
    };

    return this.http.post(
      ApiUrls.POST_PRATICA_ASSEGNA_TASK
        .replace('{idPratica}', '' + idPratica )
        .replace('{idAttivita}', '' + idAttivita ),
      request
    );
  }

  assegnaAttivitaAMe(idPratica: number, idAttivita: number) {
    Utils.require(idPratica, 'idPratica');
    Utils.require(idAttivita, 'idAttivita');

    return this.http.post(
      ApiUrls.POST_PRATICA_ASSEGNA_TASK_ME
        .replace('{idPratica}', '' + idPratica )
        .replace('{idAttivita}', '' + idAttivita ),
      {}
    );
  }
}
