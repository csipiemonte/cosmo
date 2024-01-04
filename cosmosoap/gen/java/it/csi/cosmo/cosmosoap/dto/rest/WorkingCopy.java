/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class WorkingCopy  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String workingCopyOwner = null;

  /**
   **/
  


  // nome originario nello yaml: workingCopyOwner 
  public String getWorkingCopyOwner() {
    return workingCopyOwner;
  }
  public void setWorkingCopyOwner(String workingCopyOwner) {
    this.workingCopyOwner = workingCopyOwner;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WorkingCopy workingCopy = (WorkingCopy) o;
    return Objects.equals(workingCopyOwner, workingCopy.workingCopyOwner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(workingCopyOwner);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WorkingCopy {\n");
    
    sb.append("    workingCopyOwner: ").append(toIndentedString(workingCopyOwner)).append("\n");
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

