/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { Constants } from 'src/app/shared/constants/constants';
import { BusService } from 'src/app/shared/services/bus.service';
@Component({
  selector: 'app-tab-panel',
  templateUrl: './tab-panel.component.html',
  styleUrls: ['./tab-panel.component.scss']
})
export class TabPanelComponent implements OnInit {

  lastUpdate = new Date();
  refreshingPratiche = false;
  praticheConsts = Constants.PRATICHE;

  constructor(
    private busService: BusService
  ) {  }

  ngOnInit() {

  }
  RefreshDataSource() {
    this.refreshingPratiche = true;
    this.busService.setCercaPratiche(true);
    this.lastUpdate = new Date();
    this.refreshingPratiche = false;
  }


}
