/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { AfterViewInit, Component, Injector, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { CreaPraticaComponent } from 'src/app/shared/components/crea-pratica/crea-pratica.component';
import { PraticheAssociateComponent } from 'src/app/shared/components/pratiche-associate/pratiche-associate.component';
import { SelezioneEntity } from 'src/app/shared/components/ricerca-entity/ricerca-entity.component';
import { VariabileProcesso } from 'src/app/shared/models/api/cosmobusiness/variabileProcesso';
import { Fruitore } from 'src/app/shared/models/api/cosmonotifications/fruitore';
import { Utils } from 'src/app/shared/utilities/utilities';
import { FunzionalitaParentComponent } from '../funzionalita-parent.component';
import { FlowableOutput } from '../models/flowable-output-model';
import { TabBadge } from '../models/tab-status.models';

const OGGETTO_PRATICA_FIGLIA_KEY = 'oggetto_pratica_figlia';
const CODICE_TIPOLOGIA_PRATICA_FIGLIA_KEY = 'codice_tipologia_pratica_figlia';
const RIASSUNTO_PRATICA_FIGLIA_KEY = 'riassunto_pratica_figlia';
const API_MANAGER_ID_FRUITORE_PRATICA_FIGLIA_KEY = 'api_manager_id_fruitore_pratica_figlia';
const PRATICA_FIGLIA_CREATA_DA_FORM = 'pratiche_figlie_create_da_form';
const ID_DOCUMENTI_DA_DUPLICARE_KEY = 'id_documenti_da_duplicare';
const PRATICA_FIGLIA_DA_ASSOCIARE = 'pratica_figlia_da_associare';

@Component({
  selector: 'app-creazione-pratica',
  templateUrl: './creazione-pratica.component.html',
  styleUrls: ['./creazione-pratica.component.scss']
})
export class CreazionePraticaComponent extends FunzionalitaParentComponent implements OnInit, AfterViewInit {

  @ViewChild('associaPratiche') associa: PraticheAssociateComponent | null = null;
  @ViewChild('creaPratica') creaPratica: CreaPraticaComponent | null = null;

  praticaFigliaCreataDaForm = 0;
  oggettoPraticaFiglia: string | null = null;
  riassuntoPraticaFiglia: string | null = null;
  codiceTipologiaPraticaFiglia: string | null = null;
  apiManagerIdFruitorePraticaFiglia: string | null = null;
  idDocsDaDuplicare: number[] = [];
  daAssociare = false;

  constructor(
    public injector: Injector,
    private translateService: TranslateService,
  ) {
    super(injector);
  }

  ngOnInit() {
    this.oggettoPraticaFiglia = this.getInitialValue(OGGETTO_PRATICA_FIGLIA_KEY)
      ? '' + this.getInitialValue(OGGETTO_PRATICA_FIGLIA_KEY) : null;
    this.codiceTipologiaPraticaFiglia = this.getInitialValue(CODICE_TIPOLOGIA_PRATICA_FIGLIA_KEY)
        ? '' + this.getInitialValue(CODICE_TIPOLOGIA_PRATICA_FIGLIA_KEY) : this.pratica.tipo?.codice ?? null;
    this.riassuntoPraticaFiglia = this.getInitialValue(RIASSUNTO_PRATICA_FIGLIA_KEY)
      ? '' + this.getInitialValue(RIASSUNTO_PRATICA_FIGLIA_KEY) : null;
    this.apiManagerIdFruitorePraticaFiglia = this.getInitialValue(API_MANAGER_ID_FRUITORE_PRATICA_FIGLIA_KEY)
      ? '' + this.getInitialValue(API_MANAGER_ID_FRUITORE_PRATICA_FIGLIA_KEY) : null;
    this.praticaFigliaCreataDaForm = this.getInitialValue(PRATICA_FIGLIA_CREATA_DA_FORM)
      ? Number(this.getInitialValue(PRATICA_FIGLIA_CREATA_DA_FORM)) : 0;
    this.idDocsDaDuplicare = this.getInitialValue(ID_DOCUMENTI_DA_DUPLICARE_KEY)
      ? JSON.parse(JSON.stringify(this.getInitialValue(ID_DOCUMENTI_DA_DUPLICARE_KEY))) : [];
    this.daAssociare = this.getInitialValue(PRATICA_FIGLIA_DA_ASSOCIARE)
      ? Boolean(JSON.stringify(this.getInitialValue(PRATICA_FIGLIA_DA_ASSOCIARE))) : false;
  }

  ngAfterViewInit(): void {
    this.sendData({
      payload: this.creaVariabili(false),
    });
  }

  private getInitialValue(fieldKey: string) {
    const found = this.variabiliProcesso.find(variabile => fieldKey === variabile.name);
    return found && found.value ? found.value : null;
  }

  private resetValues() {
    this.oggettoPraticaFiglia = null;
    this.riassuntoPraticaFiglia = null;
    this.apiManagerIdFruitorePraticaFiglia = null;
    this.codiceTipologiaPraticaFiglia = this.pratica.tipo?.codice ?? null;
    this.idDocsDaDuplicare = [];
    this.daAssociare = false;
  }

  private creaVariabili(completamentoTask: boolean): FlowableOutput[] {
    const flowableOutput: FlowableOutput[] = [];

    const raw = this.creaPratica?.creaPraticaForm.getRawValue();
    flowableOutput.push({ name: OGGETTO_PRATICA_FIGLIA_KEY, value: completamentoTask ? null : raw.oggetto });
    flowableOutput.push({ name: CODICE_TIPOLOGIA_PRATICA_FIGLIA_KEY, value: completamentoTask ? null : raw.tipologia });
    flowableOutput.push({ name: RIASSUNTO_PRATICA_FIGLIA_KEY, value: completamentoTask ? null : Utils.jasperize(raw.riassunto) });

    const apiManagerId = this.creaPratica?.permettiSelezioneFruitore
      ? (raw.fruitore as SelezioneEntity<Fruitore>)?.entity?.apiManagerId : undefined;

    flowableOutput.push({ name: API_MANAGER_ID_FRUITORE_PRATICA_FIGLIA_KEY, value: completamentoTask ? null : apiManagerId });
    flowableOutput.push({ name: PRATICA_FIGLIA_CREATA_DA_FORM, value: completamentoTask ? null : this.praticaFigliaCreataDaForm });
    flowableOutput.push({ name: PRATICA_FIGLIA_DA_ASSOCIARE, value: completamentoTask ? false : raw.daAssociare });

    if (this.idDocsDaDuplicare.length > 0) {
      flowableOutput.push({ name: ID_DOCUMENTI_DA_DUPLICARE_KEY, value: completamentoTask ? null : this.idDocsDaDuplicare });
    } else {
      flowableOutput.push({ name: ID_DOCUMENTI_DA_DUPLICARE_KEY, value: null });
    }
    return flowableOutput;
  }

  private creaDocumentiDaDocumentiSelezionati(): number[] {
    this.idDocsDaDuplicare = [];

    const docs = this.creaPratica?.docs?.first.documentiSelezionati;
    if (docs) {
      docs.forEach(doc => {
        if (doc.id) {
          this.idDocsDaDuplicare.push(doc.id);
        }
      });
    }
    return this.idDocsDaDuplicare;
  }

  beforeSave() {
    this.creaDocumentiDaDocumentiSelezionati();

    this.sendData({
      payload: this.creaVariabili(false)
    });
  }

  beforeConfirm() {
    this.resetValues();

    this.sendData({
      payload: this.creaVariabili(true),
    });
  }

  public isValid(): boolean {
    return this.praticaFigliaCreataDaForm > 0;
  }

  public getBadges(): TabBadge[] | null {

    if (this.praticaFigliaCreataDaForm > 0) {

      const tooltipText = this.praticaFigliaCreataDaForm > 1
        ? this.translateService.instant('form_logici.creazione_pratica.pratiche_associate_create')
        : this.translateService.instant('form_logici.creazione_pratica.pratica_associata_creata');

      return [
        {
          class: 'info',
          text: this.praticaFigliaCreataDaForm.toString(),
          tooltip: this.praticaFigliaCreataDaForm.toString() + ' ' + tooltipText
        }
      ];
    }
    return null;
  }

  aggiornaPraticheAssociate() {

    this.praticaFigliaCreataDaForm++;

    this.resetValues();
    const variabili = this.creaVariabili(false);
    this.sendData({
      payload: variabili,
    });

    this.patchVariabili(this.buildVariabiliProcesso(variabili)).subscribe(variabiliResponse => {
      this.associa?.refresh();
    });
  }

  private buildVariabiliProcesso(variables: FlowableOutput[]): VariabileProcesso[] {
    const variabili: VariabileProcesso[] = [];

    variables.forEach(variable => {
      variabili.push({ name: variable.name, value: variable.value });
    });
    return variabili;
  }
}
