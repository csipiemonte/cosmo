/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, Output, EventEmitter, ElementRef, ViewChild, AfterViewInit, Injectable } from '@angular/core';
import { NgbActiveModal, NgbDate, NgbDatepicker, NgbDatepickerI18n, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Evento } from '../models/evento.model';
import { CalendarioService, TipoAssegnazioneEvento } from '../services/calendario.service';
import { FormGroup, Validators, FormControl } from '@angular/forms';
import { ModalService } from 'src/app/shared/services/modal.service';
import { TranslateService } from '@ngx-translate/core';
import { SecurityService } from 'src/app/shared/services/security.service';
import { Evento as CreaEvento } from 'src/app/shared/models/api/cosmobusiness/evento';
import { DatePipe } from '@angular/common';
import { ModaleParentComponent } from 'src/app/modali/modale-parent-component';
import { HelperService } from 'src/app/shared/services/helper.service';

const I18N_VALUES = {
  it: {
    weekDays: ['Lunedì', 'Martedì', 'Mercoledì', 'Giovedì', 'Venerdì', 'Sabato', 'Domenica'],
    shortWeekdays: ['Lun', 'Mar', 'Mer', 'Gio', 'Ven', 'Sab', 'Dom'],
    months: [
      'Gennaio', 'Febbraio', 'Marzo', 'Aprile', 'Maggio', 'Giugno', 'Luglio', 'Agosto', 'Settembre', 'Ottobre', 'Novembre', 'Dicembre'
    ],
    shortMonths: ['Gen', 'Feb', 'Mar', 'Apr', 'Mag', 'Giu', 'Lug', 'Ago', 'Set', 'Ott', 'Nov', 'Dic'],
  }
};

@Injectable()
export class I18n {
  language = 'it';
}

@Injectable()
export class CustomDatepickerI18n extends NgbDatepickerI18n {

  constructor(
    private i18n: I18n) {
    super();
  }

  isValidElement(value: string): value is keyof typeof I18N_VALUES {
    return value in I18N_VALUES;
  }

  getWeekdayShortName(weekday: number): string {
    if (this.isValidElement(this.i18n.language)) {
      return I18N_VALUES[this.i18n.language].shortWeekdays[weekday - 1];
    }
    else {
      throw Error('invalid element');
    }
  }

  getMonthShortName(month: number): string {
    if (this.isValidElement(this.i18n.language)) {
      return I18N_VALUES[this.i18n.language].shortMonths[month - 1];
    }
    else {
      throw Error('invalid element');
    }
  }

  getMonthFullName(month: number): string {
    if (this.isValidElement(this.i18n.language)) {
      return I18N_VALUES[this.i18n.language].months[month - 1];
    }
    else {
      throw Error('invalid element');
    }
  }

  getDayAriaLabel(date: NgbDateStruct): string {
    let month = '';
    if (this.isValidElement(this.i18n.language)) {
      month = I18N_VALUES[this.i18n.language].months[date.month - 1];
    } else {
      throw Error('invalid element');
    }
    const d = new Date(date.month + ' ' + date.day + ' ' + date.year);
    return `${I18N_VALUES[this.i18n.language].weekDays[d.getDay() - 1]}, ${date.day} ${month} ${date.year}`;
  }
}

@Component({
  selector: 'app-nuovo-evento-modal',
  templateUrl: './nuovo-evento-modal.component.html',
  styleUrls: ['./nuovo-evento-modal.component.scss'],
  providers: [I18n, { provide: NgbDatepickerI18n, useClass: CustomDatepickerI18n }]
})
export class NuovoEventoModalComponent extends ModaleParentComponent implements OnInit {

  date: NgbDate | null = null;
  nuovoEventoForm!: FormGroup;
  evento: Evento | null = null;
  cf!: string;
  now: Date = new Date();
  codicePagina!: string;
  codiceTab!: string;
  codiceModale!: string;

  @ViewChild('dp') datePicker?: NgbDatepicker;

  // output di un evento per il componente calendario (aggiunta di un evento alla lista)
  @Output() nuovoEvento: EventEmitter<Evento> = new EventEmitter();

  MIN_LEN_TITLE = 1;
  MIN_LEN_DESC = 1;

  constructor(
    public modal: NgbActiveModal,
    private security: SecurityService,
    private calendarioService: CalendarioService,
    private modalService: ModalService,
    private translateService: TranslateService,
    private datePipe: DatePipe,
    public helperService: HelperService
  ) {
    super(helperService);
  }


  ngOnInit(): void {
    this.security.getCurrentUser().subscribe(response => this.cf = response.codiceFiscale);
    if (this.evento && this.evento.dataScadenza){
      this.date = new NgbDate(this.evento.dataScadenza.getFullYear(),
      this.evento.dataScadenza.getMonth() + 1, this.evento.dataScadenza.getDate());
    }
    this.nuovoEventoForm = new FormGroup({
      nome: new FormControl( this.evento && this.evento.nome ? this.evento.nome : null,
        [Validators.required, Validators.minLength(this.MIN_LEN_TITLE)]),
      descrizione: new FormControl( this.evento && this.evento.descrizione ? this.evento.descrizione : null,
        [Validators.required, Validators.minLength(this.MIN_LEN_DESC)]),
      dataScadenza: new FormControl( this.date ? this.date : null, [Validators.required])
    });
    this.setModalName(this.codiceModale);
    this.searchHelperModale(this.codicePagina, this.codiceTab);
  }




  setDueDate(data: NgbDate) {
    this.nuovoEventoForm.controls.dataScadenza.setValue(data);
  }

  submit() {

    if (this.evento && this.evento.id){
      this.aggiornaEvento(this.evento);
    } else{
      const evento: Evento = {
        assegnatario: this.cf,
        nome: this.nuovoEventoForm.controls.nome.value,
        descrizione: this.nuovoEventoForm.controls.descrizione.value,
        dataScadenza: new Date(
          this.nuovoEventoForm.controls.dataScadenza.value.year,
          this.nuovoEventoForm.controls.dataScadenza.value.month - 1,
          this.nuovoEventoForm.controls.dataScadenza.value.day),
        dataCreazione: new Date(),
        tipoAssegnazione: TipoAssegnazioneEvento.UTENTE_CORRENTE
      };

      this.creaEvento(evento);
    }

  }

  creaEvento(evento: Evento){
    const postEvento: CreaEvento = {
      nome: evento.nome,
      descrizione: evento.descrizione,
      dtScadenza: evento.dataScadenza.toISOString(),
      dtCreazione: evento.dataCreazione.toISOString()
    };

    this.calendarioService.inserisciNuovoEvento(postEvento).subscribe(response => {
      /*
      if (response && response.id){
        evento.id = response.id;
        this.nuovoEvento.emit(evento);
      }
      this.modal.dismiss();
      */
      if (response && response.id){
        this.modal.close();
        this.modalService.simpleInfo(this.translateService.instant('eventi.evento_creato'))
        .catch(() => {});
      }
    }, error => {
      this.modalService.error(this.translateService.instant('eventi.creazione_evento'),
        this.translateService.instant('errori.creazione_nuovo_evento'), error.error.errore)
        .then(() => {
          this.modal.dismiss();
        })
        .catch(() => { });
    });
  }

  aggiornaEvento(evento: Evento){

    if (this.evento){
      this.evento.nome = this.nuovoEventoForm.controls.nome.value;
      this.evento.descrizione =  this.nuovoEventoForm.controls.descrizione.value;
      this.evento.dataScadenza = new Date(
        this.nuovoEventoForm.controls.dataScadenza.value.year,
        this.nuovoEventoForm.controls.dataScadenza.value.month - 1,
        this.nuovoEventoForm.controls.dataScadenza.value.day);
    }

    const putEvento: CreaEvento = {
      id: evento.id,
      nome: evento.nome,
      descrizione: evento.descrizione,
      dtScadenza: evento.dataScadenza.toISOString(),
    };

    this.calendarioService.modificaEvento(evento.id ?? '', putEvento).subscribe(response => {

      if (response){
        this.modal.close();
        this.modalService.simpleInfo(this.translateService.instant('eventi.evento_aggiornato'))
        .catch(() => {});
      }

    }, error => {
      this.modalService.error(this.translateService.instant('eventi.aggiornamento_evento'),
        this.translateService.instant('errori.aggiornamento_evento'), error.error.errore)
        .then(() => {
          this.modal.dismiss();
        })
        .catch(() => { });
    });
  }

  tornaAdOggi() {
    this.date = new NgbDate(this.now.getFullYear(), this.now.getMonth() + 1, this.now.getDate());
    this.nuovoEventoForm.controls.dataScadenza.patchValue(this.date);
    this.datePicker?.onNavigateDateSelect(this.date);
  }

  get today() {
    return this.datePipe.transform(this.now, 'dd MMMM yyyy', undefined, 'IT-it')?.toUpperCase();
  }
}

