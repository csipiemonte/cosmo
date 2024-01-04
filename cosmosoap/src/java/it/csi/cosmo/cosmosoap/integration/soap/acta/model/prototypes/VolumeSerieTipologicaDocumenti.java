/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes;


import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaModel;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.VolumeSerieTipologicaDocumentiDefaultImpl;


/**
 *
 */
@ActaModel(className = "VolumeSerieTipologicaDocumentiPropertiesType")
public abstract class VolumeSerieTipologicaDocumenti extends VolumeActa {

  public static VolumeSerieTipologicaDocumentiDefaultImpl.Builder builder() {
    return VolumeSerieTipologicaDocumentiDefaultImpl.builder();
  }

}
