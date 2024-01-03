import { ICosmoTableColumn } from './cosmo-table-column.model';
import { ICosmoTableContextFilter } from './cosmo-table-context-filter.model';
import { ICosmoTableStatusSnapshot } from './cosmo-table-entities.model';

export interface ICosmoTableBridge {
    getTableContext(): any;
    getStatusSnapshot(): ICosmoTableStatusSnapshot | null;
    getColumns(): ICosmoTableColumn[] | null;
    getCheckedColumnsForVisualization(): ICosmoTableColumn[] | null;
    getParsedColumns(): ICosmoTableColumn[] | null;
    getLastFetchedQuery(): string | null;
    getCheckedColumnsForFiltering(): ICosmoTableColumn[] | null;
    getCheckedHeaderFilters(): ICosmoTableContextFilter[] | null;
    getFetchedPageCount(): number | null;
    getFetchedResultNumber(): number | null;
    getSelectedPageIndex(): number | null;
    getSelectedPageSize(): number | null;
    getDefaultPageSize(): number | null;
    getPossiblePageSizes(): number[] | null;
    getCurrentSearchQuery(): string | null;
    visibleColumnsSelectionChanged(payload: ICosmoTableColumn[]): void;
    filteringChanged(query: string | null, columns: ICosmoTableColumn[]): void;
    checkedFiltersChanged(payload: ICosmoTableContextFilter[]): void;
    pageChanged(pageIndex: number): void;
    pageSizeChanged(pageSize: number): void;
}
