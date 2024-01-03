/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'itemSort', pure : false  })
export class ItemSortPipe implements PipeTransform {

  transform(items: any[], field: string ,  asc: boolean = true ): any[]{
      if (!items ){ return []; }
      const direction = asc ? 1 : -1;
      return items.sort((a, b) => ( a[field] + '' < b[field] + '' ? -1 * direction : 1 * direction));
  }


}
