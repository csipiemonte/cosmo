import {
  Component,
  Input,
} from '@angular/core';

import { TranslateService } from '@ngx-translate/core';

import {
  CosmoTableReloadReason,
  ICosmoTableReloadContext,
} from '../../model';
import { CosmoTableService } from '../../services';
import { CosmoTableLogger } from '../../utils';
import { CosmoTableSegment } from '../cosmo-table-segment.proto';

@Component({
  // tslint:disable-next-line: component-selector
  selector: 'cosmo-table-pagination',
  templateUrl: './cosmo-table-pagination.component.html',
  styleUrls: ['./cosmo-table-pagination.component.scss']
})
export class CosmoTablePaginationComponent extends CosmoTableSegment {

  private logger: CosmoTableLogger;

  @Input() nextOnlyMode?: boolean;

  @Input() enablePageSizeSelect: boolean | null = null;
  @Input() enable: boolean | null = null;
  @Input() labelGroup: string | string[] | null = null;

  constructor(
    private translateService: TranslateService,
    private configurationService: CosmoTableService
  ) {
    super();
    this.logger = new CosmoTableLogger('CosmoTablePaginationComponent');
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

  get currentPageCount(): number {
    return this.getBridge()?.getFetchedPageCount() || 0;
  }

  get currentPageIndex(): number {
    return this.getBridge()?.getSelectedPageIndex() ?? 0;
  }

  get selectedPageSize(): number | null {
    return this.getBridge()?.getSelectedPageSize() ?? null;
  }

  get currentPossiblePageSizes(): number[] {
    if (this.possiblePageSize && this.possiblePageSize.length) {
      return this.possiblePageSize;
    } else {
      return this.configurationService.getConfiguration()
        .pagination?.defaultPossiblePageSizes ?? [5, 10, 20];
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

  switchToPage(pageIndex: number, context?: ICosmoTableReloadContext) {
    if (this.currentPageIndex === pageIndex) {
      return;
    }
    if (!context) {
      context = {reason: CosmoTableReloadReason.USER};
    }
    const bridge = this.getBridge();
    if (bridge) {
      bridge.pageChanged(
        pageIndex
      );
    }
  }

}
