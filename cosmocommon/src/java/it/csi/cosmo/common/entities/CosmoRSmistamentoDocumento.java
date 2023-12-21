/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.util.List;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.enums.InfoSmistamento;
import it.csi.cosmo.common.entities.proto.CosmoREntity;


/**
 * The persistent class for the cosmo_r_smistamento_documento database table.
 *
 */
@Entity
@Table(name="cosmo_r_smistamento_documento")
@NamedQuery(name="CosmoRSmistamentoDocumento.findAll", query="SELECT c FROM CosmoRSmistamentoDocumento c")
public class CosmoRSmistamentoDocumento extends CosmoREntity {
  private static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "COSMO_R_SMISTAMENTO_DOCUMENTO_ID_GENERATOR",
  sequenceName = "COSMO_R_SMISTAMENTO_DOCUMENTO_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
  generator = "COSMO_R_SMISTAMENTO_DOCUMENTO_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name="codice_esito_smistamento")
  private String codiceEsitoSmistamento;
  @Column(name="message_uuid")
  private String messageUuid;

  @Column(name="messaggio_esito_smistamento")
  private String messaggioEsitoSmistamento;

  //bi-directional many-to-one association to CosmoDStatoSmistamento
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="codice_stato_smistamento")
  private CosmoDStatoSmistamento cosmoDStatoSmistamento;

  //bi-directional many-to-one association to CosmoTSmistamento
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="id_smistamento")
  private CosmoTSmistamento cosmoTSmistamento;

  //bi-directional many-to-one association to CosmoTInfoAggiuntiveSmistamento
  @OneToMany(mappedBy = "cosmoRSmistamentoDocumento", fetch = FetchType.LAZY)
  private List<CosmoTInfoAggiuntiveSmistamento> cosmoTInfoAggiuntiveSmistamentos;

  // bi-directional many-to-one association to CosmoTDocumento
  @ManyToOne
  @JoinColumn(name = "id_documento")
  private CosmoTDocumento cosmoTDocumento;

  @Column(name = "numero_retry")
  private Integer numeroRetry;

  public CosmoRSmistamentoDocumento() {
    // empty constructor
  }

  public Optional<String> getInfoAggiuntiva(InfoSmistamento chiave) {
    if (cosmoTInfoAggiuntiveSmistamentos == null) {
      return Optional.empty();
    }
    return cosmoTInfoAggiuntiveSmistamentos.stream()
        .filter(i -> chiave.getCodice()
            .equalsIgnoreCase(i.getCodInformazione()))
        .findFirst().map(CosmoTInfoAggiuntiveSmistamento::getValore);
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCodiceEsitoSmistamento() {
    return this.codiceEsitoSmistamento;
  }

  public void setCodiceEsitoSmistamento(String codiceEsitoSmistamento) {
    this.codiceEsitoSmistamento = codiceEsitoSmistamento;
  }

  public String getMessageUuid() {
    return this.messageUuid;
  }

  public void setMessageUuid(String messageUuid) {
    this.messageUuid = messageUuid;
  }

  public String getMessaggioEsitoSmistamento() {
    return this.messaggioEsitoSmistamento;
  }

  public void setMessaggioEsitoSmistamento(String messaggioEsitoSmistamento) {
    this.messaggioEsitoSmistamento = messaggioEsitoSmistamento;
  }

  public CosmoDStatoSmistamento getCosmoDStatoSmistamento() {
    return this.cosmoDStatoSmistamento;
  }

  public void setCosmoDStatoSmistamento(CosmoDStatoSmistamento cosmoDStatoSmistamento) {
    this.cosmoDStatoSmistamento = cosmoDStatoSmistamento;
  }

  public CosmoTSmistamento getCosmoTSmistamento() {
    return this.cosmoTSmistamento;
  }

  public void setCosmoTSmistamento(CosmoTSmistamento cosmoTSmistamento) {
    this.cosmoTSmistamento = cosmoTSmistamento;
  }

  public List<CosmoTInfoAggiuntiveSmistamento> getCosmoTInfoAggiuntiveSmistamentos() {
    return this.cosmoTInfoAggiuntiveSmistamentos;
  }

  public void setCosmoTInfoAggiuntiveSmistamentos(List<CosmoTInfoAggiuntiveSmistamento> cosmoTInfoAggiuntiveSmistamentos) {
    this.cosmoTInfoAggiuntiveSmistamentos = cosmoTInfoAggiuntiveSmistamentos;
  }

  public CosmoTDocumento getCosmoTDocumento() {
    return this.cosmoTDocumento;
  }

  public void setCosmoTDocumento(CosmoTDocumento cosmoTDocumento) {
    this.cosmoTDocumento = cosmoTDocumento;
  }

  public Integer getNumeroRetry() {
    return this.numeroRetry;
  }

  public void setNumeroRetry(Integer numeroRetry) {
    this.numeroRetry = numeroRetry;
  }

  public CosmoTInfoAggiuntiveSmistamento addCosmoTInfoAggiuntiveSmistamento(CosmoTInfoAggiuntiveSmistamento cosmoTInfoAggiuntiveSmistamento) {
    getCosmoTInfoAggiuntiveSmistamentos().add(cosmoTInfoAggiuntiveSmistamento);
    cosmoTInfoAggiuntiveSmistamento.setCosmoRSmistamentoDocumento(this);

    return cosmoTInfoAggiuntiveSmistamento;
  }

  public CosmoTInfoAggiuntiveSmistamento removeCosmoTInfoAggiuntiveSmistamento(CosmoTInfoAggiuntiveSmistamento cosmoTInfoAggiuntiveSmistamento) {
    getCosmoTInfoAggiuntiveSmistamentos().remove(cosmoTInfoAggiuntiveSmistamento);
    cosmoTInfoAggiuntiveSmistamento.setCosmoRSmistamentoDocumento(null);

    return cosmoTInfoAggiuntiveSmistamento;
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
    CosmoRSmistamentoDocumento other = (CosmoRSmistamentoDocumento) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoRSmistamentoDocumento [id=" + id + ", codiceEsitoSmistamento="
        + codiceEsitoSmistamento + ", messageUuid=" + messageUuid + ", messaggioEsitoSmistamento="
        + messaggioEsitoSmistamento 
        + ", numeroRetry=" + numeroRetry + "]";
  }

}
