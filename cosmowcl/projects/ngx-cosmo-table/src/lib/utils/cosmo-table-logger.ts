import { ICosmoTableLogAdapter } from './cosmo-table-log-adapter';
import { CosmoTableLoggerService } from '../services/cosmo-table-logger.service';

export class CosmoTableLogger {

  private prefix: string;

  constructor(private componentName: string) {
    // NOP
    this.prefix = '[' + this.componentName + '] ';
  }

  trace(message: any, ...additional: any[]): void {
    this.getEffectiveLogger().trace(this.prefix + message, ...additional);
  }

  debug(message: any, ...additional: any[]): void {
    this.getEffectiveLogger().debug(this.prefix + message, ...additional);
  }

  info(message: any, ...additional: any[]): void {
    this.getEffectiveLogger().info(this.prefix + message, ...additional);
  }

  log(message: any, ...additional: any[]): void {
    this.getEffectiveLogger().log(this.prefix + message, ...additional);
  }

  warn(message: any, ...additional: any[]): void {
    this.getEffectiveLogger().warn(this.prefix + message, ...additional);
  }

  error(message: any, ...additional: any[]): void {
    this.getEffectiveLogger().error(this.prefix + message, ...additional);
  }

  fatal(message: any, ...additional: any[]): void {
    this.getEffectiveLogger().fatal(this.prefix + message, ...additional);
  }

  private getEffectiveLogger(): ICosmoTableLogAdapter {
    return CosmoTableLoggerService.getConfiguredLogAdapter();
  }
}

