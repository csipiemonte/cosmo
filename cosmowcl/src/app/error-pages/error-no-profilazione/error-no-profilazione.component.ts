/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { SecurityService } from '../../shared/services/security.service';

@Component({
  selector: 'app-error-no-profilazione',
  templateUrl: './error-no-profilazione.component.html',
  styleUrls: ['./error-no-profilazione.component.css']
})
export class ErrorNoProfilazioneComponent implements OnInit {

  public currentUser!: UserInfoWrapper;

  constructor(private securityService: SecurityService) { }

  ngOnInit() {
    this.securityService.principal$.subscribe(
      (newUser: UserInfoWrapper) => {
        this.currentUser = newUser;
      }
    );
  }

}
