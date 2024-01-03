/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Injectable } from '@angular/core';

import {
  NgbModal,
  NgbModalOptions,
  NgbModalRef,
} from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';

import {
  ConfermaRifiutaModalComponent,
} from '../components/modals/conferma-rifiuta-modal/conferma-rifiuta-modal.component';
import {
  DueOpzioniComponent,
  MessaggioModale,
  PulsanteModale,
} from '../components/modals/due-opzioni/due-opzioni.component';
import {
  ErrorModalComponent,
} from '../components/modals/error-modal/error-modal.component';
import { HtmlCustomModalComponent } from '../components/modals/html-custom-modal/html-custom-modal.component';
import {
  InfoModalComponent,
} from '../components/modals/info-modal/info-modal.component';
import {
  LogoutModalComponent,
} from '../components/modals/logout-modal/logout-modal.component';
import {
  ShowRawDataModalComponent,
} from '../components/modals/show-raw-data-modal/show-raw-data-modal.component';

@Injectable()
export class ModalService {

  modalOptions: NgbModalOptions = { backdrop: 'static', keyboard: false, size: 'lg' };

  constructor(public modalService: NgbModal, private translateService: TranslateService) {
  }

  simpleInfo(messaggio: string): Promise<any> {

    const modalRef: NgbModalRef = this.modalService.open(InfoModalComponent, this.modalOptions);

    modalRef.componentInstance.messaggio = this.translateService.instant(messaggio);
    modalRef.componentInstance.titolo = this.translateService.instant('common.informazione');
    modalRef.componentInstance.bottone = this.translateService.instant('common.ok');

    return modalRef.result;
  }

  info(titolo: string, messaggio: string, bottone?: string): Promise<any> {
    bottone = bottone ?? this.translateService.instant('common.ok');

    const modalRef: NgbModalRef = this.modalService.open(InfoModalComponent, this.modalOptions);

    modalRef.componentInstance.messaggio = messaggio;
    modalRef.componentInstance.titolo = titolo;
    modalRef.componentInstance.bottone = bottone;

    return modalRef.result;
  }

  simpleError(messaggio: string, eccezione?: string): Promise<any> {

    const modalRef: NgbModalRef = this.modalService.open(ErrorModalComponent, this.modalOptions);
    const msg = this.translateService.instant('errori.error_message').replace('${message}', messaggio);

    modalRef.componentInstance.messaggio = msg;
    modalRef.componentInstance.eccezione = eccezione;
    modalRef.componentInstance.titolo = this.translateService.instant('errori.error_title');
    modalRef.componentInstance.bottone = this.translateService.instant('common.ok');

    return modalRef.result;
  }

  error(titolo: string, messaggio: string, eccezione?: string, bottone?: string): Promise<any> {
    bottone = bottone ?? this.translateService.instant('common.ok');

    const modalRef: NgbModalRef = this.modalService.open(ErrorModalComponent, this.modalOptions);

    modalRef.componentInstance.messaggio = messaggio;
    modalRef.componentInstance.eccezione = eccezione;
    modalRef.componentInstance.titolo = titolo;
    modalRef.componentInstance.bottone = bottone;

    return modalRef.result;
  }

  confermaRifiuta(titolo: string, messaggio: string, primoBottone?: string, secondoBottone?: string): Promise<any> {
    primoBottone = primoBottone ?? this.translateService.instant('common.si');
    secondoBottone = secondoBottone ?? this.translateService.instant('common.no');

    const modalRef: NgbModalRef = this.modalService.open(ConfermaRifiutaModalComponent, this.modalOptions);

    modalRef.componentInstance.messaggio = messaggio;
    modalRef.componentInstance.titolo = titolo;
    modalRef.componentInstance.primoBottone = primoBottone;
    modalRef.componentInstance.secondoBottone = secondoBottone;

    return modalRef.result;
  }

  scegli(titolo: string, messaggio: string | MessaggioModale[], pulsanti: PulsanteModale[]): Promise<any> {

    const modalRef: NgbModalRef = this.modalService.open(DueOpzioniComponent, this.modalOptions);

    if (Array.isArray(messaggio)) {
      modalRef.componentInstance.messaggi = messaggio as MessaggioModale[];
    } else {
      modalRef.componentInstance.messaggi = [{
        testo: messaggio
      }];
    }

    modalRef.componentInstance.titolo = titolo;
    modalRef.componentInstance.pulsanti = pulsanti;

    return modalRef.result;
  }

  logout() {
    const modalRef: NgbModalRef = this.modalService.open(LogoutModalComponent, this.modalOptions);
    return modalRef.result;
  }

  showRawData(titolo: string, data: any): Promise<any> {

    const modalRef: NgbModalRef = this.modalService.open(ShowRawDataModalComponent, this.modalOptions);

    modalRef.componentInstance.data = data;
    modalRef.componentInstance.titolo = titolo;

    return modalRef.result;
  }

  htmlModal(titolo: string, innerHtml: string): Promise<any> {
    const modalRef: NgbModalRef = this.modalService.open(HtmlCustomModalComponent, this.modalOptions);

    modalRef.componentInstance.html = innerHtml;
    modalRef.componentInstance.title = titolo;
    return modalRef.result;
  }

}
