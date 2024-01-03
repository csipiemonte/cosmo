import { ICosmoTableColumn, ICosmoTablePageResponse, ICosmoTablePageRequest, CosmoTableSortDirection } from '../model';
import { CosmoTableFormatterHelper } from './cosmo-table-formatter.utils';

export abstract class CosmoTableInMemoryHelper {

  public static fetchInMemory(
    data: any[],
    request: ICosmoTablePageRequest,
    sortColumn?: ICosmoTableColumn | null,
    filteringColumns?: ICosmoTableColumn[],
    locale?: string ): ICosmoTablePageResponse {
    const output: ICosmoTablePageResponse = {
      content: [],
      size: 0,
      totalElements: 0,
      totalPages: 0
    };

    if (!data || !data.length) {
      return output;
    }

    // apply filtering
    let filtered = data;
    if (request.query && request.query.length && request.query.trim().length
      && request.queryFields && request.queryFields.length) {

      const cleanedSearchQuery = request.query.trim().toLowerCase();
      filtered = filtered.filter(candidateRow => {
        let allowed = false;
        if (filteringColumns) {
          for (const filteringColumn of filteringColumns) {
            const extractedValue = CosmoTableFormatterHelper.extractValue(candidateRow, filteringColumn, locale);
            if (extractedValue) {
              const extractedValueStr = extractedValue + '';
              if (extractedValueStr.toLowerCase().indexOf(cleanedSearchQuery) !== -1) {
                allowed = true;
                break;
              }
            }
          }
        }
        return allowed;
      });
    }

    // apply pagination
    let startingIndex;
    let endingIndex;

    if (request && request.page != null && request.size != null &&
      (typeof request.page !== 'undefined') && (typeof request.size !== 'undefined') && (request.page === 0 || request.page > 0)) {
      startingIndex = request.page * request.size;
      endingIndex = (request.page + 1) * request.size;
      if (startingIndex >= filtered.length ) {
        return output;
      }

      if (endingIndex > filtered.length ) {
        endingIndex = filtered.length;
      }
    } else {
      startingIndex = 0;
      endingIndex = filtered.length;
    }

    if (request.sort && request.sort.length && request.sort[0]) {
      const sortDirection: number = request.sort[0].direction === CosmoTableSortDirection.DESCENDING ? -1 : +1;

      if (sortColumn) {
        filtered.sort((c1, c2) => {
          let v1 = CosmoTableFormatterHelper.extractRawValue(c1, sortColumn, locale) || '';
          let v2 = CosmoTableFormatterHelper.extractRawValue(c2, sortColumn, locale) || '';
          const v1n = (v1 === null || v1 === undefined || Number.isNaN(v1));
          const v2n = (v2 === null || v2 === undefined || Number.isNaN(v2));
          if (typeof v1 === 'string') {
            v1 = v1.toUpperCase();
          }
          if (typeof v2 === 'string') {
            v2 = v2.toUpperCase();
          }
          if (v1n && v2n) {
            return 0;
          } else if (v1n && !v2n) {
            return -1 * sortDirection;
          } else if (!v1n && v2n) {
            return 1 * sortDirection;
          } else {
            return (v1 > v2 ? 1 : v1 < v2 ? -1 : 0)  * sortDirection;
          }
        });
      }
    }

    const fetchedPage = filtered.slice(startingIndex, endingIndex);
    output.content = fetchedPage;
    output.size = fetchedPage.length;
    output.totalElements = filtered.length;
    if (request.size) {
      output.totalPages = Math.ceil(filtered.length / request.size);
    } else {
      output.totalPages = 1;
    }
    return output;
  }

  public static sort(data: any[], fields: string[]) {
    const copy = data.map(o => o);
    copy.sort(CosmoTableInMemoryHelper.fieldSorter(fields));
    return copy;
  }

  public static fieldSorter(fields: any[]) {
    return (a: any, b: any) => {
        return fields
            .map(o => {
                let dir = 1;
                if (o[0] === '-') {
                   dir = -1;
                   o = o.substring(1);
                }
                let valA = CosmoTableFormatterHelper.getPropertyValue(a, o);
                let valB = CosmoTableFormatterHelper.getPropertyValue(b, o);

                if (valA === null || typeof valA === 'undefined') {
                  valA = 'ZZZZZZZZZZZZZZZZZZZZZZZZ';
                }

                if (valB === null || typeof valB === 'undefined') {
                  valB = 'ZZZZZZZZZZZZZZZZZZZZZZZZ';
                }

                if (typeof valA === 'string') {
                  valA = valA.toUpperCase();
                }

                if (typeof valB === 'string') {
                  valB = valB.toUpperCase();
                }

                if (valA > valB) {
                  return dir;
                }
                if (valA < valB) {
                  return -dir;
                }
                return 0;
            })
            .reduce((p, n) => {
                return p ? p : n;
            }, 0);
    };
  }
}
