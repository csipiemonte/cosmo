/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, OnDestroy, HostListener, Output, EventEmitter } from '@angular/core';
import { SecurityService } from 'src/app/shared/services/security.service';
import { environment } from 'src/environments/environment';
import { ValorePreferenzeEnte } from 'src/app/shared/models/preferenze/valore-preferenze-ente.model';
import { DomSanitizer } from '@angular/platform-browser';
import { PreferenzeEnteService } from 'src/app/shared/services/preferenze-ente.service';
import { TranslateService } from '@ngx-translate/core';
import { Observable, Subscription } from 'rxjs';
import { ModalService } from 'src/app/shared/services/modal.service';
import { ActivatedRoute, Data, NavigationEnd, NavigationError, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import { Utils } from 'src/app/shared/utilities/utilities';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { HelperService } from 'src/app/shared/services/helper.service';
import { Helper } from 'src/app/shared/models/api/cosmonotifications/helper';
import { LoginService } from 'src/app/shared/services/login.service';
import { FormLogiciConfig } from 'src/app/shared/models/form-logici/form-logici-config.model';
import { HelperResponse } from 'src/app/shared/models/api/cosmonotifications/helperResponse';
import { EnvironmentRelatedFunction } from 'src/app/shared/utilities/enviroment-enum';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {

  @Output() hasPageHelper = new EventEmitter<boolean>();

  formLogiciCfg!: FormLogiciConfig[] | null;

  constructor(
    private translateService: TranslateService,
    private securityService: SecurityService,
    private sanitizer: DomSanitizer,
    private preferenzeEnteService: PreferenzeEnteService,
    private modalService: ModalService,
    private route: ActivatedRoute,
    private router: Router,
    private helperService: HelperService,
    private loginService: LoginService
  ) {

    this.loginService.isAccessoDiretto(
    ).subscribe(accediDirettamenteAllaHomepage => {
      this.accessoDiretto = accediDirettamenteAllaHomepage;
    });
    router.events.pipe(
      filter(e => e instanceof NavigationError)
    ).subscribe(e => {
      const ne = e as NavigationError;
      this.modalService.simpleError(Utils.toMessage(ne.error), ne.error.error.errore);
    });

    router.events.pipe(
      filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        /* ad oggi ci sono 3 livelli di profondita' delle route
        *  1) pagina root
        *  2) children dichiarati nel routing direttamente
        *  3) children dichiarati tramite loadChildren con passaggio su modulo dedicato
        *  In base a questi livelli ricerco il route corretto su cui instradare la ricerca dell' helper
        */
        if (this.route.firstChild?.firstChild?.firstChild) {
          this.callSearchHelper(this.route.firstChild?.firstChild?.firstChild);
        } else if (this.route.firstChild?.firstChild) {
          this.callSearchHelper(this.route.firstChild?.firstChild);
        } else {
          this.callSearchHelper(this.route.firstChild);
        }
      });
  }

  private scrollYThreshold = 200;
  stickHeader = false;
  helper!: Helper | null;
  child!: ActivatedRoute | null;
  children!: ActivatedRoute[] | null;
  accessoDiretto = false;

  valorePreferenzeEnte: ValorePreferenzeEnte = { header: '', logo: '', logoFooter: '', logoFooterCentrale: '', logoFooterDestra: '' };

  principal: UserInfoWrapper | null = null;

  deployContextPath = environment.deployContextPath;

  private preferenzeEnteSubscription: Subscription | null = null;

  @HostListener('window:scroll', ['$event']) onScroll(event: any) {
    if (event.path && event.path.length > 1) {
      this.stickHeader = event.path[1].scrollY > this.scrollYThreshold;
    }
  }

  ngOnInit(): void {
    this.securityService.principal$.subscribe(newPrincipal => {
      this.principal = newPrincipal;
      this.loginService.setAccessoDiretto(newPrincipal.accessoDiretto ?? false);
    });
    this.preferenzeEnteSubscription = this.preferenzeEnteService.subscribePreferenze.subscribe(preferenze => {
      if (preferenze?.valore) {
        this.valorePreferenzeEnte = JSON.parse(preferenze.valore);
      } else {
        this.ngOnDestroy();
        this.securityService.hasUseCases(['ADMIN_ENTE'], false).subscribe(response => {
          if (response) {
            this.modalService.info(this.translateService.instant('errori.nuove_configurazioni_ente'),
              this.translateService.instant('common.vai_alle_preferenze')).then(() => {
                this.preferenzeEnteSubscription = this.preferenzeEnteService.subscribePreferenze.subscribe(preferenzeNew => {
                  if (preferenzeNew?.valore) {
                    this.valorePreferenzeEnte = JSON.parse(preferenzeNew.valore);
                  }
                });
                this.router.navigate(['/preferenze-ente']);
              });
          }
        });
      }
    });
  }

  ngOnDestroy(): void {
    if (this.preferenzeEnteSubscription) {
      this.preferenzeEnteSubscription.unsubscribe();
    }
  }

  getLogoToview() {
    return this.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,' + this.valorePreferenzeEnte.logo);
  }

  logout() {
    this.modalService.logout();
  }

  openIntranet() {

    const intranet = Utils.performEnvironmentRelatedAction(EnvironmentRelatedFunction.INTRANET);

    window.open(intranet as string, '_blank');
  }

  private isCustomFormHelperRequest(codiceTab: string) {
    return this.formLogiciCfg && this.formLogiciCfg?.find(flc => flc.description === codiceTab && flc.tabName === 'CUSTOM-FORM');
  }

  private getHelperByCustomForm(codicePagina: string, codiceTab: string): Observable<HelperResponse> {
    const filters = this.setHelperFilters(codicePagina, 'CUSTOM-FORM', codiceTab);
    return this.helperService.getHelpers(JSON.stringify(filters));

  }

  private getHelper(codicePagina: string, codiceTab: string): Observable<HelperResponse> {
    const filters = this.setHelperFilters(codicePagina, codiceTab, '');
    return this.helperService.getHelpers(JSON.stringify(filters));
  }

  private setHelperFilters(codicePagina: string, codiceTab: string, codiceForm: string) {
    const output: any = {
      filter: {},
    };

    output.filter.codicePagina = {
      ci: codicePagina
    };

    if (codiceTab) {
      output.filter.codiceTab = {
        ci: codiceTab
      };
    }

    if (codiceForm) {
      output.filter.codiceForm = {
        ci: codiceForm
      };
    }

    return output;
  }

  private helperSearch(data: Data) {

    let codiceTab = '';
    this.helper = null;
    if (data?.codicePagina) {

      // in caso il codice della pagina riguardi il route delle pagine di errore, non gestisco la ricerca e il suo risultato
      if (data?.codicePagina === 'errorPage') { return; }
      // ad oggi, se è presente la chiave isTab, l'helper riguarderà un tab o un form, altrimenti una pagina classica, se presente
      if (data.isTab) {
        /* verifico la presenza dei form logici e la funzionalità di ognuno:
            l' array formlogicicfg sarà popolato
            in caso di pagina: lavorazione_pratica e presenza form logici
            vuoto, altrimenti (dettaglio_pratica)
        */
        this.formLogiciCfg = this.route.children[0]?.children[0]?.snapshot?.data?.formLogiciContext?.formLogici;
        codiceTab = this.route.firstChild?.snapshot.queryParams.tab ?? '';
        if (!codiceTab && this.formLogiciCfg) {
          codiceTab = this.formLogiciCfg[0].description ?? '';
        }

      }
      if (this.isCustomFormHelperRequest(codiceTab)) {
        this.helperService.getDecodifica(data?.codicePagina, 'CUSTOM-FORM', codiceTab).subscribe(decodifica => {
          if (decodifica && decodifica.codice) {
            this.hasPageHelper.emit(true);
            this.getHelperByCustomForm(data?.codicePagina, codiceTab).subscribe(h => {
              this.helper = h && h.helpers ? h.helpers[0] : null;
            });
          } else {
            this.hasPageHelper.emit(false);
          }
        });
      } else {
        this.helperService.getDecodifica(data?.codicePagina, codiceTab, '').subscribe(decodifica => {
          if (decodifica && decodifica.codice) {
            this.hasPageHelper.emit(true);
            if (data.codicePagina === 'dettaglio_pratica' && decodifica.codice === 'docs') {
              codiceTab = codiceTab.concat('-dettaglio');
            }
            this.getHelper(data?.codicePagina, codiceTab).subscribe(h => {
              h.helpers = h.helpers?.filter(x => x.codiceModale == null);
              this.helper = h && h.helpers ? h.helpers.length > 1 ?
                h.helpers.filter(x => x?.codiceTab?.codice === codiceTab)[0] : h.helpers[0]
                : null;
            });
          } else {
            this.hasPageHelper.emit(false);
          }
        });
      }
    } else {
      this.hasPageHelper.emit(false);
    }
  }

  private callSearchHelper(route: any) {
    route.data.subscribe((data: Data) => {
      this.helperSearch(data);
    });
  }

}
