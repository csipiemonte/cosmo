/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { DatePipe } from '@angular/common';
import {
  Component,
  Input,
  OnInit,
} from '@angular/core';

import { ApexOptions } from 'apexcharts';
import * as moment from 'moment';
import {
  CosmoTableFormatter,
  ICosmoTableColumn,
  ICosmoTablePageRequest,
} from 'ngx-cosmo-table';
import { NGXLogger } from 'ngx-logger';
import { of } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { Pratica } from 'src/app/shared/models/api/cosmobusiness/pratica';
import {
  EventoStoricoPratica,
} from 'src/app/shared/models/api/cosmopratiche/eventoStoricoPratica';
import {
  StoricoPratica,
} from 'src/app/shared/models/api/cosmopratiche/storicoPratica';

import { StoricoAttivitaService } from './services/storico-attivita.service';
import { DateUtils } from '../../utilities/date-utils';

@Component({
  selector: 'app-storico-attivita',
  templateUrl: './storico-attivita.component.html',
  styleUrls: ['./storico-attivita.component.scss']
})
export class StoricoAttivitaComponent implements OnInit {
  public chartOptions: Partial<ApexOptions> | null = null;

  @Input() pratica!: Pratica;

  loading = 0;
  loadingError: any | null = null;

  storico: StoricoPratica | undefined = undefined;

  toggleEventi = 1;

  columns: ICosmoTableColumn[] = [
    { name: 'stato', label: 'stato', applyTemplate: true },
    {
      name: 'attivita', label: 'attività',
      field: 'attivita.nome',
      canHide: false,
      canSort: false
    },
    {
      name: 'inizio', label: 'inizio',
      formatters: [{
        formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy HH:mm:ss',
       }, {
        format: (raw: any) => raw ?? '--',
      }],
      valueExtractor: row => DateUtils.parse(row.inizio),
      canSort: true
    },
    {
      name: 'fine', label: 'fine',
      formatters: [{
        formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy HH:mm:ss',
       }, {
        format: (raw: any) => raw ?? '--',
      }],
      valueExtractor: row => DateUtils.parse(row.fine),
      canSort: true
    },
    { name: 'utenti', label: 'utenti coinvolti', applyTemplate: true, canSort: false },
    { name: 'gruppi', label: 'gruppi coinvolti', applyTemplate: true, canSort: false },
    {
      name: 'utente-esecutore', label: 'utente esecutore',
      valueExtractor: row => row.esecutore ? row.esecutore.nome + ' ' + row.esecutore.cognome : '--',
      canSort: false
    }];
  constructor(
    private logger: NGXLogger,
    private storicoAttivitaService: StoricoAttivitaService,
    private datePipe: DatePipe
  ) { }

  ngOnInit(): void {
    this.refresh();
  }

  refresh() {
    if (!this.pratica.id) {
      this.logger.warn('nessun id pratica');
      return;
    }

    this.loading++;
    this.loadingError = null;

    this.storicoAttivitaService.recuperaStoricoAttivitaPratica(this.pratica.id)
      .pipe(
        finalize(() => {
          this.loading--;
        })
      )
      .subscribe(storico => {
        this.storico = storico;
        this.chartOptions = storico.assegnazioni?.length ?  this.buildChart(storico) : null;
      }, failure => {
        this.loadingError = failure;
      });
  }

  dataProvider = (input: ICosmoTablePageRequest) => {
    const retrieved = this.storico?.attivita;

    return of({
      content: retrieved as any[],
      totalElements: retrieved?.length ?? 0,
    });
  }

  getBadgeClass(p: EventoStoricoPratica) {
    return 'primary';
  }

  getStatusText(p: EventoStoricoPratica) {
    return 'Cambio stato';
  }

  /*
   * docs: https://apexcharts.com/angular-chart-demos/timeline-charts/multi-series/
   * playground: https://codesandbox.io/s/apx-timeline-multi-series-toqxt?from-embed=&file=/src/app/app.component.ts
   */
  private buildChart(input: StoricoPratica): Partial<ApexOptions> {
    const series: any[] = [];

    const chart: Partial<ApexOptions> = {
      series: [
        {
          name: 'Bob',
          data: [
            {
              x: 'Design',
              y: [
                new Date('2019-03-05').getTime(),
                new Date('2019-03-08').getTime()
              ]
            },
            {
              x: 'Code',
              y: [
                new Date('2019-03-08').getTime(),
                new Date('2019-03-11').getTime()
              ]
            },
            {
              x: 'Test',
              y: [
                new Date('2019-03-11').getTime(),
                new Date('2019-03-16').getTime()
              ]
            }
          ]
        },
        {
          name: 'Joe',
          data: [
            {
              x: 'Design',
              y: [
                new Date('2019-03-02').getTime(),
                new Date('2019-03-05').getTime()
              ]
            },
            {
              x: 'Code',
              y: [
                new Date('2019-03-06').getTime(),
                new Date('2019-03-09').getTime()
              ]
            },
            {
              x: 'Test',
              y: [
                new Date('2019-03-10').getTime(),
                new Date('2019-03-19').getTime()
              ]
            }
          ]
        }
      ],
      chart: {
        // height: 350,
        type: 'rangeBar',
      },
      plotOptions: {
        bar: {
          horizontal: true,
        },
      },
      dataLabels: {
        enabled: true,
        formatter: (val: any) => {
          const a = moment(val[0]);
          const b = moment(val[1]);
          const diff = b.diff(a);
          if (diff < 1000) {
            return '';
          }
          const duration = moment.duration(diff);
          const years = duration.years();
          const days = duration.days();
          const months = duration.months();
          const hrs = duration.hours();
          const mins = duration.minutes();
          const secs = duration.seconds();
          let out = '';
          if (years > 0) {
            out += years + ' anni, ';
          }
          if (months > 0) {
            out += months + ' mesi, ';
          }
          if (!years) {
            if (days > 0) {
              out += days + ' giorni, ';
            }
            if (hrs > 0) {
              out += hrs + ' ore, ';
            }
            if (!days) {
              if (mins > 0) {
                out += mins + ' minuti, ';
              }
              if (!hrs) {
                if (secs > 0) {
                  out += secs + ' secondi, ';
                }
              }
            }
          }
          if (out.endsWith(', ')) {
            out = out.substr(0, out.length - 2);
          }
          return out;
        }
      },
      xaxis: {
        type: 'datetime'
      },
      yaxis: {

      },
      grid: {
        padding: {
          top: 100,
          bottom: 50,
        }
      },
      legend: {
        position: 'top',
        horizontalAlign: 'left'
      },
      annotations: {
        xaxis: [],
        yaxis: [],
        points: []
      },
    };

    // add events markers
    let offsetYtop = 0;
    let offsetYbottom = 0;
    let maxOffsetYtop = 0;
    let maxOffsetYbottom = 0;

    const getAnnotationOffsetYTop = () => {
      offsetYtop = offsetYtop + 25;
      if (offsetYtop > 100) {
        offsetYtop = 0;
      }
      if (offsetYtop > maxOffsetYtop) {
        maxOffsetYtop = offsetYtop;
      }
      return offsetYtop * -1;
    };

    const getAnnotationOffsetYBottom = () => {
      offsetYbottom = offsetYbottom + 25;
      if (offsetYbottom > 100) {
        offsetYbottom = 0;
      }
      if (offsetYbottom > maxOffsetYbottom) {
        maxOffsetYbottom = offsetYbottom;
      }
      return offsetYbottom + 50;
    };

    for (const e of input.eventi) {

      const buildEventAnnotation = (position: 'top' | 'bottom') => {
        const offsetY = position === 'top' ? getAnnotationOffsetYTop() : getAnnotationOffsetYBottom();
        const o = {
          x: new Date(e.timestamp).getTime(),
          borderColor: '#775DD0',
          label: {
            style: {
              color: '#333',
            },
            orientation: 'horizontal',
            textAnchor: 'start',
            offsetY,
            offsetX: 0,
            position,
            text: e.descrizione,
          },
          offsetY,
          strokeDashArray: 2,
        };
        return o;
      };

      if (e.tipo === 'PRATICA_AVVIATA') {
        const eventAnnotation = buildEventAnnotation('top');
        eventAnnotation.borderColor = '#111';
        eventAnnotation.label.style.color = '#222';
        chart.annotations?.xaxis?.push(eventAnnotation);

      } else if (e.tipo === 'PRATICA_COMPLETATA' || e.tipo === 'PRATICA_ANNULLATA') {
        const eventAnnotation = buildEventAnnotation('top');
        eventAnnotation.borderColor = '#111';
        eventAnnotation.label.style.color = '#222';
        eventAnnotation.label.textAnchor = 'end';
        chart.annotations?.xaxis?.push(eventAnnotation);

      } else if (e.tipo === 'PRATICA_CAMBIO_STATO') {
        const eventAnnotation = buildEventAnnotation('bottom');
        eventAnnotation.borderColor = '#444';
        eventAnnotation.label.style.color = '#555';
        chart.annotations?.xaxis?.push(eventAnnotation);
      }
    }

    // ricalcolo padding top e bottom
    if (chart?.grid?.padding?.top) {
      chart.grid.padding.top = maxOffsetYtop + 10;
    }

    if (chart?.grid?.padding?.bottom) {
      chart.grid.padding.bottom = maxOffsetYbottom + 25;
    }

    const findOrCreateUserSerie = (chiaveUtenteSerie: string) => {
      let userSerie = series.find(c => c.name === chiaveUtenteSerie);
      if (!userSerie) {
        userSerie = {
          name: chiaveUtenteSerie,
          data: []
        };
        series.push(userSerie);
      }
      return userSerie;
    };

    const findOrCreateGroupSerie = (chiaveGruppoSerie: string) => {
      let userSerie = series.find(c => c.name === chiaveGruppoSerie);
      if (!userSerie) {
        userSerie = {
          name: chiaveGruppoSerie,
          data: []
        };
        series.push(userSerie);
      }
      return userSerie;
    };

    for (const a of (input.assegnazioni ?? [])) {
      if (a.utente) {
        // trova o crea la serie associata all'utente
        const chiaveUtenteSerie = a.utente.nome + ' ' + a.utente.cognome;
        const userSerie = findOrCreateUserSerie(chiaveUtenteSerie);

        // trova o crea la riga attivita corrispondente
        const chiaveAttivita = a.attivita.nome;
        // let attivitaRiga = userSerie.data.find((c: any) => c.x === chiaveAttivita);
        // if (!attivitaRiga) {
        const attivitaRiga = {
            x: chiaveAttivita,
            y: [
              (a.inizio ? new Date(a.inizio).getTime() : null),
              ((a.fine ? new Date(a.fine) : new Date()).getTime())
            ]
          };
        // }
        userSerie.data.push(attivitaRiga);
      }

      if (a.gruppo) {
        // trova o crea la serie associata all'utente
        const chiaveGruppoSerie = a.gruppo.nome;
        const groupSerie = findOrCreateGroupSerie(chiaveGruppoSerie);

        // trova o crea la riga attivita corrispondente
        const chiaveAttivita = a.attivita.nome;
        const inizioPeriodo = a.inizio ? new Date(a.inizio) : new Date();
        const finePeriodo = a.fine ? new Date(a.fine) : new Date();

        // let attivitaRiga = userSerie.data.find((c: any) => c.x === chiaveAttivita);
        // if (!attivitaRiga) {
        const attivitaRiga = {
          x: chiaveAttivita,
          y: [inizioPeriodo.getTime(), finePeriodo.getTime()]
        };
        // }
        groupSerie.data.push(attivitaRiga);

        // trovo eventi durante questo periodo
        let inizioSegmento = inizioPeriodo;

        for (const evento of (input.eventi ?? [])) {
          // filtro solo gli eventi rilevanti
          if (evento.attivita?.id !== a.attivita.id) {
            continue;
          }
          if (
            evento.tipo !== 'ATTIVITA_COMPLETATA' &&
            evento.tipo !== 'ATTIVITA_ANNULLATA' &&
            evento.tipo !== 'ATTIVITA_LAVORATA' &&
            evento.tipo !== 'ATTIVITA_ASSEGNATA'
          ) {
            continue;
          }
          if (!evento.utente) {
            continue;
          }
          if (!evento.timestamp) {
            continue;
          }
          const eventoDate = new Date(evento.timestamp);
          if (eventoDate < (new Date(inizioPeriodo.getTime() - 10000)) || eventoDate > (new Date(finePeriodo.getTime() + 10000))) {
            continue;
          }

          // aggiungo una voce
          const chiaveUtenteSerie = evento.utente.nome + ' ' + evento.utente.cognome;
          const userSerie = findOrCreateUserSerie(chiaveUtenteSerie);

          const fineSegmentoCorrente = eventoDate;
          const attivitaSegmentoRiga = {
            x: chiaveAttivita,
            y: [inizioSegmento.getTime(), fineSegmentoCorrente.getTime()]
          };
          userSerie.data.push(attivitaSegmentoRiga);

          inizioSegmento = fineSegmentoCorrente;
        }

      }
    }

    chart.series = series;

    console.log(series);

    return chart;
  }
}
