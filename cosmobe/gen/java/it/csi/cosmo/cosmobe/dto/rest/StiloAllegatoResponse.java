/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class StiloAllegatoResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String contentId = null;
  private String contentType = null;
  private String fileName = null;
  private List<byte[]> content = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: contentId 
  public String getContentId() {
    return contentId;
  }
  public void setContentId(String contentId) {
    this.contentId = contentId;
  }

  /**
   **/
  


  // nome originario nello yaml: contentType 
  public String getContentType() {
    return contentType;
  }
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   **/
  


  // nome originario nello yaml: fileName 
  public String getFileName() {
    return fileName;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   **/
  


  // nome originario nello yaml: content 
  public List<byte[]> getContent() {
    return content;
  }
  public void setContent(List<byte[]> content) {
    this.content = content;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StiloAllegatoResponse stiloAllegatoResponse = (StiloAllegatoResponse) o;
    return Objects.equals(contentId, stiloAllegatoResponse.contentId) &&
        Objects.equals(contentType, stiloAllegatoResponse.contentType) &&
        Objects.equals(fileName, stiloAllegatoResponse.fileName) &&
        Objects.equals(content, stiloAllegatoResponse.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contentId, contentType, fileName, content);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StiloAllegatoResponse {\n");
    
    sb.append("    contentId: ").append(toIndentedString(contentId)).append("\n");
    sb.append("    contentType: ").append(toIndentedString(contentType)).append("\n");
    sb.append("    fileName: ").append(toIndentedString(fileName)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
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

