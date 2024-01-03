/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  HttpErrorResponse,
  HttpRequest,
  HttpResponse,
} from '@angular/common/http';

import { MonoTypeOperatorFunction } from 'rxjs';
import {
  delay,
  retryWhen,
  scan,
  tap,
} from 'rxjs/operators';
import { environment } from 'src/environments/environment';

import { UserInfo } from '../models/api/cosmo/userInfo';
import { Pratica } from '../models/api/cosmobusiness/pratica';
import { Attivita } from '../models/api/cosmopratiche/attivita';
import { EnviromentEnum, EnvironmentRelatedFunction } from './enviroment-enum';

export class Utils {
  public static ARRAY_MERGE_POLICY_CONCAT = 'c';
  public static ARRAY_MERGE_POLICY_REPLACE = 'r';
  public static ARRAY_MERGE_POLICY_DEFAULT = Utils.ARRAY_MERGE_POLICY_REPLACE;

  public static OBJECT_MERGE_POLICY_MERGE = 'm';
  public static OBJECT_MERGE_POLICY_REPLACE = 'r';
  public static OBJECT_MERGE_POLICY_DEFAULT = Utils.OBJECT_MERGE_POLICY_MERGE;

  public static voidPromise(p: Promise<any>) {
    p.then(() => {
      // NOP
    }).catch(() => {
      // NOP
    });
  }

  public static hasValidProfileEnte(user: UserInfo): boolean {
    return !!user.ente && !!user.profilo;
  }

  public static hasValidProfile(user: UserInfo): boolean {
    return !!user.profilo;
  }

  public static randomString(length: number, chars: string) {
    let mask = '';
    if (chars.indexOf('a') > -1) {
      mask += 'abcdefghijklmnopqrstuvwxyz';
    }
    if (chars.indexOf('A') > -1) {
      mask += 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    }
    if (chars.indexOf('#') > -1) {
      mask += '0123456789';
    }
    if (chars.indexOf('!') > -1) {
      mask += '~`!@#$%^&*()_+-={}[]:";\'<>?,./|\\';
    }
    let result = '';
    for (let i = length; i > 0; --i) {
      result += mask[Math.floor(Math.random() * mask.length)];
    }

    return result;
  }

  /**
   * Metodo che ricerca tutte le occorrenze del pattern ${...} e le
   * rimpiazza con un array di valori ordinati per posizione di replace
   * @param str stringa contenente i placeholders da rimpiazzare
   * @param repValues array di stringhe contenente i valori rimpiazzanti
   * @return str stringa aggiornata
   */
  public static parseAndReplacePlaceholders(
    str: string,
    repValues: string[]
  ): string {
    let repValue = '';
    for (repValue of repValues) {
      str = str.replace(/\$\{(.*?)\}/, repValue);
    }
    return str;
  }
  /**
   * Metodo che recupra il nome del file a partire da una response.
   * @param res httpresponse dal quale estrarre il nome del file.
   */
  public static getFileNameFromResponseContentDisposition(
    res: HttpResponse<object>
  ) {
    const contentDisposition = res.headers.get('content-disposition') || '';
    const matches = /filename=([^;]+)/gi.exec(contentDisposition);
    if (matches && matches.length > 1) {
      return matches[1].trim();
    } else {
      return 'untitled';
    }
  }

  public static download(res: any): void {
    /*
     *  storico delle precedenti implementazioni:
     * - scaricare da un BLOB:
     *    rimosso perche' poco performante con file > 100kB (pre-carica senza dare feedback all'utente e occupa memoria)
     * - scaricare da una URL con un elemento 'a' nascosto:
     *    da problemi di random "Network Error" su chrome
     * - scaricare da una URL con window.location.href = url:
     *    non funziona perche' il browser pensa che la pagina corrente si stia chiudendo e scatena eventi
     * - scaricare da una URL con window.open -> implementazione corrente
     *
     */
    const element = document.createElement('a');
    element.setAttribute('style', 'display: none');

    if (typeof res === 'string') {
      console.log('downloading from URL ' + res);
      res = this.checkUrlMultiHost(res);
      window.open(res, '_blank');
      return;
    } else {
      console.log('downloading from BLOB ' + res);
    }

    element.download = Utils.getFileNameFromResponseContentDisposition(res);
    element.href = URL.createObjectURL(res.body);
    document.body.appendChild(element);
    element.click();

    setTimeout(() => {
      window.URL.revokeObjectURL(element.href);
      element.remove();
    }, 100);
  }

  public static previewFromBuffer(res: Blob): void {
    // var file = new Blob([res], { type: contentType });
    const fileURL = URL.createObjectURL(res);
    window.open(fileURL);
    return;
  }

  public static preview(res: any): void {
    const element = document.createElement('a');
    element.setAttribute('style', 'display: none');

    if (typeof res === 'string') {
      element.target = '_blank';
      res = this.checkUrlMultiHost(res);
      element.href = res;
      document.body.appendChild(element);
      element.click();
      setTimeout(() => {
        element.remove();
      }, 200);
      return;
    }

    element.href = URL.createObjectURL(res.body);
    element.target = '_blank';
    document.body.appendChild(element);
    element.click();
    setTimeout(() => {
      window.URL.revokeObjectURL(element.href);
      element.remove();
    }, 100);
  }

  public static getEncodedBase64ValueFromArrayBufferString(
    arrayBufferString: string
  ): string {
    let encodedBase64 = '';
    if (arrayBufferString) {
      const base64StartToken = 'base64,';
      const startIndex = arrayBufferString.indexOf(base64StartToken);
      if (startIndex && startIndex > 0) {
        encodedBase64 = arrayBufferString.substr(
          startIndex + base64StartToken.length
        );
      }
    }
    return encodedBase64;
  }

  public static getEncodeBlobValueFromBase64String(
    b64Data: any,
    contentType: any,
    sliceSize: any
  ) {
    contentType = contentType || '';
    sliceSize = sliceSize || 512;

    const byteCharacters = atob(b64Data);
    const byteArrays = [];

    for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
      const slice = byteCharacters.slice(offset, offset + sliceSize);
      const byteNumbers = new Array(slice.length);
      for (let i = 0; i < slice.length; i++) {
        byteNumbers[i] = slice.charCodeAt(i);
      }
      const byteArray = new Uint8Array(byteNumbers);
      byteArrays.push(byteArray);
    }
    return new Blob(byteArrays, { type: contentType });
  }

  public static toMessage(input: any, defaultMessage?: string): string {
    if (typeof input === 'string') {
      return input;
    }
    if (input instanceof Error) {
      return input.message;
    }
    if (input instanceof HttpErrorResponse) {
      return input.error?.title ?? input.message ?? input.statusText;
    }
    if (
      input.errore &&
      typeof input.errore === 'string' &&
      input.errore.trim().length > 0
    ) {
      return input.errore;
    }
    return defaultMessage ?? 'Errore imprevisto';
  }

  public static cleanObject(obj: any): any {
    obj = JSON.parse(JSON.stringify(obj));
    Object.keys(obj).forEach(
      (key) =>
        (obj[key] &&
          typeof obj[key] === 'object' &&
          Utils.cleanObject(obj[key])) ||
        ((obj[key] === undefined || obj[key] === null) && delete obj[key])
    );
    return obj;
  }

  public static isDefined(raw: any): boolean {
    if (raw === null || raw === undefined || typeof raw === 'undefined') {
      return false;
    }
    return true;
  }

  public static isNotBlank(raw: any): boolean {
    if (raw === null || raw === undefined || typeof raw === 'undefined') {
      return false;
    }
    if (typeof raw !== 'string') {
      return true;
    }

    return raw.trim() !== '';
  }

  public static getPropertyValue(object: any, field: string): string | null {
    if (!Utils.isDefined(object)) {
      return null;
    }
    if (field.indexOf('.') === -1) {
      return object[field];
    }
    const splitted = field.split('.');
    const subObject = object[splitted[0]];
    return this.getPropertyValue(subObject, splitted.slice(1).join('.'));
  }

  public static setPropertyValue(
    object: any,
    field: string,
    value: any
  ): string | null {
    if (!Utils.isDefined(object)) {
      return null;
    }
    if (field.indexOf('.') === -1) {
      object[field] = value;
      return field;
    }
    const splitted = field.split('.');
    let subObject = object[splitted[0]];
    if (!Utils.isDefined(subObject)) {
      subObject = {};
      object[splitted[0]] = subObject;
    }
    return this.setPropertyValue(subObject, splitted.slice(1).join('.'), value);
  }

  public static clone<T>(source: T): T {
    const isObject = (x: any) => x && typeof x === 'object';
    if (!Utils.isDefined(source)) {
      return source;
    } else if (Array.isArray(source)) {
      return (source as any).map((el: any) => Utils.clone(el));
    } else if (source instanceof Date) {
      return new Date(source.getTime()) as any;
    } else if (isObject(source)) {
      const cloned: any = {};
      Object.keys(source).forEach((key: any) => {
        cloned[key] = Utils.clone((source as any)[key]);
      });
      return cloned;
    } else {
      return source;
    }
  }

  public static mergeDeep(
    target: any,
    source: any,
    arrayPolicy = Utils.ARRAY_MERGE_POLICY_DEFAULT,
    objectPolicy = Utils.OBJECT_MERGE_POLICY_DEFAULT
  ) {
    const isObject = (obj: any) => obj && typeof obj === 'object';

    return [source].reduce((prev, obj) => {
      Object.keys(obj).forEach((key) => {
        const pVal = prev[key];
        const oVal = obj[key];

        if (Array.isArray(oVal)) {
          if (arrayPolicy === Utils.ARRAY_MERGE_POLICY_CONCAT) {
            if (Array.isArray(pVal)) {
              prev[key] = pVal.concat(...oVal);
            } else {
              prev[key] = oVal.map((o) => o);
            }
          } else {
            prev[key] = oVal;
          }
        } else if (oVal instanceof Date) {
          prev[key] = oVal;
        } else if (isObject(oVal)) {
          if (objectPolicy === Utils.OBJECT_MERGE_POLICY_MERGE) {
            if (isObject(pVal)) {
              prev[key] = Utils.mergeDeep(pVal, oVal);
            } else {
              prev[key] = Utils.mergeDeep({}, oVal);
            }
          } else {
            prev[key] = Utils.mergeDeep({}, oVal);
          }
        } else {
          prev[key] = oVal;
        }
      });

      return prev;
    }, target);
  }

  public static notNull(...values: any): void {
    for (const v of values) {
      if (!Utils.isDefined(v)) {
        throw Error('A not-null value is required');
      }
    }
  }

  public static coalesce<T>(...values: T[]): T | null {
    for (const v of values) {
      if (!!Utils.isDefined(v)) {
        return v;
      }
    }
    return null;
  }

  public static require<T>(value: T | undefined | null, name?: string): T {
    if (value === null || typeof value === 'undefined') {
      throw new Error('Missing required value' + name ? ' "' + name + '"' : '');
    }
    return value;
  }

  public static getProcessId(pratica: Pratica): number | null {
    if (pratica?.linkPratica?.length) {
      let raw = pratica.linkPratica.trim();
      while (raw.startsWith('/') || raw.startsWith(' ')) {
        raw = raw.substr(1).trim();
      }
      const prefix = 'pratiche/';
      if (raw.startsWith(prefix)) {
        return parseInt(raw.substr(prefix.length), 10);
      }
    }
    return null;
  }

  public static getTaskId(attivita: Attivita): number | null {
    if (attivita?.linkAttivita?.length) {
      let raw = attivita.linkAttivita.trim();
      while (raw.startsWith('/') || raw.startsWith(' ')) {
        raw = raw.substr(1).trim();
      }
      const prefix = 'tasks/';
      if (raw.startsWith(prefix)) {
        return parseInt(raw.substr(prefix.length), 10);
      }
    }
    return null;
  }

  public static getIdTaskFromLinkAttivita(linkAttivita: string): string {
    return linkAttivita.split('/')[1];
  }

  public static getParameterByName(name: string) {
    const url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    const regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)');
    const results = regex.exec(url);
    if (!results) {
      return null;
    }
    if (!results[2]) {
      return null;
    }
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
  }

  public static jasperize(input: string): string {
    if (!input) {
      return input;
    }
    let s = input;
    s = s.replace(/\<strong\>(.*?)\<\/strong\>/g, '<b>$1</b>');
    s = s.replace(
      /\<span style="color:(.*?);"\>(.*?)\<\/span\>/g,
      '<font color="$1">$2</font>'
    );
    s = s.replace(
      /\<span class="text-tiny"\>(.*?)\<\/span\>/g,
      '<font size="1">$1</font>'
    );
    s = s.replace(
      /\<span class="text-small"\>(.*?)\<\/span\>/g,
      '<font size="1">$1</font>'
    );
    s = s.replace(
      /\<span class="text-big"\>(.*?)\<\/span\>/g,
      '<font size="4">$1</font>'
    );
    s = s.replace(
      /\<span class="text-huge"\>(.*?)\<\/span\>/g,
      '<font size="5">$1</font>'
    );
    s = s.replace(/\<h1\>(.*?)\<\/h1\>/g, '<font size="7"><b>$1</b></font>');
    s = s.replace(/\<h2\>(.*?)\<\/h2\>/g, '<font size="6"><b>$1</b></font>');
    s = s.replace(/\<h3\>(.*?)\<\/h3\>/g, '<font size="5"><b>$1</b></font>');
    s = s.replace(/\<h4\>(.*?)\<\/h4\>/g, '<font size="4"><b>$1</b></font>');
    s = s.replace(/\<h5\>(.*?)\<\/h5\>/g, '<font size="2"><b>$1</b></font>');
    s = s.replace(
      /\<s\>(.*?)\<\/s\>/g,
      '<font style="text-decoration: line-through">$1</font>'
    );
    s = s.replace(
      /\<a href="(.*?)"\>(.*?)\<\/a\>/g,
      '<a href="$1"><u>$1<u/></a>'
    );
    return s;
  }

  public static retryWithDelay<T>(
    delayMs: number,
    count = 1
  ): MonoTypeOperatorFunction<T> {
    return (input) =>
      input.pipe(
        retryWhen((errors) =>
          errors.pipe(
            scan((acc, error) => ({ count: acc.count + 1, error }), {
              count: 0,
              error: undefined as any,
            }),
            tap((current) => {
              if (current.count > count) {
                throw current.error;
              }
            }),
            delay(delayMs)
          )
        )
      );
  }

  public static checkUrlMultiHost(url: string): string {
    // TODO: FIX per  doppia esposizione
    if ('env-01' === environment.environmentName) {
      let newUrl = '';
      if (
        !environment.beServer.includes(window.location.hostname) &&
        url.startsWith('http')
      ) {
        newUrl = url.replace(
          environment.beServer,
          environment.intracomHostname
        );
        console.log(' Redirecting to ', newUrl);
      }

      if (newUrl.length > 0) {
        url = newUrl;
      }
    } else if ('env-02' === environment.environmentName) {
      let newUrl = '';
      if (
        !environment.beServer.includes(window.location.hostname) &&
        url.startsWith('http')
      ) {
        newUrl = url.replace(
          environment.beServer,
          environment.intranetHostname
        );
        console.log(' Redirecting to ', newUrl);
      }

      if (newUrl.length > 0) {
        url = newUrl;
      }
    }
    return url;
  }

  private static checkBeServer(): string {
    let beServer = EnviromentEnum.DEFAULT;

    if (environment.moreAccesses) {
      if (environment.intracomHostname === window.location.hostname) {
        beServer = EnviromentEnum.INTRACOM;
      } else if (environment.intranetHostname === window.location.hostname) {
        beServer = EnviromentEnum.INTRANET;
      } else if (environment.internetHostname === window.location.hostname) {
        beServer = EnviromentEnum.INTERNET;
      }
    }
    return beServer;
  }

  public static performEnvironmentRelatedAction(
    funct: EnvironmentRelatedFunction,
    request?: HttpRequest<any>
  ): string | HttpRequest<any> | null {
    let stringRet: string | null = null;
    let requestRet: HttpRequest<any> | null = null;

    switch (funct) {
      case EnvironmentRelatedFunction.INTRANET:
        stringRet = Utils.getIntranetURL();

        break;
      case EnvironmentRelatedFunction.LOGOUT:
        stringRet = Utils.getLogoutURL();
        break;
      case EnvironmentRelatedFunction.REQUEST_INTERCEPTION:
        if (request) {
          requestRet = Utils.manageRequest(request);
        }
        break;
      case EnvironmentRelatedFunction.BE_SERVER_CHECK:

      stringRet = Utils.checkBeServer();
      break;
      default:
        break;
    }
    return stringRet ?? requestRet;
  }

  private static getIntranetURL(): string {
    let stringRet = environment.intranetInternet;

    if (environment.moreAccesses) {
      if (environment.intracomHostname === window.location.hostname) {
        stringRet = environment.intranetIntracom;
      } else if (environment.intranetHostname === window.location.hostname) {
        stringRet = environment.intranetIntranet;
      } else if (environment.internetHostname === window.location.hostname) {
        stringRet = environment.intranetInternet;
      }
    }
    return stringRet;
  }

  private static getLogoutURL(): string {
    let stringRet = environment.shibbolethSSOLogoutURLInternet;
    if (environment.moreAccesses) {
      if (environment.intracomHostname === window.location.hostname) {
        stringRet = environment.shibbolethSSOLogoutURLIntracom;
      } else if (environment.intranetHostname === window.location.hostname) {
        stringRet = environment.shibbolethSSOLogoutURLInternet;
      } else if (environment.internetHostname === window.location.hostname) {
        stringRet = environment.shibbolethSSOLogoutURLInternet;
      }
    }
    return stringRet;
  }

  private static manageRequest(request: HttpRequest<any>): HttpRequest<any> {
    let requestRet = request;
    let newUrl = '';
    console.log('window.location.hostname is', window.location.hostname);
    if (
      !environment.beServer.includes(window.location.hostname) &&
      request.url.startsWith('http')
    ) {
      newUrl = request.url.replace(
        environment.beServer,
        environment.intranetHostname
      );
      console.log('new URL is', newUrl);
    } else if (
      !environment.beServer.includes(window.location.hostname) &&
      request.url.startsWith('ws')
    ) {
      newUrl = request.url.replace(
        environment.wsServer,
        environment.alternativeWsServer
      );
    }
    if (newUrl.length > 0) {
      requestRet = request.clone({ url: 'https://' + newUrl });
    }
    return requestRet;
  }
}


