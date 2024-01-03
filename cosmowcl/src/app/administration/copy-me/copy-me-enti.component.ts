/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, OnDestroy } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { forkJoin, of } from 'rxjs';
import { delay, finalize } from 'rxjs/operators';
import { environment } from 'src/environments/environment';


@Component({
  selector: 'app-admin-copy-me',
  templateUrl: './copy-me.component.html',
  styleUrls: ['./copy-me.component.scss']
})
export class CopyMeComponent implements OnInit, OnDestroy {
  COMPONENT_NAME = 'CopyMeComponent';

  loading = 0;
  loadingError: any | null = null;
  loaded = false;

  constructor(
    private logger: NGXLogger,
  ) {
    this.logger.debug('creating component ' + this.COMPONENT_NAME);
  }

  ngOnInit(): void {
    this.logger.debug('initializing component ' + this.COMPONENT_NAME);
    this.refresh();
  }

  ngOnDestroy(): void {
    this.logger.debug('destroying component ' + this.COMPONENT_NAME);
  }
  
  refresh() {
    this.loading ++;
    this.loadingError = null;
    this.loaded = false;

    forkJoin({
      // TODO customize with data retrieval
      somekey: of(1),
      someOtherKey: of(2)
    })
      .pipe(
        delay(environment.httpMockDelay),
        finalize(() => {
          this.loading--;
        })
      )
      .subscribe(response => {
        this.logger.debug('loaded data', response);
        // TODO use your data!

        this.loaded = true;
      }, failure => {
        this.loadingError = failure;
      });
  }

}
