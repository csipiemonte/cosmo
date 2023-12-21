/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoLEntity;


/**
 * The persistent class for the cosmo_l_esecuzione_batch database table.
 *
 */
@Entity
@Table(name = "cosmo_l_esecuzione_batch")
@NamedQuery(name = "CosmoLEsecuzioneBatch.findAll", query = "SELECT l FROM CosmoLEsecuzioneBatch l")
public class CosmoLEsecuzioneBatch extends CosmoLEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "COSMO_L_ESECUZIONE_BATCH_ID_GENERATOR",
      sequenceName = "COSMO_L_ESECUZIONE_BATCH_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "COSMO_L_ESECUZIONE_BATCH_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name = "codice_batch", length = 255, nullable = false)
  private String codiceBatch;

  @Column(name = "dt_avvio", nullable = false)
  private Timestamp dataAvvio;

  @Column(name = "dt_fine", nullable = false)
  private Timestamp dataFine;

  @Column(name = "codice_esito", length = 255, nullable = false)
  private String codiceEsito;

  @OneToMany(mappedBy = "esecuzione", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<CosmoLSegnalazioneBatch> segnalazioni;

  private CosmoLEsecuzioneBatch(Builder builder) {
    this.dtEvento = builder.dtEvento;
    this.id = builder.id;
    this.codiceBatch = builder.codiceBatch;
    this.dataAvvio = builder.dataAvvio;
    this.dataFine = builder.dataFine;
    this.codiceEsito = builder.codiceEsito;
    this.segnalazioni = new ArrayList<>();
  }

  public CosmoLEsecuzioneBatch() {}

  public List<CosmoLSegnalazioneBatch> getSegnalazioni() {
    return segnalazioni;
  }

  public void setSegnalazioni(List<CosmoLSegnalazioneBatch> segnalazioni) {
    this.segnalazioni = segnalazioni;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCodiceBatch() {
    return codiceBatch;
  }

  public void setCodiceBatch(String codiceBatch) {
    this.codiceBatch = codiceBatch;
  }

  public Timestamp getDataAvvio() {
    return dataAvvio;
  }

  public void setDataAvvio(Timestamp dataAvvio) {
    this.dataAvvio = dataAvvio;
  }

  public Timestamp getDataFine() {
    return dataFine;
  }

  public void setDataFine(Timestamp dataFine) {
    this.dataFine = dataFine;
  }

  public String getCodiceEsito() {
    return codiceEsito;
  }

  public void setCodiceEsito(String codiceEsito) {
    this.codiceEsito = codiceEsito;
  }

  /**
   * Creates builder to build {@link CosmoLEsecuzioneBatch}.
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link CosmoLEsecuzioneBatch}.
   */
  public static final class Builder {
    private Timestamp dtEvento;
    private Long id;
    private String codiceBatch;
    private Timestamp dataAvvio;
    private Timestamp dataFine;
    private String codiceEsito;

    private Builder() {}

    public Builder withDtEvento(Timestamp dtEvento) {
      this.dtEvento = dtEvento;
      return this;
    }

    public Builder withId(Long id) {
      this.id = id;
      return this;
    }

    public Builder withCodiceBatch(String codiceBatch) {
      this.codiceBatch = codiceBatch;
      return this;
    }

    public Builder withDataAvvio(Timestamp dataAvvio) {
      this.dataAvvio = dataAvvio;
      return this;
    }

    public Builder withDataFine(Timestamp dataFine) {
      this.dataFine = dataFine;
      return this;
    }

    public Builder withCodiceEsito(String codiceEsito) {
      this.codiceEsito = codiceEsito;
      return this;
    }

    public CosmoLEsecuzioneBatch build() {
      return new CosmoLEsecuzioneBatch(this);
    }
  }


}
