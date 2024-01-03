/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Injectable } from '@angular/core';

import { NGXLogger } from 'ngx-logger';
import {
  ActiveToast,
  IndividualConfig,
  ToastrService,
} from 'ngx-toastr';
import {
  Observable,
  Subject,
} from 'rxjs';
import { filter } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

import { Constants } from '../constants/constants';
import { NotificationEvent } from '../models/notifications/notification-event';
import {
  NotificationPayload,
} from '../models/notifications/notification-payload';
import { BusService } from './bus.service';
import { NotificheService } from './notifiche.service';
import { PreferenzeUtenteService } from './preferenze-utente.service';
import { WebsocketService } from './proto/websocket.proto';
import { SecurityService } from './security.service';

@Injectable()
export class NotificationsWebsocketService extends WebsocketService<NotificationPayload, string> {

    private eventSubject: Subject<NotificationEvent>;

    deployContextPath = environment.deployContextPath;

    constructor(
        private logger: NGXLogger,
        private securityService: SecurityService,
        private toastrService: ToastrService,
        private notificheService: NotificheService,
        private busService: BusService,
        private preferenzeUtenteService: PreferenzeUtenteService,
    ) {
        super('notifications', logger, securityService, preferenzeUtenteService);
        this.eventSubject = new Subject<NotificationEvent>();
    }

    public whenEvent(event: string): Observable<NotificationEvent> {
        return this.eventSubject.pipe(filter(
            e => e.event === event
        ));
    }

    public whenEvents(events: string[]): Observable<NotificationEvent> {
        return this.eventSubject.pipe(filter(
            e => events.indexOf(e.event) !== -1
        ));
    }

    public onEvent(event: string, payload: any): void {
        this.logger.debug(this.prefix +
            'notifications websocket received event ' + event + ' with payload', payload);
        const message: NotificationEvent = {
            event, payload
        };
        this.eventSubject.next(message);
    }

    onMessage(payload: NotificationPayload): void {
        this.logger.debug(this.prefix +
            'GOT MESSAGE FROM Notifications WEBSOCKET', payload);

        const event = Constants.APPLICATION_EVENTS.NOTIFICA;
        const message: NotificationEvent = {
            event, payload
        };
        this.eventSubject.next(message);

        this.dispatchByType(payload);
    }

    public notificheDiSistemaSupportate(): boolean {
        if (!environment.enableSystemNotifications) {
            return false;
        }
        return !!('Notification' in window);
    }

    public notificheDiSistemaAbilitate(): boolean {
        if (!this.notificheDiSistemaSupportate()) {
            return false;
        }
        return Notification.permission === 'granted';
    }

    public notificheDiSistemaNegate(): boolean {
        if (!this.notificheDiSistemaSupportate()) {
            return false;
        }
        return Notification.permission === 'denied';
    }

    public notificheDiSistemaNonChieste(): boolean {
        if (!this.notificheDiSistemaSupportate()) {
            return false;
        }
        return Notification.permission !== 'granted' && Notification.permission !== 'denied';
    }

    public promptSystemNotificationsGrant(): Promise<boolean> {
        return Notification.requestPermission().then(p => {
            if (p !== 'granted') {
                this.logger.warn('permesso di notifica di sistema negato', p);
                return false;
            } else {
                const notificationInitial = new Notification('COSMO - Notifiche abilitate', {
                    ...this.getDefaultSystemNotificationOptions(),
                    silent: false,
                    body: 'Ti mostreremo le notifiche importanti di Cosmo in questo modo.'
                });
                this.logger.debug('mostrata notifica di sistema ' + notificationInitial.title);

                return true;
            }
        }, failure => {
            this.logger.warn('permesso di notifica di sistema fallito', failure);
            return false;
        });
    }

    dispatchByType(payload: NotificationPayload): void {

        if (this.cosmoIsVisible()) {
            // cosmo e' visibile e in primo piano

            if (this.notificheDiSistemaSupportate() && this.notificheDiSistemaNonChieste()) {
                // se non lo abbiamo ancora fatto
                // chiediamo l'autorizzazione a mostrare le notifiche di sistema
                // (per quelle future)
                this.promptSystemNotificationsGrant();

                // l'autorizzazione varra' per le prossime, questa la mostriamo
                // per forza come toast
                this.dispatchByTypeToToast(payload);

            } else if (this.notificheDiSistemaSupportate() && this.notificheDiSistemaAbilitate()) {
                // l'utente ha autorizzato le notifiche di sistema
                // ma cosmo e' visibile in primo piano.
                // tutti e due i tipi di notifiche vanno bene qui
                // ma il toast e' meno invasivo

                // this.dispatchByTypeToSystemNotification(payload);
                this.dispatchByTypeToToast(payload);

            } else {
                // le notifiche di sistema non sono abilitate
                // ma cosmo e' in primo piano
                // quindi il toast va benissimo
                this.dispatchByTypeToToast(payload);
            }

            return;
        }

        // cosmo non e' visibile o in primo piano
        if (!this.notificheDiSistemaAbilitate()) {
            this.logger.debug('notifica di sistema non supportata o non permessa');
            // possiamo solo mostrare il toast e sperare che l'utente torni sulla pagina
            this.dispatchByTypeToToast(payload);
            return;
        }

        // possiamo mostrare notifica di sistema
        this.dispatchByTypeToSystemNotification(payload);
    }

    private cosmoIsVisible(): boolean | null {
        if (typeof document.hidden !== 'undefined') {
            return !document.hidden;
        }
        return null;
    }

    private dispatchByTypeToToast(payload: NotificationPayload): void {
        let message = payload.message;
        const title = payload.title;
        const options: Partial<IndividualConfig> = {
            timeOut: 10000,
            disableTimeOut: false,
            tapToDismiss: true,
            positionClass: this.getPositition(),
            enableHtml: true,
            ...payload.options
        };
        if (payload.urlDescription) {
          message = message + '</br></br><strong>' + payload.urlDescription + '</strong>';
        }
        if (payload.url) {
          message = message + '</br><em>' + payload.url + '</em>';
        }

        let toast: ActiveToast<any> | null = null;
        if (payload.type === 'ERROR' || payload.type === 'DANGER') {
            toast = this.toastrService.error(message, title, options);
        } else if (payload.type === 'WARNING') {
            toast = this.toastrService.warning(message, title, options);
        } else if (payload.type === 'SUCCESS') {
            toast = this.toastrService.success(message, title, options);
        } else {
            toast = this.toastrService.info(message, title, options);
        }
        if (payload?.notificationId && !!toast) {
            toast.onTap.subscribe(() => this.notificationClicked(payload));
        }
    }

    private getDefaultSystemNotificationOptions(payload?: NotificationPayload): NotificationOptions {
        let img = this.deployContextPath + '/assets/cwwcl/i/notifications/logo-notifications.png';
        if (payload?.type === 'ERROR' || payload?.type === 'DANGER') {
            img = this.deployContextPath + '/assets/cwwcl/i/notifications/logo-notifications-error.png';
        } else if (payload?.type === 'WARNING') {
            img = this.deployContextPath + '/assets/cwwcl/i/notifications/logo-notifications-warning.png';
        }

        return {
            silent: true,
            icon: img,
            badge: img,
        };
    }

    private dispatchByTypeToSystemNotification(payload: NotificationPayload): void {
        const notification = new Notification('COSMO - ' + (payload.title ?? 'notifica'), {
            ...this.getDefaultSystemNotificationOptions(payload),
            tag: 'notification-' + (payload.notificationId ?? Math.random()),
            body: payload.message,
        });
        this.logger.debug('mostrata notifica di sistema ' + notification.title);

        notification.onclick = (event) => {
            event.preventDefault();

            if (!this.cosmoIsVisible()) {
                parent.focus(); // works on newer browsers
                window.focus(); // just in case, older browsers
            }
            notification.close();

            this.notificationClicked(payload);
        };
    }

    notificationClicked(payload: NotificationPayload) {
        this.logger.info('click on notification', payload.notificationId);

        if (!payload.notificationId) {
            return;
        }
        this.notificheService.markAsRead(payload.notificationId).then(() => {
            this.busService.setCercaNotifiche(true);
        });
    }


    private getPositition(): string {
      let positionClass = '';

      switch (this.toastPosition) {
        case 'TOP-LEFT':
          positionClass = 'toast-top-left';
          break;
        case 'TOP-RIGHT':
          positionClass = 'toast-top-right';
          break;
        case 'BOTTOM-LEFT':
          positionClass = 'toast-bottom-left';
          break;
        case 'BOTTOM-RIGHT':
          positionClass = 'toast-bottom-right';
          break;
        default:
          positionClass = 'toast-top-right';
          break;
      }
      return positionClass;
    }

}
