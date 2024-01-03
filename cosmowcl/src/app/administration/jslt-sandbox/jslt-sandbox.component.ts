/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { forkJoin, Observable, of, Subject, Subscription } from 'rxjs';
import { debounceTime, delay, finalize, first, mergeMap, tap } from 'rxjs/operators';
import { SelezionaPraticaModalComponent } from 'src/app/shared/components/modals/seleziona-pratica-modal/seleziona-pratica-modal.component';
import { Pratica } from 'src/app/shared/models/api/cosmopratiche/pratica';
import { PraticheService } from 'src/app/shared/services/pratiche.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';


@Component({
  selector: 'app-admin-jslt-sandbox',
  templateUrl: './jslt-sandbox.component.html',
  styleUrls: ['./jslt-sandbox.component.scss']
})
export class JsltSandboxComponent implements OnInit, OnDestroy {
  COMPONENT_NAME = 'JsltSandboxComponent';

  loading = 0;
  loadingError: any | null = null;
  loaded = false;

  computing = 0;
  computingError: any | null = null;

  placeholder = '{\n\t"oggettoPratica": .pratica.oggetto,\n\t"idPraticaEsterno": .pratica.id,\n\t"nomePrimaAttivita": .pratica.attivita[0].nome\n}';

  praticaSelezionata?: Pratica;
  risultatoMappatura: any = null;
  mappatura: string = this.placeholder;
  contestoMappatura: any = null;
  erroreContesto: any | null = null;

  mappaturaValue$ = new Subject<string>();
  subscriptions: Subscription[] = [];

  constructor(
    private logger: NGXLogger,
    private modal: NgbModal,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private praticaService: PraticheService,

  ) {
    this.logger.debug('creating component ' + this.COMPONENT_NAME);
  }

  get descrizionePraticaSelezionata() {
    if (this.praticaSelezionata) {
      return this.praticaSelezionata.oggetto ?? 'Pratica ' + this.praticaSelezionata.id;
    }
    return 'nessuna pratica selezionata';
  }

  get canExecute(): boolean {
    return !!this.praticaSelezionata?.id && !!this.mappatura?.length &&
      !this.computing && !this.loading;
  }

  ngOnInit(): void {
    this.logger.debug('initializing component ' + this.COMPONENT_NAME);
    this.refresh();
  }

  ngOnDestroy(): void {
    this.logger.debug('destroying component ' + this.COMPONENT_NAME);
    this.subscriptions.forEach(s => s.unsubscribe());
  }

  refresh() {
    this.loading ++;
    this.loadingError = null;
    this.loaded = false;

    this.activatedRoute.queryParams.pipe(
      first(),
      delay(environment.httpMockDelay),
      mergeMap(params => {
        if (params.spec?.length) {
          try {
            this.mappatura = atob(params.spec);
          } catch (err) {
            this.mappatura = this.placeholder;
          }
        }
        if (params.idPratica) {
          return this.praticaService.getPratica(params.idPratica, true);
        }
        return of(null);
      }),
      mergeMap(praticaIniziale => {
        if (praticaIniziale) {
          return this.praticaChanged(praticaIniziale, true);
        }
        return of(null);
      }),
      finalize(() => {
        this.loading--;
      })
    )
    .subscribe(() => {
      this.loaded = true;
      this.subscriptions.push(this.mappaturaValue$.pipe(
        debounceTime(400)
      ).subscribe(mappaturaChangedValue => this.mappaturaChangedDebounced(mappaturaChangedValue)));

    }, (failure: Error) => {
      this.loadingError = failure;
    });
  }

  cercaPratica(): void {
    const ngbModalOptions: NgbModalOptions = {
      backdrop : true,
      keyboard : true,
      size: 'xl',
    };

    const modalRef = this.modal.open(SelezionaPraticaModalComponent, ngbModalOptions);
    // const component = modalRef.componentInstance as SelezionaPraticaModalComponent;

    modalRef.result.then((pratica: Pratica) => {
      this.praticaChanged(pratica).subscribe(() => {}, () => {});
    }).catch(() => {});
  }

  praticaChanged(p: Pratica | null, initial?: boolean): Observable<any> {
    this.praticaSelezionata = p ?? undefined;
    this.risultatoMappatura = null;
    if (!p) {
      return of(null);
    }

    if (!initial) {
      this.router.navigate(
        [],
        {
          relativeTo: this.activatedRoute,
          queryParams: {
            idPratica: this.praticaSelezionata?.id,
          },
          queryParamsHandling: 'merge',
        });
    }

    this.loading ++;
    this.erroreContesto = null;

    return this.praticaService.getContestoElaborazionePratica(p.id!).pipe(
      finalize(() => this.loading --),
      tap(ctx => {
        this.contestoMappatura = ctx;
      }, err => {
        this.erroreContesto = Utils.toMessage(err);
        this.contestoMappatura = null;
      })
    );
  }

  mappaturaChanged(value: string): void {
    this.mappatura = value;
    this.mappaturaValue$.next(value);
  }

  mappaturaChangedDebounced(value: string): void {
    this.risultatoMappatura = null;
    this.router.navigate(
      [],
      {
        relativeTo: this.activatedRoute,
        queryParams: {
          spec: value?.length ? btoa(value) : '',
        },
        queryParamsHandling: 'merge',
      });
  }

  esegui(): void {
    this.risultatoMappatura = null;
    if (!this.canExecute) {
      return;
    }

    this.computing ++;
    this.computingError = null;

    this.praticaService.postElaborazionePratica(this.praticaSelezionata!.id!, this.mappatura).pipe(
      finalize(() => this.computing --)
    ).subscribe(result => {
      this.risultatoMappatura = result;

    }, err => {
      this.risultatoMappatura = null;
      this.computingError = Utils.toMessage(err);
    });
  }

}
