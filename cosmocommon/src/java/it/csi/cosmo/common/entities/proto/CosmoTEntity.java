/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities.proto;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class CosmoTEntity
implements CampiTecniciEntity, Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "dt_inserimento", nullable = false)
  protected Timestamp dtInserimento;

  @Column(name = "utente_inserimento", nullable = false, length = 50)
  protected String utenteInserimento;

  @Column(name = "dt_ultima_modifica")
  protected Timestamp dtUltimaModifica;

  @Column(name = "utente_ultima_modifica", length = 50)
  protected String utenteUltimaModifica;

  @Column(name = "dt_cancellazione")
  protected Timestamp dtCancellazione;

  @Column(name = "utente_cancellazione", length = 50)
  protected String utenteCancellazione;

  @Override
  public Timestamp getDtInserimento() {
    return dtInserimento;
  }

  @Override
  public void setDtInserimento(Timestamp dtInserimento) {
    this.dtInserimento = dtInserimento;
  }

  @Override
  public String getUtenteInserimento() {
    return utenteInserimento;
  }

  @Override
  public void setUtenteInserimento(String utenteInserimento) {
    this.utenteInserimento = utenteInserimento;
  }

  @Override
  public Timestamp getDtUltimaModifica() {
    return dtUltimaModifica;
  }

  @Override
  public void setDtUltimaModifica(Timestamp dtUltimaModifica) {
    this.dtUltimaModifica = dtUltimaModifica;
  }

  @Override
  public String getUtenteUltimaModifica() {
    return utenteUltimaModifica;
  }

  @Override
  public void setUtenteUltimaModifica(String utenteUltimaModifica) {
    this.utenteUltimaModifica = utenteUltimaModifica;
  }

  @Override
  public Timestamp getDtCancellazione() {
    return dtCancellazione;
  }

  @Override
  public void setDtCancellazione(Timestamp dtCancellazione) {
    this.dtCancellazione = dtCancellazione;
  }

  @Override
  public String getUtenteCancellazione() {
    return utenteCancellazione;
  }

  @Override
  public void setUtenteCancellazione(String utenteCancellazione) {
    this.utenteCancellazione = utenteCancellazione;
  }

}
