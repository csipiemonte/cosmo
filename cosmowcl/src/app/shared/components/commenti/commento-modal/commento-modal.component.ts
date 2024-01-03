/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { SecurityService } from 'src/app/shared/services/security.service';
import { Commento } from '../models/commento.model';
import { CommentiService } from '../services/commenti.service';

@Component({
  selector: 'app-commento-modal',
  templateUrl: './commento-modal.component.html',
  styleUrls: ['./commento-modal.component.scss']
})
export class CommentoModalComponent implements OnInit {

  nuovoCommentoForm!: FormGroup;

  @Input() idProcesso !: number;


  commento: Commento = { id: 1, cfAutore: '', messaggio: '', timestamp: new Date(), cognomeAutore: '', nomeAutore: '' };
  MIN_LEN = 5;

  constructor(
    public modal: NgbActiveModal,
    private commentoService: CommentiService,
    private secService: SecurityService
  ) { }

  async ngOnInit() {
    const user = await this.secService.getCurrentUser().toPromise();
    this.commento.cfAutore = user.codiceFiscale;
    this.commento.nomeAutore = user.nome;
    this.commento.cognomeAutore = user.cognome;
    this.nuovoCommentoForm = new FormGroup({
      commento: new FormControl(this.commento.messaggio, [Validators.required, Validators.minLength(this.MIN_LEN)]),
    });
  }

  async submit() {
    this.commento.messaggio = this.nuovoCommentoForm.controls.commento.value;
    const c = this.commentoService.postCommento(this.commento, this.idProcesso);
    this.modal.close(c);
  }



}
