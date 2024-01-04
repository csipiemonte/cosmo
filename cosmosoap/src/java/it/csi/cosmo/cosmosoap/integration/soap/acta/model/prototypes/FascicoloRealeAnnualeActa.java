/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes;

import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaModel;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaProperty;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.FascicoloRealeAnnualeActaDefaultImpl;


/**
 *
 */
@ActaModel(className = "FascicoloRealeAnnualePropertiesType")
public abstract class FascicoloRealeAnnualeActa extends FascicoloActa {

  @ActaProperty(propertyName = "anno")
  protected Integer anno;

  @ActaProperty(propertyName = "numeroInterno")
  protected String numeroInterno;

  public static FascicoloRealeAnnualeActaDefaultImpl.Builder builder() {
    return FascicoloRealeAnnualeActaDefaultImpl.builder();
  }

  public Integer getAnno() {
    return anno;
  }

  public void setAnno(Integer anno) {
    this.anno = anno;
  }

  public String getNumeroInterno() {
    return numeroInterno;
  }

  public void setNumeroInterno(String numeroInterno) {
    this.numeroInterno = numeroInterno;
  }

}
