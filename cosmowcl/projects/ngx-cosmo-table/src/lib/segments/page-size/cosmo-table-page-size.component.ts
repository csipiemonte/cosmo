import {
  Component, Input
} from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { CosmoTableService } from '../../services';
import { CosmoTableLogger } from '../../utils';
import { CosmoTableSegment } from '../cosmo-table-segment.proto';

@Component({
  // tslint:disable-next-line: component-selector
  selector: 'cosmo-table-page-size',
  templateUrl: './cosmo-table-page-size.component.html',
  styleUrls: ['./cosmo-table-page-size.component.scss']
})
export class CosmoTablePageSizeComponent extends CosmoTableSegment {

  private logger: CosmoTableLogger;

  @Input() enable: boolean | null = null;
  @Input() labelGroup: string | string[] | null = null;

  constructor(
    private translateService: TranslateService,
    private configurationService: CosmoTableService
  ) {
    super();
    this.logger = new CosmoTableLogger('CosmoTablePageSizeComponent');
    this.logger.trace('building component');
  }

  get render(): boolean {
    return super.render && this.currentEnablePagination;
  }

  get currentEnablePagination(): boolean {
    return this.enable ?? true;
  }

  get defaultPageSize(): number {
    return this.getBridge()?.getDefaultPageSize() || 0;
  }

  get possiblePageSize(): number[] {
    return this.getBridge()?.getPossiblePageSizes() || [5, 10, 20];
  }

  get currentResultNumber(): number {
    return this.getBridge()?.getFetchedResultNumber() || 0;
  }

  get selectedPageSize(): number | null {
    return this.getBridge()?.getSelectedPageSize() ?? null;
  }

  get pageSizeSelectEnabledAndPossible(): boolean {
    return this.currentPossiblePageSizes && this.currentPossiblePageSizes.length > 0 &&
      this.currentEnablePagination;
  }

  get currentPossiblePageSizes(): number[] {
    if (this.possiblePageSize && this.possiblePageSize.length) {
      return this.possiblePageSize;
    } else {
      return this.configurationService.getConfiguration()
        .pagination?.defaultPossiblePageSizes ?? [5, 10, 20];
    }
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

  clickOnPageSize(pageSize: number) {

    const bridge = this.getBridge();
    if (bridge) {
      bridge.pageSizeChanged(
        pageSize
      );
    }
  }

}
