/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class FileContent  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case]
  private static final long serialVersionUID = 1L;

  private String mimeType = null;
  private String fileName = null;
  private byte[] content = null;

  /**
   **/



  // nome originario nello yaml: mimeType
  @NotNull
  public String getMimeType() {
    return mimeType;
  }
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  /**
   **/



  // nome originario nello yaml: fileName
  @NotNull
  public String getFileName() {
    return fileName;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   **/



  // nome originario nello yaml: content
  @Pattern(regexp="^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$")
  public byte[] getContent() {
    return content;
  }
  public void setContent(byte[] content) {
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
    FileContent fileContent = (FileContent) o;
    return Objects.equals(mimeType, fileContent.mimeType) &&
        Objects.equals(fileName, fileContent.fileName) &&
        Objects.equals(content, fileContent.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mimeType, fileName, content);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FileContent {\n");

    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
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

