/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import { PipeTransform, Pipe } from '@angular/core';
import { Utente } from '../shared/models/api/cosmoauthorization/utente';

@Pipe({
  name: 'utenteFilter',
  pure: false
})
export class UtenteFilter implements PipeTransform {
  transform(input: Utente[], searchTerm: string) {
    if (!input) { return []; }
    if (!searchTerm) { return input; }

    return input.filter(utente => {
      return (utente.nome.toLowerCase().indexOf(searchTerm.toLowerCase()) !== -1) ||
        (utente.cognome.toLowerCase().indexOf(searchTerm.toLowerCase()) !== -1);
    });
  }
}
