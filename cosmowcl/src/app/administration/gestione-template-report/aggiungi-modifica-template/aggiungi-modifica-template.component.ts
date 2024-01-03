/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbTypeahead } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { forkJoin, merge, Observable, of, Subject } from 'rxjs';
import { debounceTime, delay, distinctUntilChanged, filter, map, mergeMap, switchMap } from 'rxjs/operators';
import { Constants } from 'src/app/shared/constants/constants';
import { Ente } from 'src/app/shared/models/api/cosmoauthorization/ente';
import { EntiResponse } from 'src/app/shared/models/api/cosmoauthorization/entiResponse';
import { CreaTemplateRequest } from 'src/app/shared/models/api/cosmoecm/creaTemplateRequest';
import { RiferimentoEnte } from 'src/app/shared/models/api/cosmoecm/riferimentoEnte';
import { TemplateReport } from 'src/app/shared/models/api/cosmoecm/templateReport';
import { TipoPratica } from 'src/app/shared/models/api/cosmoecm/tipoPratica';
import { ConfigurazioniService } from 'src/app/shared/services/configurazioni.service';
import { EntiService } from 'src/app/shared/services/enti.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { TemplateService } from 'src/app/shared/services/template.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';
import { GestioneTipiPraticheService } from '../../gestione-tipi-pratiche/gestione-tipi-pratiche.service';

@Component({
  selector: 'app-aggiungi-modifica-template',
  templateUrl: './aggiungi-modifica-template.component.html',
  styleUrls: ['./aggiungi-modifica-template.component.scss']
})
export class AggiungiModificaTemplateComponent implements OnInit {

  @ViewChild('instanceEnti', { static: true }) instanceEnti!: NgbTypeahead;
  focusEnti$ = new Subject<string>();
  clickEnti$ = new Subject<string>();

  @ViewChild('instanceTipoPratiche', { static: true }) instanceTipoPratiche!: NgbTypeahead;
  focusTipoPratiche$ = new Subject<string>();
  clickTipoPratiche$ = new Subject<string>();

  @ViewChild('instanceTemplatePadre', { static: true }) instanceTemplatePadre!: NgbTypeahead;
  focusTemplatePadre$ = new Subject<string>();
  clickTemplatePadre$ = new Subject<string>();

  templateReport?: TemplateReport | null;
  enti: Ente[] = [];
  enteSelezionato: Ente | null = null;
  tipiPratiche: TipoPratica[] = [];
  tipoPraticaSelezionata: TipoPratica | null = null;
  templatePadreSelezionato: TemplateReport | null | undefined = null;
  templatesPadre: TemplateReport[] = [];
  codice: string | null = null;
  nomeFile: string | null = null;
  file: File | null = null;
  maxSize!: number;
  errore: string | null = null;
  templateSorgente: string | ArrayBuffer = '';
  idTemplate!: number;
  uploadedSorgenteTemplate?: Blob;
  loading = 1;
  loadingError: any | null = null;
  loaded = false;
  templateName = '';
  codiceSalvato = '';

  formatterEnte = (result: Ente) => result.nome;
  formatterTipoPratica = (result: TipoPratica) => result.descrizione;
  formatterTemplatePadre = (result: TemplateReport) => result.codice;

  constructor(
    private route: ActivatedRoute,
    private translateService: TranslateService,
    private entiService: EntiService,
    private tipoPraticheService: GestioneTipiPraticheService,
    private templateService: TemplateService,
    private modalService: ModalService,
    private configurazioniService: ConfigurazioniService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params && params.id) {
        this.idTemplate = params.id;
        this.templateService.get(this.idTemplate).subscribe( tr => {
          if (tr) {
            this.enteSelezionato = tr.ente as RiferimentoEnte;
            this.tipoPraticaSelezionata = tr.tipoPratica ?? null;
            this.codice = tr.codice ?? null;
            if (tr.sorgenteTemplate) {
              this.uploadedSorgenteTemplate = tr.sorgenteTemplate;
            }
            if (tr.codiceTemplatePadre) {
              this.templatePadreSelezionato = {codice: tr.codiceTemplatePadre };
            }
            this.templateName = 'template_' + (this.enteSelezionato?.nome + '_' ?? '_') + this.codice + '_' + Date.now() + '.jrxml';
          }
        });

      }
      this.start();
    });

  }

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
    const inputFocus$ = this.focusTipoPratiche$;

    const clicksWithClosedPopup$ = this.clickTipoPratiche$.pipe(
      filter(() => !this.instanceTipoPratiche.isPopupOpen()),
      map(o => null)
    );

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

  searchTemplatePadre = (text$: Observable<string>) => {
    const debouncedText$ = text$.pipe(debounceTime(200), distinctUntilChanged());

    const inputFocus$ = this.focusTemplatePadre$;
    this.templatesPadreFormatter();

    const clicksWithClosedPopup$ = this.clickTemplatePadre$.pipe(
      filter(() => !this.instanceTemplatePadre.isPopupOpen()),
      map(o => null)
    );

    return merge(debouncedText$, inputFocus$, clicksWithClosedPopup$).pipe(
      switchMap((searchText) => {

        if (!searchText || searchText.length === 0) {
          return of(this.templatesPadre);

        } else {
          // tslint:disable-next-line:max-line-length
          const templatesPadreFiltrati = this.templatesPadre.filter(v => v.codice &&  v.codice.toLowerCase().indexOf(searchText.toLowerCase()) > -1)
            .sort((c1, c2) => (c1.codice ?? '').localeCompare(c2.codice ?? '')).slice(0, 10);
          return templatesPadreFiltrati.length > 0 ? of(templatesPadreFiltrati) : this.getTemplatesPadre(searchText).pipe(
            map(term =>
              term && term.templateReport && term.templateReport.length > 0 ? term.templateReport : []
            )
          );
        }
      })
    );
  }

  private getEnti(searchTerm?: string): Observable<EntiResponse> {
    const payload = this.filterEnti(searchTerm);
    return this.entiService.getEnti(JSON.stringify(payload));
  }

  getTipiPratiche() {

    if (this.enteSelezionato) {
      this.tipoPraticheService.listTipiPraticaByEnte(this.enteSelezionato?.id)
        .subscribe(response => this.tipiPratiche = response);

    } else {
      this.tipiPratiche = [];
      this.tipoPraticaSelezionata = null;
    }
  }

  private filterEnti(searchTerm?: string) {
    const f: any = {
      filter: {},
      page: 0,
      fields: 'id,nome,',
      sort: 'nome'
    };
    if (searchTerm?.length) {
      f.filter.nome = {
        ci: searchTerm
      };
    }
    return f;
  }

  private start() {
    forkJoin({
      enti: this.getEnti(),
      maxFileSize: this.configurazioniService.getConfigurazioneByChiave(
        Constants.PARAMETRI.TEMPLATE_REPORT.TEMPLATE_MAX_SIZE),
      templates: this.getTemplatesPadre('')
    }).subscribe(
      result => {
        this.templatesPadre = result.templates.templateReport ?? [];
        this.templatesPadreFormatter();
        this.enti = result.enti.enti ?? [];
        this.maxSize = +(result.maxFileSize ?? 0);
        this.loading--;
        this.loadingError = null;
        this.loaded = true;
      });
  }

  private getTemplatesPadre(searchText: string) {
    const payload = {
      fields: 'codice',
      ...this.getFilterPayload(searchText),
    };
    return this.templateService.getTemplates(JSON.stringify(payload));
  }

  private getFilterPayload(searchText: string): any {
    const output: any = {
      filter: {},
    };

    output.filter.idEnte = {
      defined: false
    };
    if (this.enteSelezionato) {
      output.filter.idEnte.eq = this.enteSelezionato.id;
    }

    if (searchText) {
      output.filter.codice = {
        ci: searchText
      };
    }

    return output;
  }

  tornaIndietro() {
    window.history.back();
  }

  onFileChanged(event: any) {
    if (!event || event.length === 0) {
      this.setErrorFile(this.translateService.instant('errori.campo_obbligatorio'));
      return;
    }

    const ext = event[0].name.split('.').pop() as string;


    if (event[0].type || !'jrxml'.match(ext.toLocaleLowerCase())) {
      this.setErrorFile(this.translateService.instant('errori.formato_file_non_valido'));
      return;
    }

    if (event[0].size > this.maxSize) {
      this.setErrorFile(this.translateService.instant('errori.grandezza_upload_file_superata')
        .replace(/{{(.*?)}}/, this.maxSize / 1024));
      return;
    }
    const file = event[0];

    const reader = new FileReader();
    reader.readAsDataURL(file);

    reader.onload = (x) => {
      if (x.target?.result) {
        this.templateSorgente  = Utils.getEncodedBase64ValueFromArrayBufferString(x.target?.result.toString());
      }
    };
    this.errore = null;
    this.nomeFile = event[0].name;
    this.file = event[0];
    this.uploadedSorgenteTemplate = undefined;
  }

  private setErrorFile(errore: string) {
    this.nomeFile = null;
    this.file = null;
    this.errore = errore;
  }

  disabilitaSalva() {
    if ((this.file || this.uploadedSorgenteTemplate) && this.codice) {
      return false;
    }
    return true;
  }

  salva() {
    const payload: CreaTemplateRequest = {
      codice: this.codice ?? '',
      sorgenteTemplate: this.templateSorgente as string,
    };

    if (this.enteSelezionato && this.enteSelezionato.id ) {
      payload.idEnte = this.enteSelezionato.id;
    }

    if (this.tipoPraticaSelezionata ) {
      payload.codiceTipoPratica = this.tipoPraticaSelezionata.codice;
    }

    if (this.templatePadreSelezionato) {
      payload.codiceTemplatePadre = this.templatePadreSelezionato.codice;
    }

    if (this.idTemplate) {
      this.templateService.update(this.idTemplate, payload).subscribe(result => {
        if (result) {
          delay(environment.httpMockDelay);
          let messaggio = this.translateService.instant('template_report.dialogs.modificato.messaggio');
          messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.codice ?? '']);
          this.modalService.info(this.translateService.instant(
            'template_report.dialogs.modificato.titolo'), messaggio).then(
              () => {
                this.tornaIndietro();
              }, err => {
                this.modalService.simpleError(Utils.toMessage(err), err.error.errore);
          });
        }
      });
    } else {
      this.templateService.create(payload).subscribe(result => {
        if (result) {
          delay(environment.httpMockDelay);
          let messaggio = this.translateService.instant('template_report.dialogs.creato.messaggio');
          messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.codice ?? '']);
          this.modalService.info(this.translateService.instant(
            'template_report.dialogs.creato.titolo'), messaggio).then(
              () => {
                this.tornaIndietro();
              }, err => {
                this.modalService.simpleError(Utils.toMessage(err), err.error.errore);
          });
        }
      });
    }
  }

  hasValue(name: any): boolean {
    return Utils.isNotBlank(name);
  }

  downloadTemplate() {
    const element = document.createElement('a');
    element.setAttribute('style', 'display: none');
    const blob = Utils.getEncodeBlobValueFromBase64String(this.uploadedSorgenteTemplate, 'application/jrxml', '');
    const url = URL.createObjectURL(blob);

    element.download = this.templateName;
    element.href = url;
    document.body.appendChild(element);
    element.click();
    setTimeout(() => {
      window.URL.revokeObjectURL(element.href);
      element.remove();
    }, 100);
  }

  updateTemplatePadre(deleteSelection: boolean = false) {
    if (deleteSelection) {
      this.templatePadreSelezionato = null;
    }
    this.getTemplatesPadre('').subscribe(x => {
      this.templatesPadre = x && x.templateReport ? x.templateReport : [];
      this.templatesPadreFormatter();
    });
  }

  private templatesPadreFormatter() {
    this.templatesPadre = this.templatesPadre.filter((v, i) => this.templatesPadre.findIndex(item => item.codice === v.codice) === i);
    this.templatesPadre = this.templatesPadre.filter(o => o.codice !== this.codice);
  }

}
