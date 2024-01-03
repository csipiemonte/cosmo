import { CommonModule } from '@angular/common';
import {
  HttpClient,
  HttpClientModule,
} from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { NgHttpLoaderModule } from 'ng-http-loader';
import { CosmoTableModule } from 'projects/ngx-cosmo-table/src/public-api';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {
  TranslateLoader,
  TranslateModule,
  TranslateService,
} from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BasicComponent } from './samples/basic/basic.component';
import {
  CellFormattingComponent,
} from './samples/cell-formatting/cell-formatting.component';
import {
  CheckboxFilteringComponent,
} from './samples/checkbox-filtering/checkbox-filtering.component';
import {
  ColumnToggleComponent,
} from './samples/column-toggle/column-toggle.component';
import {
  ContextActionsComponent,
} from './samples/context-actions/context-actions.component';
import {
  CustomCellComponent,
} from './samples/custom-cell/custom-cell.component';
import {
  DataProviderComponent,
} from './samples/data-provider/data-provider.component';
import {
  DragAndDropComponent,
} from './samples/drag-and-drop/drag-and-drop.component';
import { ExportComponent } from './samples/export/export.component';
import { FilteringComponent } from './samples/filtering/filtering.component';
import {
  HeaderActionsComponent,
} from './samples/header-actions/header-actions.component';
import {
  ObservableDataComponent,
} from './samples/observable-data/observable-data.component';
import { PaginationComponent } from './samples/pagination/pagination.component';
import { RefreshComponent } from './samples/refresh/refresh.component';
import { ReorderComponent } from './samples/reorder/reorder.component';
import {
  RowExpansionComponent,
} from './samples/row-expansion/row-expansion.component';
import { SelectionComponent } from './samples/selection/selection.component';
import { SortingComponent } from './samples/sorting/sorting.component';
import {
  StatusStoreComponent,
} from './samples/status-store/status-store.component';

@NgModule({
  declarations: [
    AppComponent,
    BasicComponent,
    ColumnToggleComponent,
    SortingComponent,
    FilteringComponent,
    PaginationComponent,
    SelectionComponent,
    RowExpansionComponent,
    CustomCellComponent,
    CellFormattingComponent,
    HeaderActionsComponent,
    ContextActionsComponent,
    DragAndDropComponent,
    ReorderComponent,
    RefreshComponent,
    ObservableDataComponent,
    DataProviderComponent,
    StatusStoreComponent,
    CheckboxFilteringComponent,
    ExportComponent,
  ],
  imports: [
    CommonModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    CosmoTableModule,
    NgbModule,
    FontAwesomeModule,
    TranslateModule.forRoot({
      loader: {
          provide: TranslateLoader,
          useFactory: HttpLoaderFactory,
          deps: [HttpClient]
      }}),
    NgHttpLoaderModule.forRoot(),
  ],
  providers: [
    TranslateService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

// required for AOT compilation
export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}
