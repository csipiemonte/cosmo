/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  Component,
  OnInit,
  ViewChild,
} from '@angular/core';
import { Router } from '@angular/router';

import {
  forkJoin,
  merge,
  Observable,
  of,
  Subject,
} from 'rxjs';
import {
  debounceTime,
  distinctUntilChanged,
  filter,
  map,
  switchMap,
} from 'rxjs/operators';
import { Constants } from 'src/app/shared/constants/constants';
import { Ente } from 'src/app/shared/models/api/cosmoauthorization/ente';
import {
  EntiResponse,
} from 'src/app/shared/models/api/cosmoauthorization/entiResponse';
import {
  ConfigurazioniService,
} from 'src/app/shared/services/configurazioni.service';
import { EntiService } from 'src/app/shared/services/enti.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { Utils } from 'src/app/shared/utilities/utilities';

import { NgbTypeahead } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';

import {
  FieldConflictResolutionInput,
  ImportMessage,
  ImportResponse,
  ProcessiService,
} from './processi.service';

@Component({
  selector: 'app-importa-processo',
  templateUrl: './importa-processo.component.html',
  styleUrls: ['./importa-processo.component.scss'],
  providers: [ProcessiService]
})
export class ImportaProcessoComponent implements OnInit {

  constructor(
    private modalService: ModalService,
    private translateService: TranslateService,
    private configurazioniService: ConfigurazioniService,
    private entiService: EntiService,
    private processiService: ProcessiService,
    private router: Router) { }

  @ViewChild('instanceEnti', { static: true }) instanceEnti!: NgbTypeahead;
  focusEnti$ = new Subject<string>();
  clickEnti$ = new Subject<string>();

  nomeFile: string | null = null;
  file: File | null = null;
  maxSize!: number;
  errore: string | null = null;

  erroreEnte: string | null = null;
  erroreTipoPratica: string | null = null;

  enti: Ente[] = [];
  enteSelezionato: Ente | null = null;

  conflicts: LocalConflict[] = [];
  messages: ImportMessage[] = [];
  anteprimaOk = false;
  anteprimeCompletate = 0;

  formatterEnte = (result: Ente) => result.nome;

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

  ngOnInit(): void {
    this.start();
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

    if (event[0].type || !'cosmo'.match(ext.toLocaleLowerCase())) {
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
    this.anteprimaOk = false;
    this.conflicts = [];
    this.messages = [];
  }

  private setErrorFile(errore: string) {
    this.nomeFile = null;
    this.file = null;
    this.errore = errore;
  }

  checkEnte() {
    if (!this.enteSelezionato) {
      this.erroreEnte = this.translateService.instant('errori.campo_obbligatorio_valore_non_valido');
    }
  }

  tornaIndietro() {
    window.history.back();
  }

  pulisciCampi() {
    this.enteSelezionato = null;
    this.nomeFile = null;
    this.file = null;
    this.errore = null;
    this.erroreEnte = null;
    this.anteprimaOk = false;
    this.conflicts = [];
    this.messages = [];
    this.start();
  }

  disabilitaAnteprima() {
    if (this.file
      && this.enteSelezionato && this.enteSelezionato.codiceIpa) {
      return false;
    }
    return true;
  }

  disabilitaSalva() {
    if (this.file
      && this.enteSelezionato && this.enteSelezionato.codiceIpa && this.anteprimaOk) {
      return false;
    }
    return true;
  }

  anteprima() {
    this.doImport(true);
  }

  importa() {
    if (!this.anteprimaOk) {
      this.modalService.simpleError('nice try').then(() => {}).catch(() => {});
      return;
    }
    this.doImport(false);
  }

  private doImport(preview: boolean) {
    if (
      !this.file
      || !this.enteSelezionato?.codiceIpa) {
      return;
    }

    const reader = new FileReader();
    reader.readAsDataURL(this.file);
    reader.onload = () => {
      const resStr = (reader.result as string).split(',')[1];
      const resolutionsInput: FieldConflictResolutionInput[] = this.conflicts.map(c => ({
        fullKey: c.fullKey,
        action: c.action!,
        acceptedValue: c.importValue,
      })).filter(e => !!e.action);

      this.processiService.importProcesso(
        this.enteSelezionato!.codiceIpa,
        preview,
        resolutionsInput,
        resStr
      ).subscribe(result => {
        this.parseImportResponse(result);
        this.anteprimeCompletate ++;
      }, failure => {
        this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore);
      });
    };
    reader.onerror = (error: ProgressEvent<FileReader>) => {
      this.modalService.simpleError(Utils.toMessage(error));
    };
  }

  private parseImportResponse(response: ImportResponse) {
    this.messages = (response.messages ?? []);

    if (response.conflicts?.length) {
      for (const conflict of response.conflicts) {
        if (!this.conflicts.some(c => c.fullKey === conflict.fullKey)) {
          // new conflict
          this.conflicts.push({
            ...conflict,
            acceptedValue: conflict.importValue,
            action: null,
          });
        }
      }
      this.modalService.info(
        'Conflitti',
        'Ci sono dei conflitti che richiedono una valutazione manuale. '
        + 'Per favore verifica i conflitti mostrati e decidi come procedere.'
      ).then(() => {}).catch(() => {});
      return;
    }

    if (response.preview) {
      this.anteprimaOk = true;
      this.modalService.info(
        'Procedura interrotta',
        'La procedura sarebbe andata a buon fine ma è stata interrotta perché in modalità di anteprima (' + (response.exitReason ?? 'MODE_PREVIEW') + '). ' +
        'Puoi ora procedere con l\'importazione effettiva.'
      ).then(() => {}).catch(() => {});
      return;
    }

    if (response.done) {
      this.anteprimaOk = false;
      this.modalService.info(
        'Procedura completata',
        'Importazione completata con successo (' + (response.exitReason ?? 'OK') + ')'
      ).catch(() => {}).then(() => {
        this.router.navigate(['amministrazione', 'gestione-tipologie-pratiche', 'modifica', response.tipoPratica]);
      });
      return;
    }

    this.anteprimaOk = false;

    this.modalService.error(
      'Procedura non completata',
      'Importazione non completata con successo: ' + JSON.stringify(response, null, 2)
    ).then(() => {}).catch(() => {});
  }

}

interface LocalConflict {
  existingValue: string;
  importValue: string;
  message: string;
  path: string;
  fullKey: string;
  fieldName: string;
  acceptedValue: string;
  action: string | null;
}
