/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.util.List;
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
import it.csi.cosmo.common.entities.enums.TipoContenutoDocumento;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_documento database table.
 *
 */
@Entity
@Table(name = "cosmo_t_documento")
@NamedQuery(name = "CosmoTDocumento.findAll", query = "SELECT c FROM CosmoTDocumento c")
public class CosmoTDocumento extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_DOCUMENTO_ID_GENERATOR",
  sequenceName = "COSMO_T_DOCUMENTO_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COSMO_T_DOCUMENTO_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(length = 100)
  private String autore;

  private String descrizione;

  @Column(name = "id_doc_parent_ext", length = 50)
  private String idDocParentExt;

  @Column(name = "id_documento_ext", length = 255)
  private String idDocumentoExt;

  @Column(length = 255)
  private String titolo;

  @Column(name = "numero_tentativi_acquisizione", nullable = true)
  private Integer numeroTentativiAcquisizione;

  // bi-directional many-to-one association to CosmoDStatoDocumento
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_stato_documento")
  private CosmoDStatoDocumento stato;

  // bi-directional many-to-one association to CosmoTContenutoDocumento
  @OneToMany(mappedBy = "documentoPadre")
  private List<CosmoTContenutoDocumento> contenuti;

  // bi-directional many-to-one association to CosmoDTipoDocumento
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_tipo_documento")
  private CosmoDTipoDocumento tipo;

  // bi-directional many-to-one association to CosmoTDocumento
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private CosmoTDocumento documentoPadre;

  // bi-directional many-to-one association to CosmoTDocumento
  @OneToMany(mappedBy = "documentoPadre")
  private List<CosmoTDocumento> documentiFigli;

  // bi-directional many-to-one association to CosmoTPratica
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_pratica")
  private CosmoTPratica pratica;

  // bi-directional many-to-one association to CosmoRSmistamentoDocumento
  @OneToMany(mappedBy = "cosmoTDocumento", fetch = FetchType.LAZY)
  private List<CosmoRSmistamentoDocumento> cosmoRSmistamentoDocumentos;

  // bi-directional many-to-one association to CosmoRInvioStiloDocumento
  @OneToMany(mappedBy = "cosmoTDocumento")
  private List<CosmoRInvioStiloDocumento> cosmoRInvioStiloDocumentos;

  // bi-directional many-to-one association to CosmoTApprovazione
  @OneToMany(mappedBy = "cosmoTDocumento")
  private List<CosmoTApprovazione> cosmoTApprovaziones;

  // bi-directional many-to-one association to CosmoRSmistamentoDocumento
  @OneToMany(mappedBy = "cosmoTDocumento", fetch = FetchType.LAZY)
  private List<CosmoRSigilloDocumento> cosmoRSigilloDocumentos;

  public CosmoTDocumento() {
    // empty constructor
  }

  public List<CosmoRInvioStiloDocumento> getCosmoRInvioStiloDocumentos() {
    return this.cosmoRInvioStiloDocumentos;
  }

  public void setCosmoRInvioStiloDocumentos(
      List<CosmoRInvioStiloDocumento> cosmoRInvioStiloDocumentos) {
    this.cosmoRInvioStiloDocumentos = cosmoRInvioStiloDocumentos;
  }

  public CosmoRInvioStiloDocumento addCosmoRInvioStiloDocumento(
      CosmoRInvioStiloDocumento cosmoRInvioStiloDocumento) {
    getCosmoRInvioStiloDocumentos().add(cosmoRInvioStiloDocumento);
    cosmoRInvioStiloDocumento.setCosmoTDocumento(this);

    return cosmoRInvioStiloDocumento;
  }

  public CosmoRInvioStiloDocumento removeCosmoRInvioStiloDocumento(
      CosmoRInvioStiloDocumento cosmoRInvioStiloDocumento) {
    getCosmoRInvioStiloDocumentos().remove(cosmoRInvioStiloDocumento);
    cosmoRInvioStiloDocumento.setCosmoTDocumento(null);

    return cosmoRInvioStiloDocumento;
  }

  public boolean hasContenuto(TipoContenutoDocumento tipo) {
    return this.contenuti != null && !this.contenuti.isEmpty()
        && this.contenuti.stream()
        .anyMatch(c -> tipo.name().equals(c.getTipo().getCodice()) && !c.cancellato());
  }

  public CosmoTContenutoDocumento findContenuto(TipoContenutoDocumento tipo) {
    if (this.contenuti == null) {
      return null;
    }
    return this.contenuti.stream()
        .filter(c -> tipo.name().equals(c.getTipo().getCodice()) && !c.cancellato())
        .findFirst().orElse(null);
  }

  public Integer getNumeroTentativiAcquisizione() {
    return numeroTentativiAcquisizione;
  }

  public void setNumeroTentativiAcquisizione(Integer numeroTentativiAcquisizione) {
    this.numeroTentativiAcquisizione = numeroTentativiAcquisizione;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAutore() {
    return this.autore;
  }

  public void setAutore(String autore) {
    this.autore = autore;
  }

  public String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public String getIdDocParentExt() {
    return this.idDocParentExt;
  }

  public void setIdDocParentExt(String idDocParentExt) {
    this.idDocParentExt = idDocParentExt;
  }

  public String getIdDocumentoExt() {
    return this.idDocumentoExt;
  }

  public void setIdDocumentoExt(String idDocumentoExt) {
    this.idDocumentoExt = idDocumentoExt;
  }

  public String getTitolo() {
    return this.titolo;
  }

  public void setTitolo(String titolo) {
    this.titolo = titolo;
  }


  public CosmoDStatoDocumento getStato() {
    return stato;
  }

  public void setStato(CosmoDStatoDocumento stato) {
    this.stato = stato;
  }

  public List<CosmoTContenutoDocumento> getContenuti() {
    return contenuti;
  }

  public void setContenuti(List<CosmoTContenutoDocumento> contenuti) {
    this.contenuti = contenuti;
  }

  public CosmoDTipoDocumento getTipo() {
    return tipo;
  }

  public void setTipo(CosmoDTipoDocumento tipo) {
    this.tipo = tipo;
  }

  public CosmoTDocumento getDocumentoPadre() {
    return documentoPadre;
  }

  public void setDocumentoPadre(CosmoTDocumento documentoPadre) {
    this.documentoPadre = documentoPadre;
  }

  public List<CosmoTDocumento> getDocumentiFigli() {
    return documentiFigli;
  }

  public void setDocumentiFigli(List<CosmoTDocumento> documentiFigli) {
    this.documentiFigli = documentiFigli;
  }

  public CosmoTPratica getPratica() {
    return pratica;
  }

  public void setPratica(CosmoTPratica pratica) {
    this.pratica = pratica;
  }

  public List<CosmoRSmistamentoDocumento> getCosmoRSmistamentoDocumentos() {
    return this.cosmoRSmistamentoDocumentos;
  }

  public void setCosmoRSmistamentoDocumentos(
      List<CosmoRSmistamentoDocumento> cosmoRSmistamentoDocumentos) {
    this.cosmoRSmistamentoDocumentos = cosmoRSmistamentoDocumentos;
  }

  public List<CosmoTApprovazione> getCosmoTApprovaziones() {
    return this.cosmoTApprovaziones;
  }

  public void setCosmoTApprovaziones(List<CosmoTApprovazione> cosmoTApprovaziones) {
    this.cosmoTApprovaziones = cosmoTApprovaziones;
  }

  public CosmoTApprovazione addCosmoTApprovazione(CosmoTApprovazione cosmoTApprovazione) {
    getCosmoTApprovaziones().add(cosmoTApprovazione);
    cosmoTApprovazione.setCosmoTDocumento(this);

    return cosmoTApprovazione;
  }

  public CosmoTApprovazione removeCosmoTApprovazione(CosmoTApprovazione cosmoTApprovazione) {
    getCosmoTApprovaziones().remove(cosmoTApprovazione);
    cosmoTApprovazione.setCosmoTDocumento(null);

    return cosmoTApprovazione;
  }


  public List<CosmoRSigilloDocumento> getCosmoRSigilloDocumentos() {
    return this.cosmoRSigilloDocumentos;
  }

  public void setCosmoRSigilloDocumentos(List<CosmoRSigilloDocumento> cosmoRSigilloDocumentos) {
    this.cosmoRSigilloDocumentos = cosmoRSigilloDocumentos;
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
    CosmoTDocumento other = (CosmoTDocumento) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CosmoTDocumento [" + (id != null ? "id=" + id + ", " : "")
        + (autore != null ? "autore=" + autore + ", " : "")
        + (descrizione != null ? "descrizione=" + descrizione + ", " : "")
        + (idDocParentExt != null ? "idDocParentExt=" + idDocParentExt + ", " : "")
        + (idDocumentoExt != null ? "idDocumentoExt=" + idDocumentoExt + ", " : "")
        + (titolo != null ? "titolo=" + titolo + ", " : "");
  }

}
