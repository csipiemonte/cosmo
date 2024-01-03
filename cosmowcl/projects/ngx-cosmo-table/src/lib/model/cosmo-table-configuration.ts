import {
  CosmoTableActionActivationCondition,
  CosmoTableActionVisibilityCondition,
} from './cosmo-table-action.model';
import {
  CosmoTableActionPosition,
  CosmoTablePaginationMethod,
  CosmoTableRefreshStrategy,
  CosmoTableRowHeaderPosition,
  ICosmoTableDialogueProvider,
  ICosmoTableLoadingAnimationProvider,
} from './cosmo-table-entities.model';

export interface ICosmoTableConfiguration {
  header?: ICosmoTableHeaderConfiguration;
  sort?: ICosmoTableSortConfiguration;
  rowSelection?: ICosmoTableRowSelectionConfiguration;
  columnToggling?: ICosmoTableColumnTogglingConfiguration;
  filtering?: ICosmoTableFilteringConfiguration;
  contextFiltering?: ICosmoTableContextFilteringConfiguration;
  headerFiltering?: ICosmoTableContextFilteringConfiguration;
  pagination?: ICosmoTablePaginationConfiguration;
  actions?: ICosmoTableActionsConfiguration;
  columns?: ICosmoTableColumnsConfiguration;
  rowExpansion?: ICosmoTableRowExpansionConfiguration;
  itemTracking?: ICosmoTableItemTrackingConfiguration;
  storePersistence?: ICosmoTableStorePersistenceConfiguration;
  dragAndDrop?: ICosmoTableDnDConfiguration;
  refresh?: ICosmoTableRefreshConfiguration;
  export?: ICosmoTableExportConfiguration;
  currentSchemaVersion?: string;
}

export interface ICosmoTableHeaderConfiguration {
  enabledByDefault?: boolean;
  rowHeaderEnabledByDefault?: boolean;
  rowHeaderDefaultPosition?: CosmoTableRowHeaderPosition;
}

export interface ICosmoTableSortConfiguration {
  enabledByDefault?: boolean;
}

export interface ICosmoTableExportConfiguration {
  enabledByDefault?: boolean;
  loadingAnimationProvider?: ICosmoTableLoadingAnimationProvider;
  maxResults?: number;
  dialogueProvider?: ICosmoTableDialogueProvider;
}

export interface ICosmoTableRefreshConfiguration {
  defaultStrategy?: CosmoTableRefreshStrategy;
  defaultInterval?: number;
  defaultOnPushInBackground?: boolean;
  defaultOnTickInBackground?: boolean;
}

export interface ICosmoTableRowSelectionConfiguration {
  enabledByDefault?: boolean;
  selectAllEnabledByDefault?: boolean;
  multipleSelectionEnabledByDefault?: boolean;
}

export interface ICosmoTablePaginationConfiguration {
  enabledByDefault?: boolean;
  pageSizeSelectionEnabledByDefault?: boolean;
  defaultPaginationMode?: CosmoTablePaginationMethod;
  defaultPageSize?: number;
  defaultPossiblePageSizes?: number[];
}

export interface ICosmoTableFilteringConfiguration {
  enabledByDefault?: boolean;
  autoReloadOnQueryClear?: boolean;
  autoReloadOnQueryChange?: boolean;
  autoReloadTimeout?: number;
  showUnfilterableColumns?: boolean;
}

export interface ICosmoTableDnDConfiguration {
  dragEnabledByDefault?: boolean;
  dropEnabledByDefault?: boolean;
}

export interface ICosmoTableContextFilteringConfiguration {
  enabledByDefault?: boolean;
}

export interface ICosmoTableHeaderFilteringConfiguration {
  enabledByDefault?: boolean;
}

export interface ICosmoTableItemTrackingConfiguration {
  enabledByDefault?: boolean;
}

export interface ICosmoTableStorePersistenceConfiguration {
  enabledByDefault?: boolean;
}

export interface ICosmoTableRowExpansionConfiguration {
  enabledByDefault?: boolean;
  multipleExpansionEnabledByDefault?: boolean;
  manualExpansionEnabledByDefault?: boolean;
}

export interface ICosmoTableColumnTogglingConfiguration {
  enabledByDefault?: boolean;
  showFixedColumns?: boolean;
}

export interface ICosmoTableActionsConfiguration {
  contextActionsEnabledByDefault?: boolean;
  headerActionsEnabledByDefault?: boolean;
  defaultHeaderActionActivationCondition?: CosmoTableActionActivationCondition;
  defaultContextActionActivationCondition?: CosmoTableActionActivationCondition;
  contextActionsPositions?: CosmoTableActionPosition[];
  headerActionsPositions?: CosmoTableActionPosition[];
  defaultContextActionVisibilityCondition?: CosmoTableActionVisibilityCondition;
  defaultHeaderActionVisibilityCondition?: CosmoTableActionVisibilityCondition;
}

export interface ICosmoTableColumnsConfiguration {
  defaultShowLabelInTable?: boolean;
  defaultCanSort?: boolean;
  defaultCanHide?: boolean;
  defaultCanFilter?: boolean;
  defaultIsDefaultFilter?: boolean;
  defaultIsDefaulView?: boolean;
}

export const COLUMN_DEFAULTS = {
  SHOW_LABEL_IN_TABLE: true,
  CAN_SORT: false,
  CAN_HIDE: true,
  CAN_FILTER: true,
  DEFAULT_FILTER: true,
  DEFAULT_VIEW: true
};
