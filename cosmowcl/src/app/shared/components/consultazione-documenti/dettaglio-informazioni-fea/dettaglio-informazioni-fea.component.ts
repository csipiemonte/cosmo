/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { BreakpointObserver, Breakpoints, BreakpointState } from '@angular/cdk/layout';
import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { DocumentoDTO, TipoContenutoDocumentoEnum,
  TipoContenutoDocumentoFirmatoEnum } from 'src/app/shared/models/documento/documento.model';
import { InfoFirmaFea } from 'src/app/shared/models/api/cosmoecm/infoFirmaFea';
import { ModaleParentComponent } from 'src/app/modali/modale-parent-component';
import { HelperService } from 'src/app/shared/services/helper.service';

@Component({
  selector: 'app-dettaglio-informazioni-fea',
  templateUrl: './dettaglio-informazioni-fea.component.html',
  styleUrls: ['./dettaglio-informazioni-fea.component.scss']
})
export class DettaglioInformazioniFeaComponent extends ModaleParentComponent implements OnInit {

  firme: InfoFirmaFea[] | null = null;
  @Input() layout: string | null = null;
  @Input() documento: DocumentoDTO | null = null;
  codicePagina!: string;
  codiceTab!: string;
  codiceModale!: string;


  constructor(public modal: NgbActiveModal,
              private logger: NGXLogger,
              public breakpointObserver: BreakpointObserver,
              public helperService: HelperService) {
    super(helperService);
  }

  ngOnInit(): void {
    if (this.documento && this.documento.id){
      this.firme = this.documento.contenuti?.filter(contenuto => contenuto.tipo?.codice === TipoContenutoDocumentoEnum.FIRMATO
        && contenuto.tipoContenutoFirmato?.codice === TipoContenutoDocumentoFirmatoEnum.FEA)
      .map(firmato => firmato.infoFirmaFea).filter(elem => elem !== undefined) as InfoFirmaFea[] ?? [];
    }
    this.breakpointObserver
      .observe([Breakpoints.XSmall, Breakpoints.Small, Breakpoints.Medium, Breakpoints.HandsetPortrait])
      .subscribe((state: BreakpointState) => {
        if (state.matches) {
          this.logger.debug(
            'Matched small viewport or handset in portrait mode'
          );
          this.layout = 'small';
        }
      });
    this.breakpointObserver
      .observe([Breakpoints.Large, Breakpoints.XLarge])
      .subscribe((state: BreakpointState) => {
        if (state.matches) {
          this.logger.debug(
            'Matched large viewport'
          );
          this.layout = 'large';
        }
      });

    this.setModalName(this.codiceModale);
    this.searchHelperModale(this.codicePagina, this.codiceTab);
  }

  get render(): boolean {
    return !!(this.firme?.length);
  }

  get small(): boolean {
    return 'small' === this.layout;
  }

}
