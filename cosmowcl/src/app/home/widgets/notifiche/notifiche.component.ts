/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';

import { Subscription } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { Constants } from 'src/app/shared/constants/constants';
import {
  Notifica,
} from 'src/app/shared/models/api/cosmonotifications/notifica';
import {
  PaginaNotifiche,
} from 'src/app/shared/models/api/cosmonotifications/paginaNotifiche';
import {
  Notification,
} from 'src/app/shared/models/notifications/notification.model';
import { BusService } from 'src/app/shared/services/bus.service';
import {
  NotificationsWebsocketService,
} from 'src/app/shared/services/notifications-websocket.service';
import { environment } from 'src/environments/environment';

import { NotificheService } from '../../../shared/services/notifiche.service';

@Component({
  selector: 'app-notifiche',
  templateUrl: './notifiche.component.html',
  styleUrls: ['./notifiche.component.scss'],
})
export class NotificheComponent implements OnInit, OnDestroy {

  loading = 0;
  loadingMore = 0;
  loadingError: any | null = null;

  @ViewChild('notificheList') notificheList?: ElementRef;

  size = 5; // va recuperato da configurazione su db, valore iniziale di visualizzazione notifiche
  pageNum: number | null = null; // la pagina caricata

  currentPage: PaginaNotifiche | undefined;
  elementi: Notifica[] = [];
  unread = 0;

  private notificationSubcritpion: Subscription | null = null;
  private busServiceSubscription!: Subscription;

  constructor(
    private notificationService: NotificheService,
    private busService: BusService,
    private notificationsWebsocketService: NotificationsWebsocketService
  ) { }

  get allLoaded(): boolean {
    return !this.loadingError && !!this.pageNum &&
      !!this.currentPage && !!this.currentPage.info &&
      this.pageNum >= (this.currentPage.info.totalPages ?? 1) - 1;
  }

  get mostraPromptNotificheSistema(): boolean {
    return this.notificationsWebsocketService.notificheDiSistemaSupportate() &&
      this.notificationsWebsocketService.notificheDiSistemaNonChieste();
  }

  abilitaNotificheSistema(): void {
    if (!this.mostraPromptNotificheSistema) {
      return;
    }
    this.notificationsWebsocketService.promptSystemNotificationsGrant();
  }

  goUp() {
    if (this.notificheList) {
      this.notificheList.nativeElement.scrollTop = 0;
    }
  }

  async ngOnInit() {
    this.busServiceSubscription = this.busService.getCercaNotifiche().subscribe(async () => {
      await this.reset();
    });

    this.notificationSubcritpion = this.notificationsWebsocketService
      .whenEvent(Constants.APPLICATION_EVENTS.NOTIFICA)
      .pipe(
        debounceTime(1000)
      )
      .subscribe(async () => {
        await this.reset();
      });
  }

  async reset() {
    this.elementi = [];
    this.loading ++;
    try {
      await this.loadPage(0);
    } finally {
      this.loading --;
    }
  }

  // put method to backend (if notification has not already be marked as read)
  async markAsRead(n: Notification) {
    const inputGeneric = (n as any);
    if (n.lettura || inputGeneric.loading) {
      return;
    }

    inputGeneric.loading = true;
    try {
      await new Promise(r => setTimeout(r, environment.httpMockDelay));
      await this.notificationService.markAsRead(n.id);

      // aggiorno solo il badge col numero totale delle notifiche non lette
      const page = await this.notificationService.getAllNotifications(this.size, 0).toPromise();
      this.unread = page.totaleNonLette ?? 0;
      n.lettura = new Date();

    } finally {
      inputGeneric.loading = false;
    }
  }

  async markAllAsRead() {
    this.loading ++;
    try {
      await this.notificationService.markAllAsRead();
    } finally {
      this.loading --;
    }
    this.reset();
  }

  // funzione ausiliaria che calcola la posizione relativa della scrollbar rispetto alla fine del div
  private getPosition(notifiche: ElementRef | undefined): number | null {
    return (notifiche !== undefined && typeof (notifiche) !== 'undefined') ?
      notifiche.nativeElement.scrollHeight - notifiche.nativeElement.scrollTop - notifiche.nativeElement.clientHeight : null;
  }

  // evento la cui logica viene invocata quando si effettua lo scrolling sul div delle notifiche
  async onScroll(event: any) {
    if (this.loadingMore) {
      return;
    }
    const position = this.getPosition(this.notificheList);

    if (position && position > 200) {
      return;
    }
    if (!this.currentPage?.info?.totalPages) {
      return;
    }
    if (this.currentPage && this.currentPage.info && !this.currentPage.info.totalElements) {
      return;
    }
    if (this.pageNum !== null && this.pageNum >= this.currentPage.info.totalPages - 1) {
      return;
    }

    await this.loadPage(this.pageNum !== null ? this.pageNum + 1 : 0);
  }

  private async loadPage(pageNumber: number) {
    if (this.pageNum !== null && this.pageNum > pageNumber) {
      return;
    }

    this.loadingMore ++;

    try {
      await new Promise(r => setTimeout(r, environment.httpMockDelay));
      this.currentPage = await this.notificationService.getAllNotifications(this.size, pageNumber).toPromise();

      if (this.pageNum === null || this.pageNum <= pageNumber) {
        this.pageNum = pageNumber;
        this.elementi = this.elementi.concat(this.currentPage.elementi ?? []);
        this.unread = this.currentPage.totaleNonLette ?? 0;
      }
    } catch (failure) {
      this.loadingError = failure;
    } finally {
      this.loadingMore --;
    }
  }

  ngOnDestroy(): void {
    if (this.notificationSubcritpion) {
      this.notificationSubcritpion.unsubscribe();
    }
    if (this.busServiceSubscription) {
      this.busServiceSubscription.unsubscribe();
    }
  }
}
