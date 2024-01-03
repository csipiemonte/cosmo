import { ICosmoTableColumn, ICosmoTableConfiguration } from '../model';
import { CosmoTableFormatterHelper } from './cosmo-table-formatter.utils';
import { CosmoTableComponent } from '../cosmo-table.component';

export abstract class CosmoTableValidatorHelper {

  public static validateColumnsSpecification(
      columns: ICosmoTableColumn[], component: CosmoTableComponent, config: ICosmoTableConfiguration) {
    const err = CosmoTableValidatorHelper._validateColumnsSpecification(columns, component, config);
    if (err) {
      throw new Error(err);
    }
    return null;
  }

  private static _validateColumnsSpecification(
    columns: ICosmoTableColumn[], component: CosmoTableComponent, config: ICosmoTableConfiguration) {

    if (!columns || !columns.length) {
      return 'A non-empty list of columns must be provided';
    }

    for (const col of columns) {
      if ( !col.name ) {
        return 'Columns need to have a non-empty name.';
      }
    }

    for (const col of columns) {
      if ( (CosmoTableFormatterHelper.isFilterable(col, config) || CosmoTableFormatterHelper.isHideable(col, config) )
      && !CosmoTableFormatterHelper.hasLabel(col, component, config) ) {
        return 'Columns to be filtered or toggled must have either a labelKey or a label. ' +
          'To hide a column label use the showLabelInTable property. Offending column is [' + col.name + ']';
      }
    }

    if (columns.filter(c => !CosmoTableFormatterHelper.isHideable(c, config)).length < 1) {
      return 'At least one of the columns must be not hideable: set canHide=false on primary or relevant column.';
    }

    return null;
  }
}
