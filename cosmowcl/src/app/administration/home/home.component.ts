/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient } from '@angular/common/http';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { CaricamentoInCorsoIconPicker } from 'src/app/shared/components/caricamento-in-corso/caricamento-in-corso.icons';
import { OperazioneAsincrona } from 'src/app/shared/models/api/cosmobusiness/operazioneAsincrona';
import { AsyncTaskModalService } from 'src/app/shared/services/async-task-modal.service';
import { ApiUrls } from 'src/app/shared/utilities/apiurls';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {

  constructor(
    private http: HttpClient,
    private asyncTaskModalService: AsyncTaskModalService
  ) {
  }

  ngOnInit(): void {
  }

  ngOnDestroy() {
  }

  test(): void {
    this.http.get<OperazioneAsincrona>(ApiUrls.COSMO_BUSINESS + '/test').subscribe(op => {
      console.log('avviata operazione', op.uuid);
      this.asyncTaskModalService.open({
        taskUUID: op.uuid,
      })?.result.then(result => {
        console.log('MODAL RESULT', result);
      }, fail => {
        console.error('MODAL FAIL', fail);
      });
    });
  }

}
