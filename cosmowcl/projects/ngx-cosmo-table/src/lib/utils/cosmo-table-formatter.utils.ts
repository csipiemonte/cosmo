import {
  CosmoTableFormatter,
  ICosmoTableColumn,
  ICosmoTableFormatSpecification,
  ICosmoTableFormatProvider,
  ICosmoTableConfiguration
} from '../model';
import { LowerCasePipe, UpperCasePipe, DatePipe, CurrencyPipe } from '@angular/common';
import { CosmoTableComponent } from '../cosmo-table.component';
import moment from 'moment';


// @dynamic
export abstract class CosmoTableFormatterHelper {
  private static lowercasePipe = new LowerCasePipe();
  private static uppercasePipe = new UpperCasePipe();

  private static transformMap = {
    UPPERCASE: (raw: any, locale: string, args: any) => CosmoTableFormatterHelper.uppercasePipe.transform(raw),
    LOWERCASE: (raw: any, locale: string, args: any) => CosmoTableFormatterHelper.lowercasePipe.transform(raw),
    DATE: (raw: any, locale: string, args: any) => {
      if (!raw) {
        return null;
      } else if (!(raw instanceof Date)) {
        raw = moment(raw).toDate();
      }
      return new DatePipe(locale).transform(raw, args);
    },
    CURRENCY: (raw: any, locale: string, args: any) => {
      // signature: currencyCode?: string, display?: string | boolean, digitsInfo?: string, locale?: string
      if (!args) {
        args = {};
      }
      return new CurrencyPipe(locale).transform(raw, args.currencyCode, args.display, args.digitsInfo, locale);
    }
  };

  public static getPropertyValue(object: any, field: string): string | null {
    if (object === null || object === undefined) {
      return null;
    }
    if (field.indexOf('.') === -1) {
      return object[field];
    }
    const splitted = field.split('.');
    const subObject = object[splitted[0]];
    return this.getPropertyValue(subObject, splitted.slice(1).join('.'));
  }

  public static format(raw: any, formatterSpec: ICosmoTableFormatSpecification, locale?: string) {
    if (!raw) {
      return raw;
    }

    const transformFn = CosmoTableFormatterHelper.transformMap[formatterSpec.formatter];
    if (!transformFn) {
      throw new Error('Unknown formatter: ' + formatterSpec.formatter);
    }

    try {
      return transformFn(raw, locale || 'en', formatterSpec.arguments);
    } catch (err) {
      console.error('error transforming', err);
      // throw err;
      return null;
    }
  }

  public static extractRawValue(row: any, column: ICosmoTableColumn, locale?: string) {
    if (!row) {
      return null;
    }
    let val: any;
    if (column.valueExtractor) {
      val = column.valueExtractor(row);
    } else if (column.field) {
      val = CosmoTableFormatterHelper.getPropertyValue(row, column.field);
    } else {
      val = undefined;
    }

    return val;
  }

  public static extractValue(row: any, column: ICosmoTableColumn, locale?: string) {
    if (!row) {
      return null;
    }
    let val: any = CosmoTableFormatterHelper.extractRawValue(row, column, locale);

    if (column.formatters) {
      for (const formatter of (Array.isArray(column.formatters) ? column.formatters : [column.formatters])) {
        if ( (formatter as any).formatter ) {
          val = CosmoTableFormatterHelper.format(val, (formatter as ICosmoTableFormatSpecification), locale);
        } else if ( (formatter as any).format ) {
          val = (formatter as ICosmoTableFormatProvider).format(val);
        } else {
          val = CosmoTableFormatterHelper.format(val, { formatter: (formatter as CosmoTableFormatter), arguments: null }, locale);
        }
      }
    }

    return val;
  }

  public static isDefaultVisibleColumn(column: ICosmoTableColumn, config: ICosmoTableConfiguration) {
    return (column.defaultVisible === true || column.defaultVisible === false ?
      column.defaultVisible : config?.columns?.defaultIsDefaulView) ||
        !CosmoTableFormatterHelper.isHideable(column, config);
  }

  public static isDefaultFilterColumn(column: ICosmoTableColumn, config: ICosmoTableConfiguration) {
    return column.defaultFilter === true || column.defaultFilter === false ? column.defaultFilter : config?.columns?.defaultIsDefaultFilter;
  }

  public static isHideable(column: ICosmoTableColumn, config: ICosmoTableConfiguration): boolean | undefined {
    return column.canHide === true || column.canHide === false ? column.canHide : config?.columns?.defaultCanHide;
  }

  public static isSortable(column: ICosmoTableColumn, config: ICosmoTableConfiguration) {
    return column.canSort === true || column.canSort === false ? column.canSort : config?.columns?.defaultCanSort;
  }

  public static isFilterable(column: ICosmoTableColumn, config: ICosmoTableConfiguration) {
    return (column.canFilter === true || column.canFilter === false ? column.canFilter : config?.columns?.defaultCanFilter) &&
      (column.serverField || column.field);
  }

  public static hasLabel(column: ICosmoTableColumn, component: CosmoTableComponent | null, config: ICosmoTableConfiguration) {
    return (column.labelKey || column.label) || (component?.labelGroup && column.label !== null);
  }

  public static isLabelVisibleInTable(column: ICosmoTableColumn, config: ICosmoTableConfiguration) {
    return column.showLabelInTable === true || column.showLabelInTable === false ?
      column.showLabelInTable : config?.columns?.defaultShowLabelInTable;
  }

}
