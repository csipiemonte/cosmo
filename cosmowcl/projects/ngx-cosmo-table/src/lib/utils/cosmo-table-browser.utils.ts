import { detect, BrowserInfo, BotInfo, NodeInfo } from 'detect-browser';

export abstract class CosmoTableBrowserHelper {

  private static browser: BrowserInfo | BotInfo | NodeInfo | null = detect();

  public static getBrowser(): Browser {
    const name = this.browser?.name;
    if (
      name === Browser.CHROME ||
      name === Browser.FIREFOX ||
      name === Browser.EDGE ||
      name === Browser.IE ||
      name === Browser.OPERA ||
      name === Browser.SAFARI ) {
        return name as Browser;
      } else {
        return Browser.OTHER;
      }
  }

  public static isIE(): boolean {
    return CosmoTableBrowserHelper.getBrowser() === Browser.IE;
  }
}

export enum Browser {
  EDGE = 'edge',
  OPERA = 'opera',
  CHROME = 'chrome',
  IE = 'ie',
  FIREFOX = 'firefox',
  SAFARI = 'safari',
  OTHER = 'other'
}
