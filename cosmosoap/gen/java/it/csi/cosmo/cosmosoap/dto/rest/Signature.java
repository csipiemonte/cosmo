/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmosoap.dto.rest.Signature;
import java.time.OffsetDateTime;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Signature  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Boolean valida = null;
  private String certificateAuthority = null;
  private byte[] cert = null;
  private String codiceFiscale = null;
  private OffsetDateTime dataOra = null;
  private OffsetDateTime dataOraVerifica = null;
  private String dipartimento = null;
  private String dnQualifier = null;
  private String errorCode = null;
  private String fineValidita = null;
  private String firmatario = null;
  private String givenName = null;
  private String surname = null;
  private String inizioValidita = null;
  private String nominativoFirmatario = null;
  private Long numeroControfirme = null;
  private String organizzazione = null;
  private String paese = null;
  private String serialNumber = null;
  private Boolean timestamped = null;
  private List<Signature> signature = new ArrayList<>();
  private String tipoCertificato = null;
  private String tipoFirma = null;

  /**
   **/
  


  // nome originario nello yaml: valida 
  public Boolean isValida() {
    return valida;
  }
  public void setValida(Boolean valida) {
    this.valida = valida;
  }

  /**
   **/
  


  // nome originario nello yaml: certificateAuthority 
  public String getCertificateAuthority() {
    return certificateAuthority;
  }
  public void setCertificateAuthority(String certificateAuthority) {
    this.certificateAuthority = certificateAuthority;
  }

  /**
   **/
  


  // nome originario nello yaml: cert 
  @Pattern(regexp="^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$")
  public byte[] getCert() {
    return cert;
  }
  public void setCert(byte[] cert) {
    this.cert = cert;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceFiscale 
  public String getCodiceFiscale() {
    return codiceFiscale;
  }
  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }

  /**
   **/
  


  // nome originario nello yaml: dataOra 
  public OffsetDateTime getDataOra() {
    return dataOra;
  }
  public void setDataOra(OffsetDateTime dataOra) {
    this.dataOra = dataOra;
  }

  /**
   **/
  


  // nome originario nello yaml: dataOraVerifica 
  public OffsetDateTime getDataOraVerifica() {
    return dataOraVerifica;
  }
  public void setDataOraVerifica(OffsetDateTime dataOraVerifica) {
    this.dataOraVerifica = dataOraVerifica;
  }

  /**
   **/
  


  // nome originario nello yaml: dipartimento 
  public String getDipartimento() {
    return dipartimento;
  }
  public void setDipartimento(String dipartimento) {
    this.dipartimento = dipartimento;
  }

  /**
   **/
  


  // nome originario nello yaml: dnQualifier 
  public String getDnQualifier() {
    return dnQualifier;
  }
  public void setDnQualifier(String dnQualifier) {
    this.dnQualifier = dnQualifier;
  }

  /**
   **/
  


  // nome originario nello yaml: errorCode 
  public String getErrorCode() {
    return errorCode;
  }
  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  /**
   **/
  


  // nome originario nello yaml: fineValidita 
  public String getFineValidita() {
    return fineValidita;
  }
  public void setFineValidita(String fineValidita) {
    this.fineValidita = fineValidita;
  }

  /**
   **/
  


  // nome originario nello yaml: firmatario 
  public String getFirmatario() {
    return firmatario;
  }
  public void setFirmatario(String firmatario) {
    this.firmatario = firmatario;
  }

  /**
   **/
  


  // nome originario nello yaml: givenName 
  public String getGivenName() {
    return givenName;
  }
  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  /**
   **/
  


  // nome originario nello yaml: surname 
  public String getSurname() {
    return surname;
  }
  public void setSurname(String surname) {
    this.surname = surname;
  }

  /**
   **/
  


  // nome originario nello yaml: inizioValidita 
  public String getInizioValidita() {
    return inizioValidita;
  }
  public void setInizioValidita(String inizioValidita) {
    this.inizioValidita = inizioValidita;
  }

  /**
   **/
  


  // nome originario nello yaml: nominativoFirmatario 
  public String getNominativoFirmatario() {
    return nominativoFirmatario;
  }
  public void setNominativoFirmatario(String nominativoFirmatario) {
    this.nominativoFirmatario = nominativoFirmatario;
  }

  /**
   **/
  


  // nome originario nello yaml: numeroControfirme 
  public Long getNumeroControfirme() {
    return numeroControfirme;
  }
  public void setNumeroControfirme(Long numeroControfirme) {
    this.numeroControfirme = numeroControfirme;
  }

  /**
   **/
  


  // nome originario nello yaml: organizzazione 
  public String getOrganizzazione() {
    return organizzazione;
  }
  public void setOrganizzazione(String organizzazione) {
    this.organizzazione = organizzazione;
  }

  /**
   **/
  


  // nome originario nello yaml: paese 
  public String getPaese() {
    return paese;
  }
  public void setPaese(String paese) {
    this.paese = paese;
  }

  /**
   **/
  


  // nome originario nello yaml: serialNumber 
  public String getSerialNumber() {
    return serialNumber;
  }
  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  /**
   **/
  


  // nome originario nello yaml: timestamped 
  public Boolean isTimestamped() {
    return timestamped;
  }
  public void setTimestamped(Boolean timestamped) {
    this.timestamped = timestamped;
  }

  /**
   **/
  


  // nome originario nello yaml: signature 
  public List<Signature> getSignature() {
    return signature;
  }
  public void setSignature(List<Signature> signature) {
    this.signature = signature;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoCertificato 
  public String getTipoCertificato() {
    return tipoCertificato;
  }
  public void setTipoCertificato(String tipoCertificato) {
    this.tipoCertificato = tipoCertificato;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoFirma 
  public String getTipoFirma() {
    return tipoFirma;
  }
  public void setTipoFirma(String tipoFirma) {
    this.tipoFirma = tipoFirma;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Signature signature = (Signature) o;
    return Objects.equals(valida, signature.valida) &&
        Objects.equals(certificateAuthority, signature.certificateAuthority) &&
        Objects.equals(cert, signature.cert) &&
        Objects.equals(codiceFiscale, signature.codiceFiscale) &&
        Objects.equals(dataOra, signature.dataOra) &&
        Objects.equals(dataOraVerifica, signature.dataOraVerifica) &&
        Objects.equals(dipartimento, signature.dipartimento) &&
        Objects.equals(dnQualifier, signature.dnQualifier) &&
        Objects.equals(errorCode, signature.errorCode) &&
        Objects.equals(fineValidita, signature.fineValidita) &&
        Objects.equals(firmatario, signature.firmatario) &&
        Objects.equals(givenName, signature.givenName) &&
        Objects.equals(surname, signature.surname) &&
        Objects.equals(inizioValidita, signature.inizioValidita) &&
        Objects.equals(nominativoFirmatario, signature.nominativoFirmatario) &&
        Objects.equals(numeroControfirme, signature.numeroControfirme) &&
        Objects.equals(organizzazione, signature.organizzazione) &&
        Objects.equals(paese, signature.paese) &&
        Objects.equals(serialNumber, signature.serialNumber) &&
        Objects.equals(timestamped, signature.timestamped) &&
        Objects.equals(signature, signature.signature) &&
        Objects.equals(tipoCertificato, signature.tipoCertificato) &&
        Objects.equals(tipoFirma, signature.tipoFirma);
  }

  @Override
  public int hashCode() {
    return Objects.hash(valida, certificateAuthority, cert, codiceFiscale, dataOra, dataOraVerifica, dipartimento, dnQualifier, errorCode, fineValidita, firmatario, givenName, surname, inizioValidita, nominativoFirmatario, numeroControfirme, organizzazione, paese, serialNumber, timestamped, signature, tipoCertificato, tipoFirma);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Signature {\n");
    
    sb.append("    valida: ").append(toIndentedString(valida)).append("\n");
    sb.append("    certificateAuthority: ").append(toIndentedString(certificateAuthority)).append("\n");
    sb.append("    cert: ").append(toIndentedString(cert)).append("\n");
    sb.append("    codiceFiscale: ").append(toIndentedString(codiceFiscale)).append("\n");
    sb.append("    dataOra: ").append(toIndentedString(dataOra)).append("\n");
    sb.append("    dataOraVerifica: ").append(toIndentedString(dataOraVerifica)).append("\n");
    sb.append("    dipartimento: ").append(toIndentedString(dipartimento)).append("\n");
    sb.append("    dnQualifier: ").append(toIndentedString(dnQualifier)).append("\n");
    sb.append("    errorCode: ").append(toIndentedString(errorCode)).append("\n");
    sb.append("    fineValidita: ").append(toIndentedString(fineValidita)).append("\n");
    sb.append("    firmatario: ").append(toIndentedString(firmatario)).append("\n");
    sb.append("    givenName: ").append(toIndentedString(givenName)).append("\n");
    sb.append("    surname: ").append(toIndentedString(surname)).append("\n");
    sb.append("    inizioValidita: ").append(toIndentedString(inizioValidita)).append("\n");
    sb.append("    nominativoFirmatario: ").append(toIndentedString(nominativoFirmatario)).append("\n");
    sb.append("    numeroControfirme: ").append(toIndentedString(numeroControfirme)).append("\n");
    sb.append("    organizzazione: ").append(toIndentedString(organizzazione)).append("\n");
    sb.append("    paese: ").append(toIndentedString(paese)).append("\n");
    sb.append("    serialNumber: ").append(toIndentedString(serialNumber)).append("\n");
    sb.append("    timestamped: ").append(toIndentedString(timestamped)).append("\n");
    sb.append("    signature: ").append(toIndentedString(signature)).append("\n");
    sb.append("    tipoCertificato: ").append(toIndentedString(tipoCertificato)).append("\n");
    sb.append("    tipoFirma: ").append(toIndentedString(tipoFirma)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

