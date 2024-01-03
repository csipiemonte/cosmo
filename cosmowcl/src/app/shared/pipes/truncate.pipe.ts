/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'truncate', pure : false  })
export class TruncatePipe implements PipeTransform {

  transform(items: any, limite: number = 10 )  {
    if (!items || typeof items !== 'string'){
      return [];
    }
    if (items.length <= limite) {
      return items;
    }
    if (limite < 5) {
      return items.substr(0, limite);
    } else {
      return items.substr(0, limite - 3) + '...';
    }
  }

}
