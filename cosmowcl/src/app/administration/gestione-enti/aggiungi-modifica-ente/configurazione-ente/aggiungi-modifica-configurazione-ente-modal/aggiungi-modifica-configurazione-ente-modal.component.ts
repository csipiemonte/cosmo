/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { Constants } from 'src/app/shared/constants/constants';
import { ConfigurazioneEnte } from 'src/app/shared/models/api/cosmoauthorization/configurazioneEnte';
import { ConfigurazioneEnteService } from 'src/app/shared/services/configurazione-ente.service';
import { ModalService } from 'src/app/shared/services/modal.service';

@Component({
  selector: 'app-aggiungi-modifica-configurazione-ente-modal',
  templateUrl: './aggiungi-modifica-configurazione-ente-modal.component.html',
  styleUrls: ['./aggiungi-modifica-configurazione-ente-modal.component.scss']
})
export class AggiungiModificaConfigurazioneEnteModalComponent implements OnInit {

  idEnte?: number;
  configurazione?: ConfigurazioneEnte;
  configurazioneForm!: FormGroup;
  constructor(
    public modal: NgbActiveModal,
    private configurazioneEnteService: ConfigurazioneEnteService,
    private modalService: ModalService,
    private translateService: TranslateService) { }

  ngOnInit(): void {
    this.initForm();
  }


  initForm() {

    this.configurazioneForm = new FormGroup({
      chiave: new FormControl(this.configurazione && this.configurazione.chiave ? this.configurazione.chiave : null,
         [Validators.required, Validators.pattern(Constants.PATTERNS.PROFILO_UTENTE_DEFAULT)]),
      valore: new FormControl(this.configurazione && this.configurazione.valore ? this.configurazione.valore : null),
      descrizione: new FormControl(this.configurazione && this.configurazione.descrizione ? this.configurazione?.descrizione : null)
    });

    if (this.configurazione) {
      this.configurazioneForm.controls.chiave.disable();
    }
  }

  pulisciCampi() {
    this.configurazioneForm.patchValue({
      valore: null,
      descrizione: null
    });

    if (!this.configurazione){
      this.configurazioneForm.patchValue({
        chiave: null,
      });
    }
  }

  modificaConfigurazione() {
    if (!this.configurazione || !this.configurazione.chiave){
      return;
    }
    const request: ConfigurazioneEnte = {
      chiave: this.configurazione.chiave,
      valore: this.configurazioneForm.value.valore,
      descrizione: this.configurazioneForm.value.descrizione,
    };

    this.configurazioneEnteService.aggiornaConfigurazioneEnte(this.configurazione.chiave, request, this.idEnte).subscribe(response => {
      this.modal.close('OK');
    }, error => {
      this.modalService.error(this.translateService.instant('configurazione_ente.aggiornamento_configurazione'),
        this.translateService.instant('errori.aggiornamento_nuova_configurazione_ente'), error.error.errore)
        .then(() => { this.modal.close('KO'); })
        .catch(() => { });
    });

  }

  aggiungiConfigurazione() {
    const request: ConfigurazioneEnte = {
      chiave: this.configurazioneForm.value.chiave,
      valore: this.configurazioneForm.value.valore,
      descrizione: this.configurazioneForm.value.descrizione,
    };

    this.configurazioneEnteService.creaConfigurazioneEnte(request, this.idEnte).subscribe(response => {
      this.modal.close('OK');
    }, error => {
      this.modalService.error(this.translateService.instant('configurazione_ente.creazione_configurazione'),
        this.translateService.instant('errori.creazione_nuova_configurazione_ente'), error.error.errore)
        .then(() => { this.modal.close('KO'); })
        .catch(() => { });
    });
  }

  displayInvalid(name: string): boolean {
    if (!this.configurazioneForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return (control.dirty || control.touched) && control.invalid;
  }

  getError(name: string, type: string): any {
    if (!this.configurazioneForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return control.errors && control.errors[type] ?
      (control.errors[type] ?? true) : null;
  }

  hasError(name: string, type: string): any {
    return this.getError(name, type) !== null;
  }

  getControl(name: string): AbstractControl | undefined {
    if (!this.configurazioneForm) {
      return undefined;
    }
    return this.resolveControl(name) ?? undefined;
  }

  private resolveControl(name: string): AbstractControl | undefined {
    let actual: any = this.configurazioneForm;
    for (const token of name.split('.')) {
      const newControl = actual.controls[token];
      if (!newControl) {
        return undefined;
      }
      actual = newControl;
    }
    return actual as AbstractControl;
  }

}
