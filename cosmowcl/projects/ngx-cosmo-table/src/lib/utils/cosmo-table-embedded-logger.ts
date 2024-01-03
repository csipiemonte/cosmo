import { ICosmoTableLogAdapter } from './cosmo-table-log-adapter';

export class CosmoTableEmbeddedLogger implements ICosmoTableLogAdapter {

  trace(message: any, ...additional: any[]): void {
    console.log('[trace] ' + message, ...additional);
  }

  debug(message: any, ...additional: any[]): void {
    console.log('[trace] ' + message, ...additional);
  }

  info(message: any, ...additional: any[]): void {
    console.log('[INFO] ' + message, ...additional);
  }

  log(message: any, ...additional: any[]): void {
    console.log('[INFO] ' + message, ...additional);
  }

  warn(message: any, ...additional: any[]): void {
    console.warn('[WARN] ' + message, ...additional);
  }

  error(message: any, ...additional: any[]): void {
    console.error('[ERROR] ' + message, ...additional);
  }

  fatal(message: any, ...additional: any[]): void {
    console.error('[FATAL] ' + message, ...additional);
  }
}

