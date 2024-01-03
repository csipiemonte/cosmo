/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Type } from '@angular/core';

import {
  ApprovazioneComponent,
} from 'src/app/form-logici/approvazione/approvazione.component';
import { AssegnazioneTagsComponent } from 'src/app/form-logici/assegnazione-tags/assegnazione-tags.component';
import {
  AssociazionePraticheComponent,
} from 'src/app/form-logici/associazione-pratiche/associazione-pratiche.component';
import {
  AttivaSistemaEsternoComponent,
} from 'src/app/form-logici/attiva-sistema-esterno/attiva-sistema-esterno.component';
import {
  CollaborazioneComponent,
} from 'src/app/form-logici/collaborazione/collaborazione.component';
import {
  CommentiFormLogiciComponent,
} from 'src/app/form-logici/commenti/commenti.component';
import {
  ConsultazioneDocumentiFormLogiciComponent,
} from 'src/app/form-logici/consultazione-documenti/consultazione-documenti.component';
import {
  CreazionePraticaComponent,
} from 'src/app/form-logici/creazione-pratica/creazione-pratica.component';
import {
  CustomFormComponent,
} from 'src/app/form-logici/custom-form/custom-form.component';
import {
  FirmaDocumentiComponent,
} from 'src/app/form-logici/firma-documenti/firma-documenti.component';
import {
  GenerazioneReportComponent,
} from 'src/app/form-logici/generazione-report/generazione-report.component';
import {
  GestioneDocumentiComponent,
} from 'src/app/form-logici/gestione-documenti/gestione-documenti.component';
import {
  PianificazioneAttivitaComponent,
} from 'src/app/form-logici/pianificazione-attivita/pianificazione-attivita.component';
import { SceltaComponent } from 'src/app/form-logici/scelta/scelta.component';
import { SimpleFormComponent } from 'src/app/form-logici/simple-form/simple-form.component';
import { NomeFunzionalita } from '../../enums/nome-funzionalita';

export class FormLogiciConfig {
  static readonly CONSULTAZIONE_DOCUMENTI = new FormLogiciConfig(ConsultazioneDocumentiFormLogiciComponent, 'docs', 'Consultazione documenti',
  '#docs', 'docs-tab', NomeFunzionalita.CONSULTAZIONE_DOCUMENTI, false);
  static readonly APPROVAZIONE = new FormLogiciConfig(ApprovazioneComponent, 'approvazione', 'Approvazione',
  '#approvazione', 'approvazione-tab', NomeFunzionalita.APPROVAZIONE, true);
  static readonly PIANIFICAZIONE_ATTIVITA = new FormLogiciConfig(PianificazioneAttivitaComponent, 'pianificazione', 'Pianificazione attività',
  '#pianificazione', 'pianificazione-tab', NomeFunzionalita.PIANIFICAZIONE_ATTIVITA, true);
  static readonly GESTIONE_DOCUMENTI = new FormLogiciConfig(GestioneDocumentiComponent, 'managedocs', 'Gestione documenti',
  '#managedocs', 'managedocs-tab', NomeFunzionalita.GESTIONE_DOCUMENTI, false);
  static readonly COMMENTI = new FormLogiciConfig(CommentiFormLogiciComponent, 'commenti', 'Commenti',
  '#commenti', 'commenti-tab', NomeFunzionalita.COMMENTI, false);
  static readonly COLLABORAZIONE = new FormLogiciConfig(CollaborazioneComponent, 'collabora', 'Collabora',
  '#collabora', 'collabora-tab', NomeFunzionalita.COLLABORAZIONE, false);
  static readonly SIMPLE_FORM = new FormLogiciConfig(SimpleFormComponent, 'simple-form', 'Form',
  '#simple-form', 'simple-form-tab', NomeFunzionalita.SIMPLE_FORM, true);
  static readonly FIRMA_DOCUMENTI = new FormLogiciConfig(FirmaDocumentiComponent, 'firma-documenti', 'Firma documenti',
  '#firma-documenti', 'firma-documenti-tab', NomeFunzionalita.FIRMA_DOCUMENTI, true);
  static readonly ATTIVA_SISTEMA_ESTERNO = new FormLogiciConfig(AttivaSistemaEsternoComponent, 'attiva', 'Attivazione sistema esterno',
  '#attiva', 'attiva-tab', NomeFunzionalita.ATTIVA_SISTEMA_ESTERNO, false);
  static readonly ASSOCIAZIONE_PRATICHE = new FormLogiciConfig(AssociazionePraticheComponent, 'associazione pratiche', 'Associazione pratiche',
  '#associa-pratiche', 'associa-pratiche-tab', NomeFunzionalita.ASSOCIAZIONE_PRATICHE, false);
  static readonly GENERAZIONE_REPORT = new FormLogiciConfig(GenerazioneReportComponent, 'generazione-report', 'Generazione report',
  '#generazione-report', 'generazione-report', NomeFunzionalita.GENERAZIONE_REPORT, false);
  static readonly CUSTOM_FORM = new FormLogiciConfig(CustomFormComponent, 'custom-form', 'Custom form',
  '#custom-form', 'custom-form-tab', NomeFunzionalita.CUSTOM_FORM, true);
  static readonly CREAZIONE_PRATICA = new FormLogiciConfig(CreazionePraticaComponent, 'creazione-pratica', 'Creazione pratica',
  '#creazione-pratica', 'creazione-pratica-tab', NomeFunzionalita.CREAZIONE_PRATICA, true);
  static readonly SCELTA = new FormLogiciConfig(SceltaComponent, 'scelta', 'Scelta',
  '#scelta', 'scelta-tab', NomeFunzionalita.SCELTA, true);
  static readonly ASSEGNAZIONE_TAGS = new FormLogiciConfig(AssegnazioneTagsComponent, 'assegnazione-tags', 'Assegnazione tags',
  '#assegnazione-tags', 'assegnazione-tags-tab', NomeFunzionalita.ASSEGNAZIONE_TAGS, false);

  constructor(
    // componente da visualizzare
    public readonly formLogico: Type<any>,
    // descrizione del tab (deve essre uguale a ref senza #)
    public description: string,
    // titolo del tab
    public title: string,
    // riferimento del tab (deve essere uguale a description con #)
    public ref: string,
    // id del tab
    public id: string,
    // nome della funzionalita' sul database
    public readonly tabName: string,
    // se il componente deve restituire delle variabili da inviare a flowable
    public readonly returnOutput: boolean,
    // se il componente riguarda una funzionalità con multi istanza per form logico
    public readonly multiIstanza: boolean = false) { }

}
