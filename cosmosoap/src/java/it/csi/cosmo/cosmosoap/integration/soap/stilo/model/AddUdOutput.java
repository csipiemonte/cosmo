/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.stilo.model;

import it.eng.auriga.repository2.webservices.addunitadoc.dto.BaseOutputWS;
import it.eng.auriga.repository2.webservices.addunitadoc.dto.OutputUD;

/**
 *
 */

public class AddUdOutput {

  private OutputUD outputUD;
  private BaseOutputWS baseOutputWS;

  public OutputUD getOutputUD() {
    return outputUD;
  }

  public void setOutputUD(OutputUD outputUD) {
    this.outputUD = outputUD;
  }

  public BaseOutputWS getBaseOutputWS() {
    return baseOutputWS;
  }

  public void setBaseOutputWS(BaseOutputWS baseOutputWS) {
    this.baseOutputWS = baseOutputWS;
  }



}
