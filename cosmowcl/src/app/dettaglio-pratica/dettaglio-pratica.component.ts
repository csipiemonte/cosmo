/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  Component,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import {
  ActivatedRoute,
  Router,
} from '@angular/router';

import { forkJoin, Subscription } from 'rxjs';
import {
  debounceTime,
  finalize,
} from 'rxjs/operators';
import { ModalService } from 'src/app/shared/services/modal.service';

import { TranslateService } from '@ngx-translate/core';

import { Constants } from '../shared/constants/constants';
import { Pratica } from '../shared/models/api/cosmobusiness/pratica';
import { CustomFormService } from '../shared/services/customForm.service';
import {
  NotificationsWebsocketService,
} from '../shared/services/notifications-websocket.service';
import { PraticheService } from '../shared/services/pratiche.service';
import { TabsDettaglioService } from '../shared/services/tabs-dettaglio.service';

@Component({
  selector: 'app-dettaglio-pratica',
  templateUrl: './dettaglio-pratica.component.html',
  styleUrls: ['./dettaglio-pratica.component.scss']
})
export class DettaglioPraticaComponent implements OnInit, OnDestroy {

  loading = 0;
  loadingError: any | null = null;
  loaded = false;

  tabAttivo!: string;
  pratica: Pratica | null = null;
  private notificationSubcritpion: Subscription | null = null;
  idPratica!: number;
  codiceForm!: string;

  constructor(
    private route: ActivatedRoute,
    private praticheService: PraticheService,
    private customFormService: CustomFormService,
    private modalService: ModalService,
    private translateService: TranslateService,
    private notificationsWebsocketService: NotificationsWebsocketService,
    private router: Router,
    private tabsDettaglioService: TabsDettaglioService
  ) {
  }

  ngOnDestroy(): void {
    if (this.notificationSubcritpion) {
      this.notificationSubcritpion.unsubscribe();
    }
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(routeData => {
      const newTab = routeData.tab;
      if (newTab && this.tabAttivo && newTab !== this.cleanTabRef(this.tabAttivo)) {
        this.tabActivated(newTab);
      }
    });

    this.route.params.subscribe(params => {
      this.idPratica = +params.id;
      this.refresh();
    });


    this.notificationSubcritpion = this.notificationsWebsocketService
      .whenEvent(Constants.APPLICATION_EVENTS.NOTIFICA_ULTIME_LAVORATE)
      .pipe(
        debounceTime(1000)
      )
      .subscribe(e => {
        this.refresh();
      });
  }

  refresh(){

    this.loading++;
    this.loadingError = null;

    this.praticheService.getVisibilitaById(this.idPratica ?? 0)
      .subscribe(visibilitaPratica => {

        if (visibilitaPratica != null) {
          this.pratica = visibilitaPratica;

          if (this.pratica?.tipo?.codice) {
            forkJoin({
              customForm: this.customFormService.getFromCodiceTipoPratica(this.pratica?.tipo?.codice),
              tabsDettaglio: this.tabsDettaglioService.getTabsDettaglioCodiceTipoPratica(this.pratica?.tipo?.codice)
            })
            .pipe(
              finalize(() => {
                this.loading--;
              })
            )
            .subscribe(data => {
              this.codiceForm = data.customForm?.codice;
              if (data.tabsDettaglio.length > 0) {
                this.tabActivated(data.tabsDettaglio[0].codice);
              } else{
                this.tabActivated('docs');
              }
            });
          }
        }
      },
      err => {

        this.loading--;
        this.loadingError = err;
        this.modalService.error(this.translateService.instant('errori.non_autorizzato'),
        err.error.title,
        err.error.errore)
          .then(() => {this.router.navigate(['']); })
          .catch(() => {});
      });
  }

  tabActivated(tabRef: string) {
    if (!tabRef) {
      return;
    }

    this.tabAttivo = tabRef;
    this.router.navigate(
      [],
      {
        relativeTo: this.route,
        queryParams: {
          tab: this.cleanTabRef(tabRef)
        },
        queryParamsHandling: 'merge',
    });
  }

  cleanTabRef(raw: string): string {
    if (!raw) {
      return raw;
    }
    return raw.replace('#', '');
  }

}
