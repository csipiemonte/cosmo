/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NGXLogger } from 'ngx-logger';
import { timer, Observable } from 'rxjs';
import { WebSocketSubject, webSocket } from 'rxjs/webSocket';
import { finalize, share, tap } from 'rxjs/operators';
import { SecurityService } from '../security.service';
import { ApiUrls } from '../../utilities/apiurls';
import { environment } from 'src/environments/environment';
import { UserInfoWrapper } from '../../models/user/user-info';
import { PreferenzeUtenteService } from '../preferenze-utente.service';
import { ValorePreferenzeUtente } from '../../models/preferenze/valore-preferenze-utente.model';

export abstract class WebsocketService<I, O> {

  protected prefix = '[WEBSOCKET ' + this.channel.toUpperCase() + '] ';
  private connectionIndex = 0;
  private lastConnectionAttempt: Observable<WebSocketSubject<any>> | null = null;

  private principal: UserInfoWrapper | null = null;
  private principalHash: string | null = null;

  private webSocket: WebSocketSubject<any> | null = null;

  private lastOperation: Date = new Date();
  private lastMessage: Date = new Date();

  protected toastPosition: string | undefined = undefined;

  constructor(
    private channel: string,
    private baseLogger: NGXLogger,
    private baseSecurityService: SecurityService,
    private basePreferenzeUtenteService: PreferenzeUtenteService
  ) {
    if (!environment.enableWebSockets) {
      return;
    }
    this.baseLogger.trace(this.prefix + 'creo websocket service');

    this.baseSecurityService.principal$.subscribe(newPrincipal => {
      this.principal = newPrincipal;
      const newPrincipalHash = newPrincipal?.hashIdentita || null;
      if (newPrincipalHash !== this.principalHash) {
        this.principalHashChanged(newPrincipalHash, !!newPrincipal?.profilo?.id);
      }
    });

    this.basePreferenzeUtenteService.subscribePreferenze.subscribe(preferenze => {
      if (preferenze && preferenze.valore) {
        const valorePreferenzeUtente = JSON.parse(preferenze.valore) as ValorePreferenzeUtente;
        this.toastPosition = valorePreferenzeUtente.posizioneToast;
      }
    });

    timer(5000, 5000).subscribe(() => this.periodicCheck());

    timer(5000, 15000).subscribe(() => this.periodicKeepAlive());
  }

  private periodicKeepAlive(): void {
    if (!this.webSocket) {
      return;
    }

    const payload: any = 'keep-alive from client';
    this.sendEvent('KEEP_ALIVE', payload);
  }

  private periodicCheck(): void {
    const sslo = this.secondsSinceLastOperation();
    if (sslo < 5) {
      this.baseLogger.trace(this.prefix + 'skipping periodic check because of recent opeation on socket ('
        + sslo + ' seconds ago)');
      return;
    }
    this.baseLogger.trace(this.prefix + 'running periodic check of connection status');
    const shouldHaveSocket = !!this.principal?.profilo?.id;
    const haveSocket = !!this.webSocket;
    const sslm = this.secondsSinceLastMessage();
    const probablyExpired = shouldHaveSocket && haveSocket && sslm >= 20;
    if (shouldHaveSocket) {
      this.baseLogger.trace(this.prefix + sslm + ' seconds since last message received');
    }

    if (shouldHaveSocket && !haveSocket) {
      this.baseLogger.warn(this.prefix + 'should have an opened websocket but have none. Attempting connection now');
      this.reconnect();
    } else if (!shouldHaveSocket && haveSocket) {
      this.baseLogger.warn(this.prefix + 'should not have an opened websocket but have one. Closing connection now');
      this.close();
    } else if (shouldHaveSocket && haveSocket && probablyExpired) {
      this.baseLogger.warn(this.prefix + 'should have an opened websocket but the current one is probably faulty. Attempting reconnection now');
      this.reconnect();
    } else {
      this.baseLogger.trace(this.prefix + 'no problems detected with connection');
    }
  }

  private principalHashChanged(newPrincipalHash: string | null, doConnect: boolean): void {
    this.principalHash = newPrincipalHash;

    this.baseLogger.debug(this.prefix + 'principal hash changed, resetting websocket connection');

    if (doConnect && newPrincipalHash) {
      this.baseLogger.debug(this.prefix + 'reconnecting');
      this.reconnect();
    } else {
      this.baseLogger.debug(this.prefix + 'dropping connection');
      this.close();
    }
  }

  reconnect(): void {
    this.close();
    this.connect();
  }

  connect(): void {
    const ep = ApiUrls.WEBSOCKET_ROOT + this.channel;
    this.lastOperation = new Date();

    this.baseLogger.debug(this.prefix + 'attempting websocket connection to ' + ep);

    this.connectionAttempt().subscribe(websocket => {
      this.baseLogger.debug(this.prefix + 'connection attempt succeeded!');
    }, connectionFailed => {
      this.baseLogger.error(this.prefix + 'connection attempt failed!', connectionFailed);
    });
  }

  connectionAttempt(): Observable<WebSocketSubject<any>> {

    const ep = this.checkWSUrlMultiHost(ApiUrls.WEBSOCKET_ROOT + this.channel);
    this.baseLogger.debug(this.prefix + 'attempting websocket connection to ' + ep);

    const prefix = this.prefix + '[connection attempt #' + (this.connectionIndex++) + '] ';

    if (this.lastConnectionAttempt) {
      this.baseLogger.trace(prefix + 'returning current connection attempt (already in progress)');
      return this.lastConnectionAttempt;
    }

    this.lastConnectionAttempt = new Observable<WebSocketSubject<any>>(emitter => {
      this.baseLogger.debug(prefix + 'launching new connection attempt');
      const websocketInstance = webSocket(ep);
      let validated = false;
      let terminated = false;

      const subscriptionAttempt = websocketInstance.asObservable().subscribe(dataFromServer => {
        this.baseLogger.debug(prefix + '<== ' + JSON.stringify(dataFromServer));
        const data: any = dataFromServer as any;

        if (!validated && !terminated) {
          if (data?.messageType === 'EVENT' && data?.event === 'CONNECTION_OPENED') {
            validated = true;
            terminated = true;
            emitter.next(websocketInstance);
            emitter.complete();
            setTimeout(() => {
              subscriptionAttempt.unsubscribe();
            }, 500);
          } else {
            this.baseLogger.warn(prefix + 'received unexpected message from connection opening, will be ignored');
          }
        } else {
          this.baseLogger.trace(prefix + 'ignoring message callback after connection attempt terminated');
        }
      }, err => {

        this.baseLogger.error(prefix + 'error in connection attempt: ' + JSON.stringify(err));
        if (!validated && !terminated) {
          emitter.error(err);
          emitter.complete();
        } else {
          this.baseLogger.trace(prefix + 'ignoring error callback #1 after connection attempt terminated');
        }

        if (!terminated) {
          terminated = true;
          subscriptionAttempt.unsubscribe();
        } else {
          this.baseLogger.trace(prefix + 'ignoring error callback #2 after connection attempt terminated');
        }
      }, () => {
        if (!validated) {
          const msg = 'connection closed unexpectedly before validation';
          this.baseLogger.error(prefix + msg);
          emitter.error(new Error(msg));
          emitter.complete();
        } else if (!terminated) {
          this.baseLogger.debug(prefix + 'connection attempt terminated');
          terminated = true;
          subscriptionAttempt.unsubscribe();
          emitter.complete();
        } else {
          this.baseLogger.trace(prefix + 'ignoring completion callback after connection attempt terminated');
        }
      });

    }).pipe(
      tap(websocket => {
        this.baseLogger.debug(prefix + 'VALIDATING WS CONNECTION ATTEMPT');
        this.webSocket = websocket;
        websocket.asObservable().subscribe((dfs: any) => {
          this.baseLogger.debug(this.prefix + '<== ' + JSON.stringify(dfs));
          this.lastMessage = new Date();
          this.dispatchMessage(dfs);
        }, (errfs: any) => {
          this.baseLogger.error(this.prefix + 'ERROR: ' + JSON.stringify(errfs));
        }, () => {
          this.baseLogger.debug(this.prefix + 'CONNECTION CLOSED');
          this.closedFromServer();
        });
      }),
      finalize(() => this.lastConnectionAttempt = null),
      share()
    );

    return this.lastConnectionAttempt;
  }

  close(): void {
    if (this.webSocket) {
      this.lastOperation = new Date();
      this.baseLogger.debug(this.prefix + 'CLOSING CONNECTION FROM CLIENT');
      this.webSocket.complete();
      this.webSocket = null;
    } else {
      this.baseLogger.trace(this.prefix + 'closing from client skipped because socket is already gone');
    }
  }

  closedFromServer(): void {
    if (this.webSocket) {
      this.lastOperation = new Date();
      this.baseLogger.debug(this.prefix + 'CONNECTION CLOSED FROM SERVER');
      this.webSocket.unsubscribe();
      this.webSocket = null;
    } else {
      this.baseLogger.trace(this.prefix + 'closing from server skipped because socket is already gone');
    }
  }

  private secondsSinceLastOperation(): number {
    return (new Date().getTime() - this.lastOperation.getTime()) / 1000;
  }

  private secondsSinceLastMessage(): number {
    return (new Date().getTime() - this.lastMessage.getTime()) / 1000;
  }

  private dispatchMessage(message: any): void {
    if (message?.messageType === 'EVENT') {
      // is event
      const eventCode = message?.event;
      if (eventCode === 'KEEP_ALIVE') {
        // suppress
        this.baseLogger.trace(this.prefix + 'keep-alive event will not be forwarded');
      } else {
        this.onEvent(eventCode, message?.payload);
      }
    } else if (message?.messageType === 'RESPONSE') {
      // is message
      this.onMessage(message.response as I, message?.code, message?.status);
    } else {
      this.baseLogger.error(this.prefix + 'received invalid messageType', message?.messageType);
    }
  }

  protected send(payload: O): void {
    if (!this.webSocket) {
      this.baseLogger.error(this.prefix + 'no websocket connection available for sending data');
    } else {
      const req = {
        messageType: 'REQUEST',
        request: payload
      };
      this.baseLogger.debug(this.prefix + '==> ' + JSON.stringify(req));
      this.webSocket.next(req);
    }
  }

  protected sendEvent(event: string, payload: any): void {
    if (!this.webSocket) {
      this.baseLogger.error(this.prefix + 'no websocket connection available for sending event');
    } else {
      const req = {
        messageType: 'EVENT',
        event,
        payload
      };
      this.baseLogger.debug(this.prefix + '==> ' + JSON.stringify(req));
      this.webSocket.next(req);
    }
  }

  public abstract onMessage(message: I, code?: string, status?: number): void;

  public onEvent(event: string, payload: any): void {
    this.baseLogger.trace(this.prefix +
      'received event ' + event + ' with payload', payload);
  }

  public onConnectionLost(): void {
    this.baseLogger.warn(this.prefix + 'connection lost');
  }

  public onConnectionRestored(): void {
    this.baseLogger.debug(this.prefix + 'connection restored');
  }

  public onConnectionEstablished(): void {
    this.baseLogger.debug(this.prefix + 'connection established');
  }


  private checkWSUrlMultiHost(url: string): string {

    this.baseLogger.debug('WS url before multi host check', url);

    // TODO: FIX per  doppia esposizione
    if ('env-01' === environment.environmentName || 'env-02' === environment.environmentName) {
      let newUrl = '';
      if (!environment.wsServer.includes(window.location.hostname) && url.startsWith('ws')) {
        newUrl = url.replace(environment.wsServer, environment.alternativeWsServer);
        this.baseLogger.trace(' Redirecting websocket request to ', newUrl);
      }

      if (newUrl.length > 0) {
        url = newUrl;
      }
    }

    this.baseLogger.debug('WS url after multi host check', url);

    return url;
  }
}
