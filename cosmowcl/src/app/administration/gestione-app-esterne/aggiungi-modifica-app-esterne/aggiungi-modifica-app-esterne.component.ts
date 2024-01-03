/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { forkJoin } from 'rxjs';
import { Constants } from 'src/app/shared/constants/constants';
import { ApplicazioneEsterna } from 'src/app/shared/models/api/cosmoauthorization/applicazioneEsterna';
import { AppEsterneService } from 'src/app/shared/services/app-esterne.service';
import { ConfigurazioniService } from 'src/app/shared/services/configurazioni.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { Utils } from 'src/app/shared/utilities/utilities';

@Component({
  selector: 'app-aggiungi-modifica-app-esterne',
  templateUrl: './aggiungi-modifica-app-esterne.component.html',
  styleUrls: ['./aggiungi-modifica-app-esterne.component.scss']
})
export class AggiungiModificaAppEsterneComponent implements OnInit {

  appForm!: FormGroup;
  idApp = 0;
  applicazione!: ApplicazioneEsterna;

  title!: string;

  sizeIcona = 0;
  heightIcona = 0;
  widthIcona = 0;

  maxSize = 0;
  maxNumOfPixels = 0;

  iconaURL: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private modalService: ModalService,
    private translateService: TranslateService,
    private configurazioniService: ConfigurazioniService,
    private appEsterneService: AppEsterneService) { }

  ngOnInit(): void {

    this.route.params.subscribe(param => {
      if (param.id) {
        this.idApp = +param.id;
        this.title = this.translateService.instant('app_esterne.modifica_applicazione');

        forkJoin({
          maxSize: this.configurazioniService.getConfigurazioneByChiave(Constants.PARAMETRI.PREFERENZE_ENTE.LOGO_MAX_SIZE),
          maxPixel: this.configurazioniService.getConfigurazioneByChiave(Constants.PARAMETRI.PREFERENZE_ENTE.LOGO_MAX_NUM_OF_PIXELS),
          applicazione: this.appEsterneService.getAppEsterna(this.idApp || 0)
        }).subscribe(
          result => {
            this.maxSize = +(result.maxSize ?? 0);
            this.maxNumOfPixels = +(result.maxPixel ?? 0);
            this.applicazione = result.applicazione;

            this.checkIconAndInitForm();

          });

      } else {
        this.title = this.translateService.instant('app_esterne.aggiungi_applicazione');

        forkJoin({
          maxSize: this.configurazioniService.getConfigurazioneByChiave(Constants.PARAMETRI.PREFERENZE_ENTE.LOGO_MAX_SIZE),
          maxPixel: this.configurazioniService.getConfigurazioneByChiave(Constants.PARAMETRI.PREFERENZE_ENTE.LOGO_MAX_NUM_OF_PIXELS),
        }).subscribe(
          result => {
            this.maxSize = +(result.maxSize ?? 0);
            this.maxNumOfPixels = +(result.maxPixel ?? 0);

            this.checkIconAndInitForm();
          });
      }
    });
  }

  private checkIconAndInitForm() {

    if (this.applicazione && this.applicazione.icona) {
      const imgBlob = Utils.getEncodeBlobValueFromBase64String(this.applicazione.icona, 'image/png', '');

      const readerLogo = new FileReader();
      readerLogo.readAsDataURL(imgBlob);
      readerLogo.onload = () => {
        if (readerLogo.result) {
          const Img = new Image();
          Img.src = URL.createObjectURL(imgBlob);
          Img.onload = (e: any) => {

            const path = e.path || (e.composedPath && e.composedPath());

            this.setDimensions(path[0].height, path[0].width, imgBlob.size);
            this.initForm();
          };
        }
      };
    } else {
      this.setDimensions(0, 0, 0);
      this.initForm();
    }
  }

  setDimensions(height: number, width: number, size: number) {
    this.heightIcona = height;
    this.widthIcona = width;
    this.sizeIcona = size;
  }

  initForm() {
    this.iconaURL = this.applicazione ? ('data:image/png;base64,' + this.applicazione.icona) : null;

    this.appForm = new FormGroup({
      descrizione: new FormControl(this.applicazione ? this.applicazione.descrizione : null, Validators.required),
      icona: new FormControl(this.applicazione ? this.applicazione.icona : null, Validators.required),
      sizeIcona: new FormControl(this.sizeIcona, Validators.max((this.maxSize))),
      heightIcona: new FormControl(this.heightIcona, Validators.max(this.maxNumOfPixels)),
      widthIcona: new FormControl(this.widthIcona, Validators.max(this.maxNumOfPixels)),
    });

  }

  onFileChanged(event: any) {

    if (event.target.files && event.target.files.length > 0
        && event.target.files[0].type.match(/image\/*/) != null) {

      const file = event.target.files[0];

      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        if (reader.result) {
          const Img = new Image();
          Img.src = URL.createObjectURL(file);
          const encodeBase64Img = Utils.getEncodedBase64ValueFromArrayBufferString(reader.result.toString());

          this.appForm.controls.icona.setValue(encodeBase64Img);
          this.iconaURL = 'data:image/png;base64,' + encodeBase64Img;

          Img.onload = (e: any) => {
            const path = e.path || (e.composedPath && e.composedPath());
            this.setDimensions(path[0].height, path[0].width, file.size);

            this.appForm.controls.sizeIcona.setValue(this.sizeIcona);
            this.appForm.controls.heightIcona.setValue(this.heightIcona);
            this.appForm.controls.widthIcona.setValue(this.widthIcona);
          };
        }
      };
    }
    else {
      this.iconaURL = null;
      this.appForm.controls.icona.setValue(null);
      this.appForm.get('icona')?.markAsTouched();
    }
  }

  tornaIndietro() {
    this.router.navigate(['back'], { relativeTo: this.route });
  }

  pulisciCampi() {
    this.checkIconAndInitForm();
  }

  salva() {
    if (this.idApp) {

      this.applicazione.descrizione = this.appForm.value.descrizione;
      this.applicazione.icona = this.appForm.value.icona;

      this.appEsterneService.aggiornaApplicazione(this.idApp, this.applicazione).subscribe(response => {
        this.modalService.info(this.translateService.instant('app_esterne.modifica_applicazione'),
          this.translateService.instant('app_esterne.aggiornamento_applicazione_msg'))
          .then(() => {
            this.appEsterneService.setReloadMenu(true);
            this.tornaIndietro();
          }
          ).catch(() => { });
      }, error => {
        this.modalService.error(this.translateService.instant('app_esterne.modifica_applicazione'),
          this.translateService.instant('errori.aggiornamento_applicazione'), error.error.errore)
          .then(() => {
          }
          ).catch(() => { });
      });
    } else {

      const app: ApplicazioneEsterna = {
        descrizione: this.appForm.value.descrizione,
        icona: this.appForm.value.icona
      };

      this.appEsterneService.salvaApplicazione(app).subscribe(response => {
        this.modalService.info(this.translateService.instant('app_esterne.aggiungi_applicazione'),
          this.translateService.instant('app_esterne.salvataggio_applicazione_msg'))
          .then(() => {
            this.appEsterneService.setReloadMenu(true);
            this.tornaIndietro();
          }
          ).catch(() => { });
        }, error => {
          this.modalService.error(this.translateService.instant('app_esterne.aggiungi_applicazione'),
            this.translateService.instant('errori.creazione_applicazione'), error.error.errore)
            .then(() => {
            }
            ).catch(() => { });
        });
    }

  }

}
