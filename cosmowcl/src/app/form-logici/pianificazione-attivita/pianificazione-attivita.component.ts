/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Injector, OnInit, ViewChild } from '@angular/core';
import { SecurityService } from 'src/app/shared/services/security.service';
import { UtentiService } from 'src/app/shared/services/utenti.service';
import { FunzionalitaParentComponent } from '../funzionalita-parent.component';
import { PianificazioneAttivita } from './models/pianificazione-attivita.model';
import { AttivitaAssegnazione } from './models/attivita-assegnazione.model';
import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { AttivitaUtente } from './models/attivita-utente.model';
import { FlowableOutput } from '../models/flowable-output-model';
import { OutputAssegnazione } from './models/output-assegnazione.model';
import { VariabileProcesso } from 'src/app/shared/models/api/cosmobusiness/variabileProcesso';
import { Utils } from 'src/app/shared/utilities/utilities';
import { TranslateService } from '@ngx-translate/core';
import { MessaggioControlliObbligatori, TipoMessaggio } from 'src/app/shared/models/messaggi-controlli-obbligatori/messaggio-controlli-obbligatori';
import { Utente } from 'src/app/shared/models/api/cosmoauthorization/utente';
import { Constants } from 'src/app/shared/constants/constants';
import { RicercaUtenteComponent, SelezioneUtenteGruppo } from 'src/app/shared/components/ricerca-utente/ricerca-utente.component';
import { Gruppo } from 'src/app/shared/models/api/cosmoauthorization/gruppo';
import { GruppiService } from 'src/app/shared/services/gruppi.service';
import { forkJoin } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { UtenteGruppoDTO, UtenteGruppoType } from './models/utente-gruppo.model';


const ATTIVITA_KEY = 'ATT_ASSEGNABILI';
const OUTPUT_KEY = 'O_ASSEGNAZIONE';
@Component({
  selector: 'app-pianificazione-attivita',
  templateUrl: './pianificazione-attivita.component.html',
  styleUrls: ['./pianificazione-attivita.component.scss']
})
export class PianificazioneAttivitaComponent extends FunzionalitaParentComponent implements OnInit {


  idEnte: number | null = null;
  utenti: Utente[] = [];
  attivitaUtente = new Array<AttivitaUtente>();
  outputAssegnazione: OutputAssegnazione | null = null;
  attivitaSelezionata: AttivitaUtente | null = null;
  pianificazioni = new Array<PianificazioneAttivita>();
  attivitaWip: string[] = [];
  messaggiObbligatori: MessaggioControlliObbligatori[] = [];
  attivitaObbligatorie: string[] = [];
  attivitaFacoltative: string[] = [];

  ricercaAncheGruppi = true;

  public selectedUser?: SelezioneUtenteGruppo = undefined;
  @ViewChild('ricerca') ricerca: RicercaUtenteComponent | null = null;
  loading = 0;
  loadingError: any = null;

  constructor(
    private securityService: SecurityService,
    private utentiService: UtentiService,
    private gruppiService: GruppiService,
    public injector: Injector,
    private translateService: TranslateService) {

    super(injector);
    if (this.parametri.get(ATTIVITA_KEY) && this.parametri.get(OUTPUT_KEY)) {
      this.attivitaUtente = JSON.parse(this.parametri.get(ATTIVITA_KEY) || '{}');

      if (this.attivitaUtente.length === 1) {
        this.attivitaSelezionata = this.attivitaUtente[0];
        if (this.attivitaSelezionata && (this.attivitaSelezionata.soloUtenti || ((this.attivitaSelezionata.selezioneUtentiDeiGruppi
           && this.attivitaSelezionata.selezioneUtentiDeiGruppi.length > 0) || (this.attivitaSelezionata.selezioneUtentiDeiTag
           && this.attivitaSelezionata.selezioneUtentiDeiTag.length > 0)))){
          this.ricercaAncheGruppi = false;
        }
      }
      this.outputAssegnazione = JSON.parse(this.parametri.get(OUTPUT_KEY) || '{}');
    }
  }

  ngOnInit(): void {
    this.securityService.getCurrentUser().subscribe((userInfo) => {
      if (userInfo.ente?.id) {
        this.idEnte = userInfo.ente.id;
      }
    });

    this.refresh();

    this.sendToParent();
  }

  refresh() {
    if (this.variabiliProcesso && this.variabiliProcesso.length > 0) {
      this.outputAssegnazione?.attivitaAssegnazione.forEach(attivita => {

        const utenteGruppoList: VariabileProcesso | undefined = this.variabiliProcesso.find(variabile => variabile.name === attivita.ruolo);

        if (utenteGruppoList?.value && (utenteGruppoList.value as UtenteGruppoDTO[]).length > 0) {
          const utenteList = (utenteGruppoList.value as UtenteGruppoDTO[]).filter(v => v.type === UtenteGruppoType.UTENTE && v.userId);

          const gruppoList = (utenteGruppoList.value as UtenteGruppoDTO[]).filter(v => v.type === UtenteGruppoType.GRUPPO && v.groupId);

          this.loading++;
          this.loadingError = null;

          if (utenteList && utenteList.length > 0 && gruppoList && gruppoList.length > 0) {
            this.cercaUtentiGruppi(utenteList, gruppoList, attivita.nomeAttivita);

          } else if (utenteList && utenteList.length > 0 && (!gruppoList || gruppoList.length === 0)) {
            this.cercaUtenti(utenteList, attivita.nomeAttivita);

          } else if (gruppoList && gruppoList.length > 0 && (!utenteList || utenteList.length === 0)) {
            this.cercaGruppi( gruppoList, attivita.nomeAttivita);
          }

        } else {
          this.controlloAttivitaAssegnate();
        }
      });
    }
  }

  private cercaUtenti(utenteList: UtenteGruppoDTO[], nomeAttivita: string){
    const utenteFilter = {
      filter: {
        codiceFiscale: {
          in: utenteList.map(v => v.userId)
        },
        idEnte: {
          eq: this.idEnte
        }
      }
    };

    this.utentiService.getUtenti(JSON.stringify(utenteFilter))
    .pipe(
      finalize(() => {
        this.loading--;
      })
    )
    .subscribe(result => {
      if (result.utenti) {
        this.setUtenti(result.utenti, nomeAttivita);
        this.controlloAttivitaAssegnate();
        this.sendToParent();
      }
    }, failure => {
      this.loadingError = failure;
    });
  }

  private cercaGruppi(gruppoList: UtenteGruppoDTO[], nomeAttivita: string){
    const gruppoFilter = {
      filter: {
        codice: {
          in: gruppoList.map(v => v.groupId)
        },
        idEnte: {
          eq: this.idEnte
        },
        codiceTipoPratica: {
          eq: this.pratica.tipo?.codice
        }
      }
    };

    this.gruppiService.getGruppi(JSON.stringify(gruppoFilter))
    .pipe(
      finalize(() => {
        this.loading--;
      })
    )
    .subscribe(result => {
      if (result.gruppi) {
        this.setGruppi(result.gruppi, nomeAttivita);
        this.controlloAttivitaAssegnate();
        this.sendToParent();
      }
    }, failure => {
      this.loadingError = failure;
    });
  }

  private cercaUtentiGruppi(utenteList: UtenteGruppoDTO[], gruppoList: UtenteGruppoDTO[], nomeAttivita: string){

    const utenteFilter = {
      filter: {
        codiceFiscale: {
          in: utenteList.map(v => v.userId)
        },
        idEnte: {
          eq: this.idEnte
        }
      }
    };

    const gruppoFilter = {
      filter: {
        codice: {
          in: gruppoList.map(v => v.groupId)
        },
        idEnte: {
          eq: this.idEnte
        },
        codiceTipoPratica: {
          eq: this.pratica.tipo?.codice
        }
      }
    };
    forkJoin({
      utenti: this.utentiService.getUtenti(JSON.stringify(utenteFilter)),
      gruppi: this.gruppiService.getGruppi(JSON.stringify(gruppoFilter))
    })
    .pipe(
      finalize(() => {
        this.loading--;
      })
    )
    .subscribe(result => {
      if ( result.utenti.utenti) {
        this.setUtenti(result.utenti.utenti, nomeAttivita);
      }

      if (result.gruppi.gruppi) {
        this.setGruppi(result.gruppi.gruppi, nomeAttivita);
      }

      this.controlloAttivitaAssegnate();
      this.sendToParent();
    }, failure => {
      this.loadingError = failure;
    });
  }

  private setUtenti(utenti: Utente[], nomeAttivita: string) {
    utenti.forEach(utente => {
        this.pianificazioni.push({
          utenteGruppo: {
            utente
          },
          attivita: {
            id: 0,
            nome: nomeAttivita,
            obbligatorio: true
          }
        });
    });
  }

  private setGruppi(gruppi: Gruppo[], nomeAttivita: string) {
    gruppi.forEach(gruppo => {
        this.pianificazioni.push({
          utenteGruppo: {
            gruppo
          },
          attivita: {
            id: 0,
            nome: nomeAttivita,
            obbligatorio: true
          }
        });
    });
  }

  aggiungi() {
    if (this.selectedUser && this.attivitaSelezionata && !this.attivitaDaEscludere(this.attivitaSelezionata)) {
      const pianificazione: PianificazioneAttivita = { utenteGruppo: this.selectedUser, attivita: this.attivitaSelezionata };
      if (this.attivitaSelezionata.attivitaDaEscludere){
        this.attivitaSelezionata.attivitaDaEscludere.forEach(elemEscluso => {
          const elemTrovati = this.pianificazioni.filter(pianific => +pianific.attivita.id === +elemEscluso);
          elemTrovati.forEach(element => {
            this.pianificazioni.splice(this.pianificazioni.indexOf(element), 1);
          });
        });
      }
      this.pianificazioni.push(pianificazione);
      this.selectedUser = undefined;
      this.ricerca?.clear();
      this.attivitaSelezionata = this.attivitaUtente.length === 1 ? this.attivitaUtente[0] : null;

      if (this.attivitaSelezionata  && (this.attivitaSelezionata.soloUtenti || ((this.attivitaSelezionata.selezioneUtentiDeiGruppi
        && this.attivitaSelezionata.selezioneUtentiDeiGruppi.length > 0) || (this.attivitaSelezionata.selezioneUtentiDeiTag
        && this.attivitaSelezionata.selezioneUtentiDeiTag.length > 0)))){
        this.ricercaAncheGruppi = false;
      } else{
        this.ricercaAncheGruppi = true;
      }

      this.controlloAttivitaAssegnate();
      this.sendToParent();
      this.dataChanged = true;
    }
  }


  deletePianificazione(index: number) {
    this.pianificazioni.splice(index, 1);
    this.controlloAttivitaAssegnate();
    this.sendToParent();
    this.dataChanged = true;
  }

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.pianificazioni, event.previousIndex, event.currentIndex);
    this.sendToParent();
  }

  disabledAddButton() {
    return (!this.selectedUser || this.selectedUser.nuovo) || !this.attivitaSelezionata ? true :
      this.pianificazioni.find(o => (this.selectedUser?.utente !== null && o.utenteGruppo.utente
        && o.utenteGruppo.utente.id === this.selectedUser?.utente?.id && o.attivita.nome === this.attivitaSelezionata?.nome)
        || (this.selectedUser?.gruppo !== null && o.utenteGruppo.gruppo
          && o.utenteGruppo.gruppo.id === this.selectedUser?.gruppo?.id && o.attivita.nome === this.attivitaSelezionata?.nome)
        || this.raggiuntoNumeroMaxUtenti());
  }

  sendToParent() {
    const flowableOutputArray: FlowableOutput[] = [];

    this.attivitaUtente.forEach(singolaAttivita => {
      const matched: PianificazioneAttivita[] = this.pianificazioni
        .filter(pianificazione => pianificazione.attivita.nome === singolaAttivita.nome);

      const attivitaAssegnazione: AttivitaAssegnazione = this.outputAssegnazione?.attivitaAssegnazione.find(
        output => output.nomeAttivita === singolaAttivita.nome
      ) || { nomeAttivita: '', ruolo: '' };

      if (matched && matched.length > 0 && attivitaAssegnazione.nomeAttivita.length > 0) {
        const utenteGruppo: UtenteGruppoDTO[] = [];

        matched.forEach(elem => {
          if (elem.utenteGruppo.utente) {
            utenteGruppo.push({
              userId: elem.utenteGruppo.utente.codiceFiscale,
              type: UtenteGruppoType.UTENTE
            });
          } else if (elem.utenteGruppo.gruppo) {
            utenteGruppo.push({
              groupId: elem.utenteGruppo.gruppo.codice,
              type: UtenteGruppoType.GRUPPO
            });
          }
        });
        flowableOutputArray.push({ name: attivitaAssegnazione.ruolo, value: utenteGruppo });
      }
    });

    let payload = {};
    if (this.outputAssegnazione && flowableOutputArray.length < this.outputAssegnazione?.attivitaAssegnazione.length) {
      this.outputAssegnazione?.attivitaAssegnazione.forEach(attivitaAssegnazione => {
        let found = false;
        flowableOutputArray.forEach(flowableOutputArrayElem => {
          if (flowableOutputArrayElem.name === attivitaAssegnazione.ruolo) {
            found = true;
          }
        });
        if (!found) {
          flowableOutputArray.push({ name: attivitaAssegnazione.ruolo, value: [] });
        }
      });
    }
    payload = JSON.parse(JSON.stringify(flowableOutputArray));

    this.sendData({
      payload,
    });
  }

  public isValid(): boolean {
    if (this.isSubtask) {
      return true;
    }
    return this.isTabValid();
  }

  public isWip(): boolean {
    if (this.readOnly || this.isSubtask) {
      return false;
    }
    return this.attivitaObbligatorie.length > 0;
  }

  private controlloAttivitaAssegnate() {
    const attivitaWip = this.controlloAttivita(this.pianificazioni, this.parametri);
    const maxNumUtentiWip = this.controllaMaxNumeroUtenti(this.pianificazioni, this.parametri);

    this.messaggiObbligatori = [];
    this.attivitaObbligatorie = [];
    this.attivitaFacoltative = [];

    if (attivitaWip.length > 0) {
      attivitaWip.sort();
      attivitaWip.forEach(attivita => {
        if (String(attivita.obbligatorio) === 'true') {
          this.attivitaObbligatorie.push(attivita.nome);
        } else {
          this.attivitaFacoltative.push(attivita.nome);
        }
      });
      if (this.attivitaObbligatorie.length > 0) {
        // tslint:disable-next-line:max-line-length
        const message = Utils.parseAndReplacePlaceholders(this.translateService.instant('errori.attivita_obbligatorie'), [this.attivitaObbligatorie.join(', ')]);
        this.messaggiObbligatori.push({ messaggio: message, tipoMessaggio: TipoMessaggio.ERROR });
      }
      if (this.attivitaFacoltative.length > 0) {
        // tslint:disable-next-line:max-line-length
        const messageF = Utils.parseAndReplacePlaceholders(this.translateService.instant('errori.attivita_facoltative'), [this.attivitaFacoltative.join(', ')]);
        this.messaggiObbligatori.push({ messaggio: messageF, tipoMessaggio: TipoMessaggio.WARNING });
      }
    }

    if (maxNumUtentiWip.length > 0) {
      const message = maxNumUtentiWip.length === 1
        // tslint:disable-next-line:max-line-length
        ? Utils.parseAndReplacePlaceholders(this.translateService.instant('form_logici.max_numero_utenti_una_attivita'), [maxNumUtentiWip[0]])
        // tslint:disable-next-line:max-line-length
        : Utils.parseAndReplacePlaceholders(this.translateService.instant('form_logici.max_numero_utenti_piu_attivita'), [maxNumUtentiWip.join(', ')]);
      this.messaggiObbligatori.push({ messaggio: message, tipoMessaggio: TipoMessaggio.WARNING });
    }
  }

  private isTabValid() {
    return this.pianificazioni && this.pianificazioni.length > 0 && this.attivitaObbligatorie.length === 0;
  }

  controlloAttivita(pianificazioni: Array<PianificazioneAttivita>, parametri: Map<string, string>) {
    let attivitaUtente: AttivitaUtente[];
    const attivitaWip: AttivitaUtente[] = [];
    if (parametri?.get(Constants.FORM_LOGICI.ATTIVITA_KEY)) {
      attivitaUtente = JSON.parse(parametri.get(Constants.FORM_LOGICI.ATTIVITA_KEY) || '{}');
      if (pianificazioni.length === 0 && attivitaUtente.length > 0) {
        return attivitaUtente;
      }
      attivitaUtente.forEach(singolaAttivita => {
        const findAttivitaPianificata = pianificazioni.find(pian => pian.attivita.nome === singolaAttivita.nome);
        if (!findAttivitaPianificata) {
          attivitaWip.push(singolaAttivita);
        }
      });
    }
    return attivitaWip;
  }

  controllaMaxNumeroUtenti(pianificazioni: Array<PianificazioneAttivita>, parametri: Map<string, string>): string[] {
    const attivitaConUtentiRaggiunti: string[] = [];
    if (parametri?.get(Constants.FORM_LOGICI.ATTIVITA_KEY)) {
      const attivitaUtente: AttivitaUtente[] = JSON.parse(parametri.get(Constants.FORM_LOGICI.ATTIVITA_KEY) || '{}');
      if (pianificazioni.length > 0 && attivitaUtente.length > 0) {
        attivitaUtente.forEach(singolaAttivita => {
          const numAttivita = pianificazioni.filter(pianificazione => pianificazione.attivita.nome === singolaAttivita.nome).length;
          if (singolaAttivita.numeroUtentiMax && (+singolaAttivita.numeroUtentiMax) === numAttivita) {
            attivitaConUtentiRaggiunti.push(singolaAttivita.nome);
          }
        });
      }
    }
    return attivitaConUtentiRaggiunti;
  }

  raggiuntoNumeroMaxUtenti(): boolean {
    if (this.attivitaSelezionata?.numeroUtentiMax) {
      const numAttuale = this.pianificazioni.filter(pian => pian.attivita.nome === this.attivitaSelezionata?.nome).length;
      if (numAttuale === (+this.attivitaSelezionata.numeroUtentiMax)) {
        return true;
      }
    }
    return false;
  }

  filtroRicercaGruppi = (filtro: any) => {
    if (this.pratica.tipo){
      filtro.codiceTipoPratica = filtro.codiceTipoPratica || {};
      filtro.codiceTipoPratica.eq = this.pratica.tipo.codice;
    }

    return filtro;
  }

  disabilitaAttivita(singolaAttivita: AttivitaUtente): boolean{
    return !!this.attivitaDaEscludere(singolaAttivita);
  }

  attivitaDaEscludere(attivita: AttivitaUtente): AttivitaUtente | undefined {
    return this.attivitaUtente.find(singolaAttivitaUtente =>
      singolaAttivitaUtente.attivitaDaEscludere
      && singolaAttivitaUtente.attivitaDaEscludere?.length > 0
      && singolaAttivitaUtente.attivitaDaEscludere.find(att => +att === +attivita.id)
      && this.pianificazioni.find(pianificazione => pianificazione.attivita.nome === singolaAttivitaUtente.nome));
  }


  filtroRicercaUtenti = (filtro: any) => {

    if (this.attivitaSelezionata  && this.attivitaSelezionata.selezioneUtentiDeiGruppi
      && this.attivitaSelezionata.selezioneUtentiDeiGruppi.length > 0){
        filtro.neiGruppi = this.attivitaSelezionata.selezioneUtentiDeiGruppi
        .filter(x => x && x.type === Constants.FORM_LOGICI.NOME_GRUPPO).map(x => {
          const array = x.tag?.filter(y => y.type === Constants.FORM_LOGICI.NOME_TAG && y.valore)?.map(y => y.valore) ?? [];

          const arr2 = x.tag?.filter(z => z && z.type === Constants.FORM_LOGICI.VARIABILE_PROCESSO && z.valore)
          .map(z => this.variabiliProcesso.find(y => y.name === z.valore)?.value) ?? [];
          for (const el of arr2){
            if (Array.isArray(el)){
              for (const el1 of el){
                if (typeof el1 === 'string'){
                  array.push(el1);
                }
              }
            }
          }

          return {nomeGruppo: x.valore, nomeTag: array};
        }

        );

        const arr = this.attivitaSelezionata.selezioneUtentiDeiGruppi
        .filter(x => x && x.type === Constants.FORM_LOGICI.VARIABILE_PROCESSO)
        .map(x => ({variabile: this.variabiliProcesso.find(y => y.name === x.valore)?.value, tags: x.tag}));

        for (const a of arr){
          if (Array.isArray(a.variabile)){
            for (const b of a.variabile){
              if (typeof b === 'string'){

                const array = a.tags?.filter(y => y.type === Constants.FORM_LOGICI.NOME_TAG && y.valore)?.map(y => y.valore) ?? [];

                const arr2 = a.tags?.filter(z => z && z.type === Constants.FORM_LOGICI.VARIABILE_PROCESSO)
                .map(z => this.variabiliProcesso.find(y => y.name === z.valore)?.value) ?? [];

                for (const el of arr2){
                  if (Array.isArray(el)){
                    for (const el1 of el){
                      if (typeof el1 === 'string'){
                        array.push(el1);
                      }
                    }
                  }
                }

                filtro.neiGruppi.push({nomeGruppo: b, nomeTag: array});

              }
            }
          }
        }

      }
    if (this.attivitaSelezionata?.selezioneUtentiDeiTag && this.attivitaSelezionata.selezioneUtentiDeiTag.length > 0){
          filtro.neiTag = this.attivitaSelezionata
          .selezioneUtentiDeiTag?.filter(elem => elem && elem.type === Constants.FORM_LOGICI.NOME_TAG)
          .map(elem => elem.valore);
          const arr = this.attivitaSelezionata
          .selezioneUtentiDeiTag?.filter(x => x && x.type === Constants.FORM_LOGICI.VARIABILE_PROCESSO)
          .map(x => this.variabiliProcesso.find(y => y.name === x.valore)).map(w => w?.value);
          for (const c of arr){
            if (Array.isArray(c)){
              for (const d of c){
                if (typeof d === 'string'){
                  filtro.neiTag.push(d);
                }
              }
            }
          }
        }
    return filtro;
  }

  cleanUtentiGruppi(){
    this.selectedUser = undefined;
    this.ricerca?.clear();

    if (this.attivitaSelezionata  && (this.attivitaSelezionata.soloUtenti || ((this.attivitaSelezionata.selezioneUtentiDeiGruppi
      && this.attivitaSelezionata.selezioneUtentiDeiGruppi.length > 0) || (this.attivitaSelezionata.selezioneUtentiDeiTag
      && this.attivitaSelezionata.selezioneUtentiDeiTag.length > 0)))){
        this.ricercaAncheGruppi = false;
    } else {
      this.ricercaAncheGruppi = true;
    }

  }
}
