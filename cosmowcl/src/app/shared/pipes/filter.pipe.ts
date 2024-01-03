/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'filtro', pure : false  })
export class FilterPipe implements PipeTransform {

  transform(items: any[] = [], str: string = '', fields: string[] = [])  {
       return items.filter(e => this.getAsString(e, fields).includes(this.parseString(str)) );
  }

  private getAsString(e: any, fields: string[] ) {
      fields = fields.length === 0 ? Object.keys(e) : fields;
      return fields.map( x => e[x] ).join(' ').trim().toLowerCase();
  }

  private parseString(str: string){
    let result = str.toLowerCase().trim();
    while (result.includes('  ') ){
        result = result.replace('  ' , ' ' );
    }
    return result;
  }
}
