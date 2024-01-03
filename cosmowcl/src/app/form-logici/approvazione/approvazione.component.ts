/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ArrayType } from '@angular/compiler';
import { Component, Injector, Input, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { max } from 'rxjs/operators';
import { Utils } from 'src/app/shared/utilities/utilities';
import { FunzionalitaParentComponent } from '../funzionalita-parent.component';
import { SimpleFlowableOutput } from '../models/simple-flowable-output.model';
import { TabBadge } from '../models/tab-status.models';

export const Variabili = {
  RIS_APPROVAZIONE: 'ris-approvazione',
  NOTE: 'note'
};

@Component({
  selector: 'app-approvazione',
  templateUrl: './approvazione.component.html',
  styleUrls: ['./approvazione.component.scss']
})
export class ApprovazioneComponent extends FunzionalitaParentComponent implements OnInit {

  constructor(
    public injector: Injector,
    private translateService: TranslateService) {
      super(injector);
  }

  note = '';
  approvazione?: boolean;
  loaded = false;

  siButton: string | null = null;
  noButton: string | null = null;

  variabileRisultato = Variabili.RIS_APPROVAZIONE;
  variabileNote = Variabili.NOTE;

  ngOnInit() {
    this.parseParametri();
    if (this.variabiliProcesso && this.variabiliProcesso.length > 0) {
      this.variabiliProcesso.forEach(variabile => {
        if (Utils.isNotBlank(variabile?.value) && this.variabileRisultato === variabile.name) {
          this.approvazione = (variabile.value as any) === 'true';
          this.loaded = true;
        }
        if (Utils.isNotBlank(variabile?.value) && this.variabileNote === variabile.name) {
          this.note = variabile.value as any as string;
          this.loaded = true;
        }
      });
    }
    this.update();
  }

  public isValid(): boolean {
    if (this.isSubtask) {
      return true;
    }
    return this.approvazione === true || (this.approvazione === false && this.note.trim().length > 0);
  }

  public isWip(): boolean {
    return !this.readOnly && !this.isSubtask && !this.isValid();
  }

  public isChanged(): boolean {
      return this.dataChanged;
  }

  parseParametri(): void {
    const raw = this.parametri?.get('O_APPROVAZIONE') ?? undefined;
    if (!raw) {
      throw new Error('Tab non configurato correttamente');
    }
    let parsed;
    try {
      parsed = JSON.parse(raw);
    } catch (err) {
      throw new Error('Tab non configurato correttamente - il parametro O_APPROVAZIONE non è un JSON valido');
    }

    const variabileRisultato = parsed.variabileRisultatoApprovazione;
    if (!variabileRisultato?.trim().length) {
      throw new Error('Tab non configurato correttamente - campo variabileRisultato del parametro O_APPROVAZIONE non presente');
    }

    const variabileNote = parsed.variabileNoteApprovazione;
    if (!variabileNote?.trim().length) {
      throw new Error('Tab non configurato correttamente - campo variabileNote del parametro O_APPROVAZIONE non presente');
    }

    this.variabileRisultato = variabileRisultato.trim();
    this.variabileNote = variabileNote.trim();

    if (this.parametri?.get('SI_BUTTON')){
      this.siButton =  this.parametri.get('SI_BUTTON') as string;
    }

    if (this.parametri?.get('NO_BUTTON')){
      this.noButton = this.parametri.get('NO_BUTTON') as string;
    }
  }

  rifiuta(note: string) {
    this.datiModificati(false, note);
    this.approvazione = false;
    this.noteChanged(note);
  }

  accetta(note: string) {
    this.datiModificati(true, note);
    this.approvazione = true;
    this.noteChanged(note);
  }

  noteChanged(note: string) {
    this.note = note;
    this.markDirty();
    this.update();
  }

  private datiModificati(approvazione: boolean, note: string) {
    if (approvazione !== this.approvazione || note !== this.note) {
      this.dataChanged = true;
    } else {
      this.dataChanged = false;
    }
  }

  update() {
    const flowableOutput: SimpleFlowableOutput[] = [];
    if (this.approvazione === true || this.approvazione === false) {
      flowableOutput.push({ name: this.variabileRisultato, value: '' + this.approvazione });
    }
    flowableOutput.push({ name: this.variabileNote, value: this.note });

    this.sendData({
      payload: flowableOutput,
    });
  }

  public getBadges(): TabBadge[] | null {
    const numApprovatori = this.getNumeroApprovatori();
    if (numApprovatori > 0 ) {
      return [
        {
          class: 'info',
          text: numApprovatori.toString(),
          tooltip: numApprovatori.toString() + ' ' + this.translateService.instant('common.approvatori') }
      ];
    }
    return null;
  }

  private getNumeroApprovatori() {
    const approvalsArrays = this.variabiliProcesso.filter(vp => vp.type === 'serializable').map(x => x.value as Array<any>);
    let maximum = 0;
    approvalsArrays.forEach(x => {
      if (x.length > maximum) {
        maximum = x.length;
      }
    });
    return maximum;
  }

}
