/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmobusiness.dto.rest.DeadLetterJob;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class DeadLetterJobsResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<DeadLetterJob> deadLetterJobs = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: deadLetterJobs 
  public List<DeadLetterJob> getDeadLetterJobs() {
    return deadLetterJobs;
  }
  public void setDeadLetterJobs(List<DeadLetterJob> deadLetterJobs) {
    this.deadLetterJobs = deadLetterJobs;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeadLetterJobsResponse deadLetterJobsResponse = (DeadLetterJobsResponse) o;
    return Objects.equals(deadLetterJobs, deadLetterJobsResponse.deadLetterJobs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deadLetterJobs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeadLetterJobsResponse {\n");
    
    sb.append("    deadLetterJobs: ").append(toIndentedString(deadLetterJobs)).append("\n");
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

