/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Funzionalita } from '../api/cosmobusiness/funzionalita';
import { Lock } from '../api/cosmobusiness/lock';
import { Pratica } from '../api/cosmobusiness/pratica';
import { Task } from '../api/cosmobusiness/task';
import { VariabileProcesso } from '../api/cosmobusiness/variabileProcesso';
import { Attivita } from '../api/cosmopratiche/attivita';
import { FormLogiciConfig } from './form-logici-config.model';

export interface FormLogiciContext {
  pratica?: Pratica;
  funzionalita?: Funzionalita[];
  parametri?: Map<string, Map <string, string>>;
  formLogici?: FormLogiciConfig[];
  task?: Task;
  childTask?: Task;
  variabiliProcesso?: VariabileProcesso[];
  error?: string;
  ownLock?: Lock;
  otherLock?: Lock;
  readOnly?: boolean;
  attivita?: Attivita;
  childAttivita?: Attivita;
  wizard?: boolean;
}
