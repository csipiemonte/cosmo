/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgbTimeStruct } from '@ng-bootstrap/ng-bootstrap';
import moment from 'moment';

export abstract class DateUtils {

  public static parse(input: string | null): Date | null {
    if (!input) {
      return null;
    }

    return moment(input).toDate();
  }

  public static dateToTimeString(input: Date | null): string | null {
    if (!input) {
      return null;
    }
    return input.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  }

  public static timeStringToTodayDate(input: string | null): Date | null {
    if (!input) {
      return null;
    }
    const splitted = input.split(':');
    const output = new Date();
    output.setHours(parseInt(splitted[0], 10), parseInt(splitted[1], 10), 0, 0);
    return output;
  }

  public static zonedDateTimeStringToDate(input: string | null): Date | null {
    if (!input) {
      return null;
    }
    return DateUtils.parse(input);
  }

  public static dateToZonedDateTimeString(input: Date | null): string | null {
    if (!input) {
      return null;
    }

    const tzo = -input.getTimezoneOffset();
    const dif = tzo >= 0 ? '+' : '-';
    const pad = (num: any) => {
        const norm = Math.floor(Math.abs(num));
        return (norm < 10 ? '0' : '') + norm;
    };
    return input.getFullYear() +
        '-' + pad(input.getMonth() + 1) +
        '-' + pad(input.getDate()) +
        'T' + pad(input.getHours()) +
        ':' + pad(input.getMinutes()) +
        ':' + pad(input.getSeconds()) +
        dif + pad(tzo / 60) +
        '' + pad(tzo % 60);
  }

  public static dateToDateString(input: Date | null): string | null {
    if (!input) {
      return null;
    }

    const pad = (num: any) => {
        const norm = Math.floor(Math.abs(num));
        return (norm < 10 ? '0' : '') + norm;
    };
    return input.getFullYear() +
        '-' + pad(input.getMonth() + 1) +
        '-' + pad(input.getDate());
  }

  public static combineDateTime(date: Date | null, time: NgbTimeStruct | null): Date | null {
    if (!date && !time) {
      return null;
    } else {
      const now = new Date();

      return new Date(
        date ? date.getFullYear() : now.getFullYear(),
        date ? date.getMonth() : now.getMonth(),
        date ? date.getDate() : now.getDate(),
        time ? (time.hour || 0) : 0,
        time ? (time.minute || 0) : 0,
        time ? (time.second || 0) : 0,
      );
    }
  }

}
