/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { forkJoin, Observable, of } from 'rxjs';
import { delay, finalize, map, mergeMap, tap } from 'rxjs/operators';
import { RiferimentoGruppo } from 'src/app/shared/models/api/cosmoauthorization/riferimentoGruppo';
import { CondivisionePratica } from 'src/app/shared/models/api/cosmobusiness/condivisionePratica';
import { Pratica } from 'src/app/shared/models/api/cosmobusiness/pratica';
import { CreaCondivisionePraticaRequest } from 'src/app/shared/models/api/cosmopratiche/creaCondivisionePraticaRequest';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { BusService } from 'src/app/shared/services/bus.service';
import { GruppiService } from 'src/app/shared/services/gruppi.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { PraticheService } from 'src/app/shared/services/pratiche.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';
import { RicercaUtenteComponent, SelezioneUtenteGruppo } from '../../ricerca-utente/ricerca-utente.component';
import { ModaleParentComponent } from 'src/app/modali/modale-parent-component';
import { HelperService } from 'src/app/shared/services/helper.service';


@Component({
  selector: 'app-condividi-pratica',
  templateUrl: './condividi-pratica.component.html',
  styleUrls: ['./condividi-pratica.component.scss']
})
export class CondividiPraticaComponent extends ModaleParentComponent implements OnInit, AfterViewInit {

  constructor(
    public modal: NgbActiveModal,
    private securityService: SecurityService,
    private praticaService: PraticheService,
    private busService: BusService,
    private modalService: ModalService,
    private gruppiService: GruppiService,
    public helperService: HelperService
  ) {
    super(helperService);
  }

  public selectedUser?: SelezioneUtenteGruppo = undefined;
  idPratica?: number;
  pratica?: Pratica;
  idAttivita?: number;

  idEnte: number | undefined;
  currCf: string | undefined;
  currGruppi: RiferimentoGruppo[] = [];

  loading = 0;
  loadingError: any = null;
  codicePagina!: string;
  codiceTab!: string;
  codiceModale!: string;

  @ViewChild('defaultfocus') ricercaUtenteComponent: RicercaUtenteComponent | null = null;

  filtroRicercaUtenti = (filtro: any) => {
    const cfGiaCondivisi = this.condivisioniAdAltri.map(c => c.condivisaAUtente?.codiceFiscale);
    if (!cfGiaCondivisi.length) {
      return filtro;
    }

    filtro.codiceFiscale = filtro.codiceFiscale || {};
    filtro.codiceFiscale.nin = [...(filtro.codiceFiscale.nin ?? []), ...cfGiaCondivisi];
    return filtro;
  }

  filtroRicercaGruppi = (filtro: any) => {
    const gruppiGiaCondivisi = this.condivisioniAGruppi.map(c => c.condivisaAGruppo?.codice);
    if (!gruppiGiaCondivisi.length) {
      return filtro;
    }
    filtro.idEnte = { eq: this.idEnte };
    filtro.codice = filtro.codice || {};
    filtro.codice.nin = [...(filtro.codice.nin ?? []), ...gruppiGiaCondivisi];
    return filtro;
  }

  ngOnInit(): void {
    this.refresh().subscribe();
  }

  refresh(): Observable<any> {
    this.loading ++;
    this.loadingError = null;

    return of(this.idPratica).pipe(
      map(idPratica => {
        if (!idPratica) {
          throw new Error('Nessun ID pratica fornito');
        }
        return idPratica;
      }),
      mergeMap(idPratica => forkJoin({
        user: this.securityService.getCurrentUser(),
        pratica: this.praticaService.getPratica(idPratica, true),
        gruppi: this.gruppiService.getGruppiUtenteCorrente()
      })),
      delay(environment.httpMockDelay),
      tap(loaded => {
        this.idEnte = loaded.user.ente?.id;
        this.currCf = loaded.user.codiceFiscale;
        this.pratica = loaded.pratica;
        this.currGruppi = loaded.gruppi;
      }, err => {
        this.loadingError = Utils.toMessage(err);
      }),
      finalize(() => {
        this.loading --;
        this.setModalName(this.codiceModale);
        this.searchHelperModale(this.codicePagina, this.codiceTab);
      })
    );


  }

  getGruppiFilter(user: UserInfoWrapper): any {
    const output: any = {
      filter: {
        idEnte: {
          eq: Utils.require(user?.ente?.id, 'idEnte')
        },
        codiceFiscaleUtente: {
          ci: user.codiceFiscale
        }
      },
    };
    return output;
  }

  get condivisioniAdAltri(): CondivisionePratica[] {
    return (this.pratica?.condivisioni ?? [])
      .filter(c => c.condivisaDa?.codiceFiscale === this.currCf && c.condivisaAUtente && c.condivisaAUtente.codiceFiscale !== this.currCf)
      .sort((c1, c2) => (c1.condivisaAUtente?.nome + ' ' + c1.condivisaAUtente?.cognome)
        .localeCompare((c2.condivisaAUtente?.nome + ' ' + c2.condivisaAUtente?.cognome)));
  }

  get condivisioniAGruppi(): CondivisionePratica[] {
    return (this.pratica?.condivisioni ?? [])
      .filter(c => c.condivisaDa?.codiceFiscale === this.currCf && c.condivisaAGruppo)
      .sort((c1, c2) => (c1.condivisaAGruppo?.nome + '')
        .localeCompare((c2.condivisaAGruppo?.nome + '')));
  }


  ngAfterViewInit() {
    setTimeout(() => {
    if (this.ricercaUtenteComponent) {
      this.ricercaUtenteComponent.focus();
    }}, 0);
  }

  condividiPratica() {
    if (!this.idPratica || (!this.selectedUser?.utente?.id && !this.selectedUser?.gruppo?.id)) {
      return;
    }

    const request: CreaCondivisionePraticaRequest = {
      idUtente: this.selectedUser?.utente?.id,
      idGruppo: this.selectedUser?.gruppo?.id
    };

    this.loading ++;
    this.praticaService.condividiPratica(this.idPratica, request).pipe(
      mergeMap(() => this.refresh()),
      finalize(() => this.loading--),
    ).subscribe(() => {
      this.ricercaUtenteComponent?.clear();
      this.selectedUser = undefined;
      this.busService.setCercaPratiche(true);
    }, failure => {
      this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore);
    });
  }

  eliminaCondivisione(c: CondivisionePratica): void {
    if (!this.idPratica) {
      return;
    }

    this.loading ++;
    this.praticaService.eliminaCondivisionePratica(this.idPratica, c.id).pipe(
      mergeMap(() => this.refresh()),
      finalize(() => this.loading--),
    ).subscribe(() => {
      this.busService.setCercaPratiche(true);
    }, failure => {
      this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore);
    });
  }
}
