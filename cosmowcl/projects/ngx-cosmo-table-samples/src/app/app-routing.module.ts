import { NgModule } from '@angular/core';
import {
  RouterModule,
  Routes,
} from '@angular/router';

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

const sampleRoutes: Routes = [
  { path: 'basic', component: BasicComponent },
  { path: 'column-selection', component: ColumnToggleComponent },
  { path: 'sorting', component: SortingComponent },
  { path: 'checkbox-filtering', component: CheckboxFilteringComponent },
  { path: 'filtering', component: FilteringComponent },
  { path: 'pagination', component: PaginationComponent },
  { path: 'selection', component: SelectionComponent },
  { path: 'row-expansion', component: RowExpansionComponent },
  { path: 'cell-formatting', component: CellFormattingComponent },
  { path: 'custom-cell', component: CustomCellComponent },
  { path: 'header-actions', component: HeaderActionsComponent },
  { path: 'context-actions', component: ContextActionsComponent },
  { path: 'drag-and-drop', component: DragAndDropComponent },
  { path: 'reorder', component: ReorderComponent },
  { path: 'refresh', component: RefreshComponent },
  { path: 'observable-data', component: ObservableDataComponent },
  { path: 'data-provider', component: DataProviderComponent },
  { path: 'status-store', component: StatusStoreComponent },
  { path: 'export', component: ExportComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(sampleRoutes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
