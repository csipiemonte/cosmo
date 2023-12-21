/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import it.csi.cosmo.common.batch.model.BatchReportedEventLevel;
import it.csi.cosmo.common.entities.proto.CosmoLEntity;


/**
 * The persistent class for the cosmo_l_segnalazione_batch database table.
 *
 */
@Entity
@Table(name = "cosmo_l_segnalazione_batch")
@NamedQuery(name = "CosmoLSegnalazioneBatch.findAll",
    query = "SELECT l FROM CosmoLSegnalazioneBatch l")
public class CosmoLSegnalazioneBatch extends CosmoLEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "COSMO_L_SEGNALAZIONE_BATCH_ID_GENERATOR",
      sequenceName = "COSMO_L_SEGNALAZIONE_BATCH_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "COSMO_L_SEGNALAZIONE_BATCH_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  // bi-directional many-to-one association to CosmoTEnte
  @Fetch(FetchMode.JOIN)
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_esecuzione", nullable = false)
  private CosmoLEsecuzioneBatch esecuzione;

  @Enumerated(EnumType.STRING)
  @Column(name = "livello", length = 255, nullable = false)
  private BatchReportedEventLevel livello;

  @Column(name = "messaggio", nullable = false)
  private String messaggio;

  @Column(name = "dettagli", nullable = true)
  private String dettagli;

  private CosmoLSegnalazioneBatch(Builder builder) {
    this.dtEvento = builder.dtEvento;
    this.id = builder.id;
    this.esecuzione = builder.esecuzione;
    this.livello = builder.livello;
    this.messaggio = builder.messaggio;
    this.dettagli = builder.dettagli;
  }

  public CosmoLSegnalazioneBatch() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CosmoLEsecuzioneBatch getEsecuzione() {
    return esecuzione;
  }

  public void setEsecuzione(CosmoLEsecuzioneBatch esecuzione) {
    this.esecuzione = esecuzione;
  }

  public BatchReportedEventLevel getLivello() {
    return livello;
  }

  public void setLivello(BatchReportedEventLevel livello) {
    this.livello = livello;
  }

  public String getMessaggio() {
    return messaggio;
  }

  public void setMessaggio(String messaggio) {
    this.messaggio = messaggio;
  }

  public String getDettagli() {
    return dettagli;
  }

  public void setDettagli(String dettagli) {
    this.dettagli = dettagli;
  }

  /**
   * Creates builder to build {@link CosmoLSegnalazioneBatch}.
   * 
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link CosmoLSegnalazioneBatch}.
   */
  public static final class Builder {
    private Timestamp dtEvento;
    private Long id;
    private CosmoLEsecuzioneBatch esecuzione;
    private BatchReportedEventLevel livello;
    private String messaggio;
    private String dettagli;

    private Builder() {}

    public Builder withDtEvento(Timestamp dtEvento) {
      this.dtEvento = dtEvento;
      return this;
    }

    public Builder withId(Long id) {
      this.id = id;
      return this;
    }

    public Builder withEsecuzione(CosmoLEsecuzioneBatch esecuzione) {
      this.esecuzione = esecuzione;
      return this;
    }

    public Builder withLivello(BatchReportedEventLevel livello) {
      this.livello = livello;
      return this;
    }

    public Builder withMessaggio(String messaggio) {
      this.messaggio = messaggio;
      return this;
    }

    public Builder withDettagli(String dettagli) {
      this.dettagli = dettagli;
      return this;
    }

    public CosmoLSegnalazioneBatch build() {
      return new CosmoLSegnalazioneBatch(this);
    }
  }

}
