/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.dto;

public class UtentiBatchError {

  private String codiceFiscaleUtente;
  private String errror;


  public String getCodiceFiscaleUtente() {
    return codiceFiscaleUtente;
  }
  public void setCodiceFiscaleUtente(String codiceFiscaleUtente) {
    this.codiceFiscaleUtente = codiceFiscaleUtente;
  }
  public String getErrror() {
    return errror;
  }
  public void setErrror(String errror) {
    this.errror = errror;
  }


}
