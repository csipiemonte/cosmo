import {
  Component, EventEmitter, Input, Output
} from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { ICosmoTableColumn } from '../../model';
import { CosmoTableService } from '../../services';
import { CosmoTableFormatterHelper, CosmoTableLogger } from '../../utils';
import { CosmoTableSegment } from '../cosmo-table-segment.proto';

@Component({
  // tslint:disable-next-line: component-selector
  selector: 'cosmo-table-search-filter',
  templateUrl: './cosmo-table-search-filter.component.html',
  styleUrls: ['./cosmo-table-search-filter.component.scss']
})
export class CosmoTableSearchFilterComponent extends CosmoTableSegment {

  private logger: CosmoTableLogger;

  @Input() enable: boolean | null = null;
  @Input() labelGroup: string | string[] | null = null;
  @Input() enableSearchIn: boolean | null = null;

  selectedSearchQuery: string | null = null;
  searchQueryChanged: Subject<string> = new Subject<string>();

  constructor(
    private translateService: TranslateService,
    private configurationService: CosmoTableService
  ) {
    super();
    this.logger = new CosmoTableLogger('CosmoTableSearchFilterComponent');
    this.logger.trace('building component');

    this.searchQueryChanged.pipe(
      debounceTime(500),
      distinctUntilChanged())
      .subscribe(() => {
        this.searchQueryChangedDebounced();
      });

    if (this.table) {
      this.selectedSearchQuery = this.currentActiveSearchQueryOnTable;
    }
  }

  get localQueryOrEffectiveQueryOnTable(): string | null {
    return this.selectedSearchQuery ?? this.currentActiveSearchQueryOnTable;
  }

  get render(): boolean {
    return super.render && this.filteringPossibleAndAllowed;
  }

  get filteringPossibleAndAllowed(): boolean {
    return this.enable ?? true;
  }

  get currentEnableFiltering(): boolean {
    return this.enable ?? true;
  }

  get currentActiveSearchQueryOnTable(): string | null {
    return this.getBridge()?.getCurrentSearchQuery() ?? null;
  }

  private resolveCheckedColumnsForFiltering(): ICosmoTableColumn[] | null {
    return this.getBridge()?.getCheckedColumnsForFiltering() ?? null;
  }

  get currentSearchQuery(): string | null {
    if (this.selectedSearchQuery) {
      return this.selectedSearchQuery;
    } else {
      return null;
    }
  }

  get searchQueryActive(): boolean {
    if (!this.currentSearchQuery) {
      return false;
    }
    if (this.currentEnableSearchIn && !(this.resolveCheckedColumnsForFiltering()?.length)) {
      return false;
    }
    return true;
  }

  get searchQueryMalformed(): boolean {
    if (!this.currentSearchQuery) {
      return false;
    }
    if (this.currentEnableSearchIn && !(this.resolveCheckedColumnsForFiltering()?.length)) {
      return true;
    }
    return false;
  }

  get lastFetchedSearchQuery(): string | null {
    return this.getBridge()?.getLastFetchedQuery() ?? null;
  }

  searchQueryFocusOut() {
    const comp = this;
    setTimeout(() => {
      if ((comp.selectedSearchQuery || '') !== (comp.lastFetchedSearchQuery || '')) {
        if (comp.selectedSearchQuery) {
          if (this.configurationService.getConfiguration().filtering?.autoReloadOnQueryChange) {
            comp.applySelectedFilter();
          }
        } else {
          if (this.configurationService.getConfiguration().filtering?.autoReloadOnQueryClear) {
            comp.applySelectedFilter();
          }
        }
      }
    }, this.configurationService.getConfiguration().filtering?.autoReloadTimeout);
  }

  onSearchQueryChanged(event: KeyboardEvent): void {
    if (event) {
      this.searchQueryChanged.next(this.selectedSearchQuery || undefined);
    }
  }

  searchQueryChangedDebounced(): void {
    if (this.selectedSearchQuery !== null && this.selectedSearchQuery !== undefined
      && this.selectedSearchQuery?.length >= 0) {
      this.applySelectedFilter();
    }
  }

  get searchQueryNeedApply(): boolean {
    return (this.currentSearchQuery || '') !== (this.lastFetchedSearchQuery || '');
  }

  get noFilteringColumnshecked(): boolean {
    return !this.anyFilteringColumnsChecked;
  }

  get compatibilityModeForDropDowns(): boolean {
    return false;
  }

  get configFilteringShowUnfilterableColumns() {
    return this.configurationService.getConfiguration().filtering?.showUnfilterableColumns;
  }

  get parsedColumns(): ICosmoTableColumn[] {
    return this.getBridge()?.getParsedColumns() ?? [];
  }

  get filterableColumns(): ICosmoTableColumn[] {
    return this.parsedColumns.filter(c => this.isFilterable(c));
  }

  get anyFilteringColumnsChecked(): boolean {
    const dataItems = this.filterableColumns;
    if (!dataItems.length) {
      return false;
    }
    for (const item of dataItems) {
      if ((this.resolveCheckedColumnsForFiltering() || []).indexOf(item) !== -1) {
        return true;
      }
    }
    return false;
  }

  get allFilteringColumnsChecked(): boolean {
    const dataItems = this.filterableColumns;
    if (!dataItems.length) {
      return false;
    }
    for (const item of dataItems) {
      if ((this.resolveCheckedColumnsForFiltering() || []).indexOf(item) === -1) {
        return false;
      }
    }
    return true;
  }

  toggleFilteringColumnChecked(item: ICosmoTableColumn) {
    if (!this.currentEnableFiltering) {
      return;
    }

    if (!this.isFilterable(item)) {
      return;
    }

    const payload: ICosmoTableColumn[] = [];
    for (const el of this.resolveCheckedColumnsForFiltering() ?? []) {
      payload.push(el);
    }

    if (this.isColumnCheckedForFiltering(item)) {
      payload.splice(payload.indexOf(item), 1);
    } else {
      payload.push(item);
    }

    this.columnsChanged(payload);
  }

  toggleAllFilteringColumnsChecked() {
    if (!this.currentEnableFiltering) {
      return;
    }

    let payload: ICosmoTableColumn[];

    if (this.allFilteringColumnsChecked) {
      payload = [];
    } else {
      payload = [];
      for (const el of this.filterableColumns) {
        payload.push(el);
      }
    }

    this.columnsChanged(payload);
  }

  private columnsChanged(columns: ICosmoTableColumn[]): void {
    this.getBridge()?.filteringChanged(
      this.selectedSearchQuery,
      columns
    );
  }

  applySelectedFilter() {
    this.getBridge()?.filteringChanged(
      this.selectedSearchQuery,
      this.resolveCheckedColumnsForFiltering() || []
    );
  }

  isFilterable(column: ICosmoTableColumn) {
    return CosmoTableFormatterHelper.isFilterable(column, this.configurationService.getConfiguration());
  }

  hasLabel(column: ICosmoTableColumn) {
    return CosmoTableFormatterHelper.hasLabel(column, this.table, this.configurationService.getConfiguration());
  }

  isColumnCheckedForFiltering(item: ICosmoTableColumn) {
    return (this.resolveCheckedColumnsForFiltering() || []).indexOf(item) !== -1;
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

  get currentEnableSearchIn(): boolean {
    return this.enableSearchIn ?? true;
  }

}
