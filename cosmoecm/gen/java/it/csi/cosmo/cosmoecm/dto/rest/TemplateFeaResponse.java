/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.PageInfo;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateFea;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class TemplateFeaResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<TemplateFea> templateFea = new ArrayList<>();
  private PageInfo pageInfo = null;

  /**
   **/
  


  // nome originario nello yaml: templateFea 
  public List<TemplateFea> getTemplateFea() {
    return templateFea;
  }
  public void setTemplateFea(List<TemplateFea> templateFea) {
    this.templateFea = templateFea;
  }

  /**
   **/
  


  // nome originario nello yaml: pageInfo 
  public PageInfo getPageInfo() {
    return pageInfo;
  }
  public void setPageInfo(PageInfo pageInfo) {
    this.pageInfo = pageInfo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TemplateFeaResponse templateFeaResponse = (TemplateFeaResponse) o;
    return Objects.equals(templateFea, templateFeaResponse.templateFea) &&
        Objects.equals(pageInfo, templateFeaResponse.pageInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(templateFea, pageInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TemplateFeaResponse {\n");
    
    sb.append("    templateFea: ").append(toIndentedString(templateFea)).append("\n");
    sb.append("    pageInfo: ").append(toIndentedString(pageInfo)).append("\n");
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

