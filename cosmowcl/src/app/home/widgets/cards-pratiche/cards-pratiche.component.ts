/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnDestroy, OnInit, QueryList, ViewChildren } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbDropdown, NgbModal, NgbPopover } from '@ng-bootstrap/ng-bootstrap';
import { Subscription, forkJoin } from 'rxjs';
import { debounceTime, finalize } from 'rxjs/operators';
import { AssegnaAttivitaComponent } from 'src/app/shared/components/modals/assegna-attivita/assegna-attivita.component';
import { CondividiPraticaComponent } from 'src/app/shared/components/modals/condividi-pratica/condividi-pratica.component';
import { Constants } from 'src/app/shared/constants/constants';
import { Attivita } from 'src/app/shared/models/api/cosmopratiche/attivita';
import { Pratica } from 'src/app/shared/models/api/cosmopratiche/pratica';
import { TipoPratica } from 'src/app/shared/models/api/cosmopratiche/tipoPratica';
import { BusService } from 'src/app/shared/services/bus.service';
import { ConfigurazioniService } from 'src/app/shared/services/configurazioni.service';
import { HelperService } from 'src/app/shared/services/helper.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { NotificationsWebsocketService } from 'src/app/shared/services/notifications-websocket.service';
import { PraticheService } from 'src/app/shared/services/pratiche.service';
import { SecurityService } from 'src/app/shared/services/security.service';

@Component({
  selector: 'app-cards-pratiche',
  templateUrl: './cards-pratiche.component.html',
  styleUrls: ['./cards-pratiche.component.scss']
})
export class CardsPraticheComponent implements OnInit, OnDestroy {

  maxSize!: string;
  pratiche: Pratica[] = [];
  loading = 0;
  loadingError: any| null = null;

  isAdminPrat = false;

  @ViewChildren('popOverAttivita') public popOver: QueryList<NgbPopover> | null = null;



  private notificationSubcritpion: Subscription | null = null;
  private praticheSubscription!: Subscription;

  constructor(
    private praticheService: PraticheService,
    private configurazioniService: ConfigurazioniService,
    private notificationsWebsocketService: NotificationsWebsocketService,
    public modal: NgbModal,
    public modalService: ModalService,
    private busService: BusService,
    private router: Router,
    private securityService: SecurityService,
    public helperService: HelperService,
    public route: ActivatedRoute
  ) { }



  ngOnInit(): void {

    this.praticheSubscription = this.busService.getCercaPratiche().subscribe(ricerca => {
      if (ricerca) {
        this.refresh();
      }
    });

    this.notificationSubcritpion = this.notificationsWebsocketService
      .whenEvent(Constants.APPLICATION_EVENTS.NOTIFICA_ULTIME_LAVORATE)
      .pipe(
        debounceTime(1000)
      )
      .subscribe(e => {
        this.refresh();
      });

    forkJoin({
      configurazione: this.configurazioniService.getConfigurazioneByChiave(Constants.PARAMETRI.HOMEPAGE.PRATICHE.MAX_SIZE_DA_LAVORARE),
      isAdminPrat: this.securityService.hasUseCases(['ADMIN_PRAT'], false)
    })
    .subscribe(result => {
      this.isAdminPrat = result.isAdminPrat;
      if (result.configurazione) {
        this.maxSize = result.configurazione;
        this.refresh();
      }
    });

  }

  refresh(){
    this.loading++;
    this.loadingError = null;
    this.praticheService.search(this.getSearchPayload(this.maxSize), false).pipe(finalize(() => {this.loading--; }))
    .subscribe(p => this.pratiche = p.pratiche ?? [], failure => this.loadingError = failure);
  }

  lavora(attivita: Attivita){

    if (attivita.linkAttivitaEsterna) {
      window.open(attivita.linkAttivitaEsterna, '_blank');
    } else {
      this.router.navigate([attivita.linkAttivita]);
    }

  }




  apriPopOver(id: number){

    const index = this.pratiche.findIndex(elem => elem.id === id);
    if (this.popOver?.toArray()[index]){
      this.popOver?.toArray()[index]?.open();
    }

  }



  hasImmaginePratica(tipoPratica: TipoPratica){
    return tipoPratica?.immagine && tipoPratica?.immagine !== '';
  }

  getImmaginePratica(tipoPratica: TipoPratica){
    return 'data:image/png;base64,' + tipoPratica.immagine;

  }

  isAnnullabile(pratica: Pratica): boolean {
    return !!pratica.tipo?.annullabile && this.isAdminPrat;
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
      return p.stato?.descrizione ? '\u00A0\u00A0' +
      (p.stato?.descrizione.length > 30 ? p.stato?.descrizione.substring(0, 30).concat('...') : p.stato?.descrizione) : ' ';
    }

    return p.stato?.descrizione && p.stato?.descrizione.length > 20 ?
     p.stato?.descrizione?.substring(0, 30).concat('...') : p.stato?.descrizione;
  }

  dettaglioPratica(pratica: Pratica){
    this.router.navigate(['pratica', pratica.id]);
  }


  canShare(pratica: Pratica): boolean {


    return ['B1', 'B2', 'B3', 'E1', 'E2'].some(
      clause => (pratica.visibilita ?? []).indexOf(clause) !== -1) && !!pratica.tipo?.condivisibile;
  }

  canAssign(attivita: Attivita[], tipo?: TipoPratica): boolean {
    return  attivita && attivita.length > 0 && !attivita.find(a => !!a.parent) && !!tipo?.assegnabile;
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
    modalRef.componentInstance.codiceModale = 'assegna-pratica-cards';
    modalRef.result.then(result => {
      if (result === Constants.OK_MODAL) {
        this.refresh();
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
    modalRef.componentInstance.codiceModale = 'condividi-pratica-cards';
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
      size: 18,
      sort: 'dataCreazionePratica DESC',
      filter: {
        groups: {
          daLavorare:  {
            inCorso: true
          }
        }
      },
    };
    return payload;
  }

  ngOnDestroy(): void {
    if (this.notificationSubcritpion) {
      this.notificationSubcritpion.unsubscribe();
    }
    if (this.praticheSubscription) {
      this.praticheSubscription.unsubscribe();
    }
  }

}
