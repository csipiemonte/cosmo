/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 */

public class DoSignMassivePayloadDTO implements Serializable {
  /**
  *
  */
  private static final long serialVersionUID = 1L;

  private List<DosignPayloadDTO> documentiDaFirmare;

  private Integer idAttivita;

  private Integer idPratica;

  private Integer idFunzionalita;

  private DoSignMassivePayloadDTO(Builder builder) {
    this.setDocumentiDaFirmare(builder.documentiDaFirmare);
    this.idAttivita = builder.idAttivita;
    this.idPratica = builder.idPratica;
  }

  /**
   * Creates builder to build {@link DosignMassivePayloadDTO}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link DosignMassivePayloadDTO}.
   */
  public static final class Builder {

    private List<DosignPayloadDTO> documentiDaFirmare;

    private Integer idPratica;

    private Integer idAttivita;

    private Builder() {}

    public Builder withDocumentiDaFirmare(List<DosignPayloadDTO> listaDocDaFirmare) {
      this.documentiDaFirmare = listaDocDaFirmare;
      return this;
    }

    public Builder withIdPratica(Integer idPratica) {
      this.idPratica = idPratica;
      return this;
    }

    public Builder withIdAttivita(Integer idAttivita) {
      this.idAttivita = idAttivita;
      return this;
    }


    public DoSignMassivePayloadDTO build() {
      return new DoSignMassivePayloadDTO(this);
    }

    public Integer getIdPratica() {
      return idPratica;
    }

    public void setIdPratica(Integer idPratica) {
      this.idPratica = idPratica;
    }

    public Integer getIdAttivita() {
      return idAttivita;
    }

    public void setIdAttivita(Integer idAttivita) {
      this.idAttivita = idAttivita;
    }

  }

  public List<DosignPayloadDTO> getDocumentiDaFirmare() {
    return documentiDaFirmare;
  }

  public void setDocumentiDaFirmare(List<DosignPayloadDTO> documentiDaFirmare2) {
    this.documentiDaFirmare = documentiDaFirmare2;
  }

  public Integer getIdAttivita() {
    return idAttivita;
  }

  public void setIdAttivita(Integer idAttivita) {
    this.idAttivita = idAttivita;
  }

  public Integer getIdPratica() {
    return idPratica;
  }

  public void setIdPratica(Integer idPratica) {
    this.idPratica = idPratica;
  }

  public Integer getIdFunzionalita() {
    return idFunzionalita;
  }

  public void setIdFunzionalita(Integer idFunzionalita) {
    this.idFunzionalita = idFunzionalita;
  }

}
