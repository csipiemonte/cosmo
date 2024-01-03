import { CdkDragDrop } from '@angular/cdk/drag-drop';

import { Observable } from 'rxjs';

import { CosmoTableComponent } from '../cosmo-table.component';
import { ICosmoTableAction } from './cosmo-table-action.model';
import { ICosmoTableColumn } from './cosmo-table-column.model';

export enum CosmoTableSortDirection {
  ASCENDING = 'ASC',
  DESCENDING = 'DESC'
}

export interface ICosmoTableSort {
  property: string | null;
  direction?: CosmoTableSortDirection;
}

export interface ICosmoTablePageRequest {
  page?: number | null;
  size?: number | null;
  sort?: (ICosmoTableSort | null)[];
  query?: string | null;
  queryFields?: (string | null)[];
  filters?: (string | null)[];
  context?: any;
}

export interface ICosmoTablePageResponse {
  content: any[]; // Returns the page content
  number?: number; // Returns the number of the current Slice.
  numberOfElements?: number; // Returns the number of elements currently on this Slice.
  size?: number; // Returns the size of the Slice.
  sort?: ICosmoTableSort[]; // Returns the sorting parameters for the Slice.
  totalElements?: number; // Returns the total amount of elements.
  totalPages?: number; // Returns the number of total pages.
}

export enum CosmoTablePaginationMethod {
  CLIENT = 'CLIENT',
  SERVER = 'SERVER'
}

export enum CosmoTableFormatter {
  UPPERCASE = 'UPPERCASE',
  LOWERCASE = 'LOWERCASE',
  DATE = 'DATE',
  CURRENCY = 'CURRENCY'
}

export const CosmoTableFormatterBuilder = {
  orDefault: (def?: any) => {
    return { format: (val: any) => val || def || '- -' };
  },
};

export enum CosmoTableRefreshStrategy {
  ON_PUSH = 'ON_PUSH',
  TIMED = 'TIMED',
  NONE = 'NONE'
}

export interface ICosmoTableFormatProvider {
  format: (input: any) => any;
}

export interface ICosmoTableFormatSpecification {
  formatter: CosmoTableFormatter;
  arguments: any;
}

export interface ICosmoTableSortingSpecification {
  column: ICosmoTableColumn | null;
  direction: CosmoTableSortDirection | null;
}

export interface ICosmoTableActionDispatchingContext {
  selectedItems: any[];
  action: ICosmoTableAction;
  status?: ICosmoTableStatusSnapshot | null;
  tableContext?: any;
}

// tslint:disable-next-line: no-empty-interface
export interface ICosmoTableActionDispatchingResult {
}

export interface ICosmoTableStatusSnapshot {
  schemaVersion: string;
  dataSnapshot?: any[] | null;
  orderColumn?: string | null;
  orderColumnDirection?: string;
  query?: string | null;
  queryColumns?: string[];
  visibleColumns?: string[];
  contextFilters?: string[];
  headerFilters?: string[];
  currentPage?: number;
  pageSize?: number;
  checkedItems?: any[];
  expandedItems?: any[];
  tableContext?: any;
}

export interface ICosmoTablePersistableStatusSnapshot {
  schemaVersion: string;
  orderColumn?: string | null;
  orderColumnDirection?: string;
  query?: string | null;
  queryColumns?: string[];
  visibleColumns?: string[];
  contextFilters?: string[];
  headerFilters?: string[];
  currentPage?: number;
  pageSize?: number;
  checkedItemIdentifiers?: any[];
  expandedItemIdentifiers?: any[];
}

export interface ICosmoTableStoreAdapterSaveContext {
  status: ICosmoTablePersistableStatusSnapshot | null;
}

export interface ICosmoTableStoreAdapterLoadContext {
  status: ICosmoTablePersistableStatusSnapshot | null;
}

export interface ICosmoTableStoreAdapter {
  save: (payload: ICosmoTableStoreAdapterSaveContext | null) => Observable<boolean>;
  load: () => Observable<ICosmoTablePersistableStatusSnapshot | null>;
}

export interface ICosmoTableIdentifierProvider {
  extract: (input: any) => any;
}

export interface ICosmoTableItemDraggedContext {
  item: any;
  event: CdkDragDrop<any>;
  fromDataSnapshot: any[] | null;
  toDataSnapshot: any[] | null;
  fromComponent: CosmoTableComponent;
  toComponent: CosmoTableComponent;
}

export interface ICosmoTableItemDroppedContext {
  item: any;
  event: CdkDragDrop<any>;
  fromDataSnapshot: any[] | null;
  toDataSnapshot: any[] | null;
  fromComponent: CosmoTableComponent | null;
  toComponent: CosmoTableComponent | null;
}

export interface ICosmoTablePushRefreshRequest {
  event?: any;
  inBackground?: boolean;
}

export enum CosmoTableReloadReason {
  INTERNAL = 'INTERNAL',
  INTERVAL = 'INTERVAL',
  USER = 'USER',
  PUSH = 'PUSH',
  EXTERNAL = 'EXTERNAL',
  EXPORT = 'EXPORT'
}

export interface ICosmoTableReloadContext {
  reason: CosmoTableReloadReason;
  inBackground?: boolean;
  withStatusSnapshot?: ICosmoTablePersistableStatusSnapshot | null;
  pushRequest?: ICosmoTablePushRefreshRequest | null;
}

export enum CosmoTableColumnSize {
  XXS = 'xxs',
  XS = 'xs',
  SMALL = 's',
  MEDIUM = 'm',
  LARGE = 'l',
  XL = 'xl',
  XXL = 'xxl',
  DEFAULT = 'default'
}

export enum CosmoTableRowHeaderPosition {
  TOP = 'TOP',
  TOP_INNER = 'TOP_INNER',
  BOTTOM = 'BOTTOM'
}

export enum CosmoTableActionPosition {
  TOP = 'TOP',
  BOTTOM = 'BOTTOM'
}

export interface ICosmoTableDataGroup {
  groupKey: any;
  data: any[];
}

export interface ICosmoTableLoadingAnimationProvider {
  start: () => void;
  stop: () => void;
}

export interface ICosmoTableDialogueProvider {
  error: (title: string, message: string) => void;
}
