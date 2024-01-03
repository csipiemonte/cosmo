/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit } from '@angular/core';
import { MessaggioControlliObbligatori, TipoMessaggio } from '../../models/messaggi-controlli-obbligatori/messaggio-controlli-obbligatori';

@Component({
  selector: 'app-messaggi-controlli-obbligatori',
  templateUrl: './messaggi-controlli-obbligatori.component.html',
  styleUrls: ['./messaggi-controlli-obbligatori.component.scss']
})

export class MessaggiControlliObbligatoriComponent implements OnInit {

  @Input() messaggiObbligatori!: MessaggioControlliObbligatori[];
  msgFacoltativi!: string[];
  msgObbligatori!: string[];

  ngOnInit(): void {

  }

}
