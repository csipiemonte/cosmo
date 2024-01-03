/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, Injectable, ViewChild, ElementRef, LOCALE_ID } from '@angular/core';
import { NgbDateStruct, NgbDatepickerI18n, NgbDate, NgbModal, NgbModalOptions, NgbModalRef, NgbDatepicker
  } from '@ng-bootstrap/ng-bootstrap';
import { CalendarioService, TipoAssegnazioneEvento } from './services/calendario.service';
import { Evento } from './models/evento.model';
import { SecurityService } from 'src/app/shared/services/security.service';
import { EventoModalComponent } from './evento-modal/evento-modal.component';
import { NuovoEventoModalComponent } from './nuovo-evento-modal/nuovo-evento-modal.component';
import { forkJoin } from 'rxjs';
import { delay, finalize } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { PaginaTask } from 'src/app/shared/models/api/cosmobusiness/paginaTask';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
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
  selector: 'app-calendario',
  templateUrl: './calendario.component.html',
  styleUrls: ['./calendario.component.scss'],
  providers: [I18n, { provide: NgbDatepickerI18n, useClass: CustomDatepickerI18n },
    { provide: LOCALE_ID, useValue: 'it-IT' }
  ]
})
export class CalendarioComponent implements OnInit {

  loading = 0;
  loadingError: any | null = null;
  userInfo: UserInfoWrapper | undefined;

  eventiCalendario: Evento[] = [];
  prossimiEventi: Evento[] = [];

  date: NgbDate | null = null;
  provDate: NgbDate | null = null;
  showedDate!: Date;
  hoveredDate: NgbDate | null = null;

  I18N_VALUES: any;

  size = 5; // va recuperato da configurazione su db
  start = 0;


  semaphore = false;

  now: Date = new Date();

  @ViewChild('dp') datePicker?: NgbDatepicker;
  @ViewChild('eventiList') eventiList?: ElementRef;

  constructor(
    private calendarioService: CalendarioService,
    public i18n: I18n,
    private securityService: SecurityService,
    private ngbModal: NgbModal,
    private datePipe: DatePipe,
    private helperService: HelperService,
    private route: ActivatedRoute) {
    this.i18n = i18n;
    this.I18N_VALUES = I18N_VALUES;
  }

  private modalOptions: NgbModalOptions = {
    backdrop: 'static',
    centered: true,
    size: 'lg'
  };

  ngOnInit(): void {
    // carico eventi assegnati e prossimi eventi all'init
    this.eventiCalendario = [];
    this.securityService.getCurrentUser().subscribe(userInfo => {
      this.userInfo = userInfo;
      if (userInfo) {
        this.refresh();
      }
    });
  }
  focus(){

    this.provDate = this.date;
    this.date = null;
    setTimeout(() => {
      this.provDate = null;
    }, 500);

  }


  refresh() {
    if (!this.userInfo) {
      return;
    }

    this.loading ++;
    this.loadingError = null;

    forkJoin({
      eventiAssegnati: this.calendarioService.recuperaEventiAssegnati(
        this.userInfo.codiceFiscale, this.now.getMonth(), this.now.getFullYear(), 100, 0),
      prossimiEventiAssegnati: this.calendarioService.recuperaProssimiEventiAssegnati(
        this.userInfo.codiceFiscale, this.now.getMonth(), this.now.getFullYear(), this.size, this.start
      )
    })
    .pipe(
      delay(environment.httpMockDelay),
      finalize(() => {
        this.loading --;
      })
    )
    .subscribe(result => {
      this.eventiCalendario = [];
      this.parseEventiAssegnati(result.eventiAssegnati);
      this.parseProssimiEventiAssegnati(result.prossimiEventiAssegnati);
      if (result.eventiAssegnati.dimensionePagina && result.eventiAssegnati.totale &&
        result.eventiAssegnati.dimensionePagina < result.eventiAssegnati.totale && this.userInfo){
        this.loading++;
        this.calendarioService.recuperaEventiAssegnati(
          this.userInfo.codiceFiscale, this.now.getMonth(), this.now.getFullYear(),
            result.eventiAssegnati.totale - result.eventiAssegnati.dimensionePagina, 100).pipe(finalize(() => {this.loading--; }))
            .subscribe(
            elem => {
              this.parseEventiAssegnati(elem);
            },
            failure => {this.loadingError = failure; }
          );
      }

    }, failure => {
      this.loadingError = failure;
    });
  }

  parseEventiAssegnati(taskResponse: PaginaTask) {

    if (!this.userInfo) {
      return;
    }
    // this.eventiCalendario = [];
    if (taskResponse && taskResponse.elementi) {
      taskResponse.elementi.forEach((task) => {
        if (!this.userInfo) {
          return;
        }
        this.eventiCalendario.push({
          id: task.id ?? undefined,
          assegnatario: task.assignee ?? '',
          dataCreazione: task.createTime ? new Date(task.createTime) : new Date(),
          dataScadenza: task.dueDate ? new Date(task.dueDate) : new Date(),
          descrizione: task.description ?? '',
          nome: task.name ?? '',
          tipoAssegnazione: this.userInfo.codiceFiscale === task.assignee ?
            TipoAssegnazioneEvento.UTENTE_CORRENTE : TipoAssegnazioneEvento.GRUPPO
        });
      });
    }
  }

  parseProssimiEventiAssegnati(taskResponse: PaginaTask) {
    if (taskResponse && taskResponse.elementi) {
      taskResponse.elementi.forEach((task) => {
        if (!this.userInfo) {
          return;
        }

        const d = new Date();
        d.setHours(0, 0, 0, 0);
        if (task.dueDate && d.getTime() >  new Date(task.dueDate).getTime()) {
          return;
        }

        this.prossimiEventi.push({
          id: task.id ?? undefined,
          assegnatario: task.assignee ?? '',
          dataCreazione: task.createTime ? new Date(task.createTime) : new Date(),
          dataScadenza: task.dueDate ? new Date(task.dueDate) : new Date(),
          descrizione: task.description ?? '',
          nome: task.name ?? '',
          tipoAssegnazione: this.userInfo.codiceFiscale === task.assignee ?
            TipoAssegnazioneEvento.UTENTE_CORRENTE : TipoAssegnazioneEvento.GRUPPO
          });
        });
      }
  }

  onDateSelection(date: NgbDate) {
    const data = this.helperService.searchHelperRef(this.route);
    const eventsToDate = this.getEventsToDate(date);
    // lancia errore in console, bug di ng-bootstrap -> https://github.com/ng-bootstrap/ng-bootstrap/issues/1252
    if (eventsToDate && eventsToDate.length > 0) {
      const modalRef: NgbModalRef = this.ngbModal.open(EventoModalComponent, this.modalOptions);
      modalRef.componentInstance.eventi = eventsToDate;
      modalRef.componentInstance.date = date;
      modalRef.componentInstance.i18n = this.i18n;
      modalRef.componentInstance.I18N_VALUES = this.I18N_VALUES;
      modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
      modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
      modalRef.componentInstance.codiceModale = 'calendario-evento-dettaglio';

      modalRef.result.then(res => {
        this.ricalcolo();
      });
    }
  }

  // recupera tutti gli eventi a una certa data
  private getEventsToDate(ngbDate: NgbDate): Evento[] {
    const date = new Date(ngbDate.month + ' ' + ngbDate.day + ' ' + ngbDate.year);

    return this.eventiCalendario.filter(evento => {
      return evento.dataScadenza.getDate() === date.getDate() &&
        evento.dataScadenza.getMonth() === date.getMonth() && evento.dataScadenza.getFullYear() === date.getFullYear();
    });
  }

  // true se con assignee === involvedUser
  hasUserEvent(ngbDate: NgbDate): boolean {

    const eventsToDate: Evento[] = this.getEventsToDate(ngbDate);

    return eventsToDate.length > 0 && eventsToDate.every(eventToDate => {
      return TipoAssegnazioneEvento.UTENTE_CORRENTE === eventToDate.tipoAssegnazione;
    });
  }

  // rtrue se assingee !== involvedUser
  hasGroupEvent(ngbDate: NgbDate): boolean {
    const eventsToDate: Evento[] = this.getEventsToDate(ngbDate);

    return eventsToDate.length > 0 && eventsToDate.every(eventToDate => {
      return TipoAssegnazioneEvento.GRUPPO === eventToDate.tipoAssegnazione;
    });
  }

  // true se in un certo giorno esistono sia eventi con assignee === involvedUser che assingee !== involvedUser
  hasMultiEvent(ngbDate: NgbDate): boolean {
    const eventsToDate: Evento[] = this.getEventsToDate(ngbDate);

    let hasUserEvent = false;
    let hasGroupEvent = false;

    eventsToDate.forEach((evento) => {
      if (TipoAssegnazioneEvento.GRUPPO === evento.tipoAssegnazione) {
        hasGroupEvent = true;
      }
      if (TipoAssegnazioneEvento.UTENTE_CORRENTE === evento.tipoAssegnazione) {
        hasUserEvent = true;
      }
      if (hasGroupEvent && hasUserEvent) {
        return;
      }
    });
    return hasUserEvent && hasGroupEvent;
  }

  // chiamato quando viene cambiato mese o anno del calendario
  navigate(nextDate: any) {
    if (!this.userInfo) {
      return;
    }
    // this.loading ++;
    this.loadingError = null;
    this.provDate = null;
    this.showedDate = new Date(nextDate.month + ' 01 ' + nextDate.year);
    this.calendarioService.recuperaEventiAssegnati(
      this.userInfo.codiceFiscale, this.showedDate.getMonth(), this.showedDate.getFullYear(), 100, 0
    )
    .pipe(
      delay(environment.httpMockDelay),
      finalize(() => {
        // this.loading --;
      })
    )
    .subscribe((taskResponse) => {
      this.eventiCalendario = [];
      this.parseEventiAssegnati(taskResponse);
      if (taskResponse.dimensionePagina && taskResponse.totale && taskResponse.dimensionePagina < taskResponse.totale && this.userInfo){
        this.calendarioService.recuperaEventiAssegnati(
          this.userInfo.codiceFiscale, this.showedDate.getMonth(), this.showedDate.getFullYear(),
           taskResponse.totale - taskResponse.dimensionePagina, 100).subscribe(
            elem => {
              this.parseEventiAssegnati(elem);
            }
          );
      }
    }, failure => {
      this.loadingError = failure;
    });
  }

  newEventModal() {
    const data = this.helperService.searchHelperRef(this.route);
    const modalRef: NgbModalRef = this.ngbModal.open(NuovoEventoModalComponent, this.modalOptions);
    modalRef.componentInstance.date = this.provDate;
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = 'calendario-nuovo-evento';
    modalRef.result.finally(() => {
      if (this.provDate){
        this.datePicker?.focusDate(this.provDate);
        this.datePicker?.focus();
      }
    });
    modalRef.result.then(res => {
      this.ricalcolo();
    });

  }

  // apertura modale al click sulla data o su uno dei prossimi eventi
  openModal(evento: Evento): void {
    const data = this.helperService.searchHelperRef(this.route);
    const modalRef: NgbModalRef = this.ngbModal.open(EventoModalComponent, this.modalOptions);
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = 'calendario-evento';
    if (evento) {
      modalRef.componentInstance.eventi.push(evento);
      modalRef.componentInstance.date = new NgbDate(
        evento.dataScadenza.getFullYear(), evento.dataScadenza.getMonth() + 1, evento.dataScadenza.getDate());
      modalRef.componentInstance.i18n = this.i18n;
      modalRef.componentInstance.I18N_VALUES = this.I18N_VALUES;

    }

    modalRef.result.then(res => {
      this.ricalcolo();
    });
  }

  scrollPosition() {
    return (this.eventiList !== undefined && typeof (this.eventiList) !== 'undefined') ?
      this.eventiList.nativeElement.scrollHeight - this.eventiList.nativeElement.scrollTop - this.eventiList.nativeElement.clientHeight
      : 0;
  }

  // evento la cui logica viene invocata quando si effettua lo scrolling sul div dei prossimi eventi
  onScroll(event: any) {
    // uguaglianza che restituisce true se la barra di scroll è arrivata al fondo
    // console.log("SCROLL: " + this.scrollPosition())

    if (this.semaphore) {
      return;
    }

    if (this.eventiList && this.scrollPosition() > 1) {
      return;
    }
    else {
        // recupero della prossima pagina di eventi:
        // TODO: capire come evitare di effettuare ogni volta la chiamata se tutti gli eventi sono stati caricati
        if (this.userInfo) {
          this.start += this.size;
          // console.log("START: " + this.start)
          this.calendarioService.recuperaProssimiEventiAssegnati(
            this.userInfo.codiceFiscale, this.now.getMonth(), this.now.getFullYear(), this.size, this.start
          ).subscribe(taskResponse => {
              this.semaphore = true;

              if (taskResponse && taskResponse.elementi) {
                taskResponse.elementi.forEach((task) => {
                  if (!this.userInfo) {
                    return;
                  }
                  this.prossimiEventi.push({
                    id: task.id ?? undefined,
                    assegnatario: task.assignee ?? '',
                    dataCreazione: task.createTime ? new Date(task.createTime) : new Date(),
                    dataScadenza: task.dueDate ? new Date(task.dueDate) : new Date(),
                    descrizione: task.description ?? '',
                    nome: task.name ?? '',
                    tipoAssegnazione: this.userInfo.codiceFiscale === task.assignee ?
                      TipoAssegnazioneEvento.UTENTE_CORRENTE : TipoAssegnazioneEvento.GRUPPO
                  });
                });
              }
              this.semaphore = false;
            });
        }
    }
  }

  ricalcolo() {
    if (!this.userInfo) {
      return;
    }
    this.start = 0;


    forkJoin({
      eventiAssegnati: this.calendarioService.recuperaEventiAssegnati(
        this.userInfo.codiceFiscale, this.showedDate ? this.showedDate.getMonth() : this.now.getMonth(),
        this.showedDate ? this.showedDate.getFullYear() : this.now.getFullYear(), 100, 0),
      prossimiEventiAssegnati: this.calendarioService.recuperaProssimiEventiAssegnati(
        this.userInfo.codiceFiscale, this.now.getMonth(), this.now.getFullYear(), this.size, this.start
      )
    })
    .subscribe(result => {
      this.prossimiEventi = [];
      this.eventiCalendario = [];
      this.parseEventiAssegnati(result.eventiAssegnati);
      this.parseProssimiEventiAssegnati(result.prossimiEventiAssegnati);
      if (result.eventiAssegnati.dimensionePagina && result.eventiAssegnati.totale &&
        result.eventiAssegnati.dimensionePagina < result.eventiAssegnati.totale && this.userInfo){
        this.calendarioService.recuperaEventiAssegnati(
          this.userInfo.codiceFiscale, this.showedDate ? this.showedDate.getMonth() : this.now.getMonth(),
          this.showedDate ? this.showedDate.getFullYear() : this.now.getFullYear(),
          result.eventiAssegnati.totale - result.eventiAssegnati.dimensionePagina, 100).subscribe(
            elem => {
              this.parseEventiAssegnati(elem);
            }
          );
      }

    });
  }

  tornaAdOggi() {
    this.date = null;
    this.datePicker?.onNavigateDateSelect(new NgbDate(this.now.getFullYear(), this.now.getMonth() + 1, this.now.getDate()));
  }

  get today() {
    return this.datePipe.transform(this.now, 'dd MMMM yyyy', undefined, 'IT-it')?.toUpperCase();
  }

}
