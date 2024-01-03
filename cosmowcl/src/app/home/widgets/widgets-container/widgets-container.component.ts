/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, Input, Type, ViewContainerRef, ComponentFactoryResolver, Directive, OnDestroy } from '@angular/core';
import { WidgetsService } from '../widgets.service';
import { WidgetConfig } from 'src/app/shared/models/widget-config.model';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { BreakpointObserver, Breakpoints, BreakpointState } from '@angular/cdk/layout';
import { NGXLogger } from 'ngx-logger';
import { PreferenzeUtenteService } from 'src/app/shared/services/preferenze-utente.service';
import { ValorePreferenzeUtente } from 'src/app/shared/models/preferenze/valore-preferenze-utente.model';
import { PreferenzeWidgets } from 'src/app/shared/models/preferenze/preferenze-widgets.model';

@Directive({
  selector: '[appWidgetsContainer]',
})
export class WidgetsContainerDirective implements OnInit {

  @Input() widgetComponent?: Type<any>;
  @Input() widgetSize?: number;

  constructor(private vc: ViewContainerRef,
              private widgetsService: WidgetsService) { }

  ngOnInit(): void {
    if (this.widgetSize && this.widgetComponent) {
      this.widgetsService.registerContainer(this.vc, this.widgetComponent);
    }

  }
}

@Component({
  selector: 'app-widgets-container',
  templateUrl: './widgets-container.component.html',
  styleUrls: ['./widgets-container.component.scss']
})
export class WidgetsContainerComponent implements OnInit, OnDestroy {

  private vc?: ViewContainerRef;
  widgets: WidgetConfig[] = [];

  enabledWidgetsMatrix: WidgetConfig[][] = [];
  firstElement: WidgetConfig[] = [];
  disabledWidgetsMatrix: WidgetConfig[][] = [];

  modificabile = true;
  editMode = false;
  mediumOrMobilePortrait = false;

  constructor(
    private factoryResolver: ComponentFactoryResolver,
    private widgetsService: WidgetsService,
    public breakpointObserver: BreakpointObserver,
    private logger: NGXLogger,
    private preferenzeUtenteService: PreferenzeUtenteService
  ) {

  }

  ngOnDestroy(): void {
    if (this.vc) {
      this.widgetsService.destroyContainer(this.vc);
    }
  }

  ngOnInit(): void {
    this.widgets = this.widgetsService.widgets;
    this.modificabile = this.widgetsService.areModificabili;

    if (this.modificabile === false){
      const preferenzeSalvate = this.preferenzeUtenteService.getPreferenzeSalvate();
      if (preferenzeSalvate && preferenzeSalvate.valore) {

        const valorePreferenze = JSON.parse(preferenzeSalvate.valore) as ValorePreferenzeUtente;
        if (valorePreferenze.home && valorePreferenze.home.widgets 
          && this.checkChangedValues(valorePreferenze.home.widgets, this.widgets)) {
          const preferenzeDaSalvare: PreferenzeWidgets[] = [];
          this.widgets.forEach(widget => {
            preferenzeDaSalvare.push({ attivo: widget.enabled, colsMin: widget.colsMin, colsMax: widget.colsMax,
              nome: widget.name, descrizione: widget.descrizione });
          });
          valorePreferenze.home.widgets = preferenzeDaSalvare;
          preferenzeSalvate.valore = JSON.stringify(valorePreferenze);
          this.preferenzeUtenteService.aggiornaSenzaModale(preferenzeSalvate);
        }


      }
    }

    this.widgetsService.onContainerCreated((container: ViewContainerRef, component: Type<any>) => {
      this.vc = container;
      const factory = this.factoryResolver.resolveComponentFactory(component);
      if (this.vc) {
        this.vc.createComponent(factory);
      }
    });

    this.widgetsService.onContainerDestroyed((container: ViewContainerRef) => {
      this.vc = container;
      if (this.vc) {
        this.vc.clear();
      }
    });

    this.organizeEnabledWidgets(this.widgets);
    this.organizeDisabledWidgets(this.widgets);

    this.breakpointObserver
      .observe([Breakpoints.XSmall, Breakpoints.Small, Breakpoints.Medium, Breakpoints.HandsetPortrait])
      .subscribe((state: BreakpointState) => {
        if (state.matches) {
          this.logger.debug(
            'Matched small viewport or handset in portrait mode'
          );
          this.mediumOrMobilePortrait = true;
        }
      });
    this.breakpointObserver
      .observe([Breakpoints.Large, Breakpoints.XLarge])
      .subscribe((state: BreakpointState) => {
        if (state.matches) {
          this.logger.debug(
            'Matched large viewport'
          );
          this.mediumOrMobilePortrait = false;
        }
      });
  }

  checkChangedValues(preferenze: PreferenzeWidgets[], widgets: WidgetConfig[]) {
    if (preferenze.length !== widgets.length){
      return true;
    }
    for (let index = 0; index < preferenze.length; index++){
      if (preferenze[index].nome !== widgets[index].name || preferenze[index].attivo !== widgets[index].enabled){
        return true;
      }
    }
    return false;
  }


  dropAbilitato(event: CdkDragDrop<string[]>, primoRiempimento: boolean) {
    this.drop(event);

    this.widgets = [];
    this.pushWidgets(this.enabledWidgetsMatrix, primoRiempimento, true);

    for (const row of this.disabledWidgetsMatrix) {
      for (const widget of row) {
        this.widgets.push(widget);
      }
    }

    this.organizeWidgets();
  }

  dropDisabilitato(event: CdkDragDrop<string[]>, primoRiempimento: boolean) {
    this.drop(event);

    this.widgets = [];
    this.pushWidgets(this.disabledWidgetsMatrix, primoRiempimento, false);

    for (const row of this.enabledWidgetsMatrix) {
      for (const widget of row) {
        this.widgets.push(widget);
      }
    }
    this.organizeWidgets();
  }


  drop(event: CdkDragDrop<string[]>) {

    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
    }
  }

  private pushWidgets(widgetsConfig: WidgetConfig[][], primoRiempimento: boolean, fromEnableToDisable: boolean){
    if (primoRiempimento){
      this.pushWidget(this.firstElement, fromEnableToDisable);
      this.firstElement = [];
    } else{

      for (const row of widgetsConfig) {
        this.pushWidget(row, fromEnableToDisable);
      }
    }
  }

  private pushWidget(widgets: WidgetConfig[], fromEnableToDisable: boolean) {
    for (const row of widgets) {
      if (fromEnableToDisable){
        if (!row.enabled) {
          row.enabled = true;
        }
      } else {
        if (row.enabled) {
          row.enabled = false;
        }
      }
      this.widgets.push(row);
    }
  }


  toggleEditMode() {
    this.editMode = !this.editMode;

    if (!this.editMode) {

      const preferenzeDaSalvare: PreferenzeWidgets[] = [];
      this.widgets.forEach(widget => {
        preferenzeDaSalvare.push({ attivo: widget.enabled, colsMin: widget.colsMin, colsMax: widget.colsMax,
          nome: widget.name, descrizione: widget.descrizione });
      });

      let preferenzeSalvate = this.preferenzeUtenteService.getPreferenzeSalvate();
      if (preferenzeSalvate && preferenzeSalvate.valore) {
        const valorePreferenze = JSON.parse(preferenzeSalvate.valore) as ValorePreferenzeUtente;
        if (valorePreferenze.home) {
          valorePreferenze.home.widgets = preferenzeDaSalvare;
          preferenzeSalvate.valore = JSON.stringify(valorePreferenze);
        } else {
          valorePreferenze.home = { widgets: preferenzeDaSalvare };
          preferenzeSalvate.valore = JSON.stringify(valorePreferenze);
        }
        this.preferenzeUtenteService.aggiorna(preferenzeSalvate);

      } else {
        preferenzeSalvate = { versione: '1.0', valore: JSON.stringify({ home: { widgets: preferenzeDaSalvare } }) };
        this.preferenzeUtenteService.salva(preferenzeSalvate);
      }
    }
  }

  private organizeWidgets(){
    this.enabledWidgetsMatrix = [];
    this.organizeEnabledWidgets(this.widgets);

    this.disabledWidgetsMatrix = [];
    this.organizeDisabledWidgets(this.widgets);

  }

  private organizeEnabledWidgets(widgets: WidgetConfig[]): void {
    let row = 0;
    for (const widget of widgets) {
      if (!widget.enabled) {
        continue;
      }

      if (!this.enabledWidgetsMatrix[row]) {
        const widgetsRow: WidgetConfig[] = [];
        this.enabledWidgetsMatrix.push(widgetsRow);
      }
      let colSum = 0;
      for (const existingWidget of this.enabledWidgetsMatrix[row]) {
        colSum += existingWidget.colsMin;
      }
      if (colSum + widget.colsMin > 12) {
        row++;
        const widgetsRow: WidgetConfig[] = [];
        this.enabledWidgetsMatrix.push(widgetsRow);
      }
      this.enabledWidgetsMatrix[row].push(widget);
    }
    this.logger.debug('widgetsMatrix', this.enabledWidgetsMatrix);
  }

  private organizeDisabledWidgets(widgets: WidgetConfig[]): void {
    let row = 0;
    for (const widget of widgets) {
      if (widget.enabled) {
        continue;
      }

      if (!this.disabledWidgetsMatrix[row]) {
        const widgetsRow: WidgetConfig[] = [];
        this.disabledWidgetsMatrix.push(widgetsRow);
      }
      let colSum = 0;
      for (const existingWidget of this.disabledWidgetsMatrix[row]) {
        colSum += existingWidget.colsMin;
      }
      if (this.disabledWidgetsMatrix.length > 6) {
        row++;
        const widgetsRow: WidgetConfig[] = [];
        this.disabledWidgetsMatrix.push(widgetsRow);
      }
      this.disabledWidgetsMatrix[row].push(widget);
    }
    this.logger.debug('widgetsMatrix', this.disabledWidgetsMatrix);
  }
  getWidgetClass(widget: WidgetConfig): string {
    let classList = '';
    if (this.mediumOrMobilePortrait) {
      classList = 'drag-element-md col';
    } else {
      const widgetsArray = this.enabledWidgetsMatrix.find(array => array.find(elem => elem.name === widget.name)) ?? [];
      if (widgetsArray.length === 1){
        classList = 'drag-element col-lg-' + widget.colsMax;
      }else{
        classList = 'drag-element col-lg-' + widget.colsMin;
      }
    }
    return classList;
  }

  disabilitaWidget(widget: WidgetConfig){
    widget.enabled = !widget.enabled;

    this.organizeWidgets();
  }
}
