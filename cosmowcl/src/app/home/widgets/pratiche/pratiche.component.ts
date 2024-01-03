/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  Component,
  Input,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import {
  CosmoTableColumnSize,
  CosmoTableComponent,
  CosmoTableFormatter,
  ICosmoTableColumn,
  ICosmoTablePageRequest,
  ICosmoTablePageResponse,
} from 'ngx-cosmo-table';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import {
  debounceTime,
  map,
  mergeMap,
  tap,
} from 'rxjs/operators';
import {
  AssegnaAttivitaComponent,
} from 'src/app/shared/components/modals/assegna-attivita/assegna-attivita.component';
import {
  CondividiPraticaComponent,
} from 'src/app/shared/components/modals/condividi-pratica/condividi-pratica.component';
import { Constants } from 'src/app/shared/constants/constants';
import { Attivita } from 'src/app/shared/models/api/cosmopratiche/attivita';
import { Pratica } from 'src/app/shared/models/api/cosmopratiche/pratica';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { BusService } from 'src/app/shared/services/bus.service';
import {
  ConfigurazioniService,
} from 'src/app/shared/services/configurazioni.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import {
  NotificationsWebsocketService,
} from 'src/app/shared/services/notifications-websocket.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { DateUtils } from 'src/app/shared/utilities/date-utils';
import { Utils } from 'src/app/shared/utilities/utilities';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { PraticheService } from '../../../shared/services/pratiche.service';
import { TipoPratica } from 'src/app/shared/models/api/cosmopratiche/tipoPratica';
import { HelperService } from 'src/app/shared/services/helper.service';

@Component({
  selector: 'app-pratiche',
  templateUrl: './pratiche.component.html',
  styleUrls: ['./pratiche.component.scss'],
  providers: [PraticheService]
})
export class PraticheComponent implements OnInit, OnDestroy {

  isAdminPrat = false;
  maxSize!: string;

  @Input() titolo = '';
  @Input() kindOfPractice = '';
  @Input() size = 'small';

  columns: ICosmoTableColumn[] = [
    {
      name: 'favorite',
      canHide: false, canSort: false,
      applyTemplate: true, size: CosmoTableColumnSize.XXS
    },
    {
      name: 'dataCreazionePratica', label: 'Data inizio',
      field: 'dataCreazionePratica',
      formatters: [{
        formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy'
      }],
      valueExtractor: raw => DateUtils.parse(raw.dataCreazionePratica),
      canSort: true
    },
    { name: 'oggetto', label: 'Oggetto', field: 'oggetto', canSort: true },
    { name: 'tipoPratica', label: 'Tipo', field: 'tipo.descrizione', serverField: 'tipoPratica', canSort: true, canHide: false },
    { name: 'stato', label: 'Stato', field: 'stato.descrizione', serverField: 'stato', canSort: true, applyTemplate: true },
    {
      name: 'dettaglio',
      canHide: false, canSort: false,
      applyTemplate: true, size: CosmoTableColumnSize.XXS
    },
    {
      name: 'azioni_dropdown',
      canHide: false, canSort: false,
      applyTemplate: true, size: CosmoTableColumnSize.XS
    },
    {
      name: 'azioni_menu',
      canHide: false, canSort: false,
      applyTemplate: true, size: CosmoTableColumnSize.XXS
    },
    {
      name: 'alert',
      canHide: false, canSort: false,
      applyTemplate: true, size: CosmoTableColumnSize.XXS
    },
  ];

  columnsSmall: ICosmoTableColumn[] = [
    {
      name: 'favorite',
      canHide: false, canSort: false,
      applyTemplate: true, size: CosmoTableColumnSize.XXS
    },
    { name: 'oggetto', label: 'Oggetto', field: 'oggetto' },
    {
      name: 'dettaglio',
      canHide: false, canSort: false,
      applyTemplate: true, size: CosmoTableColumnSize.XXS
    },
    {
      name: 'azioni_menu',
      canHide: false, canSort: false,
      applyTemplate: true, size: CosmoTableColumnSize.XXS
    },
    {
      name: 'alert',
      canHide: false, canSort: false,
      applyTemplate: true, size: CosmoTableColumnSize.XXS
    },
    {
      name: 'dataCreazionePratica',
      label: 'Data inizio',
      field: 'dataCreazionePratica',
      valueExtractor: raw => DateUtils.parse(raw.dataCreazionePratica),
      canSort: true,
      defaultVisible: false
    },
  ];

  @ViewChild('table') table: CosmoTableComponent | null = null;

  private notificationSubcritpion: Subscription | null = null;
  private praticheSubscription!: Subscription;
  private principal?: UserInfoWrapper;

  constructor(
    private logger: NGXLogger,
    private praticheService: PraticheService,
    private busService: BusService,
    private securityService: SecurityService,
    private router: Router,
    public modal: NgbModal,
    public modalService: ModalService,
    private notificationsWebsocketService: NotificationsWebsocketService,
    private configurazioniService: ConfigurazioniService,
    public helperService: HelperService,
    public route: ActivatedRoute
  ) { }


  ngOnDestroy(): void {
    if (this.notificationSubcritpion) {
      this.notificationSubcritpion.unsubscribe();
    }
    if (this.praticheSubscription) {
      this.praticheSubscription.unsubscribe();
    }
  }

  get effectiveColumns(): ICosmoTableColumn[] {
    if (this.size === 'small') {
      return this.columnsSmall;
    } else {
      return this.columns;
    }
  }

   ngOnInit() {
    const maxSearchSize = this.getMaxSearchSizeByKindOfPractice();
    this.configurazioniService.getConfigurazioneByChiave(maxSearchSize).subscribe(maxSizeCfg => {
      if (maxSizeCfg) {
        this.maxSize = maxSizeCfg;
      }
    });

    this.praticheSubscription = this.busService.getCercaPratiche().subscribe(ricerca => {
      if (ricerca) {
        this.table?.refresh();
      }
    });

    this.notificationSubcritpion = this.notificationsWebsocketService
      .whenEvent(Constants.APPLICATION_EVENTS.NOTIFICA_ULTIME_LAVORATE)
      .pipe(
        debounceTime(1000)
      )
      .subscribe(e => {
        this.table?.refresh();
      });

    this.securityService.principal$.pipe(
        tap( principal => this.principal = principal ),
        mergeMap( () => this.securityService.hasUseCases(['ADMIN_PRAT'], false))
      ).subscribe(result => this.isAdminPrat = result);
  }

  dataProvider = (input: ICosmoTablePageRequest) => {
    return this.praticheService.search(this.getSearchPayload(this.maxSize), false).pipe(
      map(response => {
        const copy: ICosmoTablePageResponse = {
          content: response.pratiche as any[],
          number: response.pratiche?.length,
          numberOfElements: response.pratiche?.length ?? 0,
          size: response.pageInfo?.pageSize ?? 0,
          totalElements: response.pageInfo?.totalElements ?? 0,
          totalPages: response.pageInfo?.totalPages
        };
        return copy;
      })
    );
  }

  togglePreference(p: Pratica) {
    this.praticheService.setPreferredStatus(p, !p.preferita).subscribe(() => {
      this.busService.setCercaPratiche(true);
    });
  }

  goToDettaglio(p: Pratica) {
    this.router.navigate(['pratica', p.id]);
  }

  doAttivita(a: Attivita) {
    if (a.linkAttivitaEsterna) {
      window.open(a.linkAttivitaEsterna, '_blank');
    } else {
      this.router.navigate([a.linkAttivita]);
    }
  }

  canShare(pratica: Pratica): boolean {
    if (!this.isDaLavorare()) {
      return false;
    }

    return ['B1', 'B2', 'B3', 'E1', 'E2'].some(
      clause => (pratica.visibilita ?? []).indexOf(clause) !== -1) && !!pratica.tipo?.condivisibile;
  }

  canAssign(attivita: Attivita[], tipo?: TipoPratica): boolean {
    return this.isDaLavorare() && attivita && attivita.length > 0 && !attivita.find(a => !!a.parent) && !!tipo?.assegnabile;
  }

  isAnnullabile(pratica: Pratica): boolean {
    return !!pratica.tipo?.annullabile && this.isAdminPrat;
  }

  isDaLavorare(): boolean {
    return this.kindOfPractice === Constants.PRATICHE.IN_LAVORAZIONE;
  }

  isCondivise(): boolean {
    return this.kindOfPractice === Constants.PRATICHE.IN_EVIDENZA;
  }

  eliminaCondivisione(p: Pratica): void {
    if (!this.isCondivise() || !p.id) {
      return;
    }

    const firstCondWithMe = p.condivisioni?.find(c =>
      (c.condivisaAUtente && c.condivisaAUtente?.codiceFiscale === this.principal?.codiceFiscale)
      || (c.condivisaAGruppo && this.principal?.gruppi?.some(gruppo => gruppo.id === c.condivisaAGruppo?.id)));

    if (firstCondWithMe) {
      this.praticheService.eliminaCondivisionePratica(p.id, firstCondWithMe.id).subscribe(() => {
        this.busService.setCercaPratiche(true);
      }, failure => this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore));
    }
  }

  tutte(): void {
    this.router.navigate(['elenco-lavorazioni'], {
      queryParams: {
        ref: this.kindOfPractice ??  undefined
      }
    });
  }

  assignPratica(p: Pratica) {
    if (!p?.id) {
      return;
    }
    const data = this.helperService.searchHelperRef(this.route);
    const modalRef = this.modal.open(AssegnaAttivitaComponent);
    modalRef.componentInstance.idAttivita = null;
    modalRef.componentInstance.idPratica = p.id;
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = 'assegna-pratica-home';
    modalRef.result.then(result => {
      if (result === Constants.OK_MODAL) {
        this.table?.refresh(false);
      }
    });
  }

  condividiPratica(p: Pratica) {
    if (!p?.id) {
      return;
    }
    const data = this.helperService.searchHelperRef(this.route);
    const modalRef = this.modal.open(CondividiPraticaComponent);
    const component = modalRef.componentInstance as CondividiPraticaComponent;
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = 'condividi-pratica-home';
    component.idPratica = p.id;
    modalRef.result.then(() => {}).catch(() => {});
  }

  annullaPratica(p: Pratica) {
    if (!p.id) {
      return;
    }
    const titolo = 'Conferma annullamento pratica';
    const descrizione = 'Si conferma l\'annullamento della pratica ' + p.oggetto + ' ?';

    this.modalService.confermaRifiuta(titolo, descrizione).then(
      () => {
        if (p.id) {
          this.praticheService.annullaPratica(p.id).subscribe(response => {
            if (response.ok) {
              this.modalService.info('Esito', 'Cancellazione pratica andata a buon fine', 'chiudi').then(
                () => this.busService.setCercaPratiche(true)
              );
            }
            else {
              this.modalService.error('Esito', 'Cancellazione fallita: ' + response.statusText, undefined, 'chiudi');
            }
          });
        }
      }
    );
  }

  private getSearchPayload(searchSize: string){
    const payload = {
      page: 0,
      size: searchSize,
      sort: 'dataCreazionePratica DESC',
      filter: {
        groups: {
          preferite: this.kindOfPractice === Constants.PRATICHE.PREFERITE ? {
            inCorso: true,
            concluse: true,
            annullate: false,
          } : undefined,
          inEvidenza: this.kindOfPractice === Constants.PRATICHE.IN_EVIDENZA ? {
            inCorso: true,
            concluse: true,
            annullate: false,
          } : undefined,
          daLavorare: this.kindOfPractice === Constants.PRATICHE.IN_LAVORAZIONE ? {
            inCorso: true
          } : undefined,
        }
      },
    };
    return payload;
  }

  private getMaxSearchSizeByKindOfPractice(){
    switch (this.kindOfPractice){
      case Constants.PRATICHE.PREFERITE: {
        return Constants.PARAMETRI.HOMEPAGE.PRATICHE.MAX_SIZE_PREFERITE;
      }
      case Constants.PRATICHE.IN_EVIDENZA: {
        return Constants.PARAMETRI.HOMEPAGE.PRATICHE.MAX_SIZE_IN_EVIDENZA;
      }
      case Constants.PRATICHE.IN_LAVORAZIONE: {
        return Constants.PARAMETRI.HOMEPAGE.PRATICHE.MAX_SIZE_DA_LAVORARE;
      }
      default: {
        return Constants.PARAMETRI.HOMEPAGE.PRATICHE.MAX_SIZE_DEFAULT;
      }
    }
  }

  isConclusaOAnnullata(p: any): boolean {
    return this.isConclusa(p) || this.isAnnullata(p);
  }

  isConclusa(p: any): boolean {
    return !!p.dataFinePratica;
  }

  isAnnullata(p: any): boolean {
    return !!p.dataCancellazione;
  }

  getBadgeClass(p: Pratica) {
    if (this.isAnnullata(p)) {
      return 'danger';
    }

    const coll = p.attivita?.find(att => att.hasChildren) || p.attivita?.find(att => att.parent);

    if (p?.stato?.classe?.length) {
      if (coll){
        return p.stato.classe + ' fas fa-hands-helping fas-hands';
      } else{
        return p.stato.classe;
      }
    } else {
      if (coll){
        return 'primary fas fa-hands-helping fas-hands';
      }
    }

    return 'primary';
  }

  getStatusText(p: Pratica) {
    if (this.isAnnullata(p)) {
      return 'Annullata';
    }

    const coll = p.attivita?.find(att => att.hasChildren) || p.attivita?.find(att => att.parent);
    if (coll){
      return p.stato?.descrizione ? '\u00A0\u00A0' + p.stato?.descrizione : ' ';
    }

    return p.stato?.descrizione;
  }
}

