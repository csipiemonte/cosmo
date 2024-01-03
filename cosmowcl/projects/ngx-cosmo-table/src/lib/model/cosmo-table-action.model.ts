import { ICosmoTableStatusSnapshot } from './cosmo-table-entities.model';

export interface ICosmoTableAction {
  // nome interno della action. OBBLIGATORIO
  name: string;

  // etichetta da mostrare nell'header della tabella
  label?: string;

  // chiave per ottenere l'etichetta da mostrare nell'header da i18n
  labelKey?: string;

  // classe bootstrap da associare al button
  displayClass?: string;

  // classi ulteriori da associare al button
  additionalClasses?: string;

  // condizione di attivazione. Se non specificato il default e' secondo ACTION_DEFAULTS
  activationCondition?: CosmoTableActionActivationCondition;

  // condizione di attivazione. Se non specificato il default e' secondo ACTION_DEFAULTS
  visibilityCondition?: CosmoTableActionVisibilityCondition;

  isEnabled?: (status: ICosmoTableStatusSnapshot | null) => boolean;

  isVisible?: (status: ICosmoTableStatusSnapshot | null) => boolean;

  // icona da visualizzare
  icon?: string;

  // classe dell'icona da visualizzare
  iconClass?: string;
}

export enum CosmoTableActionActivationCondition {
  NEVER = 'NEVER',
  ALWAYS = 'ALWAYS',
  SINGLE_SELECTION = 'SINGLE_SELECTION',
  MULTIPLE_SELECTION = 'MULTIPLE_SELECTION',
  NO_SELECTION = 'NO_SELECTION',
  DYNAMIC = 'DYNAMIC'
}

export enum CosmoTableActionVisibilityCondition {
  NEVER = 'NEVER',
  ALWAYS = 'ALWAYS',
  SINGLE_SELECTION = 'SINGLE_SELECTION',
  MULTIPLE_SELECTION = 'MULTIPLE_SELECTION',
  NO_SELECTION = 'NO_SELECTION',
  DYNAMIC = 'DYNAMIC'
}
