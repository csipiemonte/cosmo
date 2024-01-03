/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

export class FileTypeUtils {

  private static mimeMap: {[key: string]: string} = {
    'application/x-zip-compressed': 'Archivio ZIP',
    'application/CDFX+XML': 'File XML',
    'image/jpg': 'Immagine (jpg)',
    'image/jpeg': 'Immagine (jpg)',
    'image/gif': 'Immagine (gif)',
    'application/mp4': 'File video (MP4)',
    'application/octet-stream': 'File generico',
    'application/msword': 'Documento di testo (DOC)',
    'application/PDX': 'File PDX',
    'application/pem-certificate-chain': 'Certificato PEM',
    'application/rtf': 'Documento di testo (RTF)',
    'application/vnd.ms-excel': 'Foglio di calcolo (XLS)',
    'application/vnd.oasis.opendocument.spreadsheet-template': 'Foglio di calcolo',
    'application/vnd.oasis.opendocument.spreadsheet': 'Foglio di calcolo (ODS)',
    'application/vnd.oasis.opendocument.text': 'Documento di testo (ODT)',
    'application/vnd.openxmlformats-officedocument.wordprocessingml.document': 'Documento di testo (DOCX)',
    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet': 'Foglio di calcolo (XLSX)',
    'application/zip': 'Archivio ZIP',
    'application/x-compressed': 'Archivio ZIP',
    'multipart/x-zip': 'Archivio ZIP',
    'application/pkcs10': 'Contenuto firmato PKCS10',
    'application/pkcs8': 'Contenuto firmato PKCS8',
    'application/pkcs8-encrypted': 'Contenuto firmato PKCS8',
    'application/pkcs12': 'Contenuto firmato PKCS12',
    'application/pkcs7-signature': 'Firma PKCS7',
    'application/pdf': 'File PDF',
    'text/xml': 'File XML',
    'text/mpeg': 'File testuale',
    'text/csv': 'File CSV',
    'application/pkcs7-mime': 'Contenuto firmato PKCS7',
  };

  public static isFoglioDiCalcolo(mimeType: string): boolean{

    return mimeType === 'application/vnd.ms-excel' || mimeType === 'application/vnd.oasis.opendocument.spreadsheet-template' ||
            mimeType === 'application/vnd.oasis.opendocument.spreadsheet' ||
            mimeType === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
  }

  public static isArchivioZip(mimeType: string): boolean{
    return mimeType === 'application/x-zip-compressed' || mimeType === 'application/zip' ||
     mimeType === 'application/x-compressed' || mimeType === 'multipart/x-zip';
  }

  public static getMimeDescription(mimeType: string): string | null{
    if (!mimeType?.length) {
      return null;
    }
    const decoded = this.mimeMap[mimeType];
    const secondPart = mimeType.indexOf('/') !== -1 ? mimeType.split('/')[1] : null;
    const suffix = secondPart ? ' (' + secondPart + ')' : '';

    if (decoded) {
      return decoded;
    } else if (mimeType.startsWith('image/')) {
      return 'Immagine' + suffix;
    } else if (mimeType.startsWith('video/')) {
      return 'Video' + suffix;
    } else if (mimeType.startsWith('text/')) {
      return 'File di testo';
    } else if (mimeType.startsWith('audio/')) {
      return 'File audio' + suffix;
    } else if (mimeType.endsWith('+xml')) {
      return 'File XML';
    } else if (mimeType.endsWith('+json')) {
      return 'File JSON';
    }

    return null;
  }
}
