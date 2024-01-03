/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { DatePipe } from '@angular/common';
import { EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { Component, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-gestisci-funzionalita',
  templateUrl: './gestisci-funzionalita.component.html',
  styleUrls: ['./gestisci-funzionalita.component.scss']
})
export class GestisciFunzionalitaComponent implements OnInit {

  private urlRegex = new RegExp('^(https?://)((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.?)+[a-z]{2,}|' +
    '((\\d{1,3}\\.){3}\\d{1,3}))(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*(\\?[;&amp;a-z\\d%_.~+=-]*)?(\\#[-a-z\\d_]*)?$', 'i');


  form!: FormGroup;

  @Input() descrizioneApp!: string;
  @Input() iconaApp!: string;
  @Input() url: string | null = null;
  @Input() descrizioneFunzionalita: string | null = null;
  @Input() inizioValidita: string | null = null;
  @Input() fineValidita: string | null = null;
  @Input() showInfoApp = false;

  @Output() salvaEmitter: EventEmitter<FormGroup> = new EventEmitter<FormGroup>();
  @Output() backEmitter: EventEmitter<void> = new EventEmitter<void>();

  constructor(
    private datePipe: DatePipe,
  ) { }


  ngOnInit(): void {
    this.initForm();
  }

  initForm() {
    this.form = new FormGroup({
      descrizione: new FormControl(this.descrizioneFunzionalita ?? null, [Validators.required, Validators.maxLength(255)]),
      url: new FormControl(this.url ?? null, [Validators.required, Validators.pattern(this.urlRegex)]),
      inizioValidita: new FormControl(this.inizioValidita ? this.datePipe.transform(this.inizioValidita, 'yyyy-MM-dd') : null,
        Validators.required),
      fineValidita: new FormControl(this.fineValidita ? this.datePipe.transform(this.fineValidita, 'yyyy-MM-dd') : null)
    });
  }

  getLogo() {
    if (this.iconaApp) {
      return 'data:image/png;base64,' + this.iconaApp;
    }
  }

  pulisciCampi() {
    this.initForm();
  }

  salva() {
    this.salvaEmitter.emit(this.form);
  }

  tornaIndietro() {
    this.backEmitter.emit();
  }

}
