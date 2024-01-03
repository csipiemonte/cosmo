/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  Component,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import {
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import {
  forkJoin,
  Subscription,
} from 'rxjs';
import {
  ValorePreferenzeEnte,
} from 'src/app/shared/models/preferenze/valore-preferenze-ente.model';
import {
  ConfigurazioniService,
} from 'src/app/shared/services/configurazioni.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import {
  PreferenzeEnteService,
} from 'src/app/shared/services/preferenze-ente.service';
import { Utils } from 'src/app/shared/utilities/utilities';

import { TranslateService } from '@ngx-translate/core';

import {
  ImpostazioniFirmaComponent,
} from '../shared/components/impostazioni-firma/impostazioni-firma.component';
import { Constants } from '../shared/constants/constants';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { WidgetsService } from '../home/widgets/widgets.service';

@Component({
  selector: 'app-preferenze-ente',
  templateUrl: './preferenze-ente.component.html',
  styleUrls: ['./preferenze-ente.component.scss']
})
export class PreferenzeEnteComponent implements OnInit, OnDestroy {
  @ViewChild('impostazioniFirma') impostazioniFirmaComp!: ImpostazioniFirmaComponent;

  private preferenzeEnteSubscription: Subscription | null = null;
  preferenzeEnteForm!: FormGroup;
  versione!: string;
  imgURL: any;

  imgURLFea: any;

  defaultDimensioneMassimaAllegatiEmail = 100 * 1024 * 1024; // 10 MB

  idEnte = 0;
  sizeHeader = 0;
  heightHeader = 0;
  widthHeader = 0;
  sizeFooter = 0;
  heightFooter = 0;
  widthFooter = 0;
  sizeFooterCentrale = 0;
  heightFooterCentrale = 0;
  widthFooterCentrale = 0;
  sizeFooterDestra = 0;
  heightFooterDestra = 0;
  widthFooterDestra = 0;
  dimensioneMassimaAllegatiEmail = 0;
  initSizeHeader = 0;
  initHeightHeader = 0;
  initWidthHeader = 0;
  initSizeFooter = 0;
  initHeightFooter = 0;
  initWidthFooter = 0;
  initSizeFooterCentrale = 0;
  initHeightFooterCentrale = 0;
  initWidthFooterCentrale = 0;
  initSizeFooterDestra = 0;
  initHeightFooterDestra = 0;
  initWidthFooterDestra = 0;
  initDimensioneMassimaAllegatiEmail = 0;
  feaMaxSize = 0;
  feaMaxNumOfPixels = 0;
  imgFeaPixels = 0;
  imgFeaHeight = 0;
  imgFeaWidth = 0;
  initSizeFea = 0;
  initHeightFea = 0;
  initWidthFea = 0;
  widgetDisponibili: string[] = [];

  valorePreferenzeEnte: ValorePreferenzeEnte = { header: '', logo: '', logoFooter: '', logoFooterCentrale: '', logoFooterDestra: '',
                                          iconaFea: '', dimensioneMassimaAllegatiEmail: 0, widgets: [], isWidgetModificabile: true };
  initValorePreferenzeEnte: ValorePreferenzeEnte = { header: '', logo: '', logoFooter: '', logoFooterCentrale: '', logoFooterDestra: '',
                                          iconaFea: '', dimensioneMassimaAllegatiEmail: 0, widgets: [], isWidgetModificabile: true };
  maxSize = 0;
  maxNumOfPixels = 0;
  imgURLFooter: any;
  imgURLFooterCentrale: any;
  imgURLFooterDestra: any;
  obbligatorietaCampi = false;
  dragAndDropIsTouched = false;

  constructor(
    private route: ActivatedRoute,
    private configurazioniService: ConfigurazioniService,
    private preferenzeEnteService: PreferenzeEnteService,
    private translateService: TranslateService,
    private widgetService: WidgetsService,
    private modalService: ModalService) { }

  ngOnInit(): void {
    setTimeout(() => {
      forkJoin({
        maxSize: this.configurazioniService.getConfigurazioneByChiave(Constants.PARAMETRI.PREFERENZE_ENTE.LOGO_MAX_SIZE),
        maxPixel: this.configurazioniService.getConfigurazioneByChiave(Constants.PARAMETRI.PREFERENZE_ENTE.LOGO_MAX_NUM_OF_PIXELS),
        feaMaxSize: this.configurazioniService.getConfigurazioneByChiave(Constants.PARAMETRI.PREFERENZE_ENTE.FEA_MAX_SIZE),
        feaMaxPixel: this.configurazioniService.getConfigurazioneByChiave(Constants.PARAMETRI.PREFERENZE_ENTE.FEA_MAX_NUM_OF_PIXELS),
      }).pipe().subscribe(
        result => {
          this.maxSize = +(result.maxSize ?? 0);
          this.maxNumOfPixels = +(result.maxPixel ?? 0);
          this.feaMaxSize = +(result.feaMaxSize ?? 0);
          this.feaMaxNumOfPixels = +(result.feaMaxPixel ?? 0);
          this.preferenzeEnteSubscription = this.preferenzeEnteService.subscribePreferenze.subscribe(preferenze => {
            if (preferenze?.valore) {
              this.valorePreferenzeEnte = JSON.parse(preferenze.valore);
              this.versione = preferenze.versione ?? '';
              this.initValorePreferenzeEnte = Object.assign({}, this.valorePreferenzeEnte);
              this.initValorePreferenzeEnte.widgets = Object.assign([], this.valorePreferenzeEnte.widgets);
              this.checkingCurrentLogos();
              if ( this.valorePreferenzeEnte?.impostazioniFirma) {
                this.setValoreSelezionato(this.valorePreferenzeEnte.impostazioniFirma);
              }


            } else {
              this.valorePreferenzeEnte.header = '';
              this.valorePreferenzeEnte.logo = '';
              this.valorePreferenzeEnte.logoFooter = '';
              this.valorePreferenzeEnte.logoFooterCentrale = '';
              this.valorePreferenzeEnte.logoFooterDestra = '';
              this.valorePreferenzeEnte.iconaFea = '';
              this.valorePreferenzeEnte.dimensioneMassimaAllegatiEmail = this.defaultDimensioneMassimaAllegatiEmail;
              this.versione = '';
            }
            // provide defaults
            if (!this.valorePreferenzeEnte.dimensioneMassimaAllegatiEmail) {
              this.valorePreferenzeEnte.dimensioneMassimaAllegatiEmail = this.defaultDimensioneMassimaAllegatiEmail;
            }
            if (!this.valorePreferenzeEnte.widgets) {
              this.valorePreferenzeEnte.widgets = [];
            }
            this.widgetDisponibili = this.widgetService.defaultWidgets
            .filter(elem => !this.valorePreferenzeEnte.widgets?.find(x => x === elem.name)).map(w => w.name);

            this.initForm();
          });
        }
      );
    }, 0);
  }

  ngOnDestroy(): void {
    if (this.preferenzeEnteSubscription) {
      this.preferenzeEnteSubscription.unsubscribe();
    }
  }

  get widgetAssociati(){
    return this.valorePreferenzeEnte.widgets?.map(elem => this.widgetService.setDescrizione(elem));
  }

  get widgetsDisponibili(){
    return this.widgetDisponibili?.map(elem => this.widgetService.setDescrizione(elem));
  }

  private initForm() {
    this.imgURL = '';
    this.imgURLFooter = '';
    this.imgURLFooterCentrale = '';
    this.imgURLFooterDestra = '';
    this.imgURLFea = '';

    if (this.valorePreferenzeEnte && this.valorePreferenzeEnte.iconaFea && this.valorePreferenzeEnte.iconaFea.length > 0) {
      this.imgURLFea = 'data:image/png;base64,' + this.valorePreferenzeEnte.iconaFea;
    }
    if (this.valorePreferenzeEnte && this.valorePreferenzeEnte.logo && this.valorePreferenzeEnte.logo.length > 0) {
      this.imgURL = 'data:image/png;base64,' + this.valorePreferenzeEnte.logo;
    }
    if (this.valorePreferenzeEnte && this.valorePreferenzeEnte.logoFooter && this.valorePreferenzeEnte.logoFooter.length > 0) {
      this.imgURLFooter = 'data:image/png;base64,' + this.valorePreferenzeEnte.logoFooter;
    }
    if (this.valorePreferenzeEnte && this.valorePreferenzeEnte.logoFooterCentrale &&
        this.valorePreferenzeEnte.logoFooterCentrale.length > 0) {
      this.imgURLFooterCentrale = 'data:image/png;base64,' + this.valorePreferenzeEnte.logoFooterCentrale;
    }
    if (this.valorePreferenzeEnte && this.valorePreferenzeEnte.logoFooterDestra
      && this.valorePreferenzeEnte.logoFooterDestra.length > 0) {
      this.imgURLFooterDestra = 'data:image/png;base64,' + this.valorePreferenzeEnte.logoFooterDestra;
    }
    this.preferenzeEnteForm = new FormGroup({
      header: new FormControl(this.valorePreferenzeEnte.header, Validators.required),
      logo: new FormControl(this.valorePreferenzeEnte.logo, Validators.required),
      sizeHeader: new FormControl(this.sizeHeader, Validators.max((this.maxSize))),
      heightHeader: new FormControl(this.heightHeader, Validators.max(this.maxNumOfPixels)),
      widthHeader: new FormControl(this.widthHeader, Validators.max(this.maxNumOfPixels)),
      logoFooter: new FormControl(this.valorePreferenzeEnte.logoFooter ?? ''),
      logoFooterCentrale: new FormControl(this.valorePreferenzeEnte.logoFooterCentrale ?? ''),
      logoFooterDestra: new FormControl(this.valorePreferenzeEnte.logoFooterDestra ?? ''),
      sizeFooter: new FormControl(this.sizeFooter, Validators.max((this.maxSize))),
      heightFooter: new FormControl(this.heightFooter, Validators.max(this.maxNumOfPixels)),
      widthFooter: new FormControl(this.widthFooter, Validators.max(this.maxNumOfPixels)),
      sizeFooterCentrale: new FormControl(this.sizeFooterCentrale, Validators.max((this.maxSize))),
      heightFooterCentrale: new FormControl(this.heightFooterCentrale, Validators.max(this.maxNumOfPixels)),
      widthFooterCentrale: new FormControl(this.widthFooterCentrale, Validators.max(this.maxNumOfPixels)),
      sizeFooterDestra: new FormControl(this.sizeFooterDestra, Validators.max((this.maxSize))),
      heightFooterDestra: new FormControl(this.heightFooterDestra, Validators.max(this.maxNumOfPixels)),
      widthFooterDestra: new FormControl(this.widthFooterDestra, Validators.max(this.maxNumOfPixels)),
      modificabile: new FormControl(this.valorePreferenzeEnte.isWidgetModificabile ?? true, Validators.required),
      dimensioneMassimaAllegatiEmail: new FormControl(this.valorePreferenzeEnte.dimensioneMassimaAllegatiEmail, Validators.min(0)),
      immagineFea: new FormControl(this.valorePreferenzeEnte.iconaFea),
      sizeimmagineFea: new FormControl(this.imgFeaPixels, Validators.max((this.feaMaxSize))),
      heightimmagineFea: new FormControl(this.imgFeaHeight, Validators.max(this.feaMaxNumOfPixels)),
      widthimmagineFea: new FormControl(this.imgFeaWidth, Validators.max(this.feaMaxNumOfPixels)),
    });
  }

  onChange(event: any) {
    const file = event.target.files;
    const mimeType = file[0].type;
    if (file.length === 0 || mimeType.match(/image\/*/) == null) {
      if (event.target.name === 'logoFooter') {
        this.preferenzeEnteForm.get('logoFooter')?.markAsTouched();
      }else if (event.target.name === 'logoFooterCentrale'){
        this.preferenzeEnteForm.get('logoFooterCentrale')?.markAsTouched();
      }else if (event.target.name === 'logoFooterDestra'){
        this.preferenzeEnteForm.get('logoFooterDestra')?.markAsTouched();
      }else if (event.target.name === 'immagineFea'){
        this.preferenzeEnteForm.get('immagineFea')?.markAsTouched();
      }else {
        this.preferenzeEnteForm.get('logo')?.markAsTouched();
      }
    }
    const reader = new FileReader();
    reader.readAsDataURL(file[0]);
    reader.onload = () => {
      if (reader.result) {
        const Img = new Image();
        Img.src = URL.createObjectURL(file[0]);
        this.valorePreferenzeEnte.header = this.preferenzeEnteForm.value.header;
        const encodeBase64Img = Utils.getEncodedBase64ValueFromArrayBufferString(reader.result.toString());
        if (event.target.name === 'immagineFea') {
          this.valorePreferenzeEnte.iconaFea = encodeBase64Img;
          this.imgURLFea = 'data:image/png;base64,' + this.valorePreferenzeEnte.iconaFea;
          Img.onload = (e: any) => {
            const path = e.path || (e.composedPath && e.composedPath());
            this.setImmagineFeaDimensions(path[0].height, path[0].width, file[0].size);
            this.initForm();
          };

        }else if (event.target.name === 'logoFooter') {
          this.valorePreferenzeEnte.logoFooter = encodeBase64Img;
          this.imgURLFooter = 'data:image/png;base64,' + this.valorePreferenzeEnte.logoFooter;
          Img.onload = (e: any) => {
            const path = e.path || (e.composedPath && e.composedPath());
            this.setFooterDimensions(path[0].height, path[0].width, file[0].size);
            this.initForm();
          };

        }else if (event.target.name === 'logoFooterCentrale'){
          this.valorePreferenzeEnte.logoFooterCentrale = encodeBase64Img;
          this.imgURLFooterCentrale = 'data:image/png;base64,' + this.valorePreferenzeEnte.logoFooterCentrale;
          Img.onload = (e: any) => {
            const path = e.path || (e.composedPath && e.composedPath());
            this.setFooterCentraleDimensions(path[0].height, path[0].width, file[0].size);
            this.initForm();
          };
        }else if (event.target.name === 'logoFooterDestra'){
          this.valorePreferenzeEnte.logoFooterDestra = encodeBase64Img;
          this.imgURLFooterDestra = 'data:image/png;base64,' + this.valorePreferenzeEnte.logoFooterDestra;
          Img.onload = (e: any) => {
            const path = e.path || (e.composedPath && e.composedPath());
            this.setFooterDestraDimensions(path[0].height, path[0].width, file[0].size);
            this.initForm();
          };
        }else {
          this.valorePreferenzeEnte.logo = encodeBase64Img;
          this.preferenzeEnteForm.controls.logo.setValue(encodeBase64Img);
          this.imgURL = 'data:image/png;base64,' + this.valorePreferenzeEnte.logo;
          Img.onload = (e: any) => {
            const path = e.path || (e.composedPath && e.composedPath());
            this.setHeaderDimensions(path[0].height, path[0].width, file[0].size);
            this.initForm();
          };
        }
      }
    };
  }

  resetFields() {
    this.dragAndDropIsTouched = false;
    this.dimensioneMassimaAllegatiEmail = this.initDimensioneMassimaAllegatiEmail;
    this.heightHeader = this.initHeightHeader;
    this.widthHeader = this.initWidthHeader;
    this.sizeHeader = this.initSizeHeader;
    this.heightFooter = this.initHeightFooter;
    this.widthFooter = this.initWidthFooter;
    this.sizeFooter = this.initSizeFooter;
    this.heightFooterCentrale = this.initHeightFooterCentrale;
    this.widthFooterCentrale = this.initWidthFooterCentrale;
    this.sizeFooterCentrale = this.initSizeFooterCentrale;
    this.heightFooterDestra = this.initHeightFooterDestra;
    this.widthFooterDestra = this.initWidthFooterDestra;
    this.sizeFooterDestra = this.initSizeFooterDestra;
    this.imgFeaHeight = this.initHeightFea;
    this.imgFeaWidth = this.initWidthFea;
    this.imgFeaPixels = this.initSizeFea;

    this.valorePreferenzeEnte = Object.assign({}, this.initValorePreferenzeEnte);
    this.valorePreferenzeEnte.widgets = Object.assign([], this.initValorePreferenzeEnte.widgets);
    if (this.valorePreferenzeEnte.impostazioniFirma) {
      this.setValoreSelezionato(this.valorePreferenzeEnte.impostazioniFirma);
    } else {
      this.impostazioniFirmaComp.enteCertificatore = null;
      this.impostazioniFirmaComp.tipoCredenzialiFirma = null;
      this.impostazioniFirmaComp.tipoOTP = null;
      this.impostazioniFirmaComp.profiloFEQ = null;
      this.impostazioniFirmaComp.sceltaMarcaTemporale = null;
    }
    if (!this.valorePreferenzeEnte.widgets) {
      this.valorePreferenzeEnte.widgets = [];
    }
    this.widgetDisponibili = this.widgetService.defaultWidgets
    .filter(elem => !this.valorePreferenzeEnte.widgets?.find(x => x === elem.name)).map(w => w.name);

    this.initForm();
  }

  salva() {
    this.dragAndDropIsTouched = false;
    const preferenzeEnte = {
      versione: this.versione,
      valore: JSON.stringify({
        header: this.preferenzeEnteForm.value.header,
        logo: this.valorePreferenzeEnte.logo,
        logoFooter: this.valorePreferenzeEnte.logoFooter ?? '',
        logoFooterCentrale: this.valorePreferenzeEnte.logoFooterCentrale ?? '',
        logoFooterDestra: this.valorePreferenzeEnte.logoFooterDestra ?? '',
        dimensioneMassimaAllegatiEmail: this.preferenzeEnteForm.controls.dimensioneMassimaAllegatiEmail.value,
        impostazioniFirma: this.setImpostazioniFirma(),
        widgets: this.valorePreferenzeEnte.widgets,
        isWidgetModificabile: this.preferenzeEnteForm.controls.modificabile.value ?? true,
        iconaFea: this.valorePreferenzeEnte.iconaFea ?? ''
      })
    };

    this.modalService.info(this.translateService.instant('preferenze.salvataggio_preferenze_ente'),
      this.translateService.instant('preferenze.preferenze_salvataggio_ok'))
      .then(() => {
        if (!this.preferenzeEnteService.getPreferenzeSalvate()) {
          this.preferenzeEnteService.salva(preferenzeEnte);
        } else {
          this.preferenzeEnteService.aggiorna(preferenzeEnte);
        }
      }
      ).catch(() => {});
  }

  setValoreSelezionato(impostazioniFirma: any) {
    this.impostazioniFirmaComp.enteCertificatore = impostazioniFirma.enteCertificatore ?? null;
    this.impostazioniFirmaComp.tipoCredenzialiFirma = impostazioniFirma.tipoCredenzialiFirma ?? null;
    this.impostazioniFirmaComp.tipoOTP = impostazioniFirma.tipoOTP ?? null;
    this.impostazioniFirmaComp.profiloFEQ = impostazioniFirma.profiloFEQ ?? null;
    this.impostazioniFirmaComp.sceltaMarcaTemporale = impostazioniFirma.sceltaMarcaTemporale ?? null;
    this.impostazioniFirmaComp.setValoreSelezionatoImpFirma();
  }

  private setImpostazioniFirma() {
    if (this.valorePreferenzeEnte) {
      if (this.valorePreferenzeEnte.impostazioniFirma) {
        this.valorePreferenzeEnte.impostazioniFirma.enteCertificatore = this.impostazioniFirmaComp.enteCertificatore ?? undefined;
        this.valorePreferenzeEnte.impostazioniFirma.tipoCredenzialiFirma = this.impostazioniFirmaComp.tipoCredenzialiFirma ?? undefined;
        this.valorePreferenzeEnte.impostazioniFirma.tipoOTP = this.impostazioniFirmaComp.tipoOTP ?? undefined;
        this.valorePreferenzeEnte.impostazioniFirma.profiloFEQ = this.impostazioniFirmaComp.profiloFEQ ?? undefined;
        this.valorePreferenzeEnte.impostazioniFirma.sceltaMarcaTemporale = this.impostazioniFirmaComp.sceltaMarcaTemporale ?? undefined;
      } else {
        this.valorePreferenzeEnte.impostazioniFirma = {
          enteCertificatore: this.impostazioniFirmaComp.enteCertificatore ?? undefined,
          tipoCredenzialiFirma: this.impostazioniFirmaComp.tipoCredenzialiFirma ?? undefined,
          tipoOTP: this.impostazioniFirmaComp.tipoOTP ?? undefined,
          profiloFEQ: this.impostazioniFirmaComp.profiloFEQ ?? undefined,
          sceltaMarcaTemporale: this.impostazioniFirmaComp.sceltaMarcaTemporale ?? undefined
        };
      }
    }
    return this.valorePreferenzeEnte.impostazioniFirma;
  }

  drop(event: CdkDragDrop<string[]>) {
    this.dragAndDropIsTouched = true;
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
    }
  }

  private checkingCurrentLogos(){
    if (this.valorePreferenzeEnte.logo){
      const readerLogo = new FileReader();
      const imgBlob = Utils.getEncodeBlobValueFromBase64String(this.valorePreferenzeEnte.logo, 'image/png', '');
      readerLogo.readAsDataURL(imgBlob);
      readerLogo.onload = () => {
        if (readerLogo.result) {
          const Img = new Image();
          Img.src = URL.createObjectURL(imgBlob);
          Img.onload = (e: any) => {
              const path = e.path || (e.composedPath && e.composedPath());
              this.setHeaderDimensions(path[0].height, path[0].width, imgBlob.size);
              this.initHeightHeader = path[0].height;
              this.initWidthHeader = path[0].width;
              this.initSizeHeader = imgBlob.size;
              this.initForm();
            };
          }
      };
    }
    if (this.valorePreferenzeEnte.logoFooter){
      const readerLogoFooter = new FileReader();
      const imgBlob = Utils.getEncodeBlobValueFromBase64String(this.valorePreferenzeEnte.logoFooter, 'image/png', '');
      readerLogoFooter.readAsDataURL(imgBlob);
      readerLogoFooter.onload = () => {
        if (readerLogoFooter.result) {
          const Img = new Image();
          Img.src = URL.createObjectURL(imgBlob);
          Img.onload = (e: any) => {
              const path = e.path || (e.composedPath && e.composedPath());
              this.setFooterDimensions(path[0].height, path[0].width, imgBlob.size);
              this.initHeightFooter = path[0].height;
              this.initWidthFooter = path[0].width;
              this.initSizeFooter = imgBlob.size;
              this.initForm();
            };
          }
      };
    }
    if (this.valorePreferenzeEnte.logoFooterCentrale){
      const readerLogoFooterCentrale = new FileReader();
      const imgBlob = Utils.getEncodeBlobValueFromBase64String(this.valorePreferenzeEnte.logoFooterCentrale, 'image/png', '');
      readerLogoFooterCentrale.readAsDataURL(imgBlob);
      readerLogoFooterCentrale.onload = () => {
        if (readerLogoFooterCentrale.result) {
          const Img = new Image();
          Img.src = URL.createObjectURL(imgBlob);
          Img.onload = (e: any) => {
              const path = e.path || (e.composedPath && e.composedPath());
              this.setFooterCentraleDimensions(path[0].height, path[0].width, imgBlob.size);
              this.initHeightFooterCentrale = path[0].height;
              this.initWidthFooterCentrale = path[0].width;
              this.initSizeFooterCentrale = imgBlob.size;
              this.initForm();
            };
          }
      };
    }
    if (this.valorePreferenzeEnte.logoFooterDestra){
      const readerLogoFooterDestra = new FileReader();
      const imgBlob = Utils.getEncodeBlobValueFromBase64String(this.valorePreferenzeEnte.logoFooterDestra, 'image/png', '');
      readerLogoFooterDestra.readAsDataURL(imgBlob);
      readerLogoFooterDestra.onload = () => {
        if (readerLogoFooterDestra.result) {
          const Img = new Image();
          Img.src = URL.createObjectURL(imgBlob);
          Img.onload = (e: any) => {
              const path = e.path || (e.composedPath && e.composedPath());
              this.setFooterDestraDimensions(path[0].height, path[0].width, imgBlob.size);
              this.initHeightFooterDestra = path[0].height;
              this.initWidthFooterDestra = path[0].width;
              this.initSizeFooterDestra = imgBlob.size;
              this.initForm();
            };
          }
      };
    }
    if (this.valorePreferenzeEnte.iconaFea){
      const readerIconaFea = new FileReader();
      const imgBlob = Utils.getEncodeBlobValueFromBase64String(this.valorePreferenzeEnte.iconaFea, 'image/png', '');
      readerIconaFea.readAsDataURL(imgBlob);
      readerIconaFea.onload = () => {
        if (readerIconaFea.result) {
          const Img = new Image();
          Img.src = URL.createObjectURL(imgBlob);
          Img.onload = (e: any) => {
              const path = e.path || (e.composedPath && e.composedPath());
              this.setFooterDestraDimensions(path[0].height, path[0].width, imgBlob.size);
              this.initHeightFea = path[0].height;
              this.initWidthFea = path[0].width;
              this.initSizeFea = imgBlob.size;
              this.initForm();
            };
          }
      };
    }
  }

  private setHeaderDimensions(height: number, width: number, size: number) {
    this.heightHeader = height;
    this.widthHeader = width;
    this.sizeHeader = size;
  }

  private setFooterDimensions(height: number, width: number, size: number) {
    this.heightFooter = height;
    this.widthFooter = width;
    this.sizeFooter = size;
  }

  private setFooterCentraleDimensions(height: number, width: number, size: number) {
    this.heightFooterCentrale = height;
    this.widthFooterCentrale = width;
    this.sizeFooterCentrale = size;
  }

  private setFooterDestraDimensions(height: number, width: number, size: number) {
    this.heightFooterDestra = height;
    this.widthFooterDestra = width;
    this.sizeFooterDestra = size;
  }

  private setImmagineFeaDimensions(height: number, width: number, size: number) {
    this.imgFeaHeight = height;
    this.imgFeaWidth = width;
    this.imgFeaPixels = size;
  }
}
