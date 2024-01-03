/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgbModal, NgbModalOptions, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { finalize } from 'rxjs/operators';
import { SecurityService } from 'src/app/shared/services/security.service';
import { Pratica } from '../../models/api/cosmobusiness/pratica';
import { UserInfoWrapper } from '../../models/user/user-info';
import { Utils } from '../../utilities/utilities';
import { CommentoModalComponent } from './commento-modal/commento-modal.component';
import { Commento } from './models/commento.model';
import { CommentiService } from './services/commenti.service';

@Component({
  selector: 'app-commenti',
  templateUrl: './commenti.component.html',
  styleUrls: ['./commenti.component.scss']
})
export class CommentiComponent implements OnInit {

  componentName = 'CommentiComponent';

  @Input() pratica: Pratica | undefined;
  @Input() readOnly = false;
  @Output() numCommenti = new EventEmitter<number>();

  loading = 0;
  loadingError: any | null = null;
  commenti: Commento[] = [];
  user !: UserInfoWrapper;

  // plural pipe da mostrare nell'header del modale
  messageMapping: { [k: string]: string } =
    { '=0': 'Nessun commento presente.', '=1': 'Un commento presente.', other: '# commenti presenti.' };
  prossimiEventi: any;
  eventiCalendario: any;

  private modalOptions: NgbModalOptions = {
    backdrop: 'static',
    centered: true,
    keyboard: true,
    size: 'lg'
  };

  constructor(
    private logger: NGXLogger,
    private commentiService: CommentiService,
    private ngbModal: NgbModal,
    private secService: SecurityService
   ) { }

  ngOnInit() {
    this.logger.debug(this.componentName + ' - inizializzazione');
    this.secService.getCurrentUser().subscribe(user => {
      this.logger.debug(this.componentName + ' - ottenuto utente corrente');
      this.user = user;
      this.refresh();
    });
  }

  refresh() {
    if (!this.pratica?.linkPratica) {
      this.logger.warn('nessuna pratica con id processo per cui cercare i commenti');
      return;
    }

    const processId = Utils.getProcessId(this.pratica);
    if (!processId) {
      this.logger.warn('nessun id processo per cui cercare i commenti');
      return;
    }

    this.logger.debug(`${this.componentName} - refresh dei dati`);
    this.loading ++;
    this.loadingError = null;

    this.commentiService.recuperaCommentiPratica(processId)
    .pipe(
      finalize(() => {
        this.logger.debug(this.componentName + ' - termine del caricamento');
        this.loading--;
      })
    )
    .subscribe(result => {
      this.logger.debug(this.componentName + ' - caricati commenti');
      this.commenti = result?.elementi ?? [];
      this.numCommenti.emit(result?.elementi.length ?? 0);
    }, failure => {
      this.logger.error(this.componentName + ' - errore nel caricamento dei commenti');
      this.loadingError = failure;
    });
  }

  inserisciCommento() {
    if (!this.pratica?.linkPratica) {
      this.logger.warn('nessuna pratica');
      return;
    }

    const processId = Utils.getProcessId(this.pratica);
    if (!processId) {
      this.logger.warn('nessun id processo per cui cercare i commenti');
      return;
    }

    const modalRef: NgbModalRef = this.ngbModal.open(CommentoModalComponent, this.modalOptions);
    modalRef.componentInstance.idProcesso = processId;
    modalRef.result.then(() => this.refresh());
  }

}
