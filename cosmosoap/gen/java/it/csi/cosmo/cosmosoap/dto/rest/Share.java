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

public class Share  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String entry = null;
  private String contentHash = null;
  private String source = null;
  private String contentPropertyPrefixedName = null;
  private String resultPropertyPrefixedName = null;
  private String contentDisposition = null;
  private OffsetDateTime fromDate = null;
  private OffsetDateTime toDate = null;

  /**
   **/
  


  // nome originario nello yaml: entry 
  public String getEntry() {
    return entry;
  }
  public void setEntry(String entry) {
    this.entry = entry;
  }

  /**
   **/
  


  // nome originario nello yaml: contentHash 
  public String getContentHash() {
    return contentHash;
  }
  public void setContentHash(String contentHash) {
    this.contentHash = contentHash;
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
  


  // nome originario nello yaml: contentPropertyPrefixedName 
  public String getContentPropertyPrefixedName() {
    return contentPropertyPrefixedName;
  }
  public void setContentPropertyPrefixedName(String contentPropertyPrefixedName) {
    this.contentPropertyPrefixedName = contentPropertyPrefixedName;
  }

  /**
   **/
  


  // nome originario nello yaml: resultPropertyPrefixedName 
  public String getResultPropertyPrefixedName() {
    return resultPropertyPrefixedName;
  }
  public void setResultPropertyPrefixedName(String resultPropertyPrefixedName) {
    this.resultPropertyPrefixedName = resultPropertyPrefixedName;
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
    Share share = (Share) o;
    return Objects.equals(entry, share.entry) &&
        Objects.equals(contentHash, share.contentHash) &&
        Objects.equals(source, share.source) &&
        Objects.equals(contentPropertyPrefixedName, share.contentPropertyPrefixedName) &&
        Objects.equals(resultPropertyPrefixedName, share.resultPropertyPrefixedName) &&
        Objects.equals(contentDisposition, share.contentDisposition) &&
        Objects.equals(fromDate, share.fromDate) &&
        Objects.equals(toDate, share.toDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entry, contentHash, source, contentPropertyPrefixedName, resultPropertyPrefixedName, contentDisposition, fromDate, toDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Share {\n");
    
    sb.append("    entry: ").append(toIndentedString(entry)).append("\n");
    sb.append("    contentHash: ").append(toIndentedString(contentHash)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    contentPropertyPrefixedName: ").append(toIndentedString(contentPropertyPrefixedName)).append("\n");
    sb.append("    resultPropertyPrefixedName: ").append(toIndentedString(resultPropertyPrefixedName)).append("\n");
    sb.append("    contentDisposition: ").append(toIndentedString(contentDisposition)).append("\n");
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

