/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Commento } from '../../models/api/cosmobusiness/commento';
import { UserInfoWrapper } from '../../models/user/user-info';
import { SecurityService } from '../../services/security.service';

@Component({
  selector: 'app-commento',
  templateUrl: './commento.component.html',
  styleUrls: ['./commento.component.scss']
})
export class CommentoComponent implements OnInit {

  @Input() commento: Commento | undefined;
  @Input() canDelete: ((c: Commento) => boolean) | undefined;
  @Output() delete = new EventEmitter<Commento>();
  @Input() readOnly = false;

  currentUser: UserInfoWrapper | undefined;

  constructor(private securityService: SecurityService) { }

  ngOnInit(): void {
    this.securityService.getCurrentUser().subscribe(user => this.currentUser = user);
  }

  isOwn() {
    return !!this.commento &&
      this.currentUser &&
      this.currentUser.codiceFiscale &&
      this.currentUser.codiceFiscale === this.commento.cfAutore;
  }

  getInitials(c: Commento) {
    let out = '';
    if (c.nomeAutore) {
      out += c.nomeAutore.charAt(0).toUpperCase();
    }
    if (c.cognomeAutore) {
      out += c.cognomeAutore.charAt(0).toUpperCase();
    }
    if (!out) {
      out = '??';
    }
    return out;
  }
}
