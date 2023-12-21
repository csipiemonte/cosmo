/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;


/**
 * The persistent class for the cosmo_t_gruppo database table.
 *
 */
@Entity
@Table(name = "cosmo_t_lock")
@NamedQuery(name = "CosmoTLock.findAll", query = "SELECT c FROM CosmoTLock c")
public class CosmoTLock extends CosmoTEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "COSMO_T_LOCK_ID_GENERATOR", sequenceName = "COSMO_T_LOCK_ID_SEQ",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COSMO_T_LOCK_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name = "codice_risorsa", length = 255, nullable = false)
  private String codiceRisorsa;

  @Column(name = "codice_owner", length = 255, nullable = false)
  private String codiceOwner;

  @Column(name = "dt_scadenza", nullable = false)
  private Timestamp dtScadenza;

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the codiceRisorsa
   */
  public String getCodiceRisorsa() {
    return codiceRisorsa;
  }

  /**
   * @param codiceRisorsa the codiceRisorsa to set
   */
  public void setCodiceRisorsa(String codiceRisorsa) {
    this.codiceRisorsa = codiceRisorsa;
  }

  /**
   * @return the codiceOwner
   */
  public String getCodiceOwner() {
    return codiceOwner;
  }

  /**
   * @param codiceOwner the codiceOwner to set
   */
  public void setCodiceOwner(String codiceOwner) {
    this.codiceOwner = codiceOwner;
  }

  /**
   * @return the dtScadenza
   */
  public Timestamp getDtScadenza() {
    return dtScadenza;
  }

  /**
   * @param dtScadenza the dtScadenza to set
   */
  public void setDtScadenza(Timestamp dtScadenza) {
    this.dtScadenza = dtScadenza;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    CosmoTLock other = (CosmoTLock) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoTLock [id=" + id + ", codiceRisorsa=" + codiceRisorsa + ", codiceOwner="
        + codiceOwner + ", dtScadenza=" + dtScadenza + "]";
  }

}
