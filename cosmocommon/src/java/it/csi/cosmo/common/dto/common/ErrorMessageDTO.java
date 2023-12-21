/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.dto.common;

import java.io.Serializable;

/**
 * Classe per la costruzione del messaggio di errore da inviare in risposta ad una chiamata
 */

public class ErrorMessageDTO implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -577491364804404819L;

  private String code;

  private Integer status;

  private String title;

  private String errore;

  private ErrorMessageDTO(Builder builder) {
    this.code = builder.codice;
    this.status = builder.status;
    this.title = builder.messaggio;
    this.errore = builder.errore;
  }

  public ErrorMessageDTO() {
    // empty
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getErrore() {
    return errore;
  }

  public void setErrore(String errore) {
    this.errore = errore;
  }

  /**
   * Creates builder to build {@link ErrorMessageDTO}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link ErrorMessageDTO}.
   */
  public static final class Builder {

    private String codice;

    private Integer status;

    private String messaggio;

    private String errore;

    private Builder() {}

    public Builder withCodice(String codice) {
      this.codice = codice;
      return this;
    }

    public Builder withStatus(Integer status) {
      this.status = status;
      return this;
    }

    public Builder withMessaggio(String messaggio) {
      this.messaggio = messaggio;
      return this;
    }

    public Builder withErrore(String errore) {
      this.errore = errore;
      return this;
    }

    public ErrorMessageDTO build() {
      return new ErrorMessageDTO(this);
    }
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }


  @Override
  public String toString() {
    return "ErrorMessageDTO [" + (code != null ? "code=" + code + ", " : "")
        + (status != null ? "status=" + status + ", " : "")
        + (title != null ? "title=" + title + ", " : "")
        + (errore != null ? "errore=" + errore : "") + "]";
  }

}
