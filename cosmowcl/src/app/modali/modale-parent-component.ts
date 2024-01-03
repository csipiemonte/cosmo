/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Helper } from '../shared/models/api/cosmonotifications/helper';
import { HelperService } from '../shared/services/helper.service';

export abstract class ModaleParentComponent  {

  private modalName = 'MODALE GENERICO';
  public soloCodicePagina = false;
  public helper!: Helper;

  constructor(public helperService: HelperService) {
  }

  protected setModalName(modalName: string): void {
    this.modalName = modalName;
  }

  protected getModalName(): string {
    return this.modalName;
  }

  protected setUsaSoloCodicePagina(value: boolean) {
    this.soloCodicePagina = value;
  }

  protected getUsaSoloCodicePagina() {
    return this.soloCodicePagina;
  }

  protected searchHelperModale(codicePagina: string, codiceTab: string) {
    if (codicePagina === 'dettaglio_pratica' && codiceTab === 'docs') {
      codiceTab = codiceTab.concat('-dettaglio');
    }
    if (this.getUsaSoloCodicePagina()) {
      codiceTab = '';
    }
    const filters = this.setHelperFilters(codicePagina, codiceTab, this.getModalName());
    this.helperService.getHelpers(JSON.stringify(filters)).subscribe(h => {
      if (h && h.helpers?.length === 1) {
        this.helper = h.helpers[0];
      }
    });
  }

  private setHelperFilters(codicePagina: string, codiceTab: string, codiceModale: string) {
    const output: any = {
      filter: {},
    };

    output.filter.codicePagina = {
      eq: codicePagina
    };

    if (codiceTab) {
      output.filter.codiceTab = {
      eq: codiceTab
      };
    }

    if (codiceModale) {
      output.filter.codiceModale = {
        eq: codiceModale
      };
    }

    return output;
  }


}
