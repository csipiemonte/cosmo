export interface ICosmoTableLogAdapter {

  trace(message: any, ...additional: any[]): void;

  debug(message: any, ...additional: any[]): void;

  info(message: any, ...additional: any[]): void;

  log(message: any, ...additional: any[]): void;

  warn(message: any, ...additional: any[]): void;

  error(message: any, ...additional: any[]): void;

  fatal(message: any, ...additional: any[]): void;

}

