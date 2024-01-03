/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  Component,
  OnInit,
} from '@angular/core';
import { Router } from '@angular/router';

import { NGXLogger } from 'ngx-logger';
import {
  forkJoin,
  from,
} from 'rxjs';
import {
  delay,
  finalize,
  mergeMap,
} from 'rxjs/operators';
import { Constants } from 'src/app/shared/constants/constants';
import {
  AssociazioneEnteUtente,
} from 'src/app/shared/models/api/cosmoauthorization/associazioneEnteUtente';
import {
  AssociazioneUtenteProfilo,
} from 'src/app/shared/models/api/cosmoauthorization/associazioneUtenteProfilo';
import { Utente } from 'src/app/shared/models/api/cosmoauthorization/utente';
import { ValorePreferenzeEnte } from 'src/app/shared/models/preferenze/valore-preferenze-ente.model';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { ConfigurazioniService } from 'src/app/shared/services/configurazioni.service';
import { LoginService } from 'src/app/shared/services/login.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { PreferenzeEnteService } from 'src/app/shared/services/preferenze-ente.service';
import { RedirectService } from 'src/app/shared/services/redirect.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loading = 0;
  loadingError: any | null = null;

  profilazione: Utente | null = null;
  idEnteSelezionato: number | null = null;
  idProfiloSelezionato: number | null = null;
  widgetsEnteSelezionato: string[] | null = null;
  widgetEnteModificabile = true;
  versionePreferenze: string | null = null;

  originalRequestedUrl: string | null = null;
  redirecting = false;
  accessoDirettoHomepage = false;

  constructor(
    private logger: NGXLogger,
    private loginService: LoginService,
    private securityService: SecurityService,
    private preferenzeEnteService: PreferenzeEnteService,
    private configurazioneService: ConfigurazioniService,
    private router: Router,
    private redirectService: RedirectService,
    private modalService: ModalService,
  ) { }

  ngOnInit(): void {
    this.originalRequestedUrl = this.redirectService.getOriginalRequestedUrl();
    this.refresh();
  }

  refresh(): void {
    this.loading ++;
    this.loadingError = null;

    forkJoin({
      principal: from(this.securityService.getCurrentUser()),
      configurazione: this.configurazioneService.getConfigurazioneByChiave(Constants.PARAMETRI.PREFERENZE_ENTE.VERSIONE),
      profilazione: this.loginService.getProfilazioneUtenteCorrente()
    })
    .pipe(
      delay(environment.httpMockDelay),
      finalize(() => {
        this.loading --;
      })
    )
    .subscribe(loaded => {
      this.logger.debug('PROFILAZIONE', loaded.profilazione);
      this.logger.debug('PRINCIPAL', loaded.principal);
      this.versionePreferenze = loaded.configurazione ?? '';

      if (loaded.principal.anonimo) {
        this.loadingError = 'Utente corrente non riconosciuto. Verificare la presenza dell\'header di autenticazione.';

      } else {
        if (this.goToHomepage(loaded.profilazione)) {
          this.accessoDirettoHomepage = true;
          this.idEnteSelezionato = loaded.profilazione.enti[0].ente?.id ?? null;
          this.idProfiloSelezionato = loaded.profilazione.profili[0].profilo.id ?? null;
          this.selezionaRuolo();
        } else {
          this.profilazione = loaded.profilazione;
          this.idEnteSelezionato = loaded.principal?.ente?.id || null;
          this.idProfiloSelezionato = loaded.principal?.profilo?.id || null;

          if (!this.idEnteSelezionato) {
            if (this.entiDisponibili?.length) {
              this.idEnteSelezionato = this.entiDisponibili[0]?.ente?.id || null;
              if (this.profiliDisponibili?.length) {
                this.idProfiloSelezionato = this.profiliDisponibili[0].profilo.id || null;
              }
            }

            try {
              this.restoreLastSelection(loaded.principal);
            } catch (e) {
              this.logger.error('error restoring last selection', e);
            }
          }
        }
      }
    }, failed => {
      this.loadingError = 'Utente corrente non riconosciuto. Verificare la presenza dell\'header di autenticazione.';
    });
  }

  private restoreLastSelection(principal: UserInfoWrapper): void {
    const lastSelection = localStorage.getItem('LastLogin' + principal.codiceFiscale);
    if (lastSelection?.length) {
      const lastSelectionParsed = JSON.parse(lastSelection);
      if (lastSelectionParsed?.ente) {
        const enteMatching = this.entiDisponibili?.find(e => e.ente?.id === lastSelectionParsed.ente);
        if (enteMatching?.ente?.id) {
          this.idEnteSelezionato = enteMatching.ente.id;
        }
      }
      if (lastSelectionParsed?.profilo) {
        const profiloMatching = this.profiliDisponibili?.find(p => p.profilo.id === lastSelectionParsed?.profilo);
        if (profiloMatching?.profilo?.id) {
          this.idProfiloSelezionato = profiloMatching.profilo.id;
        }
      }
    }
  }

  selezionaRuolo(): void {
    if (!this.idEnteSelezionato || !this.idProfiloSelezionato) {
      return;
    }
    this.logger.debug('logging in');

    this.loginService.selezionaEnteProfilo(this.idEnteSelezionato, this.idProfiloSelezionato)
    .pipe(
      mergeMap(() =>
      forkJoin({
        user: this.securityService.fetchCurrentUser(true),
        preferenzeEnte: this.idEnteSelezionato ? this.preferenzeEnteService.getPreferenze(this.idEnteSelezionato, this.versionePreferenze ?? '') : null
      })))
    .subscribe(

      result => {
        if (result.preferenzeEnte && result.preferenzeEnte.valore) {
          const valorePreferenze = JSON.parse(result.preferenzeEnte.valore) as ValorePreferenzeEnte;
          this.widgetsEnteSelezionato = valorePreferenze.widgets ?? [];
          this.widgetEnteModificabile = valorePreferenze.isWidgetModificabile ?? true;
        }
        this.loginService.setAccessoDiretto(this.accessoDirettoHomepage);
        if (this.accessoDirettoHomepage) {
          setTimeout(() => {
            this.loggedIn(result.user);
          }, 3000);
        } else {
          this.loggedIn(result.user);
        }
      },
      failure => this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore)
    );
  }

  selezionaProfiloDiretto(ass: AssociazioneUtenteProfilo): void {
    if (!ass?.profilo?.id) {
      return;
    }
    this.logger.debug('logging in with direct profile');

    this.loginService.selezionaProfilo(ass.profilo.id)
    .pipe(
      mergeMap(() => this.securityService.fetchCurrentUser(true)
    ))
    .subscribe(
      ui => this.loggedIn(ui),
      failure => this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore)
    );
  }

  private loggedIn(ui: UserInfoWrapper) {
    this.logger.debug('new userInfo', ui);

    this.redirecting = true;
    this.loading ++;

    localStorage.setItem('LastLogin' + ui.codiceFiscale, JSON.stringify({
      ente: this.idEnteSelezionato,
      profilo: this.idProfiloSelezionato
    }));
    if (this.widgetsEnteSelezionato !== null){
      localStorage.setItem('widgets', JSON.stringify({
        widgets: this.widgetsEnteSelezionato ?? [],
        modificabile: this.widgetEnteModificabile
      }));
    }
    if (!ui.ente) {
      this.router.navigate([Constants.ROUTE_PAGINA_HOME_ADMIN]);
    } else {
      const redirectTarget = this.originalRequestedUrl;
      if (!!redirectTarget) {
        this.originalRequestedUrl = null;
        this.redirectService.clearOriginalRequestedUrl();

        this.logger.info('restoring requested url after login', redirectTarget);
        this.router.navigateByUrl(redirectTarget);
      } else {
        this.router.navigate(['/home']);
      }
    }
  }

  enteChanged($event: Event): void {
    const pd = this.profiliDisponibili;
    if (pd?.length) {
      this.idProfiloSelezionato = pd[0].profilo.id || null;
    }
  }

  get profiliDirettiDisponibili(): AssociazioneUtenteProfilo[] {
    if (!this.profilazione || !this.profilazione.profili) {
      return [];
    }
    return this.profilazione.profili.filter(p => !p.ente)
      .sort((p1, p2) => (p1.profilo.descrizione ?? '').localeCompare(p2.profilo.descrizione ?? ''));
  }

  get entiDisponibili(): AssociazioneEnteUtente[] {
    if (!this.profilazione || !this.profilazione.enti) {
      return [];
    } else {
      return this.profilazione.enti
        .filter(ente => !!this.profilazione?.profili.find(p => p?.ente?.id === ente?.ente?.id))
        .sort((e1, e2) => (e1.ente?.nome ?? '').localeCompare(e2.ente?.nome ?? ''));
    }
  }

  get profiliDisponibili(): AssociazioneUtenteProfilo[] {
    if (!this.profilazione || !this.idEnteSelezionato || !this.profilazione.profili) {
      return [];
    } else {
      return this.profilazione.profili
      .filter(p => p?.ente?.id === this.idEnteSelezionato)
      .sort((p1, p2) => (p1.profilo?.descrizione ?? '').localeCompare(p2.profilo?.descrizione ?? ''));
    }
  }

  private goToHomepage(profilazione: Utente): boolean {
    return profilazione.profili.length === 1  && profilazione.enti.length === 1 && !!profilazione.profili[0].ente;
  }
}
