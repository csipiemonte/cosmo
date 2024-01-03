/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { finalize } from 'rxjs/operators';
import { Pratica } from '../../models/api/cosmobusiness/pratica';
import { PraticheService } from '../../services/pratiche.service';

@Component({
  selector: 'app-dettaglio-praticaworkflow',
  templateUrl: './dettaglio-praticaworkflow.component.html',
  styleUrls: ['./dettaglio-praticaworkflow.component.scss']
})
export class DettaglioPraticaworkflowComponent implements OnInit {

  @Input() pratica!: Pratica;

  isImgLoaded = false;
  autoZoom = false;

  loading = 0;
  image: any | null = null;

  constructor(
    private logger: NGXLogger,
    private praticheService: PraticheService
  ) { }

  ngOnInit(): void {
    if (!this.pratica.linkPratica || !this.pratica.id) {
      this.logger.warn('nessun id pratica');
      return;
    }

    this.loading++;

    this.praticheService.getDiagram(this.pratica.id)
      .pipe(
        finalize(() => {
          this.loading--;
        })
      )
      .subscribe((data) => {
        if (data) {
          this.blobToBase64(data)
            .then(res => this.image = res);
        }
      });
  }

  hasLoaded(): void {
    this.isImgLoaded = true;
  }

  blobToBase64(blob: any) {
    return new Promise((resolve, _) => {
      const reader = new FileReader();
      reader.onloadend = () => resolve(reader.result);
      reader.readAsDataURL(blob);
    });
  }
}


