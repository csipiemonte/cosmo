import { Injectable } from '@angular/core';
import { ICosmoTableLogAdapter } from '../utils/cosmo-table-log-adapter';
import { CosmoTableEmbeddedLogger } from '../utils/cosmo-table-embedded-logger';

@Injectable({providedIn: 'root'})
export class CosmoTableLoggerService {

  private static embeddedLogger: ICosmoTableLogAdapter = new CosmoTableEmbeddedLogger();
  private static logAdapter: ICosmoTableLogAdapter | null = null;

  constructor() {
  }

  public static getConfiguredLogAdapter(): ICosmoTableLogAdapter {
    return CosmoTableLoggerService.logAdapter || CosmoTableLoggerService.embeddedLogger;
  }

  public withLogAdapter(logAdapter: ICosmoTableLogAdapter | null) {
    CosmoTableLoggerService.logAdapter = logAdapter;
  }

}
