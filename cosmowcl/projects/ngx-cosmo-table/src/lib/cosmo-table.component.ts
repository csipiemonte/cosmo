import {
  animate,
  state,
  style,
  transition,
  trigger,
} from '@angular/animations';
import {
  CdkDrag,
  CdkDragDrop,
} from '@angular/cdk/drag-drop';
import { HttpErrorResponse } from '@angular/common/http';
import {
  AfterContentInit,
  ChangeDetectorRef,
  Component,
  ContentChild,
  EventEmitter,
  Input,
  NgZone,
  OnChanges,
  OnDestroy,
  OnInit,
  Output,
  SimpleChanges,
  TemplateRef,
} from '@angular/core';

import $ from 'jquery';
import {
  Observable,
  of,
  Subject,
  Subscriber,
  Subscription,
  throwError,
  timer,
} from 'rxjs';
import {
  debounceTime,
  finalize,
  map,
  mergeMap,
  share,
} from 'rxjs/operators';

import { TranslateService } from '@ngx-translate/core';

import {
  CosmoTableActionActivationCondition,
  CosmoTableActionVisibilityCondition,
  CosmoTablePaginationMethod,
  CosmoTableRefreshStrategy,
  CosmoTableReloadReason,
  CosmoTableRowHeaderPosition,
  CosmoTableSortDirection,
  ICosmoTableAction,
  ICosmoTableActionDispatchingContext,
  ICosmoTableColumn,
  ICosmoTableContextFilter,
  ICosmoTableDataGroup,
  ICosmoTableIdentifierProvider,
  ICosmoTableItemDraggedContext,
  ICosmoTableItemDroppedContext,
  ICosmoTablePageRequest,
  ICosmoTablePageResponse,
  ICosmoTablePersistableStatusSnapshot,
  ICosmoTablePushRefreshRequest,
  ICosmoTableReloadContext,
  ICosmoTableSortingSpecification,
  ICosmoTableStatusSnapshot,
  ICosmoTableStoreAdapter,
} from './model';
import { ICosmoTableBridge } from './model/cosmo-table-bridge.model';
import {
  CosmoTableRegistryService,
  CosmoTableService,
} from './services';
import {
  CosmoTableBrowserHelper,
  CosmoTableFormatterHelper,
  CosmoTableInMemoryHelper,
  CosmoTableLogger,
  CosmoTableValidatorHelper,
} from './utils';
import { CosmoTableExporterXlsx } from './utils/cosmo-table-exporter-xlsx';

@Component({
  // tslint:disable-next-line: component-selector
  selector: 'cosmo-table',
  templateUrl: './cosmo-table.component.html',
  styleUrls: ['./cosmo-table.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0', visibility: 'hidden' })),
      state('expanded', style({ height: '*', visibility: 'visible' })),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class CosmoTableComponent implements OnInit, OnDestroy, OnChanges, AfterContentInit {

  private static counter = 0;

  public bridge: ICosmoTableBridge;

  // input parameters
  @Input() data: any[] | Observable<any> | Subject<any> | null = null;
  @Input() dataProvider: ((input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext)
    => Observable<ICosmoTablePageResponse>) | null = null;
  @Input() columns: ICosmoTableColumn[] | null = null;
  @Input() defaultSortingColumn: string | null = null;
  @Input() defaultSortingDirection: string | null = null;
  @Input() defaultPageSize: number | null = null;
  @Input() possiblePageSize: number[] | null = null;
  @Input() tableId: string | null = null;
  @Input() dropConnectedTo: string | null = null;
  @Input() paginationMode: CosmoTablePaginationMethod | null = null;
  @Input() enableSorting: boolean | null = null;
  @Input() enablePagination: boolean | null = null;
  @Input() enableSelection: boolean | null = null;
  @Input() enableContextFiltering: boolean | null = null;
  @Input() enableSelectAll: boolean | null = null;
  @Input() enableMultiSelect: boolean | null = null;
  @Input() enableRowExpansion: boolean | null = null;
  @Input() enableManualRowExpansion: boolean | null = null;
  @Input() enablePageSizeSelect: boolean | null = null;
  @Input() enableMultipleRowExpansion: boolean | null = null;
  @Input() enableItemTracking: boolean | null = null;
  @Input() enableStorePersistence: boolean | null = null;
  @Input() enableDrag: boolean | null = null;
  @Input() enableDrop: boolean | null = null;
  @Input() enableMainHeader: boolean | null = null;
  @Input() enableRowHeader: boolean | null = null;
  @Input() enableGrouping: boolean | null = null;
  @Input() enableExport: boolean | null = null;
  @Input() maxRowExport: number | null = null;
  @Input() exportHiddenColumns: boolean | null = null;
  @Input() rowHeaderPosition: CosmoTableRowHeaderPosition | null = null;
  @Input() contextFilters: ICosmoTableContextFilter[] | null = null;
  @Input() contextFilteringPrompt: string | null = null;
  @Input() actionVisibilityProvider: ((action: ICosmoTableAction, status: ICosmoTableStatusSnapshot | null) => boolean) | null = null;
  @Input() actionStatusProvider: ((action: ICosmoTableAction, status: ICosmoTableStatusSnapshot | null) => boolean) | null = null;
  @Input() expandableStatusProvider: ((row: any, status: ICosmoTableStatusSnapshot | null) => boolean) | null = null;
  @Input() selectableStatusProvider: ((row: any, status: ICosmoTableStatusSnapshot | null) => boolean) | null = null;
  @Input() rowClassProvider: ((row: any, status: ICosmoTableStatusSnapshot | null) => string) | null = null;
  @Input() storeAdapter: ICosmoTableStoreAdapter | null = null;
  @Input() itemIdentifier: string | ICosmoTableIdentifierProvider | null = null;
  @Input() refreshStrategy: CosmoTableRefreshStrategy | CosmoTableRefreshStrategy[] | null = null;
  @Input() refreshInterval: number | null = null;
  @Input() refreshEmitter: Observable<ICosmoTablePushRefreshRequest> | null = null;
  @Input() refreshIntervalInBackground: boolean | null = null;
  @Input() refreshOnPushInBackground: boolean | null = null;
  @Input() tableDisplayClass: string[] | null = null;
  @Input() dataProviderContext: any;
  @Input() tableContext: any;
  @Input() dataGrouper: ((row: any) => any) | null = null;
  @Input() labelGroup: string | string[] | null = null;
  @Input() forceRowExpansion: ((row: any, status: ICosmoTableStatusSnapshot | null) => boolean) | null = null;
  @Input() selectionWriteback: boolean | null = null;


  // output events
  @Output() pageChange = new EventEmitter<number>();
  @Output() sortChange = new EventEmitter<ICosmoTableSortingSpecification>();
  @Output() selectionChange = new EventEmitter<any[]>();
  @Output() visibleColumnsChange = new EventEmitter<ICosmoTableColumn[]>();
  @Output() contextFiltersChange = new EventEmitter<ICosmoTableContextFilter[]>();
  @Output() headerFiltersChange = new EventEmitter<ICosmoTableContextFilter[]>();
  @Output() statusChange = new EventEmitter<ICosmoTableStatusSnapshot>();
  @Output() itemDragged = new EventEmitter<ICosmoTableItemDraggedContext>();
  @Output() itemDropped = new EventEmitter<ICosmoTableItemDroppedContext>();
  @Output() action = new EventEmitter<ICosmoTableActionDispatchingContext>();
  @Output() allChecked = new EventEmitter<boolean>();
  @Output() itemChecked = new EventEmitter<[any, boolean]>();

  // input template
  @ContentChild('cellTemplate') cellTemplate: TemplateRef<any> | null = null;
  @ContentChild('rowDetailTemplate') rowDetailTemplate: TemplateRef<any> | null = null;
  @ContentChild('dragTemplate') dragTemplate: TemplateRef<any> | null = null;
  @ContentChild('dropTemplate') dropTemplate: TemplateRef<any> | null = null;
  @ContentChild('groupHeaderTemplate') groupHeaderTemplate: TemplateRef<any> | null = null;

  private isIE = CosmoTableBrowserHelper.isIE();

  private logger: CosmoTableLogger;
  private registrationId: string | null = null;
  private statusSnapshot: ICosmoTableStatusSnapshot | null = null;
  private persistableStatusSnapshot: ICosmoTableStatusSnapshot | null = null;

  private uuid: string;
  forceReRender = false;
  private initialized = false;

  // local data
  private clientDataSnapshot: any[] | null = null;
  dataSnapshot: any[] | null = null;
  dataSnapshotStr: string | null = null;
  groupedDataSnapshot: ICosmoTableDataGroup[] | null = null;

  private selectedPageIndex: number | null = null;
  private selectedPageSize: number | null = null;

  private selectedSortColumn: ICosmoTableColumn | null = null;
  private selectedSortDirection: CosmoTableSortDirection | null = null;

  selectedSearchQuery: string | null = null;
  private lastFetchedSearchQuery: string | null = null;

  // selected items with checkbox
  private checkedItems: any[] | null = null;

  // selected columns for filtering
  private checkedColumnsForFiltering: ICosmoTableColumn[] | null = null;

  // selected columns for visualization
  private checkedColumnsForVisualization: ICosmoTableColumn[] | null = null;

  // selected custom filters
  private checkedContextFilters: ICosmoTableContextFilter[] | null = null;

  // selected custom filters
  private checkedHeaderFilters: ICosmoTableContextFilter[] | null = null;

  // expanded rows
  private expandedItems: any[] | null = null;

  // for server pagination
  private fetchedPageCount: number | null = null;
  private fetchedResultNumber: number | null = null;
  fetching = false;
  showFetching = false;

  private refreshEmitterSubscription: Subscription | null = null;
  private refreshIntervalTimer: Observable<number> | null = null;
  private refreshIntervalSubscription: Subscription | null = null;
  private inputObservableSubscription: Subscription | null = null;

  public lastFetchError: any = null;

  // MatTable adapter
  expandedElement: any = null;

  // lifecycle hooks

  constructor(
    private translateService: TranslateService,
    private configurationService: CosmoTableService,
    private registry: CosmoTableRegistryService,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone ) {

    this.uuid = 'MTCMP-' + (++CosmoTableComponent.counter) + '-' + Math.round(Math.random() * 100000);
    this.logger = new CosmoTableLogger('CosmoTableComponent_' + this.currentTableId);
    this.logger.trace('building component');
    this.bridge = this.buildBridge();
  }

  isItemSelectable(item: any): boolean {
    if (!this.selectableStatusProvider) {
      return true;
    }
    return this.selectableStatusProvider(item, this.statusSnapshot);
  }

  ngOnInit() {
    this.logger = new CosmoTableLogger('CosmoTableComponent_' + this.currentTableId);
    this.logger.trace('initializing component');

    this.registrationId = this.registry.register(this.currentTableId, this);

    // input validation
    CosmoTableValidatorHelper.validateColumnsSpecification(this.parsedColumns, this, this.configurationService.getConfiguration());

    this.clientDataSnapshot = [];
    this.updateDataSnapshot([]);
    this.fetchedPageCount = 0;
    this.fetchedResultNumber = 0;
    this.selectedPageIndex = 0;
    this.selectedSearchQuery = null;
    this.selectedPageSize = null;
    this.expandedItems = [];
    this.checkedItems = [];
    this.checkedContextFilters = [];
    this.checkedHeaderFilters = [];
    this.checkedColumnsForFiltering = this.filterableColumns.filter(
      c => CosmoTableFormatterHelper.isDefaultFilterColumn(c, this.configurationService.getConfiguration()));
    this.checkedColumnsForVisualization = this.parsedColumns.filter(
      c => CosmoTableFormatterHelper.isDefaultVisibleColumn(c, this.configurationService.getConfiguration()));

    this.statusSnapshot = this.buildStatusSnapshot();
    this.persistableStatusSnapshot = this.buildPersistableStatusSnapshot();

    if (this.dataProvider) {
      // input is a provider function
      if (this.data) {
        throw new Error('CosmoTable can\'t be provided both data and dataProvider');
      }

    } else if (this.data instanceof Observable || this.data instanceof Subject) {
      // input is observable
      this.inputObservableSubscription = this.data.subscribe(dataSnapshot => {
        this.handleInputDataObservableEmission(dataSnapshot);
      });
    } else {
      // input is data array
      this.clientDataSnapshot = this.data;
    }

    // LOAD STATUS from store if possible
    let activationObservable: Observable<ICosmoTablePersistableStatusSnapshot | null>;
    if (this.storePersistenceEnabledAndPossible) {
      activationObservable = this.loadStatusFromStore();
    } else {
      activationObservable = new Observable(subscriber => {
        subscriber.next(null);
        subscriber.complete();
      });
    }

    activationObservable.subscribe((status: ICosmoTablePersistableStatusSnapshot | null) => {
      if (status) {
        this.logger.trace('restored status snapshot from storage', status);
        this.applyStatusSnapshot(status);
      }

      this.reload({reason: CosmoTableReloadReason.INTERNAL, withStatusSnapshot: status}).subscribe(yes => {
        this.completeInitialization();
      }, nope => {
        this.completeInitialization();
      });

    }, failure => {
      this.logger.error('restoring status from store failed', failure);
      this.reload({reason: CosmoTableReloadReason.INTERNAL}).subscribe(yes => {
        this.completeInitialization();
      }, nope => {
        this.completeInitialization();
      });
    });
  }

  ngAfterContentInit() {
    this.logger.trace('after content init');
  }

  private handleInputDataObservableEmission(dataSnapshot: any[]) {
    this.logger.debug('data snapshot emitted from input data');
    this.clientDataSnapshot = dataSnapshot;
    this.reload({reason: CosmoTableReloadReason.EXTERNAL});
  }

  private completeInitialization() {
    this.initialized = true;

    this.statusChange.pipe(debounceTime(200)).subscribe(statusSnapshot => {
      this.persistStatusToStore().subscribe();
    });

    if (this.refreshEmitter) {
      this.handleRefreshEmitterChange(this.refreshEmitter);
    }
    if (this.refreshInterval) {
      this.handleRefreshIntervalChange(this.refreshInterval);
    }
  }

  ngOnDestroy() {
    this.logger.trace('destroying component');
    if (this.registrationId) {
      this.registry.unregister(this.currentTableId, this.registrationId);
    }
    if (this.inputObservableSubscription) {
      this.inputObservableSubscription.unsubscribe();
    }
    if (this.refreshEmitterSubscription) {
      this.refreshEmitterSubscription.unsubscribe();
    }
    if (this.refreshIntervalSubscription) {
      this.refreshIntervalSubscription.unsubscribe();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.refreshEmitter) {
      this.handleRefreshEmitterChange(changes.refreshEmitter.currentValue);
    }
    if (changes.refreshInterval) {
      this.handleRefreshIntervalChange(changes.refreshInterval.currentValue);
    }
    if (changes.refreshStrategy && !changes.refreshStrategy.firstChange) {
      this.logger.warn('REFRESH STRATEGY CHANGED WHILE RUNNING. YOU SURE ABOUT THIS?', changes);
      this.handleRefreshEmitterChange(this.refreshEmitter);
      this.handleRefreshIntervalChange(this.refreshInterval);
    }
  }

  private handleRefreshEmitterChange(newValue: Observable<ICosmoTablePushRefreshRequest> | null) {
    if (this.refreshEmitterSubscription) {
      this.refreshEmitterSubscription.unsubscribe();
    }
    if (newValue) {
      this.refreshEmitterSubscription = newValue.subscribe(event => {
        this.logger.trace('received refresh push request', event);
        if (this.dragInProgress) {
          this.logger.warn('refresh from emitter ignored because user is dragging elements');
        } else if (this.currentRefreshStrategies.indexOf(CosmoTableRefreshStrategy.ON_PUSH) === -1) {
          this.logger.warn('refresh from emitter ignored because refresh strategies are ' + this.currentRefreshStrategies +
            '. Why are you pushing to this component?');
        } else if (!this.initialized) {
          this.logger.warn('refresh from emitter ignored because the component is not fully initialized');
        } else if (this.fetching) {
          this.logger.warn('refresh from emitter ignored because the component is fetching already');
        } else {
          this.logger.debug('launching reload following push request');
          this.reload({
            reason: CosmoTableReloadReason.PUSH,
            pushRequest: event,
            inBackground: event.inBackground === true || event.inBackground === false ?
              event.inBackground : this.currentRefreshOnPushInBackground
          });
        }
      });
    }
  }

  private handleRefreshIntervalChange(newValue: number | null) {
    if (this.refreshIntervalSubscription) {
      this.refreshIntervalSubscription.unsubscribe();
    }
    if (this.refreshIntervalTimer) {
      this.refreshIntervalTimer = null;
    }
    if (newValue) {
      this.refreshIntervalTimer = timer(newValue, newValue);
      this.refreshIntervalSubscription = this.refreshIntervalTimer.subscribe(tick => {
        this.logger.trace('emitted refresh tick request');
        if (this.dragInProgress) {
          this.logger.warn('refresh from emitter ignored because user is dragging elements');
        } else if (this.currentRefreshStrategies.indexOf(CosmoTableRefreshStrategy.TIMED) === -1) {
          this.logger.warn('refresh from tick ignored because refresh strategies are ' + this.currentRefreshStrategies +
            '. Why is this component emitting ticks ?');
        } else if (!this.initialized) {
          this.logger.warn('refresh from tick ignored because the component is not fully initialized');
        } else if (this.fetching) {
          this.logger.warn('refresh from tick ignored because the component is fetching already');
        } else {
          this.logger.debug('launching reload following tick');
          this.reload({
            reason: CosmoTableReloadReason.INTERVAL,
            inBackground: this.currentRefreshIntervalInBackground
          });
        }
      });
    }
  }

  // public methods

  public refresh(background?: boolean): Observable<void> {
    if (!this.initialized) {
      return throwError('table component is still initializing');
    }
    return this.reload({reason: CosmoTableReloadReason.EXTERNAL, inBackground: background});
  }

  reset(context?: ICosmoTableReloadContext) {
    if (!context) {
      context = {reason: CosmoTableReloadReason.USER};
    }

    this.setPage(0);
    this.statusChanged();
    this.reload(context);
  }

  public clearFilters(reload = true) {
    this.checkedHeaderFilters = [];
    this.checkedContextFilters = [];

    this.emitContextFiltersSelectionChanged();
    this.emitHeaderFiltersSelectionChanged();

    this.setPage(0);
    this.statusChanged();
    if (reload) {
      this.reload({reason: CosmoTableReloadReason.EXTERNAL});
    }
  }

  public sortBy(columnName: string, direction?: CosmoTableSortDirection): void {
    const column = this.parsedColumns.find(c => c.name === columnName);
    if (!column) {
      this.logger.warn('requested sorting by unknown column ' + columnName);
      return;
    }

    if (!CosmoTableFormatterHelper.isSortable(column, this.configurationService.getConfiguration())) {
      return;
    }
    if (!this.currentEnableSorting) {
      return;
    }

    this.selectedSortColumn = column;
    this.selectedSortDirection = direction ?? this.currentSortDirection ?? CosmoTableSortDirection.ASCENDING;

    this.emitSortChanged();

    // forza ritorno alla prima pagina
    this.setPage(0);
    this.statusChanged();
    this.reload({reason: CosmoTableReloadReason.USER, inBackground: true});
  }

  public getDataSnapshot(): any[] | null {
    return this.dataSnapshot;
  }

  public getStatusSnapshot(): ICosmoTableStatusSnapshot | null {
    return this.statusSnapshot;
  }

  public getExpandedItems(): any[] | null {
    return this.expandedItems;
  }

  public checkAll(): void {
    this.setAllChecked(true);
  }

  public uncheckAll(): void {
    this.setAllChecked(false);
  }

  public loadStatus(status: ICosmoTablePersistableStatusSnapshot) {
    if (!this.initialized) {
      return throwError('table component is still initializing');
    }
    this.logger.trace('loading status snapshot from external caller', status);
    this.applyStatusSnapshot(status);
    this.reload({reason: CosmoTableReloadReason.EXTERNAL, withStatusSnapshot: status});
  }

  private applyStatusSnapshot(status: ICosmoTablePersistableStatusSnapshot) {
   if (!status) {
     this.logger.warn('not restoring status because it is malformed');
     return;
   }

   if (status.schemaVersion && status.schemaVersion !== this.configurationService.getConfiguration().currentSchemaVersion) {
    this.logger.warn('not restoring status because it is obsolete (snapshot is version ' +
      status.schemaVersion + ' while current version is ' + this.configurationService.getConfiguration().currentSchemaVersion + ')');
    return;
   }

   if (this.currentEnableSorting) {
    if (status.orderColumn) {
      this.selectedSortColumn = this.parsedColumns.find(c => this.isSortable(c) && c.name === status.orderColumn) || null;
    }

    if (status.orderColumnDirection) {
      this.selectedSortDirection = (status.orderColumnDirection === CosmoTableSortDirection.DESCENDING) ?
        CosmoTableSortDirection.DESCENDING : CosmoTableSortDirection.ASCENDING;
    }
   }

   if (status.query) {
     this.selectedSearchQuery = status.query.trim();
   }

   if (status.queryColumns && status.queryColumns.length) {
    this.checkedColumnsForFiltering = this.filterableColumns.filter(c => c.name && status?.queryColumns?.indexOf(c.name) !== -1);
   }

   if (status.visibleColumns && status.visibleColumns.length) {

      this.checkedColumnsForVisualization =
        (status?.visibleColumns || []).map(columnName =>
          this.parsedColumns.find(candidate => candidate.name === columnName)
        ).filter(o => !!o) as ICosmoTableColumn[];

      // add mandatory columns if missing
      this.parsedColumns.filter(c => !this.isHideable(c) && c.name && status?.visibleColumns?.indexOf(c.name) === -1)
        .forEach(missing => {
          if (this.checkedColumnsForVisualization !== null) {
            this.checkedColumnsForVisualization.push(missing);
          }
        });
   }

   if (this.currentEnableContextFiltering && status.contextFilters && status.contextFilters.length) {
     this.checkedContextFilters = (this.contextFilters || []).filter(f => status?.contextFilters?.indexOf(f.name) !== -1);
   }

   if (this.currentEnablePagination && status.currentPage || status.currentPage === 0) {
     this.selectedPageIndex = status.currentPage;
   }

   if (this.currentEnablePagination && status.pageSize) {
     this.selectedPageSize = this.currentPossiblePageSizes.find(s => status.pageSize === s) || null;
   }

   this.statusSnapshot = this.buildStatusSnapshot();
   this.persistableStatusSnapshot = this.buildPersistableStatusSnapshot();
  }

  private applyStatusSnapshotPostFetch(status: ICosmoTablePersistableStatusSnapshot) {
   if (!status) {
     this.logger.warn('not restoring status because it is malformed');
     return;
   }

   if (status.schemaVersion && status.schemaVersion !== this.configurationService.getConfiguration().currentSchemaVersion) {
    this.logger.warn('not restoring status because it is obsolete (snapshot is version ' +
      status.schemaVersion + ' while current version is ' + this.configurationService.getConfiguration().currentSchemaVersion + ')');
    return;
   }

   if (this.currentEnableSelection && this.itemTrackingEnabledAndPossible
    && status.checkedItemIdentifiers && status.checkedItemIdentifiers.length) {

    const newChecked = (this.dataSnapshot || []).filter(data => {
      const id = this.getItemIdentifier(data);
      return id && (status.checkedItemIdentifiers || []).indexOf(id) !== -1;
    });
    this.updateChecked(newChecked);
   }

   if (this.currentEnableRowExpansion && this.itemTrackingEnabledAndPossible
    && status.expandedItemIdentifiers && status.expandedItemIdentifiers.length) {

    this.expandedItems = (this.dataSnapshot || []).filter(data => {
      const id = this.getItemIdentifier(data);
      return id && (status.expandedItemIdentifiers || []).indexOf(id) !== -1 && this.isExpandable(data);
    });
   }

   this.statusSnapshot = this.buildStatusSnapshot();
   this.persistableStatusSnapshot = this.buildPersistableStatusSnapshot();
  }

  reloadFromFailure() {
    this.reload({reason: CosmoTableReloadReason.USER});
  }

  private reload( context: ICosmoTableReloadContext ): Observable<void> {

    this.fetching = true;
    this.showFetching = !context.inBackground;
    this.lastFetchError = null;

    const obs: Observable<void> = new Observable<void>(subscriber => {
      try {
        this.reloadInObservable(subscriber, context);
      } catch (e) {
        subscriber.error(e);
        subscriber.complete();
      }
    }).pipe(share());

    obs.subscribe(success => {
      this.logger.trace('async reload success');
      this.fetching = false;
      if (!context.inBackground) {
        this.showFetching = false;
      }

      this.statusChanged();
    }, failure => {
      this.logger.trace('async reload failed', failure);
      this.lastFetchError = this.extractErrorMessage(failure);

      this.updateDataSnapshot([]);
      this.fetchedResultNumber = 0;
      this.fetchedPageCount = 1;

      this.fetching = false;
      if (!context.inBackground) {
        this.showFetching = false;
      }

      this.statusChanged();
    });
    return obs;
  }

  private extractErrorMessage(input: any): string {
    if (typeof input === 'string') {
      return input;
    }
    if (input instanceof Error) {
      return input.message;
    }
    if (input instanceof HttpErrorResponse) {
      return input.error?.title ?? input.message ?? input.statusText;
    }
    return 'Errore imprevisto';
  }

  private reloadInObservable(tracker: Subscriber<void>, context: ICosmoTableReloadContext) {
    let withSnapshot: ICosmoTablePersistableStatusSnapshot | null = context.withStatusSnapshot || null;
    if (!withSnapshot) {
      withSnapshot = this.buildPersistableStatusSnapshot();
    }

    const pageRequest = this.buildPageRequest();

    this.logger.debug('reloading table data', pageRequest);

    // clear checked items
    if (!context.inBackground) {
      // this.checkedItems = [];
      this.setAllChecked(false);

      this.expandedItems = [];
    }
    this.lastFetchedSearchQuery = pageRequest.query || null;

    if (this.dataProvider) {
      // call data provider
      this.logger.trace('reload has been called, fetching data from provided function');
      this.logger.trace('page request for data fetch is', pageRequest);

      this.dataProvider(pageRequest, context).subscribe((response: ICosmoTablePageResponse) => {
        this.logger.trace('fetching data completed successfully');

        if (!this.dragInProgress) {
          if (this.paginationMode === CosmoTablePaginationMethod.SERVER) {
            this.parseResponseWithServerPagination(response);
          } else {
            this.parseResponseWithClientPagination(response.content, pageRequest);
          }
          if (withSnapshot) {
            this.applyStatusSnapshotPostFetch(withSnapshot);
          }
        } else {
          this.logger.warn('data fetch aborted because user is dragging things');
        }

        tracker.next();
        tracker.complete();
      }, failure => {
        this.logger.error('error fetching data from provider function', failure);

        if (!this.dragInProgress) {
          this.updateDataSnapshot([]);
        } else {
          this.logger.warn('data fetch aborted because user is dragging things');
        }

        tracker.error(failure);
        tracker.complete();
      });
    } else if (this.data instanceof Observable || this.data instanceof Subject) {
      // data is provided on push and now in clientDataSnapshot
      this.logger.trace('reload has been called on locally fetched data from observable');

      if (!this.dragInProgress) {
        this.parseResponseWithClientPagination(this.clientDataSnapshot, pageRequest);
        if (withSnapshot) {
          this.applyStatusSnapshotPostFetch(withSnapshot);
        }
      } else {
        this.logger.warn('data fetch aborted because user is dragging things');
      }

      tracker.next();
      tracker.complete();

    } else {
      // data is not provided on request and is static, now in clientDataSnapshot
      this.logger.trace('reload has been called on locally fetched data');
      this.clientDataSnapshot = this.data;

      if (!this.dragInProgress) {
        this.parseResponseWithClientPagination(this.clientDataSnapshot, pageRequest);
        if (withSnapshot) {
          this.applyStatusSnapshotPostFetch(withSnapshot);
        }
      } else {
        this.logger.warn('data fetch aborted because user is dragging things');
      }

      tracker.next();
      tracker.complete();
    }
  }

  private parseResponseWithServerPagination(response: ICosmoTablePageResponse) {
    if (this.checkIfDataDiffers(response.content)) {
      this.updateDataSnapshot(response.content);
    }

    if (this.currentEnablePagination) {
      if (response.totalPages === 0 || response.totalPages) {
        this.fetchedPageCount = response.totalPages;
      } else {
        throw new Error('data from server did not contain required totalPages field');
      }

      if (response.totalElements === 0 || response.totalElements) {
        this.fetchedResultNumber = response.totalElements;
      } else {
        throw new Error('data from server did not contain required totalElements field');
      }
    }
  }

  private parseResponseWithClientPagination(data: any[] | null, request: ICosmoTablePageRequest) {
    this.logger.trace('applying in-memory fetching, paginating, ordering and filtering');

    const inMemoryResponse = CosmoTableInMemoryHelper.fetchInMemory(
      data || [],
      request,
      this.currentSortColumn,
      this.checkedColumnsForFiltering || [],
      this.getCurrentLocale() );

    if (this.checkIfDataDiffers(inMemoryResponse.content)) {
      this.updateDataSnapshot(inMemoryResponse.content);
    }
    this.fetchedResultNumber = typeof inMemoryResponse.totalElements === 'undefined' ? null : inMemoryResponse.totalElements;
    this.fetchedPageCount = typeof inMemoryResponse.totalPages === 'undefined' ? null : inMemoryResponse.totalPages;
  }

  private buildPageRequest(page?: number, size?: number): ICosmoTablePageRequest {
    const output: ICosmoTablePageRequest = {
      page: this.currentEnablePagination ? (page ?? this.currentPageIndex) : null,
      size: this.currentEnablePagination ? (size ?? this.currentPageSize) : null,
      sort: [],
      query: null,
      queryFields: [],
      filters: [],
      context: {
        tableId: this.currentTableId,
        ...(this.tableContext || {}),
        ...(this.dataProviderContext || {})
      }
    };

    if (this.searchQueryActive) {
      output.query = this.currentSearchQuery;
      output.queryFields = (this.checkedColumnsForFiltering || []).map(column => column.serverField || column.field || null );
    }

    if (this.currentEnableContextFiltering && this.checkedContextFilters?.length) {
      for (const filter of (this.checkedContextFilters || [])) {
        output.filters?.push(filter.name || null);
      }
    }

    if (this.checkedHeaderFilters?.length) {
      for (const filter of (this.checkedHeaderFilters || [])) {
        output.filters?.push(filter.name || null);
      }
    }

    if (this.currentEnableSorting) {
      const sortColumn = this.currentSortColumn;
      const sortDirection = this.currentSortDirection;
      if (sortColumn) {
        output.sort?.push({
          property: sortColumn.serverField || sortColumn.field || null,
          direction: sortDirection || CosmoTableSortDirection.ASCENDING
        });
      }
    }

    return output;
  }

  private setPage(index: number) {
    this.selectedPageIndex = index;
    this.statusChanged();
    this.emitPageChanged();
  }

  get currentExportEnabled(): boolean {
    if (this.enableExport === true || this.enableExport === false) {
      return this.enableExport;
    } else {
      return this.configurationService.getConfiguration()
        .export?.enabledByDefault ?? false;
    }
  }

  get currentGroupingEnabled(): boolean {
    if (this.enableGrouping === true || this.enableGrouping === false) {
      return this.enableGrouping;
    } else {
      return !!this.dataGrouper;
    }
  }

  get currentEnableMainHeader(): boolean {
    if (this.enableMainHeader === true || this.enableMainHeader === false) {
      return this.enableMainHeader;
    } else {
      return this.configurationService.getConfiguration()
        .header?.enabledByDefault ?? false;
    }
  }

  get currentEnableRowHeader(): boolean {
    if (this.enableRowHeader === true || this.enableRowHeader === false) {
      return this.enableRowHeader;
    } else {
      return this.configurationService.getConfiguration()
        .header?.rowHeaderEnabledByDefault ?? false;
    }
  }

  get currentRowHeaderPosition(): CosmoTableRowHeaderPosition {
    if (this.rowHeaderPosition) {
      return this.rowHeaderPosition;
    } else {
      return this.configurationService.getConfiguration()
        .header?.rowHeaderDefaultPosition ?? CosmoTableRowHeaderPosition.TOP;
    }
  }

  get currentEnableSorting(): boolean {
    if (this.enableSorting === true || this.enableSorting === false) {
      return this.enableSorting;
    } else {
      return this.configurationService.getConfiguration()
        .sort?.enabledByDefault ?? false;
    }
  }

  get currentRefreshOnPushInBackground(): boolean {
    if (this.refreshOnPushInBackground === true || this.refreshOnPushInBackground === false) {
      return this.refreshOnPushInBackground;
    } else {
      return this.configurationService.getConfiguration()
        .refresh?.defaultOnPushInBackground ?? false;
    }
  }

  get currentRefreshIntervalInBackground(): boolean {
    if (this.refreshIntervalInBackground === true || this.refreshIntervalInBackground === false) {
      return this.refreshIntervalInBackground;
    } else {
      return this.configurationService.getConfiguration()
        .refresh?.defaultOnTickInBackground ?? false;
    }
  }

  get currentRefreshStrategies(): CosmoTableRefreshStrategy[] {
    if (this.refreshStrategy) {
      if (Array.isArray(this.refreshStrategy) && this.refreshStrategy.length) {
        return this.refreshStrategy;
      } else {
        return [this.refreshStrategy as CosmoTableRefreshStrategy];
      }
    }
    return [this.configurationService.getConfiguration()
        .refresh?.defaultStrategy ?? CosmoTableRefreshStrategy.NONE];
  }

  get currentRefreshInterval(): number {
    return this.refreshInterval || (this.configurationService.getConfiguration()
      .refresh?.defaultInterval ?? 60);
  }

  get currentTableId(): string {
    return this.tableId || this.uuid;
  }

  get currentEnableDrag(): boolean {
    if (this.enableDrag === true || this.enableDrag === false) {
      return this.enableDrag;
    } else {
      return this.configurationService.getConfiguration()
        .dragAndDrop?.dragEnabledByDefault ?? false;
    }
  }

  get currentEnableDrop(): boolean {
    if (this.enableDrop === true || this.enableDrop === false) {
      return this.enableDrop;
    } else {
      return this.configurationService.getConfiguration()
        .dragAndDrop?.dropEnabledByDefault ?? false;
    }
  }

  get currentEnablePagination(): boolean {
    if (this.enablePagination === true || this.enablePagination === false) {
      return this.enablePagination;
    } else {
      return this.configurationService.getConfiguration()
        .pagination?.enabledByDefault ?? false;
    }
  }

  get currentEnableItemTracking(): boolean {
    if (this.enableItemTracking === true || this.enableItemTracking === false) {
      return this.enableItemTracking;
    } else {
      return this.configurationService.getConfiguration()
        .itemTracking?.enabledByDefault ?? false;
    }
  }

  get currentEnableStorePersistence(): boolean {
    if (this.enableStorePersistence === true || this.enableStorePersistence === false) {
      return this.enableStorePersistence;
    } else {
      return this.configurationService.getConfiguration()
        .storePersistence?.enabledByDefault ?? false;
    }
  }

  get currentEnablePageSizeSelect(): boolean {
    if (this.enablePageSizeSelect === true || this.enablePageSizeSelect === false) {
      return this.enablePageSizeSelect;
    } else {
      return this.configurationService.getConfiguration()
        .pagination?.pageSizeSelectionEnabledByDefault ?? false;
    }
  }

  get currentEnableMultipleRowExpansion(): boolean {
    if (this.enableMultipleRowExpansion === true || this.enableMultipleRowExpansion === false) {
      return this.enableMultipleRowExpansion;
    } else {
      return this.configurationService.getConfiguration()
        .rowExpansion?.multipleExpansionEnabledByDefault ?? false;
    }
  }

  get currentEnableRowExpansion(): boolean {
    if (this.enableRowExpansion === true || this.enableRowExpansion === false) {
      return this.enableRowExpansion;
    } else {
      return this.configurationService.getConfiguration()
        .rowExpansion?.enabledByDefault ?? false;
    }
  }

  get currentEnableManualRowExpansion(): boolean {
    if (this.enableManualRowExpansion === true || this.enableManualRowExpansion === false) {
      return this.enableManualRowExpansion;
    } else {
      return this.configurationService.getConfiguration()
        .rowExpansion?.manualExpansionEnabledByDefault ?? false;
    }
  }

  get currentEnableMultiSelect(): boolean {
    if (this.enableMultiSelect === true || this.enableMultiSelect === false) {
      return this.enableMultiSelect;
    } else {
      return this.configurationService.getConfiguration()
        .rowSelection?.multipleSelectionEnabledByDefault ?? false;
    }
  }

  get currentEnableSelectAll(): boolean {
    if (this.enableSelectAll === true || this.enableSelectAll === false) {
      return this.enableSelectAll;
    } else {
      return this.configurationService.getConfiguration()
        .rowSelection?.selectAllEnabledByDefault ?? false;
    }
  }

  get currentEnableContextFiltering(): boolean {
    if (this.enableContextFiltering === true || this.enableContextFiltering === false) {
      return this.enableContextFiltering;
    } else {
      return this.configurationService.getConfiguration()
        .contextFiltering?.enabledByDefault ?? false;
    }
  }

  get currentEnableSelection(): boolean {
    if (this.enableSelection === true || this.enableSelection === false) {
      return this.enableSelection;
    } else {
      return this.configurationService.getConfiguration()
        .rowSelection?.enabledByDefault ?? false;
    }
  }

  get currentPaginationMode(): CosmoTablePaginationMethod {
    if (this.paginationMode) {
      return this.paginationMode;
    } else {
      return this.configurationService.getConfiguration()
        .pagination?.defaultPaginationMode ?? CosmoTablePaginationMethod.CLIENT;
    }
  }

  get itemTrackingEnabledAndPossible(): boolean {
    return !!this.itemIdentifier && this.currentEnableItemTracking;
  }

  get storePersistenceEnabledAndPossible(): boolean {
    return !!this.storeAdapter && (this.currentEnableStorePersistence || false);
  }

  get rowExpansionEnabledAndPossible(): boolean {
    return this.currentEnableRowExpansion;
  }

  get pageSizeSelectEnabledAndPossible(): boolean {
    return this.currentPossiblePageSizes && this.currentPossiblePageSizes.length > 0 && this.currentEnablePageSizeSelect;
  }

  get contextFilteringEnabledAndPossible(): boolean {
    return !!this.contextFilters && this.contextFilters.length > 0 && this.currentEnableContextFiltering;
  }

  get currentPossiblePageSizes(): number[] {
    if (this.possiblePageSize && this.possiblePageSize.length) {
      return this.possiblePageSize;
    } else {
      return this.configurationService.getConfiguration()
        .pagination?.defaultPossiblePageSizes ?? [5, 10, 20];
    }
  }

  get currentSearchQuery(): string | null {
    if (this.selectedSearchQuery) {
      return this.selectedSearchQuery;
    } else {
      return null;
    }
  }

  get currentPageCount(): number {
    return this.fetchedPageCount || 0;
  }

  get currentResultNumber(): number {
    return this.fetchedResultNumber || 0;
  }

  get currentPageSize(): number {
    const def = this.configurationService.getConfiguration()
      .pagination?.defaultPageSize ?? 5;
    if (this.selectedPageSize) {
      return this.selectedPageSize;
    } else if (this.defaultPageSize) {
      return this.defaultPageSize;
    } else if (
        this.currentPossiblePageSizes.length &&
        def &&
        this.currentPossiblePageSizes.indexOf(def) !== -1) {
        return def;
    } else if (this.currentPossiblePageSizes.length) {
      return this.currentPossiblePageSizes[0];
    } else if (def) {
      return def;
    } else {
      return 10;
    }
  }

  get currentPageIndex(): number {
    if (this.selectedPageIndex) {
      return this.selectedPageIndex;
    } else {
      return 0;
    }
  }

  get currentSortColumn(): ICosmoTableColumn | null {
    if (this.selectedSortColumn) {
      // todo log if sorting column disappears
      return this.visibleColumns.find(c => c.name === this.selectedSortColumn?.name) || null;
    } else if (this.defaultSortingColumn) {
      const foundColumn = this.parsedColumns.find(c => c.name === this.defaultSortingColumn);
      if (foundColumn) {
        return foundColumn;
      } else {
        // find first sortable column?
        return this.visibleColumns.find(column =>
          CosmoTableFormatterHelper.isSortable(column, this.configurationService.getConfiguration())) || null;
      }
    } else {
      // find first sortable column?
      return this.visibleColumns.find(column =>
        CosmoTableFormatterHelper.isSortable(column, this.configurationService.getConfiguration())) || null;
    }
  }

  get currentSortDirection(): CosmoTableSortDirection {
    if (this.selectedSortDirection) {
      return this.selectedSortDirection;
    } else if (this.defaultSortingDirection) {
      return this.defaultSortingDirection === CosmoTableSortDirection.DESCENDING ?
      CosmoTableSortDirection.DESCENDING : CosmoTableSortDirection.ASCENDING;
    } else {
      // return DEFAULT
      return CosmoTableSortDirection.ASCENDING;
    }
  }

  get currentData(): any[] {
    return this.dataSnapshot || [];
  }

  get groupedData(): ICosmoTableDataGroup[] {
    return this.groupedDataSnapshot || [];
  }

  private groupData(data: any[]): ICosmoTableDataGroup[] {
    if (!!this.dataGrouper && this.currentGroupingEnabled) {
      const grouped: {
        [key: string]: ICosmoTableDataGroup
      } = {};
      const nullGroup: ICosmoTableDataGroup = { groupKey: null, data: [] };

      data.forEach(row => {
        const groupKey = this.dataGrouper ? this.dataGrouper(row) : null;
        if (groupKey === null || typeof groupKey === 'undefined') {
          nullGroup.data.push(row);
        } else {
          const groupKeySerialized = JSON.stringify(groupKey);
          if (!grouped[groupKeySerialized]) {
            grouped[groupKeySerialized] = {
              groupKey,
              data: []
            };
          }
          grouped[groupKeySerialized].data.push(row);
        }
      });

      const output = Object.values(grouped);
      if (nullGroup.data.length > 0) {
        output.unshift(nullGroup);
      }

      return output as ICosmoTableDataGroup[];

    } else {
      return [{
        groupKey: null,
        data
      }];
    }
  }

  get enumPages(): (number | { skip: boolean; })[] {
    const rangeStart = 1;
    const rangeEnd = 1;
    const rangeSelected = 1;

    const pages = [];
    const pageNum = this.currentPageCount;
    const currentIndex = this.currentPageIndex;
    let isSkipping = false;

    for (let i = 0; i < pageNum; i ++) {
      if (Math.abs(i - currentIndex) <= (rangeSelected) || i <= (rangeStart - 1) || i >= (pageNum - rangeEnd)) {
        isSkipping = false;
        pages.push(i);
      } else {
        if (!isSkipping) {
          pages.push({
            skip: true
          });
          isSkipping = true;
        }
      }
    }
    return pages;
  }

  get activeColumnsCount(): number {
    let output = this.visibleColumns.length;
    if (this.currentEnableSelection) {
      output ++;
    }
    if (this.contextFilteringEnabledAndPossible) {
      output ++;
    }
    if (this.rowExpansionEnabledAndPossible && this.currentEnableManualRowExpansion) {
      output ++;
    }
    if (this.currentExportEnabled) {
      output ++;
    }
    return output;
  }

  get noResults(): boolean {
    if (this.paginationMode === CosmoTablePaginationMethod.CLIENT) {
      return !this.currentData.length;
    } else {
      return !(this.dataSnapshot?.length);
    }
  }

  get hideableColumns(): ICosmoTableColumn[] {
    return this.parsedColumns.filter(c => CosmoTableFormatterHelper.isHideable(c, this.configurationService.getConfiguration()));
  }

  get visibleColumns(): ICosmoTableColumn[] {
    // return in order
    return (this.checkedColumnsForVisualization || []).map(checkedSpecification => {
      return this.parsedColumns.find(x => x.name === checkedSpecification.name);
    }).filter(o => !!o) as ICosmoTableColumn[];
  }

  get currentContextFilters(): ICosmoTableContextFilter[] {
    return this.contextFilters || [];
  }

  get searchQueryActive(): boolean {
    if (!this.currentSearchQuery) {
      return false;
    }
    /*
    if (!(this.checkedColumnsForFiltering?.length)) {
      return false;
    }
    */
    return true;
  }

  get filterableColumns(): ICosmoTableColumn[] {
    return this.parsedColumns.filter(c => this.isFilterable(c));
  }

  // drag and drop support

  acceptDropPredicate = (item: CdkDrag<any>) => {
    return this.acceptDrop;
  }

  get acceptDrop() {
    return this.currentEnableDrop;
  }

  get acceptDrag() {
    return this.currentEnableDrag;
  }

  get referencedTable(): CosmoTableComponent | null {
    const v = this.getReferencedTable();
    return v;
  }

  getReferencedTable(): CosmoTableComponent | null {
    if (this.dropConnectedTo && this.dropConnectedTo.length) {
      const connectedToList: string[] = (Array.isArray(this.dropConnectedTo)) ? this.dropConnectedTo : [this.dropConnectedTo];
      const found = [];
      for (const connectedToToken of connectedToList) {
        const registeredList: any[] = this.registry.get(connectedToToken);
        if (registeredList && registeredList.length) {
          for (const regComp of registeredList) {
            found.push(regComp);
          }
        }
      }
      if (found.length < 1) {
        return null;
      } else if (found.length > 1) {
        this.logger.error('MULTIPLE TABLE REFERENCED STILL ACTIVE', found);
        return null;
      } else {
        return found[0].component;
      }
    } else {
      return this;
    }
  }

  // user actions

  handleItemDragged(event: CdkDragDrop<any[]>, from: CosmoTableComponent, to: CosmoTableComponent) {
    this.logger.debug('item dragged from table ' + this.currentTableId);
    this.itemDragged.emit({
      item: event.item.data,
      event,
      fromDataSnapshot: from.getDataSnapshot(),
      toDataSnapshot: to.getDataSnapshot(),
      fromComponent: from,
      toComponent: to
    });
  }

  handleItemDropped(event: CdkDragDrop<any[]>) {
    this.logger.debug('DROP EVENT FROM TABLE ' + this.currentTableId, event);
    const droppedSource: CosmoTableComponent | null = this.registry.getSingle(event.previousContainer.id);
    const droppedTarget: CosmoTableComponent | null = this.registry.getSingle(event.container.id);

    if (!droppedTarget) {
      this.logger.warn('NO DROP TARGET FOUND');
      return;
    }

    if (!droppedTarget.acceptDrop) {
      this.logger.debug('skipping drop on container with acceptDrop = false');
      return;
    }

    if (droppedSource) {
      droppedSource.handleItemDragged(event, droppedSource, droppedTarget);
    }

    this.logger.debug('item dropped on table ' + droppedTarget.currentTableId);
    this.itemDropped.emit({
      item: event.item.data,
      event,
      fromDataSnapshot: droppedSource ? droppedSource.getDataSnapshot() : null,
      toDataSnapshot: droppedTarget.getDataSnapshot(),
      fromComponent: droppedSource,
      toComponent: droppedTarget
    });

    
    /*
    this.cdr.detectChanges();
    this.ngZone.run(() => {
      this.dataSnapshot = this.dataSnapshot.map(o => o);
      this.cdr.detectChanges();
      setTimeout(() => this.forceReRender = true);
      setTimeout(() => this.forceReRender = false);
      this.handleMatTableDataSnapshotChanged();
    });
    */
  }

  applySelectedFilter() {
    this.setPage(0);
    this.statusChanged();
    this.reload({reason: CosmoTableReloadReason.USER});
  }

  // PUBLIC ACCESS
  public expand(item: any, force = false) {
    if (!this.rowExpansionEnabledAndPossible) {
      return;
    }
    if (!force && !this.isExpandable(item)) {
      return;
    }

    if (!this.isExpanded(item)) {
      if (!this.currentEnableMultipleRowExpansion) {
        this.expandedItems = [];
      }
      if (this.expandedItems === null) {
        this.expandedItems = [];
      }
      this.expandedItems.push(item);
    }

    this.statusChanged();
  }

  public collapse(item: any) {
    if (!this.rowExpansionEnabledAndPossible) {
      return;
    }

    if (this.isExpanded(item)) {
      if (this.expandedItems === null) {
        this.expandedItems = [];
      }
      this.expandedItems.splice(this.expandedItems.indexOf(this.getIfExpanded(item)), 1);
    }

    this.statusChanged();
  }

  clickOnRowExpansion(item: any, force = false) {
    if (!this.rowExpansionEnabledAndPossible) {
      return;
    }
    if (!force && !this.isExpandable(item)) {
      return;
    }

    if (this.expandedItems === null) {
      this.expandedItems = [];
    }

    if (this.isExpanded(item)) {
      this.expandedItems.splice(this.expandedItems.indexOf(this.getIfExpanded(item)), 1);
    } else {
      if (!this.currentEnableMultipleRowExpansion) {
        this.expandedItems = [];
      }
      this.expandedItems.push(item);
    }

    this.statusChanged();
  }

  clickOnPageSize(pageSize: number) {
    this.selectedPageSize = pageSize;
    this.setPage(0);
    this.statusChanged();
    this.reload({reason: CosmoTableReloadReason.USER});
  }

  searchQueryFocusOut() {
    const comp = this;
    setTimeout(() => {
      if ((comp.selectedSearchQuery || '') !== (comp.lastFetchedSearchQuery || '')) {
        if (comp.selectedSearchQuery) {
          if (this.configurationService.getConfiguration().filtering?.autoReloadOnQueryChange) {
            comp.applySelectedFilter();
          } else {
            // comp.selectedSearchQuery = comp.lastFetchedSearchQuery;
          }
        } else {
          if (this.configurationService.getConfiguration().filtering?.autoReloadOnQueryClear) {
            comp.applySelectedFilter();
          }
        }
      }
    }, this.configurationService.getConfiguration().filtering?.autoReloadTimeout);
  }

  switchToPage(pageIndex: number, context?: ICosmoTableReloadContext) {
    if (this.currentPageIndex === pageIndex) {
      return;
    }
    if (!context) {
      context = {reason: CosmoTableReloadReason.USER};
    }

    this.setPage(pageIndex);
    this.statusChanged();
    this.reload(context);
  }

  clickOnColumn(column: ICosmoTableColumn) {
    if (!CosmoTableFormatterHelper.isSortable(column, this.configurationService.getConfiguration())) {
      return;
    }
    if (!this.currentEnableSorting) {
      return;
    }

    const sortColumn = this.currentSortColumn;
    const sortDirection = this.currentSortDirection;

    if (sortColumn && sortColumn.name === column.name) {
      this.selectedSortDirection = (sortDirection === CosmoTableSortDirection.DESCENDING ?
        CosmoTableSortDirection.ASCENDING : CosmoTableSortDirection.DESCENDING);
    } else {
      this.selectedSortColumn = column;
      this.selectedSortDirection = CosmoTableSortDirection.ASCENDING;
    }

    this.emitSortChanged();

    // forza ritorno alla prima pagina
    this.setPage(0);
    this.statusChanged();
    this.reload({reason: CosmoTableReloadReason.USER, inBackground: true});
  }

  // parsing functions

  getItemIdentifier(item: any) {
    if (this.itemIdentifier) {
      if ((this.itemIdentifier as any).extract) {
        return (this.itemIdentifier as ICosmoTableIdentifierProvider).extract(item);
      } else {
        return CosmoTableFormatterHelper.getPropertyValue(item, this.itemIdentifier as string);
      }
    } else {
      return null;
    }
  }

  getCurrentLocale(): string {
    return this.translateService.getDefaultLang();
  }

  extractValue(row: any, column: ICosmoTableColumn) {
    return CosmoTableFormatterHelper.extractValue(row, column, this.getCurrentLocale());
  }

  resolveLabel(column: ICosmoTableColumn | ICosmoTableAction | ICosmoTableContextFilter) {
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

  isCurrentSortingColumn(column: ICosmoTableColumn, direction: string | null = null) {
    const sortColumn = this.currentSortColumn;
    if (sortColumn && column && column.name === sortColumn.name) {
      if (!direction) {
        return true;
      }
      return direction === this.currentSortDirection;
    } else {
      return false;
    }
  }

  isDefaultVisibleColumn(column: ICosmoTableColumn) {
    return CosmoTableFormatterHelper.isDefaultVisibleColumn(column, this.configurationService.getConfiguration());
  }

  isDefaultFilterColumn(column: ICosmoTableColumn) {
    return CosmoTableFormatterHelper.isDefaultFilterColumn(column, this.configurationService.getConfiguration());
  }

  isExpandable(row: any): boolean {
    return this.currentEnableRowExpansion &&
      (!this.expandableStatusProvider || this.expandableStatusProvider(row, this.statusSnapshot));
  }

  isHideable(column: ICosmoTableColumn): boolean | undefined {
    return CosmoTableFormatterHelper.isHideable(column, this.configurationService.getConfiguration());
  }

  isSortable(column: ICosmoTableColumn) {
    return CosmoTableFormatterHelper.isSortable(column, this.configurationService.getConfiguration());
  }

  isFilterable(column: ICosmoTableColumn) {
    return CosmoTableFormatterHelper.isFilterable(column, this.configurationService.getConfiguration());
  }

  hasLabel(column: ICosmoTableColumn) {
    return CosmoTableFormatterHelper.hasLabel(column, this, this.configurationService.getConfiguration());
  }

  isLabelVisibleInTable(column: ICosmoTableColumn) {
    return CosmoTableFormatterHelper.isLabelVisibleInTable(column, this.configurationService.getConfiguration());
  }

  isActionVisible(action: ICosmoTableAction, def?: CosmoTableActionVisibilityCondition) {
    const actCond = action.visibilityCondition || def;
    if (action.isVisible && !action.isVisible(this.statusSnapshot || null)) {
      return false;
    }

    if (actCond === CosmoTableActionVisibilityCondition.ALWAYS) {
      return true;
    } else if (actCond === CosmoTableActionVisibilityCondition.NEVER) {
      return false;
    } else if (actCond === CosmoTableActionVisibilityCondition.DYNAMIC) {
      // delega decisione a provider esterno
      if (!this.actionVisibilityProvider) {
        this.logger.error('Action with dynamic enabling but no actionVisibilityProvider provided');
        return false;
      }

      return this.actionVisibilityProvider(action, this.statusSnapshot || null);
    }

    const numSelected = this.checkedItems?.length || 0;
    if (actCond === CosmoTableActionVisibilityCondition.SINGLE_SELECTION) {
      return numSelected === 1;
    } else if (actCond === CosmoTableActionVisibilityCondition.MULTIPLE_SELECTION) {
      return numSelected > 0;
    } else if (actCond === CosmoTableActionVisibilityCondition.NO_SELECTION) {
      return numSelected < 1;
    } else {
      throw new Error('Unknown action visibility condition: ' + actCond);
    }
  }

  isActionAllowed(action: ICosmoTableAction, def?: CosmoTableActionActivationCondition) {
    if (action.isEnabled && !action.isEnabled(this.statusSnapshot || null)) {
      return false;
    }

    const actCond = action.activationCondition || def;
    if (actCond === CosmoTableActionActivationCondition.ALWAYS) {
      return true;
    } else if (actCond === CosmoTableActionActivationCondition.NEVER) {
      return false;
    } else if (actCond === CosmoTableActionActivationCondition.DYNAMIC) {
      // delega decisione a provider esterno
      if (!this.actionStatusProvider) {
        this.logger.error('Action with dynamic enabling but no actionStatusProvider provided');
        return false;
      }

      return this.actionStatusProvider(action, this.statusSnapshot || null);
    }

    const numSelected = this.checkedItems?.length || 0;
    if (actCond === CosmoTableActionActivationCondition.SINGLE_SELECTION) {
      return numSelected === 1;
    } else if (actCond === CosmoTableActionActivationCondition.MULTIPLE_SELECTION) {
      return numSelected > 0;
    } else if (actCond === CosmoTableActionActivationCondition.NO_SELECTION) {
      return numSelected < 1;
    } else {
      throw new Error('Unknown action activation condition: ' + actCond);
    }
  }

  clickOnAction(action: ICosmoTableAction) {
    if (!this.isActionAllowed(action)) {
      return;
    }

    const status = this.statusSnapshot;

    const dispatchContext: ICosmoTableActionDispatchingContext = {
      action,
      selectedItems: status?.checkedItems || [],
      status,
      tableContext: this.tableContext
    };

    this.logger.debug('dispatching action ' + action.name + ' with payload', dispatchContext);

    this.action.emit(dispatchContext);
  }

  // gestione delle righe espanse

  getIfExpanded(item: any) {
    if (this.forceRowExpansion && this.forceRowExpansion(item, this.statusSnapshot)) {
      return item;
    }
    if (this.itemIdentifier) {
      const id = this.getItemIdentifier(item);
      return (this.expandedItems || []).find(i => this.getItemIdentifier(i) === id) || null;
    }
    return (this.expandedItems || []).indexOf(item) !== -1 ? item : null;
  }

  public isExpanded(item: any) {
    return !!this.getIfExpanded(item);
  }

  // checkbox select per items

  getIfChecked(item: any) {
    if (this.itemIdentifier) {
      const id = this.getItemIdentifier(item);
      return (this.checkedItems || []).find(i => this.getItemIdentifier(i) === id) || null;
    }
    return (this.checkedItems || []).indexOf(item) !== -1 ? item : null;
  }

  public isChecked(item: any) {
    return !!this.getIfChecked(item);
  }

  get areAllChecked(): boolean {
    const dataItems = this.currentData;
    if (!dataItems.length) {
      return false;
    }
    for (const item of dataItems) {
      if (this.isItemSelectable(item) && !this.isChecked(item)) {
        return false;
      }
    }
    return true;
  }

  get anyChecked(): boolean {
    const dataItems = this.currentData;
    if (!dataItems.length) {
      return false;
    }
    for (const item of dataItems) {
      if (this.isChecked(item)) {
        return true;
      }
    }
    return false;
  }

  get noneChecked(): boolean {
    return !this.anyChecked;
  }

  updateChecked(items: any[]) {
    items = items ?? [];
    const previous = this.checkedItems ?? [];
    if (this.checkedItems === null) {
      this.checkedItems = [];
    }

    // find now checked that was not
    const newlyChecked = items.filter(i => previous.indexOf(i) === -1);

    // find now not checked that were
    const notLongerChecked = previous.filter(i => items.indexOf(i) === -1);

    this.checkedItems = items;

    this.statusChanged();
    notLongerChecked.forEach(item => {
      if (this.selectionWriteback) {
        (item as any).selected = false;
      }
      this.emitItemChecked(item, false);
    });
    newlyChecked.forEach(item => {
        if (this.selectionWriteback) {
          (item as any).selected = true;
        }
        this.emitItemChecked(item, true);
    });
    this.emitSelectionChanged();
  }

  toggleChecked(item: any) {
    if (this.checkedItems === null) {
      this.checkedItems = [];
    }

    let newCheckedValue = false;
    if (this.isChecked(item)) {
      this.checkedItems.splice(this.checkedItems.indexOf(this.getIfChecked(item)), 1);
    } else {
      newCheckedValue = true;
      if (!this.currentEnableMultiSelect) {
        this.checkedItems = [];
      }
      this.checkedItems.push(item);
    }

    if (this.selectionWriteback) {
      (item as any).selected = newCheckedValue;
    }

    this.statusChanged();
    this.emitItemChecked(item, newCheckedValue);
    this.emitSelectionChanged();
  }

  toggleAllChecked() {
    if (!this.currentEnableSelectAll) {
      return;
    }
    if (!this.currentEnableMultiSelect) {
      return;
    }

    let newCheckedValue = false;
    if (this.areAllChecked) {
      this.checkedItems = [];
      if (this.selectionWriteback) {
        for (const item of this.currentData) {
          (item as any).selected = false;
        }
      }
    } else {
      this.checkedItems = [];
      newCheckedValue = true;
      for (const item of this.currentData) {
        if (this.isItemSelectable(item)) {
          this.checkedItems.push(item);
          if (this.selectionWriteback) {
            (item as any).selected = true;
          }
        }
      }
    }

    this.statusChanged();
    this.emitCheckAll(newCheckedValue);
    for (const item of this.currentData) {
      this.emitItemChecked(item, newCheckedValue);
    }
    this.emitSelectionChanged();
  }

  setAllChecked(newValue: boolean) {
    if (!this.currentEnableMultiSelect) {
      return;
    }

    if (!newValue) {
      this.checkedItems = [];
      if (this.selectionWriteback) {
        for (const item of this.currentData) {
          (item as any).selected = false;
        }
      }
    } else {
      this.checkedItems = [];
      for (const el of this.currentData) {
        this.checkedItems.push(el);
        if (this.selectionWriteback) {
          (el as any).selected = true;
        }
      }
    }

    this.statusChanged();
    for (const item of this.currentData) {
      this.emitItemChecked(item, newValue);
    }
    this.emitSelectionChanged();
  }

  // checkbox select per visible columns

  isColumnCheckedForVisualization(item: ICosmoTableColumn) {
    return (this.checkedColumnsForVisualization || []).indexOf(item) !== -1;
  }

  get allVisibleColumnsChecked(): boolean {
    const dataItems = this.hideableColumns;
    if (!dataItems.length) {
      return false;
    }
    for (const item of dataItems) {
      if ((this.checkedColumnsForVisualization || []).indexOf(item) === -1) {
        return false;
      }
    }
    return true;
  }

  get anyVisibleColumnsChecked(): boolean {
    const dataItems = this.hideableColumns;
    if (!dataItems.length) {
      return false;
    }
    for (const item of dataItems) {
      if ((this.checkedColumnsForVisualization || []).indexOf(item) !== -1) {
        return true;
      }
    }
    return false;
  }

  get noVisibleColumnshecked(): boolean {
    return !this.anyVisibleColumnsChecked;
  }

  // checkbox select per filtri custom

  isContextFilterChecked(item: ICosmoTableContextFilter) {
    return (this.checkedContextFilters || []).indexOf(item) !== -1;
  }

  get allContextFilterChecked(): boolean {
    const dataItems = this.currentContextFilters;
    if (!dataItems.length) {
      return false;
    }
    for (const item of dataItems) {
      if ((this.checkedContextFilters || []).indexOf(item) === -1) {
        return false;
      }
    }
    return true;
  }

  get anyContextFilterChecked(): boolean {
    const dataItems = this.currentContextFilters;
    if (!dataItems.length) {
      return false;
    }
    for (const item of dataItems) {
      if ((this.checkedContextFilters || []).indexOf(item) !== -1) {
        return true;
      }
    }
    return false;
  }

  get noContextFiltersChecked(): boolean {
    return !this.anyContextFilterChecked;
  }

  toggleContextFilterChecked(item: ICosmoTableContextFilter) {
    if (!this.currentEnableContextFiltering) {
      return;
    }

    if (this.checkedContextFilters === null) {
      this.checkedContextFilters = [];
    }

    if (this.isContextFilterChecked(item)) {
      this.checkedContextFilters.splice(this.checkedContextFilters.indexOf(item), 1);
    } else {
      this.checkedContextFilters.push(item);
      if (item.group) {
        this.checkedContextFilters = this.checkedContextFilters.filter(o => {
          return (o.name === item.name) || (!o.group) || (o.group !== item.group);
        });
      }
    }

    this.emitContextFiltersSelectionChanged();

    this.setPage(0);
    this.statusChanged();
    this.reload({reason: CosmoTableReloadReason.USER});
  }

  private buildStatusSnapshot(): ICosmoTableStatusSnapshot {
    return {
      schemaVersion: this.configurationService.getConfiguration().currentSchemaVersion || 'NONE',
      dataSnapshot: this.dataSnapshot,
      orderColumn: this.currentSortColumn ? this.currentSortColumn.name : null,
      orderColumnDirection: this.currentSortDirection || null,
      query: this.currentSearchQuery,
      queryColumns: (this.checkedColumnsForFiltering || []).map(c => c.name) as string[],
      visibleColumns: (this.visibleColumns || []).map(c => c.name) as string[],
      contextFilters: (this.checkedContextFilters || []).map(c => c.name),
      headerFilters: (this.checkedHeaderFilters || []).map(c => c.name),
      currentPage: this.currentPageIndex,
      pageSize: this.currentPageSize,
      checkedItems: !this.currentEnableSelection ? [] : (this.checkedItems || []).map(v => v),
      expandedItems:  !this.currentEnableRowExpansion ? [] : (this.expandedItems || []).map(v => v),
      tableContext: this.tableContext
    };
  }

  private buildPersistableStatusSnapshot(): ICosmoTablePersistableStatusSnapshot {
    return {
      schemaVersion: this.configurationService.getConfiguration().currentSchemaVersion || 'NONE',
      orderColumn: this.currentSortColumn ? this.currentSortColumn.name : null,
      orderColumnDirection: this.currentSortDirection || null,
      query: this.currentSearchQuery,
      queryColumns: (this.checkedColumnsForFiltering || []).map(c => c.name) as string[],
      visibleColumns: (this.visibleColumns || []).map(c => c.name) as string[],
      contextFilters: (this.checkedContextFilters || []).map(c => c.name),
      headerFilters: (this.checkedHeaderFilters || []).map(c => c.name),
      currentPage: this.currentPageIndex,
      pageSize: this.currentPageSize,
      checkedItemIdentifiers: !this.itemTrackingEnabledAndPossible ? [] :
        (this.checkedItems || []).map(item => this.getItemIdentifier(item)).filter(v => !!v),
      expandedItemIdentifiers:  !this.itemTrackingEnabledAndPossible ? [] :
        (this.expandedItems || []).map(item => this.getItemIdentifier(item)).filter(v => !!v)
    };
  }

  private statusChanged() {
    this.statusSnapshot = this.buildStatusSnapshot();
    this.persistableStatusSnapshot = this.buildPersistableStatusSnapshot();
    this.logger.trace('table status changed', this.persistableStatusSnapshot);

    this.emitStatusChanged();
  }

  private persistStatusToStore(): Observable<void> {
    if (!this.statusSnapshot) {
      return throwError('No status to save');
    }
    return new Observable( subscriber => {
      if (this.storePersistenceEnabledAndPossible) {
        this.logger.trace('passing status to persistence store');
        if (this.storeAdapter?.save) {
          this.storeAdapter.save({
            status: this.persistableStatusSnapshot
          }).subscribe(result => {
            this.logger.trace('saved status snapshot to persistence store');
            subscriber.next();
            subscriber.complete();
          }, failure => {
            this.logger.warn('failed to save status snapshot to persistence store', failure);
            subscriber.error(failure);
            subscriber.complete();
          });
        } else {
          subscriber.error('No save function in store adapter');
          subscriber.complete();
        }
      }
      subscriber.next();
      subscriber.complete();
    });
  }

  private loadStatusFromStore(): Observable<ICosmoTablePersistableStatusSnapshot | null> {
    return new Observable( subscriber => {
      if (this.storePersistenceEnabledAndPossible) {
        this.logger.trace('fetching status from persistence store');
        if (this.storeAdapter?.load) {
          this.storeAdapter.load().subscribe(result => {
            this.logger.trace('fetched status snapshot from persistence store');
            subscriber.next(result);
            subscriber.complete();
          }, failure => {
            subscriber.error(failure);
            this.logger.warn('failed to fetch status snapshot from persistence store', failure);
            subscriber.complete();
          });
        } else {
          subscriber.error('No load function in store adapter');
          subscriber.complete();
        }
      }
      subscriber.next(null);
      subscriber.complete();
    });
  }

  get dragInProgress(): boolean {
    return (
      $('.cdk-drag-preview:visible').length +
      $('.cdk-drag-placeholder:visible').length +
      $('.cdk-drop-list-dragging:visible').length +
      $('.cdk-drop-list-receiving:visible').length
    ) > 0;
  }

  // event emitters

  private emitCheckAll(newValue: boolean) {
    this.allChecked.next(newValue);
  }

  private emitItemChecked(row: any, newValue: boolean) {
    this.itemChecked.next([row, newValue]);
  }

  private emitSelectionChanged() {
    this.selectionChange.emit(this.checkedItems || undefined);
  }

  private emitSortChanged() {
    this.sortChange.emit({
      column: this.currentSortColumn,
      direction: this.currentSortDirection
    });
  }

  private emitPageChanged() {
    this.pageChange.emit(this.selectedPageIndex || undefined);
  }

  private emitVisibleColumnsSelectionChanged() {
    this.visibleColumnsChange.emit(this.checkedColumnsForVisualization || undefined);
  }

  private emitContextFiltersSelectionChanged() {
    this.contextFiltersChange.emit(this.checkedContextFilters || undefined);
  }

  private emitHeaderFiltersSelectionChanged() {
    this.headerFiltersChange.emit(this.checkedHeaderFilters || undefined);
  }

  private emitStatusChanged() {
    if (this.statusSnapshot) {
      this.statusChange.emit(this.statusSnapshot);
    }
  }

  get parsedColumns(): ICosmoTableColumn[] {
    const output = [];
    const usedNames = [];
    for (const col of (this.columns || [])) {
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

  get currentSelectablePageSizes(): number[] {
    const available = this.currentPossiblePageSizes;
    if (!available) {
      return available;
    }

    if (this.currentResultNumber === 0) {
      return [];
    }

    const filtered = available.filter( num => this.currentPageSize === num || num <= this.currentResultNumber );

    // add topper one if available
    if (filtered.length < available.length) {
      if (!filtered.length || filtered[filtered.length - 1] < this.currentResultNumber) {
        filtered.push( Math.min(...available.filter(n => n > filtered[filtered.length - 1])) );
      }
    }

    return filtered;
  }

  get shouldDisplayPageSizePicker(): boolean {
    if (!this.pageSizeSelectEnabledAndPossible) {
      return false;
    }
    const available = this.currentSelectablePageSizes;
    if (!available.length) {
      return false;
    }

    if (available.length === 1 && available[0] === this.currentPageSize) {
      return false;
    }

    return true;
  }

  private updateDataSnapshot(newData: any[]): void {
    this.dataSnapshot = newData;
    try {
      this.dataSnapshotStr = this.serializeData(newData);
    } catch (err) {
      this.logger.warn('ERROR SAVING DATA SNAPSHOT SERIALIZATION', err);
      this.dataSnapshotStr = null;
    }
    this.groupedDataSnapshot = this.groupData(this.dataSnapshot);
  }

  private checkIfDataDiffers(newData: any[]): boolean {
    if (!this.dataSnapshot || !this.dataSnapshot.length) {
      return true;
    } else {
      try {
        return this.serializeData(newData) !== this.dataSnapshotStr;
      } catch (err) {
        this.logger.warn('ERROR COMPARING NEW DATA TO SAVE SNAPSHOT', err);
        return false;
      }
    }
  }

  private serializeData(newData: any[]): string {
    return JSON.stringify(newData.map(o => {
      const out: any = {};
      for (const k of Object.keys(o)) {
        if (!k.startsWith('_')) {
          out[k] = o[k];
        }
      }
      return out;
    }));
  }

  get compatibilityModeForDropDowns(): boolean {
    return false;
  }

  public getClassForRow(row: any): string {
    if (this.rowClassProvider) {
      return this.rowClassProvider(row, this.statusSnapshot);
    } else {
      return '';
    }
  }

  // export
  private buildDataForExport(): Observable<any[]> {
    const config = this.configurationService.getConfiguration().export;

    if (!this.dataProvider) {
      return of([...this.dataSnapshot ?? []]);
    }

    const context: ICosmoTableReloadContext = {
      inBackground: true,
      reason: CosmoTableReloadReason.EXPORT,
    };

    if (!this.currentEnablePagination) {
      return this.dataProvider(this.buildPageRequest(), context).pipe(
        map(pageResponse => pageResponse.content)
      );
    }

    const dp = this.dataProvider;

    const pageSize = this.maxRowExport ?? config?.maxResults;

    const fetchPageInObs: ((pageIndex: number, loaded: number) => Observable<any[]>) = (pageIndex: number, loaded: number) => {
      return dp(this.buildPageRequest(pageIndex, pageSize), context).pipe(
        mergeMap(pageResponse => {

          const resultsNow = (pageResponse.content?.length ?? 0) + (loaded ?? 0);
          const maxValue = this.maxRowExport ?? config?.maxResults;
          if (maxValue && (
              (pageResponse.totalElements && pageResponse.totalElements > maxValue)
              ||
              (resultsNow > maxValue)
            )
          ) {
            throw new Error('Non è possibile esportare più di ' + maxValue + ' risultati. Raffina la ricerca e riprova.');
          }

          return of([...pageResponse.content]);

        }),
      );
    };

    return fetchPageInObs(0, 0);
  }

  public exportXlsx() {
    const config = this.configurationService.getConfiguration().export;

    of(null).pipe(
      map(() => {
        if (config?.loadingAnimationProvider) {
          config.loadingAnimationProvider.start();
        } else {
          this.logger.warn('no loadingAnimationProvider configured at configuration level. fetching multiple results without user feedback is a bad thing.');
        }
      }),
      mergeMap(() => {
        return this.buildDataForExport();
      }),
      map(data => {
        const exportableList: any[] = this.buildXlsxExportData(data);
        CosmoTableExporterXlsx.exportAsExcelFile(exportableList, 'cosmo');
        return null;
      }),
      finalize(() => {
        if (config?.loadingAnimationProvider) {
          config.loadingAnimationProvider.stop();
        }
      }),
    ).subscribe(
      () => {
        this.logger.info('export finished');
      },
      failure => {
        this.logger.error('export failed: ', failure);
        if (config?.dialogueProvider) {
         config.dialogueProvider.error('Errore nell\'esportazione', failure);
        } else {
          alert('' + failure);
        }
      }
    );
  }

  private columnIsExportable(c: ICosmoTableColumn): boolean {
    if (c.includeInExport === true || c.includeInExport === false) {
      return c.includeInExport;
    }
    return !c.applyTemplate || !!c.valueExtractor || !!c.field;
  }

  private buildXlsxExportData(data: any[]): any[][] {
    // build the data as similar to the rendered table as possible

    const sourceList = [...data];
    const exportableList: any[] = [];

    const ignoreColumn: (c: ICosmoTableColumn) => boolean = (c: ICosmoTableColumn) => {
      return !this.columnIsExportable(c);
    };

    const columns = this.exportHiddenColumns && this.exportHiddenColumns===true ? this.parsedColumns.filter(c => !ignoreColumn(c))
     : this.visibleColumns.filter(c => !ignoreColumn(c));

    const labels: string[] = [];
    for (const column of columns) {
      labels.push(this.resolveLabel(column));
    }
    exportableList.push(labels);

    for (const source of sourceList) {
      const exportable: any[] = [];
      for (const column of columns) {
        exportable.push(this.extractValue(source, column));
      }
      exportableList.push(exportable);
    }

    return exportableList;
  }

  private buildBridge(): ICosmoTableBridge {
    const bridge = {
      getTableContext: () => this.tableContext,
      getStatusSnapshot: () => this.statusSnapshot,
      getColumns: () => this.columns,
      getCheckedColumnsForVisualization: () => this.checkedColumnsForVisualization,
      getParsedColumns: () => this.parsedColumns,
      getLastFetchedQuery: () => this.lastFetchedSearchQuery,
      getCheckedColumnsForFiltering: () => this.checkedColumnsForFiltering,
      getCheckedHeaderFilters: () => this.checkedHeaderFilters,
      getFetchedPageCount: () => this.fetchedPageCount,
      getFetchedResultNumber: () => this.fetchedResultNumber,
      getSelectedPageIndex: () => this.selectedPageIndex,
      getSelectedPageSize: () => this.selectedPageSize,
      getDefaultPageSize: () => this.defaultPageSize,
      getPossiblePageSizes: () => this.currentPossiblePageSizes,
      getCurrentSearchQuery: () => this.currentSearchQuery,
      visibleColumnsSelectionChanged: (columns: ICosmoTableColumn[]) => {
        this.checkedColumnsForVisualization = columns;
        this.statusChanged();
        this.emitVisibleColumnsSelectionChanged();
      },
      filteringChanged: (query: string | null, columns: ICosmoTableColumn[]) => {
        this.selectedSearchQuery = query;
        this.checkedColumnsForFiltering = columns;
        this.applySelectedFilter();
      },
      checkedFiltersChanged: (payload: ICosmoTableContextFilter[]) => {
        this.checkedHeaderFilters = payload;
        this.emitHeaderFiltersSelectionChanged();
        this.setPage(0);
        this.statusChanged();
        this.reload({reason: CosmoTableReloadReason.USER});
      },
      pageChanged: (pageIndex: number) => {
        this.setPage(pageIndex);
        this.statusChanged();
        this.reload({reason: CosmoTableReloadReason.USER});
      },
      pageSizeChanged: (pageSize: number) => {
        this.selectedPageSize = pageSize;
        this.setPage(0);
        this.statusChanged();
        this.reload({reason: CosmoTableReloadReason.USER});
      }
    };

    return bridge;
  }
}
