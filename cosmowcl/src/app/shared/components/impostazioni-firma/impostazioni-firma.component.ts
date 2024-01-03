/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ImpostazioniFirmaService } from '../../services/impostazioni-firma.service';
import { ModalService } from '../../services/modal.service';
import { TranslateService } from '@ngx-translate/core';
import { PreferenzeUtenteService } from '../../services/preferenze-utente.service';
import { EnteCertificatore } from '../../models/api/cosmoauthorization/enteCertificatore';
import { TipoCredenzialiFirma } from '../../models/api/cosmoauthorization/tipoCredenzialiFirma';
import { TipoOTP } from '../../models/api/cosmoauthorization/tipoOTP';
import { ProfiloFEQ } from '../../models/api/cosmoauthorization/profiloFEQ';
import { SceltaMarcaTemporale } from '../../models/api/cosmoauthorization/sceltaMarcaTemporale';

@Component({
  selector: 'app-impostazioni-firma',
  templateUrl: './impostazioni-firma.component.html',
  styleUrls: ['./impostazioni-firma.component.scss']
})
export class ImpostazioniFirmaComponent implements OnInit {

  entiCertificatori: EnteCertificatore[] = [];
  tipiCredenzialiFirma: TipoCredenzialiFirma[] = [];
  tipiOTP: TipoOTP[] = [];
  profiliFEQ: ProfiloFEQ[] = [];
  scelteMarcaTemporale: SceltaMarcaTemporale[] = [];

  enteCertificatore: EnteCertificatore | null = null;
  tipoCredenzialiFirma: TipoCredenzialiFirma | null = null;
  tipoOTP: TipoOTP | null = null;
  profiloFEQ: ProfiloFEQ | null = null;
  sceltaMarcaTemporale: SceltaMarcaTemporale | null = null;

   defaultCodeValue: string[] = [];

  @Input() disableAll = false;

  @Input() abilitaSelezioneNulla = false;
  @Input() obbligatorietaCampi = false;
  @Input() profiliFeqAbilitati: string[] = [];
  @Output() enteCertificatoreSelezionato = new EventEmitter<EnteCertificatore>();

  constructor(
    private modalService: ModalService,
    private translateService: TranslateService,
    private impostazioniFirmaService: ImpostazioniFirmaService,
    private preferenzeUtenteService: PreferenzeUtenteService) { }

  ngOnInit(): void {

    // chiamo l'elenco delle impostazioni firma per popolare i dropdown
    this.impostazioniFirmaService.getImpostazioniFirma().subscribe(impostazioniFirma => {
      this.preferenzeUtenteService.getPreferenze().subscribe( preferenze => {

        if (impostazioniFirma) {
          this.entiCertificatori = impostazioniFirma.entiCertificatori ?? [];
          this.tipiCredenzialiFirma = impostazioniFirma.tipiCredenzialiFirma ?? [];
          this.tipiOTP = impostazioniFirma.tipiOTP ?? [];
          this.profiliFEQ = impostazioniFirma.profiliFEQ ?? [];
          this.scelteMarcaTemporale = impostazioniFirma.scelteMarcaTemporale ?? [];
          this.setValoreSelezionatoImpFirma();

          if ( ! preferenze ){
            this.setDefaultValues();
          }
        } else {
          this.modalService.error(this.translateService.instant('preferenze.impostazioni_firma'),
            this.translateService.instant('errori.impostazioni_firma_non_configurate'))
            .then(() => { })
            .catch(() => { });
        }
      });
    });


  }

  setValoreSelezionatoImpFirma() {

    this.enteCertificatore = this.entiCertificatori.find(enteCertificatore =>
      enteCertificatore.codice === this.enteCertificatore?.codice) ?? this.enteCertificatore;

    this.tipoCredenzialiFirma = this.tipiCredenzialiFirma.find(tipoCredenzialiFirma =>
      tipoCredenzialiFirma.codice === this.tipoCredenzialiFirma?.codice) ?? this.tipoCredenzialiFirma;

    this.tipoOTP = this.tipiOTP.find(tipoOTP =>
      tipoOTP.codice === this.tipoOTP?.codice) ?? this.tipoOTP;

    this.profiloFEQ = this.profiliFEQ.find(profiloFEQ =>
      profiloFEQ.codice === this.profiloFEQ?.codice) ?? this.profiloFEQ;

    this.sceltaMarcaTemporale = this.scelteMarcaTemporale.find(sceltaMarcaTemporale =>
      sceltaMarcaTemporale.codice === this.sceltaMarcaTemporale?.codice) ?? this.sceltaMarcaTemporale;
  }


  setDefaultValues() {
    this.defaultCodeValue.push(this.enteCertificatore?.codice ?? '');
    this.defaultCodeValue.push(this.tipoCredenzialiFirma?.codice ?? '');
    this.defaultCodeValue.push(this.tipoOTP?.codice ?? '');
    this.defaultCodeValue.push(this.profiloFEQ?.codice ?? '');
    this.defaultCodeValue.push(this.sceltaMarcaTemporale?.codice ?? '');
    this.defaultCodeValue = this.defaultCodeValue.filter(code => code !== '');
  }

  isDefault(arg: string | null): boolean {
    if (arg) {
      return this.defaultCodeValue.includes(arg);
    }
    return false ;
  }

  disableProfiloFEQ(codice: string){
    if (this.profiliFeqAbilitati.length === 0 || this.profiliFeqAbilitati.includes(codice)){
      return false;
    }else{
      return true;
    }
  }

  onChangeEnteCertificatore(enteCertificatore: EnteCertificatore) {
    this.enteCertificatoreSelezionato.emit(enteCertificatore);
  }

}



