/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { DatePipe } from '@angular/common';
import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { CertificatiService } from 'src/app/shared/components/certificati-firma/certificati/certificati.service';
import { ImpostazioniFirmaComponent } from 'src/app/shared/components/impostazioni-firma/impostazioni-firma.component';
import { CertificatoFirma } from 'src/app/shared/models/api/cosmoauthorization/certificatoFirma';
import { EnteCertificatore } from 'src/app/shared/models/api/cosmoauthorization/enteCertificatore';
import { ValorePreferenzeEnte } from 'src/app/shared/models/preferenze/valore-preferenze-ente.model';
import { ModalService } from 'src/app/shared/services/modal.service';
import { PreferenzeEnteService } from 'src/app/shared/services/preferenze-ente.service';
import { Utils } from 'src/app/shared/utilities/utilities';

@Component({
  selector: 'app-aggiungi-modifica-certificato-firma',
  templateUrl: './aggiungi-modifica-certificato-firma.component.html',
  styleUrls: ['./aggiungi-modifica-certificato-firma.component.scss']
})
export class AggiungiModificaCertificatoFirmaComponent implements OnInit, OnDestroy, AfterViewInit {

  @ViewChild('impostazioniFirma') impostazioniFirmaComp!: ImpostazioniFirmaComponent;

  private preferenzeEnteSubscription!: Subscription;

  idCertificato: number | null = null;

  certificatoFirmaForm!: FormGroup;
  showPassword = false;
  showPin = false;
  canManagePasswordField = false;

  certificatoFirma: CertificatoFirma | null = null;

  impostazioniFirmaEnte: any = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private datePipe: DatePipe,
    private translateService: TranslateService,
    private modalService: ModalService,
    private preferenzeEnteService: PreferenzeEnteService,
    private certificatiService: CertificatiService) {
  }


  ngOnInit(): void {
    this.initForm();
    this.route.params.subscribe(paramCertificato => {
      if (paramCertificato.id) {
        this.idCertificato = paramCertificato.id;
      }
    });
  }

  ngAfterViewInit(): void {
    if (this.idCertificato) {
      this.getCertificato();
    } else {
      this.preferenzeEnteSubscription = this.preferenzeEnteService.subscribePreferenze.subscribe(preferenze => {
        if (preferenze?.valore) {
          const valorePreferenzeEnte = JSON.parse(preferenze.valore) as ValorePreferenzeEnte;

          if (valorePreferenzeEnte?.impostazioniFirma) {
            this.impostazioniFirmaEnte = valorePreferenzeEnte.impostazioniFirma;
            setTimeout(() => this.impostazioniFirma(), 0);
          }
        }
      });
    }
  }

  getCertificato() {
    this.certificatiService.getCertificato(this.idCertificato ?? 0).subscribe(response => {
      this.certificatoFirma = response;
      this.initForm();
      this.impostazioniFirma();
    });
  }

  initForm() {
    this.certificatoFirmaForm = new FormGroup({
      descrizione: new FormControl(this.certificatoFirma?.descrizione ?? null, Validators.required),
      username: new FormControl(this.certificatoFirma?.username ?? null, Validators.required),
      pin: new FormControl(this.certificatoFirma?.pin ?? null, Validators.required),
      password: new FormControl( this.certificatoFirma?.password ?? null),
      scadenza: new FormControl(this.certificatoFirma?.dataScadenza ?
          this.datePipe.transform(this.certificatoFirma?.dataScadenza, 'yyyy-MM-dd') : null)
    });

  }

  impostazioniFirma() {

    this.impostazioniFirmaComp.enteCertificatore = this.idCertificato ? this.certificatoFirma?.enteCertificatore ?? null
      : this.impostazioniFirmaEnte?.enteCertificatore ?? null;

    this.impostazioniFirmaComp.profiloFEQ = this.idCertificato ? this.certificatoFirma?.profiloFEQ ?? null
      : this.impostazioniFirmaEnte?.profiloFEQ ?? null;

    this.impostazioniFirmaComp.sceltaMarcaTemporale = this.idCertificato ? this.certificatoFirma?.sceltaMarcaTemporale ?? null
      : this.impostazioniFirmaEnte?.sceltaMarcaTemporale ?? null;

    this.impostazioniFirmaComp.tipoCredenzialiFirma = this.idCertificato ? this.certificatoFirma?.tipoCredenzialiFirma ?? null
      : this.impostazioniFirmaEnte?.tipoCredenzialiFirma ?? null;

    this.impostazioniFirmaComp.tipoOTP = this.idCertificato ? this.certificatoFirma?.tipoOTP ?? null
      : this.impostazioniFirmaEnte?.tipoOTP ?? null;

    this.impostazioniFirmaComp.setValoreSelezionatoImpFirma();

    if (this.impostazioniFirmaComp.enteCertificatore) {
      this.enteCertificatoreSelezionato(this.impostazioniFirmaComp.enteCertificatore);
    }
  }

  mostrarePassword() {
    this.showPassword = !this.showPassword;
  }

  mostrarePin() {
    this.showPin = !this.showPin;
  }


  tornaIndietro() {
    this.router.navigate(['back'], { relativeTo: this.route });
  }

  resetCertificato() {
    if (this.idCertificato) {
      this.getCertificato();
    } else {
      this.certificatoFirma = null;
      this.initForm();
      this.impostazioniFirmaComp.enteCertificatore = null;
      this.impostazioniFirmaComp.profiloFEQ = null;
      this.impostazioniFirmaComp.sceltaMarcaTemporale = null;
      this.impostazioniFirmaComp.tipoCredenzialiFirma = null;
      this.impostazioniFirmaComp.tipoOTP = null;

      this.impostazioniFirmaComp.setValoreSelezionatoImpFirma();
    }
  }

  disabilitaSalvaCertificato() {
    return !this.certificatoFirmaForm.valid ||
      (!this.impostazioniFirmaComp || (this.impostazioniFirmaComp &&
        !this.impostazioniFirmaComp.enteCertificatore || !this.impostazioniFirmaComp.tipoCredenzialiFirma ||
        !this.impostazioniFirmaComp.tipoOTP || !this.impostazioniFirmaComp.profiloFEQ ||
        !this.impostazioniFirmaComp.sceltaMarcaTemporale));
  }

  aggiornaSalvaCertificato() {
    if (this.certificatoFirmaForm.valid && this.impostazioniFirmaComp &&
      this.impostazioniFirmaComp.enteCertificatore && this.impostazioniFirmaComp.tipoCredenzialiFirma &&
      this.impostazioniFirmaComp.tipoOTP && this.impostazioniFirmaComp.profiloFEQ &&
      this.impostazioniFirmaComp.sceltaMarcaTemporale) {

      if (this.certificatoFirma?.id) {
        this.certificatoFirma.descrizione = this.certificatoFirmaForm.get('descrizione')?.value;
        this.certificatoFirma.username = this.certificatoFirmaForm.get('username')?.value;
        this.certificatoFirma.pin = this.certificatoFirmaForm.get('pin')?.value;
        this.certificatoFirma.password = this.certificatoFirmaForm.get('password')?.value;
        this.certificatoFirma.dataScadenza = this.certificatoFirmaForm.get('scadenza')?.value
          ? new Date(this.certificatoFirmaForm.get('scadenza')?.value).toISOString() : undefined;
        this.certificatoFirma.enteCertificatore = this.impostazioniFirmaComp.enteCertificatore;
        this.certificatoFirma.profiloFEQ = this.impostazioniFirmaComp.profiloFEQ;
        this.certificatoFirma.sceltaMarcaTemporale = this.impostazioniFirmaComp.sceltaMarcaTemporale;
        this.certificatoFirma.tipoCredenzialiFirma = this.impostazioniFirmaComp.tipoCredenzialiFirma;
        this.certificatoFirma.tipoOTP = this.impostazioniFirmaComp.tipoOTP;

        this.certificatiService.aggiornaCertificato(this.certificatoFirma.id, this.certificatoFirma).subscribe(
          response => {
            this.modalService.info(
              this.translateService.instant('firma_digitale.saltavaggio_certificato'),
              this.translateService.instant('firma_digitale.salvataggio_certificato_msg')
              )
              .then(() => {
                this.tornaIndietro();
              }
              ).catch(() => { });
          },
          error => {
            const msg = this.certificatoFirmaForm.get('descrizione')?.value ?? 'Certificato';

            const messaggioErrore = Utils.parseAndReplacePlaceholders(
              this.translateService.instant('errori.aggiornamento_certificazione_msg'),
              [msg]);

            this.modalService.error(this.translateService.instant('firma_digitale.saltavaggio_certificato'),
              messaggioErrore, error.error.errore)
              .then(() => { })
              .catch(() => { });
          });
      } else {
        const certificato: CertificatoFirma = {
          descrizione: this.certificatoFirmaForm.get('descrizione')?.value,
          username: this.certificatoFirmaForm.get('username')?.value,
          pin: this.certificatoFirmaForm.get('pin')?.value,
          password: this.certificatoFirmaForm.get('password')?.value,
          dataScadenza: this.certificatoFirmaForm.get('scadenza')?.value
            ? new Date(this.certificatoFirmaForm.get('scadenza')?.value).toISOString() : undefined,
          enteCertificatore: this.impostazioniFirmaComp.enteCertificatore,
          profiloFEQ: this.impostazioniFirmaComp.profiloFEQ,
          sceltaMarcaTemporale: this.impostazioniFirmaComp.sceltaMarcaTemporale,
          tipoCredenzialiFirma: this.impostazioniFirmaComp.tipoCredenzialiFirma,
          tipoOTP: this.impostazioniFirmaComp.tipoOTP
        };

        this.certificatiService.salvaCertificato(certificato).subscribe(
          () => {
            this.modalService.info(this.translateService.instant('firma_digitale.saltavaggio_certificato'),
              this.translateService.instant('firma_digitale.salvataggio_certificato_msg'))
              .then(() => {
                this.tornaIndietro();
              }
              ).catch(() => { });
          },
          error => {
            const msg = certificato.descrizione ?? 'Certificato';

            const messaggioErrore = Utils.parseAndReplacePlaceholders(
              this.translateService.instant('errori.creazione_certificazione_msg'),
              [msg]);

            this.modalService.error(this.translateService.instant('firma_digitale.saltavaggio_certificato'),
              messaggioErrore, error.error.errore)
              .then(() => { })
              .catch(() => { });
          });
      }
    }
  }

  ngOnDestroy(): void {
    if (this.preferenzeEnteSubscription) {
      this.preferenzeEnteSubscription.unsubscribe();
    }
  }

  enteCertificatoreSelezionato(enteCertificatore: EnteCertificatore) {
    this.canManagePasswordField = enteCertificatore.codice === 'Uanataca';
    if (this.canManagePasswordField) {
      this.certificatoFirmaForm.addControl('password',
        new FormControl(this.certificatoFirma?.password ?
          this.certificatoFirma?.password : null, Validators.required));
    } else {
      this.certificatoFirmaForm.removeControl('password');
    }
  }

}
