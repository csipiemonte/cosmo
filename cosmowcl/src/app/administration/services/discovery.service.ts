/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Injectable } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiUrls } from 'src/app/shared/utilities/apiurls';
import { map } from 'rxjs/operators';

@Injectable()
export class DiscoveryService {

  SERVICE_NAME = 'DiscoveryService';

  constructor(
    private logger: NGXLogger,
    private http: HttpClient
  ) {
    this.logger.debug('creating service ' + this.SERVICE_NAME);
  }

  getServices(): Observable<any[]> {
    return this.http.get<any[]>(ApiUrls.DISCOVERY_SERVICES)
      .pipe(
        map(list => list.map(service => {
          service.instances.sort(this.sortInstances);
          return service;
        })),
        map(list => list.sort(this.sortServices)),
      );
  }

  private sortServices(o1: any, o2: any): number {
    return (o1.registryEntry.configuration.name as string).localeCompare(
      (o2.registryEntry.configuration.name as string));
  }

  private sortInstances(o1: any, o2: any): number {
    return (o2.registryEntry.firstDiscoveryTime as string).localeCompare(
      (o1.registryEntry.firstDiscoveryTime as string));
    /*
    return (o1.registryEntry.configuration.instanceId as string).localeCompare(
      (o2.registryEntry.configuration.instanceId as string));
    */
  }

}
