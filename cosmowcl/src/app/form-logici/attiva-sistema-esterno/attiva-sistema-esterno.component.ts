/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { AsyncTaskModalService } from 'src/app/shared/services/async-task-modal.service';
import { Component, Injector, OnInit } from '@angular/core';
import { mergeMap } from 'rxjs/operators';
import { Constants } from 'src/app/shared/constants/constants';
import { AttivazioneSistemaEsternoService } from 'src/app/shared/services/attivazione-sistema-esterno.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { FunzionalitaParentComponent } from '../funzionalita-parent.component';
import { from } from 'rxjs';
import { ModalService } from 'src/app/shared/services/modal.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-attiva-sistema-esterno',
  templateUrl: './attiva-sistema-esterno.component.html',
  styleUrls: ['./attiva-sistema-esterno.component.scss']
})
export class AttivaSistemaEsternoComponent extends FunzionalitaParentComponent implements OnInit {

  titolo?: string;
  link?: string;
  payload?: string;
  metodo?: string;

  constructor(
    public injector: Injector,
    private attivazioneSistemaEsternoService: AttivazioneSistemaEsternoService,
    private asyncTaskModalService: AsyncTaskModalService,
    private modalService: ModalService,
    private translateService: TranslateService
  ) {
    super(injector);
    if (this.parametri) {
      if (this.parametri.get(Constants.FORM_LOGICI.SYS_EXT_CALL.TITOLO_KEY)){
        this.titolo = this.parametri.get(Constants.FORM_LOGICI.SYS_EXT_CALL.TITOLO_KEY) as string;
      }
      if (this.parametri.get(Constants.FORM_LOGICI.SYS_EXT_CALL.LINK_KEY)){
        this.link = this.parametri.get(Constants.FORM_LOGICI.SYS_EXT_CALL.LINK_KEY) as string;
      }
      if (this.parametri.get(Constants.FORM_LOGICI.SYS_EXT_CALL.PAYLOAD_KEY)){
        this.payload = this.parametri.get(Constants.FORM_LOGICI.SYS_EXT_CALL.PAYLOAD_KEY) as string;
      }
      if (this.parametri.get(Constants.FORM_LOGICI.SYS_EXT_CALL.METODO_KEY)){
        this.metodo = this.parametri.get(Constants.FORM_LOGICI.SYS_EXT_CALL.METODO_KEY) as string;
      }
    }
  }

  doExternalCall() {
    switch (Utils.require(this.metodo)) {
      case 'GET': this.externalCallGet(); break;
      case 'POST': this.externalCallPost(); break;
      case 'PUT': this.externalCallPost(); break;
      case 'PATCH': this.externalCallPost(); break;
      case 'DELETE': this.externalCallPost(); break;
      default: throw new Error('Http method not found!');
    }
  }

  private externalCallGet() {
    this.attivazioneSistemaEsternoService.getPayload(
      Utils.require(this.idPratica),
      Utils.require(this.idAttivita)).subscribe(output => {
          let url = Utils.require(this.link);
          if (output) {
            const payload = Utils.getPropertyValue(output, 'payload');
            if (!!payload) {
              const params: URLSearchParams = new URLSearchParams(payload);
              url = url.concat('/?' + params.toString());
            }
          }
          if (/^http[s]?:\/\//.test(url)) {
            window.open(url, '_blank');
          } else {
            window.open('//' + url, '_blank');
          }
    });
  }

  private externalCallPost() {
    this.attivazioneSistemaEsternoService.setAttivazione(
      Utils.require(this.idPratica),
      Utils.require(this.idAttivita))
      .pipe(
        mergeMap(operazione =>
          from(this.asyncTaskModalService.open({ taskUUID: operazione.uuid}).result)
      )
      ).subscribe(
        () => {
          // chiamata andata a buon fine
        },
        error => {
          this.modalService.error(
            this.translateService.instant('errori.attivazione_sistema_esterno'),
            Utils.toMessage(error) ?? this.translateService.instant('errori.attivazione_sistema_esterno'),
            error.error.errore
          ).then(() => {}).catch(() => {});
        }
      );
  }

}
