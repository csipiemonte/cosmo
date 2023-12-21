/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.dto.rest;

import java.io.Serializable;

public class ImpostazioniFirmaDTO  implements Serializable {

  private static final long serialVersionUID = 1L;

  private EnteCertificatoreDTO enteCertificatore;
  private TipoCredenzialiFirmaDTO tipoCredenzialiFirma;
  private TipoOTPDTO tipoOTP;
  private ProfiloFEQDTO profiloFEQ;
  private SceltaMarcaTemporaleDTO sceltaMarcaTemporale;

  public EnteCertificatoreDTO getEnteCertificatore() {
    return enteCertificatore;
  }

  public void setEnteCertificatore(EnteCertificatoreDTO enteCertificatore) {
    this.enteCertificatore = enteCertificatore;
  }

  public TipoCredenzialiFirmaDTO getTipoCredenzialiFirma() {
    return tipoCredenzialiFirma;
  }

  public void setTipoCredenzialiFirma(TipoCredenzialiFirmaDTO tipoCredenzialiFirma) {
    this.tipoCredenzialiFirma = tipoCredenzialiFirma;
  }

  public TipoOTPDTO getTipoOTP() {
    return tipoOTP;
  }

  public void setTipoOTP(TipoOTPDTO tipoOTP) {
    this.tipoOTP = tipoOTP;
  }

  public ProfiloFEQDTO getProfiloFEQ() {
    return profiloFEQ;
  }

  public void setProfiloFEQ(ProfiloFEQDTO profiloFEQ) {
    this.profiloFEQ = profiloFEQ;
  }

  public SceltaMarcaTemporaleDTO getSceltaMarcaTemporale() {
    return sceltaMarcaTemporale;
  }

  public void setSceltaMarcaTemporale(SceltaMarcaTemporaleDTO sceltaMarcaTemporale) {
    this.sceltaMarcaTemporale = sceltaMarcaTemporale;
  }

  @Override
  public String toString() {
    return "ImpostazioniFirmaDTO [enteCertificatore=" + enteCertificatore
        + ", tipoCredenzialiFirma=" + tipoCredenzialiFirma + ", tipoOTP=" + tipoOTP
        + ", profiloFEQ=" + profiloFEQ + ", sceltaMarcaTemporale=" + sceltaMarcaTemporale + "]";
  }


}

