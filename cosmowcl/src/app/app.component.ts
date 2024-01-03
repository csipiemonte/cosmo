/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { registerLocaleData } from '@angular/common';
import localeEN from '@angular/common/locales/en';
import localeIT from '@angular/common/locales/it';
import {
  Component,
  HostListener,
} from '@angular/core';

import { SpinnerVisibilityService } from 'ng-http-loader';
import {
  CosmoTableActionActivationCondition,
  CosmoTablePaginationMethod,
  CosmoTableRefreshStrategy,
  CosmoTableService,
} from 'ngx-cosmo-table';
import { NGXLogger } from 'ngx-logger';
import { environment } from 'src/environments/environment';

import { TranslateService } from '@ngx-translate/core';

import { Constants } from './shared/constants/constants';
import { ModalService } from './shared/services/modal.service';
import { RedirectService } from './shared/services/redirect.service';
import { Utils } from './shared/utilities/utilities';
import { Router } from '@angular/router';

declare global {
  interface Window { __PUBLIC_PATH__: any; }
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {



  constructor(
    private translate: TranslateService,
    private redirectService: RedirectService,
    private cosmoTableService: CosmoTableService,
    private logger: NGXLogger,
    private spinner: SpinnerVisibilityService,
    private modalService: ModalService,
    private router: Router,
  ) {
    this.translate.addLangs(['it']);
    this.translate.setDefaultLang('it');
    this.translate.use(Utils.getParameterByName('lang') ?? 'it');

    registerLocaleData(localeIT, 'it');
    registerLocaleData(localeEN, 'en');

    if (environment.environmentName !== 'local' && environment.environmentName !== 'dev-eng'  ) {
      window.__PUBLIC_PATH__ = '/cosmo' + window.__PUBLIC_PATH__;
    }

    this.redirectService.checkForQueryParam();

    this.cosmoTableService
    .withLogAdapter(this.logger)
    .configure({
      currentSchemaVersion: Constants.STORAGE_SCHEMA_VERSION,
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
        defaultPossiblePageSizes: [5, 10, 25, 50]
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
      sort: {
        enabledByDefault: false
      },
      export: {
        enabledByDefault: false,
        maxResults: 1000,
        loadingAnimationProvider: {
          start: () => this.spinner.show(),
          stop: () => this.spinner.hide()
        },
        dialogueProvider: {
          error: (title: string, message: string) => {
            this.modalService.error(title, Utils.toMessage(message));
          }
        },
      }
    });
  }

  private scrollYThreshold = 200;
  addPadding = false;

  title = 'cosmowcl';
  showHelperWarning = false;

  @HostListener('window:scroll', ['$event']) onScroll(event: any) {
    if (event.path && event.path.length > 1) {
      this.addPadding = event.path[1].scrollY > this.scrollYThreshold;
    }
  }

  onActivate(event: any) {
    window.scroll(0, 0);
  }

  hasPageHelper(event: boolean) {
    const listInternalEnv = 'local';
    const isSystemAdminPath = this.router.url.match(/(amministrazione|configurazione)/);
    if (listInternalEnv.match(environment.environmentName)) {
    this.showHelperWarning = !event && !isSystemAdminPath;
    }
  }
}
