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
import it.csi.cosmo.common.entities.enums.TipoEventoStoricoPratica;
import it.csi.cosmo.common.entities.proto.CosmoLEntity;


/**
 * The persistent class for the cosmo_l_storico_attivita database table.
 *
 */
@Entity
@Table(name = "cosmo_l_storico_pratica")
@NamedQuery(name = "CosmoLStoricoPratica.findAll", query = "SELECT l FROM CosmoLStoricoPratica l")
public class CosmoLStoricoPratica extends CosmoLEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "COSMO_L_STORICO_PRATICA_ID_GENERATOR",
      sequenceName = "COSMO_L_STORICO_PRATICA_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "COSMO_L_STORICO_PRATICA_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "codice_tipo_evento", length = 255, nullable = false)
  private TipoEventoStoricoPratica codiceTipoEvento;

  @Column(name = "descrizione_evento", length = 1000, nullable = true)
  private String descrizioneEvento;

  // bi-directional many-to-one association to CosmoTEnte
  @Fetch(FetchMode.JOIN)
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_pratica", nullable = false)
  private CosmoTPratica pratica;

  // bi-directional many-to-one association to CosmoTEnte
  @Fetch(FetchMode.JOIN)
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_attivita", nullable = true)
  private CosmoTAttivita attivita;

  // bi-directional many-to-one association to CosmoTEnte
  @Fetch(FetchMode.JOIN)
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_utente", nullable = true)
  private CosmoTUtente utente;

  // bi-directional many-to-one association to CosmoTFruitore
  @Fetch(FetchMode.JOIN)
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_fruitore", nullable = true)
  private CosmoTFruitore fruitore;

  // bi-directional many-to-one association to CosmoTEnte
  @Fetch(FetchMode.JOIN)
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_utente_coinvolto", nullable = true)
  private CosmoTUtente utenteCoinvolto;

  // bi-directional many-to-one association to CosmoTEnte
  @Fetch(FetchMode.JOIN)
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_gruppo_coinvolto", nullable = true)
  private CosmoTGruppo gruppoCoinvolto;

  private transient Boolean utenteCorrente;

  private transient Boolean fruitoreCorrente;

  private CosmoLStoricoPratica(Builder builder) {
    this.dtEvento = builder.dtEvento;
    this.id = builder.id;
    this.codiceTipoEvento = builder.codiceTipoEvento;
    this.descrizioneEvento = builder.descrizioneEvento;
    this.pratica = builder.pratica;
    this.attivita = builder.attivita;
    this.utente = builder.utente;
    this.fruitore = builder.fruitore;
    this.utenteCoinvolto = builder.utenteCoinvolto;
    this.gruppoCoinvolto = builder.gruppoCoinvolto;
    this.utenteCorrente = builder.utenteCorrente;
    this.fruitoreCorrente = builder.fruitoreCorrente;
  }

  public CosmoLStoricoPratica() {
    // empty constructor
  }

  @Override
  public String toString() {
    return "CosmoLStoricoPratica [id=" + id + ", codiceTipoEvento=" + codiceTipoEvento
        + ", descrizioneEvento=" + descrizioneEvento + ", pratica=" + pratica + ", attivita="
        + attivita + ", utente=" + utente + ", fruitore=" + fruitore + ", utenteCoinvolto="
        + utenteCoinvolto + ", gruppoCoinvolto=" + gruppoCoinvolto + "]";
  }

  public CosmoTUtente getUtenteCoinvolto() {
    return utenteCoinvolto;
  }

  public void setUtenteCoinvolto(CosmoTUtente utenteCoinvolto) {
    this.utenteCoinvolto = utenteCoinvolto;
  }

  public CosmoTGruppo getGruppoCoinvolto() {
    return gruppoCoinvolto;
  }

  public void setGruppoCoinvolto(CosmoTGruppo gruppoCoinvolto) {
    this.gruppoCoinvolto = gruppoCoinvolto;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public TipoEventoStoricoPratica getCodiceTipoEvento() {
    return codiceTipoEvento;
  }

  public void setCodiceTipoEvento(TipoEventoStoricoPratica codiceTipoEvento) {
    this.codiceTipoEvento = codiceTipoEvento;
  }

  public String getDescrizioneEvento() {
    return descrizioneEvento;
  }

  public void setDescrizioneEvento(String descrizioneEvento) {
    this.descrizioneEvento = descrizioneEvento;
  }

  public CosmoTPratica getPratica() {
    return pratica;
  }

  public void setPratica(CosmoTPratica pratica) {
    this.pratica = pratica;
  }

  public CosmoTAttivita getAttivita() {
    return attivita;
  }

  public void setAttivita(CosmoTAttivita attivita) {
    this.attivita = attivita;
  }

  public CosmoTUtente getUtente() {
    return utente;
  }

  public void setUtente(CosmoTUtente utente) {
    this.utente = utente;
  }

  public CosmoTFruitore getFruitore() {
    return fruitore;
  }

  public void setFruitore(CosmoTFruitore fruitore) {
    this.fruitore = fruitore;
  }

  public Boolean detectUtenteCorrente() {
    return utenteCorrente;
  }

  public Boolean detectFruitoreCorrente() {
    return fruitoreCorrente;
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
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    CosmoLStoricoPratica other = (CosmoLStoricoPratica) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }

  /**
   * Creates builder to build {@link CosmoLStoricoPratica}.
   * 
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link CosmoLStoricoPratica}.
   */
  public static final class Builder {
    private Timestamp dtEvento;
    private Long id;
    private TipoEventoStoricoPratica codiceTipoEvento;
    private String descrizioneEvento;
    private CosmoTPratica pratica;
    private CosmoTAttivita attivita;
    private CosmoTUtente utente;
    private CosmoTFruitore fruitore;
    private CosmoTUtente utenteCoinvolto;
    private CosmoTGruppo gruppoCoinvolto;
    private Boolean utenteCorrente;
    private Boolean fruitoreCorrente;

    private Builder() {}

    public Builder withUtenteCorrente(Boolean utenteCorrente) {
      this.utenteCorrente = utenteCorrente;
      return this;
    }

    public Builder withFruitoreCorrente(Boolean fruitoreCorrente) {
      this.fruitoreCorrente = fruitoreCorrente;
      return this;
    }

    public Builder withUtenteCoinvolto(CosmoTUtente utenteCoinvolto) {
      this.utenteCoinvolto = utenteCoinvolto;
      return this;
    }
    
    public Builder withGruppoCoinvolto(CosmoTGruppo gruppoCoinvolto) {
      this.gruppoCoinvolto = gruppoCoinvolto;
      return this;
    }

    public Builder withDtEvento(Timestamp dtEvento) {
      this.dtEvento = dtEvento;
      return this;
    }

    public Builder withId(Long id) {
      this.id = id;
      return this;
    }

    public Builder withCodiceTipoEvento(TipoEventoStoricoPratica codiceTipoEvento) {
      this.codiceTipoEvento = codiceTipoEvento;
      return this;
    }

    public Builder withDescrizioneEvento(String descrizioneEvento) {
      this.descrizioneEvento = descrizioneEvento;
      return this;
    }

    public Builder withPratica(CosmoTPratica pratica) {
      this.pratica = pratica;
      return this;
    }

    public Builder withAttivita(CosmoTAttivita attivita) {
      this.attivita = attivita;
      return this;
    }

    public Builder withUtente(CosmoTUtente utente) {
      this.utente = utente;
      return this;
    }

    public Builder withFruitore(CosmoTFruitore fruitore) {
      this.fruitore = fruitore;
      return this;
    }

    public CosmoLStoricoPratica build() {
      return new CosmoLStoricoPratica(this);
    }
  }

}
