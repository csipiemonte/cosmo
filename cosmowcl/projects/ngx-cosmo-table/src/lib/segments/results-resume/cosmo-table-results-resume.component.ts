import {
  Component, Input
} from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { CosmoTableService } from '../../services';
import { CosmoTableLogger } from '../../utils';
import { CosmoTableSegment } from '../cosmo-table-segment.proto';

@Component({
  // tslint:disable-next-line: component-selector
  selector: 'cosmo-table-results-resume',
  templateUrl: './cosmo-table-results-resume.component.html',
  styleUrls: ['./cosmo-table-results-resume.component.scss']
})
export class CosmoTableResultsResumeComponent extends CosmoTableSegment {

  private logger: CosmoTableLogger;

  @Input() enable: boolean | null = null;
  @Input() labelGroup: string | string[] | null = null;

  constructor(
    private translateService: TranslateService,
    private configurationService: CosmoTableService
  ) {
    super();
    this.logger = new CosmoTableLogger('CosmoTableResultsResumeComponent');
    this.logger.trace('building component');
  }

  get render(): boolean {
    return super.render && this.currentEnable;
  }

  get currentEnable(): boolean {
    return this.enable ?? true;
  }

  get currentResultNumber(): number {
    return this.getBridge()?.getFetchedResultNumber() || 0;
  }

  get currentPageCount(): number {
    return this.getBridge()?.getFetchedPageCount() || 0;
  }

}
