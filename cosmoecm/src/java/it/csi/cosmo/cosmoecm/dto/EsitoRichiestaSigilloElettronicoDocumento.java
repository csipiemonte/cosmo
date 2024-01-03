/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto;

import java.io.Serializable;

/**
 *
 */

public class EsitoRichiestaSigilloElettronicoDocumento implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private String codice;

  private String messaggio;

  private String messageUUID;

  public EsitoRichiestaSigilloElettronicoDocumento(String codice, String messaggio) {
    super();
    this.codice = codice;
    this.messaggio = messaggio;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((messageUUID == null) ? 0 : messageUUID.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    EsitoRichiestaSigilloElettronicoDocumento other = (EsitoRichiestaSigilloElettronicoDocumento) obj;
    if (messageUUID == null) {
      if (other.messageUUID != null)
        return false;
    } else if (!messageUUID.equals(other.messageUUID)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "EsitoSmistamentoDocumento [codice=" + codice + ", messaggio=" + messaggio
        + ", messageUUID=" + messageUUID + "]";
  }

  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public String getMessaggio() {
    return messaggio;
  }

  public void setMessaggio(String messaggio) {
    this.messaggio = messaggio;
  }

  public String getMessageUUID() {
    return messageUUID;
  }

  public void setMessageUUID(String messageUUID) {
    this.messageUUID = messageUUID;
  }

}
