/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, ElementRef, EventEmitter, Input, OnInit, Output, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { delay, finalize } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { OperazioneAsincrona } from '../../models/api/cosmobusiness/operazioneAsincrona';
import { Pratica } from '../../models/api/cosmobusiness/pratica';
import { TipoPratica } from '../../models/api/cosmobusiness/tipoPratica';
import { OperazioneAsincronaWrapper } from '../../models/async';
import { AsyncTaskModalService } from '../../services/async-task-modal.service';
import { ModalService } from '../../services/modal.service';
import { Utils } from '../../utilities/utilities';
import { RicercaFruitoreComponent } from '../../components/ricerca-fruitore/ricerca-fruitore.component';
import { SelezioneEntity } from '../../components/ricerca-entity/ricerca-entity.component';
import { Fruitore } from '../../models/api/cosmoauthorization/fruitore';
import { forkJoin, of } from 'rxjs';
import { FruitoriService } from '../../services/fruitori.service';
import { CreazionePraticaService } from 'src/app/creazione-pratica/creazione-pratica.service';
import { ConsultazioneDocumentiComponent } from '../consultazione-documenti/consultazione-documenti.component';
import { CreaPraticaRequest } from '../../models/api/cosmobusiness/creaPraticaRequest';
import { FruitoriResponse } from '../../models/api/cosmoauthorization/fruitoriResponse';
import { GestioneTipiPraticheService } from 'src/app/administration/gestione-tipi-pratiche/gestione-tipi-pratiche.service';

@Component({
  selector: 'app-crea-pratica',
  templateUrl: './crea-pratica.component.html',
  styleUrls: ['./crea-pratica.component.scss']
})
export class CreaPraticaComponent implements OnInit {

  loading = 0;
  loadingError: any | null = null;
  creaPraticaForm !: FormGroup;
  listaTipoPratica: TipoPratica[] = [];

  permettiSelezioneFruitore = false;
  selezionaFruitore = false;


  @ViewChild('defaultfocus') defaultfocus: ElementRef | null = null;
  @ViewChild('ricercaFruitoreCtrl') ricercaFruitoreCtrl: RicercaFruitoreComponent | null = null;
  @ViewChildren('docs') docs: QueryList<ConsultazioneDocumentiComponent> | null = null;

  @Input() idPraticaEsistente: number | null = null;
  @Output() aggiornaPraticheAssociateEmitter: EventEmitter<void> = new EventEmitter<void>();

  @Input() oggetto: string | null = null;
  @Input() riassunto: string | null = null;
  @Input() codiceTipologiaPratica: string | null = null;
  @Input() apiManagerIdFruitore: string| null = null;
  @Input() idDocumentiSelezionati: number[] = [];
  @Input() daAssociare = false;

  fruitore: SelezioneEntity<Fruitore> | null = null;

  constructor(
    private router: Router,
    private creazionePraticaService: CreazionePraticaService,
    private modalService: ModalService,
    private translateService: TranslateService,
    private asyncTaskModalService: AsyncTaskModalService,
    private fruitoriService: FruitoriService,
    private tipiPraticaService: GestioneTipiPraticheService
  ) { }

  ngOnInit(): void {
    this.initForm();
    this.refresh();
  }

  refresh() {
    this.loading++;
    this.loadingError = null;

    forkJoin({
      tipi: this.tipiPraticaService.listTipiPraticaByEnte(undefined, true),
      fruitoriAssociati: environment.production ? of(null) : this.fruitoriService.getFruitori(this.createFilter()),
    }).pipe(
      delay(environment.httpMockDelay),
      finalize(() => {
        this.loading--;
      }),
    ).subscribe(data => {
      this.permettiSelezioneFruitore = (data.fruitoriAssociati?.pageInfo?.totalElements ?? 0) > 0;
      this.listaTipoPratica = data.tipi.filter(tipo => tipo.creabileDaInterfaccia === true);
      this.setPatchValue(this.listaTipoPratica, data.fruitoriAssociati);

      setTimeout(() => {
        if (this.defaultfocus) {
          this.defaultfocus.nativeElement.focus();
        }
      }, 1);
    }, failure => {
      this.loadingError = failure;
    });
  }

  private setPatchValue(tipiPratica: TipoPratica[], fruitoriAssociati: FruitoriResponse | null){
    if (this.creaPraticaForm){
      if (this.codiceTipologiaPratica){
        this.creaPraticaForm.patchValue({
          tipologia: this.codiceTipologiaPratica
        });
      } else{
        if (tipiPratica.length === 1) {
          this.creaPraticaForm.patchValue({
            tipologia: tipiPratica[0].codice
          });
        }
      }

      if (this.apiManagerIdFruitore && fruitoriAssociati?.fruitori?.length === 1) {
        this.selezionaFruitore = true;
        this.fruitore = {entity: fruitoriAssociati.fruitori[0] ?? null};

        this.creaPraticaForm.patchValue({
          fruitore: this.fruitore
        });
      }
    }
  }

  private initForm() {
    this.creaPraticaForm = new FormGroup({
      oggetto: new FormControl(this.oggetto, [Validators.required, this.doNotMatchRegexValidator]),
      riassunto: new FormControl(this.riassunto, []),
      fruitore: new FormControl(null, []),
      tipologia: new FormControl(null, [Validators.required]),
      daAssociare: new FormControl(this.daAssociare, [])
    });
  }

  private buildPratica(): CreaPraticaRequest {
    const raw = this.creaPraticaForm.getRawValue();
    return {
      codiceTipo: raw.tipologia,
      oggetto: raw.oggetto,
      riassunto: Utils.jasperize(raw.riassunto),
      codiceFruitore: this.selezionaFruitore ? (raw.fruitore as SelezioneEntity<Fruitore>)?.entity?.apiManagerId : undefined,
      idPraticaEsistente: this.idPraticaEsistente ?? undefined,
      documentiDaDuplicare: this.idPraticaEsistente ? this.creaIdDocumentiDaDuplicare() : undefined,
      daAssociare: raw.daAssociare
    };
  }

  creaIdDocumentiDaDuplicare(): Array<number>{
    const docs = this.docs?.first.documentiSelezionati;
    this.idDocumentiSelezionati = [];

    if (docs) {
      docs.forEach(doc => {
        if (doc.id) {
          this.idDocumentiSelezionati.push(doc.id);
        }
        if (doc.allegati){
          doc.allegati.forEach(allegato => {
            if (allegato.id){
              this.idDocumentiSelezionati.push(allegato.id);
            }
          });
        }
      });
    }
    return this.idDocumentiSelezionati.sort((a, b) => a - b);
  }

  doNotMatchRegexValidator(control: AbstractControl): { [key: string]: any } | null {
    const trimInput: string = control.value?.trim();
    return /(.*[\"\*\\\>\<\?\/\:\|]+.*)|(.*[\.]?.*[\.]+$)|(.*[ ]+$)/g.test(trimInput) ? { regexMatch: trimInput } : null;
  }

  onSubmit() {
    const pratica = this.buildPratica();

    this.creazionePraticaService.creaPratica(pratica).subscribe(result => {
      this.waitOperazioneAsincronaCreazionePratica(result);
    }, error => {
      Utils.voidPromise(
        this.modalService.error(
          this.translateService.instant('errori.creazione_pratica'),
          error.error && error.error.title ?
            error.error.title : this.translateService.instant('errori.creazione_pratica'), error.error.errore)

      );
    });
  }

  selectSizer() {
    const screenWidth = window.screen.width;
    if (screenWidth < 768) { return '100%'; }
    else { return this.creaPraticaForm?.controls.oggetto.touched && this.creaPraticaForm?.controls.oggetto.invalid ? '51%' : '100%'; }
  }

  private resetValues(){
    this.oggetto = null;
    this.riassunto = null;
    this.fruitore = null;
    this.selezionaFruitore = false;
    this.daAssociare = false;
  }

  private waitOperazioneAsincronaCreazionePratica(operazione: OperazioneAsincrona) {

    this.asyncTaskModalService.open({
      taskUUID: operazione.uuid
    })?.result.then((terminata: OperazioneAsincronaWrapper<Pratica>) => {

      if (this.idPraticaEsistente) {
        this.resetValues();
        this.initForm();
        this.setPatchValue(this.listaTipoPratica, null);
        this.docs?.first.mainTable?.uncheckAll();

        this.aggiornaPraticheAssociateEmitter.emit();
      } else{
        this.router.navigate(['pratica/' + terminata.risultato?.id]);
      }
    }, (fail: OperazioneAsincrona) => {
      Utils.voidPromise(
        this.modalService.error(
          this.translateService.instant('errori.creazione_pratica'),
          Utils.toMessage(fail.errore),
          fail.dettagliErrore
        )
      );
    });
  }

  abilitaSelezioneFruitore(): void {
    this.selezionaFruitore = true;
    setTimeout(() => {
      if (this.ricercaFruitoreCtrl) {
        this.ricercaFruitoreCtrl.focus();
      }
    }, 1);
  }

  annullaSelezioneFruitore(): void {
    this.selezionaFruitore = false;
    this.creaPraticaForm.patchValue({
      fruitore: null
    });
    this.fruitore = null;
  }

  private createFilter(): string {
    if (this.apiManagerIdFruitore){
     return JSON.stringify({ filter: { apiManagerId : {eq: this.apiManagerIdFruitore} }, size: 1 });
    } else{
      return JSON.stringify({ size: 1 });
    }

  }

}
