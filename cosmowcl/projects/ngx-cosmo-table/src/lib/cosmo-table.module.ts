import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { TranslateModule } from '@ngx-translate/core';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { CdkTableModule } from '@angular/cdk/table';
import { CosmoTableComponent } from './cosmo-table.component';
import { FormsModule } from '@angular/forms';
import { CosmoTableActionButtonsComponent } from './segments/action-buttons/cosmo-table-action-buttons.component';
import { CosmoTableColumnSelectorComponent } from './segments/column-selector/cosmo-table-column-selector.component';
import { CosmoTableSearchFilterComponent } from './segments/search-filter/cosmo-table-search-filter.component';
import { CosmoTableCheckboxFiltersComponent } from './segments/checkbox-filters/cosmo-table-checkbox-filters.component';
import { CosmoTablePaginationComponent } from './segments/pagination/cosmo-table-pagination.component';
import { CosmoTablePageSizeComponent } from './segments/page-size/cosmo-table-page-size.component';
import { CosmoTableResultsResumeComponent } from './segments/results-resume/cosmo-table-results-resume.component';

@NgModule({
  declarations: [
    CosmoTableComponent,
    CosmoTableActionButtonsComponent,
    CosmoTableColumnSelectorComponent,
    CosmoTableSearchFilterComponent,
    CosmoTableCheckboxFiltersComponent,
    CosmoTablePaginationComponent,
    CosmoTablePageSizeComponent,
    CosmoTableResultsResumeComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    FontAwesomeModule,
    NgbModule,
    TranslateModule,
    CdkTableModule,
    DragDropModule,
  ],
  exports: [
    CosmoTableComponent,
    CosmoTableActionButtonsComponent,
    CosmoTableColumnSelectorComponent,
    CosmoTableSearchFilterComponent,
    CosmoTableCheckboxFiltersComponent,
    CosmoTablePaginationComponent,
    CosmoTablePageSizeComponent,
    CosmoTableResultsResumeComponent,
  ],
  providers: [
  ]
})
export class CosmoTableModule { }
