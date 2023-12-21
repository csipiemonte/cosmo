/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.test.cosmo.cosmobusiness.testbed.entities;

import java.util.Arrays;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.ColumnTransformer;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_ente database table.
 *
 */
@Entity
@Table(name = "cosmo_t_ente")
public class CosmoTEnteCustom extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_ENTE_ID_GENERATOR", sequenceName = "COSMO_T_ENTE_ID_SEQ",
  allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COSMO_T_ENTE_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name = "codice_ipa", length = 10, nullable = false)
  private String codiceIpa;

  @Column(name = "codice_fiscale", length = 16)
  private String codiceFiscale;

  // @Lob
  @Column(name = "logo")
  private byte[] logo;

  @ColumnTransformer(forColumn = "nome", read = "ALTER_CUSTOM(nome)")
  @Column(name = "nome", nullable = false, length = 255)
  private String nome;

  public CosmoTEnteCustom() {
    // empty constructor
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCodiceIpa() {
    return codiceIpa;
  }

  public void setCodiceIpa(String codiceIpa) {
    this.codiceIpa = codiceIpa;
  }

  public String getCodiceFiscale() {
    return codiceFiscale;
  }

  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }

  public byte[] getLogo() {
    return logo;
  }

  public void setLogo(byte[] logo) {
    this.logo = logo;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  @Override
  public String toString() {
    return "CosmoTEnteCustom [id=" + id + ", codiceIpa=" + codiceIpa + ", codiceFiscale="
        + codiceFiscale + ", logo=" + Arrays.toString(logo) + ", nome=" + nome + "]";
  }

}
