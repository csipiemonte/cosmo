import { Injectable } from '@angular/core';
import { CosmoTableLogger, CosmoTableBrowserHelper, ICosmoTableLogAdapter } from '../utils';
import {
  CosmoTableActionActivationCondition,
  CosmoTablePaginationMethod,
  CosmoTableRefreshStrategy,
  ICosmoTableConfiguration,
  CosmoTableRowHeaderPosition,
  CosmoTableActionPosition,
  CosmoTableActionVisibilityCondition
} from '../model';
import { CosmoTableLoggerService } from './cosmo-table-logger.service';


@Injectable({providedIn: 'root'})
export class CosmoTableService {

  private logger: CosmoTableLogger;
  private configuration: ICosmoTableConfiguration;

  constructor(private cosmoTableLoggerService: CosmoTableLoggerService) {
    this.logger = new CosmoTableLogger('CosmoTableService');
    this.logger.trace('building service');
    this.configuration = this.buildDefaultConfiguration();

    this.logger.debug('initializing Cosmotable component for browser', CosmoTableBrowserHelper.getBrowser());
    this.cosmoTableLoggerService.withLogAdapter(null);
  }

  public configure(config: ICosmoTableConfiguration): CosmoTableService {
    if (config) {
      this.mergeDeep(this.configuration, config);
    }
    return this;
  }

  public withLogAdapter(logAdapter: ICosmoTableLogAdapter): CosmoTableService {
    this.cosmoTableLoggerService.withLogAdapter(logAdapter);
    return this;
  }

  public getConfiguration(): ICosmoTableConfiguration {
    return this.configuration;
  }

  private mergeDeep(target: any, ...sources: any): any {
    if (!sources.length) {
      return target;
    }
    const source = sources.shift();

    if (target !== null && typeof target === 'object' && source !== null && typeof source === 'object') {
      for (const key in source) {
        if (source[key] !== null && typeof source[key] === 'object') {
          if (!target[key]) {
            Object.assign(target, { [key]: {} });
          }
          this.mergeDeep(target[key], source[key]);
        } else {
          Object.assign(target, { [key]: source[key] });
        }
      }
    }

    return this.mergeDeep(target, ...sources);
  }

  private buildDefaultConfiguration(): ICosmoTableConfiguration {
    return {
      currentSchemaVersion: 'DEFAULT',
      refresh: {
        defaultStrategy: CosmoTableRefreshStrategy.NONE,
        defaultInterval: 60000,
        defaultOnPushInBackground: true,
        defaultOnTickInBackground: true
      },
      header: {
        enabledByDefault: true,
        rowHeaderEnabledByDefault: false,
        rowHeaderDefaultPosition: CosmoTableRowHeaderPosition.TOP
      },
      rowSelection: {
        enabledByDefault: false,
        selectAllEnabledByDefault: true,
        multipleSelectionEnabledByDefault: true
      },
      sort: {
        enabledByDefault: true,
      },
      columnToggling: {
        enabledByDefault: true,
        showFixedColumns: true
      },
      filtering: {
        enabledByDefault: true,
        autoReloadOnQueryClear: true,
        autoReloadOnQueryChange: true,
        autoReloadTimeout: 300,
        showUnfilterableColumns: true
      },
      contextFiltering: {
        enabledByDefault: true
      },
      headerFiltering: {
        enabledByDefault: true
      },
      pagination: {
        enabledByDefault: true,
        pageSizeSelectionEnabledByDefault: true,
        defaultPaginationMode: CosmoTablePaginationMethod.CLIENT,
        defaultPageSize: 10,
        defaultPossiblePageSizes: [10, 25, 50]
      },
      actions: {
        contextActionsEnabledByDefault: true,
        headerActionsEnabledByDefault: true,
        defaultHeaderActionActivationCondition: CosmoTableActionActivationCondition.ALWAYS,
        defaultContextActionActivationCondition: CosmoTableActionActivationCondition.MULTIPLE_SELECTION,
        contextActionsPositions: [CosmoTableActionPosition.TOP, CosmoTableActionPosition.BOTTOM],
        headerActionsPositions: [CosmoTableActionPosition.TOP, CosmoTableActionPosition.BOTTOM],
        defaultContextActionVisibilityCondition: CosmoTableActionVisibilityCondition.ALWAYS,
        defaultHeaderActionVisibilityCondition: CosmoTableActionVisibilityCondition.ALWAYS
      },
      columns: {
        defaultShowLabelInTable: true,
        defaultCanSort: true,
        defaultCanHide: true,
        defaultCanFilter: false,
        defaultIsDefaultFilter: true,
        defaultIsDefaulView: true
      },
      rowExpansion: {
        enabledByDefault: false,
        multipleExpansionEnabledByDefault: false,
        manualExpansionEnabledByDefault: true
      },
      dragAndDrop: {
        dragEnabledByDefault: false,
        dropEnabledByDefault: false
      },
      itemTracking: {
        enabledByDefault: false
      },
      storePersistence: {
        enabledByDefault: true
      }
    };
  }
}
