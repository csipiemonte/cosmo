import { Component } from '@angular/core';

import {
  CosmoTableActionActivationCondition,
  CosmoTablePaginationMethod,
  CosmoTableRefreshStrategy,
  CosmoTableService,
} from 'projects/ngx-cosmo-table/src/public-api';

import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'ngx-cosmo-table-sample';

  constructor(
    private translate: TranslateService,
    private cosmoTableService: CosmoTableService) {

    this.translate.setDefaultLang('en');

    this.cosmoTableService
    .configure({
      currentSchemaVersion: '0.0.1',
      refresh: {
        defaultStrategy: CosmoTableRefreshStrategy.NONE,
        defaultInterval: 60000,
        defaultOnPushInBackground: true,
        defaultOnTickInBackground: true
      },
      rowSelection: {
        enabledByDefault: false,
        selectAllEnabledByDefault: true,
        multipleSelectionEnabledByDefault: true
      },
      columnToggling: {
        enabledByDefault: false,
        showFixedColumns: true
      },
      filtering: {
        enabledByDefault: false,
        autoReloadOnQueryClear: true,
        autoReloadOnQueryChange: true,
        autoReloadTimeout: 300,
        showUnfilterableColumns: true
      },
      contextFiltering: {
        enabledByDefault: false
      },
      pagination: {
        enabledByDefault: false,
        pageSizeSelectionEnabledByDefault: true,
        defaultPaginationMode: CosmoTablePaginationMethod.CLIENT,
        defaultPageSize: 10,
        defaultPossiblePageSizes: [10, 25, 50]
      },
      actions: {
        contextActionsEnabledByDefault: false,
        headerActionsEnabledByDefault: false,
        defaultHeaderActionActivationCondition: CosmoTableActionActivationCondition.ALWAYS,
        defaultContextActionActivationCondition: CosmoTableActionActivationCondition.MULTIPLE_SELECTION
      },
      columns: {
        defaultShowLabelInTable: true,
        defaultCanSort: false,
        defaultCanHide: true,
        defaultCanFilter: false,
        defaultIsDefaultFilter: false,
        defaultIsDefaulView: true
      },
      rowExpansion: {
        enabledByDefault: false,
        multipleExpansionEnabledByDefault: false
      },
      dragAndDrop: {
        dragEnabledByDefault: false,
        dropEnabledByDefault: false
      },
      itemTracking: {
        enabledByDefault: false
      },
      storePersistence: {
        enabledByDefault: false
      },
      export: {
        enabledByDefault: false,
        loadingAnimationProvider: {
          start: () => console.log('starting to fetch data for export ...'),
          stop: () => console.log('finished fetching data for export')
        }
      }
    });
  }
}
