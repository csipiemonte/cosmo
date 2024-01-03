/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  Component,
  Input,
  OnDestroy,
  OnInit,
} from '@angular/core';

import {
  CosmoTableColumnSize,
  ICosmoTableColumn,
} from 'ngx-cosmo-table';
import { Subscription } from 'rxjs';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { TipoSegnalibroEnum } from '../shared/enums/tipo-segnalibro';
import { Preferenza } from '../shared/models/api/cosmo/preferenza';
import {
  TipoSegnalibro,
} from '../shared/models/api/cosmoauthorization/tipoSegnalibro';
import { IdentitaUtente } from '../shared/models/api/cosmoecm/identitaUtente';
import {
  ValorePreferenzeUtente,
} from '../shared/models/preferenze/valore-preferenze-utente.model';
import { ModalService } from '../shared/services/modal.service';
import {
  PreferenzeUtenteService,
} from '../shared/services/preferenze-utente.service';
import { SegnalibroService } from '../shared/services/segnalibro.service';
import { ModaleParentComponent } from '../modali/modale-parent-component';
import { HelperService } from '../shared/services/helper.service';

@Component({
  selector: 'app-identita-utente-acta',
  templateUrl: './identita-utente-acta.component.html',
  styleUrls: ['./identita-utente-acta.component.scss']
})
export class IdentitaUtenteActaComponent extends ModaleParentComponent implements OnInit, OnDestroy {

  @Input() identitaUtente: IdentitaUtente[] = [];
  identitaUtenteSelezionata: IdentitaUtente | null = null;
  segnalibro: IdentitaUtente | null = null;
  starClicked = false;

  private preferenzeUtenteSubscription!: Subscription;
  private actaSegnalibro?: TipoSegnalibro;
  private preferenzeUtente?: Preferenza;
  codicePagina!: string;
  codiceTab!: string;

  columns: ICosmoTableColumn[] = [
    {
      name: 'favorite', canHide: false, canSort: false, applyTemplate: true, size: CosmoTableColumnSize.XXS
    },
    {
      name: 'nodo', label: 'Nodo', field: 'nodo', canSort: true, canHide: false,
      valueExtractor: row => this.setRowValue(row.codiceNodo, row.descrizioneNodo)
    },
    {
      name: 'struttura', label: 'Struttura', field: 'struttura', canSort: true, canHide: false,
      valueExtractor: row => this.setRowValue(row.codiceStruttura, row.descrizioneNodo)
    },
    {
      name: 'aoo', label: 'AOO', field: 'aoo', canSort: true, canHide: false,
      valueExtractor: row => this.setRowValue(row.codiceAOO, row.descrizioneAOO)
    }
  ];

  private setRowValue(codice: string, descrizione: string): string {
    if (codice && descrizione) {
      return codice + ' - ' + descrizione;
    } else if (codice && !descrizione) {
      return codice;
    } else if (!codice && descrizione) {
      return descrizione;
    } else {
      return '--';
    }
  }

  constructor(
    public modal: NgbActiveModal,
    private preferenzeUtenteService: PreferenzeUtenteService,
    private segnalibroService: SegnalibroService,
    private modalService: ModalService,
    public helperService: HelperService,
    ) {
      super(helperService);
      this.setModalName('identita-utente-acta');
    }

  ngOnInit(): void {
    this.segnalibroService.getTipiSegnalibro().subscribe(response => {
      this.actaSegnalibro = response.tipiSegnalibro?.find(tipo => tipo.codice === TipoSegnalibroEnum.COLLOCAZIONE_ACTA);
    });

    this.preferenzeUtenteSubscription = this.preferenzeUtenteService.subscribePreferenze.subscribe(preferenze => {
      this.preferenzeUtente = preferenze;
    });
    this.searchHelperModale(this.codicePagina, this.codiceTab);
  }

  selectionChangeHandler(selezione: IdentitaUtente[]) {
    this.identitaUtenteSelezionata = selezione?.length ? selezione[0] : null;
  }

  creaSegnalibro(row: IdentitaUtente) {
    if (!this.actaSegnalibro) {
      this.modalService.error('Salvataggio segnalibro', 'Tipologia di segnalibro non presente')
        .then(() => { })
        .catch(() => { });
    } else {
      this.starClicked = true;

      if (this.checkSegnalibro(row)) {
        this.segnalibro = null;
      } else {
        this.segnalibro = row;
      }
    }
  }

  checkSegnalibro(row: IdentitaUtente): boolean {
    if (this.segnalibro && this.segnalibro.id === row.id && this.segnalibro.identificativoStruttura === row.identificativoStruttura
      && this.segnalibro.codiceNodo === row.codiceNodo && this.segnalibro.identificativoNodo === row.identificativoNodo
      && this.segnalibro.codiceStruttura === row.codiceStruttura && this.segnalibro.codiceAOO === row.codiceAOO
      && this.segnalibro.descrizioneAOO === row.descrizioneAOO && this.segnalibro.descrizioneNodo === row.descrizioneNodo
      && this.segnalibro.descrizioneStruttura === row.descrizioneStruttura
      && this.segnalibro.identificativoAOO === row.identificativoAOO) {
      return true;
    } else {
      return false;
    }
  }

  salvaPreferenze() {
    if (!this.actaSegnalibro || !this.starClicked) {
      return;
    }

    if (this.preferenzeUtente && this.preferenzeUtente.valore) {
        const valorePreferenzeUtente = JSON.parse(this.preferenzeUtente.valore) as ValorePreferenzeUtente;

        if (valorePreferenzeUtente && valorePreferenzeUtente.segnalibri) {
          const collocazioneActa = valorePreferenzeUtente.segnalibri
            .find(segnalibro => segnalibro?.tipo === this.actaSegnalibro?.descrizione);

          if (this.segnalibro) {
            if (collocazioneActa) {
              collocazioneActa.segnalibro = this.segnalibro;
            } else {
              valorePreferenzeUtente.segnalibri.push(this.creSegnalibroElement(this.segnalibro));
            }
            this.preferenzeUtente.valore = JSON.stringify(valorePreferenzeUtente);
            this.preferenzeUtenteService.aggiornaSenzaModale(this.preferenzeUtente);
          } else {
            if (collocazioneActa) {
              const index: number = valorePreferenzeUtente.segnalibri.indexOf(collocazioneActa);
              if (index !== -1) {
                valorePreferenzeUtente.segnalibri.splice(index, 1);
                this.preferenzeUtente.valore = JSON.stringify(valorePreferenzeUtente);
                this.preferenzeUtenteService.aggiornaSenzaModale(this.preferenzeUtente);
              }
            }
          }
        } else {
          if (this.segnalibro) {
            valorePreferenzeUtente.segnalibri = [this.creSegnalibroElement(this.segnalibro)];
            this.preferenzeUtente.valore = JSON.stringify(valorePreferenzeUtente);
            this.preferenzeUtenteService.aggiornaSenzaModale(this.preferenzeUtente);
          }
        }
    } else {
      if (this.segnalibro) {
        const segnalibri = [this.creSegnalibroElement(this.segnalibro)];
        this.preferenzeUtente = {
          versione: '1.0',
          valore: JSON.stringify({ segnalibri })
        };
        this.preferenzeUtenteService.salva(this.preferenzeUtente);
      }
    }
  }

  creSegnalibroElement(row: IdentitaUtente): { tipo?: string; nome?: string; segnalibro?: any; } {
    return {
      tipo: this.actaSegnalibro?.descrizione,
      nome: 'Codice collocazione',
      segnalibro: row
    };
  }

  inviaIdentita() {
    this.salvaPreferenze();
    this.modal.close(this.identitaUtenteSelezionata);
  }

  closeModal() {
    this.salvaPreferenze();
    this.modal.dismiss('click_on_back_arrow');
  }

  ngOnDestroy(): void {
    if (this.preferenzeUtenteSubscription) {
      this.preferenzeUtenteSubscription.unsubscribe();
    }
  }
}
