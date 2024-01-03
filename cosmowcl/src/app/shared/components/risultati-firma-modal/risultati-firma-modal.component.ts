/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  Component,
  OnInit,
} from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';
import { NotificationsWebsocketService } from '../../services/notifications-websocket.service';
import { Constants } from '../../constants/constants';
import { debounceTime } from 'rxjs/operators';
import { AttivitaEseguibileMassivamente } from '../../models/api/cosmopratiche/attivitaEseguibileMassivamente';

export interface RisultatiFirmaOptions {
  showMessages?: boolean;
  layout?: 'default' | 'minimal';
  totaleDocumenti: number;
  ids?: any;
  tasks: AttivitaEseguibileMassivamente[];
}

export interface RisFirmaPayload {
  oggettoPratica: string;
  idPratica: number;
  descrizioneAttivita: string;
  idAttivita: number;
  numeroDocumento: number;
  nomeDocumento: string;
  stato: string;
  id: number;
  idDocumento: number;
  descrizioneDocumento: string;
}

export interface DocumentiPayload {
  descrizioneDocumento: string;
  stato: string;
}

export interface PratichePayload {
  idPratica: number;
  oggettoPratica: string;
  stato: string;
}


@Component({
  selector: 'app-risultati-firma-modal',
  templateUrl: './risultati-firma-modal.component.html',
  styles: ['./risultati-firma-modal.component.scss']
})
export class RisultatiFirmaModalComponent implements OnInit {
  private notificationSubcritpion: Subscription | null = null;
  private notificationStartedSubcritpion: Subscription | null = null;
  options: RisultatiFirmaOptions = {totaleDocumenti : 0, tasks: []};

  started = false;
  finished = false;
  error = false;
  numeroPraticheProcessate = 0;
  numeroDocumentiProcessati = 0;
  percentualeFirma = 0;
  titoloDocumento?: string;
  webSocketDocumenti: Array<RisFirmaPayload> = [];
  webSocketPratiche: Array<PratichePayload> = [];
  tasks: AttivitaEseguibileMassivamente[] = [];
  documentiPratica: Array<DocumentiPayload> = [];
  erroriFirma: Array<string> = [];

  icon: string;

  POSSIBLE_ICONS = [
    'fas fa-user-astronaut',
    'fas fa-meteor',
    'far fa-moon',
    'fas fa-robot',
    'fas fa-rocket',
    'fas fa-satellite',
    'fas fa-satellite-dish',
    'fas fa-globe-europe',
  ];

  constructor(
    private logger: NGXLogger,
    public activeModal: NgbActiveModal,
    private notificationsWebsocketService: NotificationsWebsocketService,
  ) {
    this.icon = this.POSSIBLE_ICONS[Math.floor(Math.random() * this.POSSIBLE_ICONS.length)];
  }


  get activeLayout(): 'default' | 'minimal' {
    return this.options?.layout ?? 'default';
  }

  get isDefaultLayout(): boolean {
    return this.activeLayout === 'default';
  }

  get isMinimalLayout(): boolean {
    return this.activeLayout === 'minimal';
  }

  get showMessages(): boolean {
    return this.options?.showMessages ?? true;
  }

  get autoCloseDelaySuccess(): number {
    if (this.isDefaultLayout) {
      return 2000;
    } else {
      return 100;
    }
  }

  get autoCloseDelayFailure(): number {
    if (this.isDefaultLayout) {
      return 2500;
    } else {
      return 100;
    }
  }

  ngOnInit(): void {
    const eventiGestiti = [
      Constants.APPLICATION_EVENTS.PRATICA_INIZIO_FIRMA,
      Constants.APPLICATION_EVENTS.PRATICA_FINE_FIRMA,
      Constants.APPLICATION_EVENTS.DOCUMENTI_INIZIO_FIRMA,
      Constants.APPLICATION_EVENTS.DOCUMENTI_FIRMATI,
      Constants.APPLICATION_EVENTS.PRATICA_ERRORI_FIRMA,
      Constants.APPLICATION_EVENTS.FIRMA_ERRORI_PRELIMINARI,
      Constants.APPLICATION_EVENTS.DOCUMENTI_FINE_FIRMA,
      Constants.APPLICATION_EVENTS.FIRMA_ERRORE_DOCUMENTO
    ];

    this.notificationSubcritpion = this.notificationsWebsocketService
    .whenEvents(eventiGestiti)
    .subscribe( evento => {
      this.started = true;
      switch (evento.event) {
        case Constants.APPLICATION_EVENTS.PRATICA_INIZIO_FIRMA: {
          this.webSocketPratiche.push(evento.payload);
          setTimeout(() => {}, 3000);
          break;
        }
        case Constants.APPLICATION_EVENTS.PRATICA_FINE_FIRMA: {
          this.webSocketPratiche[this.numeroPraticheProcessate].stato = evento.payload.stato;

          ++this.numeroPraticheProcessate;
          if (this.numeroPraticheProcessate === this.options.tasks.length) {
            this.closeModal();
          }
          break;
        }
        case Constants.APPLICATION_EVENTS.PRATICA_ERRORI_FIRMA: {
          this.error = true;
          const descrizione = evento.payload.errore.length > 0 ? evento.payload.errore : 'errore generico';
          this.erroriFirma.push(descrizione);
          this.webSocketPratiche[this.numeroPraticheProcessate].stato = evento.payload.stato;
          ++this.numeroPraticheProcessate;
          if (this.numeroPraticheProcessate === this.options.tasks.length) {
            this.closeModal();
          }
          break;
        }

        case Constants.APPLICATION_EVENTS.FIRMA_ERRORI_PRELIMINARI: {
          this.error = true;
          const descrizione = evento.payload.errore.length > 0 ? evento.payload.errore : 'errore generico';
          this.erroriFirma.push(descrizione);
          this.closeModal();
          break;
        }
        case Constants.APPLICATION_EVENTS.DOCUMENTI_INIZIO_FIRMA: {
          this.webSocketDocumenti.push(evento.payload);
          setTimeout(() => {}, 3000);
          break;
        }
        case Constants.APPLICATION_EVENTS.DOCUMENTI_FIRMATI: {
          this.webSocketDocumenti[this.numeroDocumentiProcessati].stato = evento.payload.stato;
          ++this.numeroDocumentiProcessati;
          setTimeout(() => {}, 3000);
          break;
        }
        case Constants.APPLICATION_EVENTS.DOCUMENTI_FINE_FIRMA: {
          this.closeModal();
          break;
        }

        case Constants.APPLICATION_EVENTS.FIRMA_ERRORE_DOCUMENTO: {
          const descrizione = evento.payload.errore.length > 0 ? evento.payload.errore : 'errore generico';
          this.erroriFirma.push(descrizione);
          this.closeModal();
          break;
        }
      }
    });
  }



  cancel() {
    this.logger.info('cancel signal received');
  }

  public initialize(options: RisultatiFirmaOptions) {
    this.options = options;
  }

  private closeModal() {
    this.finished = true;
    setTimeout(() => {
      this.activeModal.close(this.erroriFirma);
    }, 3000);
  }

}
