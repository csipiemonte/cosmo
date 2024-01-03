
export interface ICosmoTableContextFilter {
  // nome interno del filter. OBBLIGATORIO
  name: string;

  // etichetta da mostrare nell'header della tabella
  label?: string;

  // chiave per ottenere l'etichetta da mostrare nell'header da i18n
  labelKey?: string;

  // chiave di gruppo per escludere filtri dello stesso gruppo
  group?: string;
}
