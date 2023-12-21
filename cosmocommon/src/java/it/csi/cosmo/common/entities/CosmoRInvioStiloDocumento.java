/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoREntity;


/**
 * The persistent class for the cosmo_r_invio_stilo_documento database table.
 *
 */
@Entity
@Table(name="cosmo_r_invio_stilo_documento")
@NamedQuery(name="CosmoRInvioStiloDocumento.findAll", query="SELECT c FROM CosmoRInvioStiloDocumento c")
public class CosmoRInvioStiloDocumento extends CosmoREntity implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private CosmoRInvioStiloDocumentoPK id;

  @Column(name="codice_esito_invio_stilo")
  private String codiceEsitoInvioStilo;

  @Column(name="messaggio_esito_invio_stilo")
  private String messaggioEsitoInvioStilo;

  @Column(name="numero_retry")
  private Integer numeroRetry;

  //bi-directional many-to-one association to CosmoDStatoInvioStilo
  @ManyToOne
  @JoinColumn(name="codice_stato_invio_stilo")
  private CosmoDStatoInvioStilo cosmoDStatoInvioStilo;

  //bi-directional many-to-one association to CosmoTDocumento
  @ManyToOne
  @JoinColumn(name = "id_documento", insertable = false, updatable = false)
  private CosmoTDocumento cosmoTDocumento;

  public CosmoRInvioStiloDocumento() {
  }

  public CosmoRInvioStiloDocumentoPK getId() {
    return this.id;
  }

  public void setId(CosmoRInvioStiloDocumentoPK id) {
    this.id = id;
  }

  public String getCodiceEsitoInvioStilo() {
    return this.codiceEsitoInvioStilo;
  }

  public void setCodiceEsitoInvioStilo(String codiceEsitoInvioStilo) {
    this.codiceEsitoInvioStilo = codiceEsitoInvioStilo;
  }

  public String getMessaggioEsitoInvioStilo() {
    return this.messaggioEsitoInvioStilo;
  }

  public void setMessaggioEsitoInvioStilo(String messaggioEsitoInvioStilo) {
    this.messaggioEsitoInvioStilo = messaggioEsitoInvioStilo;
  }

  public Integer getNumeroRetry() {
    return this.numeroRetry;
  }

  public void setNumeroRetry(Integer numeroRetry) {
    this.numeroRetry = numeroRetry;
  }

  public CosmoDStatoInvioStilo getCosmoDStatoInvioStilo() {
    return this.cosmoDStatoInvioStilo;
  }

  public void setCosmoDStatoInvioStilo(CosmoDStatoInvioStilo cosmoDStatoInvioStilo) {
    this.cosmoDStatoInvioStilo = cosmoDStatoInvioStilo;
  }

  public CosmoTDocumento getCosmoTDocumento() {
    return this.cosmoTDocumento;
  }

  public void setCosmoTDocumento(CosmoTDocumento cosmoTDocumento) {
    this.cosmoTDocumento = cosmoTDocumento;
  }


  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CosmoRInvioStiloDocumento other = (CosmoRInvioStiloDocumento) obj;
    return Objects.equals(id, other.id);
  }

  @Override
  public String toString() {
    return "CosmoRInvioStiloDocumento [id=" + id + ", codiceEsitoInvioStilo="
        + codiceEsitoInvioStilo + ", messaggioEsitoInvioStilo=" + messaggioEsitoInvioStilo + "]";
  }


}
