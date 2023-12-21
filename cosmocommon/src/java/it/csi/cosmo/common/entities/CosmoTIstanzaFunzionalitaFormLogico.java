/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_istanza_funzionalita_form_logico database table.
 *
 */
@Entity
@Table(name="cosmo_t_istanza_funzionalita_form_logico")
@NamedQuery(name="CosmoTIstanzaFunzionalitaFormLogico.findAll", query="SELECT c FROM CosmoTIstanzaFunzionalitaFormLogico c")
public class CosmoTIstanzaFunzionalitaFormLogico extends CosmoTEntity  implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(generator = "COSMO_T_ISTANZA_FUNZIONALITA_FORM_LOGICO_ID_GENERATOR")
  @SequenceGenerator(name = "COSMO_T_ISTANZA_FUNZIONALITA_FORM_LOGICO_ID_GENERATOR",
  sequenceName = "COSMO_T_ISTANZA_FUNZIONALITA_FORM_LOGICO_ID_SEQ", allocationSize = 1)
  private Long id;

  private String descrizione;


  //bi-directional many-to-one association to CosmoRFormLogicoIstanzaFunzionalita
  @OneToMany(mappedBy="cosmoTIstanzaFunzionalitaFormLogico")
  private List<CosmoRFormLogicoIstanzaFunzionalita> cosmoRFormLogicoIstanzaFunzionalitas;

  //bi-directional many-to-one association to CosmoRIstanzaFormLogicoParametroValore
  @OneToMany(mappedBy="cosmoTIstanzaFunzionalitaFormLogico")
  private List<CosmoRIstanzaFormLogicoParametroValore> cosmoRIstanzaFormLogicoParametroValores;

  //bi-directional many-to-one association to CosmoDFunzionalitaFormLogico
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="codice_funzionalita")
  private CosmoDFunzionalitaFormLogico cosmoDFunzionalitaFormLogico;

  public CosmoTIstanzaFunzionalitaFormLogico() {
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }
  public List<CosmoRFormLogicoIstanzaFunzionalita> getCosmoRFormLogicoIstanzaFunzionalitas() {
    return this.cosmoRFormLogicoIstanzaFunzionalitas;
  }

  public void setCosmoRFormLogicoIstanzaFunzionalitas(List<CosmoRFormLogicoIstanzaFunzionalita> cosmoRFormLogicoIstanzaFunzionalitas) {
    this.cosmoRFormLogicoIstanzaFunzionalitas = cosmoRFormLogicoIstanzaFunzionalitas;
  }

  public CosmoRFormLogicoIstanzaFunzionalita addCosmoRFormLogicoIstanzaFunzionalita(CosmoRFormLogicoIstanzaFunzionalita cosmoRFormLogicoIstanzaFunzionalita) {
    getCosmoRFormLogicoIstanzaFunzionalitas().add(cosmoRFormLogicoIstanzaFunzionalita);
    cosmoRFormLogicoIstanzaFunzionalita.setCosmoTIstanzaFunzionalitaFormLogico(this);

    return cosmoRFormLogicoIstanzaFunzionalita;
  }

  public CosmoRFormLogicoIstanzaFunzionalita removeCosmoRFormLogicoIstanzaFunzionalita(CosmoRFormLogicoIstanzaFunzionalita cosmoRFormLogicoIstanzaFunzionalita) {
    getCosmoRFormLogicoIstanzaFunzionalitas().remove(cosmoRFormLogicoIstanzaFunzionalita);
    cosmoRFormLogicoIstanzaFunzionalita.setCosmoTIstanzaFunzionalitaFormLogico(null);

    return cosmoRFormLogicoIstanzaFunzionalita;
  }

  public List<CosmoRIstanzaFormLogicoParametroValore> getCosmoRIstanzaFormLogicoParametroValores() {
    return this.cosmoRIstanzaFormLogicoParametroValores;
  }

  public void setCosmoRIstanzaFormLogicoParametroValores(List<CosmoRIstanzaFormLogicoParametroValore> cosmoRIstanzaFormLogicoParametroValores) {
    this.cosmoRIstanzaFormLogicoParametroValores = cosmoRIstanzaFormLogicoParametroValores;
  }

  public CosmoRIstanzaFormLogicoParametroValore addCosmoRIstanzaFormLogicoParametroValore(CosmoRIstanzaFormLogicoParametroValore cosmoRIstanzaFormLogicoParametroValore) {
    getCosmoRIstanzaFormLogicoParametroValores().add(cosmoRIstanzaFormLogicoParametroValore);
    cosmoRIstanzaFormLogicoParametroValore.setCosmoTIstanzaFunzionalitaFormLogico(this);

    return cosmoRIstanzaFormLogicoParametroValore;
  }

  public CosmoRIstanzaFormLogicoParametroValore removeCosmoRIstanzaFormLogicoParametroValore(CosmoRIstanzaFormLogicoParametroValore cosmoRIstanzaFormLogicoParametroValore) {
    getCosmoRIstanzaFormLogicoParametroValores().remove(cosmoRIstanzaFormLogicoParametroValore);
    cosmoRIstanzaFormLogicoParametroValore.setCosmoTIstanzaFunzionalitaFormLogico(null);

    return cosmoRIstanzaFormLogicoParametroValore;
  }

  public CosmoDFunzionalitaFormLogico getCosmoDFunzionalitaFormLogico() {
    return this.cosmoDFunzionalitaFormLogico;
  }

  public void setCosmoDFunzionalitaFormLogico(CosmoDFunzionalitaFormLogico cosmoDFunzionalitaFormLogico) {
    this.cosmoDFunzionalitaFormLogico = cosmoDFunzionalitaFormLogico;
  }

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

}
