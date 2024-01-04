/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class ShareOptions  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String filename = null;
  private String contentDisposition = null;
  private String source = null;
  private OffsetDateTime fromDate = null;
  private OffsetDateTime toDate = null;

  /**
   **/
  


  // nome originario nello yaml: filename 
  public String getFilename() {
    return filename;
  }
  public void setFilename(String filename) {
    this.filename = filename;
  }

  /**
   **/
  


  // nome originario nello yaml: contentDisposition 
  public String getContentDisposition() {
    return contentDisposition;
  }
  public void setContentDisposition(String contentDisposition) {
    this.contentDisposition = contentDisposition;
  }

  /**
   **/
  


  // nome originario nello yaml: source 
  public String getSource() {
    return source;
  }
  public void setSource(String source) {
    this.source = source;
  }

  /**
   **/
  


  // nome originario nello yaml: fromDate 
  public OffsetDateTime getFromDate() {
    return fromDate;
  }
  public void setFromDate(OffsetDateTime fromDate) {
    this.fromDate = fromDate;
  }

  /**
   **/
  


  // nome originario nello yaml: toDate 
  public OffsetDateTime getToDate() {
    return toDate;
  }
  public void setToDate(OffsetDateTime toDate) {
    this.toDate = toDate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShareOptions shareOptions = (ShareOptions) o;
    return Objects.equals(filename, shareOptions.filename) &&
        Objects.equals(contentDisposition, shareOptions.contentDisposition) &&
        Objects.equals(source, shareOptions.source) &&
        Objects.equals(fromDate, shareOptions.fromDate) &&
        Objects.equals(toDate, shareOptions.toDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(filename, contentDisposition, source, fromDate, toDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShareOptions {\n");
    
    sb.append("    filename: ").append(toIndentedString(filename)).append("\n");
    sb.append("    contentDisposition: ").append(toIndentedString(contentDisposition)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    fromDate: ").append(toIndentedString(fromDate)).append("\n");
    sb.append("    toDate: ").append(toIndentedString(toDate)).append("\n");
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

