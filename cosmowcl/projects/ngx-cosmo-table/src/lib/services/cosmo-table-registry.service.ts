import { Injectable } from '@angular/core';
import { CosmoTableLogger } from '../utils';
import { CosmoTableComponent } from '../cosmo-table.component';

@Injectable({providedIn: 'root'})
export class CosmoTableRegistryService {

  private static counter = 0;

  private logger: CosmoTableLogger;

  private registry: any = {};

  constructor() {
    this.logger = new CosmoTableLogger('CosmoTableRegistryService');
    this.logger.trace('building service');
  }

  public register(key: string, component: CosmoTableComponent): string {
    if (!key) {
      this.logger.warn('Invalid key provided to registry', key);
      throw Error('Invalid key provided to registry');
    }
    if (!component) {
      this.logger.warn('Invalid component provided to registry', component);
      throw Error('Invalid component provided to registry');
    }

    if (!this.registry[key]) {
      this.registry[key] = [];
    }

    const uuid: string = 'MTREG-' + (++CosmoTableRegistryService.counter) + '-' + Math.round(Math.random() * 100000);
    const item = {
      key,
      component,
      registrationId: uuid
    };

    this.registry[key].push(item);

    this.logger.debug('registered table', item);
    return uuid;
  }

  public unregister(key: string, uuid: string) {
    if (!key) {
      this.logger.warn('Invalid key provided to registry', key);
      throw Error('Invalid key provided to registry');
    }
    if (!uuid) {
      this.logger.warn('Invalid uuid provided to registry', uuid);
      throw Error('Invalid uuid provided to registry');
    }

    this.logger.debug('unregistered table', uuid);
    this.registry[key] = this.registry[key].filter( (o: any) => o.registrationId !== uuid);
  }

  public get(key: string) {
    return this.registry[key];
  }

  public getSingle(key: string): CosmoTableComponent | null {
    const arr = this.registry[key];
    if (!arr || !arr.length) {
      return null;
    } else if (arr.length > 1) {
      this.logger.error('MULTIPLE REFERENCES FOR KEY', key);
      return null;
    } else {
      return arr[0].component;
    }
  }
}
