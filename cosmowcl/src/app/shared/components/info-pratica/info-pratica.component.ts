/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import {
  ActivatedRoute,
  Router,
} from '@angular/router';

import { ClipboardService } from 'ngx-clipboard';
import { Subscription } from 'rxjs';
import { PraticheService } from 'src/app/shared/services/pratiche.service';
import {
  RouterHistoryService,
} from 'src/app/shared/services/router-history.service';
import { environment } from 'src/environments/environment';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';

import { Constants } from '../../constants/constants';
import { UserInfoGruppo } from '../../models/api/cosmo/userInfoGruppo';
import { Pratica } from '../../models/api/cosmobusiness/pratica';
import { Attivita } from '../../models/api/cosmopratiche/attivita';
import {
  FormLogiciContext,
} from '../../models/form-logici/form-logici-context.model';
import {
  RouterHistoryInfo,
} from '../../models/router-history/routerHistoryInfo';
import { AttivitaService } from '../../services/attivita.service';
import { BusService } from '../../services/bus.service';
import { ModalService } from '../../services/modal.service';
import { SecurityService } from '../../services/security.service';
import { ignore } from '../../utilities/function-utils';
import { Utils } from '../../utilities/utilities';
import {
  AssegnaAttivitaComponent,
} from '../modals/assegna-attivita/assegna-attivita.component';
import { HelperService } from '../../services/helper.service';

@Component({
  selector: 'app-info-pratica',
  templateUrl: './info-pratica.component.html',
  styleUrls: ['./info-pratica.component.scss']
})
export class InfoPraticaComponent implements OnInit, OnDestroy {

  @Input() context?: FormLogiciContext;
  @Input() readOnly = false;
  @Input() pratica!: Pratica;
  @Input() isDettaglio!: boolean;
  @Input() embedded!: boolean;

  @Output() refresh: EventEmitter<void> = new EventEmitter<void>();

  taskId !: string;
  currentUserCF: string | null = null;
  gruppiUtente!: UserInfoGruppo[];
  routerHistoryInfo!: RouterHistoryInfo;
  private storicoPreviousSubscription!: Subscription;

  mostraInfoFruitore = !environment.production;
  justCopied = false;

  assegnaStato: string | undefined = undefined;

  constructor(
    private praticheService: PraticheService,
    private modal: NgbModal,
    private router: Router,
    private route: ActivatedRoute,
    private securityService: SecurityService,
    private routerHistoryService: RouterHistoryService,
    private modalService: ModalService,
    private translateService: TranslateService,
    private attivitaService: AttivitaService,
    private busService: BusService,
    private clipboardService: ClipboardService,
    public helperService: HelperService
  ) {}

  attivita: Attivita[] = [];

  get isEsterna(): boolean {
    return this.pratica?.esterna ?? false;
  }

  get isAssignedToGroup(): boolean {
    if (!this.context?.attivita) {
      return false;
    }

    if (!this.context.attivita?.assegnazione?.length) {
      // non posso sapere a chi e' assegnata
      return false;
    }

    if (this.context.attivita?.assegnazione
      .filter(a => !(a.campiTecnici?.dtFineVal) && !!a.idUtente).length) {
      // e' assegnata ad almeno un utente
      return false;
    }

    if (this.context.attivita?.assegnazione
      .filter(a => !(a.campiTecnici?.dtFineVal) && !!a.idGruppo).length) {
        // e' assegnata ad almeno un gruppo
      return true;
    }

    return false;
  }

  get isSubtask(): boolean {
    return !!(this.context?.childTask?.id);
  }

  ngOnDestroy(): void {
    if (this.storicoPreviousSubscription) {
      this.storicoPreviousSubscription.unsubscribe();
    }
  }

  ngOnInit(): void {
    this.storicoPreviousSubscription = this.routerHistoryService.historyUrl$.subscribe(history => {
      this.creaLinkPaginaPrecedente(history);
    });

    this.route.params.subscribe(params => { this.taskId = params.id; });

    this.securityService.getCurrentUser().subscribe(user => {
      if (user && user.codiceFiscale) {
        this.currentUserCF = user.codiceFiscale;
        this.gruppiUtente = (user.gruppi ?? []);

        if (!this.pratica?.linkPratica && !this.pratica.esterna) {
          throw new Error('Missing pratica -> linkPratica in info-pratica.component.ts initialization');
        }

        this.praticheService.getElencoAttivitaPratica(
          (this.pratica) ? this.pratica.id as number : null
        )
        .subscribe(response => {
          if (response && response.length > 0) {
            response.forEach( elem => {
              this.attivita.push({
                nome: elem.nome,
                gruppoAssegnatario: elem.gruppoAssegnatario ? '(' + elem.gruppoAssegnatario + ')' : '',
                linkAttivita: elem.linkAttivita ? Utils.getIdTaskFromLinkAttivita(elem.linkAttivita as string) : undefined,
                linkAttivitaEsterna: elem.linkAttivitaEsterna,
              });
            });
          }
        });
      }
    });
  }

  apriPraticaEsterna(): void {
    if (!this.pratica?.linkPraticaEsterna) {
      return;
    }
    window.open(this.pratica.linkPraticaEsterna, '_blank');
  }

  doAttivita(a: Attivita) {
    if (a.linkAttivitaEsterna) {
      window.open(a.linkAttivitaEsterna, '_blank');
    } else {
      this.router.navigate(['/tasks', a.linkAttivita]);
    }
  }

  getNomeTaskFromAttivita(practice: Pratica){
    if (practice.attivita?.length){
      return (practice.attivita ?? []).find( att => att.linkAttivita === 'tasks/' + this.taskId )?.nome;
    }
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

    if (this.isDettaglio){
      const coll = p.attivita?.find(att => att.hasChildren) || p.attivita?.find(att => att.parent);

      if (p?.stato?.classe?.length) {
        if (coll){
          return p.stato.classe + ' fas fa-hands-helping fas-hands';
        } else{
          return p.stato.classe;
        }
      } else {
        if (coll){
          return ' fas fa-hands-helping fas-hands';
        }
      }

    } else {
      if (p?.stato?.classe?.length) {
        return p.stato.classe;
      }
    }

    return 'primary';
  }

  getStatusText(p: Pratica) {
    if (this.isAnnullata(p)) {
      return 'Annullata';
    }

    if (this.isDettaglio) {
      const coll = p.attivita?.find(att => att.hasChildren) || p.attivita?.find(att => att.parent);
      if (coll){
        return p.stato?.descrizione ? '\u00A0\u00A0' + p.stato?.descrizione : ' ';
      }
    }
    return p.stato?.descrizione;
  }

  backTo() {
    this.routerHistoryService.back();
  }

  isAssegnabile(): boolean {
    return !!this.pratica.tipo?.assegnabile;
  }

  async assegnaAttivita() {
    if (!this.context || !this.context.attivita || !this.context.pratica) {
      return;
    }
    const data = this.helperService.searchHelperRef(this.route);
    const modalRef = this.modal.open(AssegnaAttivitaComponent);
    modalRef.componentInstance.idAttivita = this.context.attivita.id;
    modalRef.componentInstance.idPratica = this.context.pratica.id;
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = 'assegna-pratica-info-pratica';
    modalRef.componentInstance.soloCodicePagina = true;

    const res = await modalRef.result;
    if (res === Constants.OK_MODAL) {
      this.router.navigateByUrl('home');
    }
  }

  assegnaAMe(): void {
    if (this.readOnly) {
      return;
    }

    this.modalService.scegli(
      this.translateService.instant('form_logici.dialogs.assegna_a_me.title'), [
        { testo: this.translateService.instant('form_logici.dialogs.assegna_a_me.warning'), classe: 'text-warning' },
        { testo: this.translateService.instant('form_logici.dialogs.assegna_a_me.confirm') }
      ], [
        { testo: this.translateService.instant('common.procedi'), classe: 'btn-outline-primary', valore: true },
        { testo: this.translateService.instant('common.annulla'), classe: 'btn-primary', dismiss: true, defaultFocus: true }
      ]
    ).then(() => {
      this.securityService.getCurrentUser().subscribe(user => {
        if (!this.context?.attivita?.id || !this.context?.pratica?.id) {
          return;
        }
        this.attivitaService.assegnaAttivitaAMe(this.context.pratica.id, this.context.attivita.id).subscribe(() => {
          this.busService.setCercaPratiche(true);
          this.refresh.next();
        }, failure => {
          this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore);
        });
      });
    }, ignore);
  }

  hasAttivitaAssegnate(): boolean {
    for (const a of this.pratica.attivita ?? []) {
      for (const ass of a.assegnazione ?? [])  {
        if (ass.assegnatario &&
            (ass.campiTecnici && (!ass.campiTecnici.dtFineVal || new Date(ass.campiTecnici.dtFineVal) > new Date()))) {
              return true;
        } else {
          if (ass.idGruppo){
            const gruppo = this.gruppiUtente.find( o => ass.idGruppo && +ass.idGruppo === o.id );
            if (gruppo && (ass.campiTecnici && (!ass.campiTecnici.dtFineVal || new Date(ass.campiTecnici.dtFineVal) > new Date())) ) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  copiaIdEsterno(): void {
    this.clipboardService.copy(this.pratica?.idPraticaExt ?? '');
    this.justCopied = true;
    setTimeout(() => this.justCopied = false, 1000);
  }

  creaLinkPaginaPrecedente(history: RouterHistoryInfo []){

    if (history && history.length > 0){

      const previous = this.routerHistoryService.getPreviousUrl(history);

      if (previous && previous.data) {
        if (previous.data.descrizione){
          this.routerHistoryInfo = previous;
          if (previous.data?.root) {
            this.routerHistoryService.setRoot(previous);
          }
        } else {
          const root = this.routerHistoryService.root;
          if (root) {
            this.routerHistoryInfo = root;
          }
        }
      }
    }
  }
}
