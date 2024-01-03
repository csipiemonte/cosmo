/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

export enum EnviromentEnum {
  INTRANET = 'intranet',
  INTRACOM = 'intracom',
  INTERNET = 'internet',
  DEFAULT = 'default'
}

export enum EnvironmentRelatedFunction {
  INTRANET,
  LOGOUT,
  REQUEST_INTERCEPTION,
  BE_SERVER_CHECK
}
