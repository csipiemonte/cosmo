/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import it.csi.cosmo.common.async.model.LongTask.LongTaskStatus;
import it.csi.cosmo.common.async.model.LongTaskPersistableEntry;
import it.csi.cosmo.common.entities.converter.LongTaskConverter;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;


/**
 * The persistent class for the cosmo_t_operazione_asincrona database table.
 *
 */
@Entity
@Table(name = "cosmo_t_operazione_asincrona")
@NamedQuery(name = "CosmoTOperazioneAsincrona.findAll",
    query = "SELECT c FROM CosmoTOperazioneAsincrona c")
public class CosmoTOperazioneAsincrona extends CosmoTEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "COSMO_T_OPERAZIONE_ASINCRONA_ID_GENERATOR",
      sequenceName = "COSMO_T_OPERAZIONE_ASINCRONA_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "COSMO_T_OPERAZIONE_ASINCRONA_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name = "uuid", nullable = false, length = 50)
  private String uuid;

  @Enumerated(EnumType.STRING)
  @Column(name = "codice_stato", nullable = false, length = 50)
  private LongTaskStatus stato;

  @Column(name = "data_avvio", nullable = true)
  private Timestamp dataAvvio;

  @Column(name = "data_fine", nullable = true)
  private Timestamp dataFine;

  @JsonIgnore
  @Column(name = "metadati", nullable = true, columnDefinition = "json")
  @Convert(converter = LongTaskConverter.class)
  private LongTaskPersistableEntry metadati;

  @Column(name = "versione", nullable = false)
  private Long versione;

  public CosmoTOperazioneAsincrona() {
    // empty constructor
  }

  public Long getVersione() {
    return versione;
  }

  public void setVersione(Long versione) {
    this.versione = versione;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public LongTaskStatus getStato() {
    return stato;
  }

  public void setStato(LongTaskStatus stato) {
    this.stato = stato;
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

  public LongTaskPersistableEntry getMetadati() {
    return metadati;
  }

  public void setMetadati(LongTaskPersistableEntry metadati) {
    this.metadati = metadati;
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
    CosmoTOperazioneAsincrona other = (CosmoTOperazioneAsincrona) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoTOperazioneAsincrona [id=" + id + ", uuid=" + uuid + ", stato=" + stato
        + ", dataAvvio=" + dataAvvio + ", dataFine=" + dataFine + ", versione=" + versione + "]";
  }

}
