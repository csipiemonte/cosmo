/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Constants } from 'src/app/shared/constants/constants';
import { AttivitaService } from 'src/app/shared/services/attivita.service';
import { BusService } from 'src/app/shared/services/bus.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { PraticheService } from 'src/app/shared/services/pratiche.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { RicercaUtenteComponent, SelezioneUtenteGruppo } from '../../ricerca-utente/ricerca-utente.component';
import { ModaleParentComponent } from 'src/app/modali/modale-parent-component';
import { HelperService } from 'src/app/shared/services/helper.service';


@Component({
  selector: 'app-assegna-attivita',
  templateUrl: './assegna-attivita.component.html',
  styleUrls: ['./assegna-attivita.component.scss']
})
export class AssegnaAttivitaComponent extends ModaleParentComponent implements OnInit, AfterViewInit {

  constructor(
    public modal: NgbActiveModal,
    private securityService: SecurityService,
    private attivitaService: AttivitaService,
    private praticaService: PraticheService,
    private busService: BusService,
    private modalService: ModalService,
    public helperService: HelperService
  ) {
    super(helperService);
  }

  public selectedUser?: SelezioneUtenteGruppo = undefined;
  idPratica?: number;
  idAttivita?: number;
  tipoPratica?: string;

  idEnte: number | undefined;
  currCf: string | undefined;

  mantieniAssegnazione = false;
  codicePagina!: string;
  codiceTab!: string;
  codiceModale!: string;
  soloCodicePagina = false;

  @ViewChild('defaultfocus') defaultfocus: RicercaUtenteComponent | null = null;

  ngOnInit(): void {
    this.securityService.getCurrentUser().subscribe((userInfo) => {
      if (userInfo) {
        this.idEnte = userInfo.ente?.id;
        this.currCf = userInfo.codiceFiscale;
      }
      this.praticaService.getPratica(this.idPratica ?? 0, false ).subscribe( response => {
        this.tipoPratica = response.tipo?.codice;
      });
    });
    this.setModalName(this.codiceModale);
    this.setUsaSoloCodicePagina(this.soloCodicePagina);
    this.searchHelperModale(this.codicePagina, this.codiceTab);
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if (this.defaultfocus) {
        this.defaultfocus.focus();
      }}, 0);
  }

  assegnaAttivita() {
    if (!this.idPratica || !this.selectedUser) {
      return;
    }

    if (this.idAttivita) {

      this.attivitaService.assegnaAttivita(this.idPratica, this.idAttivita,
        this.selectedUser.utente?.id, this.selectedUser.gruppo?.id, this.mantieniAssegnazione
      ).subscribe(() => {
        this.modal.close(Constants.OK_MODAL);
        this.busService.setCercaPratiche(true);
      }, failure => {
        this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore);
      });
    } else {

      this.praticaService.assegnaPratica(this.idPratica,
        this.selectedUser.utente?.id, this.selectedUser.gruppo?.id,
        this.mantieniAssegnazione
      ).subscribe(() => {
        this.modal.close(Constants.OK_MODAL);
        this.busService.setCercaPratiche(true);
      }, failure => {
        this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore);
      });
    }
  }

  filtroRicercaGruppi = (filtro: any) => {
    if (this.tipoPratica){
      filtro.codiceTipoPratica = filtro.codiceTipoPratica || {};
      filtro.codiceTipoPratica.eq = this.tipoPratica;
    }

    return filtro;
  }
}
