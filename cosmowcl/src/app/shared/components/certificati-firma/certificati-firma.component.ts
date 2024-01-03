/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { DatePipe } from '@angular/common';
import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NgbModal, NgbModalOptions, NgbTypeahead } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { Subject } from 'rxjs';
import { CertificatoFirma } from '../../models/api/cosmoauthorization/certificatoFirma';
import { EnteCertificatore } from '../../models/api/cosmoauthorization/enteCertificatore';
import { ModalService } from '../../services/modal.service';
import { Utils } from '../../utilities/utilities';
import { ImpostazioniFirmaComponent } from '../impostazioni-firma/impostazioni-firma.component';
import { CertificatiService } from './certificati/certificati.service';
import { TipoPratica } from '../../models/api/cosmobusiness/tipoPratica';
import { SelezionaCertificatoComponent } from './seleziona-certificato/seleziona-certificato.component';
import { HelperService } from '../../services/helper.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-certificati-firma',
  templateUrl: './certificati-firma.component.html',
  styleUrls: ['./certificati-firma.component.scss']
})
export class CertificatiFirmaComponent implements OnInit {

  constructor(
    private certificatiService: CertificatiService,
    private datePipe: DatePipe,
    private translateService: TranslateService,
    private modalService: ModalService,
    private modal: NgbModal,
    public helperService: HelperService,
    private route: ActivatedRoute) { }

  @ViewChild('impostazioniFirma') impostazioniFirmaComp!: ImpostazioniFirmaComponent;


  @Input() profiliFeqAbilitati: string[] = [];
  @Input() codiceTipoPratica?: string;

  showPassword = false;
  showPin = false;

  impostazioniFirmaDisabilitate = false;

  certificatiFirmaSelezionata: CertificatoFirma | null = null;

  public certificatoFirmaForm!: FormGroup;
  canManagePasswordField = false;

  loading = 0;

  @ViewChild('instance', { static: true }) instance!: NgbTypeahead;

  focus$ = new Subject<string>();
  click$ = new Subject<string>();

  formatter = (result: CertificatoFirma) => result.descrizione;



  togglePinField() {
    this.showPin = !this.showPin;
  }

  togglePasswordField() {
    this.showPassword = !this.showPassword;
  }

  get descrizioneCertificato(){
    return this.certificatiFirmaSelezionata?.descrizione ?? null;
  }

  ngOnInit(): void {

    this.certificatoFirmaForm = new FormGroup({
      username: new FormControl({value: null, disabled: this.certificatiFirmaSelezionata === null }, [Validators.required], []),
      pin: new FormControl({value: null, disabled: this.certificatiFirmaSelezionata === null }, [Validators.required], []),
      scadenza: new FormControl({value: null, disabled: this.certificatiFirmaSelezionata === null }, [], [])
    });

    this.impostazioniFirmaDisabilitate = this.certificatiFirmaSelezionata === null;

    this.certificatiService.getCertificati().subscribe(response => {

      this.certificatiFirmaSelezionata = response.find(elem => elem.ultimoUtilizzato === true) ?? null;

      if (this.certificatiFirmaSelezionata) {
        this.aggiornaValori();
      }

    });
  }

  initForm() {

    this.certificatoFirmaForm = new FormGroup({
      username: new FormControl({value: (this.certificatiFirmaSelezionata ? this.certificatiFirmaSelezionata.username : null),
                disabled: true}, [Validators.required], []),
      pin: new FormControl({value: (this.certificatiFirmaSelezionata ? this.certificatiFirmaSelezionata.pin : null), disabled: true},
          [Validators.required], []),
      scadenza: new FormControl({ value: (this.certificatiFirmaSelezionata ?
                this.datePipe.transform(this.certificatiFirmaSelezionata?.dataScadenza, 'yyyy-MM-dd') : null), disabled: true  }, [], [])
    }, [], []);


    this.impostazioniFirmaDisabilitate = this.certificatiFirmaSelezionata === null;

    setTimeout(() => {
      if (this.certificatiFirmaSelezionata !== null){
        this.certificatoFirmaForm.enable();
        this.certificatoFirmaForm.updateValueAndValidity();
      }else{
        this.certificatoFirmaForm.disable();
      }
    }, 1);

  }

  aggiornaValori() {
    this.canManagePasswordField = false;
    this.initForm();

    this.impostazioniFirmaComp.enteCertificatore = this.certificatiFirmaSelezionata?.enteCertificatore
      ? this.certificatiFirmaSelezionata.enteCertificatore : this.impostazioniFirmaComp.enteCertificatore ?? null;

    this.impostazioniFirmaComp.profiloFEQ = this.certificatiFirmaSelezionata?.profiloFEQ
      ? this.certificatiFirmaSelezionata.profiloFEQ : this.impostazioniFirmaComp.profiloFEQ ?? null;

    this.impostazioniFirmaComp.sceltaMarcaTemporale = this.certificatiFirmaSelezionata?.sceltaMarcaTemporale
      ? this.certificatiFirmaSelezionata.sceltaMarcaTemporale : this.impostazioniFirmaComp.sceltaMarcaTemporale
      ?? null;

    this.impostazioniFirmaComp.tipoCredenzialiFirma = this.certificatiFirmaSelezionata?.tipoCredenzialiFirma
      ? this.certificatiFirmaSelezionata.tipoCredenzialiFirma : this.impostazioniFirmaComp.tipoCredenzialiFirma
      ?? null;

    this.impostazioniFirmaComp.tipoOTP = this.certificatiFirmaSelezionata?.tipoOTP
      ? this.certificatiFirmaSelezionata.tipoOTP : this.impostazioniFirmaComp.tipoOTP ?? null;

    this.impostazioniFirmaComp.setValoreSelezionatoImpFirma();

    if (this.impostazioniFirmaComp.enteCertificatore) {
      this.enteCertificatoreSelezionato(this.impostazioniFirmaComp.enteCertificatore);
    }

  }


  resetCertificato() {
    this.certificatiFirmaSelezionata = null;
    this.impostazioniFirmaComp.enteCertificatore = null;
    this.impostazioniFirmaComp.profiloFEQ = null;
    this.impostazioniFirmaComp.sceltaMarcaTemporale = null;
    this.impostazioniFirmaComp.tipoCredenzialiFirma = null;
    this.impostazioniFirmaComp.tipoOTP = null;
    this.canManagePasswordField = false;

    if (this.certificatoFirmaForm.get('password')){
      this.certificatoFirmaForm.removeControl('password');
    }

    this.initForm();

  }

  disabilitaSalvaCertificato() {
    return !this.certificatiFirmaSelezionata || !this.certificatoFirmaForm.valid ||
      (!this.impostazioniFirmaComp || (this.impostazioniFirmaComp &&
        !this.impostazioniFirmaComp.enteCertificatore || !this.impostazioniFirmaComp.tipoCredenzialiFirma ||
        !this.impostazioniFirmaComp.tipoOTP || !this.impostazioniFirmaComp.profiloFEQ ||
        !this.impostazioniFirmaComp.sceltaMarcaTemporale));
  }

  creaCertificato() {

    if (this.certificatiFirmaSelezionata && this.certificatoFirmaForm.valid && this.impostazioniFirmaComp &&
      this.impostazioniFirmaComp.enteCertificatore && this.impostazioniFirmaComp.tipoCredenzialiFirma &&
      this.impostazioniFirmaComp.tipoOTP && this.impostazioniFirmaComp.profiloFEQ &&
      this.impostazioniFirmaComp.sceltaMarcaTemporale) {

      const certificato: CertificatoFirma = {
        descrizione: this.certificatiFirmaSelezionata.descrizione ?? this.certificatiFirmaSelezionata,
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

      if (this.certificatiFirmaSelezionata.id) {
        certificato.id = this.certificatiFirmaSelezionata.id;
      }
      return certificato;
    }
  }

  salvaCertificato() {
    const certificato = this.creaCertificato();

    if (!certificato) {
      return;
    }

    if (certificato.id) {

      this.certificatiService.aggiornaCertificato(certificato.id, certificato).subscribe(
        response => {
          this.modalService.info(this.translateService.instant('firma_digitale.saltavaggio_certificato'),
            this.translateService.instant('firma_digitale.salvataggio_certificato_msg'))
            .then(() => {
            }
            ).catch(() => { });
        },
        error => {
          const msg = certificato.descrizione ?? 'Certificato';

          const messaggioErrore = Utils.parseAndReplacePlaceholders(
            this.translateService.instant('errori.aggiornamento_certificazione_msg'),
            [msg]);

          this.modalService.error(this.translateService.instant('firma_digitale.saltavaggio_certificato'),
            messaggioErrore, error.error.errore)
            .then(() => { })
            .catch(() => { });
        });
    } else {

      this.certificatiService.salvaCertificato(certificato).subscribe(
        (response) => {
          this.modalService.info(this.translateService.instant('firma_digitale.saltavaggio_certificato'),
            this.translateService.instant('firma_digitale.salvataggio_certificato_msg'))
            .then(() => {
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

  eliminaCertificato() {
    if (this.certificatiFirmaSelezionata?.id) {

      let messaggio = this.translateService.instant('common.eliminazione_messaggio');
      messaggio = Utils.parseAndReplacePlaceholders(messaggio, [this.certificatiFirmaSelezionata.descrizione]);

      this.modalService.confermaRifiuta(
        this.translateService.instant('firma_digitale.eliminazione_certificato'),
        messaggio
      ).then(proceed => {
        if (this.certificatiFirmaSelezionata?.id) {
          this.certificatiService.deleteCertificato(this.certificatiFirmaSelezionata.id).subscribe(
            response => {
              this.modalService.info(this.translateService.instant('firma_digitale.eliminazione_certificato'),
                this.translateService.instant('firma_digitale.eliminazione_certificato_msg'))
                .then(() => {
                  this.reloadCertificati();
                }
                ).catch(() => { });
            },
            error => {
              const msg = this.certificatiFirmaSelezionata?.descrizione ? this.certificatiFirmaSelezionata.descrizione : 'Certificato';

              const messaggioErrore = Utils.parseAndReplacePlaceholders(
                this.translateService.instant('errori.eliminazione_certificazione_msg'),
                [msg]);

              this.modalService.error(this.translateService.instant('firma_digitale.eliminazione_certificato'),
                messaggioErrore, error.error.errore)
                .then(() => { })
                .catch(() => { });
            });
        }

      })
        .catch(() => { });
    }
  }

  reloadCertificati() {
    this.certificatiService.getCertificati().subscribe(response => {

      this.certificatiFirmaSelezionata = response.find(elem => elem.ultimoUtilizzato === true) ?? null;
      if (!this.certificatiFirmaSelezionata) {
        this.impostazioniFirmaComp.enteCertificatore = null;
        this.impostazioniFirmaComp.profiloFEQ = null;
        this.impostazioniFirmaComp.sceltaMarcaTemporale = null;
        this.impostazioniFirmaComp.tipoCredenzialiFirma = null;
        this.impostazioniFirmaComp.tipoOTP = null;

      }
      this.aggiornaValori();

    });
  }

  private setValoreSelezionato(tipoPratica: TipoPratica | null, impostazioniFirma: any) {

    this.impostazioniFirmaComp.enteCertificatore = tipoPratica?.enteCertificatore ?? impostazioniFirma?.enteCertificatore ?? null;
    this.impostazioniFirmaComp.tipoCredenzialiFirma = tipoPratica?.tipoCredenziale ?? impostazioniFirma?.tipoCredenzialiFirma ?? null;
    this.impostazioniFirmaComp.tipoOTP = tipoPratica?.tipoOtp ?? impostazioniFirma?.tipoOTP ?? null;
    this.impostazioniFirmaComp.profiloFEQ = tipoPratica?.profiloFEQ ?? impostazioniFirma?.profiloFEQ ?? null;
    this.impostazioniFirmaComp.sceltaMarcaTemporale = tipoPratica?.sceltaMarcaTemporale ?? impostazioniFirma?.sceltaMarcaTemporale ?? null;

    this.impostazioniFirmaComp.setValoreSelezionatoImpFirma();
  }



  enteCertificatoreSelezionato(enteCertificatore: EnteCertificatore) {
    this.canManagePasswordField = enteCertificatore.codice === 'Uanataca';
    if (this.canManagePasswordField) {
      this.certificatoFirmaForm.addControl('password',
        new FormControl(this.certificatiFirmaSelezionata?.password ?
          this.certificatiFirmaSelezionata?.password : null, Validators.required));
    } else if (this.certificatoFirmaForm.get('password')) {
      this.certificatoFirmaForm.removeControl('password');
    }
  }

  apriSelezionaCertificati(){
    const data = this.helperService.searchHelperRef(this.route);
    const ngbModalOptions: NgbModalOptions = {
      backdrop : 'static',
      size: 'xl'
    };

    const modalRef = this.modal.open(SelezionaCertificatoComponent, ngbModalOptions);
    modalRef.componentInstance.codiceTipoPratica = this.codiceTipoPratica ?? '';
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';

    modalRef.result.then((certificato: CertificatoFirma) => {
     this.certificatiFirmaSelezionata = certificato;
     this.aggiornaValori();
    }).catch(() => {});
  }
}
