/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { AfterViewInit, Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-due-opzioni',
  templateUrl: './due-opzioni.component.html',
  styleUrls: ['./due-opzioni.component.scss']
})
export class DueOpzioniComponent implements OnInit, AfterViewInit {

  titolo: string | null = null;
  messaggi: MessaggioModale[] = [];
  pulsanti: PulsanteModale[] = [];

  @ViewChildren('optionButtons') defaultfocus: QueryList<ElementRef> | null = null;
  @ViewChild('crossButton') crossButton: ElementRef | null = null;


  constructor(public activeModal: NgbActiveModal) { }

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    setTimeout(()=>{
    if (this.defaultfocus?.length) {
      const focus = this.defaultfocus.find(e =>
        (e.nativeElement.classList?.contains('defaultfocus') ?? false)
      );
      if (focus) {
        focus.nativeElement.focus();
      } else if (this.crossButton) {
        this.crossButton.nativeElement.focus();
      }
    }},0);
  }

  pulsanteCliccato(p: PulsanteModale) {
    if (p.dismiss) {
      this.activeModal.dismiss(p.valore);
    } else {
      this.activeModal.close(p.valore);
    }
  }
}

export interface MessaggioModale {
  testo: string;
  classe?: string;
}

export interface PulsanteModale {
  testo: string;
  dismiss?: boolean;
  valore?: any;
  classe?: string;
  defaultFocus?: boolean;
  icona?: string;
}
