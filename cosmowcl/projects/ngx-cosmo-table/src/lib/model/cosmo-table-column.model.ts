import { ICosmoTableAction } from './cosmo-table-action.model';
import {
  CosmoTableFormatter,
  ICosmoTableFormatProvider,
  ICosmoTableFormatSpecification,
} from './cosmo-table-entities.model';

export interface ICosmoTableColumn {
  // nome interno della colonna.
  name?: string;

  // etichetta da mostrare nell'header della tabella
  label?: string;

  // chiave per ottenere l'etichetta da mostrare nell'header da i18n
  labelKey?: string;

  // specifica se mostrare la label in tabella. Default COLUMN_DEFAULTS.SHOW_LABEL_IN_TABLE
  showLabelInTable?: boolean;

  // campo dell'oggetto da cui estrarre il valore
  field?: string;

  // quando si ordina per questa colonna server-side, se questo campo e' valorizzato,
  // verra' mandato al server. in alternativa verra' inviato il campo 'field'
  serverField?: string;

  // funzione che calcola il valore da mostrare a video a partire dall'oggetto
  valueExtractor?: (input: any) => any;

  // indica se il rendering della colonna avviene attraverso templating
  applyTemplate?: boolean;

  // formatter da applicare per il rendering
  formatters?: ICosmoTableFormatSpecification | CosmoTableFormatter | ICosmoTableFormatProvider |
    (ICosmoTableFormatSpecification | CosmoTableFormatter | ICosmoTableFormatProvider)[];

  // indica se la colonna puo' essere oggetto di ordinamento. Se non specificato viene considerato COLUMN_DEFAULTS.CAN_SORT
  canSort?: boolean;

  // specifica se la colonna puo' essere nascosta dall'utente. Se non specificato viene considerato COLUMN_DEFAULTS.CAN_HIDE
  canHide?: boolean;

  // specifica se la colonna viene visualizzata di default. Se non specificato viene considerato COLUMN_DEFAULTS.DEFAULT_VIEW
  defaultVisible?: boolean;

  // specifica se la colonna puo' essere utilizzata nel filtro testuale. Se non specificato viene considerato COLUMN_DEFAULTS.CAN_FILTER
  canFilter?: boolean;

  // specifica se la colonna viene utilizzata di default per il filtro. Se non specificato viene considerato COLUMN_DEFAULTS.DEFAULT_FILTER
  defaultFilter?: boolean;

  // specifica la dimensione della colonna: uno di xxs, xs, s, m, lg, xl, xxl
  // UTILIZZATO SOLO IN MODALITA' DI COMPATIBILITA' CON IE11
  size?: string;

  // classe CSS da attribuire alla cella nell'header
  headerDisplayClass?: string;

  // classe CSS da attribuire alla cella
  cellDisplayClass?: string;

  // azioni dropdown associate alla colonna
  actions?: ICosmoTableAction[];

  // icona del dropdown azioni
  actionsIcon?: string;

  // ovveride visibilita' in fase di export
  includeInExport?: boolean;

}
