import { Input, Directive } from '@angular/core';
import { CosmoTableComponent } from '../cosmo-table.component';
import { ICosmoTableStatusSnapshot } from '../model';
import { ICosmoTableBridge } from '../model/cosmo-table-bridge.model';

@Directive()
export abstract class CosmoTableSegment {

  @Input() table: CosmoTableComponent | null = null;

  get render(): boolean {
    return !!this.getBridge();
  }

  protected getBridge(): ICosmoTableBridge | null {
    return this.table?.bridge ?? null;
  }

  protected resolveStatusSnapshot(): ICosmoTableStatusSnapshot | null {
    return this.getBridge()?.getStatusSnapshot() ?? null;
  }
}
