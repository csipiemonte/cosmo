import {
  Component, EventEmitter, Input, Output
} from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { CosmoTableComponent } from '../../cosmo-table.component';
import { CosmoTableActionActivationCondition,
  CosmoTableActionVisibilityCondition,
  ICosmoTableAction,
  ICosmoTableActionDispatchingContext,
  ICosmoTableColumn,
  ICosmoTableContextFilter,
  ICosmoTableStatusSnapshot} from '../../model';
import { ICosmoTableBridge } from '../../model/cosmo-table-bridge.model';
import { CosmoTableLogger } from '../../utils';
import { CosmoTableSegment } from '../cosmo-table-segment.proto';

@Component({
  // tslint:disable-next-line: component-selector
  selector: 'cosmo-table-action-buttons',
  templateUrl: './cosmo-table-action-buttons.component.html',
  styleUrls: ['./cosmo-table-action-buttons.component.scss']
})
export class CosmoTableActionButtonsComponent extends CosmoTableSegment {

  @Input() actions: ICosmoTableAction[] | null = null;
  @Input() enable: boolean | null = null;
  @Input() labelGroup: string | string[] | null = null;
  @Input() visibilityProvider: ((action: ICosmoTableAction, status: ICosmoTableStatusSnapshot | null) => boolean) | null = null;
  @Input() statusProvider: ((action: ICosmoTableAction, status: ICosmoTableStatusSnapshot | null) => boolean) | null = null;

  @Input() display = 'inline';

  @Output() action = new EventEmitter<ICosmoTableActionDispatchingContext>();

  private logger: CosmoTableLogger;

  constructor(
    private translateService: TranslateService
  ) {
    super();
    this.logger = new CosmoTableLogger('CosmoTableActionButtonsComponent');
    this.logger.trace('building component');
  }

  get displayInline(): boolean {
    return !this.displayAsDropdown;
  }

  get displayAsDropdown(): boolean {
    return 'dropdown' === this.display;
  }

  get render(): boolean {
    return super.render && this.actionsEnabledAndPossible;
  }

  get currentEnableActions(): boolean {
    return this.enable ?? true;
  }

  get actionsEnabledAndPossible(): boolean {
    return !!this.actions && this.actions.length > 0 && this.currentEnableActions;
  }

  get anyActionsAllowed(): boolean {
    return !!(this.actions || []).find(a => this.isActionAllowed(a));
  }

  get anyActionsVisible(): boolean {
    return !!(this.actions || []).find(a => this.isActionVisible(a));
  }

  clickOnAction(action: ICosmoTableAction) {
    if (!this.currentEnableActions) {
      this.logger.warn('button actions are disabled');
      return;
    }
    if (!this.isActionAllowed(action)) {
      return;
    }

    const bridge = this.getBridge();
    const status = this.resolveStatusSnapshot();

    const dispatchContext: ICosmoTableActionDispatchingContext = {
      action,
      selectedItems: status?.checkedItems || [],
      status,
      tableContext: bridge?.getTableContext()
    };

    this.logger.debug('dispatching action ' + action.name + ' with payload', dispatchContext);

    this.action.emit(dispatchContext);
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

  isActionAllowed(
    action: ICosmoTableAction,
    def: CosmoTableActionActivationCondition | undefined = CosmoTableActionActivationCondition.ALWAYS
  ) {

    if (action.isEnabled && !action.isEnabled(this.resolveStatusSnapshot() || null)) {
      return false;
    }

    const actCond = action.activationCondition || def;
    if (actCond === CosmoTableActionActivationCondition.ALWAYS) {
      return true;
    } else if (actCond === CosmoTableActionActivationCondition.NEVER) {
      return false;
    } else if (actCond === CosmoTableActionActivationCondition.DYNAMIC) {
      // delega decisione a provider esterno
      if (!this.statusProvider) {
        this.logger.error('Action with dynamic enabling but no actionStatusProvider provided');
        return false;
      }

      return this.statusProvider(action, this.resolveStatusSnapshot() || null);
    }

    const numSelected = this.resolveStatusSnapshot()?.checkedItems?.length || 0;
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

  isActionVisible(
    action: ICosmoTableAction,
    def: CosmoTableActionVisibilityCondition | undefined = CosmoTableActionVisibilityCondition.ALWAYS
  ) {
    const actCond = action.visibilityCondition || def;
    if (action.isVisible && !action.isVisible(this.resolveStatusSnapshot() || null)) {
      return false;
    }

    if (actCond === CosmoTableActionVisibilityCondition.ALWAYS) {
      return true;
    } else if (actCond === CosmoTableActionVisibilityCondition.NEVER) {
      return false;
    } else if (actCond === CosmoTableActionVisibilityCondition.DYNAMIC) {
      // delega decisione a provider esterno
      if (!this.visibilityProvider) {
        this.logger.error('Action with dynamic enabling but no actionVisibilityProvider provided');
        return false;
      }

      return this.visibilityProvider(action, this.resolveStatusSnapshot() || null);
    }

    const numSelected = this.resolveStatusSnapshot()?.checkedItems?.length || 0;
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
}
