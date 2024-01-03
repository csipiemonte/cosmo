/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { TipoAssegnazioneEvento } from '../services/calendario.service';

export interface Evento {
    id?: string;
    assegnatario: string;
    dataCreazione: Date;
    descrizione: string;
    dataScadenza: Date;
    nome: string;
    tipoAssegnazione?: TipoAssegnazioneEvento;
}
