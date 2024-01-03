/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, OnDestroy } from '@angular/core';
import { DiscoveryService } from '../services/discovery.service';
import { NGXLogger } from 'ngx-logger';
import { timer, Subscription } from 'rxjs';


@Component({
  selector: 'app-discovery',
  templateUrl: './discovery.component.html',
  styleUrls: ['./discovery.component.scss']
})
export class DiscoveryComponent implements OnInit, OnDestroy {

  COMPONENT_NAME = 'DiscoveryComponent';

  error: string | null = null;
  services: any[] | null = null;
  loaded: boolean | null = false;
  timerSubscription: Subscription | null = null;

  constructor(
    private logger: NGXLogger,
    private discoveryService: DiscoveryService
  ) {
    this.logger.debug('creating component ' + this.COMPONENT_NAME);
  }

  ngOnInit(): void {
    this.logger.debug('initializing component ' + this.COMPONENT_NAME);

    this.timerSubscription = timer(10, 5000).subscribe(() => {
      this.discoveryService.getServices().subscribe(services => {
        this.services = services;
        this.loaded = true;
      }, failure => {
        this.error = failure;
      });
    });
  }

  ngOnDestroy(): void {
    this.logger.debug('destroying component ' + this.COMPONENT_NAME);
    if (this.timerSubscription) {
      this.timerSubscription.unsubscribe();
    }
  }

  getService(name: string): any {
    if (!this.services) {
      return [];
    }
    return this.services.find(s => (s.registryEntry?.configuration?.name ?? '') === name);
  }

  cardDisplayClass(name: string): string {
    const s = this.getService(name);
    if (!s) {
      return 'border-danger';
    }
    let badgeClass = '';

    switch (s.status) {
      case 'UP':
        badgeClass = 'border-success';
        break;
      case 'DOWN':
        badgeClass = 'border-warning';
        break;
      case 'TROUBLE':
        badgeClass = 'border-warning';
        break;
      case 'STARTING':
        badgeClass = 'border-primary';
        break;
      case 'SHUTDOWN':
        badgeClass = 'border-secondary';
        break;
      case 'NOT_REPORTING':
        badgeClass = 'border-warning';
        break;
      case 'FAILURE':
        badgeClass = 'border-danger';
        break;
      case 'UNAVAILABLE':
        badgeClass = 'border-danger';
        break;
      default:
        badgeClass = 'border-warning';
        break;
    }

    return badgeClass;
  }

  cardDisplayIcon(name: string): string {
    const s = this.getService(name);
    if (!s) {
      return 'fas fa-exclamation-circle text-danger';
    }
    let badgeClass = '';

    switch (s.status) {
      case 'UP':
        badgeClass = 'fas fa-check-circle text-success';
        break;
      case 'DOWN':
        badgeClass = 'fas fa-check-circle text-warning';
        break;
      case 'TROUBLE':
        badgeClass = 'fas fa-exclamation-triangle text-warning';
        break;
      case 'STARTING':
        badgeClass = 'fas fa-hourglass-start text-primary';
        break;
      case 'SHUTDOWN':
        badgeClass = 'fas fa-power-off text-secondary';
        break;
      case 'NOT_REPORTING':
        badgeClass = 'fas fa-late text-warning';
        break;
      case 'FAILURE':
        badgeClass = 'fas fa-exclamation-circle text-danger';
        break;
      case 'UNAVAILABLE':
        badgeClass = 'fas fa-exclamation-circle text-danger';
        break;
      default:
        badgeClass = 'fas fa-exclamation-circle text-warning';
        break;
    }

    return badgeClass;
  }

  instanceDisplayClass(row: any): string {
    let badgeClass = '';

    switch (row.status) {
      case 'UP':
        badgeClass = 'badge-success';
        break;
      case 'DOWN':
        badgeClass = 'badge-warning';
        break;
      case 'TROUBLE':
        badgeClass = 'badge-warning';
        break;
      case 'STARTING':
        badgeClass = 'badge-primary';
        break;
      case 'SHUTDOWN':
        badgeClass = 'badge-secondary';
        break;
      case 'NOT_REPORTING':
        badgeClass = 'badge-warning';
        break;
      case 'FAILURE':
        badgeClass = 'badge-danger';
        break;
      case 'UNAVAILABLE':
        badgeClass = 'badge-danger';
        break;
      default:
        badgeClass = 'badge-warning';
        break;
    }

    return badgeClass;
  }

  instanceDisplayMessage(row: any): any {
    let output = '';

    switch (row.status) {
      case 'UP':
        output = 'l\'istanza e\' attiva e disponibile';
        break;
      case 'DOWN':
        output = 'l\'istanza e\' attiva ma segnala gravi problemi';
        break;
      case 'TROUBLE':
        output = 'l\'istanza e\' attiva ma riporta possibili problemi';
        break;
      case 'STARTING':
        output = 'l\'istanza e\' in fase di avvio';
        break;
      case 'SHUTDOWN':
        output = 'l\'istanza e\' stata terminata';
        break;
      case 'NOT_REPORTING':
        output = 'l\'istanza e\' in ritardo con gli heartbeat';
        break;
      case 'FAILURE':
        output = 'l\'istanza e\' non pervenuta da troppo tempo (considerata morta)';
        break;
      default:
        output = 'l\'istanza e\' in uno stato sconosciuto';
        break;
    }

    return output;
  }

  serviceDisplayClass(row: any): any {
    return this.instanceDisplayClass(row);
  }

  serviceDisplayMessage(row: any): any {
    let output = '';

    switch (row.status) {
      case 'UP':
        output = 'il servizio e\' attivo e disponibile';
        break;
      case 'DOWN':
        output = 'il servizio e\' attivo ma le istanze segnalano gravi problemi';
        break;
      case 'TROUBLE':
        output = 'il servizio e\' attivo ma le istanze riportano possibili problemi';
        break;
      case 'STARTING':
        output = 'il servizio e\' in fase di avvio';
        break;
      case 'SHUTDOWN':
        output = 'tutte le istanze del servizio sono state terminate';
        break;
      case 'NOT_REPORTING':
        output = 'il servizio ha istanze attive ma in ritardo con gli heartbeat';
        break;
      case 'FAILURE':
        output = 'il servizio non ha istanze considerate attive';
        break;
      case 'UNAVAILABLE':
        output = 'il servizio e\' non ha istanze disponibili';
        break;
      default:
        output = 'il servizio e\' in uno stato sconosciuto';
        break;
    }

    return output;
  }
}
