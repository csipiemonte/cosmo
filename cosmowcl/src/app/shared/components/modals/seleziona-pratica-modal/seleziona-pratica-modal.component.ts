/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TipologiaStatiPratica } from 'src/app/form-logici/models/tipologie-stati-pratica.model';
import { Constants } from 'src/app/shared/constants/constants';
import { TipoRelazionePraticaPratica } from 'src/app/shared/models/api/cosmopratiche/tipoRelazionePraticaPratica';
import { AssociaPraticheComponent } from '../../associa-pratiche/associa-pratiche.component';
import { RicercaPraticheComponent } from '../../ricerca-pratiche/ricerca-pratiche.component';
import { ModaleParentComponent } from 'src/app/modali/modale-parent-component';
import { HelperService } from 'src/app/shared/services/helper.service';

@Component({
  selector: 'app-seleziona-pratica-modal',
  templateUrl: './seleziona-pratica-modal.component.html',
  styleUrls: ['./seleziona-pratica-modal.component.scss']
})
export class SelezionaPraticaModalComponent extends ModaleParentComponent implements OnInit {

  titolo: string | null = null;
  messaggio: string | null = null;
  primoBottone: string | null = null;
  secondoBottone: string | null = null;

  messaggiErroriConfigurazione: string[] = [];
  defaultTipoAssociazione!: string;
  descrizioneInfo!: string;

  tipoRicerca: 'selezione' | 'associazione' = 'selezione';

  idPratica?: number;
  tipiRelazionePratica: TipoRelazionePraticaPratica[] = [];
  tipologieStatiPraticaDaAssociare: TipologiaStatiPratica[] = [];
  codicePagina!: string;
  codiceTab!: string;
  codiceModale!: string;

  @ViewChild('ricercaPraticheComponent') ricercaPraticheComponent: RicercaPraticheComponent | null = null;
  @ViewChild('associaPraticheComponent') associaPraticheComponent: AssociaPraticheComponent | null = null;

  constructor(
    public activeModal: NgbActiveModal,
    public helperService: HelperService
  ) {
    super(helperService);
  }
  ngOnInit(): void {
    this.setModalName(this.codiceModale);
    this.searchHelperModale(this.codicePagina, this.codiceTab);
  }

  get canProceed(): boolean {
    if (this.isAssociazione){
      return !!this.ricercaPraticheComponent && this.ricercaPraticheComponent.selectedItems?.length > 0
        && this.ricercaPraticheComponent.selectedItems.filter(item => item.id === this.idPratica).length === 0
        && !!this.associaPraticheComponent && !!this.associaPraticheComponent.tipoRelazioneSelezionata;
    } else{
      return !!this.ricercaPraticheComponent && this.ricercaPraticheComponent.selectedItems?.length > 0;
    }
  }

  get isAssociazione(): boolean {
    return this.tipoRicerca === 'associazione';
  }

  proceed(): void {
    if (!this.canProceed) {
      return;
    }

    const selectedItems = (this.ricercaPraticheComponent?.selectedItems ?? []);

    if (this.isAssociazione){
      this.activeModal.close({
        esitoRisultato: Constants.OK_MODAL,
        praticheDaAssociare : selectedItems.map(item => item.id),
        tipoRelazionePratica: this.associaPraticheComponent?.tipoRelazioneSelezionata || undefined
      });

    } else{
      if (selectedItems?.length === 1) {
        this.activeModal.close(selectedItems[0]);
      }else{
        throw new Error('Invalid selected items: ' + selectedItems.length);
      }
    }
  }


}
