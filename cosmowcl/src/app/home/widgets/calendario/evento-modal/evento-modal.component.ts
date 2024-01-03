/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit} from '@angular/core';
import { NgbDate, NgbActiveModal, NgbModalRef, NgbModal, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { I18n } from '../calendario.component';
import { NGXLogger } from 'ngx-logger';
import { CalendarioService } from '../services/calendario.service';
import { TranslateService } from '@ngx-translate/core';
import { Evento } from '../models/evento.model';
import { ModalService } from 'src/app/shared/services/modal.service';
import { NuovoEventoModalComponent } from '../nuovo-evento-modal/nuovo-evento-modal.component';
import { Utils } from 'src/app/shared/utilities/utilities';
import { ModaleParentComponent } from 'src/app/modali/modale-parent-component';
import { HelperService } from 'src/app/shared/services/helper.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-evento-modal',
  templateUrl: './evento-modal.component.html',
  styleUrls: ['./evento-modal.component.scss']
})
export class EventoModalComponent extends ModaleParentComponent implements OnInit {


  eventi: Evento[] = [];
  date?: NgbDate;
  i18n: I18n | null = null;
  I18N_VALUES: any;

  // plural pipe da mostrare nell'header del modale
  messageMapping:
      {[k: string]: string} = {'=1': 'all\'evento', other: 'ai # eventi'};
  nuovoEvento: any;
  codicePagina!: string;
  codiceTab!: string;
  codiceModale!: string;

  constructor(
    private logger: NGXLogger,
    private translateService: TranslateService,
    private calendarioService: CalendarioService,
    private modalService: ModalService,
    public modal: NgbActiveModal,
    private ngbModal: NgbModal,
    public helperService: HelperService,
    private route: ActivatedRoute) {
    super(helperService);
  }

  ngOnInit(): void {
    if (this.eventi) {
      this.logger.debug('eventi are', this.eventi);
    }
    this.setModalName(this.codiceModale);
    this.searchHelperModale(this.codicePagina, this.codiceTab);
  }

  modificaEvento(evento: Evento){
    if (!evento || !evento.id){
      return;
    }
    const modalOptions: NgbModalOptions = {
      backdrop: 'static',
      centered: true,
      size: 'lg'
    };
    const data = this.helperService.searchHelperRef(this.route);
    const modalRef: NgbModalRef = this.ngbModal.open(NuovoEventoModalComponent, modalOptions);
    modalRef.componentInstance.evento = evento;
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = 'calendario-modifica-evento';
    modalRef.result.then(res => {
        this.modal.close();
    });

  }

  eliminaEvento(evento: Evento){

    if (!evento || !evento.id || !evento.nome ){
      return;
    }

    this.modalService.scegli(
      this.translateService.instant('eventi.eliminazione_evento'),
      [{
        testo: Utils.parseAndReplacePlaceholders(
          this.translateService.instant('common.eliminazione_messaggio'), [evento.nome]),
        classe: 'text-danger'
      }], [
        {testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt'},
        {testo: 'Annulla', dismiss: true, defaultFocus: true}
      ]
    ).then(() => {
      this.calendarioService.eliminaEvento(evento.id ?? '').subscribe(response => {
        this.modal.close();
        this.modalService.simpleInfo(this.translateService.instant('eventi.evento_eliminato'))
        .catch(() => {});
      }, error => {
        this.modalService.error(this.translateService.instant('eventi.eliminazione_evento'),
          this.translateService.instant('errori.eliminazione_evento'), error.error.errore)
          .then(() => {
            this.modal.close();
          })
          .catch(() => { });
      });
    }).catch(() => {});
  }
}
