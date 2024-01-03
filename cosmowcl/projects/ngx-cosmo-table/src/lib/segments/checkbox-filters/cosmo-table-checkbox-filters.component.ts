import {
  Component, EventEmitter, Input, Output
} from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { ICosmoTableContextFilter } from '../../model';
import { CosmoTableService } from '../../services';
import { CosmoTableLogger } from '../../utils';
import { CosmoTableSegment } from '../cosmo-table-segment.proto';

@Component({
  // tslint:disable-next-line: component-selector
  selector: 'cosmo-table-checkbox-filters',
  templateUrl: './cosmo-table-checkbox-filters.component.html',
  styleUrls: ['./cosmo-table-checkbox-filters.component.scss']
})
export class CosmoTableCheckboxFiltersComponent extends CosmoTableSegment {

  private logger: CosmoTableLogger;

  @Input() filters: ICosmoTableContextFilter[] | null = null;

  @Input() labelGroup: string | string[] | null = null;
  @Input() enable: boolean | null = null;

  @Output() filtersChange = new EventEmitter<ICosmoTableContextFilter[]>();

  constructor(
    private translateService: TranslateService,
    private configurationService: CosmoTableService
  ) {
    super();
    this.logger = new CosmoTableLogger('CosmoTableCheckboxFiltersComponent');
    this.logger.trace('building component');
  }

  get render(): boolean {
    return super.render && this.headerFilteringEnabledAndPossible;
  }

  private resolveCheckedHeaderFilters(): ICosmoTableContextFilter[] {
    return this.getBridge()?.getCheckedHeaderFilters() ?? [];
  }

  get currentEnableHeaderFiltering(): boolean {
    return this.enable ?? true;
  }

  get headerFilteringEnabledAndPossible(): boolean {
    return !!this.filters && this.filters.length > 0 &&
      this.currentEnableHeaderFiltering;
  }

  get currentHeaderFilters(): ICosmoTableContextFilter[] {
    return this.filters || [];
  }

  isHeaderFilterChecked(item: ICosmoTableContextFilter) {
    return (this.resolveCheckedHeaderFilters() || []).indexOf(item) !== -1;
  }

  toggleHeaderFilterChecked(item: ICosmoTableContextFilter) {
    if (!this.currentEnableHeaderFiltering) {
      return;
    }

    let output: ICosmoTableContextFilter[] = [];
    for (const o of this.resolveCheckedHeaderFilters()) {
      output.push(o);
    }

    if (this.isHeaderFilterChecked(item)) {
      output.splice(output.indexOf(item), 1);
    } else {
      output.push(item);
      if (item.group) {
        output = output.filter(o => {
          return (o.name === item.name) || (!o.group) || (o.group !== item.group);
        });
      }
    }

    const b = this.getBridge();
    if (b) {
      b.checkedFiltersChanged(output);
    }
    this.filtersChange.emit(output);
  }

  resolveLabel(column: ICosmoTableContextFilter) {
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

}
