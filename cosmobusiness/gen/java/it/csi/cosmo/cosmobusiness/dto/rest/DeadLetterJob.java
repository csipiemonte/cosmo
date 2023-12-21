/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class DeadLetterJob  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private String descrizioneEnte = null;
  private String tipoPratica = null;
  private String oggettoPratica = null;
  private OffsetDateTime data = null;
  private Long tentativi = null;
  private String info = null;
  private String nomeJob = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  @NotNull
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizioneEnte 
  public String getDescrizioneEnte() {
    return descrizioneEnte;
  }
  public void setDescrizioneEnte(String descrizioneEnte) {
    this.descrizioneEnte = descrizioneEnte;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoPratica 
  @NotNull
  public String getTipoPratica() {
    return tipoPratica;
  }
  public void setTipoPratica(String tipoPratica) {
    this.tipoPratica = tipoPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: oggettoPratica 
  @NotNull
  public String getOggettoPratica() {
    return oggettoPratica;
  }
  public void setOggettoPratica(String oggettoPratica) {
    this.oggettoPratica = oggettoPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: data 
  @NotNull
  public OffsetDateTime getData() {
    return data;
  }
  public void setData(OffsetDateTime data) {
    this.data = data;
  }

  /**
   **/
  


  // nome originario nello yaml: tentativi 
  public Long getTentativi() {
    return tentativi;
  }
  public void setTentativi(Long tentativi) {
    this.tentativi = tentativi;
  }

  /**
   **/
  


  // nome originario nello yaml: info 
  @NotNull
  public String getInfo() {
    return info;
  }
  public void setInfo(String info) {
    this.info = info;
  }

  /**
   **/
  


  // nome originario nello yaml: nomeJob 
  public String getNomeJob() {
    return nomeJob;
  }
  public void setNomeJob(String nomeJob) {
    this.nomeJob = nomeJob;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeadLetterJob deadLetterJob = (DeadLetterJob) o;
    return Objects.equals(id, deadLetterJob.id) &&
        Objects.equals(descrizioneEnte, deadLetterJob.descrizioneEnte) &&
        Objects.equals(tipoPratica, deadLetterJob.tipoPratica) &&
        Objects.equals(oggettoPratica, deadLetterJob.oggettoPratica) &&
        Objects.equals(data, deadLetterJob.data) &&
        Objects.equals(tentativi, deadLetterJob.tentativi) &&
        Objects.equals(info, deadLetterJob.info) &&
        Objects.equals(nomeJob, deadLetterJob.nomeJob);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, descrizioneEnte, tipoPratica, oggettoPratica, data, tentativi, info, nomeJob);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeadLetterJob {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    descrizioneEnte: ").append(toIndentedString(descrizioneEnte)).append("\n");
    sb.append("    tipoPratica: ").append(toIndentedString(tipoPratica)).append("\n");
    sb.append("    oggettoPratica: ").append(toIndentedString(oggettoPratica)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    tentativi: ").append(toIndentedString(tentativi)).append("\n");
    sb.append("    info: ").append(toIndentedString(info)).append("\n");
    sb.append("    nomeJob: ").append(toIndentedString(nomeJob)).append("\n");
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

