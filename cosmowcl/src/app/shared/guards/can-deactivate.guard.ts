/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { CanDeactivate } from '@angular/router';
import { Injectable } from '@angular/core';
import { from, Observable } from 'rxjs';
import { ModalService } from '../services/modal.service';
import { TranslateService } from '@ngx-translate/core';

export interface ComponentCanDeactivate {
  canDeactivate: () => boolean | Observable<boolean>;
}

@Injectable()
export class PendingChangesGuard implements CanDeactivate<ComponentCanDeactivate> {
    public constructor(
        private modalService: ModalService,
        private translateService: TranslateService
    ) {
        // NOP
    }

  canDeactivate(component: ComponentCanDeactivate): boolean | Observable<boolean> {
    // if there are no pending changes, just allow deactivation; else confirm first
    if (!component.canDeactivate || component.canDeactivate()) {
        return true;
    }

    return from(this.modalService.scegli(
        this.translateService.instant('common.dati_non_salvati'),
        [{
            testo: 'ATTENZIONE: ci sono modifiche non salvate.', classe: 'text-warning'
        }, {
            testo: 'Premi \'Annulla\' per annullare la navigazione e non perdere i dati oppure \'Procedi\' per uscire e perdere i dati inseriti.'
        }], [{
            testo: 'Procedi', valore: true, classe: 'btn-outline-warning', icona: 'fas fa-exclamation-triangle'
        }, {
            testo: 'Annulla', dismiss: true, defaultFocus: true
        }]
      ).then(() => {
        return true;
      }).catch(() => {
        return false;
    }));

    // NOTE: this warning message will only be shown when navigating elsewhere within your angular app;
    // when navigating away from your angular app, the browser will show a generic warning message
    // see http://stackoverflow.com/a/42207299/7307355
    // confirm('ATTENZIONE: ci sono modifiche non salvate. Premi \'Annulla\' per annullare la navigazione e non perdere i dati, ' +
    //  'oppure OK per uscire e perdere i dati inseriti.');
  }
}
