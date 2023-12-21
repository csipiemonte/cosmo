/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;


/**
 * The persistent class for the cosmo_d_chiave_parametro_funzionalita_form_logico database table.
 *
 */
@Entity
@Table(name = "cosmo_d_chiave_parametro_funzionalita_form_logico")
@NamedQuery(name = "CosmoDChiaveParametroFunzionalitaFormLogico.findAll",
    query = "SELECT c FROM CosmoDChiaveParametroFunzionalitaFormLogico c")
public class CosmoDChiaveParametroFunzionalitaFormLogico extends CosmoDEntity
    implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "codice", nullable = false, length = 100)
  private String codice;

  @Column(name = "descrizione", nullable = true, length = 255)
  private String descrizione;

  @Column(name = "tipo", nullable = true, length = 100)
  private String tipo;

  @Column(name = "schema_json", nullable = true)
  private String schema;

  @Column(name = "valore_default", nullable = true)
  private String valoreDefault;

  public CosmoDChiaveParametroFunzionalitaFormLogico() {
    // NOOP
  }

  public String getValoreDefault() {
    return valoreDefault;
  }

  public void setValoreDefault(String valoreDefault) {
    this.valoreDefault = valoreDefault;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getSchema() {
    return schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  public String getCodice() {
    return this.codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codice == null) ? 0 : codice.hashCode());
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
    CosmoDChiaveParametroFunzionalitaFormLogico other =
        (CosmoDChiaveParametroFunzionalitaFormLogico) obj;
    if (codice == null) {
      if (other.codice != null)
        return false;
    } else if (!codice.equals(other.codice))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoDChiaveParametroFunzionalitaFormLogico [codice=" + codice + ", descrizione="
        + descrizione + "]";
  }

}
