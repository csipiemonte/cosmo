import {
  Component, EventEmitter, Input, Output
} from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { ICosmoTableColumn } from '../../model';
import { CosmoTableService } from '../../services';
import { CosmoTableFormatterHelper, CosmoTableLogger } from '../../utils';
import { CosmoTableSegment } from '../cosmo-table-segment.proto';

@Component({
  // tslint:disable-next-line: component-selector
  selector: 'cosmo-table-column-selector',
  templateUrl: './cosmo-table-column-selector.component.html',
  styleUrls: ['./cosmo-table-column-selector.component.scss']
})
export class CosmoTableColumnSelectorComponent extends CosmoTableSegment {

  private logger: CosmoTableLogger;

  @Input() labelGroup: string | string[] | null = null;
  @Input() enable: boolean | null = null;

  @Output() visibleColumnsChange = new EventEmitter<ICosmoTableColumn[]>();

  constructor(
    private translateService: TranslateService,
    private configurationService: CosmoTableService
  ) {
    super();
    this.logger = new CosmoTableLogger('CosmoTableColumnSelectorComponent');
    this.logger.trace('building component');
  }

  get render(): boolean {
    return super.render && this.columnSelectionPossibleAndAllowed;
  }

  private resolveColumns(): ICosmoTableColumn[] | null {
    return this.getBridge()?.getColumns() ?? null;
  }

  private resolveCheckedColumnsForVisualization(): ICosmoTableColumn[] | null {
    return this.getBridge()?.getCheckedColumnsForVisualization() ?? null;
  }

  get columnSelectionPossibleAndAllowed(): boolean {
    return this.currentEnableColumnsSelection && this.parsedColumns.length > 0 && this.hideableColumns.length > 0;
  }

  get currentEnableColumnsSelection(): boolean {
    return this.enable ?? true;
  }

  get configColumnVisibilityShowFixedColumns() {
    return this.configurationService.getConfiguration().columnToggling?.showFixedColumns;
  }

  get compatibilityModeForDropDowns(): boolean {
    return false;
  }

  get hideableColumns(): ICosmoTableColumn[] {
    return this.parsedColumns.filter(c => CosmoTableFormatterHelper.isHideable(c,
      this.configurationService.getConfiguration()));
  }

  get allVisibleColumnsChecked(): boolean {
    const dataItems = this.hideableColumns;
    if (!dataItems.length) {
      return false;
    }
    for (const item of dataItems) {
      if ((this.resolveCheckedColumnsForVisualization() || []).indexOf(item) === -1) {
        return false;
      }
    }
    return true;
  }

  get parsedColumns(): ICosmoTableColumn[] {
    const output = [];
    const usedNames = [];
    for (const col of (this.resolveColumns() || [])) {
      output.push(col);
      if (!col.name) {
        col.name = col.field || col.serverField;
      }
      if (!col.name) {
        throw new Error('Column has no name');
      } else {
        if (usedNames.indexOf(col.name) !== -1) {
          throw new Error('Duplicate column name "' + col.name + '"');
        } else {
          usedNames.push(col.name);
        }
      }
    }
    return output;
  }

  isHideable(column: ICosmoTableColumn): boolean | undefined {
    return CosmoTableFormatterHelper.isHideable(column, this.configurationService.getConfiguration());
  }

  hasLabel(column: ICosmoTableColumn) {
    return CosmoTableFormatterHelper.hasLabel(
      column,
      this.table,
      this.configurationService.getConfiguration());
  }

  isColumnCheckedForVisualization(item: ICosmoTableColumn) {
    return (this.resolveCheckedColumnsForVisualization() || []).indexOf(item) !== -1;
  }

  resolveLabel(column: ICosmoTableColumn) {
    let resolved = null;
    if (typeof column.label !== 'undefined' && column.label === null) {
      return null;
    }

    const labelPostfix = (column.name || (column as any).field || null);

    if (column.labelKey) {
      resolved = this.translateService.instant(column.labelKey);
      if (resolved && resolved !== column.labelKey) {
        return resolved;
      }
    }

    if (column.label) {
      return column.label;
    }

    if (this.labelGroup) {
      for (const labelPrefix of (Array.isArray(this.labelGroup) ? this.labelGroup : [this.labelGroup])) {
        const labelAttempt = labelPrefix + '.' + labelPostfix;
        resolved = this.translateService.instant(labelAttempt);
        if (resolved && resolved !== labelAttempt) {
          return resolved;
        }
      }
    }

    return (column.name || (column as any).field || null);
  }

  toggleAllVisibleColumnsChecked() {
    if (!this.currentEnableColumnsSelection) {
      return;
    }

    let newColumns: ICosmoTableColumn[] = [];

    if (this.allVisibleColumnsChecked) {
      newColumns = this.parsedColumns.filter(c =>
        !CosmoTableFormatterHelper.isHideable(c, this.configurationService.getConfiguration()));
    } else {
      newColumns = [];
      for (const el of this.parsedColumns) {
        newColumns.push(el);
      }
    }

    const b = this.getBridge();
    if (b) {
      b.visibleColumnsSelectionChanged(newColumns);
    }
    this.visibleColumnsChange.emit(newColumns);
  }

  toggleVisibleColumnChecked(item: ICosmoTableColumn) {
    if (!CosmoTableFormatterHelper.isHideable(item, this.configurationService.getConfiguration()) || !this.currentEnableColumnsSelection) {
      return;
    }

    const newColumns: ICosmoTableColumn[] = [];
    for (const c of this.resolveCheckedColumnsForVisualization() ?? []) {
      newColumns.push(c);
    }

    if (this.isColumnCheckedForVisualization(item)) {
      newColumns.splice(newColumns.indexOf(item), 1);
    } else {
      newColumns.push(item);
    }

    const b = this.getBridge();
    if (b) {
      b.visibleColumnsSelectionChanged(newColumns);
    }
    this.visibleColumnsChange.emit(newColumns);
  }
}
