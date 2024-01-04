/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.aspect;

import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexProperty;

/**
 *
 */

@IndexAspect(value = "cm:workingcopy")
public class WorkingCopyAspect {

  @IndexProperty(value = "cm:workingCopyOwner", required = false, readOnly = true)
  protected String workingCopyOwner;

  public WorkingCopyAspect() {
    // NOP
  }

  @JsonProperty("isWorkingCopy")
  public boolean isWorkingCopy() {
    return !StringUtils.isBlank(workingCopyOwner);
  }

  public String getWorkingCopyOwner() {
    return workingCopyOwner;
  }

  public void setWorkingCopyOwner(String workingCopyOwner) {
    this.workingCopyOwner = workingCopyOwner;
  }

  @Override
  public String toString() {
    return "WorkingCopyAspect [workingCopyOwner=" + workingCopyOwner + "]";
  }

}
