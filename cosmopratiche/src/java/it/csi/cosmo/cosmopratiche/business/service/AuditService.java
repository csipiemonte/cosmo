/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service;

public interface AuditService {

  void prePersist(Object target);

  void preUpdate(Object target);

  void preRemove(Object target);

}
