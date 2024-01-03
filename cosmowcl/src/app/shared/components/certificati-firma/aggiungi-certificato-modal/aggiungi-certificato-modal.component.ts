/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { CertificatoFirma } from 'src/app/shared/models/api/cosmoauthorization/certificatoFirma';
import { EnteCertificatore } from 'src/app/shared/models/api/cosmobusiness/enteCertificatore';
import { ValorePreferenzeEnte } from 'src/app/shared/models/preferenze/valore-preferenze-ente.model';
import { PreferenzeEnteService } from 'src/app/shared/services/preferenze-ente.service';
import { ImpostazioniFirmaComponent } from '../../impostazioni-firma/impostazioni-firma.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { GestioneTipiPraticheService } from 'src/app/administration/gestione-tipi-pratiche/gestione-tipi-pratiche.service';
import { ModaleParentComponent } from 'src/app/modali/modale-parent-component';
import { HelperService } from 'src/app/shared/services/helper.service';

@Component({
  selector: 'app-aggiungi-certificato-modal',
  templateUrl: './aggiungi-certificato-modal.component.html',
  styleUrls: ['./aggiungi-certificato-modal.component.scss']
})
export class AggiungiCertificatoModalComponent extends ModaleParentComponent implements OnInit, AfterViewInit, OnDestroy{

  @ViewChild('impostazioniFirma') impostazioniFirmaComp!: ImpostazioniFirmaComponent;

  private preferenzeEnteSubscription!: Subscription;

  idCertificato: number | null = null;
  certificatoFirmaForm!: FormGroup;
  showPassword = false;
  showPin = false;
  canManagePasswordField = false;
  codiceTipoPratica = '';
  certificatoFirma: CertificatoFirma | null = null;
  impostazioniFirmaEnte: any = null;
  codicePagina!: string;
  codiceTab!: string;

  constructor(
    public modal: NgbActiveModal,
    private preferenzeEnteService: PreferenzeEnteService,
    private tipoPraticaService: GestioneTipiPraticheService,
    public helperService: HelperService) {
      super(helperService);
      this.setModalName('aggiungi-certificato');
  }


  ngOnInit(): void {
    this.initForm();
  }

  ngAfterViewInit(): void {
      this.preferenzeEnteSubscription = this.preferenzeEnteService.subscribePreferenze.subscribe(preferenze => {
          const valorePreferenzeEnte = preferenze.valore ? JSON.parse(preferenze.valore) as ValorePreferenzeEnte : null;


          if (valorePreferenzeEnte?.impostazioniFirma) {
            this.impostazioniFirmaEnte = valorePreferenzeEnte.impostazioniFirma;
          }
          if (this.codiceTipoPratica){
            this.tipoPraticaService.get(this.codiceTipoPratica).subscribe(tipoPratica => {
              if (tipoPratica && (tipoPratica.enteCertificatore || tipoPratica.tipoOtp
                || tipoPratica.tipoCredenziale || tipoPratica.profiloFEQ || tipoPratica.sceltaMarcaTemporale)){
                  this.impostazioniFirmaEnte = {
                    enteCertificatore: tipoPratica.enteCertificatore ?? null,
                    tipoOTP: tipoPratica.tipoOtp ?? null,
                    tipoCredenzialiFirma: tipoPratica.tipoCredenziale ?? null,
                    profiloFEQ: tipoPratica.profiloFEQ ?? null,
                    sceltaMarcaTemporale: tipoPratica.sceltaMarcaTemporale ?? null
                  };

                }
              this.impostazioniFirma();
            });
          }else{
            this.impostazioniFirma();
          }

      });
      this.searchHelperModale(this.codicePagina, this.codiceTab);
  }


  initForm() {
    this.certificatoFirmaForm = new FormGroup({
      descrizione: new FormControl(null, Validators.required),
      username: new FormControl(null, Validators.required),
      pin: new FormControl(null, Validators.required),
      scadenza: new FormControl(null)
    });
  }

  impostazioniFirma() {

    this.impostazioniFirmaComp.enteCertificatore = this.impostazioniFirmaEnte?.enteCertificatore ?? null;

    this.impostazioniFirmaComp.profiloFEQ = this.impostazioniFirmaEnte?.profiloFEQ ?? null;

    this.impostazioniFirmaComp.sceltaMarcaTemporale = this.impostazioniFirmaEnte?.sceltaMarcaTemporale ?? null;

    this.impostazioniFirmaComp.tipoCredenzialiFirma = this.impostazioniFirmaEnte?.tipoCredenzialiFirma ?? null;

    this.impostazioniFirmaComp.tipoOTP =  this.impostazioniFirmaEnte?.tipoOTP ?? null;

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


  disabilitaSalvaCertificato() {
    return !this.certificatoFirmaForm.valid ||
      (!this.impostazioniFirmaComp || (this.impostazioniFirmaComp &&
        !this.impostazioniFirmaComp.enteCertificatore || !this.impostazioniFirmaComp.tipoCredenzialiFirma ||
        !this.impostazioniFirmaComp.tipoOTP || !this.impostazioniFirmaComp.profiloFEQ ||
        !this.impostazioniFirmaComp.sceltaMarcaTemporale));
  }

  selezionaCertificato() {
    if (this.certificatoFirmaForm.valid && this.impostazioniFirmaComp &&
      this.impostazioniFirmaComp.enteCertificatore && this.impostazioniFirmaComp.tipoCredenzialiFirma &&
      this.impostazioniFirmaComp.tipoOTP && this.impostazioniFirmaComp.profiloFEQ &&
      this.impostazioniFirmaComp.sceltaMarcaTemporale) {

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

        this.modal.close(certificato);

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
