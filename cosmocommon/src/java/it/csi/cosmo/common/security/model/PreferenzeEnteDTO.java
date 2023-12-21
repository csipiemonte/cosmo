/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.security.model;

import java.io.Serializable;

public class PreferenzeEnteDTO implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1285182471L;

  private Long id;

  private String valore;

  private String versione;

  public PreferenzeEnteDTO() {
    // public
  }

  private PreferenzeEnteDTO(Builder builder) {
    this.id = builder.id;
    this.valore = builder.valore;
    this.versione = builder.versione;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getValore() {
    return valore;
  }

  public void setValore(String valore) {
    this.valore = valore;
  }

  public String getVersione() {
    return versione;
  }

  public void setVersione(String versione) {
    this.versione = versione;
  }

  @Override
  public String toString() {
    return "PreferenzeUtenteDTO [id=" + id + ", valore=" + valore + ", versione=" + versione + "]";
  }

  /**
   * Creates builder to build {@link PreferenzeEnteDTO}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link PreferenzeEnteDTO}.
   */
  public static final class Builder {
    private Long id;
    private String valore;
    private String versione;

    private Builder() {}

    public Builder withId(Long id) {
      this.id = id;
      return this;
    }

    public Builder withValore(String valore) {
      this.valore = valore;
      return this;
    }

    public Builder withVersione(String versione) {
      this.versione = versione;
      return this;
    }

    public PreferenzeEnteDTO build() {
      return new PreferenzeEnteDTO(this);
    }
  }

}
