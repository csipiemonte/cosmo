/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { ConfigurazioniService } from 'src/app/shared/services/configurazioni.service';
import { Constants } from 'src/app/shared/constants/constants';
import { forkJoin, merge, Observable, of, Subject } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { Ente } from 'src/app/shared/models/api/cosmoauthorization/ente';
import { debounceTime, distinctUntilChanged, filter, map, mergeMap, switchMap } from 'rxjs/operators';
import { EntiService } from 'src/app/shared/services/enti.service';
import { EntiResponse } from 'src/app/shared/models/api/cosmoauthorization/entiResponse';
import { NgbTypeahead } from '@ng-bootstrap/ng-bootstrap';
import { ProcessiService } from './processi.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { TipoPratica } from 'src/app/shared/models/api/cosmobusiness/tipoPratica';
import { GestioneTipiPraticheService } from '../gestione-tipi-pratiche/gestione-tipi-pratiche.service';
import { Router } from '@angular/router';
import { SecurityService } from 'src/app/shared/services/security.service';

@Component({
  selector: 'app-deploy-processo',
  templateUrl: './deploy-processo.component.html',
  styleUrls: ['./deploy-processo.component.scss']
})
export class DeployProcessoComponent implements OnInit {

  @ViewChild('instanceEnti', { static: true }) instanceEnti!: NgbTypeahead;
  focusEnti$ = new Subject<string>();
  clickEnti$ = new Subject<string>();

  @ViewChild('instanceTipoPratiche', { static: true }) instanceTipoPratiche!: NgbTypeahead;
  focusTipoPratiche$ = new Subject<string>();
  clickTipoPratiche$ = new Subject<string>();

  nomeFile: string | null = null;
  file: File | null = null;
  maxSize!: number;
  errore: string | null = null;

  erroreEnte: string | null = null;
  erroreTipoPratica: string | null = null;

  enti: Ente[] = [];
  enteSelezionato: Ente | null = null;
  tipiPratiche: TipoPratica[] = [];
  tipoPraticaSelezionata: TipoPratica | null = null;

  isConfiguratore = false;

  formatterEnte = (result: Ente) => result.nome;
  formatterTipoPratica = (result: TipoPratica) => result.descrizione;

  constructor(
    private modalService: ModalService,
    private translateService: TranslateService,
    private configurazioniService: ConfigurazioniService,
    private entiService: EntiService,
    private securityService: SecurityService,
    private tipoPraticheService: GestioneTipiPraticheService,
    private processiService: ProcessiService,
    private router: Router) { }

  searchEnti = (text$: Observable<string>) => {

    const debouncedText$ = text$.pipe(debounceTime(200), distinctUntilChanged());

    const clicksWithClosedPopup$ = this.clickEnti$.pipe(
      filter(() => !this.instanceEnti.isPopupOpen()),
      map(o => null)
    );

    const inputFocus$ = this.focusEnti$;

    return merge(debouncedText$, inputFocus$, clicksWithClosedPopup$).pipe(
      switchMap((searchText) => {

        if (!searchText || searchText.length === 0) {
          return of(this.enti);
        } else {
          const entiFiltrati = this.enti.filter(v => v.nome.toLowerCase().indexOf(searchText.toLowerCase()) > -1)
            .sort((c1, c2) => (c1.nome ?? '').localeCompare(c2.nome ?? '')).slice(0, 10);
          return entiFiltrati.length > 0 ? of(entiFiltrati) : this.getEnti(searchText).pipe(
            map(term =>
              term && term.enti && term.enti.length > 0 ? term.enti : []
            )
          );
        }
      })
    );
  }

  searchTipiPratiche = (text$: Observable<string>) => {

    const debouncedText$ = text$.pipe(debounceTime(200), distinctUntilChanged());

    const clicksWithClosedPopup$ = this.clickTipoPratiche$.pipe(
      filter(() => !this.instanceTipoPratiche.isPopupOpen()),
      map(o => null)
    );

    const inputFocus$ = this.focusTipoPratiche$;

    return merge(debouncedText$, inputFocus$, clicksWithClosedPopup$).pipe(
      mergeMap((term: string | null) => {
        return of(this.tipiPratiche ?
          this.tipiPratiche.filter(v => !(term?.length) || v.codice.toLowerCase().indexOf(term.toLowerCase()) > -1)
            .sort((c1, c2) => (c1.codice ?? '').localeCompare(c2.codice ?? ''))
            .slice(0, 10)
          : []);
      })
    );
  }

  ngOnInit(): void {
    this.start();
    this.securityService.getCurrentUser().subscribe(utente => {
      if (utente && utente.profilo && utente.profilo?.codice === Constants.CODICE_PROFILO_CONFIGURAZIONE){
        this.isConfiguratore = true;
        this.entiService.getEnte(utente.ente?.id as number).subscribe(result => {
          this.enteSelezionato = result.ente || null;
          this.getTipiPratiche();
        });
      }});
  }

  private start() {
    forkJoin({
      maxSize: this.configurazioniService.getConfigurazioneByChiave(Constants.PARAMETRI.AMMINISTRAZIONE.FILE_PROCESSO_MAX_SIZE),
      enti: this.getEnti(),
    }).subscribe(
      result => {
        this.maxSize = +(result.maxSize ?? 0);
        this.enti = result.enti.enti ?? [];
      });
  }

  private getEnti(searchTerm?: string): Observable<EntiResponse> {
    const payload = this.filterEnti(searchTerm);
    return this.entiService.getEnti(JSON.stringify(payload));
  }

  private filterEnti(searchTerm?: string) {
    const f: any = {
      filter: {},
      page: 0,
      fields: 'id,nome,codiceIpa,codiceFiscale',
      sort: 'nome'
    };
    if (searchTerm?.length) {
      f.filter.nome = {
        ci: searchTerm
      };
    }
    return f;
  }

  onFileChanged(event: any) {

    if (!event || event.length === 0) {
      this.setErrorFile(this.translateService.instant('errori.campo_obbligatorio'));
      return;
    }

    const ext = event[0].name.split('.').pop() as string;

    if (event[0].type || !'bar'.match(ext.toLocaleLowerCase())) {
      this.setErrorFile(this.translateService.instant('errori.formato_file_non_valido'));
      return;
    }

    if (event[0].size > this.maxSize) {
      this.setErrorFile(this.translateService.instant('errori.grandezza_upload_file_superata')
        .replace(/{{(.*?)}}/, this.maxSize / 1024));
      return;
    }

    this.errore = null;
    this.nomeFile = event[0].name;
    this.file = event[0];

  }

  private setErrorFile(errore: string) {
    this.nomeFile = null;
    this.file = null;
    this.errore = errore;
  }

  getTipiPratiche() {
    this.erroreEnte = null;
    this.erroreTipoPratica = null;

    if (this.enteSelezionato) {
      return this.tipoPraticheService.listTipiPraticaByEnte(this.enteSelezionato?.id)
        .subscribe(response => this.tipiPratiche = response);
    } else {
      this.tipiPratiche = [];
      this.tipoPraticaSelezionata = null;
    }
  }


  checkEnte() {
    if (!this.enteSelezionato) {
      this.erroreEnte = this.translateService.instant('errori.campo_obbligatorio_valore_non_valido');
    }
  }

  checkTipoPratica() {
    if (!this.tipoPraticaSelezionata) {
      this.erroreTipoPratica = this.translateService.instant('errori.campo_obbligatorio_valore_non_valido');
    }
  }

  tornaIndietro() {
    window.history.back();
  }

  pulisciCampi() {
    this.enteSelezionato = null;
    this.tipoPraticaSelezionata = null;
    this.nomeFile = null;
    this.file = null;
    this.errore = null;
    this.erroreEnte = null;
    this.erroreTipoPratica = null;
    this.start();

  }

  disabilitaSalva() {
    if (this.file
      && this.enteSelezionato && this.enteSelezionato.codiceIpa
      && this.tipoPraticaSelezionata && this.tipoPraticaSelezionata.processDefinitionKey) {
      return false;
    }
    return true;
  }

  salva() {
    if (this.file
      && this.enteSelezionato && this.enteSelezionato.codiceIpa
      && this.tipoPraticaSelezionata && this.tipoPraticaSelezionata.processDefinitionKey) {

      const formData = new FormData();
      formData.append(this.tipoPraticaSelezionata.processDefinitionKey, this.file);
      formData.append('tenantId', this.enteSelezionato.codiceIpa);
      // formData.append('file', this.file);
      // formData.append('processDefinitionKey', this.tipoPraticaSelezionata.processDefinitionKey);
      // formData.append('tenantId', this.enteSelezionato.codiceIpa);
      this.processiService.deployProcesso(formData).subscribe(response => {

        this.modalService.info(this.translateService.instant('processi.salvataggio_processo'),
          this.translateService.instant('processi.salvataggio_processo_msg'), this.translateService.instant('common.ok'))
          .then(() => { this.tornaIndietro(); })
          .catch(() => { });

      }, error => {
        this.modalService.error(this.translateService.instant('processi.salvataggio_processo'),
          this.translateService.instant('errori.salvataggio_processo'), error.error.errore, this.translateService.instant('common.ok'))
          .then(() => {  })
          .catch(() => { });
      });
    }
  }

  goBack(){
    if (this.isConfiguratore){
      this.router.navigate(['/configurazione']);
    }else{
      this.router.navigate(['/amministrazione']);
    }
  }

}
