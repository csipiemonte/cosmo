/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { OperazioneAsincrona } from './api/cosmobusiness/operazioneAsincrona';

export enum AsyncTaskStatus {
    STARTED = 'STARTED',
    COMPLETED = 'COMPLETED',
    FAILED = 'FAILED',
}

export interface OperazioneAsincronaWrapper<T> extends Omit<OperazioneAsincrona, 'risultato'> {
    risultato: T | undefined;
    steps: OperazioneAsincronaWrapper<any>[] | undefined;
}
