/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, } from '@angular/core';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { forkJoin } from 'rxjs';
import { ApplicazioneEsterna } from 'src/app/shared/models/api/cosmoauthorization/applicazioneEsterna';
import { AppEsterneService } from 'src/app/shared/services/app-esterne.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { TranslateService } from '@ngx-translate/core';
import { FunzionalitaApplicazioneEsterna } from 'src/app/shared/models/api/cosmoauthorization/funzionalitaApplicazioneEsterna';

@Component({
  selector: 'app-app-esterne',
  templateUrl: './app-esterne.component.html',
  styleUrls: ['./app-esterne.component.scss'],
})
export class AppEsterneComponent implements OnInit {

  applicazioniConfigurabili: ApplicazioneEsterna[] = [];
  applicazioniConfigurate: ApplicazioneEsterna[] = [];
  applicazioniConfigurateOnDB: ApplicazioneEsterna[] = [];
  hiddenButton = true;

  constructor(
    private modalService: ModalService,
    private translateService: TranslateService,
    private appEsterneService: AppEsterneService) { }

  ngOnInit(): void {

    this.appEsterneService.getShowMenu().subscribe(showMenu => {
      this.hiddenButton = showMenu;

      if (showMenu) {
        this.getApp();
      }
    });

    this.appEsterneService.getReloadMenu().subscribe(reloadMenu => {
      if (reloadMenu) {
        this.getApp();
      }
    });
  }

  getApp() {
    forkJoin({
      allApp: this.appEsterneService.getAppEsterne(false),
      confApp: this.appEsterneService.getAppEsterne(true)
    })
      .subscribe(result => {
        this.setApp(result.confApp, result.allApp);
      });
  }

  setApp(confApp: ApplicazioneEsterna[], allApp: ApplicazioneEsterna[]) {

    this.applicazioniConfigurabili = [];
    this.applicazioniConfigurate = [];
    this.applicazioniConfigurateOnDB = [];


    let configurati= confApp.sort((a,b)=>(a.posizione ?? 0) - (b.posizione ?? 0) );
    configurati.forEach(app => {
      const newApp = { ...app };
      newApp.funzionalita = app.funzionalita?.slice();
      this.applicazioniConfigurate.push(newApp);

    });

    

    confApp.forEach(app => {
      const newApp = { ...app };
      newApp.funzionalita = app.funzionalita?.slice();
      this.applicazioniConfigurateOnDB.push(newApp);
    });

    if (this.applicazioniConfigurate && this.applicazioniConfigurate.length > 0) {

      allApp.forEach(appConfigurabile => {
        if (this.applicazioniConfigurate.some(singolaAppConfigurata => singolaAppConfigurata.id === appConfigurabile.id)) {
          const appConfig = this.applicazioniConfigurate.find(singolaAppConfigurata => singolaAppConfigurata.id === appConfigurabile.id);

          if (appConfig) {
            const funzionalitaNonConfigurate = appConfigurabile.funzionalita?.filter(x => {
              return !appConfig.funzionalita?.some(f => f.id === x.id);
            });

            appConfigurabile.funzionalita = funzionalitaNonConfigurate;
          }
          if (appConfigurabile && appConfigurabile.funzionalita && appConfigurabile.funzionalita.length > 0) {
            this.applicazioniConfigurabili.push(appConfigurabile);
          }


        } else {
          this.applicazioniConfigurabili.push(appConfigurabile);
        }

      });
    } else {
      this.applicazioniConfigurabili = allApp;
    }

    this.sortDataAlph();


  }

  getAppTitle(app: ApplicazioneEsterna, configurabile: boolean): string {
    let descrizione = 'Non definita';

    if (app.funzionalita) {
      const principale = app.funzionalita.find(singolaFunzionalita => singolaFunzionalita.principale);

      if (principale) {
        descrizione = principale.descrizione;
      } else {
        if (configurabile) {
          descrizione = this.applicazioniConfigurate.find(singolaApp => app.descrizione === singolaApp.descrizione)
            ?.funzionalita?.find(singolaFunzionalita => singolaFunzionalita.principale)?.descrizione ?? descrizione;
        }
      }
    }

    return descrizione;
  }

  getAppLink(app: ApplicazioneEsterna) {
    if (app.funzionalita) {
      const url = app.funzionalita.find(singolaFunzionalita => singolaFunzionalita.principale)?.url;
      if (url) {
        this.goToUrl(url);
      }
    }
  }

  getFunzionalitaLink(funzionalita: FunzionalitaApplicazioneEsterna) {
    this.goToUrl(funzionalita.url);
  }

  goToUrl(url: string) {
    if (/^http[s]?:\/\//.test(url)) {
      window.open(url, '_blank');
    } else {
      window.open('//' + url, '_blank');
    }
  }

  getLogo(icona: string) {
    if (icona) {
      return 'data:image/png;base64,' + icona;
    }
  }

  dropDownApp(app: ApplicazioneEsterna): boolean {
    if (app.funzionalita && app.funzionalita.length > 0 && app.funzionalita?.some(t => !t.principale)) {
      return false;
    }
    return true;

  }

  funzionalitaNonPrincipali(app: ApplicazioneEsterna): boolean {
    if (app.funzionalita && app.funzionalita.some(funz => !funz.principale)) {
      return true;
    }
    return false;
  }

  spostaConfigurate(app: ApplicazioneEsterna) {
    const index = this.applicazioniConfigurate.indexOf(app);
    if (index !== -1) {
      this.applicazioniConfigurate.splice(index, 1);
      const appConfigurabile = this.applicazioniConfigurabili.find(appConf => app.id === appConf.id);
      if (appConfigurabile) {
        if (appConfigurabile.funzionalita) {
          app.funzionalita?.forEach(funz => appConfigurabile.funzionalita?.push(funz));
        } else {
          appConfigurabile.funzionalita = app.funzionalita;
        }
      } else {
        this.applicazioniConfigurabili.push(app);
      }
      
      this.sortDataAlph();
    }
  }

  spostaFunzionalitaConfigurate(app: ApplicazioneEsterna, funzionalita: FunzionalitaApplicazioneEsterna) {

    const appConfigurata = this.applicazioniConfigurate.find(appConf => app.id === appConf.id);
    if (appConfigurata && appConfigurata.funzionalita) {
      const index = appConfigurata.funzionalita.indexOf(funzionalita);
      if (index !== -1) {
        appConfigurata.funzionalita.splice(index, 1);
      }
    }

    const appConfigurabile = this.applicazioniConfigurabili.find(appConf => app.id === appConf.id);

    if (appConfigurabile) {
      if (appConfigurabile.funzionalita) {
        if (!appConfigurabile.funzionalita.some(funzConf => funzionalita.id === funzConf.id)) {
          appConfigurabile.funzionalita.push(funzionalita);
        }
      } else {
        appConfigurabile.funzionalita = [funzionalita];
      }
    } else {
      const appNuova = { ...app };
      appNuova.funzionalita = [funzionalita];
      this.applicazioniConfigurabili.push(appNuova);
    }

    
    this.sortDataAlph();
  }

  sortDataAlph() {
    

    this.applicazioniConfigurate.forEach(appConfigurata =>
      appConfigurata.funzionalita?.sort((o1, o2) =>
        (o1.descrizione.toUpperCase() ?? '').localeCompare((o2.descrizione.toUpperCase() ?? '')))
    );

    this.applicazioniConfigurabili
      .sort((o1, o2) => (o1.descrizione.toUpperCase() ?? '').localeCompare((o2.descrizione.toUpperCase() ?? '')));

    this.applicazioniConfigurabili.forEach(appConfigurabile =>
      appConfigurabile.funzionalita?.sort((o1, o2) =>
        (o1.descrizione.toUpperCase() ?? '').localeCompare((o2.descrizione.toUpperCase() ?? '')))
    );
  }

  showButton() {
    this.hiddenButton = !this.hiddenButton;

    if (this.hiddenButton) {
      this.associaDissociaApp();
    }
  }

  drop(event: CdkDragDrop<string[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {

      if (!isNaN(Number(event.previousContainer.id))) {
        this.aggiungiFunzionalita(event);

      } else {
        this.aggiungiApplicazioni(event);
      }

    }

    this.sortDataAlph();
  }

  aggiungiFunzionalita(event: CdkDragDrop<string[]>) {
    const idApp = Number(event.previousContainer.id);

    const funzionalita = JSON.parse(
      JSON.stringify(event.previousContainer.data[event.previousIndex])) as FunzionalitaApplicazioneEsterna;

    const applicazioneConfigurata = this.applicazioniConfigurate.find(app => idApp === app.id);

    if (applicazioneConfigurata) {
      applicazioneConfigurata.funzionalita?.push(funzionalita);
      const applicazioneConfigurabile = this.applicazioniConfigurabili.find(app => idApp === app.id);
      if (applicazioneConfigurabile) {
        applicazioneConfigurabile.funzionalita?.splice(event.previousIndex, 1);

        if (!applicazioneConfigurabile.funzionalita || applicazioneConfigurabile.funzionalita.length === 0) {
          const indexAppConfigurabile = this.applicazioniConfigurabili.indexOf(applicazioneConfigurabile);
          if (indexAppConfigurabile !== -1) {
            this.applicazioniConfigurabili.splice(indexAppConfigurabile, 1);
          }
        }
      }
    } else {

      const applicazioneConfigurabile = this.applicazioniConfigurabili.find(app => idApp === app.id);
      if (applicazioneConfigurabile) {
        const nuovaApp = { ...applicazioneConfigurabile };
        nuovaApp.funzionalita = [];

        const funzionalitaPrincipale = applicazioneConfigurabile.funzionalita?.find(funz => funz.principale);

        if (funzionalitaPrincipale && applicazioneConfigurabile.funzionalita) {
          const indexFunzPrincipale = applicazioneConfigurabile.funzionalita.indexOf(funzionalitaPrincipale);
          if (indexFunzPrincipale !== -1) {
            nuovaApp.funzionalita.push(funzionalitaPrincipale);
            applicazioneConfigurabile.funzionalita?.splice(indexFunzPrincipale, 1);
          }
        }

        const funzionalitaTemp = applicazioneConfigurabile.funzionalita?.find(funz => funzionalita.id === funz.id);
        if (funzionalitaTemp && applicazioneConfigurabile.funzionalita) {
          const indexFunz = applicazioneConfigurabile.funzionalita.indexOf(funzionalitaTemp);
          if (indexFunz !== -1) {
            nuovaApp.funzionalita.push(funzionalita);
            applicazioneConfigurabile.funzionalita?.splice(indexFunz, 1);
          }
        }

        this.applicazioniConfigurate.push(nuovaApp);

        if (!applicazioneConfigurabile.funzionalita || applicazioneConfigurabile.funzionalita.length === 0) {
          const indexAppConfigurabile = this.applicazioniConfigurabili.indexOf(applicazioneConfigurabile);
          if (indexAppConfigurabile !== -1) {
            this.applicazioniConfigurabili.splice(indexAppConfigurabile, 1);
          }
        }
      }
    }
  }

  aggiungiApplicazioni(event: CdkDragDrop<string[]>) {
    transferArrayItem(event.previousContainer.data,
      event.container.data,
      event.previousIndex,
      event.currentIndex);
    this.applicazioniConfigurate.forEach(applicazioneConfigurata => {

      const index = this.applicazioniConfigurate.indexOf(applicazioneConfigurata);
      const appDuplicata = this.applicazioniConfigurate
        .find(app => app.id === applicazioneConfigurata.id && index !== this.applicazioniConfigurate.indexOf(app));
      if (appDuplicata) {
        if (appDuplicata.funzionalita) {
          appDuplicata.funzionalita.forEach(funzionalita => applicazioneConfigurata.funzionalita?.push(funzionalita));
          const indexDuplicato: number = this.applicazioniConfigurate.indexOf(appDuplicata);
          if (indexDuplicato !== -1) {
            this.applicazioniConfigurate.splice(indexDuplicato, 1);
          }
        }
      }
    });
  }

  associaDissociaApp() {
   
    this.applicazioniConfigurate.forEach(elem=>elem.posizione=this.applicazioniConfigurate.indexOf(elem));
    this.chiamataAssociazione(this.applicazioniConfigurate);
   
  }

  chiamataAssociazione(app: ApplicazioneEsterna[]) {
    forkJoin({
      confApp: this.appEsterneService.associaDissociaAppUtente(app),
      allApp: this.appEsterneService.getAppEsterne(false)
    })
      .subscribe(result => {
        this.setApp(result.confApp, result.allApp);
      }, error => {
        this.modalService.error(this.translateService.instant('app_esterne.associazione_applicazioni'),
          this.translateService.instant('errori.errore_associazione_applicazioni'), error.error.errore,
          this.translateService.instant('common.ok'))
          .then(() => this.getApp())
          .catch(() => { });
      });
  }


}
