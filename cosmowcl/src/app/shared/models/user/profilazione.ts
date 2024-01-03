/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
export interface Profilazione {
  hasValidRole: boolean;
  profili: {[key: string]: true};
  useCase: {[key: string]: true};
}
