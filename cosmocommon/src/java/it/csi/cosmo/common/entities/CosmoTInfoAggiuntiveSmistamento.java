/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_info_aggiuntive_smistamento database table.
 *
 */
@Entity
@Table(name="cosmo_t_info_aggiuntive_smistamento")
@NamedQuery(name="CosmoTInfoAggiuntiveSmistamento.findAll", query="SELECT c FROM CosmoTInfoAggiuntiveSmistamento c")
public class CosmoTInfoAggiuntiveSmistamento extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "COSMO_T_INFO_AGGIUNTIVE_SMISTAMENTO_ID_GENERATOR",
  sequenceName = "COSMO_T_INFO_AGGIUNTIVE_SMISTAMENTO_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
  generator = "COSMO_T_INFO_AGGIUNTIVE_SMISTAMENTO_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name="cod_informazione")
  private String codInformazione;

  private String valore;

  //bi-directional many-to-one association to CosmoRSmistamentoDocumento
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="id_smistamento_documento")
  private CosmoRSmistamentoDocumento cosmoRSmistamentoDocumento;

  public CosmoTInfoAggiuntiveSmistamento() {
    // noop
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCodInformazione() {
    return this.codInformazione;
  }

  public void setCodInformazione(String codInformazione) {
    this.codInformazione = codInformazione;
  }

  public String getValore() {
    return this.valore;
  }

  public void setValore(String valore) {
    this.valore = valore;
  }

  public CosmoRSmistamentoDocumento getCosmoRSmistamentoDocumento() {
    return this.cosmoRSmistamentoDocumento;
  }

  public void setCosmoRSmistamentoDocumento(CosmoRSmistamentoDocumento cosmoRSmistamentoDocumento) {
    this.cosmoRSmistamentoDocumento = cosmoRSmistamentoDocumento;
  }

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

}
