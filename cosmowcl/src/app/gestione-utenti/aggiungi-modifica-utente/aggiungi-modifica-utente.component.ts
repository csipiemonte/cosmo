/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, Validators, FormControl } from '@angular/forms';
import { UtentiService } from 'src/app/shared/services/utenti.service';
import { EntiService } from 'src/app/shared/services/enti.service';
import { DatePipe } from '@angular/common';
import { ProfiliService } from 'src/app/shared/services/profili.service';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { combineLatest, forkJoin, Observable, of, Subject, Subscription } from 'rxjs';
import { ModalService } from 'src/app/shared/services/modal.service';
import { TranslateService } from '@ngx-translate/core';
import { Utils } from 'src/app/shared/utilities/utilities';
import { UtenteCampiTecnici } from 'src/app/shared/models/api/cosmoauthorization/utenteCampiTecnici';
import { Ente } from 'src/app/shared/models/api/cosmoauthorization/ente';
import { Profilo } from 'src/app/shared/models/api/cosmoauthorization/profilo';
import { AssociazioneUtenteProfilo } from 'src/app/shared/models/api/cosmoauthorization/associazioneUtenteProfilo';
import { ComponentCanDeactivate } from 'src/app/shared/guards/can-deactivate.guard';
import { SecurityService } from 'src/app/shared/services/security.service';
import { debounce, debounceTime, finalize, map, mergeMap } from 'rxjs/operators';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { Constants } from 'src/app/shared/constants/constants';
import { SpinnerVisibilityService } from 'ng-http-loader';


@Component({
  selector: 'app-aggiungi-modifica-utente',
  templateUrl: './aggiungi-modifica-utente.component.html',
  styleUrls: ['./aggiungi-modifica-utente.component.scss']
})
export class AggiungiModificaUtenteComponent implements OnInit, OnDestroy, ComponentCanDeactivate {

  codiceFiscalePattern = Constants.PATTERNS.CODICE_FISCALE_UTENTE;
  idEnte: number | null = null;
  idUtente?: number | null = null;
  utenteForm!: FormGroup;
  utenteCampiTecnici: UtenteCampiTecnici | null = null;
  ente: Ente | null = null;
  profiliDisponibili: Array<Profilo> = [];
  profili: Array<Profilo> = [];
  dragAndDropIsTouched = false;
  principal?: UserInfoWrapper;
  isAssociazioneEsistente = false;

  subscriptions: Subscription[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private utentiService: UtentiService,
    private entiService: EntiService,
    private profiliService: ProfiliService,
    private datePipe: DatePipe,
    private modalService: ModalService,
    private translateService: TranslateService,
    private securityService: SecurityService,
    private spinner: SpinnerVisibilityService,
  ) {}

  ngOnDestroy(): void {
    for (const sub of this.subscriptions) {
      sub.unsubscribe();
    }
  }

  get itsAMe(): boolean {
    return !!this.principal && !!this.utenteCampiTecnici && this.utenteCampiTecnici.utente?.codiceFiscale === this.principal.codiceFiscale;
  }

  canRemoveProfile(profilo: Profilo): boolean {
    if (!profilo?.useCases?.length || !this.itsAMe) {
      return true;
    }

    // un amministratore non puo' rimuoversi il profilo di amministrazione ente da solo
    return !profilo.useCases.find(u => u.codice === 'ADMIN_ENTE');
  }

  // @HostListener allows us to also guard against browser refresh, close, etc.
  @HostListener('window:beforeunload')
  canDeactivate(): Observable<boolean> | boolean {
    // insert logic to check if there are pending changes here;
    // returning true will navigate without confirmation
    // returning false will show a confirm dialog before navigating away
    if (this.dragAndDropIsTouched || this.utenteForm?.dirty) {
      return false;
    }
    return true;
  }

  ngOnInit(): void {
    combineLatest([this.securityService.getCurrentUser(), this.route.params]).pipe(
      map(latest => {
        return {
          principal: latest[0],
          idEnte: Utils.require(latest[0].ente?.id, 'idEnte'),
          idUtente: latest[1]?.id ? +latest[1].id : undefined
        };
      }),
      mergeMap(data => {
        return forkJoin({
          utenteCurrent: data.idUtente ? this.utentiService.getUtenteValidita(data.idUtente, data.idEnte) : of(null),
          enteCurrent: this.entiService.getEnte(data.idEnte),
          profiliDisponibili: this.profiliService.getProfili(JSON.stringify({
            filter: {
              assegnabile: {
                defined: true,
                eq: true,
              }
            }
          }))
        }).pipe(
          map(moreData => {
            return {
              ...data,
              ...moreData
            };
          })
        );
      })
    ).subscribe(loaded => {
      this.principal = loaded.principal;
      this.idEnte = loaded.idEnte;
      this.idUtente = loaded.idUtente;

      if (loaded.utenteCurrent) {
        this.ente = loaded.enteCurrent.ente as Ente;
        this.utenteCampiTecnici = loaded.utenteCurrent;
        this.profiliDisponibili = loaded.profiliDisponibili.profili as Array<Profilo>;

        if (this.utenteCampiTecnici.utente){
          this.utenteCampiTecnici.utente.profili.filter(profilo => profilo.ente?.id === this.idEnte).forEach(profilo => {
            this.profili.push(profilo.profilo);

            this.profiliDisponibili = this.profiliDisponibili
              .filter(profiloDisponibile => profiloDisponibile.id !== profilo.profilo.id);
          });
          this.initForm();
        }
      } else {
        this.profiliDisponibili = loaded.profiliDisponibili.profili as Array<Profilo>;
        this.ente = loaded.enteCurrent.ente as Ente;
        if (this.ente.codiceProfiloDefault){
          const profiloDefault = this.profiliDisponibili.filter(profilo => this.ente?.codiceProfiloDefault === profilo.codice)[0];
          if (profiloDefault){
            this.profili.push(profiloDefault);
            this.profiliDisponibili = this.profiliDisponibili
              .filter(profiloDisponibile => profiloDisponibile.id !== profiloDefault.id);
          }
        }

        this.initForm();
      }
    });
  }

  drop(event: CdkDragDrop<string[]>) {
    this.dragAndDropIsTouched = true;
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
    }
  }

  private initForm() {
    let nome = '';
    let cognome = '';
    let codiceFiscale = '';
    let email = '';
    let telefono = null;
    let inizioValidita = this.datePipe.transform(new Date(), 'yyyy-MM-dd');
    let fineValidita = null;

    if (this.idUtente) {
      nome = this.utenteCampiTecnici?.utente?.nome as string;
      cognome = this.utenteCampiTecnici?.utente?.cognome as string;
      codiceFiscale = this.utenteCampiTecnici?.utente?.codiceFiscale as string;
      inizioValidita = this.utenteCampiTecnici?.dtInizioValidita ? this.datePipe
        .transform(this.utenteCampiTecnici?.dtInizioValidita, 'yyyy-MM-dd') : null;
      fineValidita = this.utenteCampiTecnici?.dtFineValidita ? this.datePipe
        .transform(this.utenteCampiTecnici?.dtFineValidita, 'yyyy-MM-dd') : null;
      this.utenteCampiTecnici?.utente?.enti.filter(ente => {
        if (ente.ente && ente.ente.id === this.idEnte) {
          email = ente.email ? ente.email : '';
          telefono = ente.telefono ? parseInt(ente.telefono, 10) : null;
        }
      });
    }

    this.utenteForm = new FormGroup({
      nome: new FormControl(nome, Validators.required),
      cognome: new FormControl(cognome, Validators.required),
      codiceFiscale: new FormControl(codiceFiscale, [Validators.required, Validators.pattern(this.codiceFiscalePattern)]),
      email: new FormControl(email, [Validators.email, Validators.required]),
      telefono: new FormControl(telefono),
      inizioValidita: new FormControl(inizioValidita, Validators.required),
      fineValidita: new FormControl(fineValidita),
    });

    this.utenteForm.get('codiceFiscale')?.valueChanges.pipe(
      debounceTime(500),
    ).subscribe(newCodFiscale => {
      this.checkCFEsistente(newCodFiscale);
    });
  }

  private checkCFEsistente(input: string) {
    if (!input?.trim().length) {
      // cleared
      if (this.isAssociazioneEsistente) {
        this.utenteForm.patchValue({
          nome: '',
          cognome: '',
        });
      }
      this.isAssociazioneEsistente = false;
      return;
    }

    this.spinner.show();

    this.utentiService.getUtenteByCodiceFiscale(input).pipe(
      finalize(() => this.spinner.hide())
    ).subscribe(user => {
      this.isAssociazioneEsistente = !!user;
      if (this.isAssociazioneEsistente) {
        this.utenteForm.patchValue({
          nome: user?.nome,
          cognome: user?.cognome,
        });
      }
    }, failure => this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore));
  }

  tornaIndietro() {
    // this.router.navigate(['back'], { relativeTo: this.route });
    window.history.back();
  }

  pulisciCampi() {

    this.profiliDisponibili = [];
    this.profili = [];

    this.dragAndDropIsTouched = false;
    this.isAssociazioneEsistente = false;
    this.initForm();

    this.profiliService.getProfili(JSON.stringify({
      filter: {
        assegnabile: {
          defined: true,
          eq: true,
        }
      }
    })).subscribe(profili => {
      this.profiliDisponibili = profili.profili as Array<Profilo>;

      if (this.utenteCampiTecnici?.utente) {

        this.utenteCampiTecnici?.utente.profili.filter(profilo => profilo.ente?.id === this.idEnte).forEach(profilo => {
          this.profili.push(profilo.profilo);

          this.profiliDisponibili = this.profiliDisponibili
            .filter(profiloDisponibile => profiloDisponibile.id !== profilo.profilo.id);
        });
      }

    });

  }

  onSubmit() {
    // e' una modifica dell'utente
    if (this.idUtente && this.utenteCampiTecnici && this.utenteCampiTecnici.utente) {
      this.utenteCampiTecnici.utente.nome = this.utenteForm.value.nome;
      this.utenteCampiTecnici.utente.cognome = this.utenteForm.value.cognome;
      this.utenteCampiTecnici.utente.codiceFiscale = this.utenteForm.value.codiceFiscale;
      this.utenteCampiTecnici.dtInizioValidita = this.utenteForm.value.inizioValidita ?
        new Date(this.utenteForm.value.inizioValidita).toISOString() : new Date().toISOString();
      this.utenteCampiTecnici.dtFineValidita = this.utenteForm.value.fineValidita ?
        new Date(this.utenteForm.value.fineValidita).toISOString() : undefined;
      this.utenteCampiTecnici.utente?.enti.filter(ente => {
        if (ente.ente && ente.ente.id === this.idEnte) {
          ente.email = this.utenteForm?.value.email;
          ente.telefono = this.utenteForm?.value.telefono;
        }
      });

      this.utenteCampiTecnici.utente.profili.filter(profiloUtente => profiloUtente.ente?.id === this.idEnte)
        .forEach(profiloUtente => {
          if (!this.profili.includes(profiloUtente.profilo) && this.utenteCampiTecnici) {
            this.utenteCampiTecnici.utente?.profili.splice(this.utenteCampiTecnici.utente.profili.indexOf(profiloUtente), 1);
          }
        });

      const profiliUtente = this.utenteCampiTecnici.utente.profili.map(profiloUtente => {
        if (profiloUtente.ente?.id === this.idEnte) {
          return profiloUtente.profilo;
        }
      });

      this.profili.filter(profilo => !profiliUtente.includes(profilo) )
        .forEach(profilo => this.utenteCampiTecnici?.utente?.profili.push({ ente: this.ente as Ente, profilo }));

      this.utentiService.aggiornaUtenteCampiTecnici(this.utenteCampiTecnici).subscribe(result => {
        this.clearDirty();
        if (result.utente) {
          let messaggio = this.translateService.instant('common.aggiornamento_utente_messaggio');
          messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.utente.nome + ' ' + result.utente.cognome]);
          this.modalService.info(this.translateService.instant('common.aggiornamento_utente_titolo'),
            messaggio).then(
              () => {
                this.tornaIndietro();
              }
            ).catch(() => { });
        }
      },
        error => {
          const messaggioErrore = error.error.title ? error.error.title : this.translateService.instant('common.aggiornamento_utente_messaggio_errore');

          this.modalService.error(this.translateService.instant('common.aggiornamento_utente_titolo'),
            messaggioErrore, error.error.errore)
            .then()
            .catch(() => { });
        });

    }
    // e' un nuovo utente
    else {
      const profiliUtenti: Array<AssociazioneUtenteProfilo> = [];

      if (this.profili && this.ente) {
        this.profili.forEach(profilo => profiliUtenti.push({ ente: this.ente as Ente, profilo }));
      }

      this.utenteCampiTecnici = {
        utente: {
          nome: this.utenteForm.value.nome,
          cognome: this.utenteForm.value.cognome,
          codiceFiscale: this.utenteForm.value.codiceFiscale,
          enti: [{
            ente: this.ente as Ente,
            email: this.utenteForm.value.email,
            telefono: this.utenteForm.value.telefono
          }],
          profili: profiliUtenti
        },
        dtInizioValidita: this.utenteForm.value.inizioValidita ?
          new Date(this.utenteForm.value.inizioValidita).toISOString() : new Date().toISOString(),
        dtFineValidita: this.utenteForm.value.fineValidita ?
          new Date(this.utenteForm.value.fineValidita).toISOString() : undefined
      };

      const obs = this.utentiService.salvaUtenteCampiTecnici(this.utenteCampiTecnici);

      obs.subscribe(
        result => {
          this.clearDirty();
          if (result.utente) {
            let messaggio = this.translateService.instant('common.creazione_utente_messaggio');
            messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.utente.nome + ' ' + result.utente.cognome]);
            this.modalService.info(this.translateService.instant('common.creazione_utente_titolo'),
              messaggio).then(
                () => {
                  this.tornaIndietro();
                }
              ).catch(() => { });
          }
        },
        error => {
          const messaggioErrore = error.error.title ? error.error.title : this.translateService.instant('common.aggiornamento_utente_messaggio_errore');

          this.modalService.error(this.translateService.instant('common.aggiornamento_utente_titolo'),
            messaggioErrore, error.error.errore)
            .then()
            .catch(() => { });
        }
      );
    }
  }

  clearDirty(): void {
    this.dragAndDropIsTouched = false;
    if (this.utenteForm) {
      this.utenteForm.markAsPristine();
    }
  }

}
