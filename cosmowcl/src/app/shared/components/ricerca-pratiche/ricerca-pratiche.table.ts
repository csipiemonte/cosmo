/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  CosmoTableColumnSize,
  CosmoTableFormatter,
  ICosmoTableColumn,
} from 'ngx-cosmo-table';

import { DateUtils } from '../../utilities/date-utils';

export const ricercaPraticheTableConfig = {

  columns: [
    {
      name: 'favorite',
      canHide: false, canSort: false,
      applyTemplate: true, size: CosmoTableColumnSize.XXS
    },
    {
        name: 'dataCreazionePratica', label: 'Apertura pratica',
        field: 'dataCreazionePratica',
        formatters: [{
            formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy'
        }, {
            format: (raw: any) => raw ?? '--',
        }],
        valueExtractor: raw => DateUtils.parse(raw.dataCreazionePratica),
        canSort: true,
    },
    { name: 'oggetto', label: 'Oggetto', field: 'oggetto', canSort: true },
    { name: 'tipoPratica', label: 'Tipologia', field: 'tipo.descrizione', serverField: 'tipologia', canSort: true,
      canHide: false,
    },
    { name: 'stato', label: 'Stato', field: 'stato.descrizione', serverField: 'stato', canSort: true,
      applyTemplate: true,
    },
    {
        name: 'dataAggiornamentoPratica', label: 'Ultima modifica',
        field: 'dataAggiornamentoPratica',
        formatters: [{
            formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy',
        }, {
            format: (raw: any) => raw ?? '--',
        }],
        valueExtractor: raw => DateUtils.parse(raw.dataAggiornamentoPratica),
        canSort: true,
    },
    {
        name: 'dataCambioStato', label: 'Ultimo cambio di stato',
        field: 'dataCambioStato',
        formatters: [{
            formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy'
        }, {
            format: (raw: any) => raw ?? '--',
        }],
        valueExtractor: raw => DateUtils.parse(raw.dataCambioStato),
        canSort: true,
    },
    {
      name: 'dettaglio',
      canHide: false, canSort: false,
      applyTemplate: true, size: CosmoTableColumnSize.XXS
    },
    {
      name: 'azioni_dropdown',
      canHide: false, canSort: false,
      applyTemplate: true, size: CosmoTableColumnSize.XS
    },
    {
      name: 'azioni_menu',
      canHide: false, canSort: false,
      applyTemplate: true, size: CosmoTableColumnSize.XXS
    },
  ] as ICosmoTableColumn[],

};
