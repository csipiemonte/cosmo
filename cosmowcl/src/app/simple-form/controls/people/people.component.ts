/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { RicercaUtenteComponent, SelezioneUtenteGruppo } from 'src/app/shared/components/ricerca-utente/ricerca-utente.component';
import { SecurityService } from 'src/app/shared/services/security.service';
import { UtentiService } from 'src/app/shared/services/utenti.service';
import { ParentControl } from '../parent-control';

@Component({
  selector: 'app-simpleform-people',
  templateUrl: './people.component.html',
  styleUrls: ['./people.component.scss']
})
export class PeopleComponent extends ParentControl implements OnInit {

  @ViewChild('ricerca') ricerca: RicercaUtenteComponent | null = null;

  public selectedUser?: SelezioneUtenteGruppo = undefined;
  public allUsers: SelezioneUtenteGruppo[] = [];

  public isFieldRequired = false;

  constructor(private securityService: SecurityService,
              private utentiService: UtentiService) {
    super();
  }

  ngOnInit(): void {
    if (!this.formField.id || this.formField.id.trim().length === 0) {
      this.formField.id = this.FieldNames.PEOPLE + '-' + this.getUniqueId();
    }

    if (this.formField.id && this.controls) {
      const cfArray = this.controls.controls[this.formField.id].value as string[] ?? [];
      if (cfArray.length > 0) {
        this.getUtenti(cfArray);
      }
    }
  }

  private getUtenti(codiciFiscali: string[]) {

    this.securityService.getCurrentUser().subscribe(response => {
      if (response.ente?.id) {
        const utenteFilter = {
          filter: {
            codiceFiscale: {
              in: codiciFiscali
            },
            idEnte: {
              eq: response.ente?.id
            }
          }
        };

        this.utentiService.getUtenti(JSON.stringify(utenteFilter))
        .subscribe(result => {
          if (result && result.utenti) {
            result.utenti.forEach(utente => {
              this.allUsers.push({
                utente
              });
            });
          }
        });
      }

    });

  }

  focusOut(){
    this.isFieldRequired = this.checkRequire();
  }

  checkRequire(): boolean {
    if (this.allUsers.length === 0 && this.formField.required) {
      return true;
    }
    return false;
  }

  addUser() {
    if (this.selectedUser && !this.allUsers.find(user => user.utente?.codiceFiscale === this.selectedUser?.utente?.codiceFiscale)) {
      this.allUsers.push(this.selectedUser);
      if (this.formField.id && this.controls) {
        this.controls.patchValue({
          ['' + this.formField.id] : this.allUsers.map(user => user.utente?.codiceFiscale),
        });
      }
    }
    this.ricerca?.clear();
    this.isFieldRequired = this.checkRequire();
  }

  removeUser(userToRemove: SelezioneUtenteGruppo | undefined) {
    const valueToRemove = this.allUsers.find(user => user.utente?.codiceFiscale === userToRemove?.utente?.codiceFiscale);
    if (valueToRemove) {
      this.allUsers.splice(this.allUsers.indexOf(valueToRemove), 1);

      if (this.formField.id && this.controls) {
        this.controls.patchValue({
          ['' + this.formField.id] : this.allUsers.map(user => user.utente?.codiceFiscale),
        });
      }
    }
    this.isFieldRequired = this.checkRequire();
  }
}
