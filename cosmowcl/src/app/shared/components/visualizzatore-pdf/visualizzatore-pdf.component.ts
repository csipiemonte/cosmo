/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import { Component, HostListener, Input, ViewChild } from '@angular/core';
import { IPDFViewerApplication, NgxExtendedPdfViewerComponent, pdfDefaultOptions } from 'ngx-extended-pdf-viewer';
import { environment } from 'src/environments/environment';
import { ModalService } from '../../services/modal.service';
import { TranslateService } from '@ngx-translate/core';
import { MessaggioModale } from '../modals/due-opzioni/due-opzioni.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-visualizzatore-pdf',
  templateUrl: './visualizzatore-pdf.component.html',
  styleUrls: ['./visualizzatore-pdf.component.scss']
})

export class VisualizzatorePdfComponent {
  @Input() pdfSrc: Blob | string | null = null;
  @Input() height = '80vh';
  @Input() showBookmarkButton = false;
  @Input() showDownloadButton = false;
  @Input() showOpenFileButton = false;
  @Input() textLayer = true;
  @Input() ignoreKeyboard = false;
  @Input() ignoreKeys = [];
  @Input() riquadroFirma = false;
  @Input() zoom: string | number | undefined;
  @Input() showZoomButtons = true;
  @Input() pageViewMode!: 'single' | 'book' | 'multiple' | 'infinite-scroll';
  @Input() templateType!: 'classic' | 'void';
  showDragAndDrop = false;
  position = '';
  dragPosition = {x: 240, y: 10};
  boundX = {start: 0, end: 0};
  boundY = {start: 0, end: 0};
  boxWidth = window.innerWidth < 1500 ? 75 : 150;
  canvasHeight = 0;
  canvasWidth = 0;
  signatureIcon = environment.deployContextPath + '/assets/cwwcl/i/signature-solid.svg';

  @ViewChild('container') container: NgxExtendedPdfViewerComponent | null = null;

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.boxWidth = event.target.innerWidth < 1500 ? 75 : 150;
    const canvas: HTMLCanvasElement = (
      document.querySelector('canvas')
    ) as HTMLCanvasElement;
    const rect = canvas.getBoundingClientRect();
    if (rect.height > 0) {
      this.canvasHeight = rect.height;
    }
    if (rect.width > 0) {
      this.canvasWidth = rect.width;
    }

    const PDFViewerApplication: IPDFViewerApplication = (window as any).PDFViewerApplication;
    const currentPage = PDFViewerApplication.page;
    const textLayer = document.getElementsByClassName('textLayer');
    const outerContainer = document.getElementsByClassName('html');
    this.dragPosition.x = (textLayer[currentPage - 1].getBoundingClientRect().left - outerContainer[0].getBoundingClientRect().left);
    this.boundX.start = this.dragPosition.x;
    this.boundX.end = this.dragPosition.x + this.canvasWidth - this.boxWidth;
    this.boundY.start = (textLayer[currentPage - 1].getBoundingClientRect().top - outerContainer[0].getBoundingClientRect().top);
    this.boundY.end = this.boundY.start + this.canvasHeight;

  }

  constructor(
    private modalService: ModalService,
    private translateService: TranslateService,
    private modal: NgbActiveModal) { }

  showDragAndDropActivation() {
    this.showDragAndDrop = !this.showDragAndDrop;
    return this.showDragAndDrop;
  }

  public async onDragEnd(event: any){
    const PDFViewerApplication: IPDFViewerApplication = (window as any).PDFViewerApplication;
    const pdf = PDFViewerApplication.pdfDocument;
    const currentPage = PDFViewerApplication.page; // pdf.getPage(PDFViewerApplication.page);
    const element = event.source.element.nativeElement;
    const transform = element.style.transform;
    const regex = /translate3d\(\s?(?<x>[-]?\d*)px,\s?(?<y>[-]?\d*)px,\s?(?<z>[-]?\d*)px\)/;
    const values = regex.exec(transform);
    let xxx = 0;
    let yyy = 0;

    if (values) {
      xxx = element.offsetLeft + +values[1];
      yyy = element.offsetTop + +values[2];

      if ((xxx < this.boundX.start || xxx > this.boundX.end) || (yyy < this.boundY.start || yyy > this.boundY.end)) {
        this.modalService.simpleInfo('Selezionare un\' area valida all\' interno del documento')
        .then(() => {})
        .catch(() => { });
      } else {
      const modalText: MessaggioModale[] = [];
      modalText.push({
          testo: this.translateService.instant('template_fea.actions.conferma_dati_riquadro_firma')
        });
      this.modalService.scegli(
        this.translateService.instant('template_fea.actions.conferma_dati_riquadro_firma'),
          modalText,
          [
            { testo: this.translateService.instant('common.si'), valore: 'primoBottone', classe: 'btn-outline-primary' },
            { testo: this.translateService.instant('common.no'), valore: 'secondoBottone', classe: 'btn-primary', defaultFocus: true }
          ])
          .then(proceed => {
            if ('primoBottone' === proceed) {
              // resize for pdfBox
              const coordinataX = this.resizeForPdfBox(595, xxx, this.boundX.start, this.boundX.end);
              const coordinataY = this.resizeForPdfBox(841, yyy, this.boundY.start, this.boundY.end);
              if ('void' === this.templateType) {
                this.modal.dismiss({ type: 'infoAxis', done: {x: coordinataX, y: coordinataY}});
              } else {
                this.modal.dismiss({ type: 'infoPageAndAxis', done: {page: currentPage, x: coordinataX, y: coordinataY}});
              }
            }
          })
          .catch(() => { });
      }
    }
  }

  private resizeForPdfBox(maxAxis: number, value: number, start: number, end: number) {
    const rapporto = maxAxis / (end - start);
    const rapportoSecondo = value - start;
    return (rapporto * rapportoSecondo).toFixed(2);
  }

  get isContextAvaible() {
    if (!!environment) {
      if (environment.deployContextPath.length > 0) {
        pdfDefaultOptions.assetsFolder = 'cosmo/assets/visualizzatorePdf';
      } else {
        pdfDefaultOptions.assetsFolder = 'assets/visualizzatorePdf';
      }

      return true;
    }
    return false;
  }

  pageRendered(event: any) {
    const canvas: HTMLCanvasElement = (
      document.querySelector('canvas')
    ) as HTMLCanvasElement;
    const PDFViewerApplication: IPDFViewerApplication = (window as any).PDFViewerApplication;
    const currentPage = PDFViewerApplication.page;
    const rect = canvas.getBoundingClientRect();
    if (rect.height > 0) {
      this.canvasHeight = rect.height;
    }
    if (rect.width > 0) {
      this.canvasWidth = rect.width;
    }
    const textLayer = document.getElementsByClassName('textLayer');
    const outerContainer = document.getElementsByClassName('html');
    this.dragPosition.x = (textLayer[currentPage - 1].getBoundingClientRect().left - outerContainer[0].getBoundingClientRect().left);
    this.boundX.start = this.dragPosition.x;
    this.boundX.end = this.dragPosition.x + this.canvasWidth - this.boxWidth;
    this.boundY.start = (textLayer[currentPage - 1].getBoundingClientRect().top - outerContainer[0].getBoundingClientRect().top);
    this.boundY.end = this.boundY.start + this.canvasHeight;
  }

}
