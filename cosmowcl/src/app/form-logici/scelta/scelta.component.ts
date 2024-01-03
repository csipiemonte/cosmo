/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  Component,
  Injector,
  OnInit,
} from '@angular/core';

import { Utils } from 'src/app/shared/utilities/utilities';

import { TranslateService } from '@ngx-translate/core';

import { FunzionalitaParentComponent } from '../funzionalita-parent.component';
import { SimpleFlowableOutput } from '../models/simple-flowable-output.model';

export const Variabili = {
  RIS_SCELTA: 'ris-scelta',
};

interface ConfigurazioneSceltaMessaggio {
  testo?: string;
  chiaveTesto?: string;
  classe?: string;
  icona?: string;
}

interface ConfigurazioneSceltaOpzione {
  testo?: string;
  chiaveTesto?: string;
  icona?: string;
  classe?: string;
  valore?: string;
  default?: boolean;
  riassunto?: string;
}

@Component({
  selector: 'app-scelta',
  templateUrl: './scelta.component.html',
  styleUrls: ['./scelta.component.scss']
})
export class SceltaComponent extends FunzionalitaParentComponent implements OnInit {

  constructor(
    public injector: Injector,
    private translateService: TranslateService) {
      super(injector);
  }

  loaded = false;

  messaggi: ConfigurazioneSceltaMessaggio[] = [];
  scelte: ConfigurazioneSceltaOpzione[] = [];
  variabileRisultato = Variabili.RIS_SCELTA;
  sceltaSelezionata?: ConfigurazioneSceltaOpzione;

  ngOnInit() {
    this.parseParametri();
    if (this.variabiliProcesso && this.variabiliProcesso.length > 0) {
      this.variabiliProcesso.forEach(variabile => {
        if (Utils.isNotBlank(variabile?.value) && this.variabileRisultato === variabile.name) {
          this.sceltaSelezionata = this.scelte.find(c => c.valore === (variabile.value as any as string));
          this.loaded = true;
        }
      });
    }
    this.update();
  }

  public isValid(): boolean {
    if (this.readOnly) {
      return true;
    }
    if (this.isSubtask) {
      return true;
    }
    return !!this.sceltaSelezionata;
  }

  public isWip(): boolean {
    if (this.readOnly) {
      return false;
    }
    return !this.readOnly && !this.isSubtask && !this.isValid();
  }

  parseParametri(): void {
    const variabileRisultato = this.parametri?.get('O_SCELTA') as string;
    if (!Utils.isNotBlank(this.variabileRisultato)) {
      throw new Error('Tab non configurato correttamente - il parametro O_SCELTA non è una stringa valida');
    }
    this.variabileRisultato = variabileRisultato.trim();

    const rawScelte = this.parametri?.get('SCELTE') ?? undefined;
    if (!rawScelte) {
      throw new Error('Tab non configurato correttamente');
    }
    let parsedScelte: ConfigurazioneSceltaOpzione[];
    try {
      parsedScelte = JSON.parse(rawScelte);
    } catch (err) {
      throw new Error('Tab non configurato correttamente - il parametro SCELTE non è un JSON valido');
    }
    this.scelte = parsedScelte;

    const rawMessaggi = this.parametri?.get('MESSAGGI') ?? undefined;
    if (rawMessaggi) {
      let parsedMessaggi: ConfigurazioneSceltaOpzione[];
      try {
        parsedMessaggi = JSON.parse(rawMessaggi);
      } catch (err) {
        throw new Error('Tab non configurato correttamente - il parametro MESSAGGI non è un JSON valido');
      }
      this.messaggi = parsedMessaggi;
    }
  }

  scegli(scelta: ConfigurazioneSceltaOpzione) {
    if (this.readOnly) {
      return;
    }

    if (scelta.testo !== this.sceltaSelezionata?.testo || scelta.valore !== this.sceltaSelezionata?.valore){
      this.dataChanged = true;
    }

    this.sceltaSelezionata = scelta;
    this.markDirty();
    this.update();
  }

  update() {
    const flowableOutput: SimpleFlowableOutput[] = [];
    if (this.sceltaSelezionata) {
      flowableOutput.push({ name: this.variabileRisultato, value: '' + this.sceltaSelezionata.valore });
    }

    this.sendData({
      payload: flowableOutput,
    });
  }

}
