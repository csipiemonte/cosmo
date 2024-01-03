/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { SelezioneUtenteGruppo } from 'src/app/shared/components/ricerca-utente/ricerca-utente.component';
import { AttivitaUtente } from './attivita-utente.model';

export interface PianificazioneAttivita {
  utenteGruppo: SelezioneUtenteGruppo;
  attivita: AttivitaUtente;
}
