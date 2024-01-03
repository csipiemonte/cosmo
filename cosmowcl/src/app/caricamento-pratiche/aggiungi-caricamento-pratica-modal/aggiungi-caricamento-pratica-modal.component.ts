/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { forkJoin, of } from 'rxjs';
import { CaricamentoPratica } from 'src/app/shared/models/api/cosmopratiche/caricamentoPratica';
import { CaricamentoPraticaRequest } from 'src/app/shared/models/api/cosmopratiche/caricamentoPraticaRequest';
import { StatoCaricamentoPratica } from 'src/app/shared/models/api/cosmopratiche/statoCaricamentoPratica';
import { CaricamentoPraticheService } from 'src/app/shared/services/caricamento-pratiche.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { AggiungiPraticheDocumentiModalComponent } from '../aggiungi-pratiche-documenti-modal/aggiungi-pratiche-documenti-modal.component';
import { FileTypeUtils } from 'src/app/shared/utilities/file-type-utils';
import { ModaleParentComponent } from 'src/app/modali/modale-parent-component';
import { HelperService } from 'src/app/shared/services/helper.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-aggiungi-caricamento-pratica-modal',
  templateUrl: './aggiungi-caricamento-pratica-modal.component.html',
  styleUrls: ['./aggiungi-caricamento-pratica-modal.component.scss']
})
export class AggiungiCaricamentoPraticaModalComponent extends ModaleParentComponent implements OnInit {

  file: Partial<File> | null = null;
  documenti: Partial<File>[] = [];
  loading = 0;
  idUtente: number | null = null;
  idEnte: number | null = null;

  folderName: string | null = null;

  caricamentoPratica: CaricamentoPratica | null = null;
  statiCaricamento: StatoCaricamentoPratica[] = [];
  loadingError: any | null = null;

  errorSize = false;
  errorType = false;

  uploaded = false;

  maxSize = 0;
  codicePagina!: string;
  codiceTab!: string;
  codiceModale!: string;


  constructor(
    private caricamentoPraticheService: CaricamentoPraticheService,
    private modalService: ModalService,
    private translateService: TranslateService,
    private securityService: SecurityService,
    private ngbModal: NgbModal,
    public modal: NgbActiveModal,
    public helperService: HelperService,
    public route: ActivatedRoute,
  ) {
    super(helperService);
  }

  close(){
    if (this.file){
      this.modalService.confermaRifiuta(this.translateService.instant('common.dati_non_salvati'), this.translateService.instant('caricamento_pratiche.caricamento_non_completato'))
      .then(() => this.modal.dismiss('click_on_back_arrow')).catch();
    }else{
      this.modal.dismiss('click_on_back_arrow');
    }

  }

  ngOnInit(): void {

    this.loading++;
    this.loadingError = null;

    forkJoin({
        file: this.folderName ? this.caricamentoPraticheService.getFilePratiche(this.folderName) : of(null),
        user: this.securityService.getCurrentUser(),
        stati: this.caricamentoPraticheService.getStatiCaricamento()
      })
      .subscribe(
        response => {
          if (response.file !== null){
            const excel = response.file.find(elem => elem?.mimeType && FileTypeUtils.isFoglioDiCalcolo(elem?.mimeType));
            this.file = { name: excel?.nomeFile, size: excel?.dimensione };
            this.documenti = response.file.filter(elem => elem?.mimeType && FileTypeUtils.isArchivioZip(elem?.mimeType))
            .map(zip => ({name: zip?.nomeFile, size: zip?.dimensione}));
          }
          this.statiCaricamento = response.stati ?? [];
          this.idEnte = response.user?.ente?.id ?? 0;
          this.loading--;
        },
        error => {
          this.loading--;
          this.loadingError = error.error.title;
        }
    );
    this.setModalName(this.codiceModale);
    this.searchHelperModale(this.codicePagina, this.codiceTab);
  }

  refresh(){
    this.ngOnInit();
  }

  uploadFile(files: FileList) {

    if (files && files.item(0)) {
      const file = files.item(0);
      this.errorSize = false;
      this.errorType = false;
      if (file) {
        this.file = file;
        if (file.size > (this.maxSize * 1024 * 1024)){
          this.errorSize = true;
        }
        if (file.type !== 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'){
          this.errorType = true;
        }
      }
    }
  }

  nop() {
    // NOP
  }

  pulisciCampi(){
    this.errorSize = false;
    this.errorType = false;
    this.file = null;
  }


  apriModale(){
    const data = this.helperService.searchHelperRef(this.route);
    const modalRef = this.ngbModal.open(AggiungiPraticheDocumentiModalComponent, {backdrop: 'static', keyboard: false, size: 'lg'});
    modalRef.componentInstance.pratica = !this.file;
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = 'caricamento-file-caricamento-pratiche';
    if (this.file && this.file.name){
      modalRef.componentInstance.folderName = this.folderName;
      modalRef.componentInstance.documentiInseriti = this.documenti.map(x => x?.name ?? '');
    }
    modalRef.result.then((result: {file: Partial<File>, pathfile?: string}) =>  {
      if (!this.file){
        this.file = result.file;
        this.folderName = result.pathfile ?? '';
        this.caricamentoInBozza();
      }else{
        this.documenti.push(result.file);
      }
    }).catch();
  }

  caricamentoInBozza(){
    if (this.file && this.file.name && this.idEnte && this.folderName){

    const bozza = this.statiCaricamento.find(elem => elem.codice === 'CARICAMENTO_IN_BOZZA');

    const req: CaricamentoPraticaRequest = {
      nomeFile: this.file?.name,
      pathFile: this.folderName,
      statoCaricamentoPratica: bozza?.codice,
      idEnte: this.idEnte
    };
    this.caricamentoPraticheService.startUploading(req).subscribe(
      response => this.caricamentoPratica = response,
      error => this.modalService.error(this.translateService.instant('caricamento_pratiche.titolo'),
        this.translateService.instant('caricamento_pratiche.errore_caricamento_in_bozza'), error.error.errore).finally().catch()

    );

    }
  }

  completaCaricamento(){
    if (this.file && this.file.name && this.caricamentoPratica && this.caricamentoPratica.id && this.idEnte){
      const elem: CaricamentoPraticaRequest = {statoCaricamentoPratica: 'IN_ATTESA_DI_ELABORAZIONE'};
      this.caricamentoPraticheService.completeUploading(this.caricamentoPratica.id?.toString(), elem).subscribe(
        response => this.modalService.info(this.translateService.instant('caricamento_pratiche.titolo'),
          this.translateService.instant('caricamento_pratiche.caricamento_completato'))
          .finally(() => {this.file = null; this.modal.close(); }).catch(),
        error => this.modalService.error(this.translateService.instant('caricamento_pratiche.titolo'),
          this.translateService.instant('caricamento_pratiche.errore_completa_caricamento'), error.error.errore).finally().catch()

      );
    }
  }

}
