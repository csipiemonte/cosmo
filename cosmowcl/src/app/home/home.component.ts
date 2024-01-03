/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnDestroy, OnInit, Type } from '@angular/core';
import { WidgetConfig } from '../shared/models/widget-config.model';
import { WidgetsService } from './widgets/widgets.service';
import { CalendarioComponent } from './widgets/calendario/calendario.component';
import { NotificheComponent } from './widgets/notifiche/notifiche.component';
import { ButtonsPraticheComponent } from './widgets/buttons-pratiche/buttons-pratiche.component';
import { TabPanelComponent } from './widgets/tab-panel/tab-panel.component';
import { PreferenzeUtenteService } from '../shared/services/preferenze-utente.service';
import { ValorePreferenzeUtente } from '../shared/models/preferenze/valore-preferenze-utente.model';
import { Subscription } from 'rxjs';
import { RouterHistoryService } from '../shared/services/router-history.service';
import { PreferenzeWidgets } from '../shared/models/preferenze/preferenze-widgets.model';
import { CardsPraticheComponent } from './widgets/cards-pratiche/cards-pratiche.component';
import { PreferenzeEnteService } from '../shared/services/preferenze-ente.service';
import { ValorePreferenzeEnte } from '../shared/models/preferenze/valore-preferenze-ente.model';




@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  widgets: WidgetConfig[] = [];
  widgetEnte: string[] = [];
  modificabile = true;
  private preferenzeUtenteSubscription: Subscription | null = null;


  constructor(
    private widgetsService: WidgetsService,
    private preferenzeUtenteService: PreferenzeUtenteService,
    private routerHistoryService: RouterHistoryService) {

    const lastwidget = localStorage.getItem('widgets');
    if (lastwidget){
      const lastWidgetParsed = JSON.parse(lastwidget);
      if (lastWidgetParsed){
        if (lastWidgetParsed?.widgets){
          this.widgetEnte = lastWidgetParsed.widgets ?? [];
        }
        if (lastWidgetParsed?.modificabile !== null && lastWidgetParsed?.modificabile !== undefined ){
          this.modificabile = lastWidgetParsed.modificabile ?? true;
          this.widgetsService.areModificabili = this.modificabile;
        }
      }
    }
    this.preferenzeUtenteSubscription = this.preferenzeUtenteService.subscribePreferenze.subscribe(preferenze => {
      this.widgets = [];
      if (preferenze && preferenze.valore) {
        const valorePreferenze = JSON.parse(preferenze.valore) as ValorePreferenzeUtente;
        if (valorePreferenze.home && valorePreferenze.home.widgets && valorePreferenze.home.widgets.length > 0) {

          if (this.modificabile){

            valorePreferenze.home.widgets.forEach(widget => {
              const cmpt = this.widgetsService.getWidget(widget.nome);
              const colsMin = widget.colsMin ?? widget.cols;
              const colsMax = widget.colsMax ?? widget.cols;
              const descrizione = widget.descrizione ?? this.widgetsService.setDescrizione(widget.nome);
              if (cmpt){
                this.widgets.push({ widget: cmpt, colsMin, colsMax, enabled: widget.attivo, name: widget.nome, descrizione });
              }
            });

            this.widgetEnte.forEach(
              widgetEnte => {
                if (!this.widgets.find(widget => widget.name === widgetEnte)){
                  const defaultWidget = this.widgetsService.defaultWidgets.find(elem => elem.name === widgetEnte) ?? null;
                  if (defaultWidget){
                    defaultWidget.enabled = false;
                    this.widgets.push(defaultWidget);
                  }
                }
              }
            );

          }else{

            valorePreferenze.home.widgets.forEach(widget => {
              const widgetEnte = this.widgetEnte.find(elem => elem === widget.nome);
              const cmpt = this.widgetsService.getWidget(widget.nome);
              const colsMin = widget.colsMin ?? widget.cols;
              const colsMax = widget.colsMax ?? widget.cols;
              const descrizione = widget.descrizione ?? this.widgetsService.setDescrizione(widget.nome);
              if (cmpt && ((widget.attivo && widgetEnte) || !widgetEnte)){
                this.widgets.push({ widget: cmpt, colsMin, colsMax, enabled: widgetEnte ? true : false, name: widget.nome, descrizione });
              }

            });

            this.widgetEnte.forEach(
              (widgetEnte, indice) => {
                if (!this.widgets.find(widget => widget.name === widgetEnte)){
                  const defaultWidget = this.widgetsService.defaultWidgets.find(elem => elem.name === widgetEnte) ?? null;
                  if (defaultWidget){
                    defaultWidget.enabled = true;
                    this.widgets = [...this.widgets.slice(0, indice), defaultWidget, ...this.widgets.slice(indice)];
                  }

                }
              }
            );

          }

          }


        if (this.widgets.length === 0  || (!this.modificabile && this.widgets.filter(elem => elem.enabled === true).length === 0)) {
          this.creaDefaultWidget();
        }
      } else {
        this.creaDefaultWidget();
      }
      this.widgetsService.widgets = this.widgets;
    });
  }

  ngOnInit(): void {

  }

  ngOnDestroy(): void {
    if (this.preferenzeUtenteSubscription) {
      this.preferenzeUtenteSubscription.unsubscribe();
    }

  }



  creaDefaultWidget() {
      if (this.widgetEnte && this.widgetEnte.length > 0){
        this.widgetEnte.forEach(
          widget => {
            const elem = this.widgetsService.defaultWidgets.find(w => w.name === widget) ?? null;
            if (elem){
              elem.enabled = true;
              this.widgets.push(elem);
            }
          }
        );

        this.widgetsService.defaultWidgets.forEach(
          widget => {
           if (!this.widgetEnte.find(elem => elem === widget.name)){
            widget.enabled = false;
            this.widgets.push(widget);
           }
          }
        );

      }else{
        this.widgets.push(...this.widgetsService.defaultWidgets);
      }

  }


}
