import * as FileSaver from 'file-saver';
import * as XLSX from 'xlsx';

const EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
const EXCEL_EXTENSION = '.xlsx';

export abstract class CosmoTableExporterXlsx {

  public static exportAsExcelFile(input: any[], excelFileName: string): void {
    const worksheet: XLSX.WorkSheet = XLSX.utils.aoa_to_sheet(input);
    const workbook: XLSX.WorkBook = {
      Sheets: {
        data: worksheet
      },
      SheetNames: ['data']
    };

    const excelBuffer: any = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
    this.saveAsExcelFile(excelBuffer, excelFileName);
  }

  private static saveAsExcelFile(buffer: any, fileName: string): void {
    const data: Blob = new Blob([buffer], {type: EXCEL_TYPE});

    const d = new Date();
    const datestring =
      ('0' + d.getDate()).slice(-2) + '-' +
      ('0' + (d.getMonth() + 1)).slice(-2) + '-' +
      d.getFullYear() + '-' +
      ('0' + d.getHours()).slice(-2) + '-' +
      ('0' + d.getMinutes()).slice(-2) + '-' +
      ('0' + d.getSeconds()).slice(-2);

    const finalFileName = fileName + '_' + datestring + EXCEL_EXTENSION;

    FileSaver.saveAs(
       data,
       finalFileName,
       {
         autoBom: false
       }
    );
  }
}
