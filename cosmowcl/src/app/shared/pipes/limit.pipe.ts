/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'limite', pure : false  })
export class LimitPipe implements PipeTransform {

  transform(items: any[], limite: number = 10 )  {
       if (!items){
           return [];
       }
       return items.filter( (e, i) => i < limite );
  }

}
