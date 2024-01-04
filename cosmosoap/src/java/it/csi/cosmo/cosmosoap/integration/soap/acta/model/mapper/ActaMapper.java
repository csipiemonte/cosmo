/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.mapper;

import it.csi.cosmo.cosmosoap.integration.soap.acta.model.ActaClientContext;


/**
 *
 */

public interface ActaMapper<LocalEntity, ActaEntity> {

  public ActaEntity map(LocalEntity input, ActaClientContext contesto);
}
