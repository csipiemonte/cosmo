/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Preferenza } from '../api/cosmo/preferenza';
import { UserInfo } from '../api/cosmo/userInfo';
import { UserInfoEnte } from '../api/cosmo/userInfoEnte';
import { UserInfoGruppo } from '../api/cosmo/userInfoGruppo';
import { UserInfoProfilo } from '../api/cosmo/userInfoProfilo';
import { Profilazione } from './profilazione';

export interface UserInfoWrapper extends UserInfo {
  anonimo: boolean;
  nome: string;
  cognome: string;
  codiceFiscale: string;
  email?: string;
  telefono?: string;
  ente?: UserInfoEnte;
  profilo?: UserInfoProfilo;
  gruppi?: Array<UserInfoGruppo>;
  preferenze?: Array<Preferenza>;
  fineValidita?: string;
  hashIdentita?: string;
  profilazione: Profilazione;
}
