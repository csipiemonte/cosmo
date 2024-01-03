/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

export interface EsitoApprovazioneRifiuto {
  approvazione: boolean;
  note?: string;
}

@Component({
  selector: 'app-approvazione-rifiuto',
  templateUrl: './approvazione-rifiuto.component.html',
  styleUrls: ['./approvazione-rifiuto.component.scss']
})
export class ApprovazioneRifiutoComponent implements OnInit {

  @Input() note = '';
  @Input() approvazione?: boolean;
  @Input() siButton: string | null = null;
  @Input() noButton: string | null = null;
  @Input() readOnly = false;
  @Input() mostraConferma = false;

  @Output() rifiutaEmitter: EventEmitter<string> = new EventEmitter<string>();
  @Output() accettaEmitter: EventEmitter<string> = new EventEmitter<string>();
  @Output() noteChangedEmitter: EventEmitter<string> = new EventEmitter<string>();
  @Output() confermaEmitter: EventEmitter<EsitoApprovazioneRifiuto> = new EventEmitter<EsitoApprovazioneRifiuto>();

  constructor() {
    // NOP
  }

  ngOnInit(): void {
    // NOP
  }

  abilitaConferma(): boolean {
    return this.mostraConferma && (this.approvazione || (!this.approvazione && this.note?.trim().length > 0));
  }

  conferma() {
    this.confermaEmitter.emit({
      approvazione: this.approvazione ?? false,
      note: this.note
    });
  }

  rifiuta() {
    this.approvazione = false;
    this.rifiutaEmitter.emit(this.note);
  }

  accetta() {
    this.approvazione = true;
    this.accettaEmitter.emit(this.note);
  }

  noteChanged() {
    this.noteChangedEmitter.emit(this.note);
  }

}
